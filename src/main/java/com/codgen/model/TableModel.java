package com.codgen.model;

import java.util.List;

/**
 * 表模型
 */
public class TableModel {
    private String tableName;//表名
    private String tableLabel;//表标签
    private String tableComment = "";//表注释(中文)
    private Convert convert; //

    private List<ColumnModel> columnList;//列模型集合

    public TableModel(String tableName, String tableComment) {
        this.tableName = tableName;
        this.tableComment = tableComment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableLabel() {
        return tableLabel;
    }

    public void setTableLabel(String tableLabel) {
        this.tableLabel = tableLabel;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public Convert getConvert() {
        return convert;
    }

    public void setConvert(Convert convert) {
        this.convert = convert;
    }

    public List<ColumnModel> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnModel> columnList) {
        this.columnList = columnList;
    }

}
