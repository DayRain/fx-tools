package com.dayrain.dbmark.entity;

public class ColumnInfo {
    /**标识符号，表名+字段名组成**/
    private String identify;
    /**列名**/
    private String columnName;
    /**表名**/
    private String tableName;
    /**数据库名**/
    private String dbName;
    /**在表中的顺序**/
    private int position;
    /**是否可以为null**/
    private boolean nullable;
    /**默认值**/
    private String defaultValue;
    /**字段类型**/
    private String columnType;
    /**字符集**/
    private String characterSet;
    /**排序规则**/
    private String collationName;
    /**PRI，代表主键，UNI，代表唯一键，MUL，可重复**/
    private String indexType;
    /**额外信息，例如自增、根据时间戳更新等**/
    private String extra;
    /**备注**/
    private String comment;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", dbName='" + dbName + '\'' +
                ", position=" + position +
                ", nullable=" + nullable +
                ", defaultValue='" + defaultValue + '\'' +
                ", columnType='" + columnType + '\'' +
                ", characterSet='" + characterSet + '\'' +
                ", collationName='" + collationName + '\'' +
                ", indexType='" + indexType + '\'' +
                ", extra='" + extra + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
