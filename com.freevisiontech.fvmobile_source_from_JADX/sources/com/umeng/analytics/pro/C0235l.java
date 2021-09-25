package com.umeng.analytics.pro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.umeng.analytics.pro.l */
/* compiled from: UMCCVerbatimObject */
public class C0235l implements Serializable {

    /* renamed from: a */
    private static final long f765a = 1;

    /* renamed from: b */
    private List<String> f766b = new ArrayList();

    /* renamed from: c */
    private String f767c;

    /* renamed from: d */
    private long f768d;

    /* renamed from: e */
    private long f769e;

    /* renamed from: f */
    private String f770f;

    public C0235l(List<String> list, long j, String str, long j2) {
        this.f766b = list;
        this.f768d = j;
        this.f767c = str;
        this.f769e = j2;
        m1287f();
    }

    /* renamed from: f */
    private void m1287f() {
        this.f770f = C0260q.m1377a(this.f769e);
    }

    /* renamed from: a */
    public List<String> mo692a() {
        return this.f766b;
    }

    /* renamed from: b */
    public String mo693b() {
        return this.f767c;
    }

    /* renamed from: c */
    public long mo694c() {
        return this.f768d;
    }

    /* renamed from: d */
    public long mo695d() {
        return this.f769e;
    }

    /* renamed from: e */
    public String mo696e() {
        return this.f770f;
    }
}
