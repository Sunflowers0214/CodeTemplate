package com.codgen.model;


/**
 * 列模型。注意该类由于和TableModel存在相互引用的关系，因此实现了一个接口CycleRecoverable来防止在序列化或克隆时带来的循环引用问题
 *
 * @author 黄天政
 */
public class ColumnModel {

    private String columnName; //列名
    private String columnLabel;//列标签，列注释的标签部分。用于打印输出和显示的指定列的建议标题（中文）
    private String colComment;//列注释
    private String columnType;//列的 SQL 类型。
    private int columnSize;//列的 SQL 长度
    private String columnClassName;//面向具体编程语言中类的完全限定名称，默认的编程语言为Java，如：java.lang.String。
    private String columnSimpleClassName;//面向具体编程语言中类的简单类名，非全限定名，，默认的编程语言为Java，如String
    private String columnClassPackage;//面向具体编程语言中类的所在的包(命名空间)，默认的编程语言为Java，如：java.lang
    private int precision = 0;//获取指定列的指定列宽。对于数值型数据，是指最大精度。对于字符型数据，是指字符串长度。
    private int scale = 0;//列的小数点右边的位数。对于其标度不可用的数据类型，默认为 0。
    private boolean primaryKey = false;//标识该列是否为主键
    private boolean nullable = true;//标识该列的值能否为空

    public ColumnModel() {
        this.columnName = columnName;
    }

    public ColumnModel(String columnName, String colComment, String columnType, int columnSize) {
        this.columnName = columnName;
        this.colComment = colComment;
        this.columnType = columnType;
        this.columnSize = columnSize;
    }

    public ColumnModel(String columnName, String colComment, String columnType, int columnSize, int precision, int scale, boolean primaryKey, boolean nullable) {
        this.columnName = columnName;
        this.colComment = colComment;
        this.columnType = columnType;
        this.columnSize = columnSize;
        this.precision = precision;
        this.scale = scale;
        this.primaryKey = primaryKey;
        this.nullable = nullable;
    }

    //@return 取得列名称
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
//        if (columnName != null) {
//            columnName = columnName.toLowerCase();
//        }
        this.columnName = columnName;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    //@return 取得列注释
    public String getColComment() {
        return colComment;
    }

    public void setColComment(String colComment) {
        this.colComment = colComment;
    }

    //取得在java.sql.Types定义的类型
    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public String getColumnSimpleClassName() {
        return columnSimpleClassName;
    }

    public void setColumnSimpleClassName(String columnSimpleClassName) {
        this.columnSimpleClassName = columnSimpleClassName;
    }

    //@return 取得面向具体编程语言中类的完全限定名称，如java中的java.lang.String。
    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    //@return 取得面向具体编程语言中类的所在的包(命名空间)，如java中的java.lang
    public String getColumnClassPackage() {
        return columnClassPackage;
    }

    public void setColumnClassPackage(String columnClassPackage) {
        this.columnClassPackage = columnClassPackage;
    }

    /**
     * @return 获取指定列的指定列宽。
     * <br>对于数值型数据，是指最大精度。对于字符型数据，是指字符串长度。
     * <br>对于日期时间的数据类型，是指 String 表示形式的字符串长度（假定为最大允许的小数秒组件）。
     * <br>对于二进制型数据，是指字节长度。
     * <br>对于 ROWID 数据类型，是指字节长度。
     * <br>对于其列大小不可用的数据类型，则返回 0。
     */
    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    //@return 获取列的小数点右边的位数。对于其标度不可用的数据类型，默认为 0。
    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    //@return 判断该列是否为主键列
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    //@return 判断该列是否允许空
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ColumnModel)) {
            return false;
        }
        ColumnModel other = (ColumnModel) obj;
        if (columnName == null) {
            if (other.columnName != null) {
                return false;
            }
        } else if (!columnName.equalsIgnoreCase(other.columnName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n---<" + colComment + ">{columnName=");
        builder.append(columnName);
        builder.append(", colComment=");
        builder.append(colComment);
        builder.append(", columnLabel=");
        builder.append(columnLabel);
        builder.append(", columnType=");
        builder.append(columnType);
        builder.append(", columnSize=");
        builder.append(columnSize);
        builder.append(", columnClassName=");
        builder.append(columnClassName);
        builder.append(", columnClassPackage=");
        builder.append(columnClassPackage);
        builder.append(", columnSimpleClassName=");
        builder.append(columnSimpleClassName);
        builder.append(", nullable=");
        builder.append(nullable);
        builder.append(", precision=");
        builder.append(precision);
        builder.append(", primaryKey=");
        builder.append(primaryKey);
        builder.append(", scale=");
        builder.append(scale);
        builder.append("}");
        return builder.toString();
    }

}
