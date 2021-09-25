package org.opencv.xfeatures2d;

import org.opencv.core.MatOfInt;
import org.opencv.features2d.Feature2D;

public class FREAK extends Feature2D {
    public static final int NB_ORIENPAIRS = 45;
    public static final int NB_PAIRS = 512;
    public static final int NB_SCALES = 64;

    private static native long create_0(boolean z, boolean z2, float f, int i, long j);

    private static native long create_1();

    private static native void delete(long j);

    protected FREAK(long addr) {
        super(addr);
    }

    public static FREAK create(boolean orientationNormalized, boolean scaleNormalized, float patternScale, int nOctaves, MatOfInt selectedPairs) {
        return new FREAK(create_0(orientationNormalized, scaleNormalized, patternScale, nOctaves, selectedPairs.nativeObj));
    }

    public static FREAK create() {
        return new FREAK(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
