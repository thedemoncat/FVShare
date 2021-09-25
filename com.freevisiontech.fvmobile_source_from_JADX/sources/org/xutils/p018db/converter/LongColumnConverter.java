package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.LongColumnConverter */
public class LongColumnConverter implements ColumnConverter<Long> {
    public Long getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Long.valueOf(cursor.getLong(index));
    }

    public Object fieldValue2DbValue(Long fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
