package com.umeng.analytics.pro;

import java.io.Serializable;

/* renamed from: com.umeng.analytics.pro.k */
/* compiled from: UMCCSystemBuffer */
public class C0234k implements Serializable {

    /* renamed from: a */
    private static final long f760a = 1;

    /* renamed from: b */
    private String f761b;

    /* renamed from: c */
    private long f762c;

    /* renamed from: d */
    private long f763d;

    /* renamed from: e */
    private String f764e;

    private C0234k() {
        this.f761b = null;
        this.f762c = 0;
        this.f763d = 0;
        this.f764e = null;
    }

    public C0234k(String str, long j, long j2) {
        this(str, j, j2, (String) null);
    }

    public C0234k(String str, long j, long j2, String str2) {
        this.f761b = null;
        this.f762c = 0;
        this.f763d = 0;
        this.f764e = null;
        this.f761b = str;
        this.f762c = j;
        this.f763d = j2;
        this.f764e = str2;
    }

    /* renamed from: a */
    public C0234k mo684a() {
        this.f763d++;
        return this;
    }

    /* renamed from: b */
    public String mo687b() {
        return this.f764e;
    }

    /* renamed from: a */
    public void mo686a(String str) {
        this.f764e = str;
    }

    /* renamed from: c */
    public String mo689c() {
        return this.f761b;
    }

    /* renamed from: b */
    public void mo688b(String str) {
        this.f761b = str;
    }

    /* renamed from: d */
    public long mo690d() {
        return this.f762c;
    }

    /* renamed from: e */
    public long mo691e() {
        return this.f763d;
    }

    /* renamed from: a */
    public C0234k mo685a(C0234k kVar) {
        this.f763d = kVar.mo691e() + this.f763d;
        this.f762c = kVar.mo690d();
        return this;
    }
}
