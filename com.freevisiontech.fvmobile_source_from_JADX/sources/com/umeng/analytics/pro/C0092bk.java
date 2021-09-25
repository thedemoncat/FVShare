package com.umeng.analytics.pro;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: com.umeng.analytics.pro.bk */
/* compiled from: IdTracking */
public class C0092bk implements C0164ce<C0092bk, C0098e>, Serializable, Cloneable {

    /* renamed from: d */
    public static final Map<C0098e, C0183cq> f371d;

    /* renamed from: e */
    private static final long f372e = -5764118265293965743L;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public static final C0214di f373f = new C0214di("IdTracking");
    /* access modifiers changed from: private */

    /* renamed from: g */
    public static final C0194cy f374g = new C0194cy("snapshots", C0217dk.f721k, 1);
    /* access modifiers changed from: private */

    /* renamed from: h */
    public static final C0194cy f375h = new C0194cy("journals", C0217dk.f723m, 2);
    /* access modifiers changed from: private */

    /* renamed from: i */
    public static final C0194cy f376i = new C0194cy("checksum", (byte) 11, 3);

    /* renamed from: j */
    private static final Map<Class<? extends C0218dl>, C0219dm> f377j = new HashMap();

    /* renamed from: a */
    public Map<String, C0085bj> f378a;

    /* renamed from: b */
    public List<C0078bi> f379b;

    /* renamed from: c */
    public String f380c;

    /* renamed from: k */
    private C0098e[] f381k;

    static {
        f377j.put(C0220dn.class, new C0095b());
        f377j.put(C0221do.class, new C0097d());
        EnumMap enumMap = new EnumMap(C0098e.class);
        enumMap.put(C0098e.SNAPSHOTS, new C0183cq("snapshots", (byte) 1, new C0186ct(C0217dk.f721k, new C0184cr((byte) 11), new C0188cv((byte) 12, C0085bj.class))));
        enumMap.put(C0098e.JOURNALS, new C0183cq("journals", (byte) 2, new C0185cs(C0217dk.f723m, new C0188cv((byte) 12, C0078bi.class))));
        enumMap.put(C0098e.CHECKSUM, new C0183cq("checksum", (byte) 2, new C0184cr((byte) 11)));
        f371d = Collections.unmodifiableMap(enumMap);
        C0183cq.m1027a(C0092bk.class, f371d);
    }

    /* renamed from: com.umeng.analytics.pro.bk$e */
    /* compiled from: IdTracking */
    public enum C0098e implements C0173cl {
        SNAPSHOTS(1, "snapshots"),
        JOURNALS(2, "journals"),
        CHECKSUM(3, "checksum");
        

        /* renamed from: d */
        private static final Map<String, C0098e> f385d = null;

        /* renamed from: e */
        private final short f387e;

        /* renamed from: f */
        private final String f388f;

        static {
            f385d = new HashMap();
            Iterator it = EnumSet.allOf(C0098e.class).iterator();
            while (it.hasNext()) {
                C0098e eVar = (C0098e) it.next();
                f385d.put(eVar.mo288b(), eVar);
            }
        }

        /* renamed from: a */
        public static C0098e m508a(int i) {
            switch (i) {
                case 1:
                    return SNAPSHOTS;
                case 2:
                    return JOURNALS;
                case 3:
                    return CHECKSUM;
                default:
                    return null;
            }
        }

        /* renamed from: b */
        public static C0098e m510b(int i) {
            C0098e a = m508a(i);
            if (a != null) {
                return a;
            }
            throw new IllegalArgumentException("Field " + i + " doesn't exist!");
        }

        /* renamed from: a */
        public static C0098e m509a(String str) {
            return f385d.get(str);
        }

        private C0098e(short s, String str) {
            this.f387e = s;
            this.f388f = str;
        }

        /* renamed from: a */
        public short mo287a() {
            return this.f387e;
        }

        /* renamed from: b */
        public String mo288b() {
            return this.f388f;
        }
    }

    public C0092bk() {
        this.f381k = new C0098e[]{C0098e.JOURNALS, C0098e.CHECKSUM};
    }

    public C0092bk(Map<String, C0085bj> map) {
        this();
        this.f378a = map;
    }

    public C0092bk(C0092bk bkVar) {
        this.f381k = new C0098e[]{C0098e.JOURNALS, C0098e.CHECKSUM};
        if (bkVar.mo327f()) {
            HashMap hashMap = new HashMap();
            for (Map.Entry next : bkVar.f378a.entrySet()) {
                hashMap.put((String) next.getKey(), new C0085bj((C0085bj) next.getValue()));
            }
            this.f378a = hashMap;
        }
        if (bkVar.mo332k()) {
            ArrayList arrayList = new ArrayList();
            for (C0078bi biVar : bkVar.f379b) {
                arrayList.add(new C0078bi(biVar));
            }
            this.f379b = arrayList;
        }
        if (bkVar.mo335n()) {
            this.f380c = bkVar.f380c;
        }
    }

    /* renamed from: a */
    public C0092bk mo276p() {
        return new C0092bk(this);
    }

    /* renamed from: b */
    public void mo257b() {
        this.f378a = null;
        this.f379b = null;
        this.f380c = null;
    }

    /* renamed from: c */
    public int mo323c() {
        if (this.f378a == null) {
            return 0;
        }
        return this.f378a.size();
    }

    /* renamed from: a */
    public void mo320a(String str, C0085bj bjVar) {
        if (this.f378a == null) {
            this.f378a = new HashMap();
        }
        this.f378a.put(str, bjVar);
    }

    /* renamed from: d */
    public Map<String, C0085bj> mo325d() {
        return this.f378a;
    }

    /* renamed from: a */
    public C0092bk mo318a(Map<String, C0085bj> map) {
        this.f378a = map;
        return this;
    }

    /* renamed from: e */
    public void mo326e() {
        this.f378a = null;
    }

    /* renamed from: f */
    public boolean mo327f() {
        return this.f378a != null;
    }

    /* renamed from: a */
    public void mo321a(boolean z) {
        if (!z) {
            this.f378a = null;
        }
    }

    /* renamed from: g */
    public int mo328g() {
        if (this.f379b == null) {
            return 0;
        }
        return this.f379b.size();
    }

    /* renamed from: h */
    public Iterator<C0078bi> mo329h() {
        if (this.f379b == null) {
            return null;
        }
        return this.f379b.iterator();
    }

    /* renamed from: a */
    public void mo319a(C0078bi biVar) {
        if (this.f379b == null) {
            this.f379b = new ArrayList();
        }
        this.f379b.add(biVar);
    }

    /* renamed from: i */
    public List<C0078bi> mo330i() {
        return this.f379b;
    }

    /* renamed from: a */
    public C0092bk mo317a(List<C0078bi> list) {
        this.f379b = list;
        return this;
    }

    /* renamed from: j */
    public void mo331j() {
        this.f379b = null;
    }

    /* renamed from: k */
    public boolean mo332k() {
        return this.f379b != null;
    }

    /* renamed from: b */
    public void mo322b(boolean z) {
        if (!z) {
            this.f379b = null;
        }
    }

    /* renamed from: l */
    public String mo333l() {
        return this.f380c;
    }

    /* renamed from: a */
    public C0092bk mo316a(String str) {
        this.f380c = str;
        return this;
    }

    /* renamed from: m */
    public void mo334m() {
        this.f380c = null;
    }

    /* renamed from: n */
    public boolean mo335n() {
        return this.f380c != null;
    }

    /* renamed from: c */
    public void mo324c(boolean z) {
        if (!z) {
            this.f380c = null;
        }
    }

    /* renamed from: a */
    public C0098e mo256b(int i) {
        return C0098e.m508a(i);
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f377j.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f377j.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("IdTracking(");
        sb.append("snapshots:");
        if (this.f378a == null) {
            sb.append("null");
        } else {
            sb.append(this.f378a);
        }
        if (mo332k()) {
            sb.append(", ");
            sb.append("journals:");
            if (this.f379b == null) {
                sb.append("null");
            } else {
                sb.append(this.f379b);
            }
        }
        if (mo335n()) {
            sb.append(", ");
            sb.append("checksum:");
            if (this.f380c == null) {
                sb.append("null");
            } else {
                sb.append(this.f380c);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /* renamed from: o */
    public void mo336o() throws C0172ck {
        if (this.f378a == null) {
            throw new C0210de("Required field 'snapshots' was not present! Struct: " + toString());
        }
    }

    /* renamed from: a */
    private void m463a(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            mo258b((C0209dd) new C0191cx(new C0222dp((OutputStream) objectOutputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: a */
    private void m462a(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            mo253a((C0209dd) new C0191cx(new C0222dp((InputStream) objectInputStream)));
        } catch (C0172ck e) {
            throw new IOException(e.getMessage());
        }
    }

    /* renamed from: com.umeng.analytics.pro.bk$b */
    /* compiled from: IdTracking */
    private static class C0095b implements C0219dm {
        private C0095b() {
        }

        /* renamed from: a */
        public C0094a mo283b() {
            return new C0094a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bk$a */
    /* compiled from: IdTracking */
    private static class C0094a extends C0220dn<C0092bk> {
        private C0094a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0092bk bkVar) throws C0172ck {
            ddVar.mo602j();
            while (true) {
                C0194cy l = ddVar.mo604l();
                if (l.f652b == 0) {
                    ddVar.mo603k();
                    bkVar.mo336o();
                    return;
                }
                switch (l.f653c) {
                    case 1:
                        if (l.f652b != 13) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            C0206da n = ddVar.mo606n();
                            bkVar.f378a = new HashMap(n.f690c * 2);
                            for (int i = 0; i < n.f690c; i++) {
                                String z = ddVar.mo618z();
                                C0085bj bjVar = new C0085bj();
                                bjVar.mo253a(ddVar);
                                bkVar.f378a.put(z, bjVar);
                            }
                            ddVar.mo607o();
                            bkVar.mo321a(true);
                            break;
                        }
                    case 2:
                        if (l.f652b != 15) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            C0195cz p = ddVar.mo608p();
                            bkVar.f379b = new ArrayList(p.f655b);
                            for (int i2 = 0; i2 < p.f655b; i2++) {
                                C0078bi biVar = new C0078bi();
                                biVar.mo253a(ddVar);
                                bkVar.f379b.add(biVar);
                            }
                            ddVar.mo609q();
                            bkVar.mo322b(true);
                            break;
                        }
                    case 3:
                        if (l.f652b != 11) {
                            C0212dg.m1197a(ddVar, l.f652b);
                            break;
                        } else {
                            bkVar.f380c = ddVar.mo618z();
                            bkVar.mo324c(true);
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
        public void mo279a(C0209dd ddVar, C0092bk bkVar) throws C0172ck {
            bkVar.mo336o();
            ddVar.mo586a(C0092bk.f373f);
            if (bkVar.f378a != null) {
                ddVar.mo581a(C0092bk.f374g);
                ddVar.mo583a(new C0206da((byte) 11, (byte) 12, bkVar.f378a.size()));
                for (Map.Entry next : bkVar.f378a.entrySet()) {
                    ddVar.mo587a((String) next.getKey());
                    ((C0085bj) next.getValue()).mo258b(ddVar);
                }
                ddVar.mo597e();
                ddVar.mo593c();
            }
            if (bkVar.f379b != null && bkVar.mo332k()) {
                ddVar.mo581a(C0092bk.f375h);
                ddVar.mo582a(new C0195cz((byte) 12, bkVar.f379b.size()));
                for (C0078bi b : bkVar.f379b) {
                    b.mo258b(ddVar);
                }
                ddVar.mo598f();
                ddVar.mo593c();
            }
            if (bkVar.f380c != null && bkVar.mo335n()) {
                ddVar.mo581a(C0092bk.f376i);
                ddVar.mo587a(bkVar.f380c);
                ddVar.mo593c();
            }
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bk$d */
    /* compiled from: IdTracking */
    private static class C0097d implements C0219dm {
        private C0097d() {
        }

        /* renamed from: a */
        public C0096c mo283b() {
            return new C0096c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.bk$c */
    /* compiled from: IdTracking */
    private static class C0096c extends C0221do<C0092bk> {
        private C0096c() {
        }

        /* renamed from: a */
        public void mo279a(C0209dd ddVar, C0092bk bkVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            djVar.mo579a(bkVar.f378a.size());
            for (Map.Entry next : bkVar.f378a.entrySet()) {
                djVar.mo587a((String) next.getKey());
                ((C0085bj) next.getValue()).mo258b((C0209dd) djVar);
            }
            BitSet bitSet = new BitSet();
            if (bkVar.mo332k()) {
                bitSet.set(0);
            }
            if (bkVar.mo335n()) {
                bitSet.set(1);
            }
            djVar.mo630a(bitSet, 2);
            if (bkVar.mo332k()) {
                djVar.mo579a(bkVar.f379b.size());
                for (C0078bi b : bkVar.f379b) {
                    b.mo258b((C0209dd) djVar);
                }
            }
            if (bkVar.mo335n()) {
                djVar.mo587a(bkVar.f380c);
            }
        }

        /* renamed from: b */
        public void mo281b(C0209dd ddVar, C0092bk bkVar) throws C0172ck {
            C0215dj djVar = (C0215dj) ddVar;
            C0206da daVar = new C0206da((byte) 11, (byte) 12, djVar.mo615w());
            bkVar.f378a = new HashMap(daVar.f690c * 2);
            for (int i = 0; i < daVar.f690c; i++) {
                String z = djVar.mo618z();
                C0085bj bjVar = new C0085bj();
                bjVar.mo253a((C0209dd) djVar);
                bkVar.f378a.put(z, bjVar);
            }
            bkVar.mo321a(true);
            BitSet b = djVar.mo631b(2);
            if (b.get(0)) {
                C0195cz czVar = new C0195cz((byte) 12, djVar.mo615w());
                bkVar.f379b = new ArrayList(czVar.f655b);
                for (int i2 = 0; i2 < czVar.f655b; i2++) {
                    C0078bi biVar = new C0078bi();
                    biVar.mo253a((C0209dd) djVar);
                    bkVar.f379b.add(biVar);
                }
                bkVar.mo322b(true);
            }
            if (b.get(1)) {
                bkVar.f380c = djVar.mo618z();
                bkVar.mo324c(true);
            }
        }
    }
}
