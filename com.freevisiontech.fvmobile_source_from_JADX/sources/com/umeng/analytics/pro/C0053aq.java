package com.umeng.analytics.pro;

import android.content.Context;

/* renamed from: com.umeng.analytics.pro.aq */
/* compiled from: CacheService */
public final class C0053aq implements C0061at {

    /* renamed from: c */
    private static C0053aq f224c;
    /* access modifiers changed from: private */

    /* renamed from: a */
    public C0061at f225a = new C0050ap(this.f226b);

    /* renamed from: b */
    private Context f226b;

    private C0053aq(Context context) {
        this.f226b = context;
    }

    /* renamed from: a */
    public synchronized C0050ap mo185a(Context context) {
        return (C0050ap) this.f225a;
    }

    /* renamed from: b */
    public static synchronized C0053aq m237b(Context context) {
        C0053aq aqVar;
        synchronized (C0053aq.class) {
            if (f224c == null && context != null) {
                f224c = new C0053aq(context);
            }
            aqVar = f224c;
        }
        return aqVar;
    }

    /* renamed from: a */
    public void mo186a(C0061at atVar) {
        this.f225a = atVar;
    }

    /* renamed from: a */
    public void mo178a(final Object obj) {
        C0139bx.m859b(new C0151bz() {
            /* renamed from: a */
            public void mo87a() {
                C0053aq.this.f225a.mo178a(obj);
            }
        });
    }

    /* renamed from: a */
    public void mo175a() {
        C0139bx.m859b(new C0151bz() {
            /* renamed from: a */
            public void mo87a() {
                C0053aq.this.f225a.mo175a();
            }
        });
    }

    /* renamed from: b */
    public void mo180b() {
        C0139bx.m860c(new C0151bz() {
            /* renamed from: a */
            public void mo87a() {
                C0053aq.this.f225a.mo180b();
            }
        });
    }
}
