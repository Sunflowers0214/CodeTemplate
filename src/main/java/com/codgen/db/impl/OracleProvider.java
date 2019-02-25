package com.codgen.db.impl;

import com.codgen.helper.DbProvider;
import com.codgen.model.ColumnModel;
import com.codgen.model.TableModel;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 针对Oracle的数据库信息提供者
 * 45
 */
public class OracleProvider extends DbProvider {

    @Override
    public List<TableModel> getTableList(Connection conn, String schema) {
        List<TableModel> tableList = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM all_tab_comments WHERE table_type='TABLE' AND owner='" + schema + "'";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME").toLowerCase();
                String tableComment = rs.getString("COMMENTS");
//                if (StringUtils.isEmpty(tableComment)) {
//                    tableComment = tableName;
//                }
                tableList.add(new TableModel(tableName, tableComment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            safelyClose(rs);
            safelyClose(stmt);
        }
        return tableList;
    }

    @Override
    public List<ColumnModel> getColumnList(Connection conn, String schema, String tableName) {
        List<ColumnModel> columnList = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT COLUMN_NAME,COMMENTS AS COLUMN_COMMENT FROM ALL_COL_COMMENTS WHERE OWNER='" + schema + "' AND TABLE_NAME='" + tableName + "'";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME").toLowerCase();
                String columnComment = StringUtils.trim(rs.getString("COLUMN_COMMENT"));
                String columnType = "";//StringUtils.trim(rs.getString("COLUMN_TYPE"));
                int columnSize = 0;//Integer.valueOf(StringUtils.trim(rs.getString("COLUMN_SIZE")));
                columnList.add(new ColumnModel(columnName, columnComment, columnType, columnSize));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            safelyClose(rs);
            safelyClose(stmt);
        }
        return columnList;
    }

}
