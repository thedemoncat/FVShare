package com.umeng.analytics.pro;

import android.content.Context;
import android.content.SharedPreferences;
import com.umeng.analytics.C0015a;

/* renamed from: com.umeng.analytics.pro.al */
/* compiled from: UOPTracker */
public class C0044al extends C0282y {

    /* renamed from: a */
    private static final String f178a = "uop";

    /* renamed from: b */
    private Context f179b;

    public C0044al(Context context) {
        super(f178a);
        this.f179b = context;
    }

    /* renamed from: f */
    public String mo122f() {
        SharedPreferences a = C0067az.m285a(this.f179b);
        if (a != null) {
            return a.getString(C0015a.f31r, "");
        }
        return "";
    }
}
