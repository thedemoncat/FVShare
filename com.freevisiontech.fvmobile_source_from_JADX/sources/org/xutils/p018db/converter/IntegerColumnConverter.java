package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.IntegerColumnConverter */
public class IntegerColumnConverter implements ColumnConverter<Integer> {
    public Integer getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Integer.valueOf(cursor.getInt(index));
    }

    public Object fieldValue2DbValue(Integer fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
