package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.view.ScrollPickerView;
import com.freevisiontech.fvmobile.widget.view.StringScrollPicker;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVTimeLapseStandardPop {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    Button btnStart;
    private TextView btnTitleText;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public String duration = BleConstant.SHUTTER;
    private int height;
    private boolean isBacked = false;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    private LinearLayout layout_lapse_shutter_interval;
    private RelativeLayout layout_lapse_time_duration;
    private LinearLayout layout_lapse_time_visible_or_gone;
    private LinearLayout ll_recording_long_light_switch;
    /* access modifiers changed from: private */
    public List<CharSequence> mData3;
    /* access modifiers changed from: private */
    public List<CharSequence> mData4;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVTimeLapseStandardPop.this.setHorUiZero();
                    return;
                case 11:
                    FVTimeLapseStandardPop.this.setHorUiNinety();
                    return;
                case 12:
                    FVTimeLapseStandardPop.this.setHorUiZero180();
                    return;
                case 13:
                    FVTimeLapseStandardPop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    StringScrollPicker pickerDuration;
    StringScrollPicker pickerShutter;
    /* access modifiers changed from: private */
    public ArrayList<String> pointList = new ArrayList<>();
    /* access modifiers changed from: private */
    public ArrayList<String> pointListStatic = new ArrayList<>();
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private SwitchButton recording_long_light_switch;
    private RelativeLayout rl_recording_long_light;
    private boolean scaleSlide = false;
    /* access modifiers changed from: private */
    public String shutter = "0.25";
    private int stirPosition = -1;
    TextView tvHint;
    TextView tvVideoDuration;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.parentView = parentView2;
        this.context = context2;
        this.isBacked = isBacked2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_time_lapse_pop_new, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.tvHint = (TextView) this.view.findViewById(C0853R.C0855id.tv_hint);
        this.tvVideoDuration = (TextView) this.view.findViewById(C0853R.C0855id.tv_video_duration);
        this.btnStart = (Button) this.view.findViewById(C0853R.C0855id.btn_start);
        this.btnStart.setText(C0853R.string.label_next_step);
        this.layout_lapse_time_visible_or_gone = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_lapse_time_visible_or_gone);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.layout_time_lapse_pop_new_title);
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.btnTitleText.setText(C0853R.string.label_move_delay_video);
        } else {
            this.btnTitleText.setText(C0853R.string.label_move_video);
            this.layout_lapse_time_visible_or_gone.setVisibility(8);
        }
        ((RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_tripod_model)).setVisibility(8);
        this.pickerShutter = (StringScrollPicker) this.view.findViewById(C0853R.C0855id.picker_shutter);
        this.pickerDuration = (StringScrollPicker) this.view.findViewById(C0853R.C0855id.picker_duration);
        initData();
        initSystemLight();
        initListener();
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
                if (FVTimeLapseStandardPop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapseStandardPop.this.pop != null) {
                }
            }
        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        CameraUtils.setFrameLayerNumber(4);
        this.layout_lapse_shutter_interval = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_lapse_time_visible_or_gone);
        this.layout_lapse_time_duration = (RelativeLayout) this.view.findViewById(C0853R.C0855id.layout_lapse_time_duration);
        Boolean isConnected2 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected2.booleanValue()) {
            if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
                this.stirPosition = 0;
            } else {
                this.stirPosition = 1;
            }
            setBackgroundColorSelect(this.stirPosition);
        }
        this.pickerShutter.autoScrollToPosition(1, 100, new LinearInterpolator());
        this.pickerDuration.autoScrollToPosition(1, 100, new LinearInterpolator());
    }

    private void initSystemLight() {
        this.rl_recording_long_light = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_recording_long_light);
        this.recording_long_light_switch = (SwitchButton) this.view.findViewById(C0853R.C0855id.recording_long_light_switch);
        this.recording_long_light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SPUtil.setParam(FVTimeLapseStandardPop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_YES));
                } else {
                    SPUtil.setParam(FVTimeLapseStandardPop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO));
                }
            }
        });
        if (((Integer) SPUtil.getParam(this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue() == 107770) {
            this.recording_long_light_switch.setChecked(false);
        } else {
            this.recording_long_light_switch.setChecked(true);
        }
        this.ll_recording_long_light_switch = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_recording_long_light_switch);
        this.ll_recording_long_light_switch.setVisibility(8);
        if (Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(this.context)) {
            SPUtil.setParam(this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_YES));
            this.recording_long_light_switch.setChecked(true);
            this.recording_long_light_switch.setClickable(false);
            this.ll_recording_long_light_switch.setVisibility(0);
            this.ll_recording_long_light_switch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    EventBusUtil.sendEvent(new Event(21));
                }
            });
        }
    }

    private void setBgRuleColorSelect(int select) {
        switch (select) {
            case 0:
                int mStartColorPs0 = this.context.getResources().getColor(C0853R.color.yellow_18);
                int mStartColorPd0 = this.context.getResources().getColor(C0853R.color.color_black4);
                int mEndColor0 = this.context.getResources().getColor(C0853R.color.color_light_gray);
                this.pickerShutter.setColor(mStartColorPs0, mEndColor0);
                this.pickerDuration.setColor(mStartColorPd0, mEndColor0);
                return;
            case 1:
                int mStartColorPs1 = this.context.getResources().getColor(C0853R.color.color_black4);
                int mStartColorPd1 = this.context.getResources().getColor(C0853R.color.yellow_18);
                int mEndColor1 = this.context.getResources().getColor(C0853R.color.color_light_gray);
                this.pickerShutter.setColor(mStartColorPs1, mEndColor1);
                this.pickerDuration.setColor(mStartColorPd1, mEndColor1);
                return;
            case 2:
                int mStartColorPs2 = this.context.getResources().getColor(C0853R.color.color_black4);
                int mStartColorPd2 = this.context.getResources().getColor(C0853R.color.color_black4);
                int mEndColor2 = this.context.getResources().getColor(C0853R.color.color_light_gray);
                this.pickerShutter.setColor(mStartColorPs2, mEndColor2);
                this.pickerDuration.setColor(mStartColorPd2, mEndColor2);
                return;
            default:
                return;
        }
    }

    private void setBackgroundColorSelect(int select) {
        switch (select) {
            case 0:
                this.layout_lapse_shutter_interval.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.layout_lapse_time_duration.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.rl_recording_long_light.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.btnStart.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 1:
                this.layout_lapse_shutter_interval.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.layout_lapse_time_duration.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.rl_recording_long_light.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.btnStart.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 2:
                this.layout_lapse_shutter_interval.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.layout_lapse_time_duration.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.rl_recording_long_light.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.btnStart.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 3:
                this.layout_lapse_shutter_interval.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.layout_lapse_time_duration.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.rl_recording_long_light.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.btnStart.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 4 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition--;
                        if (this.stirPosition < 0) {
                            this.stirPosition = 0;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        int pos = this.pickerShutter.getSelectedPosition() + 1;
                        if (pos > this.mData3.size() - 1) {
                            pos = this.mData3.size() - 1;
                        }
                        this.pickerShutter.autoScrollToPosition(pos, 300, new LinearInterpolator());
                        return;
                    } else if (this.stirPosition == 1) {
                        int pos2 = this.pickerDuration.getSelectedPosition() + 1;
                        if (pos2 > this.mData4.size() - 1) {
                            pos2 = this.mData4.size() - 1;
                        }
                        this.pickerDuration.autoScrollToPosition(pos2, 300, new LinearInterpolator());
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 4 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 3) {
                            this.stirPosition = 3;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        int pos3 = this.pickerShutter.getSelectedPosition() - 1;
                        if (pos3 < 0) {
                            pos3 = 0;
                        }
                        this.pickerShutter.autoScrollToPosition(pos3, 300, new LinearInterpolator());
                        return;
                    } else if (this.stirPosition == 1) {
                        int pos4 = this.pickerDuration.getSelectedPosition() - 1;
                        if (pos4 < 0) {
                            pos4 = 0;
                        }
                        this.pickerDuration.autoScrollToPosition(pos4, 300, new LinearInterpolator());
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (this.scaleSlide) {
                    this.scaleSlide = false;
                } else {
                    this.scaleSlide = true;
                }
                if (this.stirPosition == 0) {
                    if (this.scaleSlide) {
                        setBgRuleColorSelect(0);
                        return;
                    } else {
                        setBgRuleColorSelect(2);
                        return;
                    }
                } else if (this.stirPosition == 1) {
                    if (this.scaleSlide) {
                        setBgRuleColorSelect(1);
                        return;
                    } else {
                        setBgRuleColorSelect(2);
                        return;
                    }
                } else if (this.stirPosition == 2) {
                    this.scaleSlide = false;
                    if (this.recording_long_light_switch.isChecked()) {
                        this.recording_long_light_switch.setChecked(false);
                        return;
                    } else {
                        this.recording_long_light_switch.setChecked(true);
                        return;
                    }
                } else if (this.stirPosition == 3) {
                    this.scaleSlide = false;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVTimeLapseStandardPop.this.startTimeLapseStandardVideoOnClick();
                        }
                    }, 50);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 4) {
                    showMoveTimeLapseSmoothPop();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void startTimeLapseStandardVideoOnClick() {
        if (this.tvHint.getVisibility() == 0) {
            return;
        }
        if (this.isConnected) {
            MoveTimelapseUtil.getInstance();
            if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                EventBusUtil.sendEvent(new Event(153));
            } else if (Util.isPovReverTimeLapse(this.context)) {
                ViseLog.m1466e("StandardPop 标准模式各参数");
                double minute = Double.parseDouble(this.duration);
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(2);
                MoveTimelapseUtil.getInstance().setCameraProgressLinearTime((int) (minute * 60.0d));
                MoveTimelapseUtil.getInstance().setSelectShutter((double) Float.valueOf(this.shutter).floatValue());
                MoveTimelapseUtil.getInstance().selectModelCommunication(1, MoveTimelapseUtil.getInstance().getSelectSmoothness(), (int) (60.0d * minute));
                if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                    SPUtils.put(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                }
            }
        } else {
            Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
        }
    }

    private class OrientationBroadPopup extends BroadcastReceiver {
        private Message message;

        private OrientationBroadPopup() {
        }

        public void onReceive(Context context, Intent intent) {
            int orientation = intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1);
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVTimeLapseStandardPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVTimeLapseStandardPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVTimeLapseStandardPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVTimeLapseStandardPop.this.sendToHandler(13);
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
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 6;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 6;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(0);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = this.height;
        linearParams.width = (this.height * 2) / 3;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 6;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height;
        linearParams2.width = this.height / 6;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
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
        linearParams2.width = this.height / 6;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 6;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
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
        linearParams2.width = this.height / 6;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 6;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    private void initListener() {
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        this.pickerShutter.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String unused = FVTimeLapseStandardPop.this.shutter = (String) FVTimeLapseStandardPop.this.mData3.get(position);
                String videoDuration = FVTimeLapseStandardPop.this.calculateVideoTime(FVTimeLapseStandardPop.this.shutter, FVTimeLapseStandardPop.this.duration);
                FVTimeLapseStandardPop.this.tvVideoDuration.setText(Html.fromHtml(String.format(FVTimeLapseStandardPop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
            }
        });
        this.pickerDuration.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String videoDuration;
                String unused = FVTimeLapseStandardPop.this.duration = (String) FVTimeLapseStandardPop.this.mData4.get(position);
                if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
                    videoDuration = FVTimeLapseStandardPop.this.calculateVideoTime(FVTimeLapseStandardPop.this.shutter, FVTimeLapseStandardPop.this.duration);
                } else {
                    videoDuration = FVTimeLapseStandardPop.this.calculateVideoTimeTwo(FVTimeLapseStandardPop.this.duration);
                }
                FVTimeLapseStandardPop.this.tvVideoDuration.setText(Html.fromHtml(String.format(FVTimeLapseStandardPop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
            }
        });
        this.btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVTimeLapseStandardPop.this.showMoveTimeLapseSmoothPop();
            }
        });
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapseStandardPop.this.tvHint.getVisibility() == 0) {
                    return;
                }
                if (FVTimeLapseStandardPop.this.isConnected) {
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                        EventBusUtil.sendEvent(new Event(153));
                    } else if (Util.isPovReverTimeLapse(FVTimeLapseStandardPop.this.context)) {
                        ViseLog.m1466e("StandardPop 标准模式各参数");
                        double minute = Double.parseDouble(FVTimeLapseStandardPop.this.duration);
                        MoveTimelapseUtil.getInstance().setCameraProgressLinear(2);
                        MoveTimelapseUtil.getInstance().setCameraProgressLinearTime((int) (minute * 60.0d));
                        MoveTimelapseUtil.getInstance().setSelectShutter((double) Float.valueOf(FVTimeLapseStandardPop.this.shutter).floatValue());
                        MoveTimelapseUtil.getInstance().selectModelCommunication(1, MoveTimelapseUtil.getInstance().getSelectSmoothness(), (int) (60.0d * minute));
                        if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(FVTimeLapseStandardPop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                            SPUtils.put(FVTimeLapseStandardPop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                        }
                    }
                } else {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, C0853R.string.label_device_not_connected, 1).show();
                }
            }
        });
        MoveTimelapseUtil.getInstance().init(new MoveTimelapseUtil.MoveTimelapseListener() {
            public void isPtzDisconnected() {
                boolean unused = FVTimeLapseStandardPop.this.isConnected = false;
                MoveTimelapseUtil.getInstance().detroy();
                if (FVTimeLapseStandardPop.this.pop != null) {
                    FVTimeLapseStandardPop.this.pop.dismiss();
                }
            }

            public void isAddOrRemorePictrueOk(int pictrueCount, int addOrRemove, boolean outOfRange) {
            }

            public void isEveryStepTimeout(int failType, int addOrRemoveOrError) {
                if (failType == 1) {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, FVTimeLapseStandardPop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    ViseLog.m1466e("StandardPop 设置模式返回超时");
                } else if (failType == 2) {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, FVTimeLapseStandardPop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    ViseLog.m1466e("StandardPop 接受点数据总超时");
                } else if (failType == 3) {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, FVTimeLapseStandardPop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    ViseLog.m1466e("StandardPop 云台发起开始拍摄超时");
                } else if (failType == 4) {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, FVTimeLapseStandardPop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    ViseLog.m1466e("StandardPop 手动开启录像超时");
                    FVTimeLapseStandardPop.this.stopShootAndListener();
                } else if (failType == 5) {
                    Toast.makeText(FVTimeLapseStandardPop.this.context, FVTimeLapseStandardPop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    ViseLog.m1466e("StandardPop 自由模式发送总点数超时");
                }
            }

            public void isPtzSendDataComeon(int point, int pointTime) {
                ViseLog.m1466e("StandardPop 点数据 point:" + point + "pointTime:" + pointTime);
                if (MoveTimelapseUtil.getInstance().getSelectPictruePathList().size() == MoveTimelapseUtil.getInstance().getMap().size() + 1) {
                    List<String> mapValuesList = new ArrayList<>(MoveTimelapseUtil.getInstance().getMap().values());
                    for (int i = 0; i < mapValuesList.size(); i++) {
                        ViseLog.m1466e("StandardPop 所有点数据" + mapValuesList.get(i));
                    }
                    FVTimeLapseStandardPop.this.pointList.clear();
                    FVTimeLapseStandardPop.this.pointList.addAll(mapValuesList);
                    FVTimeLapseStandardPop.this.pointListStatic.addAll(FVTimeLapseStandardPop.this.pointList);
                    FVTimeLapseStandardPop.this.pointListStatic.add(0, "0");
                    MoveTimelapseUtil.getInstance().setPointLineStatic(FVTimeLapseStandardPop.this.pointListStatic);
                    FVTimeLapseStandardPop.this.showTimeLinePop();
                }
            }

            public void isPtzStartShootComeon() {
                ViseLog.m1466e("StandardPop 云台发起开始录像");
            }

            public void isPtzAckShootingComeon() {
                ViseLog.m1466e("StandardPop 云台应答正在录像");
            }

            public void isPtzShootEnd(int type) {
            }

            public void isPtzCancelShootSuccess(boolean exit) {
                ViseLog.m1466e("StandardPop 退出延时摄影");
            }
        });
    }

    /* access modifiers changed from: private */
    public void stopShootAndListener() {
        MoveTimelapseUtil.getInstance().detroy();
        timeDelayCloseLapseMedia("0");
    }

    private void initData() {
        String videoDuration;
        List<CharSequence> mData = new ArrayList<>();
        List<CharSequence> mData2 = new ArrayList<>();
        this.mData3 = new ArrayList();
        this.mData4 = new ArrayList();
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            mData.addAll(new ArrayList(Arrays.asList(new String[]{"0.25s", "0.5s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "20s", "30s", "45s", "60s"})));
            mData2.addAll(new ArrayList(Arrays.asList(new String[]{"1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "10m", "20m", "30m", "40m", "50m", "1h", "2h", "3h", "4h", "5h"})));
            this.mData3.addAll(new ArrayList(Arrays.asList(new String[]{"0.25", "0.5", BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "45", "60"})));
            this.mData4.addAll(new ArrayList(Arrays.asList(new String[]{BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "40", "50", "60", "120", "180", "240", "300"})));
        } else {
            mData.addAll(new ArrayList(Arrays.asList(new String[]{"0.5s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "20s", "30s", "45s", "60s"})));
            mData2.addAll(new ArrayList(Arrays.asList(new String[]{"10s", "30s", "1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "10m", "20m", "30m", "40m", "50m", "1h", "2h", "3h", "4h", "5h"})));
            this.mData3.addAll(new ArrayList(Arrays.asList(new String[]{"0.5", BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "45", "60"})));
            this.mData4.addAll(new ArrayList(Arrays.asList(new String[]{"0.17", "0.5", BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "40", "50", "60", "120", "180", "240", "300"})));
        }
        this.pickerShutter.setData(mData);
        this.pickerDuration.setData(mData2);
        if (this.isBacked) {
            this.shutter = String.valueOf(MoveTimelapseUtil.getInstance().getSelectShutter());
            this.duration = String.valueOf(MoveTimelapseUtil.getInstance().getSelectDuration() / 60);
            int shutterIndex = this.mData3.indexOf(this.shutter);
            int durationIndex = this.mData4.indexOf(this.duration);
            this.pickerShutter.setSelectedPosition(shutterIndex);
            this.pickerDuration.setSelectedPosition(durationIndex);
        }
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            videoDuration = calculateVideoTime(this.shutter, this.duration);
        } else {
            this.duration = "0.17";
            videoDuration = calculateVideoTimeTwo(this.duration);
        }
        this.tvVideoDuration.setText(Html.fromHtml(String.format(this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
    }

    /* access modifiers changed from: private */
    public String calculateVideoTime(String interval, String duration2) {
        float totaltime = (2.0f * Float.parseFloat(duration2)) / Float.parseFloat(interval);
        if (totaltime < 1.0f) {
            this.tvHint.setVisibility(0);
        } else {
            this.tvHint.setVisibility(8);
        }
        return Util.secToTime((int) totaltime);
    }

    /* access modifiers changed from: private */
    public String calculateVideoTimeTwo(String duration2) {
        float totaltime = Float.parseFloat(duration2) * 60.0f;
        if (totaltime < 1.0f) {
            this.tvHint.setVisibility(0);
        } else {
            this.tvHint.setVisibility(8);
        }
        return Util.secToTime((int) totaltime);
    }

    private void timeDelayCloseLapseMedia(String duration2) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (FVTimeLapseStandardPop.this.cameraManager != null) {
                    FVTimeLapseStandardPop.this.cameraManager.stopMediaRecord();
                }
            }
        }, (long) ((int) ((Double.parseDouble(duration2) * 1000.0d * 60.0d) + 900.0d)));
    }

    public void setPop(PopupWindow pop2, final FVTimeLapseStandardPop timeLapsePop) {
        this.pop = pop2;
        CameraUtils.setFrameLayerNumber(4);
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVTimeLapseStandardPop.this.context.unregisterReceiver(FVTimeLapseStandardPop.this.broad);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVTimeLapseStandardPop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                    EventBus.getDefault().unregister(timeLapsePop);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void showMoveTimeLapseSmoothPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVMoveTimelapseSmoothPop fvMoveTimelapseSmoothPop = new FVMoveTimelapseSmoothPop();
        fvMoveTimelapseSmoothPop.init(this.context, this.parentView, true);
        PopupWindow pop2 = new PopupWindow(fvMoveTimelapseSmoothPop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation2);
        pop2.setOutsideTouchable(false);
        fvMoveTimelapseSmoothPop.setPop(pop2, fvMoveTimelapseSmoothPop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    /* access modifiers changed from: private */
    public void showTimeLinePop() {
        Log.e("-----------------", "----------  7777  8888  9999   2019-03-04  end ------- FVTimeLapseStandardPop  OK键响应事件   响应事件   响应事件");
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVMoveTimelapseTimeLinePop timeLinePop = new FVMoveTimelapseTimeLinePop();
        timeLinePop.init(this.context, this.parentView);
        PopupWindow pop2 = new PopupWindow(timeLinePop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation2);
        pop2.setOutsideTouchable(false);
        timeLinePop.setPop(pop2, timeLinePop);
        timeLinePop.setData(MoveTimelapseUtil.getInstance().getSelectPictruePathList(), this.pointList);
        timeLinePop.setShutterAndDuration(this.shutter, this.duration);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public View getView() {
        return this.view;
    }
}
