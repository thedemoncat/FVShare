package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVCalibrationActivity4;
import com.freevisiontech.fvmobile.widget.TransparentPop;

public class FVCalibrationMessagePop implements TransparentPop.InvokeListener {
    private RelativeLayout btnClose = ((RelativeLayout) this.view.findViewById(C0853R.C0855id.rl_close));
    private Button btnSure = ((Button) this.view.findViewById(C0853R.C0855id.btn_sure));
    /* access modifiers changed from: private */
    public Activity content;
    private boolean exit;
    private ImageView iconGuide = ((ImageView) this.view.findViewById(C0853R.C0855id.icon_guide));
    private final TransparentPop mPop;
    private ProgressBar progress = ((ProgressBar) this.view.findViewById(C0853R.C0855id.progress));
    private TextView tvDescription = ((TextView) this.view.findViewById(C0853R.C0855id.tv_description));
    private final View view;

    public FVCalibrationMessagePop(Activity content2, boolean exit2) {
        this.content = content2;
        this.exit = exit2;
        this.view = LayoutInflater.from(content2).inflate(C0853R.layout.layout_calibration_pop, new RelativeLayout(content2), false);
        this.mPop = new TransparentPop(content2, this);
        initView();
        initListener();
    }

    private void initView() {
        if (this.exit) {
            this.iconGuide.setVisibility(0);
            this.tvDescription.setText(C0853R.string.label_ptz_calibration_description8);
            this.btnSure.setVisibility(0);
            return;
        }
        this.progress.setVisibility(0);
        this.tvDescription.setText(C0853R.string.label_ptz_calibration_description3);
        this.btnClose.setVisibility(0);
    }

    private void initListener() {
        this.mPop.setOutsideTouchable(false);
        this.btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationMessagePop.this.dismiss();
            }
        });
        this.btnSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FVCalibrationMessagePop.this.gotoActivity();
            }
        });
        this.mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = FVCalibrationMessagePop.this.content.getWindow().getAttributes();
                lp.alpha = 1.0f;
                FVCalibrationMessagePop.this.content.getWindow().setAttributes(lp);
            }
        });
    }

    /* access modifiers changed from: private */
    public void gotoActivity() {
        if (this.content != null) {
            Intent intent = new Intent(this.content, FVCalibrationActivity4.class);
            intent.putExtra("success", false);
            this.content.startActivity(intent);
            this.content.overridePendingTransition(C0853R.anim.vilta_slide_in_right, C0853R.anim.vilta_slide_out_right);
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
