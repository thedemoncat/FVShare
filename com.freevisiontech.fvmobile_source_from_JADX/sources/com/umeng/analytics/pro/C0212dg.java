package com.umeng.analytics.pro;

import com.umeng.analytics.pro.C0191cx;

/* renamed from: com.umeng.analytics.pro.dg */
/* compiled from: TProtocolUtil */
public class C0212dg {

    /* renamed from: a */
    private static int f707a = Integer.MAX_VALUE;

    /* renamed from: a */
    public static void m1196a(int i) {
        f707a = i;
    }

    /* renamed from: a */
    public static void m1197a(C0209dd ddVar, byte b) throws C0172ck {
        m1198a(ddVar, b, f707a);
    }

    /* renamed from: a */
    public static void m1198a(C0209dd ddVar, byte b, int i) throws C0172ck {
        int i2 = 0;
        if (i <= 0) {
            throw new C0172ck("Maximum skip depth exceeded");
        }
        switch (b) {
            case 2:
                ddVar.mo612t();
                return;
            case 3:
                ddVar.mo613u();
                return;
            case 4:
                ddVar.mo617y();
                return;
            case 6:
                ddVar.mo614v();
                return;
            case 8:
                ddVar.mo615w();
                return;
            case 10:
                ddVar.mo616x();
                return;
            case 11:
                ddVar.mo575A();
                return;
            case 12:
                ddVar.mo602j();
                while (true) {
                    C0194cy l = ddVar.mo604l();
                    if (l.f652b == 0) {
                        ddVar.mo603k();
                        return;
                    } else {
                        m1198a(ddVar, l.f652b, i - 1);
                        ddVar.mo605m();
                    }
                }
            case 13:
                C0206da n = ddVar.mo606n();
                while (i2 < n.f690c) {
                    m1198a(ddVar, n.f688a, i - 1);
                    m1198a(ddVar, n.f689b, i - 1);
                    i2++;
                }
                ddVar.mo607o();
                return;
            case 14:
                C0213dh r = ddVar.mo610r();
                while (i2 < r.f709b) {
                    m1198a(ddVar, r.f708a, i - 1);
                    i2++;
                }
                ddVar.mo611s();
                return;
            case 15:
                C0195cz p = ddVar.mo608p();
                while (i2 < p.f655b) {
                    m1198a(ddVar, p.f654a, i - 1);
                    i2++;
                }
                ddVar.mo609q();
                return;
            default:
                return;
        }
    }

    /* renamed from: a */
    public static C0211df m1195a(byte[] bArr, C0211df dfVar) {
        if (bArr[0] > 16) {
            return new C0191cx.C0192a();
        }
        if (bArr.length <= 1 || (bArr[1] & 128) == 0) {
            return dfVar;
        }
        return new C0191cx.C0192a();
    }
}
