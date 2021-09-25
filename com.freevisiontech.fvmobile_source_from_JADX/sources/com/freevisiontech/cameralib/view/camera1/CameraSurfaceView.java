package com.freevisiontech.cameralib.view.camera1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.freevisiontech.cameralib.AspectRatio;

public class CameraSurfaceView extends SurfaceView {
    private static final String TAG = "CameraSurfaceView";
    Context mContext;
    CameraView mParent;
    SurfaceHolder mSurfaceHolder = getHolder();

    public CameraSurfaceView(Context context, AttributeSet attrs, CameraView parent) {
        super(context, attrs);
        this.mContext = context;
        this.mSurfaceHolder.setFormat(-2);
        this.mSurfaceHolder.setType(3);
        this.mParent = parent;
        this.mSurfaceHolder.addCallback(this.mParent);
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mSurfaceHolder;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        AspectRatio aspectRatio = this.mParent.cameraManager.getAspectRatio();
        if (width < height) {
            setMeasuredDimension((aspectRatio.getY() * height) / aspectRatio.getX(), height);
        } else {
            setMeasuredDimension(width, height);
        }
    }
}
