package org.opencv.tracking;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class Tracking {
    private static native double[] selectROI_0(long j, boolean z);

    private static native double[] selectROI_1(long j);

    private static native double[] selectROI_2(String str, long j, boolean z, boolean z2);

    private static native double[] selectROI_3(String str, long j);

    private static native void selectROI_4(String str, long j, long j2, boolean z);

    private static native void selectROI_5(String str, long j, long j2);

    public static Rect selectROI(Mat img, boolean fromCenter) {
        return new Rect(selectROI_0(img.nativeObj, fromCenter));
    }

    public static Rect selectROI(Mat img) {
        return new Rect(selectROI_1(img.nativeObj));
    }

    public static Rect selectROI(String windowName, Mat img, boolean showCrossair, boolean fromCenter) {
        return new Rect(selectROI_2(windowName, img.nativeObj, showCrossair, fromCenter));
    }

    public static Rect selectROI(String windowName, Mat img) {
        return new Rect(selectROI_3(windowName, img.nativeObj));
    }

    public static void selectROI(String windowName, Mat img, MatOfRect boundingBox, boolean fromCenter) {
        selectROI_4(windowName, img.nativeObj, boundingBox.nativeObj, fromCenter);
    }

    public static void selectROI(String windowName, Mat img, MatOfRect boundingBox) {
        selectROI_5(windowName, img.nativeObj, boundingBox.nativeObj);
    }
}
