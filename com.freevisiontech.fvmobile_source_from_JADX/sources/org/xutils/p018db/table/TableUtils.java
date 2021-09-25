package org.xutils.p018db.table;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.xutils.common.util.LogUtil;
import org.xutils.p018db.annotation.Column;
import org.xutils.p018db.converter.ColumnConverterFactory;

/* renamed from: org.xutils.db.table.TableUtils */
final class TableUtils {
    private TableUtils() {
    }

    static synchronized LinkedHashMap<String, ColumnEntity> findColumnMap(Class<?> entityType) {
        LinkedHashMap<String, ColumnEntity> columnMap;
        synchronized (TableUtils.class) {
            columnMap = new LinkedHashMap<>();
            addColumns2Map(entityType, columnMap);
        }
        return columnMap;
    }

    private static void addColumns2Map(Class<?> entityType, HashMap<String, ColumnEntity> columnMap) {
        Column columnAnn;
        if (!Object.class.equals(entityType)) {
            try {
                for (Field field : entityType.getDeclaredFields()) {
                    int modify = field.getModifiers();
                    if (!Modifier.isStatic(modify) && !Modifier.isTransient(modify) && (columnAnn = (Column) field.getAnnotation(Column.class)) != null && ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
                        ColumnEntity column = new ColumnEntity(entityType, field, columnAnn);
                        if (!columnMap.containsKey(column.getName())) {
                            columnMap.put(column.getName(), column);
                        }
                    }
                }
                addColumns2Map(entityType.getSuperclass(), columnMap);
            } catch (Throwable e) {
                LogUtil.m1565e(e.getMessage(), e);
            }
        }
    }
}
