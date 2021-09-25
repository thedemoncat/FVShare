package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.C0015a;
import com.umeng.analytics.pro.C0262s;
import com.umeng.analytics.pro.C0277w;
import java.lang.reflect.Method;
import org.json.JSONObject;

/* renamed from: com.umeng.analytics.pro.bb */
/* compiled from: SessionTracker */
public class C0071bb {

    /* renamed from: a */
    private static final String f269a = "session_start_time";

    /* renamed from: b */
    private static final String f270b = "session_end_time";

    /* renamed from: c */
    private static final String f271c = "session_id";

    /* renamed from: f */
    private static String f272f = null;

    /* renamed from: g */
    private static Context f273g = null;

    /* renamed from: d */
    private final String f274d = "a_start_time";

    /* renamed from: e */
    private final String f275e = "a_end_time";

    /* renamed from: a */
    public boolean mo213a(Context context) {
        SharedPreferences a = C0067az.m285a(context);
        String string = a.getString(f271c, (String) null);
        if (string == null) {
            return false;
        }
        long j = a.getLong(f269a, 0);
        long j2 = a.getLong(f270b, 0);
        if (j2 == 0 || Math.abs(j2 - j) > C0015a.f22i) {
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(C0262s.C0269c.C0270a.f836a, string);
            jSONObject.put("__e", j);
            jSONObject.put(C0262s.C0269c.C0270a.f842g, j2);
            double[] location = AnalyticsConfig.getLocation();
            if (location != null) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(C0281x.f923ae, location[0]);
                jSONObject2.put(C0281x.f924af, location[1]);
                jSONObject2.put("ts", System.currentTimeMillis());
                jSONObject.put(C0262s.C0269c.C0270a.f840e, jSONObject2);
            }
            Class<?> cls = Class.forName("android.net.TrafficStats");
            Method method = cls.getMethod("getUidRxBytes", new Class[]{Integer.TYPE});
            Method method2 = cls.getMethod("getUidTxBytes", new Class[]{Integer.TYPE});
            int i = context.getApplicationInfo().uid;
            if (i == -1) {
                return false;
            }
            long longValue = ((Long) method.invoke((Object) null, new Object[]{Integer.valueOf(i)})).longValue();
            long longValue2 = ((Long) method2.invoke((Object) null, new Object[]{Integer.valueOf(i)})).longValue();
            if (longValue > 0 && longValue2 > 0) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put(C0281x.f928aj, longValue);
                jSONObject3.put(C0281x.f927ai, longValue2);
                jSONObject.put(C0262s.C0269c.C0270a.f839d, jSONObject3);
            }
            C0277w.m1400a(context).mo745a(string, jSONObject, C0277w.C0279a.NEWSESSION);
            C0073bd.m332a(f273g);
            C0048ao.m197a(f273g);
            m309a(a);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    /* renamed from: a */
    private void m309a(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(f269a);
        edit.remove(f270b);
        edit.remove("a_start_time");
        edit.remove("a_end_time");
        edit.commit();
    }

    /* renamed from: b */
    public String mo214b(Context context) {
        String c = C0135bt.m784c(context);
        String appkey = AnalyticsConfig.getAppkey(context);
        long currentTimeMillis = System.currentTimeMillis();
        if (appkey == null) {
            throw new RuntimeException("Appkey is null or empty, Please check AndroidManifest.xml");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(currentTimeMillis).append(appkey).append(c);
        f272f = C0136bu.m813a(sb.toString());
        return f272f;
    }

    /* renamed from: c */
    public void mo215c(Context context) {
        f273g = context;
        SharedPreferences a = C0067az.m285a(context);
        if (a != null) {
            SharedPreferences.Editor edit = a.edit();
            int i = a.getInt(C0015a.f11B, 0);
            int parseInt = Integer.parseInt(C0135bt.m774a(f273g));
            if (i != 0 && parseInt != i) {
                try {
                    edit.putInt("vers_code", i);
                    edit.putString("vers_name", a.getString(C0015a.f12C, ""));
                    edit.commit();
                } catch (Throwable th) {
                }
                if (m311g(context) == null) {
                    m308a(context, a);
                }
                mo217e(f273g);
                C0053aq.m237b(f273g).mo180b();
                mo218f(f273g);
                C0053aq.m237b(f273g).mo175a();
            } else if (m310b(a)) {
                C0138bw.m837c("Start new session: " + m308a(context, a));
            } else {
                String string = a.getString(f271c, (String) null);
                edit.putLong("a_start_time", System.currentTimeMillis());
                edit.putLong("a_end_time", 0);
                edit.commit();
                C0138bw.m837c("Extend current session: " + string);
            }
        }
    }

    /* renamed from: d */
    public void mo216d(Context context) {
        SharedPreferences a = C0067az.m285a(context);
        if (a != null) {
            if (a.getLong("a_start_time", 0) != 0 || !AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
                long currentTimeMillis = System.currentTimeMillis();
                SharedPreferences.Editor edit = a.edit();
                edit.putLong("a_start_time", 0);
                edit.putLong("a_end_time", currentTimeMillis);
                edit.putLong(f270b, currentTimeMillis);
                edit.commit();
                return;
            }
            C0138bw.m849e("onPause called before onResume");
        }
    }

    /* renamed from: b */
    private boolean m310b(SharedPreferences sharedPreferences) {
        long j = sharedPreferences.getLong("a_start_time", 0);
        long j2 = sharedPreferences.getLong("a_end_time", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (j != 0 && currentTimeMillis - j < AnalyticsConfig.kContinueSessionMillis) {
            C0138bw.m849e("onResume called before onPause");
            return false;
        } else if (currentTimeMillis - j2 <= AnalyticsConfig.kContinueSessionMillis) {
            return false;
        } else {
            String a = m307a();
            if (!TextUtils.isEmpty(a)) {
                long j3 = sharedPreferences.getLong(f270b, 0);
                if (j3 == 0) {
                    j3 = System.currentTimeMillis();
                }
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(C0262s.C0269c.C0270a.f842g, j3);
                    C0277w.m1400a(f273g).mo745a(a, jSONObject, C0277w.C0279a.END);
                } catch (Throwable th) {
                }
            }
            return true;
        }
    }

    /* renamed from: a */
    private String m308a(Context context, SharedPreferences sharedPreferences) {
        C0053aq b = C0053aq.m237b(context);
        String b2 = mo214b(context);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("__e", currentTimeMillis);
            C0277w.m1400a(f273g).mo745a(b2, jSONObject, C0277w.C0279a.BEGIN);
        } catch (Throwable th) {
        }
        mo213a(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(f271c, b2);
        edit.putLong(f269a, System.currentTimeMillis());
        edit.putLong(f270b, 0);
        edit.putLong("a_start_time", currentTimeMillis);
        edit.putLong("a_end_time", 0);
        edit.putInt(C0015a.f11B, Integer.parseInt(C0135bt.m774a(context)));
        edit.putString(C0015a.f12C, C0135bt.m781b(context));
        edit.commit();
        b.mo178a((Object) true);
        return b2;
    }

    /* renamed from: e */
    public boolean mo217e(Context context) {
        boolean z = false;
        SharedPreferences a = C0067az.m285a(context);
        if (!(a == null || a.getString(f271c, (String) null) == null)) {
            long j = a.getLong("a_start_time", 0);
            long j2 = a.getLong("a_end_time", 0);
            if (j > 0 && j2 == 0) {
                z = true;
                mo216d(context);
            }
            long j3 = a.getLong(f270b, 0);
            try {
                JSONObject jSONObject = new JSONObject();
                if (j3 == 0) {
                    j3 = System.currentTimeMillis();
                }
                jSONObject.put(C0262s.C0269c.C0270a.f842g, j3);
                C0277w.m1400a(f273g).mo745a(m307a(), jSONObject, C0277w.C0279a.END);
            } catch (Throwable th) {
            }
            mo213a(context);
        }
        return z;
    }

    /* renamed from: f */
    public void mo218f(Context context) {
        SharedPreferences a = C0067az.m285a(context);
        if (a != null) {
            String b = mo214b(context);
            SharedPreferences.Editor edit = a.edit();
            long currentTimeMillis = System.currentTimeMillis();
            edit.putString(f271c, b);
            edit.putLong(f269a, System.currentTimeMillis());
            edit.putLong(f270b, 0);
            edit.putLong("a_start_time", currentTimeMillis);
            edit.putLong("a_end_time", 0);
            edit.putInt(C0015a.f11B, Integer.parseInt(C0135bt.m774a(context)));
            edit.putString(C0015a.f12C, C0135bt.m781b(context));
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("__e", currentTimeMillis);
                C0277w.m1400a(f273g).mo745a(b, jSONObject, C0277w.C0279a.BEGIN);
            } catch (Throwable th) {
            }
            edit.commit();
        }
    }

    /* renamed from: g */
    public static String m311g(Context context) {
        if (f272f == null) {
            f272f = C0067az.m285a(context).getString(f271c, (String) null);
        }
        return f272f;
    }

    /* renamed from: a */
    public static String m307a() {
        return m311g(f273g);
    }
}
