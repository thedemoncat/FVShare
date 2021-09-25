package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class StarDetector extends Feature2D {
    private static native long create_0(int i, int i2, int i3, int i4, int i5);

    private static native long create_1();

    private static native void delete(long j);

    protected StarDetector(long addr) {
        super(addr);
    }

    public static StarDetector create(int maxSize, int responseThreshold, int lineThresholdProjected, int lineThresholdBinarized, int suppressNonmaxSize) {
        return new StarDetector(create_0(maxSize, responseThreshold, lineThresholdProjected, lineThresholdBinarized, suppressNonmaxSize));
    }

    public static StarDetector create() {
        return new StarDetector(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
