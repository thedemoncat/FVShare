package com.lzy.okgo.utils;

import android.util.Log;

public class OkLogger {
    private static boolean isLogEnable = true;
    private static String tag = "OkGo";

    public static void debug(boolean isEnable) {
        debug(tag, isEnable);
    }

    public static void debug(String logTag, boolean isEnable) {
        tag = logTag;
        isLogEnable = isEnable;
    }

    /* renamed from: v */
    public static void m1454v(String msg) {
        m1455v(tag, msg);
    }

    /* renamed from: v */
    public static void m1455v(String tag2, String msg) {
        if (isLogEnable) {
            Log.v(tag2, msg);
        }
    }

    /* renamed from: d */
    public static void m1448d(String msg) {
        m1449d(tag, msg);
    }

    /* renamed from: d */
    public static void m1449d(String tag2, String msg) {
        if (isLogEnable) {
            Log.d(tag2, msg);
        }
    }

    /* renamed from: i */
    public static void m1452i(String msg) {
        m1453i(tag, msg);
    }

    /* renamed from: i */
    public static void m1453i(String tag2, String msg) {
        if (isLogEnable) {
            Log.i(tag2, msg);
        }
    }

    /* renamed from: w */
    public static void m1456w(String msg) {
        m1457w(tag, msg);
    }

    /* renamed from: w */
    public static void m1457w(String tag2, String msg) {
        if (isLogEnable) {
            Log.w(tag2, msg);
        }
    }

    /* renamed from: e */
    public static void m1450e(String msg) {
        m1451e(tag, msg);
    }

    /* renamed from: e */
    public static void m1451e(String tag2, String msg) {
        if (isLogEnable) {
            Log.e(tag2, msg);
        }
    }

    public static void printStackTrace(Throwable t) {
        if (isLogEnable && t != null) {
            t.printStackTrace();
        }
    }
}
