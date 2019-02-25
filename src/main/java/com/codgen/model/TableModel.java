package com.codgen.model;

import java.util.List;


/**
 * 表模型
 */
public class TableModel {
    private String tableName;//表名
    private String tableLabel;//表标签
    private String tableComment = "";//表注释(中文)
    private List<ColumnModel> columnList;//列模型集合
    private List<ColumnModel> columnCommonList;//普通列模型集合
    private List<ColumnModel> columnPKList;//主键列模型集合
    private List<DeleteModel> columnDeleteList;//逻辑删除模型

    private TableModel() {
    }

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

    public List<ColumnModel> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnModel> columnList) {
        this.columnList = columnList;
    }

    public List<ColumnModel> getColumnCommonList() {
        return columnCommonList;
    }

    public void setColumnCommonList(List<ColumnModel> columnCommonList) {
        this.columnCommonList = columnCommonList;
    }

    public List<ColumnModel> getColumnPKList() {
        return columnPKList;
    }

    public void setColumnPKList(List<ColumnModel> columnPKList) {
        this.columnPKList = columnPKList;
    }

    public List<DeleteModel> getColumnDeleteList() {
        return columnDeleteList;
    }

    public void setColumnDeleteList(List<DeleteModel> columnDeleteList) {
        this.columnDeleteList = columnDeleteList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TableModel)) {
            return false;
        }
        TableModel other = (TableModel) obj;
        if (tableName == null) {
            if (other.tableName != null) {
                return false;
            }
        } else if (!tableName.equals(other.tableName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<" + tableComment + ">模型：{");
        builder.append("tableName=" + tableName);
        builder.append(", tableLabel=" + tableLabel);
        builder.append(", tableComment=" + tableComment);
        builder.append(", \n>>>表字段集合columnList=");
        builder.append(columnList);
        builder.append(", \n>>>主键列集合columnPKList=");
        builder.append(columnPKList);
        builder.append(", \n>>>逻辑删除columnDeleteList=");
        builder.append(columnDeleteList);
        builder.append("}\n");
        return builder.toString();
    }


}
