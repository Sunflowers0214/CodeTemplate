package com.codgen.util;

import java.util.List;
import java.util.UUID;

import com.codgen.model.TableModel;


public class BuilderHelper {

    public TableModel getTableModelByName(List<TableModel> liTable, String tableName) {
        TableModel tableModel = new TableModel();
        for (int i = 0; i < liTable.size(); i++) {
            if (liTable.get(i).getTableName().endsWith(tableName)) {
                tableModel = liTable.get(i);
                break;
            }
        }
        return tableModel;
    }

    public String checkValue(String colComment, String s) {
        if (colComment != null) {
            String[] des = colComment.split(";");
            if (des.length == 1) {
                return colComment;
            }
            for (int i = 0; i < des.length; i++) {
                String[] subdes = des[i].split(":");
                if (subdes[0].equals(s)) {
                    return "1";
                }
            }
        }
        return "0";
    }

    public static String firstToUpper(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }

    public String firstToLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
    }

    public String nametoLower(String name) {
        return name.toLowerCase();
    }

    public Integer getYiBan(Integer name) {
        return name / 2;
    }

    public String getyu() {
        return "$";
    }

    public String getjing() {
        return "#";
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getPartString(String s) {
        s = s.toLowerCase();
        String[] parts = s.split("_");
        String result = "";
        if (parts.length > 1) {
            for (int k = 0; k < parts.length; k++) {
                if (k == 1 && parts[0].length() == 1) {
                    result += parts[k].toLowerCase();
                } else {
                    result += firstToUpper(parts[k].toLowerCase());
                }
            }
        } else {
            result = firstToUpper(s.toLowerCase());
        }
        return result;
    }

    /**
     * 去掉前缀
     *
     * @param s
     * @param prefix
     * @return
     */
    public static String getPartString(String s, String prefix) {
        if (prefix != null && !prefix.equals("")) {
            if (s.indexOf(prefix) == 0) {
                s = s.replaceFirst(prefix, "");
            }
        }
        return getPartString(s);
    }
}
