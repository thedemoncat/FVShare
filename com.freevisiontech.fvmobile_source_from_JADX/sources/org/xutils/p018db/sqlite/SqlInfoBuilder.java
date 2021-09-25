package org.xutils.p018db.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.xutils.common.util.KeyValue;
import org.xutils.p018db.table.ColumnEntity;
import org.xutils.p018db.table.TableEntity;
import org.xutils.p019ex.DbException;

/* renamed from: org.xutils.db.sqlite.SqlInfoBuilder */
public final class SqlInfoBuilder {
    private static final ConcurrentHashMap<TableEntity<?>, String> INSERT_SQL_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<TableEntity<?>, String> REPLACE_SQL_CACHE = new ConcurrentHashMap<>();

    private SqlInfoBuilder() {
    }

    public static SqlInfo buildInsertSqlInfo(TableEntity<?> table, Object entity) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(table, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        SqlInfo result = new SqlInfo();
        String sql = INSERT_SQL_CACHE.get(table);
        if (sql == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO ");
            builder.append("\"").append(table.getName()).append("\"");
            builder.append(" (");
            for (KeyValue kv : keyValueList) {
                builder.append("\"").append(kv.key).append("\"").append(',');
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(") VALUES (");
            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                builder.append("?,");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            String sql2 = builder.toString();
            result.setSql(sql2);
            result.addBindArgs(keyValueList);
            INSERT_SQL_CACHE.put(table, sql2);
            return result;
        }
        result.setSql(sql);
        result.addBindArgs(keyValueList);
        return result;
    }

    public static SqlInfo buildReplaceSqlInfo(TableEntity<?> table, Object entity) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(table, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        SqlInfo result = new SqlInfo();
        String sql = REPLACE_SQL_CACHE.get(table);
        if (sql == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("REPLACE INTO ");
            builder.append("\"").append(table.getName()).append("\"");
            builder.append(" (");
            for (KeyValue kv : keyValueList) {
                builder.append("\"").append(kv.key).append("\"").append(',');
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(") VALUES (");
            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                builder.append("?,");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append(")");
            String sql2 = builder.toString();
            result.setSql(sql2);
            result.addBindArgs(keyValueList);
            REPLACE_SQL_CACHE.put(table, sql2);
            return result;
        }
        result.setSql(sql);
        result.addBindArgs(keyValueList);
        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(TableEntity<?> table, Object entity) throws DbException {
        SqlInfo result = new SqlInfo();
        ColumnEntity id = table.getId();
        Object idValue = id.getColumnValue(entity);
        if (idValue == null) {
            throw new DbException("this entity[" + table.getEntityType() + "]'s id value is null");
        }
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append("\"").append(table.getName()).append("\"");
        builder.append(" WHERE ").append(WhereBuilder.m1578b(id.getName(), "=", idValue));
        result.setSql(builder.toString());
        return result;
    }

    public static SqlInfo buildDeleteSqlInfoById(TableEntity<?> table, Object idValue) throws DbException {
        SqlInfo result = new SqlInfo();
        ColumnEntity id = table.getId();
        if (idValue == null) {
            throw new DbException("this entity[" + table.getEntityType() + "]'s id value is null");
        }
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append("\"").append(table.getName()).append("\"");
        builder.append(" WHERE ").append(WhereBuilder.m1578b(id.getName(), "=", idValue));
        result.setSql(builder.toString());
        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(TableEntity<?> table, WhereBuilder whereBuilder) throws DbException {
        StringBuilder builder = new StringBuilder("DELETE FROM ");
        builder.append("\"").append(table.getName()).append("\"");
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            builder.append(" WHERE ").append(whereBuilder.toString());
        }
        return new SqlInfo(builder.toString());
    }

    public static SqlInfo buildUpdateSqlInfo(TableEntity<?> table, Object entity, String... updateColumnNames) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(table, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        HashSet<String> updateColumnNameSet = null;
        if (updateColumnNames != null && updateColumnNames.length > 0) {
            updateColumnNameSet = new HashSet<>(updateColumnNames.length);
            Collections.addAll(updateColumnNameSet, updateColumnNames);
        }
        ColumnEntity id = table.getId();
        Object idValue = id.getColumnValue(entity);
        if (idValue == null) {
            throw new DbException("this entity[" + table.getEntityType() + "]'s id value is null");
        }
        SqlInfo result = new SqlInfo();
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append("\"").append(table.getName()).append("\"");
        builder.append(" SET ");
        for (KeyValue kv : keyValueList) {
            if (updateColumnNameSet == null || updateColumnNameSet.contains(kv.key)) {
                builder.append("\"").append(kv.key).append("\"").append("=?,");
                result.addBindArg(kv);
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" WHERE ").append(WhereBuilder.m1578b(id.getName(), "=", idValue));
        result.setSql(builder.toString());
        return result;
    }

    public static SqlInfo buildUpdateSqlInfo(TableEntity<?> table, WhereBuilder whereBuilder, KeyValue... nameValuePairs) throws DbException {
        if (nameValuePairs == null || nameValuePairs.length == 0) {
            return null;
        }
        SqlInfo result = new SqlInfo();
        StringBuilder builder = new StringBuilder("UPDATE ");
        builder.append("\"").append(table.getName()).append("\"");
        builder.append(" SET ");
        for (KeyValue kv : nameValuePairs) {
            builder.append("\"").append(kv.key).append("\"").append("=?,");
            result.addBindArg(kv);
        }
        builder.deleteCharAt(builder.length() - 1);
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            builder.append(" WHERE ").append(whereBuilder.toString());
        }
        result.setSql(builder.toString());
        return result;
    }

    public static SqlInfo buildCreateTableSqlInfo(TableEntity<?> table) throws DbException {
        ColumnEntity id = table.getId();
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append("\"").append(table.getName()).append("\"");
        builder.append(" ( ");
        if (id.isAutoId()) {
            builder.append("\"").append(id.getName()).append("\"").append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        } else {
            builder.append("\"").append(id.getName()).append("\"").append(id.getColumnDbType()).append(" PRIMARY KEY, ");
        }
        for (ColumnEntity column : table.getColumnMap().values()) {
            if (!column.isId()) {
                builder.append("\"").append(column.getName()).append("\"");
                builder.append(' ').append(column.getColumnDbType());
                builder.append(' ').append(column.getProperty());
                builder.append(',');
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" )");
        return new SqlInfo(builder.toString());
    }

    public static List<KeyValue> entity2KeyValueList(TableEntity<?> table, Object entity) {
        Collection<ColumnEntity> columns = table.getColumnMap().values();
        List<KeyValue> keyValueList = new ArrayList<>(columns.size());
        for (ColumnEntity column : columns) {
            KeyValue kv = column2KeyValue(entity, column);
            if (kv != null) {
                keyValueList.add(kv);
            }
        }
        return keyValueList;
    }

    private static KeyValue column2KeyValue(Object entity, ColumnEntity column) {
        if (column.isAutoId()) {
            return null;
        }
        return new KeyValue(column.getName(), column.getFieldValue(entity));
    }
}
