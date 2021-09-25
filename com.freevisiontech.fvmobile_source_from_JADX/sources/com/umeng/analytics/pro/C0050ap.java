package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.C0015a;
import com.umeng.analytics.pro.C0037af;
import com.umeng.analytics.pro.C0140by;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.ap */
/* compiled from: CacheImpl */
public final class C0050ap implements C0061at, C0066ay {
    /* access modifiers changed from: private */

    /* renamed from: j */
    public static Context f197j = null;

    /* renamed from: q */
    private static final String f198q = "thtstart";

    /* renamed from: r */
    private static final String f199r = "gkvc";

    /* renamed from: s */
    private static final String f200s = "ekvc";

    /* renamed from: a */
    String f201a = null;

    /* renamed from: b */
    private C0155ca f202b = null;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public C0072bc f203c = null;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public C0075bf f204d = null;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public C0074be f205e = null;
    /* access modifiers changed from: private */

    /* renamed from: f */
    public C0076bg f206f = null;

    /* renamed from: g */
    private C0052a f207g = null;
    /* access modifiers changed from: private */

    /* renamed from: h */
    public C0037af.C0038a f208h = null;

    /* renamed from: i */
    private long f209i = 0;

    /* renamed from: k */
    private int f210k = 10;

    /* renamed from: l */
    private JSONArray f211l = new JSONArray();

    /* renamed from: m */
    private final int f212m = 5000;

    /* renamed from: n */
    private int f213n = 0;

    /* renamed from: o */
    private int f214o = 0;

    /* renamed from: p */
    private long f215p = 0;

    /* renamed from: t */
    private final long f216t = 28800000;

    public C0050ap(Context context) {
        f197j = context;
        this.f203c = new C0072bc(context);
        this.f202b = C0155ca.m887a(context);
        this.f208h = C0037af.m144a(context).mo140b();
        this.f207g = new C0052a();
        this.f205e = C0074be.m336a(f197j);
        this.f204d = C0075bf.m349a(f197j);
        this.f206f = C0076bg.m357a(f197j, this.f203c);
        SharedPreferences a = C0067az.m285a(f197j);
        this.f215p = a.getLong(f198q, 0);
        this.f213n = a.getInt(f199r, 0);
        this.f214o = a.getInt(f200s, 0);
        this.f201a = this.f208h.mo152b((String) null);
    }

    /* renamed from: a */
    public void mo175a() {
        if (C0135bt.m796l(f197j)) {
            m218d();
        } else {
            C0138bw.m831b("network is unavailable");
        }
    }

    /* renamed from: a */
    public void mo178a(Object obj) {
        if (this.f203c.mo220f()) {
            this.f209i = this.f203c.mo226l();
        }
        boolean z = true;
        if (obj instanceof JSONObject) {
            z = false;
            try {
                m212b((JSONObject) obj);
            } catch (Throwable th) {
            }
        }
        if (m208a(z)) {
            m218d();
        }
    }

    /* renamed from: b */
    private void m212b(JSONObject jSONObject) {
        try {
            if (2050 == jSONObject.getInt("__t")) {
                if (m216c(this.f213n)) {
                    this.f213n++;
                } else {
                    return;
                }
            } else if (2049 == jSONObject.getInt("__t")) {
                if (m216c(this.f214o)) {
                    this.f214o++;
                } else {
                    return;
                }
            }
            if (this.f211l.length() > this.f210k) {
                C0277w.m1400a(f197j).mo742a(this.f211l);
                this.f211l = new JSONArray();
            }
            if (this.f215p == 0) {
                this.f215p = System.currentTimeMillis();
            }
            this.f211l.put(jSONObject);
        } catch (Throwable th) {
        }
    }

    /* renamed from: b */
    public void mo180b() {
        m215c(mo174a(new int[0]));
    }

    /* renamed from: a */
    private void m205a(int i) {
        m215c(mo174a(i, (int) (System.currentTimeMillis() - this.f203c.mo227m())));
        C0139bx.m858a(new C0151bz() {
            /* renamed from: a */
            public void mo87a() {
                C0050ap.this.mo175a();
            }
        }, (long) i);
    }

    /* renamed from: c */
    private void m215c(JSONObject jSONObject) {
        C0031aa a;
        if (jSONObject != null) {
            try {
                C0034ad a2 = C0034ad.m126a(f197j);
                a2.mo123a();
                try {
                    String encodeToString = Base64.encodeToString(new C0175cn().mo544a(a2.mo126b()), 0);
                    if (!TextUtils.isEmpty(encodeToString)) {
                        JSONObject jSONObject2 = jSONObject.getJSONObject(C0015a.f10A);
                        jSONObject2.put(C0281x.f889O, encodeToString);
                        jSONObject.put(C0015a.f10A, jSONObject2);
                    }
                } catch (Exception e) {
                }
                byte[] bytes = String.valueOf(jSONObject).getBytes();
                if (bytes != null && !C0133br.m754a(f197j, bytes)) {
                    if (m221e()) {
                        a = C0031aa.m114b(f197j, AnalyticsConfig.getAppkey(f197j), bytes);
                    } else {
                        a = C0031aa.m111a(f197j, AnalyticsConfig.getAppkey(f197j), bytes);
                    }
                    byte[] c = a.mo120c();
                    C0155ca a3 = C0155ca.m887a(f197j);
                    a3.mo510g();
                    a3.mo501a(c);
                    a2.mo128d();
                }
            } catch (Exception e2) {
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.json.JSONObject mo174a(int... r9) {
        /*
            r8 = this;
            android.content.Context r0 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r0 = com.umeng.analytics.AnalyticsConfig.getAppkey(r0)     // Catch:{ Throwable -> 0x04bc }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x04bc }
            if (r0 == 0) goto L_0x0018
            java.lang.String r0 = "Appkey is missing ,Please check AndroidManifest.xml"
            com.umeng.analytics.pro.C0138bw.m849e((java.lang.String) r0)     // Catch:{ Throwable -> 0x04bc }
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r0.<init>()     // Catch:{ Throwable -> 0x04bc }
        L_0x0017:
            return r0
        L_0x0018:
            android.content.Context r0 = f197j     // Catch:{ Throwable -> 0x04bc }
            r8.mo176a((android.content.Context) r0)     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r0 = f197j     // Catch:{ Throwable -> 0x04bc }
            com.umeng.analytics.pro.w r0 = com.umeng.analytics.pro.C0277w.m1400a((android.content.Context) r0)     // Catch:{ Throwable -> 0x04bc }
            org.json.JSONObject r0 = r0.mo741a()     // Catch:{ Throwable -> 0x04bc }
            if (r0 != 0) goto L_0x002e
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r0.<init>()     // Catch:{ Throwable -> 0x04bc }
        L_0x002e:
            java.lang.String r1 = "body"
            org.json.JSONObject r1 = r0.getJSONObject(r1)     // Catch:{ Throwable -> 0x046b }
            r2 = r1
        L_0x0036:
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r1 = r2.toString()     // Catch:{ Throwable -> 0x04bc }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r1 = f197j     // Catch:{ Throwable -> 0x04bc }
            android.content.SharedPreferences r1 = com.umeng.analytics.pro.C0067az.m285a(r1)     // Catch:{ Throwable -> 0x04bc }
            if (r1 == 0) goto L_0x005d
            java.lang.String r4 = "userlevel"
            java.lang.String r5 = ""
            java.lang.String r4 = r1.getString(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x005d
            java.lang.String r5 = "userlevel"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x005d:
            com.umeng.analytics.pro.bc r4 = r8.f203c     // Catch:{ Throwable -> 0x04bc }
            boolean r4 = r4.mo220f()     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x0086
            long r4 = r8.f209i     // Catch:{ Throwable -> 0x04bc }
            r6 = 0
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 == 0) goto L_0x0086
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r4.<init>()     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "ts"
            long r6 = r8.f209i     // Catch:{ Throwable -> 0x04bc }
            r4.put(r5, r6)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "activate_msg"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "activate_msg"
            r3.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0086:
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r4.<init>()     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            com.umeng.analytics.pro.m r5 = com.umeng.analytics.pro.C0236m.m1296a((android.content.Context) r5)     // Catch:{ Throwable -> 0x04bc }
            org.json.JSONObject r5 = r5.mo701b()     // Catch:{ Throwable -> 0x04bc }
            if (r5 == 0) goto L_0x00a3
            int r6 = r5.length()     // Catch:{ Throwable -> 0x04bc }
            if (r6 <= 0) goto L_0x00a3
            java.lang.String r6 = "ag"
            r4.put(r6, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x00a3:
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            com.umeng.analytics.pro.m r5 = com.umeng.analytics.pro.C0236m.m1296a((android.content.Context) r5)     // Catch:{ Throwable -> 0x04bc }
            org.json.JSONObject r5 = r5.mo703c()     // Catch:{ Throwable -> 0x04bc }
            if (r5 == 0) goto L_0x00bb
            int r6 = r5.length()     // Catch:{ Throwable -> 0x04bc }
            if (r6 <= 0) goto L_0x00bb
            java.lang.String r6 = "ve_meta"
            r4.put(r6, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x00bb:
            int r5 = r4.length()     // Catch:{ Throwable -> 0x04bc }
            if (r5 <= 0) goto L_0x00cd
            java.lang.String r5 = "cc"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "cc"
            r3.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x00cd:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String[] r4 = com.umeng.analytics.C0023c.m59a(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x0110
            r5 = 0
            r5 = r4[r5]     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x0110
            r5 = 1
            r5 = r4[r5]     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x0110
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r5.<init>()     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r6 = "provider"
            r7 = 0
            r7 = r4[r7]     // Catch:{ Throwable -> 0x04bc }
            r5.put(r6, r7)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r6 = "puid"
            r7 = 1
            r4 = r4[r7]     // Catch:{ Throwable -> 0x04bc }
            r5.put(r6, r4)     // Catch:{ Throwable -> 0x04bc }
            int r4 = r5.length()     // Catch:{ Throwable -> 0x04bc }
            if (r4 <= 0) goto L_0x0110
            java.lang.String r4 = "active_user"
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "active_user"
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x0110:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            com.umeng.analytics.pro.be r4 = com.umeng.analytics.pro.C0074be.m336a((android.content.Context) r4)     // Catch:{ Throwable -> 0x04bc }
            boolean r4 = r4.mo232a()     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x011f
            r8.m219d((org.json.JSONObject) r2)     // Catch:{ Throwable -> 0x04bc }
        L_0x011f:
            com.umeng.analytics.pro.bf r4 = r8.f204d     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            r4.mo241a(r2, r5)     // Catch:{ Throwable -> 0x04bc }
            if (r9 == 0) goto L_0x0156
            int r4 = r9.length     // Catch:{ Throwable -> 0x04bc }
            r5 = 2
            if (r4 != r5) goto L_0x0156
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r4.<init>()     // Catch:{ Throwable -> 0x04bc }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r5.<init>()     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r6 = "interval"
            r7 = 0
            r7 = r9[r7]     // Catch:{ Throwable -> 0x04bc }
            int r7 = r7 / 1000
            r5.put(r6, r7)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r6 = "latency"
            r7 = 1
            r7 = r9[r7]     // Catch:{ Throwable -> 0x04bc }
            r5.put(r6, r7)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r6 = "latent"
            r4.put(r6, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "control_policy"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0156:
            int r4 = r2.length()     // Catch:{ Throwable -> 0x04bc }
            if (r4 <= 0) goto L_0x0474
            java.lang.String r4 = "body"
            r0.put(r4, r2)     // Catch:{ Throwable -> 0x04bc }
        L_0x0162:
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r2.<init>()     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "appkey"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.AnalyticsConfig.getAppkey(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "channel"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.AnalyticsConfig.getChannel(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = com.umeng.analytics.AnalyticsConfig.getSecretKey(r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = com.umeng.analytics.pro.C0136bu.m813a((java.lang.String) r4)     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x0195
            java.lang.String r5 = "secret"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0195:
            java.lang.String r4 = "display_name"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m807w(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "package_name"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m804t(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "app_signature"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m805u(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            if (r1 != 0) goto L_0x01c1
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x049d }
            android.content.SharedPreferences r1 = com.umeng.analytics.pro.C0067az.m285a(r4)     // Catch:{ Throwable -> 0x049d }
        L_0x01c1:
            if (r1 == 0) goto L_0x01e7
            java.lang.String r4 = "vers_name"
            java.lang.String r5 = ""
            java.lang.String r4 = r1.getString(r4, r5)     // Catch:{ Throwable -> 0x049d }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x049d }
            if (r5 != 0) goto L_0x047f
            java.lang.String r5 = "app_version"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x049d }
            java.lang.String r4 = "version_code"
            java.lang.String r5 = "vers_code"
            r6 = 0
            int r5 = r1.getInt(r5, r6)     // Catch:{ Throwable -> 0x049d }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x049d }
        L_0x01e7:
            java.lang.String r4 = com.umeng.analytics.AnalyticsConfig.mWrapperType     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x01ff
            java.lang.String r4 = com.umeng.analytics.AnalyticsConfig.mWrapperVersion     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x01ff
            java.lang.String r4 = "wrapper_type"
            java.lang.String r5 = com.umeng.analytics.AnalyticsConfig.mWrapperType     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "wrapper_version"
            java.lang.String r5 = com.umeng.analytics.AnalyticsConfig.mWrapperVersion     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x01ff:
            java.lang.String r4 = "sdk_type"
            java.lang.String r5 = "Android"
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "sdk_version"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.AnalyticsConfig.getSDKVersion(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "vertical_type"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            int r5 = com.umeng.analytics.AnalyticsConfig.getVerticalType(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "idmd5"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m786d(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "cpu"
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m773a()     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "os"
            java.lang.String r5 = "Android"
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "os_version"
            java.lang.String r5 = android.os.Build.VERSION.RELEASE     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            int[] r4 = com.umeng.analytics.pro.C0135bt.m802r(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x0273
            java.lang.String r5 = "resolution"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04bc }
            r6.<init>()     // Catch:{ Throwable -> 0x04bc }
            r7 = 1
            r7 = r4[r7]     // Catch:{ Throwable -> 0x04bc }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r7 = "*"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Throwable -> 0x04bc }
            r7 = 0
            r4 = r4[r7]     // Catch:{ Throwable -> 0x04bc }
            java.lang.StringBuilder r4 = r6.append(r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x04bc }
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0273:
            java.lang.String r4 = "mc"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m801q(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_id"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m784c(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_model"
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_board"
            java.lang.String r5 = android.os.Build.BOARD     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_brand"
            java.lang.String r5 = android.os.Build.BRAND     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_manutime"
            long r6 = android.os.Build.TIME     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r6)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_manufacturer"
            java.lang.String r5 = android.os.Build.MANUFACTURER     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_manuid"
            java.lang.String r5 = android.os.Build.ID     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "device_name"
            java.lang.String r5 = android.os.Build.DEVICE     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = com.umeng.analytics.pro.C0135bt.m808x(r4)     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x02d5
            java.lang.String r5 = "sub_os_name"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x02d5:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = com.umeng.analytics.pro.C0135bt.m809y(r4)     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x02e7
            java.lang.String r5 = "sub_os_version"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x02e7:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String[] r4 = com.umeng.analytics.pro.C0135bt.m794j(r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "Wi-Fi"
            r6 = 0
            r6 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = r5.equals(r6)     // Catch:{ Throwable -> 0x04bc }
            if (r5 == 0) goto L_0x04cd
            java.lang.String r5 = "access"
            java.lang.String r6 = "wifi"
            r2.put(r5, r6)     // Catch:{ Throwable -> 0x04bc }
        L_0x0302:
            java.lang.String r5 = ""
            r6 = 1
            r6 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = r5.equals(r6)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x0317
            java.lang.String r5 = "access_subtype"
            r6 = 1
            r4 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0317:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = com.umeng.analytics.pro.C0135bt.m787e(r4)     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r5 != 0) goto L_0x04ef
            java.lang.String r5 = "mccmnc"
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
        L_0x0329:
            android.content.Context r4 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String[] r4 = com.umeng.analytics.pro.C0135bt.m799o(r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "country"
            r6 = 0
            r6 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            r2.put(r5, r6)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = "language"
            r6 = 1
            r4 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            r2.put(r5, r4)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "timezone"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            int r5 = com.umeng.analytics.pro.C0135bt.m797m(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "carrier"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m792h(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "successful_requests"
            java.lang.String r5 = "successful_request"
            r6 = 0
            int r5 = r1.getInt(r5, r6)     // Catch:{ Exception -> 0x04fd }
            r2.put(r4, r5)     // Catch:{ Exception -> 0x04fd }
            java.lang.String r4 = "failed_requests"
            java.lang.String r5 = "failed_requests"
            r6 = 0
            int r5 = r1.getInt(r5, r6)     // Catch:{ Exception -> 0x04fd }
            r2.put(r4, r5)     // Catch:{ Exception -> 0x04fd }
            java.lang.String r4 = "req_time"
            java.lang.String r5 = "last_request_spent_ms"
            r6 = 0
            int r5 = r1.getInt(r5, r6)     // Catch:{ Exception -> 0x04fd }
            r2.put(r4, r5)     // Catch:{ Exception -> 0x04fd }
        L_0x0383:
            android.content.Context r4 = f197j     // Catch:{ Exception -> 0x04fa }
            com.umeng.analytics.pro.af r4 = com.umeng.analytics.pro.C0037af.m144a((android.content.Context) r4)     // Catch:{ Exception -> 0x04fa }
            com.umeng.analytics.pro.bl r4 = r4.mo136a()     // Catch:{ Exception -> 0x04fa }
            if (r4 == 0) goto L_0x03a3
            com.umeng.analytics.pro.cn r5 = new com.umeng.analytics.pro.cn     // Catch:{ Exception -> 0x04fa }
            r5.<init>()     // Catch:{ Exception -> 0x04fa }
            byte[] r4 = r5.mo544a(r4)     // Catch:{ Exception -> 0x04fa }
            java.lang.String r5 = "imprint"
            r6 = 0
            java.lang.String r4 = android.util.Base64.encodeToString(r4, r6)     // Catch:{ Exception -> 0x04fa }
            r2.put(r5, r4)     // Catch:{ Exception -> 0x04fa }
        L_0x03a3:
            java.lang.String r4 = "header"
            r0.put(r4, r2)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "sdk_version"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x03bf
            java.lang.String r4 = "sdk_version"
            java.lang.String r5 = "sdk_version"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x03bf:
            java.lang.String r4 = "device_id"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x03d5
            java.lang.String r4 = "device_id"
            java.lang.String r5 = "device_id"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x03d5:
            java.lang.String r4 = "device_model"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x03eb
            java.lang.String r4 = "device_model"
            java.lang.String r5 = "device_model"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x03eb:
            java.lang.String r4 = "version"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x0401
            java.lang.String r4 = "version"
            java.lang.String r5 = "version_code"
            int r5 = r2.getInt(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x0401:
            java.lang.String r4 = "appkey"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x0417
            java.lang.String r4 = "appkey"
            java.lang.String r5 = "appkey"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x0417:
            java.lang.String r4 = "channel"
            boolean r4 = r2.has(r4)     // Catch:{ Throwable -> 0x04bc }
            if (r4 == 0) goto L_0x042d
            java.lang.String r4 = "channel"
            java.lang.String r5 = "channel"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x04bc }
            r3.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
        L_0x042d:
            boolean r2 = r8.mo179a((org.json.JSONObject) r2)     // Catch:{ Throwable -> 0x04bc }
            if (r2 != 0) goto L_0x0434
            r0 = 0
        L_0x0434:
            boolean r2 = com.umeng.analytics.pro.C0138bw.f509a     // Catch:{ Throwable -> 0x04bc }
            if (r2 == 0) goto L_0x0445
            int r2 = r3.length()     // Catch:{ Throwable -> 0x04bc }
            if (r2 <= 0) goto L_0x0445
            java.lang.String r2 = java.lang.String.valueOf(r3)     // Catch:{ Throwable -> 0x04bc }
            com.umeng.analytics.pro.C0138bw.m831b((java.lang.String) r2)     // Catch:{ Throwable -> 0x04bc }
        L_0x0445:
            if (r1 == 0) goto L_0x0017
            android.content.SharedPreferences$Editor r1 = r1.edit()     // Catch:{ Throwable -> 0x0468 }
            java.lang.String r2 = "vers_name"
            r1.remove(r2)     // Catch:{ Throwable -> 0x0468 }
            java.lang.String r2 = "vers_code"
            r1.remove(r2)     // Catch:{ Throwable -> 0x0468 }
            java.lang.String r2 = "vers_date"
            r1.remove(r2)     // Catch:{ Throwable -> 0x0468 }
            java.lang.String r2 = "vers_pre_version"
            r1.remove(r2)     // Catch:{ Throwable -> 0x0468 }
            r1.commit()     // Catch:{ Throwable -> 0x0468 }
            goto L_0x0017
        L_0x0468:
            r1 = move-exception
            goto L_0x0017
        L_0x046b:
            r1 = move-exception
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04bc }
            r1.<init>()     // Catch:{ Throwable -> 0x04bc }
            r2 = r1
            goto L_0x0036
        L_0x0474:
            java.lang.String r2 = "body"
            r0.remove(r2)     // Catch:{ Throwable -> 0x047c }
            goto L_0x0162
        L_0x047c:
            r2 = move-exception
            goto L_0x0162
        L_0x047f:
            java.lang.String r4 = "app_version"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x049d }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m781b((android.content.Context) r5)     // Catch:{ Throwable -> 0x049d }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x049d }
            java.lang.String r4 = "version_code"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x049d }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m774a((android.content.Context) r5)     // Catch:{ Throwable -> 0x049d }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x049d }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x049d }
            goto L_0x01e7
        L_0x049d:
            r4 = move-exception
            java.lang.String r4 = "app_version"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m781b((android.content.Context) r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r4 = "version_code"
            android.content.Context r5 = f197j     // Catch:{ Throwable -> 0x04bc }
            java.lang.String r5 = com.umeng.analytics.pro.C0135bt.m774a((android.content.Context) r5)     // Catch:{ Throwable -> 0x04bc }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x04bc }
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            goto L_0x01e7
        L_0x04bc:
            r0 = move-exception
            android.content.Context r0 = f197j
            com.umeng.analytics.pro.ca r0 = com.umeng.analytics.pro.C0155ca.m887a((android.content.Context) r0)
            r0.mo510g()
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            goto L_0x0017
        L_0x04cd:
            java.lang.String r5 = "2G/3G"
            r6 = 0
            r6 = r4[r6]     // Catch:{ Throwable -> 0x04bc }
            boolean r5 = r5.equals(r6)     // Catch:{ Throwable -> 0x04bc }
            if (r5 == 0) goto L_0x04e4
            java.lang.String r5 = "access"
            java.lang.String r6 = "2G/3G"
            r2.put(r5, r6)     // Catch:{ Throwable -> 0x04bc }
            goto L_0x0302
        L_0x04e4:
            java.lang.String r5 = "access"
            java.lang.String r6 = "unknow"
            r2.put(r5, r6)     // Catch:{ Throwable -> 0x04bc }
            goto L_0x0302
        L_0x04ef:
            java.lang.String r4 = "mccmnc"
            java.lang.String r5 = ""
            r2.put(r4, r5)     // Catch:{ Throwable -> 0x04bc }
            goto L_0x0329
        L_0x04fa:
            r4 = move-exception
            goto L_0x03a3
        L_0x04fd:
            r4 = move-exception
            goto L_0x0383
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0050ap.mo174a(int[]):org.json.JSONObject");
    }

    /* renamed from: d */
    private void m219d(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(C0074be.m336a(f197j).mo237f(), C0074be.m336a(f197j).mo236e());
        jSONObject.put(C0281x.f935aq, jSONObject2);
    }

    /* renamed from: a */
    private String[] m209a(SharedPreferences.Editor editor, SharedPreferences sharedPreferences, String str, String str2) {
        String string = sharedPreferences.getString("pre_version", "");
        String string2 = sharedPreferences.getString("pre_date", "");
        String string3 = sharedPreferences.getString("cur_version", "");
        String b = C0135bt.m781b(f197j);
        if (!string3.equals(b)) {
            string2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
            editor.putString("pre_version", string3);
            editor.putString("pre_date", string2);
            editor.putString("cur_version", b);
            editor.commit();
        } else {
            string3 = string;
        }
        return new String[]{string3, string2};
    }

    /* renamed from: a */
    private void m207a(JSONObject jSONObject, String str, String str2) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            str = "0";
        }
        jSONObject.put(C0281x.f892R, str);
        if (TextUtils.isEmpty(str2)) {
            str2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        }
        jSONObject.put(C0281x.f893S, str2);
    }

    /* renamed from: a */
    public boolean mo179a(JSONObject jSONObject) {
        if (TextUtils.isEmpty(C0281x.f964u) || TextUtils.isEmpty(C0281x.f962s) || TextUtils.isEmpty(C0281x.f961r) || TextUtils.isEmpty(C0281x.f901a) || TextUtils.isEmpty(C0281x.f945b) || TextUtils.isEmpty(C0281x.f949f) || TextUtils.isEmpty(C0281x.f948e) || TextUtils.isEmpty(C0281x.f947d)) {
            return false;
        }
        return true;
    }

    /* renamed from: a */
    private boolean m208a(boolean z) {
        if (!C0135bt.m796l(f197j)) {
            C0138bw.m849e("network is unavailable");
            return false;
        } else if (this.f203c.mo220f()) {
            return true;
        } else {
            return this.f207g.mo184b(z).mo490a(z);
        }
    }

    /* renamed from: d */
    private void m218d() {
        try {
            if (this.f202b.mo511h()) {
                C0069ba baVar = new C0069ba(f197j, this.f203c);
                baVar.mo206a((C0066ay) this);
                if (this.f204d.mo244d()) {
                    baVar.mo209b(true);
                }
                baVar.mo205a();
                return;
            }
            JSONObject a = mo174a(new int[0]);
            if (a != null && a.length() > 0) {
                C0069ba baVar2 = new C0069ba(f197j, this.f203c);
                baVar2.mo206a((C0066ay) this);
                if (this.f204d.mo244d()) {
                    baVar2.mo209b(true);
                }
                baVar2.mo207a(a);
                baVar2.mo208a(m221e());
                baVar2.mo205a();
            }
        } catch (Throwable th) {
            if (th != null) {
                th.printStackTrace();
            }
        }
    }

    /* renamed from: e */
    private boolean m221e() {
        switch (this.f208h.mo154c(-1)) {
            case -1:
                return AnalyticsConfig.sEncrypt;
            case 1:
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public void m211b(int i) {
        m205a(i);
    }

    /* renamed from: a */
    public void mo177a(C0037af.C0038a aVar) {
        this.f205e.mo177a(aVar);
        this.f204d.mo177a(aVar);
        this.f206f.mo177a(aVar);
        this.f207g.mo182a(aVar);
        this.f201a = C0037af.m144a(f197j).mo140b().mo152b((String) null);
    }

    /* renamed from: com.umeng.analytics.pro.ap$a */
    /* compiled from: CacheImpl */
    public class C0052a {

        /* renamed from: b */
        private C0140by.C0148h f219b;

        /* renamed from: c */
        private int f220c = -1;

        /* renamed from: d */
        private int f221d = -1;

        /* renamed from: e */
        private int f222e = -1;

        /* renamed from: f */
        private int f223f = -1;

        public C0052a() {
            int[] a = C0050ap.this.f208h.mo150a(-1, -1);
            this.f220c = a[0];
            this.f221d = a[1];
        }

        /* access modifiers changed from: protected */
        /* renamed from: a */
        public void mo183a(boolean z) {
            C0140by.C0148h bVar;
            boolean z2 = true;
            int i = 0;
            if (C0050ap.this.f204d.mo244d()) {
                if (!(this.f219b instanceof C0140by.C0142b) || !this.f219b.mo491a()) {
                    z2 = false;
                }
                if (z2) {
                    bVar = this.f219b;
                } else {
                    bVar = new C0140by.C0142b(C0050ap.this.f203c, C0050ap.this.f204d);
                }
                this.f219b = bVar;
                return;
            }
            if (!(this.f219b instanceof C0140by.C0143c) || !this.f219b.mo491a()) {
                z2 = false;
            }
            if (z2) {
                return;
            }
            if (z && C0050ap.this.f206f.mo245a()) {
                this.f219b = new C0140by.C0143c((int) C0050ap.this.f206f.mo246b());
                C0050ap.this.m211b((int) C0050ap.this.f206f.mo246b());
            } else if (C0138bw.f509a && C0050ap.this.f208h.mo153b()) {
                this.f219b = new C0140by.C0141a(C0050ap.this.f203c);
            } else if (!C0050ap.this.f205e.mo232a() || !"RPT".equals(C0050ap.this.f205e.mo237f())) {
                int i2 = this.f222e;
                int i3 = this.f223f;
                if (this.f220c != -1) {
                    i2 = this.f220c;
                    i3 = this.f221d;
                }
                this.f219b = m231b(i2, i3);
            } else {
                if (C0050ap.this.f205e.mo233b() == 6) {
                    if (C0050ap.this.f208h.mo149a()) {
                        i = C0050ap.this.f208h.mo156d(90000);
                    } else if (this.f221d > 0) {
                        i = this.f221d;
                    } else {
                        i = this.f223f;
                    }
                }
                this.f219b = m231b(C0050ap.this.f205e.mo233b(), i);
            }
        }

        /* renamed from: b */
        public C0140by.C0148h mo184b(boolean z) {
            mo183a(z);
            return this.f219b;
        }

        /* renamed from: b */
        private C0140by.C0148h m231b(int i, int i2) {
            switch (i) {
                case 0:
                    return this.f219b instanceof C0140by.C0147g ? this.f219b : new C0140by.C0147g();
                case 1:
                    return this.f219b instanceof C0140by.C0144d ? this.f219b : new C0140by.C0144d();
                case 4:
                    if (this.f219b instanceof C0140by.C0146f) {
                        return this.f219b;
                    }
                    return new C0140by.C0146f(C0050ap.this.f203c);
                case 5:
                    if (this.f219b instanceof C0140by.C0149i) {
                        return this.f219b;
                    }
                    return new C0140by.C0149i(C0050ap.f197j);
                case 6:
                    if (!(this.f219b instanceof C0140by.C0145e)) {
                        return new C0140by.C0145e(C0050ap.this.f203c, (long) i2);
                    }
                    C0140by.C0148h hVar = this.f219b;
                    ((C0140by.C0145e) hVar).mo492a((long) i2);
                    return hVar;
                case 8:
                    if (this.f219b instanceof C0140by.C0150j) {
                        return this.f219b;
                    }
                    return new C0140by.C0150j(C0050ap.this.f203c);
                default:
                    if (this.f219b instanceof C0140by.C0144d) {
                        return this.f219b;
                    }
                    return new C0140by.C0144d();
            }
        }

        /* renamed from: a */
        public void mo181a(int i, int i2) {
            this.f222e = i;
            this.f223f = i2;
        }

        /* renamed from: a */
        public void mo182a(C0037af.C0038a aVar) {
            int[] a = aVar.mo150a(-1, -1);
            this.f220c = a[0];
            this.f221d = a[1];
        }
    }

    /* renamed from: c */
    private boolean m216c(int i) {
        if (this.f215p == 0) {
            return true;
        }
        if (System.currentTimeMillis() - this.f215p > 28800000) {
            m222f();
            return true;
        } else if (i > 5000) {
            return false;
        } else {
            return true;
        }
    }

    /* renamed from: a */
    public void mo176a(Context context) {
        try {
            if (f197j == null) {
                f197j = context;
            }
            if (this.f211l.length() > 0) {
                C0277w.m1400a(f197j).mo742a(this.f211l);
                this.f211l = new JSONArray();
            }
            C0067az.m285a(f197j).edit().putLong(f198q, this.f215p).putInt(f199r, this.f213n).putInt(f200s, this.f214o).commit();
        } catch (Throwable th) {
        }
    }

    /* renamed from: f */
    private void m222f() {
        this.f213n = 0;
        this.f214o = 0;
        this.f215p = System.currentTimeMillis();
    }
}
