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
    private List<ColumnModel> columnPKList;//主键列模型集合
    private DeleteModel deleteModel;//逻辑删除模型

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

    public List<ColumnModel> getColumnPKList() {
        return columnPKList;
    }

    public void setColumnPKList(List<ColumnModel> columnPKList) {
        this.columnPKList = columnPKList;
    }

    public DeleteModel getDeleteModel() {
        return deleteModel;
    }

    public void setDeleteModel(DeleteModel deleteModel) {
        this.deleteModel = deleteModel;
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
        builder.append(", \n>>>逻辑删除deleteModel=");
        builder.append(deleteModel);
        builder.append("}\n");
        return builder.toString();
    }


}
