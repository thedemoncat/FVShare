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
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockWT;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockMf;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockMfToAf;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockWT;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVHitchCockEndPlaceLegacyPop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private BaseRVAdapter adapter;
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
    private LinearLayout horizontalScaleMf_linear;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewHitchCockWT horizontalScaleScrollViewHitchCockWT;
    private TextView horizontal_scale_mf_text_af;
    private TextView horizontal_scale_mf_text_mf;
    private TextView horizontal_scale_mf_textview;
    private boolean isBacked = false;
    private boolean isConnected = false;
    private boolean isOutOfRange = false;
    private LinearLayoutManager layoutManager;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private RelativeLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    private ArrayList<String> list;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVHitchCockEndPlaceLegacyPop.this.setHorUiZero();
                    return;
                case 11:
                    FVHitchCockEndPlaceLegacyPop.this.setHorUiNinety();
                    return;
                case 12:
                    FVHitchCockEndPlaceLegacyPop.this.setHorUiZero180();
                    return;
                case 13:
                    FVHitchCockEndPlaceLegacyPop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private HorizontalScaleScrollViewHitchCockMf scaleScrollViewHitchCockMf;
    private HorizontalScaleScrollViewHitchCockMfToAf scaleScrollViewHitchCockMfToAf;
    boolean startFirstMF = false;
    boolean startFirstMFToAF = false;
    boolean startFirstWT = false;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.context = context2;
        this.parentView = parentView2;
        this.isBacked = isBacked2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_hitch_cock_end_place_legacy_pop, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnStandard = (TextView) this.view.findViewById(C0853R.C0855id.btn_standard);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        this.horizontal_scale_mf_text_af = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_text_af);
        this.horizontal_scale_mf_text_mf = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_text_mf);
        this.horizontalScaleMf_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.horizontalScaleMf_linear);
        this.height = Util.dip2px(context2, 290.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (RelativeLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        int topRange = ((Util.getDeviceSize(context2).y - Util.dip2px(context2, 290.0f)) / 2) - Util.dip2px(context2, 10.0f);
        int dip2px = (Util.getDeviceSize(context2).x - Util.dip2px(context2, 290.0f)) / 2;
        ((LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams()).setMargins(17, topRange, 0, 0);
        initListener();
        if (this.broad != null) {
            context2.unregisterReceiver(this.broad);
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
                if (FVHitchCockEndPlaceLegacyPop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVHitchCockEndPlaceLegacyPop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(true);
        CameraUtils.setIsHitchCockRecordUI(true);
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
                FVHitchCockEndPlaceLegacyPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVHitchCockEndPlaceLegacyPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVHitchCockEndPlaceLegacyPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVHitchCockEndPlaceLegacyPop.this.sendToHandler(13);
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
        this.horizontal_scale_mf_textview = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_textview);
        this.curScrollScaleWTOld = 0;
        this.curScrollScaleWT = 0;
        this.horizontalScaleScrollViewHitchCockWT = (HorizontalScaleScrollViewHitchCockWT) this.view.findViewById(C0853R.C0855id.horizontalScaleScrollViewHitchCockWT);
        this.horizontalScaleScrollViewHitchCockWT.setOnScrollListener(new BaseScaleViewHitchCockWT.OnScrollListener() {
            public void onScaleScroll(int scale) {
                if (FVHitchCockEndPlaceLegacyPop.this.startFirstWT) {
                    double sca = (double) scale;
                    FVHitchCockEndPlaceLegacyPop.this.curScrollScaleWT = (int) sca;
                    if (FVHitchCockEndPlaceLegacyPop.this.curScrollScaleWTOld != FVHitchCockEndPlaceLegacyPop.this.curScrollScaleWT) {
                        float pro = (float) (((sca / 10.0d) / 2.0d) + 1.0d);
                        Log.e("---------------", "--------  mCountScale 9990 WT  WT sc sc sc ------  scale:   scale:   scale:  scale:" + scale + "  pro:" + pro);
                        FVHitchCockEndPlaceLegacyPop.this.cameraManager.setZoom(pro);
                        CameraUtils.setHitchCockEndPlaceWT(pro);
                    }
                    FVHitchCockEndPlaceLegacyPop.this.curScrollScaleWTOld = FVHitchCockEndPlaceLegacyPop.this.curScrollScaleWT;
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVHitchCockEndPlaceLegacyPop.this.startFirstWT = true;
                FVHitchCockEndPlaceLegacyPop.this.horizontalScaleScrollViewHitchCockWT.setCurScale((int) ((double) ((CameraUtils.getHitchCockEndPlaceWT() - 1.0f) * 10.0f * 2.0f)));
                FVHitchCockEndPlaceLegacyPop.this.cameraManager.setZoom(CameraUtils.getHitchCockEndPlaceWT());
            }
        }, 200);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                Log.e("--------------", "-------- 退出移动摄影, 返回上一级弹框 -----");
                if (this.pop != null) {
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    this.pop.dismiss();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVHitchCockEndPlaceLegacyPop  ");
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下    FVHitchCockEndPlaceLegacyPop  ");
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVHitchCockEndPlaceLegacyPop");
                if (CameraUtils.getFrameLayerNumber() == 3) {
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    hitchCockEndPlaceLegacyPopReturnClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void hitchCockEndPlaceLegacyPopReturnClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        CameraUtils.setHitchCockEndPlaceWT(1.0f);
        CameraUtils.setHitchCockEndPlaceMF(0.0d);
        showHitchCockStartPlaceLegacyPop();
    }

    private void hitchCockEndPlaceLegacyPopNextClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        showHitchCockStartingTimePop();
    }

    public void setPop(PopupWindow pop2, final FVHitchCockEndPlaceLegacyPop hitchCockPop) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVHitchCockEndPlaceLegacyPop.this.context.unregisterReceiver(FVHitchCockEndPlaceLegacyPop.this.broad);
                    EventBus.getDefault().unregister(hitchCockPop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVHitchCockEndPlaceLegacyPop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(3);
    }

    public View getView() {
        return this.view;
    }

    private void showHitchCockStartingTimePop() {
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartingTimeTradPop hitchCockStartingTimePop = new FVHitchCockStartingTimeTradPop();
        hitchCockStartingTimePop.init(this.context, this.parentView, false);
        PopupWindow pop2 = new PopupWindow(hitchCockStartingTimePop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        hitchCockStartingTimePop.setPop(pop2, hitchCockStartingTimePop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    private void showHitchCockStartPlaceLegacyPop() {
        if (this.cameraManager.getCameraManagerType() != 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartPlaceLegacyPop hitchCockPop = new FVHitchCockStartPlaceLegacyPop();
        hitchCockPop.init(this.context, this.parentView, false);
        PopupWindow pop2 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        hitchCockPop.setPop(pop2, hitchCockPop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_close:
                hitchCockEndPlaceLegacyPopReturnClick();
                return;
            case C0853R.C0855id.btn_standard:
                hitchCockEndPlaceLegacyPopNextClick();
                return;
            default:
                return;
        }
    }
}
