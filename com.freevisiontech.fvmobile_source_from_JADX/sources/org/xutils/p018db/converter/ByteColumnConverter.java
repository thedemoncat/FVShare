package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.ByteColumnConverter */
public class ByteColumnConverter implements ColumnConverter<Byte> {
    public Byte getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return Byte.valueOf((byte) cursor.getInt(index));
    }

    public Object fieldValue2DbValue(Byte fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
