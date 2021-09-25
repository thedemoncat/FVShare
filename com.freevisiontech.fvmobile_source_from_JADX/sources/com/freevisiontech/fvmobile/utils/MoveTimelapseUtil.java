package com.freevisiontech.fvmobile.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.bean.MoveTimeLapseBean;
import com.freevisiontech.fvmobile.utility.Util;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MoveTimelapseUtil {
    private static final int CAMERA_START_SHOOT_TIMEOUT = 1007;
    private static final int FREESTYLE_TOTAL_POINT_TIMEOUT = 1010;
    private static final int PTZ_CANCEL_SHOOT_TIMEOUT = 1006;
    private static final int PTZ_SEND_POINT_DATA_TOTAL_TIMEOUT = 1004;
    private static final int PTZ_START_SHOOT_TIMEOUT = 1005;
    private static final int PTZ_TAKE_MODEL_TIMEOUT = 1002;
    private static final int PTZ_TAKE_PICTRUE_TIMEOUT = 1001;
    private static boolean TimeLapseStaticOrDynamic = true;
    private static int cameraFvShareSleep = 0;
    private static int cameraSelectOneOrTwo = 0;
    private static int cameraTrackingStart = 0;
    private static int cameraVideoSymbolStart = 0;
    private static boolean deletePictrueCommun = true;
    private static boolean motionLapseTimeYesOrNo = true;
    private static MoveTimelapseUtil moveTimelapseUtil;
    private static int moveVideoAndMoveTimeVideo = 0;
    private static boolean rockerFocalLengthTureOrFalse = false;
    private static boolean takenPictrueCommun = true;
    /* access modifiers changed from: private */
    public int addOrRemovePoint = 0;
    private int cameraProgressLinear = 0;
    private int cameraProgressLinearAllTime = 0;
    /* access modifiers changed from: private */
    public int cameraStartFailCount = 0;
    private Context context;
    /* access modifiers changed from: private */
    public int fSTotalPointFailCount = 0;
    /* access modifiers changed from: private */
    public int freeStyleTotalPoint = 0;
    /* access modifiers changed from: private */
    public boolean isConnected = true;
    private int isHitchCockRecord = 0;
    private boolean isXYfinish = false;
    private float kXPer;
    private float kYPer;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    ViseLog.m1466e("MoveTimelapseUtil 移动延时摄影选择样本失败,云台通讯超时");
                    MoveTimelapseUtil.access$008(MoveTimelapseUtil.this);
                    if (MoveTimelapseUtil.this.selectPictrueFailCount > 3) {
                        int unused = MoveTimelapseUtil.this.selectPictrueFailCount = 0;
                        if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                            MoveTimelapseUtil.this.mMoveTimelapseListener.isEveryStepTimeout(0, MoveTimelapseUtil.this.addOrRemovePoint);
                            return;
                        }
                        return;
                    } else if (MoveTimelapseUtil.this.isConnected) {
                        BleByteUtil.setPTZParameters(ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS, (byte) MoveTimelapseUtil.this.selectPictrueCount, (byte) MoveTimelapseUtil.this.addOrRemovePoint);
                        if (MoveTimelapseUtil.this.mHandler != null) {
                            MoveTimelapseUtil.this.mHandler.sendEmptyMessageDelayed(1001, 3000);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                case 1002:
                    ViseLog.m1466e("MoveTimelapseUtil 移动延时摄影拍摄失败,云台回应选择模式超时");
                    return;
                case 1004:
                    ViseLog.m1466e("MoveTimelapseUtil 移动延时摄影失败,云台发送位置数据与实际不匹配超时");
                    MoveTimelapseUtil.this.map.clear();
                    if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                        MoveTimelapseUtil.this.mMoveTimelapseListener.isEveryStepTimeout(2, -1);
                        return;
                    }
                    return;
                case 1005:
                    ViseLog.m1466e("MoveTimelapseUtil 移动延时摄影失败,云台发送开始录像超时");
                    if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                        MoveTimelapseUtil.this.mMoveTimelapseListener.isEveryStepTimeout(3, -1);
                        return;
                    }
                    return;
                case 1006:
                    ViseLog.m1466e("MoveTimelapseUtil 移动延时摄影手机发起退出超时");
                    MoveTimelapseUtil.this.detroy();
                    if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                        MoveTimelapseUtil.this.mMoveTimelapseListener.isPtzCancelShootSuccess(false);
                        return;
                    }
                    return;
                case 1007:
                    ViseLog.m1466e("MoveTimelapseUtil 相机反馈,正在录像超时");
                    MoveTimelapseUtil.access$708(MoveTimelapseUtil.this);
                    if (MoveTimelapseUtil.this.cameraStartFailCount > 3) {
                        int unused2 = MoveTimelapseUtil.this.cameraStartFailCount = 0;
                        if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                            MoveTimelapseUtil.this.mMoveTimelapseListener.isEveryStepTimeout(4, -1);
                            return;
                        }
                        return;
                    } else if (MoveTimelapseUtil.this.isConnected) {
                        BleByteUtil.setPTZParameters((byte) 40, (byte) 3);
                        if (MoveTimelapseUtil.this.mHandler != null) {
                            MoveTimelapseUtil.this.mHandler.sendEmptyMessageDelayed(1007, 2000);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                case 1010:
                    ViseLog.m1466e("MoveTimelapseUtil 自由模式发送总点数超时");
                    MoveTimelapseUtil.access$808(MoveTimelapseUtil.this);
                    if (MoveTimelapseUtil.this.fSTotalPointFailCount > 3) {
                        int unused3 = MoveTimelapseUtil.this.fSTotalPointFailCount = 0;
                        if (MoveTimelapseUtil.this.mMoveTimelapseListener != null) {
                            MoveTimelapseUtil.this.mMoveTimelapseListener.isEveryStepTimeout(5, -1);
                            return;
                        }
                        return;
                    } else if (MoveTimelapseUtil.this.isConnected) {
                        BleByteUtil.sendMLFreeStyleTotalPoint((byte) 42, MoveTimelapseUtil.this.freeStyleTotalPoint);
                        if (MoveTimelapseUtil.this.mHandler != null) {
                            MoveTimelapseUtil.this.mHandler.sendEmptyMessageDelayed(1010, 2000);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public MoveTimelapseListener mMoveTimelapseListener;
    /* access modifiers changed from: private */
    public TreeMap<Integer, String> map = new TreeMap<>();
    private MoveTimeLapseBean moveTimeLapseBean;
    private ArrayList<MoveTimeLapseBean> moveTimeLapseList = new ArrayList<>();
    private boolean outOfRange = false;
    private View parentView;
    private ArrayList<String> pathList = new ArrayList<>();
    /* access modifiers changed from: private */
    public List<Integer> pointLine = new ArrayList();
    private List<Integer> pointLinePingMu = new ArrayList();
    private List<String> pointLineStatic = new ArrayList();
    /* access modifiers changed from: private */
    public int pointRemainder = 0;
    private ArrayList<Integer> pointTimeList = new ArrayList<>();
    private int selectDuration = 0;
    private int selectModelFailCount = 0;
    /* access modifiers changed from: private */
    public int selectPictrueCount = 0;
    /* access modifiers changed from: private */
    public int selectPictrueFailCount = 0;
    private int selectPictrueTotalCount = 0;
    private double selectShutter = 0.25d;
    private int selectSmoothness = 0;
    private int selectStyle = 0;

    public interface MoveTimelapseListener {
        void isAddOrRemorePictrueOk(int i, int i2, boolean z);

        void isEveryStepTimeout(int i, int i2);

        void isPtzAckShootingComeon();

        void isPtzCancelShootSuccess(boolean z);

        void isPtzDisconnected();

        void isPtzSendDataComeon(int i, int i2);

        void isPtzShootEnd(int i);

        void isPtzStartShootComeon();
    }

    static /* synthetic */ int access$008(MoveTimelapseUtil x0) {
        int i = x0.selectPictrueFailCount;
        x0.selectPictrueFailCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$708(MoveTimelapseUtil x0) {
        int i = x0.cameraStartFailCount;
        x0.cameraStartFailCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$808(MoveTimelapseUtil x0) {
        int i = x0.fSTotalPointFailCount;
        x0.fSTotalPointFailCount = i + 1;
        return i;
    }

    public static MoveTimelapseUtil getInstance() {
        if (moveTimelapseUtil == null) {
            synchronized (MoveTimelapseUtil.class) {
                if (moveTimelapseUtil == null) {
                    moveTimelapseUtil = new MoveTimelapseUtil();
                }
            }
        }
        return moveTimelapseUtil;
    }

    public void init(MoveTimelapseListener moveTimelapseListener) {
        this.mMoveTimelapseListener = moveTimelapseListener;
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void receiveEvent(com.freevisiontech.fvmobile.utils.Event r26) {
        /*
            r25 = this;
            int r20 = r26.getCode()
            switch(r20) {
                case 34: goto L_0x0008;
                case 119: goto L_0x003e;
                default: goto L_0x0007;
            }
        L_0x0007:
            return
        L_0x0008:
            java.lang.String r20 = "MoveTimelapseUtil 移动延时摄影 disconnect"
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 0
            r0 = r20
            r1 = r25
            r1.isConnected = r0
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0029
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 0
            r20.removeCallbacksAndMessages(r21)
        L_0x0029:
            r25.detroy()
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r20.isPtzDisconnected()
            goto L_0x0007
        L_0x003e:
            java.lang.Object r20 = r26.getData()
            byte[] r20 = (byte[]) r20
            r15 = r20
            byte[] r15 = (byte[]) r15
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 31
            r0 = r20
            r1 = r21
            if (r0 == r1) goto L_0x0084
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "MoveTimelapseUtil:"
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = java.util.Arrays.toString(r15)
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = " ,hex: "
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = com.freevisiontech.fvmobile.utils.HexUtil.encodeHexStr(r15)
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
        L_0x0084:
            r20 = 0
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 165(0xa5, float:2.31E-43)
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x02fd
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 37
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x01c1
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r14 = r0 & 255(0xff, float:3.57E-43)
            r20 = 3
            byte r20 = r15[r20]
            r0 = r20
            r6 = r0 & 255(0xff, float:3.57E-43)
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "MoveTimelapseUtil 回复删除  99999  33 "
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 1
            r0 = r20
            if (r6 != r0) goto L_0x01bd
            java.lang.String r20 = "MoveTimelapseUtil 回复删除  99999"
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 0
            r0 = r20
            r1 = r25
            r1.selectPictrueFailCount = r0
            int r20 = r14 + -1
            if (r20 != 0) goto L_0x01b0
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            int r21 = r14 + -1
            r20.remove(r21)
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            int r20 = r20.size()
            if (r20 <= 0) goto L_0x0007
            java.util.ArrayList r10 = new java.util.ArrayList
            r10.<init>()
            r10.clear()
            r9 = 0
        L_0x0109:
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            int r20 = r20.size()
            r0 = r20
            if (r9 >= r0) goto L_0x0148
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            r0 = r20
            java.lang.Object r20 = r0.get(r9)
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r20 = (com.freevisiontech.fvmobile.bean.MoveTimeLapseBean) r20
            java.lang.Integer r20 = r20.getXPoint()
            r0 = r20
            r10.add(r0)
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            r0 = r20
            java.lang.Object r20 = r0.get(r9)
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r20 = (com.freevisiontech.fvmobile.bean.MoveTimeLapseBean) r20
            java.lang.Integer r20 = r20.getYPoint()
            r0 = r20
            r10.add(r0)
            int r9 = r9 + 1
            goto L_0x0109
        L_0x0148:
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            r20.clear()
            r4 = 0
        L_0x0152:
            int r20 = r10.size()
            r0 = r20
            if (r4 >= r0) goto L_0x0007
            r8 = r4
            int r20 = r8 % 2
            if (r20 != 0) goto L_0x01ad
            java.lang.Object r20 = r10.get(r4)
            java.lang.Integer r20 = (java.lang.Integer) r20
            int r21 = r20.intValue()
            r20 = 0
            r0 = r20
            java.lang.Object r20 = r10.get(r0)
            java.lang.Integer r20 = (java.lang.Integer) r20
            int r20 = r20.intValue()
            int r5 = r21 - r20
            int r20 = r4 + 1
            r0 = r20
            java.lang.Object r20 = r10.get(r0)
            java.lang.Integer r20 = (java.lang.Integer) r20
            int r21 = r20.intValue()
            r20 = 1
            r0 = r20
            java.lang.Object r20 = r10.get(r0)
            java.lang.Integer r20 = (java.lang.Integer) r20
            int r20 = r20.intValue()
            int r7 = r21 - r20
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r11 = new com.freevisiontech.fvmobile.bean.MoveTimeLapseBean
            r11.<init>()
            r11.setXPoint(r5)
            r11.setYPoint(r7)
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            r0 = r20
            r0.add(r11)
        L_0x01ad:
            int r4 = r4 + 1
            goto L_0x0152
        L_0x01b0:
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            int r21 = r14 + -1
            r20.remove(r21)
            goto L_0x0007
        L_0x01bd:
            if (r6 != 0) goto L_0x0007
            goto L_0x0007
        L_0x01c1:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 38
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x022d
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x01e6
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1002(0x3ea, float:1.404E-42)
            r20.removeMessages(r21)
        L_0x01e6:
            java.lang.String r20 = "MoveTimelapseUtil 选择模式超时移除"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            int r0 = r0.selectStyle
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x021a
            r0 = r25
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r0.map
            r20 = r0
            r20.clear()
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1004(0x3ec, float:1.407E-42)
            r22 = 5000(0x1388, double:2.4703E-320)
            r20.sendEmptyMessageDelayed(r21, r22)
            goto L_0x0007
        L_0x021a:
            r0 = r25
            int r0 = r0.selectStyle
            r20 = r0
            r21 = 2
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            r25.sendEveryPointData()
            goto L_0x0007
        L_0x022d:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 40
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x02c8
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 2
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0285
            java.lang.String r20 = "MoveTimelapseUtil 相机反馈，正在录像"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x026a
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1007(0x3ef, float:1.411E-42)
            r20.removeMessages(r21)
        L_0x026a:
            r20 = 0
            r0 = r20
            r1 = r25
            r1.cameraStartFailCount = r0
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r20.isPtzAckShootingComeon()
            goto L_0x0007
        L_0x0285:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 3
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            java.lang.String r20 = "MoveTimelapseUtil 移动延时摄影手机发起退出"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x02b0
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 0
            r20.removeCallbacksAndMessages(r21)
        L_0x02b0:
            r25.detroy()
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r21 = 1
            r20.isPtzCancelShootSuccess(r21)
            goto L_0x0007
        L_0x02c8:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 42
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            java.lang.String r20 = "MoveTimelapseUtil 自由模式总点数回复"
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 0
            r0 = r20
            r1 = r25
            r1.fSTotalPointFailCount = r0
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1010(0x3f2, float:1.415E-42)
            r20.removeMessages(r21)
            goto L_0x0007
        L_0x02fd:
            r20 = 0
            byte r20 = r15[r20]
            r21 = 90
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 37
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0369
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r14 = r0 & 255(0xff, float:3.57E-43)
            r20 = 3
            byte r20 = r15[r20]
            r0 = r20
            r6 = r0 & 255(0xff, float:3.57E-43)
            java.lang.String r20 = "MoveTimelapseUtil 回复拍照"
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 2
            r0 = r20
            if (r6 != r0) goto L_0x0007
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x034a
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1001(0x3e9, float:1.403E-42)
            r20.removeMessages(r21)
        L_0x034a:
            r20 = 0
            r0 = r20
            r1 = r25
            r1.selectPictrueFailCount = r0
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r21 = 0
            r22 = 2
            r20.isEveryStepTimeout(r21, r22)
            goto L_0x0007
        L_0x0369:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 39
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x04b5
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x038e
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1002(0x3ea, float:1.404E-42)
            r20.removeMessages(r21)
        L_0x038e:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r12 = r0 & 255(0xff, float:3.57E-43)
            r20 = 3
            byte r20 = r15[r20]
            int r20 = r20 << 0
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 4
            byte r21 = r15[r21]
            int r21 = r21 << 8
            r22 = 65280(0xff00, float:9.1477E-41)
            r21 = r21 & r22
            int r13 = r20 + r21
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "MoveTimelapseUtil 点数据"
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r12)
            java.lang.String r21 = "==="
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r13)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
            r20 = 39
            r21 = 2
            byte r21 = r15[r21]
            r22 = 3
            byte r22 = r15[r22]
            r23 = 4
            byte r23 = r15[r23]
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZMoveTimeLapsePoint(r20, r21, r22, r23)
            if (r12 <= 0) goto L_0x0007
            r0 = r25
            int r0 = r0.selectPictrueCount
            r20 = r0
            r0 = r20
            if (r12 > r0) goto L_0x0007
            r0 = r25
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r0.map
            r20 = r0
            int r21 = r12 + -1
            java.lang.Integer r21 = java.lang.Integer.valueOf(r21)
            java.lang.StringBuilder r22 = new java.lang.StringBuilder
            r22.<init>()
            r0 = r22
            java.lang.StringBuilder r22 = r0.append(r13)
            java.lang.String r23 = ""
            java.lang.StringBuilder r22 = r22.append(r23)
            java.lang.String r22 = r22.toString()
            r20.put(r21, r22)
            r0 = r25
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r0.map
            r20 = r0
            int r20 = r20.size()
            r0 = r25
            java.util.ArrayList<java.lang.String> r0 = r0.pathList
            r21 = r0
            int r21 = r21.size()
            int r21 = r21 + -1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0464
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0464
            java.lang.String r20 = "MoveTimelapseUtil 移除点超时,开启云台发起录像超时"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1004(0x3ec, float:1.407E-42)
            r20.removeMessages(r21)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1005(0x3ed, float:1.408E-42)
            r20.removeMessages(r21)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1005(0x3ed, float:1.408E-42)
            r22 = 6000(0x1770, double:2.9644E-320)
            r20.sendEmptyMessageDelayed(r21, r22)
        L_0x0464:
            r0 = r25
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r0.map
            r20 = r0
            int r20 = r20.size()
            r0 = r25
            java.util.ArrayList<java.lang.String> r0 = r0.pathList
            r21 = r0
            int r21 = r21.size()
            r0 = r20
            r1 = r21
            if (r0 >= r1) goto L_0x0491
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0491
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r0 = r20
            r0.isPtzSendDataComeon(r12, r13)
        L_0x0491:
            r0 = r25
            java.util.TreeMap<java.lang.Integer, java.lang.String> r0 = r0.map
            r20 = r0
            int r20 = r20.size()
            r0 = r25
            java.util.ArrayList<java.lang.String> r0 = r0.pathList
            r21 = r0
            int r21 = r21.size()
            int r21 = r21 + -1
            r0 = r20
            r1 = r21
            if (r0 <= r1) goto L_0x0007
            java.lang.String r20 = "MoveTimelapseUtil 云台反馈点数超出实际样本点数"
            com.vise.log.ViseLog.m1466e(r20)
            goto L_0x0007
        L_0x04b5:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 40
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x06d1
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x050c
            java.lang.String r20 = "MoveTimelapseUtil 云台发起开始录像"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x04f2
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1005(0x3ed, float:1.408E-42)
            r20.removeMessages(r21)
        L_0x04f2:
            r20 = 40
            r21 = 1
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r20, r21)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r20.isPtzStartShootComeon()
            goto L_0x0007
        L_0x050c:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 3
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0556
            java.lang.String r20 = "MoveTimelapseUtil 移动延时摄影云台发起退出"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0537
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 0
            r20.removeCallbacksAndMessages(r21)
        L_0x0537:
            r20 = 40
            r21 = 3
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r20, r21)
            r25.clearAllData()
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r21 = 1
            r20.isPtzShootEnd(r21)
            goto L_0x0007
        L_0x0556:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 4
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x05d9
            java.lang.String r20 = "MoveTimelapseUtil 位置设置超出范围"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x058f
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x058f
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1001(0x3e9, float:1.403E-42)
            r20.removeMessages(r21)
        L_0x058f:
            r20 = 40
            r21 = 4
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r20, r21)
            r20 = 1
            r0 = r20
            r1 = r25
            r1.outOfRange = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x05d1
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x05d1
            java.lang.String r20 = "MoveTimelapseUtil 回调拍照正  88888"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r0 = r25
            int r0 = r0.selectPictrueCount
            r21 = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r22 = r0
            r23 = 1
            r20.isAddOrRemorePictrueOk(r21, r22, r23)
        L_0x05d1:
            r20 = 106307(0x19f43, float:1.48968E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r20)
            goto L_0x0007
        L_0x05d9:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 5
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x05f3
            java.lang.String r20 = "MoveTimelapseUtil 接收数据完成"
            com.vise.log.ViseLog.m1466e(r20)
            goto L_0x0007
        L_0x05f3:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 6
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x063d
            java.lang.String r20 = "MoveTimelapseUtil 退出延时摄影,接收数据丢失"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x061e
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 0
            r20.removeCallbacksAndMessages(r21)
        L_0x061e:
            r20 = 40
            r21 = 6
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r20, r21)
            r25.clearAllData()
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r21 = 3
            r20.isPtzShootEnd(r21)
            goto L_0x0007
        L_0x063d:
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 7
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            java.lang.String r20 = "MoveTimelapseUtil 位置设置未超出范围（解除禁用自由模式）"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0676
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x0676
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1001(0x3e9, float:1.403E-42)
            r20.removeMessages(r21)
        L_0x0676:
            r20 = 40
            r21 = 7
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZPanorama(r20, r21)
            r20 = 0
            r0 = r20
            r1 = r25
            r1.outOfRange = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r20 = r0
            r21 = 1
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x06c9
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x06c9
            getInstance()
            boolean r20 = getDeletePictrueCommun()
            if (r20 == 0) goto L_0x06c9
            getInstance()
            r20 = 0
            setDeletePictrueCommun(r20)
            java.lang.String r20 = "MoveTimelapseUtil 回调拍照正  66666"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r0 = r25
            int r0 = r0.selectPictrueCount
            r21 = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r22 = r0
            r23 = 0
            r20.isAddOrRemorePictrueOk(r21, r22, r23)
        L_0x06c9:
            r20 = 106306(0x19f42, float:1.48966E-40)
            com.freevisiontech.fvmobile.utility.Util.sendIntEventMessge(r20)
            goto L_0x0007
        L_0x06d1:
            r20 = 1
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 41
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0007
            r20 = 0
            r0 = r20
            r1 = r25
            r1.selectPictrueFailCount = r0
            r20 = 41
            r21 = 2
            byte r21 = r15[r21]
            r22 = 3
            byte r22 = r15[r22]
            r23 = 4
            byte r23 = r15[r23]
            r24 = 5
            byte r24 = r15[r24]
            com.freevisiontech.fvmobile.utils.BleByteUtil.ackPTZMoveTimeLapsePoint(r20, r21, r22, r23, r24)
            r20 = 2
            byte r20 = r15[r20]
            r0 = r20
            r14 = r0 & 255(0xff, float:3.57E-43)
            r20 = 3
            byte r20 = r15[r20]
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r16 = r0
            r0 = r25
            boolean r0 = r0.isXYfinish
            r20 = r0
            if (r20 != 0) goto L_0x0725
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r20 = new com.freevisiontech.fvmobile.bean.MoveTimeLapseBean
            r20.<init>()
            r0 = r20
            r1 = r25
            r1.moveTimeLapseBean = r0
        L_0x0725:
            if (r16 != 0) goto L_0x0794
            r20 = 4
            byte r20 = r15[r20]
            int r20 = r20 << 0
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 5
            byte r21 = r15[r21]
            int r21 = r21 << 8
            r22 = 65280(0xff00, float:9.1477E-41)
            r21 = r21 & r22
            int r17 = r20 + r21
            r20 = 32767(0x7fff, float:4.5916E-41)
            r0 = r17
            r1 = r20
            if (r0 <= r1) goto L_0x074c
            r20 = 65536(0x10000, float:9.18355E-41)
            int r17 = r17 - r20
        L_0x074c:
            r0 = r25
            boolean r0 = r0.isXYfinish
            r20 = r0
            if (r20 != 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            r0 = r20
            r1 = r17
            r0.setXPoint(r1)
            r20 = 1
            r0 = r20
            r1 = r25
            r1.isXYfinish = r0
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "moveTimeLapseBean x轴坐标正:"
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r21 = r0
            java.lang.Integer r21 = r21.getXPoint()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
            goto L_0x0007
        L_0x0794:
            r20 = 1
            r0 = r16
            r1 = r20
            if (r0 != r1) goto L_0x0881
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x07af
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1001(0x3e9, float:1.403E-42)
            r20.removeMessages(r21)
        L_0x07af:
            r20 = 4
            byte r20 = r15[r20]
            int r20 = r20 << 0
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 5
            byte r21 = r15[r21]
            int r21 = r21 << 8
            r22 = 65280(0xff00, float:9.1477E-41)
            r21 = r21 & r22
            int r18 = r20 + r21
            r20 = 32767(0x7fff, float:4.5916E-41)
            r0 = r18
            r1 = r20
            if (r0 <= r1) goto L_0x07d4
            r20 = 65536(0x10000, float:9.18355E-41)
            int r18 = r18 - r20
        L_0x07d4:
            r20 = 26224(0x6670, float:3.6748E-41)
            r0 = r18
            r1 = r20
            if (r0 <= r1) goto L_0x0875
            r18 = 26224(0x6670, float:3.6748E-41)
        L_0x07de:
            r0 = r25
            boolean r0 = r0.isXYfinish
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            r0 = r20
            r1 = r18
            r0.setYPoint(r1)
            r0 = r25
            java.util.ArrayList<com.freevisiontech.fvmobile.bean.MoveTimeLapseBean> r0 = r0.moveTimeLapseList
            r20 = r0
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r21 = r0
            r20.add(r21)
            boolean r20 = com.freevisiontech.fvmobile.utility.CameraUtils.getBlueConnectBoolean300()
            if (r20 != 0) goto L_0x0007
            r20 = 0
            r0 = r20
            r1 = r25
            r1.isXYfinish = r0
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "moveTimeLapseBean y轴坐标正:"
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r21 = r0
            java.lang.Integer r21 = r21.getYPoint()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            getInstance()
            boolean r20 = getTakenPictrueCommun()
            if (r20 == 0) goto L_0x0007
            getInstance()
            r20 = 0
            setTakenPictrueCommun(r20)
            java.lang.String r20 = "MoveTimelapseUtil 回调拍照正  77777"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r21 = r0
            r0 = r25
            boolean r0 = r0.outOfRange
            r22 = r0
            r0 = r20
            r1 = r21
            r2 = r22
            r0.isAddOrRemorePictrueOk(r14, r1, r2)
            goto L_0x0007
        L_0x0875:
            r20 = -26224(0xffffffffffff9990, float:NaN)
            r0 = r18
            r1 = r20
            if (r0 >= r1) goto L_0x07de
            r18 = -26224(0xffffffffffff9990, float:NaN)
            goto L_0x07de
        L_0x0881:
            r20 = 2
            r0 = r16
            r1 = r20
            if (r0 != r1) goto L_0x0007
            boolean r20 = com.freevisiontech.fvmobile.utility.CameraUtils.getBlueConnectBoolean300()
            if (r20 == 0) goto L_0x0007
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            if (r20 == 0) goto L_0x08a2
            r0 = r25
            android.os.Handler r0 = r0.mHandler
            r20 = r0
            r21 = 1001(0x3e9, float:1.403E-42)
            r20.removeMessages(r21)
        L_0x08a2:
            r20 = 4
            byte r20 = r15[r20]
            int r20 = r20 << 0
            r0 = r20
            r0 = r0 & 255(0xff, float:3.57E-43)
            r20 = r0
            r21 = 5
            byte r21 = r15[r21]
            int r21 = r21 << 8
            r22 = 65280(0xff00, float:9.1477E-41)
            r21 = r21 & r22
            int r19 = r20 + r21
            r20 = 32767(0x7fff, float:4.5916E-41)
            r0 = r19
            r1 = r20
            if (r0 <= r1) goto L_0x08c7
            r20 = 65536(0x10000, float:9.18355E-41)
            int r19 = r19 - r20
        L_0x08c7:
            r0 = r25
            boolean r0 = r0.isXYfinish
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            if (r20 == 0) goto L_0x0007
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r20 = r0
            java.lang.Integer r21 = java.lang.Integer.valueOf(r19)
            r20.setZPoint(r21)
            r20 = 0
            r0 = r20
            r1 = r25
            r1.isXYfinish = r0
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "moveTimeLapseBean z轴坐标正:"
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r25
            com.freevisiontech.fvmobile.bean.MoveTimeLapseBean r0 = r0.moveTimeLapseBean
            r21 = r0
            java.lang.Integer r21 = r21.getZPoint()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            if (r20 == 0) goto L_0x0007
            getInstance()
            boolean r20 = getTakenPictrueCommun()
            if (r20 == 0) goto L_0x0007
            getInstance()
            r20 = 0
            setTakenPictrueCommun(r20)
            java.lang.String r20 = "MoveTimelapseUtil 回调拍照正  77777"
            com.vise.log.ViseLog.m1466e(r20)
            r0 = r25
            com.freevisiontech.fvmobile.utils.MoveTimelapseUtil$MoveTimelapseListener r0 = r0.mMoveTimelapseListener
            r20 = r0
            r0 = r25
            int r0 = r0.addOrRemovePoint
            r21 = r0
            r0 = r25
            boolean r0 = r0.outOfRange
            r22 = r0
            r0 = r20
            r1 = r21
            r2 = r22
            r0.isAddOrRemorePictrueOk(r14, r1, r2)
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: com.freevisiontech.fvmobile.utils.MoveTimelapseUtil.receiveEvent(com.freevisiontech.fvmobile.utils.Event):void");
    }

    private void sendEveryPointData() {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.getData().getBoolean("pointdata", false)) {
                    ViseLog.m1466e("发送点数据成功");
                    MoveTimelapseUtil.getInstance().sendFreeStyleTotalPoint((MoveTimelapseUtil.this.pointLine.size() - MoveTimelapseUtil.this.pointRemainder) / 2);
                    return;
                }
                ViseLog.m1466e("发送点数据失败");
            }
        };
        new Thread(new Runnable() {
            public void run() {
                ViseLog.m1466e("循环发送点数据线程id" + Thread.currentThread().getId());
                int totalPoint = 0;
                ArrayList<Integer> list = new ArrayList<>();
                list.addAll(MoveTimelapseUtil.this.pointLine);
                int x1 = 0;
                int x2 = 0;
                int x3 = 0;
                int x4 = 0;
                int y1 = 0;
                int y2 = 0;
                int y3 = 0;
                int y4 = 0;
                if (list == null || list.size() <= 0) {
                    ViseLog.m1466e("发送点数异常消息");
                    MoveTimelapseUtil.this.sendPointDataResult(handler, false);
                    return;
                }
                while (totalPoint < list.size() && totalPoint < 10000) {
                    if (list.size() - totalPoint < 8) {
                        int unused = MoveTimelapseUtil.this.pointRemainder = list.size() % 8;
                        totalPoint += 8;
                        ViseLog.m1466e("循环发送2===" + totalPoint);
                    } else {
                        for (int i = 1; i <= 8; i++) {
                            if (i == 1) {
                                x1 = list.get(totalPoint).intValue();
                            } else if (i == 2) {
                                y1 = list.get(totalPoint + 1).intValue();
                            } else if (i == 3) {
                                x2 = list.get(totalPoint + 2).intValue();
                            } else if (i == 4) {
                                y2 = list.get(totalPoint + 3).intValue();
                            } else if (i == 5) {
                                x3 = list.get(totalPoint + 4).intValue();
                            } else if (i == 6) {
                                y3 = list.get(totalPoint + 5).intValue();
                            } else if (i == 7) {
                                x4 = list.get(totalPoint + 6).intValue();
                            } else if (i == 8) {
                                y4 = list.get(totalPoint + 7).intValue();
                            }
                        }
                        ViseLog.m1466e("point1:" + x1 + "," + y1 + "point2:" + x2 + "," + y2 + "point3:" + x3 + "," + y3 + "point4:" + x4 + "," + y4);
                        MoveTimelapseUtil.getInstance().sendFreeStyleEveryPoint(x1, y1, x2, y2, x3, y3, x4, y4);
                        totalPoint += 8;
                        SystemClock.sleep(15);
                        MoveTimelapseUtil.this.clearPointData(x1, y1, x2, y2, x3, y3, x4, y4);
                    }
                }
                ViseLog.m1466e("发送点数结束消息");
                MoveTimelapseUtil.this.sendPointDataResult(handler, true);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void sendPointDataResult(Handler handler, boolean result) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean("pointdata", result);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    /* access modifiers changed from: private */
    public void clearPointData(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
    }

    public void selectPictrueCommunication(int count, int addOrRemove) {
        if (count == 1 && addOrRemove == 0) {
            clearAllData();
        }
        this.selectPictrueCount = count;
        this.addOrRemovePoint = addOrRemove;
        if (this.isConnected) {
            BleByteUtil.setPTZParameters(ClosedCaptionCtrl.ROLL_UP_CAPTIONS_2_ROWS, (byte) count, (byte) addOrRemove);
        }
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(1001, 3000);
        }
    }

    public void selectModelCommunication(int style, int smoothness, int duration) {
        this.selectStyle = style;
        this.selectSmoothness = smoothness;
        this.selectDuration = duration;
        if (this.isConnected) {
            BleByteUtil.setMoveTimeLapse(ClosedCaptionCtrl.ROLL_UP_CAPTIONS_3_ROWS, (byte) style, (byte) smoothness, duration);
        }
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(1002, 2000);
        }
    }

    public void sendFreeStyleTotalPoint(int totalPoint) {
        this.freeStyleTotalPoint = totalPoint;
        if (this.isConnected) {
            BleByteUtil.sendMLFreeStyleTotalPoint((byte) 42, totalPoint);
        }
        if (this.mHandler != null) {
        }
    }

    public void sendFreeStyleEveryPoint(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        if (this.isConnected) {
            BleByteUtil.sendMTLFreeStyleData(x1, y1, x2, y2, x3, y3, x4, y4);
        }
    }

    public void detroy() {
        EventBus.getDefault().unregister(this);
        clearAllData();
        Util.deleteCatchPictrue();
        if (moveTimelapseUtil != null) {
            moveTimelapseUtil = null;
        }
    }

    public void cancelShoot() {
        clearAllData();
        if (this.isConnected) {
            BleByteUtil.setPTZParameters((byte) 40, (byte) 3);
        }
    }

    public void startShoot() {
        if (this.isConnected) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BleByteUtil.setPTZParameters((byte) 40, (byte) 2);
                }
            }, 50);
        }
        if (this.mHandler != null) {
            this.mHandler.sendEmptyMessageDelayed(1007, 2000);
        }
    }

    public void clearAllData() {
        this.context = null;
        this.parentView = null;
        this.selectPictrueCount = 0;
        this.selectPictrueFailCount = 0;
        this.selectModelFailCount = 0;
        this.addOrRemovePoint = 0;
        this.map.clear();
        this.selectPictrueTotalCount = 0;
        this.selectStyle = 0;
        this.selectSmoothness = 0;
        this.selectDuration = 0;
        this.cameraStartFailCount = 0;
        this.cameraProgressLinear = 0;
        this.isHitchCockRecord = 0;
        moveVideoAndMoveTimeVideo = 0;
        this.cameraProgressLinearAllTime = 0;
        this.moveTimeLapseList.clear();
        this.pathList.clear();
        this.fSTotalPointFailCount = 0;
        this.freeStyleTotalPoint = 0;
        this.pointLine.clear();
        this.pointLineStatic.clear();
        this.pointRemainder = 0;
        this.pointLinePingMu.clear();
        this.outOfRange = false;
        TimeLapseStaticOrDynamic = true;
    }

    public void setSelectPictruePathList(ArrayList<String> list) {
        this.pathList.clear();
        this.pathList.addAll(list);
        ViseLog.m1466e("MoveTimelapseUtil 样本数" + this.pathList.size());
    }

    public ArrayList<String> getSelectPictruePathList() {
        return this.pathList;
    }

    public ArrayList<MoveTimeLapseBean> getMoveTimeLapseList() {
        return this.moveTimeLapseList;
    }

    public int getSelectStyle() {
        return this.selectStyle;
    }

    public int getSelectSmoothness() {
        return this.selectSmoothness;
    }

    public void setSelectSmoothness(int selectSmoothness2) {
        this.selectSmoothness = selectSmoothness2;
    }

    public int getSelectDuration() {
        return this.selectDuration;
    }

    public double getSelectShutter() {
        return this.selectShutter;
    }

    public void setSelectShutter(double selectShutter2) {
        this.selectShutter = selectShutter2;
    }

    public int getCameraProgressLinear() {
        return this.cameraProgressLinear;
    }

    public void setCameraProgressLinear(int cameraProgressLinear2) {
        this.cameraProgressLinear = cameraProgressLinear2;
    }

    public int getIsHitchCockRecord() {
        return this.isHitchCockRecord;
    }

    public void setIsHitchCockRecord(int hitchCockRecord) {
        this.isHitchCockRecord = hitchCockRecord;
    }

    public static int getMoveVideoAndMoveTimeVideo() {
        return moveVideoAndMoveTimeVideo;
    }

    public static void setMoveVideoAndMoveTimeVideo(int moveVideoAndMoveTimeVideo2) {
        moveVideoAndMoveTimeVideo = moveVideoAndMoveTimeVideo2;
    }

    public static int getCameraFvShareSleep() {
        return cameraFvShareSleep;
    }

    public static void setCameraFvShareSleep(int cameraFvShareSleep2) {
        cameraFvShareSleep = cameraFvShareSleep2;
    }

    public static int getCameraSelectOneOrTwo() {
        return cameraSelectOneOrTwo;
    }

    public static void setCameraSelectOneOrTwo(int cameraSelectOneOrTwo2) {
        cameraSelectOneOrTwo = cameraSelectOneOrTwo2;
    }

    public static int getCameraVideoSymbolStart() {
        return cameraVideoSymbolStart;
    }

    public static void setCameraVideoSymbolStart(int cameraVideoSymbolStart2) {
        cameraVideoSymbolStart = cameraVideoSymbolStart2;
    }

    public static int getCameraTrackingStart() {
        return cameraTrackingStart;
    }

    public static void setCameraTrackingStart(int cameraTrackingStart2) {
        cameraTrackingStart = cameraTrackingStart2;
    }

    public static boolean getDeletePictrueCommun() {
        return deletePictrueCommun;
    }

    public static void setDeletePictrueCommun(boolean deletePictrueCommun2) {
        deletePictrueCommun = deletePictrueCommun2;
    }

    public static boolean getTakenPictrueCommun() {
        return takenPictrueCommun;
    }

    public static void setTakenPictrueCommun(boolean takenPictrueCommun2) {
        takenPictrueCommun = takenPictrueCommun2;
    }

    public static boolean getMotionLapseTimeYesOrNo() {
        return motionLapseTimeYesOrNo;
    }

    public static void setMotionLapseTimeYesOrNo(boolean motionLapseTimeYesOrNo2) {
        motionLapseTimeYesOrNo = motionLapseTimeYesOrNo2;
    }

    public static boolean getTimeLapseStaticOrDynamic() {
        return TimeLapseStaticOrDynamic;
    }

    public static void setTimeLapseStaticOrDynamic(boolean timeLapseStaticOrDynamic) {
        TimeLapseStaticOrDynamic = timeLapseStaticOrDynamic;
    }

    public static boolean getRockerFocalLengthTureOrFalse() {
        return rockerFocalLengthTureOrFalse;
    }

    public static void setRockerFocalLengthTureOrFalse(boolean rockerFocalLengthTureOrFalse2) {
        rockerFocalLengthTureOrFalse = rockerFocalLengthTureOrFalse2;
    }

    public int getCameraProgressLinearAllTime() {
        return this.cameraProgressLinearAllTime;
    }

    public void setCameraProgressLinearTime(int cameraProgressLinearAllTime2) {
        this.cameraProgressLinearAllTime = cameraProgressLinearAllTime2;
    }

    public List<Integer> getPointLine() {
        return this.pointLine;
    }

    public void setPointLine(List<Integer> pointLine2) {
        this.pointLine = pointLine2;
    }

    public List<String> getPointLineStatic() {
        return this.pointLineStatic;
    }

    public void setPointLineStatic(List<String> pointLineStatic2) {
        this.pointLineStatic = pointLineStatic2;
    }

    public List<Integer> getPointLinePingMu() {
        return this.pointLinePingMu;
    }

    public void setPointLinePingMu(List<Integer> pointLinePingMu2) {
        this.pointLinePingMu = pointLinePingMu2;
    }

    public View getParentView() {
        return this.parentView;
    }

    public void setParentView(View parentView2) {
        this.parentView = parentView2;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public ArrayList<Integer> getPointTimeList() {
        return this.pointTimeList;
    }

    public void setPointTimeList(ArrayList<Integer> pointTimeList2) {
        this.pointTimeList = pointTimeList2;
    }

    public TreeMap<Integer, String> getMap() {
        return this.map;
    }

    public void setMap(TreeMap<Integer, String> map2) {
        this.map = map2;
    }

    public float getkXPer() {
        return this.kXPer;
    }

    public void setkXPer(float kXPer2) {
        this.kXPer = kXPer2;
    }

    public float getkYPer() {
        return this.kYPer;
    }

    public void setkYPer(float kYPer2) {
        this.kYPer = kYPer2;
    }
}
