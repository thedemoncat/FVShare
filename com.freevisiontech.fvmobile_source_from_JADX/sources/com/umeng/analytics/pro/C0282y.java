package com.umeng.analytics.pro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* renamed from: com.umeng.analytics.pro.y */
/* compiled from: AbstractIdTracker */
public abstract class C0282y {

    /* renamed from: a */
    private final int f970a = 10;

    /* renamed from: b */
    private final int f971b = 20;

    /* renamed from: c */
    private final String f972c;

    /* renamed from: d */
    private List<C0078bi> f973d;

    /* renamed from: e */
    private C0085bj f974e;

    /* renamed from: f */
    public abstract String mo122f();

    public C0282y(String str) {
        this.f972c = str;
    }

    /* renamed from: a */
    public boolean mo751a() {
        return mo161g();
    }

    /* renamed from: b */
    public String mo752b() {
        return this.f972c;
    }

    /* renamed from: c */
    public boolean mo753c() {
        if (this.f974e == null || this.f974e.mo303i() <= 20) {
            return true;
        }
        return false;
    }

    /* renamed from: g */
    private boolean mo161g() {
        C0085bj bjVar = this.f974e;
        String c = bjVar == null ? null : bjVar.mo296c();
        int i = bjVar == null ? 0 : bjVar.mo303i();
        String a = mo747a(mo122f());
        if (a == null || a.equals(c)) {
            return false;
        }
        if (bjVar == null) {
            bjVar = new C0085bj();
        }
        bjVar.mo292a(a);
        bjVar.mo291a(System.currentTimeMillis());
        bjVar.mo290a(i + 1);
        C0078bi biVar = new C0078bi();
        biVar.mo252a(this.f972c);
        biVar.mo260c(a);
        biVar.mo255b(c);
        biVar.mo251a(bjVar.mo300f());
        if (this.f973d == null) {
            this.f973d = new ArrayList(2);
        }
        this.f973d.add(biVar);
        if (this.f973d.size() > 10) {
            this.f973d.remove(0);
        }
        this.f974e = bjVar;
        return true;
    }

    /* renamed from: d */
    public C0085bj mo754d() {
        return this.f974e;
    }

    /* renamed from: a */
    public void mo748a(C0085bj bjVar) {
        this.f974e = bjVar;
    }

    /* renamed from: e */
    public List<C0078bi> mo755e() {
        return this.f973d;
    }

    /* renamed from: a */
    public void mo750a(List<C0078bi> list) {
        this.f973d = list;
    }

    /* renamed from: a */
    public String mo747a(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.length() == 0 || "0".equals(trim) || "unknown".equals(trim.toLowerCase(Locale.US))) {
            return null;
        }
        return trim;
    }

    /* renamed from: a */
    public void mo749a(C0092bk bkVar) {
        this.f974e = bkVar.mo325d().get(this.f972c);
        List<C0078bi> i = bkVar.mo330i();
        if (i != null && i.size() > 0) {
            if (this.f973d == null) {
                this.f973d = new ArrayList();
            }
            for (C0078bi next : i) {
                if (this.f972c.equals(next.f337a)) {
                    this.f973d.add(next);
                }
            }
        }
    }
}
