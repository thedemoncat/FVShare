package com.umeng.analytics.pro;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.bj */
/* compiled from: IdSnapshot */
public class C0085bj implements C0164ce<C0085bj, C0091e>, Serializable, Cloneable {

    /* renamed from: d */
    public static final Map<C0091e, C0183cq> f351d;

    /* renamed from: e */
    private static final long f352e = -6496538196005191531L;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public static final C0214di f353f = new C0214di("IdSnapshot");
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0194cy f354g = new C0194cy("identity", (byte) 11, 1);
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f355h = new C0194cy("ts", (byte) 10, 2);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f356i = new C0194cy("version", (byte) 8, 3);

    /* renamed from: j */
    private static final Map<Class<? extends C0218dl>, C0219dm> f357j = new HashMap();

    /* renamed from: k */
    private static final int f358k = 0;

    /* renamed from: l */
    private static final int f359l = 1;

    /* renamed from: a */
    public String f360a;

    /* renamed from: b */
    public long f361b;

    /* renamed from: c */
    public int f362c;

    /* renamed from: m */
    private byte f363m;

    static {
        f357j.put(C0220dn.class, new C0088b());
        f357j.put(C0221do.class, new C0090d());
        EnumMap enumMap = new EnumMap(C0091e.class);
        enumMap.put(C0091e.IDENTITY, new C0183cq("identity", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0091e.TS, new C0183cq("ts", (byte) 1, new C0184cr((byte) 10)));
        enumMap.put(C0091e.VERSION, new C0183cq("version", (byte) 1, new C0184cr((byte) 8)));
        f351d = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0085bj.class, f351d);
    }

    /* renamed from: com.umeng.analytics.pro.bj$e */
    /* compiled from: IdSnapshot */
    public enum C0091e implements C0173cl {
        IDENTITY(1, "identity"),
        TS(2, "ts"),
        VERSION(3, "version");
        

        /* renamed from: d */
        private static final Map<String, C0091e> f367d = null;

        /* renamed from: e */
        private final short f369e;

        /* renamed from: f */
        private final String f370f;

        static {
            f367d = new HashMap();
            Iterator it = EnumSet.allOf(C0091e.class).iterator();
            while (it.hasNext()) {
                C0091e eVar = (C0091e) it.next();
                f367d.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0091e m457a(int i) {
            switch (i) {
                case 1:
                    return IDENTITY;
                case 2:
                    return TS;
                case 3:
                    return VERSION;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0091e m459b(int i) {
            C0091e a = m457a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0091e m458a(String str) {
            return f367d.get(str);
        }

        private C0091e(short s, String str) {
            this.f369e = s;
            this.f370f = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f369e;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f370f;
        }
    }

    public C0085bj() {
        this.f363m = 0;
    }

    public C0085bj(String str, long j, int i) {
        this();
        this.f360a = str;
        this.f361b = j;
        mo294b(true);
        this.f362c = i;
        mo297c(true);
    }

    public C0085bj(C0085bj bjVar) {
        this.f363m = 0;
        this.f363m = bjVar.f363m;
        if (bjVar.mo299e()) {
            this.f360a = bjVar.f360a;
        }
        this.f361b = bjVar.f361b;
        this.f362c = bjVar.f362c;
    }

    /* renamed from: a */
    public C0085bj mo276p() {
        return new C0085bj(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f360a = null;
        mo294b(false);
        this.f361b = 0;
        mo297c(false);
        this.f362c = 0;
    }

    /* renamed from: c */
    public String mo296c() {
        return this.f360a;
    }

    /* renamed from: a */
    public C0085bj mo292a(String str) {
        this.f360a = str;
        return this;
    }

    /* renamed from: d */
    public void mo298d() {
        this.f360a = null;
    }

    /* renamed from: e */
    public boolean mo299e() {
        return this.f360a != null;
    }

    /* renamed from: a */
    public void mo293a(boolean z) {
        if (!z) {
            this.f360a = null;
        }
    }

    /* renamed from: f */
    public long mo300f() {
        return this.f361b;
    }

    /* renamed from: a */
    public C0085bj mo291a(long j) {
        this.f361b = j;
        mo294b(true);
        return this;
    }

    /* renamed from: g */
    public void mo301g() {
        this.f363m = C0161cb.m928b(this.f363m, 0);
    }

    /* renamed from: h */
    public boolean mo302h() {
        return C0161cb.m924a(this.f363m, 0);
    }

    /* renamed from: b */
    public void mo294b(boolean z) {
        this.f363m = C0161cb.m916a(this.f363m, 0, z);
    }

    /* renamed from: i */
    public int mo303i() {
        return this.f362c;
    }

    /* renamed from: a */
    public C0085bj mo290a(int i) {
        this.f362c = i;
        mo297c(true);
        return this;
    }

    /* renamed from: j */
    public void mo304j() {
        this.f363m = C0161cb.m928b(this.f363m, 1);
    }

    /* renamed from: k */
    public boolean mo305k() {
        return C0161cb.m924a(this.f363m, 1);
    }

    /* renamed from: c */
    public void mo297c(boolean z) {
        this.f363m = C0161cb.m916a(this.f363m, 1, z);
    }

    /* renamed from: c */
    public C0091e mo256b(int i) {
        return C0091e.m457a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f357j.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f357j.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("IdSnapshot(");
        sb.append("identity:");
        if (this.f360a == null) {
            sb.append("null");
        } else {
            sb.append(this.f360a);
        }
        sb.append(", ");
        sb.append("ts:");
        sb.append(this.f361b);
        sb.append(", ");
        sb.append("version:");
        sb.append(this.f362c);
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: l */
    public void mo306l() throws C0172ck {
        if (this.f360a == null) {
            throw new C0210de("Required field 'identity' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m417a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m416a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f363m = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bj$b */
    /* compiled from: IdSnapshot */
    private static class C0088b implements C0219dm {
        private C0088b() {
        }

        /* renamed from: a */
        public C0087a mo283b() {
            return new C0087a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bj$a */
    /* compiled from: IdSnapshot */
    private static class C0087a extends C0220dn<C0085bj> {
        private C0087a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0085bj bjVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!bjVar.mo302h()) {
                        throw new C0210de("Required field 'ts' was not found in serialized data! Struct: " + toString());
                    } else if (!bjVar.mo305k()) {
                        throw new C0210de("Required field 'version' was not found in serialized data! Struct: " + toString());
                    } else {
                        bjVar.mo306l();
                        return;
                    }
                } else {
                    switch (l.f653c) {
                        case 1:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                bjVar.f360a = ddVar.mo618z();
                                bjVar.mo293a(true);
                                break;
                            }
                        case 2:
                            if (l.f652b != 10) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                bjVar.f361b = ddVar.mo616x();
                                bjVar.mo294b(true);
                                break;
                            }
                        case 3:
                            if (l.f652b != 8) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                bjVar.f362c = ddVar.mo615w();
                                bjVar.mo297c(true);
                                break;
                            }
                        default:
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                    }
                    ddVar.mo605m();
                }
            }
        }

        /* renamed from: b */
        public void mo279a(C0209dd ddVar, C0085bj bjVar) throws C0172ck {
            bjVar.mo306l();
            ddVar.mo586a(C0085bj.f353f);
            if (bjVar.f360a != null) {
                ddVar.mo581a(C0085bj.f354g);
                ddVar.mo587a(bjVar.f360a);
                ddVar.mo593c();
            }
            ddVar.mo581a(C0085bj.f355h);
            ddVar.mo580a(bjVar.f361b);
            ddVar.mo593c();
            ddVar.mo581a(C0085bj.f356i);
            ddVar.mo579a(bjVar.f362c);
            ddVar.mo593c();
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bj$d */
    /* compiled from: IdSnapshot */
    private static class C0090d implements C0219dm {
        private C0090d() {
        }

        /* renamed from: a */
        public C0089c mo283b() {
            return new C0089c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bj$c */
    /* compiled from: IdSnapshot */
    private static class C0089c extends C0221do<C0085bj> {
        private C0089c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0085bj bjVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo587a(bjVar.f360a);
            djVar.mo580a(bjVar.f361b);
            djVar.mo579a(bjVar.f362c);
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0085bj bjVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            bjVar.f360a = djVar.mo618z();
            bjVar.mo293a(true);
            bjVar.f361b = djVar.mo616x();
            bjVar.mo294b(true);
            bjVar.f362c = djVar.mo615w();
            bjVar.mo297c(true);
        }
    }
}
