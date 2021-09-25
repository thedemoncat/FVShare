package org.xutils.p018db.converter;

import android.database.Cursor;
import org.xutils.p018db.sqlite.ColumnDbType;

/* renamed from: org.xutils.db.converter.ColumnConverter */
public interface ColumnConverter<T> {
    Object fieldValue2DbValue(T t);

    ColumnDbType getColumnDbType();

    T getFieldValue(Cursor cursor, int i);
}
