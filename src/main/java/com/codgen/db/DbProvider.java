package com.codgen.db;

import com.codgen.db.impl.DataTypeConverterForJava;
import com.codgen.db.impl.MysqlProvider;
import com.codgen.db.impl.OracleProvider;
import com.codgen.model.*;
import com.codgen.util.BuilderHelper;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库信息提供者
 */
public class DbProvider {
    protected JdbcConfig jdbcConfig;
    private Connection conn;

    /**
     * 根据JDBC配置模型构造一个数据库信息提供者
     *
     * @param jdbcConfig jdbc配置模型
     */
    public DbProvider(JdbcConfig jdbcConfig) {
        super();
        this.jdbcConfig = jdbcConfig;
    }

    /**
     * 获取数据库连接
     */
    protected Connection getConn() {
        if (conn == null && jdbcConfig != null) {
            String driver = jdbcConfig.getDriverClass();
            String url = jdbcConfig.getJdbcUrl();
            String user = jdbcConfig.getUser();
            String password = jdbcConfig.getPassword();
            try {
                Class.forName(driver).newInstance();
                conn = DriverManager.getConnection(url, user, password);
            } catch (Exception e) {
                throw new RuntimeException("初始化jdbc连接异常！");
            }
        }
        if (conn == null) {
            throw new RuntimeException("初始化jdbc连接失败！");
        }
        return conn;
    }

    public static void safelyClose(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void safelyClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取得所有表的表注释，不同的数据库方言有不同的实现方式。
     *
     * @return
     */
    public List<TableModel> getTableList() {
        if ("mysql".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            return MysqlProvider.getTableList(getConn(), jdbcConfig.getSchema());
        } else if ("oracle".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            return OracleProvider.getTableList(getConn(), jdbcConfig.getSchema());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 获取指定表所有列注释。返回的集合的元素格式为：列名(COLUMN_NAME)和注释(COMMENTS)。
     *
     * @return
     */
    public List<ColumnModel> getColumnList(String tableName) {
        if ("mysql".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            return MysqlProvider.getColumnList(getConn(), jdbcConfig.getSchema(), tableName);
        } else if ("oracle".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            return OracleProvider.getColumnList(getConn(), jdbcConfig.getSchema(), tableName);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 初始化表模型
     *
     * @param table 表名称
     * @return
     * @throws SQLException
     */
    public TableModel initTableModel(TableModel table, TableConfig tableConfig) {
        String tableName = table.getTableName();
        String tableLabel = BuilderHelper.getPartString(tableName, tableConfig.getPrefix());
        String tableComment = StringUtils.defaultString(table.getTabComment(), tableLabel);
        table.setTableLabel(tableLabel);
        table.setTabComment(tableComment);
        String validFlags = tableConfig.getValidFlag();
        ValidFlagModel validFlagModel = gainValidFlagModel(validFlags);
        Map<String, String> excludeFields = tableConfig.getExcludeFields();

        List<ColumnModel> columnList = getColumnList(tableName);
        List<ColumnModel> pkColumnList = new ArrayList<>();//主键列模型集合
        List<ColumnModel> teshuColList = new ArrayList<>();//特殊处理列模型集合
        List<ColumnModel> noteshuColList = new ArrayList<>();//无特殊处理列模型集合
        List<ValidFlagModel> validFlagList = new ArrayList<>();//逻辑删除模型
        for (ColumnModel column : columnList) {
            String columnLabel = BuilderHelper.getPartString(column.getColumnName());
            if(StringUtils.isEmpty(column.getColComment())){
                column.setColComment(columnLabel);
            }
            column.setColumnLabel(columnLabel);

            //如果没有设置任何的列模型处理器，则默认使用一个java数据类型转换器来处理列模型
            DataTypeConverterForJava.handle(column);
            if (column.isPrimaryKey()) {
                pkColumnList.add(column);
            }
            if (excludeFields.containsKey(column.getColumnName())) {
                teshuColList.add(column);
            } else {
                noteshuColList.add(column);
            }
            if (column.getColumnName().equals(validFlagModel.getColname())) {
                //validFlagModel.setColumnModel(column);
                validFlagList.add(validFlagModel);
            }
        }
        table.setColumnList(columnList);
        table.setPkColumnList(pkColumnList);
        table.setTeshuColList(teshuColList);
        table.setNoteshuColList(noteshuColList);
        table.setValidFlagList(validFlagList);
        System.out.println(table);
        return table;
    }

    /**
     * 判断指定列名是否为主键
     *
     * @param columnName 要判断的列名称
     * @return 如果指定列为主键则返回true，否则返回false
     */
    public boolean isPrimaryKey(List<PrimaryKeyModel> primaryKeyList, String columnName) {
        boolean result = false;
        for (PrimaryKeyModel pkModel : primaryKeyList) {
            if (pkModel.getColumnName().equalsIgnoreCase(columnName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public ValidFlagModel gainValidFlagModel(String s) {
        ValidFlagModel validFlagModel = new ValidFlagModel();
        validFlagModel.setColname(s.substring(0, s.indexOf("?")));
        validFlagModel.setValid(s.substring(s.indexOf("?") + 1, s.indexOf(":")));
        validFlagModel.setDisable(s.substring(s.indexOf(":") + 1, s.length()));
        return validFlagModel;
    }


}
