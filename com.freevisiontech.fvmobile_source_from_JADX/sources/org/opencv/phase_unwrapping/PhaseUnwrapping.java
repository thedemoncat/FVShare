package org.opencv.phase_unwrapping;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;

public class PhaseUnwrapping extends Algorithm {
    private static native void delete(long j);

    private static native void unwrapPhaseMap_0(long j, long j2, long j3, long j4);

    private static native void unwrapPhaseMap_1(long j, long j2, long j3);

    protected PhaseUnwrapping(long addr) {
        super(addr);
    }

    public void unwrapPhaseMap(Mat wrappedPhaseMap, Mat unwrappedPhaseMap, Mat shadowMask) {
        unwrapPhaseMap_0(this.nativeObj, wrappedPhaseMap.nativeObj, unwrappedPhaseMap.nativeObj, shadowMask.nativeObj);
    }

    public void unwrapPhaseMap(Mat wrappedPhaseMap, Mat unwrappedPhaseMap) {
        unwrapPhaseMap_1(this.nativeObj, wrappedPhaseMap.nativeObj, unwrappedPhaseMap.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
