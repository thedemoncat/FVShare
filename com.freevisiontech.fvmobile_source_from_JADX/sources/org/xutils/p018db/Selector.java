package org.xutils.p018db;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;
import org.xutils.common.util.IOUtil;
import org.xutils.p018db.sqlite.WhereBuilder;
import org.xutils.p018db.table.DbModel;
import org.xutils.p018db.table.TableEntity;
import org.xutils.p019ex.DbException;

/* renamed from: org.xutils.db.Selector */
public final class Selector<T> {
    private int limit = 0;
    private int offset = 0;
    private List<OrderBy> orderByList;
    private final TableEntity<T> table;
    private WhereBuilder whereBuilder;

    private Selector(TableEntity<T> table2) {
        this.table = table2;
    }

    static <T> Selector<T> from(TableEntity<T> table2) {
        return new Selector<>(table2);
    }

    public Selector<T> where(WhereBuilder whereBuilder2) {
        this.whereBuilder = whereBuilder2;
        return this;
    }

    public Selector<T> where(String columnName, String op, Object value) {
        this.whereBuilder = WhereBuilder.m1578b(columnName, op, value);
        return this;
    }

    public Selector<T> and(String columnName, String op, Object value) {
        this.whereBuilder.and(columnName, op, value);
        return this;
    }

    public Selector<T> and(WhereBuilder where) {
        this.whereBuilder.and(where);
        return this;
    }

    /* renamed from: or */
    public Selector<T> mo20860or(String columnName, String op, Object value) {
        this.whereBuilder.mo20902or(columnName, op, value);
        return this;
    }

    /* renamed from: or */
    public Selector mo20861or(WhereBuilder where) {
        this.whereBuilder.mo20903or(where);
        return this;
    }

    public Selector<T> expr(String expr) {
        if (this.whereBuilder == null) {
            this.whereBuilder = WhereBuilder.m1577b();
        }
        this.whereBuilder.expr(expr);
        return this;
    }

    public DbModelSelector groupBy(String columnName) {
        return new DbModelSelector((Selector<?>) this, columnName);
    }

    public DbModelSelector select(String... columnExpressions) {
        return new DbModelSelector((Selector<?>) this, columnExpressions);
    }

    public Selector<T> orderBy(String columnName) {
        if (this.orderByList == null) {
            this.orderByList = new ArrayList(5);
        }
        this.orderByList.add(new OrderBy(columnName));
        return this;
    }

    public Selector<T> orderBy(String columnName, boolean desc) {
        if (this.orderByList == null) {
            this.orderByList = new ArrayList(5);
        }
        this.orderByList.add(new OrderBy(columnName, desc));
        return this;
    }

    public Selector<T> limit(int limit2) {
        this.limit = limit2;
        return this;
    }

    public Selector<T> offset(int offset2) {
        this.offset = offset2;
        return this;
    }

    public TableEntity<T> getTable() {
        return this.table;
    }

    public WhereBuilder getWhereBuilder() {
        return this.whereBuilder;
    }

    public List<OrderBy> getOrderByList() {
        return this.orderByList;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public T findFirst() throws DbException {
        T t = null;
        if (this.table.tableIsExist()) {
            limit(1);
            Cursor cursor = this.table.getDb().execQuery(toString());
            if (cursor != null) {
                try {
                    if (cursor.moveToNext()) {
                        t = CursorUtils.getEntity(this.table, cursor);
                        IOUtil.closeQuietly(cursor);
                    } else {
                        IOUtil.closeQuietly(cursor);
                    }
                } catch (Throwable th) {
                    IOUtil.closeQuietly(cursor);
                    throw th;
                }
            }
        }
        return t;
    }

    public List<T> findAll() throws DbException {
        Cursor cursor;
        if (!this.table.tableIsExist() || (cursor = this.table.getDb().execQuery(toString())) == null) {
            return null;
        }
        try {
            ArrayList arrayList = new ArrayList();
            while (cursor.moveToNext()) {
                try {
                    arrayList.add(CursorUtils.getEntity(this.table, cursor));
                } catch (Throwable th) {
                    th = th;
                    ArrayList arrayList2 = arrayList;
                    IOUtil.closeQuietly(cursor);
                    throw th;
                }
            }
            IOUtil.closeQuietly(cursor);
            return arrayList;
        } catch (Throwable th2) {
            e = th2;
            throw new DbException(e);
        }
    }

    public long count() throws DbException {
        if (!this.table.tableIsExist()) {
            return 0;
        }
        DbModel firstModel = select("count(\"" + this.table.getId().getName() + "\") as count").findFirst();
        if (firstModel != null) {
            return firstModel.getLong("count");
        }
        return 0;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ");
        result.append(Marker.ANY_MARKER);
        result.append(" FROM ").append("\"").append(this.table.getName()).append("\"");
        if (this.whereBuilder != null && this.whereBuilder.getWhereItemSize() > 0) {
            result.append(" WHERE ").append(this.whereBuilder.toString());
        }
        if (this.orderByList != null && this.orderByList.size() > 0) {
            result.append(" ORDER BY ");
            for (OrderBy orderBy : this.orderByList) {
                result.append(orderBy.toString()).append(',');
            }
            result.deleteCharAt(result.length() - 1);
        }
        if (this.limit > 0) {
            result.append(" LIMIT ").append(this.limit);
            result.append(" OFFSET ").append(this.offset);
        }
        return result.toString();
    }

    /* renamed from: org.xutils.db.Selector$OrderBy */
    public static class OrderBy {
        private String columnName;
        private boolean desc;

        public OrderBy(String columnName2) {
            this.columnName = columnName2;
        }

        public OrderBy(String columnName2, boolean desc2) {
            this.columnName = columnName2;
            this.desc = desc2;
        }

        public String toString() {
            return "\"" + this.columnName + "\"" + (this.desc ? " DESC" : " ASC");
        }
    }
}
