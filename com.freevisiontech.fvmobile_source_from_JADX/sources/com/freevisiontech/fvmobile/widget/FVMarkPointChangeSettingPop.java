package com.freevisiontech.fvmobile.widget;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.utils.ScreenOrientationUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVMarkPointChangeSettingPop {
    private final int CAMERA_POPUP_HUN_EIGHTY = 12;
    private final int CAMERA_POPUP_NINTY = 11;
    private final int CAMERA_POPUP_TWO_SERTY = 13;
    private final int CAMERA_POPUP_ZARO = 10;
    /* access modifiers changed from: private */
    public OrientationBroadPopup broad;
    RelativeLayout btnClose;
    TextView btnStandard;
    private FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    private int height;
    private LinearLayout layout_camera_shortcut_pop_horizontal_bottom;
    private RelativeLayout layout_camera_shortcut_pop_int_linear;
    /* access modifiers changed from: private */
    public LinearLayout layout_camera_shortcut_pop_out_linear;
    private LinearLayout layout_camera_shortcut_pop_vertical_bottom_top;
    /* access modifiers changed from: private */
    public Context mContext;
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    FVMarkPointChangeSettingPop.this.layout_camera_shortcut_pop_out_linear.setRotation(0.0f);
                    return;
                case 11:
                    FVMarkPointChangeSettingPop.this.layout_camera_shortcut_pop_out_linear.setRotation(-90.0f);
                    return;
                case 12:
                    FVMarkPointChangeSettingPop.this.layout_camera_shortcut_pop_out_linear.setRotation(180.0f);
                    return;
                case 13:
                    FVMarkPointChangeSettingPop.this.layout_camera_shortcut_pop_out_linear.setRotation(90.0f);
                    return;
                default:
                    return;
            }
        }
    };
    private View parentView;
    /* access modifiers changed from: private */
    public PopupWindow pop;
    private RelativeLayout rl_mark_point_change_smooth;
    private RelativeLayout rl_mark_point_change_speed;
    private boolean scaleSlide = false;
    private SeekBar seekbar_mark_point_change_smooth;
    private SeekBar seekbar_mark_point_change_speed;
    private int stirPosition = -1;
    /* access modifiers changed from: private */
    public TextView tv_mark_point_change_smooth_set;
    /* access modifiers changed from: private */
    public TextView tv_mark_point_change_speed_set;
    private int variValue = 1;
    private View view;

    public void init(Context context2, View parentView2) {
        this.context = context2;
        this.parentView = parentView2;
        this.mContext = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_mark_point_change_setting_pop, (ViewGroup) null);
        this.btnClose = (RelativeLayout) this.view.findViewById(C0853R.C0855id.ll_close);
        this.height = Util.dip2px(context2, 256.0f);
        this.layout_camera_shortcut_pop_out_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_out_linear);
        this.layout_camera_shortcut_pop_int_linear = (RelativeLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_int_linear);
        this.layout_camera_shortcut_pop_horizontal_bottom = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom);
        this.layout_camera_shortcut_pop_vertical_bottom_top = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_camera_shortcut_pop_vertical_bottom_top);
        int topRange = ((Util.getDeviceSize(context2).y - Util.dip2px(context2, 256.0f)) / 2) - Util.dip2px(context2, 10.0f);
        int dip2px = (Util.getDeviceSize(context2).x - Util.dip2px(context2, 256.0f)) / 2;
        ((LinearLayout.LayoutParams) this.layout_camera_shortcut_pop_out_linear.getLayoutParams()).setMargins(17, topRange, 0, 0);
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
                if (FVMarkPointChangeSettingPop.this.pop != null) {
                }
            }
        });
        this.layout_camera_shortcut_pop_vertical_bottom_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (FVMarkPointChangeSettingPop.this.pop != null) {
                }
            }
        });
        this.btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVMarkPointChangeSettingPop.this.pop.dismiss();
            }
        });
        this.seekbar_mark_point_change_speed = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_mark_point_change_speed);
        this.seekbar_mark_point_change_smooth = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar_mark_point_change_smooth);
        this.tv_mark_point_change_speed_set = (TextView) this.view.findViewById(C0853R.C0855id.tv_mark_point_change_speed_set);
        this.tv_mark_point_change_smooth_set = (TextView) this.view.findViewById(C0853R.C0855id.tv_mark_point_change_smooth_set);
        setSeekBarMarkPointSmooth();
        this.rl_mark_point_change_speed = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_point_change_speed);
        this.rl_mark_point_change_smooth = (RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_mark_point_change_smooth);
        Boolean isConnected = Boolean.valueOf(ViseBluetooth.getInstance().isConnected());
        if (CameraUtils.getCurrentPageIndex() == 2 && isConnected.booleanValue()) {
            this.stirPosition = 0;
            if (this.stirPosition == 0) {
                setBackgroundColorSelect(this.stirPosition);
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        CameraUtils.setFrameLayerNumber(45);
    }

    private void setSeekBarMarkPointSmooth() {
        int changeSpeed = ((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, 3)).intValue();
        this.seekbar_mark_point_change_speed.setProgress(changeSpeed);
        this.tv_mark_point_change_speed_set.setText((changeSpeed + 1) + "");
        int changeSmooth = ((Integer) SPUtils.get(this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, 0)).intValue();
        this.seekbar_mark_point_change_smooth.setProgress(changeSmooth);
        this.tv_mark_point_change_smooth_set.setText((changeSmooth + 1) + "");
        this.seekbar_mark_point_change_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"LongLogTag"})
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                FVMarkPointChangeSettingPop.this.tv_mark_point_change_speed_set.setText((progress + 1) + "");
                SPUtils.put(FVMarkPointChangeSettingPop.this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, Integer.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVMarkPointChangeSettingPop.this.tv_mark_point_change_speed_set.setText((progress + 1) + "");
                SPUtils.put(FVMarkPointChangeSettingPop.this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SPEED, Integer.valueOf(progress));
            }
        });
        this.seekbar_mark_point_change_smooth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"LongLogTag"})
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                FVMarkPointChangeSettingPop.this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                SPUtils.put(FVMarkPointChangeSettingPop.this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, Integer.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                FVMarkPointChangeSettingPop.this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                SPUtils.put(FVMarkPointChangeSettingPop.this.mContext, SharePrefConstant.CAMERA_MARK_POINT_CHANGE_SMOOTH, Integer.valueOf(progress));
            }
        });
    }

    private void setBackgroundColorSelect(int select) {
        switch (select) {
            case 0:
                this.rl_mark_point_change_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.rl_mark_point_change_smooth.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.btnClose.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_white_round8_bg));
                return;
            case 1:
                this.rl_mark_point_change_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.rl_mark_point_change_smooth.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                this.btnClose.setBackground(this.context.getResources().getDrawable(C0853R.C0854drawable.sp_white_round8_bg));
                return;
            case 2:
                this.rl_mark_point_change_speed.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.rl_mark_point_change_smooth.setBackgroundColor(this.context.getResources().getColor(C0853R.color.color_white));
                this.btnClose.setBackgroundColor(this.context.getResources().getColor(C0853R.color.black15));
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.LABEL_CAMERA_STIR_UP_210:
                if (CameraUtils.getFrameLayerNumber() == 45 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition--;
                        if (this.stirPosition < 0) {
                            this.stirPosition = 0;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 波轮拨动向下   第一刻度尺,向右滑动   第一刻度尺,向右滑动   向右");
                        setControlSeekBarValue(0, true, 1);
                        return;
                    } else if (this.stirPosition == 1) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 波轮拨动向下   第二刻度尺,向右滑动   第二刻度尺,向右滑动   向右");
                        setControlSeekBarValue(1, true, 1);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 45 && this.pop != null) {
                    if (!this.scaleSlide) {
                        this.stirPosition++;
                        if (this.stirPosition > 2) {
                            this.stirPosition = 2;
                        }
                        setBackgroundColorSelect(this.stirPosition);
                        return;
                    } else if (this.stirPosition == 0) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 波轮拨动向上   第一刻度尺,向左滑动   第一刻度尺,向左滑动   向左");
                        setControlSeekBarValue(0, false, 1);
                        return;
                    } else if (this.stirPosition == 1) {
                        Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 波轮拨动向上   第二刻度尺,向左滑动   第二刻度尺,向左滑动   向左");
                        setControlSeekBarValue(1, false, 1);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  OK键响应事件   响应事件   响应事件");
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (this.scaleSlide) {
                    this.scaleSlide = false;
                } else {
                    this.scaleSlide = true;
                }
                if (!this.scaleSlide) {
                    this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                    this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                    this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                    this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                    if (this.stirPosition == 2 && this.pop != null) {
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                        return;
                    }
                    return;
                } else if (this.stirPosition == 0) {
                    setControlSeekBarColorGrayToYellow(this.rl_mark_point_change_speed);
                    return;
                } else if (this.stirPosition == 1) {
                    setControlSeekBarColorGrayToYellow(this.rl_mark_point_change_smooth);
                    return;
                } else if (this.stirPosition == 2 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                } else {
                    return;
                }
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 云台操作返回键   云台操作返回键 ");
                if (CameraUtils.getFrameLayerNumber() == 45 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                Log.e("-----------------", "----------  7777  8888  9999   ------- FVMarkPointChangeSettingPop  210 云台, 弹框消失键   云台, 弹框消失键 ");
                if (CameraUtils.getFrameLayerNumber() == 45 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            case Constants.MARK_POINT_CHANGE_SETTING_POP_DISMISS:
                if (CameraUtils.getFrameLayerNumber() == 45 && this.pop != null) {
                    this.pop.dismiss();
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setControlSeekBarValue(int num, boolean boo, int vaValue) {
        int progress;
        int progress2;
        this.variValue = vaValue;
        switch (num) {
            case 0:
                int progress3 = this.seekbar_mark_point_change_speed.getProgress();
                if (!boo) {
                    progress2 = progress3 - this.variValue;
                    if (progress2 < 0) {
                        progress2 = 0;
                    }
                } else {
                    progress2 = progress3 + this.variValue;
                    if (progress2 > 6) {
                        progress2 = 6;
                    }
                }
                this.tv_mark_point_change_speed_set.setText((progress2 + 1) + "");
                this.seekbar_mark_point_change_speed.setProgress(progress2);
                return;
            case 1:
                int progress4 = this.seekbar_mark_point_change_smooth.getProgress();
                if (!boo) {
                    progress = progress4 - this.variValue;
                    if (progress < 0) {
                        progress = 0;
                    }
                } else {
                    progress = progress4 + this.variValue;
                    if (progress > 4) {
                        progress = 4;
                    }
                }
                this.tv_mark_point_change_smooth_set.setText((progress + 1) + "");
                this.seekbar_mark_point_change_smooth.setProgress(progress);
                return;
            default:
                return;
        }
    }

    public void setControlSeekBarColorGrayToYellow(View view2) {
        switch (view2.getId()) {
            case C0853R.C0855id.rl_mark_point_change_speed:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                return;
            case C0853R.C0855id.rl_mark_point_change_smooth:
                this.seekbar_mark_point_change_speed.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_black4));
                this.seekbar_mark_point_change_speed.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_thumb));
                this.seekbar_mark_point_change_smooth.setProgressDrawable(this.mContext.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_setting_red5));
                this.seekbar_mark_point_change_smooth.setThumb(this.mContext.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
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
                FVMarkPointChangeSettingPop.this.sendToHandler(10);
            } else if (orientation == 90) {
                FVMarkPointChangeSettingPop.this.sendToHandler(11);
            } else if (orientation == 180) {
                FVMarkPointChangeSettingPop.this.sendToHandler(12);
            } else if (orientation == 270) {
                FVMarkPointChangeSettingPop.this.sendToHandler(13);
            }
        }
    }

    public void sendToHandler(int what) {
        Message me = new Message();
        me.what = what;
        this.myHandler.sendMessage(me);
    }

    private void setHorUiZero180() {
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

    private void setHorUiZero() {
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

    private void setHorUiNinety() {
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

    private void setHorUiNinety270() {
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

    public void setPop(PopupWindow pop2, final FVMarkPointChangeSettingPop markPointChangeSettingPop) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    FVMarkPointChangeSettingPop.this.context.unregisterReceiver(FVMarkPointChangeSettingPop.this.broad);
                    EventBus.getDefault().unregister(markPointChangeSettingPop);
                    SPUtils.put(FVMarkPointChangeSettingPop.this.context, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
                    CameraUtils.setFrameLayerNumber(0);
                }
            });
        }
        CameraUtils.setFrameLayerNumber(45);
    }

    public View getView() {
        return this.view;
    }
}
