package com.umeng.analytics.pro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* renamed from: com.umeng.analytics.pro.v */
/* compiled from: UMDBUtils */
class C0276v {
    C0276v() {
    }

    /* renamed from: a */
    public static boolean m1397a(String str, SQLiteDatabase sQLiteDatabase) {
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

    /* renamed from: a */
    public static String m1394a(Context context) {
        return "/data/data/" + context.getPackageName() + C0262s.f814b;
    }

    /* renamed from: a */
    public static String m1395a(List<String> list) {
        return TextUtils.join("!", list);
    }

    /* renamed from: a */
    public static List<String> m1396a(String str) {
        return new ArrayList(Arrays.asList(str.split("!")));
    }

    /* renamed from: b */
    public static List<String> m1398b(List<String> list) {
        ArrayList arrayList = new ArrayList();
        try {
            for (String next : list) {
                if (Collections.frequency(arrayList, next) < 1) {
                    arrayList.add(next);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void m1399b(android.content.Context r3) {
        /*
            if (r3 != 0) goto L_0x0003
        L_0x0002:
            return
        L_0x0003:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            r1.<init>()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.String r2 = "/data/data/"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.String r2 = r3.getPackageName()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.String r2 = "/databases/"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.String r2 = "ua.db"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            if (r0 == 0) goto L_0x0039
            boolean r1 = r0.exists()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            if (r1 == 0) goto L_0x0039
            r0.delete()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
        L_0x0039:
            com.umeng.analytics.pro.t r0 = com.umeng.analytics.pro.C0272t.m1383a((android.content.Context) r3)     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            r0.mo735a()     // Catch:{ Throwable -> 0x0041, all -> 0x0043 }
            goto L_0x0002
        L_0x0041:
            r0 = move-exception
            goto L_0x0002
        L_0x0043:
            r0 = move-exception
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0276v.m1399b(android.content.Context):void");
    }
}
