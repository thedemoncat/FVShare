package com.freevisiontech.fvmobile.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.log.inner.Tree;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.opencv.android.LoadOpenCV;
import org.xutils.C2090x;

public class MyApplication extends Application {
    public static String CURRENT_PTZ_TYPE = SharePrefConstant.CURRENT_PTZ_TYPE;
    private static MyApplication instance;
    public static List<String> test;

    public void initTest() {
        test = new ArrayList();
        for (int i = 1; i < 6; i++) {
            test.add("测试" + i);
        }
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        ViseLog.getLogConfig().configAllowLog(true);
        ViseLog.plant((Tree) new LogcatTree());
        ViseBluetooth.getInstance().init(getApplicationContext());
        initOkgo();
        initTest();
        C2090x.Ext.init(this);
        LoadOpenCV.LoadOpenCVLib();
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
                activity.getWindow().addFlags(128);
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.controlDefaultCamSwitch((byte) 87, 0);
                }
            }

            public void onActivityPaused(Activity activity) {
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.controlDefaultCamSwitch((byte) 87, 1);
                }
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityDestroyed(Activity activity) {
            }
        });
        initCanChargeByWireless();
    }

    private void initCanChargeByWireless() {
        if (((Boolean) SPUtil.getParam(this, "isFirstLaunch", true)).booleanValue()) {
            ArrayList<String> modelList = new ArrayList<>();
            modelList.add("samsung,SM-G9250");
            modelList.add("samsung,SM-G9600");
            modelList.add("samsung,SM-N9500");
            modelList.add("Sony,H8296");
            modelList.add("Xiaomi,MIX 2S");
            SPUtils.setDataList(this, SharePrefConstant.PTZ_FM300_CAN_CHARGE_BY_WIRELESS_LIST, modelList);
        }
    }

    private void initOkgo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(60000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(60000, TimeUnit.MILLISECONDS);
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1", new boolean[0]);
        params.put("commonParamsKey2", "这里支持中文参数", new boolean[0]);
        OkGo.getInstance().init(this).setOkHttpClient(builder.build()).setCacheMode(CacheMode.NO_CACHE).setCacheTime(-1).setRetryCount(3).addCommonHeaders(headers).addCommonParams(params);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
