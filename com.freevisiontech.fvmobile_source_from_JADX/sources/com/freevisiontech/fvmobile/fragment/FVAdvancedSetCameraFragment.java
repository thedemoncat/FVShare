package com.freevisiontech.fvmobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.Log;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVAdvancedSettingAvtivity;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.BluetoothHeadsetUtil;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.FVCameraCompaModeChangeDialog;
import com.freevisiontech.fvmobile.widget.StorageChangeDialog;
import com.freevisiontech.fvmobile.widget.ToastMicrophoneBluetoothDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVAdvancedSetCameraFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    public static final String TAG = "FVAdvancedSetCameraFragment";
    /* access modifiers changed from: private */
    public BaseRVAdapter adapterHighSpeed;
    /* access modifiers changed from: private */
    public FVCameraCompaModeChangeDialog cameraCompaModeChangeDialog;
    private RelativeLayout camera_prevent_jitter_setting;
    private LinearLayout camera_prevent_jitter_setting_linear;
    /* access modifiers changed from: private */
    public SwitchButton camera_prevent_jitter_switch_button;
    /* access modifiers changed from: private */
    public int checkHighSpeedPosition;
    /* access modifiers changed from: private */
    public String highSpeed = "";
    private RadioButton high_speed_record_rb_ratio120;
    private RadioButton high_speed_record_rb_ratio240;
    private View high_speed_record_rb_ratio240_view;
    private RadioButton high_speed_record_rb_ratio480;
    private View high_speed_record_rb_ratio480_view;
    private RadioButton high_speed_record_rb_ratio960;
    private RecyclerView high_speed_record_recycler;
    private LinearLayout high_speed_record_recycler_linear;
    private RadioGroup high_speed_record_rg_ratio;
    private boolean isFront = false;
    /* access modifiers changed from: private */
    public List itemAllName;
    boolean itemIsSeekBar = false;
    /* access modifiers changed from: private */
    public List itemVisible;
    /* access modifiers changed from: private */
    public List itemVisibleSort;
    private ImageView iv_mark_model_setting_auto;
    private ImageView iv_mark_model_setting_hand;
    private ImageView iv_set_camera_lock_focus_select;
    /* access modifiers changed from: private */
    public ImageView iv_set_camera_mode_pro_select;
    /* access modifiers changed from: private */
    public ImageView iv_set_camera_mode_trad_select;
    private ImageView iv_set_camera_move_focus_select;
    private ImageView iv_set_camera_move_lapse_high_select;
    private ImageView iv_set_camera_move_lapse_low_select;
    private ImageView iv_set_camera_panama_lock_no_select;
    private ImageView iv_set_camera_panama_lock_yes_select;
    private ImageView iv_set_mark_velocity_high_select;
    private ImageView iv_set_mark_velocity_medium_select;
    private ImageView iv_set_mark_velocity_slow_select;
    /* access modifiers changed from: private */
    public ImageView iv_set_microphone_sound_bouetooth_select;
    /* access modifiers changed from: private */
    public ImageView iv_set_microphone_sound_system_select;
    private ImageView iv_set_zoom_velocity_medium_select;
    private ImageView iv_set_zoom_velocity_slow_select;
    private ImageView iv_storage_path_album;
    private ImageView iv_storage_path_sd;
    /* access modifiers changed from: private */
    public int linearDistanceTop;
    /* access modifiers changed from: private */
    public List list;
    private LinearLayout ll_camera_focus;
    private LinearLayout ll_camera_panama_save;
    private LinearLayout ll_mark_model_setting;
    private LinearLayout ll_microphone_sound;
    private LinearLayout ll_move_lapse_high_or_low;
    private LinearLayout ll_panama_lock_yes_or_no;
    private LinearLayout ll_photo_resolution;
    private LinearLayout ll_set_mark_velocity;
    private LinearLayout ll_storage_path;
    private LinearLayout ll_video_resolution;
    private LinearLayout ll_zoom_velocity_speed;
    /* access modifiers changed from: private */
    public Context mContext;
    private MyCountDownTimer mMyCountDownTimer;
    private RelativeLayout panama_save_setting;
    /* access modifiers changed from: private */
    public int photoVideoItemVisibilityNums;
    /* access modifiers changed from: private */
    public RadioButton rb_photo_ratio1920;
    /* access modifiers changed from: private */
    public RadioButton rb_photo_ratio2560;
    private View rb_photo_ratio2560_view;
    /* access modifiers changed from: private */
    public RadioButton rb_ratio1080;
    /* access modifiers changed from: private */
    public RadioButton rb_ratio2160;
    private View rb_ratio2160_linear;
    /* access modifiers changed from: private */
    public RadioButton rb_ratio480;
    private View rb_ratio480_linear;
    /* access modifiers changed from: private */
    public RadioButton rb_ratio720;
    private RadioGroup rgRatio;
    private RadioGroup rg_photo_ratio;
    private RelativeLayout rlRecoverCameraSetting;
    private RelativeLayout rl_mark_model_setting_auto;
    private RelativeLayout rl_mark_model_setting_hand;
    private RelativeLayout rl_mark_point_change_smooth;
    private RelativeLayout rl_mark_point_change_speed;
    private RelativeLayout rl_recover_camera_setting;
    private RelativeLayout rl_set_camera_lock_focus;
    private RelativeLayout rl_set_camera_mode_pro;
    private LinearLayout rl_set_camera_mode_pro_linear;
    private RelativeLayout rl_set_camera_mode_trad;
    private RelativeLayout rl_set_camera_move_focus;
    private RelativeLayout rl_set_camera_move_lapse_high;
    private RelativeLayout rl_set_camera_move_lapse_low;
    private RelativeLayout rl_set_camera_panama_lock_no;
    private RelativeLayout rl_set_camera_panama_lock_yes;
    private RelativeLayout rl_set_mark_velocity_high;
    private RelativeLayout rl_set_mark_velocity_medium;
    private RelativeLayout rl_set_mark_velocity_slow;
    private RelativeLayout rl_set_microphone_sound_bouetooth;
    private RelativeLayout rl_set_microphone_sound_system;
    private RelativeLayout rl_set_zoom_velocity_medium;
    private RelativeLayout rl_set_zoom_velocity_slow;
    private RelativeLayout rl_storage_path_album;
    private RelativeLayout rl_storage_path_sd;
    private Runnable runnable = new Runnable() {
        public void run() {
            int stP = FVAdvancedSetCameraFragment.this.linearDistanceTop;
            if (FVAdvancedSetCameraFragment.this.scrollview != null) {
                FVAdvancedSetCameraFragment.this.scrollview.scrollTo(0, stP);
            }
        }
    };
    private Runnable runnableHighSpeed = new Runnable() {
        public void run() {
            FVAdvancedSetCameraFragment.this.scrollview.scrollTo(0, 0);
        }
    };
    private boolean scaleSlide = false;
    /* access modifiers changed from: private */
    public ScrollView scrollview;
    private SeekBar seekbar_mark_point_change_smooth;
    private SeekBar seekbar_mark_point_change_speed;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    private StorageChangeDialog storageChangeDialog;
    private TextView storage_sd_card_path;
    private boolean switchButtonItemValue = false;
    private SwitchButton switchButtonPanama;
    /* access modifiers changed from: private */
    public ToastMicrophoneBluetoothDialog toastMicrophoneBluetoothDialog;
    private TextView tv_mark_point_change_setting;
    /* access modifiers changed from: private */
    public TextView tv_mark_point_change_smooth_set;
    /* access modifiers changed from: private */
    public TextView tv_mark_point_change_speed_set;
    private int variValue = 1;
    private View view;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(C0853R.layout.layout_advance_set_camera, container, false);
        this.mContext = getActivity();
        this.itemVisible = new ArrayList();
        this.itemAllName = new ArrayList(Arrays.asList(new String[]{"rb_photo_ratio2560", "rb_photo_ratio1920", "rb_ratio1080", "rb_ratio720", "rb_ratio480", "rb_ratio2160", "high_speed_record_recycler_0", "high_speed_record_recycler_1", "high_speed_record_recycler_2", "high_speed_record_recycler_3", "high_speed_record_recycler_4", "rl_set_camera_move_lapse_high", "rl_set_camera_move_lapse_low", "rl_set_camera_mode_pro", "rl_set_camera_mode_trad", "rl_set_camera_panama_lock_yes", "rl_set_camera_panama_lock_no", "rl_set_microphone_sound_system", "rl_set_microphone_sound_bouetooth", "rl_set_zoom_velocity_medium", "rl_set_zoom_velocity_slow", "panama_save_setting", "rl_storage_path_album", "rl_storage_path_sd", "camera_prevent_jitter_setting", "rl_mark_model_setting_auto", "rl_mark_model_setting_hand"}));
        this.itemVisible.add("rl_set_camera_move_lapse_high");
        this.itemVisible.add("rl_set_camera_move_lapse_low");
        this.itemVisible.add("rl_set_camera_mode_pro");
        this.itemVisible.add("rl_set_camera_mode_trad");
        this.itemVisible.add("rl_set_camera_panama_lock_yes");
        this.itemVisible.add("rl_set_camera_panama_lock_no");
        this.itemVisible.add("rl_set_microphone_sound_system");
        this.itemVisible.add("rl_set_microphone_sound_bouetooth");
        this.itemVisible.add("rl_set_zoom_velocity_medium");
        this.itemVisible.add("rl_set_zoom_velocity_slow");
        this.itemVisible.add("panama_save_setting");
        this.itemVisible.add("rl_storage_path_album");
        this.itemVisible.add("rl_storage_path_sd");
        this.itemVisible.add("rl_mark_model_setting_auto");
        this.itemVisible.add("rl_mark_model_setting_hand");
        this.ll_photo_resolution = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_photo_resolution);
        this.ll_video_resolution = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_video_resolution);
        this.high_speed_record_recycler_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.high_speed_record_recycler_linear);
        this.ll_move_lapse_high_or_low = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_move_lapse_high_or_low);
        this.rl_set_camera_mode_pro_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_mode_pro_linear);
        this.ll_camera_focus = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_camera_focus);
        this.ll_panama_lock_yes_or_no = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_panama_lock_yes_or_no);
        this.ll_microphone_sound = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_microphone_sound);
        this.ll_zoom_velocity_speed = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_zoom_velocity_speed);
        this.ll_camera_panama_save = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_camera_panama_save);
        this.ll_storage_path = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_storage_path);
        this.rl_storage_path_album = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_storage_path_album);
        this.rl_storage_path_sd = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_storage_path_sd);
        this.storage_sd_card_path = (TextView) this.view.findViewById(C0853R.C0855id.storage_sd_card_path);
        this.rl_recover_camera_setting = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_recover_camera_setting);
        this.tv_mark_point_change_setting = (TextView) this.view.findViewById(C0853R.C0855id.tv_mark_point_change_setting);
        this.rl_mark_point_change_speed = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_point_change_speed);
        this.rl_mark_point_change_smooth = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_point_change_smooth);
        this.seekbar_mark_point_change_speed = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_mark_point_change_speed);
        this.seekbar_mark_point_change_smooth = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_mark_point_change_smooth);
        this.tv_mark_point_change_speed_set = (TextView) this.view.findViewById(C0853R.C0855id.tv_mark_point_change_speed_set);
        this.tv_mark_point_change_smooth_set = (TextView) this.view.findViewById(C0853R.C0855id.tv_mark_point_change_smooth_set);
        setSeekBarMarkPointSmooth();
        this.scrollview = (ScrollView) this.view.findViewById(C0853R.C0855id.scrollview);
        this.rb_ratio2160 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_ratio2160);
        this.rb_ratio1080 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_ratio1080);
        this.rb_ratio720 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_ratio720);
        this.rb_ratio480 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_ratio480);
        this.rb_ratio1080.setText("3840x2160");
        this.rb_ratio720.setText("1920x1080");
        this.rb_ratio480.setText("1280x720");
        this.rb_ratio2160.setText("720x480");
        this.rgRatio = (RadioGroup) this.view.findViewById(C0853R.C0855id.rg_ratio);
        this.rb_ratio2160_linear = this.view.findViewById(C0853R.C0855id.rb_ratio2160_view);
        this.rb_ratio480_linear = this.view.findViewById(C0853R.C0855id.rb_ratio480_view);
        this.high_speed_record_rb_ratio120 = (RadioButton) this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio120);
        this.high_speed_record_rb_ratio240 = (RadioButton) this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio240);
        this.high_speed_record_rb_ratio480 = (RadioButton) this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio480);
        this.high_speed_record_rb_ratio960 = (RadioButton) this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio960);
        this.high_speed_record_rg_ratio = (RadioGroup) this.view.findViewById(C0853R.C0855id.high_speed_record_rg_ratio);
        this.high_speed_record_rb_ratio240_view = this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio240_view);
        this.high_speed_record_rb_ratio480_view = this.view.findViewById(C0853R.C0855id.high_speed_record_rb_ratio480_view);
        this.rlRecoverCameraSetting = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_recover_camera_setting);
        this.rg_photo_ratio = (RadioGroup) this.view.findViewById(C0853R.C0855id.rg_photo_ratio);
        this.rb_photo_ratio2560 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_photo_ratio2560);
        this.rb_photo_ratio1920 = (RadioButton) this.view.findViewById(C0853R.C0855id.rb_photo_ratio1920);
        this.rb_photo_ratio2560_view = this.view.findViewById(C0853R.C0855id.rb_photo_ratio2560_view);
        this.switchButtonPanama = (SwitchButton) this.view.findViewById(C0853R.C0855id.switchButtonPanama);
        this.rl_set_camera_move_focus = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_move_focus);
        this.iv_set_camera_move_focus_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_move_focus_select);
        this.rl_set_camera_lock_focus = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_lock_focus);
        this.iv_set_camera_lock_focus_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_lock_focus_select);
        this.rl_set_camera_move_lapse_high = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_move_lapse_high);
        this.iv_set_camera_move_lapse_high_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_move_lapse_high_select);
        this.rl_set_camera_move_lapse_low = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_move_lapse_low);
        this.iv_set_camera_move_lapse_low_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_move_lapse_low_select);
        this.rl_set_camera_panama_lock_no = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_panama_lock_no);
        this.iv_set_camera_panama_lock_no_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_panama_lock_no_select);
        this.rl_set_camera_panama_lock_yes = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_panama_lock_yes);
        this.iv_set_camera_panama_lock_yes_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_panama_lock_yes_select);
        this.rl_set_microphone_sound_system = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_microphone_sound_system);
        this.iv_set_microphone_sound_system_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_microphone_sound_system_select);
        this.rl_set_microphone_sound_bouetooth = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_microphone_sound_bouetooth);
        this.iv_set_microphone_sound_bouetooth_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_microphone_sound_bouetooth_select);
        this.rl_set_zoom_velocity_medium = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_zoom_velocity_medium);
        this.iv_set_zoom_velocity_medium_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_zoom_velocity_medium_select);
        this.rl_set_zoom_velocity_slow = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_zoom_velocity_slow);
        this.iv_set_zoom_velocity_slow_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_zoom_velocity_slow_select);
        this.rl_set_camera_mode_pro = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_mode_pro);
        this.iv_set_camera_mode_pro_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_mode_pro_select);
        this.rl_set_camera_mode_trad = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_mode_trad);
        this.iv_set_camera_mode_trad_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_camera_mode_trad_select);
        this.high_speed_record_recycler_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.high_speed_record_recycler_linear);
        this.ll_set_mark_velocity = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_set_mark_velocity);
        this.rl_set_mark_velocity_high = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_mark_velocity_high);
        this.rl_set_mark_velocity_medium = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_mark_velocity_medium);
        this.rl_set_mark_velocity_slow = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_set_mark_velocity_slow);
        this.iv_set_mark_velocity_high_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_mark_velocity_high_select);
        this.iv_set_mark_velocity_medium_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_mark_velocity_medium_select);
        this.iv_set_mark_velocity_slow_select = (ImageView) this.view.findViewById(C0853R.C0855id.iv_set_mark_velocity_slow_select);
        this.ll_mark_model_setting = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_mark_model_setting);
        this.rl_mark_model_setting_auto = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_model_setting_auto);
        this.iv_mark_model_setting_auto = (ImageView) this.view.findViewById(C0853R.C0855id.iv_mark_model_setting_auto);
        this.rl_mark_model_setting_hand = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_model_setting_hand);
        this.iv_mark_model_setting_hand = (ImageView) this.view.findViewById(C0853R.C0855id.iv_mark_model_setting_hand);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.ll_set_mark_velocity.setVisibility(8);
            this.tv_mark_point_change_setting.setVisibility(8);
            this.rl_mark_point_change_speed.setVisibility(8);
            this.rl_mark_point_change_smooth.setVisibility(8);
            this.ll_mark_model_setting.setVisibility(0);
        } else {
            this.ll_set_mark_velocity.setVisibility(8);
            this.tv_mark_point_change_setting.setVisibility(8);
            this.rl_mark_point_change_speed.setVisibility(8);
            this.rl_mark_point_change_smooth.setVisibility(8);
            this.ll_mark_model_setting.setVisibility(8);
        }
        initStatus();
        initView();
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
            this.mMyCountDownTimer.start();
        } else {
            this.mMyCountDownTimer = new MyCountDownTimer(1000, 1000);
            this.mMyCountDownTimer.start();
        }
        this.switchButtonPanama.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_SAVE_YES_NO, Integer.valueOf(Constants.PANAMA_PHOTO_SAVE_BOOLEAN_TRUE));
                } else {
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.PANAMA_PHOTO_SAVE_YES_NO, Integer.valueOf(Constants.PANAMA_PHOTO_SAVE_BOOLEAN_FALSE));
                }
            }
        });
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.PANAMA_PHOTO_SAVE_YES_NO, Integer.valueOf(Constants.PANAMA_PHOTO_SAVE_BOOLEAN_FALSE))).intValue() == 107020) {
            this.switchButtonPanama.setChecked(true);
        } else {
            this.switchButtonPanama.setChecked(false);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021) {
            this.iv_mark_model_setting_auto.setVisibility(8);
            this.iv_mark_model_setting_hand.setVisibility(0);
        } else {
            this.iv_mark_model_setting_auto.setVisibility(0);
            this.iv_mark_model_setting_hand.setVisibility(8);
        }
        this.iv_storage_path_album = (ImageView) this.view.findViewById(C0853R.C0855id.iv_storage_path_album);
        this.iv_storage_path_sd = (ImageView) this.view.findViewById(C0853R.C0855id.iv_storage_path_sd);
        this.camera_prevent_jitter_setting_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.camera_prevent_jitter_setting_linear);
        this.camera_prevent_jitter_switch_button = (SwitchButton) this.view.findViewById(C0853R.C0855id.camera_prevent_jitter_switch_button);
        this.camera_prevent_jitter_switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (CameraUtils.getVideoStabilizationSupport()) {
                        Log.e("---------------", "--------- 相机防抖  打开  打开  打开 ------");
                        SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_PREVENT_JITTER_YES_NO, Integer.valueOf(Constants.CAMERA_PREVENT_JITTER_TRUE));
                        return;
                    }
                    FVAdvancedSetCameraFragment.this.camera_prevent_jitter_switch_button.setChecked(false);
                } else if (CameraUtils.getVideoStabilizationSupport()) {
                    Log.e("---------------", "--------- 相机防抖  关闭  关闭  关闭 ------");
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_PREVENT_JITTER_YES_NO, Integer.valueOf(Constants.CAMERA_PREVENT_JITTER_FALSE));
                } else {
                    FVAdvancedSetCameraFragment.this.camera_prevent_jitter_switch_button.setChecked(false);
                }
            }
        });
        TextView camera_prevent_jitter_body_text = (TextView) this.view.findViewById(C0853R.C0855id.camera_prevent_jitter_body_text);
        if (CameraUtils.getVideoStabilizationSupport()) {
            camera_prevent_jitter_body_text.setVisibility(8);
        } else {
            camera_prevent_jitter_body_text.setVisibility(0);
            this.camera_prevent_jitter_switch_button.setClickable(false);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_PREVENT_JITTER_YES_NO, Integer.valueOf(Constants.CAMERA_PREVENT_JITTER_FALSE))).intValue() == 107017) {
            this.camera_prevent_jitter_switch_button.setChecked(true);
        } else {
            this.camera_prevent_jitter_switch_button.setChecked(false);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE))).intValue() == 107040) {
            this.iv_set_camera_move_focus_select.setVisibility(0);
            this.iv_set_camera_lock_focus_select.setVisibility(8);
        } else {
            this.iv_set_camera_move_focus_select.setVisibility(8);
            this.iv_set_camera_lock_focus_select.setVisibility(0);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_YES))).intValue() == 107080) {
            this.iv_set_camera_panama_lock_no_select.setVisibility(8);
            this.iv_set_camera_panama_lock_yes_select.setVisibility(0);
        } else {
            this.iv_set_camera_panama_lock_no_select.setVisibility(0);
            this.iv_set_camera_panama_lock_yes_select.setVisibility(8);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_SYSYTEM))).intValue() == 107091) {
            this.iv_set_microphone_sound_bouetooth_select.setVisibility(8);
            this.iv_set_microphone_sound_system_select.setVisibility(0);
        } else {
            this.iv_set_microphone_sound_bouetooth_select.setVisibility(0);
            this.iv_set_microphone_sound_system_select.setVisibility(8);
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM))).intValue() == 107094) {
            this.iv_set_zoom_velocity_medium_select.setVisibility(0);
            this.iv_set_zoom_velocity_slow_select.setVisibility(8);
        } else {
            this.iv_set_zoom_velocity_medium_select.setVisibility(8);
            this.iv_set_zoom_velocity_slow_select.setVisibility(0);
        }
        int mark_velocity = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_HIGH))).intValue();
        if (mark_velocity == 107730) {
            this.iv_set_mark_velocity_high_select.setVisibility(0);
            this.iv_set_mark_velocity_medium_select.setVisibility(8);
            this.iv_set_mark_velocity_slow_select.setVisibility(8);
        } else if (mark_velocity == 107731) {
            this.iv_set_mark_velocity_high_select.setVisibility(8);
            this.iv_set_mark_velocity_medium_select.setVisibility(0);
            this.iv_set_mark_velocity_slow_select.setVisibility(8);
        } else {
            this.iv_set_mark_velocity_high_select.setVisibility(8);
            this.iv_set_mark_velocity_medium_select.setVisibility(8);
            this.iv_set_mark_velocity_slow_select.setVisibility(0);
        }
        int storage_path = ((Integer) SPUtils.get(getActivity(), "save_storage_path", Integer.valueOf(Constants.SAVE_STORAGE_PATH_ALBUM))).intValue();
        if (Util.getSDCardPath() == null || storage_path == 107750) {
            this.iv_storage_path_album.setVisibility(0);
            this.iv_storage_path_sd.setVisibility(8);
            this.storage_sd_card_path.setVisibility(8);
        } else {
            this.iv_storage_path_album.setVisibility(8);
            this.iv_storage_path_sd.setVisibility(0);
            this.storage_sd_card_path.setVisibility(0);
            this.storage_sd_card_path.setText(getString(C0853R.string.storage_change_current_path) + ":" + Util.getParentPath(this.mContext));
        }
        this.list = new ArrayList();
        LinearLayout linearLayout = (LinearLayout) this.view.findViewById(C0853R.C0855id.rl_set_camera_mode_pro_linear);
        int cameraLevel = FVCameraManager.GetCameraLevel(getActivity());
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() != 0 || cameraLevel == -1 || cameraLevel != 2 || !Util.versionRelease()) {
        }
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() != 0) {
            if (MoveTimelapseUtil.getCameraSelectOneOrTwo() == 1) {
                this.iv_set_camera_mode_pro_select.setVisibility(8);
                this.iv_set_camera_mode_trad_select.setVisibility(0);
                CameraUtils.setUserCameraOneOrTwo(1);
                setCameraVideoOne();
                setCameraMoveLapseHighOrLowUI(1);
                this.camera_prevent_jitter_setting_linear.setVisibility(8);
            } else {
                this.iv_set_camera_mode_pro_select.setVisibility(0);
                this.iv_set_camera_mode_trad_select.setVisibility(8);
                CameraUtils.setUserCameraOneOrTwo(2);
                setCameraVideoTwo();
                setCameraMoveLapseHighOrLowUI(2);
                if (!"smartisan".equals(Build.BRAND.toLowerCase())) {
                    setCameraHighSpeedRecord();
                }
                this.camera_prevent_jitter_setting_linear.setVisibility(0);
                this.itemVisible.add("camera_prevent_jitter_setting");
            }
        } else if (cameraLevel == 2) {
            this.iv_set_camera_mode_pro_select.setVisibility(8);
            this.iv_set_camera_mode_trad_select.setVisibility(0);
            CameraUtils.setUserCameraOneOrTwo(1);
            setCameraVideoOne();
            setCameraMoveLapseHighOrLowUI(1);
            this.camera_prevent_jitter_setting_linear.setVisibility(8);
        } else if (Util.versionRelease()) {
            this.iv_set_camera_mode_pro_select.setVisibility(0);
            this.iv_set_camera_mode_trad_select.setVisibility(8);
            CameraUtils.setUserCameraOneOrTwo(2);
            setCameraVideoTwo();
            setCameraMoveLapseHighOrLowUI(2);
            if (!"smartisan".equals(Build.BRAND.toLowerCase())) {
                setCameraHighSpeedRecord();
            }
            this.camera_prevent_jitter_setting_linear.setVisibility(0);
            this.itemVisible.add("camera_prevent_jitter_setting");
        } else if (cameraLevel == -1) {
            this.iv_set_camera_mode_pro_select.setVisibility(8);
            this.iv_set_camera_mode_trad_select.setVisibility(0);
            CameraUtils.setUserCameraOneOrTwo(1);
            setCameraVideoOne();
            setCameraMoveLapseHighOrLowUI(1);
            this.camera_prevent_jitter_setting_linear.setVisibility(8);
        } else {
            this.iv_set_camera_mode_pro_select.setVisibility(0);
            this.iv_set_camera_mode_trad_select.setVisibility(8);
            CameraUtils.setUserCameraOneOrTwo(2);
            setCameraVideoTwo();
            setCameraMoveLapseHighOrLowUI(2);
            if (!"smartisan".equals(Build.BRAND.toLowerCase())) {
                setCameraHighSpeedRecord();
            }
            this.camera_prevent_jitter_setting_linear.setVisibility(0);
            this.itemVisible.add("camera_prevent_jitter_setting");
        }
        TextView rl_set_camera_mode_pro_text = (TextView) this.view.findViewById(C0853R.C0855id.rl_set_camera_mode_pro_text);
        if (cameraLevel == 2) {
            rl_set_camera_mode_pro_text.setText(C0853R.string.label_camera_mode_trad_text);
        } else if (Util.versionRelease()) {
            rl_set_camera_mode_pro_text.setText(C0853R.string.label_camera_mode_pro_text);
        } else if (cameraLevel == -1) {
            rl_set_camera_mode_pro_text.setText(C0853R.string.label_camera_mode_trad_text);
        } else {
            rl_set_camera_mode_pro_text.setText(C0853R.string.label_camera_mode_pro_text);
        }
        setCameraCheckEnableUI();
        this.panama_save_setting = (RelativeLayout) this.view.findViewById(C0853R.C0855id.panama_save_setting);
        this.camera_prevent_jitter_setting = (RelativeLayout) this.view.findViewById(C0853R.C0855id.camera_prevent_jitter_setting);
        CameraUtils.setFrameLayerNumber(21);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
                    List unused = FVAdvancedSetCameraFragment.this.itemVisibleSort = new ArrayList();
                    FVAdvancedSetCameraFragment.this.itemVisibleSort.clear();
                    for (int i = 0; i < FVAdvancedSetCameraFragment.this.itemAllName.size(); i++) {
                        for (int a = 0; a < FVAdvancedSetCameraFragment.this.itemVisible.size(); a++) {
                            if (FVAdvancedSetCameraFragment.this.itemAllName.get(i).toString().equals(FVAdvancedSetCameraFragment.this.itemVisible.get(a).toString())) {
                                FVAdvancedSetCameraFragment.this.itemVisibleSort.add(FVAdvancedSetCameraFragment.this.itemAllName.get(i).toString());
                            }
                        }
                    }
                    Log.e("-----------------", "----------  6666  7777  8888  9999   ---------  itemVisibleSort:" + FVAdvancedSetCameraFragment.this.itemVisibleSort);
                    int unused2 = FVAdvancedSetCameraFragment.this.stirPosition = 0;
                    FVAdvancedSetCameraFragment.this.setControlOnClickItemBlackgroundColor(FVAdvancedSetCameraFragment.this.controlItemStringToView(FVAdvancedSetCameraFragment.this.itemVisibleSort.get(FVAdvancedSetCameraFragment.this.stirPosition).toString()));
                }
                Log.e("-------------------", "---------   6666   8888   9999   7777   ----------   visNums:" + FVAdvancedSetCameraFragment.this.photoVideoItemVisibilityNums);
            }
        }, 100);
        this.storageChangeDialog = new StorageChangeDialog(this.mContext);
        return this.view;
    }

    private void setSeekBarMarkPointSmooth() {
        int changeSpeed = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue();
        this.seekbar_mark_point_change_speed.setProgress(changeSpeed);
        this.tv_mark_point_change_speed_set.setText((changeSpeed + 1) + "");
        int changeSmooth = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, 0)).intValue();
        this.seekbar_mark_point_change_smooth.setProgress(changeSmooth);
        this.tv_mark_point_change_smooth_set.setText((changeSmooth + 1) + "");
        this.seekbar_mark_point_change_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"LongLogTag"})
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetCameraFragment.TAG, "onProgressChange: change_speed" + i);
                int progress = seekBar.getProgress();
                FVAdvancedSetCameraFragment.this.tv_mark_point_change_speed_set.setText((progress + 1) + "");
                SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, Integer.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetCameraFragment.this.tv_mark_point_change_speed_set.setText((progress + 1) + "");
                SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, Integer.valueOf(progress));
            }
        });
        this.seekbar_mark_point_change_smooth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"LongLogTag"})
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e(FVAdvancedSetCameraFragment.TAG, "onProgressChange: change_speed" + i);
                int progress = seekBar.getProgress();
                FVAdvancedSetCameraFragment.this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, Integer.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVAdvancedSetCameraFragment.this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, Integer.valueOf(progress));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_SETTING_STIR_UP_210 /*107706*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    String value2 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   波轮拨动向下   波轮拨动向下" + this.stirPosition);
                    controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString());
                    Boolean isSwitchButton = Boolean.valueOf(setControlOnClickIsSwitchButton(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString())));
                    if (this.itemIsSeekBar) {
                        String str = this.itemVisibleSort.get(this.stirPosition).toString();
                        if (str.equals("rl_mark_point_change_speed")) {
                            setControlSeekBarValue(0, true, Integer.valueOf(value2).intValue());
                        } else if (str.equals("rl_mark_point_change_smooth")) {
                            setControlSeekBarValue(1, true, Integer.valueOf(value2).intValue());
                        }
                        setControlSeekBarColorGrayToYellow(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                        return;
                    } else if (!isSwitchButton.booleanValue()) {
                        return;
                    } else {
                        if (controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()) == this.panama_save_setting) {
                            if (this.switchButtonPanama.isChecked()) {
                                this.switchButtonPanama.setChecked(false);
                                return;
                            } else {
                                this.switchButtonPanama.setChecked(true);
                                return;
                            }
                        } else if (controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()) != this.camera_prevent_jitter_setting) {
                            return;
                        } else {
                            if (this.camera_prevent_jitter_switch_button.isChecked()) {
                                this.camera_prevent_jitter_switch_button.setChecked(false);
                                return;
                            } else {
                                this.camera_prevent_jitter_switch_button.setChecked(true);
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            case Constants.LABEL_SETTING_STIR_DOWN_210 /*107707*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    String value22 = (String) fvModeSelectEvent.getMessage();
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向上   波轮拨动向上   波轮拨动向上" + this.stirPosition);
                    controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString());
                    Boolean isSwitchButton2 = Boolean.valueOf(setControlOnClickIsSwitchButton(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString())));
                    if (this.itemIsSeekBar) {
                        String str2 = this.itemVisibleSort.get(this.stirPosition).toString();
                        if (str2.equals("rl_mark_point_change_speed")) {
                            setControlSeekBarValue(0, false, Integer.valueOf(value22).intValue());
                        } else if (str2.equals("rl_mark_point_change_smooth")) {
                            setControlSeekBarValue(1, false, Integer.valueOf(value22).intValue());
                        }
                        setControlSeekBarColorGrayToYellow(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                        return;
                    } else if (!isSwitchButton2.booleanValue()) {
                        return;
                    } else {
                        if (controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()) == this.panama_save_setting) {
                            if (this.switchButtonPanama.isChecked()) {
                                this.switchButtonPanama.setChecked(false);
                                return;
                            } else {
                                this.switchButtonPanama.setChecked(true);
                                return;
                            }
                        } else if (controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()) != this.camera_prevent_jitter_setting) {
                            return;
                        } else {
                            if (this.camera_prevent_jitter_switch_button.isChecked()) {
                                this.camera_prevent_jitter_switch_button.setChecked(false);
                                return;
                            } else {
                                this.camera_prevent_jitter_switch_button.setChecked(true);
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            case Constants.LABEL_SETTING_OK_TOP_BAR_UP_OR_DOWN_210 /*107708*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString());
                    if (!this.itemIsSeekBar) {
                        Log.e("-----------------", "----------  7777  8888  9999   -------  OK键  OK键  OK键  OK键" + this.itemVisibleSort.get(this.stirPosition).toString());
                        setControlOnClickItemDown(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_SETTING_RETURN_KEY_210 /*107709*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  云台操作返回键 ");
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_UP_210 /*107710*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 摇杆拨动向上   向上   向上 " + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    setControlSeekBarColorToGray();
                    return;
                }
                return;
            case Constants.LABEL_SETTING_ROCKING_BAR_DOWN_210 /*107711*/:
                if (CameraUtils.getFrameLayerNumber() == 21) {
                    this.stirPosition++;
                    if (this.stirPosition > this.itemVisibleSort.size() - 1) {
                        this.stirPosition = this.itemVisibleSort.size() - 1;
                    }
                    Log.e("-----------------", "----------  7777  8888  9999   -------  摇杆拨动向下   向下   向下" + this.stirPosition);
                    setControlOnClickItemBlackgroundColor(controlItemStringToView(this.itemVisibleSort.get(this.stirPosition).toString()));
                    setControlSeekBarColorToGray();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setControlSeekBarColorToGray() {
        this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
        this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
        this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
    }

    private void setControlSeekBarValue(int num, boolean boo, int vaValue) {
        int progress;
        int progress2;
        this.variValue = vaValue;
        switch (num) {
            case 0:
                int progress3 = this.seekbar_mark_point_change_speed.getProgress();
                if (!boo) {
                    progress2 = progress3 - this.variValue;
                    if (progress2 < 0) {
                        progress2 = 0;
                    }
                } else {
                    progress2 = progress3 + this.variValue;
                    if (progress2 > 6) {
                        progress2 = 6;
                    }
                }
                this.tv_mark_point_change_speed_set.setText((progress2 + 1) + "");
                this.seekbar_mark_point_change_speed.setProgress(progress2);
                return;
            case 1:
                int progress4 = this.seekbar_mark_point_change_smooth.getProgress();
                if (!boo) {
                    progress = progress4 - this.variValue;
                    if (progress < 0) {
                        progress = 0;
                    }
                } else {
                    progress = progress4 + this.variValue;
                    if (progress > 4) {
                        progress = 4;
                    }
                }
                this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                this.seekbar_mark_point_change_smooth.setProgress(progress);
                return;
            default:
                return;
        }
    }

    public void setControlSeekBarColorGrayToYellow(View view2) {
        switch (view2.getId()) {
            case C0853R.C0855id.rl_mark_point_change_speed:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                return;
            case C0853R.C0855id.rl_mark_point_change_smooth:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                return;
            default:
                return;
        }
    }

    public void setControlOnClickItemDown(View view2) {
        switch (view2.getId()) {
            case C0853R.C0855id.rb_photo_ratio2560:
                this.rb_photo_ratio2560.setEnabled(false);
                this.rb_photo_ratio1920.setEnabled(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.MAX_LEVEL));
                    CameraUtils.setMaxSupOrReComPictureFrontSize(CameraUtils.MAX_LEVEL);
                    return;
                }
                SPUtils.put(getActivity(), SharePrefConstant.PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.MAX_LEVEL));
                CameraUtils.setMaxSupOrReComPictureSize(CameraUtils.MAX_LEVEL);
                return;
            case C0853R.C0855id.rb_photo_ratio1920:
                this.rb_photo_ratio2560.setEnabled(true);
                this.rb_photo_ratio1920.setEnabled(false);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.RECOMMEND_LEVEL));
                    CameraUtils.setMaxSupOrReComPictureFrontSize(CameraUtils.RECOMMEND_LEVEL);
                    return;
                }
                SPUtils.put(getActivity(), SharePrefConstant.PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.RECOMMEND_LEVEL));
                CameraUtils.setMaxSupOrReComPictureSize(CameraUtils.RECOMMEND_LEVEL);
                return;
            case C0853R.C0855id.rb_ratio1080:
                this.rb_ratio1080.setEnabled(false);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num1080 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio1080.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num1080);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num1080));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num1080);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num1080));
                    }
                } else {
                    Size num10802 = CameraUtils.getRecordStringToSize(this.rb_ratio1080.getText().toString());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num10802);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num10802.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num10802);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num10802.toString());
                        String str = (String) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, "");
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio720:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(false);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num720 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio720.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num720);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num720));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num720);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num720));
                    }
                } else {
                    int lensMode720 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size num7202 = CameraUtils.getRecordStringToSize(this.rb_ratio720.getText().toString());
                    if (lensMode720 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num7202);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num7202.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num7202);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num7202.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio480:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(false);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num480 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio480.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num480);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num480));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num480);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num480));
                    }
                } else {
                    int lensMode480 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size num4802 = CameraUtils.getRecordStringToSize(this.rb_ratio480.getText().toString());
                    if (lensMode480 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num4802);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num4802.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num4802);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num4802.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio2160:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(false);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num2160 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio2160.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num2160);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num2160));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num2160);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num2160));
                    }
                } else {
                    int lensMode2160 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size num21602 = CameraUtils.getRecordStringToSize(this.rb_ratio2160.getText().toString());
                    if (lensMode2160 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num21602);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num21602.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num21602);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num21602.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.high_speed_record_recycler:
                this.checkHighSpeedPosition = this.stirPosition - this.photoVideoItemVisibilityNums;
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, this.list.get(this.checkHighSpeedPosition).toString());
                } else {
                    SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, this.list.get(this.checkHighSpeedPosition).toString());
                }
                this.adapterHighSpeed.notifyDataSetChanged();
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                return;
            case C0853R.C0855id.rl_set_camera_move_lapse_high:
                this.iv_set_camera_move_lapse_high_select.setVisibility(0);
                this.iv_set_camera_move_lapse_low_select.setVisibility(8);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH));
                    return;
                } else {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH));
                    return;
                }
            case C0853R.C0855id.rl_set_camera_move_lapse_low:
                this.iv_set_camera_move_lapse_high_select.setVisibility(8);
                this.iv_set_camera_move_lapse_low_select.setVisibility(0);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_LOW));
                    return;
                } else {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_LOW));
                    return;
                }
            case C0853R.C0855id.rl_set_camera_mode_pro:
                if (this.iv_set_camera_mode_pro_select.getVisibility() == 8) {
                    dialogCameraModeProSelect();
                    return;
                }
                return;
            case C0853R.C0855id.rl_set_camera_mode_trad:
                if (this.iv_set_camera_mode_trad_select.getVisibility() == 8) {
                    dialogCameraModeTradSelect();
                    return;
                }
                return;
            case C0853R.C0855id.rl_set_camera_panama_lock_yes:
                this.iv_set_camera_panama_lock_no_select.setVisibility(8);
                this.iv_set_camera_panama_lock_yes_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_YES));
                return;
            case C0853R.C0855id.rl_set_camera_panama_lock_no:
                this.iv_set_camera_panama_lock_no_select.setVisibility(0);
                this.iv_set_camera_panama_lock_yes_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_NO));
                return;
            case C0853R.C0855id.rl_set_microphone_sound_system:
                this.iv_set_microphone_sound_bouetooth_select.setVisibility(8);
                this.iv_set_microphone_sound_system_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_SYSYTEM));
                return;
            case C0853R.C0855id.rl_set_microphone_sound_bouetooth:
                if (BluetoothHeadsetUtil.isHeadSetCanUse()) {
                    this.iv_set_microphone_sound_bouetooth_select.setVisibility(0);
                    this.iv_set_microphone_sound_system_select.setVisibility(8);
                    SPUtils.put(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_BOUETOOTH));
                    return;
                }
                toastMicrophoneBluetooth();
                return;
            case C0853R.C0855id.rl_set_zoom_velocity_medium:
                this.iv_set_zoom_velocity_medium_select.setVisibility(0);
                this.iv_set_zoom_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM));
                return;
            case C0853R.C0855id.rl_set_zoom_velocity_slow:
                this.iv_set_zoom_velocity_medium_select.setVisibility(8);
                this.iv_set_zoom_velocity_slow_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_SLOW));
                return;
            case C0853R.C0855id.panama_save_setting:
                if (this.switchButtonPanama.isChecked()) {
                    this.switchButtonPanama.setChecked(false);
                    return;
                } else {
                    this.switchButtonPanama.setChecked(true);
                    return;
                }
            case C0853R.C0855id.rl_storage_path_album:
                this.iv_storage_path_album.setVisibility(0);
                this.iv_storage_path_sd.setVisibility(8);
                this.storage_sd_card_path.setVisibility(8);
                SPUtils.put(getActivity(), "save_storage_path", Integer.valueOf(Constants.SAVE_STORAGE_PATH_ALBUM));
                SPUtils.put(getActivity(), SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile");
                this.storage_sd_card_path.setText("");
                return;
            case C0853R.C0855id.rl_storage_path_sd:
                if (Util.getSDCardPath() == null) {
                    this.storageChangeDialog.show();
                    this.storageChangeDialog.setMessage(getString(C0853R.string.storage_change_no_sd_card_tip));
                    this.storage_sd_card_path.setText("");
                    return;
                }
                this.iv_storage_path_album.setVisibility(8);
                this.iv_storage_path_sd.setVisibility(0);
                this.storage_sd_card_path.setVisibility(0);
                this.storageChangeDialog.show();
                this.storageChangeDialog.setMessage(getString(C0853R.string.storage_change_has_sd_card_tip));
                SPUtils.put(getActivity(), "save_storage_path", Integer.valueOf(Constants.SAVE_STORAGE_PATH_SD));
                saveToSdCard();
                return;
            case C0853R.C0855id.camera_prevent_jitter_setting:
                if (this.camera_prevent_jitter_switch_button.isChecked()) {
                    this.camera_prevent_jitter_switch_button.setChecked(false);
                    return;
                } else {
                    this.camera_prevent_jitter_switch_button.setChecked(true);
                    return;
                }
            case C0853R.C0855id.rl_mark_point_change_speed:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                return;
            case C0853R.C0855id.rl_mark_point_change_smooth:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                return;
            case C0853R.C0855id.rl_mark_model_setting_auto:
                this.iv_mark_model_setting_auto.setVisibility(0);
                this.iv_mark_model_setting_hand.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO));
                return;
            case C0853R.C0855id.rl_mark_model_setting_hand:
                this.iv_mark_model_setting_auto.setVisibility(8);
                this.iv_mark_model_setting_hand.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_YES));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_high:
                this.iv_set_mark_velocity_high_select.setVisibility(0);
                this.iv_set_mark_velocity_medium_select.setVisibility(8);
                this.iv_set_mark_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_HIGH));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_medium:
                this.iv_set_mark_velocity_high_select.setVisibility(8);
                this.iv_set_mark_velocity_medium_select.setVisibility(0);
                this.iv_set_mark_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_MEDIUM));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_slow:
                this.iv_set_mark_velocity_high_select.setVisibility(8);
                this.iv_set_mark_velocity_medium_select.setVisibility(8);
                this.iv_set_mark_velocity_slow_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_SLOW));
                return;
            default:
                return;
        }
    }

    public boolean setControlOnClickIsSwitchButton(View view2) {
        this.switchButtonItemValue = false;
        if (view2 == this.panama_save_setting) {
            this.switchButtonItemValue = true;
        } else if (view2 == this.camera_prevent_jitter_setting) {
            this.switchButtonItemValue = true;
        }
        return this.switchButtonItemValue;
    }

    public View controlItemStringToView(String str) {
        if (str.equals("rb_photo_ratio2560")) {
            this.linearDistanceTop = this.ll_photo_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_photo_ratio2560;
        } else if (str.equals("rb_photo_ratio1920")) {
            this.linearDistanceTop = this.ll_photo_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_photo_ratio1920;
        } else if (str.equals("rb_ratio1080")) {
            this.linearDistanceTop = this.ll_video_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_ratio1080;
        } else if (str.equals("rb_ratio720")) {
            this.linearDistanceTop = this.ll_video_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_ratio720;
        } else if (str.equals("rb_ratio480")) {
            this.linearDistanceTop = this.ll_video_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_ratio480;
        } else if (str.equals("rb_ratio2160")) {
            this.linearDistanceTop = this.ll_video_resolution.getTop();
            this.itemIsSeekBar = false;
            return this.rb_ratio2160;
        } else if (str.equals("rl_set_camera_move_lapse_high")) {
            this.linearDistanceTop = this.ll_move_lapse_high_or_low.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_move_lapse_high;
        } else if (str.equals("rl_set_camera_move_lapse_low")) {
            this.linearDistanceTop = this.ll_move_lapse_high_or_low.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_move_lapse_low;
        } else if (str.equals("rl_set_camera_mode_pro")) {
            this.linearDistanceTop = this.rl_set_camera_mode_pro_linear.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_mode_pro;
        } else if (str.equals("rl_set_camera_mode_trad")) {
            this.linearDistanceTop = this.rl_set_camera_mode_pro_linear.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_mode_trad;
        } else if (str.equals("rl_set_camera_panama_lock_yes")) {
            this.linearDistanceTop = this.ll_panama_lock_yes_or_no.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_panama_lock_yes;
        } else if (str.equals("rl_set_camera_panama_lock_no")) {
            this.linearDistanceTop = this.ll_panama_lock_yes_or_no.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_camera_panama_lock_no;
        } else if (str.equals("rl_set_microphone_sound_system")) {
            this.linearDistanceTop = this.ll_microphone_sound.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_microphone_sound_system;
        } else if (str.equals("rl_set_microphone_sound_bouetooth")) {
            this.linearDistanceTop = this.ll_microphone_sound.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_microphone_sound_bouetooth;
        } else if (str.equals("rl_set_zoom_velocity_medium")) {
            this.linearDistanceTop = this.ll_zoom_velocity_speed.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_zoom_velocity_medium;
        } else if (str.equals("rl_set_zoom_velocity_slow")) {
            this.linearDistanceTop = this.ll_zoom_velocity_speed.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_zoom_velocity_slow;
        } else if (str.equals("panama_save_setting")) {
            this.linearDistanceTop = this.ll_camera_panama_save.getTop();
            this.itemIsSeekBar = false;
            return this.panama_save_setting;
        } else if (str.equals("rl_storage_path_album")) {
            this.linearDistanceTop = this.ll_storage_path.getTop();
            this.itemIsSeekBar = false;
            return this.rl_storage_path_album;
        } else if (str.equals("rl_storage_path_sd")) {
            this.linearDistanceTop = this.ll_storage_path.getTop();
            this.itemIsSeekBar = false;
            return this.rl_storage_path_sd;
        } else if (str.equals("rl_mark_model_setting_auto")) {
            this.linearDistanceTop = this.ll_mark_model_setting.getTop();
            this.itemIsSeekBar = false;
            return this.rl_mark_model_setting_auto;
        } else if (str.equals("rl_mark_model_setting_hand")) {
            this.linearDistanceTop = this.ll_mark_model_setting.getTop();
            this.itemIsSeekBar = false;
            return this.rl_mark_model_setting_hand;
        } else if (str.equals("camera_prevent_jitter_setting")) {
            this.linearDistanceTop = this.camera_prevent_jitter_setting_linear.getTop();
            this.itemIsSeekBar = false;
            return this.camera_prevent_jitter_setting;
        } else if (str.equals("rl_set_mark_velocity_high")) {
            this.linearDistanceTop = this.ll_set_mark_velocity.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_mark_velocity_high;
        } else if (str.equals("rl_set_mark_velocity_medium")) {
            this.linearDistanceTop = this.ll_set_mark_velocity.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_mark_velocity_medium;
        } else if (str.equals("rl_set_mark_velocity_slow")) {
            this.linearDistanceTop = this.ll_set_mark_velocity.getTop();
            this.itemIsSeekBar = false;
            return this.rl_set_mark_velocity_slow;
        } else if (str.equals("rl_mark_point_change_speed")) {
            this.linearDistanceTop = this.rl_mark_point_change_speed.getTop();
            this.itemIsSeekBar = true;
            return this.rl_mark_point_change_speed;
        } else if (str.equals("rl_mark_point_change_smooth")) {
            this.linearDistanceTop = this.rl_mark_point_change_smooth.getTop();
            this.itemIsSeekBar = true;
            return this.rl_mark_point_change_smooth;
        } else {
            this.linearDistanceTop = this.high_speed_record_recycler_linear.getTop();
            this.itemIsSeekBar = false;
            return this.high_speed_record_recycler;
        }
    }

    public void setControlOnClickItemBlackgroundColor(View view2) {
        this.rb_photo_ratio2560.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rb_photo_ratio1920.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rb_ratio1080.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rb_ratio720.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rb_ratio480.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rb_ratio2160.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_move_lapse_high.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_move_lapse_low.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_mode_pro.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_mode_trad.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_panama_lock_yes.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_camera_panama_lock_no.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_microphone_sound_system.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_microphone_sound_bouetooth.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_zoom_velocity_medium.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_zoom_velocity_slow.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.panama_save_setting.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_storage_path_album.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_storage_path_sd.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_mark_model_setting_auto.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_mark_model_setting_hand.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.camera_prevent_jitter_setting.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_mark_velocity_high.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_mark_velocity_medium.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_set_mark_velocity_slow.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_mark_point_change_speed.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        this.rl_mark_point_change_smooth.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.color_white));
        new ArrayList(Arrays.asList(new String[]{"high_speed_record_recycler_0", "high_speed_record_recycler_1", "high_speed_record_recycler_2", "high_speed_record_recycler_3", "high_speed_record_recycler_4"}));
        if (view2 == this.high_speed_record_recycler) {
            Log.e("----------------", "---------  5555  8888  6666  recycler --------");
            if (this.adapterHighSpeed != null) {
                this.adapterHighSpeed.notifyDataSetChanged();
            }
            new Handler().postDelayed(this.runnable, 200);
            return;
        }
        view2.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.black15));
        Log.e("----------------", "---------  5555  8888  6666  item --------");
        new Handler().postDelayed(this.runnable, 200);
        if (this.adapterHighSpeed != null) {
            this.adapterHighSpeed.notifyDataSetChanged();
        }
    }

    private void setCameraMoveLapseHighOrLowUI(int select) {
        TextView rl_set_camera_move_lapse_high_body_text = (TextView) this.view.findViewById(C0853R.C0855id.rl_set_camera_move_lapse_high_body_text);
        if (select == 1) {
            rl_set_camera_move_lapse_high_body_text.setVisibility(8);
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
                this.iv_set_camera_move_lapse_high_select.setVisibility(0);
                this.iv_set_camera_move_lapse_low_select.setVisibility(8);
                return;
            }
            this.iv_set_camera_move_lapse_high_select.setVisibility(8);
            this.iv_set_camera_move_lapse_low_select.setVisibility(0);
            return;
        }
        rl_set_camera_move_lapse_high_body_text.setVisibility(0);
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
            this.iv_set_camera_move_lapse_high_select.setVisibility(0);
            this.iv_set_camera_move_lapse_low_select.setVisibility(8);
            return;
        }
        this.iv_set_camera_move_lapse_high_select.setVisibility(8);
        this.iv_set_camera_move_lapse_low_select.setVisibility(0);
    }

    private void setCameraCheckEnableUI() {
        int cameraLevel = FVCameraManager.GetCameraLevel(getActivity());
        if (MoveTimelapseUtil.getCameraSelectOneOrTwo() != 0) {
            if (MoveTimelapseUtil.getCameraSelectOneOrTwo() == 1) {
                CameraUtils.setUserCameraOneOrTwo(1);
            } else {
                CameraUtils.setUserCameraOneOrTwo(2);
            }
        } else if (cameraLevel == 2) {
            CameraUtils.setUserCameraOneOrTwo(1);
        } else if (Util.versionRelease()) {
            CameraUtils.setUserCameraOneOrTwo(2);
        } else if (cameraLevel == -1) {
            CameraUtils.setUserCameraOneOrTwo(1);
        } else {
            CameraUtils.setUserCameraOneOrTwo(2);
        }
    }

    private void setCameraHighSpeedRecord() {
        this.high_speed_record_recycler_linear.setVisibility(0);
        this.high_speed_record_recycler = (RecyclerView) this.view.findViewById(C0853R.C0855id.high_speed_record_recycler);
        LinearLayout high_speed_record_recycler_linear2 = (LinearLayout) this.view.findViewById(C0853R.C0855id.high_speed_record_recycler_linear);
        Map<Range<Integer>, Size[]> map1 = CameraUtils.getCheckMediaHighSpeedRecordMapSize();
        int a = 0;
        this.list.clear();
        for (Object obj : map1.keySet()) {
            Size[] size = (Size[]) map1.get(obj);
            for (int i = 0; i < size.length; i++) {
                a++;
                this.list.add(size[i].toString() + obj.toString());
            }
        }
        int size2 = this.list.size();
        for (int i2 = 0; i2 < size2 - 1; i2++) {
            for (int j = 0; j < (size2 - 1) - i2; j++) {
                String posStrJ = this.list.get(j).toString();
                int posIntJ = Integer.valueOf(posStrJ.substring(0, posStrJ.indexOf("x"))).intValue();
                String posStrJ1 = this.list.get(j + 1).toString();
                int posIntJ1 = Integer.valueOf(posStrJ1.substring(0, posStrJ1.indexOf("x"))).intValue();
                if (posIntJ <= posIntJ1) {
                    if (posIntJ < posIntJ1) {
                        this.list.set(j, posStrJ1);
                        this.list.set(j + 1, posStrJ);
                    } else {
                        if (Integer.valueOf(posStrJ.substring(posStrJ.indexOf("[") + 1, posStrJ.indexOf(","))).intValue() < Integer.valueOf(posStrJ1.substring(posStrJ1.indexOf("[") + 1, posStrJ1.indexOf(","))).intValue()) {
                            this.list.set(j, posStrJ1);
                            this.list.set(j + 1, posStrJ);
                        }
                    }
                }
            }
        }
        if (this.list.size() > 5) {
            this.list = this.list.subList(0, 5);
        }
        if (this.list.size() != 0) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) this.high_speed_record_recycler.getLayoutParams();
            lps.height = this.list.size() * Util.dip2px(this.mContext, 51.0f);
            this.high_speed_record_recycler.setLayoutParams(lps);
        }
        this.high_speed_record_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.adapterHighSpeed = new BaseRVAdapter(getActivity(), this.list) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.recycle_item_high_speed_record;
            }

            public void onBind(BaseViewHolder holder, final int position) {
                View high_speed_record_recycler_view = holder.getView(C0853R.C0855id.high_speed_record_recycler_view);
                RadioButton high_speed_record_recycler_ratio = (RadioButton) holder.getView(C0853R.C0855id.high_speed_record_recycler_ratio);
                if (position == FVAdvancedSetCameraFragment.this.stirPosition - FVAdvancedSetCameraFragment.this.photoVideoItemVisibilityNums) {
                    high_speed_record_recycler_ratio.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.black15));
                } else {
                    high_speed_record_recycler_ratio.setBackgroundColor(this.mContext.getResources().getColor(C0853R.color.transparent));
                }
                String highSpeedPos = FVAdvancedSetCameraFragment.this.list.get(position).toString();
                String siz = highSpeedPos.substring(0, highSpeedPos.indexOf("["));
                high_speed_record_recycler_ratio.setText(siz + "  " + highSpeedPos.substring(highSpeedPos.indexOf("[") + 1, highSpeedPos.indexOf(",")) + "fps");
                high_speed_record_recycler_view.setVisibility(0);
                if (((Integer) SPUtils.get(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    String unused = FVAdvancedSetCameraFragment.this.highSpeed = SPUtils.get(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                } else {
                    String unused2 = FVAdvancedSetCameraFragment.this.highSpeed = SPUtils.get(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
                }
                high_speed_record_recycler_ratio.setEnabled(true);
                if ("".equals(FVAdvancedSetCameraFragment.this.highSpeed)) {
                    high_speed_record_recycler_ratio.setEnabled(true);
                } else {
                    for (int i = 0; i < FVAdvancedSetCameraFragment.this.list.size(); i++) {
                        if (FVAdvancedSetCameraFragment.this.list.get(position).toString().equals(FVAdvancedSetCameraFragment.this.highSpeed)) {
                            int unused3 = FVAdvancedSetCameraFragment.this.checkHighSpeedPosition = position;
                            if (position == FVAdvancedSetCameraFragment.this.checkHighSpeedPosition) {
                                Log.e("---------------", "----------  66666  77777  88888  99999  position  position -------" + position);
                                high_speed_record_recycler_ratio.setEnabled(false);
                            } else {
                                high_speed_record_recycler_ratio.setEnabled(true);
                            }
                        }
                    }
                    Log.e("---------------", "----------  66666  77777  88888  99999  checkHighSpeedPosition  checkHighSpeedPosition -------" + FVAdvancedSetCameraFragment.this.checkHighSpeedPosition);
                }
                high_speed_record_recycler_ratio.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int unused = FVAdvancedSetCameraFragment.this.checkHighSpeedPosition = position;
                        Log.e("----------------", "-------  size1   size1   size1  size1   --------   66666666  " + FVAdvancedSetCameraFragment.this.list.get(position).toString());
                        if (((Integer) SPUtils.get(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                            SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, FVAdvancedSetCameraFragment.this.list.get(position).toString());
                        } else {
                            SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, FVAdvancedSetCameraFragment.this.list.get(position).toString());
                        }
                        FVAdvancedSetCameraFragment.this.adapterHighSpeed.notifyDataSetChanged();
                        FVAdvancedSetCameraFragment.this.rb_ratio1080.setEnabled(true);
                        FVAdvancedSetCameraFragment.this.rb_ratio720.setEnabled(true);
                        FVAdvancedSetCameraFragment.this.rb_ratio480.setEnabled(true);
                        FVAdvancedSetCameraFragment.this.rb_ratio2160.setEnabled(true);
                    }
                });
                if (!FVAdvancedSetCameraFragment.this.itemVisible.contains("high_speed_record_recycler_" + position)) {
                    FVAdvancedSetCameraFragment.this.itemVisible.add("high_speed_record_recycler_" + position);
                    Log.e("-----------------", "----------  6666  7777  8888  9999   ---------high_speed_record_recycler_" + position);
                }
            }
        };
        this.high_speed_record_recycler.setAdapter(this.adapterHighSpeed);
        this.high_speed_record_rg_ratio.clearCheck();
        if (this.adapterHighSpeed != null) {
            this.adapterHighSpeed.notifyDataSetChanged();
        }
        if (this.list.size() == 0) {
            high_speed_record_recycler_linear2.setVisibility(8);
        }
        if (this.list.size() != 0 && CameraUtils.getFvAdvancedSettingActivityIsShow()) {
            new Handler().postDelayed(this.runnableHighSpeed, 50);
        }
    }

    private void setCameraVideoOne() {
        int checkMediaSize;
        this.rgRatio.clearCheck();
        int[] recordSize = CameraUtils.getMediaRecordSize();
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            checkMediaSize = CameraUtils.getCheckMediaRecordFrontSize();
            if (recordSize.length > 0 && recordSize[0] < checkMediaSize) {
                checkMediaSize = recordSize[0];
                CameraUtils.setCheckMediaRecordFrontSize(checkMediaSize);
            }
        } else {
            checkMediaSize = CameraUtils.getCheckMediaRecordSize();
            if (recordSize.length > 0 && recordSize[0] < checkMediaSize) {
                checkMediaSize = recordSize[0];
                CameraUtils.setCheckMediaRecordSize(checkMediaSize);
            }
        }
        if (recordSize.length == 3) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            this.rb_ratio480.setText(CameraUtils.getRecordSizeText(recordSize[2]));
            this.rb_ratio480_linear.setVisibility(0);
            this.rb_ratio480.setVisibility(0);
            int checkNum = 0;
            for (int i = 0; i < recordSize.length; i++) {
                if (recordSize[i] == checkMediaSize) {
                    checkNum = i;
                }
            }
            setCheckEnableUI(checkNum);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.itemVisible.add("rb_ratio480");
            this.photoVideoItemVisibilityNums += 3;
        } else if (recordSize.length == 2) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            int checkNum2 = 0;
            for (int i2 = 0; i2 < recordSize.length; i2++) {
                if (recordSize[i2] == checkMediaSize) {
                    checkNum2 = i2;
                }
            }
            setCheckEnableUI(checkNum2);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.photoVideoItemVisibilityNums += 2;
        } else if (recordSize.length == 4) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            this.rb_ratio480.setText(CameraUtils.getRecordSizeText(recordSize[2]));
            this.rb_ratio2160.setText(CameraUtils.getRecordSizeText(recordSize[3]));
            this.rb_ratio480_linear.setVisibility(0);
            this.rb_ratio480.setVisibility(0);
            this.rb_ratio2160_linear.setVisibility(0);
            this.rb_ratio2160.setVisibility(0);
            int checkNum3 = 0;
            for (int i3 = 0; i3 < recordSize.length; i3++) {
                if (recordSize[i3] == checkMediaSize) {
                    checkNum3 = i3;
                }
            }
            setCheckEnableUI(checkNum3);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.itemVisible.add("rb_ratio480");
            this.itemVisible.add("rb_ratio2160");
            this.photoVideoItemVisibilityNums += 4;
        }
    }

    private void setCameraVideoTwo() {
        Size checkMediaSize;
        this.rgRatio.clearCheck();
        Size[] recordSize = CameraUtils.getMediaRecordSizeTwo();
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            checkMediaSize = CameraUtils.getCheckMediaRecordFrontSizeTwo();
            String str = (String) SPUtils.get(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, "");
            if (!"".equals(str)) {
                checkMediaSize = CameraUtils.getRecordStringToSize(str);
                CameraUtils.setCheckMediaRecordSizeTwo(checkMediaSize);
            }
        } else {
            checkMediaSize = CameraUtils.getCheckMediaRecordSizeTwo();
            String str2 = (String) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, "");
            if (!"".equals(str2)) {
                checkMediaSize = CameraUtils.getRecordStringToSize(str2);
                CameraUtils.setCheckMediaRecordSizeTwo(checkMediaSize);
            }
        }
        this.rb_ratio1080.setText("3840x2160");
        this.rb_ratio720.setText("1920x1080");
        this.rb_ratio480.setText("1280x720");
        this.rb_ratio2160.setText("720x480");
        if (recordSize.length == 1) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setVisibility(8);
            int checkNum = 0;
            for (int i = 0; i < recordSize.length; i++) {
                if (recordSize[i].toString().equals(checkMediaSize.toString())) {
                    checkNum = i;
                }
            }
            setCheckEnableUI(checkNum);
            this.itemVisible.add("rb_ratio1080");
            this.photoVideoItemVisibilityNums++;
        } else if (recordSize.length == 2) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            int checkNum2 = 0;
            for (int i2 = 0; i2 < recordSize.length; i2++) {
                if (recordSize[i2].toString().equals(checkMediaSize.toString())) {
                    checkNum2 = i2;
                }
            }
            setCheckEnableUI(checkNum2);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.photoVideoItemVisibilityNums += 2;
        } else if (recordSize.length == 3) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            this.rb_ratio480.setText(CameraUtils.getRecordSizeText(recordSize[2]));
            this.rb_ratio480_linear.setVisibility(0);
            this.rb_ratio480.setVisibility(0);
            int checkNum3 = 0;
            for (int i3 = 0; i3 < recordSize.length; i3++) {
                if (recordSize[i3].toString().equals(checkMediaSize.toString())) {
                    checkNum3 = i3;
                }
            }
            setCheckEnableUI(checkNum3);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.itemVisible.add("rb_ratio480");
            this.photoVideoItemVisibilityNums += 3;
        } else if (recordSize.length == 4) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            this.rb_ratio480.setText(CameraUtils.getRecordSizeText(recordSize[2]));
            this.rb_ratio2160.setText(CameraUtils.getRecordSizeText(recordSize[3]));
            this.rb_ratio480_linear.setVisibility(0);
            this.rb_ratio480.setVisibility(0);
            this.rb_ratio2160_linear.setVisibility(0);
            this.rb_ratio2160.setVisibility(0);
            int checkNum4 = 0;
            for (int i4 = 0; i4 < recordSize.length; i4++) {
                if (recordSize[i4].toString().equals(checkMediaSize.toString())) {
                    checkNum4 = i4;
                }
            }
            setCheckEnableUI(checkNum4);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.itemVisible.add("rb_ratio480");
            this.itemVisible.add("rb_ratio2160");
            this.photoVideoItemVisibilityNums += 4;
        } else if (recordSize.length > 4) {
            this.rb_ratio1080.setText(CameraUtils.getRecordSizeText(recordSize[0]));
            this.rb_ratio720.setText(CameraUtils.getRecordSizeText(recordSize[1]));
            this.rb_ratio480.setText(CameraUtils.getRecordSizeText(recordSize[2]));
            this.rb_ratio2160.setText(CameraUtils.getRecordSizeText(recordSize[3]));
            this.rb_ratio480_linear.setVisibility(0);
            this.rb_ratio480.setVisibility(0);
            this.rb_ratio2160_linear.setVisibility(0);
            this.rb_ratio2160.setVisibility(0);
            int checkNum5 = 0;
            for (int i5 = 0; i5 < recordSize.length; i5++) {
                if (recordSize[i5].toString().equals(checkMediaSize.toString())) {
                    checkNum5 = i5;
                }
            }
            setCheckEnableUI(checkNum5);
            this.itemVisible.add("rb_ratio1080");
            this.itemVisible.add("rb_ratio720");
            this.itemVisible.add("rb_ratio480");
            this.itemVisible.add("rb_ratio2160");
            this.photoVideoItemVisibilityNums += 4;
        }
    }

    private void initStatus() {
        this.rg_photo_ratio.clearCheck();
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            this.rb_photo_ratio2560.setText(CameraUtils.getMaxSupPictureSize() + "");
            this.rb_photo_ratio1920.setText(CameraUtils.getReComPictureSize() + "");
            if (CameraUtils.getMaxSupOrReComPictureFrontSize() == CameraUtils.RECOMMEND_LEVEL) {
                this.rb_photo_ratio2560.setEnabled(true);
                this.rb_photo_ratio1920.setEnabled(false);
            } else {
                this.rb_photo_ratio2560.setEnabled(false);
                this.rb_photo_ratio1920.setEnabled(true);
            }
            this.rb_photo_ratio2560_view.setVisibility(8);
            this.rb_photo_ratio2560.setVisibility(8);
            this.itemVisible.add("rb_photo_ratio1920");
            this.photoVideoItemVisibilityNums++;
            return;
        }
        this.rb_photo_ratio2560.setText(CameraUtils.getMaxSupPictureSize() + "");
        this.rb_photo_ratio1920.setText(CameraUtils.getReComPictureSize() + "");
        if (CameraUtils.getMaxSupOrReComPictureSize() == CameraUtils.RECOMMEND_LEVEL) {
            this.rb_photo_ratio2560.setEnabled(true);
            this.rb_photo_ratio1920.setEnabled(false);
        } else {
            this.rb_photo_ratio2560.setEnabled(false);
            this.rb_photo_ratio1920.setEnabled(true);
        }
        this.itemVisible.add("rb_photo_ratio2560");
        this.itemVisible.add("rb_photo_ratio1920");
        this.photoVideoItemVisibilityNums += 2;
    }

    public static int[] clearTenSiz(int[] arr) {
        int count = 0;
        for (int i : arr) {
            if (i == 16) {
                count++;
            }
        }
        int[] newArr = new int[(arr.length - count)];
        int index = 0;
        for (int i2 = 0; i2 < arr.length; i2++) {
            if (arr[i2] != 16) {
                newArr[index] = arr[i2];
                index++;
            }
        }
        return newArr;
    }

    private void setCheckEnableUI(int num) {
        switch (num) {
            case 0:
                this.rb_ratio1080.setEnabled(false);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                break;
            case 1:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(false);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                break;
            case 2:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(false);
                this.rb_ratio2160.setEnabled(true);
                break;
            case 3:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(false);
                break;
        }
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            this.highSpeed = SPUtils.get(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
        } else {
            this.highSpeed = SPUtils.get(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "").toString();
        }
        if (!"".equals(this.highSpeed) && CameraUtils.getUserCameraOneOrTwo() == 2) {
            this.rb_ratio1080.setEnabled(true);
            this.rb_ratio720.setEnabled(true);
            this.rb_ratio480.setEnabled(true);
            this.rb_ratio2160.setEnabled(true);
        }
    }

    private void setCheckEnableHighSpeedRecordUI(int num) {
        switch (num) {
            case 0:
                this.high_speed_record_rb_ratio120.setEnabled(false);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                return;
            case 1:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(false);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                return;
            case 2:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(false);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                return;
            case 3:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(false);
                return;
            default:
                return;
        }
    }

    private void initView() {
        this.rgRatio.setOnCheckedChangeListener(this);
        this.high_speed_record_rg_ratio.setOnCheckedChangeListener(this);
        this.rg_photo_ratio.setOnCheckedChangeListener(this);
        this.rlRecoverCameraSetting.setVisibility(8);
        this.rlRecoverCameraSetting.setOnClickListener(this);
        this.rl_set_camera_move_focus.setOnClickListener(this);
        this.rl_set_camera_lock_focus.setOnClickListener(this);
        this.rl_set_camera_move_lapse_high.setOnClickListener(this);
        this.rl_set_camera_move_lapse_low.setOnClickListener(this);
        this.rl_set_camera_panama_lock_no.setOnClickListener(this);
        this.rl_set_camera_panama_lock_yes.setOnClickListener(this);
        this.rl_set_microphone_sound_bouetooth.setOnClickListener(this);
        this.rl_set_microphone_sound_system.setOnClickListener(this);
        this.rl_set_camera_mode_pro.setOnClickListener(this);
        this.rl_set_camera_mode_trad.setOnClickListener(this);
        this.rl_set_zoom_velocity_medium.setOnClickListener(this);
        this.rl_set_zoom_velocity_slow.setOnClickListener(this);
        this.rl_set_mark_velocity_high.setOnClickListener(this);
        this.rl_set_mark_velocity_medium.setOnClickListener(this);
        this.rl_set_mark_velocity_slow.setOnClickListener(this);
        this.rl_storage_path_album.setOnClickListener(this);
        this.rl_storage_path_sd.setOnClickListener(this);
        this.rl_mark_model_setting_auto.setOnClickListener(this);
        this.rl_mark_model_setting_hand.setOnClickListener(this);
    }

    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        Size num2160;
        Size num480;
        Size num720;
        Size num1080;
        switch (checkedId) {
            case C0853R.C0855id.rb_photo_ratio2560:
                this.rb_photo_ratio2560.setEnabled(false);
                this.rb_photo_ratio1920.setEnabled(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.MAX_LEVEL));
                    CameraUtils.setMaxSupOrReComPictureFrontSize(CameraUtils.MAX_LEVEL);
                    return;
                }
                SPUtils.put(getActivity(), SharePrefConstant.PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.MAX_LEVEL));
                CameraUtils.setMaxSupOrReComPictureSize(CameraUtils.MAX_LEVEL);
                return;
            case C0853R.C0855id.rb_photo_ratio1920:
                this.rb_photo_ratio2560.setEnabled(true);
                this.rb_photo_ratio1920.setEnabled(false);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.RECOMMEND_LEVEL));
                    CameraUtils.setMaxSupOrReComPictureFrontSize(CameraUtils.RECOMMEND_LEVEL);
                    return;
                }
                SPUtils.put(getActivity(), SharePrefConstant.PHOTO_RESOLUTION, Integer.valueOf(CameraUtils.RECOMMEND_LEVEL));
                CameraUtils.setMaxSupOrReComPictureSize(CameraUtils.RECOMMEND_LEVEL);
                return;
            case C0853R.C0855id.rb_ratio1080:
                this.rb_ratio1080.setEnabled(false);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num10802 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio1080.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num10802);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num10802));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num10802);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num10802));
                    }
                } else {
                    Size[] recordSize = CameraUtils.getMediaRecordSizeTwo();
                    if (recordSize.length > 0) {
                        num1080 = recordSize[0];
                    } else {
                        num1080 = new Size(1920, 1080);
                    }
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num1080);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num1080.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num1080);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num1080.toString());
                        String str = (String) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, "");
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio720:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(false);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num7202 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio720.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num7202);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num7202));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num7202);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num7202));
                    }
                } else {
                    int lensMode720 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size[] recordSize2 = CameraUtils.getMediaRecordSizeTwo();
                    if (recordSize2.length > 1) {
                        num720 = recordSize2[1];
                    } else {
                        num720 = new Size(1920, 1080);
                    }
                    if (lensMode720 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num720);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num720.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num720);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num720.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio480:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(false);
                this.rb_ratio2160.setEnabled(true);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num4802 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio480.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num4802);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num4802));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num4802);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num4802));
                    }
                } else {
                    int lensMode480 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size[] recordSize3 = CameraUtils.getMediaRecordSizeTwo();
                    if (recordSize3.length > 2) {
                        num480 = recordSize3[2];
                    } else {
                        num480 = new Size(1920, 1080);
                    }
                    if (lensMode480 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num480);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num480.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num480);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num480.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.rb_ratio2160:
                this.rb_ratio1080.setEnabled(true);
                this.rb_ratio720.setEnabled(true);
                this.rb_ratio480.setEnabled(true);
                this.rb_ratio2160.setEnabled(false);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    int num21602 = CameraUtils.getRecordSizeLevel(Integer.valueOf(this.rb_ratio2160.getText().toString().replace("P", "")).intValue());
                    if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSize(num21602);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION, Integer.valueOf(num21602));
                    } else {
                        CameraUtils.setCheckMediaRecordSize(num21602);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION, Integer.valueOf(num21602));
                    }
                } else {
                    int lensMode2160 = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue();
                    Size[] recordSize4 = CameraUtils.getMediaRecordSizeTwo();
                    if (recordSize4.length > 3) {
                        num2160 = recordSize4[3];
                    } else {
                        num2160 = new Size(1920, 1080);
                    }
                    if (lensMode2160 == 10101) {
                        CameraUtils.setCheckMediaRecordFrontSizeTwo(num2160);
                        SPUtils.put(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, num2160.toString());
                    } else {
                        CameraUtils.setCheckMediaRecordSizeTwo(num2160);
                        SPUtils.put(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, num2160.toString());
                    }
                }
                setHighSpeedRestart();
                return;
            case C0853R.C0855id.high_speed_record_rb_ratio120:
                this.high_speed_record_rb_ratio120.setEnabled(false);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    CameraUtils.setCheckMediaHighSpeedRecordFrontSize(6);
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, 120);
                    return;
                }
                CameraUtils.setCheckMediaHighSpeedRecordSize(6);
                SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, 120);
                return;
            case C0853R.C0855id.high_speed_record_rb_ratio240:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(false);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    CameraUtils.setCheckMediaHighSpeedRecordFrontSize(6);
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, 240);
                    return;
                }
                CameraUtils.setCheckMediaHighSpeedRecordSize(6);
                SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, 240);
                return;
            case C0853R.C0855id.high_speed_record_rb_ratio480:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(false);
                this.high_speed_record_rb_ratio960.setEnabled(true);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    CameraUtils.setCheckMediaHighSpeedRecordFrontSize(6);
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, 480);
                    return;
                }
                CameraUtils.setCheckMediaHighSpeedRecordSize(6);
                SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, 480);
                return;
            case C0853R.C0855id.high_speed_record_rb_ratio960:
                this.high_speed_record_rb_ratio120.setEnabled(true);
                this.high_speed_record_rb_ratio240.setEnabled(true);
                this.high_speed_record_rb_ratio480.setEnabled(true);
                this.high_speed_record_rb_ratio960.setEnabled(false);
                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                    CameraUtils.setCheckMediaHighSpeedRecordFrontSize(6);
                    SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, 960);
                    return;
                }
                CameraUtils.setCheckMediaHighSpeedRecordSize(6);
                SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, 960);
                return;
            default:
                return;
        }
    }

    private void setHighSpeedRestart() {
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            SPUtils.put(getActivity(), SharePrefConstant.FRONT_HIGH_SPEED_VIDEO_RESOLUTION, "");
        } else {
            SPUtils.put(getActivity(), SharePrefConstant.HIGH_SPEED_VIDEO_RESOLUTION, "");
        }
        if (this.adapterHighSpeed != null) {
            this.adapterHighSpeed.notifyDataSetChanged();
        }
    }

    public void toastMicrophoneBluetooth() {
        if (this.toastMicrophoneBluetoothDialog == null || !this.toastMicrophoneBluetoothDialog.isShowing()) {
            this.toastMicrophoneBluetoothDialog = new ToastMicrophoneBluetoothDialog(getContext());
            Window window = this.toastMicrophoneBluetoothDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = -1;
            lp.height = -2;
            window.setAttributes(lp);
            this.toastMicrophoneBluetoothDialog.setDialogCancleable(false);
            this.toastMicrophoneBluetoothDialog.setDialogOutsideCancleable(false);
            this.toastMicrophoneBluetoothDialog.setYesOnclickListener(new ToastMicrophoneBluetoothDialog.onYesOnclickListener() {
                public void onYesClick() {
                    FVAdvancedSetCameraFragment.this.toastMicrophoneBluetoothDialog.dismiss();
                    FVAdvancedSetCameraFragment.this.iv_set_microphone_sound_bouetooth_select.setVisibility(8);
                    FVAdvancedSetCameraFragment.this.iv_set_microphone_sound_system_select.setVisibility(0);
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_SYSYTEM));
                }
            });
            this.toastMicrophoneBluetoothDialog.show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.rl_set_camera_move_lapse_high:
                this.iv_set_camera_move_lapse_high_select.setVisibility(0);
                this.iv_set_camera_move_lapse_low_select.setVisibility(8);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH));
                    return;
                } else {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH));
                    return;
                }
            case C0853R.C0855id.rl_set_camera_move_lapse_low:
                this.iv_set_camera_move_lapse_high_select.setVisibility(8);
                this.iv_set_camera_move_lapse_low_select.setVisibility(0);
                if (CameraUtils.getUserCameraOneOrTwo() == 1) {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_LOW));
                    return;
                } else {
                    SPUtils.put(getActivity(), SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_LOW));
                    return;
                }
            case C0853R.C0855id.rl_set_camera_mode_pro:
                if (this.iv_set_camera_mode_pro_select.getVisibility() == 8) {
                    dialogCameraModeProSelect();
                    return;
                }
                return;
            case C0853R.C0855id.rl_set_camera_mode_trad:
                if (this.iv_set_camera_mode_trad_select.getVisibility() == 8) {
                    dialogCameraModeTradSelect();
                    return;
                }
                return;
            case C0853R.C0855id.rl_set_camera_move_focus:
                this.iv_set_camera_move_focus_select.setVisibility(0);
                this.iv_set_camera_lock_focus_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE));
                return;
            case C0853R.C0855id.rl_set_camera_lock_focus:
                this.iv_set_camera_move_focus_select.setVisibility(8);
                this.iv_set_camera_lock_focus_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_LOCK));
                return;
            case C0853R.C0855id.rl_set_camera_panama_lock_yes:
                this.iv_set_camera_panama_lock_no_select.setVisibility(8);
                this.iv_set_camera_panama_lock_yes_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_YES));
                return;
            case C0853R.C0855id.rl_set_camera_panama_lock_no:
                this.iv_set_camera_panama_lock_no_select.setVisibility(0);
                this.iv_set_camera_panama_lock_yes_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.PANAMA_PHOTO_FOCUS_LOCK_YES_OR_NO, Integer.valueOf(Constants.PANAMA_PHOTO_FOCUS_LOCK_NO));
                return;
            case C0853R.C0855id.rl_set_microphone_sound_system:
                this.iv_set_microphone_sound_bouetooth_select.setVisibility(8);
                this.iv_set_microphone_sound_system_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_SYSYTEM));
                return;
            case C0853R.C0855id.rl_set_microphone_sound_bouetooth:
                if (BluetoothHeadsetUtil.isHeadSetCanUse()) {
                    this.iv_set_microphone_sound_bouetooth_select.setVisibility(0);
                    this.iv_set_microphone_sound_system_select.setVisibility(8);
                    SPUtils.put(getActivity(), SharePrefConstant.MICROPHONE_SOUND_SYSYTEM_OR_BOUETOOTH, Integer.valueOf(Constants.MICROPHONE_SOUND_BOUETOOTH));
                    return;
                }
                toastMicrophoneBluetooth();
                return;
            case C0853R.C0855id.rl_set_zoom_velocity_medium:
                this.iv_set_zoom_velocity_medium_select.setVisibility(0);
                this.iv_set_zoom_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_MEDIUM));
                return;
            case C0853R.C0855id.rl_set_zoom_velocity_slow:
                this.iv_set_zoom_velocity_medium_select.setVisibility(8);
                this.iv_set_zoom_velocity_slow_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.ZOOM_VELOCITY_MEDIUM_OR_SLOW, Integer.valueOf(Constants.ZOOM_VELOCITY_SLOW));
                return;
            case C0853R.C0855id.rl_storage_path_album:
                this.iv_storage_path_album.setVisibility(0);
                this.iv_storage_path_sd.setVisibility(8);
                this.storage_sd_card_path.setVisibility(8);
                SPUtils.put(getActivity(), "save_storage_path", Integer.valueOf(Constants.SAVE_STORAGE_PATH_ALBUM));
                SPUtils.put(getActivity(), SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile");
                this.storage_sd_card_path.setText("");
                return;
            case C0853R.C0855id.rl_storage_path_sd:
                if (Util.getSDCardPath() == null) {
                    this.storageChangeDialog.show();
                    this.storageChangeDialog.setMessage(getString(C0853R.string.storage_change_no_sd_card_tip));
                    this.storage_sd_card_path.setText("");
                    return;
                }
                this.iv_storage_path_album.setVisibility(8);
                this.iv_storage_path_sd.setVisibility(0);
                this.storage_sd_card_path.setVisibility(0);
                this.storageChangeDialog.show();
                this.storageChangeDialog.setMessage(getString(C0853R.string.storage_change_has_sd_card_tip));
                SPUtils.put(getActivity(), "save_storage_path", Integer.valueOf(Constants.SAVE_STORAGE_PATH_SD));
                saveToSdCard();
                return;
            case C0853R.C0855id.rl_mark_model_setting_auto:
                this.iv_mark_model_setting_auto.setVisibility(0);
                this.iv_mark_model_setting_hand.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO));
                return;
            case C0853R.C0855id.rl_mark_model_setting_hand:
                this.iv_mark_model_setting_auto.setVisibility(8);
                this.iv_mark_model_setting_hand.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_YES));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_high:
                this.iv_set_mark_velocity_high_select.setVisibility(0);
                this.iv_set_mark_velocity_medium_select.setVisibility(8);
                this.iv_set_mark_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_HIGH));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_medium:
                this.iv_set_mark_velocity_high_select.setVisibility(8);
                this.iv_set_mark_velocity_medium_select.setVisibility(0);
                this.iv_set_mark_velocity_slow_select.setVisibility(8);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_MEDIUM));
                return;
            case C0853R.C0855id.rl_set_mark_velocity_slow:
                this.iv_set_mark_velocity_high_select.setVisibility(8);
                this.iv_set_mark_velocity_medium_select.setVisibility(8);
                this.iv_set_mark_velocity_slow_select.setVisibility(0);
                SPUtils.put(getActivity(), SharePrefConstant.CAMERA_VILTA_X_MARK_VELOCITY, Integer.valueOf(Constants.MARK_VELOCITY_SLOW));
                return;
            default:
                return;
        }
    }

    private void saveToSdCard() {
        String parentPath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            for (File file : this.mContext.getExternalFilesDirs("mounted")) {
                if (file.getPath().contains(com.google.android.vending.expansion.downloader.Constants.FILENAME_SEQUENCE_SEPARATOR)) {
                    parentPath = file.getPath();
                }
            }
        }
        if (parentPath != null) {
            File file2 = new File(parentPath, "fvmobile");
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (file2.exists()) {
                SPUtils.put(getActivity(), SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, file2.getPath() + "/");
                this.storage_sd_card_path.setText(getString(C0853R.string.storage_change_current_path) + ":" + file2.getPath());
                return;
            }
            return;
        }
        SPUtils.put(getActivity(), SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/fvmobile/");
    }

    private void dialogCameraModeProSelect() {
        if (this.cameraCompaModeChangeDialog != null) {
            this.cameraCompaModeChangeDialog.dismiss();
        }
        if (this.cameraCompaModeChangeDialog == null || !this.cameraCompaModeChangeDialog.isShowing()) {
            this.cameraCompaModeChangeDialog = new FVCameraCompaModeChangeDialog(getContext());
            Window window = this.cameraCompaModeChangeDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = -1;
            lp.height = -2;
            window.setAttributes(lp);
            this.cameraCompaModeChangeDialog.setDialogCancleable(false);
            this.cameraCompaModeChangeDialog.setDialogOutsideCancleable(false);
            this.cameraCompaModeChangeDialog.setButtonOnClick(new FVCameraCompaModeChangeDialog.CheckButtonOnclick() {
                public void onClick(View view) {
                    FVAdvancedSetCameraFragment.this.cameraCompaModeChangeDialog.dismiss();
                    Util.hideBottomUIMenu((Activity) FVAdvancedSetCameraFragment.this.mContext);
                }
            });
            this.cameraCompaModeChangeDialog.setButtonSureOnClick(new FVCameraCompaModeChangeDialog.CheckButtonSureOnclick() {
                public void onClick(View view) {
                    FVAdvancedSetCameraFragment.this.cameraCompaModeChangeDialog.dismiss();
                    MoveTimelapseUtil.setCameraSelectOneOrTwo(2);
                    FVAdvancedSetCameraFragment.this.iv_set_camera_mode_pro_select.setVisibility(0);
                    FVAdvancedSetCameraFragment.this.iv_set_camera_mode_trad_select.setVisibility(8);
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD, Integer.valueOf(Constants.CAMERA_MODE_PRO));
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD_SWITCH, Integer.valueOf(Constants.CAMERA_MODE_PRO_TRAD_OPEN));
                    BaseActivityManager.getActivityManager().popActivityOne(FVMainActivity.class);
                    BaseActivityManager.getActivityManager().popActivityOne(FVAdvancedSettingAvtivity.class);
                    FVAdvancedSetCameraFragment.this.getActivity().finish();
                }
            });
            this.cameraCompaModeChangeDialog.show();
            return;
        }
        this.cameraCompaModeChangeDialog.dismiss();
    }

    private void dialogCameraModeTradSelect() {
        if (this.cameraCompaModeChangeDialog != null) {
            this.cameraCompaModeChangeDialog.dismiss();
        }
        if (this.cameraCompaModeChangeDialog == null || !this.cameraCompaModeChangeDialog.isShowing()) {
            this.cameraCompaModeChangeDialog = new FVCameraCompaModeChangeDialog(getContext());
            Window window = this.cameraCompaModeChangeDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = -1;
            lp.height = -2;
            window.setAttributes(lp);
            this.cameraCompaModeChangeDialog.setDialogCancleable(false);
            this.cameraCompaModeChangeDialog.setDialogOutsideCancleable(false);
            this.cameraCompaModeChangeDialog.setButtonOnClick(new FVCameraCompaModeChangeDialog.CheckButtonOnclick() {
                public void onClick(View view) {
                    FVAdvancedSetCameraFragment.this.cameraCompaModeChangeDialog.dismiss();
                    Util.hideBottomUIMenu((Activity) FVAdvancedSetCameraFragment.this.mContext);
                }
            });
            this.cameraCompaModeChangeDialog.setButtonSureOnClick(new FVCameraCompaModeChangeDialog.CheckButtonSureOnclick() {
                public void onClick(View view) {
                    FVAdvancedSetCameraFragment.this.cameraCompaModeChangeDialog.dismiss();
                    MoveTimelapseUtil.setCameraSelectOneOrTwo(1);
                    FVAdvancedSetCameraFragment.this.iv_set_camera_mode_pro_select.setVisibility(8);
                    FVAdvancedSetCameraFragment.this.iv_set_camera_mode_trad_select.setVisibility(0);
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD, Integer.valueOf(Constants.CAMERA_MODE_TRAD));
                    SPUtils.put(FVAdvancedSetCameraFragment.this.getActivity(), SharePrefConstant.CAMERA_MODE_PRO_OR_TRAD_SWITCH, Integer.valueOf(Constants.CAMERA_MODE_PRO_TRAD_OPEN));
                    BaseActivityManager.getActivityManager().popActivityOne(FVMainActivity.class);
                    BaseActivityManager.getActivityManager().popActivityOne(FVAdvancedSettingAvtivity.class);
                    FVAdvancedSetCameraFragment.this.getActivity().finish();
                }
            });
            this.cameraCompaModeChangeDialog.show();
            return;
        }
        this.cameraCompaModeChangeDialog.dismiss();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            FVAdvancedSetCameraFragment.this.rb_photo_ratio2560.setText(CameraUtils.getMaxSupPictureSize() + "");
            FVAdvancedSetCameraFragment.this.rb_photo_ratio1920.setText(CameraUtils.getReComPictureSize() + "");
        }
    }
}
