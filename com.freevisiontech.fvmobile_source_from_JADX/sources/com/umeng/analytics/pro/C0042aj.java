package com.umeng.analytics.pro;

import android.os.Build;

/* renamed from: com.umeng.analytics.pro.aj */
/* compiled from: SerialTracker */
public class C0042aj extends C0282y {

    /* renamed from: a */
    private static final String f175a = "serial";

    public C0042aj() {
        super(f175a);
    }

    /* renamed from: f */
    public String mo122f() {
        if (Build.VERSION.SDK_INT >= 9) {
            return Build.SERIAL;
        }
        return null;
    }
}
