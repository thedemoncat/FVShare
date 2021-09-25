package com.freevisiontech.cameralib.view.camera2;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import com.freevisiontech.cameralib.FVCamera2Manager;
import com.freevisiontech.cameralib.utils.CameraUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class CameraView extends com.freevisiontech.cameralib.view.CameraView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "Camera2.CameraView";
    AttributeSet attributeSet;
    FVCamera2Manager cameraManager = null;
    private AtomicInteger cameraStates = new AtomicInteger(-1);
    CameraTextureView cameraTextureView = null;

    public CameraView(@NonNull Context context) {
        super(context);
    }

    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attributeSet = attrs;
    }

    public void setCameraManager(FVCamera2Manager manager) {
        this.cameraManager = manager;
    }

    public boolean isAvailable() {
        if (this.cameraTextureView == null) {
            return false;
        }
        return this.cameraTextureView.isAvailable();
    }

    public void refreshTextureView() {
        if (this.cameraTextureView != null) {
            this.cameraTextureView.requestLayout();
        }
    }

    public void addCameraTextureView() {
        if (this.cameraTextureView == null) {
            this.cameraTextureView = new CameraTextureView(getContext(), this.attributeSet, this);
            this.cameraTextureView.setSurfaceTextureListener(this);
        }
        addView(this.cameraTextureView);
    }

    public CameraTextureView getCameraTextureView() {
        return this.cameraTextureView;
    }

    public Surface getSurface() {
        return new Surface(this.cameraTextureView.getSurfaceTexture());
    }

    public void onResume() {
        CameraUtils.LogV("CameraView2", "onResume " + this.cameraStates.get());
        if (this.cameraStates.get() == 0) {
            CameraUtils.LogV("CameraView2", "onResume: to do setupCamera");
            if (this.cameraManager.setupCamera(0, 0)) {
                CameraUtils.LogV("CameraView2", "onResume: do setupCamera success");
                this.cameraStates.set(1);
                return;
            }
            CameraUtils.LogV("CameraView2", "onResume: do setupCamera failed");
        }
    }

    public void onPause() {
        CameraUtils.LogV("CameraView2", "onPause " + this.cameraStates.get());
        if (this.cameraStates.get() == 1) {
            this.cameraStates.set(0);
            CameraUtils.LogV("CameraView2", "onPause do destroyCamrea");
            this.cameraManager.destroy();
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        CameraUtils.LogV("CameraView2", "onSurfaceTextureAvailable...:" + width + "x" + height + " state=" + this.cameraStates.get());
        if (this.cameraStates.get() < 1) {
            CameraUtils.LogV("CameraView2", "onSurfaceTextureAvailable: to do setupCamrea");
            this.cameraStates.set(1);
            if (this.cameraManager.setupCamera(width, height)) {
                CameraUtils.LogV("CameraView2", "onSurfaceTextureAvailable: do setupCamrea success");
                return;
            }
            this.cameraStates.set(-1);
            CameraUtils.LogV("CameraView2", "onSurfaceTextureAvailable: do setupCamrea failed");
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        CameraUtils.LogV("CameraView2", "onSurfaceTextureSizeChanged...:" + width + "x" + height);
        if (this.cameraStates.get() < 1) {
            CameraUtils.LogV("CameraView2", "onSurfaceTextureSizeChanged: to do setupCamera");
            if (this.cameraManager.setupCamera(0, 0)) {
                CameraUtils.LogV("CameraView2", "onSurfaceTextureSizeChanged: do setupCamera success");
                this.cameraStates.set(1);
                return;
            }
            CameraUtils.LogV("CameraView2", "onSurfaceTextureSizeChanged: do setupCamera failed");
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        CameraUtils.LogV("CameraView2", "onSurfaceTextureDestroyed... state=" + this.cameraStates.get());
        if (this.cameraStates.get() == 1) {
            CameraUtils.LogV("CameraView2", "onSurfaceTextureDestroyed do destroyCamrea");
            this.cameraStates.set(0);
            this.cameraManager.destroy();
        }
        return true;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
