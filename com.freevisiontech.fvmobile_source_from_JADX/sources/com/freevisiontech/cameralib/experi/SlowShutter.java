package com.freevisiontech.cameralib.experi;

import android.graphics.Bitmap;
import com.freevisiontech.cameralib.FVCamera1Manager;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.filterlib.FilterType;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageFilter;
import p008jp.p009co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;

public class SlowShutter {
    private GPUImageFilter blendfilter = null;
    /* access modifiers changed from: private */
    public FVCamera1Manager cameraManager = null;
    private long interval = 1000;
    /* access modifiers changed from: private */
    public boolean isSlownShutterOn = false;
    private Size mPhotoSize = null;
    private int slowshuttermode = 0;

    public SlowShutter(FVCamera1Manager parent) {
        this.cameraManager = parent;
    }

    public void startSlowShutter(int mode, Size photosize) {
        this.isSlownShutterOn = true;
        this.slowshuttermode = mode;
        this.mPhotoSize = photosize;
        takephoto();
    }

    public void stopSlowShutter() {
        this.isSlownShutterOn = false;
    }

    public void destroy() {
        this.slowshuttermode = 0;
    }

    /* access modifiers changed from: private */
    public void imageBlend(Bitmap bitmap) {
        if (bitmap != null) {
            createImageBlend(bitmap);
            try {
                Thread.sleep(this.interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            takephoto();
        }
    }

    private void createImageBlend(Bitmap bitmap) {
        if (this.slowshuttermode == 1) {
            createImageBlendByOverlayMode(bitmap);
        }
    }

    private void createImageBlendByOverlayMode(Bitmap bitmap) {
        if (this.blendfilter == null && this.cameraManager.setFilter(FilterType.BLEND_SCREEN)) {
            this.blendfilter = this.cameraManager.getFilter();
        }
        ((GPUImageScreenBlendFilter) this.blendfilter).setBitmap(bitmap);
    }

    /* access modifiers changed from: private */
    public void slowShutterFinished() {
        this.cameraManager.setFilter(FilterType.NOFILTER);
        destroy();
    }

    private void takephoto() {
        new Thread(new Runnable() {
            public void run() {
                Bitmap bitmap;
                try {
                    bitmap = SlowShutter.this.cameraManager.getGpuImageView().capture();
                } catch (InterruptedException e) {
                    bitmap = null;
                }
                if (SlowShutter.this.isSlownShutterOn) {
                    SlowShutter.this.imageBlend(bitmap);
                    return;
                }
                SlowShutter.this.slowShutterFinished();
                if (SlowShutter.this.cameraManager.mCallback != null) {
                    SlowShutter.this.cameraManager.mCallback.onPictureTaken(bitmap);
                }
            }
        }).start();
    }
}
