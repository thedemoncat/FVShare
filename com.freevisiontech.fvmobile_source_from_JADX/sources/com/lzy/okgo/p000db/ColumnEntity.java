package com.lzy.okgo.p000db;

/* renamed from: com.lzy.okgo.db.ColumnEntity */
public class ColumnEntity {
    public String columnName;
    public String columnType;
    public String[] compositePrimaryKey;
    public boolean isAutoincrement;
    public boolean isNotNull;
    public boolean isPrimary;

    public ColumnEntity(String... compositePrimaryKey2) {
        this.compositePrimaryKey = compositePrimaryKey2;
    }

    public ColumnEntity(String columnName2, String columnType2) {
        this(columnName2, columnType2, false, false, false);
    }

    public ColumnEntity(String columnName2, String columnType2, boolean isPrimary2, boolean isNotNull2) {
        this(columnName2, columnType2, isPrimary2, isNotNull2, false);
    }

    public ColumnEntity(String columnName2, String columnType2, boolean isPrimary2, boolean isNotNull2, boolean isAutoincrement2) {
        this.columnName = columnName2;
        this.columnType = columnType2;
        this.isPrimary = isPrimary2;
        this.isNotNull = isNotNull2;
        this.isAutoincrement = isAutoincrement2;
    }
}
