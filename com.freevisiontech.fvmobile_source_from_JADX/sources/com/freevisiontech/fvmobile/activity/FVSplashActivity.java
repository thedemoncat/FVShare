package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.p001v4.content.ContextCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.network.Advertisement;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.FileUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.umeng.analytics.MobclickAgent;
import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

public class FVSplashActivity extends AppCompatActivity {
    public static final String AD_URL = "http://www.freevisiontech.com/oss//appManagerTool/queryAdvistorByType.jspx";
    public static final String TAG = "FVSplashActivity";
    /* access modifiers changed from: private */
    public CountDownTimer AdDownTimer = new CountDownTimer(4000, 1000) {
        public void onTick(long millisUntilFinished) {
            if (FVSplashActivity.this.skipAdTv != null) {
                FVSplashActivity.this.skipAdTv.setText((millisUntilFinished / 1000) + " " + FVSplashActivity.this.getString(C0853R.string.ad_skip));
            }
        }

        public void onFinish() {
            FVSplashActivity.this.enterNextActivity();
        }
    };
    @Bind({2131755511})
    ImageView adIv;
    /* access modifiers changed from: private */
    public boolean mAdClickable = false;
    /* access modifiers changed from: private */
    public String mAdPath;
    private String mAdTitle;
    private String mAdUrl;
    /* access modifiers changed from: private */
    public Context mContext;
    private PermissionUtil.PermissionRequestObject requestObject;
    @Bind({2131755512})
    TextView skipAdTv;
    private CountDownTimer welcomeDownTimer = new CountDownTimer(1000, 500) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            FVSplashActivity.this.enterNextActivity();
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_splash);
        CameraUtils.setApkIsFromGoogle(true);
        this.mContext = this;
        Util.setPrimaryDarkColor(this);
        MobclickAgent.setCatchUncaughtExceptions(true);
        ButterKnife.bind((Activity) this);
        checkPermission();
        BleUpgradeUtil.getInstance().init(this);
        Util.hideBottomUIMenu(this);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            this.requestObject = PermissionUtil.with((AppCompatActivity) this).request("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.CAMERA", "android.permission.RECORD_AUDIO").onAllGranted(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                    FVSplashActivity.this.showdownLoadImg();
                    FVSplashActivity.this.downLoadImg();
                }
            }).onAnyDenied(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                    new AlertDialog.Builder(FVSplashActivity.this.mContext).setMessage((int) C0853R.string.permission_denied_dialog_tip).setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).show();
                }
            }).ask(0);
            return;
        }
        showdownLoadImg();
        downLoadImg();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0) {
            this.requestObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void downLoadImg() {
        ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(AD_URL).tag(this)).params("advistorType", "8001", new boolean[0])).client(new OkHttpClient().newBuilder().readTimeout(1000, TimeUnit.MILLISECONDS).writeTimeout(1000, TimeUnit.MILLISECONDS).connectTimeout(1000, TimeUnit.MILLISECONDS).build())).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
                try {
                    final Advertisement ad = (Advertisement) new Gson().fromJson(new JsonParser().parse(response.body()).getAsString(), Advertisement.class);
                    if (ad.getCode().equals("1000")) {
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                        if (((String) SPUtil.getParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_VERSION, "")).compareTo(ad.getVersion()) < 0) {
                            SPUtil.setParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_VERSION, ad.getVersion());
                            File adFile = new File(FVSplashActivity.this.mAdPath);
                            if (adFile.exists()) {
                                adFile.delete();
                            }
                        }
                        if (currentTime.compareTo(ad.getEndTime()) < 0 || currentTime.compareTo(ad.getStartTime()) > 0) {
                            Glide.with(FVSplashActivity.this.mContext).load(URLDecoder.decode(ad.getUrl(), "UTF-8")).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
                                public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                                    String adPath = new BleUpgradeUtil().getFileDir() + BleConstant.AD_Dir;
                                    File file = new File(adPath);
                                    if (!file.exists()) {
                                        file.mkdirs();
                                    }
                                    FileUtils.copy(adPath + "ad.jpg", resource);
                                    if (ad.getImageUrl() == null) {
                                        SPUtil.setParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_URL, "none");
                                        SPUtil.setParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_TITLE, "none");
                                        return;
                                    }
                                    SPUtil.setParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_URL, ad.getImageUrl());
                                    SPUtil.setParam(FVSplashActivity.this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_TITLE, ad.getTitle());
                                }

                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                }
                            });
                            return;
                        }
                        return;
                    }
                    FVSplashActivity.this.cleanAd();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    /* access modifiers changed from: private */
    public void showdownLoadImg() {
        this.mAdUrl = (String) SPUtil.getParam(this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_URL, "");
        this.mAdTitle = (String) SPUtil.getParam(this.mContext, SharePrefConstant.ADVERTISEMENT_IMAGE_CLICK_TITLE, "");
        this.mAdPath = new BleUpgradeUtil().getFileDir() + BleConstant.AD_Dir + "ad.jpg";
        if (new File(this.mAdPath).exists()) {
            Glide.with(this.mContext).load(this.mAdPath).placeholder((int) C0853R.mipmap.splash).error((int) C0853R.mipmap.splash).diskCacheStrategy(DiskCacheStrategy.NONE).into(new GlideDrawableImageViewTarget(this.adIv) {
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    FVSplashActivity.this.skipAdTv.setVisibility(0);
                    FVSplashActivity.this.AdDownTimer.start();
                    boolean unused = FVSplashActivity.this.mAdClickable = true;
                }
            });
            return;
        }
        this.skipAdTv.setVisibility(4);
        this.welcomeDownTimer.start();
    }

    /* access modifiers changed from: private */
    public void cleanAd() {
        File adFile = new File(new BleUpgradeUtil().getFileDir() + BleConstant.AD_Dir + "ad.jpg");
        if (adFile.exists()) {
            adFile.delete();
        }
    }

    /* access modifiers changed from: private */
    public void enterNextActivity() {
        if (LocalResourceManager.expansionFilesDelivered(this)) {
            startActivity(new Intent(this, FVHomeActivity.class));
        } else {
            startActivity(new Intent(this, FVDownloadExpansionActivity.class));
        }
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({2131755511, 2131755512})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.ad_iv:
                toAdInfo();
                return;
            case C0853R.C0855id.skip_ad_tv:
                this.AdDownTimer.cancel();
                enterNextActivity();
                return;
            default:
                return;
        }
    }

    private void toAdInfo() {
        if (!this.mAdUrl.equals("none") && this.mAdClickable) {
            this.AdDownTimer.cancel();
            Intent intent = new Intent(this.mContext, FVADActivity.class);
            intent.putExtra("adUrl", this.mAdUrl);
            intent.putExtra("adTitle", this.mAdTitle);
            startActivity(intent);
            finish();
        }
    }
}
