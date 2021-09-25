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
import android.widget.PopupWindow;
import android.widget.TextView;
import com.freevisiontech.cameralib.FVCameraManager;
import com.freevisiontech.fvmobile.C0853R;
import com.freevisiontech.fvmobile.bean.FVModeSelectEvent;
import com.freevisiontech.fvmobile.utility.Constants;
import com.freevisiontech.fvmobile.utility.SPUtils;
import com.freevisiontech.fvmobile.utility.SharePrefConstant;
import com.freevisiontech.fvmobile.utility.Util;
import com.freevisiontech.utils.ScreenOrientationUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FVPhotoModeCommonShortcutPop implements View.OnClickListener {
    public static final int Rotation_0 = 0;
    public static final int Rotation_180 = 180;
    public static final int Rotation_270 = 270;
    public static final int Rotation_90 = 90;
    private OrientationBroad broad;
    /* access modifiers changed from: private */
    public ImageView btnItem1;
    /* access modifiers changed from: private */
    public ImageView btnItem2;
    /* access modifiers changed from: private */
    public ImageView btnItem3;
    /* access modifiers changed from: private */
    public ImageView btnItem4;
    /* access modifiers changed from: private */
    public TextView btnNone;
    private FVCameraManager cameraManager;
    private Context context;
    private int itemId = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (FVPhotoModeCommonShortcutPop.this.mStartAngle == -270) {
                        FVPhotoModeCommonShortcutPop.this.rotateView(-360, FVPhotoModeCommonShortcutPop.this.btnNone, FVPhotoModeCommonShortcutPop.this.btnItem1, FVPhotoModeCommonShortcutPop.this.btnItem2, FVPhotoModeCommonShortcutPop.this.btnItem3, FVPhotoModeCommonShortcutPop.this.btnItem4);
                        return;
                    }
                    FVPhotoModeCommonShortcutPop.this.rotateView(0, FVPhotoModeCommonShortcutPop.this.btnNone, FVPhotoModeCommonShortcutPop.this.btnItem1, FVPhotoModeCommonShortcutPop.this.btnItem2, FVPhotoModeCommonShortcutPop.this.btnItem3, FVPhotoModeCommonShortcutPop.this.btnItem4);
                    return;
                case 90:
                    if (FVPhotoModeCommonShortcutPop.this.mStartAngle == -360) {
                        int unused = FVPhotoModeCommonShortcutPop.this.mStartAngle = 0;
                    }
                    FVPhotoModeCommonShortcutPop.this.rotateView(-90, FVPhotoModeCommonShortcutPop.this.btnNone, FVPhotoModeCommonShortcutPop.this.btnItem1, FVPhotoModeCommonShortcutPop.this.btnItem2, FVPhotoModeCommonShortcutPop.this.btnItem3, FVPhotoModeCommonShortcutPop.this.btnItem4);
                    return;
                case 180:
                    FVPhotoModeCommonShortcutPop.this.rotateView(-180, FVPhotoModeCommonShortcutPop.this.btnNone, FVPhotoModeCommonShortcutPop.this.btnItem1, FVPhotoModeCommonShortcutPop.this.btnItem2, FVPhotoModeCommonShortcutPop.this.btnItem3, FVPhotoModeCommonShortcutPop.this.btnItem4);
                    return;
                case 270:
                    if (FVPhotoModeCommonShortcutPop.this.mStartAngle == 0) {
                        int unused2 = FVPhotoModeCommonShortcutPop.this.mStartAngle = -360;
                    }
                    FVPhotoModeCommonShortcutPop.this.rotateView(-270, FVPhotoModeCommonShortcutPop.this.btnNone, FVPhotoModeCommonShortcutPop.this.btnItem1, FVPhotoModeCommonShortcutPop.this.btnItem2, FVPhotoModeCommonShortcutPop.this.btnItem3, FVPhotoModeCommonShortcutPop.this.btnItem4);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public int mStartAngle = 0;
    private PopupWindow pop;
    private int type;
    private View view;

    public void init(Context context2, FVCameraManager cameraManager2, int type2) {
        this.context = context2;
        this.cameraManager = cameraManager2;
        this.type = type2;
        this.view = LayoutInflater.from(context2).inflate(C0853R.layout.layout_photo_mode_common_shortcut_pop, (ViewGroup) null);
        this.btnNone = (TextView) this.view.findViewById(C0853R.C0855id.btn_none);
        this.btnItem1 = (ImageView) this.view.findViewById(C0853R.C0855id.btn_item1);
        this.btnItem2 = (ImageView) this.view.findViewById(C0853R.C0855id.btn_item2);
        this.btnItem3 = (ImageView) this.view.findViewById(C0853R.C0855id.btn_item3);
        this.btnItem4 = (ImageView) this.view.findViewById(C0853R.C0855id.btn_item4);
        initData();
        initListener();
        getAngle();
    }

    private void initData() {
        switch (this.type) {
            case 0:
                int fsMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE))).intValue();
                if (fsMode == 10025) {
                    this.btnItem1.setAlpha(1.0f);
                    this.itemId = 1;
                } else if (fsMode == 10026) {
                    this.btnItem2.setAlpha(1.0f);
                    this.itemId = 2;
                } else if (fsMode == 10027) {
                    this.btnItem3.setAlpha(1.0f);
                    this.itemId = 3;
                } else if (fsMode == 10028) {
                    this.btnItem4.setAlpha(1.0f);
                    this.itemId = 4;
                }
                this.btnItem1.setVisibility(0);
                this.btnItem1.setImageResource(C0853R.mipmap.ic_180);
                this.btnItem2.setVisibility(0);
                this.btnItem2.setImageResource(C0853R.mipmap.ic_330);
                this.btnItem3.setVisibility(0);
                this.btnItem3.setImageResource(C0853R.mipmap.ic_3_3);
                this.btnItem4.setVisibility(0);
                this.btnItem4.setImageResource(C0853R.mipmap.ic_3_5up);
                return;
            case 1:
                int leMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE))).intValue();
                if (leMode == 106206) {
                    this.btnItem1.setAlpha(1.0f);
                    this.itemId = 1;
                } else if (leMode == 106207) {
                    this.btnItem2.setAlpha(1.0f);
                    this.itemId = 2;
                }
                this.btnItem1.setVisibility(0);
                this.btnItem1.setImageResource(C0853R.mipmap.ic_le_double_image);
                return;
            case 2:
                int dpMode = ((Integer) SPUtils.get(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S))).intValue();
                if (dpMode == 100012) {
                    this.btnItem1.setAlpha(1.0f);
                    this.itemId = 1;
                } else if (dpMode == 100013) {
                    this.btnItem2.setAlpha(1.0f);
                    this.itemId = 2;
                } else if (dpMode == 10014) {
                    this.btnItem3.setAlpha(1.0f);
                    this.itemId = 3;
                }
                this.btnItem1.setVisibility(0);
                this.btnItem1.setImageResource(C0853R.mipmap.ic_delay_2s);
                this.btnItem2.setVisibility(0);
                this.btnItem2.setImageResource(C0853R.mipmap.ic_delay_5s);
                this.btnItem3.setVisibility(0);
                this.btnItem3.setImageResource(C0853R.mipmap.ic_delay_10s);
                return;
            default:
                return;
        }
    }

    private void initListener() {
        this.btnNone.setOnClickListener(this);
        this.btnItem1.setOnClickListener(this);
        this.btnItem2.setOnClickListener(this);
        this.btnItem3.setOnClickListener(this);
        this.btnItem4.setOnClickListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.broad = new OrientationBroad();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScreenOrientationUtil.BC_OrientationChanged);
        this.context.registerReceiver(this.broad, filter);
    }

    public void unRegisterListener() {
        if (this.broad != null) {
            this.context.unregisterReceiver(this.broad);
        }
        EventBus.getDefault().unregister(this);
    }

    private class OrientationBroad extends BroadcastReceiver {
        private OrientationBroad() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(ScreenOrientationUtil.BC_OrientationChangedKey, -1) != -1) {
                FVPhotoModeCommonShortcutPop.this.getAngle();
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
            if (this.mStartAngle != angle) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view2, "rotation", new float[]{(float) this.mStartAngle, (float) angle});
                animator.setDuration(300);
                animator.setInterpolator(new LinearInterpolator());
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        int unused = FVPhotoModeCommonShortcutPop.this.mStartAngle = angle;
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

    public void setPop(PopupWindow pop2, final FVPhotoModeCommonShortcutPop csPop) {
        this.pop = pop2;
        if (pop2 != null) {
            pop2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    EventBus.getDefault().unregister(csPop);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModeSwitch(FVModeSelectEvent fvModeSelectEvent) {
        switch (fvModeSelectEvent.getMode()) {
            case Constants.PTZ_SEND_PHOTO_OR_VIDEO_DISMISS_POP:
                if (this.pop != null) {
                    this.pop.dismiss();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onClick(View v) {
        switch (this.type) {
            case 0:
                switch (v.getId()) {
                    case C0853R.C0855id.btn_none:
                        if (this.pop != null) {
                            this.pop.dismiss();
                        }
                        SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
                        Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                        Util.sendIntEventMessge(Constants.FULL_SHOT_CLOSE);
                        return;
                    case C0853R.C0855id.btn_item1:
                        if (this.itemId != 1) {
                            SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_180));
                            Util.sendIntEventMessge(Constants.FULL_SHOT_180);
                            Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                            this.btnItem1.setAlpha(1.0f);
                            this.btnItem3.setAlpha(0.5f);
                            this.btnItem2.setAlpha(0.5f);
                            this.btnItem4.setAlpha(0.5f);
                            this.itemId = 1;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item2:
                        if (this.itemId != 2) {
                            SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_330));
                            Util.sendIntEventMessge(Constants.FULL_SHOT_330);
                            Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                            this.btnItem2.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.btnItem3.setAlpha(0.5f);
                            this.btnItem4.setAlpha(0.5f);
                            this.itemId = 2;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item3:
                        if (this.itemId != 3) {
                            SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_3X3));
                            Util.sendIntEventMessge(Constants.FULL_SHOT_3X3);
                            Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                            this.btnItem3.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.btnItem2.setAlpha(0.5f);
                            this.btnItem4.setAlpha(0.5f);
                            this.itemId = 3;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item4:
                        if (this.itemId != 4) {
                            SPUtils.put(this.context, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_3X5));
                            Util.sendIntEventMessge(Constants.FULL_SHOT_3X5);
                            Util.sendIntEventMessge(Constants.FULL_SHOT_OPEN);
                            this.btnItem4.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.btnItem2.setAlpha(0.5f);
                            this.btnItem3.setAlpha(0.5f);
                            this.itemId = 4;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case 1:
                switch (v.getId()) {
                    case C0853R.C0855id.btn_none:
                        if (this.pop != null) {
                            this.pop.dismiss();
                        }
                        this.cameraManager.changeCameraManagerMode(1);
                        SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
                        Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                        Util.sendIntEventMessge(Constants.LONG_EXPOSURE_CLOSE);
                        return;
                    case C0853R.C0855id.btn_item1:
                        if (this.itemId != 1) {
                            SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_DOUBLE_IMAGE));
                            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_DOUBLE_IMAGE);
                            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_OPEN);
                            this.btnItem1.setAlpha(1.0f);
                            this.btnItem2.setAlpha(0.5f);
                            this.itemId = 1;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item2:
                        if (this.itemId != 2) {
                            SPUtils.put(this.context, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_TRACK));
                            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_TRACK);
                            Util.sendIntEventMessge(Constants.LONG_EXPOSURE_OPEN);
                            this.btnItem2.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.itemId = 2;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case 2:
                switch (v.getId()) {
                    case C0853R.C0855id.btn_none:
                        if (this.pop != null) {
                            this.pop.dismiss();
                        }
                        SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
                        Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_0S);
                        Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_CLOSE);
                        return;
                    case C0853R.C0855id.btn_item1:
                        if (this.itemId != 1) {
                            SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_2S));
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_2S);
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                            this.btnItem1.setAlpha(1.0f);
                            this.btnItem3.setAlpha(0.5f);
                            this.btnItem2.setAlpha(0.5f);
                            this.itemId = 1;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item2:
                        if (this.itemId != 2) {
                            SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_5S));
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_5S);
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                            this.btnItem2.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.btnItem3.setAlpha(0.5f);
                            this.itemId = 2;
                            return;
                        }
                        return;
                    case C0853R.C0855id.btn_item3:
                        if (this.itemId != 3) {
                            SPUtils.put(this.context, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_10S));
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_10S);
                            Util.sendIntEventMessge(Constants.DELAY_TAKE_PHOTO_OPEN);
                            this.btnItem3.setAlpha(1.0f);
                            this.btnItem1.setAlpha(0.5f);
                            this.btnItem2.setAlpha(0.5f);
                            this.itemId = 3;
                            return;
                        }
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }
}
