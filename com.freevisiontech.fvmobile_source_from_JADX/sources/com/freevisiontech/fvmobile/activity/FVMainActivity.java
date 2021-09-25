package com.freevisiontech.fvmobile.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.p001v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.application.MyApplication;
import com.freevisiontech.fvmobile.bean.UpgradeFromAssetsBean;
import com.freevisiontech.fvmobile.bean.network.ActivateVerify;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.fragment.FVMainFragment;
import com.freevisiontech.fvmobile.utility.BaseActivityManager;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.BleNotifyDataUtil;
import com.freevisiontech.fvmobile.utils.BlePtzParasConstant;
import com.freevisiontech.fvmobile.utils.BleUpgradeFromAssetsUtil;
import com.freevisiontech.fvmobile.utils.BleUpgradeUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.HexUtil;
import com.freevisiontech.fvmobile.utils.JsonUtiCls;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.utils.SPUtil;
import com.freevisiontech.fvmobile.widget.ActivateDialog;
import com.freevisiontech.fvmobile.widget.CustomToast;
import com.freevisiontech.fvmobile.widget.GuideView;
import com.freevisiontech.fvmobile.widget.SelfDialog;
import com.freevisiontech.fvmobile.widget.ToastDialog;
import com.freevisiontech.fvmobile.widget.ToastForceDialog;
import com.freevisiontech.fvmobile.widget.WirelessChargeSupportDialog;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.pro.C0217dk;
import com.vise.log.ViseLog;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

public class FVMainActivity extends BaseActivity {
    private static final int COMPARE_TO_GMU_AND_IMU_VERSION = 10002;
    private static final int GET_PTZ_GMU_AND_IMU_VERSION = 10001;
    private static final int REQUEST_AUDIO_PERMISSION = 11;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    public static final String TAG = "FVMainActivity";
    private final int SCREEN_BRIGHTNESS_30S_TO_DARK = 106;
    private final int TOAST_SLEEP_DIALOG_M = 103;
    private final int TOAST_SLEEP_DIALOG_MPRO = 105;
    private final int TOAST_SLEEP_DIALOG_S = 101;
    private final int TOAST_STRESS_ANOMALY_DIALOG_MPRO = 104;
    private final int TOAST_STRESS_ANOMALY_DIALOG_S = 102;
    /* access modifiers changed from: private */
    public ActivateVerify activateVerify;
    /* access modifiers changed from: private */
    public Activity activity;
    boolean boDown = false;
    boolean boLeft = false;
    boolean boRight = false;
    boolean boTop = false;
    /* access modifiers changed from: private */
    public Button btn_next_page;
    /* access modifiers changed from: private */
    public TreeSet<Byte> changeSet;
    /* access modifiers changed from: private */
    public boolean connected = false;
    /* access modifiers changed from: private */
    public TreeSet<Byte> constantSet;
    private CustomToast customToast;
    private Queue<Byte> dataInfoQueue = new LinkedList();
    private boolean firstLaunch = true;
    /* access modifiers changed from: private */
    public String gmuAssetsVersion = "";
    /* access modifiers changed from: private */
    public String gmuServerVersion = "";
    /* access modifiers changed from: private */
    public int guideStep = 0;
    /* access modifiers changed from: private */
    public GuideView guideView;
    /* access modifiers changed from: private */
    public String imuAssetsVersion = "";
    /* access modifiers changed from: private */
    public String imuServerVersion = "";
    private GestureDetector mDetector;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (FVMainActivity.this.changeSet.size() != FVMainActivity.this.constantSet.size()) {
                        Iterator it = FVMainActivity.this.constantSet.iterator();
                        while (it.hasNext()) {
                            Byte index = (Byte) it.next();
                            if (!FVMainActivity.this.changeSet.contains(index)) {
                                FVMainActivity.this.requreSet.add(index);
                            }
                        }
                        FVMainActivity.this.changeSet.clear();
                        FVMainActivity.this.constantSet.clear();
                        TreeSet unused = FVMainActivity.this.changeSet = null;
                        TreeSet unused2 = FVMainActivity.this.constantSet = null;
                        if (FVMainActivity.this.requreSet.size() > 0) {
                            Iterator it2 = FVMainActivity.this.requreSet.iterator();
                            while (it2.hasNext()) {
                                Byte index2 = (Byte) it2.next();
                                BleByteUtil.getPTZSingleParameters(index2.byteValue());
                                ViseLog.m1466e("需要重新请求的参数1===" + index2);
                                SystemClock.sleep(10);
                            }
                            FVMainActivity.this.requreSet.clear();
                            return;
                        }
                        ViseLog.m1466e("所有参数全部正确1");
                        return;
                    } else if (FVMainActivity.this.requreSet.size() > 0) {
                        Iterator it3 = FVMainActivity.this.requreSet.iterator();
                        while (it3.hasNext()) {
                            Byte index3 = (Byte) it3.next();
                            BleByteUtil.getPTZSingleParameters(index3.byteValue());
                            ViseLog.m1466e("需要重新请求的参数2===" + index3);
                            SystemClock.sleep(10);
                        }
                        FVMainActivity.this.requreSet.clear();
                        return;
                    } else {
                        ViseLog.m1466e("所有参数全部正确2");
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private Handler mHandlerDialog = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    FVMainActivity.this.showToastForceDialog(FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_sleep_s), FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_s));
                    return;
                case 102:
                    FVMainActivity.this.showToastForceDialog(FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_sleep_s), FVMainActivity.this.getString(C0853R.string.label_ptz_force_body_s));
                    return;
                case 103:
                    FVMainActivity.this.showToastForceDialog(FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_sleep_s), FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy));
                    return;
                case 104:
                    FVMainActivity.this.showToastForceDialog(FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_sleep_s), FVMainActivity.this.getString(C0853R.string.label_ptz_force_body_mpro));
                    return;
                case 105:
                    FVMainActivity.this.showToastForceDialog(FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_sleep_s), FVMainActivity.this.getString(C0853R.string.label_ptz_motor_dormancy_mpro));
                    return;
                case 106:
                    int unused = FVMainActivity.this.screenBrightness = FVMainActivity.this.getScreenBrightness();
                    int permSystemSetting = ((Integer) SPUtil.getParam(FVMainActivity.this.activity, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
                    int timeLapseLongLight = ((Integer) SPUtil.getParam(FVMainActivity.this.activity, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue();
                    if (permSystemSetting == 107768 && timeLapseLongLight == 107770 && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 0) {
                        FVMainActivity.this.saveScreenBrightnessMin();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private String mImuFirmwareVersion = "";
    private boolean mIsLeftDisplayed = false;
    private boolean mIsRightDisplayed = false;
    /* access modifiers changed from: private */
    public int mUpgradeType = 1;
    private MyGestureListener mgListener;
    /* access modifiers changed from: private */
    public ToastDialog myDialogToast;
    private int notifyCount = 0;
    float rangeDown;
    float rangeLeft;
    float rangeRight;
    float rangeTop;
    /* access modifiers changed from: private */
    public TreeSet<Byte> requreSet;
    private Runnable runnable = new Runnable() {
        public void run() {
            FVMainActivity.this.send();
        }
    };
    /* access modifiers changed from: private */
    public int screenBrightness;
    /* access modifiers changed from: private */
    public SelfDialog selfDialog;
    private SelfDialog selfDialogKnow;
    /* access modifiers changed from: private */
    public ToastForceDialog toastForceDialog;

    static /* synthetic */ int access$2408(FVMainActivity x0) {
        int i = x0.guideStep;
        x0.guideStep = i + 1;
        return i;
    }

    public void setScrennManualMode() {
        ContentResolver contentResolver = this.activity.getContentResolver();
        try {
            if (Settings.System.getInt(contentResolver, "screen_brightness_mode") == 1) {
                Settings.System.putInt(contentResolver, "screen_brightness_mode", 0);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setScrennAutomaticMode() {
        ContentResolver contentResolver = this.activity.getContentResolver();
        try {
            if (Settings.System.getInt(contentResolver, "screen_brightness_mode") == 0) {
                Settings.System.putInt(contentResolver, "screen_brightness_mode", 1);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public int getScreenBrightness() {
        return Settings.System.getInt(this.activity.getContentResolver(), "screen_brightness", 125);
    }

    /* access modifiers changed from: private */
    public void saveScreenBrightnessMin() {
        setScrennManualMode();
        Settings.System.putInt(this.activity.getContentResolver(), "screen_brightness", 0);
    }

    private void saveScreenBrightnessNum(int value) {
        setScrennManualMode();
        Settings.System.putInt(this.activity.getContentResolver(), "screen_brightness", value);
    }

    /* access modifiers changed from: private */
    public void send() {
        if (this.dataInfoQueue == null) {
            return;
        }
        if (this.dataInfoQueue.peek() != null) {
            Byte poll = this.dataInfoQueue.poll();
            BleByteUtil.getPTZSingleParameters(poll.byteValue());
            ViseLog.m1466e("send:" + poll);
            this.mHandler.postDelayed(this.runnable, 50);
            return;
        }
        ViseLog.m1466e("send over");
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            BleByteUtil.controlDefaultCamSwitch((byte) 87, 0);
            checkVersion(1);
        }
        checkActivateState(BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS);
        this.dataInfoQueue = null;
        this.mHandler.sendEmptyMessageDelayed(100, 300);
    }

    public Queue<Byte> splitQueue(TreeSet<Byte> data) {
        Queue<Byte> dataInfoQueue2 = new LinkedList<>();
        if (data != null) {
            Iterator<Byte> it = data.iterator();
            while (it.hasNext()) {
                dataInfoQueue2.offer(it.next());
            }
        }
        return dataInfoQueue2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0853R.layout.activity_main);
        ViseLog.m1467e(TAG, "onCreate");
        Util.setFullScreen(this);
        getWindow().addFlags(128);
        initView();
        this.activity = this;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVMainActivity.this.initData();
            }
        }, 200);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int visibility) {
                Util.setFullScreen(FVMainActivity.this);
            }
        });
        CameraUtils.setBosIsResume(true);
        BaseActivityManager.getActivityManager().pushActivity(this);
        this.mgListener = new MyGestureListener();
        this.mDetector = new GestureDetector(this, this.mgListener);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            CameraUtils.setFrameLayerNumber(0);
        }
        this.screenBrightness = getScreenBrightness();
    }

    private void checkActivateState(int activateStatus) {
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) {
            if (activateStatus == 2) {
                showActivateDialog();
            } else if (activateStatus == 3) {
                hideActivate();
            }
        } else if (ptzType.equals("") && activateStatus == 3) {
            hideActivate();
        }
    }

    private void hideActivate() {
        String ptzSnCode = BlePtzParasConstant.GET_PTZ_SN_CODE;
        String ptzMac = (String) SPUtils.get(this, SharePrefConstant.CURRENT_PTZ_MAC, "");
        if (ptzSnCode != null && !ptzSnCode.equals("") && ptzMac != null && ptzMac != "") {
            ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(ActivateDialog.URL_ACTIVATE_STEP_ONE).tag(this)).headers("charset", "UTF-8")).params("sn_code", ptzSnCode, new boolean[0])).params("mac_address", ptzMac.replace(":", ""), new boolean[0])).execute(new StringCallback() {
                public void onSuccess(Response<String> response) {
                    try {
                        String jsonStr = new JsonParser().parse(response.body()).getAsString();
                        ActivateVerify unused = FVMainActivity.this.activateVerify = (ActivateVerify) new Gson().fromJson(jsonStr, ActivateVerify.class);
                        if (FVMainActivity.this.activateVerify.getCode().equals("1000") && FVMainActivity.this.activateVerify != null) {
                            BleByteUtil.setPTZParameters((byte) 58, FVMainActivity.this.formatCmd(FVMainActivity.this.activateVerify));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onError(Response<String> response) {
                    super.onError(response);
                }
            });
        }
    }

    private void affirmActivate(String result) {
        ((PostRequest) ((PostRequest) ((PostRequest) ((PostRequest) OkGo.post(ActivateDialog.URL_ACTIVATE_STEP_TWO).tag(this)).headers("charset", "UTF-8")).params("activation_id", this.activateVerify.getActivationId(), new boolean[0])).params("results", result, new boolean[0])).execute(new StringCallback() {
            public void onSuccess(Response<String> response) {
            }

            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }

    /* access modifiers changed from: private */
    public byte[] formatCmd(ActivateVerify activateVerify2) {
        byte[] activateCmd = new byte[20];
        activateCmd[0] = 1;
        int sum = 0;
        byte[] randomCmdList = HexUtil.StringToBytes(activateVerify2.getRandom());
        byte[] encodeCmdList = HexUtil.StringToBytes(activateVerify2.getResult());
        for (int i = 0; i < randomCmdList.length; i++) {
            activateCmd[i + 1] = randomCmdList[i];
            sum = i + 2;
        }
        for (int j = 0; j < encodeCmdList.length; j++) {
            activateCmd[sum + j] = encodeCmdList[j];
        }
        return activateCmd;
    }

    private void showActivateDialog() {
        ActivateDialog activateDialog = new ActivateDialog(this.activity);
        activateDialog.show();
        Display d = this.activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams params = activateDialog.getWindow().getAttributes();
        params.width = (int) (((double) d.getHeight()) * 0.6d);
        params.height = (int) (((double) d.getWidth()) * 0.5d);
        params.gravity = 1;
        params.gravity = 16;
        activateDialog.getWindow().setAttributes(params);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        float lastDistance;

        private MyGestureListener() {
            this.lastDistance = -1.0f;
        }

        public boolean onDown(MotionEvent motionEvent) {
            this.lastDistance = -1.0f;
            return false;
        }

        public void onShowPress(MotionEvent motionEvent) {
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (CameraUtils.getCameraHandModel()) {
                return false;
            }
            Util.sendIntEventMessge((int) Constants.CAMERA_GESTURE_ON_SINGLE_TAP_UP, motionEvent);
            Point point = new Point();
            point.set((int) motionEvent.getX(), (int) motionEvent.getY());
            CameraUtils.setOnDownOneFingerMotionEvent(point);
            CameraUtils.setOnDownMotionEvent(motionEvent);
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float v, float v1) {
            if (CameraUtils.getCameraHandModel() || e2 == null) {
                return false;
            }
            int pointCount = e2.getPointerCount();
            if (pointCount == 2) {
                float deltaX = e2.getX(1) - e2.getX(0);
                float deltaY = e2.getY(1) - e2.getY(0);
                float distance = (float) Math.sqrt((double) ((deltaX * deltaX) + (deltaY * deltaY)));
                if (this.lastDistance < 0.0f) {
                    this.lastDistance = distance;
                    return false;
                } else if (distance - this.lastDistance > 10.0f) {
                    Log.d(FVMainActivity.TAG, "放大   放大   放大   放大   放大");
                    Util.sendIntEventMessge((int) Constants.CAMERA_GESTURE_TWO_FINGER_SCROLL, e2);
                    return false;
                } else if (distance - this.lastDistance >= 10.0f) {
                    return false;
                } else {
                    Log.d(FVMainActivity.TAG, "缩小   缩小   缩小   缩小   缩小");
                    Util.sendIntEventMessge((int) Constants.CAMERA_GESTURE_TWO_FINGER_SCROLL, e2);
                    return false;
                }
            } else if (pointCount != 1) {
                return false;
            } else {
                CameraUtils.setGestureDownMotionEvent(e1);
                Log.d(FVMainActivity.TAG, "一个手指   滑动   滑动   滑动   滑动");
                MotionEvent event1 = e1;
                MotionEvent event2 = e2;
                Log.d(FVMainActivity.TAG, "x坐标：" + String.valueOf(event1.getX()) + "," + String.valueOf(event2.getX()));
                Log.d(FVMainActivity.TAG, "x坐标差值:" + String.valueOf(event1.getX() - event2.getX()));
                Log.d(FVMainActivity.TAG, "y坐标：" + String.valueOf(event1.getY()) + "," + String.valueOf(event2.getY()));
                Log.d(FVMainActivity.TAG, "y坐标差值:" + String.valueOf(event1.getY() - event2.getY()));
                FVMainActivity.this.boTop = false;
                FVMainActivity.this.boDown = false;
                FVMainActivity.this.boLeft = false;
                FVMainActivity.this.boRight = false;
                FVMainActivity.this.rangeTop = 0.0f;
                FVMainActivity.this.rangeDown = 0.0f;
                FVMainActivity.this.rangeLeft = 0.0f;
                FVMainActivity.this.rangeRight = 0.0f;
                if (event1.getY() - event2.getY() > 10.0f) {
                    Log.d(FVMainActivity.TAG, "向上滑动  向上滑动  向上滑动 ");
                    FVMainActivity.this.boTop = true;
                    FVMainActivity.this.rangeTop = event1.getY() - event2.getY();
                }
                if (event2.getY() - event1.getY() > 10.0f) {
                    Log.d(FVMainActivity.TAG, "向下滑动  向下滑动  向下滑动 ");
                    FVMainActivity.this.boDown = true;
                    FVMainActivity.this.rangeDown = event2.getY() - event1.getY();
                }
                if (event1.getX() - event2.getX() > 10.0f) {
                    Log.d(FVMainActivity.TAG, "向左滑动  向左滑动  向左滑动 ");
                    FVMainActivity.this.boLeft = true;
                    FVMainActivity.this.rangeLeft = event1.getX() - event2.getX();
                }
                if (event2.getX() - event1.getX() > 10.0f) {
                    Log.d(FVMainActivity.TAG, "向右滑动  向右滑动  向右滑动 ");
                    FVMainActivity.this.boRight = true;
                    FVMainActivity.this.rangeRight = event2.getX() - event1.getX();
                }
                if (FVMainActivity.this.boTop) {
                    if (FVMainActivity.this.rangeLeft == 0.0f) {
                        if (FVMainActivity.this.rangeTop > FVMainActivity.this.rangeRight) {
                            Log.d(FVMainActivity.TAG, "向上滑动  向上滑动  向上滑动   最终  最终  最终");
                            Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_TOP);
                            return false;
                        }
                        Log.d(FVMainActivity.TAG, "向右滑动  向右滑动  向右滑动   最终  最终  最终");
                        Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_RIGHT);
                        return false;
                    } else if (FVMainActivity.this.rangeTop > FVMainActivity.this.rangeLeft) {
                        Log.d(FVMainActivity.TAG, "向上滑动  向上滑动  向上滑动   最终  最终  最终");
                        Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_TOP);
                        return false;
                    } else {
                        Log.d(FVMainActivity.TAG, "向左滑动  向左滑动  向左滑动   最终  最终  最终");
                        Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_LEFT);
                        return false;
                    }
                } else if (FVMainActivity.this.rangeLeft == 0.0f) {
                    if (FVMainActivity.this.rangeDown > FVMainActivity.this.rangeRight) {
                        Log.d(FVMainActivity.TAG, "向下滑动  向下滑动  向下滑动   最终  最终  最终");
                        Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_DOWN);
                        return false;
                    }
                    Log.d(FVMainActivity.TAG, "向右滑动  向右滑动  向右滑动   最终  最终  最终");
                    Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_RIGHT);
                    return false;
                } else if (FVMainActivity.this.rangeDown > FVMainActivity.this.rangeLeft) {
                    Log.d(FVMainActivity.TAG, "向下滑动  向下滑动  向下滑动   最终  最终  最终");
                    Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_DOWN);
                    return false;
                } else {
                    Log.d(FVMainActivity.TAG, "向左滑动  向左滑动  向左滑动   最终  最终  最终");
                    Util.sendIntEventMessge(Constants.CAMERA_GESTURE_ONE_FINGER_SCROLL_LEFT);
                    return false;
                }
            }
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (!CameraUtils.getCameraHandModel()) {
                Util.sendIntEventMessge((int) Constants.CAMERA_GESTURE_ON_LONG_PRESS, motionEvent);
            }
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    }

    private int checkScreenRotate() {
        try {
            return Settings.System.getInt(getContentResolver(), "accelerometer_rotation");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().replace(C0853R.C0855id.container, new FVMainFragment(), "mainFragment").commit();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int i = newConfig.orientation;
        getResources().getConfiguration();
        if (i == 1) {
            setRequestedOrientation(0);
            return;
        }
        int i2 = newConfig.orientation;
        getResources().getConfiguration();
        if (i2 == 2) {
            setRequestedOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        CameraUtils.setBosIsResume(true);
    }

    /* access modifiers changed from: private */
    public void initData() {
        this.customToast = new CustomToast(this);
        this.connected = ViseBluetooth.getInstance().isConnected();
        if (this.connected) {
            initSet();
            this.mHandler.postDelayed(this.runnable, 50);
        }
    }

    private void initSet() {
        this.constantSet = new TreeSet<>();
        this.changeSet = new TreeSet<>();
        this.requreSet = new TreeSet<>();
        this.constantSet.add(Byte.valueOf(ClosedCaptionCtrl.MISC_CHAN_2));
        this.constantSet.add((byte) 29);
        this.constantSet.add((byte) 30);
        this.constantSet.add(Byte.valueOf(ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS));
        this.constantSet.add((byte) 58);
        this.constantSet.add((byte) 59);
        this.dataInfoQueue = splitQueue(this.constantSet);
        BleByteUtil.getPTZSingleParameters((byte) 26);
        BleByteUtil.getPTZSingleParameters((byte) 27);
    }

    /* access modifiers changed from: protected */
    public boolean isRegisterEventBus() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 19:
                setScreenBrightnessTimeToDark();
                return;
            case 20:
                stopScreenBrightnessTimeToDark();
                return;
            case 21:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_permission_system_write_setting_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_permission_system_write_setting_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 34:
                this.connected = false;
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    Toast.makeText(this, getResources().getString(C0853R.string.lable_bluetooth_is_disconnected), 0).show();
                } else if (this.selfDialog == null) {
                    Toast.makeText(this, getResources().getString(C0853R.string.lable_bluetooth_is_disconnected), 0).show();
                } else if (!this.selfDialog.isShowing()) {
                    Toast.makeText(this, getResources().getString(C0853R.string.lable_bluetooth_is_disconnected), 0).show();
                } else {
                    this.selfDialog.dismiss();
                }
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    toastAboutMarkPointCancelTV();
                }
                CameraUtils.setFullShotIng(false);
                if (CameraUtils.getBosIsResume()) {
                    Util.sendIntEventMessge(Constants.FLASH_AUTO_RESET);
                }
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraTrackingStart() == 1) {
                    if (this.connected) {
                        BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                    }
                    if (CameraUtils.isFollowIng()) {
                        Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF_CONNECT);
                    }
                }
                String str = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    EventBusUtil.sendEvent(new Event(129));
                }
                BlePtzParasConstant.restoreFactorySettings();
                if (this.selfDialog != null && this.selfDialog.isShowing() && this.selfDialog.mProgressbar.getProgress() != 0) {
                    this.selfDialog.dismiss();
                    return;
                }
                return;
            case 49:
                if (this.activateVerify != null) {
                    affirmActivate("10");
                    return;
                }
                return;
            case 50:
            case 53:
                if (this.activateVerify != null) {
                    affirmActivate("30");
                    return;
                }
                return;
            case 119:
                byte[] value = (byte[]) event.getData();
                if ((value[1] & 255) != 31) {
                    this.notifyCount++;
                    if (this.notifyCount == 5) {
                        BleNotifyDataUtil.getInstance();
                        BleNotifyDataUtil.resetMap();
                        this.notifyCount = 0;
                    }
                    ViseLog.m1466e("mainActivity:byte[]:" + Arrays.toString(value) + " ,hex: " + HexUtil.encodeHexStr(value));
                }
                if ((value[0] & 255) == 163) {
                    getAllParaAndCheck(value);
                    if ((value[1] & 255) == 37) {
                        EventBusUtil.sendEvent(new Event(136));
                    }
                }
                if ((value[0] & 255) == 165) {
                    settingAndToastFromPhone(value);
                }
                if ((value[0] & 255) == 90) {
                    settingAndActFromPtz(value);
                }
                if ((value[0] & 255) == 85) {
                    ptzStatuChangeAndToast(value);
                }
                if ((value[1] & 255) == 31 || (value[0] & 255) == 90) {
                }
                if ((value[0] & 255) == 90) {
                    if ((value[1] & 255) != 25) {
                        return;
                    }
                    if ((value[2] & 255) == 0) {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_custom_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_custom_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    } else if ((value[2] & 255) == 1) {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_walk_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_walk_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    } else if ((value[2] & 255) != 2) {
                        return;
                    } else {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_sport_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_sport_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    }
                } else if ((value[0] & 255) != 165 || (value[1] & 255) != 25) {
                    return;
                } else {
                    if ((value[2] & 255) == 0) {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_custom_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_custom_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    } else if ((value[2] & 255) == 1) {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_walk_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_walk_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    } else if ((value[2] & 255) != 2) {
                        return;
                    } else {
                        if (this.customToast != null) {
                            this.customToast.hide();
                            this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_sport_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                            return;
                        }
                        this.customToast.customToast(getString(C0853R.string.text_fv_ptz_setting_sport_select), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                        return;
                    }
                }
            case 120:
                ViseLog.m1466e("主控界面已连接");
                this.connected = true;
                initSet();
                this.mHandler.postDelayed(this.runnable, 50);
                return;
            case 131:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_running_setting_meau_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_running_setting_meau_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 132:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_is_following_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_is_following_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 133:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_running_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_running_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 134:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_mf_lens_front_mode_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_mf_lens_front_mode_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 137:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_lens_front_mode_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_lens_front_mode_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 144:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 145:
                Util.sendIntEventMessge(Constants.CAMERA_MARK_POINT_QUIT_OUT);
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_exited_mark_point_function), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_exited_mark_point_function), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 146:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 147:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_mark_point_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 148:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_parameters_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_parameters_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 149:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_parameters_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_parameters_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 150:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_trad_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 151:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_hand_model_no_support_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 152:
                if (this.customToast != null) {
                    this.customToast.hide();
                    this.customToast.customToast(getString(C0853R.string.label_camera_focus_custom_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                    return;
                }
                this.customToast.customToast(getString(C0853R.string.label_camera_focus_custom_toast), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                return;
            case 153:
                String str2 = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(101)) {
                            this.mHandlerDialog.removeMessages(101);
                        }
                        while (this.mHandlerDialog.hasMessages(102)) {
                            this.mHandlerDialog.removeMessages(102);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessage(101);
                    return;
                } else if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(105)) {
                            this.mHandlerDialog.removeMessages(105);
                        }
                        while (this.mHandlerDialog.hasMessages(104)) {
                            this.mHandlerDialog.removeMessages(104);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessage(105);
                    return;
                } else {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(103)) {
                            this.mHandlerDialog.removeMessages(103);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessage(103);
                    return;
                }
            default:
                return;
        }
    }

    private void ptzStatuChangeAndToast(byte[] value) {
        Map<Byte, Boolean> map = BleNotifyDataUtil.getInstance().setPtzStatusSyncNotifyData(value);
        String str = (String) SPUtils.get(this, SharePrefConstant.CURRENT_PTZ_TYPE, "");
        if (map == null) {
            return;
        }
        if (map.containsKey((byte) 1)) {
            if (map.get((byte) 1).booleanValue()) {
            }
        } else if (map.containsKey((byte) 2)) {
            if (map.get((byte) 2).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice1), C0853R.mipmap.tishi, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 3)) {
            if (map.get((byte) 3).booleanValue()) {
                String str2 = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    this.customToast.customToast(getString(C0853R.string.ptz_msg_notice2_s), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                } else {
                    this.customToast.customToast(getString(C0853R.string.ptz_msg_notice2), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                }
            }
        } else if (map.containsKey((byte) 4)) {
            if (map.get((byte) 4).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice3), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 5)) {
            Boolean bool = map.get((byte) 5);
            this.customToast.customToast(getString(C0853R.string.ptz_msg_notice4), C0853R.mipmap.tishi, C0853R.style.anim_view, 0);
        } else if (map.containsKey((byte) 6)) {
            if (map.get((byte) 6).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice5), C0853R.mipmap.tishi, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 7)) {
            if (map.get((byte) 7).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice6), C0853R.mipmap.tishi, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 8)) {
            if (map.get((byte) 8).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice7), C0853R.mipmap.cuowu, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 9)) {
            if (map.get((byte) 9).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice8), C0853R.mipmap.cuowu, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 10)) {
            if (map.get((byte) 10).booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice9), C0853R.mipmap.cuowu, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey((byte) 11)) {
            if (map.get((byte) 11).booleanValue()) {
            }
        } else if (map.containsKey((byte) 12)) {
            Boolean aBoolean = map.get((byte) 12);
            ViseLog.m1466e("0x0c boolean :" + aBoolean);
            if (aBoolean.booleanValue()) {
                this.customToast.customToast(getString(C0853R.string.ptz_msg_notice11), C0853R.mipmap.cuowu, C0853R.style.anim_view, 0);
            }
        } else if (map.containsKey(Byte.valueOf(C0217dk.f721k))) {
            if (map.get(Byte.valueOf(C0217dk.f721k)).booleanValue()) {
            }
        } else if (map.containsKey(Byte.valueOf(C0217dk.f722l))) {
            if (map.get(Byte.valueOf(C0217dk.f722l)).booleanValue()) {
            }
        } else if (map.containsKey(Byte.valueOf(C0217dk.f723m))) {
            if (map.get(Byte.valueOf(C0217dk.f723m)).booleanValue() && !this.mIsLeftDisplayed) {
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    showToastDialog(C0853R.mipmap.to_right_fm300, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                } else if (CameraUtils.getCurrentPageIndex() == 2) {
                    showToastDialog(C0853R.mipmap.to_right_fm210, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                } else {
                    showToastDialog(C0853R.mipmap.to_right, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                }
                this.mIsLeftDisplayed = true;
            }
        } else if (map.containsKey(Byte.valueOf(C0217dk.f724n))) {
            if (map.get(Byte.valueOf(C0217dk.f724n)).booleanValue() && !this.mIsRightDisplayed) {
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    showToastDialog(C0853R.mipmap.to_left_fm300, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                } else if (CameraUtils.getCurrentPageIndex() == 2) {
                    showToastDialog(C0853R.mipmap.to_left_fm210, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                } else {
                    showToastDialog(C0853R.mipmap.to_left, getString(C0853R.string.direction_of_adjustment), getString(C0853R.string.label_sure));
                }
                this.mIsRightDisplayed = true;
            }
        } else if (!map.containsKey(Byte.valueOf(ClosedCaptionCtrl.MID_ROW_CHAN_1))) {
        } else {
            if (CameraUtils.getCurrentPageIndex() == 1) {
                if (this.mHandlerDialog != null) {
                    while (this.mHandlerDialog.hasMessages(101)) {
                        this.mHandlerDialog.removeMessages(101);
                    }
                    while (this.mHandlerDialog.hasMessages(102)) {
                        this.mHandlerDialog.removeMessages(102);
                    }
                }
                this.mHandlerDialog.sendEmptyMessageDelayed(102, 2000);
            } else if (CameraUtils.getCurrentPageIndex() == 2) {
                if (this.mHandlerDialog != null) {
                    while (this.mHandlerDialog.hasMessages(105)) {
                        this.mHandlerDialog.removeMessages(105);
                    }
                    while (this.mHandlerDialog.hasMessages(104)) {
                        this.mHandlerDialog.removeMessages(104);
                    }
                }
                this.mHandlerDialog.sendEmptyMessageDelayed(104, 2000);
            } else {
                showToastForceDialog(getString(C0853R.string.label_ptz_force_title), getString(C0853R.string.label_ptz_force_body));
            }
        }
    }

    private void toastAboutMarkPointCancelTV() {
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
            EventBusUtil.sendEvent(new Event(145));
        }
    }

    private void showToastDialog(int image, String messageStr, String yesStr) {
        if (this.myDialogToast != null) {
            this.myDialogToast.dismiss();
        }
        this.myDialogToast = new ToastDialog(this);
        Window window = this.myDialogToast.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.myDialogToast.setImage(image);
        this.myDialogToast.setMessage(messageStr);
        this.myDialogToast.setDialogCancleable(false);
        this.myDialogToast.setDialogOutsideCancleable(false);
        this.myDialogToast.setYesOnclickListener(yesStr, new ToastDialog.onYesOnclickListener() {
            public void onYesClick() {
                FVMainActivity.this.myDialogToast.dismiss();
            }
        });
        this.myDialogToast.show();
    }

    /* access modifiers changed from: private */
    public void showToastForceDialog(String titleStr, String bodyStr) {
        if (this.toastForceDialog == null || !this.toastForceDialog.isShowing()) {
            this.toastForceDialog = new ToastForceDialog(this);
            Window window = this.toastForceDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = -1;
            lp.height = -2;
            window.setAttributes(lp);
            this.toastForceDialog.setDialogCancleable(false);
            this.toastForceDialog.setDialogOutsideCancleable(false);
            this.toastForceDialog.setYesOnclickListener(new ToastForceDialog.onYesOnclickListener() {
                public void onYesClick() {
                    FVMainActivity.this.toastForceDialog.dismiss();
                }
            });
            this.toastForceDialog.show();
            this.toastForceDialog.setTitleAndBodyText(titleStr, bodyStr);
        }
    }

    private void settingAndActFromPtz(byte[] value) {
        byte settingnotifydata = BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value);
        if (settingnotifydata == 5) {
            if (!((value[2] & 255) == 0 || (value[2] & 255) == 1)) {
            }
            if (ViseBluetooth.getInstance().isConnected()) {
                BleByteUtil.actPTZSettingChange(settingnotifydata);
            }
        } else if (settingnotifydata == 25) {
            if (!((value[2] & 255) == 0 || (value[2] & 255) == 1 || (value[2] & 255) != 2)) {
            }
            if (ViseBluetooth.getInstance().isConnected()) {
                BleByteUtil.actPTZSettingChange(settingnotifydata);
            }
        } else if (settingnotifydata == 16) {
            boolean connected2 = ViseBluetooth.getInstance().isConnected();
            if ((value[2] & 255) == 0) {
                if (connected2) {
                    BleByteUtil.actPTZSettingChange(settingnotifydata);
                }
                if (this.customToast != null) {
                    this.customToast.hide();
                }
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraFvShareSleep(0);
                String str = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(101)) {
                            this.mHandlerDialog.removeMessages(101);
                        }
                        while (this.mHandlerDialog.hasMessages(102)) {
                            this.mHandlerDialog.removeMessages(102);
                        }
                    }
                    if (this.toastForceDialog != null && this.toastForceDialog.isShowing()) {
                        this.toastForceDialog.dismiss();
                    }
                } else if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(105)) {
                            this.mHandlerDialog.removeMessages(105);
                        }
                        while (this.mHandlerDialog.hasMessages(104)) {
                            this.mHandlerDialog.removeMessages(104);
                        }
                    }
                    if (this.toastForceDialog != null && this.toastForceDialog.isShowing()) {
                        this.toastForceDialog.dismiss();
                    }
                } else {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(103)) {
                            this.mHandlerDialog.removeMessages(103);
                        }
                    }
                    if (this.toastForceDialog != null && this.toastForceDialog.isShowing()) {
                        this.toastForceDialog.dismiss();
                    }
                }
            } else if ((value[2] & 255) == 1) {
                MoveTimelapseUtil.getInstance();
                MoveTimelapseUtil.setCameraFvShareSleep(1);
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraTrackingStart() == 1) {
                    if (connected2) {
                        BleByteUtil.actPTZSettingChange(settingnotifydata);
                    }
                    if (CameraUtils.isFollowIng()) {
                        Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
                    }
                }
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraVideoSymbolStart() == 1) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (ViseBluetooth.getInstance().isConnected()) {
                                BleByteUtil.actPTZSettingChange(C0217dk.f724n);
                            }
                            Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_CLOSE_VIDEO);
                        }
                    }, 1500);
                }
                if (CameraUtils.isFullShotIng()) {
                    if (connected2) {
                        BleByteUtil.actPTZSettingChange(settingnotifydata);
                    }
                    CameraUtils.setFullCameraErrorCode(BleConstant.ISO);
                    Util.sendIntEventMessge(Constants.FV_CAMERA_SLEEP_STOP_FULL_SHOT);
                }
                String str2 = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(101)) {
                            this.mHandlerDialog.removeMessages(101);
                        }
                        while (this.mHandlerDialog.hasMessages(102)) {
                            this.mHandlerDialog.removeMessages(102);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessageDelayed(101, 2000);
                } else if (CameraUtils.getCurrentPageIndex() == 2) {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(105)) {
                            this.mHandlerDialog.removeMessages(105);
                        }
                        while (this.mHandlerDialog.hasMessages(104)) {
                            this.mHandlerDialog.removeMessages(104);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessageDelayed(105, 2000);
                } else {
                    if (this.mHandlerDialog != null) {
                        while (this.mHandlerDialog.hasMessages(103)) {
                            this.mHandlerDialog.removeMessages(103);
                        }
                    }
                    this.mHandlerDialog.sendEmptyMessage(103);
                }
            } else if (connected2) {
                BleByteUtil.actPTZSettingChange(settingnotifydata);
            }
        } else if (settingnotifydata == 17) {
            boolean connected3 = ViseBluetooth.getInstance().isConnected();
            if ((value[2] & 255) == 17 && connected3) {
                showToastForceDialog(getString(C0853R.string.label_ptz_force_title), getString(C0853R.string.label_ptz_force_body));
            }
        } else if (settingnotifydata == 3) {
            boolean connected4 = ViseBluetooth.getInstance().isConnected();
            if ((value[2] & 255) == 3 && connected4) {
                String str3 = (String) SPUtils.get(this.activity, SharePrefConstant.CURRENT_PTZ_TYPE, "");
                if (CameraUtils.getCurrentPageIndex() == 1) {
                    this.customToast.customToast(getString(C0853R.string.ptz_msg_notice2_s), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                } else {
                    this.customToast.customToast(getString(C0853R.string.ptz_msg_notice2), C0853R.mipmap.jinggao, C0853R.style.anim_view, 0);
                }
            }
        }
    }

    private void settingAndToastFromPhone(byte[] value) {
        byte settingnotifydata = BleNotifyDataUtil.getInstance().setPtzSettingParametersNotifyData(value);
        if (settingnotifydata != 5 ? settingnotifydata != 25 ? settingnotifydata != 44 ? settingnotifydata != 18 || (value[2] & 255) == 0 || (value[2] & 255) == 1 : (value[2] & 255) == 1 || (value[2] & 255) == 2 : (value[2] & 255) == 1 || (value[2] & 255) == 2 : (value[2] & 255) != 0 && (value[2] & 255) == 1) {
        }
        if ((value[1] & 255) != 58) {
            return;
        }
        if ((value[2] & 255) == 1) {
            EventBusUtil.sendEvent(new Event(49));
        } else if ((value[2] & 255) == 2) {
            EventBusUtil.sendEvent(new Event(50));
        } else if ((value[2] & 255) == 3) {
            EventBusUtil.sendEvent(new Event(53));
        }
    }

    private void getAllParaAndCheck(byte[] value) {
        byte b;
        byte getnotifyData = BleNotifyDataUtil.getInstance().setPtzGetParametersNotifyData(value);
        if (this.changeSet == null) {
            return;
        }
        if (getnotifyData == 28 || getnotifyData == 29 || getnotifyData == 30 || getnotifyData == 37) {
            this.changeSet.add(Byte.valueOf(getnotifyData));
            int count = ((value[18] << 0) & 255) + ((value[19] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            int mcount = 0;
            for (int i = 0; i < value.length - 2; i++) {
                if ((value[i] & 255) >= 0) {
                    b = value[i] & 255;
                } else {
                    b = value[i];
                }
                mcount += b;
            }
            if (mcount != count) {
                this.requreSet.add(Byte.valueOf(getnotifyData));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 3) {
                EventBusUtil.sendEvent(new Event(121));
            }
        } else if (resultCode == 0 && requestCode == 3) {
            EventBusUtil.sendEvent(new Event(128));
        }
    }

    private void checkVersion(int type) {
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        this.imuServerVersion = SPUtil.getParam(this, ptzType + "imuVersion", "").toString();
        this.gmuServerVersion = SPUtil.getParam(this, ptzType + "gmuVersion", "").toString();
        if (!isPtzVersionValid()) {
            return;
        }
        if (SPUtil.isEmpty(this.gmuServerVersion) || SPUtil.isEmpty(this.imuServerVersion)) {
            BleUpgradeFromAssetsUtil util = new BleUpgradeFromAssetsUtil(this);
            String assetsupdateinfoname = BleUpgradeFromAssetsUtil.UPDATEINFONAME;
            if (ptzType.equals("")) {
                assetsupdateinfoname = BleUpgradeFromAssetsUtil.UPDATEINFONAME;
            } else if (ptzType.equals(BleConstant.FM_300)) {
                assetsupdateinfoname = BleUpgradeFromAssetsUtil.UPDATEINFONAME_FM300;
            } else if (ptzType.equals(BleConstant.FM_210)) {
                assetsupdateinfoname = BleUpgradeFromAssetsUtil.UPDATEINFONAME_FM210;
            }
            UpgradeFromAssetsBean upgradeFromAssetsBean = (UpgradeFromAssetsBean) JsonUtiCls.fromJson(util.ReadAssertResource(assetsupdateinfoname), UpgradeFromAssetsBean.class);
            List<UpgradeFromAssetsBean.UpdateinfoBean> updateinfo = null;
            if (!(upgradeFromAssetsBean == null || (updateinfo = upgradeFromAssetsBean.getUpdateinfo()) == null || updateinfo.size() <= 0)) {
                this.imuAssetsVersion = updateinfo.get(0).getVersion();
                this.gmuAssetsVersion = updateinfo.get(1).getVersion();
            }
            if (this.gmuAssetsVersion.compareTo(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING) > 0) {
                String isForce = updateinfo.get(1).getIsforce();
                String verifycode = updateinfo.get(1).getVerifycode();
                String releasenotes = updateinfo.get(1).getReleasenotes();
                SPUtils.put(this, ptzType + "gmuAssetsReleasenotes", releasenotes);
                if (!SPUtil.isEmpty(isForce) && !SPUtil.isEmpty(verifycode) && !SPUtil.isEmpty(releasenotes)) {
                    byte[] assetsContents = util.getGMUBinaryFromAssert();
                    if (assetsContents == null) {
                        Toast.makeText(this, "升级文件不存在", 0).show();
                        return;
                    }
                    showAssetsUpdateDialog(assetsContents, 1, verifycode, this.gmuAssetsVersion, Integer.parseInt(isForce));
                }
            } else if (this.imuAssetsVersion.compareTo(BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING) > 0) {
                String isForce2 = updateinfo.get(0).getIsforce();
                String verifycode2 = updateinfo.get(0).getVerifycode();
                String releasenotes2 = updateinfo.get(0).getReleasenotes();
                SPUtils.put(this, ptzType + "imuAssetsReleasenotes", releasenotes2);
                if (!SPUtil.isEmpty(isForce2) && !SPUtil.isEmpty(verifycode2) && !SPUtil.isEmpty(releasenotes2)) {
                    byte[] assetsContents2 = util.getIMUBinaryFromAssert();
                    if (assetsContents2 == null) {
                        Toast.makeText(this, "升级文件不存在", 0).show();
                        return;
                    }
                    showAssetsUpdateDialog(assetsContents2, 2, verifycode2, this.imuAssetsVersion, Integer.parseInt(isForce2));
                }
            }
        } else if (this.gmuServerVersion.compareTo(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING) > 0) {
            if (BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING.equals("00.00.00.00") && !ptzType.equals("")) {
                SPUtil.setParam(this, ptzType + "gmuIsforce", BleConstant.SHUTTER);
            }
            compare2ServerAndFirmwareVersion(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING, 1);
        } else if (this.imuServerVersion.compareTo(BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING) > 0) {
            if (BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING.equals("00.00.00.00") && !ptzType.equals("")) {
                SPUtil.setParam(this, ptzType + "imuIsforce", BleConstant.SHUTTER);
            }
            compare2ServerAndFirmwareVersion(BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING, 2);
        }
    }

    private boolean isPtzVersionValid() {
        if (BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING == null || BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING == null || SPUtil.isEmpty(BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING) || SPUtil.isEmpty(BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING)) {
            return false;
        }
        return true;
    }

    private void compare2ServerAndFirmwareVersion(String version, int type) {
        String currentServerVersion;
        String isForce;
        String currentCrc;
        String filePath;
        byte[] contents;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type == 1) {
            currentServerVersion = SPUtil.getParam(this, ptzType + "gmuVersion", "").toString();
            this.gmuServerVersion = currentServerVersion;
            isForce = SPUtil.getParam(this, ptzType + "gmuIsforce", "").toString();
            currentCrc = SPUtil.getParam(this, ptzType + "gmuVerifycode", "").toString();
        } else {
            currentServerVersion = SPUtil.getParam(this, ptzType + "imuVersion", "").toString();
            this.imuServerVersion = currentServerVersion;
            isForce = SPUtil.getParam(this, ptzType + "imuIsforce", "").toString();
            currentCrc = SPUtil.getParam(this, ptzType + "imuVerifycode", "").toString();
        }
        if (isForce.equals("") || currentServerVersion.equals("") || currentCrc.equals("")) {
            continueCompareImuVersion(type);
        } else if (currentServerVersion.compareTo(version) > 0) {
            int isforce = Integer.parseInt(isForce);
            if (type == 1) {
                filePath = BleUpgradeUtil.getInstance().getFirmwarePath(BleUpgradeUtil.getInstance().getGmuImuName(1));
            } else {
                filePath = BleUpgradeUtil.getInstance().getFirmwarePath(BleUpgradeUtil.getInstance().getGmuImuName(2));
            }
            if (new File(filePath).exists()) {
                if (type == 1) {
                    contents = BleUpgradeUtil.getInstance().getGMUBinaryFromSDCard();
                } else {
                    contents = BleUpgradeUtil.getInstance().getIMUBinaryFromSDCard();
                }
                if (contents == null) {
                    continueCompareImuVersion(type);
                } else if (type == 1) {
                    if (isforce == 0) {
                        if (ptzType.equals("")) {
                            showMyDialogKnow(contents, type, currentCrc, currentServerVersion, 0);
                        } else if ((ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) && BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS != 2) {
                            showMyDialogKnow(contents, type, currentCrc, currentServerVersion, 0);
                        }
                    } else if (ptzType.equals("")) {
                        showMyDialog(contents, type, currentCrc, currentServerVersion, isforce);
                    } else if ((ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) && BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS != 2) {
                        showMyDialog(contents, type, currentCrc, currentServerVersion, isforce);
                    }
                } else if (isforce == 0) {
                    if (ptzType.equals("")) {
                        showMyDialogKnow(contents, type, currentCrc, currentServerVersion, 0);
                    } else if ((ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) && BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS != 2) {
                        showMyDialogKnow(contents, type, currentCrc, currentServerVersion, 0);
                    }
                } else if (ptzType.equals("")) {
                    showMyDialog(contents, type, currentCrc, currentServerVersion, isforce);
                } else if ((ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) && BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS != 2) {
                    showMyDialog(contents, type, currentCrc, currentServerVersion, isforce);
                }
            } else {
                continueCompareImuVersion(type);
            }
        } else {
            continueCompareImuVersion(type);
        }
    }

    private void continueCompareImuVersion(int type) {
        if (type == 1 && !this.mImuFirmwareVersion.equals("")) {
            compare2ServerAndFirmwareVersion(this.mImuFirmwareVersion, 2);
        }
    }

    public void showMyDialog(byte[] contents, int type, String crc, String version, int isForce) {
        String releasenotes;
        Log.e(TAG, "getResultStatus: 弹框");
        this.mUpgradeType = type;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type == 1) {
            releasenotes = SPUtil.getParam(this, ptzType + "gmuReleasenotes", "").toString();
        } else {
            releasenotes = SPUtil.getParam(this, ptzType + "imuReleasenotes", "").toString();
        }
        if (this.selfDialog != null) {
            this.selfDialog.dismiss();
        }
        if (this.selfDialogKnow != null) {
            this.selfDialogKnow.dismiss();
        }
        this.selfDialog = new SelfDialog(this, contents, type, crc, version, isForce);
        Window window = this.selfDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.selfDialog.setDialogCancleable(false);
        this.selfDialog.setDialogOutsideCancleable(false);
        this.selfDialog.setUpgradePoint(releasenotes);
        this.selfDialog.setYesOnclickListener(getResources().getString(C0853R.string.upgrade_now), new SelfDialog.onYesOnclickListener() {
            public void onYesClick() {
            }
        });
        this.selfDialog.setNoOnclickListener(getResources().getString(C0853R.string.upgrade_cancel), new SelfDialog.onNoOnclickListener() {
            public void onNoClick() {
                FVMainActivity.this.selfDialog.dismiss();
            }
        });
        this.selfDialog.setFinishOnclickListener(getResources().getString(C0853R.string.finish), new SelfDialog.onFinishOnclickListener() {
            public void onFinishClick(boolean isUpgradeSuccess) {
                FVMainActivity.this.selfDialog.dismiss();
                if (!isUpgradeSuccess) {
                    return;
                }
                if (FVMainActivity.this.mUpgradeType == 1) {
                    BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.gmuServerVersion;
                } else {
                    BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.imuServerVersion;
                }
            }
        });
        this.selfDialog.show();
        this.selfDialog.setForcedToUpgrade();
        if (type == 1) {
            SPUtil.setParam(this, ptzType + "gmuIsforce", "0");
        } else {
            SPUtil.setParam(this, ptzType + "imuIsforce", "0");
        }
    }

    public void showAssetsUpdateDialog(byte[] contents, int type, String crc, String version, int isForce) {
        String releasenotes;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        String gmuReleasenotes = SPUtils.get(this, ptzType + "gmuAssetsReleasenotes", "").toString();
        String imuReleasenotes = SPUtils.get(this, ptzType + "imuAssetsReleasenotes", "").toString();
        if (type == 1) {
            releasenotes = gmuReleasenotes;
        } else {
            releasenotes = imuReleasenotes;
        }
        ViseLog.m1466e("升级更新点" + releasenotes);
        this.selfDialog = new SelfDialog(this, contents, type, crc, version, isForce);
        Window window = this.selfDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.selfDialog.setDialogCancleable(false);
        this.selfDialog.setDialogOutsideCancleable(false);
        this.selfDialog.setUpgradePoint(releasenotes);
        this.selfDialog.setYesOnclickListener(getResources().getString(C0853R.string.upgrade_now), new SelfDialog.onYesOnclickListener() {
            public void onYesClick() {
            }
        });
        if (!isNeedForceUpdate(ptzType)) {
            this.selfDialog.setNoOnclickListener(getResources().getString(C0853R.string.upgrade_cancel), new SelfDialog.onNoOnclickListener() {
                public void onNoClick() {
                    FVMainActivity.this.selfDialog.dismiss();
                }
            });
        }
        this.selfDialog.setFinishOnclickListener(getResources().getString(C0853R.string.finish), new SelfDialog.onFinishOnclickListener() {
            public void onFinishClick(boolean isUpgradeSuccess) {
                FVMainActivity.this.selfDialog.dismiss();
                if (isUpgradeSuccess) {
                    BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.gmuAssetsVersion;
                    BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.imuAssetsVersion;
                }
            }
        });
        this.selfDialog.show();
    }

    private boolean isNeedForceUpdate(String ptzType) {
        if (!BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING.equals("00.00.00.00") && !BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING.equals("00.00.00.00")) {
            return false;
        }
        if ((ptzType.equals(BleConstant.FM_300) || ptzType.equals(BleConstant.FM_210)) && BlePtzParasConstant.GET_PTZ_ACTIVATE_STATUS != 2) {
            return true;
        }
        return false;
    }

    public void showMyDialogKnow(byte[] contents, int type, String crc, String version, int isForce) {
        String releasenotes;
        String ptzType = MyApplication.CURRENT_PTZ_TYPE;
        if (type == 1) {
            releasenotes = SPUtil.getParam(this, ptzType + "gmuReleasenotes", "").toString();
        } else {
            releasenotes = SPUtil.getParam(this, ptzType + "imuReleasenotes", "").toString();
        }
        ViseLog.m1466e("升级更新点" + releasenotes);
        this.selfDialog = new SelfDialog(this, contents, type, crc, version, isForce);
        Window window = this.selfDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = -1;
        lp.height = -2;
        window.setAttributes(lp);
        this.selfDialog.setDialogCancleable(false);
        this.selfDialog.setDialogOutsideCancleable(false);
        this.selfDialog.setUpgradePoint(releasenotes);
        this.selfDialog.setYesOnclickListener(getResources().getString(C0853R.string.upgrade_now), new SelfDialog.onYesOnclickListener() {
            public void onYesClick() {
            }
        });
        this.selfDialog.setNoOnclickListener(getResources().getString(C0853R.string.upgrade_cancel), new SelfDialog.onNoOnclickListener() {
            public void onNoClick() {
                FVMainActivity.this.selfDialog.dismiss();
            }
        });
        this.selfDialog.setFinishOnclickListener(getResources().getString(C0853R.string.finish), new SelfDialog.onFinishOnclickListener() {
            public void onFinishClick(boolean isUpgradeSuccess) {
                FVMainActivity.this.selfDialog.dismiss();
                if (!isUpgradeSuccess) {
                    return;
                }
                if (FVMainActivity.this.connected) {
                    if (FVMainActivity.this.mUpgradeType == 1) {
                        BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.gmuServerVersion;
                    } else {
                        BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.imuServerVersion;
                    }
                } else if (FVMainActivity.this.mUpgradeType == 1) {
                    BlePtzParasConstant.GET_GMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.gmuServerVersion;
                } else {
                    BlePtzParasConstant.GET_IMU_FIRMWARE_APP_VERSION_STRING = FVMainActivity.this.imuServerVersion;
                }
            }
        });
        this.selfDialog.show();
    }

    private void showGuideView(final int leftPosition, int rightPosition) {
        this.guideView = new GuideView(this);
        this.guideView.setLeftHintPosition(leftPosition);
        this.guideView.setRightHintPosition(rightPosition);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 1000, 24, -3);
                layoutParams.flags |= 1024;
                WindowManager mWindowManager = FVMainActivity.this.getWindowManager();
                FVMainActivity.this.guideView.setLeftContainer((RelativeLayout) FVMainActivity.this.findViewById(C0853R.C0855id.ll_root));
                FVMainActivity.this.guideView.setRightContainer((LinearLayout) FVMainActivity.this.findViewById(C0853R.C0855id.ll_layout));
                if (leftPosition == 2) {
                    FVMainActivity.this.guideView.setLeftHint(C0853R.mipmap.hint_advanced_setting);
                    FVMainActivity.this.guideView.setRightHint(C0853R.mipmap.hint_thumbnail);
                    CameraUtils.setLabelTopBarSelectMemory(1);
                    CameraUtils.setLabelTopBarSelect(1);
                    CameraUtils.setBtnSettingBgColorIsYellow(true);
                    CameraUtils.setBtnCameraBgColorIsYellow(false);
                    CameraUtils.setBtnViltaBgColorIsYellow(false);
                    CameraUtils.setBtnBackBgColorIsYellow(false);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                } else if (leftPosition == 1) {
                    FVMainActivity.this.guideView.setLeftHint(C0853R.mipmap.hint_ptz_setting);
                    FVMainActivity.this.guideView.setRightHint(C0853R.mipmap.hint_follow);
                    CameraUtils.setLabelTopBarSelectMemory(2);
                    CameraUtils.setLabelTopBarSelect(2);
                    CameraUtils.setBtnSettingBgColorIsYellow(false);
                    CameraUtils.setBtnCameraBgColorIsYellow(false);
                    CameraUtils.setBtnViltaBgColorIsYellow(true);
                    CameraUtils.setBtnBackBgColorIsYellow(false);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                } else {
                    FVMainActivity.this.guideView.setLeftHint(C0853R.mipmap.hint_camera_setting);
                    FVMainActivity.this.guideView.setRightHint(C0853R.mipmap.hint_swap);
                    CameraUtils.setLabelTopBarSelectMemory(3);
                    CameraUtils.setLabelTopBarSelect(3);
                    CameraUtils.setBtnSettingBgColorIsYellow(false);
                    CameraUtils.setBtnCameraBgColorIsYellow(true);
                    CameraUtils.setBtnViltaBgColorIsYellow(false);
                    CameraUtils.setBtnBackBgColorIsYellow(false);
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                }
                FVMainActivity.this.guideView.setSystemUiVisibility(4102);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                Button unused = FVMainActivity.this.btn_next_page = new Button(this);
                FVMainActivity.this.btn_next_page.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ViseLog.m1466e("nextStepOnClick");
                        if (FVMainActivity.this.guideStep == 0) {
                            FVMainActivity.this.guideView.setLeftHintPosition(1);
                            FVMainActivity.this.guideView.setRightHintPosition(1);
                            FVMainActivity.this.guideView.setLeftHint(C0853R.mipmap.hint_ptz_setting);
                            FVMainActivity.this.guideView.setRightHint(C0853R.mipmap.hint_follow);
                            FVMainActivity.this.btn_next_page.setBackgroundResource(C0853R.mipmap.hint_guide_view_next);
                            FVMainActivity.this.guideView.setDrawNew(true);
                            FVMainActivity.this.guideView.invalidate();
                            FVMainActivity.access$2408(FVMainActivity.this);
                            CameraUtils.setLabelTopBarSelectMemory(2);
                            CameraUtils.setLabelTopBarSelect(2);
                            CameraUtils.setBtnSettingBgColorIsYellow(false);
                            CameraUtils.setBtnCameraBgColorIsYellow(false);
                            CameraUtils.setBtnViltaBgColorIsYellow(true);
                            CameraUtils.setBtnBackBgColorIsYellow(false);
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        } else if (FVMainActivity.this.guideStep == 1) {
                            FVMainActivity.this.guideView.setLeftHintPosition(0);
                            FVMainActivity.this.guideView.setRightHintPosition(3);
                            FVMainActivity.this.guideView.setLeftHint(C0853R.mipmap.hint_camera_setting);
                            FVMainActivity.this.guideView.setRightHint(C0853R.mipmap.hint_swap);
                            FVMainActivity.this.btn_next_page.setBackgroundResource(C0853R.mipmap.hint_guide_view_start);
                            FVMainActivity.this.guideView.setDrawNew(true);
                            FVMainActivity.this.guideView.invalidate();
                            FVMainActivity.access$2408(FVMainActivity.this);
                            CameraUtils.setLabelTopBarSelectMemory(3);
                            CameraUtils.setLabelTopBarSelect(3);
                            CameraUtils.setBtnSettingBgColorIsYellow(false);
                            CameraUtils.setBtnCameraBgColorIsYellow(true);
                            CameraUtils.setBtnViltaBgColorIsYellow(false);
                            CameraUtils.setBtnBackBgColorIsYellow(false);
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        } else {
                            WindowManager mWindowManager = FVMainActivity.this.getWindowManager();
                            mWindowManager.removeView(FVMainActivity.this.btn_next_page);
                            mWindowManager.removeView(FVMainActivity.this.guideView);
                            SPUtil.setParam(this, "isFirstLaunch", false);
                            CameraUtils.setLabelTopBarSelectMemory(-1);
                            CameraUtils.setLabelTopBarSelect(-1);
                            CameraUtils.setBtnSettingBgColorIsYellow(false);
                            CameraUtils.setBtnCameraBgColorIsYellow(false);
                            CameraUtils.setBtnViltaBgColorIsYellow(false);
                            CameraUtils.setBtnBackBgColorIsYellow(false);
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        }
                    }
                });
                if (leftPosition != 0) {
                    FVMainActivity.this.btn_next_page.setBackgroundResource(C0853R.mipmap.hint_guide_view_next);
                } else {
                    FVMainActivity.this.btn_next_page.setBackgroundResource(C0853R.mipmap.hint_guide_view_start);
                }
                FVMainActivity.this.btn_next_page.setSystemUiVisibility(4102);
                params.width = -2;
                params.height = -2;
                params.gravity = 17;
                params.format = -3;
                params.type = 1002;
                mWindowManager.addView(FVMainActivity.this.btn_next_page, params);
                mWindowManager.addView(FVMainActivity.this.guideView, layoutParams);
            }
        }, 500);
    }

    private void removeGuideView() {
        WindowManager mWindowManager = getWindowManager();
        mWindowManager.removeView(this.btn_next_page);
        mWindowManager.removeView(this.guideView);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        CameraUtils.setBosIsResume(true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        CameraUtils.setBosIsResume(true);
        if (this.customToast != null) {
            this.customToast.setNeedToast(true);
        }
        MobclickAgent.onResume(this);
        if (CameraUtils.getCurrentPageIndex() != 2) {
            showGuide();
        } else if (getSupportState() == 0) {
            checkIsWirelessChargeSupport();
        } else {
            showGuide();
        }
    }

    private int getSupportState() {
        return ((Integer) SPUtil.getParam(this.activity, "save_storage_path", 0)).intValue();
    }

    private void checkIsWirelessChargeSupport() {
        WirelessChargeSupportDialog dialog = new WirelessChargeSupportDialog(this.activity);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                FVMainActivity.this.showGuide();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showGuide() {
        if (!((Boolean) SPUtil.getParam(this, "isFirstLaunch", true)).booleanValue()) {
            return;
        }
        if (this.guideView == null) {
            ViseLog.m1466e("FVMainActivityonResume guideview==null");
            showGuideView(2, 0);
            return;
        }
        ViseLog.m1466e("FVMainActivityonResume guideview!=null");
        removeGuideView();
        showGuideView(this.guideView.getLeftHintPosition(), this.guideView.getRightHintPosition());
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        boolean z = false;
        super.onPause();
        CameraUtils.setBosIsResume(false);
        if (this.customToast != null) {
            this.customToast.setNeedToast(false);
            this.customToast.cancel();
        }
        MobclickAgent.onPause(this);
        StringBuilder append = new StringBuilder().append("FVMainActivityonPause guideview!=null: ");
        if (this.guideView != null) {
            z = true;
        }
        ViseLog.m1466e(append.append(z).toString());
        MoveTimelapseUtil.getInstance();
        if (MoveTimelapseUtil.getCameraTrackingStart() == 1) {
            if (this.connected) {
                BleByteUtil.actPTZSettingChange(C0217dk.f724n);
            }
            if (CameraUtils.isFollowIng()) {
                Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        ViseLog.m1466e("MainActivity onDestroy");
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mHandlerDialog != null) {
            this.mHandlerDialog.removeCallbacksAndMessages((Object) null);
        }
        EventBusUtil.unregister(this);
        ButterKnife.unbind(this);
        CameraUtils.clearData();
        stopScreenBrightnessTimeToDark();
    }

    private String[] setReleasenotesText(String s) {
        if (s.equals("")) {
            return null;
        }
        String substring = s.substring(3, s.length());
        Log.e(TAG, "setReleasenotesText: " + substring);
        return substring.split("##");
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        setScreenBrightnessTimeToDark();
        if (((Boolean) SPUtils.get(this, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue()) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void stopScreenBrightnessTimeToDark() {
        int permSystemSetting = ((Integer) SPUtil.getParam(this.activity, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
        int timeLapseLongLight = ((Integer) SPUtil.getParam(this.activity, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue();
        if (permSystemSetting == 107768 && timeLapseLongLight == 107770 && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 0) {
            setScrennAutomaticMode();
            saveScreenBrightnessNum(this.screenBrightness);
            if (this.mHandlerDialog != null) {
                while (this.mHandlerDialog.hasMessages(106)) {
                    this.mHandlerDialog.removeMessages(106);
                }
            }
        }
    }

    private void setScreenBrightnessTimeToDark() {
        int permSystemSetting = ((Integer) SPUtil.getParam(this.activity, SharePrefConstant.PERMISSION_SYSTEM_SETTING, Integer.valueOf(Constants.PERMISSION_SYSTEM_SETTING_REMIND_OPEN))).intValue();
        int timeLapseLongLight = ((Integer) SPUtil.getParam(this.activity, SharePrefConstant.TIME_LAPSE_RECORDING_LONG_LIGHT, Integer.valueOf(Constants.TIME_LAPSE_RECORDING_LONG_LIGHT_NO))).intValue();
        if (permSystemSetting == 107768 && timeLapseLongLight == 107770 && MoveTimelapseUtil.getInstance().getCameraProgressLinear() != 0 && Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(this.activity)) {
            setScrennManualMode();
            saveScreenBrightnessNum(this.screenBrightness);
            if (this.mHandlerDialog != null) {
                while (this.mHandlerDialog.hasMessages(106)) {
                    this.mHandlerDialog.removeMessages(106);
                }
                this.mHandlerDialog.sendEmptyMessageDelayed(106, 10000);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && event.getAction() == 0) {
            return false;
        }
        if (keyCode == 25 || keyCode == 24) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        if (BlePtzParasConstant.isFirmwareUpgrading) {
            Toast.makeText(this, getString(C0853R.string.upgrading_donot_leave), 1).show();
        } else {
            super.onBackPressed();
        }
    }
}
