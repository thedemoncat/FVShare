package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;

/* renamed from: com.umeng.analytics.pro.bc */
/* compiled from: StatTracer */
public class C0072bc implements C0062au {

    /* renamed from: h */
    private static final String f276h = "successful_request";

    /* renamed from: i */
    private static final String f277i = "failed_requests ";

    /* renamed from: j */
    private static final String f278j = "last_request_spent_ms";

    /* renamed from: k */
    private static final String f279k = "last_request_time";

    /* renamed from: l */
    private static final String f280l = "first_activate_time";

    /* renamed from: m */
    private static final String f281m = "last_req";

    /* renamed from: a */
    public int f282a;

    /* renamed from: b */
    public int f283b;

    /* renamed from: c */
    public long f284c;

    /* renamed from: d */
    private final int f285d = 3600000;

    /* renamed from: e */
    private int f286e;

    /* renamed from: f */
    private long f287f = 0;

    /* renamed from: g */
    private long f288g = 0;

    /* renamed from: n */
    private Context f289n;

    public C0072bc(Context context) {
        m318a(context);
    }

    /* renamed from: a */
    private void m318a(Context context) {
        this.f289n = context.getApplicationContext();
        SharedPreferences a = C0067az.m285a(context);
        this.f282a = a.getInt(f276h, 0);
        this.f283b = a.getInt(f277i, 0);
        this.f286e = a.getInt(f278j, 0);
        this.f284c = a.getLong(f279k, 0);
        this.f287f = a.getLong(f281m, 0);
    }

    /* renamed from: e */
    public int mo219e() {
        if (this.f286e > 3600000) {
            return 3600000;
        }
        return this.f286e;
    }

    /* renamed from: f */
    public boolean mo220f() {
        boolean z;
        boolean z2;
        if (this.f284c == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!C0155ca.m887a(this.f289n).mo511h()) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    /* renamed from: g */
    public void mo221g() {
        this.f282a++;
        this.f284c = this.f287f;
    }

    /* renamed from: h */
    public void mo222h() {
        this.f283b++;
    }

    /* renamed from: i */
    public void mo223i() {
        this.f287f = System.currentTimeMillis();
    }

    /* renamed from: j */
    public void mo224j() {
        this.f286e = (int) (System.currentTimeMillis() - this.f287f);
    }

    /* renamed from: k */
    public void mo225k() {
        C0067az.m285a(this.f289n).edit().putInt(f276h, this.f282a).putInt(f277i, this.f283b).putInt(f278j, this.f286e).putLong(f279k, this.f284c).putLong(f281m, this.f287f).commit();
    }

    /* renamed from: l */
    public long mo226l() {
        SharedPreferences a = C0067az.m285a(this.f289n);
        this.f288g = C0067az.m285a(this.f289n).getLong(f280l, 0);
        if (this.f288g == 0) {
            this.f288g = System.currentTimeMillis();
            a.edit().putLong(f280l, this.f288g).commit();
        }
        return this.f288g;
    }

    /* renamed from: m */
    public long mo227m() {
        return this.f287f;
    }

    /* renamed from: a */
    public void mo196a() {
        mo223i();
    }

    /* renamed from: b */
    public void mo197b() {
        mo224j();
    }

    /* renamed from: c */
    public void mo198c() {
        mo221g();
    }

    /* renamed from: d */
    public void mo199d() {
        mo222h();
    }
}
