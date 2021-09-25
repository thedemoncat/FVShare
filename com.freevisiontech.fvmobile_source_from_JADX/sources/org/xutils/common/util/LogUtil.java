package org.xutils.common.util;

import android.text.TextUtils;
import android.util.Log;
import org.xutils.C2090x;

public class LogUtil {
    public static String customTagPrefix = "x_log";

    private LogUtil() {
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String callerClazzName = caller.getClassName();
        String tag = String.format("%s.%s(L:%d)", new Object[]{callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1), caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
        if (TextUtils.isEmpty(customTagPrefix)) {
            return tag;
        }
        return customTagPrefix + ":" + tag;
    }

    /* renamed from: d */
    public static void m1562d(String content) {
        if (C2090x.isDebug()) {
            Log.d(generateTag(), content);
        }
    }

    /* renamed from: d */
    public static void m1563d(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.d(generateTag(), content, tr);
        }
    }

    /* renamed from: e */
    public static void m1564e(String content) {
        if (C2090x.isDebug()) {
            Log.e(generateTag(), content);
        }
    }

    /* renamed from: e */
    public static void m1565e(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.e(generateTag(), content, tr);
        }
    }

    /* renamed from: i */
    public static void m1566i(String content) {
        if (C2090x.isDebug()) {
            Log.i(generateTag(), content);
        }
    }

    /* renamed from: i */
    public static void m1567i(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.i(generateTag(), content, tr);
        }
    }

    /* renamed from: v */
    public static void m1568v(String content) {
        if (C2090x.isDebug()) {
            Log.v(generateTag(), content);
        }
    }

    /* renamed from: v */
    public static void m1569v(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.v(generateTag(), content, tr);
        }
    }

    /* renamed from: w */
    public static void m1570w(String content) {
        if (C2090x.isDebug()) {
            Log.w(generateTag(), content);
        }
    }

    /* renamed from: w */
    public static void m1571w(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.w(generateTag(), content, tr);
        }
    }

    /* renamed from: w */
    public static void m1572w(Throwable tr) {
        if (C2090x.isDebug()) {
            Log.w(generateTag(), tr);
        }
    }

    public static void wtf(String content) {
        if (C2090x.isDebug()) {
            Log.wtf(generateTag(), content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (C2090x.isDebug()) {
            Log.wtf(generateTag(), content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (C2090x.isDebug()) {
            Log.wtf(generateTag(), tr);
        }
    }
}
