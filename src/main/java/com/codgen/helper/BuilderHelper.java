package com.codgen.helper;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BuilderHelper {

    /**
     * 首字母大写
     *
     * @param value
     * @return
     */
    public static String firstToUpper(String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1, value.length());
    }

    /**
     * 首字母小写
     *
     * @param value
     * @return
     */
    public String firstToLower(String value) {
        return value.substring(0, 1).toLowerCase() + value.substring(1, value.length());
    }

    /**
     * 大写
     *
     * @param value
     * @return
     */
    public String toUpperCase(String value) {
        return value.toUpperCase();
    }

    /**
     * 小写
     *
     * @param value
     * @return
     */
    public String toLowerCase(String value) {
        return value.toLowerCase();
    }

    /**
     * 获取$符号
     *
     * @return
     */
    public String getyu() {
        return "$";
    }

    /**
     * 获取#符号
     *
     * @return
     */
    public String getJing() {
        return "#";
    }

    public boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 驼峰格式化
     *
     * @param line
     * @return
     */
    public static String getPartString(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        //匹配正则表达式
        while (matcher.find()) {
            String word = matcher.group();
            //当是true 或则是空的情况
            if (matcher.start() == 0) {
                sb.append(Character.toLowerCase(word.charAt(0)));
            } else {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }

            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 去掉前缀
     *
     * @param value
     * @param prefix
     * @return
     */
    public static String getPartString(String value, String prefix) {
        if (StringUtils.isNotEmpty(prefix) && value.startsWith(prefix)) {
            value = value.replaceFirst(prefix, "");
        }
        return getPartString(value);
    }
}
