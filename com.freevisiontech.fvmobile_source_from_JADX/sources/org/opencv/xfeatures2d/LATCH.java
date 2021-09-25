package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class LATCH extends Feature2D {
    private static native long create_0(int i, boolean z, int i2);

    private static native long create_1();

    private static native void delete(long j);

    protected LATCH(long addr) {
        super(addr);
    }

    public static LATCH create(int bytes, boolean rotationInvariance, int half_ssd_size) {
        return new LATCH(create_0(bytes, rotationInvariance, half_ssd_size));
    }

    public static LATCH create() {
        return new LATCH(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
