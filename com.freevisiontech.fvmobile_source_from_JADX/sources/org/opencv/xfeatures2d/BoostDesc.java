package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class BoostDesc extends Feature2D {
    private static native long create_0(int i, boolean z, float f);

    private static native long create_1();

    private static native void delete(long j);

    protected BoostDesc(long addr) {
        super(addr);
    }

    public static BoostDesc create(int desc, boolean use_scale_orientation, float scale_factor) {
        return new BoostDesc(create_0(desc, use_scale_orientation, scale_factor));
    }

    public static BoostDesc create() {
        return new BoostDesc(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
