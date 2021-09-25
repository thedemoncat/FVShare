package com.freevisiontech.fvmobile.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.widget.TextView;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.Util;

public class FVCountTimer extends CountDownTimer {
    private TextView textView;

    public FVCountTimer(long millisInFuture, long countDownInterval, TextView textView2) {
        super(millisInFuture, countDownInterval);
        this.textView = textView2;
    }

    public void onTick(long millisUntilFinished) {
        if (this.textView != null) {
            this.textView.setText((millisUntilFinished / 1000) + "");
            ObjectAnimator animator = ObjectAnimator.ofFloat(this.textView, "alpha", new float[]{0.3f, 0.8f, 1.0f});
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(this.textView, "scaleX", new float[]{0.5f, 2.0f, 1.0f});
            ObjectAnimator animator3 = ObjectAnimator.ofFloat(this.textView, "scaleY", new float[]{0.5f, 2.0f, 1.0f});
            AnimatorSet set = new AnimatorSet();
            set.setDuration(1000);
            set.playTogether(new Animator[]{animator, animator2, animator3});
            set.start();
        }
    }

    public void onFinish() {
        CameraUtils.setBosIsResume(true);
        Util.sendIntEventMessge(Constants.START_TAKE_PHOTO);
        if (this.textView != null) {
            this.textView.setText("");
        }
    }
}
