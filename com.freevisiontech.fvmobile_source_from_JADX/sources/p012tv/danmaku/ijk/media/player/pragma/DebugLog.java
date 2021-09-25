package p012tv.danmaku.ijk.media.player.pragma;

import android.util.Log;
import java.util.Locale;

/* renamed from: tv.danmaku.ijk.media.player.pragma.DebugLog */
public class DebugLog {
    public static final boolean ENABLE_DEBUG = true;
    public static final boolean ENABLE_ERROR = true;
    public static final boolean ENABLE_INFO = true;
    public static final boolean ENABLE_VERBOSE = true;
    public static final boolean ENABLE_WARN = true;

    /* renamed from: e */
    public static void m1552e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /* renamed from: e */
    public static void m1553e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    public static void efmt(String tag, String fmt, Object... args) {
        Log.e(tag, String.format(Locale.US, fmt, args));
    }

    /* renamed from: i */
    public static void m1554i(String tag, String msg) {
        Log.i(tag, msg);
    }

    /* renamed from: i */
    public static void m1555i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    public static void ifmt(String tag, String fmt, Object... args) {
        Log.i(tag, String.format(Locale.US, fmt, args));
    }

    /* renamed from: w */
    public static void m1558w(String tag, String msg) {
        Log.w(tag, msg);
    }

    /* renamed from: w */
    public static void m1559w(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    public static void wfmt(String tag, String fmt, Object... args) {
        Log.w(tag, String.format(Locale.US, fmt, args));
    }

    /* renamed from: d */
    public static void m1550d(String tag, String msg) {
        Log.d(tag, msg);
    }

    /* renamed from: d */
    public static void m1551d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    public static void dfmt(String tag, String fmt, Object... args) {
        Log.d(tag, String.format(Locale.US, fmt, args));
    }

    /* renamed from: v */
    public static void m1556v(String tag, String msg) {
        Log.v(tag, msg);
    }

    /* renamed from: v */
    public static void m1557v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

    public static void vfmt(String tag, String fmt, Object... args) {
        Log.v(tag, String.format(Locale.US, fmt, args));
    }

    public static void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    public static void printCause(Throwable e) {
        Throwable cause = e.getCause();
        if (cause != null) {
            e = cause;
        }
        printStackTrace(e);
    }
}
