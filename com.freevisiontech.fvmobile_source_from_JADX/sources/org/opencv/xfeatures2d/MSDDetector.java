package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class MSDDetector extends Feature2D {
    private static native void delete(long j);

    protected MSDDetector(long addr) {
        super(addr);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
