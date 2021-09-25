package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.base.BaseRVAdapter;
import com.freevisiontech.fvmobile.base.BaseViewHolder;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
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
import com.vise.log.ViseLog;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVMoveTimelapseTimeLinePop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    private BaseRVAdapter adapter;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    TextView btnNext;
    private TextView btnTitleText;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    private String duration;
    private int height;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    /* access modifiers changed from: private */
    public boolean isStartShootCome = false;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public ArrayList<String> list;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVMoveTimelapseTimeLinePop.this.setHorUiZero();
                    return;
                case 11:
                    FVMoveTimelapseTimeLinePop.this.setHorUiNinety();
                    return;
                case 12:
                    FVMoveTimelapseTimeLinePop.this.setHorUiZero180();
                    return;
                case 13:
                    FVMoveTimelapseTimeLinePop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    RecyclerView recyclerView;
    private String shutter;
    /* access modifiers changed from: private */
    public ArrayList<String> timeList;
    private View view;

    public void init(Context context2, View parentView2) {
        this.context = context2;
        this.parentView = parentView2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_move_time_lapse_timeline_pop, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnNext = (TextView) this.view.findViewById(C0853R.C0855id.btn_next);
        this.btnNext.setText(C0853R.string.label_start);
        this.recyclerView = (RecyclerView) this.view.findViewById(C0853R.C0855id.recyclerView);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.btnTitleText.setText(C0853R.string.label_move_delay_video);
        } else {
            this.btnTitleText.setText(C0853R.string.label_move_video);
        }
        this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 30.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        initListener();
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
                if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(false);
        CameraUtils.setMoveTimelapseRecording(true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Boolean isConnected2 = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected2.booleanValue()) {
            this.btnNext.setBackground(context2.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
        }
    }

    private class OrientationBroadPopup extends BroadcastReceiver {
        private OrientationBroadPopup() {
        }

        public void onReceive(Context context, Intent intent) {
            int orientation = intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1);
            Log.e("-------------", "----------888-- orientation --" + orientation);
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVMoveTimelapseTimeLinePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVMoveTimelapseTimeLinePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVMoveTimelapseTimeLinePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVMoveTimelapseTimeLinePop.this.sendToHandler(13);
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
        linearParams2.width = this.height / 6;
        this.layout_camera_shortcut_pop_horizontal_bottom.setLayoutParams(linearParams2);
        LinearLayout.LayoutParams linearParams3 = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_vertical_bottom_top.getLayoutParams();
        linearParams3.height = this.height;
        linearParams3.width = this.height / 6;
        this.layout_camera_shortcut_pop_vertical_bottom_top.setLayoutParams(linearParams3);
        this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.pop != null) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    Util.sendIntEventMessge(10008);
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setMoveTimelapseRecording(false);
                    this.pop.dismiss();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 5 && this.pop != null) {
                    this.btnNext.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 5 && this.pop != null) {
                    this.btnNext.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (CameraUtils.getFrameLayerNumber() == 5 && this.pop != null) {
                    startButtonOnClick();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 5) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setMoveTimelapseRecording(false);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 5) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    CameraUtils.setMoveTimelapseRecording(false);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void initListener() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.btnClose.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        MoveTimelapseUtil.getInstance().init(new MoveTimelapseUtil.MoveTimelapseListener() {
            public void isPtzDisconnected() {
                boolean unused = FVMoveTimelapseTimeLinePop.this.isConnected = false;
                FVMoveTimelapseTimeLinePop.this.stopShootAndListener();
                if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                    FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                }
            }

            public void isAddOrRemorePictrueOk(int pictrueCount, int addOrRemove, boolean outOfRange) {
            }

            public void isEveryStepTimeout(int failType, int addOrRemoveOrError) {
                if (failType == 1) {
                    Toast.makeText(FVMoveTimelapseTimeLinePop.this.context, FVMoveTimelapseTimeLinePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                        FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                    }
                } else if (failType == 2) {
                    Toast.makeText(FVMoveTimelapseTimeLinePop.this.context, FVMoveTimelapseTimeLinePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                        FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                    }
                } else if (failType == 3) {
                    Toast.makeText(FVMoveTimelapseTimeLinePop.this.context, FVMoveTimelapseTimeLinePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                        MoveTimelapseUtil.getInstance().detroy();
                        FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                    }
                } else if (failType == 4) {
                    Toast.makeText(FVMoveTimelapseTimeLinePop.this.context, FVMoveTimelapseTimeLinePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                        FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                    }
                    ViseLog.m1466e("手动开启录像超时");
                } else if (failType == 5) {
                    Toast.makeText(FVMoveTimelapseTimeLinePop.this.context, FVMoveTimelapseTimeLinePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                    if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                        FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                    }
                }
            }

            public void isPtzSendDataComeon(int point, int pointTime) {
            }

            public void isPtzStartShootComeon() {
                ViseLog.m1466e("云台发起开始录像");
                boolean unused = FVMoveTimelapseTimeLinePop.this.isStartShootCome = true;
            }

            public void isPtzAckShootingComeon() {
                ViseLog.m1466e("云台应答手动开始录像");
            }

            public void isPtzShootEnd(int type) {
                ViseLog.m1466e("录像通讯异常,退出");
                MoveTimelapseUtil.getInstance().detroy();
                if (type == 1) {
                    FVMoveTimelapseTimeLinePop.this.stopShootAndListener();
                } else if (type == 2) {
                    FVMoveTimelapseTimeLinePop.this.stopShootAndListener();
                } else if (type == 3) {
                    FVMoveTimelapseTimeLinePop.this.stopShootAndListener();
                }
            }

            public void isPtzCancelShootSuccess(boolean exit) {
                ViseLog.m1466e("FVMoveTimelapseTimeLinePop 退出延时摄影");
                if (FVMoveTimelapseTimeLinePop.this.pop != null) {
                    FVMoveTimelapseTimeLinePop.this.pop.dismiss();
                }
            }
        });
    }

    private void initAdapter() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context, 1, false));
        this.adapter = new BaseRVAdapter<String>(this.context, this.list) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.layout_move_time_lapse_timeline_item;
            }

            public void onBind(BaseViewHolder holder, int position) {
                TextView tvTime = (TextView) holder.getView(C0853R.C0855id.tv_time);
                ImageView icon = (ImageView) holder.getView(C0853R.C0855id.icon);
                View viewShadow = holder.getView(C0853R.C0855id.lineShadow);
                int llTimeLineMeasuredHeight = ((LinearLayout) holder.getView(C0853R.C0855id.ll_timeline)).getMeasuredHeight();
                if (position != 0) {
                    viewShadow.setVisibility(8);
                } else if (llTimeLineMeasuredHeight > 0) {
                    ViewGroup.LayoutParams lp = viewShadow.getLayoutParams();
                    lp.width = Util.dip2px(FVMoveTimelapseTimeLinePop.this.context, 1.0f);
                    lp.height = llTimeLineMeasuredHeight / 2;
                    viewShadow.setLayoutParams(lp);
                    viewShadow.setVisibility(0);
                } else {
                    viewShadow.setVisibility(0);
                }
                String time = (String) FVMoveTimelapseTimeLinePop.this.timeList.get(position);
                String path = (String) FVMoveTimelapseTimeLinePop.this.list.get(position);
                if (!Util.isEmpty(time)) {
                    time = Util.secToTime(Integer.valueOf(time).intValue());
                }
                tvTime.setText(time);
                Glide.with(FVMoveTimelapseTimeLinePop.this.context).load(path).into(icon);
            }
        };
        this.recyclerView.setAdapter(this.adapter);
    }

    public void setPop(PopupWindow pop2, final FVMoveTimelapseTimeLinePop timeLinePop) {
        this.pop = pop2;
        CameraUtils.setFrameLayerNumber(5);
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ViseLog.m1466e("标准模式销毁");
                    if (FVMoveTimelapseTimeLinePop.this.cameraManager == null || !FVMoveTimelapseTimeLinePop.this.cameraManager.isMediaRecording()) {
                    }
                    if (FVMoveTimelapseTimeLinePop.this.broad != null) {
                        FVMoveTimelapseTimeLinePop.this.context.unregisterReceiver(FVMoveTimelapseTimeLinePop.this.broad);
                    }
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVMoveTimelapseTimeLinePop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                    EventBus.getDefault().unregister(timeLinePop);
                }
            });
        }
    }

    public View getView() {
        return this.view;
    }

    public void setData(List<String> listPath, List<String> listTime) {
        if (listPath != null && listPath.size() > 0) {
            this.list = new ArrayList<>();
            this.list.addAll(listPath);
        }
        if (listTime != null && listTime.size() > 0) {
            this.timeList = new ArrayList<>();
            this.timeList.addAll(listTime);
            this.timeList.add(0, "0");
        }
        if (this.list != null && this.list.size() > 0 && this.timeList != null && this.timeList.size() > 0) {
            initAdapter();
        }
    }

    public void setShutterAndDuration(String shutter2, String duration2) {
        this.shutter = shutter2;
        this.duration = duration2;
    }

    private void startTimeLapseMeida(String interval) {
        String path;
        int qulity;
        float fInterval = Float.parseFloat(interval);
        int orientation = ScreenOrientationUtil.getInstance().getOrientation();
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            path = Util.getOutputMoveTimeLapseMediaFile(3, this.context).getPath();
        } else {
            path = Util.getOutputMoveLapseMediaFile(3, this.context).getPath();
        }
        CameraUtils.setMoveOrDelayTimeLapsePath(path);
        CameraUtils.setMoveOrDelayTimeLapseIng(true);
        CameraUtils.setMoveOrDelayTimeLapseShutter(fInterval);
        if (((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            if (orientation == 90) {
                orientation = 270;
            } else if (orientation == 270) {
                orientation = 90;
            }
        }
        if (((Integer) SPUtils.get(this.context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            qulity = CameraUtils.getCheckMediaRecordFrontSize();
        } else {
            qulity = CameraUtils.getCheckMediaRecordSize();
        }
        if (!MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.cameraManager.startMediaRecordEx(path, qulity, orientation);
        } else if (this.cameraManager.getCameraManagerType() == 1) {
            if (((Integer) SPUtils.get(this.context, SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
                this.cameraManager.startTimeLapseMediaRecordEx(path, 1001, orientation, (double) fInterval);
            } else {
                this.cameraManager.startTimeLapseMediaRecordEx(path, 1000, orientation, (double) fInterval);
            }
        } else if (((Integer) SPUtils.get(this.context, SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW_CAMERA2, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH))).intValue() == 107050) {
            this.cameraManager.startTimeLapseMediaRecordEx(path, 1001, orientation, (double) fInterval);
        } else {
            this.cameraManager.startTimeLapseMediaRecordEx(path, 1000, orientation, (double) fInterval);
        }
        SPUtils.put(this.context, SharePrefConstant.CURRENT_VIDEO_PATH, path);
    }

    private void timeDelayCloseLapseMedia(String duration2) {
        int time = (int) (Double.parseDouble(duration2) * 1000.0d * 60.0d);
        Log.e("--------------", "-------  time  time  time  time  -------  " + time);
        Util.sendIntEventMessge((int) Constants.MOVE_OR_DELAY_TIMELAPSE_COUNTDOWN_END, String.valueOf(time));
    }

    /* access modifiers changed from: private */
    public void stopShootAndListener() {
        timeDelayCloseLapseMedia("0");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_next:
                if (!this.isConnected) {
                    Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 1).show();
                    return;
                } else if (this.isStartShootCome) {
                    MoveTimelapseUtil.getInstance();
                    if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                        EventBusUtil.sendEvent(new Event(153));
                        return;
                    } else if (Util.isPovReverTimeLapse(this.context)) {
                        Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = true;
                        MoveTimelapseUtil.getInstance().startShoot();
                        startTimeLapseMeida(this.shutter);
                        timeDelayCloseLapseMedia(this.duration);
                        if (this.pop != null) {
                            this.pop.dismiss();
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                } else {
                    Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.wait_for_device_to_start), 1).show();
                    return;
                }
            case C0853R.C0855id.btn_close:
                if (this.isConnected) {
                    MoveTimelapseUtil.getInstance().cancelShoot();
                }
                if (this.pop != null) {
                    this.pop.dismiss();
                }
                CameraUtils.setMoveTimelapseIng(false);
                CameraUtils.setMoveTimelapseRecording(false);
                if (CameraUtils.getCurrentPageIndex() == 2) {
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void startButtonOnClick() {
        if (!this.isConnected) {
            Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.label_device_not_connected), 1).show();
        } else if (this.isStartShootCome) {
            MoveTimelapseUtil.getInstance();
            if (MoveTimelapseUtil.getCameraFvShareSleep() == 1) {
                EventBusUtil.sendEvent(new Event(153));
            } else if (Util.isPovReverTimeLapse(this.context)) {
                Constants.IS_MOVE_TIME_LAPSE_AND_SHOOTING = true;
                MoveTimelapseUtil.getInstance().startShoot();
                startTimeLapseMeida(this.shutter);
                timeDelayCloseLapseMedia(this.duration);
                if (this.pop != null) {
                    this.pop.dismiss();
                }
            }
        } else {
            Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.wait_for_device_to_start), 1).show();
        }
    }
}
