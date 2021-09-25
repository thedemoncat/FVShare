package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.ShortColumnConverter */
public class ShortColumnConverter implements ColumnConverter<Short> {
    public Short getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Short.valueOf(cursor.getShort(index));
    }

    public Object fieldValue2DbValue(Short fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
