package org.xutils.p018db.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;
import org.xutils.common.util.KeyValue;
import org.xutils.p018db.converter.ColumnConverterFactory;
import org.xutils.p018db.table.ColumnUtils;

/* renamed from: org.xutils.db.sqlite.SqlInfo */
public final class SqlInfo {
    private List<KeyValue> bindArgs;
    private String sql;

    public SqlInfo() {
    }

    public SqlInfo(String sql2) {
        this.sql = sql2;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql2) {
        this.sql = sql2;
    }

    public void addBindArg(KeyValue kv) {
        if (this.bindArgs == null) {
            this.bindArgs = new ArrayList();
        }
        this.bindArgs.add(kv);
    }

    public void addBindArgs(List<KeyValue> bindArgs2) {
        if (this.bindArgs == null) {
            this.bindArgs = bindArgs2;
        } else {
            this.bindArgs.addAll(bindArgs2);
        }
    }

    public SQLiteStatement buildStatement(SQLiteDatabase database) {
        SQLiteStatement result = database.compileStatement(this.sql);
        if (this.bindArgs != null) {
            for (int i = 1; i < this.bindArgs.size() + 1; i++) {
                Object value = ColumnUtils.convert2DbValueIfNeeded(this.bindArgs.get(i - 1).value);
                if (value != null) {
                    switch (ColumnConverterFactory.getColumnConverter(value.getClass()).getColumnDbType()) {
                        case INTEGER:
                            result.bindLong(i, ((Number) value).longValue());
                            break;
                        case REAL:
                            result.bindDouble(i, ((Number) value).doubleValue());
                            break;
                        case TEXT:
                            result.bindString(i, value.toString());
                            break;
                        case BLOB:
                            result.bindBlob(i, (byte[]) value);
                            break;
                        default:
                            result.bindNull(i);
                            break;
                    }
                } else {
                    result.bindNull(i);
                }
            }
        }
        return result;
    }

    public Object[] getBindArgs() {
        Object[] result = null;
        if (this.bindArgs != null) {
            result = new Object[this.bindArgs.size()];
            for (int i = 0; i < this.bindArgs.size(); i++) {
                result[i] = ColumnUtils.convert2DbValueIfNeeded(this.bindArgs.get(i).value);
            }
        }
        return result;
    }

    public String[] getBindArgsAsStrArray() {
        String[] result = null;
        if (this.bindArgs != null) {
            result = new String[this.bindArgs.size()];
            for (int i = 0; i < this.bindArgs.size(); i++) {
                Object value = ColumnUtils.convert2DbValueIfNeeded(this.bindArgs.get(i).value);
                result[i] = value == null ? null : value.toString();
            }
        }
        return result;
    }
}
