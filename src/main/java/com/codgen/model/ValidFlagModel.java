package com.codgen.model;

/**
 * 逻辑删除
 *
 * @author wangzhuzhu
 */
public class ValidFlagModel {
    private ColumnModel columnModel;
    private String colname;
    private String valid;//有效值
    private String disable;//无效值


    public ColumnModel getColumnModel() {
        return columnModel;
    }

    public void setColumnModel(ColumnModel columnModel) {
        this.columnModel = columnModel;
    }

    public String getColname() {
        return colname;
    }

    public void setColname(String colname) {
        this.colname = colname;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<"+colname+"> [colname=");
        builder.append(colname);
        builder.append(", valid=");
        builder.append(valid);
        builder.append(", disable=");
        builder.append(disable);
        return builder.toString();
    }

}
