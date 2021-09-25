package com.freevisiontech.fvmobile.widget;

import android.app.Dialog;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleFirmWareUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.NumberProgressBar;
import com.vise.log.ViseLog;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SelfDialog extends Dialog {
    /* access modifiers changed from: private */
    public BleFirmWareUtil bleFirmWareUtil;
    /* access modifiers changed from: private */
    public byte[] content;
    /* access modifiers changed from: private */
    public String crc32;
    private Button finish;
    /* access modifiers changed from: private */
    public onFinishOnclickListener finishOnclickListener;
    private String finishStr;
    private int imageView;
    /* access modifiers changed from: private */
    public boolean isFirmwareUpgradeSuccess = false;
    private int isforce;
    private ImageView iv_gif;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public BleFirmWareUtil.OnProgressCallback mProgressCallback = new BleFirmWareUtil.OnProgressCallback() {
        public void onProgressChanging(int progress) {
            SelfDialog.this.mProgressbar.setProgress(progress);
        }
    };
    public NumberProgressBar mProgressbar;
    private String messagePointStr1;
    private String messagePointStr2;
    private String messagePointStr3;
    private String messagePointStr4;
    private String messageStr;
    private TextView messageTv;
    /* access modifiers changed from: private */
    public BleFirmWareUtil.FirmwareUpgradeStateListener mlistenergmu = new BleFirmWareUtil.FirmwareUpgradeStateListener() {
        public void onSuccess() {
            BlePtzParasConstant.isFirmwareUpgrading = false;
            boolean unused = SelfDialog.this.isFirmwareUpgradeSuccess = true;
            SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_success_to_gmu));
            SelfDialog.this.mProgressbar.setVisibility(8);
            BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = SelfDialog.this.version;
        }

        public void onFailed(int result) {
            String errorCode = " G:" + result;
            if (result == 2) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                boolean unused = SelfDialog.this.isFirmwareUpgradeSuccess = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout) + errorCode);
            } else if (result == 3) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.battery_too_low) + errorCode);
            } else if (result == 4) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout) + errorCode);
            } else if (result == 7) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout) + errorCode);
            } else if (result == 8) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout) + errorCode);
            } else if (result == 14) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout) + errorCode);
            } else {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_other_reson) + errorCode);
            }
        }
    };
    /* access modifiers changed from: private */
    public BleFirmWareUtil.FirmwareUpgradeStateListener mlistenerimu = new BleFirmWareUtil.FirmwareUpgradeStateListener() {
        public void onSuccess() {
            BlePtzParasConstant.isFirmwareUpgrading = false;
            boolean unused = SelfDialog.this.isFirmwareUpgradeSuccess = true;
            SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_success_to_imu));
            SelfDialog.this.mProgressbar.setVisibility(8);
            BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = SelfDialog.this.version;
        }

        public void onFailed(int result) {
            String errorCode = " I:" + result;
            if (result == 2) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 3) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.battery_too_low) + errorCode);
            } else if (result == 4) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 6) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 7) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 8) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 9) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 10) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 12) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 13) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else if (result == 14) {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            } else {
                BlePtzParasConstant.isFirmwareUpgrading = false;
                SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout_imu) + errorCode);
            }
        }
    };
    private int myType = 2;

    /* renamed from: no */
    private Button f1110no;
    /* access modifiers changed from: private */
    public onNoOnclickListener noOnclickListener;
    private String noStr;
    /* access modifiers changed from: private */
    public int powerPercent = -1;
    private LinearLayout rl_finish;
    private RelativeLayout rl_upgrade_ing;
    private RelativeLayout rl_upgrade_pre;
    private String titleStr;
    private TextView titleTv;
    private ImageView track_img;
    private TextView tv_upgrade_ing;
    private TextView tv_upgrade_pre1;
    private TextView tv_upgrade_pre2;
    private TextView tv_upgrade_pre3;
    private TextView tv_upgrade_pre4;
    /* access modifiers changed from: private */
    public int type;
    private View upgrade_view;
    private int upgradingFailCount = 0;
    /* access modifiers changed from: private */
    public String version;
    private Button yes;
    /* access modifiers changed from: private */
    public onYesOnclickListener yesOnclickListener;
    private String yesStr;

    public interface onFinishOnclickListener {
        void onFinishClick(boolean z);
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public SelfDialog(Context context, byte[] contents, int type2, String crc, String version2, int isForce) {
        super(context, C0853R.style.Theme_Light_Dialog);
        this.content = contents;
        this.type = type2;
        this.crc32 = crc;
        this.version = version2;
        this.isforce = isForce;
        this.mContext = context;
    }

    public void unRegisterEvent() {
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        boolean z;
        switch (event.getCode()) {
            case 17:
                ViseLog.m1466e("GMU CONNECTED");
                ViseBluetooth.getInstance().setBluetoothGatt((BluetoothGatt) event.getData());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViseBluetooth.getInstance().getGattSerAndChaAndNotify(ViseBluetooth.getInstance().getSupportedGattServices());
                    }
                }, 1000);
                if (!this.bleFirmWareUtil.isGmuInBooter()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            SelfDialog.this.bleFirmWareUtil.sendGmuJumpToBootCommand();
                        }
                    }, 1500);
                    return;
                }
                return;
            case 34:
                StringBuilder append = new StringBuilder().append("!BleUpgradeUtil.getInstance().isGmuInBooter(): ");
                if (!this.bleFirmWareUtil.isGmuInBooter()) {
                    z = true;
                } else {
                    z = false;
                }
                ViseLog.m1466e(append.append(z).toString());
                if (!this.bleFirmWareUtil.isGmuInBooter()) {
                    ViseLog.m1466e("GMU INITIATIVE DISCONNECT");
                    ViseBluetooth.getInstance().reconnectByName();
                    return;
                } else if (BlePtzParasConstant.isFirmwareUpgrading) {
                    BlePtzParasConstant.isFirmwareUpgrading = false;
                    if (isShowing()) {
                        setStepThreee(this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_disconnect));
                        return;
                    }
                    return;
                } else {
                    return;
                }
            case 85:
                if (BlePtzParasConstant.isFirmwareUpgrading) {
                    this.upgradingFailCount = 0;
                    this.bleFirmWareUtil.writeCallbackSuccess();
                    return;
                }
                return;
            case 102:
                int data = ((Integer) event.getData()).intValue();
                if (data == 97) {
                    ViseLog.m1466e("write data timeout");
                    if (BlePtzParasConstant.isFirmwareUpgrading) {
                        this.upgradingFailCount++;
                        if (this.upgradingFailCount <= 3) {
                            this.bleFirmWareUtil.reSend();
                            return;
                        }
                        ViseLog.m1466e("写入失败后,重发3次失败");
                        this.upgradingFailCount = 0;
                        BlePtzParasConstant.isFirmwareUpgrading = false;
                        this.isFirmwareUpgradeSuccess = false;
                        setStepThreee(this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout));
                        return;
                    }
                    return;
                } else if (data == 98) {
                    ViseLog.m1466e("characteristic is null");
                    return;
                } else if (data == 99) {
                    ViseLog.m1466e("write data return false");
                    if (BlePtzParasConstant.isFirmwareUpgrading) {
                        this.upgradingFailCount++;
                        if (this.upgradingFailCount <= 3) {
                            this.bleFirmWareUtil.reSend();
                            return;
                        }
                        ViseLog.m1466e("写入失败后,重发3次失败");
                        this.upgradingFailCount = 0;
                        BlePtzParasConstant.isFirmwareUpgrading = false;
                        this.isFirmwareUpgradeSuccess = false;
                        setStepThreee(this.mContext.getResources().getString(C0853R.string.upgrade_fail_for_device_timeout));
                        return;
                    }
                    return;
                } else {
                    return;
                }
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[0] & 255) == 91 || (value[0] & 255) == 92) {
                    this.bleFirmWareUtil.upgradeInfo(value);
                    return;
                } else if ((value[0] & 255) == 94 || (value[0] & 255) == 76) {
                    this.bleFirmWareUtil.upgradeInfo(value);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0853R.layout.upgrade_dialog);
        EventBusUtil.register(this);
        initView();
        initData();
        initEvent();
        this.bleFirmWareUtil = new BleFirmWareUtil(this.mContext);
        this.bleFirmWareUtil.init(BleByteUtil.getWriteCharacteristic());
    }

    private void initEvent() {
        this.yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SelfDialog.this.setStepTwo();
                SelfDialog.this.displayGif();
                int unused = SelfDialog.this.powerPercent = BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY_PERCENTAGE;
                if (SelfDialog.this.powerPercent > 15 || SelfDialog.this.powerPercent <= 0) {
                    BlePtzParasConstant.isFirmwareUpgrading = false;
                    ViseLog.m1466e(" yes onClick content  :" + SelfDialog.this.content);
                    if (SelfDialog.this.type == 1) {
                        SelfDialog.this.bleFirmWareUtil.requireIntoUpdateMode(SelfDialog.this.content, SelfDialog.this.type, SelfDialog.this.crc32, SelfDialog.this.version, SelfDialog.this.mlistenergmu, SelfDialog.this.mProgressCallback);
                    } else {
                        SelfDialog.this.bleFirmWareUtil.requireIntoUpdateMode(SelfDialog.this.content, SelfDialog.this.type, SelfDialog.this.crc32, SelfDialog.this.version, SelfDialog.this.mlistenerimu, SelfDialog.this.mProgressCallback);
                    }
                } else {
                    SelfDialog.this.setStepThreee(SelfDialog.this.mContext.getResources().getString(C0853R.string.ptz_msg_low_power));
                }
                if (SelfDialog.this.yesOnclickListener != null) {
                    SelfDialog.this.yesOnclickListener.onYesClick();
                }
            }
        });
        this.f1110no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SelfDialog.this.noOnclickListener != null) {
                    SelfDialog.this.noOnclickListener.onNoClick();
                }
            }
        });
        this.finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SelfDialog.this.finishOnclickListener != null) {
                    SelfDialog.this.finishOnclickListener.onFinishClick(SelfDialog.this.isFirmwareUpgradeSuccess);
                }
            }
        });
    }

    private void initData() {
        if (this.titleStr != null) {
        }
        if (this.messageStr != null) {
        }
        if (this.messagePointStr1 != null) {
            if (!"00.00.00.00".equals(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING)) {
                this.tv_upgrade_pre1.setText(this.messagePointStr1);
                setReleasenotesText(this.messagePointStr1);
                if (!this.messagePointStr1.contains("$$") || !this.messagePointStr1.contains("cn=") || !this.messagePointStr1.contains("en=")) {
                    this.tv_upgrade_pre1.setText(this.messagePointStr1);
                } else {
                    String[] languageSp = this.messagePointStr1.split("\\$\\$");
                    ViseLog.m1466e("onCreate: " + languageSp.length);
                    String aaa = "";
                    String bbb = "";
                    for (int i = 0; i < languageSp.length; i++) {
                        ViseLog.m1466e("鎴? " + languageSp[i]);
                        if (languageSp[i].substring(0, 3).equals("cn=")) {
                            aaa = languageSp[i];
                        } else {
                            bbb = languageSp[i];
                        }
                    }
                    if (!this.messagePointStr1.equals("")) {
                        if (Util.isZh(this.mContext)) {
                            setReleasenotesText(aaa);
                        } else {
                            setReleasenotesText(bbb);
                        }
                    }
                }
            } else {
                this.tv_upgrade_pre1.setText(this.mContext.getString(C0853R.string.update_firmware_interrupted));
            }
        }
        if (this.messagePointStr2 != null) {
            this.tv_upgrade_pre2.setText(this.messagePointStr2);
        }
        if (this.messagePointStr3 != null) {
            this.tv_upgrade_pre3.setText(this.messagePointStr3);
        }
        if (this.messagePointStr4 != null) {
            this.tv_upgrade_pre4.setText(this.messagePointStr4);
        }
        if (this.yesStr != null) {
            this.yes.setText(this.yesStr);
        } else {
            this.yes.setVisibility(8);
        }
        if (this.noStr != null) {
            this.f1110no.setText(this.noStr);
        } else {
            this.f1110no.setVisibility(8);
        }
        if (this.finishStr != null) {
            this.finish.setText(this.finishStr);
        }
        if (this.imageView != 0) {
        }
    }

    private void setReleasenotesText(String s) {
        if (s.length() >= 4) {
            String substring = s.substring(3, s.length());
            ViseLog.m1466e("setReleasenotesText: " + substring);
            String[] split = substring.split("##");
            String content2 = "";
            for (int i = 0; i < split.length; i++) {
                ViseLog.m1466e("onCreate: " + split[i]);
                content2 = content2 + split[i] + "\n";
            }
            this.tv_upgrade_pre1.setText(content2);
        }
    }

    public void displayGif() {
        Glide.with(this.mContext).load("file:///android_asset/upgrade_loading.gif").asGif().error((int) C0853R.mipmap.upgradegif).placeholder((int) C0853R.mipmap.upgradegif).into(this.iv_gif);
    }

    private void initView() {
        this.yes = (Button) findViewById(C0853R.C0855id.yes);
        this.f1110no = (Button) findViewById(C0853R.C0855id.f1088no);
        this.upgrade_view = findViewById(C0853R.C0855id.upgrade_view);
        this.tv_upgrade_pre1 = (TextView) findViewById(C0853R.C0855id.tv_upgrade_pre1);
        this.tv_upgrade_pre2 = (TextView) findViewById(C0853R.C0855id.tv_upgrade_pre2);
        this.tv_upgrade_pre3 = (TextView) findViewById(C0853R.C0855id.tv_upgrade_pre3);
        this.tv_upgrade_pre4 = (TextView) findViewById(C0853R.C0855id.tv_upgrade_pre4);
        this.rl_upgrade_pre = (RelativeLayout) findViewById(C0853R.C0855id.rl_upgrade_pre);
        this.rl_upgrade_ing = (RelativeLayout) findViewById(C0853R.C0855id.rl_upgrade_ing);
        this.rl_finish = (LinearLayout) findViewById(C0853R.C0855id.rl_finish);
        this.tv_upgrade_ing = (TextView) findViewById(C0853R.C0855id.tv_upgrade_ing);
        this.iv_gif = (ImageView) findViewById(C0853R.C0855id.iv_gif);
        this.finish = (Button) findViewById(C0853R.C0855id.finish);
        this.mProgressbar = (NumberProgressBar) findViewById(C0853R.C0855id.number_progress_bar_upgrade);
    }

    public void setUpgradePoint(String strings) {
        this.messagePointStr1 = strings;
    }

    public void setForcedToUpgrade() {
        this.f1110no.setVisibility(8);
        this.upgrade_view.setVisibility(8);
    }

    public void setStepTwo() {
        this.rl_upgrade_pre.setVisibility(8);
        this.rl_upgrade_ing.setVisibility(0);
    }

    public void setStepThreee(String text) {
        this.tv_upgrade_ing.setText(text);
        this.iv_gif.setVisibility(8);
        this.rl_finish.setVisibility(0);
    }

    public void setTitle(String title) {
        this.titleStr = title;
    }

    public void setMessage(String message) {
        this.messageStr = message;
    }

    public void setImage(int image) {
        this.imageView = image;
    }

    public void setDialogCancleable(boolean cancleable) {
        setCancelable(cancleable);
    }

    public void setDialogOutsideCancleable(boolean outsideCancleable) {
        setCanceledOnTouchOutside(outsideCancleable);
    }

    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener2) {
        if (str != null) {
            this.yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener2;
    }

    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener2) {
        if (str != null) {
            this.noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener2;
    }

    public void setFinishOnclickListener(String str, onFinishOnclickListener onFinishOnclickListener2) {
        if (str != null) {
            this.finishStr = str;
        }
        this.finishOnclickListener = onFinishOnclickListener2;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRegisterEvent();
    }
}
