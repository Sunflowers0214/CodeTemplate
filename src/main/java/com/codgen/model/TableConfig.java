package com.codgen.model;

import java.util.HashMap;
import java.util.Map;

/**
 * JDBC配置模型
 *
 */
public class TableConfig {
    private String prefix;
    private String validFlag;
    private Map<String, String> excludeFields = new HashMap<>();

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlags(String validFlag) {
        this.validFlag = validFlag;
    }

    public Map<String, String> getExcludeFields() {
        return excludeFields;
    }

    public void setExcludeFields(String[] excludeFields) {
        for (int k = 0; k < excludeFields.length; k++) {
            this.excludeFields.put(excludeFields[k], excludeFields[k]);
        }
    }
}
