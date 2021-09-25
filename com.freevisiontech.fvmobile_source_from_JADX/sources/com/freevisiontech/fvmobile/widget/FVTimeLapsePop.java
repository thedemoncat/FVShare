package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.view.ScrollPickerView;
import com.freevisiontech.fvmobile.widget.view.StringScrollPicker;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;

public class FVTimeLapsePop {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    Button btnStart;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public String duration = BleConstant.SHUTTER;
    private int height;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    private LinearLayout layout_lapse_shutter_interval;
    private RelativeLayout layout_lapse_time_duration;
    private LinearLayout ll_recording_long_light_switch;
    /* access modifiers changed from: private */
    public List<CharSequence> mData3;
    /* access modifiers changed from: private */
    public List<CharSequence> mData4;
    private SwitchButton mTripodSwitch;
    /* access modifiers changed from: private */
    public boolean misTripodCanUse = false;
    private RelativeLayout msTripodRl;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVTimeLapsePop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVTimeLapsePop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVTimeLapsePop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVTimeLapsePop.this.setHorUiNinety270();
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
    public PopupWindow pop;
    private LoadingView progressDialog;
    private SwitchButton recording_long_light_switch;
    private RelativeLayout rl_recording_long_light;
    private boolean scaleSlide = false;
    /* access modifiers changed from: private */
    public String shutter = "0.25";
    private int stirPosition = -1;
    TextView tvHint;
    TextView tvVideoDuration;
    private View view;

    public void init(Context context2, View parentView2) {
        this.parentView = parentView2;
        this.context = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_time_lapse_pop_new, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.tvHint = (TextView) this.view.findViewById(C0853R.C0855id.tv_hint);
        this.tvVideoDuration = (TextView) this.view.findViewById(C0853R.C0855id.tv_video_duration);
        this.btnStart = (Button) this.view.findViewById(C0853R.C0855id.btn_start);
        this.pickerShutter = (StringScrollPicker) this.view.findViewById(C0853R.C0855id.picker_shutter);
        this.pickerDuration = (StringScrollPicker) this.view.findViewById(C0853R.C0855id.picker_duration);
        this.progressDialog = new LoadingView(context2);
        this.mTripodSwitch = (SwitchButton) this.view.findViewById(C0853R.C0855id.tripod_switch);
        this.msTripodRl = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_tripod_model);
        checkTripodCanUse();
        initData();
        initSystemLight();
        initListener();
        this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 30.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        TextView layout_time_lapse_pop_new_title = (TextView) this.view.findViewById(C0853R.C0855id.layout_time_lapse_pop_new_title);
        if (MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
            layout_time_lapse_pop_new_title.setText(C0853R.string.label_delay_video_static);
        } else {
            layout_time_lapse_pop_new_title.setText(C0853R.string.label_delay_video_dynamic);
        }
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
                if (FVTimeLapsePop.this.pop != null) {
                    FVTimeLapsePop.this.pop.dismiss();
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapsePop.this.pop != null) {
                    FVTimeLapsePop.this.pop.dismiss();
                }
            }
        });
        CameraUtils.setFrameLayerNumber(2);
        this.layout_lapse_shutter_interval = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_lapse_time_visible_or_gone);
        this.layout_lapse_time_duration = (RelativeLayout) this.view.findViewById(C0853R.C0855id.layout_lapse_time_duration);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirPosition = 0;
            if (this.stirPosition == 0) {
                setBackgroundColorSelect(this.stirPosition);
            }
        }
        CameraUtils.setIsBooleanTimeLapseUIShow(true);
        this.pickerShutter.autoScrollToPosition(1, 100, new LinearInterpolator());
        this.pickerDuration.autoScrollToPosition(1, 100, new LinearInterpolator());
    }

    private void initSystemLight() {
        this.rl_recording_long_light = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_recording_long_light);
        this.recording_long_light_switch = (SwitchButton) this.view.findViewById(C0853R.C0855id.recording_long_light_switch);
        this.recording_long_light_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    SPUtil.setParam(FVTimeLapsePop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_YES));
                } else {
                    SPUtil.setParam(FVTimeLapsePop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO));
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
                FVTimeLapsePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVTimeLapsePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVTimeLapsePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVTimeLapsePop.this.sendToHandler(13);
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.pickerShutter.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String unused = FVTimeLapsePop.this.shutter = (String) FVTimeLapsePop.this.mData3.get(position);
                String videoDuration = FVTimeLapsePop.this.calculateVideoTime(FVTimeLapsePop.this.shutter, FVTimeLapsePop.this.duration);
                String msg = String.format(FVTimeLapsePop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>");
                if (Integer.valueOf(FVTimeLapsePop.this.duration).intValue() == 99999) {
                    FVTimeLapsePop.this.tvVideoDuration.setText(C0853R.string.label_time_lapse_time_max_text);
                } else {
                    FVTimeLapsePop.this.tvVideoDuration.setText(Html.fromHtml(msg));
                }
                CameraUtils.setTimeLapseDuration(Integer.valueOf(FVTimeLapsePop.this.duration).intValue());
            }
        });
        this.pickerDuration.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String unused = FVTimeLapsePop.this.duration = (String) FVTimeLapsePop.this.mData4.get(position);
                String videoDuration = FVTimeLapsePop.this.calculateVideoTime(FVTimeLapsePop.this.shutter, FVTimeLapsePop.this.duration);
                String msg = String.format(FVTimeLapsePop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>");
                if (Integer.valueOf(FVTimeLapsePop.this.duration).intValue() == 99999) {
                    FVTimeLapsePop.this.tvVideoDuration.setText(C0853R.string.label_time_lapse_time_max_text);
                } else {
                    FVTimeLapsePop.this.tvVideoDuration.setText(Html.fromHtml(msg));
                }
                CameraUtils.setTimeLapseDuration(Integer.valueOf(FVTimeLapsePop.this.duration).intValue());
            }
        });
        this.btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapsePop.this.pop != null) {
                    FVTimeLapsePop.this.pop.dismiss();
                }
                Util.sendIntEventMessge(10008);
            }
        });
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapsePop.this.tvHint.getVisibility() != 0) {
                    boolean unused = FVTimeLapsePop.this.misTripodCanUse = true;
                    if (MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                        BleByteUtil.setPTZParameters((byte) 55, (byte) 1, (byte) 1);
                        SPUtils.put(FVTimeLapsePop.this.context, SharePrefConstant.TRIPOD_MODE_SWITCH, true);
                    } else {
                        BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 1);
                        SPUtils.put(FVTimeLapsePop.this.context, SharePrefConstant.TRIPOD_MODE_SWITCH, false);
                    }
                    if (FVTimeLapsePop.this.cameraManager.getCameraManagerType() == 2) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 2);
                            }
                        }, 200);
                    } else {
                        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 2);
                    }
                    int minute = Integer.parseInt(FVTimeLapsePop.this.duration);
                    MoveTimelapseUtil.getInstance().setCameraProgressLinear(1);
                    MoveTimelapseUtil.getInstance().setCameraProgressLinearTime(minute * 60);
                    FVTimeLapsePop.this.startTimeLapseMeida(FVTimeLapsePop.this.shutter);
                    FVTimeLapsePop.this.timeDelayCloseLapseMedia(FVTimeLapsePop.this.duration);
                    if (FVTimeLapsePop.this.pop != null) {
                        FVTimeLapsePop.this.pop.dismiss();
                    }
                    CameraUtils.setTimelapseIng(true);
                    MoveTimelapseUtil.getInstance();
                    MoveTimelapseUtil.setCameraVideoSymbolStart(0);
                    if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(FVTimeLapsePop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                        SPUtils.put(FVTimeLapsePop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                    }
                }
            }
        });
    }

    private void startTimeLapseVideoOnClick() {
        if (this.tvHint.getVisibility() != 0) {
            this.myHandler.postDelayed(new Runnable() {
                public void run() {
                    boolean unused = FVTimeLapsePop.this.misTripodCanUse = true;
                    if (MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                        BleByteUtil.setPTZParameters((byte) 55, (byte) 1, (byte) 1);
                        SPUtils.put(FVTimeLapsePop.this.context, SharePrefConstant.TRIPOD_MODE_SWITCH, true);
                        return;
                    }
                    BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 1);
                    SPUtils.put(FVTimeLapsePop.this.context, SharePrefConstant.TRIPOD_MODE_SWITCH, false);
                }
            }, 100);
            if (this.cameraManager.getCameraManagerType() == 2) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 2);
                    }
                }, 200);
            } else {
                this.myHandler.postDelayed(new Runnable() {
                    public void run() {
                        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 2);
                    }
                }, 50);
            }
            int minute = Integer.parseInt(this.duration);
            MoveTimelapseUtil.getInstance().setCameraProgressLinear(1);
            MoveTimelapseUtil.getInstance().setCameraProgressLinearTime(minute * 60);
            startTimeLapseMeida(this.shutter);
            timeDelayCloseLapseMedia(this.duration);
            if (this.pop != null) {
                this.pop.dismiss();
            }
            CameraUtils.setTimelapseIng(true);
            MoveTimelapseUtil.getInstance();
            MoveTimelapseUtil.setCameraVideoSymbolStart(0);
            if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                SPUtils.put(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
            }
        }
    }

    private void checkTripodCanUse() {
        boolean gmuVersionHigher;
        boolean gmuVersionHigher2;
        boolean gmuVersionHigher3;
        boolean z = true;
        boolean connected = ViseBluetooth.getInstance().isConnected();
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        String gmuFirmwareVersion = BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING;
        String imuFirmwareVersion = BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING;
        if (!connected) {
            this.misTripodCanUse = false;
        } else if (gmuFirmwareVersion == null || imuFirmwareVersion == null) {
            this.misTripodCanUse = false;
        } else {
            boolean gmuVersionHigher4 = true;
            boolean imuVersionHigher = true;
            if (ptzType.equals("")) {
                if (gmuFirmwareVersion.compareTo("01.00.00.38") > 0) {
                    gmuVersionHigher4 = true;
                } else {
                    gmuVersionHigher4 = false;
                }
                if (imuFirmwareVersion.compareTo("01.00.00.55") > 0) {
                    imuVersionHigher = true;
                } else {
                    imuVersionHigher = false;
                }
            } else if (ptzType.equals(BleConstant.FM_300)) {
                if (gmuFirmwareVersion.compareTo("00.00.02.08") > 0) {
                    gmuVersionHigher = true;
                } else {
                    gmuVersionHigher = false;
                }
                if (imuFirmwareVersion.compareTo("02.00.00.04") > 0) {
                    imuVersionHigher = true;
                } else {
                    imuVersionHigher = false;
                }
            } else if (ptzType.equals(BleConstant.FM_210)) {
                gmuVersionHigher4 = true;
                imuVersionHigher = true;
            }
            if (CameraUtils.getCurrentPageIndex() == 0) {
                if (gmuFirmwareVersion.compareTo("01.00.00.38") > 0) {
                    gmuVersionHigher3 = true;
                } else {
                    gmuVersionHigher3 = false;
                }
                if (imuFirmwareVersion.compareTo("01.00.00.55") > 0) {
                    imuVersionHigher = true;
                } else {
                    imuVersionHigher = false;
                }
            } else if (CameraUtils.getCurrentPageIndex() == 1) {
                if (gmuFirmwareVersion.compareTo("00.00.02.08") > 0) {
                    gmuVersionHigher2 = true;
                } else {
                    gmuVersionHigher2 = false;
                }
                if (imuFirmwareVersion.compareTo("02.00.00.04") > 0) {
                    imuVersionHigher = true;
                } else {
                    imuVersionHigher = false;
                }
            } else if (CameraUtils.getCurrentPageIndex() == 2) {
                gmuVersionHigher4 = true;
                imuVersionHigher = true;
            }
            if (!gmuVersionHigher4 || !imuVersionHigher) {
                z = false;
            }
            this.misTripodCanUse = z;
        }
        if (this.misTripodCanUse) {
            this.msTripodRl.setVisibility(8);
            return;
        }
        this.msTripodRl.setVisibility(8);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.tvHint.getLayoutParams());
        lp.setMargins(0, 10, 0, 0);
        lp.setMarginStart(this.context.getResources().getDimensionPixelSize(C0853R.dimen.time_lapse_pop_tv_hint_margin_start));
        lp.setMarginEnd(this.context.getResources().getDimensionPixelSize(C0853R.dimen.time_lapse_pop_tv_hint_margin_end));
        this.tvHint.setLayoutParams(lp);
    }

    private void initData() {
        List<CharSequence> mData = new ArrayList<>();
        List<CharSequence> mData2 = new ArrayList<>();
        this.mData3 = new ArrayList();
        this.mData4 = new ArrayList();
        mData.addAll(new ArrayList(Arrays.asList(new String[]{"0.25s", "0.5s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "20s", "30s", "45s", "60s"})));
        mData2.addAll(new ArrayList(Arrays.asList(new String[]{"1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "10m", "20m", "30m", "40m", "50m", "1h", "2h", "3h", "4h", "5h", "M"})));
        this.mData3.addAll(new ArrayList(Arrays.asList(new String[]{"0.25", "0.5", BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "45", "60"})));
        this.mData4.addAll(new ArrayList(Arrays.asList(new String[]{BleConstant.SHUTTER, BleConstant.ISO, BleConstant.f1095WB, BleConstant.FOCUS, "5", "6", "7", "8", "9", "10", "20", "30", "40", "50", "60", "120", "180", "240", "300", "99999"})));
        this.pickerShutter.setData(mData);
        this.pickerDuration.setData(mData2);
        String videoDuration = calculateVideoTime(this.shutter, this.duration);
        String msg = String.format(this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>");
        if (Integer.valueOf(this.duration).intValue() == 99999) {
            this.tvVideoDuration.setText(C0853R.string.label_time_lapse_time_max_text);
        } else {
            this.tvVideoDuration.setText(Html.fromHtml(msg));
        }
        CameraUtils.setTimeLapseDuration(Integer.valueOf(this.duration).intValue());
    }

    /* access modifiers changed from: private */
    public void startTimeLapseMeida(String interval) {
        float fInterval = Float.parseFloat(interval);
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        String path = Util.getOutputMoveTimeLapseMediaFile(3, this.context).getPath();
        CameraUtils.setMoveOrDelayTimeLapsePath(path);
        CameraUtils.setMoveOrDelayTimeLapseIng(true);
        CameraUtils.setMoveOrDelayTimeLapseShutter(fInterval);
        if (((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        if (((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            int checkMediaRecordFrontSize = CameraUtils.getCheckMediaRecordFrontSize();
        } else {
            int checkMediaRecordSize = CameraUtils.getCheckMediaRecordSize();
        }
        if (this.cameraManager.getCameraManagerType() == 1) {
            if (((Integer) SPUtils.get(this.context, SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
                this.cameraManager.startTimeLapseMediaRecordEx(path, 1001, orientation, (double) fInterval);
            } else {
                this.cameraManager.startTimeLapseMediaRecordEx(path, 1000, orientation, (double) fInterval);
            }
        } else if (((Integer) SPUtils.get(this.context, SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
            this.cameraManager.startTimeLapseMediaRecordEx(path, 1001, orientation, (double) fInterval);
        } else {
            this.cameraManager.startTimeLapseMediaRecordEx(path, 1000, orientation, (double) fInterval);
        }
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
    public void timeDelayCloseLapseMedia(String duration2) {
        CameraUtils.setTimeLapseDuration(Integer.valueOf(duration2).intValue());
        Util.sendIntEventMessge((int) Constants.MOVE_OR_DELAY_TIMELAPSE_COUNTDOWN_END, String.valueOf((Integer.valueOf(duration2).intValue() * 1000 * 60) + IMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR));
    }

    public void setPop(PopupWindow pop2, final FVTimeLapsePop timeLapsePop) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    CameraUtils.setIsBooleanTimeLapseUIShow(false);
                    FVTimeLapsePop.this.context.unregisterReceiver(FVTimeLapsePop.this.broad);
                    EventBus.getDefault().unregister(timeLapsePop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        CameraUtils.setFrameLayerNumber(0);
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    if (ViseBluetooth.getInstance().isConnected()) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setMoveTimelapseRecording(false);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
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
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
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
                    startTimeLapseVideoOnClick();
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                    Util.sendIntEventMessge(10008);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public View getView() {
        return this.view;
    }
}
