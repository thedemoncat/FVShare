package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.DoubleColumnConverter */
public class DoubleColumnConverter implements ColumnConverter<Double> {
    public Double getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Double.valueOf(cursor.getDouble(index));
    }

    public Object fieldValue2DbValue(Double fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
