package org.opencv.tracking;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class MultiTracker {
    protected final long nativeObj;

    private static native long MultiTracker_0(String str);

    private static native long MultiTracker_1();

    private static native boolean add_0(long j, long j2, int i, int i2, int i3, int i4);

    private static native boolean add_1(long j, long j2, long j3);

    private static native boolean add_2(long j, String str, long j2, int i, int i2, int i3, int i4);

    private static native boolean add_3(long j, String str, long j2, long j3);

    private static native void delete(long j);

    private static native boolean update_0(long j, long j2, long j3);

    protected MultiTracker(long addr) {
        this.nativeObj = addr;
    }

    public MultiTracker(String trackerType) {
        this.nativeObj = MultiTracker_0(trackerType);
    }

    public MultiTracker() {
        this.nativeObj = MultiTracker_1();
    }

    public boolean add(Mat image, Rect boundingBox) {
        return add_0(this.nativeObj, image.nativeObj, boundingBox.f1130x, boundingBox.f1131y, boundingBox.width, boundingBox.height);
    }

    public boolean add(Mat image, MatOfRect boundingBox) {
        return add_1(this.nativeObj, image.nativeObj, boundingBox.nativeObj);
    }

    public boolean add(String trackerType, Mat image, Rect boundingBox) {
        return add_2(this.nativeObj, trackerType, image.nativeObj, boundingBox.f1130x, boundingBox.f1131y, boundingBox.width, boundingBox.height);
    }

    public boolean add(String trackerType, Mat image, MatOfRect boundingBox) {
        return add_3(this.nativeObj, trackerType, image.nativeObj, boundingBox.nativeObj);
    }

    public boolean update(Mat image, MatOfRect boundingBox) {
        return update_0(this.nativeObj, image.nativeObj, boundingBox.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
