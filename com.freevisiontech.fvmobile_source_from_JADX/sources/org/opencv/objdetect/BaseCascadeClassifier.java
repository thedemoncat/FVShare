package org.opencv.objdetect;

import org.opencv.core.Algorithm;

public class BaseCascadeClassifier extends Algorithm {
    private static native void delete(long j);

    protected BaseCascadeClassifier(long addr) {
        super(addr);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
