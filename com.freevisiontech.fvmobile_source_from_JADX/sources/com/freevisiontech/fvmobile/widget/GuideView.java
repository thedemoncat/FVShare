package com.freevisiontech.fvmobile.widget;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utility.CameraUtils;
import com.freevisiontech.fvmobile.utility.Util;
import com.vise.log.ViseLog;
import java.util.List;

public class GuideView extends View {
    private String TAG = "GuideView";
    private int borderSize = 0;
    private boolean drawNew = true;
    private Bitmap hintLeft;
    private Bitmap hintRight;
    private float hintRightXOffset = 0.0f;
    private RelativeLayout leftContainer;
    private float leftHighLightZoneLength = 0.0f;
    private int leftHintPosition = 2;
    private Context mContext;
    private int mHeight;
    private int mWidth;
    private LinearLayout rightContainer;
    private float rightHighLightZoneLength = 0.0f;
    private int rightHintPosition = 0;
    private float yOffset = 0.0f;

    public int getLeftHintPosition() {
        return this.leftHintPosition;
    }

    public void setLeftHintPosition(int leftHintPosition2) {
        this.leftHintPosition = leftHintPosition2;
    }

    public int getRightHintPosition() {
        return this.rightHintPosition;
    }

    public void setRightHintPosition(int rightHintPosition2) {
        this.rightHintPosition = rightHintPosition2;
    }

    public boolean isDrawNew() {
        return this.drawNew;
    }

    public void setDrawNew(boolean drawNew2) {
        this.drawNew = drawNew2;
    }

    public GuideView(Context context) {
        super(context);
        this.mContext = context;
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float lLeft;
        float rLeft;
        super.onDraw(canvas);
        ViseLog.m1466e("onDraw");
        if (this.drawNew) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            this.mWidth = metrics.widthPixels;
            this.mHeight = metrics.heightPixels;
            Paint paint = new Paint();
            int guideLayerNum = canvas.saveLayer(0.0f, 0.0f, (float) this.mWidth, (float) this.mHeight, (Paint) null, 31);
            paint.setARGB(CompanyIdentifierResolver.BEATS_ELECTRONICS, 0, 0, 0);
            canvas.drawRect(0.0f, 0.0f, (float) this.mWidth, (float) this.mHeight, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            this.yOffset = (float) ((this.leftContainer.getHeight() / 4) * this.leftHintPosition);
            int[] lLocation = new int[2];
            this.leftContainer.getLocationOnScreen(lLocation);
            this.leftHighLightZoneLength = (float) this.leftContainer.getWidth();
            ViseLog.m1466e(this.TAG + " left yOffSet: " + this.yOffset);
            Resources resources = this.mContext.getResources();
            int statusBarHeight2 = resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
            if (lLocation[0] < 40) {
                statusBarHeight2 = 0;
            }
            if (CameraUtils.hasNotchScreen((Activity) this.mContext)) {
                lLeft = (float) ((lLocation[0] - statusBarHeight2) + Util.dip2px(this.mContext, 12.0f));
            } else {
                lLeft = (float) (lLocation[0] - statusBarHeight2);
            }
            float lTop = ((float) lLocation[1]) + this.yOffset + ((((float) (this.leftContainer.getHeight() / 4)) - this.leftHighLightZoneLength) / 2.0f);
            float lRight = lLeft + this.leftHighLightZoneLength;
            float lBottom = (this.leftHighLightZoneLength + lTop) - ((float) 0);
            canvas.drawRoundRect(new RectF(lLeft, lTop, lRight, lBottom), 20.0f, 20.0f, paint);
            this.yOffset = (float) ((this.rightContainer.getHeight() / 5) * this.rightHintPosition);
            int[] rLocation = new int[2];
            this.rightContainer.getLocationOnScreen(rLocation);
            this.rightHighLightZoneLength = (float) this.rightContainer.getWidth();
            if (CameraUtils.hasNotchScreen((Activity) this.mContext)) {
                rLeft = (float) ((rLocation[0] - statusBarHeight2) + Util.dip2px(this.mContext, 12.0f));
            } else {
                rLeft = (float) (rLocation[0] - statusBarHeight2);
            }
            float rTop = ((float) rLocation[1]) + this.yOffset + ((((float) (this.rightContainer.getHeight() / 5)) - this.rightHighLightZoneLength) / 2.0f);
            float rRight = rLeft + this.rightHighLightZoneLength;
            float rBottom = (this.rightHighLightZoneLength + rTop) - ((float) 0);
            canvas.drawRoundRect(new RectF(rLeft, rTop, rRight, rBottom), 20.0f, 20.0f, paint);
            ViseLog.m1466e(this.TAG + " right yOffSet: " + this.yOffset);
            paint.setXfermode((Xfermode) null);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            float leftHintY = lTop + (this.leftHighLightZoneLength / 2.0f);
            canvas.drawBitmap(this.hintLeft, lRight + 20.0f, leftHintY, paint);
            float rightHintX = (rLeft - this.hintRightXOffset) - 20.0f;
            float rightHintY = rTop + (this.rightHighLightZoneLength / 2.0f);
            canvas.drawBitmap(this.hintRight, rightHintX, rightHintY, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(-1);
            paint.setStrokeWidth(10.0f);
            RectF rectF = new RectF(5.0f + lLeft, 5.0f + lTop, lRight - 5.0f, lBottom - 5.0f);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawRoundRect(rectF, 20.0f, 20.0f, paint);
            canvas.drawRoundRect(new RectF(5.0f + rLeft, 5.0f + rTop, rRight - 5.0f, rBottom - 5.0f), 20.0f, 20.0f, paint);
            canvas.restoreToCount(guideLayerNum);
            ViseLog.m1466e(this.TAG + "   lLeft:" + lLeft + "    lTop:" + lTop + " lRight:" + lRight + " lBottom:" + lBottom);
        }
        this.drawNew = false;
    }

    public void setLeftHint(int resourceId) {
        this.hintLeft = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    private int getNavigationBarHeight() {
        int navigationHeight = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int widthReal = metrics.widthPixels;
        int heightReal = metrics.heightPixels;
        ((Activity) this.mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if (!(widthReal == width && heightReal == height)) {
            Resources resources = this.mContext.getResources();
            navigationHeight = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        }
        ViseLog.m1466e(this.TAG + " Navi height:" + navigationHeight);
        return navigationHeight;
    }

    public void setRightHint(int resourceId) {
        this.hintRight = BitmapFactory.decodeResource(getResources(), resourceId);
        this.hintRightXOffset = (float) this.hintRight.getWidth();
    }

    public boolean isForeground(String className) {
        if (this.mContext == null || TextUtils.isEmpty(className)) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> list = ((ActivityManager) this.mContext.getSystemService("activity")).getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            ViseLog.m1466e("FVMainActivity className: " + cpn.getClassName());
            if (className.equals(cpn.getClassName())) {
                ViseLog.m1466e("FVMainActivity className: " + cpn.getClassName());
                return true;
            }
        }
        return false;
    }

    public void setLeftContainer(RelativeLayout ll) {
        this.leftContainer = ll;
    }

    public void setRightContainer(LinearLayout ll) {
        this.rightContainer = ll;
    }
}
