package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.pro.C0048ao;
import com.umeng.analytics.pro.C0053aq;
import com.umeng.analytics.pro.C0057ar;
import com.umeng.analytics.pro.C0058as;
import com.umeng.analytics.pro.C0065ax;
import com.umeng.analytics.pro.C0067az;
import com.umeng.analytics.pro.C0071bb;
import com.umeng.analytics.pro.C0073bd;
import com.umeng.analytics.pro.C0133br;
import com.umeng.analytics.pro.C0135bt;
import com.umeng.analytics.pro.C0137bv;
import com.umeng.analytics.pro.C0138bw;
import com.umeng.analytics.pro.C0139bx;
import com.umeng.analytics.pro.C0151bz;
import com.umeng.analytics.pro.C0277w;
import com.umeng.analytics.pro.C0281x;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.b */
/* compiled from: InternalAgent */
public class C0016b implements C0065ax {

    /* renamed from: j */
    private static final String f40j = "sp";
    /* access modifiers changed from: private */

    /* renamed from: a */
    public Context f41a;

    /* renamed from: b */
    private C0137bv f42b;

    /* renamed from: c */
    private C0057ar f43c;

    /* renamed from: d */
    private C0073bd f44d;

    /* renamed from: e */
    private C0071bb f45e;

    /* renamed from: f */
    private C0058as f46f;

    /* renamed from: g */
    private Object f47g;
    /* access modifiers changed from: private */

    /* renamed from: h */
    public C0053aq f48h;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public C0048ao f49i;

    /* renamed from: k */
    private boolean f50k;

    /* renamed from: l */
    private JSONObject f51l;

    /* renamed from: m */
    private boolean f52m;

    private C0016b() {
        this.f41a = null;
        this.f43c = new C0057ar();
        this.f44d = new C0073bd();
        this.f45e = new C0071bb();
        this.f46f = null;
        this.f47g = new Object();
        this.f48h = null;
        this.f49i = null;
        this.f50k = false;
        this.f51l = null;
        this.f52m = false;
        this.f43c.mo187a((C0065ax) this);
    }

    /* renamed from: com.umeng.analytics.b$a */
    /* compiled from: InternalAgent */
    private static class C0022a {
        /* access modifiers changed from: private */

        /* renamed from: a */
        public static final C0016b f63a = new C0016b();

        private C0022a() {
        }
    }

    /* renamed from: a */
    public static C0016b m6a() {
        return C0022a.f63a;
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    /* renamed from: g */
    private synchronized void m11g(final android.content.Context r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 != 0) goto L_0x0005
        L_0x0003:
            monitor-exit(r3)
            return
        L_0x0005:
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            r1 = 13
            if (r0 <= r1) goto L_0x001a
            boolean r0 = r3.f52m     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            if (r0 != 0) goto L_0x001a
            r0 = 1
            r3.f52m = r0     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            com.umeng.analytics.b$1 r0 = new com.umeng.analytics.b$1     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            r0.<init>(r4)     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            com.umeng.analytics.pro.C0139bx.m859b(r0)     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
        L_0x001a:
            boolean r0 = r3.f50k     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            if (r0 != 0) goto L_0x0003
            android.content.Context r0 = r4.getApplicationContext()     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            r3.f41a = r0     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            r0 = 1
            r3.f50k = r0     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            com.umeng.analytics.pro.as r0 = r3.f46f     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            if (r0 != 0) goto L_0x0038
            java.lang.Object r1 = r3.f47g     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            com.umeng.analytics.pro.as r0 = new com.umeng.analytics.pro.as     // Catch:{ all -> 0x0043 }
            android.content.Context r2 = r3.f41a     // Catch:{ all -> 0x0043 }
            r0.<init>(r2)     // Catch:{ all -> 0x0043 }
            r3.f46f = r0     // Catch:{ all -> 0x0043 }
            monitor-exit(r1)     // Catch:{ all -> 0x0043 }
        L_0x0038:
            android.content.Context r0 = r3.f41a     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            com.umeng.analytics.pro.aq r0 = com.umeng.analytics.pro.C0053aq.m237b(r0)     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            r3.f48h = r0     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
            goto L_0x0003
        L_0x0041:
            r0 = move-exception
            goto L_0x0003
        L_0x0043:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0043 }
            throw r0     // Catch:{ Throwable -> 0x0041, all -> 0x0046 }
        L_0x0046:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.C0016b.m11g(android.content.Context):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo66a(String str) {
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                if (this.f44d != null) {
                    this.f44d.mo229a(str);
                }
            } catch (Throwable th) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo75b(String str) {
        if (!AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
            try {
                if (this.f44d != null) {
                    this.f44d.mo230b(str);
                }
            } catch (Throwable th) {
            }
        }
    }

    /* renamed from: a */
    public void mo65a(C0137bv bvVar) {
        this.f42b = bvVar;
    }

    /* renamed from: a */
    public void mo53a(Context context, int i) {
        AnalyticsConfig.m0a(context, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo52a(final Context context) {
        if (context == null) {
            try {
                C0138bw.m849e("unexpected null context in onResume");
            } catch (Throwable th) {
                C0138bw.m851e("Exception occurred in Mobclick.onResume(). ", th);
            }
        } else {
            if (AnalyticsConfig.ACTIVITY_DURATION_OPEN && this.f44d != null) {
                this.f44d.mo229a(context.getClass().getName());
            }
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            C0139bx.m857a(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    C0016b.this.m12h(context.getApplicationContext());
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo73b(final Context context) {
        if (context == null) {
            try {
                C0138bw.m849e("unexpected null context in onPause");
            } catch (Throwable th) {
                if (C0138bw.f509a) {
                    C0138bw.m851e("Exception occurred in Mobclick.onRause(). ", th);
                }
            }
        } else {
            if (AnalyticsConfig.ACTIVITY_DURATION_OPEN && this.f44d != null) {
                this.f44d.mo230b(context.getClass().getName());
            }
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            C0139bx.m857a(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    C0016b.this.m13i(context.getApplicationContext());
                }
            });
        }
    }

    /* renamed from: b */
    public C0071bb mo71b() {
        return this.f45e;
    }

    /* renamed from: a */
    public void mo58a(Context context, String str, HashMap<String, Object> hashMap) {
        try {
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            if (this.f46f != null) {
                this.f46f.mo194b(str, hashMap);
            }
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                C0138bw.m853e(th);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo55a(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            if (context == null) {
                C0138bw.m849e("unexpected null context in reportError");
                return;
            }
            try {
                if (!this.f50k || !this.f52m) {
                    m11g(context);
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("ts", System.currentTimeMillis());
                jSONObject.put(C0281x.f909aH, 2);
                jSONObject.put(C0281x.f910aI, str);
                C0277w.m1400a(this.f41a).mo744a(C0071bb.m307a(), jSONObject.toString(), 2);
            } catch (Throwable th) {
                if (C0138bw.f509a) {
                    C0138bw.m853e(th);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo61a(Context context, Throwable th) {
        if (context != null && th != null) {
            try {
                mo55a(this.f41a, C0133br.m752a(th));
            } catch (Throwable th2) {
                if (C0138bw.f509a) {
                    C0138bw.m853e(th2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: h */
    public void m12h(Context context) {
        try {
            if (this.f41a == null && context != null) {
                this.f41a = context.getApplicationContext();
            }
            if (this.f45e != null) {
                this.f45e.mo215c(this.f41a == null ? context.getApplicationContext() : this.f41a);
            }
            if (this.f42b != null) {
                this.f42b.mo99a();
            }
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: i */
    public void m13i(Context context) {
        try {
            if (this.f41a == null && context != null) {
                this.f41a = context.getApplicationContext();
            }
            if (this.f41a != null) {
                if (this.f45e != null) {
                    this.f45e.mo216d(this.f41a);
                }
                C0073bd.m332a(this.f41a);
                C0048ao.m197a(this.f41a);
                if (this.f48h != null) {
                    this.f48h.mo185a(this.f41a).mo176a(this.f41a);
                }
            }
            if (this.f42b != null) {
                this.f42b.mo109b();
            }
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public void mo78c(Context context) {
        try {
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            if (this.f48h != null) {
                this.f48h.mo175a();
            }
        } catch (Throwable th) {
        }
    }

    /* renamed from: a */
    public void mo63a(Context context, List<String> list, int i, String str) {
    }

    /* renamed from: a */
    public void mo57a(Context context, String str, String str2, long j, int i) {
        try {
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            synchronized (this.f47g) {
                if (this.f46f != null) {
                    this.f46f.mo190a(str, str2, j, i);
                }
            }
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                C0138bw.m853e(th);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo60a(Context context, String str, Map<String, Object> map, long j) {
        try {
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            if (this.f46f != null) {
                this.f46f.mo192a(str, map, j);
            }
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                C0138bw.m853e(th);
            }
        }
    }

    /* renamed from: a */
    public void mo59a(Context context, String str, Map<String, Object> map) {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public void mo82d(Context context) {
        try {
            if (this.f49i != null) {
                this.f49i.mo166b();
            }
            if (this.f44d != null) {
                this.f44d.mo228a();
            }
            if (context != null) {
                m13i(context);
                C0067az.m285a(context).edit().commit();
            } else if (this.f41a != null) {
                m13i(this.f41a);
                C0067az.m285a(this.f41a).edit().commit();
            }
            C0139bx.m856a();
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                th.printStackTrace();
            }
        }
    }

    /* renamed from: a */
    public void mo68a(Throwable th) {
        try {
            if (this.f44d != null) {
                this.f44d.mo228a();
            }
            if (this.f49i != null) {
                this.f49i.mo166b();
            }
            if (this.f41a != null) {
                if (!(th == null || this.f48h == null)) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("ts", System.currentTimeMillis());
                    jSONObject.put(C0281x.f909aH, 1);
                    jSONObject.put(C0281x.f910aI, C0133br.m752a(th));
                    C0277w.m1400a(this.f41a).mo744a(C0071bb.m307a(), jSONObject.toString(), 1);
                }
                m13i(this.f41a);
                C0067az.m285a(this.f41a).edit().commit();
            }
            C0139bx.m856a();
        } catch (Throwable th2) {
            if (C0138bw.f509a) {
                C0138bw.m851e("Exception in onAppCrash", th2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo67a(final String str, final String str2) {
        try {
            C0139bx.m857a(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    String[] a = C0023c.m59a(C0016b.this.f41a);
                    if (a == null || !str.equals(a[0]) || !str2.equals(a[1])) {
                        if (C0016b.this.f48h != null) {
                            C0016b.this.f48h.mo185a(C0016b.this.f41a).mo176a(C0016b.this.f41a);
                        }
                        boolean e = C0016b.this.mo71b().mo217e(C0016b.this.f41a);
                        C0053aq.m237b(C0016b.this.f41a).mo180b();
                        if (e) {
                            C0016b.this.mo71b().mo218f(C0016b.this.f41a);
                        }
                        C0023c.m58a(C0016b.this.f41a, str, str2);
                    }
                }
            });
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                C0138bw.m851e(" Excepthon  in  onProfileSignIn", th);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public void mo77c() {
        try {
            C0139bx.m857a(new C0151bz() {
                /* renamed from: a */
                public void mo87a() {
                    String[] a = C0023c.m59a(C0016b.this.f41a);
                    if (a != null && !TextUtils.isEmpty(a[0]) && !TextUtils.isEmpty(a[1])) {
                        if (C0016b.this.f48h != null) {
                            C0016b.this.f48h.mo185a(C0016b.this.f41a).mo176a(C0016b.this.f41a);
                        }
                        boolean e = C0016b.this.mo71b().mo217e(C0016b.this.f41a);
                        C0053aq.m237b(C0016b.this.f41a).mo180b();
                        if (e) {
                            C0016b.this.mo71b().mo218f(C0016b.this.f41a);
                        }
                        C0023c.m60b(C0016b.this.f41a);
                    }
                }
            });
        } catch (Throwable th) {
            if (C0138bw.f509a) {
                C0138bw.m851e(" Excepthon  in  onProfileSignOff", th);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo70a(boolean z) {
        AnalyticsConfig.CATCH_EXCEPTION = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo69a(GL10 gl10) {
        String[] a = C0135bt.m779a(gl10);
        if (a.length == 2) {
            AnalyticsConfig.GPU_VENDER = a[0];
            AnalyticsConfig.GPU_RENDERER = a[1];
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo76b(boolean z) {
        AnalyticsConfig.ACTIVITY_DURATION_OPEN = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public void mo80c(boolean z) {
        C0015a.f17d = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public void mo83d(boolean z) {
        C0138bw.f509a = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public void mo85e(boolean z) {
        AnalyticsConfig.m3a(z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo50a(double d, double d2) {
        if (AnalyticsConfig.f0a == null) {
            AnalyticsConfig.f0a = new double[2];
        }
        AnalyticsConfig.f0a[0] = d;
        AnalyticsConfig.f0a[1] = d2;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo51a(long j) {
        AnalyticsConfig.sLatentWindow = ((int) j) * 1000;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo54a(Context context, MobclickAgent.EScenarioType eScenarioType) {
        if (context != null) {
            this.f41a = context.getApplicationContext();
        }
        if (eScenarioType != null) {
            mo53a(this.f41a, eScenarioType.toValue());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo74b(Context context, String str) {
        if (context != null) {
            this.f41a = context.getApplicationContext();
        }
        AnalyticsConfig.m4b(this.f41a, str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public void mo64a(MobclickAgent.UMAnalyticsConfig uMAnalyticsConfig) {
        if (uMAnalyticsConfig.mContext != null) {
            this.f41a = uMAnalyticsConfig.mContext.getApplicationContext();
        }
        if (!TextUtils.isEmpty(uMAnalyticsConfig.mAppkey)) {
            AnalyticsConfig.m1a(uMAnalyticsConfig.mContext, uMAnalyticsConfig.mAppkey);
            if (!TextUtils.isEmpty(uMAnalyticsConfig.mChannelId)) {
                AnalyticsConfig.m2a(uMAnalyticsConfig.mChannelId);
            }
            AnalyticsConfig.CATCH_EXCEPTION = uMAnalyticsConfig.mIsCrashEnable;
            mo54a(this.f41a, uMAnalyticsConfig.mType);
            return;
        }
        C0138bw.m849e("the appkey is null!");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public void mo72b(long j) {
        AnalyticsConfig.kContinueSessionMillis = j;
    }

    /* renamed from: a */
    public void mo56a(Context context, String str, Object obj) {
    }

    /* renamed from: c */
    public void mo79c(Context context, String str) {
    }

    /* renamed from: d */
    public Object mo81d(Context context, String str) {
        if (this.f41a != null || context == null) {
            return null;
        }
        this.f41a = context.getApplicationContext();
        return null;
    }

    /* renamed from: e */
    public String mo84e(Context context) {
        if (this.f41a != null || context == null) {
            return null;
        }
        this.f41a = context.getApplicationContext();
        return null;
    }

    /* renamed from: j */
    private JSONObject m14j(Context context) {
        if (this.f41a == null && context != null) {
            this.f41a = context.getApplicationContext();
        }
        try {
            String string = C0067az.m285a(this.f41a).getString(f40j, (String) null);
            if (!TextUtils.isEmpty(string)) {
                return new JSONObject(string);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }

    /* renamed from: f */
    public void mo86f(Context context) {
        if (this.f41a == null && context != null) {
            this.f41a = context.getApplicationContext();
        }
    }

    /* renamed from: a */
    public void mo62a(Context context, List<String> list) {
        try {
            if (!this.f50k || !this.f52m) {
                m11g(context);
            }
            if (this.f46f != null) {
                this.f46f.mo189a(context, list);
            }
        } catch (Throwable th) {
            C0138bw.m853e(th);
        }
    }
}
