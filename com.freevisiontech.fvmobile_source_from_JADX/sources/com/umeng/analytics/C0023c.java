package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;
import com.umeng.analytics.pro.C0155ca;

/* renamed from: com.umeng.analytics.c */
/* compiled from: InternalConfig */
public class C0023c {

    /* renamed from: a */
    private static String[] f64a = new String[2];

    /* renamed from: a */
    public static void m58a(Context context, String str, String str2) {
        f64a[0] = str;
        f64a[1] = str2;
        if (context != null) {
            C0155ca.m887a(context).mo500a(str, str2);
        }
    }

    /* renamed from: a */
    public static String[] m59a(Context context) {
        String[] a;
        if (!TextUtils.isEmpty(f64a[0]) && !TextUtils.isEmpty(f64a[1])) {
            return f64a;
        }
        if (context == null || (a = C0155ca.m887a(context).mo502a()) == null) {
            return null;
        }
        f64a[0] = a[0];
        f64a[1] = a[1];
        return f64a;
    }

    /* renamed from: b */
    public static void m60b(Context context) {
        f64a[0] = null;
        f64a[1] = null;
        if (context != null) {
            C0155ca.m887a(context).mo503b();
        }
    }
}
