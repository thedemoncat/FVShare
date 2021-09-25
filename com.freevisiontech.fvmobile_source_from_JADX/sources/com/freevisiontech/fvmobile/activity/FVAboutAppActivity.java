package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAboutAppActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Activity activity;
    Handler handlerAboutApp = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 119:
                    int unused = FVAboutAppActivity.this.logoDownNums = 0;
                    return;
                default:
                    return;
            }
        }
    };
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    @Bind({2131755199})
    ImageView iv_logo;
    /* access modifiers changed from: private */
    public int logoDownNums = 0;
    @Bind({2131755201})
    TextView tv_app_version;
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_about_app);
        this.activity = this;
        BaseActivityManager.getActivityManager().pushActivity(this);
        getWindow().setFlags(1024, 1024);
        Util.hideBottomUIMenu(this);
        ButterKnife.bind((Activity) this);
        initTitle();
        initView();
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            CameraUtils.setFrameLayerNumber(28);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    private void initTitle() {
        this.img_back.setVisibility(0);
        this.img_back.setImageResource(C0853R.mipmap.fanhui);
        this.tv_center_title.setVisibility(0);
        this.tv_center_title.setText(getResources().getString(C0853R.string.about_app));
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(8);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVAboutAppActivity.this.finish();
            }
        });
    }

    private void initView() {
        String appVersionName = getAppVersionName(this);
        if (!appVersionName.equals("")) {
            this.tv_app_version.setText(appVersionName);
        }
        this.iv_logo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVAboutAppActivity.this.dialogShowUI();
                while (FVAboutAppActivity.this.handlerAboutApp.hasMessages(119)) {
                    FVAboutAppActivity.this.handlerAboutApp.removeMessages(119);
                }
                FVAboutAppActivity.this.handlerAboutApp.sendEmptyMessageDelayed(119, 5000);
            }
        });
    }

    /* access modifiers changed from: private */
    public void dialogShowUI() {
        this.logoDownNums++;
        if (this.logoDownNums == 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
            builder.setMessage((CharSequence) "确认要展示210界面吗?");
            builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int unused = FVAboutAppActivity.this.logoDownNums = 0;
                }
            });
            builder.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BaseActivityManager.getActivityManager().popActivityOne(FVHomeActivity.class);
                    BaseActivityManager.getActivityManager().popActivityOne(FVAboutAppActivity.class);
                    BaseActivityManager.getActivityManager().exitApp();
                    CameraUtils.setCameraDeviceSum(3);
                    SPUtil.setParam(FVAboutAppActivity.this.activity, SharePrefConstant.PTZ_LIST_CAMERA_DEVICE_SUM, 3);
                    FVAboutAppActivity.this.startActivity(new Intent(FVAboutAppActivity.this, FVHomeActivity.class));
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    public String getAppVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 28) {
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 28) {
                    finish();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 28) {
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 28) {
                }
                return;
            case Constants.LABEL_SETTING_LONG_RETURN_KEY_210 /*107718*/:
                if (CameraUtils.getFrameLayerNumber() == 28) {
                    finish();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            CameraUtils.setFrameLayerNumber(22);
        }
        if (this.handlerAboutApp != null) {
            this.handlerAboutApp.removeCallbacksAndMessages((Object) null);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
