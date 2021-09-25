package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.pro.C0132bq;
import com.umeng.analytics.pro.C0135bt;
import com.umeng.analytics.pro.C0138bw;
import com.umeng.analytics.pro.C0155ca;

public class AnalyticsConfig {
    public static boolean ACTIVITY_DURATION_OPEN = true;
    public static boolean CATCH_EXCEPTION = true;
    public static String GPU_RENDERER = "";
    public static String GPU_VENDER = "";

    /* renamed from: a */
    static double[] f0a = null;

    /* renamed from: b */
    private static String f1b = null;

    /* renamed from: c */
    private static String f2c = null;

    /* renamed from: d */
    private static String f3d = null;

    /* renamed from: e */
    private static int f4e = 0;
    public static long kContinueSessionMillis = 30000;
    public static String mWrapperType = null;
    public static String mWrapperVersion = null;
    public static boolean sEncrypt = false;
    public static int sLatentWindow;

    /* renamed from: a */
    static void m3a(boolean z) {
        sEncrypt = z;
    }

    /* renamed from: a */
    static void m1a(Context context, String str) {
        if (context == null) {
            f1b = str;
            return;
        }
        String p = C0135bt.m800p(context);
        if (!TextUtils.isEmpty(p)) {
            f1b = p;
            if (!p.equals(str)) {
                C0138bw.m843d("Appkey和AndroidManifest.xml中配置的不一致 ");
                return;
            }
            return;
        }
        String c = C0155ca.m887a(context).mo505c();
        if (TextUtils.isEmpty(c)) {
            C0155ca.m887a(context).mo499a(str);
        } else if (!c.equals(str)) {
            C0138bw.m843d("Appkey和上次配置的不一致 ");
            C0155ca.m887a(context).mo499a(str);
        }
        f1b = str;
    }

    /* renamed from: a */
    static void m2a(String str) {
        f2c = str;
    }

    public static String getAppkey(Context context) {
        if (TextUtils.isEmpty(f1b)) {
            f1b = C0135bt.m800p(context);
            if (TextUtils.isEmpty(f1b)) {
                f1b = C0155ca.m887a(context).mo505c();
            }
        }
        return f1b;
    }

    public static String getChannel(Context context) {
        if (TextUtils.isEmpty(f2c)) {
            f2c = C0135bt.m803s(context);
        }
        return f2c;
    }

    public static double[] getLocation() {
        return f0a;
    }

    /* renamed from: b */
    static void m4b(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            f3d = str;
            C0155ca.m887a(context).mo506c(f3d);
        }
    }

    public static String getSecretKey(Context context) {
        if (TextUtils.isEmpty(f3d)) {
            f3d = C0155ca.m887a(context).mo508e();
        }
        return f3d;
    }

    /* renamed from: a */
    static void m0a(Context context, int i) {
        f4e = i;
        C0155ca.m887a(context).mo498a(f4e);
    }

    public static int getVerticalType(Context context) {
        if (f4e == 0) {
            f4e = C0155ca.m887a(context).mo509f();
        }
        return f4e;
    }

    public static String getSDKVersion(Context context) {
        return C0132bq.f497a;
    }
}
