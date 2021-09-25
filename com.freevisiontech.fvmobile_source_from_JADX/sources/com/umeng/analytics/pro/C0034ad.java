package com.umeng.analytics.pro;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.C0015a;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* renamed from: com.umeng.analytics.pro.ad */
/* compiled from: IdTracker */
public class C0034ad {

    /* renamed from: a */
    public static C0034ad f133a;

    /* renamed from: b */
    private final String f134b = "umeng_it.cache";

    /* renamed from: c */
    private File f135c;

    /* renamed from: d */
    private C0092bk f136d = null;

    /* renamed from: e */
    private long f137e;

    /* renamed from: f */
    private long f138f;

    /* renamed from: g */
    private Set<C0282y> f139g = new HashSet();

    /* renamed from: h */
    private C0035a f140h = null;

    C0034ad(Context context) {
        this.f135c = new File(context.getFilesDir(), "umeng_it.cache");
        this.f138f = C0015a.f22i;
        this.f140h = new C0035a(context);
        this.f140h.mo133b();
    }

    /* renamed from: a */
    public static synchronized C0034ad m126a(Context context) {
        C0034ad adVar;
        synchronized (C0034ad.class) {
            if (f133a == null) {
                f133a = new C0034ad(context);
                f133a.mo125a((C0282y) new C0036ae(context));
                f133a.mo125a((C0282y) new C0283z(context));
                f133a.mo125a((C0282y) new C0045am(context));
                f133a.mo125a((C0282y) new C0033ac(context));
                f133a.mo125a((C0282y) new C0032ab(context));
                f133a.mo125a((C0282y) new C0039ag(context));
                f133a.mo125a((C0282y) new C0042aj());
                f133a.mo125a((C0282y) new C0046an(context));
                C0041ai aiVar = new C0041ai(context);
                if (aiVar.mo161g()) {
                    f133a.mo125a((C0282y) aiVar);
                    f133a.mo125a((C0282y) new C0040ah(context));
                    aiVar.mo163i();
                }
                f133a.mo129e();
            }
            adVar = f133a;
        }
        return adVar;
    }

    /* renamed from: a */
    public boolean mo125a(C0282y yVar) {
        if (this.f140h.mo132a(yVar.mo752b())) {
            return this.f139g.add(yVar);
        }
        return false;
    }

    /* renamed from: a */
    public void mo124a(long j) {
        this.f138f = j;
    }

    /* renamed from: a */
    public void mo123a() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f137e >= this.f138f) {
            boolean z = false;
            for (C0282y next : this.f139g) {
                if (next.mo753c()) {
                    if (next.mo751a()) {
                        z = true;
                        if (!next.mo753c()) {
                            this.f140h.mo134b(next.mo752b());
                        }
                    }
                    z = z;
                }
            }
            if (z) {
                m128g();
                this.f140h.mo131a();
                mo130f();
            }
            this.f137e = currentTimeMillis;
        }
    }

    /* renamed from: b */
    public C0092bk mo126b() {
        return this.f136d;
    }

    /* renamed from: g */
    private void m128g() {
        C0092bk bkVar = new C0092bk();
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        for (C0282y next : this.f139g) {
            if (next.mo753c()) {
                if (next.mo754d() != null) {
                    hashMap.put(next.mo752b(), next.mo754d());
                }
                if (next.mo755e() != null && !next.mo755e().isEmpty()) {
                    arrayList.addAll(next.mo755e());
                }
            }
        }
        bkVar.mo317a((List<C0078bi>) arrayList);
        bkVar.mo318a((Map<String, C0085bj>) hashMap);
        synchronized (this) {
            this.f136d = bkVar;
        }
    }

    /* renamed from: c */
    public String mo127c() {
        return null;
    }

    /* renamed from: d */
    public void mo128d() {
        boolean z = false;
        for (C0282y next : this.f139g) {
            if (next.mo753c()) {
                if (next.mo755e() != null && !next.mo755e().isEmpty()) {
                    next.mo750a((List<C0078bi>) null);
                    z = true;
                }
                z = z;
            }
        }
        if (z) {
            this.f136d.mo322b(false);
            mo130f();
        }
    }

    /* renamed from: e */
    public void mo129e() {
        C0092bk h = m129h();
        if (h != null) {
            ArrayList<C0282y> arrayList = new ArrayList<>(this.f139g.size());
            synchronized (this) {
                this.f136d = h;
                for (C0282y next : this.f139g) {
                    next.mo749a(this.f136d);
                    if (!next.mo753c()) {
                        arrayList.add(next);
                    }
                }
                for (C0282y remove : arrayList) {
                    this.f139g.remove(remove);
                }
            }
            m128g();
        }
    }

    /* renamed from: f */
    public void mo130f() {
        if (this.f136d != null) {
            m127a(this.f136d);
        }
    }

    /* renamed from: h */
    private C0092bk m129h() {
        FileInputStream fileInputStream;
        Throwable th;
        if (!this.f135c.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(this.f135c);
            try {
                byte[] b = C0136bu.m820b((InputStream) fileInputStream);
                C0092bk bkVar = new C0092bk();
                new C0169ch().mo533a((C0164ce) bkVar, b);
                C0136bu.m821c(fileInputStream);
                return bkVar;
            } catch (Exception e) {
                e = e;
                try {
                    e.printStackTrace();
                    C0136bu.m821c(fileInputStream);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    C0136bu.m821c(fileInputStream);
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            fileInputStream = null;
            e.printStackTrace();
            C0136bu.m821c(fileInputStream);
            return null;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            C0136bu.m821c(fileInputStream);
            throw th;
        }
    }

    /* renamed from: a */
    private void m127a(C0092bk bkVar) {
        byte[] a;
        if (bkVar != null) {
            try {
                synchronized (this) {
                    a = new C0175cn().mo544a(bkVar);
                }
                if (a != null) {
                    C0136bu.m816a(this.f135c, a);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.umeng.analytics.pro.ad$a */
    /* compiled from: IdTracker */
    public static class C0035a {

        /* renamed from: a */
        private Context f141a;

        /* renamed from: b */
        private Set<String> f142b = new HashSet();

        public C0035a(Context context) {
            this.f141a = context;
        }

        /* renamed from: a */
        public boolean mo132a(String str) {
            return !this.f142b.contains(str);
        }

        /* renamed from: b */
        public void mo134b(String str) {
            this.f142b.add(str);
        }

        /* renamed from: c */
        public void mo135c(String str) {
            this.f142b.remove(str);
        }

        /* renamed from: a */
        public void mo131a() {
            if (!this.f142b.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String append : this.f142b) {
                    sb.append(append);
                    sb.append(',');
                }
                sb.deleteCharAt(sb.length() - 1);
                C0067az.m285a(this.f141a).edit().putString("invld_id", sb.toString()).commit();
            }
        }

        /* renamed from: b */
        public void mo133b() {
            String[] split;
            String string = C0067az.m285a(this.f141a).getString("invld_id", (String) null);
            if (!TextUtils.isEmpty(string) && (split = string.split(",")) != null) {
                for (String str : split) {
                    if (!TextUtils.isEmpty(str)) {
                        this.f142b.add(str);
                    }
                }
            }
        }
    }
}
