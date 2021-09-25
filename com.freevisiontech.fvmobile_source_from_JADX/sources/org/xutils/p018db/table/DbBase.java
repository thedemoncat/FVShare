package org.xutils.p018db.table;

import android.database.Cursor;
import android.text.TextUtils;
import java.util.HashMap;
import org.xutils.DbManager;
import org.xutils.common.util.IOUtil;
import org.xutils.p018db.sqlite.SqlInfoBuilder;
import org.xutils.p019ex.DbException;

/* renamed from: org.xutils.db.table.DbBase */
public abstract class DbBase implements DbManager {
    private final HashMap<Class<?>, TableEntity<?>> tableMap = new HashMap<>();

    public <T> TableEntity<T> getTable(Class<T> entityType) throws DbException {
        TableEntity<T> table;
        synchronized (this.tableMap) {
            table = this.tableMap.get(entityType);
            if (table == null) {
                try {
                    table = new TableEntity<>(this, entityType);
                    this.tableMap.put(entityType, table);
                } catch (Throwable ex) {
                    throw new DbException(ex);
                }
            }
        }
        return table;
    }

    public void dropTable(Class<?> entityType) throws DbException {
        TableEntity<?> table = getTable(entityType);
        if (table.tableIsExist()) {
            execNonQuery("DROP TABLE \"" + table.getName() + "\"");
            table.setCheckedDatabase(false);
            removeTable(entityType);
        }
    }

    public void dropDb() throws DbException {
        Cursor cursor = execQuery("SELECT name FROM sqlite_master WHERE type='table' AND name<>'sqlite_sequence'");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    execNonQuery("DROP TABLE " + cursor.getString(0));
                } catch (Throwable e) {
                    try {
                        throw new DbException(e);
                    } catch (Throwable th) {
                        IOUtil.closeQuietly(cursor);
                        throw th;
                    }
                }
            }
            synchronized (this.tableMap) {
                for (TableEntity<?> table : this.tableMap.values()) {
                    table.setCheckedDatabase(false);
                }
                this.tableMap.clear();
            }
            IOUtil.closeQuietly(cursor);
        }
    }

    public void addColumn(Class<?> entityType, String column) throws DbException {
        TableEntity<?> table = getTable(entityType);
        ColumnEntity col = table.getColumnMap().get(column);
        if (col != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("ALTER TABLE ").append("\"").append(table.getName()).append("\"").append(" ADD COLUMN ").append("\"").append(col.getName()).append("\"").append(" ").append(col.getColumnDbType()).append(" ").append(col.getProperty());
            execNonQuery(builder.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void createTableIfNotExist(TableEntity<?> table) throws DbException {
        if (!table.tableIsExist()) {
            synchronized (table.getClass()) {
                if (!table.tableIsExist()) {
                    execNonQuery(SqlInfoBuilder.buildCreateTableSqlInfo(table));
                    String execAfterTableCreated = table.getOnCreated();
                    if (!TextUtils.isEmpty(execAfterTableCreated)) {
                        execNonQuery(execAfterTableCreated);
                    }
                    table.setCheckedDatabase(true);
                    DbManager.TableCreateListener listener = getDaoConfig().getTableCreateListener();
                    if (listener != null) {
                        listener.onTableCreated(this, table);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeTable(Class<?> entityType) {
        synchronized (this.tableMap) {
            this.tableMap.remove(entityType);
        }
    }
}
