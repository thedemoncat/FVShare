package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockStartingTime;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockStartingTime;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p012tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

public class FVHitchCockStartingTimePop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private BaseRVAdapter adapter;
    private LinearLayout bg_hitch_cock_setting_time_linear_time;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    TextView btnStandard;
    private TextView btnTitleText;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    int curScrollScaleMf;
    int curScrollScaleMfOld;
    int curScrollScaleWT;
    int curScrollScaleWTOld;
    private int height;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewHitchCockStartingTime horizontalScaleScrollViewHitchCockStartingTime;
    private boolean isBacked = false;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private boolean isOutOfRange = false;
    private LinearLayoutManager layoutManager;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    private ArrayList<String> list;
    private LinearLayout ll_scale_hitch_cock_starting_time;
    /* access modifiers changed from: private */
    public Context mContext;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVHitchCockStartingTimePop.this.setHorUiZero();
                    return;
                case 11:
                    FVHitchCockStartingTimePop.this.setHorUiNinety();
                    return;
                case 12:
                    FVHitchCockStartingTimePop.this.setHorUiZero180();
                    return;
                case 13:
                    FVHitchCockStartingTimePop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private PopupWindow pop2;
    private boolean scaleSlide = false;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.context = context2;
        this.parentView = parentView2;
        this.isBacked = isBacked2;
        this.mContext = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_hitch_cock_setting_time_pop, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnStandard = (TextView) this.view.findViewById(C0853R.C0855id.btn_standard);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        this.height = Util.dip2px(context2, 290.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        this.ll_scale_hitch_cock_starting_time = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_scale_hitch_cock_starting_time);
        int topRange = ((Util.getDeviceSize(context2).y - Util.dip2px(context2, 290.0f)) / 2) - Util.dip2px(context2, 10.0f);
        int dip2px = (Util.getDeviceSize(context2).x - Util.dip2px(context2, 290.0f)) / 2;
        ((LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams()).setMargins(17, topRange, 0, 0);
        initListener();
        if (this.broad != null) {
            context2.unregisterReceiver(this.broad);
        }
        CameraUtils.setHitchCockEndPlacePageAppears(1);
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
                if (FVHitchCockStartingTimePop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVHitchCockStartingTimePop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(true);
        CameraUtils.setIsHitchCockRecordUI(true);
        this.bg_hitch_cock_setting_time_linear_time = (LinearLayout) this.view.findViewById(C0853R.C0855id.bg_hitch_cock_setting_time_linear_time);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (CameraUtils.getCurrentPageIndex() == 2) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FVHitchCockStartingTimePop.this.isConnected) {
                        int unused = FVHitchCockStartingTimePop.this.stirPosition = 0;
                        if (FVHitchCockStartingTimePop.this.stirPosition == 0) {
                            FVHitchCockStartingTimePop.this.setBackgroundColorSelect(FVHitchCockStartingTimePop.this.stirPosition);
                        }
                    }
                    int color = FVHitchCockStartingTimePop.this.mContext.getResources().getColor(C0853R.color.yellow_18);
                    FVHitchCockStartingTimePop.this.horizontalScaleScrollViewHitchCockStartingTime.setColor(FVHitchCockStartingTimePop.this.mContext.getResources().getColor(C0853R.color.color_black4), FVHitchCockStartingTimePop.this.mContext.getResources().getColor(C0853R.color.white));
                }
            }, 100);
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorSelect(int select) {
        switch (select) {
            case 0:
                this.ll_scale_hitch_cock_starting_time.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.rule_cheek_round_yellow_stroke_bg));
                this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 1:
                this.ll_scale_hitch_cock_starting_time.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black_20));
                this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
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
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVHitchCockStartingTimePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVHitchCockStartingTimePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVHitchCockStartingTimePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVHitchCockStartingTimePop.this.sendToHandler(13);
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
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
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
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    private void initListener() {
        this.btnClose.setOnClickListener(this);
        this.btnStandard.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        this.curScrollScaleMfOld = 0;
        this.curScrollScaleMf = 0;
        this.curScrollScaleWTOld = 0;
        this.curScrollScaleWT = 0;
        this.horizontalScaleScrollViewHitchCockStartingTime = (HorizontalScaleScrollViewHitchCockStartingTime) this.view.findViewById(C0853R.C0855id.horizontalScaleScrollViewHitchCockStartingTime);
        this.horizontalScaleScrollViewHitchCockStartingTime.setOnScrollListener(new BaseScaleViewHitchCockStartingTime.OnScrollListener() {
            public void onScaleScroll(int scale) {
                double sca = (double) scale;
                FVHitchCockStartingTimePop.this.curScrollScaleWT = (int) sca;
                CameraUtils.setHitchCockVideoTime(((int) (sca / 10.0d)) + 2);
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVHitchCockStartingTimePop.this.horizontalScaleScrollViewHitchCockStartingTime.setCurScale(0);
            }
        }, 200);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                }
                EventBus.getDefault().unregister(this);
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 33 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVHitchCockStartPlacePop  ");
                    if (!this.scaleSlide) {
                        this.stirPosition--;
                        if (this.stirPosition < 0) {
                            this.stirPosition = 0;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVHitchCockStartingTimeTradPop  210 波轮拨动向下   第一刻度尺,向右滑动   第一刻度尺,向右滑动   向右");
                        int pos = this.curScrollScaleWT + 10;
                        if (pos > 580) {
                            pos = 580;
                        }
                        this.horizontalScaleScrollViewHitchCockStartingTime.setCurScale(pos);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 33 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下    FVHitchCockStartPlacePop  ");
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 1) {
                            this.stirPosition = 1;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVHitchCockStartingTimeTradPop  210 波轮拨动向上   第一刻度尺,向左滑动   第一刻度尺,向左滑动   向左");
                        int pos2 = this.curScrollScaleWT - 10;
                        if (pos2 < 0) {
                            pos2 = 0;
                        }
                        this.horizontalScaleScrollViewHitchCockStartingTime.setCurScale(pos2);
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
                        int mStartColorPs0 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                        int color = this.mContext.getResources().getColor(C0853R.color.color_black4);
                        this.horizontalScaleScrollViewHitchCockStartingTime.setColor(mStartColorPs0, this.mContext.getResources().getColor(C0853R.color.white));
                    } else {
                        int color2 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                        this.horizontalScaleScrollViewHitchCockStartingTime.setColor(this.mContext.getResources().getColor(C0853R.color.color_black4), this.mContext.getResources().getColor(C0853R.color.white));
                    }
                }
                if (this.stirPosition == 1) {
                    this.scaleSlide = false;
                    hitchCockStartingTimePopNextClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVHitchCockStartPlacePop");
                if (CameraUtils.getFrameLayerNumber() == 33) {
                    hitchCockStartingTimePopReturnClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 33) {
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                        CameraUtils.setMoveTimelapseIng(false);
                        CameraUtils.setIsHitchCockRecordUI(false);
                    }
                    hitchCockStartingTimePopDismissReturnClick();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void hitchCockStartingTimePopDismissReturnClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
        if (this.cameraManager.getCameraManagerType() != 2) {
            CameraUtils.setHitchCockStartPlaceWT(0.0f);
            CameraUtils.setHitchCockEndPlaceWT(0.0f);
            this.cameraManager.setZoom(0.0f);
        } else if (cameraLevel == 2) {
            CameraUtils.setHitchCockStartPlaceWT(0.0f);
            CameraUtils.setHitchCockEndPlaceWT(0.0f);
            this.cameraManager.setZoom(0.0f);
        } else {
            this.cameraManager.enableMFMode(false);
            this.cameraManager.enableManualMode(false);
            CameraUtils.setHitchCockStartPlaceWT(1.0f);
            CameraUtils.setHitchCockStartPlaceMF(0.0d);
            CameraUtils.setHitchCockEndPlaceWT(1.0f);
            CameraUtils.setHitchCockEndPlaceMF(0.0d);
            CameraUtils.setHitchCockStartPlaceStringMF("AF");
            CameraUtils.setHitchCockEndPlaceStringMF("AF");
            this.cameraManager.setZoom(1.0f);
        }
    }

    private void hitchCockStartingTimePopReturnClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
        if (this.cameraManager.getCameraManagerType() != 2) {
            showHitchCockEndPlaceTradPop();
        } else if (cameraLevel == 2) {
            showHitchCockEndPlaceTradPop();
        } else {
            showHitchCockEndPlacePop();
        }
    }

    private void hitchCockStartingTimePopNextClick() {
        double startWT = (double) CameraUtils.getHitchCockStartPlaceWT();
        double startMF = CameraUtils.getHitchCockStartPlaceMF();
        double endWT = (double) CameraUtils.getHitchCockEndPlaceWT();
        double endMF = CameraUtils.getHitchCockEndPlaceMF();
        double time = (double) CameraUtils.getHitchCockVideoTime();
        Log.e("--------------", "---------  6666  7777  8888  9999  -------  startWT:" + startWT + "   startMF:" + startMF + "   endWT:" + endWT + "   endMF:" + endMF + "   time:" + time);
        this.cameraManager.setZoom((float) startWT);
        this.cameraManager.setFocusDistance((float) startMF);
        if (this.pop != null) {
            this.pop.dismiss();
        }
        CameraUtils.setMoveTimelapseIng(false);
        EventBus.getDefault().unregister(this);
        Util.sendIntEventMessge(Constants.CAMERA_HITCH_COCK_START_VIDEO);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (FVHitchCockStartingTimePop.this.cameraManager != null && FVHitchCockStartingTimePop.this.cameraManager.isMediaRecording()) {
                    Boolean connected = true;
                    FVHitchCockStartingTimePop.this.cameraManager.stopMediaRecord();
                    CameraUtils.setTimelapseIng(false);
                    if (Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING) {
                        Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = false;
                        if (connected.booleanValue()) {
                            MoveTimelapseUtil.getInstance().cancelShoot();
                        }
                        MoveTimelapseUtil.getInstance().detroy();
                        CameraUtils.setMoveTimelapseRecording(false);
                    } else if (connected.booleanValue()) {
                        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.CARRIAGE_RETURN, (byte) 0);
                    }
                    CameraUtils.setMoveOrDelayTimeLapsePath((String) null);
                    CameraUtils.setMoveOrDelayTimeLapseIng(false);
                    CameraUtils.setMoveOrDelayTimeLapseCurrentTime(0);
                    CameraUtils.setMoveOrDelayTimeLapseShutter(0.0f);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FVHitchCockStartingTimePop.this.cameraManager.setZoom(1.0f);
                            if (FVHitchCockStartingTimePop.this.cameraManager.isMaunalFocus()) {
                                FVHitchCockStartingTimePop.this.cameraManager.enableMFMode(false);
                            }
                            FVHitchCockStartingTimePop.this.cameraManager.enableManualMode(false);
                        }
                    }, 200);
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        }, (long) ((((int) time) * 1000) + IjkMediaCodecInfo.RANK_LAST_CHANCE + 3400));
    }

    public void setPop(PopupWindow pop3, final FVHitchCockStartingTimePop hitchCockStartingTimePop) {
        this.pop = pop3;
        if (pop3 != null) {
            pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVHitchCockStartingTimePop.this.context.unregisterReceiver(FVHitchCockStartingTimePop.this.broad);
                    EventBus.getDefault().unregister(hitchCockStartingTimePop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVHitchCockStartingTimePop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(33);
    }

    public View getView() {
        return this.view;
    }

    private void showHitchCockEndPlaceTradPop() {
        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
        if (this.cameraManager.getCameraManagerType() == 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) / 10.0d);
        } else {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockEndPlaceTradPop hitchCockPop = new FVHitchCockEndPlaceTradPop();
        hitchCockPop.init(this.context, this.parentView, false);
        PopupWindow pop22 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop22.setBackgroundDrawable(new ColorDrawable(0));
        pop22.setAnimationStyle(C0853R.style.popAnimation);
        pop22.setOutsideTouchable(false);
        hitchCockPop.setPop(pop22, hitchCockPop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop22.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    private void showHitchCockEndPlacePop() {
        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
        if (this.cameraManager.getCameraManagerType() != 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockEndPlacePop hitchCockPop = new FVHitchCockEndPlacePop();
        hitchCockPop.init(this.context, this.parentView, false);
        PopupWindow pop22 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop22.setBackgroundDrawable(new ColorDrawable(0));
        pop22.setAnimationStyle(C0853R.style.popAnimation);
        pop22.setOutsideTouchable(false);
        hitchCockPop.setPop(pop22, hitchCockPop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop22.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_close:
                hitchCockStartingTimePopReturnClick();
                return;
            case C0853R.C0855id.btn_standard:
                hitchCockStartingTimePopNextClick();
                return;
            default:
                return;
        }
    }
}
