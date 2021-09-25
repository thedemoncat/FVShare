package com.umeng.analytics.pro;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* renamed from: com.umeng.analytics.pro.c */
/* compiled from: UMCCDBHelper */
class C0152c extends SQLiteOpenHelper {
    /* access modifiers changed from: private */

    /* renamed from: b */
    public static Context f544b;

    /* renamed from: a */
    private String f545a;

    /* renamed from: com.umeng.analytics.pro.c$a */
    /* compiled from: UMCCDBHelper */
    private static class C0154a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0152c f546a = new C0152c(C0152c.f544b, C0196d.m1143a(C0152c.f544b), C0196d.f658c, (SQLiteDatabase.CursorFactory) null, 1);

        private C0154a() {
        }
    }

    /* renamed from: a */
    public static synchronized C0152c m880a(Context context) {
        C0152c a;
        synchronized (C0152c.class) {
            f544b = context;
            a = C0154a.f546a;
        }
        return a;
    }

    private C0152c(Context context, String str, String str2, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        this(new C0227e(context, str), str2, cursorFactory, i);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private C0152c(android.content.Context r2, java.lang.String r3, android.database.sqlite.SQLiteDatabase.CursorFactory r4, int r5) {
        /*
            r1 = this;
            if (r3 == 0) goto L_0x000b
            java.lang.String r0 = ""
            boolean r0 = r3.equals(r0)
            if (r0 == 0) goto L_0x000e
        L_0x000b:
            java.lang.String r3 = "cc.db"
        L_0x000e:
            r1.<init>(r2, r3, r4, r5)
            r1.m882b()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0152c.<init>(android.content.Context, java.lang.String, android.database.sqlite.SQLiteDatabase$CursorFactory, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m882b() {
        /*
            r2 = this;
            android.database.sqlite.SQLiteDatabase r0 = r2.getWritableDatabase()     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
            java.lang.String r1 = "aggregated"
            boolean r1 = r2.mo495a(r1, r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
            if (r1 == 0) goto L_0x0016
            java.lang.String r1 = "aggregated_cache"
            boolean r1 = r2.mo495a(r1, r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
            if (r1 != 0) goto L_0x0019
        L_0x0016:
            r2.m884c(r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
        L_0x0019:
            java.lang.String r1 = "system"
            boolean r1 = r2.mo495a(r1, r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
            if (r1 != 0) goto L_0x0025
            r2.m883b(r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
        L_0x0025:
            java.lang.String r1 = "limitedck"
            boolean r1 = r2.mo495a(r1, r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
            if (r1 != 0) goto L_0x0031
            r2.m881a((android.database.sqlite.SQLiteDatabase) r0)     // Catch:{ Exception -> 0x0034, all -> 0x0032 }
        L_0x0031:
            return
        L_0x0032:
            r0 = move-exception
            throw r0
        L_0x0034:
            r0 = move-exception
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0152c.m882b():void");
    }

    /* renamed from: a */
    public boolean mo495a(String str, SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = null;
        boolean z = false;
        if (str != null) {
            try {
                Cursor rawQuery = sQLiteDatabase.rawQuery("select count(*) as c from sqlite_master where type ='table' and name ='" + str.trim() + "' ", (String[]) null);
                if (rawQuery.moveToNext() && rawQuery.getInt(0) > 0) {
                    z = true;
                }
                if (rawQuery != null) {
                    rawQuery.close();
                }
            } catch (Exception e) {
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

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.beginTransaction();
            m884c(sQLiteDatabase);
            m883b(sQLiteDatabase);
            m881a(sQLiteDatabase);
            sQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sQLiteDatabase.endTransaction();
        }
    }

    /* renamed from: a */
    private boolean m881a(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f545a = "create table if not exists limitedck(Id INTEGER primary key autoincrement, ck TEXT unique)";
            sQLiteDatabase.execSQL(this.f545a);
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("create reference table error!");
            return false;
        }
    }

    /* renamed from: b */
    private boolean m883b(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f545a = "create table if not exists system(Id INTEGER primary key autoincrement, key TEXT, timeStamp INTEGER, count INTEGER)";
            sQLiteDatabase.execSQL(this.f545a);
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("create system table error!");
            return false;
        }
    }

    /* renamed from: c */
    private boolean m884c(SQLiteDatabase sQLiteDatabase) {
        try {
            this.f545a = "create table if not exists aggregated_cache(Id INTEGER primary key autoincrement, key TEXT, totalTimestamp TEXT, value INTEGER, count INTEGER, label TEXT, timeWindowNum TEXT)";
            sQLiteDatabase.execSQL(this.f545a);
            this.f545a = "create table if not exists aggregated(Id INTEGER primary key autoincrement, key TEXT, totalTimestamp TEXT, value INTEGER, count INTEGER, label TEXT, timeWindowNum TEXT)";
            sQLiteDatabase.execSQL(this.f545a);
            return true;
        } catch (SQLException e) {
            C0138bw.m849e("create aggregated table error!");
            return false;
        }
    }
}
