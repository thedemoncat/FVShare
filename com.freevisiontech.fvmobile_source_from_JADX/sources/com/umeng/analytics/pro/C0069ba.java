package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.C0015a;
import com.umeng.analytics.pro.C0155ca;
import com.umeng.analytics.pro.C0189cw;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.ba */
/* compiled from: Sender */
public class C0069ba {

    /* renamed from: a */
    private static final int f256a = 1;

    /* renamed from: b */
    private static final int f257b = 2;

    /* renamed from: c */
    private static final int f258c = 3;

    /* renamed from: g */
    private static Context f259g;

    /* renamed from: d */
    private C0034ad f260d;

    /* renamed from: e */
    private C0037af f261e;

    /* renamed from: f */
    private final int f262f = 1;
    /* access modifiers changed from: private */

    /* renamed from: h */
    public C0072bc f263h;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public C0063av f264i;

    /* renamed from: j */
    private JSONObject f265j;

    /* renamed from: k */
    private boolean f266k = false;
    /* access modifiers changed from: private */

    /* renamed from: l */
    public boolean f267l;

    public C0069ba(Context context, C0072bc bcVar) {
        this.f260d = C0034ad.m126a(context);
        this.f261e = C0037af.m144a(context);
        f259g = context;
        this.f263h = bcVar;
        this.f264i = new C0063av(context);
        this.f264i.mo200a((C0062au) this.f263h);
    }

    /* renamed from: a */
    public void mo207a(JSONObject jSONObject) {
        this.f265j = jSONObject;
    }

    /* renamed from: a */
    public void mo208a(boolean z) {
        this.f266k = z;
    }

    /* renamed from: b */
    public void mo209b(boolean z) {
        this.f267l = z;
    }

    /* renamed from: a */
    public void mo206a(C0066ay ayVar) {
        this.f261e.mo138a(ayVar);
    }

    /* renamed from: a */
    public void mo205a() {
        try {
            if (this.f265j != null) {
                m298c();
            } else {
                m295b();
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: b */
    private void m295b() {
        C0155ca.m887a(f259g).mo512i().mo513a((C0155ca.C0160b) new C0155ca.C0160b() {
            /* renamed from: a */
            public void mo210a(File file) {
            }

            /* renamed from: b */
            public boolean mo211b(File file) {
                FileInputStream fileInputStream;
                byte[] b;
                int a;
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        b = C0136bu.m820b((InputStream) fileInputStream);
                    } catch (Throwable th) {
                        th = th;
                    }
                    try {
                        C0136bu.m821c(fileInputStream);
                        byte[] a2 = C0069ba.this.f264i.mo201a(b);
                        if (a2 == null) {
                            a = 1;
                        } else {
                            a = C0069ba.this.m293a(a2);
                        }
                        if (!C0069ba.this.f267l && a == 1) {
                            return false;
                        }
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = null;
                    C0136bu.m821c(fileInputStream);
                    throw th;
                }
            }

            /* renamed from: c */
            public void mo212c(File file) {
                C0069ba.this.f263h.mo225k();
            }
        });
    }

    /* renamed from: c */
    private void m298c() {
        C0031aa b;
        int a;
        try {
            this.f260d.mo123a();
            try {
                String encodeToString = Base64.encodeToString(new C0175cn().mo544a(this.f260d.mo126b()), 0);
                if (!TextUtils.isEmpty(encodeToString)) {
                    JSONObject jSONObject = this.f265j.getJSONObject(C0015a.f10A);
                    jSONObject.put(C0281x.f889O, encodeToString);
                    this.f265j.put(C0015a.f10A, jSONObject);
                }
            } catch (Exception e) {
            }
            byte[] bytes = String.valueOf(this.f265j).getBytes();
            if (bytes != null && !C0133br.m754a(f259g, bytes)) {
                if (!this.f266k) {
                    b = C0031aa.m111a(f259g, AnalyticsConfig.getAppkey(f259g), bytes);
                } else {
                    b = C0031aa.m114b(f259g, AnalyticsConfig.getAppkey(f259g), bytes);
                }
                byte[] c = b.mo120c();
                C0155ca.m887a(f259g).mo510g();
                byte[] a2 = this.f264i.mo201a(c);
                if (a2 == null) {
                    a = 1;
                } else {
                    a = m293a(a2);
                }
                switch (a) {
                    case 1:
                        if (!this.f267l) {
                            C0155ca.m887a(f259g).mo501a(c);
                            return;
                        }
                        return;
                    case 2:
                        this.f260d.mo128d();
                        this.f263h.mo225k();
                        return;
                    case 3:
                        this.f263h.mo225k();
                        return;
                    default:
                        return;
                }
            }
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public int m293a(byte[] bArr) {
        C0113bn bnVar = new C0113bn();
        try {
            new C0169ch(new C0189cw.C0190a()).mo533a((C0164ce) bnVar, bArr);
            if (bnVar.f436a == 1) {
                this.f261e.mo141b(bnVar.mo410i());
                this.f261e.mo143d();
            }
            C0138bw.m837c("send log:" + bnVar.mo407f());
        } catch (Throwable th) {
        }
        if (bnVar.f436a == 1) {
            return 2;
        }
        return 3;
    }
}
