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
import android.widget.TextView;
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

public class FVCameraShortcutPop {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private CameraSettingAdapter adapter;
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
                    FVCameraShortcutPop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVCameraShortcutPop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVCameraShortcutPop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVCameraShortcutPop.this.setHorUiNinety270();
                    return;
                case 50:
                    FVCameraShortcutPop.this.listview.smoothScrollToPositionFromTop(msg.arg1, 0);
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
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_camera_shortcut_pop_new2, (ViewGroup) null);
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
        this.listview = (ListView) this.view.findViewById(C0853R.C0855id.listView);
        initData();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.layout_camera_shortcut_pop_horizontal_bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraShortcutPop.this.pop != null) {
                    FVCameraShortcutPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraShortcutPop.this.pop != null) {
                    FVCameraShortcutPop.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        CameraUtils.setFrameLayerNumber(1);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirUpDown = true;
            if (this.cameraManager.getCameraManagerType() == 1) {
                this.stirPosition = 0;
            } else {
                this.stirPosition = 1;
            }
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
                FVCameraShortcutPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVCameraShortcutPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVCameraShortcutPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVCameraShortcutPop.this.sendToHandler(13);
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
                    FVCameraShortcutPop.this.context.unregisterReceiver(FVCameraShortcutPop.this.broad);
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
        this.datas.add(new CameraItem(C0853R.string.label_filter, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_delay_photo, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_full_view, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_long_exposure, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_beauty, 2, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_hdr, 2, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_white_balance, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_griding, 0, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_camera_hand_model, 2, (Class) null));
        this.datas.add(new CameraItem(C0853R.string.label_gradienter, 2, (Class) null));
        this.adapter = new CameraSettingAdapter(this.context);
        this.adapter.setList(this.datas);
        this.listview.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        int filterMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
                        List<CameraSecondItem> ItemDates4 = new ArrayList<>();
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_griding_none), C0853R.mipmap.filter_none, filterMode == 10300));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_sepia), C0853R.mipmap.filter_sepia, filterMode == 10301));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_grayscale), C0853R.mipmap.filter_grayscale, filterMode == 10302));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_sketch), C0853R.mipmap.filter_sketch, filterMode == 10306));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_lookup_amatorka), C0853R.mipmap.filter_lookup_amatorka, filterMode == 10307));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_tonecurve), C0853R.mipmap.filter_tonecurve, filterMode == 10308));
                        ItemDates4.add(new CameraSecondItem(1, FVCameraShortcutPop.this.context.getString(C0853R.string.label_filter_emboss), C0853R.mipmap.filter_emboss, filterMode == 10310));
                        FVCameraShortcutPopNext cameraShortcutPopNext4 = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext4.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates4);
                        FVCameraShortcutPop.this.showPop(cameraShortcutPopNext4);
                        return;
                    case 1:
                        int delayMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
                        List<CameraSecondItem> ItemDates = new ArrayList<>();
                        ItemDates.add(new CameraSecondItem(C0853R.string.label_no_delay, 1, delayMode == 100011));
                        ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_2s_small, 2, delayMode == 100012));
                        ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_5s_small, 2, delayMode == 100013));
                        ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_10s_small, 2, delayMode == 10014));
                        FVCameraShortcutPopNext cameraShortcutPopNext = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates);
                        FVCameraShortcutPop.this.showPop(cameraShortcutPopNext);
                        return;
                    case 2:
                        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                        MoveTimelapseUtil.getInstance();
                        if (MoveTimelapseUtil.getCameraFvShareSleep() == 1 && isConnected.booleanValue()) {
                            EventBusUtil.sendEvent(new Event(153));
                            return;
                        } else if (Util.isPovReverPano(FVCameraShortcutPop.this.context)) {
                            int fsMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                            List<CameraSecondItem> ItemDates2 = new ArrayList<>();
                            ItemDates2.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, fsMode == 10024));
                            ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_180_small, 2, fsMode == 10025));
                            ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_330_small, 2, fsMode == 10026));
                            ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_3_3_small, 2, fsMode == 10027));
                            ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_3_5_small, 2, fsMode == 10028));
                            FVCameraShortcutPopNext cameraShortcutPopNext2 = new FVCameraShortcutPopNext();
                            cameraShortcutPopNext2.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates2);
                            FVCameraShortcutPop.this.showPop(cameraShortcutPopNext2);
                            return;
                        } else {
                            return;
                        }
                    case 3:
                        int leMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
                        List<CameraSecondItem> ItemDates3 = new ArrayList<>();
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, leMode == 106205));
                        ItemDates3.add(new CameraSecondItem(C0853R.string.label_long_exposure_of_double_image, 1, leMode == 106206));
                        FVCameraShortcutPopNext cameraShortcutPopNext3 = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext3.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates3);
                        FVCameraShortcutPop.this.showPop(cameraShortcutPopNext3);
                        return;
                    case 6:
                        int wbMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
                        List<CameraSecondItem> ItemDates7 = new ArrayList<>();
                        ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_auto, 1, wbMode == 10019));
                        ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_sunshine, 1, wbMode == 10020));
                        ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_overcast, 1, wbMode == 10021));
                        ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_fluorescent_lamp, 1, wbMode == 10022));
                        ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_incandescent_lamp, 1, wbMode == 10023));
                        FVCameraShortcutPopNext cameraShortcutPopNext7 = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext7.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates7);
                        FVCameraShortcutPop.this.showPop(cameraShortcutPopNext7);
                        return;
                    case 7:
                        int gridMode = ((Integer) SPUtils.get(FVCameraShortcutPop.this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
                        List<CameraSecondItem> ItemDates8 = new ArrayList<>();
                        ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, gridMode == 10015));
                        ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_gridView, 1, gridMode == 10017));
                        ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_gridView_diagonal_line, 1, gridMode == 10018));
                        ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_center_point, 1, gridMode == 10016));
                        FVCameraShortcutPopNext cameraShortcutPopNext8 = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext8.init(FVCameraShortcutPop.this.context, ((CameraItem) FVCameraShortcutPop.this.datas.get(position)).f1105id, position, ItemDates8);
                        FVCameraShortcutPop.this.showPop(cameraShortcutPopNext8);
                        return;
                    default:
                        return;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void showPop(final FVCameraShortcutPopNext shortcutPopNext) {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        this.popupWindow = new PopupWindow(shortcutPopNext.getView(), height2, height2, true);
        shortcutPopNext.setPop(this.popupWindow, shortcutPopNext);
        this.popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(C0853R.style.popAnimation2);
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                CameraUtils.setFrameLayerNumber(0);
                shortcutPopNext.unRegisterListener();
                EventBus.getDefault().unregister(shortcutPopNext);
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
                if (this.pop != null) {
                    this.pop.dismiss();
                }
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraShortcutPop" + CameraUtils.getFrameLayerNumber());
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
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraShortcutPop");
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
        if (pos == 4) {
            this.switchButtonItemValue = true;
        } else if (pos == 5) {
            this.switchButtonItemValue = true;
        } else if (pos == 8) {
            this.switchButtonItemValue = true;
        } else if (pos == 9) {
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
                    if (this.stirPosition == 0) {
                        this.stirPosition = 1;
                    } else if (this.stirPosition == 3) {
                        this.stirPosition = 5;
                    } else if (this.stirPosition == 4) {
                        this.stirPosition = 2;
                    } else if (this.stirPosition == 8) {
                        this.stirPosition = 7;
                    }
                } else if (this.stirPosition == 0) {
                    this.stirPosition = 1;
                } else if (this.stirPosition == 3) {
                    this.stirPosition = 5;
                } else if (this.stirPosition == 4) {
                    this.stirPosition = 2;
                }
            } else if (this.stirPosition == 3) {
                this.stirPosition = 2;
            } else if (this.stirPosition == 8) {
                this.stirPosition = 7;
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
            if (this.stirPosition > 9) {
                this.stirPosition = 9;
            }
            int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
            if (this.cameraManager.getCameraManagerType() == 2) {
                if (cameraLevel == 2) {
                    if (this.stirPosition == 0) {
                        this.stirPosition = 1;
                    } else if (this.stirPosition == 3) {
                        this.stirPosition = 5;
                    } else if (this.stirPosition == 4) {
                        this.stirPosition = 2;
                    } else if (this.stirPosition == 8) {
                        this.stirPosition = 7;
                    }
                } else if (this.stirPosition == 0) {
                    this.stirPosition = 1;
                } else if (this.stirPosition == 3) {
                    this.stirPosition = 5;
                } else if (this.stirPosition == 4) {
                    this.stirPosition = 2;
                }
            } else if (this.stirPosition == 3) {
                this.stirPosition = 4;
            } else if (this.stirPosition == 8) {
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
                int filterMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE))).intValue();
                List<CameraSecondItem> ItemDates4 = new ArrayList<>();
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_griding_none), C0853R.mipmap.filter_none, filterMode == 10300));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_sepia), C0853R.mipmap.filter_sepia, filterMode == 10301));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_grayscale), C0853R.mipmap.filter_grayscale, filterMode == 10302));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_sketch), C0853R.mipmap.filter_sketch, filterMode == 10306));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_lookup_amatorka), C0853R.mipmap.filter_lookup_amatorka, filterMode == 10307));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_tonecurve), C0853R.mipmap.filter_tonecurve, filterMode == 10308));
                ItemDates4.add(new CameraSecondItem(1, this.context.getString(C0853R.string.label_filter_emboss), C0853R.mipmap.filter_emboss, filterMode == 10310));
                FVCameraShortcutPopNext cameraShortcutPopNext4 = new FVCameraShortcutPopNext();
                cameraShortcutPopNext4.init(this.context, this.datas.get(position).f1105id, position, ItemDates4);
                showPop(cameraShortcutPopNext4);
                break;
            case 1:
                int delayMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
                List<CameraSecondItem> ItemDates = new ArrayList<>();
                ItemDates.add(new CameraSecondItem(C0853R.string.label_no_delay, 1, delayMode == 100011));
                ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_2s_small, 2, delayMode == 100012));
                ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_5s_small, 2, delayMode == 100013));
                ItemDates.add(new CameraSecondItem(C0853R.mipmap.ic_delay_10s_small, 2, delayMode == 10014));
                FVCameraShortcutPopNext cameraShortcutPopNext = new FVCameraShortcutPopNext();
                cameraShortcutPopNext.init(this.context, this.datas.get(position).f1105id, position, ItemDates);
                showPop(cameraShortcutPopNext);
                break;
            case 2:
                Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                MoveTimelapseUtil.getInstance();
                if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected.booleanValue()) {
                    if (Util.isPovReverPano(this.context)) {
                        int fsMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                        List<CameraSecondItem> ItemDates2 = new ArrayList<>();
                        ItemDates2.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, fsMode == 10024));
                        ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_180_small, 2, fsMode == 10025));
                        ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_330_small, 2, fsMode == 10026));
                        ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_3_3_small, 2, fsMode == 10027));
                        ItemDates2.add(new CameraSecondItem(C0853R.mipmap.ic_3_5_small, 2, fsMode == 10028));
                        FVCameraShortcutPopNext cameraShortcutPopNext2 = new FVCameraShortcutPopNext();
                        cameraShortcutPopNext2.init(this.context, this.datas.get(position).f1105id, position, ItemDates2);
                        showPop(cameraShortcutPopNext2);
                        break;
                    }
                } else {
                    EventBusUtil.sendEvent(new Event(153));
                    break;
                }
                break;
            case 3:
                int leMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
                List<CameraSecondItem> ItemDates3 = new ArrayList<>();
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, leMode == 106205));
                ItemDates3.add(new CameraSecondItem(C0853R.string.label_long_exposure_of_double_image, 1, leMode == 106206));
                FVCameraShortcutPopNext cameraShortcutPopNext3 = new FVCameraShortcutPopNext();
                cameraShortcutPopNext3.init(this.context, this.datas.get(position).f1105id, position, ItemDates3);
                showPop(cameraShortcutPopNext3);
                break;
            case 4:
                if (((Integer) SPUtils.get(this.context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue() == 10401) {
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                    Util.sendIntEventMessge(Constants.FV_BEAUTY_POP_SHOW);
                    CameraExclusiveUtils.setBeautyExclusive(this.context, this.cameraManager, true);
                } else {
                    CameraExclusiveUtils.setBeautyExclusive(this.context, this.cameraManager, false);
                }
                this.adapter.notifyDataSetChanged();
                break;
            case 5:
                if (((Integer) SPUtils.get(this.context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue() == 106100) {
                    CameraExclusiveUtils.setHDRExclusive(this.context, this.cameraManager, true);
                } else {
                    CameraExclusiveUtils.setHDRExclusive(this.context, this.cameraManager, false);
                }
                this.adapter.notifyDataSetChanged();
                break;
            case 6:
                int wbMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
                List<CameraSecondItem> ItemDates7 = new ArrayList<>();
                ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_auto, 1, wbMode == 10019));
                ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_sunshine, 1, wbMode == 10020));
                ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_overcast, 1, wbMode == 10021));
                ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_fluorescent_lamp, 1, wbMode == 10022));
                ItemDates7.add(new CameraSecondItem(C0853R.string.label_wb_incandescent_lamp, 1, wbMode == 10023));
                FVCameraShortcutPopNext cameraShortcutPopNext7 = new FVCameraShortcutPopNext();
                cameraShortcutPopNext7.init(this.context, this.datas.get(position).f1105id, position, ItemDates7);
                showPop(cameraShortcutPopNext7);
                break;
            case 7:
                int gridMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE))).intValue();
                List<CameraSecondItem> ItemDates8 = new ArrayList<>();
                ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_none, 1, gridMode == 10015));
                ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_gridView, 1, gridMode == 10017));
                ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_gridView_diagonal_line, 1, gridMode == 10018));
                ItemDates8.add(new CameraSecondItem(C0853R.string.label_griding_center_point, 1, gridMode == 10016));
                FVCameraShortcutPopNext cameraShortcutPopNext8 = new FVCameraShortcutPopNext();
                cameraShortcutPopNext8.init(this.context, this.datas.get(position).f1105id, position, ItemDates8);
                showPop(cameraShortcutPopNext8);
                break;
            case 8:
                if (((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue() == 107212) {
                    CameraExclusiveUtils.setCamHandModelExclusive(this.context, this.cameraManager, true);
                } else {
                    CameraExclusiveUtils.setCamHandModelExclusive(this.context, this.cameraManager, false);
                }
                this.adapter.notifyDataSetChanged();
                break;
            case 9:
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
        public Class cls;
        public int flag;

        /* renamed from: id */
        public int f1105id;

        public CameraItem(int _id, int _flag, Class _cs) {
            this.f1105id = _id;
            this.flag = _flag;
            this.cls = _cs;
        }
    }

    public class CameraSettingAdapter extends BaseAdapter {
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                this.holder = new ViewHolder();
                convertView = this.layoutInflater.inflate(C0853R.layout.layout_camera_item00, (ViewGroup) null);
                this.holder.textView = (TextView) convertView.findViewById(C0853R.C0855id.text);
                this.holder.textView2 = (TextView) convertView.findViewById(C0853R.C0855id.text2);
                this.holder.switchButton = (SwitchButton) convertView.findViewById(C0853R.C0855id.switchButton);
                this.holder.layout_camera_item_relative = (LinearLayout) convertView.findViewById(C0853R.C0855id.layout_camera_item_relative);
                this.holder.layout_camera_item2_relative = (LinearLayout) convertView.findViewById(C0853R.C0855id.layout_camera_item2_relative);
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
                this.holder.textView.setText(item.f1105id);
                this.holder.textView2.setText(item.f1105id);
                if (item.flag == 0) {
                    this.holder.layout_camera_item_relative.setVisibility(0);
                    this.holder.layout_camera_item2_relative.setVisibility(8);
                    if (FVCameraShortcutPop.this.cameraManager.getCameraManagerType() == 2 && position == 0) {
                        this.holder.layout_camera_item_relative_all.setVisibility(8);
                    }
                    if (position == 3) {
                        this.holder.layout_camera_item_relative_all.setVisibility(8);
                    }
                    if (FVCameraShortcutPop.this.stirUpDown) {
                        if (position == FVCameraShortcutPop.this.stirPosition) {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
                        }
                    }
                } else if (item.flag == 2) {
                    this.holder.layout_camera_item_relative.setVisibility(8);
                    this.holder.layout_camera_item2_relative.setVisibility(0);
                    int cameraLevel = FVCameraManager.GetCameraLevel(this.context);
                    if (FVCameraShortcutPop.this.cameraManager.getCameraManagerType() == 2) {
                        if (position == 4) {
                            this.holder.layout_camera_item_relative_all.setVisibility(8);
                        }
                        if (position == 8) {
                            if (cameraLevel == 2) {
                                this.holder.layout_camera_item_relative_all.setVisibility(8);
                            } else {
                                this.holder.layout_camera_item_relative_all.setVisibility(0);
                            }
                        }
                    } else if (position == 8) {
                        this.holder.layout_camera_item_relative_all.setVisibility(8);
                    }
                    if (FVCameraShortcutPop.this.stirUpDown) {
                        if (position == FVCameraShortcutPop.this.stirPosition) {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                        } else {
                            this.holder.layout_camera_item_relative_all.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
                        }
                    }
                    int beautyMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE))).intValue();
                    int hdrMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE))).intValue();
                    int cameraHandModel = ((Integer) SPUtils.get(this.context, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE))).intValue();
                    int cameraGradienter = ((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_CLOSE))).intValue();
                    if (position == 4) {
                        if (beautyMode == 10401) {
                            if (this.holder.switchButton.isChecked()) {
                                this.holder.switchButton.setChecked(false);
                            }
                        } else if (!this.holder.switchButton.isChecked()) {
                            this.holder.switchButton.setChecked(true);
                        }
                    } else if (position == 5) {
                        if (hdrMode == 106100) {
                            if (this.holder.switchButton.isChecked()) {
                                this.holder.switchButton.setChecked(false);
                            }
                        } else if (!this.holder.switchButton.isChecked()) {
                            this.holder.switchButton.setChecked(true);
                        }
                    } else if (position == 8) {
                        if (cameraHandModel == 107212) {
                            if (this.holder.switchButton.isChecked()) {
                                this.holder.switchButton.setChecked(false);
                            }
                        } else if (!this.holder.switchButton.isChecked()) {
                            this.holder.switchButton.setChecked(true);
                        }
                    } else if (position == 9) {
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
                                if (position == 5) {
                                    CameraExclusiveUtils.setHDRExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, true);
                                    CameraSettingAdapter.this.notifyDataSetChanged();
                                } else if (position == 4) {
                                    if (FVCameraShortcutPop.this.pop != null) {
                                        FVCameraShortcutPop.this.pop.dismiss();
                                    }
                                    Util.sendIntEventMessge(Constants.FV_BEAUTY_POP_SHOW);
                                    CameraExclusiveUtils.setBeautyExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, true);
                                    CameraSettingAdapter.this.notifyDataSetChanged();
                                } else if (position == 8) {
                                    Log.e("-----------", "------- 555 -- Checked Yes ------");
                                    CameraExclusiveUtils.setCamHandModelExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, true);
                                    CameraSettingAdapter.this.notifyDataSetChanged();
                                    if (CameraUtils.getCurrentPageIndex() == 2) {
                                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
                                    }
                                    if (FVCameraShortcutPop.this.pop != null) {
                                        FVCameraShortcutPop.this.pop.dismiss();
                                    }
                                } else if (position == 9) {
                                    Log.e("---------------", "----------  7898  9999 8878  水平仪打开  水平仪打开  --------");
                                    SPUtils.put(CameraSettingAdapter.this.context, SharePrefConstant.CAMERA_GRADIENTER, Integer.valueOf(Constants.GRADIENTER_OPEN));
                                    Util.sendIntEventMessge(Constants.INIT_GRADIENTER_VIEW);
                                }
                            } else if (position == 5) {
                                CameraExclusiveUtils.setHDRExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, false);
                            } else if (position == 4) {
                                CameraExclusiveUtils.setBeautyExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, false);
                                if (((Integer) SPUtils.get(CameraSettingAdapter.this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019) {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            Util.sendIntEventMessge(Constants.CAMERA_RESET_WHITE_BALANCE);
                                        }
                                    }, 1000);
                                }
                            } else if (position == 8) {
                                Log.e("-----------", "------- 555 -- Checked No ------");
                                CameraExclusiveUtils.setCamHandModelExclusive(CameraSettingAdapter.this.context, FVCameraShortcutPop.this.cameraManager, false);
                            } else if (position == 9) {
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

        private class ViewHolder {
            LinearLayout layout_camera_item2_relative;
            LinearLayout layout_camera_item_relative;
            LinearLayout layout_camera_item_relative_all;
            SwitchButton switchButton;
            TextView textView;
            TextView textView2;

            private ViewHolder() {
            }
        }
    }
}
