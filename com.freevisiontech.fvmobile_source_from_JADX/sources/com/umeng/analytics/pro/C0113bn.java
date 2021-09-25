package com.umeng.analytics.pro;

import android.support.p001v4.app.NotificationCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.bn */
/* compiled from: Response */
public class C0113bn implements C0164ce<C0113bn, C0119e>, Serializable, Cloneable {

    /* renamed from: d */
    public static final Map<C0119e, C0183cq> f428d;

    /* renamed from: e */
    private static final long f429e = -4549277923241195391L;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public static final C0214di f430f = new C0214di("Response");
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0194cy f431g = new C0194cy("resp_code", (byte) 8, 1);
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f432h = new C0194cy(NotificationCompat.CATEGORY_MESSAGE, (byte) 11, 2);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f433i = new C0194cy(C0281x.f888N, (byte) 12, 3);

    /* renamed from: j */
    private static final Map<Class<? extends C0218dl>, C0219dm> f434j = new HashMap();

    /* renamed from: k */
    private static final int f435k = 0;

    /* renamed from: a */
    public int f436a;

    /* renamed from: b */
    public String f437b;

    /* renamed from: c */
    public C0099bl f438c;

    /* renamed from: l */
    private byte f439l;

    /* renamed from: m */
    private C0119e[] f440m;

    static {
        f434j.put(C0220dn.class, new C0116b());
        f434j.put(C0221do.class, new C0118d());
        EnumMap enumMap = new EnumMap(C0119e.class);
        enumMap.put(C0119e.RESP_CODE, new C0183cq("resp_code", (byte) 1, new C0184cr((byte) 8)));
        enumMap.put(C0119e.MSG, new C0183cq(NotificationCompat.CATEGORY_MESSAGE, (byte) 2, new C0184cr((byte) 11)));
        enumMap.put(C0119e.IMPRINT, new C0183cq(C0281x.f888N, (byte) 2, new C0188cv((byte) 12, C0099bl.class)));
        f428d = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0113bn.class, f428d);
    }

    /* renamed from: com.umeng.analytics.pro.bn$e */
    /* compiled from: Response */
    public enum C0119e implements C0173cl {
        RESP_CODE(1, "resp_code"),
        MSG(2, NotificationCompat.CATEGORY_MESSAGE),
        IMPRINT(3, C0281x.f888N);
        

        /* renamed from: d */
        private static final Map<String, C0119e> f444d = null;

        /* renamed from: e */
        private final short f446e;

        /* renamed from: f */
        private final String f447f;

        static {
            f444d = new HashMap();
            Iterator it = EnumSet.allOf(C0119e.class).iterator();
            while (it.hasNext()) {
                C0119e eVar = (C0119e) it.next();
                f444d.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0119e m648a(int i) {
            switch (i) {
                case 1:
                    return RESP_CODE;
                case 2:
                    return MSG;
                case 3:
                    return IMPRINT;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0119e m650b(int i) {
            C0119e a = m648a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0119e m649a(String str) {
            return f444d.get(str);
        }

        private C0119e(short s, String str) {
            this.f446e = s;
            this.f447f = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f446e;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f447f;
        }
    }

    public C0113bn() {
        this.f439l = 0;
        this.f440m = new C0119e[]{C0119e.MSG, C0119e.IMPRINT};
    }

    public C0113bn(int i) {
        this();
        this.f436a = i;
        mo400a(true);
    }

    public C0113bn(C0113bn bnVar) {
        this.f439l = 0;
        this.f440m = new C0119e[]{C0119e.MSG, C0119e.IMPRINT};
        this.f439l = bnVar.f439l;
        this.f436a = bnVar.f436a;
        if (bnVar.mo409h()) {
            this.f437b = bnVar.f437b;
        }
        if (bnVar.mo412k()) {
            this.f438c = new C0099bl(bnVar.f438c);
        }
    }

    /* renamed from: a */
    public C0113bn mo276p() {
        return new C0113bn(this);
    }

    /* renamed from: b */
    public void mo257b() {
        mo400a(false);
        this.f436a = 0;
        this.f437b = null;
        this.f438c = null;
    }

    /* renamed from: c */
    public int mo402c() {
        return this.f436a;
    }

    /* renamed from: a */
    public C0113bn mo397a(int i) {
        this.f436a = i;
        mo400a(true);
        return this;
    }

    /* renamed from: d */
    public void mo405d() {
        this.f439l = C0161cb.m928b(this.f439l, 0);
    }

    /* renamed from: e */
    public boolean mo406e() {
        return C0161cb.m924a(this.f439l, 0);
    }

    /* renamed from: a */
    public void mo400a(boolean z) {
        this.f439l = C0161cb.m916a(this.f439l, 0, z);
    }

    /* renamed from: f */
    public String mo407f() {
        return this.f437b;
    }

    /* renamed from: a */
    public C0113bn mo399a(String str) {
        this.f437b = str;
        return this;
    }

    /* renamed from: g */
    public void mo408g() {
        this.f437b = null;
    }

    /* renamed from: h */
    public boolean mo409h() {
        return this.f437b != null;
    }

    /* renamed from: b */
    public void mo401b(boolean z) {
        if (!z) {
            this.f437b = null;
        }
    }

    /* renamed from: i */
    public C0099bl mo410i() {
        return this.f438c;
    }

    /* renamed from: a */
    public C0113bn mo398a(C0099bl blVar) {
        this.f438c = blVar;
        return this;
    }

    /* renamed from: j */
    public void mo411j() {
        this.f438c = null;
    }

    /* renamed from: k */
    public boolean mo412k() {
        return this.f438c != null;
    }

    /* renamed from: c */
    public void mo404c(boolean z) {
        if (!z) {
            this.f438c = null;
        }
    }

    /* renamed from: c */
    public C0119e mo256b(int i) {
        return C0119e.m648a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f434j.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f434j.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Response(");
        sb.append("resp_code:");
        sb.append(this.f436a);
        if (mo409h()) {
            sb.append(", ");
            sb.append("msg:");
            if (this.f437b == null) {
                sb.append("null");
            } else {
                sb.append(this.f437b);
            }
        }
        if (mo412k()) {
            sb.append(", ");
            sb.append("imprint:");
            if (this.f438c == null) {
                sb.append("null");
            } else {
                sb.append(this.f438c);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: l */
    public void mo413l() throws C0172ck {
        if (this.f438c != null) {
            this.f438c.mo363m();
        }
    }

    /* renamed from: a */
    private void m608a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m607a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f439l = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bn$b */
    /* compiled from: Response */
    private static class C0116b implements C0219dm {
        private C0116b() {
        }

        /* renamed from: a */
        public C0115a mo283b() {
            return new C0115a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bn$a */
    /* compiled from: Response */
    private static class C0115a extends C0220dn<C0113bn> {
        private C0115a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0113bn bnVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!bnVar.mo406e()) {
                        throw new C0210de("Required field 'resp_code' was not found in serialized data! Struct: " + toString());
                    }
                    bnVar.mo413l();
                    return;
                }
                switch (l.f653c) {
                    case 1:
                        if (l.f652b != 8) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bnVar.f436a = ddVar.mo615w();
                            bnVar.mo400a(true);
                            break;
                        }
                    case 2:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bnVar.f437b = ddVar.mo618z();
                            bnVar.mo401b(true);
                            break;
                        }
                    case 3:
                        if (l.f652b != 12) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bnVar.f438c = new C0099bl();
                            bnVar.f438c.mo253a(ddVar);
                            bnVar.mo404c(true);
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
        public void mo279a(C0209dd ddVar, C0113bn bnVar) throws C0172ck {
            bnVar.mo413l();
            ddVar.mo586a(C0113bn.f430f);
            ddVar.mo581a(C0113bn.f431g);
            ddVar.mo579a(bnVar.f436a);
            ddVar.mo593c();
            if (bnVar.f437b != null && bnVar.mo409h()) {
                ddVar.mo581a(C0113bn.f432h);
                ddVar.mo587a(bnVar.f437b);
                ddVar.mo593c();
            }
            if (bnVar.f438c != null && bnVar.mo412k()) {
                ddVar.mo581a(C0113bn.f433i);
                bnVar.f438c.mo258b(ddVar);
                ddVar.mo593c();
            }
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bn$d */
    /* compiled from: Response */
    private static class C0118d implements C0219dm {
        private C0118d() {
        }

        /* renamed from: a */
        public C0117c mo283b() {
            return new C0117c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bn$c */
    /* compiled from: Response */
    private static class C0117c extends C0221do<C0113bn> {
        private C0117c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0113bn bnVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo579a(bnVar.f436a);
            BitSet bitSet = new BitSet();
            if (bnVar.mo409h()) {
                bitSet.set(0);
            }
            if (bnVar.mo412k()) {
                bitSet.set(1);
            }
            djVar.mo630a(bitSet, 2);
            if (bnVar.mo409h()) {
                djVar.mo587a(bnVar.f437b);
            }
            if (bnVar.mo412k()) {
                bnVar.f438c.mo258b((C0209dd) djVar);
            }
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0113bn bnVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            bnVar.f436a = djVar.mo615w();
            bnVar.mo400a(true);
            BitSet b = djVar.mo631b(2);
            if (b.get(0)) {
                bnVar.f437b = djVar.mo618z();
                bnVar.mo401b(true);
            }
            if (b.get(1)) {
                bnVar.f438c = new C0099bl();
                bnVar.f438c.mo253a((C0209dd) djVar);
                bnVar.mo404c(true);
            }
        }
    }
}
