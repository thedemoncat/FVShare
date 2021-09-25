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
import android.widget.RelativeLayout;
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
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockTradWT;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockTradWT;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVHitchCockStartPlaceTradPop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private BaseRVAdapter adapter;
    private LinearLayout bg_hitch_cock_start_place_trad_linear_wt;
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
    public HorizontalScaleScrollViewHitchCockTradWT horizontalScaleScrollViewHitchCockTradWT;
    private boolean isBacked = false;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private boolean isOutOfRange = false;
    private LinearLayoutManager layoutManager;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private RelativeLayout layout_camera_shortcut_pop_int_linear;
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
                    FVHitchCockStartPlaceTradPop.this.setHorUiZero();
                    return;
                case 11:
                    FVHitchCockStartPlaceTradPop.this.setHorUiNinety();
                    return;
                case 12:
                    FVHitchCockStartPlaceTradPop.this.setHorUiZero180();
                    return;
                case 13:
                    FVHitchCockStartPlaceTradPop.this.setHorUiNinety270();
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
    boolean startFirstMF = false;
    boolean startFirstMFToAF = false;
    boolean startFirstWT = false;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.context = context2;
        this.parentView = parentView2;
        this.isBacked = isBacked2;
        this.mContext = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_hitch_cock_start_place_trad_pop, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnStandard = (TextView) this.view.findViewById(C0853R.C0855id.btn_standard);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        this.height = Util.dip2px(context2, 290.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (RelativeLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
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
        CameraUtils.setHitchCockEndPlacePageAppears(0);
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
                if (FVHitchCockStartPlaceTradPop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVHitchCockStartPlaceTradPop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(true);
        CameraUtils.setIsHitchCockRecordUI(true);
        this.bg_hitch_cock_start_place_trad_linear_wt = (LinearLayout) this.view.findViewById(C0853R.C0855id.bg_hitch_cock_start_place_trad_linear_wt);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (CameraUtils.getCurrentPageIndex() == 2) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FVHitchCockStartPlaceTradPop.this.isConnected) {
                        int unused = FVHitchCockStartPlaceTradPop.this.stirPosition = 0;
                        if (FVHitchCockStartPlaceTradPop.this.stirPosition == 0) {
                            FVHitchCockStartPlaceTradPop.this.setBackgroundColorSelect(FVHitchCockStartPlaceTradPop.this.stirPosition);
                        }
                    }
                    int color = FVHitchCockStartPlaceTradPop.this.mContext.getResources().getColor(C0853R.color.yellow_18);
                    FVHitchCockStartPlaceTradPop.this.horizontalScaleScrollViewHitchCockTradWT.setColor(FVHitchCockStartPlaceTradPop.this.mContext.getResources().getColor(C0853R.color.color_black4), FVHitchCockStartPlaceTradPop.this.mContext.getResources().getColor(C0853R.color.white));
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
                FVHitchCockStartPlaceTradPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVHitchCockStartPlaceTradPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVHitchCockStartPlaceTradPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVHitchCockStartPlaceTradPop.this.sendToHandler(13);
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
        this.startFirstMF = false;
        this.startFirstMFToAF = false;
        this.startFirstWT = false;
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
        this.horizontalScaleScrollViewHitchCockTradWT = (HorizontalScaleScrollViewHitchCockTradWT) this.view.findViewById(C0853R.C0855id.horizontalScaleScrollViewHitchCockTradWT);
        this.horizontalScaleScrollViewHitchCockTradWT.setOnScrollListener(new BaseScaleViewHitchCockTradWT.OnScrollListener() {
            public void onScaleScroll(int scale) {
                if (FVHitchCockStartPlaceTradPop.this.startFirstWT) {
                    double sca = (double) scale;
                    FVHitchCockStartPlaceTradPop.this.curScrollScaleWT = (int) sca;
                    if (FVHitchCockStartPlaceTradPop.this.curScrollScaleWTOld != FVHitchCockStartPlaceTradPop.this.curScrollScaleWT) {
                        float pro = (float) (sca / 2.0d);
                        Log.e("---------------", "--------  mCountScale 9990 WT  WT sc sc sc ------  scale:   scale:   scale:  scale:" + scale + "  pro:" + pro);
                        FVHitchCockStartPlaceTradPop.this.cameraManager.setZoom(pro);
                        CameraUtils.setHitchCockStartPlaceWT(pro);
                    }
                    FVHitchCockStartPlaceTradPop.this.curScrollScaleWTOld = FVHitchCockStartPlaceTradPop.this.curScrollScaleWT;
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVHitchCockStartPlaceTradPop.this.startFirstWT = true;
                FVHitchCockStartPlaceTradPop.this.horizontalScaleScrollViewHitchCockTradWT.setCurScale((int) ((double) (CameraUtils.getHitchCockStartPlaceWT() * 2.0f)));
                FVHitchCockStartPlaceTradPop.this.cameraManager.setZoom(CameraUtils.getHitchCockStartPlaceWT());
            }
        }, 200);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 34 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVHitchCockStartPlaceTradPop  ");
                    if (!this.scaleSlide) {
                        this.stirPosition--;
                        if (this.stirPosition < 0) {
                            this.stirPosition = 0;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVTimeLapsePop  210 波轮拨动向下   第一刻度尺,向右滑动   第一刻度尺,向右滑动   向右");
                        int pos = this.curScrollScaleWT + 10;
                        int numMax = ((Util.getDrawScaleWTMax() * 2) / 10) * 10;
                        if (pos > numMax) {
                            pos = numMax;
                        }
                        this.horizontalScaleScrollViewHitchCockTradWT.setCurScale(pos);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 34 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下    FVHitchCockStartPlaceTradPop  ");
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 1) {
                            this.stirPosition = 1;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVTimeLapsePop  210 波轮拨动向上   第一刻度尺,向左滑动   第一刻度尺,向左滑动   向左");
                        int pos2 = this.curScrollScaleWT - 10;
                        if (pos2 < 0) {
                            pos2 = 0;
                        }
                        this.horizontalScaleScrollViewHitchCockTradWT.setCurScale(pos2);
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
                        this.horizontalScaleScrollViewHitchCockTradWT.setColor(mStartColorPs0, this.mContext.getResources().getColor(C0853R.color.white));
                    } else {
                        int color2 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                        this.horizontalScaleScrollViewHitchCockTradWT.setColor(this.mContext.getResources().getColor(C0853R.color.color_black4), this.mContext.getResources().getColor(C0853R.color.white));
                    }
                }
                if (this.stirPosition == 1) {
                    this.scaleSlide = false;
                    hitchCockStartPlaceTradPopNextClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVHitchCockStartPlaceTradPop");
                if (CameraUtils.getFrameLayerNumber() == 34) {
                    hitchCockStartPlaceTradPopReturnClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 34) {
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    EventBus.getDefault().unregister(this);
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    CameraUtils.setHitchCockStartPlaceWT(0.0f);
                    CameraUtils.setHitchCockEndPlaceWT(0.0f);
                    this.cameraManager.setZoom(0.0f);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void hitchCockStartPlaceTradPopReturnClick() {
        if (this.isConnected) {
            MoveTimelapseUtil.getInstance().cancelShoot();
        }
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10008);
        CameraUtils.setMoveTimelapseIng(false);
        CameraUtils.setIsHitchCockRecordUI(false);
        EventBus.getDefault().unregister(this);
        CameraUtils.setHitchCockStartPlaceWT(0.0f);
        CameraUtils.setHitchCockEndPlaceWT(0.0f);
        this.cameraManager.setZoom(0.0f);
    }

    private void hitchCockStartPlaceTradPopNextClick() {
        EventBus.getDefault().unregister(this);
        CameraUtils.setHitchCockEndPlaceWT(0.0f);
        showHitchCockEndplaceTradPop();
    }

    public void setPop(PopupWindow pop3, final FVHitchCockStartPlaceTradPop hitchCockPop) {
        this.pop = pop3;
        if (pop3 != null) {
            pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVHitchCockStartPlaceTradPop.this.context.unregisterReceiver(FVHitchCockStartPlaceTradPop.this.broad);
                    EventBus.getDefault().unregister(hitchCockPop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVHitchCockStartPlaceTradPop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(34);
    }

    public View getView() {
        return this.view;
    }

    private void showHitchCockEndplaceTradPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        if (this.cameraManager.getCameraManagerType() == 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) / 10.0d);
        } else {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockEndPlaceTradPop hitchCockEndPop = new FVHitchCockEndPlaceTradPop();
        hitchCockEndPop.init(this.context, this.parentView, false);
        PopupWindow pop22 = new PopupWindow(hitchCockEndPop.getView(), height2, height2);
        pop22.setBackgroundDrawable(new ColorDrawable(0));
        pop22.setAnimationStyle(C0853R.style.popAnimation);
        pop22.setOutsideTouchable(false);
        hitchCockEndPop.setPop(pop22, hitchCockEndPop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop22.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_close:
                hitchCockStartPlaceTradPopReturnClick();
                return;
            case C0853R.C0855id.btn_standard:
                hitchCockStartPlaceTradPopNextClick();
                return;
            default:
                return;
        }
    }
}
