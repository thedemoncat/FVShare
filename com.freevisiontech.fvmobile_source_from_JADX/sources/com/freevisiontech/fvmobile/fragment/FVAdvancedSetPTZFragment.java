package com.freevisiontech.fvmobile.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p003v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVCalibrationActivity;
import com.freevisiontech.fvmobile.activity.FVWebActivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleNotifyDataUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.widget.ControlDefaultCamDialog;
import com.freevisiontech.fvmobile.widget.StorageChangeDialog;
import com.freevisiontech.fvmobile.widget.WirelessChargeDialog;
import com.vise.log.ViseLog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAdvancedSetPTZFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "AdvancedSetPTZ";
    @Bind({2131756012})
    RelativeLayout calibration;
    /* access modifiers changed from: private */
    public boolean connected = false;
    private ControlDefaultCamDialog controlDefaultCamDialog;
    @Bind({2131756030})
    TextView controlDefaultCameraDesc;
    @Bind({2131756029})
    RelativeLayout control_default_camera;
    @Bind({2131755977})
    View custom_for_speed2_divider;
    @Bind({2131756014})
    TextView fn_title_tv;
    @Bind({2131756015})
    RelativeLayout fv_setting_rl;
    private List itemAllOnTouchSort;
    /* access modifiers changed from: private */
    public List itemAllSort;
    private String itemPositionStr;
    @Bind({2131755976})
    ImageView iv_custom_select;
    @Bind({2131756003})
    ImageView iv_exposure_select;
    @Bind({2131756021})
    ImageView iv_fn_flash;
    @Bind({2131756017})
    ImageView iv_fn_follow_hand;
    @Bind({2131756019})
    ImageView iv_fn_lock_mode;
    @Bind({2131756023})
    ImageView iv_fn_white_balance;
    @Bind({2131756025})
    ImageView iv_fn_wireless_charging;
    @Bind({2131756005})
    ImageView iv_roll_select;
    @Bind({2131755974})
    ImageView iv_sport_select;
    @Bind({2131755972})
    ImageView iv_walk_select;
    @Bind({2131756000})
    TextView layout_advance_dead_zone_what;
    @Bind({2131755988})
    TextView layout_advance_follow_speed_what;
    @Bind({2131755966})
    LinearLayout ll_connect_ptz;
    @Bind({2131755965})
    LinearLayout ll_no_connect_ptz;
    @Bind({2131756001})
    TextView long_click_mode_and_roll_mode_tv;
    @Bind({2131756006})
    View long_press_mode_focus_divider;
    @Bind({2131756007})
    LinearLayout long_press_mode_focus_rl;
    @Bind({2131756009})
    SwitchButton long_press_mode_focus_switch;
    private Context mContext;
    private StorageChangeDialog mFnLockModeSupportDialog;
    private Handler mHandler;
    private SeekBar.OnSeekBarChangeListener mOnRollSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            FVAdvancedSetPTZFragment.this.roll_axle_num_tv.setText(FVAdvancedSetPTZFragment.this.calculatePrgs(progress, false) + "");
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (FVAdvancedSetPTZFragment.this.connected) {
                BleByteUtil.setPTZParameters((byte) 11, (byte) 0, (byte) FVAdvancedSetPTZFragment.this.calculatePrgs(seekBar.getProgress(), false), (byte) 11);
            } else {
                Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        }
    };
    @Bind({2131756010})
    RelativeLayout phone_check;
    private boolean ptzModeChanged = false;
    @Bind({2131756013})
    RelativeLayout reset_to_default;
    @Bind({2131755978})
    RelativeLayout rl_custom_isvisible;
    @Bind({2131755975})
    RelativeLayout rl_custom_model;
    @Bind({2131756002})
    RelativeLayout rl_exposure_model;
    @Bind({2131756020})
    RelativeLayout rl_fn_flash;
    @Bind({2131756016})
    RelativeLayout rl_fn_follow_hand;
    @Bind({2131756018})
    RelativeLayout rl_fn_lock_mode;
    @Bind({2131756022})
    RelativeLayout rl_fn_white_balance;
    @Bind({2131756024})
    RelativeLayout rl_fn_wireless_charging;
    @Bind({2131755937})
    RelativeLayout rl_pitch_axis;
    @Bind({2131755979})
    RelativeLayout rl_pitch_spped;
    @Bind({2131755996})
    RelativeLayout rl_roll_axis;
    @Bind({2131756004})
    RelativeLayout rl_roll_model;
    @Bind({2131755985})
    RelativeLayout rl_roll_speed;
    @Bind({2131755973})
    RelativeLayout rl_sport_model;
    @Bind({2131755971})
    RelativeLayout rl_walk_model;
    @Bind({2131755992})
    RelativeLayout rl_yall_axis;
    @Bind({2131755982})
    RelativeLayout rl_yaw_speed;
    @Bind({2131755970})
    TextView roll_axle_num_tv;
    @Bind({2131755967})
    RelativeLayout roll_axle_rl;
    private Runnable runnable = new Runnable() {
        public void run() {
            int stP = FVAdvancedSetPTZFragment.this.getPosMoveToTopDistance(FVAdvancedSetPTZFragment.this.stirPosition);
            if (FVAdvancedSetPTZFragment.this.scrollview != null) {
                FVAdvancedSetPTZFragment.this.scrollview.scrollTo(0, stP);
            }
        }
    };
    private boolean scaleSlide = false;
    private boolean sceneModeChanged = false;
    @Bind({2131755203})
    ScrollView scrollview;
    private boolean seekBarItemValue = false;
    @Bind({2131755990})
    SeekBar seekbar_pitch_deadzone;
    @Bind({2131755980})
    SeekBar seekbar_pitch_speed;
    @Bind({2131755969})
    SeekBar seekbar_roll_axle;
    @Bind({2131755998})
    SeekBar seekbar_roll_deadzone;
    @Bind({2131755986})
    SeekBar seekbar_roll_spped;
    @Bind({2131755994})
    SeekBar seekbar_yaw_deadzone;
    @Bind({2131755983})
    SeekBar seekbar_yaw_speed;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    @Bind({2131756011})
    SwitchButton switchButton;
    private boolean switchButtonItemValue = false;
    @Bind({2131756027})
    com.kyleduo.switchbutton.SwitchButton switchButton_charging;
    @Bind({2131755991})
    TextView tv_pitch_pg_deadzone;
    @Bind({2131755981})
    TextView tv_pitch_pg_speed;
    @Bind({2131755999})
    TextView tv_roll_pg_deadzone;
    @Bind({2131755987})
    TextView tv_roll_pg_speed;
    @Bind({2131755995})
    TextView tv_yaw_pg_deadzone;
    @Bind({2131755984})
    TextView tv_yaw_pg_speed;
    private int variValue = 1;
    private WirelessChargeDialog wirelessChargeDialog;
    @Bind({2131756026})
    RelativeLayout wireless_charging_rl;
    @Bind({2131756028})
    TextView wireless_charging_tip;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(C0853R.layout.layout_advance_set_ptz, container, false);
        ButterKnife.bind((Object) this, view);
        this.mContext = getActivity();
        this.mHandler = new Handler();
        setAllParas();
        initView();
        initData();
        if (CameraUtils.getCurrentPageIndex() == 2) {
            newAddJavaAbout210();
            this.long_press_mode_focus_rl.setVisibility(8);
        }
        return view;
    }

    private void newAddJavaAbout210() {
        CameraUtils.setFrameLayerNumber(23);
        this.itemAllSort = new ArrayList(Arrays.asList(new String[]{"roll_axle_rl", "rl_walk_model", "rl_sport_model", "rl_custom_model", "rl_pitch_spped", "rl_yaw_speed", "rl_roll_speed", "rl_pitch_axis", "rl_yall_axis", "rl_roll_axis", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
        this.itemAllOnTouchSort = new ArrayList(Arrays.asList(new String[]{"seekbar_roll_axle", "rl_walk_model", "rl_sport_model", "rl_custom_model", "seekbar_pitch_speed", "seekbar_yaw_speed", "seekbar_roll_spped", "seekbar_pitch_deadzone", "seekbar_yaw_deadzone", "seekbar_roll_deadzone", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
        addControlDefaultCameraView();
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    int unused = FVAdvancedSetPTZFragment.this.stirPosition = 0;
                    FVAdvancedSetPTZFragment.this.setControlOnClickItemBlackgroundColor(FVAdvancedSetPTZFragment.this.controlItemStringToView(FVAdvancedSetPTZFragment.this.itemAllSort.get(FVAdvancedSetPTZFragment.this.stirPosition).toString()));
                    FVAdvancedSetPTZFragment.this.getViewVisibleTwoItem();
                }
            }, 100);
        }
        this.mFnLockModeSupportDialog = new StorageChangeDialog(this.mContext);
    }

    private void setStirPositionChangeValue(View view, Boolean viewVisible) {
        if (CameraUtils.getCurrentPageIndex() != 2) {
        }
    }

    /* access modifiers changed from: private */
    public void getViewVisibleTwoItem() {
        if (Boolean.valueOf(ViseBluetooth.getInstance().isConnected()).booleanValue() && CameraUtils.getCurrentPageIndex() == 2) {
            if (this.itemAllSort != null) {
                if (this.itemAllSort.get(this.stirPosition).equals("rl_custom_model") || this.itemAllSort.get(this.stirPosition).equals("rl_pitch_spped") || this.itemAllSort.get(this.stirPosition).equals("rl_yaw_speed") || this.itemAllSort.get(this.stirPosition).equals("rl_roll_speed")) {
                    this.stirPosition = 3;
                }
                this.itemPositionStr = this.itemAllSort.get(this.stirPosition).toString();
            }
            if (this.itemAllSort != null) {
                this.itemAllSort.clear();
            }
            if (this.itemAllOnTouchSort != null) {
                this.itemAllOnTouchSort.clear();
            }
            if (this.rl_custom_isvisible.getVisibility() == 0) {
                this.itemAllSort = new ArrayList(Arrays.asList(new String[]{"roll_axle_rl", "rl_walk_model", "rl_sport_model", "rl_custom_model", "rl_pitch_spped", "rl_yaw_speed", "rl_roll_speed", "rl_pitch_axis", "rl_yall_axis", "rl_roll_axis", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
                this.itemAllOnTouchSort = new ArrayList(Arrays.asList(new String[]{"seekbar_roll_axle", "rl_walk_model", "rl_sport_model", "rl_custom_model", "seekbar_pitch_speed", "seekbar_yaw_speed", "seekbar_roll_spped", "seekbar_pitch_deadzone", "seekbar_yaw_deadzone", "seekbar_roll_deadzone", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
                addControlDefaultCameraView();
            } else if (this.rl_custom_isvisible.getVisibility() == 8) {
                this.itemAllSort = new ArrayList(Arrays.asList(new String[]{"roll_axle_rl", "rl_walk_model", "rl_sport_model", "rl_custom_model", "rl_pitch_axis", "rl_yall_axis", "rl_roll_axis", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
                this.itemAllOnTouchSort = new ArrayList(Arrays.asList(new String[]{"seekbar_roll_axle", "rl_walk_model", "rl_sport_model", "rl_custom_model", "seekbar_pitch_speed", "seekbar_yaw_speed", "seekbar_roll_spped", "seekbar_pitch_deadzone", "seekbar_yaw_deadzone", "seekbar_roll_deadzone", "calibration", "reset_to_default", "rl_fn_follow_hand", "rl_fn_lock_mode", "rl_fn_flash", "rl_fn_white_balance", "rl_fn_wireless_charging", "wireless_charging_rl"}));
                addControlDefaultCameraView();
            }
            if (this.itemAllSort != null) {
                for (int s = 0; s < this.itemAllSort.size(); s++) {
                    if (this.itemAllSort.get(s).toString().equals(this.itemPositionStr)) {
                        this.stirPosition = s;
                    }
                }
            }
            Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
            if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
                if (this.stirPosition > this.itemAllSort.size() - 1) {
                    this.stirPosition = this.itemAllSort.size() - 1;
                } else if (this.stirPosition < 0) {
                    this.stirPosition = 0;
                }
                setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
            }
        }
    }

    private void addControlDefaultCameraView() {
        if (isControlDefaultCameraCanUse() && this.itemAllSort != null && this.itemAllOnTouchSort != null) {
            this.itemAllSort.add("control_default_camera");
            this.itemAllOnTouchSort.add("control_default_camera");
            this.control_default_camera.setVisibility(0);
            this.controlDefaultCameraDesc.setVisibility(0);
            this.controlDefaultCamDialog = new ControlDefaultCamDialog(this.mContext);
        }
    }

    public void setControlOnClickItemDownSeekBarColor() {
        this.seekbar_roll_axle.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_roll_axle.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_yaw_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_yaw_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_roll_spped.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_roll_spped.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_pitch_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_pitch_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_yaw_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_yaw_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_roll_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_roll_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
    }

    public boolean setControlOnClickIsSwitchButton(View view) {
        this.switchButtonItemValue = false;
        if (view == this.wireless_charging_rl) {
            this.switchButtonItemValue = true;
        }
        return this.switchButtonItemValue;
    }

    public boolean setControlOnClickItemDownIsSeekBar(View view) {
        this.seekBarItemValue = false;
        if (view == this.roll_axle_rl) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_pitch_spped) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_yaw_speed) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_roll_speed) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_pitch_axis) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_yall_axis) {
            this.seekBarItemValue = true;
        } else if (view == this.rl_roll_axis) {
            this.seekBarItemValue = true;
        } else {
            this.seekBarItemValue = false;
        }
        return this.seekBarItemValue;
    }

    public void setControlSeekBarGrayToYellow(View view) {
        if (view == this.roll_axle_rl) {
            this.seekbar_roll_axle.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_axle.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_pitch_spped) {
            this.seekbar_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_yaw_speed) {
            this.seekbar_yaw_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_yaw_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_roll_speed) {
            this.seekbar_roll_spped.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_spped.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_pitch_axis) {
            this.seekbar_pitch_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_pitch_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_yall_axis) {
            this.seekbar_yaw_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_yaw_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_roll_axis) {
            this.seekbar_roll_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        }
    }

    public void setControlOnClickItemDown(View view) {
        if (view == this.roll_axle_rl) {
            this.seekbar_roll_axle.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_axle.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_walk_model) {
            ViseLog.m1466e("rb_walk_mode");
            this.sceneModeChanged = true;
            this.ptzModeChanged = false;
            clearEnableContextual();
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 0) {
                this.rl_custom_isvisible.setVisibility(8);
                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                getViewVisibleTwoItem();
            }
            if (ViseBluetooth.getInstance().isConnected()) {
                setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 1);
                return;
            }
            Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (view == this.rl_sport_model) {
            ViseLog.m1466e("rb_sport_mode");
            this.sceneModeChanged = true;
            this.ptzModeChanged = false;
            clearEnableContextual();
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 0) {
                this.rl_custom_isvisible.setVisibility(8);
                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                getViewVisibleTwoItem();
            }
            if (ViseBluetooth.getInstance().isConnected()) {
                setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 2);
                return;
            }
            Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (view == this.rl_custom_model) {
            ViseLog.m1466e("rb_custom_mode");
            this.sceneModeChanged = true;
            this.ptzModeChanged = false;
            clearEnableContextual();
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 8) {
                this.rl_custom_isvisible.setVisibility(0);
                setStirPositionChangeValue(this.rl_custom_isvisible, true);
                getViewVisibleTwoItem();
            }
            if (ViseBluetooth.getInstance().isConnected()) {
                setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                this.custom_for_speed2_divider.setVisibility(0);
                BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 0);
                return;
            }
            Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (view == this.rl_pitch_spped) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_pitch_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_pitch_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_yaw_speed) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_yaw_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_yaw_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_roll_speed) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_roll_spped.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_spped.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_pitch_axis) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_pitch_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_pitch_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_yall_axis) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_yaw_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_yaw_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_roll_axis) {
            setControlOnClickItemDownSeekBarColor();
            this.seekbar_roll_deadzone.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
            this.seekbar_roll_deadzone.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        } else if (view == this.rl_exposure_model) {
            this.sceneModeChanged = false;
            this.ptzModeChanged = true;
            clearEnableContextual();
            if (ViseBluetooth.getInstance().isConnected()) {
                setEnableTrueOrfalse(this.iv_exposure_select, this.rl_exposure_model);
                BleByteUtil.setPTZParameters((byte) 49, (byte) 1);
                return;
            }
            Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (view == this.rl_roll_model) {
            this.sceneModeChanged = false;
            this.ptzModeChanged = true;
            clearEnableContextual();
            if (ViseBluetooth.getInstance().isConnected()) {
                setEnableTrueOrfalse(this.iv_roll_select, this.rl_roll_model);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.long_press_mode_focus_rl.setVisibility(8);
                }
                this.long_press_mode_focus_divider.setVisibility(0);
                setFocusState(getLongPressModeFocusState());
                BleByteUtil.setPTZParameters((byte) 49, (byte) 0);
                return;
            }
            Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (view == this.long_press_mode_focus_rl) {
            if (this.long_press_mode_focus_switch.isChecked()) {
                this.long_press_mode_focus_switch.setChecked(false);
            } else {
                this.long_press_mode_focus_switch.setChecked(true);
            }
        } else if (view == this.phone_check) {
            if (this.switchButton.isChecked()) {
                this.switchButton.setChecked(false);
            } else {
                this.switchButton.setChecked(true);
            }
        } else if (view == this.calibration) {
            Log.e(TAG, "onClick: 校准被点击了");
            startActivity(new Intent(getActivity(), FVCalibrationActivity.class));
        } else if (view == this.reset_to_default) {
            Log.e(TAG, "onClick: 恢复出厂设置被点击了");
            if (this.connected) {
                resetToDefaultDialog();
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_fn_follow_hand) {
            this.iv_fn_follow_hand.setVisibility(0);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(8);
            BleByteUtil.setPTZParameters((byte) 73, (byte) 0);
            BlePtzParasConstant.GET_PTZ_FN_OPTION = 0;
        } else if (view == this.rl_fn_flash) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(0);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(8);
            BleByteUtil.setPTZParameters((byte) 73, (byte) 1);
            BlePtzParasConstant.GET_PTZ_FN_OPTION = 1;
        } else if (view == this.rl_fn_white_balance) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(0);
            this.iv_fn_wireless_charging.setVisibility(8);
            BleByteUtil.setPTZParameters((byte) 73, (byte) 2);
            BlePtzParasConstant.GET_PTZ_FN_OPTION = 2;
        } else if (view == this.rl_fn_wireless_charging) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(0);
            BleByteUtil.setPTZParameters((byte) 73, (byte) 3);
            BlePtzParasConstant.GET_PTZ_FN_OPTION = 3;
        } else if (view == this.rl_fn_lock_mode) {
            if (isFnLockModeCanUse()) {
                this.iv_fn_follow_hand.setVisibility(8);
                this.iv_fn_lock_mode.setVisibility(0);
                this.iv_fn_flash.setVisibility(8);
                this.iv_fn_white_balance.setVisibility(8);
                this.iv_fn_wireless_charging.setVisibility(8);
                BleByteUtil.setPTZParameters((byte) 73, (byte) 4);
                BlePtzParasConstant.GET_PTZ_FN_OPTION = 4;
                return;
            }
            this.mFnLockModeSupportDialog.show();
            this.mFnLockModeSupportDialog.setMessage(getString(C0853R.string.fn_lock_mode_support_dialog_tip));
        } else if (view == this.wireless_charging_rl) {
            this.switchButton_charging.performClick();
        } else if (view == this.control_default_camera) {
            this.control_default_camera.performClick();
        }
    }

    public void setControlOnClickItemBlackgroundColor(View view) {
        this.roll_axle_rl.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_walk_model.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_sport_model.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_custom_model.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pitch_spped.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_yaw_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_pitch_axis.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_yall_axis.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_axis.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_exposure_model.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_roll_model.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.long_press_mode_focus_rl.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.phone_check.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.calibration.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.reset_to_default.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_fn_follow_hand.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_fn_lock_mode.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_fn_flash.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_fn_white_balance.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_fn_wireless_charging.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.wireless_charging_rl.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.control_default_camera.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        view.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.black15));
        this.mHandler.postDelayed(this.runnable, 200);
    }

    /* access modifiers changed from: private */
    public int getPosMoveToTopDistance(int pos) {
        return (Util.dip2px(this.mContext, 50.0f) * pos) + (Util.dip2px(this.mContext, 15.0f) * pos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_STIR_UP_210 /*107706*/:
                if (CameraUtils.getFrameLayerNumber() == 23) {
                    String value2 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------   波轮拨动向下   波轮拨动向下   波轮拨动向下" + this.stirPosition + "  value2:" + value2);
                    Boolean seekBarItemValue2 = Boolean.valueOf(setControlOnClickItemDownIsSeekBar(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString())));
                    Boolean isSwitchButton = Boolean.valueOf(setControlOnClickIsSwitchButton(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString())));
                    if (seekBarItemValue2.booleanValue()) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   波轮拨动向下   波轮拨动向下  :" + this.stirPosition + "    查询是seekBar   查询是seekBar  ");
                        setControlSeekBarValue(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()), true, Integer.parseInt(value2));
                        setControlOnClickItemDown(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
                        return;
                    } else if (isSwitchButton.booleanValue() && controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()) == this.wireless_charging_rl) {
                        this.switchButton_charging.performClick();
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_SETTING_STIR_DOWN_210 /*107707*/:
                if (CameraUtils.getFrameLayerNumber() == 23) {
                    String value22 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向上   波轮拨动向上   波轮拨动向上" + this.stirPosition + "  value2:" + value22);
                    Boolean seekBarItemValue3 = Boolean.valueOf(setControlOnClickItemDownIsSeekBar(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString())));
                    Boolean isSwitchButton2 = Boolean.valueOf(setControlOnClickIsSwitchButton(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString())));
                    if (seekBarItemValue3.booleanValue()) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向上   波轮拨动向上   波轮拨动向上  :" + this.stirPosition + "    查询是seekBar   查询是seekBar  ");
                        setControlSeekBarValue(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()), false, Integer.parseInt(value22));
                        setControlSeekBarGrayToYellow(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
                        return;
                    } else if (isSwitchButton2.booleanValue() && controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()) == this.wireless_charging_rl) {
                        this.switchButton_charging.performClick();
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 23 && !Boolean.valueOf(setControlOnClickItemDownIsSeekBar(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()))).booleanValue()) {
                    setControlOnClickItemDown(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
                    return;
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 23) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  云台操作返回键 ");
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 23) {
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 摇杆拨动向上   向上   向上 " + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
                    setControlOnClickItemDownSeekBarColor();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 23) {
                    this.stirPosition++;
                    if (this.stirPosition > this.itemAllSort.size() - 1) {
                        this.stirPosition = this.itemAllSort.size() - 1;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  摇杆拨动向下   向下   向下" + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemAllSort.get(this.stirPosition).toString()));
                    setControlOnClickItemDownSeekBarColor();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setControlSeekBarValue(View view, boolean boo, int vaValue) {
        int progress;
        int progress2;
        int progress3;
        int progress4;
        int progress5;
        int progress6;
        int progress7;
        this.variValue = vaValue;
        if (view == this.roll_axle_rl) {
            int progress8 = this.seekbar_roll_axle.getProgress();
            if (!boo) {
                progress7 = progress8 - this.variValue;
                if (progress7 < 0) {
                    progress7 = 0;
                }
            } else {
                progress7 = progress8 + this.variValue;
                if (progress7 > 100) {
                    progress7 = 100;
                }
            }
            this.roll_axle_num_tv.setText(calculatePrgs(progress7, false) + "");
            this.seekbar_roll_axle.setProgress(progress7);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 11, (byte) 0, (byte) calculatePrgs(this.seekbar_roll_axle.getProgress(), false), (byte) 11);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_pitch_spped) {
            int progress9 = this.seekbar_pitch_speed.getProgress();
            if (!boo) {
                progress6 = progress9 - this.variValue;
                if (progress6 < 0) {
                    progress6 = 0;
                }
            } else {
                progress6 = progress9 + this.variValue;
                if (progress6 > 100) {
                    progress6 = 100;
                }
            }
            this.tv_pitch_pg_speed.setText(progress6 + "");
            this.seekbar_pitch_speed.setProgress(progress6);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 9, (byte) progress6, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_yaw_speed) {
            int progress10 = this.seekbar_yaw_speed.getProgress();
            if (!boo) {
                progress5 = progress10 - this.variValue;
                if (progress5 < 0) {
                    progress5 = 0;
                }
            } else {
                progress5 = progress10 + this.variValue;
                if (progress5 > 100) {
                    progress5 = 100;
                }
            }
            this.tv_yaw_pg_speed.setText(progress5 + "");
            this.seekbar_yaw_speed.setProgress(progress5);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress5);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_roll_speed) {
            int progress11 = this.seekbar_roll_spped.getProgress();
            if (!boo) {
                progress4 = progress11 - this.variValue;
                if (progress4 < 0) {
                    progress4 = 0;
                }
            } else {
                progress4 = progress11 + this.variValue;
                if (progress4 > 100) {
                    progress4 = 100;
                }
            }
            this.tv_roll_pg_speed.setText(progress4 + "");
            this.seekbar_roll_spped.setProgress(progress4);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress4, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_pitch_axis) {
            int progress12 = this.seekbar_pitch_deadzone.getProgress();
            if (!boo) {
                progress3 = progress12 - this.variValue;
                if (progress3 < 0) {
                    progress3 = 0;
                }
            } else {
                progress3 = progress12 + this.variValue;
                if (progress3 > 100) {
                    progress3 = 100;
                }
            }
            this.tv_pitch_pg_deadzone.setText(progress3 + "");
            this.seekbar_pitch_deadzone.setProgress(progress3);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 10, (byte) progress3, (byte) BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND, (byte) BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_yall_axis) {
            int progress13 = this.seekbar_yaw_deadzone.getProgress();
            if (!boo) {
                progress2 = progress13 - this.variValue;
                if (progress2 < 0) {
                    progress2 = 0;
                }
            } else {
                progress2 = progress13 + this.variValue;
                if (progress2 > 100) {
                    progress2 = 100;
                }
            }
            this.tv_yaw_pg_deadzone.setText(progress2 + "");
            this.seekbar_yaw_deadzone.setProgress(progress2);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 10, (byte) BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND, (byte) BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND, (byte) progress2);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        } else if (view == this.rl_roll_axis) {
            int progress14 = this.seekbar_roll_deadzone.getProgress();
            if (!boo) {
                progress = progress14 - this.variValue;
                if (progress < 0) {
                    progress = 0;
                }
            } else {
                progress = progress14 + this.variValue;
                if (progress > 100) {
                    progress = 100;
                }
            }
            this.tv_roll_pg_deadzone.setText(progress + "");
            this.seekbar_roll_deadzone.setProgress(progress);
            if (this.connected) {
                BleByteUtil.setPTZParameters((byte) 10, (byte) BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND);
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
            }
        }
    }

    public View controlItemStringToView(String str) {
        if (str.equals("roll_axle_rl")) {
            return this.roll_axle_rl;
        }
        if (str.equals("rl_walk_model")) {
            return this.rl_walk_model;
        }
        if (str.equals("rl_sport_model")) {
            return this.rl_sport_model;
        }
        if (str.equals("rl_custom_model")) {
            return this.rl_custom_model;
        }
        if (str.equals("rl_pitch_spped")) {
            return this.rl_pitch_spped;
        }
        if (str.equals("rl_yaw_speed")) {
            return this.rl_yaw_speed;
        }
        if (str.equals("rl_roll_speed")) {
            return this.rl_roll_speed;
        }
        if (str.equals("rl_pitch_axis")) {
            return this.rl_pitch_axis;
        }
        if (str.equals("rl_yall_axis")) {
            return this.rl_yall_axis;
        }
        if (str.equals("rl_roll_axis")) {
            return this.rl_roll_axis;
        }
        if (str.equals("phone_check")) {
            return this.phone_check;
        }
        if (str.equals("calibration")) {
            return this.calibration;
        }
        if (str.equals("rl_fn_follow_hand")) {
            return this.rl_fn_follow_hand;
        }
        if (str.equals("rl_fn_lock_mode")) {
            return this.rl_fn_lock_mode;
        }
        if (str.equals("rl_fn_flash")) {
            return this.rl_fn_flash;
        }
        if (str.equals("rl_fn_white_balance")) {
            return this.rl_fn_white_balance;
        }
        if (str.equals("rl_fn_wireless_charging")) {
            return this.rl_fn_wireless_charging;
        }
        if (str.equals("wireless_charging_rl")) {
            return this.wireless_charging_rl;
        }
        if (str.equals("control_default_camera")) {
            return this.control_default_camera;
        }
        return this.reset_to_default;
    }

    private void initData() {
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            if (this.ll_no_connect_ptz != null) {
                this.ll_no_connect_ptz.setVisibility(8);
            }
            if (this.ll_connect_ptz != null) {
                this.ll_connect_ptz.setVisibility(0);
                return;
            }
            return;
        }
        if (this.ll_no_connect_ptz != null) {
            this.ll_no_connect_ptz.setVisibility(0);
        }
        if (this.ll_connect_ptz != null) {
            this.ll_connect_ptz.setVisibility(8);
        }
    }

    private void initView() {
        String currentPtzType = "";
        if (CameraUtils.getCurrentPageIndex() == 0) {
            currentPtzType = "";
        } else if (CameraUtils.getCurrentPageIndex() == 1) {
            currentPtzType = BleConstant.FM_300;
        } else if (CameraUtils.getCurrentPageIndex() == 2) {
            currentPtzType = BleConstant.FM_210;
        }
        if (currentPtzType.equals(BleConstant.FM_300) || currentPtzType.equals(BleConstant.FM_210)) {
            this.long_click_mode_and_roll_mode_tv.setVisibility(8);
            this.rl_roll_model.setVisibility(8);
            this.rl_exposure_model.setVisibility(8);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (isPtzCanChargePhone() || currentPtzType.equals(BleConstant.FM_210)) {
                this.wireless_charging_rl.setVisibility(0);
                this.wireless_charging_tip.setVisibility(0);
                changeText();
            }
            this.wirelessChargeDialog = new WirelessChargeDialog(this.mContext);
        }
        if (currentPtzType.equals(BleConstant.FM_210)) {
            this.fn_title_tv.setVisibility(0);
            this.fv_setting_rl.setVisibility(0);
        }
        if (currentPtzType.equals(BleConstant.FM_300) || currentPtzType.equals(BleConstant.FM_210)) {
            this.phone_check.setVisibility(8);
        }
        this.seekbar_roll_axle.setOnSeekBarChangeListener(this.mOnRollSeekBarChangeListener);
        this.layout_advance_follow_speed_what.setOnClickListener(this);
        this.layout_advance_dead_zone_what.setOnClickListener(this);
        this.rl_walk_model.setOnClickListener(this);
        this.rl_sport_model.setOnClickListener(this);
        this.rl_custom_model.setOnClickListener(this);
        this.rl_roll_model.setOnClickListener(this);
        this.rl_exposure_model.setOnClickListener(this);
        this.calibration.setOnClickListener(this);
        this.reset_to_default.setOnClickListener(this);
        this.rl_fn_follow_hand.setOnClickListener(this);
        this.rl_fn_lock_mode.setOnClickListener(this);
        this.rl_fn_flash.setOnClickListener(this);
        this.rl_fn_white_balance.setOnClickListener(this);
        this.rl_fn_wireless_charging.setOnClickListener(this);
        this.wireless_charging_rl.setOnClickListener(this);
        this.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onClick: 手机检测被点击了");
                if (isChecked) {
                    if (FVAdvancedSetPTZFragment.this.connected) {
                        Log.e(FVAdvancedSetPTZFragment.TAG, "onCheckedChanged: aaa");
                        BleByteUtil.setPTZParameters((byte) 22, (byte) 1);
                        return;
                    }
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                } else if (FVAdvancedSetPTZFragment.this.connected) {
                    Log.e(FVAdvancedSetPTZFragment.TAG, "onCheckedChanged: bbb");
                    BleByteUtil.setPTZParameters((byte) 22, (byte) 0);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.long_press_mode_focus_switch.setChecked(getLongPressModeFocusState());
        this.long_press_mode_focus_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FVAdvancedSetPTZFragment.this.setLongPressModeFocusState();
                FVAdvancedSetPTZFragment.this.setFocusState(isChecked);
            }
        });
        this.seekbar_pitch_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_pitch_speed俯仰轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_pitch_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 俯仰轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_pitch_pg_speed.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_yaw_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_yaw_speed俯仰轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_yaw_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 俯仰轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_yaw_pg_speed.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_roll_spped.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_roll_spped俯仰轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_roll_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 俯仰轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_roll_pg_speed.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_pitch_deadzone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_pitch_deadzone俯仰轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_pitch_pg_deadzone.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 俯仰轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_pitch_pg_deadzone.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 10, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND, (byte) BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_yaw_deadzone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_yaw_deadzone水平轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_yaw_pg_deadzone.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 水平轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_yaw_pg_deadzone.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 10, (byte) BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND, (byte) BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND, (byte) progress);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_roll_deadzone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: seekbar_yaw_deadzone水平轴被点击了" + i);
                FVAdvancedSetPTZFragment.this.tv_roll_pg_deadzone.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(FVAdvancedSetPTZFragment.TAG, "onProgressChanged: 水平轴停止点击了" + seekBar.getProgress());
                int progress = seekBar.getProgress();
                FVAdvancedSetPTZFragment.this.tv_roll_pg_deadzone.setText(progress + "");
                if (FVAdvancedSetPTZFragment.this.connected) {
                    BleByteUtil.setPTZParameters((byte) 10, (byte) BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND);
                } else {
                    Toast.makeText(FVAdvancedSetPTZFragment.this.getActivity(), FVAdvancedSetPTZFragment.this.getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        addControlDefaultCameraView();
        this.switchButton_charging.setBackColorRes(C0853R.color.switch_select_bg_color_black4);
        this.switchButton_charging.setThumbColorRes(C0853R.color.color_white);
        this.switchButton_charging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FVAdvancedSetPTZFragment.this.changeChargingStatus();
            }
        });
    }

    private boolean isFnLockModeCanUse() {
        String gmuFirmwareVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
        return ViseBluetooth.getInstance().isConnected() && gmuFirmwareVersion != null && CameraUtils.getCurrentPageIndex() == 2 && gmuFirmwareVersion.compareTo("01.00.00.53") >= 0;
    }

    private boolean isControlDefaultCameraCanUse() {
        String gmuFirmwareVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
        if (!ViseBluetooth.getInstance().isConnected() || gmuFirmwareVersion == null) {
            return false;
        }
        if (CameraUtils.getCurrentPageIndex() == 0) {
            return gmuFirmwareVersion.compareTo("01.00.00.50") >= 0;
        }
        if (CameraUtils.getCurrentPageIndex() == 1) {
            return gmuFirmwareVersion.compareTo("01.00.02.40") >= 0;
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            return gmuFirmwareVersion.compareTo("01.00.00.40") >= 0;
        }
        return false;
    }

    private void changeText() {
        if (Util.isSupportWirelessCharge(this.mContext)) {
            this.wireless_charging_tip.setText(getString(C0853R.string.wireless_charging_tip));
        } else {
            this.wireless_charging_tip.setText(getString(C0853R.string.the_phone_is_not_support_wireless_charging));
        }
    }

    private boolean isPtzCanChargePhone() {
        String sn = BlePtzParasConstant.GET_PTZ_SN_CODE;
        ViseLog.m1466e("Charge--sn--" + sn);
        if (sn == null) {
            return false;
        }
        if (sn.substring(3, 4).equals("C") || sn.substring(3, 4).equals("D")) {
            return true;
        }
        return false;
    }

    public void onResume() {
        super.onResume();
        this.long_press_mode_focus_switch.setChecked(getLongPressModeFocusState());
        this.switchButton_charging.setChecked(getWirelessChargingState());
        if (this.wirelessChargeDialog != null) {
            this.wirelessChargeDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public boolean getWirelessChargingState() {
        return BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH == 1;
    }

    /* access modifiers changed from: private */
    public void setLongPressModeFocusState() {
        SPUtils.put(getActivity(), SharePrefConstant.ADVANCED_SET_PTZ_LONG_PRESS_MODE_FOCUS_SWITCH, Boolean.valueOf(this.long_press_mode_focus_switch.isChecked()));
    }

    /* access modifiers changed from: private */
    public void setFocusState(boolean canFocus) {
        SPUtils.put(getActivity(), SharePrefConstant.ADVANCED_SET_PTZ_LONG_PRESS_FOCUS, Boolean.valueOf(canFocus));
    }

    private boolean getLongPressModeFocusState() {
        return ((Boolean) SPUtils.get(getActivity(), SharePrefConstant.ADVANCED_SET_PTZ_LONG_PRESS_MODE_FOCUS_SWITCH, false)).booleanValue();
    }

    private void setEnableTrueOrfalse(ImageView image, RelativeLayout rllayout) {
        image.setVisibility(0);
        rllayout.setEnabled(false);
    }

    private void clearEnableContextual() {
        ViseLog.m1466e("clearEnableContextual sceneModeChanged: " + this.sceneModeChanged);
        ViseLog.m1466e("clearEnableContextual ptzModeChanged: " + this.ptzModeChanged);
        if (this.sceneModeChanged) {
            this.iv_walk_select.setVisibility(8);
            this.iv_sport_select.setVisibility(8);
            this.iv_custom_select.setVisibility(8);
            this.custom_for_speed2_divider.setVisibility(8);
        } else if (this.ptzModeChanged) {
            this.iv_roll_select.setVisibility(8);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            this.long_press_mode_focus_divider.setVisibility(8);
            setFocusState(true);
            this.iv_exposure_select.setVisibility(8);
        } else if (!this.ptzModeChanged && !this.sceneModeChanged) {
            this.iv_walk_select.setVisibility(8);
            this.iv_sport_select.setVisibility(8);
            this.iv_custom_select.setVisibility(8);
            this.custom_for_speed2_divider.setVisibility(8);
            this.iv_roll_select.setVisibility(8);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            this.long_press_mode_focus_divider.setVisibility(8);
            setFocusState(true);
            this.iv_exposure_select.setVisibility(8);
        }
        this.rl_walk_model.setEnabled(true);
        this.rl_sport_model.setEnabled(true);
        this.rl_custom_model.setEnabled(true);
        this.rl_roll_model.setEnabled(true);
        this.rl_exposure_model.setEnabled(true);
        this.rl_fn_follow_hand.setEnabled(true);
        this.rl_fn_lock_mode.setEnabled(true);
        this.rl_fn_flash.setEnabled(true);
        this.rl_fn_white_balance.setEnabled(true);
        this.rl_fn_wireless_charging.setEnabled(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0853R.C0855id.rl_walk_model:
                ViseLog.m1466e("rb_walk_mode");
                this.sceneModeChanged = true;
                this.ptzModeChanged = false;
                clearEnableContextual();
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.long_press_mode_focus_rl.setVisibility(8);
                }
                if (this.rl_custom_isvisible.getVisibility() == 0) {
                    this.rl_custom_isvisible.setVisibility(8);
                    setStirPositionChangeValue(this.rl_custom_isvisible, false);
                    getViewVisibleTwoItem();
                }
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 1);
                    return;
                }
                Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_sport_model:
                ViseLog.m1466e("rb_sport_mode");
                this.sceneModeChanged = true;
                this.ptzModeChanged = false;
                clearEnableContextual();
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.long_press_mode_focus_rl.setVisibility(8);
                }
                if (this.rl_custom_isvisible.getVisibility() == 0) {
                    this.rl_custom_isvisible.setVisibility(8);
                    setStirPositionChangeValue(this.rl_custom_isvisible, false);
                    getViewVisibleTwoItem();
                }
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 2);
                    return;
                }
                Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_custom_model:
                ViseLog.m1466e("rb_custom_mode");
                this.sceneModeChanged = true;
                this.ptzModeChanged = false;
                clearEnableContextual();
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.long_press_mode_focus_rl.setVisibility(8);
                }
                if (this.rl_custom_isvisible.getVisibility() == 8) {
                    this.rl_custom_isvisible.setVisibility(0);
                    setStirPositionChangeValue(this.rl_custom_isvisible, true);
                    getViewVisibleTwoItem();
                }
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                    this.custom_for_speed2_divider.setVisibility(0);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 0);
                    return;
                }
                Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.layout_advance_follow_speed_what:
                startActivity(FVWebActivity.creatIntent(getActivity(), BleConstant.ISO));
                return;
            case C0853R.C0855id.layout_advance_dead_zone_what:
                startActivity(FVWebActivity.creatIntent(getActivity(), BleConstant.SHUTTER));
                return;
            case C0853R.C0855id.rl_exposure_model:
                this.sceneModeChanged = false;
                this.ptzModeChanged = true;
                clearEnableContextual();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_exposure_select, this.rl_exposure_model);
                    BleByteUtil.setPTZParameters((byte) 49, (byte) 1);
                    return;
                }
                Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_roll_model:
                this.sceneModeChanged = false;
                this.ptzModeChanged = true;
                clearEnableContextual();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_roll_select, this.rl_roll_model);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        this.long_press_mode_focus_rl.setVisibility(8);
                    }
                    this.long_press_mode_focus_divider.setVisibility(0);
                    setFocusState(getLongPressModeFocusState());
                    BleByteUtil.setPTZParameters((byte) 49, (byte) 0);
                    return;
                }
                Toast.makeText(getActivity(), getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.calibration:
                Log.e(TAG, "onClick: 校准被点击了");
                startActivity(new Intent(getActivity(), FVCalibrationActivity.class));
                return;
            case C0853R.C0855id.reset_to_default:
                Log.e(TAG, "onClick: 恢复出厂设置被点击了");
                if (this.connected) {
                    resetToDefaultDialog();
                    return;
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    return;
                }
            case C0853R.C0855id.rl_fn_follow_hand:
                this.iv_fn_follow_hand.setVisibility(0);
                this.iv_fn_lock_mode.setVisibility(8);
                this.iv_fn_flash.setVisibility(8);
                this.iv_fn_white_balance.setVisibility(8);
                this.iv_fn_wireless_charging.setVisibility(8);
                BleByteUtil.setPTZParameters((byte) 73, (byte) 0);
                BlePtzParasConstant.GET_PTZ_FN_OPTION = 0;
                return;
            case C0853R.C0855id.rl_fn_lock_mode:
                if (isFnLockModeCanUse()) {
                    this.iv_fn_follow_hand.setVisibility(8);
                    this.iv_fn_lock_mode.setVisibility(0);
                    this.iv_fn_flash.setVisibility(8);
                    this.iv_fn_white_balance.setVisibility(8);
                    this.iv_fn_wireless_charging.setVisibility(8);
                    BleByteUtil.setPTZParameters((byte) 73, (byte) 4);
                    BlePtzParasConstant.GET_PTZ_FN_OPTION = 4;
                    return;
                }
                this.mFnLockModeSupportDialog.show();
                this.mFnLockModeSupportDialog.setMessage(getString(C0853R.string.fn_lock_mode_support_dialog_tip));
                return;
            case C0853R.C0855id.rl_fn_flash:
                this.iv_fn_follow_hand.setVisibility(8);
                this.iv_fn_lock_mode.setVisibility(8);
                this.iv_fn_flash.setVisibility(0);
                this.iv_fn_white_balance.setVisibility(8);
                this.iv_fn_wireless_charging.setVisibility(8);
                BleByteUtil.setPTZParameters((byte) 73, (byte) 1);
                BlePtzParasConstant.GET_PTZ_FN_OPTION = 1;
                return;
            case C0853R.C0855id.rl_fn_white_balance:
                this.iv_fn_follow_hand.setVisibility(8);
                this.iv_fn_lock_mode.setVisibility(8);
                this.iv_fn_flash.setVisibility(8);
                this.iv_fn_white_balance.setVisibility(0);
                this.iv_fn_wireless_charging.setVisibility(8);
                BleByteUtil.setPTZParameters((byte) 73, (byte) 2);
                BlePtzParasConstant.GET_PTZ_FN_OPTION = 2;
                return;
            case C0853R.C0855id.rl_fn_wireless_charging:
                this.iv_fn_follow_hand.setVisibility(8);
                this.iv_fn_lock_mode.setVisibility(8);
                this.iv_fn_flash.setVisibility(8);
                this.iv_fn_white_balance.setVisibility(8);
                this.iv_fn_wireless_charging.setVisibility(0);
                BleByteUtil.setPTZParameters((byte) 73, (byte) 3);
                BlePtzParasConstant.GET_PTZ_FN_OPTION = 3;
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void changeChargingStatus() {
        if (this.switchButton_charging.isChecked()) {
            BleByteUtil.setPTZParameters((byte) 24, (byte) 1);
            if (!Util.isSupportWirelessCharge(this.mContext)) {
                showWirelessChargeDialog();
                return;
            }
            return;
        }
        BleByteUtil.setPTZParameters((byte) 24, (byte) 0);
        this.wirelessChargeDialog.dismiss();
    }

    private void showWirelessChargeDialog() {
        if (!((Boolean) SPUtils.get(getActivity(), SharePrefConstant.WIRELESS_CHARGE_DIALOG_DISPLAY_AGAIN, false)).booleanValue() && this.wirelessChargeDialog != null && !this.wirelessChargeDialog.isShowing()) {
            this.wirelessChargeDialog.show();
        }
    }

    private void resetToDefaultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage((int) C0853R.string.reset_to_default);
        builder.setNegativeButton((int) C0853R.string.label_cancel, (DialogInterface.OnClickListener) null);
        builder.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BleByteUtil.setPTZParameters((byte) 4, (byte) 1);
            }
        });
        builder.show();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (FVAdvancedSetPTZFragment.this.switchButton_charging != null) {
                    FVAdvancedSetPTZFragment.this.switchButton_charging.setChecked(FVAdvancedSetPTZFragment.this.getWirelessChargingState());
                }
            }
        }, 3000);
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
                initData();
                return;
            case 54:
                this.switchButton_charging.setChecked(true);
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH == 0) {
                            FVAdvancedSetPTZFragment.this.switchButton_charging.setChecked(false);
                        }
                    }
                }, 800);
                return;
            case 55:
                this.switchButton_charging.setChecked(false);
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                ViseLog.m1466e("receive data :FVAdvancedSetPTZFragment:command" + value[0] + "subcommand" + value[1]);
                if ((value[0] & 255) == 165) {
                    byte settingnotifydata = BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value);
                    if (settingnotifydata != 9 && settingnotifydata != 10) {
                        if (settingnotifydata == 22) {
                            if ((value[2] & 255) == 1) {
                            }
                            return;
                        } else if ((value[1] & 255) == 24) {
                            BleByteUtil.ackPTZPanorama((byte) 24, (byte) (value[2] & 255));
                            if ((value[2] & 255) == 0) {
                                this.switchButton_charging.setChecked(false);
                                return;
                            }
                            return;
                        } else if (settingnotifydata == 25) {
                            if ((value[2] & 255) == 0 || (value[2] & 255) == 1 || (value[2] & 255) != 2) {
                            }
                            return;
                        } else if (settingnotifydata != 4 && settingnotifydata == 49) {
                            clearEnableContextual();
                            if ((value[2] & 255) == 0) {
                                setEnableTrueOrfalse(this.iv_roll_select, this.rl_roll_model);
                                if (CameraUtils.getCurrentPageIndex() == 2) {
                                    this.long_press_mode_focus_rl.setVisibility(8);
                                }
                                this.long_press_mode_focus_divider.setVisibility(0);
                                setFocusState(getLongPressModeFocusState());
                                return;
                            } else if ((value[2] & 255) == 1) {
                                setEnableTrueOrfalse(this.iv_exposure_select, this.rl_exposure_model);
                                return;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if ((value[0] & 255) != 90) {
                    return;
                } else {
                    if (BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value) == 25) {
                        clearEnableContextual();
                        if ((value[2] & 255) == 0) {
                            setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                this.long_press_mode_focus_rl.setVisibility(8);
                            }
                            if (this.rl_custom_isvisible.getVisibility() == 8) {
                                this.rl_custom_isvisible.setVisibility(0);
                                setStirPositionChangeValue(this.rl_custom_isvisible, true);
                                getViewVisibleTwoItem();
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 1) {
                            setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                this.long_press_mode_focus_rl.setVisibility(8);
                            }
                            if (this.rl_custom_isvisible.getVisibility() == 0) {
                                this.rl_custom_isvisible.setVisibility(8);
                                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                                getViewVisibleTwoItem();
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 2) {
                            setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                this.long_press_mode_focus_rl.setVisibility(8);
                            }
                            if (this.rl_custom_isvisible.getVisibility() == 0) {
                                this.rl_custom_isvisible.setVisibility(8);
                                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                                getViewVisibleTwoItem();
                                return;
                            }
                            return;
                        } else {
                            return;
                        }
                    } else if ((value[1] & 255) == 24) {
                        BleByteUtil.ackPTZPanorama((byte) 24, (byte) (value[2] & 255));
                        if ((value[2] & 255) == 1) {
                            this.switchButton_charging.setChecked(true);
                            return;
                        } else {
                            this.switchButton_charging.setChecked(false);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            case 136:
                setAllParas();
                return;
            default:
                return;
        }
    }

    private void setAllParas() {
        this.sceneModeChanged = false;
        this.ptzModeChanged = false;
        clearEnableContextual();
        if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 0) {
            setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 8) {
                this.rl_custom_isvisible.setVisibility(0);
                setStirPositionChangeValue(this.rl_custom_isvisible, true);
                getViewVisibleTwoItem();
            }
        } else if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 1) {
            setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 0) {
                this.rl_custom_isvisible.setVisibility(8);
                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                getViewVisibleTwoItem();
            }
        } else if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 2) {
            setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                this.long_press_mode_focus_rl.setVisibility(8);
            }
            if (this.rl_custom_isvisible.getVisibility() == 0) {
                this.rl_custom_isvisible.setVisibility(8);
                setStirPositionChangeValue(this.rl_custom_isvisible, false);
                getViewVisibleTwoItem();
            }
        }
        if (BlePtzParasConstant.SET_ROLL_OR_EXPOSURE_MODEL == 0) {
            setEnableTrueOrfalse(this.iv_roll_select, this.rl_roll_model);
            String currentPtzType = (String) SPUtils.get(getActivity(), SharePrefConstant.CURRENT_PTZ_TYPE, "");
            if (currentPtzType.equals("") || currentPtzType.equals(BleConstant.FM_210)) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.long_press_mode_focus_rl.setVisibility(8);
                }
                this.long_press_mode_focus_divider.setVisibility(0);
                setFocusState(getLongPressModeFocusState());
            }
        } else if (BlePtzParasConstant.SET_ROLL_OR_EXPOSURE_MODEL == 1) {
            setEnableTrueOrfalse(this.iv_exposure_select, this.rl_exposure_model);
        }
        ViseLog.m1466e("手机检车开关数值" + BlePtzParasConstant.SET_PHONE_CHECK_SWICTH);
        if (BlePtzParasConstant.SET_PHONE_CHECK_SWICTH == 1) {
            this.switchButton.setChecked(true);
        } else {
            this.switchButton.setChecked(false);
        }
        this.seekbar_pitch_speed.setProgress(BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR);
        this.seekbar_yaw_speed.setProgress(BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
        this.seekbar_roll_spped.setProgress(BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR);
        this.tv_pitch_pg_speed.setText(BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR + "");
        this.tv_yaw_pg_speed.setText(BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR + "");
        this.tv_roll_pg_speed.setText(BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR + "");
        this.seekbar_pitch_deadzone.setProgress(BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND);
        this.seekbar_yaw_deadzone.setProgress(BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND);
        this.seekbar_roll_deadzone.setProgress(BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND);
        this.tv_pitch_pg_deadzone.setText(BlePtzParasConstant.SET_PITCH_OF_PTZ_FOLLOW_DEADBAND + "");
        this.tv_yaw_pg_deadzone.setText(BlePtzParasConstant.SET_YAW_OF_PTZ_FOLLOW_DEADBAND + "");
        this.tv_roll_pg_deadzone.setText(BlePtzParasConstant.SET_ROLL_OF_PTZ_FOLLOW_DEADBAND + "");
        int recProgress = BlePtzParasConstant.SET_ROLL_OF_PTZ_ATTITUDE_ANGLE_FOR_FINE_TUNING;
        this.roll_axle_num_tv.setText(recProgress + "");
        this.seekbar_roll_axle.setProgress(calculatePrgs(recProgress, true));
        if (BlePtzParasConstant.GET_PTZ_FN_OPTION == 0) {
            this.iv_fn_follow_hand.setVisibility(0);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(8);
        } else if (BlePtzParasConstant.GET_PTZ_FN_OPTION == 1) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(0);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(8);
        } else if (BlePtzParasConstant.GET_PTZ_FN_OPTION == 2) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(0);
            this.iv_fn_wireless_charging.setVisibility(8);
        } else if (BlePtzParasConstant.GET_PTZ_FN_OPTION == 3) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(8);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(0);
        } else if (BlePtzParasConstant.GET_PTZ_FN_OPTION == 4) {
            this.iv_fn_follow_hand.setVisibility(8);
            this.iv_fn_lock_mode.setVisibility(0);
            this.iv_fn_flash.setVisibility(8);
            this.iv_fn_white_balance.setVisibility(8);
            this.iv_fn_wireless_charging.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public int calculatePrgs(int progress, boolean isReceive) {
        if (isReceive) {
            if (progress == -100) {
                return 0;
            }
            if (progress > -100 && progress < 0) {
                return (progress / 2) + 50;
            }
            if (progress == 0) {
                return 50;
            }
            if (progress > 0) {
                return (progress / 2) + 50;
            }
            return 0;
        } else if (progress < 50) {
            if (progress > 0 && progress < 50) {
                return (progress - 50) * 2;
            }
            if (progress == 0) {
                return -100;
            }
            return 0;
        } else if (progress != 50 && progress > 50) {
            return (progress - 50) * 2;
        } else {
            return 0;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: ptz fragment onDestroyView");
        ButterKnife.unbind(this);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroyView: ptz fragment onDestroy");
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
    }

    @OnClick({2131756029})
    public void onViewClicked() {
        if (getConnectedDevice() == null) {
            this.controlDefaultCamDialog.show();
        } else {
            startActivity(new Intent("android.media.action.STILL_IMAGE_CAMERA"));
        }
    }

    private BluetoothDevice getConnectedDevice() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method method = BluetoothAdapter.class.getDeclaredMethod("getConnectionState", (Class[]) null);
            method.setAccessible(true);
            if (((Integer) method.invoke(adapter, (Object[]) null)).intValue() == 2) {
                for (BluetoothDevice device : adapter.getBondedDevices()) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    isConnectedMethod.setAccessible(true);
                    if (((Boolean) isConnectedMethod.invoke(device, (Object[]) null)).booleanValue()) {
                        String ptzType = device.getName().substring(6, 7);
                        if (ptzType.equals("S") && CameraUtils.getCurrentPageIndex() == 1) {
                            return device;
                        }
                        if (ptzType.equals("X") && CameraUtils.getCurrentPageIndex() == 2) {
                            return device;
                        }
                        if (ptzType.equals("M")) {
                            if (device.getName().substring(6, 11).equals(BleConstant.FM_210_INSTRUCTIONS_NAME)) {
                                return device;
                            }
                        } else if (CameraUtils.getCurrentPageIndex() == 0) {
                            return device;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
