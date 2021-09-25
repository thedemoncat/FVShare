package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.SelfDialog;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import java.io.File;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAboutPtzActivity extends BaseActivity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public boolean connected = false;
    private byte[] gmuContents = null;
    private String gmuCrc = "";
    private String gmuDownloadFileDir = "";
    private String gmuFirmwareVersion = "";
    private String gmuIsForce = "";
    /* access modifiers changed from: private */
    public String gmuServerVersion = "";
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    private byte[] imuContents = null;
    private String imuCrc = "";
    private String imuDownloadFileDir = "";
    private String imuFirmwareVersion = "";
    private String imuIsForce = "";
    /* access modifiers changed from: private */
    public String imuServerVersion = "";
    /* access modifiers changed from: private */
    public int mUpgradeType = 1;
    /* access modifiers changed from: private */
    public SelfDialog selfDialog;
    @Bind({2131755210})
    TextView textGmuBootstrapProgram;
    @Bind({2131755213})
    TextView textGmuFirmwareUpgrade;
    @Bind({2131755212})
    TextView textGmuFirmwareVersion;
    @Bind({2131755211})
    TextView textGmuHardwareVersion;
    @Bind({2131755205})
    TextView textImuBootstrapProgram;
    @Bind({2131755209})
    TextView textImuFirmwareUpgrade;
    @Bind({2131755207})
    TextView textImuFirmwareVersion;
    @Bind({2131755206})
    TextView textImuHardwareVersion;
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;
    private BluetoothGattCharacteristic writeCharacteristic;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_about_ptz);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        Util.hideBottomUIMenu(this);
        ButterKnife.bind((Activity) this);
        initTitle();
        initView();
        initData();
        setErrorText();
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            CameraUtils.setFrameLayerNumber(26);
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    private void initData() {
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            getPtzBatteryInfo();
            compare2Version(1);
            compare2Version(2);
        }
    }

    private void setErrorText() {
        String currentPtzType = MyApplication.CURRENT_PTZ_TYPE;
        if (currentPtzType.equals(BleConstant.FM_300) || currentPtzType.equals(BleConstant.FM_210)) {
            if ("00.00.00.00".equals(this.gmuFirmwareVersion)) {
                this.textGmuFirmwareUpgrade.setText(C0853R.string.label_can_update_firmware);
                this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_black1));
            }
            if ("00.00.00.00".equals(this.imuFirmwareVersion)) {
                this.textImuFirmwareUpgrade.setText(C0853R.string.label_can_update_firmware);
                this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_black1));
            }
        } else if (currentPtzType.equals("")) {
            if ("00.00.00.00".equals(this.gmuFirmwareVersion)) {
                this.textGmuFirmwareUpgrade.setText(C0853R.string.firmware_communication_error);
                this.textGmuFirmwareUpgrade.setTextColor(Color.parseColor("#ff4646"));
            }
            if ("00.00.00.00".equals(this.imuFirmwareVersion)) {
                this.textImuFirmwareUpgrade.setText(C0853R.string.firmware_communication_error);
                this.textImuFirmwareUpgrade.setTextColor(Color.parseColor("#ff4646"));
            }
        }
    }

    private void compare2Version(int type) {
        String serverVersion;
        String downloadFileDir;
        String isforce;
        String crc32;
        String firmwareVersion;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type == 1) {
            serverVersion = SPUtil.getParam(this, ptzType + "gmuVersion", "").toString();
            this.gmuServerVersion = serverVersion;
            downloadFileDir = SPUtil.getParam(this, ptzType + "gmuDownloadFileDir", "").toString();
            this.gmuDownloadFileDir = downloadFileDir;
            isforce = SPUtil.getParam(this, ptzType + "gmuIsforce", "").toString();
            this.gmuIsForce = isforce;
            crc32 = SPUtil.getParam(this, ptzType + "gmuVerifycode", "").toString();
            this.gmuCrc = crc32;
            firmwareVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
            this.gmuFirmwareVersion = firmwareVersion;
        } else {
            serverVersion = SPUtil.getParam(this, ptzType + "imuVersion", "").toString();
            this.imuServerVersion = serverVersion;
            downloadFileDir = SPUtil.getParam(this, ptzType + "imuDownloadFileDir", "").toString();
            this.imuDownloadFileDir = downloadFileDir;
            isforce = SPUtil.getParam(this, ptzType + "imuIsforce", "").toString();
            this.imuIsForce = isforce;
            crc32 = SPUtil.getParam(this, ptzType + "imuVerifycode", "").toString();
            this.imuCrc = crc32;
            firmwareVersion = BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING;
            this.imuFirmwareVersion = firmwareVersion;
        }
        if (SPUtil.isEmpty(serverVersion) || SPUtil.isEmpty(downloadFileDir) || SPUtil.isEmpty(firmwareVersion) || SPUtil.isEmpty(isforce) || SPUtil.isEmpty(crc32)) {
            if (type == 1) {
                this.textGmuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
                this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
                return;
            }
            this.textImuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
            this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
        } else if (new File(downloadFileDir).exists()) {
            if (serverVersion.compareTo(firmwareVersion) > 0) {
                if (type == 1) {
                    this.gmuContents = BleUpgradeUtil.getInstance().getGMUBinaryFromSDCard();
                    if (this.gmuContents != null) {
                        this.textGmuFirmwareUpgrade.setText(getString(C0853R.string.label_can_update_firmware));
                        this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_black1));
                        return;
                    }
                    this.textGmuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
                    this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
                    return;
                }
                this.imuContents = BleUpgradeUtil.getInstance().getIMUBinaryFromSDCard();
                if (this.imuContents != null) {
                    this.textImuFirmwareUpgrade.setText(getString(C0853R.string.label_can_update_firmware));
                    this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_black1));
                    return;
                }
                this.textImuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
                this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
            } else if (type == 1) {
                this.textGmuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
                this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
            } else {
                this.textImuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
                this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
            }
        } else if (type == 1) {
            this.textGmuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
            this.textGmuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
        } else {
            this.textImuFirmwareUpgrade.setText(getString(C0853R.string.label_is_latest_firmware));
            this.textImuFirmwareUpgrade.setTextColor(getResources().getColor(C0853R.color.color_light_gray2));
        }
    }

    private void getPtzBatteryInfo() {
        this.textImuBootstrapProgram.setText(BlePtzParasConstant.GET_IMU_FIRMWARE_BOOT_VERSION);
        this.textImuHardwareVersion.setText(BlePtzParasConstant.GET_IMU_FIRMWARE_ID + "");
        this.textImuFirmwareVersion.setText(BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING);
        this.textGmuBootstrapProgram.setText(BlePtzParasConstant.GET_GMU_FIRMWARE_BOOT_VERSION);
        this.textGmuHardwareVersion.setText(BlePtzParasConstant.GET_GMU_FIRMWARE_ID + "");
        this.textGmuFirmwareVersion.setText(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING);
    }

    private void setPtzParamatersText() {
        this.textImuBootstrapProgram.setText("");
        this.textImuHardwareVersion.setText("");
        this.textImuFirmwareVersion.setText("");
        this.textImuFirmwareUpgrade.setText("");
        this.textImuFirmwareUpgrade.setEnabled(false);
        this.textGmuBootstrapProgram.setText("");
        this.textGmuHardwareVersion.setText("");
        this.textGmuFirmwareVersion.setText("");
        this.textGmuFirmwareUpgrade.setText("");
        this.textGmuFirmwareUpgrade.setEnabled(false);
    }

    private void initView() {
        this.textGmuFirmwareUpgrade.setOnClickListener(this);
        this.textImuFirmwareUpgrade.setOnClickListener(this);
    }

    private void initTitle() {
        this.img_back.setVisibility(0);
        this.img_back.setImageResource(C0853R.mipmap.fanhui);
        this.tv_center_title.setVisibility(0);
        this.tv_center_title.setText(getResources().getString(C0853R.string.about_ptz));
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(8);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVAboutPtzActivity.this.finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 26) {
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 26) {
                    finish();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 26) {
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 26) {
                }
                return;
            case Constants.LABEL_SETTING_LONG_RETURN_KEY_210 /*107718*/:
                if (CameraUtils.getFrameLayerNumber() == 26) {
                    finish();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            CameraUtils.setFrameLayerNumber(22);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.text_imu_firmware_upgrade:
                if (this.textImuFirmwareUpgrade.getText().equals(getResources().getString(C0853R.string.label_is_latest_firmware))) {
                    Toast.makeText(this, getString(C0853R.string.label_is_latest_firmware_version), 0).show();
                    return;
                } else if (!this.textImuFirmwareUpgrade.getText().equals(getResources().getString(C0853R.string.label_can_update_firmware))) {
                    return;
                } else {
                    if (this.connected) {
                        ViseLog.m1466e("imuServerVersion" + this.imuServerVersion);
                        this.mUpgradeType = 2;
                        showMyDialog(this.imuContents, this.mUpgradeType, this.imuCrc, this.imuServerVersion, 0);
                        return;
                    }
                    Toast.makeText(this, getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case C0853R.C0855id.text_gmu_firmware_upgrade:
                if (this.textGmuFirmwareUpgrade.getText().equals(getResources().getString(C0853R.string.label_is_latest_firmware))) {
                    Toast.makeText(this, getString(C0853R.string.label_is_latest_firmware_version), 0).show();
                    return;
                } else if (!this.textGmuFirmwareUpgrade.getText().equals(getResources().getString(C0853R.string.label_can_update_firmware))) {
                    return;
                } else {
                    if (this.connected) {
                        ViseLog.m1466e("gmuServerVersion" + this.gmuServerVersion);
                        this.mUpgradeType = 1;
                        showMyDialog(this.gmuContents, this.mUpgradeType, this.gmuCrc, this.gmuServerVersion, 0);
                        return;
                    }
                    Toast.makeText(this, getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 34:
                this.connected = false;
                setPtzParamatersText();
                return;
            default:
                return;
        }
    }

    public void showMyDialog(byte[] contents, int type, String crc, String version, int isForce) {
        String releasenotes;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type == 1) {
            releasenotes = SPUtil.getParam(this, ptzType + "gmuReleasenotes", "").toString();
        } else {
            releasenotes = SPUtil.getParam(this, ptzType + "imuReleasenotes", "").toString();
        }
        ViseLog.m1466e("升级更新点" + releasenotes);
        this.selfDialog = new SelfDialog(this, contents, type, crc, version, isForce);
        Window window = this.selfDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.selfDialog.setDialogCancleable(false);
        this.selfDialog.setDialogOutsideCancleable(false);
        this.selfDialog.setUpgradePoint(releasenotes);
        this.selfDialog.setYesOnclickListener(getResources().getString(C0853R.string.upgrade_now), new SelfDialog.onYesOnclickListener() {
            public void onYesClick() {
            }
        });
        this.selfDialog.setNoOnclickListener(getResources().getString(C0853R.string.upgrade_cancel), new SelfDialog.onNoOnclickListener() {
            public void onNoClick() {
                FVAboutPtzActivity.this.selfDialog.dismiss();
            }
        });
        this.selfDialog.setFinishOnclickListener(getResources().getString(C0853R.string.finish), new SelfDialog.onFinishOnclickListener() {
            public void onFinishClick(boolean isUpgradeSuccess) {
                FVAboutPtzActivity.this.selfDialog.dismiss();
                if (!isUpgradeSuccess) {
                    return;
                }
                if (FVAboutPtzActivity.this.connected) {
                    if (FVAboutPtzActivity.this.mUpgradeType == 1) {
                        FVAboutPtzActivity.this.textGmuFirmwareVersion.setText(FVAboutPtzActivity.this.gmuServerVersion);
                        FVAboutPtzActivity.this.textGmuFirmwareUpgrade.setText(FVAboutPtzActivity.this.getResources().getString(C0853R.string.label_is_latest_firmware));
                        FVAboutPtzActivity.this.textGmuFirmwareUpgrade.setTextColor(FVAboutPtzActivity.this.getResources().getColor(C0853R.color.color_light_gray2));
                        BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVAboutPtzActivity.this.gmuServerVersion;
                        return;
                    }
                    FVAboutPtzActivity.this.textImuFirmwareVersion.setText(FVAboutPtzActivity.this.imuServerVersion);
                    FVAboutPtzActivity.this.textImuFirmwareUpgrade.setText(FVAboutPtzActivity.this.getResources().getString(C0853R.string.label_is_latest_firmware));
                    FVAboutPtzActivity.this.textImuFirmwareUpgrade.setTextColor(FVAboutPtzActivity.this.getResources().getColor(C0853R.color.color_light_gray2));
                    BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVAboutPtzActivity.this.imuServerVersion;
                } else if (FVAboutPtzActivity.this.mUpgradeType == 1) {
                    BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVAboutPtzActivity.this.gmuServerVersion;
                } else {
                    BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVAboutPtzActivity.this.imuServerVersion;
                }
            }
        });
        this.selfDialog.show();
    }

    private String[] setReleasenotesText(String s) {
        if (!s.equals("")) {
            return s.substring(3, s.length()).split("##");
        }
        return null;
    }

    public void onBackPressed() {
        if (BlePtzParasConstant.isFirmwareUpgrading) {
            Toast.makeText(this, getString(C0853R.string.upgrading_donot_leave), 1).show();
        } else {
            super.onBackPressed();
        }
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
