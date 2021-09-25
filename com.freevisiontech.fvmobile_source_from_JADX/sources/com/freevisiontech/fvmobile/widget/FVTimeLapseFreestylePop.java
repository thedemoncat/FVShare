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
import android.widget.Toast;
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVKcfFreestyleActivity;
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
import com.freevisiontech.fvmobile.utils.LoadingView;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
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

public class FVTimeLapseFreestylePop {
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
    /* access modifiers changed from: private */
    public boolean isBack = false;
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
                    Log.e("------------", "--------收到消息 0-----");
                    FVTimeLapseFreestylePop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVTimeLapseFreestylePop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVTimeLapseFreestylePop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVTimeLapseFreestylePop.this.setHorUiNinety270();
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
    public PopupWindow pop;
    /* access modifiers changed from: private */
    public LoadingView progressDialog;
    private SwitchButton recording_long_light_switch;
    private RelativeLayout rl_recording_long_light;
    private boolean scaleSlide = false;
    /* access modifiers changed from: private */
    public String shutter = "0.25";
    private int smooothValue = 0;
    private int stirPosition = -1;
    TextView tvHint;
    TextView tvVideoDuration;
    private int type = -1;
    private View view;

    public void init(Context context2, View parentView2) {
        this.parentView = parentView2;
        this.context = context2;
        this.isBack = false;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_time_lapse_pop_new, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.tvHint = (TextView) this.view.findViewById(C0853R.C0855id.tv_hint);
        this.tvVideoDuration = (TextView) this.view.findViewById(C0853R.C0855id.tv_video_duration);
        this.btnStart = (Button) this.view.findViewById(C0853R.C0855id.btn_start);
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
        this.progressDialog = new LoadingView(context2);
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
                if (FVTimeLapseFreestylePop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapseFreestylePop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(false);
        CameraUtils.setMoveTimelapseRecording(true);
        CameraUtils.setFrameLayerNumber(2);
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
                    SPUtil.setParam(FVTimeLapseFreestylePop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_YES));
                } else {
                    SPUtil.setParam(FVTimeLapseFreestylePop.this.context, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO));
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
                FVTimeLapseFreestylePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVTimeLapseFreestylePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVTimeLapseFreestylePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVTimeLapseFreestylePop.this.sendToHandler(13);
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
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        this.pickerShutter.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String unused = FVTimeLapseFreestylePop.this.shutter = (String) FVTimeLapseFreestylePop.this.mData3.get(position);
                String videoDuration = FVTimeLapseFreestylePop.this.calculateVideoTime(FVTimeLapseFreestylePop.this.shutter, FVTimeLapseFreestylePop.this.duration);
                FVTimeLapseFreestylePop.this.tvVideoDuration.setText(Html.fromHtml(String.format(FVTimeLapseFreestylePop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
            }
        });
        this.pickerDuration.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                String videoDuration;
                String unused = FVTimeLapseFreestylePop.this.duration = (String) FVTimeLapseFreestylePop.this.mData4.get(position);
                if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
                    videoDuration = FVTimeLapseFreestylePop.this.calculateVideoTime(FVTimeLapseFreestylePop.this.shutter, FVTimeLapseFreestylePop.this.duration);
                } else {
                    videoDuration = FVTimeLapseFreestylePop.this.calculateVideoTimeTwo(FVTimeLapseFreestylePop.this.duration);
                }
                FVTimeLapseFreestylePop.this.tvVideoDuration.setText(Html.fromHtml(String.format(FVTimeLapseFreestylePop.this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
            }
        });
        this.btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = FVTimeLapseFreestylePop.this.isBack = true;
                Intent intent = new Intent(FVTimeLapseFreestylePop.this.context, FVKcfFreestyleActivity.class);
                intent.putExtra("enterorback", 2);
                intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, MoveTimelapseUtil.getInstance().getSelectPictruePathList());
                FVTimeLapseFreestylePop.this.context.startActivity(intent);
                if (FVTimeLapseFreestylePop.this.pop != null) {
                    FVTimeLapseFreestylePop.this.pop.dismiss();
                }
            }
        });
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVTimeLapseFreestylePop.this.tvHint.getVisibility() == 0) {
                    return;
                }
                if (FVTimeLapseFreestylePop.this.isConnected) {
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                        EventBusUtil.sendEvent(new Event(153));
                    } else if (Util.isPovReverTimeLapse(FVTimeLapseFreestylePop.this.context)) {
                        ViseLog.m1466e("FreeStyle 自由模式离散点.....各流程");
                        FVTimeLapseFreestylePop.this.progressDialog.setMessage(FVTimeLapseFreestylePop.this.context.getString(C0853R.string.label_time_lapse_free_style_data_tran));
                        FVTimeLapseFreestylePop.this.progressDialog.show();
                        FVTimeLapseFreestylePop.this.progressDialog.setCancelable(true);
                        FVTimeLapseFreestylePop.this.progressDialog.setCanceledOnTouchOutside(false);
                        double minute = Double.parseDouble(FVTimeLapseFreestylePop.this.duration);
                        MoveTimelapseUtil.getInstance().setCameraProgressLinear(3);
                        MoveTimelapseUtil.getInstance().setCameraProgressLinearTime((int) (minute * 60.0d));
                        MoveTimelapseUtil.getInstance().setSelectShutter((double) Float.valueOf(FVTimeLapseFreestylePop.this.shutter).floatValue());
                        MoveTimelapseUtil.getInstance().selectModelCommunication(2, 0, (int) (minute * 60.0d));
                        if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(FVTimeLapseFreestylePop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                            SPUtils.put(FVTimeLapseFreestylePop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                        }
                    }
                } else {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, C0853R.string.label_device_not_connected, 1).show();
                }
            }
        });
        MoveTimelapseUtil.getInstance().init(new MoveTimelapseUtil.MoveTimelapseListener() {
            public void isPtzDisconnected() {
                boolean unused = FVTimeLapseFreestylePop.this.isConnected = false;
                if (FVTimeLapseFreestylePop.this.progressDialog != null && FVTimeLapseFreestylePop.this.progressDialog.isShowing()) {
                    FVTimeLapseFreestylePop.this.progressDialog.dismiss();
                }
                MoveTimelapseUtil.getInstance().detroy();
                if (FVTimeLapseFreestylePop.this.pop != null && FVTimeLapseFreestylePop.this.pop.isShowing()) {
                    FVTimeLapseFreestylePop.this.pop.dismiss();
                }
                FVTimeLapseFreestylePop.this.stopShootAndListener();
            }

            public void isAddOrRemorePictrueOk(int pictrueCount, int addOrRemove, boolean outOfRange) {
            }

            public void isEveryStepTimeout(int failType, int addOrRemoveOrError) {
                if (FVTimeLapseFreestylePop.this.progressDialog != null) {
                    FVTimeLapseFreestylePop.this.progressDialog.dismiss();
                }
                if (failType == 1) {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, FVTimeLapseFreestylePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVTimeLapseFreestylePop.this.pop != null) {
                        FVTimeLapseFreestylePop.this.pop.dismiss();
                    }
                } else if (failType == 2) {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, FVTimeLapseFreestylePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVTimeLapseFreestylePop.this.pop != null) {
                        FVTimeLapseFreestylePop.this.pop.dismiss();
                    }
                } else if (failType == 3) {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, FVTimeLapseFreestylePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVTimeLapseFreestylePop.this.pop != null) {
                        MoveTimelapseUtil.getInstance().detroy();
                        FVTimeLapseFreestylePop.this.pop.dismiss();
                    }
                } else if (failType == 4) {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, FVTimeLapseFreestylePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVTimeLapseFreestylePop.this.pop != null) {
                        FVTimeLapseFreestylePop.this.pop.dismiss();
                    }
                    ViseLog.m1466e("手动开启录像超时");
                    FVTimeLapseFreestylePop.this.stopShootAndListener();
                } else if (failType == 5) {
                    Toast.makeText(FVTimeLapseFreestylePop.this.context, FVTimeLapseFreestylePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVTimeLapseFreestylePop.this.pop != null) {
                        MoveTimelapseUtil.getInstance().detroy();
                        FVTimeLapseFreestylePop.this.pop.dismiss();
                    }
                }
            }

            public void isPtzSendDataComeon(int point, int pointTime) {
                FVTimeLapseFreestylePop.this.pointList.add(point, pointTime + "");
                ViseLog.m1466e("point:" + point + "pointTime:" + pointTime);
                if (MoveTimelapseUtil.getInstance().getSelectPictruePathList().size() == FVTimeLapseFreestylePop.this.pointList.size() + 1) {
                    FVTimeLapseFreestylePop.this.progressDialog.dismiss();
                }
            }

            public void isPtzStartShootComeon() {
                ViseLog.m1466e("云台发起开始录像");
                if (FVTimeLapseFreestylePop.this.progressDialog != null) {
                    FVTimeLapseFreestylePop.this.progressDialog.dismiss();
                }
                if (FVTimeLapseFreestylePop.this.isConnected) {
                    Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = true;
                    FVTimeLapseFreestylePop.this.startTimeLapseMeida(FVTimeLapseFreestylePop.this.shutter);
                    FVTimeLapseFreestylePop.this.timeDelayCloseLapseMedia(FVTimeLapseFreestylePop.this.duration);
                    MoveTimelapseUtil.getInstance().startShoot();
                }
                if (FVTimeLapseFreestylePop.this.pop != null) {
                    FVTimeLapseFreestylePop.this.pop.dismiss();
                }
            }

            public void isPtzAckShootingComeon() {
                ViseLog.m1466e("云台应答正在录像");
            }

            public void isPtzShootEnd(int type) {
                ViseLog.m1466e("录像异常结束,退出" + type);
                if (type == 1) {
                    FVTimeLapseFreestylePop.this.stopShootAndListener();
                } else if (type == 2) {
                    FVTimeLapseFreestylePop.this.stopShootAndListener();
                } else if (type == 3) {
                    MoveTimelapseUtil.getInstance().cancelShoot();
                    FVTimeLapseFreestylePop.this.stopShootAndListener();
                }
                if (FVTimeLapseFreestylePop.this.progressDialog != null) {
                    FVTimeLapseFreestylePop.this.progressDialog.dismiss();
                }
                if (FVTimeLapseFreestylePop.this.pop != null) {
                    FVTimeLapseFreestylePop.this.pop.dismiss();
                }
            }

            public void isPtzCancelShootSuccess(boolean exit) {
                ViseLog.m1466e("FVTimeLapsePop 退出延时摄影");
            }
        });
    }

    /* access modifiers changed from: private */
    public void stopShootAndListener() {
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
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            videoDuration = calculateVideoTime(this.shutter, this.duration);
        } else {
            this.duration = "0.17";
            videoDuration = calculateVideoTimeTwo(this.duration);
        }
        this.tvVideoDuration.setText(Html.fromHtml(String.format(this.context.getString(C0853R.string.label_produce_video_time), new Object[]{videoDuration}).replaceAll(videoDuration, "<font color='#555555'>" + videoDuration + "</font>")));
    }

    /* access modifiers changed from: private */
    public void startTimeLapseMeida(String interval) {
        String path;
        int qulity;
        float fInterval = Float.parseFloat(interval);
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            path = Util.getOutputMoveTimeLapseMediaFile(3, this.context).getPath();
        } else {
            path = Util.getOutputMoveLapseMediaFile(3, this.context).getPath();
        }
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
            qulity = CameraUtils.getCheckMediaRecordFrontSize();
        } else {
            qulity = CameraUtils.getCheckMediaRecordSize();
        }
        if (!MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.cameraManager.startMediaRecordEx(path, qulity, orientation);
        } else if (this.cameraManager.getCameraManagerType() == 1) {
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
    public String calculateVideoTimeTwo(String duration2) {
        float totaltime = Float.parseFloat(duration2) * 60.0f;
        if (totaltime < 1.0f) {
            this.tvHint.setVisibility(0);
        } else {
            this.tvHint.setVisibility(8);
        }
        return Util.secToTime((int) totaltime);
    }

    /* access modifiers changed from: private */
    public void timeDelayCloseLapseMedia(String duration2) {
        Util.sendIntEventMessge((int) Constants.MOVE_OR_DELAY_TIMELAPSE_COUNTDOWN_END, String.valueOf((int) ((Double.parseDouble(duration2) * 1000.0d * 60.0d) + 900.0d)));
    }

    public void setPop(PopupWindow pop2, final FVTimeLapseFreestylePop timeLapsePop) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ViseLog.m1466e("自由模式销毁 666 ");
                    if (FVTimeLapseFreestylePop.this.cameraManager == null || FVTimeLapseFreestylePop.this.cameraManager.isMediaRecording() || !FVTimeLapseFreestylePop.this.isBack) {
                    }
                    FVTimeLapseFreestylePop.this.context.unregisterReceiver(FVTimeLapseFreestylePop.this.broad);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVTimeLapseFreestylePop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                    EventBus.getDefault().unregister(timeLapsePop);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.pop != null) {
                    Util.sendIntEventMessge(10008);
                    CameraUtils.setMoveTimelapseIng(false);
                    this.pop.dismiss();
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
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVTimeLapseFreestylePop.this.startTimeLapseFreestyleVideoOnClick();
                        }
                    }, 50);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    this.isBack = true;
                    Intent intent = new Intent(this.context, FVKcfFreestyleActivity.class);
                    intent.putExtra("enterorback", 2);
                    intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, MoveTimelapseUtil.getInstance().getSelectPictruePathList());
                    this.context.startActivity(intent);
                    if (this.pop != null) {
                        this.pop.dismiss();
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    if (ViseBluetooth.getInstance().isConnected()) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setMoveTimelapseRecording(false);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void startTimeLapseFreestyleVideoOnClick() {
        if (this.tvHint.getVisibility() == 0) {
            return;
        }
        if (this.isConnected) {
            MoveTimelapseUtil.getInstance();
            if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                EventBusUtil.sendEvent(new Event(153));
            } else if (Util.isPovReverTimeLapse(this.context)) {
                ViseLog.m1466e("FreeStyle 自由模式离散点.....各流程");
                this.progressDialog.setMessage(this.context.getString(C0853R.string.label_time_lapse_free_style_data_tran));
                this.progressDialog.show();
                this.progressDialog.setCancelable(true);
                this.progressDialog.setCanceledOnTouchOutside(false);
                double minute = Double.parseDouble(this.duration);
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(3);
                MoveTimelapseUtil.getInstance().setCameraProgressLinearTime((int) (minute * 60.0d));
                MoveTimelapseUtil.getInstance().setSelectShutter((double) Float.valueOf(this.shutter).floatValue());
                MoveTimelapseUtil.getInstance().selectModelCommunication(2, 0, (int) (minute * 60.0d));
                if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                    SPUtils.put(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                }
            }
        } else {
            Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
        }
    }

    public View getView() {
        return this.view;
    }
}
