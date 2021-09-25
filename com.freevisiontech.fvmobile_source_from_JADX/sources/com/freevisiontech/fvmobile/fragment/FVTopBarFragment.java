package com.freevisiontech.fvmobile.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.p001v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVAdvancedSettingAvtivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.LogUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.CustomToast;
import com.freevisiontech.fvmobile.widget.FVCameraShortcutPop;
import com.freevisiontech.fvmobile.widget.FVCameraVideoShortcutPop;
import com.freevisiontech.fvmobile.widget.FVPTZSettingPop;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.umeng.analytics.pro.C0217dk;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVTopBarFragment extends Fragment implements View.OnClickListener {
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    @Bind({2131755796})
    ImageView btnBack;
    @Bind({2131755216})
    ImageView btnCamera;
    @Bind({2131755798})
    ImageView btnCameraStatus;
    @Bind({2131755282})
    ImageView btnSetting;
    @Bind({2131755800})
    ImageView btnVilta;
    @Bind({2131755801})
    ImageView btnViltaStatus;
    @Bind({2131755805})
    LinearLayout btn_bg_color_yellow;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    private boolean clickable = true;
    /* access modifiers changed from: private */
    public boolean connected = false;
    private boolean fullShotInvisible = false;
    private Handler handler = new Handler();
    @Bind({2131755812})
    ImageView icon_phone_battery;
    @Bind({2131755816})
    ImageView icon_ptz_battery;
    @Bind({2131755795})
    RelativeLayout llRoot;
    private Intent mBatteryStatusIntent;
    /* access modifiers changed from: private */
    public String mChargingType = "";
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public CustomToast mCustomToast;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ViseLog.m1466e("FVTopBarFragment rotate: " + String.valueOf(msg.what));
            switch (msg.what) {
                case 0:
                    if (FVTopBarFragment.this.mStartAngle == -270) {
                        FVTopBarFragment.this.rotateView(-360, FVTopBarFragment.this.btnBack, FVTopBarFragment.this.btnCamera, FVTopBarFragment.this.btnVilta, FVTopBarFragment.this.btnSetting);
                        return;
                    }
                    FVTopBarFragment.this.rotateView(0, FVTopBarFragment.this.btnBack, FVTopBarFragment.this.btnCamera, FVTopBarFragment.this.btnVilta, FVTopBarFragment.this.btnSetting);
                    return;
                case 90:
                    if (FVTopBarFragment.this.mStartAngle == -360) {
                        int unused = FVTopBarFragment.this.mStartAngle = 0;
                    }
                    FVTopBarFragment.this.rotateView(-90, FVTopBarFragment.this.btnBack, FVTopBarFragment.this.btnCamera, FVTopBarFragment.this.btnVilta, FVTopBarFragment.this.btnSetting);
                    return;
                case 180:
                    FVTopBarFragment.this.rotateView(-180, FVTopBarFragment.this.btnBack, FVTopBarFragment.this.btnCamera, FVTopBarFragment.this.btnVilta, FVTopBarFragment.this.btnSetting);
                    return;
                case 270:
                    if (FVTopBarFragment.this.mStartAngle == 0) {
                        int unused2 = FVTopBarFragment.this.mStartAngle = -360;
                    }
                    FVTopBarFragment.this.rotateView(-270, FVTopBarFragment.this.btnBack, FVTopBarFragment.this.btnCamera, FVTopBarFragment.this.btnVilta, FVTopBarFragment.this.btnSetting);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsCharging = false;
    /* access modifiers changed from: private */
    public boolean mIsFM210ButtonsEnable = true;
    private MyCountDownTimer mMyCountDownTimer;
    /* access modifiers changed from: private */
    public int mStartAngle = 0;
    private PopupWindow popupWindow;
    private BatteryReceiver receiver;
    @Bind({2131755808})
    RelativeLayout rlBack;
    @Bind({2131755797})
    RelativeLayout rlCamera;
    @Bind({2131755806})
    RelativeLayout rlSetting;
    @Bind({2131755799})
    RelativeLayout rlVilta;
    @Bind({2131755802})
    RelativeLayout rl_battery;
    @Bind({2131755814})
    RelativeLayout rl_ptz_battery;
    private Runnable runnable = new Runnable() {
        public void run() {
            FVTopBarFragment.this.getPtzBatteryInfo();
        }
    };
    private Boolean startVideoThread = true;
    @Bind({2131755813})
    TextView tv_phone_battery_remain;
    @Bind({2131755817})
    TextView tv_ptz_battery_remain;
    private View view;

    private class BatteryReceiver extends BroadcastReceiver {
        private BatteryReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");
            int percent = (current * 100) / intent.getExtras().getInt("scale");
            if (intent.getAction() == "android.intent.action.BATTERY_CHANGED") {
                boolean unused = FVTopBarFragment.this.mIsCharging = true;
                if (intent.getIntExtra("status", 1) == 2) {
                    FVTopBarFragment.this.icon_phone_battery.setImageResource(C0853R.mipmap.ic_charging);
                    int type = intent.getIntExtra("plugged", -1);
                    String unused2 = FVTopBarFragment.this.mChargingType = "";
                    switch (type) {
                        case 1:
                            String unused3 = FVTopBarFragment.this.mChargingType = "acCharging";
                            break;
                        case 2:
                            String unused4 = FVTopBarFragment.this.mChargingType = "usbCharging";
                            break;
                        case 4:
                            String unused5 = FVTopBarFragment.this.mChargingType = "wirelessCharging";
                            break;
                    }
                    FVTopBarFragment.this.tv_phone_battery_remain.setText(percent + "%");
                } else {
                    boolean unused6 = FVTopBarFragment.this.mIsCharging = false;
                    if (percent > 75) {
                        FVTopBarFragment.this.icon_phone_battery.setImageResource(C0853R.mipmap.ic_battery_full);
                    } else if (percent > 50) {
                        FVTopBarFragment.this.icon_phone_battery.setImageResource(C0853R.mipmap.ic_battery_seventy);
                    } else if (percent > 30) {
                        FVTopBarFragment.this.icon_phone_battery.setImageResource(C0853R.mipmap.ic_battery_half);
                    } else {
                        FVTopBarFragment.this.icon_phone_battery.setImageResource(C0853R.mipmap.ic_battery_low);
                    }
                    FVTopBarFragment.this.tv_phone_battery_remain.setText(percent + "%");
                }
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    FVTopBarFragment.this.checkChargingStatus();
                } else if (CameraUtils.getCurrentPageIndex() == 1 && FVTopBarFragment.this.isPtzCanChargePhone()) {
                    FVTopBarFragment.this.checkChargingStatus();
                }
            }
        }
    }

    private boolean isCanChargeByWireless() {
        if (!isPtzCanChargePhone()) {
            return false;
        }
        ArrayList<String> tempGetList = (ArrayList) SPUtils.getDataList(getActivity(), SharePrefConstant.PTZ_FM300_CAN_CHARGE_BY_WIRELESS_LIST);
        String brand = Build.BRAND;
        String model = Build.MODEL;
        if (brand == null || model == null || tempGetList == null) {
            return false;
        }
        boolean canCharge = false;
        Iterator<String> it = tempGetList.iterator();
        while (it.hasNext()) {
            String[] canChargeModelInfo = it.next().split(",");
            if (brand.equals(canChargeModelInfo[0]) && model.equals(canChargeModelInfo[1])) {
                canCharge = true;
            }
        }
        if (canCharge) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean isPtzCanChargePhone() {
        String sn = BlePtzParasConstant.GET_PTZ_SN_CODE;
        if (sn == null) {
            return false;
        }
        if (sn.substring(3, 4).equals("C") || sn.substring(3, 4).equals("D")) {
            return true;
        }
        return false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(C0853R.layout.fragment_top_bar_two, container, false);
        ButterKnife.bind((Object) this, this.view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.mContext = getActivity();
        btnStatusRestart();
        initListener();
        initData();
        return this.view;
    }

    private void btnStatusRestart() {
        topBarBtnStatusRestartBlack();
        CameraUtils.setLabelTopBarSelect(-1);
        CameraUtils.setBtnCameraStatus(0);
        CameraUtils.setBtnViltaStatus(0);
    }

    private void getFVCameraManager() {
        FVContentFragment contentFragment = (FVContentFragment) getActivity().getSupportFragmentManager().findFragmentByTag("contentFragment");
        if (contentFragment != null) {
            this.cameraManager = contentFragment.getCameraManager();
        }
    }

    public void onResume() {
        super.onResume();
        ScreenOrientationUtil.getInstance().start(getActivity(), new ScreenOrientationUtil.ScreenOrientationListener() {
            public void onScreenOrientationChanged(int newOrientation) {
                LogUtils.m1525v("topFragment", newOrientation + "...............topbar");
                FVTopBarFragment.this.getAngle();
            }
        });
        getAngle();
        IntentFilter filter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        this.receiver = new BatteryReceiver();
        this.mBatteryStatusIntent = getActivity().registerReceiver(this.receiver, filter);
        this.mIsFM210ButtonsEnable = true;
    }

    /* access modifiers changed from: private */
    public void checkChargingStatus() {
        if (!BlePtzParasConstant.hasToastChargingTip && BlePtzParasConstant.SET_PHONE_BATTERY_CHARGING_SWITCH == 1) {
            if (!Util.isSupportWirelessCharge(this.mContext)) {
                this.mCustomToast.customToast(getString(C0853R.string.wireless_not_support_toast_tip), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
            } else {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        if (!FVTopBarFragment.this.mIsCharging && FVTopBarFragment.this.mIsFM210ButtonsEnable) {
                            FVTopBarFragment.this.mCustomToast.customToast(FVTopBarFragment.this.getString(C0853R.string.wireless_fail_toast_tip), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        }
                    }
                }, 3000);
            }
            BlePtzParasConstant.hasToastChargingTip = true;
        }
    }

    public void onPause() {
        super.onPause();
        ScreenOrientationUtil.getInstance().stop();
        getActivity().unregisterReceiver(this.receiver);
        this.mIsFM210ButtonsEnable = false;
    }

    /* access modifiers changed from: private */
    public void getAngle() {
        int angle = ScreenOrientationUtil.getInstance().getOrientation();
        if (angle == 0) {
            this.mHandler.sendEmptyMessage(0);
        } else if (angle == 90) {
            this.mHandler.sendEmptyMessage(90);
        } else if (angle == 180) {
            this.mHandler.sendEmptyMessage(180);
        } else if (angle == 270) {
            this.mHandler.sendEmptyMessage(270);
        }
    }

    /* access modifiers changed from: private */
    public void rotateView(final int angle, View... views) {
        rotateBattery(angle);
        int length = views.length;
        int i = 0;
        while (i < length) {
            View view2 = views[i];
            if (angle != this.mStartAngle) {
                LogUtils.m1525v("animator", this.mStartAngle + "......" + angle);
                ObjectAnimator animator = ObjectAnimator.ofFloat(view2, "rotation", new float[]{(float) this.mStartAngle, (float) angle});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVTopBarFragment.this.mStartAngle = angle;
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    private void rotateBattery(int angle) {
        ViseLog.m1466e("FVTopBarFragment rotate angle: " + angle);
        if (angle != this.mStartAngle) {
            int dip2px = Util.dip2px(getActivity(), 30.0f);
            LogUtils.m1525v("animator", this.mStartAngle + "......" + angle);
            ObjectAnimator animatorRotate = ObjectAnimator.ofFloat(this.rl_battery, "rotation", new float[]{(float) this.mStartAngle, (float) angle});
            ObjectAnimator phoneAnimatorTranslate = ObjectAnimator.ofFloat(this.rl_battery, "translationY", new float[]{0.0f, (float) Math.abs(this.rl_battery.getHeight() - this.rl_battery.getTop())});
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animatorRotate).with(phoneAnimatorTranslate);
            animatorSet.setDuration(300);
            animatorSet.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        switch (event.getCode()) {
            case 17:
                this.rl_ptz_battery.setVisibility(0);
                getPtzBatteryInfo();
                if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                        }
                        CameraUtils.setBtnViltaStatus(1);
                        return;
                    }
                    return;
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                        }
                        CameraUtils.setBtnViltaStatus(2);
                        return;
                    }
                    return;
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                        }
                        CameraUtils.setBtnViltaStatus(0);
                        return;
                    }
                    return;
                } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                        }
                        CameraUtils.setBtnViltaStatus(3);
                        return;
                    }
                    return;
                } else {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                        }
                        CameraUtils.setBtnViltaStatus(0);
                        return;
                    }
                    return;
                }
            case 34:
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                        this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(0);
                }
                BlePtzParasConstant.SET_PTZ_FOLLOW_MODE = -1;
                this.rl_ptz_battery.setVisibility(4);
                if (this.handler != null) {
                    this.handler.removeCallbacks(this.runnable);
                    return;
                }
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[1] & 255) != 31) {
                    ViseLog.m1466e("upgrade receive data :byte[]:" + Arrays.toString(value) + " ,hex: " + HexUtil.encodeHexStr(value));
                }
                if ((value[0] & 255) == 90) {
                    if ((value[1] & 255) == 5) {
                        if ((value[2] & 255) == 0) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                                }
                                CameraUtils.setBtnViltaStatus(1);
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 1) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                                }
                                CameraUtils.setBtnViltaStatus(2);
                                return;
                            }
                            return;
                        } else if ((value[2] & 255) == 2) {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                                }
                                CameraUtils.setBtnViltaStatus(0);
                                return;
                            }
                            return;
                        } else {
                            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                            this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                            if (CameraUtils.getCurrentPageIndex() == 2) {
                                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                                }
                                CameraUtils.setBtnViltaStatus(3);
                            }
                            if (((Boolean) SPUtils.get(getActivity(), SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
                                Util.sendIntEventMessge(Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP);
                                Util.isPovReverTimeLapse(getActivity());
                            }
                            if (CameraUtils.isFullShotIng()) {
                                if (this.connected) {
                                    BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                                }
                                CameraUtils.setFullCameraErrorCode(BleConstant.f1095WB);
                                Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_STOP_FULL_SHOT);
                                Util.isPovReverPano(getActivity());
                            }
                            MoveTimelapseUtil.getInstance();
                            if (MoveTimelapseUtil.getCameraVideoSymbolStart() == 1) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 2 || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 3) {
                                            if (ViseBluetooth.getInstance().isConnected()) {
                                                BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                                            }
                                            Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_CLOSE_VIDEO);
                                            Util.isPovReverTimeLapse(FVTopBarFragment.this.getActivity());
                                        }
                                    }
                                }, 200);
                            }
                            MoveTimelapseUtil.getInstance();
                            if (MoveTimelapseUtil.getCameraTrackingStart() == 1) {
                                if (this.connected) {
                                    BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                                }
                                if (CameraUtils.isFollowIng()) {
                                    Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
                                    Util.isPovTracking(getActivity());
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                    } else if ((value[1] & 255) == 24) {
                        BleByteUtil.ackPTZPanorama((byte) 24, (byte) (value[2] & 255));
                        if ((value[2] & 255) == 2) {
                            this.mCustomToast.customToast(getString(C0853R.string.wireless_charging_disconnect_tip), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            BleByteUtil.ackPTZPanorama((byte) 24, (byte) 2);
                            return;
                        } else if ((value[2] & 255) == 1 && this.mIsFM210ButtonsEnable) {
                            if (this.mCustomToast != null) {
                                this.mCustomToast.hide();
                            }
                            if (!Util.isSupportWirelessCharge(this.mContext)) {
                                this.mCustomToast.customToast(getString(C0853R.string.wireless_not_support_toast_tip), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                                return;
                            } else {
                                this.mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        if (!FVTopBarFragment.this.mIsCharging) {
                                            FVTopBarFragment.this.mCustomToast.customToast(FVTopBarFragment.this.getString(C0853R.string.wireless_fail_toast_tip), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                                        }
                                    }
                                }, 5000);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else if ((value[1] & 255) == 70 || (value[1] & 255) == 72 || (value[1] & 255) == 86) {
                        if (this.mIsFM210ButtonsEnable) {
                            processDataForX(value);
                            return;
                        }
                        return;
                    } else if ((value[1] & 255) == 80) {
                        BleByteUtil.ackPTZPanorama((byte) 80, (byte) 1);
                        Util.sendIntEventMessge(Constants.CAMERA_FLASH_MODE_CHANGE);
                        ViseLog.m1466e("ViltaX--CAMERA_FLASH_MODE_CHANGE----闪光灯更改");
                        return;
                    } else if ((value[1] & 255) == 81 && this.mIsFM210ButtonsEnable) {
                        BleByteUtil.ackPTZPanorama((byte) 81, (byte) 1);
                        Util.sendIntEventMessge(Constants.CAMERA_WB_MODE_CHANGE);
                        ViseLog.m1466e("ViltaX--CAMERA_FLASH_MODE_CHANGE----白平衡更改");
                        return;
                    } else {
                        return;
                    }
                } else if ((value[0] & 255) != 165 || (value[1] & 255) != 5) {
                    return;
                } else {
                    if ((value[2] & 255) == 0) {
                        this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                        this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                                this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                            }
                            CameraUtils.setBtnViltaStatus(1);
                            return;
                        }
                        return;
                    } else if ((value[2] & 255) == 1) {
                        this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                        this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                                this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                            }
                            CameraUtils.setBtnViltaStatus(2);
                            return;
                        }
                        return;
                    } else if ((value[2] & 255) == 2) {
                        this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                        this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                                this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                            }
                            CameraUtils.setBtnViltaStatus(0);
                            return;
                        }
                        return;
                    } else {
                        this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                        this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                                this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                            }
                            CameraUtils.setBtnViltaStatus(3);
                        }
                        if (((Boolean) SPUtils.get(getActivity(), SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
                            Util.sendIntEventMessge(Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP);
                            Util.isPovReverTimeLapse(getActivity());
                        }
                        if (CameraUtils.isFullShotIng()) {
                            if (this.connected) {
                                BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                            }
                            CameraUtils.setFullCameraErrorCode(BleConstant.f1095WB);
                            Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_STOP_FULL_SHOT);
                            Util.isPovReverPano(getActivity());
                        }
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraVideoSymbolStart() == 1) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 2 || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 3) {
                                        if (ViseBluetooth.getInstance().isConnected()) {
                                            BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                                        }
                                        Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_CLOSE_VIDEO);
                                        Util.isPovReverTimeLapse(FVTopBarFragment.this.getActivity());
                                    }
                                }
                            }, 200);
                        }
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraTrackingStart() == 1) {
                            if (this.connected) {
                                BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                            }
                            if (CameraUtils.isFollowIng()) {
                                Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
                                Util.isPovTracking(getActivity());
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void setMenuAndReturnKey() {
        if (CameraUtils.getCurrentPageIndex() != 2) {
            return;
        }
        if (this.popupWindow == null || (!this.popupWindow.isShowing() && CameraUtils.getLlRootBackgroundColor() && CameraUtils.getFrameLayerNumber() == 0)) {
            if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1) {
                }
            } else if (!CameraUtils.isFollowIng() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
                if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211 && getTopBarBgColorStatusIsYellow() == 0 && CameraUtils.getLabelTopBarSelect() == -1) {
                    int selectValue = CameraUtils.getLabelTopBarSelect();
                    if (selectValue == -1) {
                        selectValue = 0;
                    }
                    topBarBtnStatusOneIsYellow(selectValue);
                } else {
                    int selectValue2 = CameraUtils.getLabelTopBarSelect();
                    if (selectValue2 == -1) {
                        selectValue2 = 0;
                    }
                    topBarBtnStatusOneIsYellow(selectValue2);
                    final int status = getTopBarBgColorStatusIsYellow();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVTopBarFragment.this.topBarImageStatusAnimation(status);
                        }
                    }, 10);
                }
                if (CameraUtils.getLabelTopBarSelect() == 1) {
                    CameraUtils.setLabelTopBarSelectMemory(4);
                    CameraUtils.setLabelTopBarSelect(4);
                } else if (CameraUtils.getLabelTopBarSelect() == 2) {
                    CameraUtils.setLabelTopBarSelectMemory(1);
                    CameraUtils.setLabelTopBarSelect(1);
                } else if (CameraUtils.getLabelTopBarSelect() == 3) {
                    CameraUtils.setLabelTopBarSelectMemory(2);
                    CameraUtils.setLabelTopBarSelect(2);
                } else if (CameraUtils.getLabelTopBarSelect() == 4) {
                    CameraUtils.setLabelTopBarSelectMemory(3);
                    CameraUtils.setLabelTopBarSelect(3);
                } else if (CameraUtils.getLabelTopBarSelect() == -1) {
                    int selectMemory = CameraUtils.getLabelTopBarSelectMemory();
                    if (selectMemory == -1) {
                        selectMemory = 3;
                    }
                    CameraUtils.setLabelTopBarSelect(selectMemory);
                    CameraUtils.setLabelTopBarSelectMemory(selectMemory);
                }
                if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211 && getTopBarBgColorStatusIsYellow() == 0 && CameraUtils.getLabelTopBarSelect() != -1) {
                    setTopBarSelectViewColor();
                } else {
                    setTopBarSelectViewColorAnimation();
                }
                if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                    Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_WHITE);
                }
                if (CameraUtils.getFrameLayerNumber() == 0 && ((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_OPEN))).intValue() == 107738) {
                    Util.sendIntEventMessge(Constants.CAMERA_PARAMETER_DISPLAY_GONE);
                }
                if (CameraUtils.getLabelTopBarSelect() == 3 || CameraUtils.getLabelTopBarSelect() == 2) {
                    CameraUtils.setTopBarStatusHaveAnimation(true);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210);
                }
            }
        } else if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
            if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1) {
            }
        } else if (!CameraUtils.isFollowIng() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_RETURN_KEY_210);
            if ((CameraUtils.getLabelTopBarSelect() == 3 || CameraUtils.getLabelTopBarSelect() == 2) && CameraUtils.getFrameLayerNumber() == 0) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        FVTopBarFragment.this.setMenuAndReturnKey();
                    }
                }, 50);
            }
        }
    }

    private void processDataForX(byte[] value) {
        String mfA;
        String mfB;
        String wtA;
        String wtB;
        if ((value[0] & 255) != 90) {
            return;
        }
        if ((value[1] & 255) == 70) {
            ViseLog.m1466e("ViltaX--0x46--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX1：菜单键单击：菜单/返回   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 1);
                    setMenuAndReturnKey();
                    return;
                case 2:
                    ViseLog.m1466e("ViltaX2：菜单键长按：菜单消失   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 2);
                    CameraUtils.setMenuKeyIsLongPress(true);
                    CameraUtils.setLabelTopBarSelectMemory(-1);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        CameraUtils.setLabelTopBarSelect(-1);
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211 && !CameraUtils.getCameraHandModelBgColorIsYellow()) {
                            Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_YELLOW);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    ViseLog.m1466e("ViltaX3：确认键单击：确认/DISP   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                    if (CameraUtils.getCurrentPageIndex() != 2) {
                        return;
                    }
                    if (CameraUtils.isRecordingIng() && CameraUtils.getBooRecordStarted()) {
                        if (MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1) {
                        }
                        return;
                    } else if (!CameraUtils.isFollowIng() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
                        if (CameraUtils.getLabelTopBarSelect() != -1) {
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210);
                            return;
                        } else if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() == 107212) {
                            if (CameraUtils.getFrameLayerNumber() != 0) {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210);
                                return;
                            } else if (!CameraUtils.getMarkPointUIIsVisible()) {
                                Util.sendIntEventMessge(Constants.CAMERA_PARAMETER_DISPLAY_VISIBLE_OR_GONE);
                                return;
                            } else {
                                return;
                            }
                        } else if (CameraUtils.getCameraHandModel()) {
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CHANGE);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 4:
                    ViseLog.m1466e("ViltaX4：确认键长按：关闭手动调参");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 4);
                    return;
                case 5:
                    ViseLog.m1466e("ViltaX5：相册键单击：进入相册");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 5);
                    EventBusUtil.sendEvent(new Event(56));
                    return;
                case 6:
                    ViseLog.m1466e("ViltaX6：相册键长按：rsv");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 6);
                    return;
                case 7:
                    ViseLog.m1466e("ViltaX7：闪关灯键单击：开/关闪光灯   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 7);
                    if (CameraUtils.getCurrentPageIndex() == 2 && !((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue() && !CameraUtils.getIsBooleanTimeLapseUIShow() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1 && !CameraUtils.isFollowIng()) {
                        if (CameraUtils.getFrameLayerNumber() != 0) {
                            int topBarSelectMemory = CameraUtils.getLabelTopBarSelectMemory();
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                            CameraUtils.setLabelTopBarSelectMemory(topBarSelectMemory);
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_RESTORE);
                            return;
                        } else if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() != 10101) {
                            int flashMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FLASH_MODE, 10003)).intValue();
                            if (flashMode == 10003) {
                                Util.sendIntEventMessge(Constants.GET_FLASH_ON_210);
                                return;
                            } else if (flashMode == 10004) {
                                Util.sendIntEventMessge(Constants.GET_FLASH_LONG_210);
                                return;
                            } else if (flashMode == 10006) {
                                Util.sendIntEventMessge(Constants.GET_FLASH_OFF_210);
                                return;
                            } else if (flashMode == 10005) {
                                Util.sendIntEventMessge(Constants.GET_FLASH_AUTO_210);
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
                case 8:
                    ViseLog.m1466e("ViltaX8：闪关灯键长按：rsv   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 8);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.GET_FLASH_AUTO_210);
                        return;
                    }
                    return;
                case 9:
                    ViseLog.m1466e("ViltaX9：EV 键单击：EV 调参   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 9);
                    if (CameraUtils.getCurrentPageIndex() == 2 && !((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue() && !CameraUtils.getIsBooleanTimeLapseUIShow() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
                        getFVCameraManager();
                        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                            if (this.cameraManager.getCameraManagerType() != 2) {
                                EventBusUtil.sendEvent(new Event(150));
                                return;
                            } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                EventBusUtil.sendEvent(new Event(151));
                                return;
                            } else {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                                int seMemory = CameraUtils.getLabelTopBarSelectMemory();
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                                CameraUtils.setLabelTopBarSelectMemory(seMemory);
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN_EV_VISIBLE);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 10:
                    ViseLog.m1466e("ViltaX10：EV 键长按：rsv   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, C0217dk.f724n);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                            if (this.cameraManager.getCameraManagerType() != 2) {
                                EventBusUtil.sendEvent(new Event(150));
                                return;
                            } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                EventBusUtil.sendEvent(new Event(151));
                                return;
                            } else {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 11:
                    ViseLog.m1466e("ViltaXISO 键单击：ISO 调参   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, ClosedCaptionCtrl.MID_ROW_CHAN_1);
                    if (CameraUtils.getCurrentPageIndex() == 2 && !((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue() && !CameraUtils.getIsBooleanTimeLapseUIShow() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
                        getFVCameraManager();
                        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                            if (this.cameraManager.getCameraManagerType() != 2) {
                                EventBusUtil.sendEvent(new Event(150));
                                return;
                            } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                EventBusUtil.sendEvent(new Event(151));
                                return;
                            } else {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                                int seMemory2 = CameraUtils.getLabelTopBarSelectMemory();
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                                CameraUtils.setLabelTopBarSelectMemory(seMemory2);
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN_ISO_VISIBLE);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 12:
                    ViseLog.m1466e("ViltaX12：ISO 键长按：rsv   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 18);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                            if (this.cameraManager.getCameraManagerType() != 2) {
                                EventBusUtil.sendEvent(new Event(150));
                                return;
                            } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                EventBusUtil.sendEvent(new Event(151));
                                return;
                            } else {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 13:
                    ViseLog.m1466e("ViltaX13：Shuter 键单击：Shuter 调参   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 19);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Log.e("---------------", "---------  5555  7777  1111  9999  --------   CamProLinear:" + MoveTimelapseUtil.getInstance().getCameraProgressLinear() + "   TimeLapseUIShow:" + CameraUtils.getIsBooleanTimeLapseUIShow());
                        if (!((Boolean) SPUtils.get(this.mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue() && !CameraUtils.getIsBooleanTimeLapseUIShow() && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 1) {
                            getFVCameraManager();
                            if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                                if (this.cameraManager.getCameraManagerType() != 2) {
                                    EventBusUtil.sendEvent(new Event(150));
                                    return;
                                } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                    EventBusUtil.sendEvent(new Event(151));
                                    return;
                                } else {
                                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                                    int seMemory3 = CameraUtils.getLabelTopBarSelectMemory();
                                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                                    CameraUtils.setLabelTopBarSelectMemory(seMemory3);
                                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_OPEN_SHUTTER_VISIBLE);
                                    return;
                                }
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 14:
                    ViseLog.m1466e("ViltaX14：Shuter 键长按：rsv   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, ClosedCaptionCtrl.MISC_CHAN_1);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager != null && this.cameraManager.isCameraOpened()) {
                            if (this.cameraManager.getCameraManagerType() != 2) {
                                EventBusUtil.sendEvent(new Event(150));
                                return;
                            } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                                EventBusUtil.sendEvent(new Event(151));
                                return;
                            } else {
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                                Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 16:
                    ViseLog.m1466e("ViltaX16：X 键长按：rsv");
                    Log.i("KBein", "FVTopBarFragment.processDataForX():--长按 16：X 键长按：rsv--");
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 22);
                    return;
                case 17:
                    ViseLog.m1466e("ViltaX17：MarkA 键单击：回焦点 A   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, ClosedCaptionCtrl.TAB_OFFSET_CHAN_1);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager == null) {
                            return;
                        }
                        if (this.cameraManager.getCameraManagerType() != 2) {
                            EventBusUtil.sendEvent(new Event(146));
                            return;
                        } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                            EventBusUtil.sendEvent(new Event(147));
                            return;
                        } else if (!CameraUtils.getMarkPointKeyCanUse(this.mContext)) {
                            return;
                        } else {
                            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                                EventBusUtil.sendEvent(new Event(137));
                                return;
                            } else if (!CameraUtils.getMarkPointUIIsVisible()) {
                                return;
                            } else {
                                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021) {
                                    Util.sendIntEventMessge(Constants.MARK_POINT_A_MF_RETURN_210);
                                    return;
                                } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                                    Util.sendIntEventMessge((int) Constants.MARK_POINT_A_VISIBLE_210_RESTART_WT, String.valueOf(1.0f));
                                    return;
                                } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                                    Util.sendIntEventMessge((int) Constants.MARK_POINT_A_VISIBLE_210_RESTART, String.valueOf(0.0f));
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                case 18:
                    ViseLog.m1466e("ViltaX18：MarkA 键长按：标记焦点A   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, (byte) 24);
                    if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager == null) {
                            return;
                        }
                        if (this.cameraManager.getCameraManagerType() != 2) {
                            EventBusUtil.sendEvent(new Event(146));
                            return;
                        } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                            EventBusUtil.sendEvent(new Event(147));
                            return;
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (!CameraUtils.getMarkPointKeyCanUse(FVTopBarFragment.this.mContext)) {
                                        return;
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                                        EventBusUtil.sendEvent(new Event(137));
                                        return;
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_OPEN))).intValue() == 107738) {
                                        Util.sendIntEventMessge(Constants.CAMERA_PARAMETER_DISPLAY_GONE);
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_A_VISIBLE_210);
                                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_A_VISIBLE_210_WT);
                                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_A_VISIBLE_210);
                                    }
                                }
                            }, 200);
                            return;
                        }
                    } else {
                        return;
                    }
                case 19:
                    ViseLog.m1466e("ViltaXMarkB 键单击：回焦点 B   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, ClosedCaptionCtrl.MID_ROW_CHAN_2);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager == null) {
                            return;
                        }
                        if (this.cameraManager.getCameraManagerType() != 2) {
                            EventBusUtil.sendEvent(new Event(146));
                            return;
                        } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                            EventBusUtil.sendEvent(new Event(147));
                            return;
                        } else if (!CameraUtils.getMarkPointKeyCanUse(this.mContext)) {
                            return;
                        } else {
                            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                                EventBusUtil.sendEvent(new Event(137));
                                return;
                            } else if (!CameraUtils.getMarkPointUIIsVisible()) {
                                return;
                            } else {
                                if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021) {
                                    Util.sendIntEventMessge(Constants.MARK_POINT_B_MF_RETURN_210);
                                    return;
                                } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                                    Util.sendIntEventMessge((int) Constants.MARK_POINT_B_VISIBLE_210_RESTART_WT, String.valueOf(CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d));
                                    return;
                                } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                                    Util.sendIntEventMessge((int) Constants.MARK_POINT_B_VISIBLE_210_RESTART, String.valueOf((int) (CameraUtils.getScaleScrollViewMFMaxNums() - 1.0d)) + ".00");
                                    return;
                                } else {
                                    return;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                case 20:
                    ViseLog.m1466e("ViltaX20：MarkB 键长按：标记焦点B   标识:" + CameraUtils.getFrameLayerNumber());
                    BleByteUtil.ackPTZPanorama((byte) 70, ClosedCaptionCtrl.RESUME_CAPTION_LOADING);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        getFVCameraManager();
                        if (this.cameraManager == null) {
                            return;
                        }
                        if (this.cameraManager.getCameraManagerType() != 2) {
                            EventBusUtil.sendEvent(new Event(146));
                            return;
                        } else if (FVCameraManager.GetCameraLevel(this.mContext) == 2) {
                            EventBusUtil.sendEvent(new Event(147));
                            return;
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    if (!CameraUtils.getMarkPointKeyCanUse(FVTopBarFragment.this.mContext)) {
                                        return;
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
                                        EventBusUtil.sendEvent(new Event(137));
                                        return;
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.mContext, SharePrefConstant.LABEL_CAMERA_PARAMETER_DISPLAY, Integer.valueOf(Constants.LABEL_CAMERA_PARAMETER_DISPLAY_OPEN))).intValue() == 107738) {
                                        Util.sendIntEventMessge(Constants.CAMERA_PARAMETER_DISPLAY_GONE);
                                    }
                                    if (((Integer) SPUtils.get(FVTopBarFragment.this.getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() != 107021) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_B_VISIBLE_210);
                                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_B_VISIBLE_210_WT);
                                    } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                                        Util.sendIntEventMessge(Constants.MARK_POINT_B_VISIBLE_210);
                                    }
                                }
                            }, 200);
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        } else if ((value[1] & 255) == 72) {
            ViseLog.m1466e("ViltaX--0x48--data--" + HexUtil.encodeHexStr(value));
            switch (value[2] & 255) {
                case 1:
                    ViseLog.m1466e("ViltaX→");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 1);
                    return;
                case 2:
                    ViseLog.m1466e("ViltaX←");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 2);
                    return;
                case 3:
                    ViseLog.m1466e("ViltaX↑");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 3);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_ROCKING_BAR_UP_210);
                    return;
                case 4:
                    ViseLog.m1466e("ViltaX↓");
                    BleByteUtil.ackPTZPanorama((byte) 72, (byte) 4);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_ROCKING_BAR_DOWN_210);
                    return;
                default:
                    return;
            }
        } else if ((value[1] & 255) == 86) {
            ViseLog.m1466e("ViltaX15：F/Z键 X 键单击：rsv");
            Log.i("KBein", "FVTopBarFragment.processDataForX():--15：F/Z键 X 键单击：rsv--");
            BleByteUtil.ackPTZPanorama((byte) (value[1] & 255), (byte) (value[2] & 255));
            BlePtzParasConstant.SET_FOCUS_KNOB_FUNCTION = value[2] & 255;
            BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = value[2] & 255;
            if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 1) {
                BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION = 21;
            }
            if (CameraUtils.getFrameLayerNumber() == 1) {
                Util.sendIntEventMessge(Constants.LABEL_FOCUSING_WHEEL_MODE_RESTART);
            }
            getFVCameraManager();
            Util.sendIntEventMessge(Constants.LABEL_FZ_ON_TOUCH_START);
            if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.MARK_POINT_SPACING_YES_NO, Integer.valueOf(Constants.MARK_POINT_SPACING_NO))).intValue() == 107021 && CameraUtils.getMarkPointUIIsVisible()) {
                if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 0) {
                    if (CameraUtils.getLlMarkPointWtA() == 1.0f) {
                        wtA = String.valueOf(1.0f);
                    } else {
                        wtA = String.valueOf(CameraUtils.getLlMarkPointWtA());
                    }
                    Util.sendIntEventMessge((int) Constants.MARK_POINT_A_VISIBLE_210_RESTART_WT, wtA);
                    if (CameraUtils.getLlMarkPointWtB() != 1.0f) {
                        wtB = String.valueOf(CameraUtils.getLlMarkPointWtB());
                    } else if (CameraUtils.getMarkPointWtIsFirst()) {
                        wtB = String.valueOf(CameraUtils.getScaleScrollViewWTMaxNums() + 1.0d);
                    } else {
                        wtB = String.valueOf(CameraUtils.getLlMarkPointWtB());
                    }
                    Util.sendIntEventMessge((int) Constants.MARK_POINT_B_VISIBLE_210_RESTART_WT, wtB);
                } else if (BlePtzParasConstant.SET_THUMB_WHEEL_SELECTION == 21) {
                    if (CameraUtils.getLlMarkPointMfA() == 0.0f) {
                        mfA = String.valueOf(0.0f);
                    } else {
                        mfA = String.valueOf(CameraUtils.getLlMarkPointMfA());
                    }
                    Util.sendIntEventMessge((int) Constants.MARK_POINT_A_VISIBLE_210_RESTART, mfA);
                    if (CameraUtils.getLlMarkPointMfB() != 0.0f) {
                        mfB = String.valueOf(CameraUtils.getLlMarkPointMfB());
                    } else if (CameraUtils.getMarkPointMfIsFirst()) {
                        mfB = String.valueOf((int) (CameraUtils.getScaleScrollViewMFMaxNums() - 1.0d)) + ".00";
                    } else {
                        mfB = String.valueOf(CameraUtils.getLlMarkPointMfB());
                    }
                    Util.sendIntEventMessge((int) Constants.MARK_POINT_B_VISIBLE_210_RESTART, mfB);
                }
            }
        }
    }

    private void initListener() {
        this.rlBack.setOnClickListener(this);
        this.rlCamera.setOnClickListener(this);
        this.rlVilta.setOnClickListener(this);
        this.rlSetting.setOnClickListener(this);
    }

    private void initData() {
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            this.rl_ptz_battery.setVisibility(0);
            this.handler.post(this.runnable);
        } else {
            this.rl_ptz_battery.setVisibility(4);
        }
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
            this.mMyCountDownTimer = new MyCountDownTimer(2000, 1000);
            this.mMyCountDownTimer.start();
        } else {
            this.mMyCountDownTimer = new MyCountDownTimer(2000, 1000);
            this.mMyCountDownTimer.start();
        }
        this.mCustomToast = new CustomToast(getActivity());
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
                FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                        FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(1);
                }
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
                FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                        FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(2);
                }
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
                FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                        FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(0);
                }
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                        FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(3);
                }
            } else {
                FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                        FVTopBarFragment.this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                        FVTopBarFragment.this.btnVilta.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnViltaStatus(0);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case 10007:
                showCameraPop(1);
                return;
            case 10008:
                showCameraPop(2);
                return;
            case 10009:
                this.llRoot.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white_round5_bg));
                this.btnCameraStatus.setVisibility(0);
                CameraUtils.setLlRootBackgroundColor(false);
                return;
            case Constants.DELAY_TAKE_PHOTO_10S:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(3);
                    return;
                }
                return;
            case Constants.FULL_SHOT_NONE:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_camera);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(0);
                    return;
                }
                return;
            case Constants.FULL_SHOT_180:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_180);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_180_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(4);
                    return;
                }
                return;
            case Constants.FULL_SHOT_330:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_330);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_330_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(5);
                    return;
                }
                return;
            case Constants.FULL_SHOT_3X3:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(6);
                    return;
                }
                return;
            case Constants.FULL_SHOT_3X5:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(7);
                    return;
                }
                return;
            case Constants.CAMERA_INDICATE_HIDDEN:
                if (CameraUtils.getBooRecordStarted()) {
                    this.llRoot.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white10_round5_bg));
                } else {
                    this.llRoot.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white50_round5_bg));
                }
                this.btnCameraStatus.setVisibility(8);
                CameraUtils.setLlRootBackgroundColor(true);
                return;
            case Constants.DELAY_TAKE_PHOTO_0S:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_camera);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(0);
                    return;
                }
                return;
            case Constants.DELAY_TAKE_PHOTO_2S:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(1);
                    return;
                }
                return;
            case Constants.DELAY_TAKE_PHOTO_5S:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                        this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s_yellow);
                        this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    }
                    CameraUtils.setBtnCameraStatus(2);
                    return;
                }
                return;
            case Constants.LONG_EXPOSURE_DOUBLE_IMAGE:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_double_image);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case Constants.LONG_EXPOSURE_TRACK:
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_track);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case Constants.TOP_BAR_FRAGMENT_BUTTON_UNCLICK:
                String value = (String) fvModeSelectEvent.getMessage();
                if (Util.isEmpty(value)) {
                    return;
                }
                if (BleConstant.SHUTTER.equals(value)) {
                    this.clickable = false;
                    unClickChangeUI();
                    return;
                } else if ("0".equals(value)) {
                    this.clickable = true;
                    recoverClickChangeUI();
                    return;
                } else {
                    return;
                }
            case Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (this.popupWindow != null && (this.popupWindow.isShowing() || !CameraUtils.getLlRootBackgroundColor() || CameraUtils.getFrameLayerNumber() != 0)) {
                    return;
                }
                if (CameraUtils.getLabelTopBarSelect() == 1) {
                    onClickDownView(this.rlSetting);
                    return;
                } else if (CameraUtils.getLabelTopBarSelect() == 2) {
                    onClickDownView(this.rlVilta);
                    return;
                } else if (CameraUtils.getLabelTopBarSelect() == 3) {
                    onClickDownView(this.rlCamera);
                    return;
                } else if (CameraUtils.getLabelTopBarSelect() == 4) {
                    onClickDownView(this.rlBack);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                topBarBtnStatusRestartBlack();
                setBtnImageCameraYellowOrBlack();
                setBtnImageViltaYellowOrBlack();
                this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
                this.btn_bg_color_yellow.setVisibility(8);
                this.btn_bg_color_yellow.clearAnimation();
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART_SETTING:
                if (CameraUtils.getCurrentPageIndex() != 2) {
                    return;
                }
                if (this.popupWindow != null && (this.popupWindow.isShowing() || !CameraUtils.getLlRootBackgroundColor() || CameraUtils.getFrameLayerNumber() != 0)) {
                    return;
                }
                if (!CameraUtils.isRecordingIng() || !CameraUtils.getBooRecordStarted()) {
                    CameraUtils.setLabelTopBarSelect(1);
                    setTopBarSelectViewColor();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    int selectMemory = CameraUtils.getLabelTopBarSelect();
                    if (selectMemory == -1) {
                    }
                    CameraUtils.setLabelTopBarSelectMemory(selectMemory);
                    CameraUtils.setFrameLayerNumber(0);
                    CameraUtils.setLabelTopBarSelect(-1);
                    topBarBtnStatusRestartBlack();
                    setBtnImageCameraYellowOrBlack();
                    setBtnImageViltaYellowOrBlack();
                    this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                    this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                    this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    this.btn_bg_color_yellow.setVisibility(8);
                    this.btn_bg_color_yellow.clearAnimation();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    int seMemory = CameraUtils.getLabelTopBarSelectMemory();
                    if (seMemory == -1) {
                    }
                    CameraUtils.setLabelTopBarSelect(seMemory);
                    topBarBtnStatusOneIsYellow(seMemory);
                    setBtnImageCameraYellowOrBlack();
                    setBtnImageViltaYellowOrBlack();
                    if (CameraUtils.getBtnSettingBgColorIsYellow()) {
                        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting_yellow));
                        this.btnSetting.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    } else {
                        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                        this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    }
                    if (CameraUtils.getBtnBackBgColorIsYellow()) {
                        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back_yellow));
                        this.btnBack.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    } else {
                        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    }
                    this.btn_bg_color_yellow.setVisibility(8);
                    this.btn_bg_color_yellow.clearAnimation();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_RESTORE:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    int seMemory2 = CameraUtils.getLabelTopBarSelectMemory();
                    if (seMemory2 == -1) {
                    }
                    CameraUtils.setLabelTopBarSelect(seMemory2);
                    topBarBtnStatusOneIsYellow(seMemory2);
                    setBtnImageCameraYellowOrBlack();
                    setBtnImageViltaYellowOrBlack();
                    if (CameraUtils.getBtnSettingBgColorIsYellow()) {
                        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting_yellow));
                        this.btnSetting.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    } else {
                        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                        this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    }
                    if (CameraUtils.getBtnBackBgColorIsYellow()) {
                        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back_yellow));
                        this.btnBack.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    } else {
                        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    }
                    this.btn_bg_color_yellow.setVisibility(8);
                    this.btn_bg_color_yellow.clearAnimation();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void unClickChangeUI() {
        int fullShot = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
        int leMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
        if (fullShot != 10024) {
            this.llRoot.setVisibility(4);
            this.rl_battery.setVisibility(4);
            this.fullShotInvisible = true;
            return;
        }
        this.btnBack.setImageResource(C0853R.mipmap.ic_home_back_enable);
        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
        this.btnSetting.setImageResource(C0853R.mipmap.ic_setting_enable);
        this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getBtnSettingBgColorIsYellow()) {
            this.btn_bg_color_yellow.setVisibility(8);
            this.btn_bg_color_yellow.clearAnimation();
        }
        if (leMode != 106205) {
            if (leMode == 106206) {
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_double_image_enable);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (leMode == 106207) {
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_track_enable);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
            }
        } else if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getBtnCameraBgColorIsYellow()) {
            this.btn_bg_color_yellow.setVisibility(8);
            this.btn_bg_color_yellow.clearAnimation();
            this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_enable);
            this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
        }
        if (CameraUtils.getCurrentPageIndex() != 2 || !CameraUtils.getBtnViltaBgColorIsYellow()) {
            if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_enable);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_enable);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_enable);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_enable);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_enable);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            }
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        }
    }

    private void recoverClickChangeUI() {
        int fullShot = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
        int leMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
        if (fullShot != 10024) {
            this.llRoot.setVisibility(0);
            this.rl_battery.setVisibility(0);
            this.fullShotInvisible = false;
            return;
        }
        this.btnBack.setImageResource(C0853R.mipmap.ic_home_back);
        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
        if (CameraUtils.getCurrentPageIndex() != 2 || !CameraUtils.getBtnSettingBgColorIsYellow()) {
            this.btnSetting.setImageResource(C0853R.mipmap.ic_setting);
            this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
        } else {
            this.btnSetting.setImageResource(C0853R.mipmap.ic_setting_yellow);
            this.btnSetting.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        }
        if (leMode != 106205) {
            if (leMode == 106206) {
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_double_image);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (leMode == 106207) {
                this.btnCamera.setImageResource(C0853R.mipmap.ic_le_track);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
            }
        } else if (CameraUtils.getCurrentPageIndex() != 2 || !CameraUtils.getBtnCameraBgColorIsYellow()) {
            this.btnCamera.setImageResource(C0853R.mipmap.ic_camera);
            this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
        } else {
            this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_yellow);
            this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        }
        if (CameraUtils.getCurrentPageIndex() != 2 || !CameraUtils.getBtnViltaBgColorIsYellow()) {
            if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else {
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
            }
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 0) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 1) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 2) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else if (BlePtzParasConstant.SET_PTZ_FOLLOW_MODE == 3) {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        } else {
            this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
        }
    }

    private void onClickDownView(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.rl_camera:
                if (this.clickable) {
                    MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(0);
                    int cameraMode = ((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_MODE, 10001)).intValue();
                    if (cameraMode == 10001) {
                        showCameraPop(1);
                        return;
                    } else if (cameraMode == 10002) {
                        showCameraPop(2);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case C0853R.C0855id.rl_vilta:
                MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(0);
                showCameraPop(3);
                return;
            case C0853R.C0855id.rl_setting:
                if (this.clickable) {
                    getFVCameraManager();
                    if (this.cameraManager != null) {
                        if (!"smartisan".equals(Build.BRAND.toLowerCase())) {
                            CameraUtils.setCheckMediaHighSpeedRecordMapSize(this.cameraManager.getSlowMotionFpsResolutionMap());
                        }
                        CameraUtils.setVideoStabilizationSupport(Boolean.valueOf(this.cameraManager.isVideoStabilizationSupport()));
                        if (this.cameraManager.getMaxSupportedPictureSize() != null) {
                            CameraUtils.setMaxSupPictureSize(String.valueOf(this.cameraManager.getMaxSupportedPictureSize()));
                        }
                        if (this.cameraManager.getRecommendPictureSize() != null) {
                            CameraUtils.setReComPictureSize(String.valueOf(this.cameraManager.getRecommendPictureSize()));
                        }
                        if (this.cameraManager.getCameraManagerType() == 1) {
                            CameraUtils.setUserCameraOneOrTwo(1);
                            CameraUtils.setMediaRecordSize(this.cameraManager.getSupportedMediaRecordQuality());
                            CameraUtils.setMediaRecordSize(this.cameraManager.getSupportedMediaRecordQuality());
                        } else {
                            CameraUtils.setUserCameraOneOrTwo(2);
                            CameraUtils.setMediaRecordSizeTwo(this.cameraManager.getSupportedMediaRecordSizes());
                            setVideoResoluCheck();
                        }
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            CameraUtils.setFrameLayerNumber(0);
                            CameraUtils.setLabelTopBarSelect(-1);
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
                            Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT);
                            CameraUtils.setLabelTopBarSelectMemory(-1);
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                if (FVTopBarFragment.this.cameraManager.getCameraManagerType() == 1) {
                                    if (CameraUtils.getMediaRecordSize() != null && CameraUtils.getIsCameraOpened() && !"[]".equals(CameraUtils.getMediaRecordSize().toString())) {
                                        Intent intent = new Intent(FVTopBarFragment.this.getActivity(), FVAdvancedSettingAvtivity.class);
                                        intent.putExtra("from", 1);
                                        FVTopBarFragment.this.startActivity(intent);
                                    }
                                } else if (CameraUtils.getMediaRecordSizeTwo() != null && CameraUtils.getIsCameraOpened() && !"[]".equals(CameraUtils.getMediaRecordSizeTwo().toString())) {
                                    Intent intent2 = new Intent(FVTopBarFragment.this.getActivity(), FVAdvancedSettingAvtivity.class);
                                    intent2.putExtra("from", 1);
                                    FVTopBarFragment.this.startActivity(intent2);
                                }
                            }
                        }, 100);
                        return;
                    }
                    return;
                }
                return;
            case C0853R.C0855id.rl_back:
                if (this.clickable) {
                    CameraUtils.clearData();
                    getActivity().setResult(2);
                    getActivity().finish();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setVideoResoluCheck() {
        CameraUtils.setMediaRecordSizeTwo(this.cameraManager.getSupportedMediaRecordSizes());
        if (((Integer) SPUtils.get(getActivity(), SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            if ("".equals((String) SPUtils.get(getActivity(), SharePrefConstant.FRONT_VIDEO_RESOLUTION_TWO, ""))) {
                Size[] recordSizeFirst = CameraUtils.getMediaRecordSizeTwo();
                if (recordSizeFirst.length >= 1) {
                    CameraUtils.setCheckMediaRecordFrontSizeTwo(recordSizeFirst[0]);
                }
            }
        } else if ("".equals((String) SPUtils.get(getActivity(), SharePrefConstant.VIDEO_RESOLUTION_TWO, ""))) {
            Size[] recordSizeFirst2 = CameraUtils.getMediaRecordSizeTwo();
            if (recordSizeFirst2.length >= 1) {
                CameraUtils.setCheckMediaRecordSizeTwo(recordSizeFirst2[0]);
            }
        }
    }

    public void onClick(View v) {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.btn_bg_color_yellow.setVisibility(8);
            this.btn_bg_color_yellow.clearAnimation();
        }
        onClickDownView(v);
    }

    /* access modifiers changed from: private */
    public void getPtzBatteryInfo() {
        int ptzBatteryPercentage = BlePtzParasConstant.GET_PTZ_BATTERY_SURPLUS_CAPACITY_PERCENTAGE;
        if (!this.fullShotInvisible) {
            if (ptzBatteryPercentage > 0) {
                this.rl_ptz_battery.setVisibility(0);
            } else {
                this.rl_ptz_battery.setVisibility(4);
            }
        }
        if (BlePtzParasConstant.GET_PTZ_BATTERY_CHARGING_NOTIFY == 1) {
            this.icon_ptz_battery.setImageResource(C0853R.mipmap.ic_charging);
        } else if (ptzBatteryPercentage > 75) {
            this.icon_ptz_battery.setImageResource(C0853R.mipmap.ic_battery_full);
        } else if (ptzBatteryPercentage > 50) {
            this.icon_ptz_battery.setImageResource(C0853R.mipmap.ic_battery_seventy);
        } else if (ptzBatteryPercentage > 30) {
            this.icon_ptz_battery.setImageResource(C0853R.mipmap.ic_battery_half);
        } else {
            this.icon_ptz_battery.setImageResource(C0853R.mipmap.ic_battery_low);
        }
        this.tv_ptz_battery_remain.setText(ptzBatteryPercentage + "%");
        this.handler.postDelayed(this.runnable, 1000);
    }

    private void setBtnImageViltaYellowOrBlack() {
        this.btn_bg_color_yellow.setVisibility(8);
        switch (CameraUtils.getBtnViltaStatus()) {
            case 0:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 1:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 2:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 3:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                    this.btnVilta.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            default:
                return;
        }
    }

    private void setBtnImageCameraYellowOrBlack() {
        this.btn_bg_color_yellow.setVisibility(8);
        switch (CameraUtils.getBtnCameraStatus()) {
            case 0:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_camera);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 1:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 2:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 3:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 4:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_180_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_180);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 5:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_330_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_330);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 6:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            case 7:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up_yellow);
                    this.btnCamera.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
                    return;
                }
                this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up);
                this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
                return;
            default:
                return;
        }
    }

    private void setBtnImageViltaYellowOrBlackAnimation() {
        if (!CameraUtils.getBtnViltaBgColorIsYellow()) {
            this.btnVilta.setBackground(getResources().getDrawable(C0853R.color.transparent));
        }
        switch (CameraUtils.getBtnViltaStatus()) {
            case 0:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_yellow);
                    return;
                } else {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta);
                    return;
                }
            case 1:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half_yellow);
                    return;
                } else {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_half);
                    return;
                }
            case 2:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all_yellow);
                    return;
                } else {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_all);
                    return;
                }
            case 3:
                if (CameraUtils.getBtnViltaBgColorIsYellow()) {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv_yellow);
                    return;
                } else {
                    this.btnVilta.setImageResource(C0853R.mipmap.ic_mobile_vilta_fpv);
                    return;
                }
            default:
                return;
        }
    }

    private void setBtnImageCameraYellowOrBlackAnimation() {
        if (!CameraUtils.getBtnCameraBgColorIsYellow()) {
            this.btnCamera.setBackground(getResources().getDrawable(C0853R.color.transparent));
        }
        switch (CameraUtils.getBtnCameraStatus()) {
            case 0:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_camera_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_camera);
                    return;
                }
            case 1:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_2s);
                    return;
                }
            case 2:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_5s);
                    return;
                }
            case 3:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_delay_10s);
                    return;
                }
            case 4:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_180_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_180);
                    return;
                }
            case 5:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_330_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_330);
                    return;
                }
            case 6:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_3);
                    return;
                }
            case 7:
                if (CameraUtils.getBtnCameraBgColorIsYellow()) {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up_yellow);
                    return;
                } else {
                    this.btnCamera.setImageResource(C0853R.mipmap.ic_3_5up);
                    return;
                }
            default:
                return;
        }
    }

    private void topBarBtnStatusRestartBlack() {
        CameraUtils.setBtnSettingBgColorIsYellow(false);
        CameraUtils.setBtnViltaBgColorIsYellow(false);
        CameraUtils.setBtnCameraBgColorIsYellow(false);
        CameraUtils.setBtnBackBgColorIsYellow(false);
    }

    private void topBarBtnStatusOneIsYellow(int status) {
        CameraUtils.setBtnSettingBgColorIsYellow(false);
        CameraUtils.setBtnViltaBgColorIsYellow(false);
        CameraUtils.setBtnCameraBgColorIsYellow(false);
        CameraUtils.setBtnBackBgColorIsYellow(false);
        if (status == 1) {
            CameraUtils.setBtnSettingBgColorIsYellow(true);
        } else if (status == 2) {
            CameraUtils.setBtnViltaBgColorIsYellow(true);
        } else if (status == 3) {
            CameraUtils.setBtnCameraBgColorIsYellow(true);
        } else if (status == 4) {
            CameraUtils.setBtnBackBgColorIsYellow(true);
        }
    }

    private int getTopBarBgColorStatusIsYellow() {
        if (CameraUtils.getBtnCameraBgColorIsYellow()) {
            return 3;
        }
        if (CameraUtils.getBtnViltaBgColorIsYellow()) {
            return 2;
        }
        if (CameraUtils.getBtnSettingBgColorIsYellow()) {
            return 1;
        }
        if (CameraUtils.getBtnBackBgColorIsYellow()) {
            return 4;
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public void topBarImageStatusAnimation(int index) {
        int dip2px = Util.dip2px(getContext(), 16.0f);
        int dip2px2 = Util.dip2px(getContext(), 15.0f);
        int[] locationLlRoot = new int[2];
        this.llRoot.getLocationOnScreen(locationLlRoot);
        int xLlRoot = locationLlRoot[0];
        int yLlRoot = locationLlRoot[1];
        int[] locationBtnCamera = new int[2];
        this.rlCamera.getLocationOnScreen(locationBtnCamera);
        int i = locationBtnCamera[0];
        int i2 = locationBtnCamera[1];
        int[] locationBtnVilta = new int[2];
        this.rlVilta.getLocationOnScreen(locationBtnVilta);
        int i3 = locationBtnVilta[0];
        int i4 = locationBtnVilta[1];
        int[] locationBtnSetting = new int[2];
        this.rlSetting.getLocationOnScreen(locationBtnSetting);
        int i5 = locationBtnSetting[0];
        int i6 = locationBtnSetting[1];
        int[] locationBtnBack = new int[2];
        this.rlBack.getLocationOnScreen(locationBtnBack);
        int i7 = locationBtnBack[0];
        int i8 = locationBtnBack[1];
        int yUnitLength = locationBtnVilta[1] - yLlRoot;
        int circleLength = Util.dip2px(getContext(), 37.0f);
        int xAddNum = (Util.dip2px(getContext(), 50.0f) - circleLength) / 2;
        int yAddNum = (yUnitLength - circleLength) / 2;
        int xCamera = (locationBtnCamera[0] - xLlRoot) + xAddNum;
        int yCamera = (locationBtnCamera[1] - yLlRoot) + yAddNum;
        int xVilta = (locationBtnVilta[0] - xLlRoot) + xAddNum;
        int yVilta = (locationBtnVilta[1] - yLlRoot) + yAddNum;
        int xSetting = (locationBtnSetting[0] - xLlRoot) + xAddNum;
        int ySetting = (locationBtnSetting[1] - yLlRoot) + yAddNum;
        int xBack = (locationBtnBack[0] - xLlRoot) + xAddNum;
        int yBack = (locationBtnBack[1] - yLlRoot) + yAddNum;
        switch (index) {
            case 0:
                this.btn_bg_color_yellow.setVisibility(0);
                Animation animation0 = new TranslateAnimation((float) xCamera, (float) xCamera, (float) ((-yCamera) - 20), (float) yCamera);
                animation0.setDuration(400);
                animation0.setRepeatCount(0);
                animation0.setFillAfter(true);
                this.btn_bg_color_yellow.startAnimation(animation0);
                return;
            case 1:
                setMenuAndReturnKey();
                return;
            case 2:
                this.btn_bg_color_yellow.setVisibility(0);
                Animation animation2 = new TranslateAnimation((float) xCamera, (float) xCamera, (float) yVilta, (float) ySetting);
                animation2.setDuration(400);
                animation2.setRepeatCount(0);
                animation2.setFillAfter(true);
                this.btn_bg_color_yellow.startAnimation(animation2);
                return;
            case 3:
                this.btn_bg_color_yellow.setVisibility(0);
                Animation animation3 = new TranslateAnimation((float) xCamera, (float) xCamera, (float) yCamera, (float) yVilta);
                animation3.setDuration(400);
                animation3.setRepeatCount(0);
                animation3.setFillAfter(true);
                this.btn_bg_color_yellow.startAnimation(animation3);
                return;
            case 4:
                topBarImageStatusAnimation(0);
                return;
            default:
                return;
        }
    }

    private void setTopBarSelectViewColor() {
        this.btn_bg_color_yellow.setVisibility(8);
        View view2 = null;
        if (CameraUtils.getLabelTopBarSelect() == 1) {
            CameraUtils.setLabelTopBarSelectMemory(1);
            view2 = this.btnSetting;
            topBarBtnStatusOneIsYellow(1);
        } else if (CameraUtils.getLabelTopBarSelect() == 2) {
            CameraUtils.setLabelTopBarSelectMemory(2);
            view2 = this.btnVilta;
            topBarBtnStatusOneIsYellow(2);
        } else if (CameraUtils.getLabelTopBarSelect() == 3) {
            CameraUtils.setLabelTopBarSelectMemory(3);
            view2 = this.btnCamera;
            topBarBtnStatusOneIsYellow(3);
        } else if (CameraUtils.getLabelTopBarSelect() == 4) {
            CameraUtils.setLabelTopBarSelectMemory(4);
            view2 = this.btnBack;
            topBarBtnStatusOneIsYellow(4);
        }
        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
        this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
        if (view2 == this.btnCamera) {
            setBtnImageCameraYellowOrBlack();
            setBtnImageViltaYellowOrBlack();
        } else if (view2 == this.btnVilta) {
            setBtnImageCameraYellowOrBlack();
            setBtnImageViltaYellowOrBlack();
        } else if (view2 == this.btnSetting) {
            this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting_yellow));
            this.btnSetting.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
            setBtnImageCameraYellowOrBlack();
            setBtnImageViltaYellowOrBlack();
        } else if (view2 == this.btnBack) {
            this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back_yellow));
            this.btnBack.setBackground(getResources().getDrawable(C0853R.mipmap.ic_topbar_status_bg_circle_yellow));
            setBtnImageCameraYellowOrBlack();
            setBtnImageViltaYellowOrBlack();
        }
    }

    private void setTopBarSelectViewColorAnimation() {
        View view2 = null;
        if (CameraUtils.getLabelTopBarSelect() == 1) {
            CameraUtils.setLabelTopBarSelectMemory(1);
            view2 = this.btnSetting;
            topBarBtnStatusOneIsYellow(1);
        } else if (CameraUtils.getLabelTopBarSelect() == 2) {
            CameraUtils.setLabelTopBarSelectMemory(2);
            view2 = this.btnVilta;
            topBarBtnStatusOneIsYellow(2);
        } else if (CameraUtils.getLabelTopBarSelect() == 3) {
            CameraUtils.setLabelTopBarSelectMemory(3);
            view2 = this.btnCamera;
            topBarBtnStatusOneIsYellow(3);
        } else if (CameraUtils.getLabelTopBarSelect() == 4) {
            CameraUtils.setLabelTopBarSelectMemory(4);
            view2 = this.btnBack;
            topBarBtnStatusOneIsYellow(4);
        }
        this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
        this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
        this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
        this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
        if (view2 == this.btnCamera) {
            setBtnImageCameraYellowOrBlackAnimation();
            setBtnImageViltaYellowOrBlackAnimation();
        } else if (view2 == this.btnVilta) {
            setBtnImageCameraYellowOrBlackAnimation();
            setBtnImageViltaYellowOrBlackAnimation();
        } else if (view2 == this.btnSetting) {
            this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting_yellow));
            setBtnImageCameraYellowOrBlackAnimation();
            setBtnImageViltaYellowOrBlackAnimation();
        } else if (view2 == this.btnBack) {
            this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back_yellow));
            setBtnImageCameraYellowOrBlackAnimation();
            setBtnImageViltaYellowOrBlackAnimation();
        }
    }

    private void showCameraPop(int type) {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            this.btnCameraStatus.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_right_dot_red));
            this.btnViltaStatus.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_right_dot_red));
            if (type == 1) {
                CameraUtils.setLabelTopBarSelect(3);
                topBarBtnStatusOneIsYellow(3);
                if (CameraUtils.getTopBarStatusHaveAnimation()) {
                    setBtnImageCameraYellowOrBlackAnimation();
                    setBtnImageViltaYellowOrBlackAnimation();
                } else {
                    setBtnImageCameraYellowOrBlack();
                    setBtnImageViltaYellowOrBlack();
                }
                CameraUtils.setTopBarStatusHaveAnimation(false);
                this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else if (type == 2) {
                CameraUtils.setLabelTopBarSelect(3);
                topBarBtnStatusOneIsYellow(3);
                if (CameraUtils.getTopBarStatusHaveAnimation()) {
                    setBtnImageCameraYellowOrBlackAnimation();
                    setBtnImageViltaYellowOrBlackAnimation();
                } else {
                    setBtnImageCameraYellowOrBlack();
                    setBtnImageViltaYellowOrBlack();
                }
                CameraUtils.setTopBarStatusHaveAnimation(false);
                this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
            } else {
                CameraUtils.setLabelTopBarSelect(2);
                topBarBtnStatusOneIsYellow(2);
                if (CameraUtils.getTopBarStatusHaveAnimation()) {
                    setBtnImageViltaYellowOrBlackAnimation();
                } else {
                    setBtnImageViltaYellowOrBlack();
                }
                CameraUtils.setTopBarStatusHaveAnimation(false);
                if (!CameraUtils.isRecordingIng() || !CameraUtils.getBooRecordStarted()) {
                    if (CameraUtils.getTopBarStatusHaveAnimation()) {
                        setBtnImageCameraYellowOrBlackAnimation();
                    } else {
                        setBtnImageCameraYellowOrBlack();
                    }
                    CameraUtils.setTopBarStatusHaveAnimation(false);
                    this.btnSetting.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_setting));
                    this.btnSetting.setBackground(getResources().getDrawable(C0853R.color.transparent));
                    this.btnBack.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_home_back));
                    this.btnBack.setBackground(getResources().getDrawable(C0853R.color.transparent));
                }
            }
            int selectMemory = CameraUtils.getLabelTopBarSelect();
            if (selectMemory == -1) {
                selectMemory = 3;
            }
            CameraUtils.setLabelTopBarSelectMemory(selectMemory);
            CameraUtils.setMenuKeyIsLongPress(false);
            if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                Util.sendIntEventMessge(Constants.CAMERA_HAND_MODEL_BG_COLOR_WHITE);
            }
        } else {
            this.btnCameraStatus.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_right_dot));
            this.btnViltaStatus.setImageDrawable(getResources().getDrawable(C0853R.mipmap.ic_right_dot));
        }
        this.llRoot.setBackground(getResources().getDrawable(C0853R.C0854drawable.sp_white_round5_bg));
        CameraUtils.setLlRootBackgroundColor(false);
        if (type == 1) {
            this.btnCameraStatus.setVisibility(0);
            this.btnViltaStatus.setVisibility(8);
        } else if (type == 2) {
            this.btnCameraStatus.setVisibility(0);
            this.btnViltaStatus.setVisibility(8);
        } else {
            this.btnViltaStatus.setVisibility(0);
            this.btnCameraStatus.setVisibility(8);
        }
        int height = Util.getDeviceSize(getActivity()).y - Util.dip2px(getActivity(), 10.0f);
        if (this.popupWindow != null) {
            this.popupWindow.dismiss();
        }
        FVCameraShortcutPop fvCameraPop = null;
        FVCameraVideoShortcutPop fvVideoPop = null;
        FVPTZSettingPop fvPTZPop = null;
        if (type == 1) {
            fvCameraPop = new FVCameraShortcutPop();
            fvCameraPop.init(getActivity(), this.llRoot);
            this.popupWindow = new PopupWindow(fvCameraPop.getView(), height, height, true);
            fvCameraPop.setPop(this.popupWindow);
        } else if (type == 2) {
            fvVideoPop = new FVCameraVideoShortcutPop();
            fvVideoPop.init(getActivity(), this.llRoot);
            this.popupWindow = new PopupWindow(fvVideoPop.getView(), height, height, true);
            fvVideoPop.setPop(this.popupWindow);
        } else {
            fvPTZPop = new FVPTZSettingPop();
            fvPTZPop.init(getActivity(), this.llRoot);
            this.popupWindow = new PopupWindow(fvPTZPop.getView(), height, height, true);
            fvPTZPop.setPop(this.popupWindow);
        }
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(C0853R.style.popAnimation3);
        final FVPTZSettingPop finalFvPTZPop = fvPTZPop;
        final FVCameraShortcutPop finalFvCameraPop = fvCameraPop;
        final FVCameraVideoShortcutPop finalFvVideoPop = fvVideoPop;
        final int i = type;
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (FVTopBarFragment.this.connected && CameraUtils.getCurrentPageIndex() == 2) {
                    BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
                }
                if (i == 1) {
                    finalFvCameraPop.unRegisterListener();
                    FVTopBarFragment.this.btnCameraStatus.setVisibility(8);
                    FVTopBarFragment.this.btnViltaStatus.setVisibility(8);
                    EventBus.getDefault().unregister(finalFvCameraPop);
                } else if (i == 2) {
                    finalFvVideoPop.unRegisterListener();
                    FVTopBarFragment.this.btnCameraStatus.setVisibility(8);
                    FVTopBarFragment.this.btnViltaStatus.setVisibility(8);
                    EventBus.getDefault().unregister(finalFvVideoPop);
                } else {
                    FVTopBarFragment.this.btnViltaStatus.setVisibility(8);
                    FVTopBarFragment.this.btnCameraStatus.setVisibility(8);
                    ViseLog.m1466e("ptzpop dismiss");
                    finalFvPTZPop.unRegisterEvent();
                }
                if (!CameraUtils.isRecordingIng() || !CameraUtils.getBooRecordStarted()) {
                    FVTopBarFragment.this.llRoot.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.C0854drawable.sp_white50_round5_bg));
                } else {
                    FVTopBarFragment.this.llRoot.setBackground(FVTopBarFragment.this.getResources().getDrawable(C0853R.C0854drawable.sp_white10_round5_bg));
                }
                CameraUtils.setLlRootBackgroundColor(true);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        Resources resources = this.mContext.getResources();
        int statusBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
        int[] position = new int[2];
        this.llRoot.getLocationOnScreen(position);
        if (position[0] < 40) {
            statusBarHeight = 0;
        }
        int width = this.llRoot.getRight() + Util.dip2px(getActivity(), 2.0f) + statusBarHeight;
        if (type == 1) {
            this.popupWindow.setFocusable(false);
            this.popupWindow.update();
            this.popupWindow.showAtLocation(this.btnCamera, 0, width, Util.dip2px(getActivity(), 4.0f));
            Util.fullScreenImmersive(this.popupWindow.getContentView());
            this.popupWindow.setFocusable(true);
            this.popupWindow.update();
        } else if (type == 2) {
            this.popupWindow.setFocusable(false);
            this.popupWindow.update();
            this.popupWindow.showAtLocation(this.btnCamera, 0, width, Util.dip2px(getActivity(), 4.0f));
            Util.fullScreenImmersive(this.popupWindow.getContentView());
            this.popupWindow.setFocusable(true);
            this.popupWindow.update();
        } else {
            this.popupWindow.setFocusable(false);
            this.popupWindow.update();
            this.popupWindow.showAtLocation(this.btnCamera, 0, width, Util.dip2px(getActivity(), 4.0f));
            Util.fullScreenImmersive(this.popupWindow.getContentView());
            this.popupWindow.setFocusable(true);
            this.popupWindow.update();
        }
        if (type == 1) {
            Util.sendIntEventMessge(10009);
        } else if (type == 2) {
            Util.sendIntEventMessge(10009);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ViseLog.m1466e("TopFragment onDestroyView");
        this.startVideoThread = false;
        if (this.handler != null) {
            this.handler.removeCallbacks(this.runnable);
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mMyCountDownTimer != null) {
            this.mMyCountDownTimer.cancel();
        }
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public void onDestroy() {
        super.onDestroy();
        ViseLog.m1466e("TopFragment OnDestroy");
    }
}
