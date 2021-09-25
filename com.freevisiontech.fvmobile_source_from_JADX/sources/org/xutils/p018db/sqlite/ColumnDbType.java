package org.xutils.p018db.sqlite;

/* renamed from: org.xutils.db.sqlite.ColumnDbType */
public enum ColumnDbType {
    INTEGER("INTEGER"),
    REAL("REAL"),
    TEXT("TEXT"),
    BLOB("BLOB");
    
    private String value;

    private ColumnDbType(String value2) {
        this.value = value2;
    }

    public String toString() {
        return this.value;
    }
}
