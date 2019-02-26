package com.codgen.model;

import java.util.Properties;

/**
 * JDBC配置模型
 */
public class TableConfig {
    private String prefix;
    private boolean prefixDeleteFlag;
    private String deleteField;
    private String excludeFields;

    private TableConfig() {
    }

    public TableConfig(String prefix, String deleteField, String excludeFields) {
        this.prefix = prefix;
        this.deleteField = deleteField;
        this.excludeFields = excludeFields;
    }

    public TableConfig(Properties properties) {
        this.prefix = properties.getProperty("prefix");
        this.prefixDeleteFlag = Boolean.valueOf(properties.getProperty("prefixDeleteFlag"));
        this.deleteField = properties.getProperty("deleteField");
        this.excludeFields = properties.getProperty("excludeFields");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isPrefixDeleteFlag() {
        return prefixDeleteFlag;
    }

    public void setPrefixDeleteFlag(boolean prefixDeleteFlag) {
        this.prefixDeleteFlag = prefixDeleteFlag;
    }

    public String getDeleteField() {
        return deleteField;
    }

    public void setDeleteField(String deleteField) {
        this.deleteField = deleteField;
    }

    public String getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(String excludeFields) {
        this.excludeFields = excludeFields;
    }
}
