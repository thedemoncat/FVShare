package com.umeng.analytics.pro;

import android.content.Context;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.pro.C0037af;

/* renamed from: com.umeng.analytics.pro.bg */
/* compiled from: ImLatent */
public class C0076bg implements C0066ay {

    /* renamed from: l */
    private static C0076bg f311l = null;

    /* renamed from: a */
    private final long f312a = 1296000000;

    /* renamed from: b */
    private final long f313b = 129600000;

    /* renamed from: c */
    private final int f314c = 1800000;

    /* renamed from: d */
    private final int f315d = 10000;

    /* renamed from: e */
    private C0155ca f316e;

    /* renamed from: f */
    private C0072bc f317f;

    /* renamed from: g */
    private long f318g = 1296000000;

    /* renamed from: h */
    private int f319h = 10000;

    /* renamed from: i */
    private long f320i = 0;

    /* renamed from: j */
    private long f321j = 0;

    /* renamed from: k */
    private Context f322k;

    /* renamed from: a */
    public static synchronized C0076bg m357a(Context context, C0072bc bcVar) {
        C0076bg bgVar;
        synchronized (C0076bg.class) {
            if (f311l == null) {
                f311l = new C0076bg(context, bcVar);
                f311l.mo177a(C0037af.m144a(context).mo140b());
            }
            bgVar = f311l;
        }
        return bgVar;
    }

    private C0076bg(Context context, C0072bc bcVar) {
        this.f322k = context;
        this.f316e = C0155ca.m887a(context);
        this.f317f = bcVar;
    }

    /* renamed from: a */
    public boolean mo245a() {
        if (this.f316e.mo511h() || this.f317f.mo220f()) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis() - this.f317f.mo227m();
        if (currentTimeMillis > this.f318g) {
            this.f320i = (long) C0133br.m751a(this.f319h, C0031aa.m112a(this.f322k));
            this.f321j = currentTimeMillis;
            return true;
        } else if (currentTimeMillis <= 129600000) {
            return false;
        } else {
            this.f320i = 0;
            this.f321j = currentTimeMillis;
            return true;
        }
    }

    /* renamed from: b */
    public long mo246b() {
        return this.f320i;
    }

    /* renamed from: c */
    public long mo247c() {
        return this.f321j;
    }

    /* renamed from: a */
    public void mo177a(C0037af.C0038a aVar) {
        this.f318g = aVar.mo146a(1296000000);
        int b = aVar.mo151b(0);
        if (b != 0) {
            this.f319h = b;
        } else if (AnalyticsConfig.sLatentWindow <= 0 || AnalyticsConfig.sLatentWindow > 1800000) {
            this.f319h = 10000;
        } else {
            this.f319h = AnalyticsConfig.sLatentWindow;
        }
    }
}
