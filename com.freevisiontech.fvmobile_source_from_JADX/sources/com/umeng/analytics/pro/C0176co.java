package com.umeng.analytics.pro;

import com.umeng.analytics.pro.C0173cl;
import com.umeng.analytics.pro.C0176co;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* renamed from: com.umeng.analytics.pro.co */
/* compiled from: TUnion */
public abstract class C0176co<T extends C0176co<?, ?>, F extends C0173cl> implements C0164ce<T, F> {

    /* renamed from: c */
    private static final Map<Class<? extends C0218dl>, C0219dm> f586c = new HashMap();

    /* renamed from: a */
    protected Object f587a;

    /* renamed from: b */
    protected F f588b;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract F mo547a(short s);

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Object mo550a(C0209dd ddVar, C0194cy cyVar) throws C0172ck;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract Object mo551a(C0209dd ddVar, short s) throws C0172ck;

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public abstract void mo554b(F f, Object obj) throws ClassCastException;

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public abstract C0194cy mo556c(F f);

    /* access modifiers changed from: protected */
    /* renamed from: c */
    public abstract void mo558c(C0209dd ddVar) throws C0172ck;

    /* access modifiers changed from: protected */
    /* renamed from: d */
    public abstract void mo560d(C0209dd ddVar) throws C0172ck;

    /* access modifiers changed from: protected */
    /* renamed from: e */
    public abstract C0214di mo562e();

    protected C0176co() {
        this.f588b = null;
        this.f587a = null;
    }

    static {
        f586c.put(C0220dn.class, new C0179b());
        f586c.put(C0221do.class, new C0181d());
    }

    protected C0176co(F f, Object obj) {
        mo553a(f, obj);
    }

    protected C0176co(C0176co<T, F> coVar) {
        if (!coVar.getClass().equals(getClass())) {
            throw new ClassCastException();
        }
        this.f588b = coVar.f588b;
        this.f587a = m990a(coVar.f587a);
    }

    /* renamed from: a */
    private static Object m990a(Object obj) {
        if (obj instanceof C0164ce) {
            return ((C0164ce) obj).mo276p();
        }
        if (obj instanceof ByteBuffer) {
            return C0165cf.m965d((ByteBuffer) obj);
        }
        if (obj instanceof List) {
            return m991a((List) obj);
        }
        if (obj instanceof Set) {
            return m993a((Set) obj);
        }
        if (obj instanceof Map) {
            return m992a((Map<Object, Object>) (Map) obj);
        }
        return obj;
    }

    /* renamed from: a */
    private static Map m992a(Map<Object, Object> map) {
        HashMap hashMap = new HashMap();
        for (Map.Entry next : map.entrySet()) {
            hashMap.put(m990a(next.getKey()), m990a(next.getValue()));
        }
        return hashMap;
    }

    /* renamed from: a */
    private static Set m993a(Set set) {
        HashSet hashSet = new HashSet();
        for (Object a : set) {
            hashSet.add(m990a(a));
        }
        return hashSet;
    }

    /* renamed from: a */
    private static List m991a(List list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (Object a : list) {
            arrayList.add(m990a(a));
        }
        return arrayList;
    }

    /* renamed from: a */
    public F mo546a() {
        return this.f588b;
    }

    /* renamed from: c */
    public Object mo557c() {
        return this.f587a;
    }

    /* renamed from: a */
    public Object mo549a(F f) {
        if (f == this.f588b) {
            return mo557c();
        }
        throw new IllegalArgumentException("Cannot get the value of field " + f + " because union's set field is " + this.f588b);
    }

    /* renamed from: a */
    public Object mo548a(int i) {
        return mo549a(mo547a((short) i));
    }

    /* renamed from: d */
    public boolean mo561d() {
        return this.f588b != null;
    }

    /* renamed from: b */
    public boolean mo555b(F f) {
        return this.f588b == f;
    }

    /* renamed from: c */
    public boolean mo559c(int i) {
        return mo555b(mo547a((short) i));
    }

    /* renamed from: a */
    public void mo253a(C0209dd ddVar) throws C0172ck {
        f586c.get(ddVar.mo628D()).mo283b().mo281b(ddVar, this);
    }

    /* renamed from: a */
    public void mo553a(F f, Object obj) {
        mo554b(f, obj);
        this.f588b = f;
        this.f587a = obj;
    }

    /* renamed from: a */
    public void mo552a(int i, Object obj) {
        mo553a(mo547a((short) i), obj);
    }

    /* renamed from: b */
    public void mo258b(C0209dd ddVar) throws C0172ck {
        f586c.get(ddVar.mo628D()).mo283b().mo279a(ddVar, this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        sb.append(getClass().getSimpleName());
        sb.append(" ");
        if (mo546a() != null) {
            Object c = mo557c();
            sb.append(mo556c(mo546a()).f651a);
            sb.append(":");
            if (c instanceof ByteBuffer) {
                C0165cf.m960a((ByteBuffer) c, sb);
            } else {
                sb.append(c.toString());
            }
        }
        sb.append(">");
        return sb.toString();
    }

    /* renamed from: b */
    public final void mo257b() {
        this.f588b = null;
        this.f587a = null;
    }

    /* renamed from: com.umeng.analytics.pro.co$b */
    /* compiled from: TUnion */
    private static class C0179b implements C0219dm {
        private C0179b() {
        }

        /* renamed from: a */
        public C0178a mo283b() {
            return new C0178a();
        }
    }

    /* renamed from: com.umeng.analytics.pro.co$a */
    /* compiled from: TUnion */
    private static class C0178a extends C0220dn<C0176co> {
        private C0178a() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0176co coVar) throws C0172ck {
            coVar.f588b = null;
            coVar.f587a = null;
            ddVar.mo602j();
            C0194cy l = ddVar.mo604l();
            coVar.f587a = coVar.mo550a(ddVar, l);
            if (coVar.f587a != null) {
                coVar.f588b = coVar.mo547a(l.f653c);
            }
            ddVar.mo605m();
            ddVar.mo604l();
            ddVar.mo603k();
        }

        /* renamed from: b */
        public void mo279a(C0209dd ddVar, C0176co coVar) throws C0172ck {
            if (coVar.mo546a() == null || coVar.mo557c() == null) {
                throw new C0210de("Cannot write a TUnion with no set value!");
            }
            ddVar.mo586a(coVar.mo562e());
            ddVar.mo581a(coVar.mo556c(coVar.f588b));
            coVar.mo558c(ddVar);
            ddVar.mo593c();
            ddVar.mo595d();
            ddVar.mo592b();
        }
    }

    /* renamed from: com.umeng.analytics.pro.co$d */
    /* compiled from: TUnion */
    private static class C0181d implements C0219dm {
        private C0181d() {
        }

        /* renamed from: a */
        public C0180c mo283b() {
            return new C0180c();
        }
    }

    /* renamed from: com.umeng.analytics.pro.co$c */
    /* compiled from: TUnion */
    private static class C0180c extends C0221do<C0176co> {
        private C0180c() {
        }

        /* renamed from: a */
        public void mo281b(C0209dd ddVar, C0176co coVar) throws C0172ck {
            coVar.f588b = null;
            coVar.f587a = null;
            short v = ddVar.mo614v();
            coVar.f587a = coVar.mo551a(ddVar, v);
            if (coVar.f587a != null) {
                coVar.f588b = coVar.mo547a(v);
            }
        }

        /* renamed from: b */
        public void mo279a(C0209dd ddVar, C0176co coVar) throws C0172ck {
            if (coVar.mo546a() == null || coVar.mo557c() == null) {
                throw new C0210de("Cannot write a TUnion with no set value!");
            }
            ddVar.mo589a(coVar.f588b.mo287a());
            coVar.mo560d(ddVar);
        }
    }
}
