package com.umeng.analytics.pro;

import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

/* renamed from: com.umeng.analytics.pro.cd */
/* compiled from: TApplicationException */
public class C0163cd extends C0172ck {

    /* renamed from: a */
    public static final int f563a = 0;

    /* renamed from: b */
    public static final int f564b = 1;

    /* renamed from: c */
    public static final int f565c = 2;

    /* renamed from: d */
    public static final int f566d = 3;

    /* renamed from: e */
    public static final int f567e = 4;

    /* renamed from: f */
    public static final int f568f = 5;

    /* renamed from: g */
    public static final int f569g = 6;

    /* renamed from: h */
    public static final int f570h = 7;

    /* renamed from: j */
    private static final C0214di f571j = new C0214di("TApplicationException");

    /* renamed from: k */
    private static final C0194cy f572k = new C0194cy("message", (byte) 11, 1);

    /* renamed from: l */
    private static final C0194cy f573l = new C0194cy(IjkMediaMeta.IJKM_KEY_TYPE, (byte) 8, 2);

    /* renamed from: m */
    private static final long f574m = 1;

    /* renamed from: i */
    protected int f575i = 0;

    public C0163cd() {
    }

    public C0163cd(int i) {
        this.f575i = i;
    }

    public C0163cd(int i, String str) {
        super(str);
        this.f575i = i;
    }

    public C0163cd(String str) {
        super(str);
    }

    /* renamed from: a */
    public int mo525a() {
        return this.f575i;
    }

    /* renamed from: a */
    public static C0163cd m937a(C0209dd ddVar) throws C0172ck {
        ddVar.mo602j();
        String str = null;
        int i = 0;
        while (true) {
            C0194cy l = ddVar.mo604l();
            if (l.f652b == 0) {
                ddVar.mo603k();
                return new C0163cd(i, str);
            }
            switch (l.f653c) {
                case 1:
                    if (l.f652b != 11) {
                        C0212dg.m1197a(ddVar, l.f652b);
                        break;
                    } else {
                        str = ddVar.mo618z();
                        break;
                    }
                case 2:
                    if (l.f652b != 8) {
                        C0212dg.m1197a(ddVar, l.f652b);
                        break;
                    } else {
                        i = ddVar.mo615w();
                        break;
                    }
                default:
                    C0212dg.m1197a(ddVar, l.f652b);
                    break;
            }
            ddVar.mo605m();
        }
    }

    /* renamed from: b */
    public void mo526b(C0209dd ddVar) throws C0172ck {
        ddVar.mo586a(f571j);
        if (getMessage() != null) {
            ddVar.mo581a(f572k);
            ddVar.mo587a(getMessage());
            ddVar.mo593c();
        }
        ddVar.mo581a(f573l);
        ddVar.mo579a(this.f575i);
        ddVar.mo593c();
        ddVar.mo595d();
        ddVar.mo592b();
    }
}
