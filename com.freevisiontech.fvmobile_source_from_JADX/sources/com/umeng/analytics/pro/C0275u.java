package com.umeng.analytics.pro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.concurrent.atomic.AtomicInteger;

/* renamed from: com.umeng.analytics.pro.u */
/* compiled from: UMDBManager */
class C0275u {

    /* renamed from: c */
    private static C0275u f855c;

    /* renamed from: d */
    private static SQLiteOpenHelper f856d;

    /* renamed from: a */
    private AtomicInteger f857a = new AtomicInteger();

    /* renamed from: b */
    private AtomicInteger f858b = new AtomicInteger();

    /* renamed from: e */
    private SQLiteDatabase f859e;

    C0275u() {
    }

    /* renamed from: b */
    private static synchronized void m1391b(Context context) {
        synchronized (C0275u.class) {
            if (f855c == null) {
                f855c = new C0275u();
                f856d = C0272t.m1383a(context);
            }
        }
    }

    /* renamed from: a */
    public static synchronized C0275u m1390a(Context context) {
        C0275u uVar;
        synchronized (C0275u.class) {
            if (f855c == null) {
                m1391b(context);
            }
            uVar = f855c;
        }
        return uVar;
    }

    /* renamed from: a */
    public synchronized SQLiteDatabase mo738a() {
        if (this.f857a.incrementAndGet() == 1) {
            this.f859e = f856d.getWritableDatabase();
        }
        return this.f859e;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void mo739b() {
        /*
            r1 = this;
            monitor-enter(r1)
            java.util.concurrent.atomic.AtomicInteger r0 = r1.f857a     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            int r0 = r0.decrementAndGet()     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            if (r0 != 0) goto L_0x000e
            android.database.sqlite.SQLiteDatabase r0 = r1.f859e     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            r0.close()     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
        L_0x000e:
            java.util.concurrent.atomic.AtomicInteger r0 = r1.f858b     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            int r0 = r0.decrementAndGet()     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            if (r0 != 0) goto L_0x001b
            android.database.sqlite.SQLiteDatabase r0 = r1.f859e     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
            r0.close()     // Catch:{ Throwable -> 0x0020, all -> 0x001d }
        L_0x001b:
            monitor-exit(r1)
            return
        L_0x001d:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        L_0x0020:
            r0 = move-exception
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0275u.mo739b():void");
    }
}
