package com.freevisiontech.fvmobile.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.camera2.DngCreator;
import android.hardware.camera2.params.RggbChannelVector;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.app.Fragment;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.cameralib.FVCamereManagerCallback;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Manager;
import com.freevisiontech.cameralib.impl.Camera2.CameraParameters;
import com.freevisiontech.cameralib.view.CameraView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utility.BackgroundMusic;
import com.freevisiontech.fvmobile.utility.CameraExclusiveUtils;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.FVCountTimer;
import com.freevisiontech.fvmobile.widget.FVHitchCockCountTimer;
import com.freevisiontech.fvmobile.widget.FVMarkPointChangeSettingPop;
import com.freevisiontech.fvmobile.widget.FVProgressCountTimer;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewEV;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewIsoTwo;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewMf;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewShutter;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewWT;
import com.freevisiontech.fvmobile.widget.scale.BaseScaleViewWbTwo;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewEV;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewIsoTwo;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewMf;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewShutter;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewWT;
import com.freevisiontech.fvmobile.widget.scale.HorizontalScaleScrollViewWbTwo;
import com.freevisiontech.fvmobile.widget.view.CustomSurfaceView;
import com.freevisiontech.tracking.FVTrackObserver;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.lzy.okgo.utils.HttpUtils;
import com.p007ny.ijk.upplayer.BuildConfig;
import com.vise.log.ViseLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVContentFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    public static final int Progress_Rotation_0 = 0;
    public static final int Progress_Rotation_180 = 180;
    public static final int Progress_Rotation_270 = 270;
    public static final int Progress_Rotation_90 = 90;
    private static final int REQUEST_AUDIO_PERMISSION = 26;
    private static final int REQUEST_CAMERA_PERMISSION = 25;
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    public static final int SCALE_SCROLL_REVOLVE = 400;
    public static final int SHADOW_MODE = 300;
    private final int GONE_BRIGHTNESS = 16;
    private final int GONE_BRIGHTNESS_EV = 19;
    private final int GONE_BRIGHTNESS_NONE = 14;
    private final int GONE_FOCAL_LENGTH = 10;
    private final int GONE_RL_FOCUS = 12;
    private final int GONE_SETTING_MARK_MF = 57;
    private final int HAND_MODEL_BG_COLOR_WHITE = 59;
    private final int HAND_MODEL_BG_COLOR_YELLOW = 61;
    private final int LOCK_AUTO_EXPOSURE = 9;
    private final int PARAMETER_DISPLAY_GET_EV = 36;
    private final int PARAMETER_DISPLAY_GET_ISO = 38;
    private final int PARAMETER_DISPLAY_GET_MF = 41;
    private final int PARAMETER_DISPLAY_GET_SHUTTER = 37;
    private final int PARAMETER_DISPLAY_GET_WB = 39;
    private final int PARAMETER_DISPLAY_GET_WT = 42;
    private final int SCALE_SCROLL_GET_ISO = 32;
    private final int SCALE_SCROLL_GET_MF = 34;
    private final int SCALE_SCROLL_GET_SHUTTER = 31;
    private final int SCALE_SCROLL_GET_WB = 33;
    private final int SCALE_SCROLL_ISO_START = 35;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_EV = 51;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_ISO = 53;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_MF = 55;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_SHUTTER = 52;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_WB = 54;
    private final int SCALE_SCROLL_VIEW_BLUE_VISIBLE_WT = 56;
    private final int SEND_TO_HANDLER_MIX_ONE = 81;
    private final int SEND_TO_HANDLER_MIX_TWO = 82;
    private final int SEND_TO_HANDLER_ONE = 71;
    private final int SEND_TO_HANDLER_TWO = 72;
    private final int TAKE_NONE_PHOTO = 13;
    /* access modifiers changed from: private */
    public ObjectAnimator alpha;
    /* access modifiers changed from: private */
    public int angle;
    private int angleScrollScale;
    private OrientationBroad broad;
    @Bind({2131755749})
    TextView btnFullShotCancel;
    @Bind({2131755658})
    ImageView btnManualFocus;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    private CameraZoomExpoThread cameraZoomExpoThread;
    private CameraZoomThread cameraZoomThread;
    private boolean camera_hand_model = false;
    @Bind({2131755728})
    TextView chro_all_timer;
    @Bind({2131755726})
    Chronometer chro_timer;
    @Bind({2131755651})
    RelativeLayout contentLayout;
    @Bind({2131755746})
    RelativeLayout content_layout_parameter_display;
    @Bind({2131755745})
    RelativeLayout content_layout_scale;
    private FVCountTimer countTimer;
    /* access modifiers changed from: private */
    public int curBright = 0;
    private int curBrightOld;
    /* access modifiers changed from: private */
    public int curBrightProgress = 50;
    private int curBrightScale;
    private int curBrightScaleOld;
    private int curFocusLength = 0;
    int curScrollScaleEV;
    int curScrollScaleEVOld;
    int curScrollScaleGetISO;
    int curScrollScaleGetISOOld;
    int curScrollScaleGetWB;
    int curScrollScaleGetWBOld;
    int curScrollScaleISO;
    int curScrollScaleISOOld;
    int curScrollScaleMf;
    int curScrollScaleMfOld;
    int curScrollScaleShutter;
    int curScrollScaleShutterOld;
    int curScrollScaleWB;
    int curScrollScaleWBOld;
    int curScrollScaleWT;
    int curScrollScaleWTOld;
    /* access modifiers changed from: private */
    public CustomSurfaceView customSurfaceView;
    private float downX;
    private float downX2;
    private float downY;
    private float downY2;
    Handler expoHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 18:
                    FVContentFragment.this.sendToHandlerExpoMix(17, Integer.valueOf(((Integer) msg.obj).intValue()));
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler expoMixHandler;
    /* access modifiers changed from: private */
    public int focusStatus = 0;
    private LinearLayout frag_content_parameter_display_linear;
    /* access modifiers changed from: private */
    public LinearLayout frag_content_scale_linear;
    /* access modifiers changed from: private */
    public ImageView frag_content_scale_linear_image_left;
    /* access modifiers changed from: private */
    public ImageView frag_content_scale_linear_image_right;
    /* access modifiers changed from: private */
    public LinearLayout frag_content_scale_linear_place_buttom;
    /* access modifiers changed from: private */
    public LinearLayout frag_content_scale_linear_place_top;
    @Bind({2131755710})
    LinearLayout fragment_content_two_ll;
    private int ftProgressOld;
    /* access modifiers changed from: private */
    public boolean gradienterColorYellow = false;
    /* access modifiers changed from: private */
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Range<Long> range;
            Range<Long> range2;
            long ranMax;
            switch (msg.what) {
                case 0:
                    FVContentFragment.this.llSeekbarInside.setRotation(0.0f);
                    FVContentFragment.this.rlBright.setVisibility(0);
                    FVContentFragment.this.rlBright2.setVisibility(8);
                    if (FVContentFragment.this.mStartAngle == -270) {
                        FVContentFragment.this.rotateView(-360, FVContentFragment.this.tvTimer, FVContentFragment.this.rlFocus, FVContentFragment.this.ll_start_video_recording_resolution_fps, FVContentFragment.this.ll_setting_x_mf);
                    } else {
                        FVContentFragment.this.rotateView(0, FVContentFragment.this.tvTimer, FVContentFragment.this.rlFocus, FVContentFragment.this.ll_start_video_recording_resolution_fps, FVContentFragment.this.ll_setting_x_mf);
                    }
                    if (FVContentFragment.this.viewScale != null) {
                        int left = (FVContentFragment.this.widthPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        int top = (FVContentFragment.this.heightPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) - Util.dip2px(FVContentFragment.this.mContext, 22.0f);
                        RelativeLayout.LayoutParams lpsScale = (RelativeLayout.LayoutParams) FVContentFragment.this.content_layout_scale.getLayoutParams();
                        lpsScale.setMargins(left, top, 0, 0);
                        FVContentFragment.this.content_layout_scale.setLayoutParams(lpsScale);
                        LinearLayout unused = FVContentFragment.this.frag_content_scale_linear = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                        FVContentFragment.this.frag_content_scale_linear.setRotation(0.0f);
                        LinearLayout unused2 = FVContentFragment.this.frag_content_scale_linear_place_top = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                        LinearLayout unused3 = FVContentFragment.this.frag_content_scale_linear_place_buttom = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                        FVContentFragment.this.frag_content_scale_linear_place_top.setVisibility(0);
                        FVContentFragment.this.frag_content_scale_linear_place_buttom.setVisibility(8);
                    }
                    if (FVContentFragment.this.ll_mark_point.getVisibility() == 0 || FVContentFragment.this.ll_mark_point_vertical.getVisibility() == 0) {
                        FVContentFragment.this.ll_mark_point.setVisibility(0);
                        FVContentFragment.this.ll_mark_point_vertical.setVisibility(8);
                        FVContentFragment.this.ll_mark_point.setRotation(0.0f);
                    }
                    if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                        FVContentFragment.this.timer_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_size_all_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_video_record_start_linear_horizontal.setRotation(0.0f);
                        FVContentFragment.this.timer.setVisibility(8);
                        FVContentFragment.this.timer_size_all.setVisibility(8);
                        FVContentFragment.this.timer_size_all_view.setVisibility(8);
                        return;
                    }
                    return;
                case 9:
                    if (FVContentFragment.this.cameraManager != null) {
                        FVContentFragment.this.cameraManager.lockAutoExposure(true);
                        return;
                    }
                    return;
                case 10:
                    if (FVContentFragment.this.llSeekbar != null) {
                        FVContentFragment.this.llSeekbar.setVisibility(8);
                        return;
                    }
                    return;
                case 12:
                    FVContentFragment.this.rl_focus_relative.setVisibility(0);
                    return;
                case 13:
                    if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                    }
                    return;
                case 14:
                    if (FVContentFragment.this.handler != null) {
                        while (FVContentFragment.this.handler.hasMessages(16)) {
                            FVContentFragment.this.handler.removeMessages(16);
                        }
                        return;
                    }
                    return;
                case 16:
                    Log.e("------------", "------ 收到了8秒消失的消息 -----");
                    if (FVContentFragment.this.rlFocus != null && !FVContentFragment.this.onLongClickTouch) {
                        Log.e("------------", "------ 收到了8秒消失的消息 点击事件执行 -----");
                        boolean unused4 = FVContentFragment.this.onLongClickTouch = false;
                        FVContentFragment.this.rlFocus.setVisibility(8);
                        FVContentFragment.this.recoverBright();
                        if (!CameraUtils.getBooRecordStarted()) {
                            FVContentFragment.this.setAutoFocusModeNewCameraOpen();
                        } else if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE))).intValue() == 107040) {
                            FVContentFragment.this.setAutoFocusModeNewCameraOpen();
                        }
                        FVContentFragment.this.cameraManager.lockAutoExposure(false);
                        return;
                    }
                    return;
                case 19:
                    boolean unused5 = FVContentFragment.this.onLongClickTouch = false;
                    if (FVContentFragment.this.rlFocus != null && FVContentFragment.this.rlFocus.getVisibility() == 0) {
                        FVContentFragment.this.rlFocus.setVisibility(8);
                    }
                    int unused6 = FVContentFragment.this.curBright = 0;
                    int unused7 = FVContentFragment.this.curBrightProgress = 50;
                    if (FVContentFragment.this.onResume) {
                        CameraUtils.setCamExposureLengthProgress(50);
                        FVContentFragment.this.seekbarBrightness.setProgress(FVContentFragment.this.curBrightProgress);
                        FVContentFragment.this.seekbarBrightness2.setProgress(FVContentFragment.this.curBrightProgress);
                        FVContentFragment.this.cameraManager.setExposureCompensation(0);
                        CameraUtils.setCamExposureLengthProgress(FVContentFragment.this.curBrightProgress);
                    }
                    if (!CameraUtils.getBooRecordStarted()) {
                        FVContentFragment.this.setAutoFocusModeNewCameraOpen();
                    } else if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE))).intValue() == 107040) {
                        FVContentFragment.this.setAutoFocusModeNewCameraOpen();
                    }
                    FVContentFragment.this.cameraManager.lockAutoExposure(false);
                    return;
                case 31:
                    long longValue = ((Long) msg.obj).longValue();
                    if (FVContentFragment.this.curScrollScaleShutter == 0 && (range = CameraUtils.getScaleShutterRange()) != null) {
                        long ranMax2 = range.getUpper().longValue();
                        if (ranMax2 < 500000000) {
                            long ranMax3 = ranMax2 * 2;
                            if (FVContentFragment.this.cameraManager.getExposureTime() != 0) {
                                FVContentFragment.this.horizontal_scale_shutter_textview.setText("1/" + (ranMax3 / FVContentFragment.this.cameraManager.getExposureTime()));
                                return;
                            }
                            return;
                        } else if (FVContentFragment.this.cameraManager.getExposureTime() != 0) {
                            FVContentFragment.this.horizontal_scale_shutter_textview.setText("1/" + (1000000000 / FVContentFragment.this.cameraManager.getExposureTime()));
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 32:
                    FVContentFragment.this.horizontal_scale_iso_textview.setText(((Integer) msg.obj).intValue() + "");
                    return;
                case 33:
                    int wb = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.horizontalScaleWbTwo.getVisibility() != 0) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(wb + "");
                        return;
                    } else if (FVContentFragment.this.curScrollScaleWB >= 40) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(wb + "");
                        return;
                    } else if (FVContentFragment.this.curScrollScaleWB == 0) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_incandescent_lamp);
                        return;
                    } else if (FVContentFragment.this.curScrollScaleWB == 10) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_fluorescent_lamp);
                        return;
                    } else if (FVContentFragment.this.curScrollScaleWB == 20) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_overcast);
                        return;
                    } else if (FVContentFragment.this.curScrollScaleWB == 30) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_sunshine);
                        return;
                    } else {
                        return;
                    }
                case 34:
                    FVContentFragment.this.horizontal_scale_mf_textview.setText(CameraUtils.strSubTwoLength((String) msg.obj) + "");
                    return;
                case 35:
                    FVContentFragment.this.scaleScrollViewIsoTwo.scrollToScaleFirst(-22);
                    Boolean unused8 = FVContentFragment.this.startISO = true;
                    return;
                case 36:
                    int disEv = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.horizontal_parameter_display_ev_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_ev_textview.setText(disEv + "");
                        return;
                    }
                    return;
                case 37:
                    long exposuretime = ((Long) msg.obj).longValue();
                    String shutterText = "";
                    if (!(FVContentFragment.this.cameraManager == null || (range2 = FVContentFragment.this.cameraManager.getExposureTimeRange()) == null)) {
                        long ranMax4 = range2.getUpper().longValue();
                        if (ranMax4 < 500000000) {
                            ranMax = ranMax4 * 2;
                        } else {
                            ranMax = 1000000000;
                        }
                        shutterText = "1/" + (ranMax / exposuretime);
                    }
                    if (FVContentFragment.this.horizontal_parameter_display_shutter_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_shutter_textview.setText(shutterText + "");
                        return;
                    }
                    return;
                case 38:
                    int disIso = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.horizontal_parameter_display_iso_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_iso_textview.setText(disIso + "");
                        return;
                    }
                    return;
                case 39:
                    int disWb = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.horizontal_parameter_display_wb_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_wb_textview.setText(disWb + "");
                        return;
                    }
                    return;
                case 41:
                    String disMf = (String) msg.obj;
                    if (FVContentFragment.this.horizontal_parameter_display_mf_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_mf_textview.setText(disMf + "");
                    }
                    String disWt2 = String.valueOf(CameraUtils.getZoomValue());
                    if (FVContentFragment.this.horizontal_parameter_display_wt_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_wt_textview.setText(CameraUtils.strSubTwoLength(disWt2) + "");
                        return;
                    }
                    return;
                case 42:
                    int disWt = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.horizontal_parameter_display_wt_textview != null) {
                        FVContentFragment.this.horizontal_parameter_display_wt_textview.setText(disWt + "");
                        return;
                    }
                    return;
                case 51:
                    if (FVContentFragment.this.scaleScrollViewEV.getVisibility() == 8) {
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(0);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        if (!FVContentFragment.this.startEV.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.scaleScrollViewEV.setCurScale((int) (CameraUtils.getScaleScrollViewEVMaxNums() * 10.0d));
                                    Boolean unused = FVContentFragment.this.startEV = true;
                                }
                            }, 200);
                        }
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_ev_textview, FVContentFragment.this.horizontal_scale_ev_text_title);
                        return;
                    }
                    return;
                case 52:
                    if (FVContentFragment.this.scaleScrollViewShutter.getVisibility() == 8) {
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(0);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                            if (!FVContentFragment.this.startShutter.booleanValue()) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        FVContentFragment.this.scaleScrollViewShutter.setCurScale(0);
                                        Boolean unused = FVContentFragment.this.startShutter = true;
                                    }
                                }, 200);
                            } else {
                                Boolean unused9 = FVContentFragment.this.startShutter = false;
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        FVContentFragment.this.scaleScrollViewShutter.setCurScale(0);
                                        Boolean unused = FVContentFragment.this.startShutter = true;
                                    }
                                }, 200);
                            }
                        } else if (!FVContentFragment.this.startShutter.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    int getScaleShutterNum;
                                    Range<Long> range = CameraUtils.getScaleShutterRange();
                                    if (range != null) {
                                        long ranMax = range.getUpper().longValue();
                                        if (ranMax < 500000000) {
                                            getScaleShutterNum = (int) (ranMax / FVContentFragment.this.cameraManager.getExposureTime());
                                        } else {
                                            getScaleShutterNum = (int) (1000000000 / FVContentFragment.this.cameraManager.getExposureTime());
                                        }
                                        FVContentFragment.this.scaleScrollViewShutter.setCurScale(Util.getDrawScaleShutterNumNear(getScaleShutterNum) * 10);
                                        Boolean unused = FVContentFragment.this.startShutter = true;
                                    }
                                }
                            }, 200);
                        }
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_shutter_textview, FVContentFragment.this.horizontal_scale_shutter_text_title);
                        return;
                    }
                    return;
                case 53:
                    if (FVContentFragment.this.scaleScrollViewIsoTwo.getVisibility() == 8) {
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(0);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                            if (!FVContentFragment.this.startISO.booleanValue()) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(0);
                                        Boolean unused = FVContentFragment.this.startISO = true;
                                    }
                                }, 200);
                            } else {
                                Boolean unused10 = FVContentFragment.this.startISO = false;
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(0);
                                        Boolean unused = FVContentFragment.this.startISO = true;
                                    }
                                }, 200);
                            }
                        } else if (!FVContentFragment.this.startISO.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(Util.getDrawScaleISONumNear(FVContentFragment.this.cameraManager.getISOValue()) * 10);
                                    Boolean unused = FVContentFragment.this.startISO = true;
                                }
                            }, 200);
                        }
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_iso_textview, FVContentFragment.this.horizontal_scale_iso_text_title);
                        return;
                    }
                    return;
                case 54:
                    if (FVContentFragment.this.horizontalScaleWbTwo.getVisibility() == 8) {
                        if (!FVContentFragment.this.startWB.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.horizontalScaleWbTwo.setCurScale(40);
                                    Boolean unused = FVContentFragment.this.startWB = true;
                                }
                            }, 200);
                        }
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(0);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_wb_textview, FVContentFragment.this.horizontal_scale_wb_text_title);
                        return;
                    }
                    return;
                case 55:
                    if (FVContentFragment.this.scaleScrollViewMf.getVisibility() == 8) {
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(0);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        if (!FVContentFragment.this.startMF.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.scaleScrollViewMf.setCurScaleFirst(0);
                                    Boolean unused = FVContentFragment.this.startMF = true;
                                }
                            }, 200);
                        }
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_mf_textview, FVContentFragment.this.horizontal_scale_mf_text_title);
                        return;
                    }
                    return;
                case 56:
                    if (FVContentFragment.this.scaleScrollViewWT.getVisibility() == 8) {
                        FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                        FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                        FVContentFragment.this.scaleScrollViewWT.setVisibility(0);
                        FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                        FVContentFragment.this.setContentHandModelVisiStateValue();
                        if (!FVContentFragment.this.startWT.booleanValue()) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.scaleScrollViewWT.setCurScale(0);
                                    Boolean unused = FVContentFragment.this.startWT = true;
                                }
                            }, 200);
                        }
                        FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_wt_textview, FVContentFragment.this.horizontal_scale_wt_text_title);
                        return;
                    }
                    return;
                case 57:
                    if (FVContentFragment.this.ll_setting_x_mf.getVisibility() == 0) {
                        FVContentFragment.this.ll_setting_x_mf.setVisibility(8);
                        return;
                    }
                    return;
                case 59:
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setCameraHandModelBgColor(false);
                        return;
                    }
                    return;
                case 61:
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setCameraHandModelBgColor(true);
                        return;
                    }
                    return;
                case 90:
                    FVContentFragment.this.llSeekbarInside.setRotation(180.0f);
                    FVContentFragment.this.rlBright.setVisibility(0);
                    FVContentFragment.this.rlBright2.setVisibility(8);
                    if (FVContentFragment.this.focusStatus == 1) {
                        FVContentFragment.this.rlBright2.setVisibility(0);
                        FVContentFragment.this.rlBright.setVisibility(8);
                    }
                    if (FVContentFragment.this.mStartAngle == -360) {
                        int unused11 = FVContentFragment.this.mStartAngle = 0;
                    }
                    FVContentFragment.this.rotateView(-90, FVContentFragment.this.tvTimer, FVContentFragment.this.rlFocus, FVContentFragment.this.ll_start_video_recording_resolution_fps, FVContentFragment.this.ll_setting_x_mf);
                    if (FVContentFragment.this.viewScale != null) {
                        int left2 = (FVContentFragment.this.widthPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        int top2 = (FVContentFragment.this.heightPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        RelativeLayout.LayoutParams lpsScale2 = (RelativeLayout.LayoutParams) FVContentFragment.this.content_layout_scale.getLayoutParams();
                        lpsScale2.setMargins(left2 + CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, top2, 0, 0);
                        FVContentFragment.this.content_layout_scale.setLayoutParams(lpsScale2);
                        LinearLayout unused12 = FVContentFragment.this.frag_content_scale_linear = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                        FVContentFragment.this.frag_content_scale_linear.setRotation(-90.0f);
                        LinearLayout unused13 = FVContentFragment.this.frag_content_scale_linear_place_top = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                        LinearLayout unused14 = FVContentFragment.this.frag_content_scale_linear_place_buttom = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                        FVContentFragment.this.frag_content_scale_linear_place_top.setVisibility(0);
                        FVContentFragment.this.frag_content_scale_linear_place_buttom.setVisibility(8);
                        Log.e("----------------", "----------  6666  7777  8888  ------- 90  Rotation:90");
                    }
                    if (FVContentFragment.this.ll_mark_point.getVisibility() == 0 || FVContentFragment.this.ll_mark_point_vertical.getVisibility() == 0) {
                        FVContentFragment.this.ll_mark_point.setVisibility(8);
                        FVContentFragment.this.ll_mark_point_vertical.setVisibility(0);
                        FVContentFragment.this.ll_mark_point_vertical.setRotation(0.0f);
                    }
                    if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                        FVContentFragment.this.timer_horizontal.setVisibility(8);
                        FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                        FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                        FVContentFragment.this.timer.setVisibility(0);
                        FVContentFragment.this.timer_size_all.setVisibility(0);
                        FVContentFragment.this.timer_size_all_view.setVisibility(0);
                        FVContentFragment.this.timer_video_record_start_linear.setRotation(-90.0f);
                        return;
                    }
                    return;
                case 180:
                    FVContentFragment.this.llSeekbarInside.setRotation(0.0f);
                    FVContentFragment.this.rlBright.setVisibility(0);
                    FVContentFragment.this.rlBright2.setVisibility(8);
                    FVContentFragment.this.rotateView(-180, FVContentFragment.this.tvTimer, FVContentFragment.this.rlFocus, FVContentFragment.this.ll_start_video_recording_resolution_fps, FVContentFragment.this.ll_setting_x_mf);
                    if (FVContentFragment.this.viewScale != null) {
                        int left3 = (FVContentFragment.this.widthPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        int top3 = (FVContentFragment.this.heightPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        RelativeLayout.LayoutParams lpsScale3 = (RelativeLayout.LayoutParams) FVContentFragment.this.content_layout_scale.getLayoutParams();
                        lpsScale3.setMargins(left3, top3, 0, 0);
                        FVContentFragment.this.content_layout_scale.setLayoutParams(lpsScale3);
                        LinearLayout unused15 = FVContentFragment.this.frag_content_scale_linear = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                        FVContentFragment.this.frag_content_scale_linear.setRotation(180.0f);
                        LinearLayout unused16 = FVContentFragment.this.frag_content_scale_linear_place_top = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                        LinearLayout unused17 = FVContentFragment.this.frag_content_scale_linear_place_buttom = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                        FVContentFragment.this.frag_content_scale_linear_place_top.setVisibility(0);
                        FVContentFragment.this.frag_content_scale_linear_place_buttom.setVisibility(8);
                        Log.e("----------------", "----------  6666  7777  8888  ------- 180  Rotation:180");
                    }
                    if (FVContentFragment.this.ll_mark_point.getVisibility() == 0 || FVContentFragment.this.ll_mark_point_vertical.getVisibility() == 0) {
                        FVContentFragment.this.ll_mark_point.setVisibility(0);
                        FVContentFragment.this.ll_mark_point.setRotation(180.0f);
                        FVContentFragment.this.ll_mark_point_vertical.setVisibility(8);
                    }
                    if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                        FVContentFragment.this.timer_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_size_all_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(0);
                        FVContentFragment.this.timer_video_record_start_linear_horizontal.setRotation(180.0f);
                        FVContentFragment.this.timer.setVisibility(8);
                        FVContentFragment.this.timer_size_all.setVisibility(8);
                        FVContentFragment.this.timer_size_all_view.setVisibility(8);
                        return;
                    }
                    return;
                case 270:
                    FVContentFragment.this.llSeekbarInside.setRotation(180.0f);
                    FVContentFragment.this.rlBright.setVisibility(0);
                    FVContentFragment.this.rlBright2.setVisibility(8);
                    if (FVContentFragment.this.focusStatus == 2) {
                        FVContentFragment.this.rlBright2.setVisibility(0);
                        FVContentFragment.this.rlBright.setVisibility(8);
                    }
                    if (FVContentFragment.this.mStartAngle == 0) {
                        int unused18 = FVContentFragment.this.mStartAngle = -360;
                    }
                    FVContentFragment.this.rotateView(-270, FVContentFragment.this.tvTimer, FVContentFragment.this.rlFocus, FVContentFragment.this.ll_start_video_recording_resolution_fps, FVContentFragment.this.ll_setting_x_mf);
                    if (FVContentFragment.this.viewScale != null) {
                        int left4 = (FVContentFragment.this.widthPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        int top4 = (FVContentFragment.this.heightPingMu - Util.dip2px(FVContentFragment.this.mContext, 345.0f)) / 2;
                        RelativeLayout.LayoutParams lpsScale4 = (RelativeLayout.LayoutParams) FVContentFragment.this.content_layout_scale.getLayoutParams();
                        lpsScale4.setMargins(left4 + CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, top4, 0, 0);
                        FVContentFragment.this.content_layout_scale.setLayoutParams(lpsScale4);
                        LinearLayout unused19 = FVContentFragment.this.frag_content_scale_linear = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                        FVContentFragment.this.frag_content_scale_linear.setRotation(90.0f);
                        LinearLayout unused20 = FVContentFragment.this.frag_content_scale_linear_place_top = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                        LinearLayout unused21 = FVContentFragment.this.frag_content_scale_linear_place_buttom = (LinearLayout) FVContentFragment.this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                        FVContentFragment.this.frag_content_scale_linear_place_top.setVisibility(8);
                        FVContentFragment.this.frag_content_scale_linear_place_buttom.setVisibility(0);
                        Log.e("----------------", "----------  6666  7777  8888  ------- 270  Rotation:270");
                    }
                    if (FVContentFragment.this.ll_mark_point.getVisibility() == 0 || FVContentFragment.this.ll_mark_point_vertical.getVisibility() == 0) {
                        FVContentFragment.this.ll_mark_point.setVisibility(8);
                        FVContentFragment.this.ll_mark_point_vertical.setVisibility(0);
                        FVContentFragment.this.ll_mark_point_vertical.setRotation(180.0f);
                    }
                    if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                        FVContentFragment.this.timer_horizontal.setVisibility(8);
                        FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                        FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                        FVContentFragment.this.timer.setVisibility(0);
                        FVContentFragment.this.timer_size_all.setVisibility(0);
                        FVContentFragment.this.timer_size_all_view.setVisibility(0);
                        FVContentFragment.this.timer_video_record_start_linear.setRotation(90.0f);
                        return;
                    }
                    return;
                case 300:
                    FVContentFragment.this.showTakePhotoShadow();
                    return;
                case 400:
                    FVContentFragment.this.revolveScrollScaleLinear();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int heightPingMu;
    private FVHitchCockCountTimer hitchCockCountTimer;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewWbTwo horizontalScaleWbTwo;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_ev_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_iso_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_mf_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_shutter_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_wb_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_parameter_display_wt_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_ev_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_ev_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_iso_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_iso_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_mf_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_mf_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_shutter_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_shutter_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_wb_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_wb_textview;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_wt_text_title;
    /* access modifiers changed from: private */
    public TextView horizontal_scale_wt_textview;
    @Bind({2131755718})
    TextView ic_seekbar_content_thumb_bubble_text;
    @Bind({2131755748})
    ImageView iconFullShot;
    @Bind({2131755653})
    ImageView iconGriding;
    private boolean isBrightUp = false;
    /* access modifiers changed from: private */
    public boolean isShadowShowing = false;
    private boolean isZoomMode = false;
    @Bind({2131755761})
    ImageView iv_gradienter_bg;
    @Bind({2131755762})
    ImageView iv_gradienter_white;
    /* access modifiers changed from: private */
    public ImageView iv_hand_model_scroll_draw_pointer;
    @Bind({2131755715})
    ImageView iv_mark_quit_out;
    @Bind({2131755723})
    ImageView iv_mark_quit_out_vertical;
    @Bind({2131755712})
    ImageView iv_mark_setting_menu;
    @Bind({2131755720})
    ImageView iv_mark_setting_menu_vertical;
    @Bind({2131755724})
    LinearLayout linear_timer;
    @Bind({2131755730})
    ProgressBar linear_timer_progress_bar;
    @Bind({2131755731})
    RecyclerView linear_timer_progress_recycler;
    @Bind({2131755747})
    LinearLayout llFullShot;
    @Bind({2131755654})
    LinearLayout llSeekbar;
    @Bind({2131755716})
    LinearLayout llSeekbarInside;
    @Bind({2131755711})
    LinearLayout ll_mark_point;
    @Bind({2131755713})
    TextView ll_mark_point_a;
    @Bind({2131755721})
    TextView ll_mark_point_a_vertical;
    @Bind({2131755714})
    TextView ll_mark_point_b;
    @Bind({2131755722})
    TextView ll_mark_point_b_vertical;
    @Bind({2131755719})
    LinearLayout ll_mark_point_vertical;
    @Bind({2131755717})
    LinearLayout ll_seekbar_inside_down;
    @Bind({2131755732})
    LinearLayout ll_setting_x_mf;
    @Bind({2131755733})
    TextView ll_setting_x_mf_value;
    @Bind({2131755734})
    LinearLayout ll_start_video_recording_resolution_fps;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public MyRecordingCountDownTimer mMyRecordingCountDownTimer;
    private OrientationEventListener mOrEventAngleListener;
    private Handler mRotateHandler = new Handler() {
        public void handleMessage(Message msg) {
            ViseLog.m1466e("linear_timer rotate: " + String.valueOf(msg.what));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) FVContentFragment.this.linear_timer.getLayoutParams();
            switch (msg.what) {
                case 0:
                    FVContentFragment.this.setLinearTimerPosition(12, 14, lp);
                    if (FVContentFragment.this.mStartAngle == -270) {
                        FVContentFragment.this.rotateView(-360, FVContentFragment.this.linear_timer);
                        return;
                    }
                    FVContentFragment.this.rotateView(0, FVContentFragment.this.linear_timer);
                    return;
                case 90:
                    FVContentFragment.this.setLinearTimerPosition(11, 15, lp);
                    if (FVContentFragment.this.mStartAngle == -360) {
                        int unused = FVContentFragment.this.mStartAngle = 0;
                    }
                    FVContentFragment.this.rotateView(-90, FVContentFragment.this.linear_timer);
                    return;
                case 180:
                    FVContentFragment.this.setLinearTimerPosition(10, 14, lp);
                    FVContentFragment.this.rotateView(-180, FVContentFragment.this.linear_timer);
                    return;
                case 270:
                    FVContentFragment.this.setLinearTimerPosition(9, 15, lp);
                    FVContentFragment.this.linear_timer.setLayoutParams(lp);
                    if (FVContentFragment.this.mStartAngle == 0) {
                        int unused2 = FVContentFragment.this.mStartAngle = -360;
                    }
                    FVContentFragment.this.rotateView(-270, FVContentFragment.this.linear_timer);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int mStartAngle = 0;
    private VelocityTracker mTracker;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 71:
                    int progress = ((Integer) msg.obj).intValue();
                    Log.e("------------", "-------- Zoom Progress Start -------  progress:" + progress);
                    if (FVContentFragment.this.cameraManager.isZoomSupported()) {
                        float zoomChange = FVContentFragment.this.getZoomChange(progress);
                        if (zoomChange > CameraUtils.getAppMaxZoom()) {
                            zoomChange = CameraUtils.getAppMaxZoom();
                        }
                        String zoomChangeStr = String.valueOf(zoomChange);
                        if (!CameraUtils.getCameraHandModel() || FVContentFragment.this.content_layout_scale.getVisibility() != 0) {
                            FVContentFragment.this.seekbarFocalLength.setProgress(progress);
                        } else if (!(CameraUtils.getHandModelVisibleStateValue() == 5.0f || CameraUtils.getHandModelVisibleStateValue() == 6.0f)) {
                            if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                                FVContentFragment.this.horizontal_scale_wt_textview.setText(CameraUtils.strSubTwoLength(zoomChangeStr) + "");
                            } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                            }
                        }
                        Log.e("------------", "-------- Zoom Progress --收到消息 0 0 0 0 -----  zoomChange:" + zoomChange);
                        if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1) {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(zoomChangeStr.substring(0, zoomChangeStr.indexOf(".") + 2));
                        } else if (zoomChangeStr.length() > 2) {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(FVContentFragment.this.setZoomTextViewRange(zoomChangeStr.substring(0, zoomChangeStr.indexOf(".") + 2)) + "x");
                        } else {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(FVContentFragment.this.setZoomTextViewRange(zoomChangeStr) + "x");
                        }
                        FVContentFragment.this.sendToHandlerMix(81, Float.valueOf(zoomChange));
                        return;
                    }
                    return;
                case 72:
                    int progress2 = ((Integer) msg.obj).intValue();
                    if (FVContentFragment.this.cameraManager.isZoomSupported()) {
                        FVContentFragment.this.seekbarFocalLength.setProgress(progress2);
                        float zoomChange2 = FVContentFragment.this.getZoomChange(progress2);
                        String zoomChangeStr2 = String.valueOf(zoomChange2);
                        if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1) {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(zoomChangeStr2.substring(0, zoomChangeStr2.indexOf(".") + 2));
                        } else if (zoomChangeStr2.length() > 2) {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(FVContentFragment.this.setZoomTextViewRange(zoomChangeStr2.substring(0, zoomChangeStr2.indexOf(".") + 2)) + "x");
                        } else {
                            FVContentFragment.this.ic_seekbar_content_thumb_bubble_text.setText(FVContentFragment.this.setZoomTextViewRange(zoomChangeStr2) + "x");
                        }
                        FVContentFragment.this.sendToHandlerMix(82, Float.valueOf(zoomChange2));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler myMixHandler;
    /* access modifiers changed from: private */
    public boolean onLongClickTouch = false;
    /* access modifiers changed from: private */
    public boolean onResume = false;
    @Bind({2131755752})
    ProgressBar pbSynthesis;
    private int progressHandlerOld;
    private int progressOld;
    private PermissionUtil.PermissionRequestObject requestObject;
    @Bind({2131755757})
    RelativeLayout rlBright;
    @Bind({2131755754})
    RelativeLayout rlBright2;
    @Bind({2131755657})
    RelativeLayout rlFocus;
    @Bind({2131755750})
    RelativeLayout rlPhotoSynthesis;
    @Bind({2131755753})
    RelativeLayout rl_focus_relative;
    @Bind({2131755729})
    RelativeLayout rl_timer_progress_bar;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewEV scaleScrollViewEV;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewIsoTwo scaleScrollViewIsoTwo;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewMf scaleScrollViewMf;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewShutter scaleScrollViewShutter;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewWT scaleScrollViewWT;
    /* access modifiers changed from: private */
    public SeekBar seekbarBrightness;
    @Bind({2131755756})
    SeekBar seekbarBrightness2;
    /* access modifiers changed from: private */
    public SeekBar seekbarFocalLength;
    @Bind({2131755755})
    View seekbar_brightness2_view;
    @Bind({2131755758})
    View seekbar_brightness_view;
    private AnimatorSet set;
    private float setMFValueOld;
    private float startDistance = 0.0f;
    /* access modifiers changed from: private */
    public Boolean startEV = false;
    /* access modifiers changed from: private */
    public Boolean startISO = false;
    private Boolean startISONew = false;
    /* access modifiers changed from: private */
    public Boolean startMF = false;
    /* access modifiers changed from: private */
    public Boolean startShutter = false;
    private Boolean startShutterNew = false;
    /* access modifiers changed from: private */
    public Boolean startWB = false;
    /* access modifiers changed from: private */
    public Boolean startWT = false;
    /* access modifiers changed from: private */
    public CameraView surfaceView;
    private String systemModel = "";
    @Bind({2131755742})
    Chronometer timer;
    @Bind({2131755738})
    Chronometer timer_horizontal;
    @Bind({2131755744})
    TextView timer_size_all;
    @Bind({2131755740})
    TextView timer_size_all_horizontal;
    @Bind({2131755743})
    TextView timer_size_all_view;
    @Bind({2131755739})
    TextView timer_size_all_view_horizontal;
    @Bind({2131755741})
    LinearLayout timer_video_record_start_linear;
    @Bind({2131755737})
    LinearLayout timer_video_record_start_linear_horizontal;
    @Bind({2131755656})
    TextView tvTimer;
    @Bind({2131755727})
    TextView tv_chro_all_timer_center;
    @Bind({2131755759})
    ImageView tv_setting_x_point_location_a;
    @Bind({2131755760})
    ImageView tv_setting_x_point_location_b;
    @Bind({2131755736})
    TextView tx_start_video_recording_fps;
    @Bind({2131755735})
    TextView tx_start_video_recording_resolution;
    private boolean useCamera2 = true;
    private View view;
    private View viewParameterDisplay;
    /* access modifiers changed from: private */
    public View viewScale;
    /* access modifiers changed from: private */
    public int widthPingMu;
    private int xSpeed;
    private int ySpeed;
    private int zoom = 0;
    LinkedList<Integer> zoomQueue = new LinkedList<>();
    Timer zoomTimer = new Timer();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() != 0) {
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD, Integer.valueOf(Constants.CAMERA_MODE_PRO))).intValue() == 107085) {
                MoveTimelapseUtil.setCameraSelectOneOrTwo(2);
            } else {
                MoveTimelapseUtil.setCameraSelectOneOrTwo(1);
            }
        } else if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD_SWITCH, Integer.valueOf(Constants.CAMERA_MODE_PRO_TRAD_CLOSE))).intValue() != 107088) {
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD, Integer.valueOf(Constants.CAMERA_MODE_PRO))).intValue() == 107085) {
                MoveTimelapseUtil.setCameraSelectOneOrTwo(2);
            } else {
                MoveTimelapseUtil.setCameraSelectOneOrTwo(1);
            }
        }
        int cameraLevel = FVCameraManager.GetCameraLevel(getActivity());
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() != 0) {
            if (MoveTimelapseUtil.getCameraSelectOneOrTwo() == 1) {
                cameraLevel = -1;
            } else {
                cameraLevel = 3;
            }
        }
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() == 0) {
            if (cameraLevel == 2) {
                this.view = inflater.inflate(C0853R.layout.fragment_content_two_1, container, false);
                this.useCamera2 = false;
            } else if (Util.versionRelease()) {
                this.view = inflater.inflate(C0853R.layout.fragment_content_two, container, false);
                this.useCamera2 = true;
            } else if (cameraLevel == -1) {
                this.view = inflater.inflate(C0853R.layout.fragment_content_two_1, container, false);
                this.useCamera2 = false;
            } else {
                this.view = inflater.inflate(C0853R.layout.fragment_content_two, container, false);
                this.useCamera2 = true;
            }
        } else if (MoveTimelapseUtil.getCameraSelectOneOrTwo() == 2) {
            this.view = inflater.inflate(C0853R.layout.fragment_content_two, container, false);
            this.useCamera2 = true;
        } else {
            this.view = inflater.inflate(C0853R.layout.fragment_content_two_1, container, false);
            this.useCamera2 = false;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        this.widthPingMu = dm.widthPixels;
        this.heightPingMu = dm.heightPixels;
        CameraUtils.setWidthPingMu(this.widthPingMu);
        CameraUtils.setHeightPingMu(this.heightPingMu);
        Log.e("-------------", "--------------  widthPingMu: " + this.widthPingMu + "  heightPingMu: " + this.heightPingMu);
        this.surfaceView = (CameraView) this.view.findViewById(C0853R.C0855id.surfaceView);
        this.seekbarFocalLength = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_focal_length);
        this.seekbarBrightness = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_brightness);
        ButterKnife.bind((Object) this, this.view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mContext = getActivity();
        askPermission();
        initListener();
        initStatus();
        this.fragment_content_two_ll.setVisibility(8);
        if (this.cameraZoomThread != null && this.cameraZoomThread.isAlive()) {
            this.cameraZoomThread.interrupt();
        }
        this.cameraZoomThread = new CameraZoomThread();
        this.cameraZoomThread.start();
        if (this.cameraZoomExpoThread != null && this.cameraZoomExpoThread.isAlive()) {
            this.cameraZoomExpoThread.interrupt();
        }
        this.cameraZoomExpoThread = new CameraZoomExpoThread();
        this.cameraZoomExpoThread.start();
        if (cameraLevel == -1 || cameraLevel != 2 || !Util.versionRelease()) {
        }
        this.systemModel = Util.getSystemModel();
        return this.view;
    }

    private void setParameterListenerNull() {
        this.cameraManager.setCameraStateListner((Camera2Manager.CameraStatesListener) null);
        this.content_layout_parameter_display.setVisibility(8);
        if (this.viewParameterDisplay != null) {
            this.content_layout_parameter_display.removeView(this.viewParameterDisplay);
        }
        SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_CLOSE));
    }

    private void getParameterListener() {
        SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_OPEN));
        if (this.viewParameterDisplay != null) {
            this.content_layout_parameter_display.removeView(this.viewParameterDisplay);
        }
        this.content_layout_parameter_display.setVisibility(0);
        this.viewParameterDisplay = LayoutInflater.from(this.mContext).inflate(C0853R.layout.fragment_content_parameter_display_linear, (ViewGroup) null);
        this.content_layout_parameter_display.addView(this.viewParameterDisplay);
        this.frag_content_parameter_display_linear = (LinearLayout) this.viewParameterDisplay.findViewById(C0853R.C0855id.frag_content_parameter_display_linear);
        this.frag_content_parameter_display_linear.setVisibility(0);
        this.horizontal_parameter_display_ev_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_ev_textview);
        this.horizontal_parameter_display_shutter_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_shutter_textview);
        this.horizontal_parameter_display_iso_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_iso_textview);
        this.horizontal_parameter_display_wb_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_wb_textview);
        this.horizontal_parameter_display_mf_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_mf_textview);
        this.horizontal_parameter_display_wt_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_parameter_display_wt_textview);
        this.cameraManager.setCameraStateListner(new Camera2Manager.CameraStatesListener() {
            private double mfScale;

            public void isoValue(Integer isovalue) {
                FVContentFragment.this.sendToHandlerScale(38, isovalue);
            }

            public void exposureTime(Long exposuretime) {
                FVContentFragment.this.sendToHandlerScale(37, exposuretime);
            }

            public void exposureCompensation(Integer ev) {
                FVContentFragment.this.sendToHandlerScale(36, ev);
            }

            public void fpsRange(Range<Integer> range) {
            }

            public void frameDuration(Long duration) {
            }

            public void colorCorrectionGains(RggbChannelVector vector) {
                FVContentFragment.this.sendToHandlerScale(39, Integer.valueOf(com.freevisiontech.cameralib.utils.CameraUtils.FactorFromWbTemperature(vector)));
            }

            public void focalLen(Float len) {
            }

            public void focusDistance(Float focusdistance) {
                String mfStr = String.valueOf(focusdistance);
                if (mfStr.length() > 3) {
                    if (mfStr.substring(0, mfStr.indexOf(".")).length() > 1) {
                        mfStr = mfStr.substring(0, 5);
                    } else {
                        mfStr = mfStr.substring(0, 4);
                    }
                }
                FVContentFragment.this.sendToHandlerScale(41, mfStr);
            }

            public void modes(Integer af, Integer ae, Integer awb, Integer flash, Integer control) {
            }

            public void states(Integer af, Integer ae, Integer awb, Integer flash) {
            }

            public void lenState(Integer state) {
            }

            public void aelocked(Boolean locked) {
            }

            public void aftrigger(Integer triggered) {
            }

            public void awblocked(Boolean awblocked) {
            }
        });
    }

    public void onViewCreated(View view2, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view2, savedInstanceState);
        setScrollScaleRestart();
    }

    private void setScrollScaleRestart() {
        SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
        this.camera_hand_model = false;
        CameraUtils.setCameraHandModel(false);
        this.content_layout_scale.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void revolveScrollScaleLinear() {
        this.angleScrollScale = ScreenOrientationUtil.getInstance().getOrientation();
        if (this.angleScrollScale == 0) {
            if (this.viewScale != null) {
                int left = (this.widthPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                int top = (this.heightPingMu - Util.dip2px(this.mContext, 345.0f)) - Util.dip2px(this.mContext, 22.0f);
                RelativeLayout.LayoutParams lpsScale = (RelativeLayout.LayoutParams) this.content_layout_scale.getLayoutParams();
                lpsScale.setMargins(left, top, 0, 0);
                this.content_layout_scale.setLayoutParams(lpsScale);
                this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                this.frag_content_scale_linear.setRotation(0.0f);
                this.frag_content_scale_linear_place_top = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                this.frag_content_scale_linear_place_buttom = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                this.frag_content_scale_linear_place_top.setVisibility(0);
                this.frag_content_scale_linear_place_buttom.setVisibility(8);
            }
        } else if (this.angleScrollScale == 90) {
            if (this.viewScale != null) {
                int left2 = (this.widthPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                int top2 = (this.heightPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                RelativeLayout.LayoutParams lpsScale2 = (RelativeLayout.LayoutParams) this.content_layout_scale.getLayoutParams();
                lpsScale2.setMargins(left2 + CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, top2, 0, 0);
                this.content_layout_scale.setLayoutParams(lpsScale2);
                this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                this.frag_content_scale_linear.setRotation(-90.0f);
                this.frag_content_scale_linear_place_top = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                this.frag_content_scale_linear_place_buttom = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                this.frag_content_scale_linear_place_top.setVisibility(0);
                this.frag_content_scale_linear_place_buttom.setVisibility(8);
            }
        } else if (this.angleScrollScale == 180) {
            if (this.viewScale != null) {
                int left3 = (this.widthPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                int top3 = (this.heightPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                RelativeLayout.LayoutParams lpsScale3 = (RelativeLayout.LayoutParams) this.content_layout_scale.getLayoutParams();
                lpsScale3.setMargins(left3, top3, 0, 0);
                this.content_layout_scale.setLayoutParams(lpsScale3);
                this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                this.frag_content_scale_linear.setRotation(180.0f);
                this.frag_content_scale_linear_place_top = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                this.frag_content_scale_linear_place_buttom = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                this.frag_content_scale_linear_place_top.setVisibility(0);
                this.frag_content_scale_linear_place_buttom.setVisibility(8);
            }
        } else if (this.angleScrollScale == 270 && this.viewScale != null) {
            int left4 = (this.widthPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
            int top4 = (this.heightPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
            RelativeLayout.LayoutParams lpsScale4 = (RelativeLayout.LayoutParams) this.content_layout_scale.getLayoutParams();
            lpsScale4.setMargins(left4 + CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, top4, 0, 0);
            this.content_layout_scale.setLayoutParams(lpsScale4);
            this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
            this.frag_content_scale_linear.setRotation(90.0f);
            this.frag_content_scale_linear_place_top = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
            this.frag_content_scale_linear_place_buttom = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
            this.frag_content_scale_linear_place_top.setVisibility(8);
            this.frag_content_scale_linear_place_buttom.setVisibility(0);
        }
    }

    private void markPointQuitOut() {
        if (this.ll_mark_point.getVisibility() == 0 || this.ll_mark_point_vertical.getVisibility() == 0) {
            this.ll_mark_point.setVisibility(8);
            this.ll_mark_point_vertical.setVisibility(8);
            this.ll_mark_point_a.setText("MF: 0.00");
            this.ll_mark_point_b.setText("MF: 0.00");
            this.ll_mark_point_a_vertical.setText("MF: 0.00");
            this.ll_mark_point_b_vertical.setText("MF: 0.00");
            CameraUtils.setLlMarkPointMfA(0.0f);
            CameraUtils.setLlMarkPointMfB(0.0f);
            CameraUtils.setLlMarkPointWtA(1.0f);
            CameraUtils.setLlMarkPointWtB(1.0f);
            CameraUtils.setMarkPointWtIsFirst(true);
            CameraUtils.setMarkPointMfIsFirst(true);
            CameraUtils.setMarkPointUIIsVisible(false);
            Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT_THREAD_STOP);
            if (CameraUtils.getCurrentPageIndex() == 2 && this.ll_seekbar_inside_down.getVisibility() == 0) {
                this.ll_seekbar_inside_down.setVisibility(8);
            }
            if (this.cameraManager.getCameraManagerType() == 2 && this.cameraManager.isCameraOpened()) {
                if (this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(false);
                }
                this.cameraManager.enableManualMode(false);
            }
        }
    }

    private void initViewScale() {
        this.startShutter = false;
        this.startISO = false;
        this.startWB = false;
        this.startMF = false;
        this.startWT = false;
        this.startEV = false;
        this.startShutterNew = false;
        this.startISONew = false;
        this.curScrollScaleShutterOld = 0;
        this.curScrollScaleShutter = 0;
        this.curScrollScaleISOOld = 0;
        this.curScrollScaleISO = 0;
        this.curScrollScaleGetISOOld = 0;
        this.curScrollScaleGetISO = 0;
        this.curScrollScaleGetWBOld = 0;
        this.curScrollScaleGetWB = 0;
        this.curScrollScaleWBOld = 0;
        this.curScrollScaleWB = 40;
        this.curScrollScaleMfOld = 0;
        this.curScrollScaleMf = 0;
        this.curScrollScaleEVOld = 0;
        this.curScrollScaleEV = 0;
        this.curScrollScaleWTOld = 0;
        this.curScrollScaleWT = 0;
        this.horizontal_scale_shutter_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_shutter_textview);
        this.horizontal_scale_shutter_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_shutter_text_title);
        this.horizontal_scale_iso_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_iso_textview);
        this.horizontal_scale_iso_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_iso_text_title);
        this.horizontal_scale_wb_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_wb_textview);
        this.horizontal_scale_wb_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_wb_text_title);
        this.horizontal_scale_mf_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_textview);
        this.horizontal_scale_mf_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_text_title);
        this.horizontal_scale_ev_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_ev_textview);
        this.horizontal_scale_ev_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_ev_text_title);
        this.horizontal_scale_wt_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_wt_textview);
        this.horizontal_scale_wt_text_title = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_wt_text_title);
        this.frag_content_scale_linear_image_left = (ImageView) this.view.findViewById(C0853R.C0855id.frag_content_scale_linear_image_left);
        this.frag_content_scale_linear_image_right = (ImageView) this.view.findViewById(C0853R.C0855id.frag_content_scale_linear_image_right);
        this.horizontal_scale_shutter_textview.setText("1/50");
        this.horizontal_scale_iso_textview.setText("55");
        this.horizontal_scale_wb_textview.setText("58");
        this.horizontal_scale_mf_textview.setText("0.0");
        this.horizontal_scale_ev_textview.setText("0");
        this.horizontal_scale_wt_textview.setText(BuildConfig.VERSION_NAME);
        this.scaleScrollViewShutter = (HorizontalScaleScrollViewShutter) this.view.findViewById(C0853R.C0855id.horizontalScaleShutter);
        this.scaleScrollViewShutter.setOnScrollListener(new BaseScaleViewShutter.OnScrollListener() {
            public void onScaleScroll(final int scale) {
                Range<Long> range;
                FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, 620);
                FVContentFragment.this.curScrollScaleShutter = (int) ((double) scale);
                if (FVContentFragment.this.curScrollScaleShutterOld != FVContentFragment.this.curScrollScaleShutter) {
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setTopBarFragmentStatusRestart();
                    }
                    if (scale % 10 == 0 && FVContentFragment.this.startShutter.booleanValue()) {
                        if (CameraUtils.getScaleScrollTouchStateShutter()) {
                            if (scale == 0) {
                                if (FVContentFragment.this.cameraManager.isManualExposure()) {
                                    FVContentFragment.this.cameraManager.enableMEMode(false);
                                }
                                FVContentFragment.this.horizontal_scale_shutter_textview.setText(CameraUtils.getScaleScrollShutterAuto());
                            } else {
                                FVContentFragment.this.horizontal_scale_shutter_textview.setText("1/" + Util.getDrawScalePaint(scale));
                                if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                                    FVContentFragment.this.cameraManager.enableMEMode(true);
                                }
                            }
                            if (!(scale == 0 || (range = CameraUtils.getScaleShutterRange()) == null)) {
                                long ranMax = range.getUpper().longValue();
                                Log.e("-----------", "-------- 6666 7777 8888 9999 shutter置为非0   44444444444  ------- ranMax: " + ranMax);
                                if (ranMax >= 500000000) {
                                    FVContentFragment.this.cameraManager.setExposureTime((long) (1000000000 / Util.getDrawScalePaint(scale)));
                                } else if (ranMax / ((long) Util.getDrawScalePaint(scale)) < ((long) (1000000000 / Util.getDrawScalePaint(scale)))) {
                                    FVContentFragment.this.cameraManager.setExposureTime(ranMax / ((long) Util.getDrawScalePaint(scale)));
                                } else {
                                    FVContentFragment.this.cameraManager.setExposureTime((long) (1000000000 / Util.getDrawScalePaint(scale)));
                                }
                            }
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Range<Long> range;
                                    if (CameraUtils.getScaleScrollTouchStateShutter()) {
                                        if (scale == 0) {
                                            if (FVContentFragment.this.cameraManager.isManualExposure()) {
                                                FVContentFragment.this.cameraManager.enableMEMode(false);
                                            }
                                            FVContentFragment.this.horizontal_scale_shutter_textview.setText(CameraUtils.getScaleScrollShutterAuto());
                                        } else {
                                            FVContentFragment.this.horizontal_scale_shutter_textview.setText("1/" + Util.getDrawScalePaint(scale));
                                            if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                                                FVContentFragment.this.cameraManager.enableMEMode(true);
                                            }
                                        }
                                        if (scale != 0 && (range = CameraUtils.getScaleShutterRange()) != null) {
                                            long ranMax = range.getUpper().longValue();
                                            if (ranMax >= 500000000) {
                                                FVContentFragment.this.cameraManager.setExposureTime((long) (1000000000 / Util.getDrawScalePaint(scale)));
                                            } else if (ranMax / ((long) Util.getDrawScalePaint(scale)) < ((long) (1000000000 / Util.getDrawScalePaint(scale)))) {
                                                FVContentFragment.this.cameraManager.setExposureTime(ranMax / ((long) Util.getDrawScalePaint(scale)));
                                            } else {
                                                FVContentFragment.this.cameraManager.setExposureTime((long) (1000000000 / Util.getDrawScalePaint(scale)));
                                            }
                                        }
                                    }
                                }
                            }, 50);
                        }
                    }
                }
                FVContentFragment.this.curScrollScaleShutterOld = FVContentFragment.this.curScrollScaleShutter;
            }
        });
        this.scaleScrollViewIsoTwo = (HorizontalScaleScrollViewIsoTwo) this.view.findViewById(C0853R.C0855id.horizontalScaleIsoTwo);
        this.scaleScrollViewIsoTwo.setOnScrollListener(new BaseScaleViewIsoTwo.OnScrollListener() {
            public void onScaleScroll(int scale) {
                Range<Integer> rangeISO;
                FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, CompanyIdentifierResolver.PROCTER_GAMBLE);
                FVContentFragment.this.curScrollScaleISO = (int) ((double) scale);
                if (FVContentFragment.this.curScrollScaleISOOld != FVContentFragment.this.curScrollScaleISO) {
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setTopBarFragmentStatusRestart();
                    }
                    if (scale % 10 == 0 && FVContentFragment.this.startISO.booleanValue()) {
                        if (scale == 0) {
                            if (FVContentFragment.this.cameraManager.isManualExposure()) {
                                FVContentFragment.this.cameraManager.enableMEMode(false);
                            }
                            if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                                FVContentFragment.this.horizontal_scale_shutter_textview.setText(CameraUtils.getScaleScrollShutterAuto());
                            }
                        } else {
                            FVContentFragment.this.horizontal_scale_iso_textview.setText(Util.getDrawScaleISOPaint(scale) + "");
                            if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                                FVContentFragment.this.cameraManager.enableMEMode(true);
                            }
                        }
                        if (!(scale == 0 || (rangeISO = CameraUtils.getScaleIsoRange()) == null)) {
                            int value = Util.getDrawScaleISOPaint(scale);
                            if (value < rangeISO.getLower().intValue()) {
                                value = rangeISO.getLower().intValue();
                            } else if (value > rangeISO.getUpper().intValue()) {
                                value = rangeISO.getUpper().intValue();
                            }
                            FVContentFragment.this.cameraManager.setISOValue(value);
                        }
                    }
                }
                FVContentFragment.this.curScrollScaleISOOld = FVContentFragment.this.curScrollScaleISO;
            }
        });
        this.horizontalScaleWbTwo = (HorizontalScaleScrollViewWbTwo) this.view.findViewById(C0853R.C0855id.horizontalScaleWbTwo);
        this.horizontalScaleWbTwo.setOnScrollListener(new BaseScaleViewWbTwo.OnScrollListener() {
            public void onScaleScroll(int scale) {
                double sca = (double) scale;
                if (Util.getDrawScaleWBMax() != 40) {
                    FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, Util.getDrawScaleWBMax());
                } else {
                    FVContentFragment.this.frag_content_scale_linear_image_left.setVisibility(0);
                    FVContentFragment.this.frag_content_scale_linear_image_right.setVisibility(0);
                }
                FVContentFragment.this.curScrollScaleWB = (int) sca;
                if (FVContentFragment.this.curScrollScaleWBOld != FVContentFragment.this.curScrollScaleWB) {
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setTopBarFragmentStatusRestart();
                    }
                    if (sca < 35.0d) {
                        if (sca < 5.0d) {
                            SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_INCANDESCENT_LAMP));
                            FVContentFragment.this.setWB("incandescent");
                            FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_incandescent_lamp);
                        } else if (sca >= 5.0d && sca < 15.0d) {
                            SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_FLUORESCENT_LAMP));
                            FVContentFragment.this.setWB("fluorescent");
                            FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_fluorescent_lamp);
                        } else if (sca >= 15.0d && sca < 25.0d) {
                            SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_OVERCAST));
                            FVContentFragment.this.setWB("cloudy-daylight");
                            FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_overcast);
                        } else if (sca >= 25.0d && sca < 35.0d) {
                            SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_SUNSHINE));
                            FVContentFragment.this.setWB("daylight");
                            FVContentFragment.this.horizontal_scale_wb_textview.setText(C0853R.string.label_wb_sunshine);
                        }
                    } else if (sca >= 35.0d && sca <= 40.0d) {
                        if (FVContentFragment.this.cameraManager.isManualWhiteBalance()) {
                            FVContentFragment.this.cameraManager.enableMWBMode(false);
                        }
                        SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
                        FVContentFragment.this.cameraManager.setWhiteBalance(Constants.SCENE_MODE_AUTO);
                        if (FVContentFragment.this.cameraManager.getWBGainFactor() != -1) {
                            FVContentFragment.this.horizontal_scale_wb_textview.setText(FVContentFragment.this.cameraManager.getWBGainFactor() + "");
                        } else {
                            FVContentFragment.this.horizontal_scale_wb_textview.setText("AUTO");
                        }
                    } else if (sca > 40.0d) {
                        FVContentFragment.this.horizontal_scale_wb_textview.setText(String.valueOf((int) (sca - 40.0d)));
                        if (!FVContentFragment.this.cameraManager.isManualWhiteBalance()) {
                            FVContentFragment.this.cameraManager.enableMWBMode(true);
                        }
                        FVContentFragment.this.cameraManager.setWBGainValue((int) (sca - 40.0d));
                    }
                }
                FVContentFragment.this.curScrollScaleWBOld = FVContentFragment.this.curScrollScaleWB;
            }
        });
        this.scaleScrollViewMf = (HorizontalScaleScrollViewMf) this.view.findViewById(C0853R.C0855id.horizontalScaleMf);
        this.scaleScrollViewMf.setOnScrollListener(new BaseScaleViewMf.OnScrollListener() {
            public void onScaleScroll(int scale) {
                double sca = (double) scale;
                FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, Util.getDrawScaleMFMax());
                FVContentFragment.this.curScrollScaleMf = (int) sca;
                if (FVContentFragment.this.curScrollScaleMfOld != FVContentFragment.this.curScrollScaleMf) {
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setTopBarFragmentStatusRestart();
                    }
                    if (sca >= 10.0d) {
                        if (!FVContentFragment.this.cameraManager.isMaunalFocus()) {
                            FVContentFragment.this.cameraManager.enableMFMode(true);
                        }
                        FVContentFragment.this.horizontal_scale_mf_textview.setText(String.valueOf((sca - 10.0d) / 10.0d));
                        FVContentFragment.this.cameraManager.setFocusDistance((float) ((sca - 10.0d) / 10.0d));
                    } else if (sca < 5.0d) {
                        FVContentFragment.this.horizontal_scale_mf_textview.setText("AUTO");
                        if (FVContentFragment.this.cameraManager.isMaunalFocus()) {
                            FVContentFragment.this.cameraManager.enableMFMode(false);
                        }
                    } else {
                        FVContentFragment.this.horizontal_scale_mf_textview.setText("0.0");
                        if (!FVContentFragment.this.cameraManager.isMaunalFocus()) {
                            FVContentFragment.this.cameraManager.enableMFMode(true);
                        }
                    }
                }
                FVContentFragment.this.curScrollScaleMfOld = FVContentFragment.this.curScrollScaleMf;
            }
        });
        this.scaleScrollViewEV = (HorizontalScaleScrollViewEV) this.view.findViewById(C0853R.C0855id.horizontalScaleEV);
        this.scaleScrollViewEV.setOnScrollListener(new BaseScaleViewEV.OnScrollListener() {
            public void onScaleScroll(int scale) {
                double sca = (double) scale;
                FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, Util.getDrawScaleEVMax());
                FVContentFragment.this.curScrollScaleEV = (int) sca;
                if (FVContentFragment.this.curScrollScaleEVOld != FVContentFragment.this.curScrollScaleEV) {
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        FVContentFragment.this.setTopBarFragmentStatusRestart();
                    }
                    if (FVContentFragment.this.startEV.booleanValue()) {
                        int scaBright = (int) (((double) ((int) (sca / 10.0d))) - CameraUtils.getScaleScrollViewEVMaxNums());
                        FVContentFragment.this.horizontal_scale_ev_textview.setText(((double) scaBright) + "");
                        FVContentFragment.this.cameraManager.setExposureCompensation(scaBright);
                    }
                }
                FVContentFragment.this.curScrollScaleEVOld = FVContentFragment.this.curScrollScaleEV;
            }
        });
        this.scaleScrollViewWT = (HorizontalScaleScrollViewWT) this.view.findViewById(C0853R.C0855id.horizontalScaleWT);
        this.scaleScrollViewWT.setOnScrollListener(new BaseScaleViewWT.OnScrollListener() {
            public void onScaleScroll(int scale) {
                if (CameraUtils.getCameraHandModel() && FVContentFragment.this.content_layout_scale.getVisibility() == 0) {
                    double sca = (double) scale;
                    FVContentFragment.this.setScaleLinearImageLeftRightVisibleOrGone(scale, Util.getDrawScaleWTMax());
                    FVContentFragment.this.curScrollScaleWT = (int) sca;
                    if (FVContentFragment.this.curScrollScaleWTOld != FVContentFragment.this.curScrollScaleWT) {
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            FVContentFragment.this.setTopBarFragmentStatusRestart();
                        }
                        if (FVContentFragment.this.startWT.booleanValue()) {
                            FVContentFragment.this.horizontal_scale_wt_textview.setText(String.valueOf((sca + 10.0d) / 10.0d));
                            if (Math.abs(System.currentTimeMillis() - CameraUtils.getCurrentTimeMillisHandModel()) > 100) {
                                float pro = ((float) (sca + 10.0d)) / 10.0f;
                                FVContentFragment.this.cameraManager.setZoom(pro);
                                CameraUtils.setZoomValue(pro);
                            }
                        }
                    }
                    FVContentFragment.this.curScrollScaleWTOld = FVContentFragment.this.curScrollScaleWT;
                }
            }
        });
        this.scaleScrollViewShutter.setVisibility(0);
        this.scaleScrollViewIsoTwo.setVisibility(0);
        this.horizontalScaleWbTwo.setVisibility(0);
        this.scaleScrollViewMf.setVisibility(0);
        this.scaleScrollViewWT.setVisibility(0);
        this.scaleScrollViewShutter.setVisibility(8);
        this.scaleScrollViewIsoTwo.setVisibility(8);
        this.horizontalScaleWbTwo.setVisibility(8);
        this.scaleScrollViewMf.setVisibility(8);
        this.scaleScrollViewWT.setVisibility(8);
        this.scaleScrollViewEV.setVisibility(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVContentFragment.this.scaleScrollViewShutter.setCurScale(0);
                FVContentFragment.this.horizontal_scale_shutter_textview.setText(CameraUtils.getScaleScrollShutterAuto());
                FVContentFragment.this.scaleScrollViewEV.setCurScale((int) (CameraUtils.getScaleScrollViewEVMaxNums() * 10.0d));
                Boolean unused = FVContentFragment.this.startEV = true;
                FVContentFragment.this.horizontal_scale_iso_textview.setText(FVContentFragment.this.cameraManager.getISOValue() + "");
                FVContentFragment.this.horizontal_scale_mf_textview.setText(CameraUtils.strSubTwoLength(String.valueOf(FVContentFragment.this.cameraManager.getFocusDistance())) + "");
            }
        }, 220);
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_shutter)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(0);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                    if (!FVContentFragment.this.startShutter.booleanValue()) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVContentFragment.this.scaleScrollViewShutter.setCurScale(0);
                                Boolean unused = FVContentFragment.this.startShutter = true;
                            }
                        }, 200);
                    } else {
                        Boolean unused = FVContentFragment.this.startShutter = false;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVContentFragment.this.scaleScrollViewShutter.setCurScale(0);
                                Boolean unused = FVContentFragment.this.startShutter = true;
                            }
                        }, 200);
                    }
                } else if (!FVContentFragment.this.startShutter.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int getScaleShutterNum;
                            Range<Long> range = CameraUtils.getScaleShutterRange();
                            if (range != null) {
                                long ranMax = range.getUpper().longValue();
                                if (ranMax < 500000000) {
                                    getScaleShutterNum = (int) (ranMax / FVContentFragment.this.cameraManager.getExposureTime());
                                } else {
                                    getScaleShutterNum = (int) (1000000000 / FVContentFragment.this.cameraManager.getExposureTime());
                                }
                                FVContentFragment.this.scaleScrollViewShutter.setCurScale(Util.getDrawScaleShutterNumNear(getScaleShutterNum) * 10);
                                Boolean unused = FVContentFragment.this.startShutter = true;
                            }
                        }
                    }, 200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int getScaleShutterNum;
                            Range<Long> range = CameraUtils.getScaleShutterRange();
                            if (range != null) {
                                long ranMax = range.getUpper().longValue();
                                if (ranMax < 500000000) {
                                    getScaleShutterNum = (int) (ranMax / FVContentFragment.this.cameraManager.getExposureTime());
                                } else {
                                    getScaleShutterNum = (int) (1000000000 / FVContentFragment.this.cameraManager.getExposureTime());
                                }
                                FVContentFragment.this.scaleScrollViewShutter.setCurScale(Util.getDrawScaleShutterNumNear(getScaleShutterNum) * 10);
                                Boolean unused = FVContentFragment.this.startShutter = true;
                            }
                        }
                    }, 200);
                }
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_shutter_textview, FVContentFragment.this.horizontal_scale_shutter_text_title);
            }
        });
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_iso)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(0);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                if (!FVContentFragment.this.cameraManager.isManualExposure()) {
                    if (!FVContentFragment.this.startISO.booleanValue()) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(0);
                                Boolean unused = FVContentFragment.this.startISO = true;
                            }
                        }, 200);
                    } else {
                        Boolean unused = FVContentFragment.this.startISO = false;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(0);
                                Boolean unused = FVContentFragment.this.startISO = true;
                            }
                        }, 200);
                    }
                } else if (!FVContentFragment.this.startISO.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(Util.getDrawScaleISONumNear(FVContentFragment.this.cameraManager.getISOValue()) * 10);
                            Boolean unused = FVContentFragment.this.startISO = true;
                        }
                    }, 200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.scaleScrollViewIsoTwo.setCurScale(Util.getDrawScaleISONumNear(FVContentFragment.this.cameraManager.getISOValue()) * 10);
                        }
                    }, 300);
                }
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_iso_textview, FVContentFragment.this.horizontal_scale_iso_text_title);
            }
        });
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_wb)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                if (!FVContentFragment.this.startWB.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.horizontalScaleWbTwo.setCurScale(40);
                            Boolean unused = FVContentFragment.this.startWB = true;
                        }
                    }, 200);
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(0);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_wb_textview, FVContentFragment.this.horizontal_scale_wb_text_title);
            }
        });
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_mf)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(0);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                if (!FVContentFragment.this.startMF.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.scaleScrollViewMf.setCurScaleFirst(0);
                            Boolean unused = FVContentFragment.this.startMF = true;
                        }
                    }, 200);
                }
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_mf_textview, FVContentFragment.this.horizontal_scale_mf_text_title);
            }
        });
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_wt)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(0);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(8);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                if (!FVContentFragment.this.startWT.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.scaleScrollViewWT.setCurScale(0);
                            Boolean unused = FVContentFragment.this.startWT = true;
                        }
                    }, 200);
                }
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_wt_textview, FVContentFragment.this.horizontal_scale_wt_text_title);
            }
        });
        ((LinearLayout) this.view.findViewById(C0853R.C0855id.fragment_content_scale_linear_ev)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVContentFragment.this.setTopBarFragmentStatusRestart();
                }
                FVContentFragment.this.scaleScrollViewShutter.setVisibility(8);
                FVContentFragment.this.scaleScrollViewIsoTwo.setVisibility(8);
                FVContentFragment.this.horizontalScaleWbTwo.setVisibility(8);
                FVContentFragment.this.scaleScrollViewMf.setVisibility(8);
                FVContentFragment.this.scaleScrollViewWT.setVisibility(8);
                FVContentFragment.this.scaleScrollViewEV.setVisibility(0);
                FVContentFragment.this.setContentHandModelVisiStateValue();
                if (!FVContentFragment.this.startEV.booleanValue()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVContentFragment.this.scaleScrollViewEV.setCurScale((int) (CameraUtils.getScaleScrollViewEVMaxNums() * 10.0d));
                            Boolean unused = FVContentFragment.this.startEV = true;
                        }
                    }, 200);
                }
                FVContentFragment.this.setTextColorWhiteAndYellow(FVContentFragment.this.horizontal_scale_ev_textview, FVContentFragment.this.horizontal_scale_ev_text_title);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Range<Long> rangeShutter = FVContentFragment.this.cameraManager.getExposureTimeRange();
                rangeShutter.getLower();
                rangeShutter.getUpper();
            }
        }, 200);
        this.cameraManager.setCameraStateListner(new Camera2Manager.CameraStatesListener() {
            private double mfScale;

            public void isoValue(Integer isovalue) {
                FVContentFragment.this.curScrollScaleGetISO = isovalue.intValue();
                if (FVContentFragment.this.curScrollScaleGetISOOld != FVContentFragment.this.curScrollScaleGetISO && !FVContentFragment.this.cameraManager.isManualExposure()) {
                    FVContentFragment.this.sendToHandlerScale(32, isovalue);
                }
                FVContentFragment.this.curScrollScaleGetISOOld = FVContentFragment.this.curScrollScaleGetISO;
            }

            public void exposureTime(Long exposuretime) {
                FVContentFragment.this.sendToHandlerScale(31, exposuretime);
            }

            public void exposureCompensation(Integer ev) {
            }

            public void fpsRange(Range<Integer> range) {
            }

            public void frameDuration(Long duration) {
            }

            public void colorCorrectionGains(RggbChannelVector vector) {
                int vet = com.freevisiontech.cameralib.utils.CameraUtils.FactorFromWbTemperature(vector);
                FVContentFragment.this.curScrollScaleGetWB = vet;
                if (FVContentFragment.this.curScrollScaleGetWBOld != FVContentFragment.this.curScrollScaleGetWB && FVContentFragment.this.curScrollScaleWB < 41) {
                    FVContentFragment.this.sendToHandlerScale(33, Integer.valueOf(vet));
                }
                FVContentFragment.this.curScrollScaleGetWBOld = FVContentFragment.this.curScrollScaleGetWB;
            }

            public void focalLen(Float len) {
            }

            public void focusDistance(Float focusdistance) {
                String mfStr = String.valueOf(focusdistance);
                if (mfStr.length() > 3) {
                    if (mfStr.substring(0, mfStr.indexOf(".")).length() > 1) {
                        mfStr = mfStr.substring(0, 5);
                    } else {
                        mfStr = mfStr.substring(0, 4);
                    }
                }
                if (!FVContentFragment.this.cameraManager.isMaunalFocus()) {
                    FVContentFragment.this.sendToHandlerScale(34, mfStr);
                }
            }

            public void modes(Integer af, Integer ae, Integer awb, Integer flash, Integer control) {
            }

            public void states(Integer af, Integer ae, Integer awb, Integer flash) {
            }

            public void lenState(Integer state) {
            }

            public void aelocked(Boolean locked) {
            }

            public void aftrigger(Integer triggered) {
            }

            public void awblocked(Boolean awblocked) {
            }
        });
    }

    /* access modifiers changed from: private */
    public void setContentHandModelVisiStateValue() {
        if (this.content_layout_scale != null) {
            CameraUtils.setHandModelVisibleStateValue(1);
            this.iv_hand_model_scroll_draw_pointer.setVisibility(8);
            if (CameraUtils.getCameraHandModel() && this.content_layout_scale.getVisibility() == 0) {
                if (this.scaleScrollViewEV.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(1);
                } else if (this.scaleScrollViewShutter.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(2);
                } else if (this.scaleScrollViewIsoTwo.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(3);
                } else if (this.horizontalScaleWbTwo.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(4);
                } else if (this.scaleScrollViewMf.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(5);
                } else if (this.scaleScrollViewWT.getVisibility() == 0) {
                    CameraUtils.setHandModelVisibleStateValue(6);
                }
                if (CameraUtils.getHandModelVisibleStateValue() == 4.0f || CameraUtils.getHandModelVisibleStateValue() == 5.0f || CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            int hight = FVContentFragment.this.scaleScrollViewWT.getHeight();
                            if (CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                                hight = FVContentFragment.this.scaleScrollViewWT.getHeight();
                            } else if (CameraUtils.getHandModelVisibleStateValue() == 5.0f) {
                                hight = FVContentFragment.this.scaleScrollViewMf.getHeight();
                            } else if (CameraUtils.getHandModelVisibleStateValue() == 4.0f) {
                                hight = FVContentFragment.this.horizontalScaleWbTwo.getHeight();
                            }
                            int topLL = (Util.dip2px(FVContentFragment.this.mContext, 90.0f) - hight) / 2;
                            FVContentFragment.this.iv_hand_model_scroll_draw_pointer.setVisibility(0);
                            int left = (CameraUtils.getMidCountScale() * CameraUtils.getScaleMargin()) + Util.dip2px(FVContentFragment.this.mContext, 17.0f);
                            RelativeLayout.LayoutParams lpsScale = (RelativeLayout.LayoutParams) FVContentFragment.this.iv_hand_model_scroll_draw_pointer.getLayoutParams();
                            lpsScale.setMargins(left - 1, (((((CameraUtils.getRectHeight() - CameraUtils.getScaleMaxHeight()) - CameraUtils.getScaleHeight()) - 5) - 10) + topLL) - 1, 0, 0);
                            FVContentFragment.this.iv_hand_model_scroll_draw_pointer.setLayoutParams(lpsScale);
                        }
                    }, 10);
                } else {
                    this.iv_hand_model_scroll_draw_pointer.setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setCameraHandModelBgColor(boolean isYellow) {
        int mStartColor;
        int mEndColor;
        if (CameraUtils.getCurrentPageIndex() == 2 && ((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
            this.horizontal_scale_shutter_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_shutter_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_iso_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_iso_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_wb_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_wb_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_mf_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_mf_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_ev_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_ev_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_wt_textview.setTextColor(getResources().getColor(C0853R.color.white));
            this.horizontal_scale_wt_text_title.setTextColor(getResources().getColor(C0853R.color.white));
            if (isYellow) {
                mStartColor = getResources().getColor(C0853R.color.color_shutter_iso_color);
                mEndColor = getResources().getColor(C0853R.color.white);
                if (this.scaleScrollViewEV.getVisibility() == 0) {
                    this.horizontal_scale_ev_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_ev_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                } else if (this.scaleScrollViewShutter.getVisibility() == 0) {
                    this.horizontal_scale_shutter_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_shutter_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                } else if (this.scaleScrollViewIsoTwo.getVisibility() == 0) {
                    this.horizontal_scale_iso_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_iso_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                } else if (this.horizontalScaleWbTwo.getVisibility() == 0) {
                    this.horizontal_scale_wb_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_wb_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                } else if (this.scaleScrollViewMf.getVisibility() == 0) {
                    this.horizontal_scale_mf_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_mf_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                } else if (this.scaleScrollViewWT.getVisibility() == 0) {
                    this.horizontal_scale_wt_textview.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                    this.horizontal_scale_wt_text_title.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
                }
            } else {
                mStartColor = getResources().getColor(C0853R.color.gray_be);
                mEndColor = getResources().getColor(C0853R.color.white);
                if (this.scaleScrollViewEV.getVisibility() == 0) {
                    this.horizontal_scale_ev_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_ev_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                } else if (this.scaleScrollViewShutter.getVisibility() == 0) {
                    this.horizontal_scale_shutter_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_shutter_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                } else if (this.scaleScrollViewIsoTwo.getVisibility() == 0) {
                    this.horizontal_scale_iso_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_iso_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                } else if (this.horizontalScaleWbTwo.getVisibility() == 0) {
                    this.horizontal_scale_wb_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_wb_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                } else if (this.scaleScrollViewMf.getVisibility() == 0) {
                    this.horizontal_scale_mf_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_mf_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                } else if (this.scaleScrollViewWT.getVisibility() == 0) {
                    this.horizontal_scale_wt_textview.setTextColor(getResources().getColor(C0853R.color.gray_be));
                    this.horizontal_scale_wt_text_title.setTextColor(getResources().getColor(C0853R.color.gray_be));
                }
            }
            this.scaleScrollViewEV.setColor(mStartColor, mEndColor);
            this.scaleScrollViewShutter.setColor(mStartColor, mEndColor);
            this.scaleScrollViewIsoTwo.setColor(mStartColor, mEndColor);
            this.horizontalScaleWbTwo.setColor(mStartColor, mEndColor);
            this.scaleScrollViewMf.setColor(mStartColor, mEndColor);
            this.scaleScrollViewWT.setColor(mStartColor, mEndColor);
        }
    }

    /* access modifiers changed from: private */
    public void setTopBarFragmentStatusRestart() {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (CameraUtils.getLabelTopBarSelect() != -1) {
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
            }
            if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setScaleLinearImageLeftRightVisibleOrGone(int scale, int max) {
        this.frag_content_scale_linear_image_left.setVisibility(0);
        this.frag_content_scale_linear_image_right.setVisibility(0);
        if (scale < 21) {
            this.frag_content_scale_linear_image_left.setVisibility(8);
            this.frag_content_scale_linear_image_right.setVisibility(0);
        } else if (scale > max - 21) {
            this.frag_content_scale_linear_image_left.setVisibility(0);
            this.frag_content_scale_linear_image_right.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void setTextColorWhiteAndYellow(TextView textOne, TextView textTwo) {
        this.horizontal_scale_shutter_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_shutter_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_iso_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_iso_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_wb_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_wb_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_mf_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_mf_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_ev_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_ev_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_wt_textview.setTextColor(getResources().getColor(C0853R.color.white));
        this.horizontal_scale_wt_text_title.setTextColor(getResources().getColor(C0853R.color.white));
        textOne.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
        textTwo.setTextColor(getResources().getColor(C0853R.color.color_shutter_iso_color));
    }

    /* access modifiers changed from: private */
    public void setWB(String mode) {
        this.cameraManager.setWhiteBalance(mode);
    }

    class CameraZoomThread extends Thread {
        CameraZoomThread() {
        }

        public void run() {
            Looper.prepare();
            Handler unused = FVContentFragment.this.myMixHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 81:
                            float progress = ((Float) msg.obj).floatValue();
                            String pro = String.valueOf(progress);
                            if (pro.substring(pro.indexOf("."), pro.length()).length() > 3) {
                                String substring = pro.substring(0, pro.indexOf(".") + 3);
                            } else {
                                String proNew = pro;
                            }
                            Log.e("--------------", "--------2343 Zoom Progress SEND_TO_HANDLER_MIX_ONE  收到 -------  progress:" + progress);
                            if (!CameraUtils.getCameraHandModel() || FVContentFragment.this.content_layout_scale.getVisibility() != 0) {
                                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 2) {
                                    if (progress <= 1.02f) {
                                        progress = 1.0f;
                                    }
                                } else if (progress <= 0.0f) {
                                    progress = 0.0f;
                                }
                                long timeMillis = System.currentTimeMillis();
                                if (Math.abs(timeMillis - CameraUtils.getCurrentTimeMillis()) > 33) {
                                    CameraUtils.setCurrentTimeMillis(timeMillis);
                                    FVContentFragment.this.cameraManager.setZoom(progress);
                                    CameraUtils.setZoomValue(progress);
                                    return;
                                }
                                return;
                            }
                            Log.e("--------------", "--------2343 Zoom Progress  210  210 -------  progress:" + progress);
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 2) {
                                    if (progress <= 1.02f) {
                                        progress = 1.0f;
                                    } else if (progress > CameraUtils.getAppMaxZoom()) {
                                        progress = CameraUtils.getAppMaxZoom();
                                    }
                                } else if (progress <= 0.0f) {
                                    progress = 0.0f;
                                }
                                long timeMillis2 = System.currentTimeMillis();
                                if (Math.abs(timeMillis2 - CameraUtils.getCurrentTimeMillisHandModel()) > 33) {
                                    CameraUtils.setCurrentTimeMillisHandModel(timeMillis2);
                                    CameraUtils.setZoomValue(progress);
                                    int progNew = (int) ((progress - 1.0f) * 10.0f);
                                    FVContentFragment.this.curScrollScaleWT = progNew;
                                    if (CameraUtils.getHandModelVisibleStateValue() == 5.0f) {
                                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE_210, String.valueOf(progNew));
                                        return;
                                    } else if (CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                                        FVContentFragment.this.cameraManager.setZoom(progress);
                                        Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE_210, String.valueOf(progNew));
                                        return;
                                    } else {
                                        FVContentFragment.this.cameraManager.setZoom(progress);
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE, String.valueOf((int) (progress - 1.0f)));
                                return;
                            }
                        case 82:
                            float progress2 = ((Float) msg.obj).floatValue();
                            Log.e("--------------", "-------- Zoom Progress -------  progress2:" + progress2);
                            if (!CameraUtils.getCameraHandModel() || FVContentFragment.this.content_layout_scale.getVisibility() != 0) {
                                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 2) {
                                    if (progress2 <= 1.02f) {
                                        progress2 = 1.0f;
                                    }
                                } else if (progress2 <= 0.0f) {
                                    progress2 = 0.0f;
                                }
                                FVContentFragment.this.cameraManager.setZoom(progress2);
                                CameraUtils.setZoomValue(progress2);
                                return;
                            }
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE, String.valueOf((int) ((progress2 - 1.0f) * 10.0f)));
                            return;
                        default:
                            return;
                    }
                }
            };
            Looper.loop();
        }
    }

    class CameraZoomExpoThread extends Thread {
        CameraZoomExpoThread() {
        }

        public void run() {
            Looper.prepare();
            Handler unused = FVContentFragment.this.expoMixHandler = new Handler() {
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 17:
                            int curBright = FVContentFragment.this.getCurBright(((Integer) msg.obj).intValue());
                            int rangerArrayOne = Math.abs(FVContentFragment.this.cameraManager.getExposureCompensationRanger()[1]);
                            if (CameraUtils.getCameraHandModel()) {
                                Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_ZOOM, String.valueOf((curBright + rangerArrayOne) * 10));
                                return;
                            } else {
                                FVContentFragment.this.cameraManager.setExposureCompensation(curBright);
                                return;
                            }
                        default:
                            return;
                    }
                }
            };
            Looper.loop();
        }
    }

    public void onResume() {
        super.onResume();
        this.onResume = true;
        this.surfaceView.onResume();
        this.broad = new OrientationBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenOrientationUtil.BC_OrientationChanged);
        getActivity().registerReceiver(this.broad, filter);
        getAngle();
        this.curBright = 0;
        this.curBrightProgress = 50;
        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE))).intValue() != 107772) {
            initGradienterView();
        }
    }

    private void initGradienterView() {
        this.gradienterColorYellow = true;
        this.iv_gradienter_white.setVisibility(0);
        this.iv_gradienter_bg.setVisibility(0);
        if (this.mOrEventAngleListener == null) {
            initOrientationEventAngleListener();
        }
        this.mOrEventAngleListener.enable();
        SPUtils.put(this.mContext, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_OPEN));
    }

    private void removeGradienterView() {
        this.iv_gradienter_white.setVisibility(8);
        this.iv_gradienter_bg.setVisibility(8);
        if (this.mOrEventAngleListener != null) {
            this.mOrEventAngleListener.disable();
        }
        SPUtils.put(this.mContext, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE));
        this.gradienterColorYellow = true;
    }

    private void initOrientationEventAngleListener() {
        this.mOrEventAngleListener = new OrientationEventListener(this.mContext) {
            public void onOrientationChanged(int rotation) {
                Log.e("---------------", "-----------  5878  9685  1898  8958 传感器角度   rotation: " + rotation + "      angle: " + FVContentFragment.this.angle);
                if (rotation == -1) {
                    FVContentFragment.this.iv_gradienter_bg.setRotation(0.0f);
                }
                if (rotation == 0 || rotation == 90 || rotation == 180 || rotation == 270 || rotation == -1) {
                    FVContentFragment.this.iv_gradienter_white.setImageDrawable((Drawable) null);
                    FVContentFragment.this.iv_gradienter_bg.setImageDrawable((Drawable) null);
                    FVContentFragment.this.iv_gradienter_white.setBackgroundResource(C0853R.mipmap.ic_gradienter_yellow);
                    FVContentFragment.this.iv_gradienter_bg.setBackgroundResource(C0853R.mipmap.ic_gradienter_yellow_bg);
                    boolean unused = FVContentFragment.this.gradienterColorYellow = true;
                } else {
                    if (FVContentFragment.this.gradienterColorYellow) {
                        FVContentFragment.this.iv_gradienter_white.setImageDrawable((Drawable) null);
                        FVContentFragment.this.iv_gradienter_bg.setImageDrawable((Drawable) null);
                    }
                    FVContentFragment.this.iv_gradienter_white.setBackgroundResource(C0853R.mipmap.ic_gradienter_white);
                    FVContentFragment.this.iv_gradienter_bg.setBackgroundResource(C0853R.mipmap.ic_gradienter_white_bg);
                    boolean unused2 = FVContentFragment.this.gradienterColorYellow = false;
                }
                if (FVContentFragment.this.angle == 270 || FVContentFragment.this.angle == 90) {
                    if (FVContentFragment.this.iv_gradienter_bg.getRotation() != 90.0f) {
                        FVContentFragment.this.iv_gradienter_bg.setRotation(90.0f);
                    }
                    if (rotation == -1) {
                        rotation = 0;
                    }
                } else {
                    if (FVContentFragment.this.iv_gradienter_bg.getRotation() != 0.0f) {
                        FVContentFragment.this.iv_gradienter_bg.setRotation(0.0f);
                    }
                    if (rotation == -1) {
                        rotation = 270;
                    }
                }
                FVContentFragment.this.iv_gradienter_white.setRotation((float) (270 - rotation));
            }
        };
    }

    private void setCameraHandModelShortCutAuto() {
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_FALSE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (FVContentFragment.this.cameraManager != null && FVContentFragment.this.cameraManager.isCameraOpened()) {
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
                }
            }
        }, 30);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0013, code lost:
        r0 = com.freevisiontech.fvmobile.utility.CameraUtils.getScaleShutterRange();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setCameraHandModelShortCut() {
        /*
            r6 = this;
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$21 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$21
            r2.<init>()
            r4 = 150(0x96, double:7.4E-322)
            r1.postDelayed(r2, r4)
            int r1 = r6.curScrollScaleShutter
            if (r1 == 0) goto L_0x0028
            android.util.Range r0 = com.freevisiontech.fvmobile.utility.CameraUtils.getScaleShutterRange()
            if (r0 == 0) goto L_0x0028
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$22 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$22
            r2.<init>(r0)
            r4 = 300(0x12c, double:1.48E-321)
            r1.postDelayed(r2, r4)
        L_0x0028:
            int r1 = r6.curScrollScaleISO
            if (r1 == 0) goto L_0x003b
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$23 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$23
            r2.<init>()
            r4 = 200(0xc8, double:9.9E-322)
            r1.postDelayed(r2, r4)
        L_0x003b:
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$24 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$24
            r2.<init>()
            r4 = 10
            r1.postDelayed(r2, r4)
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$25 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$25
            r2.<init>()
            r4 = 50
            r1.postDelayed(r2, r4)
            android.os.Handler r1 = new android.os.Handler
            r1.<init>()
            com.freevisiontech.fvmobile.fragment.FVContentFragment$26 r2 = new com.freevisiontech.fvmobile.fragment.FVContentFragment$26
            r2.<init>()
            r4 = 100
            r1.postDelayed(r2, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.fragment.FVContentFragment.setCameraHandModelShortCut():void");
    }

    public void onPause() {
        super.onPause();
        if (this.rlFocus != null) {
            this.rlFocus.setVisibility(8);
        }
        this.seekbarFocalLength.setProgress(0);
        this.ic_seekbar_content_thumb_bubble_text.setText(BuildConfig.VERSION_NAME);
        CameraUtils.setCamLengthProgress(0);
        CameraUtils.setCamWtProRealTimeValue(0);
        this.onResume = false;
        this.surfaceView.onPause();
        if (this.broad != null) {
            getActivity().unregisterReceiver(this.broad);
        }
        if (this.mOrEventAngleListener != null) {
            this.mOrEventAngleListener.disable();
        }
        if (this.countTimer != null) {
            this.countTimer.cancel();
            this.tvTimer.setText("");
        }
        if (this.hitchCockCountTimer != null) {
            this.hitchCockCountTimer.cancel();
            this.tvTimer.setText("");
        }
        if (CameraUtils.isFullShotIng()) {
            cancelFullShot();
        }
        Log.e("-------------", "---------  onPause  onPause  onPause  onPause  ------");
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            this.requestObject = PermissionUtil.with((Fragment) this).request("android.permission.CAMERA").onAllGranted(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                    FVContentFragment.this.initCamera();
                }
            }).onAnyDenied(new Func() {
                /* access modifiers changed from: protected */
                public void call() {
                }
            }).ask(25);
        } else {
            initCamera();
        }
    }

    /* access modifiers changed from: private */
    public void initCamera() {
        if (this.useCamera2) {
            initCamera2();
        } else {
            initCamera1();
        }
    }

    /* access modifiers changed from: private */
    public void cameraReatartSetValue() {
        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (((Integer) SPUtils.get(FVContentFragment.this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                        SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
                    }
                    if (((Integer) SPUtils.get(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019) {
                        FVContentFragment.this.resetWhiteBalance();
                    }
                }
            }, 150);
            if (!(((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() == 107212 || this.cameraManager == null || !this.cameraManager.isCameraOpened())) {
                setCameraHandModelShortCutAuto();
            }
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_PREVENT_JITTER_YES_NO, Integer.valueOf(Constants.CAMERA_PREVENT_JITTER_FALSE))).intValue() == 107017) {
                Util.sendIntEventMessge(Constants.CAMERA_PREVENT_JITTER_OPEN_CLOSE);
            }
        }
    }

    private void initCamera1() {
        this.cameraManager = FVCameraManager.CreateCamera1Manager(getActivity(), this.surfaceView.toCamera1view(), new FVCamereManagerCallback() {
            public void onCameraDisconnected() {
            }

            public void onCameraError(int err) {
            }

            public void onCameraClosed() {
            }

            public void onCameraStarted() {
                Size picsize;
                if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    if (CameraUtils.getMaxSupOrReComPictureFrontSize() == 0) {
                        picsize = FVContentFragment.this.cameraManager.getRecommendPictureSize();
                    } else {
                        picsize = FVContentFragment.this.cameraManager.getMaxSupportedPictureSize();
                    }
                } else if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                    picsize = FVContentFragment.this.cameraManager.getRecommendPictureSize();
                } else {
                    picsize = FVContentFragment.this.cameraManager.getMaxSupportedPictureSize();
                }
                FVContentFragment.this.cameraManager.setPictureResolution(picsize);
                CameraUtils.setAppMaxZoom(FVContentFragment.this.cameraManager.getMaxZoom());
                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1) {
                    double maxZoom = (double) FVContentFragment.this.cameraManager.getMaxZoom();
                    CameraUtils.setScaleScrollViewTradWTMaxNums((double) FVContentFragment.this.cameraManager.getMaxZoom());
                } else {
                    CameraUtils.setScaleScrollViewWTMaxNums(((double) FVContentFragment.this.cameraManager.getMaxZoom()) - 1.0d);
                }
                CameraUtils.setScaleScrollViewMFMaxNums((double) (FVContentFragment.this.cameraManager.getMinFocusDistance() + 1.0f));
                CameraUtils.setMediaRecordSize(FVContentFragment.this.cameraManager.getSupportedMediaRecordQuality());
                FVContentFragment.this.cameraReatartSetValue();
            }

            public void onPictureTaken(Bitmap bitmap) {
                File pictureFile;
                String str;
                File pictureFile2;
                CameraUtils.setHasTakePhotoOrVideo(true);
                int fullMode = ((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                boolean moveTimeLapseMode = ((Boolean) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.MOVE_TIME_LAPSE_PHOTO_MODE, false)).booleanValue();
                System.gc();
                if (fullMode == 10024) {
                    if (moveTimeLapseMode) {
                        pictureFile2 = Util.getOutputMediaFile(1, 2, FVContentFragment.this.mContext);
                    } else {
                        pictureFile2 = Util.getOutputMediaFile(1, 1, FVContentFragment.this.mContext);
                    }
                    if (pictureFile2 != null) {
                        try {
                            if (FVContentFragment.this.cameraManager.getCurrentCameraMode() == 0 || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.BRAND.toLowerCase()) || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.MANUFACTURER.toLowerCase())) {
                                bitmap = Util.rotateBitmapJudgment(FVContentFragment.this.getActivity(), bitmap);
                            }
                            FileOutputStream fos = new FileOutputStream(pictureFile2);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (FileNotFoundException e) {
                            Log.d("ASDF", "File not found: " + e.getMessage());
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (IOException e2) {
                            Log.d("ASDF", "Error accessing file: " + e2.getMessage());
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (Throwable th) {
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                            } else {
                                MoveTimelapseUtil.getInstance();
                                MoveTimelapseUtil.setTakenPictrueCommun(true);
                                Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                            }
                            throw th;
                        }
                    }
                } else {
                    if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_SAVE_YES_NO, Integer.valueOf(Constants.PANAMA_PHOTO_SAVE_BOOLEAN_FALSE))).intValue() == 107020) {
                        pictureFile = Util.getOutputMediaFile(1, 1, FVContentFragment.this.mContext);
                        CameraUtils.setHasTakePhotoOrVideo(true);
                    } else {
                        pictureFile = Util.getOutputMediaFile(1, 2, FVContentFragment.this.mContext);
                    }
                    if (pictureFile != null) {
                        try {
                            if (FVContentFragment.this.cameraManager.getCurrentCameraMode() == 0 || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.BRAND.toLowerCase()) || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.MANUFACTURER.toLowerCase())) {
                                bitmap = Util.rotateBitmapJudgment(FVContentFragment.this.getActivity(), bitmap);
                            }
                            FileOutputStream fos2 = new FileOutputStream(pictureFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
                            fos2.flush();
                            fos2.close();
                        } catch (FileNotFoundException e4) {
                            Log.d("ASDF", "File not found: " + e4.getMessage());
                        } catch (IOException e5) {
                            Log.d("ASDF", "Error accessing file: " + e5.getMessage());
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        } finally {
                            str = "image/*";
                            Util.updateGallery(FVContentFragment.this.getContext(), pictureFile.getPath(), str);
                        }
                        Util.sendIntEventMessge((int) Constants.TAKE_PHOTO_OVER_TO_BLE, pictureFile.getPath());
                    }
                }
            }

            public void onPictureTakeError(int reason) {
            }

            public void onPictureTakeCompleted() {
            }

            public void onRawPictureTaken(Image image, DngCreator dngCreator) {
            }

            public void onRecordStarted() {
                FVContentFragment.this.changeWirelessChargeOn(false);
                Util.sendIntEventMessge(Constants.START_TAKE_VIDEO);
                FVContentFragment.this.startTimer();
                Log.e("--------------", "-------- onRecord  Started  Started ------");
                CameraUtils.setBooRecordStarted(true);
                FVContentFragment.this.setRecordingResolutionFpsUI(true);
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }

            public void onRecordError(String reason) {
                FVContentFragment.this.changeWirelessChargeOn(true);
                Util.sendIntEventMessge(Constants.HAVE_TAKE_VIDEO_OVER);
                FVContentFragment.this.timer.stop();
                FVContentFragment.this.timer.setVisibility(8);
                FVContentFragment.this.timer_horizontal.stop();
                FVContentFragment.this.timer_horizontal.setVisibility(8);
                FVContentFragment.this.timer.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.timer_horizontal.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.chro_timer.stop();
                FVContentFragment.this.linear_timer.setVisibility(8);
                MoveTimelapseUtil.getInstance().setIsHitchCockRecord(0);
                CameraUtils.setIsHitchCockRecordUI(false);
                FVContentFragment.this.setCameraHandModelUIVideoVisibleOrGone(false);
                FVContentFragment.this.timer_size_all.setVisibility(8);
                FVContentFragment.this.timer_size_all_view.setVisibility(8);
                FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                if (FVContentFragment.this.mMyRecordingCountDownTimer != null) {
                    FVContentFragment.this.mMyRecordingCountDownTimer.cancel();
                }
                FVContentFragment.this.stopScreenBrightnessTimeToDark();
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraVideoSymbolStart(0);
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 && MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                    BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 0);
                }
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(0);
                Log.e("--------------", "-------- onRecord Error Error ------");
                CameraUtils.setBooRecordStarted(false);
                Util.sendIntEventMessge(Constants.BTN_LENS_SWITCH_VISIBLE);
                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1 && "Redmi Note 5".equals(Util.getSystemModel())) {
                    BackgroundMusic.getInstance(FVContentFragment.this.getContext()).playRecordSound(FVContentFragment.this.getContext(), "record_end");
                }
                FVContentFragment.this.setRecordingResolutionFpsUI(false);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                }
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }

            public void onRecordEnd(String path) {
                FVContentFragment.this.changeWirelessChargeOn(true);
                ViseLog.m1466e("callback onRecordEnd");
                CameraUtils.setBooRecordStarted(false);
                CameraUtils.setHasTakePhotoOrVideo(true);
                FVContentFragment.this.timer.stop();
                FVContentFragment.this.timer.setVisibility(8);
                FVContentFragment.this.timer_horizontal.stop();
                FVContentFragment.this.timer_horizontal.setVisibility(8);
                FVContentFragment.this.timer.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.timer_horizontal.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.chro_timer.stop();
                FVContentFragment.this.linear_timer.setVisibility(8);
                MoveTimelapseUtil.getInstance().setIsHitchCockRecord(0);
                CameraUtils.setIsHitchCockRecordUI(false);
                FVContentFragment.this.setCameraHandModelUIVideoVisibleOrGone(false);
                FVContentFragment.this.timer_size_all.setVisibility(8);
                FVContentFragment.this.timer_size_all_view.setVisibility(8);
                FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                if (FVContentFragment.this.mMyRecordingCountDownTimer != null) {
                    FVContentFragment.this.mMyRecordingCountDownTimer.cancel();
                }
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 && MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                    BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 0);
                }
                String path2 = (String) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CURRENT_VIDEO_PATH, "");
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 0) {
                    path2 = (String) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CURRENT_VIDEO_PATH, "");
                } else if (CameraUtils.getMoveOrDelayTimeLapsePath() != null) {
                    path2 = CameraUtils.getMoveOrDelayTimeLapsePath();
                }
                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_VIDEO_OVER, path2);
                Util.updateGalleryForVideo(FVContentFragment.this.getActivity(), path2);
                FVContentFragment.this.stopScreenBrightnessTimeToDark();
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraVideoSymbolStart(0);
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(0);
                Util.sendIntEventMessge(Constants.BTN_LENS_SWITCH_VISIBLE);
                Log.e("--------------", "-------- onRecord  End  End ------  path  " + path2);
                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1 && "Redmi Note 5".equals(Util.getSystemModel())) {
                    BackgroundMusic.getInstance(FVContentFragment.this.getContext()).playRecordSound(FVContentFragment.this.getContext(), "record_end");
                }
                FVContentFragment.this.setRecordingResolutionFpsUI(false);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                }
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }
        });
    }

    private void initCamera2() {
        this.cameraManager = FVCameraManager.CreateCamera2Manager(getActivity(), this.surfaceView.toCamera2view(), (CameraParameters) null, new FVCamereManagerCallback() {
            public void onCameraDisconnected() {
            }

            public void onCameraError(int err) {
            }

            public void onCameraClosed() {
            }

            public void onCameraStarted() {
                Size picsize;
                if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    if (CameraUtils.getMaxSupOrReComPictureFrontSize() == 0) {
                        picsize = FVContentFragment.this.cameraManager.getRecommendPictureSize();
                    } else {
                        picsize = FVContentFragment.this.cameraManager.getMaxSupportedPictureSize();
                    }
                } else if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                    picsize = FVContentFragment.this.cameraManager.getRecommendPictureSize();
                } else {
                    picsize = FVContentFragment.this.cameraManager.getMaxSupportedPictureSize();
                }
                FVContentFragment.this.cameraManager.setPictureResolution(picsize);
                CameraUtils.setAppMaxZoom(FVContentFragment.this.cameraManager.getMaxZoom());
                if (FVContentFragment.this.cameraManager.getCameraManagerType() == 1) {
                    double maxZoom = (double) FVContentFragment.this.cameraManager.getMaxZoom();
                    CameraUtils.setScaleScrollViewTradWTMaxNums((double) FVContentFragment.this.cameraManager.getMaxZoom());
                } else {
                    CameraUtils.setScaleScrollViewWTMaxNums(((double) FVContentFragment.this.cameraManager.getMaxZoom()) - 1.0d);
                }
                if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    CameraUtils.setScaleScrollViewMFMaxNums(0.0d);
                } else {
                    CameraUtils.setScaleScrollViewMFMaxNums((double) (FVContentFragment.this.cameraManager.getMinFocusDistance() + 1.0f));
                }
                CameraUtils.setMediaRecordSizeTwo(FVContentFragment.this.cameraManager.getSupportedMediaRecordSizes());
                FVContentFragment.this.cameraReatartSetValue();
            }

            public void onPictureTaken(Bitmap bitmap) {
                File pictureFile;
                String str;
                File pictureFile2;
                CameraUtils.setHasTakePhotoOrVideo(true);
                int fullMode = ((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                boolean moveTimeLapseMode = ((Boolean) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.MOVE_TIME_LAPSE_PHOTO_MODE, false)).booleanValue();
                System.gc();
                if (fullMode == 10024) {
                    if (moveTimeLapseMode) {
                        pictureFile2 = Util.getOutputMediaFile(1, 2, FVContentFragment.this.mContext);
                    } else {
                        pictureFile2 = Util.getOutputMediaFile(1, 1, FVContentFragment.this.mContext);
                    }
                    if (pictureFile2 != null) {
                        try {
                            if (FVContentFragment.this.cameraManager.getCurrentCameraMode() == 0 || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.BRAND.toLowerCase()) || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.MANUFACTURER.toLowerCase())) {
                                bitmap = Util.rotateBitmapJudgment(FVContentFragment.this.getActivity(), bitmap);
                            }
                            FileOutputStream fos = new FileOutputStream(pictureFile2);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (FileNotFoundException e) {
                            Log.d("ASDF", "File not found: " + e.getMessage());
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (IOException e2) {
                            Log.d("ASDF", "Error accessing file: " + e2.getMessage());
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (Exception e3) {
                            e3.printStackTrace();
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                                return;
                            }
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setTakenPictrueCommun(true);
                            Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                        } catch (Throwable th) {
                            if (!moveTimeLapseMode) {
                                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_PHOTO_OVER, pictureFile2.getPath());
                                Util.updateGallery(FVContentFragment.this.getContext(), pictureFile2.getPath(), "image/*");
                            } else {
                                MoveTimelapseUtil.getInstance();
                                MoveTimelapseUtil.setTakenPictrueCommun(true);
                                Util.sendIntEventMessge((int) Constants.MOVE_TIME_LAPSE_TAKE_PHOTO, pictureFile2.getPath());
                            }
                            throw th;
                        }
                    }
                } else {
                    if (((Integer) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_SAVE_YES_NO, Integer.valueOf(Constants.PANAMA_PHOTO_SAVE_BOOLEAN_FALSE))).intValue() == 107020) {
                        pictureFile = Util.getOutputMediaFile(1, 1, FVContentFragment.this.mContext);
                        CameraUtils.setHasTakePhotoOrVideo(true);
                    } else {
                        pictureFile = Util.getOutputMediaFile(1, 2, FVContentFragment.this.mContext);
                    }
                    if (pictureFile != null) {
                        try {
                            if (FVContentFragment.this.cameraManager.getCurrentCameraMode() == 0 || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.BRAND.toLowerCase()) || com.freevisiontech.cameralib.Constants.Camera_Rotation_Exclude.contains(Build.MANUFACTURER.toLowerCase())) {
                                bitmap = Util.rotateBitmapJudgment(FVContentFragment.this.getActivity(), bitmap);
                            }
                            FileOutputStream fos2 = new FileOutputStream(pictureFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
                            fos2.flush();
                            fos2.close();
                        } catch (FileNotFoundException e4) {
                            Log.d("ASDF", "File not found: " + e4.getMessage());
                        } catch (IOException e5) {
                            Log.d("ASDF", "Error accessing file: " + e5.getMessage());
                        } catch (Exception e6) {
                            e6.printStackTrace();
                        } finally {
                            str = "image/*";
                            Util.updateGallery(FVContentFragment.this.getContext(), pictureFile.getPath(), str);
                        }
                        Util.sendIntEventMessge((int) Constants.TAKE_PHOTO_OVER_TO_BLE, pictureFile.getPath());
                    }
                }
            }

            public void onPictureTakeError(int reason) {
            }

            public void onPictureTakeCompleted() {
            }

            public void onRawPictureTaken(Image image, DngCreator dngCreator) {
            }

            public void onRecordStarted() {
                FVContentFragment.this.changeWirelessChargeOn(false);
                Util.sendIntEventMessge(Constants.START_TAKE_VIDEO);
                FVContentFragment.this.startTimer();
                Log.e("--------------", "-------- onRecord  Started  Started ------");
                CameraUtils.setBooRecordStarted(true);
                FVContentFragment.this.setRecordingResolutionFpsUI(true);
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }

            public void onRecordError(String reason) {
                FVContentFragment.this.changeWirelessChargeOn(true);
                Util.sendIntEventMessge(Constants.HAVE_TAKE_VIDEO_OVER);
                FVContentFragment.this.timer.stop();
                FVContentFragment.this.timer.setVisibility(8);
                FVContentFragment.this.timer_horizontal.stop();
                FVContentFragment.this.timer_horizontal.setVisibility(8);
                FVContentFragment.this.timer.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.timer_horizontal.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.chro_timer.stop();
                FVContentFragment.this.linear_timer.setVisibility(8);
                MoveTimelapseUtil.getInstance().setIsHitchCockRecord(0);
                CameraUtils.setIsHitchCockRecordUI(false);
                FVContentFragment.this.setCameraHandModelUIVideoVisibleOrGone(false);
                FVContentFragment.this.timer_size_all.setVisibility(8);
                FVContentFragment.this.timer_size_all_view.setVisibility(8);
                FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                if (FVContentFragment.this.mMyRecordingCountDownTimer != null) {
                    FVContentFragment.this.mMyRecordingCountDownTimer.cancel();
                }
                FVContentFragment.this.stopScreenBrightnessTimeToDark();
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraVideoSymbolStart(0);
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 && MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                    BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 0);
                }
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(0);
                Log.e("--------------", "-------- onRecord Error Error ------");
                CameraUtils.setBooRecordStarted(false);
                Util.sendIntEventMessge(Constants.BTN_LENS_SWITCH_VISIBLE);
                if (FVContentFragment.this.cameraManager.getCameraManagerType() != 1) {
                    BackgroundMusic.getInstance(FVContentFragment.this.getContext()).playRecordSound(FVContentFragment.this.getContext(), "record_end");
                }
                FVContentFragment.this.setRecordingResolutionFpsUI(false);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                }
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }

            public void onRecordEnd(String path) {
                FVContentFragment.this.changeWirelessChargeOn(true);
                ViseLog.m1466e("callback onRecordEnd");
                CameraUtils.setBooRecordStarted(false);
                CameraUtils.setHasTakePhotoOrVideo(true);
                FVContentFragment.this.timer.stop();
                FVContentFragment.this.timer.setVisibility(8);
                FVContentFragment.this.timer_horizontal.stop();
                FVContentFragment.this.timer_horizontal.setVisibility(8);
                FVContentFragment.this.timer.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.timer_horizontal.setBase(SystemClock.elapsedRealtime());
                FVContentFragment.this.chro_timer.stop();
                FVContentFragment.this.linear_timer.setVisibility(8);
                MoveTimelapseUtil.getInstance().setIsHitchCockRecord(0);
                CameraUtils.setIsHitchCockRecordUI(false);
                FVContentFragment.this.setCameraHandModelUIVideoVisibleOrGone(false);
                FVContentFragment.this.timer_size_all.setVisibility(8);
                FVContentFragment.this.timer_size_all_view.setVisibility(8);
                FVContentFragment.this.timer_size_all_horizontal.setVisibility(8);
                FVContentFragment.this.timer_size_all_view_horizontal.setVisibility(8);
                if (FVContentFragment.this.mMyRecordingCountDownTimer != null) {
                    FVContentFragment.this.mMyRecordingCountDownTimer.cancel();
                }
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 && MoveTimelapseUtil.getTimeLapseStaticOrDynamic()) {
                    BleByteUtil.setPTZParameters((byte) 55, (byte) 0, (byte) 0);
                }
                String path2 = (String) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CURRENT_VIDEO_PATH, "");
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 0) {
                    path2 = (String) SPUtils.get(FVContentFragment.this.getActivity(), SharePrefConstant.CURRENT_VIDEO_PATH, "");
                } else if (CameraUtils.getMoveOrDelayTimeLapsePath() != null) {
                    path2 = CameraUtils.getMoveOrDelayTimeLapsePath();
                }
                Util.sendIntEventMessge((int) Constants.HAVE_TAKE_VIDEO_OVER, path2);
                Util.updateGalleryForVideo(FVContentFragment.this.getActivity(), path2);
                FVContentFragment.this.stopScreenBrightnessTimeToDark();
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraVideoSymbolStart(0);
                MoveTimelapseUtil.getInstance().setCameraProgressLinear(0);
                Util.sendIntEventMessge(Constants.BTN_LENS_SWITCH_VISIBLE);
                Log.e("--------------", "-------- onRecord  End  End ------  path  " + path2);
                if (FVContentFragment.this.cameraManager.getCameraManagerType() != 1) {
                    BackgroundMusic.getInstance(FVContentFragment.this.getContext()).playRecordSound(FVContentFragment.this.getContext(), "record_end");
                }
                FVContentFragment.this.setRecordingResolutionFpsUI(false);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                }
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setRecordingResolutionFpsUI(boolean boo) {
        int qulity;
        if (!boo) {
            this.ll_start_video_recording_resolution_fps.setVisibility(8);
        } else if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 0) {
            this.ll_start_video_recording_resolution_fps.setVisibility(0);
        } else if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.ll_start_video_recording_resolution_fps.setVisibility(8);
        } else {
            this.ll_start_video_recording_resolution_fps.setVisibility(0);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            qulity = CameraUtils.getCheckMediaRecordFrontSize();
        } else {
            qulity = CameraUtils.getCheckMediaRecordSize();
        }
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.tx_start_video_recording_resolution.setText(CameraUtils.getRecordSizeText(qulity) + "");
            this.tx_start_video_recording_fps.setText("30fps");
        } else if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            String highSpeed = SPUtils.get(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
            if ("".equals(highSpeed)) {
                this.tx_start_video_recording_resolution.setText(CameraUtils.getCheckMediaRecordFrontSizeTwo().getHeight() + "P");
                this.tx_start_video_recording_fps.setText("30fps");
                return;
            }
            Size size = CameraUtils.getRecordStringToSize(highSpeed.substring(0, highSpeed.indexOf("[")));
            String fps = highSpeed.substring(highSpeed.indexOf("[") + 1, highSpeed.indexOf(","));
            this.tx_start_video_recording_resolution.setText(size.getHeight() + "P");
            this.tx_start_video_recording_fps.setText(fps + "fps");
        } else {
            String highSpeed2 = SPUtils.get(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
            if ("".equals(highSpeed2)) {
                this.tx_start_video_recording_resolution.setText(CameraUtils.getCheckMediaRecordSizeTwo().getHeight() + "P");
                this.tx_start_video_recording_fps.setText("30fps");
                return;
            }
            Size size2 = CameraUtils.getRecordStringToSize(highSpeed2.substring(0, highSpeed2.indexOf("[")));
            String fps2 = highSpeed2.substring(highSpeed2.indexOf("[") + 1, highSpeed2.indexOf(","));
            this.tx_start_video_recording_resolution.setText(size2.getHeight() + "P");
            this.tx_start_video_recording_fps.setText(fps2 + "fps");
        }
    }

    private void setCameraFocusLockOrMove() {
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE))).intValue() == 107040) {
            setAutoFocusModeNewCameraOpen();
        } else {
            setAutoFocusModeNewCameraLock();
        }
    }

    private void setAutoFocusModeNewCameraLock() {
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.lockFocus((Camera.AutoFocusCallback) new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                }
            });
        } else {
            this.cameraManager.lockFocus((Camera2Manager.AutoFocusListener) new Camera2Manager.AutoFocusListener() {
                public void focusLocked() {
                    Log.v("TEST", "autoFocus locked");
                }

                public void focusUnlocked() {
                    Log.v("TEST", "autoFocus unlocked");
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void setAutoFocusModeNewCameraOpen() {
        Log.e("------------", "--------333 全景拍照 不锁焦 open ------");
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.cancelAutoFocus();
        } else {
            this.cameraManager.cancelAutoFocus(new Camera2Manager.AutoFocusListener() {
                public void focusLocked() {
                    Log.v("Camera2", "cancelAutoFocusLock locked");
                }

                public void focusUnlocked() {
                    Log.v("Camera2", "cancelAutoFocusLock unlocked");
                }
            });
        }
    }

    private void setAutoFocusModeNewCameraClose() {
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.autoFocus(calculateTapAreaCenter((float) (this.widthPingMu / 2), (float) (this.heightPingMu / 2), 1.0f), (Camera.AutoFocusCallback) null);
            return;
        }
        this.cameraManager.autoFocus((double) (this.widthPingMu / 2), (double) (this.heightPingMu / 2), false, new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                Log.v("TEST", "autoFocus locked");
            }

            public void focusUnlocked() {
                Log.v("TEST", "autoFocus unlocked");
            }
        });
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(200.0f * coefficient).intValue();
        int centerX = (int) (((x / ((float) this.cameraManager.getPreviewResolution().getWidth())) * 2000.0f) - 1000.0f);
        int centerY = (int) (((y / ((float) this.cameraManager.getPreviewResolution().getHeight())) * 2000.0f) - 1000.0f);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000);
        RectF rectF = new RectF((float) left, (float) top, (float) (left + areaSize), (float) (top + areaSize));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private Rect calculateTapAreaCenter(float x, float y, float coefficient) {
        int areaSize = Float.valueOf(200.0f * coefficient).intValue();
        int centerX = (int) (((x / ((float) this.cameraManager.getPreviewResolution().getWidth())) * 2000.0f) - 1000.0f);
        int centerY = (int) (((y / ((float) this.cameraManager.getPreviewResolution().getHeight())) * 2000.0f) - 1000.0f);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000);
        RectF rectF = new RectF((float) left, (float) top, (float) (left + areaSize), (float) (top + areaSize));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public FVCameraManager getCameraManager() {
        return this.cameraManager;
    }

    private class OrientationBroad extends BroadcastReceiver {
        private OrientationBroad() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1) != -1) {
                FVContentFragment.this.getAngle();
            }
        }
    }

    /* access modifiers changed from: private */
    public void getAngle() {
        this.angle = ScreenOrientationUtil.getInstance().getOrientation();
        if (this.angle == 0) {
            this.handler.sendEmptyMessage(0);
            this.mRotateHandler.sendEmptyMessage(0);
        } else if (this.angle == 90) {
            this.handler.sendEmptyMessage(90);
            this.mRotateHandler.sendEmptyMessage(90);
        } else if (this.angle == 180) {
            this.handler.sendEmptyMessage(180);
            this.mRotateHandler.sendEmptyMessage(180);
        } else if (this.angle == 270) {
            this.handler.sendEmptyMessage(270);
            this.mRotateHandler.sendEmptyMessage(270);
        }
    }

    /* access modifiers changed from: private */
    public void rotateView(final int angle2, View... views) {
        int length = views.length;
        int i = 0;
        while (i < length) {
            View view2 = views[i];
            if (angle2 != this.mStartAngle) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view2, "rotation", new float[]{(float) this.mStartAngle, (float) angle2});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVContentFragment.this.mStartAngle = angle2;
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    private void initStatus() {
        int gridingMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
        FVModeSelectEvent fvModeSelectEvent = new FVModeSelectEvent();
        fvModeSelectEvent.setMode(gridingMode);
        onModeSwitch(fvModeSelectEvent);
    }

    private void initListener() {
        this.seekbarFocalLength.setOnSeekBarChangeListener(this);
        this.seekbarBrightness.setOnSeekBarChangeListener(this);
        this.seekbarBrightness2.setOnSeekBarChangeListener(this);
        this.btnFullShotCancel.setOnClickListener(this);
        this.iv_mark_quit_out.setOnClickListener(this);
        this.iv_mark_quit_out_vertical.setOnClickListener(this);
        this.iv_mark_setting_menu.setOnClickListener(this);
        this.iv_mark_setting_menu_vertical.setOnClickListener(this);
    }

    private void getGestureOneFingerOnClickListerDown() {
        if (this.rlFocus.getVisibility() == 0) {
            int preBrightProgress = this.curBrightProgress;
            if (this.angle == 0) {
                this.curBrightProgress--;
            } else if (this.angle != 90) {
                if (this.angle == 180) {
                    this.curBrightProgress++;
                } else if (this.angle == 270) {
                }
            }
            if (preBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureOneFingerOnClickListerTop() {
        if (this.rlFocus.getVisibility() == 0) {
            int preBrightProgress = this.curBrightProgress;
            if (this.angle == 0) {
                this.curBrightProgress++;
            } else if (this.angle != 90) {
                if (this.angle == 180) {
                    this.curBrightProgress--;
                } else if (this.angle == 270) {
                }
            }
            if (preBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureOneFingerOnClickListerRight() {
        if (this.rlFocus.getVisibility() == 0) {
            int preBrightProgress = this.curBrightProgress;
            if (this.angle != 0) {
                if (this.angle == 90) {
                    this.curBrightProgress--;
                } else if (this.angle != 180 && this.angle == 270) {
                    this.curBrightProgress++;
                }
            }
            if (preBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureOneFingerOnClickListerLeft() {
        if (this.rlFocus.getVisibility() == 0) {
            int preBrightProgress = this.curBrightProgress;
            if (this.angle != 0) {
                if (this.angle == 90) {
                    this.curBrightProgress++;
                } else if (this.angle != 180 && this.angle == 270) {
                    this.curBrightProgress--;
                }
            }
            if (preBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureOneFingerOnClickListerAdd() {
        if (this.rlFocus.getVisibility() == 0) {
            if (this.curBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureOneFingerOnClickLister(MotionEvent motionEventFinger) {
        if (motionEventFinger.getPointerCount() <= 1 && this.rlFocus.getVisibility() == 0) {
            int preBrightProgress = this.curBrightProgress;
            MotionEvent mDownEvent = CameraUtils.getGestureDownMotionEvent();
            this.downX = mDownEvent.getX();
            this.downY = mDownEvent.getY();
            float moveX = motionEventFinger.getX();
            float disY = motionEventFinger.getY() - this.downY;
            float disX = moveX - this.downX;
            Log.d(FVMainActivity.TAG, "     disX: " + disX + "     disY: " + disY + "    motionEventFinger: " + motionEventFinger.getX() + " " + motionEventFinger.getY() + "    downX: " + this.downX + "  downY:" + this.downY);
            if (this.angle == 0) {
                if (disY > 10.0f) {
                    this.curBrightProgress--;
                } else if (disY < -10.0f) {
                    this.curBrightProgress++;
                }
            } else if (this.angle == 90) {
                if (disX > 10.0f) {
                    this.curBrightProgress--;
                } else if (disX < -10.0f) {
                    this.curBrightProgress++;
                }
            } else if (this.angle == 180) {
                if (disY > 10.0f) {
                    this.curBrightProgress++;
                } else if (disY < -10.0f) {
                    this.curBrightProgress--;
                }
            } else if (this.angle == 270) {
                if (disX > 10.0f) {
                    this.curBrightProgress++;
                } else if (disX < -10.0f) {
                    this.curBrightProgress--;
                }
            }
            if (preBrightProgress != this.curBrightProgress) {
                this.isBrightUp = true;
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void getGestureTwoFingerOnClickLister(MotionEvent motionEvent2Finger) {
        if (!this.camera_hand_model && !CameraExclusiveUtils.openStretchLensExclusive(getActivity())) {
            if (CameraUtils.getCurrentPageIndex() == 2) {
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible()) {
                    this.ll_seekbar_inside_down.setVisibility(0);
                    this.ll_seekbar_inside_down.setOnClickListener(this);
                    return;
                } else if (this.ll_seekbar_inside_down.getVisibility() == 0) {
                    this.ll_seekbar_inside_down.setVisibility(8);
                }
            }
            this.isZoomMode = true;
            this.llSeekbar.setVisibility(0);
            if (this.isZoomMode && !this.camera_hand_model) {
                setZoomEventGesture(motionEvent2Finger);
            }
        }
    }

    private void getGestureOnClickLister(MotionEvent motionEvent) {
        Log.e("-----------", "------ 点击事件     点击事件     点击事件      -----");
        if (!CameraUtils.isFullShotIng() && !this.camera_hand_model && this.cameraManager.isCameraOpened() && this.ll_mark_point.getVisibility() == 8 && this.ll_mark_point_vertical.getVisibility() == 8) {
            setSeekbarBrightnessBackground(true);
            this.downX2 = motionEvent.getX();
            this.downY2 = motionEvent.getY();
            this.onLongClickTouch = false;
            Point deviceSize = Util.getDeviceSize(getActivity());
            if (this.llSeekbar.getVisibility() == 0) {
                if (this.downY2 > ((float) (deviceSize.y - Util.dip2px(getActivity(), 70.0f)))) {
                    return;
                }
            }
            Rect rect = calculateTapArea(this.downX2, this.downY2, 1.0f);
            if (this.cameraManager.getCameraManagerType() == 1) {
                this.cameraManager.autoFocus(rect, new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean b, Camera camera) {
                        FVContentFragment.this.cameraManager.lockAutoExposure(false);
                    }
                });
            } else {
                if (this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(false);
                }
                this.cameraManager.enableManualMode(false);
                this.cameraManager.autoFocus((double) this.downX2, (double) this.downY2, true, new Camera2Manager.AutoFocusListener() {
                    public void focusLocked() {
                        Log.v("TEST", "autoFocus locked");
                    }

                    public void focusUnlocked() {
                        Log.v("TEST", "autoFocus unlocked");
                    }
                });
            }
            int top = Util.dip2px(getActivity(), 60.0f);
            int bottom = deviceSize.x - Util.dip2px(getActivity(), 110.0f);
            if (this.downX2 > ((float) top) && this.downX2 < ((float) bottom)) {
                this.curBright = getCurBright(this.seekbarBrightness.getProgress());
                int measuredWidth = this.rlFocus.getMeasuredWidth();
                int measuredHeight = this.rlFocus.getMeasuredHeight();
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(this.rlFocus.getLayoutParams());
                if (this.downY2 < ((float) (measuredHeight / 2))) {
                    this.focusStatus = 1;
                    if (this.angle == 90) {
                        this.rlBright2.setVisibility(0);
                        this.rlBright.setVisibility(8);
                    } else {
                        this.rlBright2.setVisibility(8);
                        this.rlBright.setVisibility(0);
                    }
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, 0, 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                } else if (((float) Util.getDeviceSize(getActivity()).y) - this.downY2 < ((float) (measuredHeight / 2))) {
                    this.focusStatus = 2;
                    if (this.angle == 270) {
                        this.rlBright2.setVisibility(0);
                        this.rlBright.setVisibility(8);
                    } else {
                        this.rlBright2.setVisibility(8);
                        this.rlBright.setVisibility(0);
                    }
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, (int) ((this.downY2 - ((float) (measuredWidth / 2))) - 60.0f), 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                } else {
                    this.focusStatus = 0;
                    this.rlBright2.setVisibility(8);
                    this.rlBright.setVisibility(0);
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, (((int) this.downY2) - (measuredHeight / 2)) + 20, 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                }
                this.rl_focus_relative.setVisibility(8);
                this.rlFocus.setVisibility(0);
                recoverBright();
                if (this.handler != null) {
                    this.handler.removeMessages(12);
                    this.handler.sendEmptyMessageDelayed(12, 900);
                }
                if (this.set != null) {
                    this.set.cancel();
                }
                if (this.alpha != null) {
                    this.alpha.cancel();
                }
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleX", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleY", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
                this.alpha = ObjectAnimator.ofFloat(this.btnManualFocus, "alpha", new float[]{1.0f, 0.4f, 1.0f});
                this.alpha.setDuration(400);
                this.alpha.setInterpolator(new LinearInterpolator());
                this.set = new AnimatorSet();
                this.set.setDuration(500);
                this.set.setInterpolator(new LinearInterpolator());
                this.set.playTogether(new Animator[]{scaleX, scaleY});
                this.set.start();
                this.set.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        FVContentFragment.this.alpha.start();
                    }
                });
                if (this.handler != null) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                    this.handler.removeMessages(16);
                    this.handler.sendEmptyMessageDelayed(16, 8000);
                }
            }
        }
    }

    private void getCenterOnLongClickLister() {
        setSeekbarBrightnessBackground(false);
        this.onLongClickTouch = true;
        this.rlFocus.setVisibility(8);
        if (this.handler != null) {
            while (this.handler.hasMessages(16)) {
                this.handler.removeMessages(16);
            }
            this.handler.removeMessages(16);
        }
        if (!CameraUtils.isFullShotIng() && !this.camera_hand_model) {
            recoverBright();
            setSeekbarBrightnessBackground(false);
            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(this.rlFocus.getLayoutParams());
            layout.setMargins((Util.getDisplaySize(getActivity()).x / 2) - (this.rlFocus.getMeasuredWidth() / 2), (Util.getDisplaySize(getActivity()).y / 2) - (this.rlFocus.getMeasuredHeight() / 2), 0, 0);
            this.rlFocus.setLayoutParams(layout);
            this.rlFocus.setVisibility(0);
            Rect rect1 = calculateTapAreaCenter((float) (this.widthPingMu / 2), (float) (this.heightPingMu / 2), 1.0f);
            if (this.cameraManager.getCameraManagerType() == 1) {
                this.cameraManager.autoFocus(rect1, new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean b, Camera camera) {
                        FVContentFragment.this.cameraManager.lockAutoExposure(false);
                    }
                });
            } else {
                if (this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(false);
                }
                this.cameraManager.enableManualMode(false);
                this.cameraManager.autoFocus((double) (this.widthPingMu / 2), (double) (this.heightPingMu / 2), true, new Camera2Manager.AutoFocusListener() {
                    public void focusLocked() {
                        Log.v("TEST", "autoFocus locked");
                    }

                    public void focusUnlocked() {
                        Log.v("TEST", "autoFocus unlocked");
                    }
                });
            }
            this.rl_focus_relative.setVisibility(8);
            this.rlFocus.setVisibility(0);
            if (this.handler != null) {
                this.handler.removeMessages(12);
                this.handler.sendEmptyMessageDelayed(12, 900);
            }
            if (this.set != null) {
                this.set.cancel();
            }
            if (this.alpha != null) {
                this.alpha.cancel();
            }
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleX", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleY", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
            this.alpha = ObjectAnimator.ofFloat(this.btnManualFocus, "alpha", new float[]{1.0f, 0.4f, 1.0f});
            this.alpha.setDuration(400);
            this.alpha.setInterpolator(new LinearInterpolator());
            this.set = new AnimatorSet();
            this.set.setDuration(500);
            this.set.setInterpolator(new LinearInterpolator());
            this.set.playTogether(new Animator[]{scaleX, scaleY});
            this.set.start();
            this.set.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    FVContentFragment.this.alpha.start();
                    if (FVContentFragment.this.handler != null) {
                        while (FVContentFragment.this.handler.hasMessages(16)) {
                            FVContentFragment.this.handler.removeMessages(16);
                        }
                        FVContentFragment.this.handler.removeMessages(16);
                    }
                }
            });
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
            }
            EventBusUtil.sendEvent(new Event(152));
            if (this.handler != null) {
                while (this.handler.hasMessages(14)) {
                    this.handler.removeMessages(14);
                }
                this.handler.sendEmptyMessageDelayed(14, 4000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void contentLockAutoExposure() {
        if (this.cameraManager.isAutoExposureLock()) {
            this.cameraManager.lockAutoExposure(false);
            while (this.handler.hasMessages(9)) {
                this.handler.removeMessages(9);
            }
            this.handler.sendEmptyMessageDelayed(9, 1000);
            return;
        }
        this.cameraManager.lockAutoExposure(true);
    }

    private void getGestureOnLongClickLister(MotionEvent MotionEventLong) {
        setSeekbarBrightnessBackground(false);
        this.onLongClickTouch = true;
        this.rlFocus.setVisibility(8);
        if (this.handler != null) {
            while (this.handler.hasMessages(16)) {
                this.handler.removeMessages(16);
            }
            this.handler.removeMessages(16);
        }
        Log.e("-----------", "------ 长按事件产生 长按事件产生 长按事件产生  -----");
        if (!CameraUtils.isFullShotIng() && !this.camera_hand_model && this.ll_mark_point.getVisibility() == 8 && this.ll_mark_point_vertical.getVisibility() == 8) {
            Point deviceSize = Util.getDeviceSize(getActivity());
            if (this.llSeekbar.getVisibility() == 0) {
                if (this.downY2 > ((float) (deviceSize.y - Util.dip2px(getActivity(), 70.0f)))) {
                    return;
                }
            }
            this.downX2 = MotionEventLong.getX();
            this.downY2 = MotionEventLong.getY();
            Rect rect = calculateTapArea(this.downX2, this.downY2, 1.0f);
            if (this.cameraManager.getCameraManagerType() == 1) {
                this.cameraManager.autoFocus(rect, new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean b, Camera camera) {
                        FVContentFragment.this.contentLockAutoExposure();
                    }
                });
            } else {
                if (this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(false);
                }
                this.cameraManager.enableManualMode(false);
                this.cameraManager.autoFocus((double) this.downX2, (double) this.downY2, true, new Camera2Manager.AutoFocusListener() {
                    public void focusLocked() {
                        Log.v("TEST", "autoFocus locked");
                    }

                    public void focusUnlocked() {
                        Log.v("TEST", "autoFocus unlocked");
                    }
                });
            }
            int top = Util.dip2px(getActivity(), 60.0f);
            int bottom = deviceSize.x - Util.dip2px(getActivity(), 110.0f);
            if (this.downX2 > ((float) top) && this.downX2 < ((float) bottom)) {
                this.curBright = getCurBright(this.seekbarBrightness.getProgress());
                int measuredWidth = this.rlFocus.getMeasuredWidth();
                int measuredHeight = this.rlFocus.getMeasuredHeight();
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(this.rlFocus.getLayoutParams());
                if (this.downY2 < ((float) (measuredHeight / 2))) {
                    this.focusStatus = 1;
                    if (this.angle == 90) {
                        this.rlBright2.setVisibility(0);
                        this.rlBright.setVisibility(8);
                    } else {
                        this.rlBright2.setVisibility(8);
                        this.rlBright.setVisibility(0);
                    }
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, 0, 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                } else if (((float) Util.getDeviceSize(getActivity()).y) - this.downY2 < ((float) (measuredHeight / 2))) {
                    this.focusStatus = 2;
                    if (this.angle == 270) {
                        this.rlBright2.setVisibility(0);
                        this.rlBright.setVisibility(8);
                    } else {
                        this.rlBright2.setVisibility(8);
                        this.rlBright.setVisibility(0);
                    }
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, (int) ((this.downY2 - ((float) (measuredWidth / 2))) - 60.0f), 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                } else {
                    this.focusStatus = 0;
                    this.rlBright2.setVisibility(8);
                    this.rlBright.setVisibility(0);
                    layout.setMargins((((int) this.downX2) - (measuredWidth / 2)) + 20, (((int) this.downY2) - (measuredHeight / 2)) + 20, 0, 0);
                    this.rlFocus.setLayoutParams(layout);
                }
                this.rl_focus_relative.setVisibility(8);
                this.rlFocus.setVisibility(0);
                recoverBright();
                if (this.handler != null) {
                    this.handler.removeMessages(12);
                    this.handler.sendEmptyMessageDelayed(12, 900);
                }
                if (this.set != null) {
                    this.set.cancel();
                }
                if (this.alpha != null) {
                    this.alpha.cancel();
                }
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleX", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.btnManualFocus, "scaleY", new float[]{1.0f, 1.1f, 1.2f, 1.1f, 1.0f});
                this.alpha = ObjectAnimator.ofFloat(this.btnManualFocus, "alpha", new float[]{1.0f, 0.4f, 1.0f});
                this.alpha.setDuration(400);
                this.alpha.setInterpolator(new LinearInterpolator());
                this.set = new AnimatorSet();
                this.set.setDuration(500);
                this.set.setInterpolator(new LinearInterpolator());
                this.set.playTogether(new Animator[]{scaleX, scaleY});
                this.set.start();
                this.set.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        FVContentFragment.this.alpha.start();
                        if (FVContentFragment.this.handler != null) {
                            while (FVContentFragment.this.handler.hasMessages(16)) {
                                FVContentFragment.this.handler.removeMessages(16);
                            }
                            FVContentFragment.this.handler.removeMessages(16);
                        }
                    }
                });
                if (this.handler != null) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                    this.handler.removeMessages(16);
                }
                EventBusUtil.sendEvent(new Event(152));
                if (this.handler != null) {
                    while (this.handler.hasMessages(14)) {
                        this.handler.removeMessages(14);
                    }
                    this.handler.sendEmptyMessageDelayed(14, 4000);
                }
            }
        }
    }

    private void setZoomEventGesture(MotionEvent event) {
        float endDistance = getDistance(event);
        if (endDistance > 5.0f) {
            if (endDistance > this.startDistance) {
                this.curFocusLength += 20;
                if (this.curFocusLength > 1000) {
                    this.curFocusLength = 1000;
                }
                this.seekbarFocalLength.setProgress(this.curFocusLength);
                changeZoom(this.curFocusLength);
                CameraUtils.setCamLengthProgress(this.curFocusLength);
            } else {
                this.curFocusLength -= 20;
                if (this.curFocusLength < 0) {
                    this.curFocusLength = 0;
                }
                this.seekbarFocalLength.setProgress(this.curFocusLength);
                changeZoom(this.curFocusLength);
                CameraUtils.setCamLengthProgress(this.curFocusLength);
            }
            this.startDistance = endDistance;
        }
    }

    private void setSeekbarBrightnessBackground(Boolean boo) {
        if (boo.booleanValue()) {
            this.btnManualFocus.setBackground(getResources().getDrawable(C0853R.mipmap.ic_manual_foucus));
            this.seekbar_brightness_view.setBackgroundColor(getResources().getColor(C0853R.color.fc0));
            this.seekbar_brightness2_view.setBackgroundColor(getResources().getColor(C0853R.color.fc0));
            return;
        }
        this.btnManualFocus.setBackground(getResources().getDrawable(C0853R.mipmap.ic_manual_foucus_lock));
        this.seekbar_brightness_view.setBackgroundColor(getResources().getColor(C0853R.color.fc0_70));
        this.seekbar_brightness2_view.setBackgroundColor(getResources().getColor(C0853R.color.fc0_70));
    }

    private float getDistance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt((double) ((dx * dx) + (dy * dy)));
    }

    private int smoothValue(int origin) {
        ViseLog.m1466e("Manual smoothValue: " + ((origin / 2) * 10));
        return (origin / 2) * 10;
    }

    private int refineValue(int origin) {
        int refine = origin;
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (origin > 1) {
                return origin / 2;
            }
            if (origin < -1) {
                return origin / 2;
            }
            return refine;
        } else if (origin > 1) {
            return origin - 1;
        } else {
            if (origin < -1) {
                return origin + 1;
            }
            return refine;
        }
    }

    private int refineValue10(int origin) {
        int refine = origin;
        if (CameraUtils.getCurrentPageIndex() == 2) {
            if (origin > 1) {
                refine = origin / 2;
            } else if (origin < -1) {
                refine = origin / 2;
            }
        } else if (origin > 1) {
            refine = origin - 1;
        } else if (origin < -1) {
            refine = origin + 1;
        }
        return refine * 10;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        Size picsize;
        String str;
        String str2;
        switch (fvModeSelectEvent.getMode()) {
            case Constants.CAMERA_RESET_WHITE_BALANCE /*10010*/:
                resetWhiteBalance();
                return;
            case Constants.GRIDING_NONE /*10015*/:
                this.iconGriding.setVisibility(8);
                return;
            case Constants.GRIDING_CENTER_POINT /*10016*/:
                this.iconGriding.setVisibility(0);
                this.iconGriding.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                this.iconGriding.setImageResource(C0853R.mipmap.ic_gradding_central_point);
                return;
            case Constants.GRIDING_GRIDVIEW /*10017*/:
                this.iconGriding.setVisibility(0);
                this.iconGriding.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.iconGriding.setImageResource(C0853R.mipmap.ic_gridding_gridview);
                this.iconGriding.setAlpha(0.5f);
                return;
            case Constants.GRIDING_GRIDVIEW_DIAGONAL_LINE /*10018*/:
                this.iconGriding.setVisibility(0);
                this.iconGriding.setScaleType(ImageView.ScaleType.CENTER_CROP);
                this.iconGriding.setImageResource(C0853R.mipmap.ic_gridding_gridview_diagonal_line);
                this.iconGriding.setAlpha(0.5f);
                return;
            case Constants.FULL_SHOT_START /*10040*/:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    toastAboutMarkPointCancelTV();
                }
                CameraUtils.setFullShotIng(true);
                CameraUtils.setFullCameraErrorCode("");
                Log.e("------------", "-------- 全景拍照  55555 ----");
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
                    this.cameraManager.setFlashMode(0);
                }
                if (this.rlFocus != null) {
                    Util.sendIntEventMessge(Constants.SMALL_SUN_DIS);
                }
                CameraUtils.setBosIsResume(false);
                this.llFullShot.setVisibility(0);
                int fullShotMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_180))).intValue();
                if (fullShotMode == 10025) {
                    this.iconFullShot.setImageResource(C0853R.mipmap.ic_180);
                } else if (fullShotMode == 10026) {
                    this.iconFullShot.setImageResource(C0853R.mipmap.ic_330);
                } else if (fullShotMode == 10027) {
                    this.iconFullShot.setImageResource(C0853R.mipmap.ic_3_3);
                } else if (fullShotMode == 10028) {
                    this.iconFullShot.setImageResource(C0853R.mipmap.ic_3_5up);
                }
                setCameraHandModelUIVisibleOrGone(true);
                if (this.ll_setting_x_mf != null && this.ll_setting_x_mf.getVisibility() == 0) {
                    this.ll_setting_x_mf.setVisibility(8);
                }
                if (this.llSeekbar != null && this.llSeekbar.getVisibility() == 0) {
                    this.llSeekbar.setVisibility(8);
                    return;
                }
                return;
            case Constants.FULL_SHOT_END /*10041*/:
                setAutoFocusModeNewCameraOpen();
                this.llFullShot.setVisibility(8);
                this.rlPhotoSynthesis.setVisibility(0);
                this.pbSynthesis.setProgress(10);
                new FVProgressCountTimer(8400, 1000, this.pbSynthesis).start();
                return;
            case Constants.FULL_SHOT_EXCEPTION_END /*10042*/:
                setAutoFocusModeNewCameraOpen();
                this.llFullShot.setVisibility(8);
                Util.sendIntEventMessge(Constants.FULL_SHOT_OVER_RECOVER_UNCLICK);
                String mode = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(mode) && BleConstant.SHUTTER.equals(mode)) {
                    fullShotFailedDialog(C0853R.string.label_full_shot_toast);
                }
                CameraUtils.setFullShotIng(false);
                Log.e("------------", "-------- 全景拍照  99999 ----");
                CameraUtils.setBosIsResume(true);
                setCameraHandModelUIVisibleOrGone(false);
                return;
            case Constants.FULL_SHOT_SYNTHESIS_END /*10043*/:
                Util.sendIntEventMessge(Constants.FULL_SHOT_OVER_RECOVER_UNCLICK);
                this.pbSynthesis.setProgress(100);
                this.rlPhotoSynthesis.setVisibility(8);
                CameraUtils.setFullShotIng(false);
                Log.e("------------", "-------- 全景拍照  444444 ----");
                CameraUtils.setBosIsResume(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
                    this.cameraManager.setFlashMode(3);
                }
                setAutoFocusModeNewCameraOpen();
                this.cameraManager.lockAutoExposure(false);
                setCameraHandModelUIVisibleOrGone(false);
                return;
            case Constants.FULL_SHOT_SYNTHESIS_FAILED /*10044*/:
                Util.sendIntEventMessge(Constants.FULL_SHOT_OVER_RECOVER_UNCLICK);
                this.rlPhotoSynthesis.setVisibility(8);
                fullShotFailedDialog(C0853R.string.label_full_shot_synthesis_failed_toast);
                CameraUtils.setFullShotIng(false);
                Log.e("------------", "-------- 全景拍照  333333 ----");
                CameraUtils.setBosIsResume(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
                    this.cameraManager.setFlashMode(3);
                }
                setAutoFocusModeNewCameraOpen();
                this.cameraManager.lockAutoExposure(false);
                setCameraHandModelUIVisibleOrGone(false);
                return;
            case Constants.RESOLUTION_1080 /*10200*/:
                this.handler.sendEmptyMessageDelayed(13, 500);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    if (CameraUtils.getMaxSupOrReComPictureFrontSize() == 0) {
                        picsize = this.cameraManager.getRecommendPictureSize();
                    } else {
                        picsize = this.cameraManager.getMaxSupportedPictureSize();
                    }
                } else if (CameraUtils.getMaxSupOrReComPictureSize() == 0) {
                    picsize = this.cameraManager.getRecommendPictureSize();
                } else {
                    picsize = this.cameraManager.getMaxSupportedPictureSize();
                }
                this.cameraManager.setPictureResolution(picsize);
                return;
            case Constants.RESOLUTION_720 /*10201*/:
                this.cameraManager.setPreviewResolutionEx(new Size(1280, 720));
                CameraUtils.setCameraPreviewWidth(720);
                return;
            case Constants.RESOLUTION_480 /*10202*/:
                this.cameraManager.setPreviewResolutionEx(new Size(720, 480));
                CameraUtils.setCameraPreviewWidth(480);
                return;
            case Constants.SHOW_TIME_DELAY_ANIMATION /*10399*/:
                showTimeDelay();
                return;
            case Constants.START_TAKE_PHOTO_SHADOW /*10501*/:
                if (this.isShadowShowing) {
                    if (this.handler != null) {
                        this.handler.removeMessages(300);
                        this.handler.sendEmptyMessageDelayed(300, 200);
                        return;
                    }
                    return;
                } else if (this.handler != null) {
                    this.handler.removeMessages(300);
                    this.handler.sendEmptyMessageDelayed(300, 100);
                    return;
                } else {
                    return;
                }
            case Constants.OPEN_KCF /*106400*/:
                this.fragment_content_two_ll.setVisibility(0);
                if (this.customSurfaceView != null) {
                    this.fragment_content_two_ll.removeView(this.customSurfaceView);
                }
                this.customSurfaceView = new CustomSurfaceView(getContext());
                this.fragment_content_two_ll.addView(this.customSurfaceView);
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraTrackingStart(1);
                return;
            case Constants.CLOSE_KCF /*106401*/:
                this.customSurfaceView.setUpStartDrawTwo();
                if (this.customSurfaceView != null) {
                    this.fragment_content_two_ll.removeView(this.customSurfaceView);
                }
                this.fragment_content_two_ll.setVisibility(8);
                this.cameraManager.stopTrack();
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraTrackingStart(0);
                return;
            case Constants.START_KCF /*106402*/:
                this.fragment_content_two_ll.setVisibility(0);
                getZuoBiaoXYContent(fvModeSelectEvent.getRect());
                return;
            case Constants.FV_CAMERA_SLEEP_STOP_FULL_SHOT /*106406*/:
                CameraUtils.setFullShotIng(false);
                Log.e("------------", "-------- 全景拍照  111111 ----");
                if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE != 3) {
                    getString(C0853R.string.label_full_shot_compound_failed) + CameraUtils.getFullCameraErrorCode();
                }
                cancelFullShot();
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
                    this.cameraManager.setFlashMode(3);
                }
                setAutoFocusModeNewCameraOpen();
                this.cameraManager.lockAutoExposure(false);
                return;
            case Constants.LENS_TENSILE_OF_CAMERA /*106501*/:
                String value = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(value)) {
                    dealPtzZoom(Integer.valueOf(value).intValue());
                }
                if (CameraUtils.getCurrentPageIndex() == 2 && BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0 && this.ll_setting_x_mf.getVisibility() == 0 && this.ll_mark_point.getVisibility() == 8 && this.ll_mark_point_vertical.getVisibility() == 8) {
                    this.ll_setting_x_mf.setVisibility(8);
                    return;
                }
                return;
            case Constants.LENS_TENSILE_OF_CAMERA_END /*106502*/:
                if (this.llSeekbar != null) {
                    this.isZoomMode = false;
                    this.llSeekbar.setVisibility(8);
                    return;
                }
                return;
            case Constants.LENS_TENSILE_OF_CAMERA_OVER /*106503*/:
                Log.e("----------", "------- 结束 结束 结束 结束 -------");
                if (this.myHandler != null) {
                    this.myHandler.removeCallbacksAndMessages((Object) null);
                }
                if (this.myMixHandler != null) {
                    this.myMixHandler.removeCallbacksAndMessages((Object) null);
                    return;
                }
                return;
            case Constants.EXPOSURE_OF_CAMERA /*106510*/:
                this.onLongClickTouch = false;
                String value2 = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(value2)) {
                    dealPtzBright(Integer.valueOf(value2).intValue());
                    setSeekbarBrightnessBackground(true);
                    return;
                }
                return;
            case Constants.EXPOSURE_OF_CAMERA_END /*106511*/:
                if (this.rlFocus != null && !this.onLongClickTouch) {
                    this.rlFocus.setVisibility(8);
                    this.cameraManager.lockAutoExposure(false);
                    CameraUtils.setCamExposureLengthProgress(50);
                    return;
                }
                return;
            case Constants.EXPOSURE_OF_CAMERA_OVER /*106512*/:
                Log.e("----------", "---------  结束 结束 结束曝光 结束曝光  --------");
                if (this.expoHandler != null) {
                    this.expoHandler.removeCallbacksAndMessages((Object) null);
                }
                if (this.expoMixHandler != null) {
                    this.expoMixHandler.removeCallbacksAndMessages((Object) null);
                    return;
                }
                return;
            case Constants.EXPOSURE_OF_CAMERA_GONE /*106513*/:
                if (this.rlFocus != null) {
                    this.rlFocus.setVisibility(8);
                    CameraUtils.setCamExposureLengthProgress(50);
                    return;
                }
                return;
            case Constants.MF_OF_CAMERA /*106514*/:
                this.onLongClickTouch = false;
                String value3 = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(value3)) {
                    if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                        this.rlFocus.setVisibility(8);
                    }
                    if (this.handler != null) {
                        while (this.handler.hasMessages(16)) {
                            this.handler.removeMessages(16);
                        }
                    }
                    if (!CameraUtils.getCameraHandModel() || this.content_layout_scale.getVisibility() != 0) {
                        double mf = (Double.valueOf(value3).doubleValue() / 10.0d) - 1.0d;
                        if (mf < 0.0d) {
                            mf = 0.0d;
                        }
                        String value5 = CameraUtils.strSubTwoLength(String.valueOf(mf));
                        if (this.llSeekbar != null && this.llSeekbar.getVisibility() == 0) {
                            this.llSeekbar.setVisibility(8);
                        }
                        if (this.ll_setting_x_mf.getVisibility() == 8) {
                            this.ll_setting_x_mf.setVisibility(0);
                            if (CameraUtils.getCurrentPageIndex() == 2 && this.llFullShot.getVisibility() == 0) {
                                this.ll_setting_x_mf.setVisibility(8);
                            }
                        }
                        float setMFValue = Float.valueOf(value5).floatValue();
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible() && BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21 && CameraUtils.getCurrentPageIndex() == 2) {
                            float markPointMfA = CameraUtils.getLlMarkPointMfA();
                            float markPointMfB = CameraUtils.getLlMarkPointMfB();
                            if (markPointMfA < markPointMfB) {
                                if (setMFValue < markPointMfA) {
                                    setMFValue = markPointMfA;
                                } else if (setMFValue > markPointMfB) {
                                    setMFValue = markPointMfB;
                                }
                            } else if (markPointMfA > markPointMfB) {
                                if (setMFValue < markPointMfB) {
                                    setMFValue = markPointMfB;
                                } else if (setMFValue > markPointMfA) {
                                    setMFValue = markPointMfA;
                                }
                            }
                            this.ll_setting_x_mf_value.setText(CameraUtils.strSubTwoLength(String.valueOf(setMFValue) + "000"));
                        } else {
                            this.ll_setting_x_mf_value.setText(value5);
                        }
                        if (this.setMFValueOld != setMFValue) {
                            if (!this.cameraManager.isMaunalFocus()) {
                                this.cameraManager.enableMFMode(true);
                            }
                            this.cameraManager.setFocusDistance(Float.valueOf(value5).floatValue());
                        }
                        this.setMFValueOld = setMFValue;
                        while (this.handler.hasMessages(57)) {
                            this.handler.removeMessages(57);
                        }
                        this.handler.sendEmptyMessageDelayed(57, 5000);
                        return;
                    } else if (CameraUtils.getCurrentPageIndex() == 2) {
                        double mf2 = Double.valueOf(value3).doubleValue() / 10.0d;
                        if (mf2 < 0.0d) {
                            mf2 = 0.0d;
                        }
                        String value52 = CameraUtils.strSubTwoLength(String.valueOf(mf2));
                        if (!(CameraUtils.getHandModelVisibleStateValue() == 5.0f || CameraUtils.getHandModelVisibleStateValue() == 6.0f || BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0 || BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION != 21)) {
                            if (mf2 > CameraUtils.getScaleScrollViewMFMaxNums()) {
                                mf2 = CameraUtils.getScaleScrollViewMFMaxNums();
                            }
                            double mfN = mf2 - 1.0d;
                            if (mfN < 0.0d) {
                                mfN = 0.0d;
                            }
                            this.horizontal_scale_mf_textview.setText(CameraUtils.strSubTwoLength(String.valueOf(mfN)) + "");
                        }
                        int ftProgress = (int) (Double.valueOf(value52).doubleValue() * 10.0d);
                        int drawScaleMFMax = Util.getDrawScaleMFMax();
                        if (this.ftProgressOld != ftProgress) {
                            if (ftProgress >= 10) {
                                if (!this.cameraManager.isMaunalFocus()) {
                                    this.cameraManager.enableMFMode(true);
                                }
                                Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_MF, String.valueOf(ftProgress));
                            } else if (ftProgress < 5) {
                                Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_MF, String.valueOf(0));
                            } else {
                                if (!this.cameraManager.isMaunalFocus()) {
                                    this.cameraManager.enableMFMode(true);
                                }
                                Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_IMPELLER_MODE_MF, String.valueOf(10));
                            }
                            this.curScrollScaleMf = ftProgress;
                        }
                        this.ftProgressOld = ftProgress;
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.FOCUS_ON_THE_CENTER /*106600*/:
                this.onLongClickTouch = false;
                if (this.handler != null) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                }
                if (this.camera_hand_model) {
                    return;
                }
                if (CameraUtils.getCurrentPageIndex() != 2) {
                    focusOnTheCenter();
                    if (this.handler != null) {
                        this.handler.sendEmptyMessageDelayed(16, 8000);
                        return;
                    }
                    return;
                } else if (this.ll_mark_point.getVisibility() == 8 && this.ll_mark_point_vertical.getVisibility() == 8 && !((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1 && !CameraUtils.getMarkPointUIIsVisible() && CameraUtils.getFrameLayerNumber() == 0) {
                    focusOnTheCenter();
                    if (this.handler != null) {
                        this.handler.sendEmptyMessageDelayed(16, 8000);
                        return;
                    }
                    return;
                } else {
                    return;
                }
            case Constants.SMALL_SUN_DIS /*107201*/:
                if (this.rlFocus != null) {
                    this.rlFocus.setVisibility(8);
                    this.cameraManager.lockAutoExposure(false);
                    recoverBright();
                    if (this.onLongClickTouch) {
                        Log.e("------------", "------ 小太阳消失 一把锁 -----");
                        setAutoFocusModeNewCameraOpen();
                    }
                }
                this.onLongClickTouch = false;
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN /*107211*/:
                if (this.content_layout_scale.getVisibility() == 8 && !CameraUtils.getCameraHandModel()) {
                    if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
                        EventBusUtil.sendEvent(new Event(145));
                    }
                    if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_OPEN))).intValue() == 107738) {
                        Util.sendIntEventMessge(Constants.CAMERA_PARAMETER_DISPLAY_GONE);
                    }
                    int cameraLevel3 = FVCameraManager.GetCameraLevel(this.mContext);
                    if (this.cameraManager.getCameraManagerType() != 2) {
                        EventBusUtil.sendEvent(new Event(150));
                        return;
                    } else if (cameraLevel3 == 2) {
                        EventBusUtil.sendEvent(new Event(151));
                        return;
                    } else {
                        if (this.llSeekbar.getVisibility() == 0) {
                            this.llSeekbar.setVisibility(8);
                        }
                        CameraUtils.setScaleIsoList(new ArrayList(Arrays.asList(new String[]{"29", "40", "50", "64", "80", "100", "125", "160", "200", "250", "320", "400", "500", "640", "800", "1000", "1250", "1600", "1856", "2000", "2200", "2300"})));
                        CameraUtils.setScaleIsoRange(this.cameraManager.getIsoRange());
                        CameraUtils.setScaleIsoList(CameraUtils.setScaleIsoListToPhone(this.cameraManager.getIsoRange().getLower().intValue()));
                        int left = (this.widthPingMu - Util.dip2px(this.mContext, 345.0f)) / 2;
                        int top = (this.heightPingMu - Util.dip2px(this.mContext, 345.0f)) - Util.dip2px(this.mContext, 22.0f);
                        RelativeLayout.LayoutParams lpsScale = (RelativeLayout.LayoutParams) this.content_layout_scale.getLayoutParams();
                        lpsScale.setMargins(left, top, 0, 0);
                        String systemModel2 = Util.getSystemModel();
                        if ("SM-N9500".equals(systemModel2)) {
                            lpsScale.setMargins(left + CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC, top, 0, 0);
                        }
                        this.content_layout_scale.setLayoutParams(lpsScale);
                        SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN));
                        this.camera_hand_model = true;
                        CameraUtils.setCameraHandModel(true);
                        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
                        if (FVCameraManager.GetCameraLevel(getActivity()) == 0 || "H8296".equals(systemModel2)) {
                            CameraUtils.setScaleScrollViewWBMaxNums(0.4d);
                        }
                        Util.sendIntEventMessge(Constants.EXPOSURE_OF_CAMERA_GONE);
                        if (this.cameraManager.getCameraManagerType() == 1) {
                            double maxZoom = (double) this.cameraManager.getMaxZoom();
                        } else {
                            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
                        }
                        CameraUtils.setScaleScrollViewEVMaxNums((double) Math.abs(this.cameraManager.getExposureCompensationRanger()[1]));
                        Range<Long> range = this.cameraManager.getExposureTimeRange();
                        CameraUtils.setScaleShutterRange(range);
                        long ranMax = range.getUpper().longValue();
                        if (ranMax < 500000000) {
                            CameraUtils.setScaleScrollShutterAuto("1/" + ((ranMax * 2) / this.cameraManager.getExposureTime()));
                        } else {
                            CameraUtils.setScaleScrollShutterAuto("1/" + (1000000000 / this.cameraManager.getExposureTime()));
                        }
                        CameraUtils.setCameraHandModelSmallImage(true);
                        if (this.viewScale != null) {
                            this.content_layout_scale.removeView(this.viewScale);
                        }
                        if (this.handler != null) {
                            this.handler.sendEmptyMessage(400);
                        }
                        this.content_layout_scale.setVisibility(0);
                        this.viewScale = LayoutInflater.from(this.mContext).inflate(C0853R.layout.fragment_content_scale_linear, (ViewGroup) null);
                        this.content_layout_scale.addView(this.viewScale);
                        this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                        this.frag_content_scale_linear.setVisibility(0);
                        this.frag_content_scale_linear_place_top = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_top);
                        this.frag_content_scale_linear_place_buttom = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear_place_buttom);
                        this.iv_hand_model_scroll_draw_pointer = (ImageView) this.viewScale.findViewById(C0853R.C0855id.iv_hand_model_scroll_draw_pointer);
                        initViewScale();
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN_VISIBLE);
                        setContentHandModelVisiStateValue();
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_HAND_MODEL_CLOSE /*107212*/:
                if (this.content_layout_scale.getVisibility() == 0 || CameraUtils.getCameraHandModel()) {
                    SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                    this.camera_hand_model = false;
                    CameraUtils.setCameraHandModel(false);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_GONE);
                    this.content_layout_scale.setVisibility(8);
                    this.scaleScrollViewShutter.setVisibility(8);
                    this.scaleScrollViewIsoTwo.setVisibility(8);
                    this.horizontalScaleWbTwo.setVisibility(8);
                    this.scaleScrollViewMf.setVisibility(8);
                    this.scaleScrollViewWT.setVisibility(8);
                    this.scaleScrollViewEV.setVisibility(0);
                    CameraUtils.setCameraHandModelSmallImage(false);
                    this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                    this.frag_content_scale_linear.setVisibility(8);
                    this.content_layout_scale.removeView(this.viewScale);
                    this.cameraManager.enableMEMode(false);
                    this.cameraManager.enableMWBMode(false);
                    this.cameraManager.enableMFMode(false);
                    this.cameraManager.enableManualMode(false);
                    this.cameraManager.setCameraStateListner((Camera2Manager.CameraStatesListener) null);
                    if (this.cameraManager.getCameraManagerType() == 1) {
                        this.cameraManager.setZoom(0.0f);
                    } else {
                        this.cameraManager.setZoom(1.0f);
                    }
                    this.handler.sendEmptyMessageDelayed(19, 1000);
                    setContentHandModelVisiStateValue();
                    if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getFrameLayerNumber() == 2 && ((Integer) SPUtils.get(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                SPUtils.put(FVContentFragment.this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
                                Util.sendIntEventMessge(Constants.CAMERA_WB_MODE_WINDOW_CHANGE);
                            }
                        }, 10);
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_CHANGE /*107213*/:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (this.scaleScrollViewEV.getVisibility() == 0) {
                    sendToHandlerScale(52, "");
                    return;
                } else if (this.scaleScrollViewShutter.getVisibility() == 0) {
                    sendToHandlerScale(53, "");
                    return;
                } else if (this.scaleScrollViewIsoTwo.getVisibility() == 0) {
                    sendToHandlerScale(54, "");
                    return;
                } else if (this.horizontalScaleWbTwo.getVisibility() == 0) {
                    sendToHandlerScale(55, "");
                    return;
                } else if (this.scaleScrollViewMf.getVisibility() == 0) {
                    sendToHandlerScale(56, "");
                    return;
                } else if (this.scaleScrollViewWT.getVisibility() == 0) {
                    sendToHandlerScale(51, "");
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_HAND_MODEL_INT_CHANGE /*107214*/:
                int delta = Integer.decode((String) fvModeSelectEvent.getMessage()).intValue();
                ViseLog.m1466e("Manual delta: " + delta);
                if (this.scaleScrollViewEV.getVisibility() == 0) {
                    this.curScrollScaleEV += smoothValue(delta);
                    int maxEV = Util.getDrawScaleEVMax();
                    if (this.curScrollScaleEV > maxEV) {
                        this.curScrollScaleEV = maxEV;
                    }
                    if (this.curScrollScaleEV < 0) {
                        this.curScrollScaleEV = 0;
                    }
                    this.scaleScrollViewEV.setCurScale(this.curScrollScaleEV);
                    return;
                } else if (this.scaleScrollViewShutter.getVisibility() == 0) {
                    this.curScrollScaleShutter += smoothValue(delta);
                    if (this.curScrollScaleShutter > 620) {
                        this.curScrollScaleShutter = 620;
                    }
                    if (this.curScrollScaleShutter < 0) {
                        this.curScrollScaleShutter = 0;
                    }
                    this.scaleScrollViewShutter.setCurScale(this.curScrollScaleShutter);
                    return;
                } else if (this.scaleScrollViewIsoTwo.getVisibility() == 0) {
                    ViseLog.m1466e("curScrollScaleISO: " + this.curScrollScaleISO);
                    this.curScrollScaleISO += smoothValue(delta);
                    ViseLog.m1466e("targetScrollScaleISO: " + this.curScrollScaleISO);
                    if (this.curScrollScaleISO > 220) {
                        this.curScrollScaleISO = CompanyIdentifierResolver.PROCTER_GAMBLE;
                    }
                    if (this.curScrollScaleISO < 0) {
                        this.curScrollScaleISO = 0;
                    }
                    ViseLog.m1466e("smoothISO: " + this.curScrollScaleISO);
                    this.scaleScrollViewIsoTwo.setCurScale(this.curScrollScaleISO);
                    return;
                } else if (this.horizontalScaleWbTwo.getVisibility() == 0) {
                    if (this.curScrollScaleWB < 40 || (this.curScrollScaleWB == 40 && delta < 0)) {
                        this.curScrollScaleWB += smoothValue(delta);
                        if (this.curScrollScaleWB > 40) {
                            this.curScrollScaleWB = 40;
                        }
                    } else if (this.curScrollScaleWB <= 40 || delta >= 0) {
                        this.curScrollScaleWB += refineValue(delta);
                    } else if (this.curScrollScaleWB + delta <= 40) {
                        this.curScrollScaleWB = 40;
                    } else {
                        this.curScrollScaleWB += refineValue(delta);
                    }
                    int maxWB = Util.getDrawScaleWBMax();
                    if (this.curScrollScaleWB > maxWB) {
                        this.curScrollScaleWB = maxWB;
                    }
                    if (this.curScrollScaleWB < 0) {
                        this.curScrollScaleWB = 0;
                    }
                    this.horizontalScaleWbTwo.setCurScale(this.curScrollScaleWB);
                    return;
                } else if (this.scaleScrollViewMf.getVisibility() == 0) {
                    if (this.curScrollScaleMf > 10 || delta >= 0) {
                        if (CameraUtils.getHandModelVisibleStateValue() != 5.0f && CameraUtils.getHandModelVisibleStateValue() != 6.0f) {
                            this.curScrollScaleMf += refineValue(delta);
                        } else if (CameraUtils.getCurrentPageIndex() != 2) {
                            this.curScrollScaleMf += refineValue(delta);
                        } else if (delta > 0) {
                            this.curScrollScaleMf = ((this.curScrollScaleMf + refineValue10(delta)) / 10) * 10;
                        } else if (this.curScrollScaleMf % 10 == 0) {
                            this.curScrollScaleMf = ((this.curScrollScaleMf + refineValue10(delta)) / 10) * 10;
                        } else {
                            this.curScrollScaleMf = (this.curScrollScaleMf / 10) * 10;
                        }
                        if (this.curScrollScaleMf < 10) {
                            this.curScrollScaleMf = 10;
                        }
                    } else {
                        this.curScrollScaleMf += smoothValue(delta);
                    }
                    int maxMF = Util.getDrawScaleMFMax();
                    if (this.curScrollScaleMf > maxMF) {
                        this.curScrollScaleMf = maxMF;
                    }
                    if (this.curScrollScaleMf < 0) {
                        this.curScrollScaleMf = 0;
                    }
                    this.scaleScrollViewMf.setCurScale(this.curScrollScaleMf);
                    CameraUtils.setHandModelScaleValueMF(this.curScrollScaleMf);
                    return;
                } else if (this.scaleScrollViewWT.getVisibility() == 0) {
                    if (CameraUtils.getHandModelVisibleStateValue() != 5.0f && CameraUtils.getHandModelVisibleStateValue() != 6.0f) {
                        this.curScrollScaleWT += refineValue(delta);
                    } else if (CameraUtils.getCurrentPageIndex() != 2) {
                        this.curScrollScaleWT += refineValue(delta);
                    } else if (delta > 0) {
                        this.curScrollScaleWT = ((this.curScrollScaleWT + refineValue10(delta)) / 10) * 10;
                    } else if (this.curScrollScaleWT % 10 == 0) {
                        this.curScrollScaleWT = ((this.curScrollScaleWT + refineValue10(delta)) / 10) * 10;
                    } else {
                        this.curScrollScaleWT = (this.curScrollScaleWT / 10) * 10;
                    }
                    int maxWT = Util.getDrawScaleWTMax();
                    if (this.curScrollScaleWT > maxWT) {
                        this.curScrollScaleWT = maxWT;
                    }
                    if (this.curScrollScaleWT < 0) {
                        this.curScrollScaleWT = 0;
                    }
                    this.scaleScrollViewWT.setCurScale(this.curScrollScaleWT);
                    CameraUtils.setHandModelScaleValueWT((int) (Double.valueOf((double) (this.curScrollScaleWT * 1000)).doubleValue() / Double.valueOf((double) maxWT).doubleValue()));
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE_OR_GONE /*107217*/:
                if (this.content_layout_scale.getVisibility() == 8) {
                    this.content_layout_scale.setVisibility(0);
                    CameraUtils.setCameraHandModelSmallImage(true);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (this.ll_setting_x_mf.getVisibility() == 0) {
                            this.ll_setting_x_mf.setVisibility(8);
                        }
                        if (this.llSeekbar.getVisibility() == 0) {
                            this.llSeekbar.setVisibility(8);
                        }
                        int selectMemory = CameraUtils.getLabelTopBarSelect();
                        if (selectMemory == -1) {
                        }
                        CameraUtils.setLabelTopBarSelectMemory(selectMemory);
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                        if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                            Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                        }
                    }
                } else {
                    this.content_layout_scale.setVisibility(8);
                    CameraUtils.setCameraHandModelSmallImage(false);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                    }
                }
                setContentHandModelVisiStateValue();
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE /*107218*/:
                if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                    if (CameraUtils.getCameraHandModelSmallImage()) {
                        this.content_layout_scale.setVisibility(0);
                    }
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN_VISIBLE);
                    setContentHandModelVisiStateValue();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_GONE /*107219*/:
                if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                    this.content_layout_scale.setVisibility(8);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_GONE);
                    setContentHandModelVisiStateValue();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_FALSE /*107300*/:
                if (this.content_layout_scale.getVisibility() == 0 || CameraUtils.getCameraHandModel()) {
                    SPUtils.put(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                    this.camera_hand_model = false;
                    CameraUtils.setCameraHandModel(false);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE_GONE);
                    if (this.content_layout_scale != null) {
                        this.content_layout_scale.setVisibility(8);
                    }
                    CameraUtils.setCameraHandModelSmallImage(false);
                    this.frag_content_scale_linear = (LinearLayout) this.viewScale.findViewById(C0853R.C0855id.frag_content_scale_linear);
                    this.frag_content_scale_linear.setVisibility(8);
                    if (this.content_layout_scale != null) {
                        this.content_layout_scale.removeView(this.viewScale);
                    }
                    this.cameraManager.setCameraStateListner((Camera2Manager.CameraStatesListener) null);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE /*107301*/:
                int deltaExposure = Integer.decode((String) fvModeSelectEvent.getMessage()).intValue();
                float progress = (float) (this.seekbarBrightness.getProgress() + deltaExposure);
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel()) {
                    if (this.scaleScrollViewWT.getVisibility() == 8) {
                        sendToHandlerScale(56, "");
                    }
                    this.scaleScrollViewWT.setCurScale(deltaExposure);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_IMPELLER_MODE_ZOOM /*107302*/:
                int deltaEV = Integer.decode((String) fvModeSelectEvent.getMessage()).intValue();
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel()) {
                    if (this.scaleScrollViewEV.getVisibility() == 8) {
                        sendToHandlerScale(51, "");
                    }
                    this.scaleScrollViewEV.setCurScale(deltaEV);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_IMPELLER_MODE_MF /*107303*/:
                int deltaMF = Integer.decode((String) fvModeSelectEvent.getMessage()).intValue();
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel()) {
                    if (CameraUtils.getHandModelVisibleStateValue() == 5.0f || CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                        if (CameraUtils.getHandModelVisibleStateValue() == 5.0f) {
                            if (this.scaleScrollViewMf.getVisibility() == 8) {
                                sendToHandlerScale(55, "");
                            }
                            this.scaleScrollViewMf.setCurScale(deltaMF);
                            return;
                        }
                        return;
                    } else if (deltaMF >= 10) {
                        double mf3 = (Double.valueOf((double) deltaMF).doubleValue() / 10.0d) - 1.0d;
                        if (mf3 < 0.0d) {
                            mf3 = 0.0d;
                        }
                        this.cameraManager.setFocusDistance(Float.valueOf(CameraUtils.strSubTwoLength(String.valueOf(mf3))).floatValue());
                        return;
                    } else if (deltaMF >= 5) {
                        this.cameraManager.setFocusDistance(0.0f);
                        return;
                    } else if (this.cameraManager.isMaunalFocus()) {
                        this.cameraManager.enableMFMode(false);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_IMPELLER_MODE_EXPOSURE_210 /*107304*/:
                int deltaExposure210 = Integer.decode((String) fvModeSelectEvent.getMessage()).intValue();
                float progress2 = (float) (this.seekbarBrightness.getProgress() + deltaExposure210);
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel()) {
                    if (CameraUtils.getHandModelVisibleStateValue() != 5.0f && CameraUtils.getHandModelVisibleStateValue() != 6.0f) {
                        this.cameraManager.setZoom((Float.valueOf(String.valueOf(deltaExposure210)).floatValue() / 10.0f) + 1.0f);
                        return;
                    } else if (CameraUtils.getHandModelVisibleStateValue() == 6.0f) {
                        if (this.scaleScrollViewWT.getVisibility() == 8) {
                            sendToHandlerScale(56, "");
                        }
                        this.scaleScrollViewWT.setCurScale(deltaExposure210);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_IMPELLER_MODE_MF_VISIBLE /*107305*/:
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel() && this.scaleScrollViewMf.getVisibility() == 8) {
                    sendToHandlerScale(55, "");
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_IMPELLER_MODE_WT_VISIBLE /*107306*/:
                if (this.content_layout_scale.getVisibility() == 0 && CameraUtils.getCameraHandModel() && this.scaleScrollViewWT.getVisibility() == 8) {
                    sendToHandlerScale(56, "");
                    return;
                }
                return;
            case Constants.CAMERA_GESTURE_ON_SINGLE_TAP_UP /*107401*/:
                getGestureOnClickLister((MotionEvent) fvModeSelectEvent.getMessage());
                return;
            case Constants.CAMERA_GESTURE_ON_LONG_PRESS /*107402*/:
                getGestureOnLongClickLister((MotionEvent) fvModeSelectEvent.getMessage());
                return;
            case Constants.CAMERA_GESTURE_TWO_FINGER_SCROLL /*107403*/:
                getGestureTwoFingerOnClickLister((MotionEvent) fvModeSelectEvent.getMessage());
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL /*107404*/:
                getGestureOneFingerOnClickLister((MotionEvent) fvModeSelectEvent.getMessage());
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_ADD /*107405*/:
                getGestureOneFingerOnClickListerAdd();
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_TOP /*107406*/:
                getGestureOneFingerOnClickListerTop();
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_DOWN /*107407*/:
                getGestureOneFingerOnClickListerDown();
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_LEFT /*107408*/:
                getGestureOneFingerOnClickListerLeft();
                return;
            case Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_RIGHT /*107409*/:
                getGestureOneFingerOnClickListerRight();
                return;
            case Constants.CAMERA_CENTER_AUTO_FOCUS_LOCK /*107410*/:
                getCenterOnLongClickLister();
                return;
            case Constants.CAMERA_SURFACE_VIEW_RESTART_DRAW /*107505*/:
                this.surfaceView.onPause();
                this.surfaceView.onResume();
                return;
            case Constants.CAMERA_HITCH_COCK_START_VIDEO_TIME /*107602*/:
                startHitchCockCountTimer(3400);
                return;
            case Constants.MARK_POINT_A_VISIBLE_210_RESTART_WT /*107651*/:
                if (this.ll_mark_point != null) {
                    String str3 = (String) fvModeSelectEvent.getMessage();
                    this.ll_mark_point_a.setText("WT: " + str3 + "x");
                    this.ll_mark_point_a_vertical.setText("WT: " + str3 + "x");
                    CameraUtils.setLlMarkPointWtA(Float.valueOf(str3).floatValue());
                    return;
                }
                return;
            case Constants.MARK_POINT_B_VISIBLE_210_RESTART_WT /*107652*/:
                if (this.ll_mark_point != null) {
                    String str4 = (String) fvModeSelectEvent.getMessage();
                    this.ll_mark_point_b.setText("WT: " + str4 + "x");
                    this.ll_mark_point_b_vertical.setText("WT: " + str4 + "x");
                    CameraUtils.setLlMarkPointWtB(Float.valueOf(str4).floatValue());
                    return;
                }
                return;
            case Constants.MARK_POINT_A_VISIBLE_210_WT /*107653*/:
                if (this.ll_mark_point != null) {
                    int orientation = ScreenOrientationUtil.getInstance().getOrientation();
                    if (orientation != -1) {
                        if (orientation == 0) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(0.0f);
                        } else if (orientation == 90) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(0.0f);
                        } else if (orientation == 180) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(180.0f);
                        }
                        if (orientation == 270) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(180.0f);
                        }
                    }
                    CameraUtils.setMarkPointUIIsVisible(true);
                    if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                        while (this.handler.hasMessages(16)) {
                            this.handler.removeMessages(16);
                        }
                        this.handler.sendEmptyMessageDelayed(16, 10);
                    }
                    if (this.cameraManager != null) {
                        String str5 = this.ic_seekbar_content_thumb_bubble_text.getText().toString().replace("x", "");
                        if (Float.valueOf(str5).floatValue() <= 1.0f) {
                            str5 = BuildConfig.VERSION_NAME;
                        }
                        this.ll_mark_point_a.setText("WT: " + str5 + "x");
                        this.ll_mark_point_a_vertical.setText("WT: " + str5 + "x");
                        CameraUtils.setLlMarkPointWtA(Float.valueOf(str5).floatValue());
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021) {
                            if (CameraUtils.getLlMarkPointWtB() == 1.0f && CameraUtils.getMarkPointWtIsFirst()) {
                                String strV = String.valueOf(CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d);
                                this.ll_mark_point_b.setText("WT: " + strV + "x");
                                this.ll_mark_point_b_vertical.setText("WT: " + strV + "x");
                                CameraUtils.setLlMarkPointWtB(Float.valueOf(strV).floatValue());
                            }
                            CameraUtils.setMarkPointWtIsFirst(false);
                            this.iv_mark_setting_menu.setVisibility(8);
                            this.iv_mark_setting_menu_vertical.setVisibility(8);
                            return;
                        }
                        this.iv_mark_setting_menu.setVisibility(0);
                        this.iv_mark_setting_menu_vertical.setVisibility(0);
                        return;
                    }
                    return;
                }
                return;
            case Constants.MARK_POINT_B_VISIBLE_210_WT /*107654*/:
                if (this.ll_mark_point != null) {
                    int orientation2 = ScreenOrientationUtil.getInstance().getOrientation();
                    if (orientation2 != -1) {
                        if (orientation2 == 0) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(0.0f);
                        } else if (orientation2 == 90) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(0.0f);
                        } else if (orientation2 == 180) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(180.0f);
                        }
                        if (orientation2 == 270) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(180.0f);
                        }
                    }
                    CameraUtils.setMarkPointUIIsVisible(true);
                    if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                        while (this.handler.hasMessages(16)) {
                            this.handler.removeMessages(16);
                        }
                        this.handler.sendEmptyMessageDelayed(16, 10);
                    }
                    if (this.cameraManager != null) {
                        String str6 = this.ic_seekbar_content_thumb_bubble_text.getText().toString().replace("x", "");
                        if (Float.valueOf(str6).floatValue() <= 1.0f) {
                            str6 = BuildConfig.VERSION_NAME;
                        }
                        this.ll_mark_point_b.setText("WT: " + str6 + "x");
                        this.ll_mark_point_b_vertical.setText("WT: " + str6 + "x");
                        CameraUtils.setLlMarkPointWtB(Float.valueOf(str6).floatValue());
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021) {
                            if (CameraUtils.getLlMarkPointWtA() == 1.0f && CameraUtils.getMarkPointWtIsFirst()) {
                                this.ll_mark_point_a.setText("WT: " + BuildConfig.VERSION_NAME + "x");
                                this.ll_mark_point_a_vertical.setText("WT: " + BuildConfig.VERSION_NAME + "x");
                                CameraUtils.setLlMarkPointWtA(Float.valueOf(BuildConfig.VERSION_NAME).floatValue());
                            }
                            CameraUtils.setMarkPointWtIsFirst(false);
                            this.iv_mark_setting_menu.setVisibility(8);
                            this.iv_mark_setting_menu_vertical.setVisibility(8);
                            return;
                        }
                        this.iv_mark_setting_menu.setVisibility(0);
                        this.iv_mark_setting_menu_vertical.setVisibility(0);
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210 /*107705*/:
                if (CameraUtils.getFrameLayerNumber() != 0) {
                    return;
                }
                if (this.tv_setting_x_point_location_a.getVisibility() == 0) {
                    this.tv_setting_x_point_location_a.setVisibility(8);
                    return;
                } else if (this.tv_setting_x_point_location_b.getVisibility() == 0) {
                    this.tv_setting_x_point_location_b.setVisibility(8);
                    return;
                } else {
                    if (this.ll_mark_point.getVisibility() == 0) {
                    }
                    return;
                }
            case Constants.LABEL_CAMERA_VERTICAL_PANORAMIC_CANCEL /*107714*/:
                cancelFullShot();
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN_EV_VISIBLE /*107715*/:
                if (!this.cameraManager.isCameraOpened()) {
                    return;
                }
                if (this.cameraManager.getCameraManagerType() != 2) {
                    EventBusUtil.sendEvent(new Event(150));
                    return;
                } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                    EventBusUtil.sendEvent(new Event(151));
                    return;
                } else {
                    if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                        if (this.content_layout_scale.getVisibility() == 8) {
                            this.content_layout_scale.setVisibility(0);
                        }
                        CameraUtils.setCameraHandModelSmallImage(true);
                        if (this.scaleScrollViewEV.getVisibility() == 8) {
                            sendToHandlerScale(51, "");
                        }
                    } else {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (FVContentFragment.this.scaleScrollViewEV.getVisibility() == 8) {
                                    FVContentFragment.this.sendToHandlerScale(51, "");
                                }
                            }
                        }, 100);
                    }
                    if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                        Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                        return;
                    }
                    return;
                }
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN_SHUTTER_VISIBLE /*107716*/:
                if (!this.cameraManager.isCameraOpened()) {
                    return;
                }
                if (this.cameraManager.getCameraManagerType() != 2) {
                    EventBusUtil.sendEvent(new Event(150));
                    return;
                } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                    EventBusUtil.sendEvent(new Event(151));
                    return;
                } else {
                    if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                        if (this.content_layout_scale.getVisibility() == 8) {
                            this.content_layout_scale.setVisibility(0);
                        }
                        CameraUtils.setCameraHandModelSmallImage(true);
                        if (this.scaleScrollViewShutter.getVisibility() == 8) {
                            sendToHandlerScale(52, "");
                        }
                    } else {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (FVContentFragment.this.scaleScrollViewShutter.getVisibility() == 8) {
                                    FVContentFragment.this.sendToHandlerScale(52, "");
                                }
                            }
                        }, 100);
                    }
                    if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                        Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                        return;
                    }
                    return;
                }
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN_ISO_VISIBLE /*107717*/:
                if (!this.cameraManager.isCameraOpened()) {
                    return;
                }
                if (this.cameraManager.getCameraManagerType() != 2) {
                    EventBusUtil.sendEvent(new Event(150));
                    return;
                } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                    EventBusUtil.sendEvent(new Event(151));
                    return;
                } else {
                    if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                        if (this.content_layout_scale.getVisibility() == 8) {
                            this.content_layout_scale.setVisibility(0);
                        }
                        CameraUtils.setCameraHandModelSmallImage(true);
                        if (this.scaleScrollViewIsoTwo.getVisibility() == 8) {
                            sendToHandlerScale(53, "");
                        }
                    } else {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (FVContentFragment.this.scaleScrollViewIsoTwo.getVisibility() == 8) {
                                    FVContentFragment.this.sendToHandlerScale(53, "");
                                }
                            }
                        }, 100);
                    }
                    if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                        Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                        return;
                    }
                    return;
                }
            case Constants.MARK_POINT_A_VISIBLE_210 /*107724*/:
                if (this.ll_mark_point != null) {
                    int orientation3 = ScreenOrientationUtil.getInstance().getOrientation();
                    if (orientation3 != -1) {
                        if (orientation3 == 0) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(0.0f);
                        } else if (orientation3 == 90) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(0.0f);
                        } else if (orientation3 == 180) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(180.0f);
                        }
                        if (orientation3 == 270) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(180.0f);
                        }
                    }
                    CameraUtils.setMarkPointUIIsVisible(true);
                    if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                        while (this.handler.hasMessages(16)) {
                            this.handler.removeMessages(16);
                        }
                        this.handler.sendEmptyMessageDelayed(16, 10);
                    }
                    if (this.cameraManager != null) {
                        if (this.ll_setting_x_mf.getVisibility() == 0) {
                            str2 = this.ll_setting_x_mf_value.getText().toString();
                        } else {
                            str2 = CameraUtils.strSubTwoLength(String.valueOf(this.cameraManager.getFocusDistance()));
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, str2);
                        }
                        this.ll_mark_point_a.setText("MF: " + str2);
                        this.ll_mark_point_a_vertical.setText("MF: " + str2);
                        Util.sendIntEventMessge((int) Constants.CAMERA_THREAD_210_MF_EQUAL, str2);
                        CameraUtils.setLlMarkPointMfA(Float.valueOf(str2).floatValue());
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021) {
                            if (CameraUtils.getLlMarkPointMfB() == 0.0f && CameraUtils.getMarkPointMfIsFirst()) {
                                String strV2 = String.valueOf((int) (CameraUtils.getScaleScrollViewMFMaxNums() - 1.0d)) + ".00";
                                this.ll_mark_point_b.setText("MF: " + strV2);
                                this.ll_mark_point_b_vertical.setText("MF: " + strV2);
                                CameraUtils.setLlMarkPointMfB(Float.valueOf(strV2).floatValue());
                            }
                            CameraUtils.setMarkPointMfIsFirst(false);
                            this.iv_mark_setting_menu.setVisibility(8);
                            this.iv_mark_setting_menu_vertical.setVisibility(8);
                        } else {
                            this.iv_mark_setting_menu.setVisibility(0);
                            this.iv_mark_setting_menu_vertical.setVisibility(0);
                        }
                    }
                    if (this.cameraManager.isCameraOpened() && !this.cameraManager.isMaunalFocus()) {
                        this.cameraManager.enableMFMode(true);
                        return;
                    }
                    return;
                }
                return;
            case Constants.MARK_POINT_B_VISIBLE_210 /*107725*/:
                if (this.ll_mark_point != null) {
                    int orientation4 = ScreenOrientationUtil.getInstance().getOrientation();
                    if (orientation4 != -1) {
                        if (orientation4 == 0) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(0.0f);
                        } else if (orientation4 == 90) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(0.0f);
                        } else if (orientation4 == 180) {
                            if (this.ll_mark_point.getVisibility() == 8) {
                                this.ll_mark_point.setVisibility(0);
                            }
                            this.ll_mark_point.setRotation(180.0f);
                        }
                        if (orientation4 == 270) {
                            if (this.ll_mark_point_vertical.getVisibility() == 8) {
                                this.ll_mark_point_vertical.setVisibility(0);
                            }
                            this.ll_mark_point_vertical.setRotation(180.0f);
                        }
                    }
                    CameraUtils.setMarkPointUIIsVisible(true);
                    if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                        while (this.handler.hasMessages(16)) {
                            this.handler.removeMessages(16);
                        }
                        this.handler.sendEmptyMessageDelayed(16, 10);
                    }
                    if (this.cameraManager != null) {
                        if (this.ll_setting_x_mf.getVisibility() == 0) {
                            str = this.ll_setting_x_mf_value.getText().toString();
                        } else {
                            str = CameraUtils.strSubTwoLength(String.valueOf(this.cameraManager.getFocusDistance()));
                            Util.sendIntEventMessge((int) Constants.LABEL_CAMERA_MF_FRAME_VISIBLE, str);
                        }
                        this.ll_mark_point_b.setText("MF: " + str);
                        this.ll_mark_point_b_vertical.setText("MF: " + str);
                        Util.sendIntEventMessge((int) Constants.CAMERA_THREAD_210_MF_EQUAL, str);
                        CameraUtils.setLlMarkPointMfB(Float.valueOf(str).floatValue());
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021) {
                            if (CameraUtils.getLlMarkPointMfA() == 0.0f && CameraUtils.getMarkPointMfIsFirst()) {
                                this.ll_mark_point_a.setText("MF: " + "0.00");
                                this.ll_mark_point_a_vertical.setText("MF: " + "0.00");
                                CameraUtils.setLlMarkPointMfA(Float.valueOf("0.00").floatValue());
                            }
                            CameraUtils.setMarkPointMfIsFirst(false);
                            this.iv_mark_setting_menu.setVisibility(8);
                            this.iv_mark_setting_menu_vertical.setVisibility(8);
                        } else {
                            this.iv_mark_setting_menu.setVisibility(0);
                            this.iv_mark_setting_menu_vertical.setVisibility(0);
                        }
                    }
                    if (this.cameraManager.isCameraOpened() && !this.cameraManager.isMaunalFocus()) {
                        this.cameraManager.enableMFMode(true);
                        return;
                    }
                    return;
                }
                return;
            case Constants.MARK_POINT_A_VISIBLE_210_RESTART /*107728*/:
                if (this.ll_mark_point != null) {
                    String str7 = CameraUtils.strSubTwoLength((String) fvModeSelectEvent.getMessage());
                    this.ll_mark_point_a.setText("MF: " + str7);
                    this.ll_mark_point_a_vertical.setText("MF: " + str7);
                    CameraUtils.setLlMarkPointMfA(Float.valueOf(str7).floatValue());
                    return;
                }
                return;
            case Constants.MARK_POINT_B_VISIBLE_210_RESTART /*107729*/:
                if (this.ll_mark_point != null) {
                    String str8 = CameraUtils.strSubTwoLength((String) fvModeSelectEvent.getMessage());
                    this.ll_mark_point_b.setText("MF: " + str8);
                    this.ll_mark_point_b_vertical.setText("MF: " + str8);
                    CameraUtils.setLlMarkPointMfB(Float.valueOf(str8).floatValue());
                    return;
                }
                return;
            case Constants.VILTA_X_PHYSICAL_KEY_X_SETTING_MF /*107735*/:
                if (this.rlFocus != null && this.rlFocus.getVisibility() == 0) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                    this.handler.sendEmptyMessageDelayed(16, 10);
                }
                if (this.ll_setting_x_mf != null && this.ll_setting_x_mf.getVisibility() == 8) {
                    this.ll_setting_x_mf.setVisibility(0);
                }
                if (this.llSeekbar != null && this.llSeekbar.getVisibility() == 0) {
                    this.llSeekbar.setVisibility(8);
                }
                if (!this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(true);
                }
                this.ll_setting_x_mf_value.setText(CameraUtils.strSubTwoLength(String.valueOf(this.cameraManager.getFocusDistance())));
                while (this.handler.hasMessages(57)) {
                    this.handler.removeMessages(57);
                }
                this.handler.sendEmptyMessageDelayed(57, 5000);
                return;
            case Constants.VILTA_X_PHYSICAL_KEY_X_SETTING_WT /*107736*/:
                if (this.llSeekbar != null && this.llSeekbar.getVisibility() == 8) {
                    this.llSeekbar.setVisibility(0);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible()) {
                            this.ll_seekbar_inside_down.setVisibility(0);
                            this.ll_seekbar_inside_down.setOnClickListener(this);
                            return;
                        } else if (this.ll_seekbar_inside_down.getVisibility() == 0) {
                            this.ll_seekbar_inside_down.setVisibility(8);
                        }
                    }
                }
                if (this.ll_setting_x_mf != null && this.ll_setting_x_mf.getVisibility() == 0) {
                    this.ll_setting_x_mf.setVisibility(8);
                }
                if (this.cameraManager.isMaunalFocus()) {
                    this.cameraManager.enableMFMode(false);
                }
                while (this.handler.hasMessages(10)) {
                    this.handler.removeMessages(10);
                }
                this.handler.sendEmptyMessageDelayed(10, 5000);
                return;
            case Constants.CAMERA_PARAMETER_DISPLAY_VISIBLE_OR_GONE /*107737*/:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (this.content_layout_parameter_display.getVisibility() == 0) {
                    setParameterListenerNull();
                    return;
                } else if (this.content_layout_parameter_display.getVisibility() == 8) {
                    int cameraLevel32 = FVCameraManager.GetCameraLevel(this.mContext);
                    if (!this.cameraManager.isCameraOpened()) {
                        return;
                    }
                    if (this.cameraManager.getCameraManagerType() != 2) {
                        EventBusUtil.sendEvent(new Event(149));
                        return;
                    } else if (cameraLevel32 == 2) {
                        EventBusUtil.sendEvent(new Event(148));
                        return;
                    } else {
                        getParameterListener();
                        return;
                    }
                } else {
                    return;
                }
            case Constants.CAMERA_PARAMETER_DISPLAY_GONE /*107740*/:
                if (this.content_layout_parameter_display.getVisibility() == 0) {
                    setParameterListenerNull();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RESTART /*107741*/:
                Log.e("-----------------", "----------  55555  88888   相机重启了   -------");
                return;
            case Constants.CAMERA_HAND_MODEL_BG_COLOR_WHITE /*107745*/:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setCameraHandModelBgColorIsYellow(false);
                    this.handler.sendEmptyMessage(59);
                    return;
                }
                return;
            case Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW /*107746*/:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setCameraHandModelBgColorIsYellow(true);
                    this.handler.sendEmptyMessage(61);
                    return;
                }
                return;
            case Constants.CAMERA_MARK_POINT_QUIT_OUT /*107749*/:
                markPointQuitOut();
                return;
            case Constants.CAMERA_FLASH_MODE_CHANGE /*107752*/:
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() != 10101) {
                    int flashMode5 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue();
                    if (flashMode5 == 10003) {
                        Util.sendIntEventMessge(Constants.GET_FLASH_ON_210);
                        return;
                    } else if (flashMode5 == 10004) {
                        Util.sendIntEventMessge(Constants.GET_FLASH_LONG_210);
                        return;
                    } else if (flashMode5 == 10006) {
                        Util.sendIntEventMessge(Constants.GET_FLASH_OFF_210);
                        return;
                    } else if (flashMode5 == 10005) {
                        Util.sendIntEventMessge(Constants.GET_FLASH_AUTO_210);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.CAMERA_WB_MODE_CHANGE /*107753*/:
                if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                    if (this.cameraManager.getCameraManagerType() != 2) {
                        EventBusUtil.sendEvent(new Event(150));
                        return;
                    } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                        EventBusUtil.sendEvent(new Event(151));
                        return;
                    } else {
                        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                            if (this.content_layout_scale.getVisibility() == 8) {
                                this.content_layout_scale.setVisibility(0);
                            }
                            CameraUtils.setCameraHandModelSmallImage(true);
                            if (this.horizontalScaleWbTwo.getVisibility() == 8) {
                                this.scaleScrollViewShutter.setVisibility(8);
                                this.scaleScrollViewIsoTwo.setVisibility(8);
                                this.horizontalScaleWbTwo.setVisibility(0);
                                this.scaleScrollViewMf.setVisibility(8);
                                this.scaleScrollViewEV.setVisibility(8);
                                this.scaleScrollViewWT.setVisibility(8);
                                setContentHandModelVisiStateValue();
                                setTextColorWhiteAndYellow(this.horizontal_scale_wb_textview, this.horizontal_scale_wb_text_title);
                                if (!this.startWB.booleanValue()) {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            FVContentFragment.this.horizontalScaleWbTwo.setCurScale(40);
                                            Boolean unused = FVContentFragment.this.startWB = true;
                                        }
                                    }, 50);
                                }
                            }
                        } else {
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVContentFragment.this.sendToHandlerScale(54, "");
                                }
                            }, 100);
                        }
                        if (!CameraUtils.getCameraHandModelBgColorIsYellow()) {
                            Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                        }
                        if (CameraUtils.getFrameLayerNumber() == 2) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Util.sendIntEventMessge(Constants.CAMERA_WB_MODE_WINDOW_CHANGE);
                                }
                            }, 10);
                            return;
                        }
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_FZ_ON_TOUCH_START /*107761*/:
                if (this.content_layout_scale.getVisibility() != 0 || !CameraUtils.getCameraHandModel()) {
                    if (this.cameraManager == null) {
                        return;
                    }
                    if (this.cameraManager.getCameraManagerType() != 2) {
                        EventBusUtil.sendEvent(new Event(144));
                        return;
                    } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                        EventBusUtil.sendEvent(new Event(151));
                        return;
                    } else if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        EventBusUtil.sendEvent(new Event(134));
                        return;
                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                        BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = 0;
                        Util.sendIntEventMessge(Constants.VILTA_X_PHYSICAL_KEY_X_SETTING_WT);
                        return;
                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                        BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = 21;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Util.sendIntEventMessge(Constants.VILTA_X_PHYSICAL_KEY_X_SETTING_MF);
                            }
                        }, 100);
                        return;
                    } else {
                        return;
                    }
                } else if (this.cameraManager == null) {
                    return;
                } else {
                    if (this.cameraManager.getCameraManagerType() != 2) {
                        EventBusUtil.sendEvent(new Event(144));
                        return;
                    } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                        EventBusUtil.sendEvent(new Event(151));
                        return;
                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_IMPELLER_MODE_WT_VISIBLE);
                        return;
                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_IMPELLER_MODE_MF_VISIBLE);
                            }
                        }, 100);
                        return;
                    } else {
                        return;
                    }
                }
            case Constants.LABEL_CAMERA_MF_FRAME_VISIBLE /*107762*/:
                String valueFrameMF = CameraUtils.strSubTwoLength((String) fvModeSelectEvent.getMessage());
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible() && BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21 && CameraUtils.getCurrentPageIndex() == 2) {
                    float markPointMfA2 = CameraUtils.getLlMarkPointMfA();
                    float markPointMfB2 = CameraUtils.getLlMarkPointMfB();
                    float setMFValue2 = Float.valueOf(valueFrameMF).floatValue();
                    if (markPointMfA2 < markPointMfB2) {
                        if (setMFValue2 < markPointMfA2) {
                            setMFValue2 = markPointMfA2;
                        } else if (setMFValue2 > markPointMfB2) {
                            setMFValue2 = markPointMfB2;
                        }
                    } else if (markPointMfA2 > markPointMfB2) {
                        if (setMFValue2 < markPointMfB2) {
                            setMFValue2 = markPointMfB2;
                        } else if (setMFValue2 > markPointMfA2) {
                            setMFValue2 = markPointMfA2;
                        }
                    }
                    this.ll_setting_x_mf_value.setText(CameraUtils.strSubTwoLength(String.valueOf(setMFValue2) + "000"));
                } else {
                    this.ll_setting_x_mf_value.setText(valueFrameMF);
                }
                if (this.ll_setting_x_mf != null && this.ll_setting_x_mf.getVisibility() == 8) {
                    this.ll_setting_x_mf.setVisibility(0);
                }
                while (this.handler.hasMessages(57)) {
                    this.handler.removeMessages(57);
                }
                this.handler.sendEmptyMessageDelayed(57, 5000);
                return;
            case Constants.INIT_GRADIENTER_VIEW /*107774*/:
                initGradienterView();
                return;
            case Constants.REMOVE_GRADIENTER_VIEW /*107775*/:
                removeGradienterView();
                return;
            default:
                return;
        }
    }

    private void setCameraWbModeChange() {
        int wbMode = ((Integer) SPUtils.get(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
        if (wbMode == 10019) {
            SPUtils.put(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_SUNSHINE));
            setWB("daylight");
        } else if (wbMode == 10020) {
            SPUtils.put(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_OVERCAST));
            setWB("cloudy-daylight");
        } else if (wbMode == 10021) {
            SPUtils.put(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_FLUORESCENT_LAMP));
            setWB("fluorescent");
        } else if (wbMode == 10022) {
            SPUtils.put(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_INCANDESCENT_LAMP));
            setWB("incandescent");
        } else if (wbMode == 10023) {
            SPUtils.put(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
            setWB(Constants.SCENE_MODE_AUTO);
        }
    }

    private void toastAboutMarkPointCancelTV() {
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
            EventBusUtil.sendEvent(new Event(145));
        }
    }

    private void setCameraHandModelUIVisibleOrGone(boolean boo) {
        if (boo) {
            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_GONE);
            }
        } else if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void setCameraHandModelUIVideoVisibleOrGone(boolean boo) {
        if (boo) {
            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212) {
                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_GONE);
            }
        } else if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() != 107212 && !CameraUtils.isFollowIng()) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_VISIBLE);
        }
    }

    /* access modifiers changed from: private */
    public void resetWhiteBalance() {
        int wbMode = ((Integer) SPUtils.get(this.mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
        if (wbMode == 10019) {
            this.cameraManager.setWhiteBalance(Constants.SCENE_MODE_AUTO);
        } else if (wbMode == 10020) {
            this.cameraManager.setWhiteBalance("daylight");
        } else if (wbMode == 10021) {
            this.cameraManager.setWhiteBalance("cloudy-daylight");
        } else if (wbMode == 10022) {
            this.cameraManager.setWhiteBalance("fluorescent");
        } else if (wbMode == 10023) {
            this.cameraManager.setWhiteBalance("incandescent");
        }
    }

    private Rect pingMu2ImageRect(Rect rect) {
        int x1;
        int y1;
        int x2;
        int y2;
        int x1New;
        int x2New;
        int y1New;
        int y2New;
        if (((Integer) SPUtils.get(getContext(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            x1 = this.widthPingMu - rect.left;
            y1 = rect.top;
            x2 = this.widthPingMu - rect.right;
            y2 = rect.bottom;
        } else {
            x1 = rect.left;
            y1 = rect.top;
            x2 = rect.right;
            y2 = rect.bottom;
        }
        Size size = this.cameraManager.getPreviewFrameSize();
        int widthSize = size.getWidth();
        int heightSize = size.getHeight();
        int aaaX1 = (x1 * widthSize) / this.widthPingMu;
        int aaaX2 = (x2 * widthSize) / this.widthPingMu;
        int aaaY1 = (y1 * heightSize) / this.heightPingMu;
        int aaaY2 = (y2 * heightSize) / this.heightPingMu;
        if (aaaX1 >= aaaX2) {
            x1New = aaaX2;
            x2New = aaaX1;
        } else {
            x1New = aaaX1;
            x2New = aaaX2;
        }
        if (aaaY1 >= aaaY2) {
            y1New = aaaY2;
            y2New = aaaY1;
        } else {
            y1New = aaaY1;
            y2New = aaaY2;
        }
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        CameraUtils.setDisplayDensity(dm.density);
        int pingMuMianJi = dm.widthPixels * dm.heightPixels;
        int paintWidth = (int) Math.sqrt((double) (x2New - x1New));
        int paintHeight = (int) Math.sqrt((double) (y2New - y1New));
        int paintMainJi = paintWidth * paintHeight;
        float sss = ((float) paintWidth) / ((float) paintHeight);
        float zzz = ((float) (pingMuMianJi / 5)) / sss;
        if (paintMainJi == 0) {
            return new Rect(x1New, y1New, x2New, y2New);
        }
        if (pingMuMianJi / paintMainJi >= 5) {
            return new Rect(x1New, y1New, x2New, y2New);
        }
        int e1 = (int) Math.sqrt((double) zzz);
        int e2 = (int) (Math.sqrt((double) zzz) * ((double) sss));
        int zhongX = x1New + ((x2New - x1New) / 2);
        int zhongY = y1New + ((y2New - y1New) / 2);
        return new Rect(zhongX - (e2 / 2), zhongY - (e1 / 2), zhongX + (e2 / 2), zhongY + (e1 / 2));
    }

    private void getZuoBiaoXYContent(Rect rect) {
        Rect rectNew = pingMu2ImageRect(rect);
        Log.v("TrackStart", "------rectNew-------" + rectNew + "   startX:" + rectNew.left + "   startY:" + rectNew.top + "   stopX:" + rectNew.right + "   stopY:" + rectNew.bottom);
        this.customSurfaceView.setUpStartDraw();
        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_DROP))).intValue() == 107502) {
            this.cameraManager.startTrack(new Point(rectNew.left, rectNew.top), (FVTrackObserver) new FVTrackObserver() {
                public void trackRect(org.opencv.core.Rect rect) {
                    Log.v("TrackReceive", "------trackRect-------" + rect + "-------x:" + rect.f1130x + "---y:" + rect.f1131y + "--------windth:" + rect.f1130x + rect.width + "----height:" + rect.f1131y + rect.height);
                    Size size = FVContentFragment.this.cameraManager.getPreviewFrameSize();
                    int widthSize = size.getWidth();
                    int heightSize = size.getHeight();
                    Log.v("PreviewSize", "getZuoBiaoXYContent perviewsize=" + size);
                    if (((Integer) SPUtils.get(FVContentFragment.this.getContext(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        int x1 = rect.f1130x;
                        int x2 = rect.f1130x + rect.width;
                        int y1 = rect.f1131y;
                        int y2 = rect.f1131y + rect.height;
                        int aaaX1 = FVContentFragment.this.widthPingMu - ((FVContentFragment.this.widthPingMu * x1) / widthSize);
                        int aaaX2 = FVContentFragment.this.widthPingMu - ((FVContentFragment.this.widthPingMu * x2) / widthSize);
                        int aaaY1 = (FVContentFragment.this.heightPingMu * y1) / heightSize;
                        int aaaY2 = (FVContentFragment.this.heightPingMu * y2) / heightSize;
                        if (FVContentFragment.this.customSurfaceView != null) {
                            FVContentFragment.this.customSurfaceView.drawsTwo(aaaX1, aaaY1, aaaX2, aaaY2);
                            return;
                        }
                        return;
                    }
                    int x12 = rect.f1130x;
                    int x22 = rect.f1130x + rect.width;
                    int y12 = rect.f1131y;
                    int y22 = rect.f1131y + rect.height;
                    int aaaX12 = (FVContentFragment.this.widthPingMu * x12) / widthSize;
                    int aaaX22 = (FVContentFragment.this.widthPingMu * x22) / widthSize;
                    int aaaY12 = (FVContentFragment.this.heightPingMu * y12) / heightSize;
                    int aaaY22 = (FVContentFragment.this.heightPingMu * y22) / heightSize;
                    if (FVContentFragment.this.customSurfaceView != null) {
                        FVContentFragment.this.customSurfaceView.drawsTwo(aaaX12, aaaY12, aaaX22, aaaY22);
                    }
                }

                public void trackStarted() {
                }

                public void trackStopped() {
                }

                public void initTrackFailed() {
                }

                public void trackLost() {
                    if (FVContentFragment.this.customSurfaceView != null) {
                        FVContentFragment.this.customSurfaceView.drawsTwo(0, 0, 0, 0);
                    }
                }
            });
            return;
        }
        this.cameraManager.startTrack(rectNew, (FVTrackObserver) new FVTrackObserver() {
            public void trackRect(org.opencv.core.Rect rect) {
                Size size = FVContentFragment.this.cameraManager.getPreviewFrameSize();
                int widthSize = size.getWidth();
                int heightSize = size.getHeight();
                Log.v("PreviewSize", "getZuoBiaoXYContent perviewsize=" + size);
                if (((Integer) SPUtils.get(FVContentFragment.this.getContext(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    int x1 = rect.f1130x;
                    int x2 = rect.f1130x + rect.width;
                    int y1 = rect.f1131y;
                    int y2 = rect.f1131y + rect.height;
                    int aaaX1 = FVContentFragment.this.widthPingMu - ((FVContentFragment.this.widthPingMu * x1) / widthSize);
                    int aaaX2 = FVContentFragment.this.widthPingMu - ((FVContentFragment.this.widthPingMu * x2) / widthSize);
                    int aaaY1 = (FVContentFragment.this.heightPingMu * y1) / heightSize;
                    int aaaY2 = (FVContentFragment.this.heightPingMu * y2) / heightSize;
                    if (FVContentFragment.this.customSurfaceView != null) {
                        FVContentFragment.this.customSurfaceView.drawsTwo(aaaX1, aaaY1, aaaX2, aaaY2);
                        return;
                    }
                    return;
                }
                int x12 = rect.f1130x;
                int x22 = rect.f1130x + rect.width;
                int y12 = rect.f1131y;
                int y22 = rect.f1131y + rect.height;
                int aaaX12 = (FVContentFragment.this.widthPingMu * x12) / widthSize;
                int aaaX22 = (FVContentFragment.this.widthPingMu * x22) / widthSize;
                int aaaY12 = (FVContentFragment.this.heightPingMu * y12) / heightSize;
                int aaaY22 = (FVContentFragment.this.heightPingMu * y22) / heightSize;
                if (FVContentFragment.this.customSurfaceView != null) {
                    FVContentFragment.this.customSurfaceView.drawsTwo(aaaX12, aaaY12, aaaX22, aaaY22);
                }
            }

            public void trackStarted() {
            }

            public void trackStopped() {
            }

            public void initTrackFailed() {
            }

            public void trackLost() {
                if (FVContentFragment.this.customSurfaceView != null) {
                    FVContentFragment.this.customSurfaceView.drawsTwo(0, 0, 0, 0);
                }
            }
        });
    }

    private Size getScreen2CameraSize(int widthPing, int heightPing) {
        Size size = this.cameraManager.getPreviewResolution();
        return new Size((widthPing * size.getWidth()) / this.widthPingMu, (heightPing * size.getHeight()) / this.heightPingMu);
    }

    private void fullShotFailedDialog(int msg) {
        AlertDialog.Builder dialogFull = new AlertDialog.Builder(getActivity());
        dialogFull.setMessage(msg);
        dialogFull.setCancelable(false);
        dialogFull.setPositiveButton((int) C0853R.string.label_sure, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogFull.show();
    }

    public void sendToHandlerScale(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.handler.sendMessage(me);
    }

    public void sendToHandler(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.myHandler.sendMessage(me);
    }

    public void sendToHandlerMix(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.myMixHandler.sendMessage(me);
    }

    public void sendToHandlerExpo(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.expoHandler.sendMessage(me);
    }

    public void sendToHandlerExpoMix(int what, Object obj) {
        Message me = new Message();
        me.what = what;
        me.obj = obj;
        this.expoMixHandler.sendMessage(me);
    }

    /* access modifiers changed from: private */
    public String setZoomTextViewRange(String valueTextWT) {
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021 || !CameraUtils.getMarkPointUIIsVisible() || BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION != 0 || CameraUtils.getCurrentPageIndex() != 2) {
            return valueTextWT;
        }
        float markPointWtA = CameraUtils.getLlMarkPointWtA();
        float markPointWtB = CameraUtils.getLlMarkPointWtB();
        float setWtValue = Float.valueOf(valueTextWT).floatValue();
        if (markPointWtA < markPointWtB) {
            if (setWtValue < markPointWtA) {
                setWtValue = markPointWtA;
            } else if (setWtValue > markPointWtB) {
                setWtValue = markPointWtB;
            }
        } else if (markPointWtA > markPointWtB) {
            if (setWtValue < markPointWtB) {
                setWtValue = markPointWtB;
            } else if (setWtValue > markPointWtA) {
                setWtValue = markPointWtA;
            }
        }
        return String.valueOf(setWtValue);
    }

    public float getZoomChange(int progress) {
        if (this.cameraManager.getCameraManagerType() == 1) {
            return (float) (((double) progress) / (1000.0d / ((double) CameraUtils.getAppMaxZoom())));
        } else if (CameraUtils.getAppMaxZoom() <= 3.0f) {
            float zoomChange = (float) ((((double) progress) / (1000.0d / (((double) CameraUtils.getAppMaxZoom()) - 1.0d))) + 1.0d);
            if (zoomChange < 1.0f) {
                return 1.0f;
            }
            return zoomChange;
        } else if (!CameraUtils.getCameraHandModel() || this.content_layout_scale.getVisibility() != 0) {
            float zoomChange2 = (float) ((((double) progress) / (1000.0d / (((double) CameraUtils.getAppMaxZoom()) - 1.0d))) + 1.0d);
            if (zoomChange2 < 1.0f) {
                return 1.0f;
            }
            return zoomChange2;
        } else if (CameraUtils.getCurrentPageIndex() == 2) {
            float zoomChange3 = (float) ((((double) progress) / (1000.0d / (((double) CameraUtils.getAppMaxZoom()) - 1.0d))) + 1.0d);
            if (zoomChange3 < 1.0f) {
                return 1.0f;
            }
            return zoomChange3;
        } else {
            float zoomChange4 = (float) (1.0d + ((((double) Util.getDrawScaleWTMax()) / 1000.0d) * ((double) progress)));
            if (zoomChange4 < 1.0f) {
                return 1.0f;
            }
            return zoomChange4;
        }
    }

    private void showTimeDelay() {
        int delay = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
        if (delay != 100011) {
            if (delay == 100012) {
                startCountTimer(2400);
            } else if (delay == 100013) {
                startCountTimer(5400);
            } else if (delay == 10014) {
                startCountTimer(10400);
            }
        }
    }

    private void startCountTimer(long time) {
        CameraUtils.setBosIsResume(false);
        this.countTimer = new FVCountTimer(time, 1000, this.tvTimer);
        this.countTimer.start();
        CameraUtils.setDelayPhotoIng(true);
    }

    private void startHitchCockCountTimer(long time) {
        this.hitchCockCountTimer = new FVHitchCockCountTimer(time, 1000, this.tvTimer);
        this.hitchCockCountTimer.start();
    }

    public void startTimer() {
        if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 0) {
            if (this.angle == 0) {
                this.timer_video_record_start_linear_horizontal.setRotation(0.0f);
            } else if (this.angle == 90) {
                this.timer_video_record_start_linear.setRotation(-90.0f);
            } else if (this.angle == 180) {
                this.timer_video_record_start_linear_horizontal.setRotation(180.0f);
            } else if (this.angle == 270) {
                this.timer_video_record_start_linear.setRotation(90.0f);
            }
            if (this.angle == 0 || this.angle == 180) {
                this.timer_horizontal.setVisibility(0);
                this.timer_size_all_horizontal.setVisibility(0);
                this.timer_size_all_view_horizontal.setVisibility(0);
            } else {
                this.timer.setVisibility(0);
                this.timer_size_all.setVisibility(0);
                this.timer_size_all_view.setVisibility(0);
            }
            int time = (int) CameraUtils.readSDCardSizeNumber(this.mContext);
            this.timer_horizontal.setBase(SystemClock.elapsedRealtime());
            this.timer_horizontal.setFormat("0" + String.valueOf((int) (((SystemClock.elapsedRealtime() - this.timer_horizontal.getBase()) / 1000) / 60)) + ":%s");
            this.timer_horizontal.start();
            this.timer_horizontal.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                public void onChronometerTick(Chronometer chronometer) {
                    CameraUtils.setMoveOrDelayTimeLapseCurrentTime(Util.timeToSec(chronometer.getText().toString()));
                }
            });
            this.timer_size_all_horizontal.setText(Util.secToTime(time));
            this.timer.setBase(SystemClock.elapsedRealtime());
            this.timer.setFormat("0" + String.valueOf((int) (((SystemClock.elapsedRealtime() - this.timer.getBase()) / 1000) / 60)) + ":%s");
            this.timer.start();
            this.timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                public void onChronometerTick(Chronometer chronometer) {
                    CameraUtils.setMoveOrDelayTimeLapseCurrentTime(Util.timeToSec(chronometer.getText().toString()));
                }
            });
            this.timer_size_all.setText(Util.secToTime(time));
            if (this.mMyRecordingCountDownTimer != null) {
                this.mMyRecordingCountDownTimer.cancel();
                this.mMyRecordingCountDownTimer = new MyRecordingCountDownTimer((long) (time * 1000), 1000);
                this.mMyRecordingCountDownTimer.start();
                return;
            }
            this.mMyRecordingCountDownTimer = new MyRecordingCountDownTimer((long) (time * 1000), 1000);
            this.mMyRecordingCountDownTimer.start();
            return;
        }
        setCameraHandModelUIVideoVisibleOrGone(true);
        MoveTimelapseUtil.getInstance();
        MoveTimelapseUtil.setCameraVideoSymbolStart(1);
        final int shutter = MoveTimelapseUtil.getInstance().getCameraProgressLinearAllTime();
        this.linear_timer.setVisibility(0);
        this.chro_timer.setBase(SystemClock.elapsedRealtime());
        this.chro_timer.setFormat("0" + String.valueOf((int) (((SystemClock.elapsedRealtime() - this.chro_timer.getBase()) / 1000) / 60)) + ":%s");
        this.chro_timer.start();
        this.chro_timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer chronometer) {
                long timeToSec = Util.timeToSec(chronometer.getText().toString());
                CameraUtils.setMoveOrDelayTimeLapseCurrentTime(timeToSec);
                FVContentFragment.this.linear_timer_progress_bar.setProgress((int) ((100 * timeToSec) / ((long) shutter)));
            }
        });
        this.chro_all_timer.setText(Util.secToTime(shutter));
        if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 2) {
            this.linear_timer_progress_recycler.setVisibility(0);
            initProgressBarRecycler();
        } else {
            this.linear_timer_progress_recycler.setVisibility(8);
        }
        int permSystemSetting = ((Integer) SPUtil.getParam(this.mContext, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
        if (((Integer) SPUtil.getParam(this.mContext, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue() == 107770 && permSystemSetting == 107768) {
            EventBusUtil.sendEvent(new Event(19));
        }
        if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 && CameraUtils.getTimeLapseDuration() == 99999) {
            this.tv_chro_all_timer_center.setVisibility(8);
            this.chro_all_timer.setVisibility(8);
            this.rl_timer_progress_bar.setVisibility(8);
            return;
        }
        this.tv_chro_all_timer_center.setVisibility(0);
        this.chro_all_timer.setVisibility(0);
        this.rl_timer_progress_bar.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void stopScreenBrightnessTimeToDark() {
        int permSystemSetting = ((Integer) SPUtil.getParam(this.mContext, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
        if (((Integer) SPUtil.getParam(this.mContext, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue() == 107770 && permSystemSetting == 107768 && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 0) {
            EventBusUtil.sendEvent(new Event(20));
        }
    }

    class MyRecordingCountDownTimer extends CountDownTimer {
        public MyRecordingCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            FVContentFragment.this.timer_size_all.setText(Util.secToTime(((int) millisUntilFinished) / 1000));
            FVContentFragment.this.timer_size_all_horizontal.setText(Util.secToTime(((int) millisUntilFinished) / 1000));
        }

        public void onFinish() {
        }
    }

    private void initProgressBarRecycler() {
        final List<String> proBarList = new ArrayList<>();
        final List<Integer> proBarIntegerList = new ArrayList<>();
        proBarList.addAll(MoveTimelapseUtil.getInstance().getPointLineStatic());
        final double proBarListItemNum = Double.valueOf(proBarList.get(proBarList.size() - 1)).doubleValue();
        this.linear_timer_progress_recycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                double aaa;
                FVContentFragment.this.linear_timer_progress_recycler.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Double proBarOne = Double.valueOf(((double) (FVContentFragment.this.linear_timer_progress_recycler.getWidth() - (proBarList.size() * Util.dip2px(FVContentFragment.this.mContext, 8.0f)))) / proBarListItemNum);
                for (int i = 0; i < proBarList.size(); i++) {
                    if (i == 0) {
                        aaa = 0.0d;
                    } else {
                        aaa = ((double) (Integer.valueOf((String) proBarList.get(i)).intValue() - Integer.valueOf((String) proBarList.get(i - 1)).intValue())) * proBarOne.doubleValue();
                    }
                    proBarIntegerList.add(Integer.valueOf((int) aaa));
                }
                FVContentFragment.this.linear_timer_progress_recycler.setLayoutManager(new LinearLayoutManager(FVContentFragment.this.mContext, 0, false));
                FVContentFragment.this.linear_timer_progress_recycler.setAdapter(new BaseRVAdapter<Integer>(FVContentFragment.this.mContext, proBarIntegerList) {
                    public int getLayoutId(int viewType) {
                        return C0853R.layout.fragment_content_video_recycler_progress_bar_item;
                    }

                    public void onBind(BaseViewHolder holder, int position) {
                        final LinearLayout fragment_content_video_recycler_progress_bar_item_imagetext_linear = (LinearLayout) holder.getView(C0853R.C0855id.f1087xd41ee9f1);
                        fragment_content_video_recycler_progress_bar_item_imagetext_linear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            public void onGlobalLayout() {
                                fragment_content_video_recycler_progress_bar_item_imagetext_linear.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        });
                        TextView recycler_seekbar_item_view = (TextView) holder.getView(C0853R.C0855id.fragment_content_video_recycler_progress_bar_item_view);
                        ImageView imageView = (ImageView) holder.getView(C0853R.C0855id.fragment_content_video_recycler_progress_bar_item_imageview);
                        ((TextView) holder.getView(C0853R.C0855id.fragment_content_video_recycler_progress_bar_item_textview)).setText((position + 1) + "");
                        if (position == 0) {
                            recycler_seekbar_item_view.setVisibility(8);
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recycler_seekbar_item_view.getLayoutParams();
                        params.width = ((Integer) proBarIntegerList.get(position)).intValue();
                        recycler_seekbar_item_view.setLayoutParams(params);
                    }
                });
            }
        });
    }

    private void dealPtzZoom(int progress) {
        changeZoom(progress);
    }

    private void changeZoom(int progress) {
        this.curFocusLength = progress;
        if (!CameraUtils.getCameraHandModel()) {
            this.llSeekbar.setVisibility(0);
            if (CameraUtils.getCurrentPageIndex() == 2 && this.llFullShot.getVisibility() == 0) {
                this.llSeekbar.setVisibility(8);
            }
        } else if (CameraUtils.getCurrentPageIndex() == 2) {
            if (this.content_layout_scale.getVisibility() != 8 || !CameraUtils.getCameraHandModel()) {
                this.llSeekbar.setVisibility(8);
            } else {
                this.llSeekbar.setVisibility(0);
            }
        }
        CameraUtils.setCamWtProRealTimeValue(progress);
        if (this.progressOld != progress) {
            sendToHandler(71, Integer.valueOf(progress));
        }
        this.progressOld = progress;
    }

    private void changeZoomTwo(int progress) {
        this.curFocusLength = progress;
        if (!CameraUtils.getCameraHandModel()) {
            this.llSeekbar.setVisibility(0);
            if (CameraUtils.getCurrentPageIndex() == 2) {
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible()) {
                    this.ll_seekbar_inside_down.setVisibility(0);
                    this.ll_seekbar_inside_down.setOnClickListener(this);
                } else if (this.ll_seekbar_inside_down.getVisibility() == 0) {
                    this.ll_seekbar_inside_down.setVisibility(8);
                }
            }
            if (CameraUtils.getCurrentPageIndex() == 2 && this.llFullShot.getVisibility() == 0) {
                this.llSeekbar.setVisibility(8);
            }
        }
        CameraUtils.setCamWtProRealTimeValue(progress);
        if (this.progressOld != progress) {
            if (this.myHandler != null) {
                while (this.myHandler.hasMessages(71)) {
                    this.myHandler.removeMessages(71);
                }
            }
            if (this.myMixHandler != null) {
                while (this.myMixHandler.hasMessages(81)) {
                    this.myMixHandler.removeMessages(81);
                }
            }
            sendToHandler(72, Integer.valueOf(progress));
        }
        this.progressOld = progress;
    }

    private void dealPtzBright(int progress) {
        if (this.cameraManager.isCameraOpened()) {
            if (CameraUtils.getCameraHandModel()) {
                this.rlFocus.setVisibility(8);
            } else {
                this.rlFocus.setVisibility(0);
            }
            this.curBrightProgress = progress;
            this.curBright = getCurBright(progress);
            this.seekbarBrightness.setProgress(progress);
            this.seekbarBrightness2.setProgress(progress);
            if (this.curBrightOld != this.curBright) {
                sendToHandlerExpo(18, Integer.valueOf(progress));
            }
            this.curBrightOld = this.curBright;
            if (this.handler != null) {
                while (this.handler.hasMessages(16)) {
                    this.handler.removeMessages(16);
                }
                this.handler.removeMessages(16);
                this.handler.sendEmptyMessageDelayed(16, 8000);
            }
        }
    }

    private void setBrightScale(int progress) {
        if (this.cameraManager.isCameraOpened()) {
            this.curBrightScale = getCurBright(progress);
            if (this.curBrightScaleOld != this.curBrightScale) {
                Log.e("---------------", "--------  88888  999999  ------ progress: " + progress);
                sendToHandlerExpo(18, Integer.valueOf(progress));
            }
            this.curBrightScaleOld = this.curBrightScale;
        }
    }

    public int getCurBright(int progress) {
        int rangerArrayOne = Math.abs(this.cameraManager.getExposureCompensationRanger()[1]);
        if (rangerArrayOne == 0) {
            rangerArrayOne = 5;
        }
        return (int) ((((double) progress) / (50.0d / ((double) rangerArrayOne))) - ((double) rangerArrayOne));
    }

    /* access modifiers changed from: private */
    public void recoverBright() {
        if (this.seekbarBrightness != null && this.cameraManager != null && this.curBright != 0) {
            this.curBright = 0;
            this.curBrightProgress = 50;
            if (this.onResume) {
                CameraUtils.setCamExposureLengthProgress(50);
                this.seekbarBrightness.setProgress(this.curBrightProgress);
                this.seekbarBrightness2.setProgress(this.curBrightProgress);
                this.cameraManager.setExposureCompensation(0);
                CameraUtils.setCamExposureLengthProgress(this.curBrightProgress);
            }
        }
    }

    private void focusOnTheCenter() {
        recoverBright();
        setSeekbarBrightnessBackground(true);
        RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(this.rlFocus.getLayoutParams());
        layout.setMargins((Util.getDisplaySize(getActivity()).x / 2) - (this.rlFocus.getMeasuredWidth() / 2), (Util.getDisplaySize(getActivity()).y / 2) - (this.rlFocus.getMeasuredHeight() / 2), 0, 0);
        this.rlFocus.setLayoutParams(layout);
        this.rlFocus.setVisibility(0);
        Rect rect1 = calculateTapAreaCenter((float) (this.widthPingMu / 2), (float) (this.heightPingMu / 2), 1.0f);
        if (this.cameraManager.getCameraManagerType() == 1) {
            this.cameraManager.autoFocus(rect1, new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean b, Camera camera) {
                    FVContentFragment.this.cameraManager.lockAutoExposure(false);
                }
            });
            return;
        }
        if (this.cameraManager.isMaunalFocus()) {
            this.cameraManager.enableMFMode(false);
        }
        this.cameraManager.enableManualMode(false);
        this.cameraManager.autoFocus((double) (this.widthPingMu / 2), (double) (this.heightPingMu / 2), true, new Camera2Manager.AutoFocusListener() {
            public void focusLocked() {
                Log.v("TEST", "autoFocus locked");
            }

            public void focusUnlocked() {
                Log.v("TEST", "autoFocus unlocked");
            }
        });
    }

    /* access modifiers changed from: private */
    public void showTakePhotoShadow() {
        final View flashview = new View(getActivity());
        flashview.setBackgroundColor(getResources().getColor(C0853R.color.blackd0));
        this.surfaceView.addView(flashview, new LinearLayout.LayoutParams(-1, -1));
        new Thread(new Runnable() {
            public void run() {
                try {
                    boolean unused = FVContentFragment.this.isShadowShowing = true;
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpUtils.runOnUiThread(new Runnable() {
                    public void run() {
                        boolean unused = FVContentFragment.this.isShadowShowing = false;
                        FVContentFragment.this.surfaceView.removeView(flashview);
                    }
                });
            }
        }).start();
    }

    private void dealBrightProgressDrawable() {
        if (Build.VERSION.SDK_INT <= 23) {
            return;
        }
        if (this.angle == 0) {
            this.seekbarBrightness.setProgressDrawable(getResources().getDrawable(C0853R.C0854drawable.seekbar_style_yellow));
        } else {
            this.seekbarBrightness.setProgressDrawable(getResources().getDrawable(C0853R.C0854drawable.seekbar_style_tranparent));
        }
    }

    private void cancelFullShot() {
        this.llFullShot.setVisibility(8);
        Util.sendIntEventMessge(Constants.FULL_SHOT_OVER_RECOVER_UNCLICK);
        Util.sendIntEventMessge(Constants.CANCLE_PANORAMA_TO_BLE);
        CameraUtils.setFullShotIng(false);
        Log.e("------------", "-------- 全景拍照  222222 ----");
        CameraUtils.setBosIsResume(true);
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue() == 10003) {
            this.cameraManager.setFlashMode(3);
        }
        setAutoFocusModeNewCameraOpen();
        this.cameraManager.lockAutoExposure(false);
        setCameraHandModelUIVisibleOrGone(false);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case C0853R.C0855id.seekbar_focal_length:
                this.curFocusLength = progress;
                if (MoveTimelapseUtil.getRockerFocalLengthTureOrFalse()) {
                    changeZoomTwo((progress / 10) * 10);
                }
                if (this.handler != null) {
                    while (this.handler.hasMessages(10)) {
                        this.handler.removeMessages(10);
                    }
                    this.handler.sendEmptyMessageDelayed(10, 5000);
                    return;
                }
                return;
            case C0853R.C0855id.seekbar_brightness:
                if (this.cameraManager.isCameraOpened()) {
                    int rangerArrayOne = Math.abs(this.cameraManager.getExposureCompensationRanger()[1]);
                    if (rangerArrayOne == 0) {
                        rangerArrayOne = 5;
                    }
                    double spaceBet = 50.0d / ((double) rangerArrayOne);
                    this.curBrightProgress = progress;
                    if (this.curBright != ((int) ((((double) progress) / spaceBet) - ((double) rangerArrayOne)))) {
                        this.curBright = (int) ((((double) progress) / spaceBet) - ((double) rangerArrayOne));
                        if (this.curBrightOld != this.curBright) {
                            this.cameraManager.setExposureCompensation(this.curBright);
                        }
                        this.curBrightOld = this.curBright;
                    }
                }
                if (this.handler != null) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                    this.handler.removeMessages(16);
                    this.handler.sendEmptyMessageDelayed(16, 8000);
                    return;
                }
                return;
            case C0853R.C0855id.seekbar_brightness2:
                if (this.cameraManager.isCameraOpened()) {
                    this.curBrightProgress = progress;
                    int rangerArrayOne2 = Math.abs(this.cameraManager.getExposureCompensationRanger()[1]);
                    if (rangerArrayOne2 == 0) {
                        rangerArrayOne2 = 5;
                    }
                    double spaceBet2 = 50.0d / ((double) rangerArrayOne2);
                    if (this.curBright != ((int) ((((double) progress) / spaceBet2) - ((double) rangerArrayOne2)))) {
                        this.curBright = (int) ((((double) progress) / spaceBet2) - ((double) rangerArrayOne2));
                        if (this.curBrightOld != this.curBright) {
                            this.cameraManager.setExposureCompensation(this.curBright);
                        }
                        this.curBrightOld = this.curBright;
                    }
                }
                if (this.handler != null) {
                    while (this.handler.hasMessages(16)) {
                        this.handler.removeMessages(16);
                    }
                    this.handler.removeMessages(16);
                    this.handler.sendEmptyMessageDelayed(16, 8000);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case C0853R.C0855id.seekbar_focal_length:
                MoveTimelapseUtil.setRockerFocalLengthTureOrFalse(true);
                return;
            default:
                return;
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case C0853R.C0855id.seekbar_focal_length:
                CameraUtils.setCamLengthProgress(seekBar.getProgress());
                MoveTimelapseUtil.setRockerFocalLengthTureOrFalse(false);
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.iv_mark_setting_menu:
                if (((FVBottomBarFragment) ((FVMainActivity) this.mContext).getSupportFragmentManager().findFragmentByTag("bottomFragment")).isMarkRunning()) {
                    EventBusUtil.sendEvent(new Event(131));
                    return;
                } else {
                    showMarkPointChangeSettingPop();
                    return;
                }
            case C0853R.C0855id.iv_mark_quit_out:
                Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT);
                return;
            case C0853R.C0855id.iv_mark_setting_menu_vertical:
                if (((FVBottomBarFragment) ((FVMainActivity) this.mContext).getSupportFragmentManager().findFragmentByTag("bottomFragment")).isMarkRunning()) {
                    EventBusUtil.sendEvent(new Event(131));
                    return;
                } else {
                    showMarkPointChangeSettingPop();
                    return;
                }
            case C0853R.C0855id.iv_mark_quit_out_vertical:
                Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT);
                return;
            case C0853R.C0855id.btn_full_shot_cancel:
                Toast.makeText(this.mContext, getString(C0853R.string.label_full_camera_fail) + CameraUtils.getFullCameraErrorCode(), 0).show();
                cancelFullShot();
                return;
            default:
                return;
        }
    }

    private void showMarkPointChangeSettingPop() {
        int height = Util.getDeviceSize(this.mContext).y - Util.dip2px(this.mContext, 10.0f);
        FVMarkPointChangeSettingPop markPointChangeSettingPop = new FVMarkPointChangeSettingPop();
        markPointChangeSettingPop.init(this.mContext, this.iv_mark_setting_menu);
        PopupWindow pop2 = new PopupWindow(markPointChangeSettingPop.getView(), height, height);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        markPointChangeSettingPop.setPop(pop2, markPointChangeSettingPop);
        pop2.showAtLocation(this.iv_mark_setting_menu, 17, 17, 17);
        SPUtils.put(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.requestObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onDestroyView() {
        super.onDestroyView();
        Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
        if (this.customSurfaceView != null) {
            this.fragment_content_two_ll.removeView(this.customSurfaceView);
        }
        if (this.viewScale != null) {
            this.content_layout_scale.removeView(this.viewScale);
        }
        if (this.viewParameterDisplay != null) {
            this.content_layout_parameter_display.removeView(this.viewParameterDisplay);
        }
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages((Object) null);
        }
        if (this.myHandler != null) {
            this.myHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.expoHandler != null) {
            this.expoHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.myMixHandler != null) {
            this.myMixHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mRotateHandler != null) {
            this.mRotateHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.cameraManager != null) {
            this.cameraManager.destroy();
        }
        if (this.cameraZoomThread != null && this.cameraZoomThread.isAlive()) {
            this.cameraZoomThread.interrupt();
        }
        if (this.cameraZoomExpoThread != null && this.cameraZoomExpoThread.isAlive()) {
            this.cameraZoomExpoThread.interrupt();
        }
        if (this.mMyRecordingCountDownTimer != null) {
            this.mMyRecordingCountDownTimer.cancel();
        }
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    /* access modifiers changed from: private */
    public void setLinearTimerPosition(int alignParentDirection, int centerDirection, RelativeLayout.LayoutParams lp) {
        lp.removeRule(9);
        lp.removeRule(10);
        lp.removeRule(11);
        lp.removeRule(12);
        lp.addRule(alignParentDirection);
        lp.addRule(centerDirection);
        this.linear_timer.setLayoutParams(lp);
    }

    public void onDestroy() {
        super.onDestroy();
        ViseLog.m1466e("ContentFragment OnDestroy");
    }

    /* access modifiers changed from: private */
    public void changeWirelessChargeOn(boolean isOpen) {
        if (!isOpen) {
            if (getWirelessChargingState()) {
                BleByteUtil.setPTZParameters((byte) 24, (byte) 0);
                BlePtzParasConstant.isTempCloseWirelessCharge = true;
            }
        } else if (BlePtzParasConstant.isTempCloseWirelessCharge) {
            BleByteUtil.setPTZParameters((byte) 24, (byte) 1);
            BlePtzParasConstant.isTempCloseWirelessCharge = false;
        }
    }

    private boolean getWirelessChargingState() {
        return BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH == 1;
    }
}
