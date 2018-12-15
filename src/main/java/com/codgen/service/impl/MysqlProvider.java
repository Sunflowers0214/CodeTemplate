package com.codgen.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.codgen.model.JdbcConfig;
import com.codgen.service.ColumnHandler;
import com.codgen.service.DbProvider;
import com.codgen.util.FileHelper;
import com.codgen.util.JdbcUtil;

/**
 * 针对MSSQLServer2000的数据库信息提供者
 *
 * @author 黄天政
 */
public class MysqlProvider extends DbProvider {

    public MysqlProvider(JdbcConfig jdbcConfig) {
        super(jdbcConfig);
    }

    @Override
    protected Map<String, String> doGetTableComments(JdbcConfig jdbcConfig) {
        Map<String, String> tableComments = new LinkedHashMap<String, String>();
        Statement stmt = null;
        ResultSet rs = null;
        String comment;
        String tableName;
        String sql = "select TABLE_NAME,TABLE_COMMENT from information_schema.tables where table_type='BASE TABLE' and table_schema='" + jdbcConfig.getSchema() + "'";
        try {
            stmt = getConn().createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME").toLowerCase();
                comment = rs.getString("TABLE_COMMENT");
                if (StringUtils.isNotEmpty(comment)) {
                    String[] comms = rs.getString("TABLE_COMMENT").split(";");
                    if (comms.length > 1) {
                        comment = comms[0];
                    }
                } else {
                    comment = tableName;
                }
                tableComments.put(tableName, comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.safelyClose(rs, stmt);
        }
        return tableComments;
    }

    @Override
    protected Map<String, String> doGetColumnComments(JdbcConfig jdbcConfig, String tableName) {
        Map<String, String> colComment = new LinkedHashMap<String, String>();
        String columnName = null, comment = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select column_name,column_comment from information_schema.columns where table_schema='" + jdbcConfig.getSchema() + "' and table_name='" + tableName + "'";
        try {
            stmt = getConn().createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                columnName = rs.getString("column_name").toLowerCase();
                comment = StringUtils.trim(rs.getString("column_comment"));
                colComment.put(columnName, comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.safelyClose(rs, stmt);
        }
        return colComment;
    }

}
