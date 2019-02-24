package com.codgen.model;

import java.util.ArrayList;
import java.util.List;


/**
 * 表模型
 */
public class TableModel {
    private String tableName;//表名
    private String schema;//数据库
    private String tableLabel;//表标签
    private String tabComment = "";//表注释(中文)
    private List<ColumnModel> columnList;//列模型集合
    private List<ColumnModel> pkColumnList;//主键列模型集合
    private List<ColumnModel> noteshuColList;//普通列模型集合
    private List<ColumnModel> teshuColList;//特殊处理列模型集合
    private List<ValidFlagModel> validFlagList;//逻辑删除模型

    private TableModel() {
    }

    public TableModel(String schema, String tableName, String tabComment) {
        this.schema = schema;
        this.tableName = tableName;
        this.tabComment = tabComment;
    }

    /**
     * @return 取得表名称
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName 设置表名称
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return 取得表的架构名称(一般为表的所有者，可能为空)
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @param schema 设置表的架构名称(一般为表的所有者，可以为空)
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return 取得表的显示标签
     */
    public String getTableLabel() {
        return tableLabel;
    }

    /**
     * @param tableLabel 设置表的显示标签
     */
    public void setTableLabel(String tableLabel) {
        this.tableLabel = tableLabel;
    }

    /**
     * @return 取得表的注释
     */
    public String getTabComment() {
        return tabComment;
    }

    /**
     * @param tabComment 设置表的注释
     */
    public void setTabComment(String tabComment) {
        this.tabComment = tabComment;
    }

    /**
     * @return 取得该表下包含的所有列模型
     */
    public List<ColumnModel> getColumnList() {
        return columnList;
    }

    /**
     * @param columnList 设置该表下包含的所有列模型
     */
    public void setColumnList(List<ColumnModel> columnList) {
        this.columnList = columnList;
    }

    public List<ColumnModel> getPkColumnList() {
        return pkColumnList;
    }

    public void setPkColumnList(List<ColumnModel> pkColumnList) {
        this.pkColumnList = pkColumnList;
    }

    public List<ColumnModel> getNoteshuColList() {
        return noteshuColList;
    }

    public void setNoteshuColList(List<ColumnModel> noteshuColList) {
        this.noteshuColList = noteshuColList;
    }

    public List<ColumnModel> getTeshuColList() {
        return teshuColList;
    }

    public void setTeshuColList(List<ColumnModel> teshuColList) {
        this.teshuColList = teshuColList;
    }

    public List<ValidFlagModel> getValidFlagList() {
        return validFlagList;
    }

    public void setValidFlagList(List<ValidFlagModel> validFlagList) {
        this.validFlagList = validFlagList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((tableName == null) ? 0 : tableName.hashCode());
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
        builder.append("<" + tabComment + ">模型：{schema=");
        builder.append(schema);
        builder.append(", tableName=");
        builder.append(tableName);
        builder.append(", tableLabel=");
        builder.append(tableLabel);
        builder.append(", tabComment=");
        builder.append(tabComment);
        builder.append(", \n>>>表字段集合columnList=");
        builder.append(columnList);
        builder.append(", \n>>>主键列集合pkColumnList=");
        builder.append(pkColumnList);
        //builder.append(", \n>>>表字段集合noteshuColList=");
        //builder.append(noteshuColList);
        builder.append(", \n>>>权限字段集合teshuColList=");
        builder.append(teshuColList);
        builder.append(", \n>>>逻辑删除validFlagList=");
        builder.append(validFlagList);
        builder.append("}\n");
        return builder.toString();
    }


}
