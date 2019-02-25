package com.codgen.db;

import com.codgen.model.ColumnModel;
import com.codgen.model.TableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 数据库信息提供者
 */
public abstract class DbProvider {

    /**
     * 取得所有表的表注释，不同的数据库方言有不同的实现方式。
     *
     * @return
     */
    public abstract List<TableModel> getTableList(Connection conn, String schema);

    /**
     * 获取指定表所有列注释。返回的集合的元素格式为：列名(COLUMN_NAME)和注释(COMMENTS)。
     *
     * @return
     */
    public abstract List<ColumnModel> getColumnList(Connection conn, String schema, String tableName);

    public void safelyClose(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void safelyClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
