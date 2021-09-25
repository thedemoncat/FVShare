package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.FloatColumnConverter */
public class FloatColumnConverter implements ColumnConverter<Float> {
    public Float getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Float.valueOf(cursor.getFloat(index));
    }

    public Object fieldValue2DbValue(Float fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.REAL;
    }
}
