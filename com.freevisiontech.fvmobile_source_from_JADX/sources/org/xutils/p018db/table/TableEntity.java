package org.xutils.p018db.table;

import android.database.Cursor;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import org.xutils.DbManager;
import org.xutils.common.util.IOUtil;
import org.xutils.p018db.annotation.Table;
import org.xutils.p019ex.DbException;

/* renamed from: org.xutils.db.table.TableEntity */
public final class TableEntity<T> {
    private volatile boolean checkedDatabase;
    private final LinkedHashMap<String, ColumnEntity> columnMap;
    private Constructor<T> constructor;

    /* renamed from: db */
    private final DbManager f1211db;
    private Class<T> entityType;

    /* renamed from: id */
    private ColumnEntity f1212id;
    private final String name;
    private final String onCreated;

    TableEntity(DbManager db, Class<T> entityType2) throws Throwable {
        this.f1211db = db;
        this.entityType = entityType2;
        this.constructor = entityType2.getConstructor(new Class[0]);
        this.constructor.setAccessible(true);
        Table table = (Table) entityType2.getAnnotation(Table.class);
        this.name = table.name();
        this.onCreated = table.onCreated();
        this.columnMap = TableUtils.findColumnMap(entityType2);
        for (ColumnEntity column : this.columnMap.values()) {
            if (column.isId()) {
                this.f1212id = column;
                return;
            }
        }
    }

    public T createEntity() throws Throwable {
        return this.constructor.newInstance(new Object[0]);
    }

    public boolean tableIsExist() throws DbException {
        if (isCheckedDatabase()) {
            return true;
        }
        Cursor cursor = this.f1211db.execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + this.name + "'");
        if (cursor != null) {
            try {
                if (!cursor.moveToNext() || cursor.getInt(0) <= 0) {
                    IOUtil.closeQuietly(cursor);
                } else {
                    setCheckedDatabase(true);
                    IOUtil.closeQuietly(cursor);
                    return true;
                }
            } catch (Throwable th) {
                IOUtil.closeQuietly(cursor);
                throw th;
            }
        }
        return false;
    }

    public DbManager getDb() {
        return this.f1211db;
    }

    public String getName() {
        return this.name;
    }

    public Class<T> getEntityType() {
        return this.entityType;
    }

    public String getOnCreated() {
        return this.onCreated;
    }

    public ColumnEntity getId() {
        return this.f1212id;
    }

    public LinkedHashMap<String, ColumnEntity> getColumnMap() {
        return this.columnMap;
    }

    /* access modifiers changed from: package-private */
    public boolean isCheckedDatabase() {
        return this.checkedDatabase;
    }

    /* access modifiers changed from: package-private */
    public void setCheckedDatabase(boolean checkedDatabase2) {
        this.checkedDatabase = checkedDatabase2;
    }

    public String toString() {
        return this.name;
    }
}
