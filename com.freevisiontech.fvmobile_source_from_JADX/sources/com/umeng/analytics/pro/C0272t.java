package com.umeng.analytics.pro;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteOpenHelper;
import com.umeng.analytics.pro.C0262s;

/* renamed from: com.umeng.analytics.pro.t */
/* compiled from: UMDBCreater */
class C0272t extends SQLiteOpenHelper {
    /* access modifiers changed from: private */

    /* renamed from: b */
    public static Context f852b = null;

    /* renamed from: a */
    private String f853a;

    /* renamed from: com.umeng.analytics.pro.t$a */
    /* compiled from: UMDBCreater */
    private static class C0274a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0272t f854a = new C0272t(C0272t.f852b, C0276v.m1394a(C0272t.f852b), C0262s.f815c, (SQLiteDatabase.CursorFactory) null, 1);

        private C0274a() {
        }
    }

    /* renamed from: a */
    public static synchronized C0272t m1383a(Context context) {
        C0272t a;
        synchronized (C0272t.class) {
            f852b = context;
            a = C0274a.f854a;
        }
        return a;
    }

    private C0272t(Context context, String str, String str2, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        this(new C0261r(context, str), str2, cursorFactory, i);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private C0272t(android.content.Context r2, java.lang.String r3, android.database.sqlite.SQLiteDatabase.CursorFactory r4, int r5) {
        /*
            r1 = this;
            if (r3 == 0) goto L_0x000b
            java.lang.String r0 = ""
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x000e
        L_0x000b:
            java.lang.String r3 = "ua.db"
        L_0x000e:
            r1.<init>(r2, r3, r4, r5)
            r0 = 0
            r1.f853a = r0
            r1.mo735a()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0272t.<init>(android.content.Context, java.lang.String, android.database.sqlite.SQLiteDatabase$CursorFactory, int):void");
    }

    /* renamed from: a */
    public void mo735a() {
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (!C0276v.m1397a(C0262s.C0269c.f835a, writableDatabase)) {
                m1387c(writableDatabase);
            }
            if (!C0276v.m1397a(C0262s.C0266b.f826a, writableDatabase)) {
                m1386b(writableDatabase);
            }
            if (!C0276v.m1397a(C0262s.C0263a.f819a, writableDatabase)) {
                m1384a(writableDatabase);
            }
        } catch (Exception e) {
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.beginTransaction();
            m1387c(sQLiteDatabase);
            m1386b(sQLiteDatabase);
            m1384a(sQLiteDatabase);
            sQLiteDatabase.setTransactionSuccessful();
            if (sQLiteDatabase != null) {
                try {
                    sQLiteDatabase.endTransaction();
                } catch (Throwable th) {
                }
            }
        } catch (SQLiteDatabaseCorruptException e) {
            C0276v.m1399b(f852b);
            if (sQLiteDatabase != null) {
                sQLiteDatabase.endTransaction();
            }
        } catch (Throwable th2) {
        }
    }

    /* renamed from: a */
    private void m1384a(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f853a = "create table if not exists __er(id INTEGER primary key autoincrement, __i TEXT, __a TEXT, __t INTEGER)";
            sQLiteDatabase.execSQL(this.f853a);
        } catch (SQLException e) {
        }
    }

    /* renamed from: b */
    private void m1386b(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f853a = "create table if not exists __et(id INTEGER primary key autoincrement, __i TEXT, __e TEXT, __s TEXT, __t INTEGER)";
            sQLiteDatabase.execSQL(this.f853a);
        } catch (SQLException e) {
        }
    }

    /* renamed from: c */
    private void m1387c(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f853a = "create table if not exists __sd(id INTEGER primary key autoincrement, __ii TEXT unique, __a TEXT, __b TEXT, __c TEXT, __d TEXT, __e TEXT, __f TEXT, __g TEXT)";
            sQLiteDatabase.execSQL(this.f853a);
        } catch (SQLException e) {
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
