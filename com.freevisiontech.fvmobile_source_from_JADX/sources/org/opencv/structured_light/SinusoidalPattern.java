package org.opencv.structured_light;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

public class SinusoidalPattern extends StructuredLightPattern {
    private static native void computeDataModulationTerm_0(long j, long j2, long j3, long j4);

    private static native void computePhaseMap_0(long j, long j2, long j3, long j4, long j5);

    private static native void computePhaseMap_1(long j, long j2, long j3);

    private static native void delete(long j);

    private static native void findProCamMatches_0(long j, long j2, long j3, long j4);

    private static native void unwrapPhaseMap_0(long j, long j2, long j3, double d, double d2, long j4);

    private static native void unwrapPhaseMap_1(long j, long j2, long j3, double d, double d2);

    protected SinusoidalPattern(long addr) {
        super(addr);
    }

    public void computeDataModulationTerm(List<Mat> patternImages, Mat dataModulationTerm, Mat shadowMask) {
        computeDataModulationTerm_0(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, dataModulationTerm.nativeObj, shadowMask.nativeObj);
    }

    public void computePhaseMap(List<Mat> patternImages, Mat wrappedPhaseMap, Mat shadowMask, Mat fundamental) {
        computePhaseMap_0(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, wrappedPhaseMap.nativeObj, shadowMask.nativeObj, fundamental.nativeObj);
    }

    public void computePhaseMap(List<Mat> patternImages, Mat wrappedPhaseMap) {
        computePhaseMap_1(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, wrappedPhaseMap.nativeObj);
    }

    public void findProCamMatches(Mat projUnwrappedPhaseMap, Mat camUnwrappedPhaseMap, List<Mat> matches) {
        Mat matches_mat = new Mat();
        findProCamMatches_0(this.nativeObj, projUnwrappedPhaseMap.nativeObj, camUnwrappedPhaseMap.nativeObj, matches_mat.nativeObj);
        Converters.Mat_to_vector_Mat(matches_mat, matches);
        matches_mat.release();
    }

    public void unwrapPhaseMap(List<Mat> wrappedPhaseMap, Mat unwrappedPhaseMap, Size camSize, Mat shadowMask) {
        unwrapPhaseMap_0(this.nativeObj, Converters.vector_Mat_to_Mat(wrappedPhaseMap).nativeObj, unwrappedPhaseMap.nativeObj, camSize.width, camSize.height, shadowMask.nativeObj);
    }

    public void unwrapPhaseMap(List<Mat> wrappedPhaseMap, Mat unwrappedPhaseMap, Size camSize) {
        unwrapPhaseMap_1(this.nativeObj, Converters.vector_Mat_to_Mat(wrappedPhaseMap).nativeObj, unwrappedPhaseMap.nativeObj, camSize.width, camSize.height);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
