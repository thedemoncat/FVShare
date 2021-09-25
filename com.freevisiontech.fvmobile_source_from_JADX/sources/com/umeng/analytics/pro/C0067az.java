package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;

/* renamed from: com.umeng.analytics.pro.az */
/* compiled from: PreferenceWrapper */
public class C0067az {

    /* renamed from: a */
    private static final String f250a = "umeng_general_config";

    private C0067az() {
    }

    /* renamed from: a */
    public static SharedPreferences m286a(Context context, String str) {
        return context.getSharedPreferences(str, 0);
    }

    /* renamed from: a */
    public static SharedPreferences m285a(Context context) {
        return context.getSharedPreferences(f250a, 0);
    }
}
