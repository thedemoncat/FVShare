package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alanapi.switchbutton.SwitchButton;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.bean.CameraSecondItem;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.CameraExclusiveUtils;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.FileUtils;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVCameraVideoShortcutPop {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public CameraSettingAdapter adapter;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public List<CameraItem> datas;
    private int height;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public ListView listview;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVCameraVideoShortcutPop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVCameraVideoShortcutPop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVCameraVideoShortcutPop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVCameraVideoShortcutPop.this.setHorUiNinety270();
                    return;
                case 50:
                    FVCameraVideoShortcutPop.this.listview.smoothScrollToPositionFromTop(msg.arg1, 0);
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    PopupWindow popupWindow;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    /* access modifiers changed from: private */
    public boolean stirUpDown = false;
    private boolean switchButtonItemValue = false;
    private View view;

    public void init(Context context2, View parent) {
        this.context = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.parentView = parent;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_camera_shortcut_pop_new, (ViewGroup) null);
        this.listview = (ListView) this.view.findViewById(C0853R.C0855id.listView);
        initData();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
                if (FVCameraVideoShortcutPop.this.pop != null) {
                    FVCameraVideoShortcutPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraVideoShortcutPop.this.pop != null) {
                    FVCameraVideoShortcutPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        CameraUtils.setFrameLayerNumber(1);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirUpDown = true;
            this.stirPosition = 0;
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
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
                FVCameraVideoShortcutPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVCameraVideoShortcutPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVCameraVideoShortcutPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVCameraVideoShortcutPop.this.sendToHandler(13);
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
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
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
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 3;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiZero() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
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
        linearParams2.width = this.height / 3;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(0);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(8);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
    }

    /* access modifiers changed from: private */
    public void setHorUiNinety270() {
        this.layout_camera_shortcut_pop_horizontal_bottom.setVisibility(8);
        this.layout_camera_shortcut_pop_vertical_bottom_top.setVisibility(0);
        this.layout_camera_shortcut_pop_out_linear.setOrientation(1);
        LinearLayout.LayoutParams linearParamsAll = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
        linearParamsAll.height = this.height;
        linearParamsAll.width = this.height;
        this.layout_camera_shortcut_pop_out_linear.setLayoutParams(linearParamsAll);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_int_linear.getLayoutParams();
        linearParams.height = (this.height * 2) / 3;
        linearParams.width = this.height;
        this.layout_camera_shortcut_pop_int_linear.setLayoutParams(linearParams);
        LinearLayout.LayoutParams linearParams2 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_horizontal_bottom.getLayoutParams();
        linearParams2.height = this.height / 3;
        linearParams2.width = this.height;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height / 3;
        linearParams3.width = this.height;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    public View getView() {
        return this.view;
    }

    public void setPop(PopupWindow pop2) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVCameraVideoShortcutPop.this.context.unregisterReceiver(FVCameraVideoShortcutPop.this.broad);
                    EventBus.getDefault().unregister(this);
                    CameraUtils.setFrameLayerNumber(0);
                }
            });
        }
    }

    public void unRegisterListener() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
    }

    private void initData() {
        if (this.datas == null) {
            this.datas = new ArrayList();
        } else {
            this.datas.clear();
        }
        this.datas.add(new CameraItem(C0853R.string.label_delay_video_static, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_delay_video_dynamic, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_move_delay_video, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_move_video, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_white_balance, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_griding, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_camera_hand_model, 2, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_hitch_cock, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_gradienter, 2, (Class) null));
        this.adapter = new CameraSettingAdapter(this.context);
        this.adapter.setList(this.datas);
        this.listview.setAdapter(this.adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                        MoveTimelapseUtil.setTimeLapseStaticOrDynamic(true);
                        FVCameraVideoShortcutPop.this.showTimeLapsePop();
                        return;
                    case 1:
                        MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                        MoveTimelapseUtil.setTimeLapseStaticOrDynamic(false);
                        FVCameraVideoShortcutPop.this.showTimeLapsePop();
                        return;
                    case 2:
                        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraFvShareSleep() == 1 && isConnected.booleanValue()) {
                            EventBusUtil.sendEvent(new Event(153));
                            return;
                        } else if (Util.isPovReverTime(FVCameraVideoShortcutPop.this.context)) {
                            MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
                            MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                            FVCameraVideoShortcutPop.this.showMoveTimeLapsePop();
                            return;
                        } else {
                            return;
                        }
                    case 3:
                        Boolean isConnected2 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraFvShareSleep() == 1 && isConnected2.booleanValue()) {
                            EventBusUtil.sendEvent(new Event(153));
                            return;
                        } else if (Util.isPovReverLapse(FVCameraVideoShortcutPop.this.context)) {
                            MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
                            MoveTimelapseUtil.setMotionLapseTimeYesOrNo(false);
                            FVCameraVideoShortcutPop.this.showMoveTimeLapsePop();
                            return;
                        } else {
                            return;
                        }
                    case 4:
                        int wbMode = ((Integer) SPUtils.get(FVCameraVideoShortcutPop.this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
                        List<CameraSecondItem> ItemDates3 = new ArrayList<>();
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_auto, 1, wbMode == 10019));
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_sunshine, 1, wbMode == 10020));
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_overcast, 1, wbMode == 10021));
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_fluorescent_lamp, 1, wbMode == 10022));
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_incandescent_lamp, 1, wbMode == 10023));
                        FVCameraVideoShortcutPopNext fvVideoShortcutPopNext3 = new FVCameraVideoShortcutPopNext();
                        fvVideoShortcutPopNext3.init(FVCameraVideoShortcutPop.this.context, ((CameraItem) FVCameraVideoShortcutPop.this.datas.get(position)).f1106id, (position - 1) - 1, ItemDates3);
                        FVCameraVideoShortcutPop.this.showPop(fvVideoShortcutPopNext3);
                        return;
                    case 5:
                        int gridMode = ((Integer) SPUtils.get(FVCameraVideoShortcutPop.this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
                        List<CameraSecondItem> ItemDates5 = new ArrayList<>();
                        ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, gridMode == 10015));
                        ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_gridView, 1, gridMode == 10017));
                        ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_gridView_diagonal_line, 1, gridMode == 10018));
                        ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_center_point, 1, gridMode == 10016));
                        FVCameraVideoShortcutPopNext fvVideoShortcutPopNext5 = new FVCameraVideoShortcutPopNext();
                        fvVideoShortcutPopNext5.init(FVCameraVideoShortcutPop.this.context, ((CameraItem) FVCameraVideoShortcutPop.this.datas.get(position)).f1106id, (position - 1) - 1, ItemDates5);
                        FVCameraVideoShortcutPop.this.showPop(fvVideoShortcutPopNext5);
                        return;
                    case 7:
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            FVCameraVideoShortcutPop.this.toastAboutMarkPointCancelTV();
                        }
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraTrackingStart() == 1 && CameraUtils.isFollowIng()) {
                            Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
                        }
                        if (FVCameraVideoShortcutPop.this.cameraManager.getCameraManagerType() != 2) {
                            FVCameraVideoShortcutPop.this.showHitchCockTradPop();
                            return;
                        } else if (FVCameraManager.GetCameraLevel(FVCameraVideoShortcutPop.this.context) == 2) {
                            FVCameraVideoShortcutPop.this.showHitchCockLegacyPop();
                            return;
                        } else if (((Integer) SPUtils.get(FVCameraVideoShortcutPop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() == 107211) {
                            SPUtils.put(FVCameraVideoShortcutPop.this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                            if (FVCameraVideoShortcutPop.this.adapter != null) {
                                FVCameraVideoShortcutPop.this.adapter.notifyDataSetChanged();
                            }
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVCameraVideoShortcutPop.this.showHitchCockPop();
                                }
                            }, 200);
                            return;
                        } else {
                            FVCameraVideoShortcutPop.this.showHitchCockPop();
                            return;
                        }
                    default:
                        return;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void toastAboutMarkPointCancelTV() {
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
            EventBusUtil.sendEvent(new Event(145));
        }
    }

    /* access modifiers changed from: private */
    public void showHitchCockLegacyPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
        CameraUtils.setHitchCockStartPlaceWT(1.0f);
        CameraUtils.setHitchCockStartPlaceMF(0.0d);
        CameraUtils.setHitchCockEndPlaceWT(1.0f);
        CameraUtils.setHitchCockEndPlaceMF(0.0d);
        CameraUtils.setHitchCockStartPlaceStringMF("AF");
        CameraUtils.setHitchCockEndPlaceStringMF("AF");
        this.cameraManager.setZoom(1.0f);
        if (this.cameraManager.getCameraManagerType() != 1) {
            double maxZoom = ((double) this.cameraManager.getMaxZoom()) - 1.0d;
            Log.e("-------------", "-------- 6666  8888  9999   two ------" + maxZoom + "      focusDistance:" + this.cameraManager.getMinFocusDistance());
            CameraUtils.setScaleScrollViewWTMaxNums(maxZoom);
        }
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartPlaceLegacyPop hitchCockPop = new FVHitchCockStartPlaceLegacyPop();
        hitchCockPop.init(this.context, this.parentView.findViewById(C0853R.C0855id.btn_camera), false);
        PopupWindow pop2 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        hitchCockPop.setPop(pop2, hitchCockPop);
        int right = this.parentView.findViewById(C0853R.C0855id.btn_camera).getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    /* access modifiers changed from: private */
    public void showHitchCockTradPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        CameraUtils.setHitchCockStartPlaceWT(0.0f);
        CameraUtils.setHitchCockEndPlaceWT(0.0f);
        this.cameraManager.setZoom(0.0f);
        CameraUtils.setHitchCockStartPlaceMF(0.0d);
        CameraUtils.setHitchCockEndPlaceMF(0.0d);
        CameraUtils.setHitchCockStartPlaceStringMF("AF");
        CameraUtils.setHitchCockEndPlaceStringMF("AF");
        if (this.cameraManager.getCameraManagerType() == 1) {
            CameraUtils.setScaleScrollViewWTMaxNums(((double) this.cameraManager.getMaxZoom()) / 10.0d);
            CameraUtils.setScaleScrollViewTradWTMaxNums((double) this.cameraManager.getMaxZoom());
        } else {
            double maxZoom = ((double) this.cameraManager.getMaxZoom()) - 1.0d;
            Log.e("-------------", "-------- 6666  8888  9999   two ------" + maxZoom + "      focusDistance:" + this.cameraManager.getMinFocusDistance());
            CameraUtils.setScaleScrollViewWTMaxNums(maxZoom);
        }
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartPlaceTradPop hitchCockPop = new FVHitchCockStartPlaceTradPop();
        hitchCockPop.init(this.context, this.parentView.findViewById(C0853R.C0855id.btn_camera), false);
        PopupWindow pop2 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        hitchCockPop.setPop(pop2, hitchCockPop);
        int right = this.parentView.findViewById(C0853R.C0855id.btn_camera).getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    /* access modifiers changed from: private */
    public void showHitchCockPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        CameraUtils.setScaleScrollViewMFMaxNums((double) (this.cameraManager.getMinFocusDistance() + 1.0f));
        CameraUtils.setHitchCockStartPlaceWT(1.0f);
        CameraUtils.setHitchCockStartPlaceMF(0.0d);
        CameraUtils.setHitchCockEndPlaceWT(1.0f);
        CameraUtils.setHitchCockEndPlaceMF(0.0d);
        CameraUtils.setHitchCockStartPlaceStringMF("AF");
        CameraUtils.setHitchCockEndPlaceStringMF("AF");
        this.cameraManager.setZoom(1.0f);
        if (this.cameraManager.getCameraManagerType() != 1) {
            double maxZoom = ((double) this.cameraManager.getMaxZoom()) - 1.0d;
            Log.e("-------------", "-------- 6666  8888  9999   two ------" + maxZoom + "      focusDistance:" + this.cameraManager.getMinFocusDistance());
            CameraUtils.setScaleScrollViewWTMaxNums(maxZoom);
        }
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVHitchCockStartPlacePop hitchCockPop = new FVHitchCockStartPlacePop();
        hitchCockPop.init(this.context, this.parentView.findViewById(C0853R.C0855id.btn_camera), false);
        PopupWindow pop2 = new PopupWindow(hitchCockPop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation);
        pop2.setOutsideTouchable(false);
        hitchCockPop.setPop(pop2, hitchCockPop);
        int right = this.parentView.findViewById(C0853R.C0855id.btn_camera).getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    /* access modifiers changed from: private */
    public void showPop(final FVCameraVideoShortcutPopNext videoShortcutPopNext) {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        this.popupWindow = new PopupWindow(videoShortcutPopNext.getView(), height2, height2, true);
        videoShortcutPopNext.setPop(this.popupWindow, videoShortcutPopNext);
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(C0853R.style.popAnimation2);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                CameraUtils.setFrameLayerNumber(0);
                videoShortcutPopNext.unRegisterListener();
                EventBus.getDefault().unregister(videoShortcutPopNext);
                Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                if (Boolean.valueOf(ViseBluetooth.getInstance().isConnected()).booleanValue() && CameraUtils.getCurrentPageIndex() == 2) {
                    BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
                }
            }
        });
        this.popupWindow.setFocusable(false);
        this.popupWindow.update();
        Resources resources = this.context.getResources();
        int statusBarHeight = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
        int[] position = new int[2];
        this.parentView.getLocationOnScreen(position);
        if (position[0] < 40) {
            statusBarHeight = 0;
        }
        this.popupWindow.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 0, this.parentView.getRight() + Util.dip2px(this.context, 2.0f) + statusBarHeight, Util.dip2px(this.context, 4.0f));
        Util.fullScreenImmersive(this.popupWindow.getContentView());
        this.popupWindow.setFocusable(true);
        this.popupWindow.update();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.popupWindow != null) {
                    this.popupWindow.dismiss();
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraShortcutPopVideo" + CameraUtils.getFrameLayerNumber());
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_OPEN:
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_HAND_MODEL_CLOSE:
                if (this.adapter != null) {
                    this.adapter.notifyDataSetChanged();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && Boolean.valueOf(setControlOnClickIsSwitchButton(this.stirPosition)).booleanValue()) {
                    onClickStirPosition(this.stirPosition);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && Boolean.valueOf(setControlOnClickIsSwitchButton(this.stirPosition)).booleanValue()) {
                    onClickStirPosition(this.stirPosition);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                onClickStirPosition(this.stirPosition);
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraVideoShortcutPop");
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_ROCKING_BAR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 1) {
                    FM210labelCameraStirUp();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_ROCKING_BAR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 1) {
                    FM210labelCameraStirDown();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean setControlOnClickIsSwitchButton(int pos) {
        this.switchButtonItemValue = false;
        if (pos == 6) {
            this.switchButtonItemValue = true;
        } else if (pos == 8) {
            this.switchButtonItemValue = true;
        }
        return this.switchButtonItemValue;
    }

    private void FM210labelCameraStirUp() {
        if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
            this.stirUpDown = true;
            this.stirPosition--;
            if (this.stirPosition < 0) {
                this.stirPosition = 0;
            }
            int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
            if (this.cameraManager.getCameraManagerType() == 2) {
                if (cameraLevel == 2) {
                    if (this.stirPosition == 7) {
                        this.stirPosition = 5;
                    } else if (this.stirPosition == 6) {
                        this.stirPosition = 5;
                    }
                }
            } else if (this.stirPosition == 6) {
                this.stirPosition = 5;
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            this.listview.setSelectionFromTop(this.stirPosition, 0);
            Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上 " + this.stirPosition);
        }
    }

    private void FM210labelCameraStirDown() {
        if (CameraUtils.getFrameLayerNumber() == 1 && this.pop != null) {
            this.stirUpDown = true;
            this.stirPosition++;
            if (this.stirPosition > 8) {
                this.stirPosition = 8;
            }
            int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
            if (this.cameraManager.getCameraManagerType() == 2) {
                if (cameraLevel == 2 && this.stirPosition > 5) {
                    this.stirPosition = 5;
                }
            } else if (this.stirPosition == 6) {
                this.stirPosition = 7;
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            this.listview.setSelectionFromTop(this.stirPosition, 0);
            Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下" + this.stirPosition);
        }
    }

    private void onClickStirPosition(int position) {
        switch (position) {
            case 0:
                MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                MoveTimelapseUtil.setTimeLapseStaticOrDynamic(true);
                showTimeLapsePop();
                break;
            case 1:
                MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                MoveTimelapseUtil.setTimeLapseStaticOrDynamic(false);
                showTimeLapsePop();
                break;
            case 2:
                Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected.booleanValue()) {
                    if (Util.isPovReverTime(this.context)) {
                        MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
                        MoveTimelapseUtil.setMotionLapseTimeYesOrNo(true);
                        showMoveTimeLapsePop();
                        break;
                    }
                } else {
                    EventBusUtil.sendEvent(new Event(153));
                    break;
                }
                break;
            case 3:
                Boolean isConnected2 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected2.booleanValue()) {
                    if (Util.isPovReverLapse(this.context)) {
                        MoveTimelapseUtil.setMoveVideoAndMoveTimeVideo(1);
                        MoveTimelapseUtil.setMotionLapseTimeYesOrNo(false);
                        showMoveTimeLapsePop();
                        break;
                    }
                } else {
                    EventBusUtil.sendEvent(new Event(153));
                    break;
                }
                break;
            case 4:
                int wbMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
                List<CameraSecondItem> ItemDates3 = new ArrayList<>();
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_auto, 1, wbMode == 10019));
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_sunshine, 1, wbMode == 10020));
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_overcast, 1, wbMode == 10021));
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_fluorescent_lamp, 1, wbMode == 10022));
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_wb_incandescent_lamp, 1, wbMode == 10023));
                FVCameraVideoShortcutPopNext fvVideoShortcutPopNext3 = new FVCameraVideoShortcutPopNext();
                fvVideoShortcutPopNext3.init(this.context, this.datas.get(position).f1106id, (position - 1) - 1, ItemDates3);
                showPop(fvVideoShortcutPopNext3);
                break;
            case 5:
                int gridMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
                List<CameraSecondItem> ItemDates5 = new ArrayList<>();
                ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, gridMode == 10015));
                ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_gridView, 1, gridMode == 10017));
                ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_gridView_diagonal_line, 1, gridMode == 10018));
                ItemDates5.add(new CameraSecondItem(C0853R.string.label_griding_center_point, 1, gridMode == 10016));
                FVCameraVideoShortcutPopNext fvVideoShortcutPopNext5 = new FVCameraVideoShortcutPopNext();
                fvVideoShortcutPopNext5.init(this.context, this.datas.get(position).f1106id, (position - 1) - 1, ItemDates5);
                showPop(fvVideoShortcutPopNext5);
                break;
            case 6:
                if (((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() == 107212) {
                    CameraExclusiveUtils.setCamHandModelExclusive(this.context, this.cameraManager, true);
                    if (CameraUtils.getCurrentPageIndex() == 2) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                } else {
                    CameraExclusiveUtils.setCamHandModelExclusive(this.context, this.cameraManager, false);
                }
                this.adapter.notifyDataSetChanged();
                break;
            case 7:
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    toastAboutMarkPointCancelTV();
                }
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraTrackingStart() == 1 && CameraUtils.isFollowIng()) {
                    Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
                }
                if (this.cameraManager.getCameraManagerType() == 2) {
                    if (FVCameraManager.GetCameraLevel(this.context) != 2) {
                        if (((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue() != 107211) {
                            showHitchCockPop();
                            break;
                        } else {
                            SPUtils.put(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
                            Util.sendIntEventMessge(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE);
                            if (this.adapter != null) {
                                this.adapter.notifyDataSetChanged();
                            }
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    FVCameraVideoShortcutPop.this.showHitchCockPop();
                                }
                            }, 200);
                            break;
                        }
                    } else {
                        showHitchCockLegacyPop();
                        break;
                    }
                } else {
                    showHitchCockTradPop();
                    break;
                }
            case 8:
                if (((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE))).intValue() == 107772) {
                    SPUtils.put(this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_OPEN));
                    Log.e("---------------", "----------  7898  9999 8878  水平仪打开  水平仪打开  --------");
                    Util.sendIntEventMessge(Constants.INIT_GRADIENTER_VIEW);
                } else {
                    SPUtils.put(this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE));
                    Log.e("---------------", "----------  7898  9999 8878  水平仪关闭  水平仪关闭  --------");
                    Util.sendIntEventMessge(Constants.REMOVE_GRADIENTER_VIEW);
                }
                this.adapter.notifyDataSetChanged();
                break;
        }
        this.myHandler.removeMessages(50);
        Message message = Message.obtain();
        message.what = 50;
        message.arg1 = position;
        this.myHandler.sendMessageDelayed(message, 10);
    }

    private class CameraItem {
        private Class cls;
        /* access modifiers changed from: private */
        public int flag;
        /* access modifiers changed from: private */

        /* renamed from: id */
        public int f1106id;

        private CameraItem(int _id, int _flag, Class _cs) {
            this.f1106id = _id;
            this.flag = _flag;
            this.cls = _cs;
        }
    }

    private class CameraSettingAdapter extends BaseAdapter {
        /* access modifiers changed from: private */
        public Context context;
        private ViewHolder holder = new ViewHolder();
        private LayoutInflater layoutInflater;
        private List<CameraItem> list;

        public CameraSettingAdapter(Context context2) {
            this.context = context2;
            this.layoutInflater = LayoutInflater.from(context2);
            this.list = new ArrayList();
        }

        public void setList(List<CameraItem> list2) {
            this.list = list2;
        }

        public void addList(List<CameraItem> list2) {
            this.list.addAll(list2);
        }

        public void clearList() {
            this.list.clear();
        }

        public List<CameraItem> getList() {
            return this.list;
        }

        public void removeItem(int position) {
            if (this.list.size() > 0) {
                this.list.remove(position);
            }
        }

        public int getCount() {
            return this.list.size();
        }

        public CameraItem getItem(int position) {
            return this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        private class ViewHolder {
            RelativeLayout layout_camera_item2_relative;
            RelativeLayout layout_camera_item_relative;
            LinearLayout layout_camera_item_relative_all;
            SwitchButton switchButton;
            TextView textView;
            TextView textView2;

            private ViewHolder() {
            }
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                this.holder = new ViewHolder();
                convertView = this.layoutInflater.inflate(C0853R.layout.layout_camera_item0, (ViewGroup) null);
                this.holder.textView = (TextView) convertView.findViewById(C0853R.C0855id.text);
                this.holder.textView2 = (TextView) convertView.findViewById(C0853R.C0855id.text2);
                this.holder.switchButton = (SwitchButton) convertView.findViewById(C0853R.C0855id.switchButton);
                this.holder.layout_camera_item_relative = (RelativeLayout) convertView.findViewById(C0853R.C0855id.layout_camera_item_relative);
                this.holder.layout_camera_item2_relative = (RelativeLayout) convertView.findViewById(C0853R.C0855id.layout_camera_item2_relative);
                this.holder.layout_camera_item_relative_all = (LinearLayout) convertView.findViewById(C0853R.C0855id.layout_camera_item_relative_all);
                convertView.setTag(this.holder);
            } else {
                this.holder = (ViewHolder) convertView.getTag();
            }
            this.holder.layout_camera_item_relative_all.setVisibility(0);
            this.holder.layout_camera_item_relative.setVisibility(0);
            this.holder.layout_camera_item2_relative.setVisibility(0);
            CameraItem item = this.list.get(position);
            if (item != null) {
                this.holder.textView.setText(item.f1106id);
                this.holder.textView2.setText(item.f1106id);
                if (item.flag == 0) {
                    this.holder.layout_camera_item_relative.setVisibility(0);
                    this.holder.layout_camera_item2_relative.setVisibility(8);
                    if (FVCameraVideoShortcutPop.this.stirUpDown) {
                        if (position == FVCameraVideoShortcutPop.this.stirPosition) {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
                        }
                    }
                    int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
                    if (FVCameraVideoShortcutPop.this.cameraManager.getCameraManagerType() == 2 && position == 7 && cameraLevel == 2) {
                        this.holder.layout_camera_item_relative_all.setVisibility(8);
                    }
                } else if (item.flag == 2) {
                    this.holder.layout_camera_item_relative.setVisibility(8);
                    this.holder.layout_camera_item2_relative.setVisibility(0);
                    if (FVCameraVideoShortcutPop.this.stirUpDown) {
                        if (position == FVCameraVideoShortcutPop.this.stirPosition) {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
                        }
                    }
                    int cameraLevel2 = FVCameraManager.GetCameraLevel(this.context);
                    if (FVCameraVideoShortcutPop.this.cameraManager.getCameraManagerType() == 2) {
                        if (position == 6 || position == 7) {
                            if (cameraLevel2 == 2) {
                                this.holder.layout_camera_item_relative_all.setVisibility(8);
                            } else {
                                this.holder.layout_camera_item_relative_all.setVisibility(0);
                            }
                        }
                    } else if (position == 6 || position == 7) {
                        this.holder.layout_camera_item_relative_all.setVisibility(8);
                    }
                    int cameraHandModel = ((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue();
                    int cameraGradienter = ((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE))).intValue();
                    if (position == 6) {
                        if (cameraHandModel == 107212) {
                            if (this.holder.switchButton.isChecked()) {
                                this.holder.switchButton.setChecked(false);
                            }
                        } else if (!this.holder.switchButton.isChecked()) {
                            this.holder.switchButton.setChecked(true);
                        }
                    } else if (position == 8) {
                        if (cameraGradienter == 107772) {
                            if (this.holder.switchButton.isChecked()) {
                                this.holder.switchButton.setChecked(false);
                            }
                        } else if (!this.holder.switchButton.isChecked()) {
                            this.holder.switchButton.setChecked(true);
                        }
                    }
                    this.holder.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                if (position == 6) {
                                    Log.e("-----------", "------- 555 -- Checked Yes ------");
                                    CameraExclusiveUtils.setCamHandModelExclusive(CameraSettingAdapter.this.context, FVCameraVideoShortcutPop.this.cameraManager, true);
                                    CameraSettingAdapter.this.notifyDataSetChanged();
                                    if (CameraUtils.getCurrentPageIndex() == 2) {
                                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                                    }
                                    if (FVCameraVideoShortcutPop.this.pop != null) {
                                        FVCameraVideoShortcutPop.this.pop.dismiss();
                                    }
                                } else if (position == 8) {
                                    Log.e("---------------", "----------  7898  9999 8878  水平仪打开  水平仪打开  --------");
                                    SPUtils.put(CameraSettingAdapter.this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_OPEN));
                                    Util.sendIntEventMessge(Constants.INIT_GRADIENTER_VIEW);
                                }
                            } else if (position == 6) {
                                Log.e("-----------", "------- 555 -- Checked No ------");
                                CameraExclusiveUtils.setCamHandModelExclusive(CameraSettingAdapter.this.context, FVCameraVideoShortcutPop.this.cameraManager, false);
                            } else if (position == 8) {
                                Log.e("---------------", "----------  7898  9999 8878  水平仪关闭  水平仪关闭  --------");
                                SPUtils.put(CameraSettingAdapter.this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE));
                                Util.sendIntEventMessge(Constants.REMOVE_GRADIENTER_VIEW);
                            }
                        }
                    });
                }
            }
            return convertView;
        }
    }

    /* access modifiers changed from: private */
    public void showTimeLapsePop() {
        if (!FileUtils.takeVideoMemoryEnough(this.context)) {
            Toast.makeText(this.context, C0853R.string.label_available_memory_not_enough, 0).show();
            return;
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            toastAboutMarkPointCancelTV();
        }
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        if (CameraUtils.isFollowIng()) {
        }
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVTimeLapsePop timeLapsePop = new FVTimeLapsePop();
        timeLapsePop.init(this.context, this.parentView);
        PopupWindow pop2 = new PopupWindow(timeLapsePop.getView(), height2, height2, true);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation2);
        pop2.setOutsideTouchable(true);
        timeLapsePop.setPop(pop2, timeLapsePop);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        Util.fullScreenImmersive(pop2.getContentView());
    }

    /* access modifiers changed from: private */
    public void showMoveTimeLapsePop() {
        if (!FileUtils.takeVideoMemoryEnough(this.context)) {
            Toast.makeText(this.context, C0853R.string.label_available_memory_not_enough, 0).show();
            return;
        }
        if (CameraUtils.getCurrentPageIndex() == 2) {
            toastAboutMarkPointCancelTV();
        }
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        if (CameraUtils.isFollowIng()) {
            Util.sendIntEventMessge(Constants.EXCLUSIVE_CLOSE_KCF);
        }
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVMoveTimelapsePop moveTimeLapsePop = new FVMoveTimelapsePop();
        moveTimeLapsePop.init(this.context, this.parentView, false);
        PopupWindow pop2 = new PopupWindow(moveTimeLapsePop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation2);
        pop2.setOutsideTouchable(false);
        moveTimeLapsePop.setPop(pop2, moveTimeLapsePop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }
}
