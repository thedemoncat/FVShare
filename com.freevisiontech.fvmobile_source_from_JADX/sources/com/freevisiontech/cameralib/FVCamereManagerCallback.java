package com.freevisiontech.cameralib;

import android.graphics.Bitmap;
import android.hardware.camera2.DngCreator;
import android.media.Image;

public interface FVCamereManagerCallback {
    void onCameraClosed();

    void onCameraDisconnected();

    void onCameraError(int i);

    void onCameraStarted();

    void onPictureTakeCompleted();

    void onPictureTakeError(int i);

    void onPictureTaken(Bitmap bitmap);

    void onRawPictureTaken(Image image, DngCreator dngCreator);

    void onRecordEnd(String str);

    void onRecordError(String str);

    void onRecordStarted();
}
