package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.ViseBluetooth;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.widget.TransparentPop;
import com.vise.log.ViseLog;

public class FVCalibrationExitPop implements TransparentPop.InvokeListener {
    private Button btnSure = ((Button) this.view.findViewById(C0853R.C0855id.btn_sure));
    /* access modifiers changed from: private */
    public Activity content;
    private final TransparentPop mPop;
    private RelativeLayout rlClose = ((RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_close));
    private final View view;

    public FVCalibrationExitPop(Activity content2) {
        this.content = content2;
        this.view = LayoutInflater.from(content2).inflate(C0853R.layout.layout_calibration_exit_pop, new RelativeLayout(content2), false);
        this.mPop = new TransparentPop(content2, this);
        initListener();
    }

    private void initListener() {
        this.btnSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationExitPop.this.ptzConnectedSendMessageOrClose();
            }
        });
        this.mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = FVCalibrationExitPop.this.content.getWindow().getAttributes();
                lp.alpha = 1.0f;
                FVCalibrationExitPop.this.content.getWindow().setAttributes(lp);
            }
        });
        this.rlClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationExitPop.this.dismiss();
            }
        });
    }

    private void sendMessage() {
        BleByteUtil.setPTZParameters((byte) ClosedCaptionCtrl.TAB_OFFSET_CHAN_1, (byte) 6);
        ViseLog.m1466e("发送退出校准");
        dismiss();
    }

    /* access modifiers changed from: private */
    public void ptzConnectedSendMessageOrClose() {
        if (ViseBluetooth.getInstance().isConnected()) {
            sendMessage();
        } else if (this.content != null) {
            dismiss();
            this.content.finish();
        }
    }

    public void dismiss() {
        if (this.mPop != null) {
            this.mPop.dismiss(true);
        }
    }

    public void show(View parent) {
        WindowManager.LayoutParams lp = this.content.getWindow().getAttributes();
        lp.alpha = 0.5f;
        this.content.getWindow().setAttributes(lp);
        if (this.mPop != null) {
            this.mPop.show(parent);
        }
    }

    public void invokeView(LinearLayout v) {
        v.setGravity(17);
        v.addView(this.view);
    }
}
