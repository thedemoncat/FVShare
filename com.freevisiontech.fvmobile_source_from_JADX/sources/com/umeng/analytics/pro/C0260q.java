package com.umeng.analytics.pro;

import com.umeng.analytics.C0015a;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/* renamed from: com.umeng.analytics.pro.q */
/* compiled from: UMCCTimeRange */
public class C0260q {

    /* renamed from: a */
    public static final int f808a = 1;

    /* renamed from: b */
    private static final int f809b = 1000;

    /* renamed from: c */
    private static final int f810c = 1001;

    /* renamed from: d */
    private static final int f811d = 1002;

    /* renamed from: a */
    public static String m1377a(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return String.valueOf(((long) ((((instance.get(12) / 6) + 1) + (instance.get(11) * 10)) - 1)) + (m1379b(j) * 240));
    }

    /* renamed from: b */
    public static long m1379b(long j) {
        long j2 = 0;
        try {
            long time = new SimpleDateFormat("yyyy", Locale.getDefault()).parse("1970").getTime();
            long j3 = (j - time) / C0015a.f22i;
            if ((j - time) % C0015a.f22i > 0) {
                j2 = 1;
            }
            return j2 + j3;
        } catch (Throwable th) {
            return 0;
        }
    }

    /* renamed from: c */
    public static long m1380c(long j) {
        return m1376a(j, 1001);
    }

    /* renamed from: d */
    public static long m1381d(long j) {
        return m1376a(j, 1002);
    }

    /* renamed from: a */
    private static long m1376a(long j, int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        int i2 = (instance.get(12) / 6) + 1 + (instance.get(11) * 10);
        int i3 = instance.get(13);
        int i4 = 0;
        if (i == 1002) {
            i4 = 360 - (((instance.get(12) % 6) * 60) + i3);
        } else if (i == 1001) {
            i4 = 60 - (i3 % 60);
            if (i2 % 6 == 0) {
                i4 += 60;
            }
        }
        return (long) (i4 * 1000);
    }

    /* renamed from: a */
    public static boolean m1378a(long j, long j2) {
        if (m1382e(j) == m1382e(j2)) {
            return true;
        }
        return false;
    }

    /* renamed from: e */
    private static int m1382e(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return instance.get(5);
    }
}
