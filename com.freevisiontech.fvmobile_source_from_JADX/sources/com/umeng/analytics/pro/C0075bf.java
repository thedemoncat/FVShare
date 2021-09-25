package com.umeng.analytics.pro;

import android.content.Context;
import com.umeng.analytics.pro.C0037af;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.bf */
/* compiled from: Defcon */
public class C0075bf implements C0066ay {

    /* renamed from: a */
    private static final int f301a = 0;

    /* renamed from: b */
    private static final int f302b = 1;

    /* renamed from: c */
    private static final int f303c = 2;

    /* renamed from: d */
    private static final int f304d = 3;

    /* renamed from: e */
    private static final long f305e = 14400000;

    /* renamed from: f */
    private static final long f306f = 28800000;

    /* renamed from: g */
    private static final long f307g = 86400000;

    /* renamed from: j */
    private static C0075bf f308j = null;

    /* renamed from: h */
    private int f309h = 0;

    /* renamed from: i */
    private final long f310i = 60000;

    /* renamed from: a */
    public static synchronized C0075bf m349a(Context context) {
        C0075bf bfVar;
        synchronized (C0075bf.class) {
            if (f308j == null) {
                f308j = new C0075bf();
                f308j.mo240a(C0037af.m144a(context).mo140b().mo145a(0));
            }
            bfVar = f308j;
        }
        return bfVar;
    }

    private C0075bf() {
    }

    /* renamed from: a */
    public void mo241a(JSONObject jSONObject, Context context) {
        if (this.f309h == 1) {
            jSONObject.remove("error");
            jSONObject.remove(C0281x.f911aJ);
            jSONObject.remove(C0281x.f912aK);
            jSONObject.remove(C0281x.f939au);
            C0277w.m1400a(context).mo743a(false, true);
            C0236m.m1296a(context).mo702b(new C0228f());
        } else if (this.f309h == 2) {
            jSONObject.remove(C0281x.f895U);
            try {
                jSONObject.put(C0281x.f895U, mo239a());
            } catch (Exception e) {
            }
            jSONObject.remove("error");
            jSONObject.remove(C0281x.f911aJ);
            jSONObject.remove(C0281x.f912aK);
            jSONObject.remove(C0281x.f939au);
            C0277w.m1400a(context).mo743a(false, true);
            C0236m.m1296a(context).mo702b(new C0228f());
        } else if (this.f309h == 3) {
            jSONObject.remove(C0281x.f895U);
            jSONObject.remove("error");
            jSONObject.remove(C0281x.f911aJ);
            jSONObject.remove(C0281x.f912aK);
            jSONObject.remove(C0281x.f939au);
            C0277w.m1400a(context).mo743a(false, true);
            C0236m.m1296a(context).mo702b(new C0228f());
        }
    }

    /* renamed from: a */
    public JSONObject mo239a() {
        JSONObject jSONObject = new JSONObject();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            jSONObject.put("id", C0071bb.m307a());
            jSONObject.put(C0281x.f897W, currentTimeMillis);
            jSONObject.put(C0281x.f898X, currentTimeMillis + 60000);
            jSONObject.put("duration", 60000);
        } catch (Throwable th) {
        }
        return jSONObject;
    }

    /* renamed from: b */
    public long mo242b() {
        switch (this.f309h) {
            case 1:
                return f305e;
            case 2:
                return f306f;
            case 3:
                return 86400000;
            default:
                return 0;
        }
    }

    /* renamed from: c */
    public long mo243c() {
        return this.f309h == 0 ? 0 : 300000;
    }

    /* renamed from: a */
    public void mo240a(int i) {
        if (i >= 0 && i <= 3) {
            this.f309h = i;
        }
    }

    /* renamed from: d */
    public boolean mo244d() {
        return this.f309h != 0;
    }

    /* renamed from: a */
    public void mo177a(C0037af.C0038a aVar) {
        mo240a(aVar.mo145a(0));
    }
}
