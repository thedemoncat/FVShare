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

/* renamed from: com.umeng.analytics.pro.bl */
/* compiled from: Imprint */
public class C0099bl implements C0164ce<C0099bl, C0105e>, Serializable, Cloneable {

    /* renamed from: d */
    public static final Map<C0105e, C0183cq> f389d;

    /* renamed from: e */
    private static final long f390e = 2846460275012375038L;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public static final C0214di f391f = new C0214di("Imprint");
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0194cy f392g = new C0194cy("property", C0217dk.f721k, 1);
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f393h = new C0194cy("version", (byte) 8, 2);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f394i = new C0194cy("checksum", (byte) 11, 3);

    /* renamed from: j */
    private static final Map<Class<? extends C0218dl>, C0219dm> f395j = new HashMap();

    /* renamed from: k */
    private static final int f396k = 0;

    /* renamed from: a */
    public Map<String, C0106bm> f397a;

    /* renamed from: b */
    public int f398b;

    /* renamed from: c */
    public String f399c;

    /* renamed from: l */
    private byte f400l;

    static {
        f395j.put(C0220dn.class, new C0102b());
        f395j.put(C0221do.class, new C0104d());
        EnumMap enumMap = new EnumMap(C0105e.class);
        enumMap.put(C0105e.PROPERTY, new C0183cq("property", (byte) 1, new C0186ct(C0217dk.f721k, new C0184cr((byte) 11), new C0188cv((byte) 12, C0106bm.class))));
        enumMap.put(C0105e.VERSION, new C0183cq("version", (byte) 1, new C0184cr((byte) 8)));
        enumMap.put(C0105e.CHECKSUM, new C0183cq("checksum", (byte) 1, new C0184cr((byte) 11)));
        f389d = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0099bl.class, f389d);
    }

    /* renamed from: com.umeng.analytics.pro.bl$e */
    /* compiled from: Imprint */
    public enum C0105e implements C0173cl {
        PROPERTY(1, "property"),
        VERSION(2, "version"),
        CHECKSUM(3, "checksum");
        

        /* renamed from: d */
        private static final Map<String, C0105e> f404d = null;

        /* renamed from: e */
        private final short f406e;

        /* renamed from: f */
        private final String f407f;

        static {
            f404d = new HashMap();
            Iterator it = EnumSet.allOf(C0105e.class).iterator();
            while (it.hasNext()) {
                C0105e eVar = (C0105e) it.next();
                f404d.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0105e m556a(int i) {
            switch (i) {
                case 1:
                    return PROPERTY;
                case 2:
                    return VERSION;
                case 3:
                    return CHECKSUM;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0105e m558b(int i) {
            C0105e a = m556a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0105e m557a(String str) {
            return f404d.get(str);
        }

        private C0105e(short s, String str) {
            this.f406e = s;
            this.f407f = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f406e;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f407f;
        }
    }

    public C0099bl() {
        this.f400l = 0;
    }

    public C0099bl(Map<String, C0106bm> map, int i, String str) {
        this();
        this.f397a = map;
        this.f398b = i;
        mo350b(true);
        this.f399c = str;
    }

    public C0099bl(C0099bl blVar) {
        this.f400l = 0;
        this.f400l = blVar.f400l;
        if (blVar.mo356f()) {
            HashMap hashMap = new HashMap();
            for (Map.Entry next : blVar.f397a.entrySet()) {
                hashMap.put((String) next.getKey(), new C0106bm((C0106bm) next.getValue()));
            }
            this.f397a = hashMap;
        }
        this.f398b = blVar.f398b;
        if (blVar.mo362l()) {
            this.f399c = blVar.f399c;
        }
    }

    /* renamed from: a */
    public C0099bl mo276p() {
        return new C0099bl(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f397a = null;
        mo350b(false);
        this.f398b = 0;
        this.f399c = null;
    }

    /* renamed from: c */
    public int mo351c() {
        if (this.f397a == null) {
            return 0;
        }
        return this.f397a.size();
    }

    /* renamed from: a */
    public void mo348a(String str, C0106bm bmVar) {
        if (this.f397a == null) {
            this.f397a = new HashMap();
        }
        this.f397a.put(str, bmVar);
    }

    /* renamed from: d */
    public Map<String, C0106bm> mo354d() {
        return this.f397a;
    }

    /* renamed from: a */
    public C0099bl mo347a(Map<String, C0106bm> map) {
        this.f397a = map;
        return this;
    }

    /* renamed from: e */
    public void mo355e() {
        this.f397a = null;
    }

    /* renamed from: f */
    public boolean mo356f() {
        return this.f397a != null;
    }

    /* renamed from: a */
    public void mo349a(boolean z) {
        if (!z) {
            this.f397a = null;
        }
    }

    /* renamed from: g */
    public int mo357g() {
        return this.f398b;
    }

    /* renamed from: a */
    public C0099bl mo345a(int i) {
        this.f398b = i;
        mo350b(true);
        return this;
    }

    /* renamed from: h */
    public void mo358h() {
        this.f400l = C0161cb.m928b(this.f400l, 0);
    }

    /* renamed from: i */
    public boolean mo359i() {
        return C0161cb.m924a(this.f400l, 0);
    }

    /* renamed from: b */
    public void mo350b(boolean z) {
        this.f400l = C0161cb.m916a(this.f400l, 0, z);
    }

    /* renamed from: j */
    public String mo360j() {
        return this.f399c;
    }

    /* renamed from: a */
    public C0099bl mo346a(String str) {
        this.f399c = str;
        return this;
    }

    /* renamed from: k */
    public void mo361k() {
        this.f399c = null;
    }

    /* renamed from: l */
    public boolean mo362l() {
        return this.f399c != null;
    }

    /* renamed from: c */
    public void mo353c(boolean z) {
        if (!z) {
            this.f399c = null;
        }
    }

    /* renamed from: c */
    public C0105e mo256b(int i) {
        return C0105e.m556a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f395j.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f395j.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Imprint(");
        sb.append("property:");
        if (this.f397a == null) {
            sb.append("null");
        } else {
            sb.append(this.f397a);
        }
        sb.append(", ");
        sb.append("version:");
        sb.append(this.f398b);
        sb.append(", ");
        sb.append("checksum:");
        if (this.f399c == null) {
            sb.append("null");
        } else {
            sb.append(this.f399c);
        }
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: m */
    public void mo363m() throws C0172ck {
        if (this.f397a == null) {
            throw new C0210de("Required field 'property' was not present! Struct: " + toString());
        } else if (this.f399c == null) {
            throw new C0210de("Required field 'checksum' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m514a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m513a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            this.f400l = 0;
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bl$b */
    /* compiled from: Imprint */
    private static class C0102b implements C0219dm {
        private C0102b() {
        }

        /* renamed from: a */
        public C0101a mo283b() {
            return new C0101a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bl$a */
    /* compiled from: Imprint */
    private static class C0101a extends C0220dn<C0099bl> {
        private C0101a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0099bl blVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    if (!blVar.mo359i()) {
                        throw new C0210de("Required field 'version' was not found in serialized data! Struct: " + toString());
                    }
                    blVar.mo363m();
                    return;
                }
                switch (l.f653c) {
                    case 1:
                        if (l.f652b != 13) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            C0206da n = ddVar.mo606n();
                            blVar.f397a = new HashMap(n.f690c * 2);
                            for (int i = 0; i < n.f690c; i++) {
                                String z = ddVar.mo618z();
                                C0106bm bmVar = new C0106bm();
                                bmVar.mo253a(ddVar);
                                blVar.f397a.put(z, bmVar);
                            }
                            ddVar.mo607o();
                            blVar.mo349a(true);
                            break;
                        }
                    case 2:
                        if (l.f652b != 8) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            blVar.f398b = ddVar.mo615w();
                            blVar.mo350b(true);
                            break;
                        }
                    case 3:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            blVar.f399c = ddVar.mo618z();
                            blVar.mo353c(true);
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
        public void mo279a(C0209dd ddVar, C0099bl blVar) throws C0172ck {
            blVar.mo363m();
            ddVar.mo586a(C0099bl.f391f);
            if (blVar.f397a != null) {
                ddVar.mo581a(C0099bl.f392g);
                ddVar.mo583a(new C0206da((byte) 11, (byte) 12, blVar.f397a.size()));
                for (Map.Entry next : blVar.f397a.entrySet()) {
                    ddVar.mo587a((String) next.getKey());
                    ((C0106bm) next.getValue()).mo258b(ddVar);
                }
                ddVar.mo597e();
                ddVar.mo593c();
            }
            ddVar.mo581a(C0099bl.f393h);
            ddVar.mo579a(blVar.f398b);
            ddVar.mo593c();
            if (blVar.f399c != null) {
                ddVar.mo581a(C0099bl.f394i);
                ddVar.mo587a(blVar.f399c);
                ddVar.mo593c();
            }
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bl$d */
    /* compiled from: Imprint */
    private static class C0104d implements C0219dm {
        private C0104d() {
        }

        /* renamed from: a */
        public C0103c mo283b() {
            return new C0103c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bl$c */
    /* compiled from: Imprint */
    private static class C0103c extends C0221do<C0099bl> {
        private C0103c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0099bl blVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo579a(blVar.f397a.size());
            for (Map.Entry next : blVar.f397a.entrySet()) {
                djVar.mo587a((String) next.getKey());
                ((C0106bm) next.getValue()).mo258b((C0209dd) djVar);
            }
            djVar.mo579a(blVar.f398b);
            djVar.mo587a(blVar.f399c);
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0099bl blVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            C0206da daVar = new C0206da((byte) 11, (byte) 12, djVar.mo615w());
            blVar.f397a = new HashMap(daVar.f690c * 2);
            for (int i = 0; i < daVar.f690c; i++) {
                String z = djVar.mo618z();
                C0106bm bmVar = new C0106bm();
                bmVar.mo253a((C0209dd) djVar);
                blVar.f397a.put(z, bmVar);
            }
            blVar.mo349a(true);
            blVar.f398b = djVar.mo615w();
            blVar.mo350b(true);
            blVar.f399c = djVar.mo618z();
            blVar.mo353c(true);
        }
    }
}
