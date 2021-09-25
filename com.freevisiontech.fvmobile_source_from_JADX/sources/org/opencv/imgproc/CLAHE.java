package org.opencv.imgproc;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class CLAHE extends Algorithm {
    private static native void apply_0(long j, long j2, long j3);

    private static native void collectGarbage_0(long j);

    private static native void delete(long j);

    private static native double getClipLimit_0(long j);

    private static native double[] getTilesGridSize_0(long j);

    private static native void setClipLimit_0(long j, double d);

    private static native void setTilesGridSize_0(long j, double d, double d2);

    protected CLAHE(long addr) {
        super(addr);
    }

    public Size getTilesGridSize() {
        return new Size(getTilesGridSize_0(this.nativeObj));
    }

    public double getClipLimit() {
        return getClipLimit_0(this.nativeObj);
    }

    public void apply(Mat src, Mat dst) {
        apply_0(this.nativeObj, src.nativeObj, dst.nativeObj);
    }

    public void collectGarbage() {
        collectGarbage_0(this.nativeObj);
    }

    public void setClipLimit(double clipLimit) {
        setClipLimit_0(this.nativeObj, clipLimit);
    }

    public void setTilesGridSize(Size tileGridSize) {
        setTilesGridSize_0(this.nativeObj, tileGridSize.width, tileGridSize.height);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
