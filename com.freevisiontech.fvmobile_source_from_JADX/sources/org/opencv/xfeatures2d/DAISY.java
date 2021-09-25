package org.opencv.xfeatures2d;

import org.opencv.core.Mat;
import org.opencv.features2d.Feature2D;

public class DAISY extends Feature2D {
    public static final int NRM_FULL = 102;
    public static final int NRM_NONE = 100;
    public static final int NRM_PARTIAL = 101;
    public static final int NRM_SIFT = 103;

    private static native long create_0(float f, int i, int i2, int i3, int i4, long j, boolean z, boolean z2);

    private static native long create_1();

    private static native void delete(long j);

    protected DAISY(long addr) {
        super(addr);
    }

    public static DAISY create(float radius, int q_radius, int q_theta, int q_hist, int norm, Mat H, boolean interpolation, boolean use_orientation) {
        return new DAISY(create_0(radius, q_radius, q_theta, q_hist, norm, H.nativeObj, interpolation, use_orientation));
    }

    public static DAISY create() {
        return new DAISY(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
