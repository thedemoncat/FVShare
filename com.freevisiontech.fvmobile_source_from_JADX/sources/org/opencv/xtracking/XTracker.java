package org.opencv.xtracking;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class XTracker {
    protected final long nativeObj;

    private static native void clear_0(long j);

    private static native long create_0(String str);

    private static native void delete(long j);

    private static native boolean init_0(long j, long j2, int i, int i2, int i3, int i4);

    private static native boolean preInit_0(long j, long j2, int i, int i2, int i3, int i4, double[] dArr, long j3);

    private static native void setParam_0(long j, int i, int i2, int i3);

    private static native boolean update_0(long j, long j2, double[] dArr);

    protected XTracker(long addr) {
        this.nativeObj = addr;
    }

    public static XTracker create(String trackerType) {
        return new XTracker(create_0(trackerType));
    }

    public boolean init(Mat image, Rect roi) {
        return init_0(this.nativeObj, image.nativeObj, roi.f1130x, roi.f1131y, roi.width, roi.height);
    }

    public boolean preInit(Mat image, Rect roi, Mat flow) {
        double[] roi_out = new double[4];
        boolean retVal = preInit_0(this.nativeObj, image.nativeObj, roi.f1130x, roi.f1131y, roi.width, roi.height, roi_out, flow.nativeObj);
        if (roi != null) {
            roi.f1130x = (int) roi_out[0];
            roi.f1131y = (int) roi_out[1];
            roi.width = (int) roi_out[2];
            roi.height = (int) roi_out[3];
        }
        return retVal;
    }

    public boolean update(Mat image, Rect roi) {
        double[] roi_out = new double[4];
        boolean retVal = update_0(this.nativeObj, image.nativeObj, roi_out);
        if (roi != null) {
            roi.f1130x = (int) roi_out[0];
            roi.f1131y = (int) roi_out[1];
            roi.width = (int) roi_out[2];
            roi.height = (int) roi_out[3];
        }
        return retVal;
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    public void setParam(int width, int height, int isYUV) {
        setParam_0(this.nativeObj, width, height, isYUV);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
