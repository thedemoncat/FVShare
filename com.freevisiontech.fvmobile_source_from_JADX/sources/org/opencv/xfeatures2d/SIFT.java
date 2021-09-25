package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class SIFT extends Feature2D {
    private static native long create_0(int i, int i2, double d, double d2, double d3);

    private static native long create_1();

    private static native void delete(long j);

    protected SIFT(long addr) {
        super(addr);
    }

    public static SIFT create(int nfeatures, int nOctaveLayers, double contrastThreshold, double edgeThreshold, double sigma) {
        return new SIFT(create_0(nfeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma));
    }

    public static SIFT create() {
        return new SIFT(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
