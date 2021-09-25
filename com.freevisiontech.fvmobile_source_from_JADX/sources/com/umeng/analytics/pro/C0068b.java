package com.umeng.analytics.pro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.concurrent.atomic.AtomicInteger;

/* renamed from: com.umeng.analytics.pro.b */
/* compiled from: DatabaseManager */
public class C0068b {

    /* renamed from: c */
    private static C0068b f251c;

    /* renamed from: d */
    private static SQLiteOpenHelper f252d;

    /* renamed from: a */
    private AtomicInteger f253a = new AtomicInteger();

    /* renamed from: b */
    private AtomicInteger f254b = new AtomicInteger();

    /* renamed from: e */
    private SQLiteDatabase f255e;

    /* renamed from: b */
    private static synchronized void m288b(Context context) {
        synchronized (C0068b.class) {
            if (f251c == null) {
                f251c = new C0068b();
                f252d = C0152c.m880a(context);
            }
        }
    }

    /* renamed from: a */
    public static synchronized C0068b m287a(Context context) {
        C0068b bVar;
        synchronized (C0068b.class) {
            if (f251c == null) {
                m288b(context);
            }
            bVar = f251c;
        }
        return bVar;
    }

    /* renamed from: a */
    public synchronized SQLiteDatabase mo202a() {
        if (this.f253a.incrementAndGet() == 1) {
            this.f255e = f252d.getReadableDatabase();
        }
        return this.f255e;
    }

    /* renamed from: b */
    public synchronized SQLiteDatabase mo203b() {
        if (this.f253a.incrementAndGet() == 1) {
            this.f255e = f252d.getWritableDatabase();
        }
        return this.f255e;
    }

    /* renamed from: c */
    public synchronized void mo204c() {
        if (this.f253a.decrementAndGet() == 0) {
            this.f255e.close();
        }
        if (this.f254b.decrementAndGet() == 0) {
            this.f255e.close();
        }
    }
}
