package com.codgen.helper;

import com.codgen.db.DbProvider;
import com.codgen.db.impl.MysqlProvider;
import com.codgen.db.impl.OracleProvider;
import com.codgen.model.ColumnModel;
import com.codgen.model.JdbcConfig;
import com.codgen.model.TableModel;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化表模型
     *
     * @return
     */
    public List<TableModel> initTableModelList() {
        List<TableModel> tableList = provider.getTableList(getConn(), jdbcConfig.getSchema());
        for (TableModel tableModel : tableList) {
            String tableName = tableModel.getTableName();
            if (StringUtils.isNotEmpty(jdbcConfig.getPrefix()) && !tableName.startsWith(jdbcConfig.getPrefix())) {
                continue;
            }
            handleTableModel(tableModel);
            initColumnModelList(tableModel);
        }
        closeConnection();
        return tableList;
    }

    /**
     * 处理表信息
     *
     * @param table
     */
    public static void handleTableModel(TableModel table) {
        System.out.println("处理表：" + table.getTableName() + ":" + table.getTableComment());
        String tableName = table.getTableName();
        if (StringUtils.isNotEmpty(jdbcConfig.getPrefix()) && jdbcConfig.isPrefixDeleteFlag()) {
            tableName = tableName.replaceFirst(jdbcConfig.getPrefix(), "");
        }
        String tableLabel = BuilderHelper.lineToCase(tableName);
        String tableComment = StringUtils.defaultString(table.getTableComment(), tableLabel);
        table.setTableLabel(tableLabel);
        table.setTableComment(tableComment);
    }

    /**
     * 初始化表模型
     *
     * @param table 表名称
     * @return
     * @throws SQLException
     */
    public void initColumnModelList(TableModel table) {
        String tableName = table.getTableName();
        if (StringUtils.isNotEmpty(jdbcConfig.getPrefix()) && jdbcConfig.isPrefixDeleteFlag()) {
            tableName = tableName.replaceFirst(jdbcConfig.getPrefix(), "");
        }
        String tableLabel = BuilderHelper.lineToCase(tableName);
        String tableComment = StringUtils.defaultString(table.getTableComment(), tableLabel);
        table.setTableLabel(tableLabel);
        table.setTableComment(tableComment);
        String excludeFields = jdbcConfig.getExcludeFields();//不处理字段
        String deleteField = jdbcConfig.getDeleteField();//删除标识字段
        String deleteCoulumn = deleteField.substring(0, deleteField.indexOf("?"));
        String valid = deleteField.substring(deleteField.indexOf("?") + 1, deleteField.indexOf(":"));
        String disable = deleteField.substring(deleteField.indexOf(":") + 1, deleteField.length());

        List<ColumnModel> columnList = provider.getColumnList(getConn(), jdbcConfig.getSchema(), table.getTableName());
        for (ColumnModel column : columnList) {
            String columnLabel = BuilderHelper.lineToCase(column.getColumnName());
            if (StringUtils.isEmpty(column.getColumnComment())) {
                column.setColumnComment(columnLabel);
            }
            column.setColumnLabel(columnLabel);

            //处理类型转换问题
            handleColumnType(column);

            //特殊标识
            if (excludeFields.indexOf(column.getColumnName()) > -1) {
                column.setIgnoreFlag(true);
            }

            //逻辑删除字段判断
            if (column.getColumnName().equals(deleteCoulumn)) {
                column.setDeleteFlag(true);
                column.setValid(valid);
                column.setDisable(disable);
            }
        }
        table.setColumnList(columnList);
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
