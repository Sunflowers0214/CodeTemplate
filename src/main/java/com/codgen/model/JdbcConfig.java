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
}
