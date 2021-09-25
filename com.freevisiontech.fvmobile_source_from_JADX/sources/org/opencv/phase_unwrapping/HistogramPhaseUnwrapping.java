package org.opencv.phase_unwrapping;

import org.opencv.core.Mat;

public class HistogramPhaseUnwrapping extends PhaseUnwrapping {
    private static native void delete(long j);

    private static native void getInverseReliabilityMap_0(long j, long j2);

    protected HistogramPhaseUnwrapping(long addr) {
        super(addr);
    }

    public void getInverseReliabilityMap(Mat reliabilityMap) {
        getInverseReliabilityMap_0(this.nativeObj, reliabilityMap.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
