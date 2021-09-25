package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentTransaction;
import android.support.p001v4.view.MotionEventCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetCameraFragment;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetInfoFragment;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetPTZFragment;
import com.freevisiontech.fvmobile.fragment.FVAdvancedSetRockerFragment;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.umeng.analytics.MobclickAgent;
import com.vise.log.ViseLog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAdvancedSettingAvtivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "FVAdvancedSetting";
    @Bind({2131755216})
    RadioButton btn_camera;
    @Bind({2131755219})
    RadioButton btn_info;
    @Bind({2131755217})
    RadioButton btn_ptz;
    @Bind({2131755218})
    RadioButton btn_rocker;
    private FVAdvancedSetCameraFragment cameraFragment;
    @Bind({2131755220})
    FrameLayout fl_main;
    private FragmentManager fragmentManager;
    @Bind({2131755249})
    ImageView img_back;
    @Bind({2131756129})
    ImageView img_right;
    private FVAdvancedSetInfoFragment infoFragment;
    /* access modifiers changed from: private */
    public int picSize;
    private FVAdvancedSetPTZFragment ptzFragment;
    private int radioButtonCheckNum = 0;
    @Bind({2131755215})
    RadioGroup rg_bottom_layout;
    private FVAdvancedSetRockerFragment rockerFragment;
    @Bind({2131756127})
    TextView tv_center_title;
    @Bind({2131756128})
    TextView tv_right;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_advanced_setting);
        getWindow().setFlags(1024, 1024);
        Util.hideBottomUIMenu(this);
        ButterKnife.bind((Activity) this);
        initTitle();
        initView();
        this.picSize = CameraUtils.getMaxSupOrReComPictureSize();
        BaseActivityManager.getActivityManager().pushActivity(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            CameraUtils.setFrameLayerNumber(20);
            this.btn_camera.setTextColor(-18664);
        }
        CameraUtils.setFvAdvancedSettingActivityIsShow(true);
    }

    private void initTitle() {
        this.img_back.setVisibility(0);
        this.img_back.setImageResource(C0853R.mipmap.fanhui);
        this.tv_center_title.setVisibility(0);
        this.tv_center_title.setText(getResources().getString(C0853R.string.advanced_settingo));
        this.tv_right.setVisibility(8);
        this.img_right.setVisibility(8);
        this.img_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (FVAdvancedSettingAvtivity.this.picSize != CameraUtils.getMaxSupOrReComPictureSize()) {
                    Util.sendIntEventMessge(Constants.RESOLUTION_1080);
                }
                FVAdvancedSettingAvtivity.this.finish();
            }
        });
    }

    private void initView() {
        this.fragmentManager = getSupportFragmentManager();
        this.cameraFragment = new FVAdvancedSetCameraFragment();
        this.ptzFragment = new FVAdvancedSetPTZFragment();
        this.rg_bottom_layout.setOnCheckedChangeListener(this);
        int from = getIntent().getIntExtra("from", 1);
        ViseLog.m1466e("from" + from);
        if (from == 1) {
            this.btn_camera.setChecked(true);
            replaceFragment(this.cameraFragment);
            return;
        }
        this.btn_ptz.setChecked(true);
        replaceFragment(this.ptzFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = this.fragmentManager.beginTransaction();
        transaction.replace(C0853R.C0855id.fl_main, fragment);
        transaction.commit();
    }

    private void setSelect(RadioButton rbtn) {
        rbtn.setSelected(true);
        rbtn.setTextColor(-12500671);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            rbtn.setTextColor(-18664);
        }
    }

    private void clearSelect() {
        this.btn_camera.setSelected(false);
        this.btn_ptz.setSelected(false);
        this.btn_rocker.setSelected(false);
        this.btn_info.setSelected(false);
        this.btn_camera.setTextColor(-5131334);
        this.btn_ptz.setTextColor(-5131334);
        this.btn_rocker.setTextColor(-5131334);
        this.btn_info.setTextColor(-5131334);
    }

    private void radioButtonCheck(int num) {
        CameraUtils.setFvAdvancedSettingActivityIsShow(false);
        clearSelect();
        switch (num) {
            case 0:
                replaceFragment(this.cameraFragment);
                setSelect(this.btn_camera);
                Log.e(TAG, "onCheckedChanged: 相机被选择了");
                return;
            case 1:
                replaceFragment(this.ptzFragment);
                setSelect(this.btn_ptz);
                Log.e(TAG, "onCheckedChanged: 云台被选择了");
                return;
            case 2:
                this.rockerFragment = new FVAdvancedSetRockerFragment();
                replaceFragment(this.rockerFragment);
                setSelect(this.btn_rocker);
                Log.e(TAG, "onCheckedChanged: 摇杆被选择了");
                return;
            case 3:
                this.infoFragment = new FVAdvancedSetInfoFragment();
                replaceFragment(this.infoFragment);
                setSelect(this.btn_info);
                Log.e(TAG, "onCheckedChanged: 信息被选择了");
                return;
            default:
                return;
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        CameraUtils.setFvAdvancedSettingActivityIsShow(false);
        clearSelect();
        switch (i) {
            case C0853R.C0855id.btn_camera:
                replaceFragment(this.cameraFragment);
                setSelect(this.btn_camera);
                Log.e(TAG, "onCheckedChanged: 相机被选择了");
                this.radioButtonCheckNum = 0;
                return;
            case C0853R.C0855id.btn_ptz:
                replaceFragment(this.ptzFragment);
                setSelect(this.btn_ptz);
                Log.e(TAG, "onCheckedChanged: 云台被选择了");
                this.radioButtonCheckNum = 1;
                return;
            case C0853R.C0855id.btn_rocker:
                this.rockerFragment = new FVAdvancedSetRockerFragment();
                replaceFragment(this.rockerFragment);
                setSelect(this.btn_rocker);
                Log.e(TAG, "onCheckedChanged: 摇杆被选择了");
                this.radioButtonCheckNum = 2;
                return;
            case C0853R.C0855id.btn_info:
                this.infoFragment = new FVAdvancedSetInfoFragment();
                replaceFragment(this.infoFragment);
                setSelect(this.btn_info);
                Log.e(TAG, "onCheckedChanged: 信息被选择了");
                this.radioButtonCheckNum = 3;
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[0] & 255) == 90) {
                    processDataForX(value);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void processDataForX(byte[] value) {
        if ((value[0] & 255) != 90) {
            return;
        }
        if ((value[1] & 255) == 72) {
            ViseLog.m1466e("ViltaX--0x48--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 1);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        this.radioButtonCheckNum++;
                        if (this.radioButtonCheckNum > 3) {
                            this.radioButtonCheckNum = 3;
                        }
                        radioButtonCheck(this.radioButtonCheckNum);
                    }
                    ViseLog.m1466e("ViltaX→" + this.radioButtonCheckNum);
                    return;
                case 2:
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 2);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        this.radioButtonCheckNum--;
                        if (this.radioButtonCheckNum < 0) {
                            this.radioButtonCheckNum = 0;
                        }
                        radioButtonCheck(this.radioButtonCheckNum);
                    }
                    ViseLog.m1466e("ViltaX←" + this.radioButtonCheckNum);
                    return;
                case 3:
                    ViseLog.m1466e("ViltaX↑   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 3);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_SETTING_ROCKING_BAR_UP_210);
                        return;
                    }
                    return;
                case 4:
                    ViseLog.m1466e("ViltaX↓   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 4);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else if ((value[1] & 255) == 70) {
            ViseLog.m1466e("ViltaX--0x46--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX1：菜单键单击：菜单/返回   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 1);
                    if (CameraUtils.getCurrentPageIndex() != 2) {
                        return;
                    }
                    if (CameraUtils.getFrameLayerNumber() == 20 || CameraUtils.getFrameLayerNumber() == 21 || CameraUtils.getFrameLayerNumber() == 22 || CameraUtils.getFrameLayerNumber() == 23 || CameraUtils.getFrameLayerNumber() == 24) {
                        finish();
                        return;
                    } else {
                        Util.sendIntEventMessge(Constants.LABEL_SETTING_RETURN_KEY_210);
                        return;
                    }
                case 2:
                    ViseLog.m1466e("ViltaX2：菜单键长按：菜单消失   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 2);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_SETTING_LONG_RETURN_KEY_210);
                        finish();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                CameraUtils.setFrameLayerNumber(0);
                            }
                        }, 100);
                        return;
                    }
                    return;
                case 3:
                    ViseLog.m1466e("ViltaX3：确认键单击：确认/DISP   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210);
                        return;
                    }
                    return;
                case 5:
                case 9:
                case 11:
                case 13:
                case 15:
                case 17:
                case 19:
                    BleByteUtil.ackPTZPanorama((byte) (value[1] & 255), (byte) (value[2] & 255));
                    return;
                default:
                    return;
            }
        } else if ((value[1] & 255) == 53) {
            ViseLog.m1466e("ViltaX--FM210，在此处进行调参--0x35--data--" + HexUtil.encodeHexStr(value) + "   标识:" + CameraUtils.getFrameLayerNumber());
            if (CameraUtils.getCurrentPageIndex() == 2) {
                int delta = ((value[2] << 0) & 255) + ((value[3] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                if (delta > 32768) {
                    delta -= 65536;
                }
                int delta2 = delta / 4;
                if (delta2 > 0) {
                    Util.sendIntEventMessge((int) Constants.LABEL_SETTING_STIR_UP_210, String.valueOf(Math.abs(delta2)));
                } else {
                    Util.sendIntEventMessge((int) Constants.LABEL_SETTING_STIR_DOWN_210, String.valueOf(Math.abs(delta2)));
                }
            }
        } else if ((value[1] & 255) == 16 || (value[1] & 255) == 45) {
            BleByteUtil.ackPTZPanorama((byte) (value[1] & 255), (byte) (value[2] & 255));
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_RESTART);
        CameraUtils.setFvAdvancedSettingActivityIsShow(false);
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        CameraUtils.setFrameLayerNumber(0);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        if (CameraUtils.getCurrentPageIndex() == 2) {
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
    }
}
