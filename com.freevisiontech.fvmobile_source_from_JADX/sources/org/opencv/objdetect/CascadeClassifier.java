package org.opencv.objdetect;

import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;

public class CascadeClassifier {
    protected final long nativeObj;

    private static native long CascadeClassifier_0(String str);

    private static native long CascadeClassifier_1();

    private static native boolean convert_0(String str, String str2);

    private static native void delete(long j);

    private static native void detectMultiScale2_0(long j, long j2, long j3, long j4, double d, int i, int i2, double d2, double d3, double d4, double d5);

    private static native void detectMultiScale2_1(long j, long j2, long j3, long j4);

    private static native void detectMultiScale3_0(long j, long j2, long j3, long j4, long j5, double d, int i, int i2, double d2, double d3, double d4, double d5, boolean z);

    private static native void detectMultiScale3_1(long j, long j2, long j3, long j4, long j5);

    private static native void detectMultiScale_0(long j, long j2, long j3, double d, int i, int i2, double d2, double d3, double d4, double d5);

    private static native void detectMultiScale_1(long j, long j2, long j3);

    private static native boolean empty_0(long j);

    private static native int getFeatureType_0(long j);

    private static native double[] getOriginalWindowSize_0(long j);

    private static native boolean isOldFormatCascade_0(long j);

    private static native boolean load_0(long j, String str);

    protected CascadeClassifier(long addr) {
        this.nativeObj = addr;
    }

    public CascadeClassifier(String filename) {
        this.nativeObj = CascadeClassifier_0(filename);
    }

    public CascadeClassifier() {
        this.nativeObj = CascadeClassifier_1();
    }

    public Size getOriginalWindowSize() {
        return new Size(getOriginalWindowSize_0(this.nativeObj));
    }

    public static boolean convert(String oldcascade, String newcascade) {
        return convert_0(oldcascade, newcascade);
    }

    public boolean empty() {
        return empty_0(this.nativeObj);
    }

    public boolean isOldFormatCascade() {
        return isOldFormatCascade_0(this.nativeObj);
    }

    public boolean load(String filename) {
        return load_0(this.nativeObj, filename);
    }

    public int getFeatureType() {
        return getFeatureType_0(this.nativeObj);
    }

    public void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize) {
        detectMultiScale_0(this.nativeObj, image.nativeObj, objects.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height);
    }

    public void detectMultiScale(Mat image, MatOfRect objects) {
        detectMultiScale_1(this.nativeObj, image.nativeObj, objects.nativeObj);
    }

    public void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize) {
        detectMultiScale2_0(this.nativeObj, image.nativeObj, objects.nativeObj, numDetections.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height);
    }

    public void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections) {
        detectMultiScale2_1(this.nativeObj, image.nativeObj, objects.nativeObj, numDetections.nativeObj);
    }

    public void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize, boolean outputRejectLevels) {
        detectMultiScale3_0(this.nativeObj, image.nativeObj, objects.nativeObj, rejectLevels.nativeObj, levelWeights.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height, outputRejectLevels);
    }

    public void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights) {
        detectMultiScale3_1(this.nativeObj, image.nativeObj, objects.nativeObj, rejectLevels.nativeObj, levelWeights.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
