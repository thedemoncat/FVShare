package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVKcfFreestyleActivity;
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
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.freevisiontech.utils.ScreenOrientationUtil;
import com.vise.log.ViseLog;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVMoveTimelapsePop implements View.OnClickListener {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public BaseRVAdapter adapter;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    TextView btnFreestyle;
    TextView btnStandard;
    private TextView btnTitleText;
    private FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    private int height;
    private boolean isBacked = false;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    /* access modifiers changed from: private */
    public boolean isOutOfRange = false;
    private LinearLayoutManager layoutManager;
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
                    FVMoveTimelapsePop.this.setHorUiZero();
                    return;
                case 11:
                    FVMoveTimelapsePop.this.setHorUiNinety();
                    return;
                case 12:
                    FVMoveTimelapsePop.this.setHorUiZero180();
                    return;
                case 13:
                    FVMoveTimelapsePop.this.setHorUiNinety270();
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
    private RecyclerView recyclerView;
    /* access modifiers changed from: private */
    public int stirPosition = -1;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.context = context2;
        this.parentView = parentView2;
        this.isBacked = isBacked2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_move_time_lapse_pop_new, (ViewGroup) null);
        this.recyclerView = (RecyclerView) this.view.findViewById(C0853R.C0855id.recyclerView);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnStandard = (TextView) this.view.findViewById(C0853R.C0855id.btn_standard);
        this.btnFreestyle = (TextView) this.view.findViewById(C0853R.C0855id.btn_freestyle);
        this.btnTitleText = (TextView) this.view.findViewById(C0853R.C0855id.btn_title_text);
        if (MoveTimelapseUtil.getMotionLapseTimeYesOrNo()) {
            this.btnTitleText.setText(C0853R.string.label_move_delay_video);
        } else {
            this.btnTitleText.setText(C0853R.string.label_move_video);
        }
        this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 30.0f);
        if ("SM-G9250".equals(Util.getSystemModel())) {
            this.height = Util.getDeviceSize(context2).y - Util.dip2px(context2, 40.0f);
        }
        if (CameraUtils.getCurrentPageIndex() == 1) {
            CameraUtils.setBlueConnectBoolean300(true);
        } else {
            CameraUtils.setBlueConnectBoolean300(false);
        }
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        initAdapter();
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
                if (FVMoveTimelapsePop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMoveTimelapsePop.this.pop != null) {
                }
            }
        });
        CameraUtils.setMoveTimelapseIng(true);
        MoveTimelapseUtil.getInstance();
        MoveTimelapseUtil.setTakenPictrueCommun(true);
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        if (CameraUtils.getCurrentPageIndex() == 2 && this.isConnected) {
            this.stirPosition = 0;
            this.adapter.notifyDataSetChanged();
            this.btnFreestyle.setBackground(context2.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
            this.btnStandard.setBackground(context2.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
            this.recyclerView.scrollToPosition(this.stirPosition);
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
                FVMoveTimelapsePop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVMoveTimelapsePop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVMoveTimelapsePop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVMoveTimelapsePop.this.sendToHandler(13);
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
        if ("SM-G9250".equals(Util.getSystemModel())) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
            lps.topMargin = Util.dip2px(this.context, 0.0f);
            this.layout_camera_shortcut_pop_out_linear.setLayoutParams(lps);
        }
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
        if ("SM-G9250".equals(Util.getSystemModel())) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
            lps.topMargin = Util.dip2px(this.context, 0.0f);
            this.layout_camera_shortcut_pop_out_linear.setLayoutParams(lps);
        }
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
        if ("SM-G9250".equals(Util.getSystemModel())) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
            lps.topMargin = Util.dip2px(this.context, 0.0f);
            this.layout_camera_shortcut_pop_out_linear.setLayoutParams(lps);
        }
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
        if ("SM-G9250".equals(Util.getSystemModel())) {
            LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams();
            lps.topMargin = Util.dip2px(this.context, 0.0f);
            this.layout_camera_shortcut_pop_out_linear.setLayoutParams(lps);
        }
    }

    private void initListener() {
        this.btnClose.setOnClickListener(this);
        this.btnStandard.setOnClickListener(this);
        this.btnFreestyle.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.isConnected = ViseBluetooth.getInstance().isConnected();
        MoveTimelapseUtil.getInstance().init(new MoveTimelapseUtil.MoveTimelapseListener() {
            public void isPtzDisconnected() {
                boolean unused = FVMoveTimelapsePop.this.isConnected = false;
                MoveTimelapseUtil.getInstance().detroy();
                if (FVMoveTimelapsePop.this.pop != null) {
                    FVMoveTimelapsePop.this.pop.dismiss();
                }
            }

            public void isAddOrRemorePictrueOk(int pictrueCount, int addOrRemove, boolean outOfRange) {
                boolean unused = FVMoveTimelapsePop.this.isOutOfRange = outOfRange;
                if (FVMoveTimelapsePop.this.isOutOfRange) {
                    FVMoveTimelapsePop.this.btnFreestyle.setEnabled(false);
                } else {
                    FVMoveTimelapsePop.this.btnFreestyle.setEnabled(true);
                }
                if (addOrRemove == 0) {
                    ViseLog.m1466e("FVMoveTimelapsePop 进行拍照");
                    SPUtils.put(FVMoveTimelapsePop.this.context, SharePrefConstant.MOVE_TIME_LAPSE_PHOTO_MODE, true);
                    Util.sendIntEventMessge(Constants.START_TAKE_PHOTO);
                } else if (addOrRemove == 1) {
                    ViseLog.m1466e("FVMoveTimelapsePop 删除位置成功" + System.currentTimeMillis());
                    FVMoveTimelapsePop.this.list.remove(pictrueCount - 1);
                    if (!FVMoveTimelapsePop.this.list.contains("click")) {
                        FVMoveTimelapsePop.this.list.add("click");
                    }
                    FVMoveTimelapsePop.this.adapter.notifyDataSetChanged();
                }
            }

            public void isEveryStepTimeout(int failType, int addOrRemoveOrError) {
                ViseLog.m1466e("FVMoveTimelapsePop 选择图片超时");
                if (failType != 0) {
                    return;
                }
                if (addOrRemoveOrError == 0) {
                    Toast.makeText(FVMoveTimelapsePop.this.context, FVMoveTimelapsePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                } else if (addOrRemoveOrError == 1) {
                    Toast.makeText(FVMoveTimelapsePop.this.context, FVMoveTimelapsePop.this.context.getResources().getString(C0853R.string.device_communication_error), 1).show();
                } else if (addOrRemoveOrError == 2) {
                    Toast.makeText(FVMoveTimelapsePop.this.context, FVMoveTimelapsePop.this.context.getResources().getString(C0853R.string.pictrue_point_is_invalid), 1).show();
                }
            }

            public void isPtzSendDataComeon(int point, int pointTime) {
            }

            public void isPtzStartShootComeon() {
            }

            public void isPtzAckShootingComeon() {
            }

            public void isPtzShootEnd(int type) {
            }

            public void isPtzCancelShootSuccess(boolean exit) {
                ViseLog.m1466e("FVMoveTimelapsePop 退出延时摄影");
                MoveTimelapseUtil.getInstance().detroy();
                if (FVMoveTimelapsePop.this.pop != null) {
                    FVMoveTimelapsePop.this.pop.dismiss();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.MOVE_TIME_LAPSE_TAKE_PHOTO:
                String path = (String) fvModeSelectEvent.getMessage();
                if (!Util.isEmpty(path) && this.list != null) {
                    this.list.add(this.list.size() - 1, path);
                    if (this.list.size() >= 11) {
                        this.list.remove(this.list.get(this.list.size() - 1));
                    }
                    this.adapter.notifyDataSetChanged();
                    this.recyclerView.scrollToPosition(this.list.size() - 1);
                    SPUtils.put(this.context, SharePrefConstant.MOVE_TIME_LAPSE_PHOTO_MODE, false);
                    return;
                }
                return;
            case Constants.MOVE_TIME_LAPSE_TAKE_PHOTO_BUTTON_BLACK:
                this.btnFreestyle.setBackgroundResource(C0853R.C0854drawable.sp_black1_round_bg);
                this.btnFreestyle.setEnabled(true);
                return;
            case Constants.MOVE_TIME_LAPSE_TAKE_PHOTO_BUTTON_GRAY:
                this.btnFreestyle.setBackgroundResource(C0853R.C0854drawable.sp_gray1_round_bg);
                this.btnFreestyle.setEnabled(false);
                return;
            case Constants.FPV_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                Log.e("--------------", "-------- 退出移动摄影, 返回上一级弹框 -----");
                if (this.pop != null) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    Util.sendIntEventMessge(10008);
                    CameraUtils.setMoveTimelapseIng(false);
                    this.pop.dismiss();
                }
                if (this.pop2 != null) {
                    FVMoveTimelapseSmoothPop.setPopDismiss(this.pop2);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.stirPosition--;
                    if (this.stirPosition < 0) {
                        this.stirPosition = 0;
                    }
                    Log.e("------------------", "-----------  5555   8888   2222   3333  --------- 向上   stirPosition:" + this.stirPosition);
                    if (this.stirPosition == this.list.size()) {
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        return;
                    } else if (this.stirPosition == this.list.size() + 1) {
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                        this.adapter.notifyDataSetChanged();
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        return;
                    } else if (this.stirPosition == this.list.size() - 1) {
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.recyclerView.scrollToPosition(this.stirPosition);
                        return;
                    } else {
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.recyclerView.scrollToPosition(this.stirPosition);
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    this.stirPosition++;
                    if (this.stirPosition > this.list.size() + 1) {
                        this.stirPosition = this.list.size() + 1;
                    }
                    Log.e("------------------", "-----------  5555   8888   2222   3333  --------- 向下   stirPosition:" + this.stirPosition);
                    if (this.stirPosition == this.list.size()) {
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        return;
                    } else if (this.stirPosition == this.list.size() + 1) {
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
                        this.adapter.notifyDataSetChanged();
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.recyclerView.scrollToPosition(this.stirPosition);
                        return;
                    } else if (this.stirPosition == this.list.size() - 1) {
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.recyclerView.scrollToPosition(this.stirPosition);
                        return;
                    } else {
                        this.adapter.notifyDataSetChanged();
                        this.btnFreestyle.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.btnStandard.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                        this.recyclerView.scrollToPosition(this.stirPosition);
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (CameraUtils.getFrameLayerNumber() == 2 && this.pop != null) {
                    if (this.stirPosition == this.list.size() - 1) {
                        if (this.isConnected) {
                            ViseLog.m1466e("position" + this.stirPosition);
                            MoveTimelapseUtil.getInstance().selectPictrueCommunication(this.stirPosition + 1, 0);
                            return;
                        }
                        Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
                        return;
                    } else if (this.stirPosition == this.list.size()) {
                        btnStandardOnClick();
                        return;
                    } else if (this.stirPosition == this.list.size() + 1) {
                        btnFreestyleOnClick();
                        return;
                    } else {
                        ViseLog.m1466e("FVMoveTimelapsePop 删除position" + this.stirPosition);
                        if (this.isConnected) {
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setDeletePictrueCommun(true);
                            MoveTimelapseUtil.getInstance().selectPictrueCommunication(this.stirPosition + 1, 1);
                            return;
                        }
                        Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                    }
                    Util.sendIntEventMessge(10008);
                    CameraUtils.setMoveTimelapseIng(false);
                    EventBus.getDefault().unregister(this);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 2) {
                    if (this.isConnected) {
                        MoveTimelapseUtil.getInstance().cancelShoot();
                    }
                    if (this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    EventBus.getDefault().unregister(this);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void initAdapter() {
        this.list = new ArrayList<>();
        if (this.isBacked) {
            ArrayList<String> selectPictruePathList = MoveTimelapseUtil.getInstance().getSelectPictruePathList();
            if (selectPictruePathList != null && selectPictruePathList.size() > 0) {
                if (selectPictruePathList.size() == 10) {
                    this.list.addAll(selectPictruePathList);
                } else {
                    this.list.addAll(selectPictruePathList);
                    this.list.add("click");
                }
            }
        } else {
            this.list.add("click");
        }
        this.layoutManager = new LinearLayoutManager(this.context, 1, false);
        this.recyclerView.setLayoutManager(this.layoutManager);
        this.adapter = new BaseRVAdapter<String>(this.context, this.list) {
            public int getLayoutId(int viewType) {
                return C0853R.layout.layout_move_time_lapse_item;
            }

            public void onBind(BaseViewHolder holder, final int position) {
                RelativeLayout rlAdd = (RelativeLayout) holder.getView(C0853R.C0855id.rl_add);
                ImageView btnAdd = (ImageView) holder.getView(C0853R.C0855id.btn_add);
                ImageView btnIcon = (ImageView) holder.getView(C0853R.C0855id.btn_icon);
                RelativeLayout rlIcon = (RelativeLayout) holder.getView(C0853R.C0855id.rl_icon);
                ImageView btnDelete = (ImageView) holder.getView(C0853R.C0855id.btn_delete);
                TextView tvDesc = (TextView) holder.getView(C0853R.C0855id.tv_desc);
                String path = (String) FVMoveTimelapsePop.this.list.get(position);
                if ("click".equals(path)) {
                    rlAdd.setVisibility(0);
                    tvDesc.setVisibility(0);
                    rlIcon.setVisibility(8);
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (FVMoveTimelapsePop.this.isConnected) {
                                ViseLog.m1466e("position" + position);
                                MoveTimelapseUtil.getInstance().selectPictrueCommunication(position + 1, 0);
                                return;
                            }
                            Toast.makeText(FVMoveTimelapsePop.this.context, C0853R.string.label_device_not_connected, 1).show();
                        }
                    });
                    rlAdd.setBackground(FVMoveTimelapsePop.this.context.getDrawable(C0853R.C0854drawable.sp_white_ebebeb_round5_bg));
                    if (position == FVMoveTimelapsePop.this.stirPosition) {
                        rlAdd.setBackground(FVMoveTimelapsePop.this.context.getDrawable(C0853R.C0854drawable.sp_yellow_stroke_round5_bg));
                        return;
                    }
                    return;
                }
                rlAdd.setVisibility(8);
                tvDesc.setVisibility(8);
                rlIcon.setVisibility(0);
                Glide.with(FVMoveTimelapsePop.this.context).load(path).into(btnIcon);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ViseLog.m1466e("FVMoveTimelapsePop 删除position" + position);
                        if (FVMoveTimelapsePop.this.isConnected) {
                            MoveTimelapseUtil.getInstance();
                            MoveTimelapseUtil.setDeletePictrueCommun(true);
                            MoveTimelapseUtil.getInstance().selectPictrueCommunication(position + 1, 1);
                            return;
                        }
                        Toast.makeText(FVMoveTimelapsePop.this.context, C0853R.string.label_device_not_connected, 1).show();
                    }
                });
                rlIcon.setBackgroundColor(FVMoveTimelapsePop.this.context.getResources().getColor(C0853R.color.transparent));
                if (position == FVMoveTimelapsePop.this.stirPosition) {
                    rlIcon.setBackground(FVMoveTimelapsePop.this.context.getDrawable(C0853R.C0854drawable.sp_yellow_stroke_round5_bg));
                }
            }
        };
        this.recyclerView.setAdapter(this.adapter);
    }

    public void setPop(PopupWindow pop3, final FVMoveTimelapsePop moveTimeLapsePop) {
        this.pop = pop3;
        if (pop3 != null) {
            pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVMoveTimelapsePop.this.context.unregisterReceiver(FVMoveTimelapsePop.this.broad);
                    EventBus.getDefault().unregister(moveTimeLapsePop);
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVMoveTimelapsePop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(2);
    }

    public View getView() {
        return this.view;
    }

    private void showMoveTimeLapseSmoothPop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int[] location = new int[2];
        this.parentView.getLocationOnScreen(location);
        int measuredWidth = location[0] + this.parentView.getMeasuredWidth();
        int dip2px = location[1] - Util.dip2px(this.context, 10.0f);
        int height2 = Util.getDisplayMetrics((Activity) this.context).heightPixels - Util.dip2px(this.context, 20.0f);
        FVMoveTimelapseSmoothPop fvMoveTimelapseSmoothPop = new FVMoveTimelapseSmoothPop();
        fvMoveTimelapseSmoothPop.init(this.context, this.parentView, false);
        this.pop2 = new PopupWindow(fvMoveTimelapseSmoothPop.getView(), height2, height2);
        this.pop2.setBackgroundDrawable(new ColorDrawable(0));
        this.pop2.setAnimationStyle(C0853R.style.popAnimation2);
        this.pop2.setOutsideTouchable(false);
        fvMoveTimelapseSmoothPop.setPop(this.pop2, fvMoveTimelapseSmoothPop);
        this.pop2.showAtLocation(this.parentView, 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_close:
                if (this.isConnected) {
                    MoveTimelapseUtil.getInstance().cancelShoot();
                }
                if (this.pop != null) {
                    this.pop.dismiss();
                }
                Util.sendIntEventMessge(10008);
                CameraUtils.setMoveTimelapseIng(false);
                EventBus.getDefault().unregister(this);
                return;
            case C0853R.C0855id.btn_standard:
                if (!this.isConnected) {
                    Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
                    return;
                } else if (this.list.size() > 2) {
                    if (this.list.get(this.list.size() - 1).equals("click")) {
                        this.list.remove(this.list.size() - 1);
                    }
                    MoveTimelapseUtil.getInstance().setSelectPictruePathList(this.list);
                    MoveTimelapseUtil.getInstance().setContext(this.context);
                    MoveTimelapseUtil.getInstance().setParentView(this.parentView);
                    showMoveTimeLapseSmoothPop();
                    return;
                } else {
                    Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.requre_more_point_stand), 1).show();
                    return;
                }
            case C0853R.C0855id.btn_freestyle:
                if (!this.isConnected) {
                    Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
                    return;
                } else if (this.list.size() > 1) {
                    if (this.list.get(this.list.size() - 1).equals("click")) {
                        this.list.remove(this.list.size() - 1);
                    }
                    MoveTimelapseUtil.getInstance().setSelectPictruePathList(this.list);
                    MoveTimelapseUtil.getInstance().setContext(this.context);
                    MoveTimelapseUtil.getInstance().setParentView(this.parentView);
                    Intent intent = new Intent(this.context, FVKcfFreestyleActivity.class);
                    intent.putExtra("enterorback", 1);
                    intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, this.list);
                    this.context.startActivity(intent);
                    if (this.pop != null) {
                        this.pop.dismiss();
                        return;
                    }
                    return;
                } else {
                    Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.requre_more_point), 1).show();
                    return;
                }
            default:
                return;
        }
    }

    private void btnStandardOnClick() {
        if (!this.isConnected) {
            Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
        } else if (this.list.size() > 2) {
            if (this.list.get(this.list.size() - 1).equals("click")) {
                this.list.remove(this.list.size() - 1);
            }
            MoveTimelapseUtil.getInstance().setSelectPictruePathList(this.list);
            MoveTimelapseUtil.getInstance().setContext(this.context);
            MoveTimelapseUtil.getInstance().setParentView(this.parentView);
            showMoveTimeLapseSmoothPop();
        } else {
            Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.requre_more_point_stand), 1).show();
        }
    }

    private void btnFreestyleOnClick() {
        if (!this.isConnected) {
            Toast.makeText(this.context, C0853R.string.label_device_not_connected, 1).show();
        } else if (this.list.size() > 1) {
            if (this.list.get(this.list.size() - 1).equals("click")) {
                this.list.remove(this.list.size() - 1);
            }
            MoveTimelapseUtil.getInstance().setSelectPictruePathList(this.list);
            MoveTimelapseUtil.getInstance().setContext(this.context);
            MoveTimelapseUtil.getInstance().setParentView(this.parentView);
            Intent intent = new Intent(this.context, FVKcfFreestyleActivity.class);
            intent.putExtra("enterorback", 1);
            intent.putStringArrayListExtra(IntentKey.VIDEOS_PATH, this.list);
            this.context.startActivity(intent);
            if (this.pop != null) {
                this.pop.dismiss();
            }
        } else {
            Toast.makeText(this.context, this.context.getResources().getString(C0853R.string.requre_more_point), 1).show();
        }
    }
}
