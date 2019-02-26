package com.codgen.model;

/**
 * 逻辑删除
 */
public class DeleteModel {
    private String columnName; //列名
    private String valid;//有效值
    private String disable;//无效值

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
        builder.append("{");
        builder.append("columnName=" + columnName + ",");
        builder.append("valid=" + valid + ",");
        builder.append("disable=" + disable);
        builder.append("}");
        return builder.toString();
    }

}
