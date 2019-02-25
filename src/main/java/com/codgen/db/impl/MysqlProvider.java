package com.codgen.db.impl;

import com.codgen.model.ColumnModel;
import com.codgen.model.JdbcConfig;
import com.codgen.db.DbProvider;
import com.codgen.model.TableModel;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 针对Mysql的数据库信息提供者
 */
public class MysqlProvider {

    public static List<TableModel> getTableList(Connection conn, String schema) {
        List<TableModel> tableList = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema='" + schema + "'";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME").toLowerCase();
                String tableComment = rs.getString("TABLE_COMMENT");
//                if (StringUtils.isEmpty(tableComment)) {
//                    tableComment = tableName;
//                }
                tableList.add(new TableModel(schema, tableName, tableComment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbProvider.safelyClose(rs);
            DbProvider.safelyClose(stmt);
        }
        return tableList;
    }

    public static List<ColumnModel> getColumnList(Connection conn, String schema, String tableName) {
        List<ColumnModel> columnList = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT COLUMN_NAME,COLUMN_COMMENT,DATA_TYPE COLUMN_TYPE,IFNULL(CHARACTER_MAXIMUM_LENGTH,0) COLUMN_SIZE,COLUMN_KEY,IS_NULLABLE,IFNULL(NUMERIC_PRECISION,IFNULL(CHARACTER_MAXIMUM_LENGTH,0)) NUMERIC_PRECISION,IFNULL(NUMERIC_SCALE,0) NUMERIC_SCALE";
        sql += " FROM information_schema.columns";
        sql += " WHERE table_schema='" + schema + "' AND table_name='" + tableName + "'";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME").toLowerCase();
                String columnComment = StringUtils.trim(rs.getString("COLUMN_COMMENT"));
                String columnType = StringUtils.trim(rs.getString("COLUMN_TYPE")).toUpperCase();
                int columnSize = Integer.valueOf(StringUtils.trim(rs.getString("COLUMN_SIZE")));
                boolean nullable = StringUtils.trim(rs.getString("IS_NULLABLE")).equals("YES");
                boolean primaryKey = StringUtils.trim(rs.getString("COLUMN_KEY")).equals("PRI");
                int precision = Integer.valueOf(StringUtils.trim(rs.getString("NUMERIC_PRECISION")));
                int scale = Integer.valueOf(StringUtils.trim(rs.getString("NUMERIC_SCALE")));
                ColumnModel columnModel = new ColumnModel(columnName, columnComment, columnType, columnSize);
                columnModel.setPrecision(precision);
                columnModel.setScale(scale);
                columnModel.setPrimaryKey(primaryKey);
                columnModel.setNullable(nullable);
                columnList.add(columnModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbProvider.safelyClose(rs);
            DbProvider.safelyClose(stmt);
        }
        return columnList;
    }

}
