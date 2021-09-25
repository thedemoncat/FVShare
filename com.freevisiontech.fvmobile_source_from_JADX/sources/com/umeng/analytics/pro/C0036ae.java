package com.umeng.analytics.pro;

import android.content.Context;
import android.telephony.TelephonyManager;

/* renamed from: com.umeng.analytics.pro.ae */
/* compiled from: ImeiTracker */
public class C0036ae extends C0282y {

    /* renamed from: a */
    private static final String f143a = "imei";

    /* renamed from: b */
    private Context f144b;

    public C0036ae(Context context) {
        super(f143a);
        this.f144b = context;
    }

    /* renamed from: f */
    public String mo122f() {
        TelephonyManager telephonyManager = (TelephonyManager) this.f144b.getSystemService("phone");
        if (telephonyManager == null) {
        }
        try {
            if (C0135bt.m778a(this.f144b, "android.permission.READ_PHONE_STATE")) {
                return telephonyManager.getDeviceId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
