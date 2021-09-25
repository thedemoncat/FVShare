package com.umeng.analytics.social;

/* renamed from: com.umeng.analytics.social.a */
/* compiled from: UMException */
public class C0297a extends RuntimeException {

    /* renamed from: b */
    private static final long f988b = -4656673116019167471L;

    /* renamed from: a */
    protected int f989a = 5000;

    /* renamed from: c */
    private String f990c = "";

    /* renamed from: a */
    public int mo783a() {
        return this.f989a;
    }

    public C0297a(int i, String str) {
        super(str);
        this.f989a = i;
        this.f990c = str;
    }

    public C0297a(String str, Throwable th) {
        super(str, th);
        this.f990c = str;
    }

    public C0297a(String str) {
        super(str);
        this.f990c = str;
    }

    public String getMessage() {
        return this.f990c;
    }
}
