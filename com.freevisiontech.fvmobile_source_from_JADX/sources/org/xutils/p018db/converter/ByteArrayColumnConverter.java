package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.ByteArrayColumnConverter */
public class ByteArrayColumnConverter implements ColumnConverter<byte[]> {
    public byte[] getFieldValue(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return cursor.getBlob(index);
    }

    public Object fieldValue2DbValue(byte[] fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.BLOB;
    }
}
