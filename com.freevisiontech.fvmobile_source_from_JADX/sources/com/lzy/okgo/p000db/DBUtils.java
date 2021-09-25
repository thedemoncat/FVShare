package com.lzy.okgo.p000db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lzy.okgo.utils.OkLogger;

/* renamed from: com.lzy.okgo.db.DBUtils */
public class DBUtils {
    public static boolean isNeedUpgradeTable(SQLiteDatabase db, TableEntity table) {
        if (!isTableExists(db, table.tableName)) {
            return true;
        }
        Cursor cursor = db.rawQuery("select * from " + table.tableName, (String[]) null);
        if (cursor == null) {
            return false;
        }
        try {
            int columnCount = table.getColumnCount();
            if (columnCount == cursor.getColumnCount()) {
                for (int i = 0; i < columnCount; i++) {
                    if (table.getColumnIndex(cursor.getColumnName(i)) == -1) {
                        return true;
                    }
                }
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } finally {
            cursor.close();
        }
    }

    public static boolean isTableExists(SQLiteDatabase db, String tableName) {
        boolean z = true;
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
                if (cursor != null) {
                    cursor.close();
                }
                if (count <= 0) {
                    z = false;
                }
                return z;
            } else if (cursor == null) {
                return false;
            } else {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            OkLogger.printStackTrace(e);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static boolean isFieldExists(SQLiteDatabase db, String tableName, String fieldName) {
        boolean z = false;
        if (!(tableName == null || db == null || fieldName == null || !db.isOpen())) {
            Cursor cursor = null;
            try {
                Cursor cursor2 = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", (String[]) null);
                if (!(cursor2 == null || cursor2.getColumnIndex(fieldName) == -1)) {
                    z = true;
                }
                if (cursor2 != null) {
                    cursor2.close();
                }
            } catch (Exception e) {
                OkLogger.printStackTrace(e);
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        return z;
    }
}
