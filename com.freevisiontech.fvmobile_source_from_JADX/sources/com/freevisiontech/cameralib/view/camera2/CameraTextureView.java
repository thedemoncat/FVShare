package com.freevisiontech.cameralib.view.camera2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import com.freevisiontech.cameralib.AspectRatio;
import com.freevisiontech.cameralib.utils.CameraUtils;

public class CameraTextureView extends TextureView {
    private static final String TAG = "CameraTextureView";
    private Context mContext;
    private CameraView mParent;

    public CameraTextureView(Context context, AttributeSet attrs, CameraView parent) {
        super(context, attrs);
        this.mContext = context;
        this.mParent = parent;
        setSurfaceTextureListener(this.mParent);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyle, CameraView parent) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.mParent = parent;
        setSurfaceTextureListener(this.mParent);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        AspectRatio aspectRatio = this.mParent.cameraManager.getAspectRatio();
        CameraUtils.LogV("Camera2.CameraView", "onMeasure: " + width + ":" + height);
        if (aspectRatio == null) {
            CameraUtils.LogV("Camera2.CameraView", "Original onMeasure:setMeasuredDimension:" + width + ":" + height);
        } else if (width < (aspectRatio.getX() * height) / aspectRatio.getY()) {
            CameraUtils.LogV("Camera2.CameraView", "Portrait onMeasure:setMeasuredDimension:" + width + ":" + height);
        } else {
            CameraUtils.LogV("Camera2.CameraView", "Landscape onMeasure:setMeasuredDimension:" + width + ":" + height);
        }
        setMeasuredDimension(width, height);
    }

    public void changeAspectRatio() {
        requestLayout();
    }
}
