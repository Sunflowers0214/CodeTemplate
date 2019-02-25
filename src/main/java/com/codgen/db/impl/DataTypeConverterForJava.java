package com.codgen.db.impl;

import com.codgen.model.ColumnModel;
import org.apache.commons.lang.StringUtils;

/**
 * 针对java编程环境的数据类型转换器
 */
public class DataTypeConverterForJava {

    public static void handle(ColumnModel columnModel) {
        String columnType = columnModel.getColumnType();
        String javaType = columnModel.getColumnClassName();
        switch (columnType) {
            case "CHAR":
                columnType = "CHAR";
                javaType = "java.lang.String";
                break;
            case "VARCHAR":
            case "VARCHAR2":
                columnType = "VARCHAR";
                javaType = "java.lang.String";
                break;
            case "NVARCHAR2":
                columnType = "NVARCHAR";
                javaType = "java.lang.String";
                break;
            case "DATE":
            case "DATETIME":
                columnType = "VARCHAR";
                javaType = "java.util.Date";
                break;
            case "DECIMAL":
                columnType = "DECIMAL";
                if (columnModel.getScale() > 0) {
                    javaType = "java.lang.Double";
                } else {
                    if (columnModel.getPrecision() > 10) {
                        javaType = "java.lang.Long";
                    } else {
                        javaType = "java.lang.Integer";
                    }
                }
                break;
            case "NUMBER":
                columnType = "NUMERIC";
                if (columnModel.getScale() > 0) {
                    javaType = "java.lang.Double";
                } else {
                    javaType = "java.lang.Integer";
                }
                break;
            case "LONG":
                columnType = "BIGINT";
                javaType = "java.lang.Integer";
                break;
            case "LONGBLOB":
                javaType = "java.lang.String";
                break;
            case "MONEY":
            case "NUMERIC":
            case "FLOAT":
            case "SMALLMONEY":
                javaType = "java.lang.Double";
                break;
            default:
                break;
        }
        columnModel.setColumnType(columnType);
        columnModel.setColumnClassName(javaType);
        //从全限定名中截取简单类名
        columnModel.setColumnSimpleClassName(StringUtils.substringAfterLast(javaType, "."));
        //设置具体编程语言的数据类型所在的包，如java.lang.String的命名空间为java.lang
        columnModel.setColumnClassPackage(StringUtils.substringBeforeLast(javaType, "."));
    }

}
