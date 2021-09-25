package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
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
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.widget.SelfDialogKnowTwo;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.vise.log.ViseLog;
import java.util.Arrays;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVPTZSettingPop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    /* access modifiers changed from: private */
    public ImageView btn_back;
    private FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    private int height;
    private LinearLayout imagin_action_linear;
    /* access modifiers changed from: private */
    public SwitchButton imagin_action_switchButton;
    private ImageView iv_all_follow_select;
    private ImageView iv_custom_select;
    private ImageView iv_fpv_follow_select;
    private ImageView iv_half_follow_select;
    private ImageView iv_no_follow_select;
    private ImageView iv_sport_select;
    private ImageView iv_walk_select;
    private ImageView iv_wheel_heading_axis_select;
    private ImageView iv_wheel_heel_focus_select;
    private ImageView iv_wheel_pitch_axis_select;
    private ImageView iv_wheel_roll_axis_select;
    private ImageView iv_wheel_zoom_select;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public ScrollView ll_contextual_model;
    /* access modifiers changed from: private */
    public LinearLayout ll_ptz;
    private LinearLayout manual_model_linear;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVPTZSettingPop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVPTZSettingPop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVPTZSettingPop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVPTZSettingPop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private RelativeLayout rl_all_follow_mode;
    private RelativeLayout rl_contextual_mode;
    /* access modifiers changed from: private */
    public RelativeLayout rl_contextual_model;
    private RelativeLayout rl_custom_isvisible;
    private RelativeLayout rl_custom_model;
    private RelativeLayout rl_focusing_wheel_out_item;
    private View rl_focusing_wheel_view;
    private RelativeLayout rl_follow_mode;
    private RelativeLayout rl_fpv_follow_mode;
    private RelativeLayout rl_half_follow_mode;
    private RelativeLayout rl_label_focusing_wheel_mode;
    private RelativeLayout rl_no_follow_mode;
    private RelativeLayout rl_pitch_axis_lock;
    private RelativeLayout rl_pitch_spped;
    private RelativeLayout rl_roll_speed;
    private RelativeLayout rl_sport_model;
    private RelativeLayout rl_walk_model;
    private RelativeLayout rl_wheel_heading_axis;
    private RelativeLayout rl_wheel_heel_focus;
    private RelativeLayout rl_wheel_pitch_axis;
    private RelativeLayout rl_wheel_roll_axis;
    private RelativeLayout rl_wheel_zoom;
    private RelativeLayout rl_yaw_speed;
    private boolean scaleSlide = false;
    private ScrollView scrollView;
    private SeekBar seekbar_pitch_speed;
    private SeekBar seekbar_roll_speed;
    private SeekBar seekbar_yaw_speed;
    /* access modifiers changed from: private */
    public SelfDialogKnowTwo selfDialogKnowTwo;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    private SwitchButton swCharging;
    private SwitchButton switchButton;
    /* access modifiers changed from: private */
    public TextView title;
    /* access modifiers changed from: private */
    public TextView tv_pitch_pg_speed;
    /* access modifiers changed from: private */
    public TextView tv_roll_pg_speed;
    /* access modifiers changed from: private */
    public TextView tv_yaw_pg_speed;
    private View view;
    private int whoIsSelect = 0;

    public void init(Context context2, View parent) {
        this.context = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.parentView = parent;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_main_setting_pop_new, (ViewGroup) null);
        this.scrollView = (ScrollView) this.view.findViewById(C0853R.C0855id.scrollView);
        initPop(this.view);
        EventBusUtil.register(this);
        this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 30.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        this.broad = new OrientationBroadPopup();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenOrientationUtil.BC_OrientationChanged);
        context2.registerReceiver(this.broad, filter);
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (orientation != -1) {
            if (orientation == 0) {
                sendToHandler(10);
            } else if (orientation == 90) {
                sendToHandler(11);
            } else if (orientation == 180) {
                sendToHandler(12);
            } else if (orientation == 270) {
                sendToHandler(13);
            }
        }
        this.layout_camera_shortcut_pop_horizontal_bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVPTZSettingPop.this.pop != null) {
                    FVPTZSettingPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVPTZSettingPop.this.pop != null) {
                    FVPTZSettingPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        CameraUtils.setFrameLayerNumber(1);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirPosition = 0;
            if (this.stirPosition == 0) {
                setBackgroundColorSelectOne();
                this.rl_contextual_model.setBackgroundColor(context2.getResources().getColor(C0853R.color.black15));
            }
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (this.cameraManager.getCameraManagerType() != 2) {
                this.rl_wheel_zoom.setVisibility(8);
            } else if (FVCameraManager.GetCameraLevel(context2) == 2) {
                this.rl_wheel_zoom.setVisibility(8);
            } else {
                this.rl_wheel_zoom.setVisibility(0);
            }
        }
        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     第一界面   第一界面");
                        return;
                    } else if (this.rl_contextual_mode.getVisibility() == 0) {
                        if (this.stirPosition == 3) {
                            this.seekbar_pitch_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_pitch_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        } else if (this.stirPosition == 4) {
                            this.seekbar_yaw_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_yaw_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        } else if (this.stirPosition == 5) {
                            this.seekbar_roll_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_roll_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        }
                        if (this.stirPosition == 3) {
                            int progress = this.seekbar_pitch_speed.getProgress() + 5;
                            if (progress > 100) {
                                progress = 100;
                            }
                            this.tv_pitch_pg_speed.setText(progress + "");
                            this.seekbar_pitch_speed.setProgress(progress);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_pitch_spped " + progress);
                        } else if (this.stirPosition == 4) {
                            int progress2 = this.seekbar_yaw_speed.getProgress() + 5;
                            if (progress2 > 100) {
                                progress2 = 100;
                            }
                            this.tv_yaw_pg_speed.setText(progress2 + "");
                            this.seekbar_yaw_speed.setProgress(progress2);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress2);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_yaw_speed " + progress2);
                        } else if (this.stirPosition == 5) {
                            int progress3 = this.seekbar_roll_speed.getProgress() + 5;
                            if (progress3 > 100) {
                                progress3 = 100;
                            }
                            this.tv_roll_pg_speed.setText(progress3 + "");
                            this.seekbar_roll_speed.setProgress(progress3);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress3, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_roll_speed " + progress3);
                        }
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     情景模式设置   情景模式设置");
                        return;
                    } else if (this.rl_follow_mode.getVisibility() == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     跟随模式设置   跟随模式设置");
                        return;
                    } else if (this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     调焦拨轮功能   调焦拨轮功能");
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     第一界面   第一界面");
                        return;
                    } else if (this.rl_contextual_mode.getVisibility() == 0) {
                        if (this.stirPosition == 3) {
                            this.seekbar_pitch_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_pitch_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        } else if (this.stirPosition == 4) {
                            this.seekbar_yaw_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_yaw_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        } else if (this.stirPosition == 5) {
                            this.seekbar_roll_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekbar_roll_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        }
                        if (this.stirPosition == 3) {
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_pitch_spped ");
                            int progress4 = this.seekbar_pitch_speed.getProgress() - 5;
                            if (progress4 < 0) {
                                progress4 = 0;
                            }
                            this.tv_pitch_pg_speed.setText(progress4 + "");
                            this.seekbar_pitch_speed.setProgress(progress4);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) progress4, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                        } else if (this.stirPosition == 4) {
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_yaw_speed ");
                            int progress5 = this.seekbar_yaw_speed.getProgress() - 5;
                            if (progress5 < 0) {
                                progress5 = 0;
                            }
                            this.tv_yaw_pg_speed.setText(progress5 + "");
                            this.seekbar_yaw_speed.setProgress(progress5);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress5);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                        } else if (this.stirPosition == 5) {
                            Log.e("---------------", "---------  5555   4444   8888  --------   rl_roll_speed ");
                            int progress6 = this.seekbar_roll_speed.getProgress() - 5;
                            if (progress6 < 0) {
                                progress6 = 0;
                            }
                            this.tv_roll_pg_speed.setText(progress6 + "");
                            this.seekbar_roll_speed.setProgress(progress6);
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress6, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                            } else {
                                Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                            }
                        }
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     情景模式设置   情景模式设置");
                        return;
                    } else if (this.rl_follow_mode.getVisibility() == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     跟随模式设置   跟随模式设置");
                        return;
                    } else if (this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     调焦拨轮功能   调焦拨轮功能");
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                        setBackgroundColorSettingPopSelect(this.stirPosition);
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 OK键  OK键  OK键    FVPTZSettingPop     第一界面   第一界面");
                        return;
                    } else if (this.rl_contextual_mode.getVisibility() == 0) {
                        if (this.rl_custom_isvisible.getVisibility() != 0) {
                            setBackgroundColorSceneModeSettingSelect(this.stirPosition);
                        } else if (this.stirPosition <= 2) {
                            setBackgroundColorSceneModeSettingSelect(this.stirPosition);
                        }
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 OK键  OK键  OK键    FVPTZSettingPop     情景模式设置   情景模式设置");
                        return;
                    } else if (this.rl_follow_mode.getVisibility() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVPTZSettingPop.this.setBackgroundColorFollowModeSelect(FVPTZSettingPop.this.stirPosition);
                                Log.e("-----------------", "----------  7777  8888  9999   -------  210 OK键  OK键  OK键    FVPTZSettingPop     跟随模式设置   跟随模式设置");
                            }
                        }, 50);
                        return;
                    } else if (this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVPTZSettingPop.this.setBackgroundColorFocusingWheelModeSelect(FVPTZSettingPop.this.stirPosition);
                            }
                        }, 50);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() != 1) {
                    return;
                }
                if (!this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                    this.title.setText(this.context.getString(C0853R.string.home_fragment_bottom_yuntai));
                    this.btn_back.setVisibility(8);
                    this.ll_contextual_model.setVisibility(8);
                    this.ll_ptz.setVisibility(0);
                    Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                    if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
                        this.stirPosition = 0;
                        if (this.stirPosition == 0) {
                            setBackgroundColorSelectOne();
                            this.rl_contextual_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        }
                    }
                    this.scaleSlide = false;
                    this.seekbar_pitch_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                    this.seekbar_pitch_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                    this.seekbar_yaw_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                    this.seekbar_yaw_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                    this.seekbar_roll_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                    this.seekbar_roll_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                    return;
                } else if (this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_ROCKING_BAR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 1) {
                    FM210labelCameraStirUp();
                    if (this.pop == null) {
                        return;
                    }
                    if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     第一界面   第一界面");
                        return;
                    } else if (this.rl_contextual_mode.getVisibility() == 0 && this.stirPosition >= 3 && this.stirPosition <= 5) {
                        this.seekbar_pitch_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_pitch_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        this.seekbar_yaw_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_yaw_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        this.seekbar_roll_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_roll_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_ROCKING_BAR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 1) {
                    FM210labelCameraStirDown();
                    if (this.pop == null) {
                        return;
                    }
                    if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     第一界面   第一界面");
                        return;
                    } else if (this.rl_contextual_mode.getVisibility() == 0 && this.stirPosition >= 3 && this.stirPosition <= 5) {
                        this.seekbar_pitch_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_pitch_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        this.seekbar_yaw_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_yaw_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        this.seekbar_roll_speed.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                        this.seekbar_roll_speed.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_FOCUSING_WHEEL_MODE_RESTART:
                if (CameraUtils.getFrameLayerNumber() == 1 && !this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai)) && this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                    setBackgroundColorSettingPopSelect(2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void controlTriaxial(int index) {
        byte data = 0;
        switch (index) {
            case 0:
                data = 0;
                break;
            case 1:
                data = 1;
                break;
            case 2:
                data = 2;
                break;
            case 3:
                data = 3;
                break;
            case 4:
                data = 4;
                break;
        }
        BleByteUtil.setPTZParameters((byte) 86, data);
    }

    private void FM210labelCameraStirUp() {
        if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
            if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                this.stirPosition--;
                if (this.stirPosition < 0) {
                    this.stirPosition = 0;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_contextual_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_pitch_axis_lock.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_focusing_wheel_out_item.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     第一界面   第一界面");
            } else if (this.rl_contextual_mode.getVisibility() == 0) {
                if (!this.scaleSlide) {
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    if (this.stirPosition == 0) {
                        setBackgroundColorSelectOne();
                        this.rl_walk_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    } else if (this.stirPosition == 1) {
                        setBackgroundColorSelectOne();
                        this.rl_sport_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    } else if (this.stirPosition == 2) {
                        setBackgroundColorSelectOne();
                        this.rl_custom_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    }
                    if (this.rl_custom_isvisible.getVisibility() == 0) {
                        if (this.stirPosition == 3) {
                            setBackgroundColorSelectOne();
                            this.rl_pitch_spped.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else if (this.stirPosition == 4) {
                            setBackgroundColorSelectOne();
                            this.rl_yaw_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else if (this.stirPosition == 5) {
                            setBackgroundColorSelectOne();
                            this.rl_roll_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        }
                    }
                    if (this.stirPosition >= 0 && this.stirPosition <= 5) {
                        scrollViewMoveToTopDistance(this.stirPosition);
                    }
                } else if (this.stirPosition == 3) {
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_pitch_spped ");
                    int progress = this.seekbar_pitch_speed.getProgress() - 5;
                    if (progress < 0) {
                        progress = 0;
                    }
                    this.tv_pitch_pg_speed.setText(progress + "");
                    this.seekbar_pitch_speed.setProgress(progress);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                } else if (this.stirPosition == 4) {
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_yaw_speed ");
                    int progress2 = this.seekbar_yaw_speed.getProgress() - 5;
                    if (progress2 < 0) {
                        progress2 = 0;
                    }
                    this.tv_yaw_pg_speed.setText(progress2 + "");
                    this.seekbar_yaw_speed.setProgress(progress2);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress2);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                } else if (this.stirPosition == 5) {
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_roll_speed ");
                    int progress3 = this.seekbar_roll_speed.getProgress() - 5;
                    if (progress3 < 0) {
                        progress3 = 0;
                    }
                    this.tv_roll_pg_speed.setText(progress3 + "");
                    this.seekbar_roll_speed.setProgress(progress3);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress3, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     情景模式设置   情景模式设置");
            } else if (this.rl_follow_mode.getVisibility() == 0) {
                this.stirPosition--;
                if (this.stirPosition < 0) {
                    this.stirPosition = 0;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_all_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_half_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_no_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 3) {
                    setBackgroundColorSelectOne();
                    this.rl_fpv_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     跟随模式设置   跟随模式设置");
            } else if (this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                this.stirPosition--;
                if (this.stirPosition < 0) {
                    this.stirPosition = 0;
                }
                if (this.cameraManager.getCameraManagerType() == 2) {
                    if (FVCameraManager.GetCameraLevel(this.context) == 2 && this.stirPosition == 1) {
                        this.stirPosition = 0;
                    }
                } else if (this.stirPosition == 1) {
                    this.stirPosition = 0;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_heel_focus.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_zoom.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_roll_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 3) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_heading_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 4) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_pitch_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                if (this.stirPosition >= 0 && this.stirPosition <= 5) {
                    scrollViewMoveToTopfocusWheelDistance(this.stirPosition);
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVPTZSettingPop     调焦拨轮功能   调焦拨轮功能");
            }
        }
    }

    private void FM210labelCameraStirDown() {
        if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
            if (this.title.getText().toString().equals(this.context.getString(C0853R.string.home_fragment_bottom_yuntai))) {
                this.stirPosition++;
                if (this.stirPosition > 2) {
                    this.stirPosition = 2;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_contextual_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_pitch_axis_lock.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_focusing_wheel_out_item.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     第一界面   第一界面");
            } else if (this.rl_contextual_mode.getVisibility() == 0) {
                if (!this.scaleSlide) {
                    this.stirPosition++;
                    if (this.rl_custom_isvisible.getVisibility() == 0) {
                        if (this.stirPosition > 5) {
                            this.stirPosition = 5;
                        }
                    } else if (this.stirPosition > 2) {
                        this.stirPosition = 2;
                    }
                    if (this.stirPosition == 0) {
                        setBackgroundColorSelectOne();
                        this.rl_walk_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    } else if (this.stirPosition == 1) {
                        setBackgroundColorSelectOne();
                        this.rl_sport_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    } else if (this.stirPosition == 2) {
                        setBackgroundColorSelectOne();
                        this.rl_custom_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                    }
                    if (this.rl_custom_isvisible.getVisibility() == 0) {
                        if (this.stirPosition == 3) {
                            setBackgroundColorSelectOne();
                            this.rl_pitch_spped.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else if (this.stirPosition == 4) {
                            setBackgroundColorSelectOne();
                            this.rl_yaw_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else if (this.stirPosition == 5) {
                            setBackgroundColorSelectOne();
                            this.rl_roll_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        }
                    }
                    if (this.stirPosition >= 0 && this.stirPosition <= 5) {
                        scrollViewMoveToTopDistance(this.stirPosition);
                    }
                } else if (this.stirPosition == 3) {
                    int progress = this.seekbar_pitch_speed.getProgress() + 5;
                    if (progress > 100) {
                        progress = 100;
                    }
                    this.tv_pitch_pg_speed.setText(progress + "");
                    this.seekbar_pitch_speed.setProgress(progress);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_pitch_spped " + progress);
                } else if (this.stirPosition == 4) {
                    int progress2 = this.seekbar_yaw_speed.getProgress() + 5;
                    if (progress2 > 100) {
                        progress2 = 100;
                    }
                    this.tv_yaw_pg_speed.setText(progress2 + "");
                    this.seekbar_yaw_speed.setProgress(progress2);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress2);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_yaw_speed " + progress2);
                } else if (this.stirPosition == 5) {
                    int progress3 = this.seekbar_roll_speed.getProgress() + 5;
                    if (progress3 > 100) {
                        progress3 = 100;
                    }
                    this.tv_roll_pg_speed.setText(progress3 + "");
                    this.seekbar_roll_speed.setProgress(progress3);
                    if (ViseBluetooth.getInstance().isConnected()) {
                        BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress3, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                    Log.e("---------------", "---------  5555   4444   8888  --------   rl_roll_speed " + progress3);
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     情景模式设置   情景模式设置");
            } else if (this.rl_follow_mode.getVisibility() == 0) {
                this.stirPosition++;
                if (this.stirPosition > 3) {
                    this.stirPosition = 3;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_all_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_half_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_no_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 3) {
                    setBackgroundColorSelectOne();
                    this.rl_fpv_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     跟随模式设置   跟随模式设置");
            } else if (this.rl_label_focusing_wheel_mode.getVisibility() == 0) {
                this.stirPosition++;
                if (this.stirPosition > 4) {
                    this.stirPosition = 4;
                }
                if (this.cameraManager.getCameraManagerType() == 2) {
                    if (FVCameraManager.GetCameraLevel(this.context) == 2 && this.stirPosition == 1) {
                        this.stirPosition = 2;
                    }
                } else if (this.stirPosition == 1) {
                    this.stirPosition = 2;
                }
                if (this.stirPosition == 0) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_heel_focus.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 1) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_zoom.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 2) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_roll_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 3) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_heading_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                } else if (this.stirPosition == 4) {
                    setBackgroundColorSelectOne();
                    this.rl_wheel_pitch_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                }
                if (this.stirPosition >= 0 && this.stirPosition <= 5) {
                    scrollViewMoveToTopfocusWheelDistance(this.stirPosition);
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向下   向下   向下    FVPTZSettingPop     调焦拨轮功能   调焦拨轮功能");
            }
        }
    }

    private void scrollViewMoveToTopDistance(int stirPos) {
        if (stirPos == 0) {
            this.ll_contextual_model.scrollTo(0, this.rl_walk_model.getTop());
        } else if (stirPos == 1) {
            this.ll_contextual_model.scrollTo(0, this.rl_sport_model.getTop());
        } else if (stirPos == 2) {
            this.ll_contextual_model.scrollTo(0, this.rl_custom_model.getTop());
        } else if (stirPos == 3) {
            this.ll_contextual_model.scrollTo(0, this.rl_pitch_spped.getTop() + this.rl_custom_model.getTop());
        } else if (stirPos == 4) {
            this.ll_contextual_model.scrollTo(0, this.rl_yaw_speed.getTop() + this.rl_custom_model.getTop());
        } else if (stirPos == 5) {
            this.ll_contextual_model.scrollTo(0, this.rl_roll_speed.getTop() + this.rl_custom_model.getTop());
        }
    }

    private void scrollViewMoveToTopfocusWheelDistance(int stirPos) {
        if (stirPos == 0) {
            this.ll_contextual_model.scrollTo(0, this.rl_wheel_heel_focus.getTop());
        } else if (stirPos == 1) {
            this.ll_contextual_model.scrollTo(0, this.rl_wheel_zoom.getTop());
        } else if (stirPos == 2) {
            this.ll_contextual_model.scrollTo(0, this.rl_wheel_roll_axis.getTop());
        } else if (stirPos == 3) {
            this.ll_contextual_model.scrollTo(0, this.rl_wheel_heading_axis.getTop());
        } else if (stirPos == 4) {
            this.ll_contextual_model.scrollTo(0, this.rl_wheel_pitch_axis.getTop());
        }
    }

    private class OrientationBroadPopup extends BroadcastReceiver {
        private Message message;

        private OrientationBroadPopup() {
        }

        public void onReceive(Context context, Intent intent) {
            int orientation = intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1);
            Log.e("-------------", "----------888-- orientation --" + orientation);
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVPTZSettingPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVPTZSettingPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVPTZSettingPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVPTZSettingPop.this.sendToHandler(13);
            }
        }
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero180() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 3;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height / 3;
        linearParams3.width = this.height;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    public View getView() {
        return this.view;
    }

    public void setPop(PopupWindow pop2) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVPTZSettingPop.this.context.unregisterReceiver(FVPTZSettingPop.this.broad);
                    EventBus.getDefault().unregister(this);
                    CameraUtils.setFrameLayerNumber(0);
                }
            });
        }
    }

    public void unRegisterEvent() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
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
        switch (event.getCode()) {
            case 34:
                if (BlePtzParasConstant.SET_MANUAL_MODE_SWITCH == 0) {
                    if (this.switchButton != null) {
                        this.switchButton.setChecked(false);
                        return;
                    }
                    return;
                } else if (this.switchButton != null) {
                    this.switchButton.setChecked(true);
                    return;
                } else {
                    return;
                }
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[1] & 255) != 31) {
                    ViseLog.m1466e("upgrade receive data :byte[]:" + Arrays.toString(value) + " ,hex: " + HexUtil.encodeHexStr(value));
                }
                if ((value[0] & 255) != 90) {
                    if (!((value[0] & 255) == 165 && BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value) == 9)) {
                    }
                    return;
                } else if ((value[1] & 255) == 5) {
                    clearEnableFollow();
                    if ((value[2] & 255) == 0) {
                        setEnableTrueOrfalse(this.iv_half_follow_select, this.rl_half_follow_mode);
                        return;
                    } else if ((value[2] & 255) == 1) {
                        setEnableTrueOrfalse(this.iv_all_follow_select, this.rl_all_follow_mode);
                        return;
                    } else if ((value[2] & 255) == 2) {
                        setEnableTrueOrfalse(this.iv_no_follow_select, this.rl_no_follow_mode);
                        return;
                    } else if (!((String) SPUtils.get(this.context, SharePrefConstant.CURRENT_PTZ_TYPE, "")).equals(BleConstant.FM_300)) {
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        return;
                    } else if (BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH != 0) {
                        Toast.makeText(this.context, C0853R.string.pov_to_imagin_action, 0).show();
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        this.imagin_action_switchButton.setChecked(false);
                        BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH = 0;
                        return;
                    } else {
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        return;
                    }
                } else if ((value[1] & 255) == 25) {
                    clearEnableContextual();
                    if ((value[2] & 255) == 0) {
                        this.iv_custom_select.setVisibility(0);
                        return;
                    } else if ((value[2] & 255) == 1) {
                        setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                        return;
                    } else if ((value[2] & 255) == 2) {
                        setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case 129:
                if (this.pop != null) {
                    this.pop.dismiss();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void initPop(View view2) {
        this.title = (TextView) view2.findViewById(C0853R.C0855id.title);
        this.btn_back = (ImageView) view2.findViewById(C0853R.C0855id.btn_back);
        this.ll_ptz = (LinearLayout) view2.findViewById(C0853R.C0855id.ll_ptz);
        this.rl_contextual_model = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_contextual_model);
        this.rl_pitch_axis_lock = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_pitch_axis_lock);
        this.switchButton = (SwitchButton) view2.findViewById(C0853R.C0855id.switchButton);
        this.swCharging = (SwitchButton) view2.findViewById(C0853R.C0855id.sw_charging);
        this.ll_contextual_model = (ScrollView) view2.findViewById(C0853R.C0855id.ll_contextual_model);
        this.rl_contextual_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_contextual_mode);
        this.rl_walk_model = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_walk_model);
        this.rl_sport_model = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_sport_model);
        this.rl_custom_model = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_custom_model);
        this.iv_walk_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_walk_select);
        this.iv_sport_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_sport_select);
        this.iv_custom_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_custom_select);
        this.rl_follow_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_follow_mode);
        this.rl_all_follow_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_all_follow_mode);
        this.rl_half_follow_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_half_follow_mode);
        this.rl_fpv_follow_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_fpv_follow_mode);
        this.rl_no_follow_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_no_follow_mode);
        this.iv_all_follow_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_all_follow_select);
        this.iv_half_follow_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_half_follow_select);
        this.iv_fpv_follow_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_fpv_follow_select);
        this.iv_no_follow_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_no_follow_select);
        this.rl_custom_isvisible = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_custom_isvisible);
        this.seekbar_pitch_speed = (SeekBar) view2.findViewById(C0853R.C0855id.seekbar_pitch_speed);
        this.tv_pitch_pg_speed = (TextView) view2.findViewById(C0853R.C0855id.tv_pitch_pg_speed);
        this.seekbar_yaw_speed = (SeekBar) view2.findViewById(C0853R.C0855id.seekbar_yaw_speed);
        this.tv_yaw_pg_speed = (TextView) view2.findViewById(C0853R.C0855id.tv_yaw_pg_speed);
        this.seekbar_roll_speed = (SeekBar) view2.findViewById(C0853R.C0855id.seekbar_roll_speed);
        this.tv_roll_pg_speed = (TextView) view2.findViewById(C0853R.C0855id.tv_roll_pg_speed);
        this.rl_pitch_spped = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_pitch_spped);
        this.rl_yaw_speed = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_yaw_speed);
        this.rl_roll_speed = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_roll_speed);
        this.imagin_action_linear = (LinearLayout) view2.findViewById(C0853R.C0855id.imagin_action_linear);
        this.imagin_action_switchButton = (SwitchButton) view2.findViewById(C0853R.C0855id.imagin_action_switchButton);
        this.imagin_action_linear.setVisibility(8);
        String ptzType = (String) SPUtils.get(this.context, SharePrefConstant.CURRENT_PTZ_TYPE, "");
        boolean connected = ViseBluetooth.getInstance().isConnected();
        if (!ptzType.equals(BleConstant.FM_300) || !connected) {
            this.imagin_action_linear.setVisibility(8);
        } else {
            this.imagin_action_linear.setVisibility(0);
        }
        this.manual_model_linear = (LinearLayout) view2.findViewById(C0853R.C0855id.manual_model_linear);
        this.manual_model_linear.setVisibility(8);
        if (!ptzType.equals("") || CameraUtils.getCurrentPageIndex() != 0) {
            this.manual_model_linear.setVisibility(8);
        } else {
            this.manual_model_linear.setVisibility(0);
        }
        this.rl_walk_model.setOnClickListener(this);
        this.rl_sport_model.setOnClickListener(this);
        this.rl_custom_model.setOnClickListener(this);
        this.rl_all_follow_mode.setOnClickListener(this);
        this.rl_half_follow_mode.setOnClickListener(this);
        this.rl_no_follow_mode.setOnClickListener(this);
        this.rl_fpv_follow_mode.setOnClickListener(this);
        this.rl_focusing_wheel_out_item = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_focusing_wheel);
        this.rl_label_focusing_wheel_mode = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_label_focusing_wheel_mode);
        this.rl_wheel_heel_focus = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_wheel_heel_focus);
        this.rl_wheel_zoom = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_wheel_zoom);
        this.rl_wheel_roll_axis = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_wheel_roll_axis);
        this.rl_wheel_heading_axis = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_wheel_heading_axis);
        this.rl_wheel_pitch_axis = (RelativeLayout) view2.findViewById(C0853R.C0855id.rl_wheel_pitch_axis);
        this.rl_focusing_wheel_view = view2.findViewById(C0853R.C0855id.rl_focusing_wheel_view);
        this.iv_wheel_heel_focus_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_wheel_heel_focus_select);
        this.iv_wheel_zoom_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_wheel_zoom_select);
        this.iv_wheel_roll_axis_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_wheel_roll_axis_select);
        this.iv_wheel_heading_axis_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_wheel_heading_axis_select);
        this.iv_wheel_pitch_axis_select = (ImageView) view2.findViewById(C0853R.C0855id.iv_wheel_pitch_axis_select);
        this.rl_wheel_heel_focus.setOnClickListener(this);
        this.rl_wheel_zoom.setOnClickListener(this);
        this.rl_wheel_roll_axis.setOnClickListener(this);
        this.rl_wheel_heading_axis.setOnClickListener(this);
        this.rl_wheel_pitch_axis.setOnClickListener(this);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.rl_focusing_wheel_out_item.setVisibility(0);
            this.rl_focusing_wheel_view.setVisibility(0);
        }
        this.title.setText(this.context.getString(C0853R.string.home_fragment_bottom_yuntai));
        this.btn_back.setVisibility(8);
        ViseLog.m1466e("手动模式开关初始值" + BlePtzParasConstant.SET_MANUAL_MODE_SWITCH);
        if (BlePtzParasConstant.SET_MANUAL_MODE_SWITCH == 0) {
            this.switchButton.setChecked(false);
            Log.e("----------------", "---------  888888  -----  setCheck  false  false  false  false  -------");
        } else {
            this.switchButton.setChecked(true);
            Log.e("----------------", "---------  888888  -----  setCheck  true  true  true  true  -------");
        }
        if (BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH == 0) {
            this.imagin_action_switchButton.setChecked(false);
        } else {
            this.imagin_action_switchButton.setChecked(true);
        }
        if (BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH == 0) {
            this.swCharging.setChecked(false);
        } else {
            this.swCharging.setChecked(true);
        }
        this.rl_contextual_model.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVPTZSettingPop.this.setBackgroundColorSettingPopSelect(0);
            }
        });
        this.rl_pitch_axis_lock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVPTZSettingPop.this.setBackgroundColorSettingPopSelect(1);
            }
        });
        this.rl_focusing_wheel_out_item.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVPTZSettingPop.this.setBackgroundColorSettingPopSelect(2);
            }
        });
        this.imagin_action_switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                boolean connected = ViseBluetooth.getInstance().isConnected();
                if (isChecked) {
                    if (!connected) {
                        Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                        Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.pov_to_imagin_action), 0).show();
                        FVPTZSettingPop.this.imagin_action_switchButton.setChecked(false);
                    } else {
                        BleByteUtil.setPTZParameters((byte) 49, (byte) 1);
                    }
                } else if (!connected) {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.pov_to_imagin_action), 0).show();
                    FVPTZSettingPop.this.imagin_action_switchButton.setChecked(false);
                } else {
                    BleByteUtil.setPTZParameters((byte) 49, (byte) 0);
                }
            }
        });
        this.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                boolean connected = ViseBluetooth.getInstance().isConnected();
                if (isChecked) {
                    if (connected) {
                        ViseLog.m1466e("打开手动开关");
                        BleByteUtil.setPTZParameters((byte) 18, (byte) 1);
                        return;
                    }
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                } else if (connected) {
                    ViseLog.m1466e("关闭手动开关");
                    BleByteUtil.setPTZParameters((byte) 18, (byte) 0);
                } else {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.swCharging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean connected = ViseBluetooth.getInstance().isConnected();
                if (isChecked) {
                    if (connected) {
                        BleByteUtil.setPTZParameters((byte) 24, (byte) 1);
                    } else {
                        Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                    }
                } else if (connected) {
                    BleByteUtil.setPTZParameters((byte) 24, (byte) 0);
                } else {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_pitch_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVPTZSettingPop.this.tv_pitch_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("---------------", "--------  6666   7777   8888   onStopTrackingTouch  -------");
                int progress = seekBar.getProgress();
                FVPTZSettingPop.this.tv_pitch_pg_speed.setText(progress + "");
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) progress, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                } else {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_yaw_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVPTZSettingPop.this.tv_yaw_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVPTZSettingPop.this.tv_yaw_pg_speed.setText(progress + "");
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress);
                } else {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.seekbar_roll_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                FVPTZSettingPop.this.tv_roll_pg_speed.setText(seekBar.getProgress() + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVPTZSettingPop.this.tv_roll_pg_speed.setText(progress + "");
                if (ViseBluetooth.getInstance().isConnected()) {
                    BleByteUtil.setPTZParameters((byte) 9, (byte) BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR, (byte) progress, (byte) BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                } else {
                    Toast.makeText(FVPTZSettingPop.this.context, FVPTZSettingPop.this.context.getResources().getString(C0853R.string.label_device_not_connected), 0).show();
                }
            }
        });
        this.btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FVPTZSettingPop.this.title.setText(FVPTZSettingPop.this.context.getString(C0853R.string.home_fragment_bottom_yuntai));
                FVPTZSettingPop.this.btn_back.setVisibility(8);
                FVPTZSettingPop.this.ll_contextual_model.setVisibility(8);
                FVPTZSettingPop.this.ll_ptz.setVisibility(0);
                Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
                    int unused = FVPTZSettingPop.this.stirPosition = 0;
                    if (FVPTZSettingPop.this.stirPosition == 0) {
                        FVPTZSettingPop.this.setBackgroundColorSelectOne();
                        FVPTZSettingPop.this.rl_contextual_model.setBackgroundColor(FVPTZSettingPop.this.context.getResources().getColor(C0853R.color.black15));
                    }
                }
            }
        });
    }

    private void setEnableTrueOrfalse(ImageView image, RelativeLayout rllayout) {
        image.setVisibility(0);
        rllayout.setEnabled(false);
    }

    private void clearEnableContextual() {
        this.iv_walk_select.setVisibility(8);
        this.iv_sport_select.setVisibility(8);
        this.iv_custom_select.setVisibility(8);
        this.rl_walk_model.setEnabled(true);
        this.rl_sport_model.setEnabled(true);
        this.rl_custom_model.setEnabled(true);
    }

    private void clearEnableFollow() {
        this.iv_all_follow_select.setVisibility(8);
        this.iv_half_follow_select.setVisibility(8);
        this.iv_no_follow_select.setVisibility(8);
        this.iv_fpv_follow_select.setVisibility(8);
        this.rl_all_follow_mode.setEnabled(true);
        this.rl_half_follow_mode.setEnabled(true);
        this.rl_no_follow_mode.setEnabled(true);
        this.rl_fpv_follow_mode.setEnabled(true);
    }

    private void clearFocusingWheelMode() {
        this.iv_wheel_heel_focus_select.setVisibility(8);
        this.iv_wheel_zoom_select.setVisibility(8);
        this.iv_wheel_roll_axis_select.setVisibility(8);
        this.iv_wheel_heading_axis_select.setVisibility(8);
        this.iv_wheel_pitch_axis_select.setVisibility(8);
        this.rl_wheel_heel_focus.setEnabled(true);
        this.rl_wheel_zoom.setEnabled(true);
        this.rl_wheel_roll_axis.setEnabled(true);
        this.rl_wheel_heading_axis.setEnabled(true);
        this.rl_wheel_pitch_axis.setEnabled(true);
    }

    public void onClick(View view2) {
        switch (view2.getId()) {
            case C0853R.C0855id.rl_walk_model:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(8);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_sport_model:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(8);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_custom_model:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(0);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_all_follow_mode:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_all_follow_select, this.rl_all_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_half_follow_mode:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_half_follow_select, this.rl_half_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_no_follow_mode:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_no_follow_select, this.rl_no_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_fpv_follow_mode:
                String gmuVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
                String imuVersion = BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING;
                if (ViseBluetooth.getInstance().isConnected()) {
                    String ptzType = (String) SPUtils.get(this.context, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                    if (ptzType.equals("")) {
                        if (!compareVersion(gmuVersion, "01.00.00.35") || !compareVersion(imuVersion, "1.0.0.30")) {
                            showMyDialogKnow();
                            return;
                        }
                        clearEnableFollow();
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        BleByteUtil.setPTZParameters((byte) 5, (byte) 3);
                        return;
                    } else if (!ptzType.equals(BleConstant.FM_300) || BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH == 0) {
                        clearEnableFollow();
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        BleByteUtil.setPTZParameters((byte) 5, (byte) 3);
                        return;
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.imagin_action_to_pov), 0).show();
                        return;
                    }
                } else {
                    Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                    return;
                }
            case C0853R.C0855id.rl_wheel_heel_focus:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_heel_focus_select, this.rl_wheel_heel_focus);
                    controlTriaxial(0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_wheel_zoom:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_zoom_select, this.rl_wheel_zoom);
                    controlTriaxial(1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_wheel_roll_axis:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_roll_axis_select, this.rl_wheel_roll_axis);
                    controlTriaxial(2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_wheel_heading_axis:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_heading_axis_select, this.rl_wheel_heading_axis);
                    controlTriaxial(3);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case C0853R.C0855id.rl_wheel_pitch_axis:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_pitch_axis_select, this.rl_wheel_pitch_axis);
                    controlTriaxial(4);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorSettingPopSelect(int select) {
        switch (select) {
            case 0:
                this.title.setText(this.context.getString(C0853R.string.contextual_model));
                this.btn_back.setVisibility(0);
                this.ll_ptz.setVisibility(8);
                this.ll_contextual_model.setVisibility(0);
                this.rl_contextual_mode.setVisibility(0);
                this.rl_follow_mode.setVisibility(8);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.rl_label_focusing_wheel_mode.setVisibility(8);
                }
                clearEnableContextual();
                this.iv_custom_select.setVisibility(8);
                if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 0) {
                    setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                    this.rl_custom_isvisible.setVisibility(0);
                    this.seekbar_pitch_speed.setProgress(BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR);
                    this.seekbar_yaw_speed.setProgress(BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR);
                    this.seekbar_roll_speed.setProgress(BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR);
                    this.tv_pitch_pg_speed.setText(BlePtzParasConstant.SET_PITCH_OF_FOLLOW_SPEED_SETTING_FOR + "");
                    this.tv_yaw_pg_speed.setText(BlePtzParasConstant.SET_YAW_OF_FOLLOW_SPEED_SETTING_FOR + "");
                    this.tv_roll_pg_speed.setText(BlePtzParasConstant.SET_ROLL_OF_FOLLOW_SPEED_SETTING_FOR + "");
                } else if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 1) {
                    setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                    this.rl_custom_isvisible.setVisibility(8);
                } else if (BlePtzParasConstant.SET_CONTEXTUAL_MODEL == 2) {
                    setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                    this.rl_custom_isvisible.setVisibility(8);
                }
                Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
                    this.stirPosition = 0;
                    if (this.stirPosition == 0) {
                        setBackgroundColorSelectOne();
                        this.rl_walk_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        return;
                    }
                    return;
                }
                return;
            case 1:
                this.title.setText(this.context.getString(C0853R.string.follow_model));
                this.btn_back.setVisibility(0);
                this.ll_ptz.setVisibility(8);
                this.ll_contextual_model.setVisibility(0);
                this.rl_contextual_mode.setVisibility(8);
                this.rl_follow_mode.setVisibility(0);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.rl_label_focusing_wheel_mode.setVisibility(8);
                }
                clearEnableFollow();
                if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
                    setEnableTrueOrfalse(this.iv_half_follow_select, this.rl_half_follow_mode);
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
                    setEnableTrueOrfalse(this.iv_all_follow_select, this.rl_all_follow_mode);
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
                    setEnableTrueOrfalse(this.iv_no_follow_select, this.rl_no_follow_mode);
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                    setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                }
                Boolean isConnectedFm = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                if (CameraUtils.getCurrentPageIndex() == 2 && isConnectedFm.booleanValue()) {
                    this.stirPosition = 0;
                    if (this.stirPosition == 0) {
                        setBackgroundColorSelectOne();
                        this.rl_all_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        return;
                    }
                    return;
                }
                return;
            case 2:
                this.title.setText(this.context.getString(C0853R.string.label_focusing_wheel_mode));
                this.btn_back.setVisibility(0);
                this.ll_ptz.setVisibility(8);
                this.ll_contextual_model.setVisibility(0);
                this.rl_contextual_mode.setVisibility(8);
                this.rl_follow_mode.setVisibility(8);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    this.rl_label_focusing_wheel_mode.setVisibility(0);
                }
                clearFocusingWheelMode();
                if (BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION == 0) {
                    setEnableTrueOrfalse(this.iv_wheel_heel_focus_select, this.rl_wheel_heel_focus);
                } else if (BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION == 1) {
                    setEnableTrueOrfalse(this.iv_wheel_zoom_select, this.rl_wheel_zoom);
                } else if (BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION == 2) {
                    setEnableTrueOrfalse(this.iv_wheel_roll_axis_select, this.rl_wheel_roll_axis);
                } else if (BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION == 3) {
                    setEnableTrueOrfalse(this.iv_wheel_heading_axis_select, this.rl_wheel_heading_axis);
                } else if (BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION == 4) {
                    setEnableTrueOrfalse(this.iv_wheel_pitch_axis_select, this.rl_wheel_pitch_axis);
                }
                Boolean isConnected3 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                if (CameraUtils.getCurrentPageIndex() == 2 && isConnected3.booleanValue()) {
                    this.stirPosition = 0;
                    if (this.stirPosition == 0) {
                        setBackgroundColorSelectOne();
                        this.rl_wheel_heel_focus.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setBackgroundColorSceneModeSettingSelect(int select) {
        switch (select) {
            case 0:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(8);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_walk_select, this.rl_walk_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 1:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(8);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_sport_select, this.rl_sport_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 2:
                clearEnableContextual();
                this.rl_custom_isvisible.setVisibility(0);
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_custom_select, this.rl_custom_model);
                    BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.MID_ROW_CHAN_2, (byte) 0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorFocusingWheelModeSelect(int select) {
        switch (select) {
            case 0:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_heel_focus_select, this.rl_wheel_heel_focus);
                    controlTriaxial(0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 1:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_zoom_select, this.rl_wheel_zoom);
                    controlTriaxial(1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 2:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_roll_axis_select, this.rl_wheel_roll_axis);
                    controlTriaxial(2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 3:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_heading_axis_select, this.rl_wheel_heading_axis);
                    controlTriaxial(3);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 4:
                clearFocusingWheelMode();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_wheel_pitch_axis_select, this.rl_wheel_pitch_axis);
                    controlTriaxial(4);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorFollowModeSelect(int select) {
        switch (select) {
            case 0:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_all_follow_select, this.rl_all_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 1);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 1:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_half_follow_select, this.rl_half_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 0);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 2:
                clearEnableFollow();
                if (ViseBluetooth.getInstance().isConnected()) {
                    setEnableTrueOrfalse(this.iv_no_follow_select, this.rl_no_follow_mode);
                    BleByteUtil.setPTZParameters((byte) 5, (byte) 2);
                    return;
                }
                Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                return;
            case 3:
                String gmuVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
                String imuVersion = BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING;
                if (ViseBluetooth.getInstance().isConnected()) {
                    String ptzType = (String) SPUtils.get(this.context, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                    if (ptzType.equals("")) {
                        if (!compareVersion(gmuVersion, "01.00.00.35") || !compareVersion(imuVersion, "1.0.0.30")) {
                            showMyDialogKnow();
                            return;
                        }
                        clearEnableFollow();
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        BleByteUtil.setPTZParameters((byte) 5, (byte) 3);
                        return;
                    } else if (!ptzType.equals(BleConstant.FM_300) || BlePtzParasConstant.SET_FM300_KALEIDOSCOPE_MODE_SWITCH == 0) {
                        clearEnableFollow();
                        setEnableTrueOrfalse(this.iv_fpv_follow_select, this.rl_fpv_follow_mode);
                        BleByteUtil.setPTZParameters((byte) 5, (byte) 3);
                        return;
                    } else {
                        Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.imagin_action_to_pov), 0).show();
                        return;
                    }
                } else {
                    Toast.makeText(this.context, this.context.getString(C0853R.string.label_device_not_connected), 1).show();
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorSelectOne() {
        this.rl_contextual_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_pitch_axis_lock.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.manual_model_linear.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_walk_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_sport_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_custom_model.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_custom_isvisible.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_pitch_spped.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_yaw_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_roll_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_all_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_half_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_no_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_fpv_follow_mode.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_wheel_heel_focus.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_wheel_zoom.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_wheel_roll_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_wheel_heading_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_wheel_pitch_axis.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
        this.rl_focusing_wheel_out_item.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
    }

    public static boolean compareVersion(String version1, String version2) {
        boolean z = true;
        if (version1 == null || "".equals(version1) || version1.equals(version2)) {
            return false;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen) {
            diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index]);
            if (diff != 0) {
                break;
            }
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return true;
                }
            }
            int i2 = index;
            while (i2 < version2Array.length && Integer.parseInt(version2Array[i2]) <= 0) {
                i2++;
            }
            return false;
        }
        if (diff <= 0) {
            z = false;
        }
        return z;
    }

    public void showMyDialogKnow() {
        if (this.selfDialogKnowTwo != null) {
            this.selfDialogKnowTwo.dismiss();
        }
        this.selfDialogKnowTwo = new SelfDialogKnowTwo(this.context);
        Window window = this.selfDialogKnowTwo.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.selfDialogKnowTwo.setButtonOnClick(new SelfDialogKnowTwo.CheckButtonOnclick() {
            public void onClick(View view) {
                FVPTZSettingPop.this.selfDialogKnowTwo.dismiss();
            }
        });
        this.selfDialogKnowTwo.show();
    }
}
