package com.umeng.analytics;

import android.content.Context;
import android.text.TextUtils;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.umeng.analytics.pro.C0138bw;
import com.umeng.analytics.social.C0300d;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMSocialService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.microedition.khronos.opengles.GL10;

public class MobclickAgent {

    /* renamed from: a */
    private static final String f7a = "input map is null";

    public static void startWithConfigure(UMAnalyticsConfig uMAnalyticsConfig) {
        if (uMAnalyticsConfig != null) {
            C0016b.m6a().mo64a(uMAnalyticsConfig);
        }
    }

    public static void setLocation(double d, double d2) {
        C0016b.m6a().mo50a(d, d2);
    }

    public static void setLatencyWindow(long j) {
        C0016b.m6a().mo51a(j);
    }

    public static void enableEncrypt(boolean z) {
        C0016b.m6a().mo85e(z);
    }

    public static void setCatchUncaughtExceptions(boolean z) {
        C0016b.m6a().mo70a(z);
    }

    public static void setSecret(Context context, String str) {
        C0016b.m6a().mo74b(context, str);
    }

    public static void setScenarioType(Context context, EScenarioType eScenarioType) {
        C0016b.m6a().mo54a(context, eScenarioType);
    }

    public static void setSessionContinueMillis(long j) {
        C0016b.m6a().mo72b(j);
    }

    public static C0016b getAgent() {
        return C0016b.m6a();
    }

    public static void setCheckDevice(boolean z) {
        C0016b.m6a().mo80c(z);
    }

    public static void setOpenGLContext(GL10 gl10) {
        C0016b.m6a().mo69a(gl10);
    }

    public static void openActivityDurationTrack(boolean z) {
        C0016b.m6a().mo76b(z);
    }

    public static void onPageStart(String str) {
        if (!TextUtils.isEmpty(str)) {
            C0016b.m6a().mo66a(str);
        } else {
            C0138bw.m849e("pageName is null or empty");
        }
    }

    public static void onPageEnd(String str) {
        if (!TextUtils.isEmpty(str)) {
            C0016b.m6a().mo75b(str);
        } else {
            C0138bw.m849e("pageName is null or empty");
        }
    }

    public static void setDebugMode(boolean z) {
        C0016b.m6a().mo83d(z);
    }

    public static void onPause(Context context) {
        C0016b.m6a().mo73b(context);
    }

    public static void onResume(Context context) {
        if (context == null) {
            C0138bw.m849e("unexpected null context in onResume");
        } else {
            C0016b.m6a().mo52a(context);
        }
    }

    public static void reportError(Context context, String str) {
        C0016b.m6a().mo55a(context, str);
    }

    public static void reportError(Context context, Throwable th) {
        C0016b.m6a().mo61a(context, th);
    }

    public static void onEvent(Context context, List<String> list, int i, String str) {
    }

    public static void onEvent(Context context, String str) {
        C0016b.m6a().mo57a(context, str, (String) null, -1, 1);
    }

    public static void onEvent(Context context, String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            C0138bw.m837c("label is null or empty");
        } else {
            C0016b.m6a().mo57a(context, str, str2, -1, 1);
        }
    }

    public static void onEvent(Context context, String str, Map<String, String> map) {
        if (map == null) {
            C0138bw.m849e(f7a);
            return;
        }
        C0016b.m6a().mo60a(context, str, (Map<String, Object>) new HashMap(map), -1);
    }

    public static void onEventValue(Context context, String str, Map<String, String> map, int i) {
        HashMap hashMap;
        if (map == null) {
            hashMap = new HashMap();
        } else {
            hashMap = new HashMap(map);
        }
        hashMap.put("__ct__", Integer.valueOf(i));
        C0016b.m6a().mo60a(context, str, (Map<String, Object>) hashMap, -1);
    }

    public static void onSocialEvent(Context context, String str, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            C0138bw.m849e("context is null in onShareEvent");
            return;
        }
        C0300d.f998d = BleConstant.f1095WB;
        UMSocialService.share(context, str, uMPlatformDataArr);
    }

    public static void onSocialEvent(Context context, UMPlatformData... uMPlatformDataArr) {
        if (context == null) {
            C0138bw.m849e("context is null in onShareEvent");
            return;
        }
        C0300d.f998d = BleConstant.f1095WB;
        UMSocialService.share(context, uMPlatformDataArr);
    }

    public static void onKillProcess(Context context) {
        C0016b.m6a().mo82d(context);
    }

    public static void onProfileSignIn(String str) {
        onProfileSignIn("_adhoc", str);
    }

    public static void onProfileSignIn(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            C0138bw.m843d("uid is null");
        } else if (str2.length() > 64) {
            C0138bw.m843d("uid is Illegal(length bigger then  legitimate length).");
        } else if (TextUtils.isEmpty(str)) {
            C0016b.m6a().mo67a("_adhoc", str2);
        } else if (str.length() > 32) {
            C0138bw.m843d("provider is Illegal(length bigger then  legitimate length).");
        } else {
            C0016b.m6a().mo67a(str, str2);
        }
    }

    public static void onProfileSignOff() {
        C0016b.m6a().mo77c();
    }

    public enum EScenarioType {
        E_UM_NORMAL(0),
        E_UM_GAME(1),
        E_UM_ANALYTICS_OEM(224),
        E_UM_GAME_OEM(CompanyIdentifierResolver.DANLERS_LTD);
        

        /* renamed from: a */
        private int f9a;

        private EScenarioType(int i) {
            this.f9a = i;
        }

        public int toValue() {
            return this.f9a;
        }
    }

    public static class UMAnalyticsConfig {
        public String mAppkey;
        public String mChannelId;
        public Context mContext;
        public boolean mIsCrashEnable;
        public EScenarioType mType;

        private UMAnalyticsConfig() {
            this.mAppkey = null;
            this.mChannelId = null;
            this.mIsCrashEnable = true;
            this.mType = EScenarioType.E_UM_NORMAL;
            this.mContext = null;
        }

        public UMAnalyticsConfig(Context context, String str, String str2) {
            this(context, str, str2, (EScenarioType) null, true);
        }

        public UMAnalyticsConfig(Context context, String str, String str2, EScenarioType eScenarioType) {
            this(context, str, str2, eScenarioType, true);
        }

        public UMAnalyticsConfig(Context context, String str, String str2, EScenarioType eScenarioType, boolean z) {
            this.mAppkey = null;
            this.mChannelId = null;
            this.mIsCrashEnable = true;
            this.mType = EScenarioType.E_UM_NORMAL;
            this.mContext = null;
            this.mContext = context;
            this.mAppkey = str;
            this.mChannelId = str2;
            this.mIsCrashEnable = z;
            if (eScenarioType != null) {
                this.mType = eScenarioType;
                return;
            }
            switch (AnalyticsConfig.getVerticalType(context)) {
                case 0:
                    this.mType = EScenarioType.E_UM_NORMAL;
                    return;
                case 1:
                    this.mType = EScenarioType.E_UM_GAME;
                    return;
                case 224:
                    this.mType = EScenarioType.E_UM_ANALYTICS_OEM;
                    return;
                case CompanyIdentifierResolver.DANLERS_LTD /*225*/:
                    this.mType = EScenarioType.E_UM_GAME_OEM;
                    return;
                default:
                    return;
            }
        }
    }
}
