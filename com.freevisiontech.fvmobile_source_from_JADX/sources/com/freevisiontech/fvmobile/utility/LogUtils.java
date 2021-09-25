package com.freevisiontech.fvmobile.utility;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "default";
    public static boolean isDebug = true;

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /* renamed from: i */
    public static void m1522i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    /* renamed from: d */
    public static void m1518d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /* renamed from: e */
    public static void m1520e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    /* renamed from: w */
    public static void m1526w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    /* renamed from: v */
    public static void m1524v(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    /* renamed from: i */
    public static void m1523i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /* renamed from: d */
    public static void m1519d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /* renamed from: e */
    public static void m1521e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    /* renamed from: w */
    public static void m1527w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    /* renamed from: v */
    public static void m1525v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }
}
