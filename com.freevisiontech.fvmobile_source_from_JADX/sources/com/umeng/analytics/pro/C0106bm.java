package com.umeng.analytics.pro;

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

/* renamed from: com.umeng.analytics.pro.bm */
/* compiled from: ImprintValue */
public class C0106bm implements C0164ce<C0106bm, C0112e>, Serializable, Cloneable {

    /* renamed from: d */
    public static final Map<C0112e, C0183cq> f408d;

    /* renamed from: e */
    private static final long f409e = 7501688097813630241L;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public static final C0214di f410f = new C0214di("ImprintValue");
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0194cy f411g = new C0194cy("value", (byte) 11, 1);
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f412h = new C0194cy("ts", (byte) 10, 2);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f413i = new C0194cy("guid", (byte) 11, 3);

    /* renamed from: j */
    private static final Map<Class<? extends C0218dl>, C0219dm> f414j = new HashMap();

    /* renamed from: k */
    private static final int f415k = 0;

    /* renamed from: a */
    public String f416a;

    /* renamed from: b */
    public long f417b;

    /* renamed from: c */
    public String f418c;

    /* renamed from: l */
    private byte f419l;

    /* renamed from: m */
    private C0112e[] f420m;

    static {
        f414j.put(C0220dn.class, new C0109b());
        f414j.put(C0221do.class, new C0111d());
        EnumMap enumMap = new EnumMap(C0112e.class);
        enumMap.put(C0112e.VALUE, new C0183cq("value", (byte) 2, new C0184cr((byte) 11)));
        enumMap.put(C0112e.TS, new C0183cq("ts", (byte) 1, new C0184cr((byte) 10)));
        enumMap.put(C0112e.GUID, new C0183cq("guid", (byte) 1, new C0184cr((byte) 11)));
        f408d = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0106bm.class, f408d);
    }

    /* renamed from: com.umeng.analytics.pro.bm$e */
    /* compiled from: ImprintValue */
    public enum C0112e implements C0173cl {
        VALUE(1, "value"),
        TS(2, "ts"),
        GUID(3, "guid");
        

        /* renamed from: d */
        private static final Map<String, C0112e> f424d = null;

        /* renamed from: e */
        private final short f426e;

        /* renamed from: f */
        private final String f427f;

        static {
            f424d = new HashMap();
            Iterator it = EnumSet.allOf(C0112e.class).iterator();
            while (it.hasNext()) {
                C0112e eVar = (C0112e) it.next();
                f424d.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0112e m602a(int i) {
            switch (i) {
                case 1:
                    return VALUE;
                case 2:
                    return TS;
                case 3:
                    return GUID;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0112e m604b(int i) {
            C0112e a = m602a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0112e m603a(String str) {
            return f424d.get(str);
        }

        private C0112e(short s, String str) {
            this.f426e = s;
            this.f427f = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f426e;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f427f;
        }
    }

    public C0106bm() {
        this.f419l = 0;
        this.f420m = new C0112e[]{C0112e.VALUE};
    }

    public C0106bm(long j, String str) {
        this();
        this.f417b = j;
        mo377b(true);
        this.f418c = str;
    }

    public C0106bm(C0106bm bmVar) {
        this.f419l = 0;
        this.f420m = new C0112e[]{C0112e.VALUE};
        this.f419l = bmVar.f419l;
        if (bmVar.mo381e()) {
            this.f416a = bmVar.f416a;
        }
        this.f417b = bmVar.f417b;
        if (bmVar.mo387k()) {
            this.f418c = bmVar.f418c;
        }
    }

    /* renamed from: a */
    public C0106bm mo276p() {
        return new C0106bm(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f416a = null;
        mo377b(false);
        this.f417b = 0;
        this.f418c = null;
    }

    /* renamed from: c */
    public String mo378c() {
        return this.f416a;
    }

    /* renamed from: a */
    public C0106bm mo374a(String str) {
        this.f416a = str;
        return this;
    }

    /* renamed from: d */
    public void mo380d() {
        this.f416a = null;
    }

    /* renamed from: e */
    public boolean mo381e() {
        return this.f416a != null;
    }

    /* renamed from: a */
    public void mo375a(boolean z) {
        if (!z) {
            this.f416a = null;
        }
    }

    /* renamed from: f */
    public long mo382f() {
        return this.f417b;
    }

    /* renamed from: a */
    public C0106bm mo373a(long j) {
        this.f417b = j;
        mo377b(true);
        return this;
    }

    /* renamed from: g */
    public void mo383g() {
        this.f419l = C0161cb.m928b(this.f419l, 0);
    }

    /* renamed from: h */
    public boolean mo384h() {
        return C0161cb.m924a(this.f419l, 0);
    }

    /* renamed from: b */
    public void mo377b(boolean z) {
        this.f419l = C0161cb.m916a(this.f419l, 0, z);
    }

    /* renamed from: i */
    public String mo385i() {
        return this.f418c;
    }

    /* renamed from: b */
    public C0106bm mo376b(String str) {
        this.f418c = str;
        return this;
    }

    /* renamed from: j */
    public void mo386j() {
        this.f418c = null;
    }

    /* renamed from: k */
    public boolean mo387k() {
        return this.f418c != null;
    }

    /* renamed from: c */
    public void mo379c(boolean z) {
        if (!z) {
            this.f418c = null;
        }
    }

    /* renamed from: a */
    public C0112e mo256b(int i) {
        return C0112e.m602a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f414j.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f414j.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ImprintValue(");
        boolean z = true;
        if (mo381e()) {
            sb.append("value:");
            if (this.f416a == null) {
                sb.append("null");
            } else {
                sb.append(this.f416a);
            }
            z = false;
        }
        if (!z) {
            sb.append(", ");
        }
        sb.append("ts:");
        sb.append(this.f417b);
        sb.append(", ");
        sb.append("guid:");
        if (this.f418c == null) {
            sb.append("null");
        } else {
            sb.append(this.f418c);
        }
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: l */
    public void mo388l() throws C0172ck {
        if (this.f418c == null) {
            throw new C0210de("Required field 'guid' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m562a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m561a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f419l = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bm$b */
    /* compiled from: ImprintValue */
    private static class C0109b implements C0219dm {
        private C0109b() {
        }

        /* renamed from: a */
        public C0108a mo283b() {
            return new C0108a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bm$a */
    /* compiled from: ImprintValue */
    private static class C0108a extends C0220dn<C0106bm> {
        private C0108a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0106bm bmVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!bmVar.mo384h()) {
                        throw new C0210de("Required field 'ts' was not found in serialized data! Struct: " + toString());
                    }
                    bmVar.mo388l();
                    return;
                }
                switch (l.f653c) {
                    case 1:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bmVar.f416a = ddVar.mo618z();
                            bmVar.mo375a(true);
                            break;
                        }
                    case 2:
                        if (l.f652b != 10) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bmVar.f417b = ddVar.mo616x();
                            bmVar.mo377b(true);
                            break;
                        }
                    case 3:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bmVar.f418c = ddVar.mo618z();
                            bmVar.mo379c(true);
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
        public void mo279a(C0209dd ddVar, C0106bm bmVar) throws C0172ck {
            bmVar.mo388l();
            ddVar.mo586a(C0106bm.f410f);
            if (bmVar.f416a != null && bmVar.mo381e()) {
                ddVar.mo581a(C0106bm.f411g);
                ddVar.mo587a(bmVar.f416a);
                ddVar.mo593c();
            }
            ddVar.mo581a(C0106bm.f412h);
            ddVar.mo580a(bmVar.f417b);
            ddVar.mo593c();
            if (bmVar.f418c != null) {
                ddVar.mo581a(C0106bm.f413i);
                ddVar.mo587a(bmVar.f418c);
                ddVar.mo593c();
            }
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bm$d */
    /* compiled from: ImprintValue */
    private static class C0111d implements C0219dm {
        private C0111d() {
        }

        /* renamed from: a */
        public C0110c mo283b() {
            return new C0110c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bm$c */
    /* compiled from: ImprintValue */
    private static class C0110c extends C0221do<C0106bm> {
        private C0110c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0106bm bmVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo580a(bmVar.f417b);
            djVar.mo587a(bmVar.f418c);
            BitSet bitSet = new BitSet();
            if (bmVar.mo381e()) {
                bitSet.set(0);
            }
            djVar.mo630a(bitSet, 1);
            if (bmVar.mo381e()) {
                djVar.mo587a(bmVar.f416a);
            }
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0106bm bmVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            bmVar.f417b = djVar.mo616x();
            bmVar.mo377b(true);
            bmVar.f418c = djVar.mo618z();
            bmVar.mo379c(true);
            if (djVar.mo631b(1).get(0)) {
                bmVar.f416a = djVar.mo618z();
                bmVar.mo375a(true);
            }
        }
    }
}
