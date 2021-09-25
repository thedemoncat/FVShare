package org.opencv.xmisc;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public class XMiscing {
    protected final long nativeObj;

    private static native boolean blurDetect_0(long j, long j2, boolean z);

    private static native boolean blurDetect_1(long j, long j2);

    private static native void clear_0(long j);

    private static native boolean cutDetect_0(long j, long j2, double d, double d2, int i, int i2, int i3, int i4, double[] dArr, boolean z);

    private static native boolean cutDetect_1(long j, long j2, double d, double d2, int i, int i2, int i3, int i4, double[] dArr);

    private static native void delete(long j);

    private static native boolean faceDectect_0(long j, long j2, int i, int i2, int i3, int i4, double[] dArr, boolean z);

    private static native boolean faceDectect_1(long j, long j2, int i, int i2, int i3, int i4, double[] dArr);

    private static native boolean faceLoadModel2_0(long j, String str);

    private static native boolean faceLoadModel3_0(long j, String str);

    private static native boolean faceRecLoadModel_0(long j, String str, String str2);

    private static native boolean faceRecognize_0(long j, long j2, long j3, boolean z);

    private static native boolean faceRecognize_1(long j, long j2, long j3);

    private static native long getInstance_0();

    protected XMiscing(long addr) {
        this.nativeObj = addr;
    }

    public static XMiscing getInstance() {
        return new XMiscing(getInstance_0());
    }

    public boolean blurDetect(Mat srcImage, boolean isYuv) {
        return blurDetect_0(this.nativeObj, srcImage.nativeObj, isYuv);
    }

    public boolean blurDetect(Mat srcImage) {
        return blurDetect_1(this.nativeObj, srcImage.nativeObj);
    }

    public boolean cutDetect(Mat srcImage, Point pt, Rect rect, boolean isYuv) {
        double[] rect_out = new double[4];
        boolean retVal = cutDetect_0(this.nativeObj, srcImage.nativeObj, pt.f1125x, pt.f1126y, rect.f1130x, rect.f1131y, rect.width, rect.height, rect_out, isYuv);
        if (rect != null) {
            rect.f1130x = (int) rect_out[0];
            rect.f1131y = (int) rect_out[1];
            rect.width = (int) rect_out[2];
            rect.height = (int) rect_out[3];
        }
        return retVal;
    }

    public boolean cutDetect(Mat srcImage, Point pt, Rect rect) {
        double[] rect_out = new double[4];
        boolean retVal = cutDetect_1(this.nativeObj, srcImage.nativeObj, pt.f1125x, pt.f1126y, rect.f1130x, rect.f1131y, rect.width, rect.height, rect_out);
        if (rect != null) {
            rect.f1130x = (int) rect_out[0];
            rect.f1131y = (int) rect_out[1];
            rect.width = (int) rect_out[2];
            rect.height = (int) rect_out[3];
        }
        return retVal;
    }

    public boolean faceDectect(Mat srcImage, Rect rect, boolean isYuv) {
        double[] rect_out = new double[4];
        boolean retVal = faceDectect_0(this.nativeObj, srcImage.nativeObj, rect.f1130x, rect.f1131y, rect.width, rect.height, rect_out, isYuv);
        if (rect != null) {
            rect.f1130x = (int) rect_out[0];
            rect.f1131y = (int) rect_out[1];
            rect.width = (int) rect_out[2];
            rect.height = (int) rect_out[3];
        }
        return retVal;
    }

    public boolean faceDectect(Mat srcImage, Rect rect) {
        double[] rect_out = new double[4];
        boolean retVal = faceDectect_1(this.nativeObj, srcImage.nativeObj, rect.f1130x, rect.f1131y, rect.width, rect.height, rect_out);
        if (rect != null) {
            rect.f1130x = (int) rect_out[0];
            rect.f1131y = (int) rect_out[1];
            rect.width = (int) rect_out[2];
            rect.height = (int) rect_out[3];
        }
        return retVal;
    }

    public boolean faceLoadModel2(String model_path) {
        return faceLoadModel2_0(this.nativeObj, model_path);
    }

    public boolean faceLoadModel3(String frontface_file) {
        return faceLoadModel3_0(this.nativeObj, frontface_file);
    }

    public boolean faceRecLoadModel(String param_files, String bin_files) {
        return faceRecLoadModel_0(this.nativeObj, param_files, bin_files);
    }

    public boolean faceRecognize(Mat srcImg, Mat dstImg, boolean isYuv) {
        return faceRecognize_0(this.nativeObj, srcImg.nativeObj, dstImg.nativeObj, isYuv);
    }

    public boolean faceRecognize(Mat srcImg, Mat dstImg) {
        return faceRecognize_1(this.nativeObj, srcImg.nativeObj, dstImg.nativeObj);
    }

    public void clear() {
        clear_0(this.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
