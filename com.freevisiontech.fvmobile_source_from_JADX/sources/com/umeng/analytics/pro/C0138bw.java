package com.umeng.analytics.pro;

import android.text.TextUtils;
import android.util.Log;
import com.umeng.analytics.C0015a;
import java.util.Formatter;
import java.util.Locale;

/* renamed from: com.umeng.analytics.pro.bw */
/* compiled from: MLog */
public class C0138bw {

    /* renamed from: a */
    public static boolean f509a = false;

    /* renamed from: b */
    private static String f510b = C0015a.f16c;

    /* renamed from: c */
    private static final int f511c = 1;

    /* renamed from: d */
    private static final int f512d = 2;

    /* renamed from: e */
    private static final int f513e = 3;

    /* renamed from: f */
    private static final int f514f = 4;

    /* renamed from: g */
    private static final int f515g = 5;

    /* renamed from: h */
    private static int f516h = 2000;

    private C0138bw() {
    }

    /* renamed from: a */
    public static void m830a(Locale locale, String str, Object... objArr) {
        try {
            m838c(f510b, new Formatter(locale).format(str, objArr).toString(), (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: b */
    public static void m836b(Locale locale, String str, Object... objArr) {
        try {
            m832b(f510b, new Formatter(locale).format(str, objArr).toString(), (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: c */
    public static void m842c(Locale locale, String str, Object... objArr) {
        try {
            m850e(f510b, new Formatter(locale).format(str, objArr).toString(), (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: d */
    public static void m848d(Locale locale, String str, Object... objArr) {
        try {
            m826a(f510b, new Formatter(locale).format(str, objArr).toString(), (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: e */
    public static void m854e(Locale locale, String str, Object... objArr) {
        try {
            m844d(f510b, new Formatter(locale).format(str, objArr).toString(), (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: a */
    public static void m828a(String str, Object... objArr) {
        String str2 = "";
        try {
            if (str.contains("%")) {
                m838c(f510b, new Formatter().format(str, objArr).toString(), (Throwable) null);
                return;
            }
            if (objArr != null) {
                str2 = objArr[0];
            }
            m838c(str, str2, (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: b */
    public static void m834b(String str, Object... objArr) {
        String str2 = "";
        try {
            if (str.contains("%")) {
                m832b(f510b, new Formatter().format(str, objArr).toString(), (Throwable) null);
                return;
            }
            if (objArr != null) {
                str2 = objArr[0];
            }
            m832b(str, str2, (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: c */
    public static void m840c(String str, Object... objArr) {
        String str2 = "";
        try {
            if (str.contains("%")) {
                m850e(f510b, new Formatter().format(str, objArr).toString(), (Throwable) null);
                return;
            }
            if (objArr != null) {
                str2 = objArr[0];
            }
            m850e(str, str2, (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: d */
    public static void m846d(String str, Object... objArr) {
        String str2 = "";
        try {
            if (str.contains("%")) {
                m826a(f510b, new Formatter().format(str, objArr).toString(), (Throwable) null);
                return;
            }
            if (objArr != null) {
                str2 = objArr[0];
            }
            m826a(str, str2, (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: e */
    public static void m852e(String str, Object... objArr) {
        String str2 = "";
        try {
            if (str.contains("%")) {
                m844d(f510b, new Formatter().format(str, objArr).toString(), (Throwable) null);
                return;
            }
            if (objArr != null) {
                str2 = objArr[0];
            }
            m844d(str, str2, (Throwable) null);
        } catch (Throwable th) {
            m853e(th);
        }
    }

    /* renamed from: a */
    public static void m829a(Throwable th) {
        m838c(f510b, (String) null, th);
    }

    /* renamed from: b */
    public static void m835b(Throwable th) {
        m826a(f510b, (String) null, th);
    }

    /* renamed from: c */
    public static void m841c(Throwable th) {
        m844d(f510b, (String) null, th);
    }

    /* renamed from: d */
    public static void m847d(Throwable th) {
        m832b(f510b, (String) null, th);
    }

    /* renamed from: e */
    public static void m853e(Throwable th) {
        m850e(f510b, (String) null, th);
    }

    /* renamed from: a */
    public static void m827a(String str, Throwable th) {
        m838c(f510b, str, th);
    }

    /* renamed from: b */
    public static void m833b(String str, Throwable th) {
        m826a(f510b, str, th);
    }

    /* renamed from: c */
    public static void m839c(String str, Throwable th) {
        m844d(f510b, str, th);
    }

    /* renamed from: d */
    public static void m845d(String str, Throwable th) {
        m832b(f510b, str, th);
    }

    /* renamed from: e */
    public static void m851e(String str, Throwable th) {
        m850e(f510b, str, th);
    }

    /* renamed from: a */
    public static void m825a(String str) {
        m826a(f510b, str, (Throwable) null);
    }

    /* renamed from: b */
    public static void m831b(String str) {
        m832b(f510b, str, (Throwable) null);
    }

    /* renamed from: c */
    public static void m837c(String str) {
        m838c(f510b, str, (Throwable) null);
    }

    /* renamed from: d */
    public static void m843d(String str) {
        m844d(f510b, str, (Throwable) null);
    }

    /* renamed from: e */
    public static void m849e(String str) {
        m850e(f510b, str, (Throwable) null);
    }

    /* renamed from: a */
    public static void m826a(String str, String str2, Throwable th) {
        if (f509a) {
            m824a(1, str, str2, th);
        }
    }

    /* renamed from: b */
    public static void m832b(String str, String str2, Throwable th) {
        if (f509a) {
            m824a(2, str, str2, th);
        }
    }

    /* renamed from: c */
    public static void m838c(String str, String str2, Throwable th) {
        if (f509a) {
            m824a(3, str, str2, th);
        }
    }

    /* renamed from: d */
    public static void m844d(String str, String str2, Throwable th) {
        if (f509a) {
            m824a(4, str, str2, th);
        }
    }

    /* renamed from: e */
    public static void m850e(String str, String str2, Throwable th) {
        if (f509a) {
            m824a(5, str, str2, th);
        }
    }

    /* renamed from: a */
    private static void m824a(int i, String str, String str2, Throwable th) {
        int i2 = 0;
        if (!TextUtils.isEmpty(str2)) {
            int length = str2.length();
            int i3 = f516h;
            int i4 = 0;
            while (true) {
                if (i2 < 100) {
                    if (length <= i3) {
                        switch (i) {
                            case 1:
                                Log.v(str, str2.substring(i4, length));
                                break;
                            case 2:
                                Log.d(str, str2.substring(i4, length));
                                break;
                            case 3:
                                Log.i(str, str2.substring(i4, length));
                                break;
                            case 4:
                                Log.w(str, str2.substring(i4, length));
                                break;
                            case 5:
                                Log.e(str, str2.substring(i4, length));
                                break;
                        }
                    } else {
                        switch (i) {
                            case 1:
                                Log.v(str, str2.substring(i4, i3));
                                break;
                            case 2:
                                Log.d(str, str2.substring(i4, i3));
                                break;
                            case 3:
                                Log.i(str, str2.substring(i4, i3));
                                break;
                            case 4:
                                Log.w(str, str2.substring(i4, i3));
                                break;
                            case 5:
                                Log.e(str, str2.substring(i4, i3));
                                break;
                        }
                        i2++;
                        i4 = i3;
                        i3 = f516h + i3;
                    }
                }
            }
        }
        if (th != null) {
            String f = m855f(th);
            if (!TextUtils.isEmpty(f)) {
                switch (i) {
                    case 1:
                        Log.v(str, f);
                        return;
                    case 2:
                        Log.d(str, f);
                        return;
                    case 3:
                        Log.i(str, f);
                        return;
                    case 4:
                        Log.w(str, f);
                        return;
                    case 5:
                        Log.e(str, f);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x002a A[SYNTHETIC, Splitter:B:16:0x002a] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0038 A[SYNTHETIC, Splitter:B:23:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003d  */
    /* renamed from: f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String m855f(java.lang.Throwable r4) {
        /*
            r2 = 0
            java.lang.String r0 = ""
            java.io.StringWriter r3 = new java.io.StringWriter     // Catch:{ Throwable -> 0x0026, all -> 0x0033 }
            r3.<init>()     // Catch:{ Throwable -> 0x0026, all -> 0x0033 }
            java.io.PrintWriter r1 = new java.io.PrintWriter     // Catch:{ Throwable -> 0x004c, all -> 0x0047 }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x004c, all -> 0x0047 }
            r4.printStackTrace(r1)     // Catch:{ Throwable -> 0x0050, all -> 0x004a }
            r1.flush()     // Catch:{ Throwable -> 0x0050, all -> 0x004a }
            r3.flush()     // Catch:{ Throwable -> 0x0050, all -> 0x004a }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x0050, all -> 0x004a }
            if (r3 == 0) goto L_0x0020
            r3.close()     // Catch:{ Throwable -> 0x0041 }
        L_0x0020:
            if (r1 == 0) goto L_0x0025
            r1.close()
        L_0x0025:
            return r0
        L_0x0026:
            r1 = move-exception
            r1 = r2
        L_0x0028:
            if (r2 == 0) goto L_0x002d
            r2.close()     // Catch:{ Throwable -> 0x0043 }
        L_0x002d:
            if (r1 == 0) goto L_0x0025
            r1.close()
            goto L_0x0025
        L_0x0033:
            r0 = move-exception
            r1 = r2
            r3 = r2
        L_0x0036:
            if (r3 == 0) goto L_0x003b
            r3.close()     // Catch:{ Throwable -> 0x0045 }
        L_0x003b:
            if (r1 == 0) goto L_0x0040
            r1.close()
        L_0x0040:
            throw r0
        L_0x0041:
            r2 = move-exception
            goto L_0x0020
        L_0x0043:
            r2 = move-exception
            goto L_0x002d
        L_0x0045:
            r2 = move-exception
            goto L_0x003b
        L_0x0047:
            r0 = move-exception
            r1 = r2
            goto L_0x0036
        L_0x004a:
            r0 = move-exception
            goto L_0x0036
        L_0x004c:
            r1 = move-exception
            r1 = r2
            r2 = r3
            goto L_0x0028
        L_0x0050:
            r2 = move-exception
            r2 = r3
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.umeng.analytics.pro.C0138bw.m855f(java.lang.Throwable):java.lang.String");
    }
}
