package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.BooleanColumnConverter */
public class BooleanColumnConverter implements ColumnConverter<Boolean> {
    public Boolean getFieldValue(Cursor cursor, int index) {
        boolean z = true;
        if (cursor.isNull(index)) {
            return null;
        }
        if (cursor.getInt(index) != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public Object fieldValue2DbValue(Boolean fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Integer.valueOf(fieldValue.booleanValue() ? 1 : 0);
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
