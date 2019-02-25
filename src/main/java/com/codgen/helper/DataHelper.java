package com.codgen.helper;

import com.codgen.db.DbProvider;
import com.codgen.db.impl.MysqlProvider;
import com.codgen.db.impl.OracleProvider;
import com.codgen.model.*;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息提供者
 */
public class DataHelper {
    private static JdbcConfig jdbcConfig;
    private static Connection conn;
    private static DbProvider provider;

    /**
     * 根据JDBC配置模型构造一个数据库信息提供者
     *
     * @param jdbcConfig jdbc配置模型
     */
    public DataHelper(JdbcConfig jdbcConfig) {
        super();
        this.jdbcConfig = jdbcConfig;
        switch (jdbcConfig.getDatabaseType()) {
            case "mysql":
                provider = new MysqlProvider();
                break;
            case "oracle":
                provider = new OracleProvider();
                break;
            default:
                break;
        }
    }

    /**
     * 获取数据库连接
     */
    private Connection getConn() {
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

    /**
     * 初始化表模型
     *
     * @return
     */
    public List<TableModel> initTableModelList(TableConfig tableConfig) {
        List<TableModel> tableModelList = new ArrayList<TableModel>();
        List<TableModel> tableList = provider.getTableList(getConn(), jdbcConfig.getSchema());
        for (TableModel tableModel : tableList) {
            String tableName = tableModel.getTableName();
            System.out.println(tableName + ":" + tableModel.getTableComment());
            if (tableName.startsWith(tableConfig.getPrefix())) {
                initTableModel(tableModel, tableConfig);
                tableModelList.add(tableModel);
            }
            System.out.println(tableModel);
        }
        return tableModelList;
    }

    /**
     * 初始化表模型
     *
     * @param table 表名称
     * @return
     * @throws SQLException
     */
    public void initTableModel(TableModel table, TableConfig tableConfig) {
        String tableName = table.getTableName();
        String tableLabel = BuilderHelper.getPartString(tableName, tableConfig.getPrefix());
        String tableComment = StringUtils.defaultString(table.getTableComment(), tableLabel);
        table.setTableLabel(tableLabel);
        table.setTableComment(tableComment);
        String deleteField = tableConfig.getDeleteField();
        DeleteModel deleteModel = gainDeleteModel(deleteField);
        String excludeFields = tableConfig.getExcludeFields();

        List<ColumnModel> columnList = provider.getColumnList(getConn(), jdbcConfig.getSchema(), tableName);
        List<ColumnModel> columnPKList = new ArrayList<>();//主键列模型集合
        List<ColumnModel> columnCommonList = new ArrayList<>();//普通列模型集合
        List<DeleteModel> columnDeleteList = new ArrayList<>();//逻辑删除列模型集合
        for (ColumnModel column : columnList) {
            String columnLabel = BuilderHelper.getPartString(column.getColumnName());
            if (StringUtils.isEmpty(column.getColumnComment())) {
                column.setColumnComment(columnLabel);
            }
            column.setColumnLabel(columnLabel);

            //处理类型转换问题
            handleColumnType(column);

            //主键字段判断
            if (column.isPrimaryKey()) {
                columnPKList.add(column);
            }

            //特殊标识
            if (excludeFields.indexOf(column.getColumnName()) > -1) {
                column.setIgnoreFlag(true);
            } else {
                columnCommonList.add(column);
            }
            //逻辑删除字段判断
            if (column.getColumnName().equals(deleteModel.getColname())) {
                column.setDeleteFlag(true);
                columnDeleteList.add(deleteModel);
            }
        }
        table.setColumnList(columnList);
        table.setColumnPKList(columnPKList);
        table.setColumnCommonList(columnCommonList);
        table.setColumnDeleteList(columnDeleteList);
    }

    public DeleteModel gainDeleteModel(String s) {
        DeleteModel deleteModel = new DeleteModel();
        deleteModel.setColname(s.substring(0, s.indexOf("?")));
        deleteModel.setValid(s.substring(s.indexOf("?") + 1, s.indexOf(":")));
        deleteModel.setDisable(s.substring(s.indexOf(":") + 1, s.length()));
        return deleteModel;
    }

    /**
     * 处理类型转换问题
     *
     * @param columnModel
     */
    public static void handleColumnType(ColumnModel columnModel) {
        String columnType = columnModel.getColumnType();
        String javaType = columnModel.getColumnClassName();
        switch (columnType) {
            case "CHAR":
                javaType = "java.lang.String";
                break;
            case "VARCHAR":
            case "VARCHAR2":
                columnType = "VARCHAR";
                javaType = "java.lang.String";
                break;
            case "NVARCHAR2":
                columnType = "NVARCHAR";
                javaType = "java.lang.String";
                break;
            case "DATE":
            case "DATETIME":
                javaType = "java.util.Date";
                break;
            case "DECIMAL":
                if (columnModel.getScale() > 0) {
                    javaType = "java.lang.Double";
                } else {
                    if (columnModel.getPrecision() > 10) {
                        javaType = "java.lang.Long";
                    } else {
                        javaType = "java.lang.Integer";
                    }
                }
                break;
            case "NUMBER":
                columnType = "NUMERIC";
                if (columnModel.getScale() > 0) {
                    javaType = "java.lang.Double";
                } else {
                    javaType = "java.lang.Integer";
                }
                break;
            case "LONG":
                columnType = "BIGINT";
                javaType = "java.lang.Integer";
                break;
            case "INT":
                columnType = "BIGINT";
                javaType = "java.lang.Integer";
                break;
            case "LONGBLOB":
                javaType = "java.lang.String";
                break;
            case "MONEY":
            case "NUMERIC":
            case "FLOAT":
            case "SMALLMONEY":
                javaType = "java.lang.Double";
                break;
            default:
                break;
        }
        columnModel.setColumnType(columnType);
        columnModel.setColumnClassName(javaType);
        //从全限定名中截取简单类名
        columnModel.setColumnSimpleClassName(StringUtils.substringAfterLast(javaType, "."));
        //设置具体编程语言的数据类型所在的包，如java.lang.String的命名空间为java.lang
        columnModel.setColumnClassPackage(StringUtils.substringBeforeLast(javaType, "."));
    }

}
