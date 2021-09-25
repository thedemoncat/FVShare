package com.umeng.analytics.pro;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.bo */
/* compiled from: UMEnvelope */
public class C0120bo implements C0164ce<C0120bo, C0126e>, Serializable, Cloneable {

    /* renamed from: A */
    private static final int f448A = 2;

    /* renamed from: B */
    private static final int f449B = 3;

    /* renamed from: k */
    public static final Map<C0126e, C0183cq> f450k;

    /* renamed from: l */
    private static final long f451l = 420342210744516016L;
    /* access modifiers changed from: private */

    /* renamed from: m */
    public static final C0214di f452m = new C0214di("UMEnvelope");
    /* access modifiers changed from: private */

    /* renamed from: n */
    public static final C0194cy f453n = new C0194cy("version", (byte) 11, 1);
    /* access modifiers changed from: private */

    /* renamed from: o */
    public static final C0194cy f454o = new C0194cy("address", (byte) 11, 2);
    /* access modifiers changed from: private */

    /* renamed from: p */
    public static final C0194cy f455p = new C0194cy("signature", (byte) 11, 3);
    /* access modifiers changed from: private */

    /* renamed from: q */
    public static final C0194cy f456q = new C0194cy("serial_num", (byte) 8, 4);
    /* access modifiers changed from: private */

    /* renamed from: r */
    public static final C0194cy f457r = new C0194cy("ts_secs", (byte) 8, 5);
    /* access modifiers changed from: private */

    /* renamed from: s */
    public static final C0194cy f458s = new C0194cy("length", (byte) 8, 6);
    /* access modifiers changed from: private */

    /* renamed from: t */
    public static final C0194cy f459t = new C0194cy("entity", (byte) 11, 7);
    /* access modifiers changed from: private */

    /* renamed from: u */
    public static final C0194cy f460u = new C0194cy("guid", (byte) 11, 8);
    /* access modifiers changed from: private */

    /* renamed from: v */
    public static final C0194cy f461v = new C0194cy("checksum", (byte) 11, 9);
    /* access modifiers changed from: private */

    /* renamed from: w */
    public static final C0194cy f462w = new C0194cy("codex", (byte) 8, 10);

    /* renamed from: x */
    private static final Map<Class<? extends C0218dl>, C0219dm> f463x = new HashMap();

    /* renamed from: y */
    private static final int f464y = 0;

    /* renamed from: z */
    private static final int f465z = 1;

    /* renamed from: C */
    private byte f466C;

    /* renamed from: D */
    private C0126e[] f467D;

    /* renamed from: a */
    public String f468a;

    /* renamed from: b */
    public String f469b;

    /* renamed from: c */
    public String f470c;

    /* renamed from: d */
    public int f471d;

    /* renamed from: e */
    public int f472e;

    /* renamed from: f */
    public int f473f;

    /* renamed from: g */
    public ByteBuffer f474g;

    /* renamed from: h */
    public String f475h;

    /* renamed from: i */
    public String f476i;

    /* renamed from: j */
    public int f477j;

    static {
        f463x.put(C0220dn.class, new C0123b());
        f463x.put(C0221do.class, new C0125d());
        EnumMap enumMap = new EnumMap(C0126e.class);
        enumMap.put(C0126e.VERSION, new C0183cq("version", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0126e.ADDRESS, new C0183cq("address", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0126e.SIGNATURE, new C0183cq("signature", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0126e.SERIAL_NUM, new C0183cq("serial_num", (byte) 1, new C0184cr((byte) 8)));
        enumMap.put(C0126e.TS_SECS, new C0183cq("ts_secs", (byte) 1, new C0184cr((byte) 8)));
        enumMap.put(C0126e.LENGTH, new C0183cq("length", (byte) 1, new C0184cr((byte) 8)));
        enumMap.put(C0126e.ENTITY, new C0183cq("entity", (byte) 1, new C0184cr((byte) 11, true)));
        enumMap.put(C0126e.GUID, new C0183cq("guid", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0126e.CHECKSUM, new C0183cq("checksum", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0126e.CODEX, new C0183cq("codex", (byte) 2, new C0184cr((byte) 8)));
        f450k = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0120bo.class, f450k);
    }

    /* renamed from: com.umeng.analytics.pro.bo$e */
    /* compiled from: UMEnvelope */
    public enum C0126e implements C0173cl {
        VERSION(1, "version"),
        ADDRESS(2, "address"),
        SIGNATURE(3, "signature"),
        SERIAL_NUM(4, "serial_num"),
        TS_SECS(5, "ts_secs"),
        LENGTH(6, "length"),
        ENTITY(7, "entity"),
        GUID(8, "guid"),
        CHECKSUM(9, "checksum"),
        CODEX(10, "codex");
        

        /* renamed from: k */
        private static final Map<String, C0126e> f488k = null;

        /* renamed from: l */
        private final short f490l;

        /* renamed from: m */
        private final String f491m;

        static {
            f488k = new HashMap();
            Iterator it = EnumSet.allOf(C0126e.class).iterator();
            while (it.hasNext()) {
                C0126e eVar = (C0126e) it.next();
                f488k.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0126e m738a(int i) {
            switch (i) {
                case 1:
                    return VERSION;
                case 2:
                    return ADDRESS;
                case 3:
                    return SIGNATURE;
                case 4:
                    return SERIAL_NUM;
                case 5:
                    return TS_SECS;
                case 6:
                    return LENGTH;
                case 7:
                    return ENTITY;
                case 8:
                    return GUID;
                case 9:
                    return CHECKSUM;
                case 10:
                    return CODEX;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0126e m740b(int i) {
            C0126e a = m738a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0126e m739a(String str) {
            return f488k.get(str);
        }

        private C0126e(short s, String str) {
            this.f490l = s;
            this.f491m = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f490l;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f491m;
        }
    }

    public C0120bo() {
        this.f466C = 0;
        this.f467D = new C0126e[]{C0126e.CODEX};
    }

    public C0120bo(String str, String str2, String str3, int i, int i2, int i3, ByteBuffer byteBuffer, String str4, String str5) {
        this();
        this.f468a = str;
        this.f469b = str2;
        this.f470c = str3;
        this.f471d = i;
        mo445d(true);
        this.f472e = i2;
        mo448e(true);
        this.f473f = i3;
        mo452f(true);
        this.f474g = byteBuffer;
        this.f475h = str4;
        this.f476i = str5;
    }

    public C0120bo(C0120bo boVar) {
        this.f466C = 0;
        this.f467D = new C0126e[]{C0126e.CODEX};
        this.f466C = boVar.f466C;
        if (boVar.mo449e()) {
            this.f468a = boVar.f468a;
        }
        if (boVar.mo456h()) {
            this.f469b = boVar.f469b;
        }
        if (boVar.mo461k()) {
            this.f470c = boVar.f470c;
        }
        this.f471d = boVar.f471d;
        this.f472e = boVar.f472e;
        this.f473f = boVar.f473f;
        if (boVar.mo475y()) {
            this.f474g = C0165cf.m965d(boVar.f474g);
        }
        if (boVar.mo422B()) {
            this.f475h = boVar.f475h;
        }
        if (boVar.mo425E()) {
            this.f476i = boVar.f476i;
        }
        this.f477j = boVar.f477j;
    }

    /* renamed from: a */
    public C0120bo mo276p() {
        return new C0120bo(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f468a = null;
        this.f469b = null;
        this.f470c = null;
        mo445d(false);
        this.f471d = 0;
        mo448e(false);
        this.f472e = 0;
        mo452f(false);
        this.f473f = 0;
        this.f474g = null;
        this.f475h = null;
        this.f476i = null;
        mo460j(false);
        this.f477j = 0;
    }

    /* renamed from: c */
    public String mo440c() {
        return this.f468a;
    }

    /* renamed from: a */
    public C0120bo mo432a(String str) {
        this.f468a = str;
        return this;
    }

    /* renamed from: d */
    public void mo444d() {
        this.f468a = null;
    }

    /* renamed from: e */
    public boolean mo449e() {
        return this.f468a != null;
    }

    /* renamed from: a */
    public void mo435a(boolean z) {
        if (!z) {
            this.f468a = null;
        }
    }

    /* renamed from: f */
    public String mo451f() {
        return this.f469b;
    }

    /* renamed from: b */
    public C0120bo mo436b(String str) {
        this.f469b = str;
        return this;
    }

    /* renamed from: g */
    public void mo453g() {
        this.f469b = null;
    }

    /* renamed from: h */
    public boolean mo456h() {
        return this.f469b != null;
    }

    /* renamed from: b */
    public void mo437b(boolean z) {
        if (!z) {
            this.f469b = null;
        }
    }

    /* renamed from: i */
    public String mo457i() {
        return this.f470c;
    }

    /* renamed from: c */
    public C0120bo mo439c(String str) {
        this.f470c = str;
        return this;
    }

    /* renamed from: j */
    public void mo459j() {
        this.f470c = null;
    }

    /* renamed from: k */
    public boolean mo461k() {
        return this.f470c != null;
    }

    /* renamed from: c */
    public void mo441c(boolean z) {
        if (!z) {
            this.f470c = null;
        }
    }

    /* renamed from: l */
    public int mo462l() {
        return this.f471d;
    }

    /* renamed from: a */
    public C0120bo mo431a(int i) {
        this.f471d = i;
        mo445d(true);
        return this;
    }

    /* renamed from: m */
    public void mo463m() {
        this.f466C = C0161cb.m928b(this.f466C, 0);
    }

    /* renamed from: n */
    public boolean mo464n() {
        return C0161cb.m924a(this.f466C, 0);
    }

    /* renamed from: d */
    public void mo445d(boolean z) {
        this.f466C = C0161cb.m916a(this.f466C, 0, z);
    }

    /* renamed from: o */
    public int mo465o() {
        return this.f472e;
    }

    /* renamed from: c */
    public C0120bo mo438c(int i) {
        this.f472e = i;
        mo448e(true);
        return this;
    }

    /* renamed from: q */
    public void mo466q() {
        this.f466C = C0161cb.m928b(this.f466C, 1);
    }

    /* renamed from: r */
    public boolean mo467r() {
        return C0161cb.m924a(this.f466C, 1);
    }

    /* renamed from: e */
    public void mo448e(boolean z) {
        this.f466C = C0161cb.m916a(this.f466C, 1, z);
    }

    /* renamed from: s */
    public int mo468s() {
        return this.f473f;
    }

    /* renamed from: d */
    public C0120bo mo442d(int i) {
        this.f473f = i;
        mo452f(true);
        return this;
    }

    /* renamed from: t */
    public void mo469t() {
        this.f466C = C0161cb.m928b(this.f466C, 2);
    }

    /* renamed from: u */
    public boolean mo471u() {
        return C0161cb.m924a(this.f466C, 2);
    }

    /* renamed from: f */
    public void mo452f(boolean z) {
        this.f466C = C0161cb.m916a(this.f466C, 2, z);
    }

    /* renamed from: v */
    public byte[] mo472v() {
        mo433a(C0165cf.m964c(this.f474g));
        if (this.f474g == null) {
            return null;
        }
        return this.f474g.array();
    }

    /* renamed from: w */
    public ByteBuffer mo473w() {
        return this.f474g;
    }

    /* renamed from: a */
    public C0120bo mo434a(byte[] bArr) {
        mo433a(bArr == null ? null : ByteBuffer.wrap(bArr));
        return this;
    }

    /* renamed from: a */
    public C0120bo mo433a(ByteBuffer byteBuffer) {
        this.f474g = byteBuffer;
        return this;
    }

    /* renamed from: x */
    public void mo474x() {
        this.f474g = null;
    }

    /* renamed from: y */
    public boolean mo475y() {
        return this.f474g != null;
    }

    /* renamed from: g */
    public void mo454g(boolean z) {
        if (!z) {
            this.f474g = null;
        }
    }

    /* renamed from: z */
    public String mo476z() {
        return this.f475h;
    }

    /* renamed from: d */
    public C0120bo mo443d(String str) {
        this.f475h = str;
        return this;
    }

    /* renamed from: A */
    public void mo421A() {
        this.f475h = null;
    }

    /* renamed from: B */
    public boolean mo422B() {
        return this.f475h != null;
    }

    /* renamed from: h */
    public void mo455h(boolean z) {
        if (!z) {
            this.f475h = null;
        }
    }

    /* renamed from: C */
    public String mo423C() {
        return this.f476i;
    }

    /* renamed from: e */
    public C0120bo mo447e(String str) {
        this.f476i = str;
        return this;
    }

    /* renamed from: D */
    public void mo424D() {
        this.f476i = null;
    }

    /* renamed from: E */
    public boolean mo425E() {
        return this.f476i != null;
    }

    /* renamed from: i */
    public void mo458i(boolean z) {
        if (!z) {
            this.f476i = null;
        }
    }

    /* renamed from: F */
    public int mo426F() {
        return this.f477j;
    }

    /* renamed from: e */
    public C0120bo mo446e(int i) {
        this.f477j = i;
        mo460j(true);
        return this;
    }

    /* renamed from: G */
    public void mo427G() {
        this.f466C = C0161cb.m928b(this.f466C, 3);
    }

    /* renamed from: H */
    public boolean mo428H() {
        return C0161cb.m924a(this.f466C, 3);
    }

    /* renamed from: j */
    public void mo460j(boolean z) {
        this.f466C = C0161cb.m916a(this.f466C, 3, z);
    }

    /* renamed from: f */
    public C0126e mo256b(int i) {
        return C0126e.m738a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f463x.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f463x.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("UMEnvelope(");
        sb.append("version:");
        if (this.f468a == null) {
            sb.append("null");
        } else {
            sb.append(this.f468a);
        }
        sb.append(", ");
        sb.append("address:");
        if (this.f469b == null) {
            sb.append("null");
        } else {
            sb.append(this.f469b);
        }
        sb.append(", ");
        sb.append("signature:");
        if (this.f470c == null) {
            sb.append("null");
        } else {
            sb.append(this.f470c);
        }
        sb.append(", ");
        sb.append("serial_num:");
        sb.append(this.f471d);
        sb.append(", ");
        sb.append("ts_secs:");
        sb.append(this.f472e);
        sb.append(", ");
        sb.append("length:");
        sb.append(this.f473f);
        sb.append(", ");
        sb.append("entity:");
        if (this.f474g == null) {
            sb.append("null");
        } else {
            C0165cf.m960a(this.f474g, sb);
        }
        sb.append(", ");
        sb.append("guid:");
        if (this.f475h == null) {
            sb.append("null");
        } else {
            sb.append(this.f475h);
        }
        sb.append(", ");
        sb.append("checksum:");
        if (this.f476i == null) {
            sb.append("null");
        } else {
            sb.append(this.f476i);
        }
        if (mo428H()) {
            sb.append(", ");
            sb.append("codex:");
            sb.append(this.f477j);
        }
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: I */
    public void mo429I() throws C0172ck {
        if (this.f468a == null) {
            throw new C0210de("Required field 'version' was not present! Struct: " + toString());
        } else if (this.f469b == null) {
            throw new C0210de("Required field 'address' was not present! Struct: " + toString());
        } else if (this.f470c == null) {
            throw new C0210de("Required field 'signature' was not present! Struct: " + toString());
        } else if (this.f474g == null) {
            throw new C0210de("Required field 'entity' was not present! Struct: " + toString());
        } else if (this.f475h == null) {
            throw new C0210de("Required field 'guid' was not present! Struct: " + toString());
        } else if (this.f476i == null) {
            throw new C0210de("Required field 'checksum' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m665a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m664a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f466C = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bo$b */
    /* compiled from: UMEnvelope */
    private static class C0123b implements C0219dm {
        private C0123b() {
        }

        /* renamed from: a */
        public C0122a mo283b() {
            return new C0122a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bo$a */
    /* compiled from: UMEnvelope */
    private static class C0122a extends C0220dn<C0120bo> {
        private C0122a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0120bo boVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!boVar.mo464n()) {
                        throw new C0210de("Required field 'serial_num' was not found in serialized data! Struct: " + toString());
                    } else if (!boVar.mo467r()) {
                        throw new C0210de("Required field 'ts_secs' was not found in serialized data! Struct: " + toString());
                    } else if (!boVar.mo471u()) {
                        throw new C0210de("Required field 'length' was not found in serialized data! Struct: " + toString());
                    } else {
                        boVar.mo429I();
                        return;
                    }
                } else {
                    switch (l.f653c) {
                        case 1:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f468a = ddVar.mo618z();
                                boVar.mo435a(true);
                                break;
                            }
                        case 2:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f469b = ddVar.mo618z();
                                boVar.mo437b(true);
                                break;
                            }
                        case 3:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f470c = ddVar.mo618z();
                                boVar.mo441c(true);
                                break;
                            }
                        case 4:
                            if (l.f652b != 8) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f471d = ddVar.mo615w();
                                boVar.mo445d(true);
                                break;
                            }
                        case 5:
                            if (l.f652b != 8) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f472e = ddVar.mo615w();
                                boVar.mo448e(true);
                                break;
                            }
                        case 6:
                            if (l.f652b != 8) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f473f = ddVar.mo615w();
                                boVar.mo452f(true);
                                break;
                            }
                        case 7:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f474g = ddVar.mo575A();
                                boVar.mo454g(true);
                                break;
                            }
                        case 8:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f475h = ddVar.mo618z();
                                boVar.mo455h(true);
                                break;
                            }
                        case 9:
                            if (l.f652b != 11) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f476i = ddVar.mo618z();
                                boVar.mo458i(true);
                                break;
                            }
                        case 10:
                            if (l.f652b != 8) {
                                C0212dg.m1197a(ddVar, l.f652b);
                                break;
                            } else {
                                boVar.f477j = ddVar.mo615w();
                                boVar.mo460j(true);
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
        public void mo279a(C0209dd ddVar, C0120bo boVar) throws C0172ck {
            boVar.mo429I();
            ddVar.mo586a(C0120bo.f452m);
            if (boVar.f468a != null) {
                ddVar.mo581a(C0120bo.f453n);
                ddVar.mo587a(boVar.f468a);
                ddVar.mo593c();
            }
            if (boVar.f469b != null) {
                ddVar.mo581a(C0120bo.f454o);
                ddVar.mo587a(boVar.f469b);
                ddVar.mo593c();
            }
            if (boVar.f470c != null) {
                ddVar.mo581a(C0120bo.f455p);
                ddVar.mo587a(boVar.f470c);
                ddVar.mo593c();
            }
            ddVar.mo581a(C0120bo.f456q);
            ddVar.mo579a(boVar.f471d);
            ddVar.mo593c();
            ddVar.mo581a(C0120bo.f457r);
            ddVar.mo579a(boVar.f472e);
            ddVar.mo593c();
            ddVar.mo581a(C0120bo.f458s);
            ddVar.mo579a(boVar.f473f);
            ddVar.mo593c();
            if (boVar.f474g != null) {
                ddVar.mo581a(C0120bo.f459t);
                ddVar.mo588a(boVar.f474g);
                ddVar.mo593c();
            }
            if (boVar.f475h != null) {
                ddVar.mo581a(C0120bo.f460u);
                ddVar.mo587a(boVar.f475h);
                ddVar.mo593c();
            }
            if (boVar.f476i != null) {
                ddVar.mo581a(C0120bo.f461v);
                ddVar.mo587a(boVar.f476i);
                ddVar.mo593c();
            }
            if (boVar.mo428H()) {
                ddVar.mo581a(C0120bo.f462w);
                ddVar.mo579a(boVar.f477j);
                ddVar.mo593c();
            }
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bo$d */
    /* compiled from: UMEnvelope */
    private static class C0125d implements C0219dm {
        private C0125d() {
        }

        /* renamed from: a */
        public C0124c mo283b() {
            return new C0124c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bo$c */
    /* compiled from: UMEnvelope */
    private static class C0124c extends C0221do<C0120bo> {
        private C0124c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0120bo boVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo587a(boVar.f468a);
            djVar.mo587a(boVar.f469b);
            djVar.mo587a(boVar.f470c);
            djVar.mo579a(boVar.f471d);
            djVar.mo579a(boVar.f472e);
            djVar.mo579a(boVar.f473f);
            djVar.mo588a(boVar.f474g);
            djVar.mo587a(boVar.f475h);
            djVar.mo587a(boVar.f476i);
            BitSet bitSet = new BitSet();
            if (boVar.mo428H()) {
                bitSet.set(0);
            }
            djVar.mo630a(bitSet, 1);
            if (boVar.mo428H()) {
                djVar.mo579a(boVar.f477j);
            }
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0120bo boVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            boVar.f468a = djVar.mo618z();
            boVar.mo435a(true);
            boVar.f469b = djVar.mo618z();
            boVar.mo437b(true);
            boVar.f470c = djVar.mo618z();
            boVar.mo441c(true);
            boVar.f471d = djVar.mo615w();
            boVar.mo445d(true);
            boVar.f472e = djVar.mo615w();
            boVar.mo448e(true);
            boVar.f473f = djVar.mo615w();
            boVar.mo452f(true);
            boVar.f474g = djVar.mo575A();
            boVar.mo454g(true);
            boVar.f475h = djVar.mo618z();
            boVar.mo455h(true);
            boVar.f476i = djVar.mo618z();
            boVar.mo458i(true);
            if (djVar.mo631b(1).get(0)) {
                boVar.f477j = djVar.mo615w();
                boVar.mo460j(true);
            }
        }
    }
}
