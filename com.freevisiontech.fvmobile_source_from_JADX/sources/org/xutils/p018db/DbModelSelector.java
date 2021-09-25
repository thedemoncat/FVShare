package org.xutils.p018db;

import android.database.Cursor;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;
import org.xutils.common.util.IOUtil;
import org.xutils.p018db.Selector;
import org.xutils.p018db.sqlite.WhereBuilder;
import org.xutils.p018db.table.DbModel;
import org.xutils.p018db.table.TableEntity;
import org.xutils.p019ex.DbException;

/* renamed from: org.xutils.db.DbModelSelector */
public final class DbModelSelector {
    private String[] columnExpressions;
    private String groupByColumnName;
    private WhereBuilder having;
    private Selector<?> selector;

    private DbModelSelector(TableEntity<?> table) {
        this.selector = Selector.from(table);
    }

    protected DbModelSelector(Selector<?> selector2, String groupByColumnName2) {
        this.selector = selector2;
        this.groupByColumnName = groupByColumnName2;
    }

    protected DbModelSelector(Selector<?> selector2, String[] columnExpressions2) {
        this.selector = selector2;
        this.columnExpressions = columnExpressions2;
    }

    static DbModelSelector from(TableEntity<?> table) {
        return new DbModelSelector(table);
    }

    public DbModelSelector where(WhereBuilder whereBuilder) {
        this.selector.where(whereBuilder);
        return this;
    }

    public DbModelSelector where(String columnName, String op, Object value) {
        this.selector.where(columnName, op, value);
        return this;
    }

    public DbModelSelector and(String columnName, String op, Object value) {
        this.selector.and(columnName, op, value);
        return this;
    }

    public DbModelSelector and(WhereBuilder where) {
        this.selector.and(where);
        return this;
    }

    /* renamed from: or */
    public DbModelSelector mo20838or(String columnName, String op, Object value) {
        this.selector.mo20860or(columnName, op, value);
        return this;
    }

    /* renamed from: or */
    public DbModelSelector mo20839or(WhereBuilder where) {
        this.selector.mo20861or(where);
        return this;
    }

    public DbModelSelector expr(String expr) {
        this.selector.expr(expr);
        return this;
    }

    public DbModelSelector groupBy(String columnName) {
        this.groupByColumnName = columnName;
        return this;
    }

    public DbModelSelector having(WhereBuilder whereBuilder) {
        this.having = whereBuilder;
        return this;
    }

    public DbModelSelector select(String... columnExpressions2) {
        this.columnExpressions = columnExpressions2;
        return this;
    }

    public DbModelSelector orderBy(String columnName) {
        this.selector.orderBy(columnName);
        return this;
    }

    public DbModelSelector orderBy(String columnName, boolean desc) {
        this.selector.orderBy(columnName, desc);
        return this;
    }

    public DbModelSelector limit(int limit) {
        this.selector.limit(limit);
        return this;
    }

    public DbModelSelector offset(int offset) {
        this.selector.offset(offset);
        return this;
    }

    public TableEntity<?> getTable() {
        return this.selector.getTable();
    }

    public DbModel findFirst() throws DbException {
        DbModel dbModel = null;
        TableEntity<?> table = this.selector.getTable();
        if (table.tableIsExist()) {
            limit(1);
            Cursor cursor = table.getDb().execQuery(toString());
            if (cursor != null) {
                try {
                    if (cursor.moveToNext()) {
                        dbModel = CursorUtils.getDbModel(cursor);
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
        return dbModel;
    }

    public List<DbModel> findAll() throws DbException {
        Cursor cursor;
        TableEntity<?> table = this.selector.getTable();
        if (!table.tableIsExist() || (cursor = table.getDb().execQuery(toString())) == null) {
            return null;
        }
        try {
            ArrayList arrayList = new ArrayList();
            while (cursor.moveToNext()) {
                try {
                    arrayList.add(CursorUtils.getDbModel(cursor));
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

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SELECT ");
        if (this.columnExpressions != null && this.columnExpressions.length > 0) {
            for (String columnExpression : this.columnExpressions) {
                result.append(columnExpression);
                result.append(",");
            }
            result.deleteCharAt(result.length() - 1);
        } else if (!TextUtils.isEmpty(this.groupByColumnName)) {
            result.append(this.groupByColumnName);
        } else {
            result.append(Marker.ANY_MARKER);
        }
        result.append(" FROM ").append("\"").append(this.selector.getTable().getName()).append("\"");
        WhereBuilder whereBuilder = this.selector.getWhereBuilder();
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            result.append(" WHERE ").append(whereBuilder.toString());
        }
        if (!TextUtils.isEmpty(this.groupByColumnName)) {
            result.append(" GROUP BY ").append("\"").append(this.groupByColumnName).append("\"");
            if (this.having != null && this.having.getWhereItemSize() > 0) {
                result.append(" HAVING ").append(this.having.toString());
            }
        }
        List<Selector.OrderBy> orderByList = this.selector.getOrderByList();
        if (orderByList != null && orderByList.size() > 0) {
            for (int i = 0; i < orderByList.size(); i++) {
                result.append(" ORDER BY ").append(orderByList.get(i).toString()).append(',');
            }
            result.deleteCharAt(result.length() - 1);
        }
        if (this.selector.getLimit() > 0) {
            result.append(" LIMIT ").append(this.selector.getLimit());
            result.append(" OFFSET ").append(this.selector.getOffset());
        }
        return result.toString();
    }
}
