package com.codgen.service;

import com.codgen.model.*;
import com.codgen.service.impl.DataTypeConverterForJava;
import com.codgen.util.BuilderHelper;
import com.codgen.util.JdbcUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库信息提供者。该抽象类基于JDBC实现了与数据库方言无关的方法，把与数据库方言相关的操作分派给子类实现。
 *
 * @author 黄天政
 */
public abstract class DbProvider {
    private Connection conn;
    private JdbcConfig jdbcConfig;
    private List<ColumnHandler> columnHandlers = new ArrayList<ColumnHandler>();
    private Map<String, Map<String, String>> columnComments;
    private Map<String, String> tableComments;

    /**
     * 根据jdbc配置模型构造一个数据库信息提供者
     *
     * @param jdbcConfig jdbc配置模型
     */
    public DbProvider(JdbcConfig jdbcConfig) {
        super();
        this.jdbcConfig = jdbcConfig;
    }

    /**
     * 获取一个数据库连接
     */
    protected Connection getConn() {
        if (jdbcConfig == null) {
            try {
                throw new Exception(this.getClass().getName() + "jdbcConfig和conn不能同时为null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        conn = JdbcUtil.getConn(jdbcConfig);
        return conn;
    }

    /**
     * 取得所有表的表注释，不同的数据库方言有不同的实现方式。
     *
     * @return
     */
    protected abstract Map<String, String> doGetTableComments(JdbcConfig jdbcConfig);

    /**
     * 获取指定表所有列注释。返回的集合的元素格式为：列名(COLUMN_NAME)和注释(COMMENTS)。
     *
     * @return
     */
    protected abstract Map<String, String> doGetColumnComments(JdbcConfig jdbcConfig, String tableName);

    /**
     * 所有表的表注释
     *
     * @return
     */
    public Map<String, String> getTableComments() {
        if (tableComments == null) {
            tableComments = doGetTableComments(jdbcConfig);
        }
        return tableComments;
    }

    /**
     * 获取所有表列表
     */
    public List<String> getTableNames() {
        Map<String, String> tableNameMap = getTableComments();
        List<String> tableNames = new ArrayList<String>(tableNameMap.keySet());
        return tableNames;
    }

    public String getTableComment(String tableName, String defaultStr) {
        String comment = getTableComments().get(tableName);
        return StringUtils.defaultString(comment, defaultStr);
    }

    /**
     * 获取指定表的所有的列名称
     *
     * @return
     */
    public List<String> getColumnNames(String tableName) {
        List<String> colList = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConn().createStatement();
            String sql = "select * from " + tableName;
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                colList.add(rsmd.getColumnName(i).toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.safelyClose(rs, stmt);
        }
        return colList;
    }


    /**
     * 一次性获取指定表的所有列注释。返回的集合的元素格式为：列名(COLUMN_NAME)和注释(COMMENTS)。
     *
     * @return
     */
    protected Map<String, String> getColumnComments(String tableName) {
        if (columnComments == null) {
            columnComments = new HashMap<>();
        }
        Map<String, String> resultMap = columnComments.get(tableName);
        if (resultMap == null) {
            resultMap = doGetColumnComments(jdbcConfig, tableName);
            columnComments.put(tableName, resultMap);
        }
        return resultMap;
    }

    /**
     * 获取指定表的主键模型集合
     *
     * @param tableName 表名称
     * @return
     */
    protected List<PrimaryKeyModel> getPrimaryKeys(String tableName) {
        ResultSet rs = null;
        List<PrimaryKeyModel> pkModelList = new ArrayList<PrimaryKeyModel>();
        PrimaryKeyModel pkModel;
        try {
            rs = getConn().getMetaData().getPrimaryKeys(jdbcConfig.getSchema(), jdbcConfig.getSchema(), tableName);
            while (rs.next()) {
                pkModel = new PrimaryKeyModel();
                pkModel.setTableCat(rs.getString("TABLE_CAT"));
                pkModel.setTableSchem(rs.getString("TABLE_SCHEM"));
                pkModel.setTableName(rs.getString("TABLE_NAME"));
                pkModel.setColumnName(rs.getString("COLUMN_NAME"));
                pkModel.setKeySeq(rs.getShort("KEY_SEQ"));
                pkModel.setPkName(rs.getString("PK_NAME"));
                pkModelList.add(pkModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.safelyClose(rs);
        }
        return pkModelList;
    }


    /**
     * 返回指定表指定列的注释，如果未取得注释，则返回指定的列名称。
     * 该操作会把列注释里的所有换行符号和回车符号统一替换成空格符号
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    public String getColumnComment(String tableName, String columnName, String columnLabel) {
        String comment = columnLabel;
        Map<String, String> ccmap = getColumnComments(tableName);
        if (ccmap != null && StringUtils.isNotEmpty(ccmap.get(columnName))) {
            comment = StringUtils.defaultString(ccmap.get(columnName), columnLabel).replace("\n", " ").replace("\r", " "); //把换行符和回车符统一替换成空格
        }
        return comment;
    }

    /**
     * 判断列类型是否为不可以返回精度（Precision）的列类型，如oracle的大字段类型Blob和Clob。
     *
     * @param columnType
     * @return
     */
    protected boolean isPrecisionUnknowType(int columnType) {
        return columnType == java.sql.Types.CLOB || columnType == java.sql.Types.BLOB;
    }

    /**
     * 根据指定的表名创建一个表模型
     *
     * @param tableName 表名称
     * @return
     * @throws SQLException
     */
    public TableModel createTableModel(String tableName, TableConfig tableConfig) {
        TableModel table = new TableModel();
        table.setTableName(tableName);
        table.setSchema(jdbcConfig.getSchema());
        table.setTableLabel(BuilderHelper.getPartString(tableName, tableConfig.getPrefix()));
        table.setTabComment(getTableComment(table.getTableName(), table.getTableLabel()));
        List<PrimaryKeyModel> primaryKeyList = getPrimaryKeys(table.getTableName());
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConn().createStatement();
            String sql = "select * from " + table.getSchema() + "." + tableName;
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            ColumnModel col = null;
            String validFlags = tableConfig.getValidFlag();
            ValidFlagModel validFlagModel = gainValidFlagModel(validFlags);
            Map<String, String> excludeFields = tableConfig.getExcludeFields();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                col = new ColumnModel();
                col.setColumnName(rsmd.getColumnName(i));
                col.setColumnLabel(BuilderHelper.getPartString(rsmd.getColumnName(i)));
                col.setColComment(getColumnComment(tableName, col.getColumnName(), col.getColumnLabel()));
                col.setColumnType(rsmd.getColumnType(i));//设置sql数据类型
                col.setColumnTypeName(rsmd.getColumnTypeName(i));//设置sql数据类型的名称
                col.setColumnDisplaySize(rsmd.getColumnDisplaySize(i));//取得列显示的最大宽度
                //设置列的sql数据类型在java编程语言中对应的具体数据类型
                col.setColumnClassName(rsmd.getColumnClassName(i));
                //如果没有设置任何的列模型处理器，则默认使用一个java数据类型转换器来处理列模型
                if (columnHandlers == null || columnHandlers.size() == 0) {
                    new DataTypeConverterForJava().handle(col);
                } else {
                    for (ColumnHandler columnHandler : columnHandlers) {
                        columnHandler.handle(col);
                    }
                }
                //设置列的数据标度
                col.setScale(rsmd.getScale(i));
                //判断列类型是否为未知类型，如oracle的大字段类型Blob和Clob。
                if (!isPrecisionUnknowType(rsmd.getColumnType(i))) {
                    //如果不是未知类型，则设置列的数据精度
                    col.setPrecision(rsmd.getPrecision(i));
                }
                //判断该列允许空否
                if (rsmd.isNullable(i) == ResultSetMetaData.columnNoNulls) {
                    col.setNullable(false);
                } else {
                    col.setNullable(true);
                }
                //判断是否为主键列
                if (isPrimaryKey(primaryKeyList, col.getColumnName())) {
                    col.setPrimaryKey(true);
                }
                table.getColumnList().add(col);
                if (col.isPrimaryKey()) {
                    table.getPkColumnList().add(col);
                }
                if (excludeFields.containsKey(col.getColumnName())) {
                    table.getTeshuColList().add(col);
                } else {
                    table.getNoteshuColList().add(col);
                }

                if (col.getColumnName().equals(validFlagModel.getColname())) {
                    validFlagModel.setColumnModel(col);
                    table.getValidFlagList().add(validFlagModel);
                }
            }
            System.out.println(table);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.safelyClose(rs, stmt);
        }
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
