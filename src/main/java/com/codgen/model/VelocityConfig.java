package com.codgen.model;

/**
 * Velocity配置模型
 */
public class VelocityConfig {
    private String templateName;
    private String filePath;
    private String prefix;
    private String suffix;
    private boolean configFlag = false;

    public VelocityConfig(String templateName, String filePath, String prefix, String suffix) {
        this.templateName = templateName;
        this.filePath = filePath;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public VelocityConfig(String templateName, String filePath, String prefix, String suffix, boolean configFlag) {
        this.templateName = templateName;
        this.filePath = filePath;
        this.prefix = prefix;
        this.suffix = suffix;
        this.configFlag = configFlag;
    }

    public VelocityConfig(String templateName, String filePath, String suffix) {
        this.templateName = templateName;
        this.filePath = filePath;
        this.suffix = suffix;
    }

    public VelocityConfig(String templateName, String filePath, String suffix, boolean configFlag) {
        this.templateName = templateName;
        this.filePath = filePath;
        this.suffix = suffix;
        this.configFlag = configFlag;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isConfigFlag() {
        return configFlag;
    }

    public void setConfigFlag(boolean configFlag) {
        this.configFlag = configFlag;
    }
}
