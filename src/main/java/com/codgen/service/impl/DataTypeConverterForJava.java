package com.codgen.service.impl;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;


import com.codgen.model.ColumnModel;
import com.codgen.service.ColumnHandler;
import com.codgen.util.FileHelper;

/**
 * 针对java编程环境的数据类型转换器
 * @author 黄天政
 *
 */
public class DataTypeConverterForJava implements ColumnHandler {

	public void handle(ColumnModel columnModel) {
		String javaType = columnModel.getColumnClassName();
		String jdbcType =columnModel.getColumnTypeName();
		FileHelper helper = new FileHelper();
		Properties properties = helper.getProperties("jdbc.properties");
		
		if("java.math.BigDecimal".equals(javaType) ){
			if(columnModel.getScale()>0){
				javaType = "java.lang.Double";
			}else{
				if(columnModel.getPrecision()>10){
					javaType="java.lang.Long";
				}else {
					javaType="java.lang.Integer";
				}
			}
        }else if("java.sql.Timestamp".equals(javaType)){
        	javaType="java.util.Date";
        }
//        else{
//        	type="java.lang.String";
//        }
		
		//根据具体数据库方言中的数据类型确定java编程语言中的数据类型
		String typeName = columnModel.getColumnTypeName();
		if("money".equalsIgnoreCase(typeName)
			||"numeric".equalsIgnoreCase(typeName)
			||"float".equalsIgnoreCase(typeName)
			||"smallmoney".equalsIgnoreCase(typeName)){
			javaType = "java.lang.Double";
		}
		if("decimal".equalsIgnoreCase(typeName)){
			if(columnModel.getScale()>0){
				javaType = "java.lang.Double";
			}else{
				if(columnModel.getPrecision()>10){
					javaType="java.lang.Long";
				}else{
					javaType="java.lang.Integer";
				}
			}
		}
		if("LONGBLOB".equalsIgnoreCase(typeName)) {
			javaType="java.lang.String";
		}
		if ("number".equalsIgnoreCase(typeName)) {
			if (columnModel.getScale()>0) {
				javaType = "java.lang.Double";
			}else {
				javaType = "java.lang.Integer";
			}
		}
		if (properties.get("databaseType").equals("oracle")) {
			if (columnModel.getColumnTypeName().equalsIgnoreCase("VARCHAR2")) {
				jdbcType="VARCHAR";
			}else if(columnModel.getColumnTypeName().equalsIgnoreCase("NUMBER")) {
				jdbcType="NUMERIC";
			}else if(columnModel.getColumnTypeName().equalsIgnoreCase("NVARCHAR2")) {
				jdbcType="NVARCHAR";
			}else if(columnModel.getColumnTypeName().equalsIgnoreCase("INTERVAL DAY TO SECOND")) {
				jdbcType="TIME";
			}else if(columnModel.getColumnTypeName().equalsIgnoreCase("LONG")) {
				jdbcType="BIGINT";
			}
		}
		
		columnModel.setColumnClassName(javaType);
		columnModel.setColumnSimpleClassName(StringUtils.substringAfterLast(javaType, "."));//从全限定名中截取简单类名
		//设置具体编程语言的数据类型所在的包，如java.lang.String的命名空间为java.lang
		columnModel.setColumnClassPackage(StringUtils.substringBeforeLast(javaType, "."));
	}

}
