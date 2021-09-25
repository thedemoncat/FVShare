package com.freevisiontech.cameralib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CameraView extends FrameLayout {
    protected AttributeSet attributeSet;
    protected Context context;

    public CameraView(@NonNull Context context2) {
        super(context2);
    }

    public CameraView(@NonNull Context context2, @Nullable AttributeSet attrs) {
        super(context2, attrs);
        this.attributeSet = attrs;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public com.freevisiontech.cameralib.view.camera2.CameraView toCamera2view() {
        return (com.freevisiontech.cameralib.view.camera2.CameraView) this;
    }

    public com.freevisiontech.cameralib.view.camera1.CameraView toCamera1view() {
        return (com.freevisiontech.cameralib.view.camera1.CameraView) this;
    }
}
