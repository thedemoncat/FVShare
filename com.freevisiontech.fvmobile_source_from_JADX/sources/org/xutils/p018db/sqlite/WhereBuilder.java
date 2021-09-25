package org.xutils.p018db.sqlite;

import android.text.TextUtils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xutils.p018db.converter.ColumnConverterFactory;
import org.xutils.p018db.table.ColumnUtils;

/* renamed from: org.xutils.db.sqlite.WhereBuilder */
public class WhereBuilder {
    private final List<String> whereItems = new ArrayList();

    private WhereBuilder() {
    }

    /* renamed from: b */
    public static WhereBuilder m1577b() {
        return new WhereBuilder();
    }

    /* renamed from: b */
    public static WhereBuilder m1578b(String columnName, String op, Object value) {
        WhereBuilder result = new WhereBuilder();
        result.appendCondition((String) null, columnName, op, value);
        return result;
    }

    public WhereBuilder and(String columnName, String op, Object value) {
        appendCondition(this.whereItems.size() == 0 ? null : "AND", columnName, op, value);
        return this;
    }

    public WhereBuilder and(WhereBuilder where) {
        return expr((this.whereItems.size() == 0 ? " " : "AND ") + "(" + where.toString() + ")");
    }

    /* renamed from: or */
    public WhereBuilder mo20902or(String columnName, String op, Object value) {
        appendCondition(this.whereItems.size() == 0 ? null : "OR", columnName, op, value);
        return this;
    }

    /* renamed from: or */
    public WhereBuilder mo20903or(WhereBuilder where) {
        return expr((this.whereItems.size() == 0 ? " " : "OR ") + "(" + where.toString() + ")");
    }

    public WhereBuilder expr(String expr) {
        this.whereItems.add(" " + expr);
        return this;
    }

    public int getWhereItemSize() {
        return this.whereItems.size();
    }

    public String toString() {
        if (this.whereItems.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : this.whereItems) {
            sb.append(item);
        }
        return sb.toString();
    }

    private void appendCondition(String conj, String columnName, String op, Object value) {
        StringBuilder builder = new StringBuilder();
        if (this.whereItems.size() > 0) {
            builder.append(" ");
        }
        if (!TextUtils.isEmpty(conj)) {
            builder.append(conj).append(" ");
        }
        builder.append("\"").append(columnName).append("\"");
        if ("!=".equals(op)) {
            op = "<>";
        } else if ("==".equals(op)) {
            op = "=";
        }
        if (value != null) {
            builder.append(" ").append(op).append(" ");
            if ("IN".equalsIgnoreCase(op)) {
                Iterable<?> items = null;
                if (value instanceof Iterable) {
                    items = (Iterable) value;
                } else if (value.getClass().isArray()) {
                    int len = Array.getLength(value);
                    List<Object> arrayList = new ArrayList<>(len);
                    for (int i = 0; i < len; i++) {
                        arrayList.add(Array.get(value, i));
                    }
                    items = arrayList;
                }
                if (items != null) {
                    StringBuilder inSb = new StringBuilder("(");
                    for (Object item : items) {
                        Object itemColValue = ColumnUtils.convert2DbValueIfNeeded(item);
                        if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(itemColValue.getClass()))) {
                            String valueStr = itemColValue.toString();
                            if (valueStr.indexOf(39) != -1) {
                                valueStr = valueStr.replace("'", "''");
                            }
                            inSb.append("'").append(valueStr).append("'");
                        } else {
                            inSb.append(itemColValue);
                        }
                        inSb.append(",");
                    }
                    inSb.deleteCharAt(inSb.length() - 1);
                    inSb.append(")");
                    builder.append(inSb.toString());
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else if ("BETWEEN".equalsIgnoreCase(op)) {
                Iterable<?> items2 = null;
                if (value instanceof Iterable) {
                    items2 = (Iterable) value;
                } else if (value.getClass().isArray()) {
                    int len2 = Array.getLength(value);
                    List<Object> arrayList2 = new ArrayList<>(len2);
                    for (int i2 = 0; i2 < len2; i2++) {
                        arrayList2.add(Array.get(value, i2));
                    }
                    items2 = arrayList2;
                }
                if (items2 != null) {
                    Iterator<?> iterator = items2.iterator();
                    if (!iterator.hasNext()) {
                        throw new IllegalArgumentException("value must have tow items.");
                    }
                    Object start = iterator.next();
                    if (!iterator.hasNext()) {
                        throw new IllegalArgumentException("value must have tow items.");
                    }
                    Object end = iterator.next();
                    Object startColValue = ColumnUtils.convert2DbValueIfNeeded(start);
                    Object endColValue = ColumnUtils.convert2DbValueIfNeeded(end);
                    if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(startColValue.getClass()))) {
                        String startStr = startColValue.toString();
                        if (startStr.indexOf(39) != -1) {
                            startStr = startStr.replace("'", "''");
                        }
                        String endStr = endColValue.toString();
                        if (endStr.indexOf(39) != -1) {
                            endStr = endStr.replace("'", "''");
                        }
                        builder.append("'").append(startStr).append("'");
                        builder.append(" AND ");
                        builder.append("'").append(endStr).append("'");
                    } else {
                        builder.append(startColValue);
                        builder.append(" AND ");
                        builder.append(endColValue);
                    }
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else {
                Object value2 = ColumnUtils.convert2DbValueIfNeeded(value);
                if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(value2.getClass()))) {
                    String valueStr2 = value2.toString();
                    if (valueStr2.indexOf(39) != -1) {
                        valueStr2 = valueStr2.replace("'", "''");
                    }
                    builder.append("'").append(valueStr2).append("'");
                } else {
                    builder.append(value2);
                }
            }
        } else if ("=".equals(op)) {
            builder.append(" IS NULL");
        } else if ("<>".equals(op)) {
            builder.append(" IS NOT NULL");
        } else {
            builder.append(" ").append(op).append(" NULL");
        }
        this.whereItems.add(builder.toString());
    }
}
