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
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.bean.CameraSecondItem;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.utils.ScreenOrientationUtil;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVCameraVideoShortcutPopNext {
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
                    FVCameraVideoShortcutPopNext.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVCameraVideoShortcutPopNext.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVCameraVideoShortcutPopNext.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVCameraVideoShortcutPopNext.this.setHorUiNinety270();
                    return;
                case 50:
                    FVCameraVideoShortcutPopNext.this.listview.smoothScrollToPositionFromTop(msg.arg1, 0);
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
                if (FVCameraVideoShortcutPopNext.this.pop != null) {
                    FVCameraVideoShortcutPopNext.this.pop.dismiss();
                    Util.sendIntEventMessge(10008);
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
                if (FVCameraVideoShortcutPopNext.this.pop != null) {
                    FVCameraVideoShortcutPopNext.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVCameraVideoShortcutPopNext.this.pop != null) {
                    FVCameraVideoShortcutPopNext.this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                }
            }
        });
        CameraUtils.setFrameLayerNumber(2);
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
                    if (FVCameraVideoShortcutPopNext.this.pop != null && CameraUtils.getFrameLayerNumber() == 2 && FVCameraVideoShortcutPopNext.this.adapter != null) {
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
                FVCameraVideoShortcutPopNext.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVCameraVideoShortcutPopNext.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVCameraVideoShortcutPopNext.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVCameraVideoShortcutPopNext.this.sendToHandler(13);
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
                if (FVCameraVideoShortcutPopNext.this.selectPosition != position) {
                    ((ViewHolder) view.getTag()).btnSelect.setVisibility(0);
                    View childAt = FVCameraVideoShortcutPopNext.this.listview.getChildAt(FVCameraVideoShortcutPopNext.this.selectPosition - FVCameraVideoShortcutPopNext.this.listview.getFirstVisiblePosition());
                    if (childAt != null) {
                        ((ViewHolder) childAt.getTag()).btnSelect.setVisibility(8);
                    }
                    if (FVCameraVideoShortcutPopNext.this.selectPosition != -1) {
                        ((CameraSecondItem) FVCameraVideoShortcutPopNext.this.datas.get(FVCameraVideoShortcutPopNext.this.selectPosition)).isItemSelect = false;
                    }
                    ((CameraSecondItem) FVCameraVideoShortcutPopNext.this.datas.get(position)).isItemSelect = true;
                    int unused = FVCameraVideoShortcutPopNext.this.selectPosition = position;
                    FVCameraVideoShortcutPopNext.this.sendMessageEvent();
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
            case 2:
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
            case 3:
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

    public void setPop(PopupWindow pop2, final FVCameraVideoShortcutPopNext videoShortcutPopNext) {
        if (CameraUtils.getCurrentPageIndex() == 2) {
            BleByteUtil.setPTZParameters((byte) 71, (byte) 1);
        }
        CameraUtils.setFrameLayerNumber(2);
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    if (FVCameraVideoShortcutPopNext.this.broad != null) {
                        FVCameraVideoShortcutPopNext.this.context.unregisterReceiver(FVCameraVideoShortcutPopNext.this.broad);
                    }
                    if (EventBus.getDefault().isRegistered(videoShortcutPopNext)) {
                        EventBus.getDefault().unregister(videoShortcutPopNext);
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

    public View getView() {
        return this.view;
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
                Log.e("-----------------", "----------  7777  8888  9999   -------  FVCameraVideoShortcutPopNext" + CameraUtils.getFrameLayerNumber());
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.pop.dismiss();
                    Util.sendIntEventMessge(10008);
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
            return FVCameraVideoShortcutPopNext.this.datas.size();
        }

        public Object getItem(int position) {
            return FVCameraVideoShortcutPopNext.this.datas.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            CameraSecondItem cameraItem = (CameraSecondItem) FVCameraVideoShortcutPopNext.this.datas.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(FVCameraVideoShortcutPopNext.this.context).inflate(C0853R.layout.layout_camera_seccond_item, (ViewGroup) null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RelativeLayout layout_camera_second_item_relative = (RelativeLayout) convertView.findViewById(C0853R.C0855id.layout_camera_second_item_relative);
            layout_camera_second_item_relative.setBackgroundColor(FVCameraVideoShortcutPopNext.this.context.getResources().getColor(C0853R.color.color_white));
            if (FVCameraVideoShortcutPopNext.this.stirUpDown && position == FVCameraVideoShortcutPopNext.this.stirPosition) {
                layout_camera_second_item_relative.setBackgroundColor(FVCameraVideoShortcutPopNext.this.context.getResources().getColor(C0853R.color.black15));
            }
            if (cameraItem.ItemType == 1) {
                TextView unused = holder.textView = (TextView) convertView.findViewById(C0853R.C0855id.text);
                holder.textView.setText(cameraItem.itemID);
                holder.textView.setVisibility(0);
            } else {
                ImageView unused2 = holder.image = (ImageView) convertView.findViewById(C0853R.C0855id.image);
                holder.image.setImageResource(cameraItem.itemID);
                holder.image.setVisibility(0);
            }
            ImageView unused3 = holder.btnSelect = (ImageView) convertView.findViewById(C0853R.C0855id.btn_select);
            if (cameraItem.isItemSelect) {
                holder.btnSelect.setVisibility(0);
                int unused4 = FVCameraVideoShortcutPopNext.this.selectPosition = position;
            } else {
                holder.btnSelect.setVisibility(8);
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
}
