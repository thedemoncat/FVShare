package com.freevisiontech.fvmobile.fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.p001v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVWebActivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleNotifyDataUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.umeng.analytics.pro.C0217dk;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAdvancedSetRockerFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "AdvancedSetRocker";
    @Bind({2131756063})
    CheckBox cb_left_or_right;
    @Bind({2131756062})
    CheckBox cb_up_or_down;
    /* access modifiers changed from: private */
    public boolean connected = false;
    /* access modifiers changed from: private */
    public int followX = 0;
    /* access modifiers changed from: private */
    public int followY = 0;
    private List itemVisibleSeekBarSort;
    /* access modifiers changed from: private */
    public List itemVisibleSort;
    @Bind({2131756057})
    TextView layout_advance_joystick_dead_zone_what;
    @Bind({2131755966})
    LinearLayout ll_connect_ptz;
    @Bind({2131755965})
    LinearLayout ll_no_connect_ptz;
    private Context mContext;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    FVAdvancedSetRockerFragment.this.sendRockerFollow();
                    return;
                default:
                    return;
            }
        }
    };
    @Bind({2131756061})
    RadioButton rb_left_right;
    @Bind({2131756060})
    RadioButton rb_up_down;
    @Bind({2131756059})
    RadioGroup rg_rocker_orientation;
    @Bind({2131756051})
    RelativeLayout rl_pan_axis_dead_zone;
    @Bind({2131756034})
    RelativeLayout rl_pan_axis_sensitivity;
    @Bind({2131756043})
    RelativeLayout rl_pan_speed;
    @Bind({2131756048})
    RelativeLayout rl_pitch_axis_dead_zone;
    @Bind({2131756031})
    RelativeLayout rl_pitch_axis_sensitivity;
    @Bind({2131756040})
    RelativeLayout rl_pitch_speed;
    @Bind({2131756054})
    RelativeLayout rl_roll_axis_dead_zone;
    @Bind({2131756037})
    RelativeLayout rl_roll_axis_sensitivity;
    @Bind({2131755985})
    RelativeLayout rl_roll_speed;
    private Runnable runnable = new Runnable() {
        public void run() {
            FVAdvancedSetRockerFragment.this.send();
        }
    };
    private Runnable runnableScr = new Runnable() {
        public void run() {
            int stP = FVAdvancedSetRockerFragment.this.getPosMoveToTopDistance(FVAdvancedSetRockerFragment.this.stirPosition);
            if (FVAdvancedSetRockerFragment.this.scrollview != null) {
                FVAdvancedSetRockerFragment.this.scrollview.scrollTo(0, stP);
            }
        }
    };
    @Bind({2131756049})
    SeekBar sb_pitch_dead_zone;
    @Bind({2131756032})
    SeekBar sb_pitch_sensitivity;
    @Bind({2131756041})
    SeekBar sb_pitch_speed;
    @Bind({2131756055})
    SeekBar sb_roll_dead_zone;
    @Bind({2131756038})
    SeekBar sb_roll_sensitivity;
    @Bind({2131756046})
    SeekBar sb_roll_speed;
    @Bind({2131756052})
    SeekBar sb_trunnion_dead_zone;
    @Bind({2131756035})
    SeekBar sb_trunnion_sensitivity;
    @Bind({2131756044})
    SeekBar sb_trunnion_speed;
    private boolean scaleSlide = false;
    @Bind({2131755203})
    ScrollView scrollview;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    @Bind({2131756050})
    TextView tv_pitch_dead_pg;
    @Bind({2131756042})
    TextView tv_pitch_sp_pg;
    @Bind({2131756033})
    TextView tv_pitch_st_pg;
    @Bind({2131756056})
    TextView tv_roll_dead_pg;
    @Bind({2131756047})
    TextView tv_roll_sp_pg;
    @Bind({2131756039})
    TextView tv_roll_st_pg;
    @Bind({2131756053})
    TextView tv_trunnion_dead_pg;
    @Bind({2131756045})
    TextView tv_trunnion_sp_pg;
    @Bind({2131756036})
    TextView tv_trunnion_st_pg;
    private int variValue = 1;
    private BluetoothGattCharacteristic writeCharacteristic;

    /* access modifiers changed from: private */
    public void sendRockerFollow() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Random random = new Random();
                    int a = random.nextInt(SupportMenu.USER_MASK);
                    int b = random.nextInt(SupportMenu.USER_MASK);
                    ViseLog.m1466e("a---" + a + "b---" + b);
                    BleByteUtil.sendPtzRockerData(a, b, FVAdvancedSetRockerFragment.this.followX, FVAdvancedSetRockerFragment.this.followY);
                    int unused = FVAdvancedSetRockerFragment.this.followX = FVAdvancedSetRockerFragment.this.followX + 100;
                    int unused2 = FVAdvancedSetRockerFragment.this.followY = FVAdvancedSetRockerFragment.this.followY + 100;
                    if (FVAdvancedSetRockerFragment.this.followX > 100) {
                        int unused3 = FVAdvancedSetRockerFragment.this.followX = 0;
                        int unused4 = FVAdvancedSetRockerFragment.this.followY = 0;
                    }
                    SystemClock.sleep(10);
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void send() {
        Random random = new Random();
        int a = random.nextInt(SupportMenu.USER_MASK);
        int b = random.nextInt(SupportMenu.USER_MASK);
        ViseLog.m1466e("a---" + a + "b---" + b);
        BleByteUtil.sendPtzRockerData(a, b, this.followX, this.followY);
        this.followX++;
        this.followY++;
        if (this.followX > 65535) {
            this.followX = 0;
        }
        if (this.followY > 65535) {
            this.followY = 0;
        }
        this.mHandler.postDelayed(this.runnable, 20);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.layout_advance_set_rocker, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        this.itemVisibleSort = new ArrayList(Arrays.asList(new String[]{"rl_pitch_axis_sensitivity", "rl_pan_axis_sensitivity", "rl_roll_axis_sensitivity", "rl_pitch_speed", "rl_pan_speed", "rl_roll_speed", "rl_pitch_axis_dead_zone", "rl_pan_axis_dead_zone", "rl_roll_axis_dead_zone", "cb_up_or_down", "cb_left_or_right"}));
        this.itemVisibleSeekBarSort = new ArrayList(Arrays.asList(new String[]{"sb_pitch_sensitivity", "sb_trunnion_sensitivity", "sb_roll_sensitivity", "sb_pitch_speed", "sb_trunnion_speed", "sb_roll_speed", "sb_pitch_dead_zone", "sb_trunnion_dead_zone", "sb_roll_dead_zone", "cb_up_or_down", "cb_left_or_right"}));
        initView();
        initData();
        this.layout_advance_joystick_dead_zone_what.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVAdvancedSetRockerFragment.this.startActivity(FVWebActivity.creatIntent(FVAdvancedSetRockerFragment.this.getActivity(), BleConstant.SHUTTER));
            }
        });
        CameraUtils.setFrameLayerNumber(24);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    int unused = FVAdvancedSetRockerFragment.this.stirPosition = 0;
                    FVAdvancedSetRockerFragment.this.setControlOnClickItemBlackgroundColor(FVAdvancedSetRockerFragment.this.controlItemStringToView(FVAdvancedSetRockerFragment.this.itemVisibleSort.get(FVAdvancedSetRockerFragment.this.stirPosition).toString()));
                }
            }, 100);
        }
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_STIR_UP_210 /*107706*/:
                if (CameraUtils.getFrameLayerNumber() == 24) {
                    String value2 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   波轮拨动向下   波轮拨动向下" + this.stirPosition);
                    if (this.stirPosition < 9) {
                        setControlSeekBarValue(this.stirPosition, true, Integer.valueOf(value2).intValue());
                        setControlItemSeekBarGlayToRed(controlItemStringToSeekBarView(this.itemVisibleSeekBarSort.get(this.stirPosition).toString()));
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_SETTING_STIR_DOWN_210 /*107707*/:
                if (CameraUtils.getFrameLayerNumber() == 24) {
                    String value22 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向上   波轮拨动向上   波轮拨动向上" + this.stirPosition);
                    if (this.stirPosition < 9) {
                        setControlSeekBarValue(this.stirPosition, false, Integer.valueOf(value22).intValue());
                        setControlItemSeekBarGlayToRed(controlItemStringToSeekBarView(this.itemVisibleSeekBarSort.get(this.stirPosition).toString()));
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 24) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  OK键  OK键  OK键  OK键" + this.itemVisibleSort.get(this.stirPosition).toString());
                    if (this.stirPosition >= 9) {
                        setControlOnClickItemDownTouch(controlItemStringToSeekBarView(this.itemVisibleSeekBarSort.get(this.stirPosition).toString()));
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 24) {
                    setControlOnClickSeekBarRedToGlay();
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 摇杆拨动向上   向上   向上 " + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 24) {
                    setControlOnClickSeekBarRedToGlay();
                    this.stirPosition++;
                    if (this.stirPosition > this.itemVisibleSort.size() - 1) {
                        this.stirPosition = this.itemVisibleSort.size() - 1;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  摇杆拨动向下   向下   向下" + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setControlSeekBarValue(int num, boolean boo, int vaValue) {
        int progress;
        int progress2;
        int progress3;
        int progress4;
        int progress5;
        int progress6;
        int progress7;
        int progress8;
        int progress9;
        this.variValue = vaValue;
        switch (num) {
            case 0:
                int progress10 = this.sb_pitch_sensitivity.getProgress();
                if (!boo) {
                    progress9 = progress10 - this.variValue;
                    if (progress9 < 0) {
                        progress9 = 0;
                    }
                } else {
                    progress9 = progress10 + this.variValue;
                    if (progress9 > 100) {
                        progress9 = 100;
                    }
                }
                this.tv_pitch_st_pg.setText(progress9 + "");
                this.sb_pitch_sensitivity.setProgress(progress9);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) progress9, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 1:
                int progress11 = this.sb_trunnion_sensitivity.getProgress();
                if (!boo) {
                    progress8 = progress11 - this.variValue;
                    if (progress8 < 0) {
                        progress8 = 0;
                    }
                } else {
                    progress8 = progress11 + this.variValue;
                    if (progress8 > 100) {
                        progress8 = 100;
                    }
                }
                this.tv_trunnion_st_pg.setText(progress8 + "");
                this.sb_trunnion_sensitivity.setProgress(progress8);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) progress8);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 2:
                int progress12 = this.sb_roll_sensitivity.getProgress();
                if (!boo) {
                    progress7 = progress12 - this.variValue;
                    if (progress7 < 0) {
                        progress7 = 0;
                    }
                } else {
                    progress7 = progress12 + this.variValue;
                    if (progress7 > 100) {
                        progress7 = 100;
                    }
                }
                this.tv_roll_st_pg.setText(progress7 + "");
                this.sb_roll_sensitivity.setProgress(progress7);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) progress7, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 3:
                int progress13 = this.sb_pitch_speed.getProgress();
                if (!boo) {
                    progress6 = progress13 - this.variValue;
                    if (progress6 < 0) {
                        progress6 = 0;
                    }
                } else {
                    progress6 = progress13 + this.variValue;
                    if (progress6 > 100) {
                        progress6 = 100;
                    }
                }
                this.tv_pitch_sp_pg.setText(progress6 + "");
                this.sb_pitch_speed.setProgress(progress6);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) progress6, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 4:
                int progress14 = this.sb_trunnion_speed.getProgress();
                if (!boo) {
                    progress5 = progress14 - this.variValue;
                    if (progress5 < 0) {
                        progress5 = 0;
                    }
                } else {
                    progress5 = progress14 + this.variValue;
                    if (progress5 > 100) {
                        progress5 = 100;
                    }
                }
                this.tv_trunnion_sp_pg.setText(progress5 + "");
                this.sb_trunnion_speed.setProgress(progress5);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED, (byte) progress5);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 5:
                int progress15 = this.sb_roll_speed.getProgress();
                if (!boo) {
                    progress4 = progress15 - this.variValue;
                    if (progress4 < 0) {
                        progress4 = 0;
                    }
                } else {
                    progress4 = progress15 + this.variValue;
                    if (progress4 > 100) {
                        progress4 = 100;
                    }
                }
                this.tv_roll_sp_pg.setText(progress4 + "");
                this.sb_roll_speed.setProgress(progress4);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED, (byte) progress4, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 6:
                int progress16 = this.sb_pitch_dead_zone.getProgress();
                if (!boo) {
                    progress3 = progress16 - this.variValue;
                    if (progress3 < 0) {
                        progress3 = 0;
                    }
                } else {
                    progress3 = progress16 + this.variValue;
                    if (progress3 > 100) {
                        progress3 = 100;
                    }
                }
                this.tv_pitch_dead_pg.setText(progress3 + "");
                this.sb_pitch_dead_zone.setProgress(progress3);
                if (this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) progress3, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 7:
                int progress17 = this.sb_trunnion_dead_zone.getProgress();
                if (!boo) {
                    progress2 = progress17 - this.variValue;
                    if (progress2 < 0) {
                        progress2 = 0;
                    }
                } else {
                    progress2 = progress17 + this.variValue;
                    if (progress2 > 100) {
                        progress2 = 100;
                    }
                }
                this.tv_trunnion_dead_pg.setText(progress2 + "");
                this.sb_trunnion_dead_zone.setProgress(progress2);
                if (this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE, (byte) progress2, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case 8:
                int progress18 = this.sb_roll_dead_zone.getProgress();
                if (!boo) {
                    progress = progress18 - this.variValue;
                    if (progress < 0) {
                        progress = 0;
                    }
                } else {
                    progress = progress18 + this.variValue;
                    if (progress > 100) {
                        progress = 100;
                    }
                }
                this.tv_roll_dead_pg.setText(progress + "");
                this.sb_roll_dead_zone.setProgress(progress);
                if (this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            default:
                return;
        }
    }

    private void setControlOnClickSeekBarRedToGlay() {
        this.sb_pitch_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_pitch_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_trunnion_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_trunnion_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_roll_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_roll_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_trunnion_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_trunnion_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_roll_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_roll_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_pitch_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_pitch_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_trunnion_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_trunnion_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.sb_roll_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.sb_roll_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
    }

    public void setControlItemSeekBarGlayToRed(View view) {
        if (view == this.sb_pitch_sensitivity) {
            this.sb_pitch_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_pitch_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_trunnion_sensitivity) {
            this.sb_trunnion_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_trunnion_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_roll_sensitivity) {
            this.sb_roll_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_roll_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_pitch_speed) {
            this.sb_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_trunnion_speed) {
            this.sb_trunnion_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_trunnion_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_roll_speed) {
            this.sb_roll_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_roll_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_pitch_dead_zone) {
            this.sb_pitch_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_pitch_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_trunnion_dead_zone) {
            this.sb_trunnion_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_trunnion_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.sb_roll_dead_zone) {
            this.sb_roll_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.sb_roll_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        }
    }

    public void setControlOnClickItemDownTouch(View view) {
        if (view == this.cb_up_or_down) {
            if (this.cb_up_or_down.isChecked()) {
                this.cb_up_or_down.setChecked(false);
            } else {
                this.cb_up_or_down.setChecked(true);
            }
        } else if (view != this.cb_left_or_right) {
        } else {
            if (this.cb_left_or_right.isChecked()) {
                this.cb_left_or_right.setChecked(false);
            } else {
                this.cb_left_or_right.setChecked(true);
            }
        }
    }

    public void setControlOnClickItemDown(View view) {
        if (view == this.cb_up_or_down) {
            if (this.cb_up_or_down.isChecked()) {
                this.cb_up_or_down.setChecked(false);
            } else {
                this.cb_up_or_down.setChecked(true);
            }
        } else if (view != this.cb_left_or_right) {
            this.sb_pitch_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_pitch_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_trunnion_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_trunnion_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_roll_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_roll_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_trunnion_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_trunnion_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_roll_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_roll_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_pitch_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_pitch_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_trunnion_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_trunnion_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            this.sb_roll_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
            this.sb_roll_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
            if (view == this.sb_pitch_sensitivity) {
                this.sb_pitch_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_pitch_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_trunnion_sensitivity) {
                this.sb_trunnion_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_trunnion_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_roll_sensitivity) {
                this.sb_roll_sensitivity.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_roll_sensitivity.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_pitch_speed) {
                this.sb_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_trunnion_speed) {
                this.sb_trunnion_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_trunnion_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_roll_speed) {
                this.sb_roll_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_roll_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_pitch_dead_zone) {
                this.sb_pitch_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_pitch_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_trunnion_dead_zone) {
                this.sb_trunnion_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_trunnion_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            } else if (view == this.sb_roll_dead_zone) {
                this.sb_roll_dead_zone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.sb_roll_dead_zone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
            }
        } else if (this.cb_left_or_right.isChecked()) {
            this.cb_left_or_right.setChecked(false);
        } else {
            this.cb_left_or_right.setChecked(true);
        }
    }

    public View controlItemStringToSeekBarView(String str) {
        if (str.equals("sb_pitch_sensitivity")) {
            return this.sb_pitch_sensitivity;
        }
        if (str.equals("sb_trunnion_sensitivity")) {
            return this.sb_trunnion_sensitivity;
        }
        if (str.equals("sb_roll_sensitivity")) {
            return this.sb_roll_sensitivity;
        }
        if (str.equals("sb_pitch_speed")) {
            return this.sb_pitch_speed;
        }
        if (str.equals("sb_trunnion_speed")) {
            return this.sb_trunnion_speed;
        }
        if (str.equals("sb_roll_speed")) {
            return this.sb_roll_speed;
        }
        if (str.equals("sb_pitch_dead_zone")) {
            return this.sb_pitch_dead_zone;
        }
        if (str.equals("sb_trunnion_dead_zone")) {
            return this.sb_trunnion_dead_zone;
        }
        if (str.equals("sb_roll_dead_zone")) {
            return this.sb_roll_dead_zone;
        }
        if (str.equals("cb_up_or_down")) {
            return this.cb_up_or_down;
        }
        return this.cb_left_or_right;
    }

    public View controlItemStringToView(String str) {
        if (str.equals("rl_pitch_axis_sensitivity")) {
            return this.rl_pitch_axis_sensitivity;
        }
        if (str.equals("rl_pan_axis_sensitivity")) {
            return this.rl_pan_axis_sensitivity;
        }
        if (str.equals("rl_roll_axis_sensitivity")) {
            return this.rl_roll_axis_sensitivity;
        }
        if (str.equals("rl_pitch_speed")) {
            return this.rl_pitch_speed;
        }
        if (str.equals("rl_pan_speed")) {
            return this.rl_pan_speed;
        }
        if (str.equals("rl_roll_speed")) {
            return this.rl_roll_speed;
        }
        if (str.equals("rl_pitch_axis_dead_zone")) {
            return this.rl_pitch_axis_dead_zone;
        }
        if (str.equals("rl_pan_axis_dead_zone")) {
            return this.rl_pan_axis_dead_zone;
        }
        if (str.equals("rl_roll_axis_dead_zone")) {
            return this.rl_roll_axis_dead_zone;
        }
        if (str.equals("cb_up_or_down")) {
            return this.cb_up_or_down;
        }
        return this.cb_left_or_right;
    }

    public void setControlOnClickItemBlackgroundColor(View view) {
        this.rl_pitch_axis_sensitivity.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pan_axis_sensitivity.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_axis_sensitivity.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pitch_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pan_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pitch_axis_dead_zone.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pan_axis_dead_zone.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_axis_dead_zone.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.cb_up_or_down.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.cb_left_or_right.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        view.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.black15));
        new Handler().postDelayed(this.runnableScr, 200);
    }

    /* access modifiers changed from: private */
    public int getPosMoveToTopDistance(int pos) {
        return (Util.dip2px(this.mContext, 50.0f) * pos) + (Util.dip2px(this.mContext, 15.0f) * pos);
    }

    private void sendRockerData() {
        if (this.connected) {
            this.mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    private void initView() {
        this.rg_rocker_orientation.setOnCheckedChangeListener(this);
        this.sb_pitch_sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_pitch_sensitivity进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_pitch_st_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_pitch_st_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_trunnion_sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_trunnion_sensitivity进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_trunnion_st_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_trunnion_st_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) progress);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_roll_sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_trunnion_sensitivity进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_roll_st_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_roll_st_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f722l, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_pitch_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVAdvancedSetRockerFragment.this.tv_pitch_sp_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_pitch_sp_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_trunnion_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVAdvancedSetRockerFragment.this.tv_trunnion_sp_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_trunnion_sp_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED, (byte) progress);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_roll_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVAdvancedSetRockerFragment.this.tv_roll_sp_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_roll_sp_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f721k, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_pitch_dead_zone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_pitch_dead_zone进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_pitch_dead_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_pitch_dead_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_trunnion_dead_zone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_trunnion_dead_zone进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_trunnion_dead_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_trunnion_dead_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE, (byte) BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE, (byte) progress);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.sb_roll_dead_zone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetRockerFragment.TAG, "onProgressChanged: sb_trunnion_dead_zone进度改变了" + i);
                FVAdvancedSetRockerFragment.this.tv_roll_dead_pg.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetRockerFragment.this.tv_roll_dead_pg.setText(progress + "");
                if (FVAdvancedSetRockerFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 12, (byte) BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
                } else {
                    Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
    }

    private void initData() {
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            this.ll_no_connect_ptz.setVisibility(8);
            this.ll_connect_ptz.setVisibility(0);
            this.sb_pitch_sensitivity.setProgress(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE);
            this.sb_trunnion_sensitivity.setProgress(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE);
            this.sb_roll_sensitivity.setProgress(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE);
            this.sb_pitch_speed.setProgress(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED);
            this.sb_trunnion_speed.setProgress(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED);
            this.sb_roll_speed.setProgress(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED);
            this.sb_pitch_dead_zone.setProgress(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE);
            this.sb_trunnion_dead_zone.setProgress(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE);
            this.sb_roll_dead_zone.setProgress(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE);
            this.tv_pitch_st_pg.setText(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SENSITIVITY_CURVE + "");
            this.tv_trunnion_st_pg.setText(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SENSITIVITY_CURVE + "");
            this.tv_roll_st_pg.setText(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SENSITIVITY_CURVE + "");
            this.tv_pitch_sp_pg.setText(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_SPEED + "");
            this.tv_trunnion_sp_pg.setText(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_SPEED + "");
            this.tv_roll_sp_pg.setText(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_SPEED + "");
            this.tv_pitch_dead_pg.setText(BlePtzParasConstant.SET_PITCH_OF_ROCKER_FOR_DEAD_ZONE + "");
            this.tv_trunnion_dead_pg.setText(BlePtzParasConstant.SET_YAW_OF_ROCKER_FOR_DEAD_ZONE + "");
            this.tv_roll_dead_pg.setText(BlePtzParasConstant.SET_ROLL_OF_ROCKER_FOR_DEAD_ZONE + "");
            if (BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION == 1) {
                this.cb_up_or_down.setChecked(true);
                setCheckedStatusDrawble(this.cb_up_or_down);
            }
            if (BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION == 1) {
                this.cb_left_or_right.setChecked(true);
                setCheckedStatusDrawble(this.cb_left_or_right);
            }
        } else {
            if (this.ll_no_connect_ptz != null) {
                this.ll_no_connect_ptz.setVisibility(0);
            }
            if (this.ll_connect_ptz != null) {
                this.ll_connect_ptz.setVisibility(8);
            }
        }
        this.cb_up_or_down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.e(FVAdvancedSetRockerFragment.TAG, "onCheckedChanged: aaa");
                    if (FVAdvancedSetRockerFragment.this.connected) {
                        FVAdvancedSetRockerFragment.this.setCheckedStatusDrawble(FVAdvancedSetRockerFragment.this.cb_up_or_down);
                        if (BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION == 1) {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 1, (byte) 1);
                        } else {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 1, (byte) 0);
                        }
                    } else {
                        Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                } else {
                    Log.e(FVAdvancedSetRockerFragment.TAG, "onCheckedChanged: bbb");
                    if (FVAdvancedSetRockerFragment.this.connected) {
                        FVAdvancedSetRockerFragment.this.setUncheckedStatusDrawble(FVAdvancedSetRockerFragment.this.cb_up_or_down);
                        if (BlePtzParasConstant.SET_LEFT_OR_RIGHT_OF_ROCKER_FOR_ORIENTATION == 1) {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 0, (byte) 1);
                        } else {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 0, (byte) 0);
                        }
                    } else {
                        Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                }
            }
        });
        this.cb_left_or_right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.e(FVAdvancedSetRockerFragment.TAG, "onCheckedChanged: aaa");
                    if (FVAdvancedSetRockerFragment.this.connected) {
                        FVAdvancedSetRockerFragment.this.setCheckedStatusDrawble(FVAdvancedSetRockerFragment.this.cb_left_or_right);
                        if (BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION == 1) {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 1, (byte) 1);
                        } else {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 0, (byte) 1);
                        }
                    } else {
                        Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                } else {
                    Log.e(FVAdvancedSetRockerFragment.TAG, "onCheckedChanged: bbb");
                    if (FVAdvancedSetRockerFragment.this.connected) {
                        FVAdvancedSetRockerFragment.this.setUncheckedStatusDrawble(FVAdvancedSetRockerFragment.this.cb_left_or_right);
                        if (BlePtzParasConstant.SET_UP_OR_DOWN_OF_ROCKER_FOR_ORIENTATION == 1) {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 1, (byte) 0);
                        } else {
                            BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 0, (byte) 0);
                        }
                    } else {
                        Toast.makeText(FVAdvancedSetRockerFragment.this.getActivity(), FVAdvancedSetRockerFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void setUncheckedStatusDrawble(CheckBox checkbox) {
        Drawable drawable = getResources().getDrawable(C0853R.mipmap.ic_select);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        checkbox.setCompoundDrawables((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
    }

    /* access modifiers changed from: private */
    public void setCheckedStatusDrawble(CheckBox checkbox) {
        Drawable drawable = getResources().getDrawable(C0853R.mipmap.ic_select);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        checkbox.setCompoundDrawables((Drawable) null, (Drawable) null, drawable, (Drawable) null);
    }

    private void setEnableTrueOrfalse(RadioButton rbtn) {
        rbtn.setEnabled(false);
    }

    private void clearEnable() {
        this.rb_up_down.setEnabled(true);
        this.rb_left_right.setEnabled(true);
    }

    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case C0853R.C0855id.rb_up_down:
                Log.e(TAG, "onCheckedChanged: 上下反向被选择了");
                setEnableTrueOrfalse(this.rb_up_down);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 1, (byte) 0);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case C0853R.C0855id.rb_left_right:
                Log.e(TAG, "onCheckedChanged: 左右反向被选择了");
                setEnableTrueOrfalse(this.rb_left_right);
                if (this.connected) {
                    BleByteUtil.setPTZParameters(C0217dk.f723m, (byte) 0, (byte) 1);
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
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
        byte settingnotifydata;
        switch (event.getCode()) {
            case 34:
                this.connected = false;
                initData();
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                ViseLog.m1466e("receive data :FVAdvancedSetRockerFragment:command" + value[0] + "subcommand" + value[1]);
                if ((value[0] & 255) != 165 || (settingnotifydata = BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value)) == 12 || settingnotifydata == 13 || settingnotifydata == 14 || settingnotifydata == 15) {
                }
                return;
            default:
                return;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: rocker fragment onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroyView: rocker fragment onDestroy");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
    }
}
