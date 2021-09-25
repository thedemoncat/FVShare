package com.umeng.analytics.pro;

/* renamed from: com.umeng.analytics.pro.bh */
/* compiled from: Gender */
public enum C0077bh implements C0170ci {
    MALE(0),
    FEMALE(1),
    UNKNOWN(2);
    

    /* renamed from: d */
    private final int f327d;

    private C0077bh(int i) {
        this.f327d = i;
    }

    /* renamed from: a */
    public int mo248a() {
        return this.f327d;
    }

    /* renamed from: a */
    public static C0077bh m362a(int i) {
        switch (i) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
            case 2:
                return UNKNOWN;
            default:
                return null;
        }
    }
}
