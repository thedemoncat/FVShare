package com.freevisiontech.fvmobile.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVBeautyPop {
    /* access modifiers changed from: private */
    public CheckButtonOnclick buttonListener;
    /* access modifiers changed from: private */
    public FVCameraManager cameraManager;
    /* access modifiers changed from: private */
    public Context context;
    private LinearLayout layout_beauty_pop_top_linear;
    private View parent;
    private PopupWindow pop;
    private SeekBar seekBar;
    private View view;

    public interface CheckButtonOnclick {
        void onClick(View view);
    }

    public void init(Context context2, View parent2, FVCameraManager fvCameraManager) {
        this.context = context2;
        this.parent = parent2;
        this.cameraManager = fvCameraManager;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_beauty_pop_two, (ViewGroup) null);
        int preValue = ((Integer) SPUtils.get(context2, SharePrefConstant.BEAUTY_VALUE, 50)).intValue();
        this.layout_beauty_pop_top_linear = (LinearLayout) this.view.findViewById(C0853R.C0855id.layout_beauty_pop_top_linear);
        this.layout_beauty_pop_top_linear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVBeautyPop.this.buttonListener.onClick(v);
            }
        });
        this.seekBar = (SeekBar) this.view.findViewById(C0853R.C0855id.seekbar);
        this.seekBar.setProgress(preValue);
        initListener();
        CameraUtils.setFrameLayerNumber(9);
        if (CameraUtils.getCurrentPageIndex() == 2) {
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_RESTART);
            this.seekBar.setProgressDrawable(context2.getResources().getDrawable(C0853R.C0854drawable.seekbar_style_red5));
            this.seekBar.setThumb(context2.getResources().getDrawable(C0853R.mipmap.ic_seekbar_red_thumb));
        }
    }

    private void initListener() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (FVBeautyPop.this.cameraManager != null) {
                    SPUtils.put(FVBeautyPop.this.context, SharePrefConstant.BEAUTY_VALUE, Integer.valueOf(progress));
                    FVBeautyPop.this.cameraManager.changeFilterIntensity(progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
                if (CameraUtils.getFrameLayerNumber() == 9 && this.pop != null) {
                    setBeautyProBar(true);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_STIR_DOWN_210:
                if (CameraUtils.getFrameLayerNumber() == 9 && this.pop != null) {
                    setBeautyProBar(false);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_TOP_BAR_UP_OR_DOWN_210:
                BleByteUtil.ackPTZPanorama((byte) 70, (byte) 3);
                if (CameraUtils.getFrameLayerNumber() == 9) {
                    if (this.pop != null) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    EventBus.getDefault().unregister(this);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_RETURN_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 9) {
                    if (this.pop != null) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    EventBus.getDefault().unregister(this);
                    return;
                }
                return;
            case Constants.LABEL_CAMERA_POP_DISMISS_KEY_210:
                if (CameraUtils.getFrameLayerNumber() == 9) {
                    if (this.pop != null) {
                        Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                        this.pop.dismiss();
                        CameraUtils.setFrameLayerNumber(0);
                    }
                    CameraUtils.setMoveTimelapseIng(false);
                    EventBus.getDefault().unregister(this);
                    CameraUtils.setFrameLayerNumber(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void setBeautyProBar(boolean boo) {
        int progress;
        int progress2 = this.seekBar.getProgress();
        if (!boo) {
            progress = progress2 - 5;
            if (progress < 0) {
                progress = 0;
            }
        } else {
            progress = progress2 + 5;
            if (progress > 100) {
                progress = 100;
            }
        }
        this.seekBar.setProgress(progress);
    }

    public void setPop(PopupWindow pop2, final FVBeautyPop fvBeautyPop) {
        this.pop = pop2;
        CameraUtils.setFrameLayerNumber(9);
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    Util.sendIntEventMessge(Constants.LABEL_CAMERA_TOP_BAR_STATUS_SELECT_CAMERA);
                    EventBus.getDefault().unregister(fvBeautyPop);
                    CameraUtils.setFrameLayerNumber(0);
                }
            });
        }
    }

    public void setButtonOnClick(CheckButtonOnclick buttonListener2) {
        this.buttonListener = buttonListener2;
    }

    public View getView() {
        return this.view;
    }
}
