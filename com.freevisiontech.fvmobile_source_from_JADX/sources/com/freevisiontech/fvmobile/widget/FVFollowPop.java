package com.freevisiontech.fvmobile.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.activity.FVMainActivity;
import com.freevisiontech.fvmobile.fragment.FVContentFragment;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.utils.ScreenOrientationUtil;

public class FVFollowPop implements View.OnClickListener {
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    private OrientationBroad broad;
    /* access modifiers changed from: private */
    public ImageView btnFollowCloseGray;
    /* access modifiers changed from: private */
    public ImageView btnFollowDrop;
    /* access modifiers changed from: private */
    public ImageView btnFollowFrame;
    private FVCameraManager cameraManager;
    private Context context;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (FVFollowPop.this.mStartAngle == -270) {
                        FVFollowPop.this.rotateView(-360, FVFollowPop.this.btnFollowCloseGray, FVFollowPop.this.btnFollowDrop, FVFollowPop.this.btnFollowFrame);
                        return;
                    }
                    FVFollowPop.this.rotateView(0, FVFollowPop.this.btnFollowCloseGray, FVFollowPop.this.btnFollowDrop, FVFollowPop.this.btnFollowFrame);
                    return;
                case 90:
                    if (FVFollowPop.this.mStartAngle == -360) {
                        int unused = FVFollowPop.this.mStartAngle = 0;
                    }
                    FVFollowPop.this.rotateView(-90, FVFollowPop.this.btnFollowCloseGray, FVFollowPop.this.btnFollowDrop, FVFollowPop.this.btnFollowFrame);
                    return;
                case 180:
                    FVFollowPop.this.rotateView(-180, FVFollowPop.this.btnFollowCloseGray, FVFollowPop.this.btnFollowDrop, FVFollowPop.this.btnFollowFrame);
                    return;
                case 270:
                    if (FVFollowPop.this.mStartAngle == 0) {
                        int unused2 = FVFollowPop.this.mStartAngle = -360;
                    }
                    FVFollowPop.this.rotateView(-270, FVFollowPop.this.btnFollowCloseGray, FVFollowPop.this.btnFollowDrop, FVFollowPop.this.btnFollowFrame);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int mStartAngle = 0;
    private View view;

    public void init(Context context2) {
        this.context = context2;
        this.cameraManager = ((FVContentFragment) ((FVMainActivity) context2).getSupportFragmentManager().findFragmentByTag("contentFragment")).getCameraManager();
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_follow_pop, (ViewGroup) null);
        this.btnFollowCloseGray = (ImageView) this.view.findViewById(C0853R.C0855id.btn_follow_close_gray);
        this.btnFollowDrop = (ImageView) this.view.findViewById(C0853R.C0855id.btn_follow_drop);
        this.btnFollowFrame = (ImageView) this.view.findViewById(C0853R.C0855id.btn_follow_frame);
        this.btnFollowCloseGray.setOnClickListener(this);
        this.btnFollowDrop.setOnClickListener(this);
        this.btnFollowFrame.setOnClickListener(this);
        this.broad = new OrientationBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenOrientationUtil.BC_OrientationChanged);
        context2.registerReceiver(this.broad, filter);
        getAngle();
    }

    public void unRegisterListener() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
    }

    private class OrientationBroad extends BroadcastReceiver {
        private OrientationBroad() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1) != -1) {
                FVFollowPop.this.getAngle();
            }
        }
    }

    /* access modifiers changed from: private */
    public void getAngle() {
        int angle = ScreenOrientationUtil.getInstance().getOrientation();
        if (angle == 0) {
            this.mHandler.sendEmptyMessage(0);
        } else if (angle == 90) {
            this.mHandler.sendEmptyMessage(90);
        } else if (angle == 180) {
            this.mHandler.sendEmptyMessage(180);
        } else if (angle == 270) {
            this.mHandler.sendEmptyMessage(270);
        }
    }

    /* access modifiers changed from: private */
    public void rotateView(final int angle, View... views) {
        int length = views.length;
        int i = 0;
        while (i < length) {
            View view2 = views[i];
            if (angle != this.mStartAngle) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view2, "rotation", new float[]{(float) this.mStartAngle, (float) angle});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVFollowPop.this.mStartAngle = angle;
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    public View getView() {
        return this.view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0853R.C0855id.btn_follow_close_gray:
                Util.sendIntEventMessge(Constants.CAMERA_FOLLOW_CLOSE_GRAY);
                SPUtils.put(this.context, SharePrefConstant.CAMERA_FOLLOW, Integer.valueOf(Constants.CAMERA_FOLLOW_CLOSE_GRAY));
                return;
            case C0853R.C0855id.btn_follow_drop:
                Util.sendIntEventMessge(Constants.CAMERA_FOLLOW_DROP);
                return;
            case C0853R.C0855id.btn_follow_frame:
                Util.sendIntEventMessge(Constants.CAMERA_FOLLOW_FRAME);
                return;
            default:
                return;
        }
    }
}
