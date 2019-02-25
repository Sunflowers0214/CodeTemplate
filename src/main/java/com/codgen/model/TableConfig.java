package com.codgen.model;

import java.util.Properties;

/**
 * JDBC配置模型
 */
public class TableConfig {
    private String prefix;
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
        this.deleteField = properties.getProperty("deleteField");
        this.excludeFields = properties.getProperty("excludeFields");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
