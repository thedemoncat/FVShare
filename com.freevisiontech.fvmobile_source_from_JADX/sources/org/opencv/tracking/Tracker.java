package org.opencv.tracking;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class Tracker extends Algorithm {
    private static native long create_0(String str);

    private static native void delete(long j);

    private static native boolean init_0(long j, long j2, int i, int i2, int i3, int i4);

    private static native boolean update_0(long j, long j2, double[] dArr);

    protected Tracker(long addr) {
        super(addr);
    }

    public static Tracker create(String trackerType) {
        return new Tracker(create_0(trackerType));
    }

    public boolean init(Mat image, Rect boundingBox) {
        return init_0(this.nativeObj, image.nativeObj, boundingBox.f1130x, boundingBox.f1131y, boundingBox.width, boundingBox.height);
    }

    public boolean update(Mat image, Rect boundingBox) {
        double[] boundingBox_out = new double[4];
        boolean retVal = update_0(this.nativeObj, image.nativeObj, boundingBox_out);
        if (boundingBox != null) {
            boundingBox.f1130x = (int) boundingBox_out[0];
            boundingBox.f1131y = (int) boundingBox_out[1];
            boundingBox.width = (int) boundingBox_out[2];
            boundingBox.height = (int) boundingBox_out[3];
        }
        return retVal;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
