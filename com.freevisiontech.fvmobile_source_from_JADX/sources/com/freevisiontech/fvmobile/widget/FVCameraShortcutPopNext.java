package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.filterlib.FilterType;
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
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVCameraShortcutPopNext {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public CameraSettingAdapter adapter;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    private FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public List<CameraSecondItem> datas;
    private int datasSize = 0;
    private int height;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public ListView listview;
    private int mPosition;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVCameraShortcutPopNext.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVCameraShortcutPopNext.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVCameraShortcutPopNext.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVCameraShortcutPopNext.this.setHorUiNinety270();
                    return;
                case 50:
                    FVCameraShortcutPopNext.this.listview.smoothScrollToPositionFromTop(msg.arg1, 0);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public PopupWindow pop;
    /* access modifiers changed from: private */
    public int selectPosition = -1;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    /* access modifiers changed from: private */
    public boolean stirUpDown = false;
    private View view;

    public void init(Context context2, int id, int position, List<CameraSecondItem> datas2) {
        this.context = context2;
        this.mPosition = position;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_camera_shortcut_pop_new, (ViewGroup) null);
        ((TextView) this.view.findViewById(C0853R.C0855id.title)).setText(id);
        View btnBack = this.view.findViewById(C0853R.C0855id.btn_back);
        btnBack.setVisibility(0);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraShortcutPopNext.this.pop != null) {
                    FVCameraShortcutPopNext.this.pop.dismiss();
                    Util.sendIntEventMessge(10007);
                }
            }
        });
        this.listview = (ListView) this.view.findViewById(C0853R.C0855id.listView);
        this.datas = datas2;
        initData();
        itemClick();
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
                if (FVCameraShortcutPopNext.this.pop != null) {
                    FVCameraShortcutPopNext.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraShortcutPopNext.this.pop != null) {
                    FVCameraShortcutPopNext.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.datasSize = datas2.size();
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirUpDown = true;
            this.stirPosition = 0;
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (FVCameraShortcutPopNext.this.pop != null && CameraUtils.getFrameLayerNumber() == 2 && FVCameraShortcutPopNext.this.adapter != null) {
                        BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
                    }
                }
            }, 200);
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
                FVCameraShortcutPopNext.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVCameraShortcutPopNext.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVCameraShortcutPopNext.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVCameraShortcutPopNext.this.sendToHandler(13);
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

    private void itemClick() {
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (FVCameraShortcutPopNext.this.selectPosition != position) {
                    int visiblePosition = FVCameraShortcutPopNext.this.listview.getFirstVisiblePosition();
                    ((ViewHolder) view.getTag()).btnSelect.setVisibility(0);
                    View childAt = FVCameraShortcutPopNext.this.listview.getChildAt(FVCameraShortcutPopNext.this.selectPosition - visiblePosition);
                    if (childAt != null) {
                        ((ViewHolder) childAt.getTag()).btnSelect.setVisibility(8);
                    }
                    if (FVCameraShortcutPopNext.this.selectPosition != -1) {
                        ((CameraSecondItem) FVCameraShortcutPopNext.this.datas.get(FVCameraShortcutPopNext.this.selectPosition)).isItemSelect = false;
                    }
                    ((CameraSecondItem) FVCameraShortcutPopNext.this.datas.get(position)).isItemSelect = true;
                    int unused = FVCameraShortcutPopNext.this.selectPosition = position;
                    FVCameraShortcutPopNext.this.sendMessageEvent();
                }
            }
        });
    }

    private void onClickStirPositionItemCustomKey(View view2, int position) {
        int visiblePosition = this.listview.getFirstVisiblePosition();
        ((ViewHolder) view2.getTag()).btnSelect.setVisibility(0);
        View childAt = this.listview.getChildAt(this.selectPosition - visiblePosition);
        if (childAt != null) {
            ((ViewHolder) childAt.getTag()).btnSelect.setVisibility(8);
        }
        if (this.selectPosition != -1) {
            this.datas.get(this.selectPosition).isItemSelect = false;
        }
        this.datas.get(position).isItemSelect = true;
        this.selectPosition = position;
        this.myHandler.removeMessages(50);
        Message message = Message.obtain();
        message.what = 50;
        message.arg1 = position;
        this.myHandler.sendMessageDelayed(message, 10);
    }

    private void onClickStirPositionItem(View view2, int position) {
        if (this.selectPosition != position) {
            int visiblePosition = this.listview.getFirstVisiblePosition();
            ((ViewHolder) view2.getTag()).btnSelect.setVisibility(0);
            View childAt = this.listview.getChildAt(this.selectPosition - visiblePosition);
            if (childAt != null) {
                ((ViewHolder) childAt.getTag()).btnSelect.setVisibility(8);
            }
            if (this.selectPosition != -1) {
                this.datas.get(this.selectPosition).isItemSelect = false;
            }
            this.datas.get(position).isItemSelect = true;
            this.selectPosition = position;
            sendMessageEvent();
            this.myHandler.removeMessages(50);
            Message message = Message.obtain();
            message.what = 50;
            message.arg1 = position;
            this.myHandler.sendMessageDelayed(message, 10);
        }
    }

    /* access modifiers changed from: private */
    public void sendMessageEvent() {
        switch (this.mPosition) {
            case 0:
                if (this.selectPosition == 0) {
                    CameraExclusiveUtils.setFilterExclusive(this.context, this.cameraManager, this.selectPosition);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
                    if (((Integer) SPUtils.get(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue() != 10019) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Util.sendIntEventMessge(Constants.CAMERA_RESET_WHITE_BALANCE);
                            }
                        }, 1000);
                        return;
                    }
                    return;
                }
                CameraExclusiveUtils.setFilterExclusive(this.context, this.cameraManager, this.selectPosition);
                if (this.selectPosition == 1) {
                    setFilter(FilterType.SEPIA);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_SEPIA_MODE));
                    return;
                } else if (this.selectPosition == 2) {
                    setFilter(FilterType.GRAYSCALE);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_GRAYSCALE_MODE));
                    return;
                } else if (this.selectPosition == 3) {
                    setFilter(FilterType.SKETCH);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_SKETCH_MODE));
                    return;
                } else if (this.selectPosition == 4) {
                    setFilter(FilterType.LOOKUP_AMATORKA);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_LOOKUP_MODE));
                    return;
                } else if (this.selectPosition == 5) {
                    setFilter(FilterType.TONE_CURVE);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_TONECARVE_MODE));
                    return;
                } else if (this.selectPosition == 6) {
                    setFilter(FilterType.EMBOSS);
                    SPUtils.put(this.context, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_EMBOSS_MODE));
                    return;
                } else {
                    return;
                }
            case 1:
                CameraExclusiveUtils.setLapsePhotoExclusive(this.context, this.cameraManager, this.selectPosition);
                if (this.selectPosition == 0) {
                    SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_CLOSE);
                    return;
                } else if (this.selectPosition == 1) {
                    SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_2S));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_2S);
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                    return;
                } else if (this.selectPosition == 2) {
                    SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_5S));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_5S);
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                    return;
                } else {
                    SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_10S));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_10S);
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                    return;
                }
            case 2:
                CameraExclusiveUtils.setFullShotExclusive(this.context, this.cameraManager, this.selectPosition);
                if (this.selectPosition == 0) {
                    SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                    Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
                    return;
                } else if (this.selectPosition == 1) {
                    Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected.booleanValue()) {
                        SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_180));
                        Util.sendIntEventMessge(Constants.FULL_SHOT_180);
                        Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            toastAboutMarkPointCancelTV();
                            return;
                        }
                        return;
                    }
                    EventBusUtil.sendEvent(new Event(153));
                    return;
                } else if (this.selectPosition == 2) {
                    Boolean isConnected2 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected2.booleanValue()) {
                        SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_330));
                        Util.sendIntEventMessge(Constants.FULL_SHOT_330);
                        Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            toastAboutMarkPointCancelTV();
                            return;
                        }
                        return;
                    }
                    EventBusUtil.sendEvent(new Event(153));
                    return;
                } else if (this.selectPosition == 3) {
                    Boolean isConnected3 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected3.booleanValue()) {
                        SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_3X3));
                        Util.sendIntEventMessge(Constants.FULL_SHOT_3X3);
                        Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            toastAboutMarkPointCancelTV();
                            return;
                        }
                        return;
                    }
                    EventBusUtil.sendEvent(new Event(153));
                    return;
                } else if (this.selectPosition == 4) {
                    Boolean isConnected4 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() != 1 || !isConnected4.booleanValue()) {
                        SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_3X5));
                        Util.sendIntEventMessge(Constants.FULL_SHOT_3X5);
                        Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                        if (CameraUtils.getCurrentPageIndex() == 2) {
                            toastAboutMarkPointCancelTV();
                            return;
                        }
                        return;
                    }
                    EventBusUtil.sendEvent(new Event(153));
                    return;
                } else {
                    return;
                }
            case 3:
                CameraExclusiveUtils.setLongExposureExclusive(this.context, this.cameraManager, this.selectPosition);
                if (this.selectPosition == 0) {
                    SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
                    Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                    Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
                    return;
                } else if (this.selectPosition == 1) {
                    SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_DOUBLE_IMAGE));
                    Util.sendIntEventMessge(Constants.LONG_EXPOSURE_DOUBLE_IMAGE);
                    Util.sendIntEventMessge(Constants.LONG_EXPOSURE_OPEN);
                    return;
                } else if (this.selectPosition == 2) {
                    SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_TRACK));
                    Util.sendIntEventMessge(Constants.LONG_EXPOSURE_TRACK);
                    Util.sendIntEventMessge(Constants.LONG_EXPOSURE_OPEN);
                    return;
                } else {
                    return;
                }
            case 6:
                if (this.selectPosition == 0) {
                    SPUtils.put(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
                    setWB(Constants.SCENE_MODE_AUTO);
                    return;
                } else if (this.selectPosition == 1) {
                    SPUtils.put(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_SUNSHINE));
                    setWB("daylight");
                    return;
                } else if (this.selectPosition == 2) {
                    SPUtils.put(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_OVERCAST));
                    setWB("cloudy-daylight");
                    return;
                } else if (this.selectPosition == 3) {
                    SPUtils.put(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_FLUORESCENT_LAMP));
                    setWB("fluorescent");
                    return;
                } else if (this.selectPosition == 4) {
                    SPUtils.put(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_INCANDESCENT_LAMP));
                    setWB("incandescent");
                    return;
                } else {
                    return;
                }
            case 7:
                if (this.selectPosition == 0) {
                    Util.sendIntEventMessge(Constants.GRIDING_NONE);
                    SPUtils.put(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_NONE));
                    return;
                } else if (this.selectPosition == 1) {
                    Util.sendIntEventMessge(Constants.GRIDING_GRIDVIEW);
                    SPUtils.put(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_GRIDVIEW));
                    return;
                } else if (this.selectPosition == 2) {
                    Util.sendIntEventMessge(Constants.GRIDING_GRIDVIEW_DIAGONAL_LINE);
                    SPUtils.put(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_GRIDVIEW_DIAGONAL_LINE));
                    return;
                } else if (this.selectPosition == 3) {
                    Util.sendIntEventMessge(Constants.GRIDING_CENTER_POINT);
                    SPUtils.put(this.context, SharePrefConstant.GRIDING_MODE, Integer.valueOf(Constants.GRIDING_CENTER_POINT));
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private void toastAboutMarkPointCancelTV() {
        if (CameraUtils.getCurrentPageIndex() == 2 && CameraUtils.getMarkPointUIIsVisible()) {
            EventBusUtil.sendEvent(new Event(145));
        }
    }

    public void setPop(PopupWindow pop2, final FVCameraShortcutPopNext shortcutPopNext) {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
        }
        CameraUtils.setFrameLayerNumber(2);
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    if (FVCameraShortcutPopNext.this.broad != null) {
                        FVCameraShortcutPopNext.this.context.unregisterReceiver(FVCameraShortcutPopNext.this.broad);
                    }
                    if (EventBus.getDefault().isRegistered(shortcutPopNext)) {
                        EventBus.getDefault().unregister(shortcutPopNext);
                    }
                    CameraUtils.setFrameLayerNumber(0);
                    if (Boolean.valueOf(ViseBluetooth.getInstance().isConnected()).booleanValue() && CameraUtils.getCurrentPageIndex() == 2) {
                        BleByteUtil.setPTZParameters((byte) 71, (byte) 0);
                    }
                }
            });
        }
    }

    public void unRegisterListener() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                }
                return;
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                onClickStirPositionItem(this.listview.getChildAt(this.stirPosition - this.listview.getFirstVisiblePosition()), this.stirPosition);
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraShortcutPopNext" + CameraUtils.getFrameLayerNumber());
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.pop.dismiss();
                    Util.sendIntEventMessge(10007);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.CAMERA_WB_MODE_WINDOW_CHANGE:
                int wbMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO))).intValue();
                if (wbMode == 10019) {
                    onClickStirPositionItemCustomKey(this.listview.getChildAt(0), 0);
                    return;
                } else if (wbMode == 10020) {
                    onClickStirPositionItemCustomKey(this.listview.getChildAt(1), 1);
                    return;
                } else if (wbMode == 10021) {
                    onClickStirPositionItemCustomKey(this.listview.getChildAt(2), 2);
                    return;
                } else if (wbMode == 10022) {
                    onClickStirPositionItemCustomKey(this.listview.getChildAt(3), 3);
                    return;
                } else if (wbMode == 10023) {
                    onClickStirPositionItemCustomKey(this.listview.getChildAt(4), 4);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_ROCKING_BAR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    FM210labelCameraStirUp();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_ROCKING_BAR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    FM210labelCameraStirDown();
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void FM210labelCameraStirUp() {
        if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
            this.stirUpDown = true;
            this.stirPosition--;
            if (this.stirPosition < 0) {
                this.stirPosition = 0;
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            this.listview.setSelectionFromTop(this.stirPosition, 0);
            Log.e("-----------------", "----------  7777  8888  9999   -------  210 波轮拨动向上   向上   向上 " + this.stirPosition);
        }
    }

    private void FM210labelCameraStirDown() {
        if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
            this.stirUpDown = true;
            this.stirPosition++;
            if (this.stirPosition > this.datasSize - 1) {
                this.stirPosition = this.datasSize - 1;
            }
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
            this.listview.setSelectionFromTop(this.stirPosition, 0);
            Log.e("-----------------", "----------  7777  8888  9999   -------  波轮拨动向下   向下   向下" + this.stirPosition);
        }
    }

    public View getView() {
        return this.view;
    }

    private void initData() {
        if (this.datas != null) {
            this.adapter = new CameraSettingAdapter();
            this.listview.setAdapter(this.adapter);
        }
    }

    private class CameraSettingAdapter extends BaseAdapter {
        private CameraSettingAdapter() {
        }

        public int getCount() {
            return FVCameraShortcutPopNext.this.datas.size();
        }

        public Object getItem(int position) {
            return FVCameraShortcutPopNext.this.datas.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            ViewHolder holder2;
            CameraSecondItem cameraItem = (CameraSecondItem) FVCameraShortcutPopNext.this.datas.get(position);
            if (cameraItem.type == 0) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(FVCameraShortcutPopNext.this.context).inflate(C0853R.layout.layout_camera_seccond_item, (ViewGroup) null);
                    holder2 = new ViewHolder();
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder) convertView.getTag();
                }
                RelativeLayout layout_camera_second_item_relative = (RelativeLayout) convertView.findViewById(C0853R.C0855id.layout_camera_second_item_relative);
                layout_camera_second_item_relative.setBackgroundColor(FVCameraShortcutPopNext.this.context.getResources().getColor(C0853R.color.color_white));
                if (FVCameraShortcutPopNext.this.stirUpDown && position == FVCameraShortcutPopNext.this.stirPosition) {
                    layout_camera_second_item_relative.setBackgroundColor(FVCameraShortcutPopNext.this.context.getResources().getColor(C0853R.color.black15));
                }
                if (cameraItem.ItemType == 1) {
                    TextView unused = holder2.textView = (TextView) convertView.findViewById(C0853R.C0855id.text);
                    holder2.textView.setText(cameraItem.itemID);
                    holder2.textView.setVisibility(0);
                } else {
                    ImageView unused2 = holder2.image = (ImageView) convertView.findViewById(C0853R.C0855id.image);
                    holder2.image.setImageResource(cameraItem.itemID);
                    holder2.image.setVisibility(0);
                }
                ImageView unused3 = holder2.btnSelect = (ImageView) convertView.findViewById(C0853R.C0855id.btn_select);
                if (cameraItem.isItemSelect) {
                    holder2.btnSelect.setVisibility(0);
                    int unused4 = FVCameraShortcutPopNext.this.selectPosition = position;
                } else {
                    holder2.btnSelect.setVisibility(8);
                }
            } else if (cameraItem.type == 1) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(FVCameraShortcutPopNext.this.context).inflate(C0853R.layout.layout_camera_seccond_item2, (ViewGroup) null);
                    holder = new ViewHolder();
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                RelativeLayout layout_camera_second_item_relative2 = (RelativeLayout) convertView.findViewById(C0853R.C0855id.layout_camera_second_item_relative);
                layout_camera_second_item_relative2.setBackgroundColor(FVCameraShortcutPopNext.this.context.getResources().getColor(C0853R.color.color_white));
                if (FVCameraShortcutPopNext.this.stirUpDown && position == FVCameraShortcutPopNext.this.stirPosition) {
                    layout_camera_second_item_relative2.setBackgroundColor(FVCameraShortcutPopNext.this.context.getResources().getColor(C0853R.color.black15));
                }
                TextView unused5 = holder.textView = (TextView) convertView.findViewById(C0853R.C0855id.text);
                holder.textView.setText(cameraItem.textID);
                if (Util.isZh(FVCameraShortcutPopNext.this.context)) {
                    holder.textView.setTextSize(1, 14.0f);
                } else {
                    holder.textView.setTextSize(1, 12.0f);
                }
                holder.textView.setVisibility(0);
                ImageView unused6 = holder.image = (ImageView) convertView.findViewById(C0853R.C0855id.image);
                holder.image.setImageResource(cameraItem.iconID);
                holder.image.setVisibility(0);
                ImageView unused7 = holder.btnSelect = (ImageView) convertView.findViewById(C0853R.C0855id.btn_select);
                if (cameraItem.isItemSelect) {
                    holder.btnSelect.setVisibility(0);
                    int unused8 = FVCameraShortcutPopNext.this.selectPosition = position;
                } else {
                    holder.btnSelect.setVisibility(8);
                }
            }
            return convertView;
        }
    }

    private class ViewHolder {
        /* access modifiers changed from: private */
        public ImageView btnSelect;
        /* access modifiers changed from: private */
        public ImageView image;
        /* access modifiers changed from: private */
        public TextView textView;

        private ViewHolder() {
        }
    }

    private void setWB(String mode) {
        this.cameraManager.setWhiteBalance(mode);
    }

    private void setFilter(FilterType filterType) {
        this.cameraManager.setFilter(filterType);
    }
}
