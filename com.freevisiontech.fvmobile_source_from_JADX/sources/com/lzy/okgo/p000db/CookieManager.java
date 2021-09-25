package com.lzy.okgo.p000db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.lzy.okgo.cookie.SerializableCookie;

/* renamed from: com.lzy.okgo.db.CookieManager */
public class CookieManager extends BaseDao<SerializableCookie> {
    private static Context context;
    private static volatile CookieManager instance;

    public static CookieManager getInstance() {
        if (instance == null) {
            synchronized (CookieManager.class) {
                if (instance == null) {
                    instance = new CookieManager();
                }
            }
        }
        return instance;
    }

    private CookieManager() {
        super(new DBHelper(context));
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    public SerializableCookie parseCursorToBean(Cursor cursor) {
        return SerializableCookie.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(SerializableCookie serializableCookie) {
        return SerializableCookie.getContentValues(serializableCookie);
    }

    public String getTableName() {
        return SerializableCookie.COOKIE;
    }

    public void unInit() {
    }
}
