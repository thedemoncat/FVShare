package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.CharColumnConverter */
public class CharColumnConverter implements ColumnConverter<Character> {
    public Character getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Character.valueOf((char) cursor.getInt(index));
    }

    public Object fieldValue2DbValue(Character fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Integer.valueOf(fieldValue.charValue());
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
