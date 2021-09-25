package org.xutils.p018db.converter;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.xutils.common.util.LogUtil;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.ColumnConverterFactory */
public final class ColumnConverterFactory {
    private static final ConcurrentHashMap<String, ColumnConverter> columnType_columnConverter_map = new ConcurrentHashMap<>();

    private ColumnConverterFactory() {
    }

    public static ColumnConverter getColumnConverter(Class columnType) {
        ColumnConverter result = null;
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            result = columnType_columnConverter_map.get(columnType.getName());
        } else if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                result = columnConverter;
            } catch (Throwable ex) {
                LogUtil.m1565e(ex.getMessage(), ex);
            }
        }
        if (result != null) {
            return result;
        }
        throw new RuntimeException("Database Column Not Support: " + columnType.getName() + ", please impl ColumnConverter or use ColumnConverterFactory#registerColumnConverter(...)");
    }

    public static ColumnDbType getDbColumnType(Class columnType) {
        return getColumnConverter(columnType).getColumnDbType();
    }

    public static void registerColumnConverter(Class columnType, ColumnConverter columnConverter) {
        columnType_columnConverter_map.put(columnType.getName(), columnConverter);
    }

    public static boolean isSupportColumnConverter(Class columnType) {
        if (columnType_columnConverter_map.containsKey(columnType.getName())) {
            return true;
        }
        if (ColumnConverter.class.isAssignableFrom(columnType)) {
            try {
                ColumnConverter columnConverter = (ColumnConverter) columnType.newInstance();
                if (columnConverter != null) {
                    columnType_columnConverter_map.put(columnType.getName(), columnConverter);
                }
                if (columnConverter != null) {
                    return false;
                }
                return true;
            } catch (Throwable th) {
            }
        }
        return false;
    }

    static {
        BooleanColumnConverter booleanColumnConverter = new BooleanColumnConverter();
        columnType_columnConverter_map.put(Boolean.TYPE.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(Boolean.class.getName(), booleanColumnConverter);
        columnType_columnConverter_map.put(byte[].class.getName(), new ByteArrayColumnConverter());
        ByteColumnConverter byteColumnConverter = new ByteColumnConverter();
        columnType_columnConverter_map.put(Byte.TYPE.getName(), byteColumnConverter);
        columnType_columnConverter_map.put(Byte.class.getName(), byteColumnConverter);
        CharColumnConverter charColumnConverter = new CharColumnConverter();
        columnType_columnConverter_map.put(Character.TYPE.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Character.class.getName(), charColumnConverter);
        columnType_columnConverter_map.put(Date.class.getName(), new DateColumnConverter());
        DoubleColumnConverter doubleColumnConverter = new DoubleColumnConverter();
        columnType_columnConverter_map.put(Double.TYPE.getName(), doubleColumnConverter);
        columnType_columnConverter_map.put(Double.class.getName(), doubleColumnConverter);
        FloatColumnConverter floatColumnConverter = new FloatColumnConverter();
        columnType_columnConverter_map.put(Float.TYPE.getName(), floatColumnConverter);
        columnType_columnConverter_map.put(Float.class.getName(), floatColumnConverter);
        IntegerColumnConverter integerColumnConverter = new IntegerColumnConverter();
        columnType_columnConverter_map.put(Integer.TYPE.getName(), integerColumnConverter);
        columnType_columnConverter_map.put(Integer.class.getName(), integerColumnConverter);
        LongColumnConverter longColumnConverter = new LongColumnConverter();
        columnType_columnConverter_map.put(Long.TYPE.getName(), longColumnConverter);
        columnType_columnConverter_map.put(Long.class.getName(), longColumnConverter);
        ShortColumnConverter shortColumnConverter = new ShortColumnConverter();
        columnType_columnConverter_map.put(Short.TYPE.getName(), shortColumnConverter);
        columnType_columnConverter_map.put(Short.class.getName(), shortColumnConverter);
        columnType_columnConverter_map.put(java.sql.Date.class.getName(), new SqlDateColumnConverter());
        columnType_columnConverter_map.put(String.class.getName(), new StringColumnConverter());
    }
}
