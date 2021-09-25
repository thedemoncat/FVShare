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
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockMf;
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockMfToAf;
import com.freevisiontech.fvmobile.widget.scale.hitch.BaseScaleViewHitchCockWT;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockMf;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockMfToAf;
import com.freevisiontech.fvmobile.widget.scale.hitch.HorizontalScaleScrollViewHitchCockWT;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVHitchCockEndPlacePop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private BaseRVAdapter adapter;
    private RelativeLayout bg_hitch_cock_setting_time_relative_mf;
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
    /* access modifiers changed from: private */
    public TextView horizontal_scale_mf_textview;
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
    private LinearLayout ll_scale_hitch_cock_end_place_wt;
    /* access modifiers changed from: private */
    public Context mContext;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVHitchCockEndPlacePop.this.setHorUiZero();
                    return;
                case 11:
                    FVHitchCockEndPlacePop.this.setHorUiNinety();
                    return;
                case 12:
                    FVHitchCockEndPlacePop.this.setHorUiZero180();
                    return;
                case 13:
                    FVHitchCockEndPlacePop.this.setHorUiNinety270();
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
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewHitchCockMf scaleScrollViewHitchCockMf;
    /* access modifiers changed from: private */
    public HorizontalScaleScrollViewHitchCockMfToAf scaleScrollViewHitchCockMfToAf;
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
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_hitch_cock_end_place_pop, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnStandard = (TextView) this.view.findViewById(C0853R.C0855id.btn_standard);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        this.horizontal_scale_mf_text_af = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_text_af);
        this.horizontal_scale_mf_text_mf = (TextView) this.view.findViewById(C0853R.C0855id.horizontal_scale_mf_text_mf);
        this.horizontalScaleMf_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.horizontalScaleMf_linear);
        this.height = Util.dip2px(context2, 290.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        RelativeLayout rl_text_af_and_mf_front = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_text_af_and_mf_front);
        RelativeLayout rl_text_af_and_mf_black = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_text_af_and_mf_black);
        if (((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            rl_text_af_and_mf_front.setVisibility(0);
            rl_text_af_and_mf_black.setVisibility(8);
        } else {
            rl_text_af_and_mf_front.setVisibility(8);
            rl_text_af_and_mf_black.setVisibility(0);
        }
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
                if (FVHitchCockEndPlacePop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVHitchCockEndPlacePop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(true);
        CameraUtils.setIsHitchCockRecordUI(true);
        this.ll_scale_hitch_cock_end_place_wt = (LinearLayout) this.view.findViewById(C0853R.C0855id.ll_scale_hitch_cock_end_place_wt);
        this.bg_hitch_cock_setting_time_relative_mf = (RelativeLayout) this.view.findViewById(C0853R.C0855id.bg_hitch_cock_setting_time_relative_mf);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (CameraUtils.getCurrentPageIndex() == 2) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FVHitchCockEndPlacePop.this.isConnected) {
                        int unused = FVHitchCockEndPlacePop.this.stirPosition = 0;
                        if (FVHitchCockEndPlacePop.this.stirPosition == 0) {
                            FVHitchCockEndPlacePop.this.setBackgroundColorSelect(FVHitchCockEndPlacePop.this.stirPosition);
                        }
                    }
                    int color = FVHitchCockEndPlacePop.this.mContext.getResources().getColor(C0853R.color.yellow_18);
                    int mStartColorPsRestart0 = FVHitchCockEndPlacePop.this.mContext.getResources().getColor(C0853R.color.color_black4);
                    int mEndColor0 = FVHitchCockEndPlacePop.this.mContext.getResources().getColor(C0853R.color.white);
                    FVHitchCockEndPlacePop.this.horizontalScaleScrollViewHitchCockWT.setColor(mStartColorPsRestart0, mEndColor0);
                    FVHitchCockEndPlacePop.this.scaleScrollViewHitchCockMf.setColor(mStartColorPsRestart0, mEndColor0);
                }
            }, 100);
        }
    }

    /* access modifiers changed from: private */
    public void setBackgroundColorSelect(int select) {
        switch (select) {
            case 0:
                this.ll_scale_hitch_cock_end_place_wt.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.rule_cheek_round_yellow_stroke_bg));
                this.bg_hitch_cock_setting_time_relative_mf.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black_20));
                this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 1:
                this.ll_scale_hitch_cock_end_place_wt.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black_20));
                this.bg_hitch_cock_setting_time_relative_mf.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.rule_cheek_round_yellow_stroke_bg));
                this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 2:
                this.ll_scale_hitch_cock_end_place_wt.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black_20));
                this.bg_hitch_cock_setting_time_relative_mf.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black_20));
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
                FVHitchCockEndPlacePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVHitchCockEndPlacePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVHitchCockEndPlacePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVHitchCockEndPlacePop.this.sendToHandler(13);
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
        this.horizontalScaleMf_linear.setOnClickListener(this);
        this.horizontal_scale_mf_text_af.setOnClickListener(this);
        this.horizontal_scale_mf_text_mf.setOnClickListener(this);
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
                if (FVHitchCockEndPlacePop.this.startFirstWT) {
                    double sca = (double) scale;
                    FVHitchCockEndPlacePop.this.curScrollScaleWT = (int) sca;
                    if (FVHitchCockEndPlacePop.this.curScrollScaleWTOld != FVHitchCockEndPlacePop.this.curScrollScaleWT) {
                        float pro = (float) (((sca / 10.0d) / 2.0d) + 1.0d);
                        Log.e("---------------", "--------  mCountScale 9990 WT  WT sc sc sc ------  scale:   scale:   scale:  scale:" + scale + "  pro:" + pro);
                        FVHitchCockEndPlacePop.this.cameraManager.setZoom(pro);
                        CameraUtils.setHitchCockEndPlaceWT(pro);
                    }
                    FVHitchCockEndPlacePop.this.curScrollScaleWTOld = FVHitchCockEndPlacePop.this.curScrollScaleWT;
                }
            }
        });
        this.scaleScrollViewHitchCockMfToAf = (HorizontalScaleScrollViewHitchCockMfToAf) this.view.findViewById(C0853R.C0855id.horizontalScaleMfToAf);
        this.scaleScrollViewHitchCockMfToAf.setOnScrollListener(new BaseScaleViewHitchCockMfToAf.OnScrollListener() {
            public void onScaleScroll(int scale) {
                if (FVHitchCockEndPlacePop.this.startFirstMFToAF && FVHitchCockEndPlacePop.this.cameraManager.isMaunalFocus()) {
                    FVHitchCockEndPlacePop.this.cameraManager.enableMFMode(false);
                }
            }
        });
        this.scaleScrollViewHitchCockMf = (HorizontalScaleScrollViewHitchCockMf) this.view.findViewById(C0853R.C0855id.horizontalScaleMf);
        this.scaleScrollViewHitchCockMf.setOnScrollListener(new BaseScaleViewHitchCockMf.OnScrollListener() {
            public void onScaleScroll(int scale) {
                double sca = (double) scale;
                FVHitchCockEndPlacePop.this.curScrollScaleMf = (int) sca;
                if (FVHitchCockEndPlacePop.this.curScrollScaleMfOld != FVHitchCockEndPlacePop.this.curScrollScaleMf && FVHitchCockEndPlacePop.this.startFirstMF) {
                    if (!FVHitchCockEndPlacePop.this.cameraManager.isMaunalFocus()) {
                        FVHitchCockEndPlacePop.this.cameraManager.enableMFMode(true);
                    }
                    FVHitchCockEndPlacePop.this.horizontal_scale_mf_textview.setText(String.valueOf(sca / 10.0d));
                    double sc = sca / 10.0d;
                    if (sc > ((double) FVHitchCockEndPlacePop.this.cameraManager.getMinFocusDistance())) {
                        sc = (double) FVHitchCockEndPlacePop.this.cameraManager.getMinFocusDistance();
                    } else if (sc < 0.0d) {
                        sc = 0.0d;
                    }
                    FVHitchCockEndPlacePop.this.cameraManager.setFocusDistance((float) sc);
                    CameraUtils.setHitchCockEndPlaceMF(sc);
                }
                FVHitchCockEndPlacePop.this.curScrollScaleMfOld = FVHitchCockEndPlacePop.this.curScrollScaleMf;
            }
        });
        if (CameraUtils.getHitchCockStartPlaceStringMF().equals("MF")) {
            CameraUtils.setHitchCockEndPlaceStringMF("MF");
            this.horizontalScaleMf_linear.setVisibility(8);
            this.scaleScrollViewHitchCockMfToAf.setVisibility(8);
            this.scaleScrollViewHitchCockMf.setVisibility(0);
        } else {
            CameraUtils.setHitchCockEndPlaceStringMF("AF");
            this.horizontalScaleMf_linear.setVisibility(0);
            this.scaleScrollViewHitchCockMfToAf.setVisibility(0);
            this.scaleScrollViewHitchCockMf.setVisibility(8);
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                double scaleMF;
                FVHitchCockEndPlacePop.this.startFirstMF = true;
                FVHitchCockEndPlacePop.this.startFirstMFToAF = true;
                if (CameraUtils.getHitchCockEndPlaceStringMF().equals("MF")) {
                    if (CameraUtils.getHitchCockStartPlaceMF() == 1.0d) {
                        scaleMF = CameraUtils.getHitchCockEndPlaceMF() * 10.0d;
                    } else if (CameraUtils.getHitchCockEndPlacePageAppears() == 1) {
                        scaleMF = CameraUtils.getHitchCockEndPlaceMF() * 10.0d;
                    } else {
                        scaleMF = CameraUtils.getHitchCockStartPlaceMF() * 10.0d;
                    }
                    FVHitchCockEndPlacePop.this.scaleScrollViewHitchCockMf.setCurScale((int) scaleMF);
                    return;
                }
                FVHitchCockEndPlacePop.this.scaleScrollViewHitchCockMfToAf.setCurScale(0);
            }
        }, 400);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FVHitchCockEndPlacePop.this.startFirstWT = true;
                FVHitchCockEndPlacePop.this.horizontalScaleScrollViewHitchCockWT.setCurScale((int) ((double) ((CameraUtils.getHitchCockEndPlaceWT() - 1.0f) * 10.0f * 2.0f)));
                FVHitchCockEndPlacePop.this.cameraManager.setZoom(CameraUtils.getHitchCockEndPlaceWT());
            }
        }, 200);
        if (CameraUtils.getHitchCockStartPlaceStringMF().equals("MF")) {
            this.horizontal_scale_mf_text_af.setEnabled(false);
            this.horizontalScaleMf_linear.setVisibility(8);
            scaleMfTextMfSetOnClick();
        } else if (CameraUtils.getHitchCockStartPlaceStringMF().equals("AF")) {
            this.horizontal_scale_mf_text_mf.setEnabled(false);
            this.horizontalScaleMf_linear.setVisibility(0);
            scaleMfTextAfSetOnClick();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                Log.e("--------------", "-------- 退出移动摄影, 返回上一级弹框 -----");
                if (this.pop != null) {
                    Util.sendIntEventMessge(10008);
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    this.pop.dismiss();
                }
                if (this.pop2 != null) {
                    FVMoveTimelapseSmoothPop.setPopDismiss(this.pop2);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 32 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上    FVHitchCockStartPlacePop  ");
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
                        this.horizontalScaleScrollViewHitchCockWT.setCurScale(pos);
                        return;
                    } else if (this.stirPosition == 1 && !CameraUtils.getHitchCockStartPlaceStringMF().equals("AF") && CameraUtils.getHitchCockStartPlaceStringMF().equals("MF")) {
                        int pos2 = this.curScrollScaleMf + 1;
                        int numMax2 = Util.getDrawScaleMFMax() - 10;
                        if (pos2 > numMax2) {
                            pos2 = numMax2;
                        }
                        this.scaleScrollViewHitchCockMf.setCurScale(pos2);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 32 && this.pop != null) {
                    Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下    FVHitchCockStartPlacePop  ");
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 2) {
                            this.stirPosition = 2;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVTimeLapsePop  210 波轮拨动向上   第一刻度尺,向左滑动   第一刻度尺,向左滑动   向左");
                        int pos3 = this.curScrollScaleWT - 10;
                        if (pos3 < 0) {
                            pos3 = 0;
                        }
                        this.horizontalScaleScrollViewHitchCockWT.setCurScale(pos3);
                        return;
                    } else if (this.stirPosition == 1 && !CameraUtils.getHitchCockStartPlaceStringMF().equals("AF") && CameraUtils.getHitchCockStartPlaceStringMF().equals("MF")) {
                        int pos4 = this.curScrollScaleMf - 1;
                        if (pos4 < 0) {
                            pos4 = 0;
                        }
                        this.scaleScrollViewHitchCockMf.setCurScale(pos4);
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
                        this.horizontalScaleScrollViewHitchCockWT.setColor(mStartColorPs0, this.mContext.getResources().getColor(C0853R.color.white));
                        return;
                    }
                    int color2 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                    this.horizontalScaleScrollViewHitchCockWT.setColor(this.mContext.getResources().getColor(C0853R.color.color_black4), this.mContext.getResources().getColor(C0853R.color.white));
                    return;
                } else if (this.stirPosition == 1) {
                    if (CameraUtils.getHitchCockStartPlaceStringMF().equals("AF")) {
                        this.scaleSlide = false;
                        return;
                    } else if (!CameraUtils.getHitchCockStartPlaceStringMF().equals("MF")) {
                        return;
                    } else {
                        if (this.scaleSlide) {
                            int mStartColorPs02 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                            int color3 = this.mContext.getResources().getColor(C0853R.color.color_black4);
                            this.scaleScrollViewHitchCockMf.setColor(mStartColorPs02, this.mContext.getResources().getColor(C0853R.color.white));
                            return;
                        }
                        int color4 = this.mContext.getResources().getColor(C0853R.color.yellow_18);
                        this.scaleScrollViewHitchCockMf.setColor(this.mContext.getResources().getColor(C0853R.color.color_black4), this.mContext.getResources().getColor(C0853R.color.white));
                        return;
                    }
                } else if (this.stirPosition == 2) {
                    this.scaleSlide = false;
                    hitchCockEndPlacePopNextClick();
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVHitchCockStartPlacePop");
                if (CameraUtils.getFrameLayerNumber() == 32) {
                    hitchCockEndPlacePopReturnClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 32) {
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    EventBus.getDefault().unregister(this);
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setIsHitchCockRecordUI(false);
                    this.cameraManager.enableMFMode(false);
                    this.cameraManager.enableManualMode(false);
                    CameraUtils.setHitchCockStartPlaceWT(1.0f);
                    CameraUtils.setHitchCockStartPlaceMF(0.0d);
                    CameraUtils.setHitchCockEndPlaceWT(1.0f);
                    CameraUtils.setHitchCockEndPlaceMF(0.0d);
                    CameraUtils.setHitchCockStartPlaceStringMF("AF");
                    CameraUtils.setHitchCockEndPlaceStringMF("AF");
                    this.cameraManager.setZoom(1.0f);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void hitchCockStartPlacePopReturnClick() {
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

    private void hitchCockEndPlacePopReturnClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        CameraUtils.setHitchCockEndPlaceWT(1.0f);
        CameraUtils.setHitchCockEndPlaceMF(0.0d);
        showHitchCockStartPlacePop();
    }

    private void hitchCockEndPlacePopNextClick() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        EventBus.getDefault().unregister(this);
        if (!this.cameraManager.isMaunalFocus()) {
            CameraUtils.setHitchCockEndPlaceMF((double) this.cameraManager.getFocusDistance());
        }
        showHitchCockStartingTimePop();
    }

    public void setPop(PopupWindow pop3, final FVHitchCockEndPlacePop hitchCockPop) {
        this.pop = pop3;
        if (pop3 != null) {
            pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVHitchCockEndPlacePop.this.context.unregisterReceiver(FVHitchCockEndPlacePop.this.broad);
                    EventBus.getDefault().unregister(hitchCockPop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVHitchCockEndPlacePop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(32);
    }

    public View getView() {
        return this.view;
    }

    private void showHitchCockStartingTimePop() {
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartingTimePop hitchCockStartingTimePop = new FVHitchCockStartingTimePop();
        hitchCockStartingTimePop.init(this.context, this.parentView, false);
        PopupWindow pop22 = new PopupWindow(hitchCockStartingTimePop.getView(), height2, height2);
        pop22.setBackgroundDrawable(new ColorDrawable(0));
        pop22.setAnimationStyle(C0853R.style.popAnimation);
        pop22.setOutsideTouchable(false);
        hitchCockStartingTimePop.setPop(pop22, hitchCockStartingTimePop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop22.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    private void showHitchCockStartPlacePop() {
        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
        if (this.cameraManager.getCameraManagerType() != 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) - 1.0d);
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartPlacePop hitchCockPop = new FVHitchCockStartPlacePop();
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

    public void scaleMfTextAfSetOnClick() {
        this.horizontal_scale_mf_text_af.setBackgroundResource(C0853R.C0854drawable.edit_button_black_round_left_bg10);
        this.horizontal_scale_mf_text_mf.setBackgroundResource(C0853R.C0854drawable.edit_button_white_round_right_bg10);
        this.horizontal_scale_mf_text_af.setTextColor(this.context.getResources().getColor(C0853R.color.white));
        this.horizontal_scale_mf_text_mf.setTextColor(this.context.getResources().getColor(C0853R.color.color_black55));
        this.horizontalScaleMf_linear.setVisibility(0);
        if (this.scaleScrollViewHitchCockMfToAf.getVisibility() == 8) {
            this.horizontalScaleMf_linear.setVisibility(0);
            this.scaleScrollViewHitchCockMfToAf.setVisibility(0);
            this.scaleScrollViewHitchCockMf.setVisibility(8);
            if (this.cameraManager.isMaunalFocus()) {
                this.cameraManager.enableMFMode(false);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    FVHitchCockEndPlacePop.this.scaleScrollViewHitchCockMfToAf.setCurScale(0);
                }
            }, 300);
        }
    }

    public void scaleMfTextMfSetOnClick() {
        this.horizontal_scale_mf_text_af.setBackgroundResource(C0853R.C0854drawable.edit_button_white_round_left_bg10);
        this.horizontal_scale_mf_text_mf.setBackgroundResource(C0853R.C0854drawable.edit_button_black_round_right_bg10);
        this.horizontal_scale_mf_text_af.setTextColor(this.context.getResources().getColor(C0853R.color.color_black55));
        this.horizontal_scale_mf_text_mf.setTextColor(this.context.getResources().getColor(C0853R.color.white));
        this.horizontalScaleMf_linear.setVisibility(8);
        if (this.scaleScrollViewHitchCockMf.getVisibility() == 8) {
            this.horizontalScaleMf_linear.setVisibility(8);
            this.scaleScrollViewHitchCockMfToAf.setCurScale(30);
            this.scaleScrollViewHitchCockMfToAf.setVisibility(8);
            this.scaleScrollViewHitchCockMf.setVisibility(0);
            if (!this.cameraManager.isMaunalFocus()) {
                this.cameraManager.enableMFMode(true);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    FVHitchCockEndPlacePop.this.scaleScrollViewHitchCockMf.setCurScale((int) (10.0d * ((double) FVHitchCockEndPlacePop.this.cameraManager.getFocusDistance())));
                }
            }, 300);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_close:
                hitchCockEndPlacePopReturnClick();
                return;
            case C0853R.C0855id.btn_standard:
                hitchCockEndPlacePopNextClick();
                return;
            case C0853R.C0855id.horizontal_scale_mf_text_af:
                scaleMfTextAfSetOnClick();
                return;
            case C0853R.C0855id.horizontal_scale_mf_text_mf:
                scaleMfTextMfSetOnClick();
                return;
            default:
                return;
        }
    }
}
