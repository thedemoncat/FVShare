package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.StringColumnConverter */
public class StringColumnConverter implements ColumnConverter<String> {
    public String getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return cursor.getString(index);
    }

    public Object fieldValue2DbValue(String fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
