package com.freevisiontech.fvmobile.widget;

import android.os.CountDownTimer;
import android.widget.ProgressBar;
import com.freevisiontech.fvmobile.utility.CameraUtils;

public class FVProgressCountTimer extends CountDownTimer {
    private ProgressBar progressBar;

    public FVProgressCountTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar2) {
        super(millisInFuture, countDownInterval);
        CameraUtils.setFullShotIng(true);
        this.progressBar = progressBar2;
    }

    public void onTick(long millisUntilFinished) {
        int progress;
        CameraUtils.setFullShotIng(true);
        if (this.progressBar != null && (progress = this.progressBar.getProgress()) < 90) {
            this.progressBar.setProgress(progress + 10);
        }
    }

    public void onFinish() {
        CameraUtils.setFullShotIng(false);
        if (this.progressBar != null) {
            this.progressBar.setProgress(90);
        }
    }
}
