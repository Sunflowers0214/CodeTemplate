package com.codgen.model;

/**
 * 逻辑删除
 */
public class DeleteModel {
    private String columnName; //列名
    private String columnLabel;//列标签，列注释的标签部分。用于打印输出和显示的指定列的建议标题（中文）
    private String colname;
    private String valid;//有效值
    private String disable;//无效值

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
        builder.append("<" + colname + "> {colname=");
        builder.append(colname);
        builder.append(", valid=");
        builder.append(valid);
        builder.append(", disable=");
        builder.append(disable);
        builder.append("}");
        return builder.toString();
    }

}
