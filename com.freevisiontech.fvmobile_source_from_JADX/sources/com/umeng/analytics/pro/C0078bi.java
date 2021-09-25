package com.umeng.analytics.pro;

import com.lzy.okgo.cookie.SerializableCookie;
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

/* renamed from: com.umeng.analytics.pro.bi */
/* compiled from: IdJournal */
public class C0078bi implements C0164ce<C0078bi, C0084e>, Serializable, Cloneable {

    /* renamed from: e */
    public static final Map<C0084e, C0183cq> f328e;

    /* renamed from: f */
    private static final long f329f = 9132678615281394583L;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0214di f330g = new C0214di("IdJournal");
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f331h = new C0194cy(SerializableCookie.DOMAIN, (byte) 11, 1);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f332i = new C0194cy("old_id", (byte) 11, 2);
    /* access modifiers changed from: private */

    /* renamed from: j */
    public static final C0194cy f333j = new C0194cy("new_id", (byte) 11, 3);
    /* access modifiers changed from: private */

    /* renamed from: k */
    public static final C0194cy f334k = new C0194cy("ts", (byte) 10, 4);

    /* renamed from: l */
    private static final Map<Class<? extends C0218dl>, C0219dm> f335l = new HashMap();

    /* renamed from: m */
    private static final int f336m = 0;

    /* renamed from: a */
    public String f337a;

    /* renamed from: b */
    public String f338b;

    /* renamed from: c */
    public String f339c;

    /* renamed from: d */
    public long f340d;

    /* renamed from: n */
    private byte f341n;

    /* renamed from: o */
    private C0084e[] f342o;

    static {
        f335l.put(C0220dn.class, new C0081b());
        f335l.put(C0221do.class, new C0083d());
        EnumMap enumMap = new EnumMap(C0084e.class);
        enumMap.put(C0084e.DOMAIN, new C0183cq(SerializableCookie.DOMAIN, (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0084e.OLD_ID, new C0183cq("old_id", (byte) 2, new C0184cr((byte) 11)));
        enumMap.put(C0084e.NEW_ID, new C0183cq("new_id", (byte) 1, new C0184cr((byte) 11)));
        enumMap.put(C0084e.TS, new C0183cq("ts", (byte) 1, new C0184cr((byte) 10)));
        f328e = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0078bi.class, f328e);
    }

    /* renamed from: com.umeng.analytics.pro.bi$e */
    /* compiled from: IdJournal */
    public enum C0084e implements C0173cl {
        DOMAIN(1, SerializableCookie.DOMAIN),
        OLD_ID(2, "old_id"),
        NEW_ID(3, "new_id"),
        TS(4, "ts");
        

        /* renamed from: e */
        private static final Map<String, C0084e> f347e = null;

        /* renamed from: f */
        private final short f349f;

        /* renamed from: g */
        private final String f350g;

        static {
            f347e = new HashMap();
            Iterator it = EnumSet.allOf(C0084e.class).iterator();
            while (it.hasNext()) {
                C0084e eVar = (C0084e) it.next();
                f347e.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0084e m411a(int i) {
            switch (i) {
                case 1:
                    return DOMAIN;
                case 2:
                    return OLD_ID;
                case 3:
                    return NEW_ID;
                case 4:
                    return TS;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0084e m413b(int i) {
            C0084e a = m411a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0084e m412a(String str) {
            return f347e.get(str);
        }

        private C0084e(short s, String str) {
            this.f349f = s;
            this.f350g = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f349f;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f350g;
        }
    }

    public C0078bi() {
        this.f341n = 0;
        this.f342o = new C0084e[]{C0084e.OLD_ID};
    }

    public C0078bi(String str, String str2, long j) {
        this();
        this.f337a = str;
        this.f339c = str2;
        this.f340d = j;
        mo264d(true);
    }

    public C0078bi(C0078bi biVar) {
        this.f341n = 0;
        this.f342o = new C0084e[]{C0084e.OLD_ID};
        this.f341n = biVar.f341n;
        if (biVar.mo265e()) {
            this.f337a = biVar.f337a;
        }
        if (biVar.mo268h()) {
            this.f338b = biVar.f338b;
        }
        if (biVar.mo271k()) {
            this.f339c = biVar.f339c;
        }
        this.f340d = biVar.f340d;
    }

    /* renamed from: a */
    public C0078bi mo276p() {
        return new C0078bi(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f337a = null;
        this.f338b = null;
        this.f339c = null;
        mo264d(false);
        this.f340d = 0;
    }

    /* renamed from: c */
    public String mo261c() {
        return this.f337a;
    }

    /* renamed from: a */
    public C0078bi mo252a(String str) {
        this.f337a = str;
        return this;
    }

    /* renamed from: d */
    public void mo263d() {
        this.f337a = null;
    }

    /* renamed from: e */
    public boolean mo265e() {
        return this.f337a != null;
    }

    /* renamed from: a */
    public void mo254a(boolean z) {
        if (!z) {
            this.f337a = null;
        }
    }

    /* renamed from: f */
    public String mo266f() {
        return this.f338b;
    }

    /* renamed from: b */
    public C0078bi mo255b(String str) {
        this.f338b = str;
        return this;
    }

    /* renamed from: g */
    public void mo267g() {
        this.f338b = null;
    }

    /* renamed from: h */
    public boolean mo268h() {
        return this.f338b != null;
    }

    /* renamed from: b */
    public void mo259b(boolean z) {
        if (!z) {
            this.f338b = null;
        }
    }

    /* renamed from: i */
    public String mo269i() {
        return this.f339c;
    }

    /* renamed from: c */
    public C0078bi mo260c(String str) {
        this.f339c = str;
        return this;
    }

    /* renamed from: j */
    public void mo270j() {
        this.f339c = null;
    }

    /* renamed from: k */
    public boolean mo271k() {
        return this.f339c != null;
    }

    /* renamed from: c */
    public void mo262c(boolean z) {
        if (!z) {
            this.f339c = null;
        }
    }

    /* renamed from: l */
    public long mo272l() {
        return this.f340d;
    }

    /* renamed from: a */
    public C0078bi mo251a(long j) {
        this.f340d = j;
        mo264d(true);
        return this;
    }

    /* renamed from: m */
    public void mo273m() {
        this.f341n = C0161cb.m928b(this.f341n, 0);
    }

    /* renamed from: n */
    public boolean mo274n() {
        return C0161cb.m924a(this.f341n, 0);
    }

    /* renamed from: d */
    public void mo264d(boolean z) {
        this.f341n = C0161cb.m916a(this.f341n, 0, z);
    }

    /* renamed from: a */
    public C0084e mo256b(int i) {
        return C0084e.m411a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f335l.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f335l.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("IdJournal(");
        sb.append("domain:");
        if (this.f337a == null) {
            sb.append("null");
        } else {
            sb.append(this.f337a);
        }
        if (mo268h()) {
            sb.append(", ");
            sb.append("old_id:");
            if (this.f338b == null) {
                sb.append("null");
            } else {
                sb.append(this.f338b);
            }
        }
        sb.append(", ");
        sb.append("new_id:");
        if (this.f339c == null) {
            sb.append("null");
        } else {
            sb.append(this.f339c);
        }
        sb.append(", ");
        sb.append("ts:");
        sb.append(this.f340d);
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: o */
    public void mo275o() throws C0172ck {
        if (this.f337a == null) {
            throw new C0210de("Required field 'domain' was not present! Struct: " + toString());
        } else if (this.f339c == null) {
            throw new C0210de("Required field 'new_id' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m365a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m364a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f341n = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bi$b */
    /* compiled from: IdJournal */
    private static class C0081b implements C0219dm {
        private C0081b() {
        }

        /* renamed from: a */
        public C0080a mo283b() {
            return new C0080a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bi$a */
    /* compiled from: IdJournal */
    private static class C0080a extends C0220dn<C0078bi> {
        private C0080a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0078bi biVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!biVar.mo274n()) {
                        throw new C0210de("Required field 'ts' was not found in serialized data! Struct: " + toString());
                    }
                    biVar.mo275o();
                    return;
                }
                switch (l.f653c) {
                    case 1:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            biVar.f337a = ddVar.mo618z();
                            biVar.mo254a(true);
                            break;
                        }
                    case 2:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            biVar.f338b = ddVar.mo618z();
                            biVar.mo259b(true);
                            break;
                        }
                    case 3:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            biVar.f339c = ddVar.mo618z();
                            biVar.mo262c(true);
                            break;
                        }
                    case 4:
                        if (l.f652b != 10) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            biVar.f340d = ddVar.mo616x();
                            biVar.mo264d(true);
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
        public void mo279a(C0209dd ddVar, C0078bi biVar) throws C0172ck {
            biVar.mo275o();
            ddVar.mo586a(C0078bi.f330g);
            if (biVar.f337a != null) {
                ddVar.mo581a(C0078bi.f331h);
                ddVar.mo587a(biVar.f337a);
                ddVar.mo593c();
            }
            if (biVar.f338b != null && biVar.mo268h()) {
                ddVar.mo581a(C0078bi.f332i);
                ddVar.mo587a(biVar.f338b);
                ddVar.mo593c();
            }
            if (biVar.f339c != null) {
                ddVar.mo581a(C0078bi.f333j);
                ddVar.mo587a(biVar.f339c);
                ddVar.mo593c();
            }
            ddVar.mo581a(C0078bi.f334k);
            ddVar.mo580a(biVar.f340d);
            ddVar.mo593c();
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bi$d */
    /* compiled from: IdJournal */
    private static class C0083d implements C0219dm {
        private C0083d() {
        }

        /* renamed from: a */
        public C0082c mo283b() {
            return new C0082c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bi$c */
    /* compiled from: IdJournal */
    private static class C0082c extends C0221do<C0078bi> {
        private C0082c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0078bi biVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo587a(biVar.f337a);
            djVar.mo587a(biVar.f339c);
            djVar.mo580a(biVar.f340d);
            BitSet bitSet = new BitSet();
            if (biVar.mo268h()) {
                bitSet.set(0);
            }
            djVar.mo630a(bitSet, 1);
            if (biVar.mo268h()) {
                djVar.mo587a(biVar.f338b);
            }
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0078bi biVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            biVar.f337a = djVar.mo618z();
            biVar.mo254a(true);
            biVar.f339c = djVar.mo618z();
            biVar.mo262c(true);
            biVar.f340d = djVar.mo616x();
            biVar.mo264d(true);
            if (djVar.mo631b(1).get(0)) {
                biVar.f338b = djVar.mo618z();
                biVar.mo259b(true);
            }
        }
    }
}
