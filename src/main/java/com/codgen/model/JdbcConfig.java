package com.codgen.model;

import java.util.Properties;

/**
 * JDBC配置模型
 */
public class JdbcConfig {
    private String databaseType;
    private String driverClass;
    private String jdbcUrl;
    private String schema;
    private String user;
    private String password;

    private String prefix;
    private boolean prefixDeleteFlag;
    private String deleteField;
    private String excludeFields;

    private JdbcConfig() {
    }

    public JdbcConfig(String databaseType, String driverClass, String jdbcUrl, String schema, String user, String password) {
        this.databaseType = databaseType;
        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.schema = schema;
        this.user = user;
        this.password = password;
    }

    public JdbcConfig(Properties prop) {
        this.databaseType = prop.getProperty("databaseType");
        this.driverClass = prop.getProperty("driverClass");
        this.jdbcUrl = prop.getProperty("jdbcUrl");
        this.schema = prop.getProperty("schema");
        this.user = prop.getProperty("user");
        this.password = prop.getProperty("password");
        this.prefix = prop.getProperty("prefix");
        this.prefixDeleteFlag = Boolean.valueOf(prop.getProperty("prefixDeleteFlag"));
        this.deleteField = prop.getProperty("deleteField");
        this.excludeFields = prop.getProperty("excludeFields");
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
