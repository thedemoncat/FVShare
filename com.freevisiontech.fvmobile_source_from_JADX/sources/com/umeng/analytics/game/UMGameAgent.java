package com.umeng.analytics.game;

import android.content.Context;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.pro.C0138bw;
import com.umeng.analytics.social.C0300d;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMSocialService;

public class UMGameAgent extends MobclickAgent {

    /* renamed from: a */
    private static final String f65a = "Input string is null or empty";

    /* renamed from: b */
    private static final String f66b = "Input string must be less than 64 chars";

    /* renamed from: c */
    private static final String f67c = "Input value type is negative";

    /* renamed from: d */
    private static final String f68d = "The int value for 'Pay Channels' ranges between 1 ~ 99 ";

    /* renamed from: e */
    private static final C0027c f69e = new C0027c();

    /* renamed from: f */
    private static Context f70f;

    public static void init(Context context) {
        f69e.mo104a(context);
        f70f = context.getApplicationContext();
    }

    public static void setTraceSleepTime(boolean z) {
        f69e.mo108a(z);
    }

    public static void setPlayerLevel(int i) {
        f69e.mo105a(String.valueOf(i));
    }

    public static void startLevel(String str) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (str.length() > 64) {
            C0138bw.m849e(f66b);
        } else {
            f69e.mo110b(str);
        }
    }

    public static void finishLevel(String str) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (str.length() > 64) {
            C0138bw.m849e(f66b);
        } else {
            f69e.mo112c(str);
        }
    }

    public static void failLevel(String str) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (str.length() > 64) {
            C0138bw.m849e(f66b);
        } else {
            f69e.mo113d(str);
        }
    }

    public static void pay(double d, double d2, int i) {
        if (i <= 0 || i >= 100) {
            C0138bw.m849e(f68d);
        } else if (d < 0.0d || d2 < 0.0d) {
            C0138bw.m849e(f67c);
        } else {
            f69e.mo100a(d, d2, i);
        }
    }

    public static void pay(double d, String str, int i, double d2, int i2) {
        if (i2 <= 0 || i2 >= 100) {
            C0138bw.m849e(f68d);
        } else if (d < 0.0d || i < 0 || d2 < 0.0d) {
            C0138bw.m849e(f67c);
        } else if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else {
            f69e.mo103a(d, str, i, d2, i2);
        }
    }

    public static void exchange(double d, String str, double d2, int i, String str2) {
        if (d < 0.0d || d2 < 0.0d) {
            C0138bw.m849e(f67c);
        } else if (i <= 0 || i >= 100) {
            C0138bw.m849e(f68d);
        } else {
            f69e.mo102a(d, str, d2, i, str2);
        }
    }

    public static void buy(String str, int i, double d) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (i < 0 || d < 0.0d) {
            C0138bw.m849e(f67c);
        } else {
            f69e.mo106a(str, i, d);
        }
    }

    public static void use(String str, int i, double d) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (i < 0 || d < 0.0d) {
            C0138bw.m849e(f67c);
        } else {
            f69e.mo111b(str, i, d);
        }
    }

    public static void bonus(double d, int i) {
        if (d < 0.0d) {
            C0138bw.m849e(f67c);
        } else if (i <= 0 || i >= 100) {
            C0138bw.m849e(f68d);
        } else {
            f69e.mo101a(d, i);
        }
    }

    public static void bonus(String str, int i, double d, int i2) {
        if (m61a(str)) {
            C0138bw.m849e(f65a);
        } else if (i < 0 || d < 0.0d) {
            C0138bw.m849e(f67c);
        } else if (i2 <= 0 || i2 >= 100) {
            C0138bw.m849e(f68d);
        } else {
            f69e.mo107a(str, i, d, i2);
        }
    }

    /* renamed from: a */
    private static boolean m61a(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }

    public static void onEvent(String str, String str2) {
        onEvent(f70f, str, str2);
    }

    public static void onSocialEvent(Context context, String str, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            C0138bw.m849e("context is null in onShareEvent");
            return;
        }
        C0300d.f998d = BleConstant.FOCUS;
        UMSocialService.share(context, str, uMPlatformDataArr);
    }

    public static void onSocialEvent(Context context, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            C0138bw.m849e("context is null in onShareEvent");
            return;
        }
        C0300d.f998d = BleConstant.FOCUS;
        UMSocialService.share(context, uMPlatformDataArr);
    }
}
