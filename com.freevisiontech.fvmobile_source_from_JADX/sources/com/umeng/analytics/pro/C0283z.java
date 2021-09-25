package com.umeng.analytics.pro;

import android.content.Context;
import android.provider.Settings;

/* renamed from: com.umeng.analytics.pro.z */
/* compiled from: AndroidIdTracker */
public class C0283z extends C0282y {

    /* renamed from: a */
    private static final String f975a = "android_id";

    /* renamed from: b */
    private Context f976b;

    public C0283z(Context context) {
        super(f975a);
        this.f976b = context;
    }

    /* renamed from: f */
    public String mo122f() {
        try {
            return Settings.Secure.getString(this.f976b.getContentResolver(), f975a);
        } catch (Exception e) {
            return null;
        }
    }
}
