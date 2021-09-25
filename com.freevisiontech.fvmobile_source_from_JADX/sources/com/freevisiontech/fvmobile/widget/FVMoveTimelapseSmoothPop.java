package com.freevisiontech.fvmobile.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.utils.ScreenOrientationUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVMoveTimelapseSmoothPop implements View.OnClickListener {
    private static PopupWindow pop1;
    private static PopupWindow pop2;
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    ImageView btnClose;
    TextView btnNext;
    private TextView btnTitleText;
    /* access modifiers changed from: private */
    public Context context;
    private int height;
    private boolean isBacked = false;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private LinearLayout layout_camera_shortcut_pop_int_linear;
    private LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Log.e("------------", "--------收到消息 0-----");
                    FVMoveTimelapseSmoothPop.this.setHorUiZero();
                    return;
                case 11:
                    Log.e("------------", "--------收到消息  90-------------");
                    FVMoveTimelapseSmoothPop.this.setHorUiNinety();
                    return;
                case 12:
                    Log.e("------------", "--------收到消息  180-------------");
                    FVMoveTimelapseSmoothPop.this.setHorUiZero180();
                    return;
                case 13:
                    Log.e("------------", "--------收到消息  270-------------");
                    FVMoveTimelapseSmoothPop.this.setHorUiNinety270();
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private RelativeLayout rl_move_time_lapse_smooth_seekbar_bg;
    private boolean scaleSlide = false;
    SeekBar seekBar;
    private int seekBarNum = 0;
    private int stirPosition = -1;
    private View view;

    public void init(Context context2, View parentView2, boolean isBacked2) {
        this.context = context2;
        this.parentView = parentView2;
        this.isBacked = isBacked2;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_move_time_lapse_smooth_pop_new, (ViewGroup) null);
        this.btnClose = (ImageView) this.view.findViewById(C0853R.C0855id.btn_close);
        this.btnNext = (TextView) this.view.findViewById(C0853R.C0855id.btn_next);
        this.seekBar = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar);
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
        this.rl_move_time_lapse_smooth_seekbar_bg = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_move_time_lapse_smooth_seekbar_bg);
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
                if (FVMoveTimelapseSmoothPop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMoveTimelapseSmoothPop.this.pop != null) {
                }
            }
        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirPosition = 0;
            if (this.stirPosition == 0) {
                setBackgroundColorSelect(this.stirPosition);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition--;
                        if (this.stirPosition < 0) {
                            this.stirPosition = 0;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        this.seekBarNum++;
                        if (this.seekBarNum > 5) {
                            this.seekBarNum = 5;
                        }
                        this.seekBar.setProgress(this.seekBarNum);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 1) {
                            this.stirPosition = 1;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        this.seekBarNum--;
                        if (this.seekBarNum < 0) {
                            this.seekBarNum = 0;
                        }
                        this.seekBar.setProgress(this.seekBarNum);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (CameraUtils.getFrameLayerNumber() == 3 && this.pop != null) {
                    if (this.scaleSlide) {
                        this.scaleSlide = false;
                    } else {
                        this.scaleSlide = true;
                    }
                    if (this.stirPosition == 0) {
                        if (this.scaleSlide) {
                            this.seekBar.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
                            this.seekBar.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                        } else {
                            this.seekBar.setProgressDrawable(this.context.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black5));
                            this.seekBar.setThumb(this.context.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                        }
                    }
                    if (this.stirPosition == 1) {
                        this.scaleSlide = false;
                        showTimeLapsePop();
                        return;
                    }
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 3) {
                    showMoveTimeLapsePop();
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 3) {
                    if (ViseBluetooth.getInstance().isConnected()) {
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
            default:
                return;
        }
    }

    private void setBackgroundColorSelect(int select) {
        switch (select) {
            case 0:
                this.rl_move_time_lapse_smooth_seekbar_bg.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.btnNext.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_black1_round_bg));
                return;
            case 1:
                this.rl_move_time_lapse_smooth_seekbar_bg.setBackgroundColor(this.context.getResources().getColor(C0853R.color.transparent));
                this.btnNext.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_yellow_round_bg));
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
            Log.e("-------------", "----------888-- orientation --" + orientation);
            if (orientation == -1) {
                return;
            }
            if (orientation == 0) {
                FVMoveTimelapseSmoothPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVMoveTimelapseSmoothPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVMoveTimelapseSmoothPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVMoveTimelapseSmoothPop.this.sendToHandler(13);
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

    private void initListener() {
        this.btnClose.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        if (this.isBacked) {
            this.seekBar.setProgress(MoveTimelapseUtil.getInstance().getSelectSmoothness());
        }
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MoveTimelapseUtil.getInstance().setSelectSmoothness(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void setPop(PopupWindow pop3, final FVMoveTimelapseSmoothPop fvMoveTimelapseSmoothPop) {
        this.pop = pop3;
        CameraUtils.setFrameLayerNumber(3);
        if (pop3 != null) {
            pop3.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    if (FVMoveTimelapseSmoothPop.this.broad != null) {
                        FVMoveTimelapseSmoothPop.this.context.unregisterReceiver(FVMoveTimelapseSmoothPop.this.broad);
                    }
                    Util.sendIntEventMessge(Constants.CAMERA_INDICATE_HIDDEN);
                    SPUtils.put(FVMoveTimelapseSmoothPop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                    EventBus.getDefault().unregister(fvMoveTimelapseSmoothPop);
                }
            });
        }
    }

    public static void setPopDismiss(PopupWindow pop3) {
        if (pop3 != null) {
            pop3.dismiss();
        }
        if (pop1 != null) {
            pop1.dismiss();
        }
        if (pop2 != null) {
            pop2.dismiss();
        }
    }

    public View getView() {
        return this.view;
    }

    private void showTimeLapsePop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVTimeLapseStandardPop timeLapsePop = new FVTimeLapseStandardPop();
        timeLapsePop.init(this.context, this.parentView, false);
        pop1 = new PopupWindow(timeLapsePop.getView(), height2, height2);
        pop1.setBackgroundDrawable(new ColorDrawable(0));
        pop1.setAnimationStyle(C0853R.style.popAnimation2);
        pop1.setOutsideTouchable(false);
        timeLapsePop.setPop(pop1, timeLapsePop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop1.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    private void showMoveTimeLapsePop() {
        if (this.pop != null) {
            this.pop.dismiss();
        }
        Util.sendIntEventMessge(10009);
        int height2 = Util.getDeviceSize(this.context).y - Util.dip2px(this.context, 10.0f);
        FVMoveTimelapsePop moveTimeLapsePop = new FVMoveTimelapsePop();
        moveTimeLapsePop.init(this.context, this.parentView, true);
        pop2 = new PopupWindow(moveTimeLapsePop.getView(), height2, height2);
        pop2.setBackgroundDrawable(new ColorDrawable(0));
        pop2.setAnimationStyle(C0853R.style.popAnimation2);
        pop2.setOutsideTouchable(false);
        moveTimeLapsePop.setPop(pop2, moveTimeLapsePop);
        int right = this.parentView.getRight() + Util.dip2px(this.context, 2.0f);
        pop2.showAtLocation(this.parentView.findViewById(C0853R.C0855id.btn_camera), 17, 17, 17);
        SPUtils.put(this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_next:
                showTimeLapsePop();
                return;
            case C0853R.C0855id.btn_close:
                showMoveTimeLapsePop();
                return;
            default:
                return;
        }
    }
}
