package org.opencv.structured_light;

import java.util.List;
import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

public class StructuredLightPattern extends Algorithm {
    private static native boolean decode_0(long j, long j2, long j3, long j4, long j5, int i);

    private static native boolean decode_1(long j, long j2, long j3);

    private static native void delete(long j);

    private static native boolean generate_0(long j, long j2);

    protected StructuredLightPattern(long addr) {
        super(addr);
    }

    public boolean decode(List<Mat> patternImages, Mat disparityMap, List<Mat> blackImages, List<Mat> whiteImages, int flags) {
        return decode_0(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, disparityMap.nativeObj, Converters.vector_Mat_to_Mat(blackImages).nativeObj, Converters.vector_Mat_to_Mat(whiteImages).nativeObj, flags);
    }

    public boolean decode(List<Mat> patternImages, Mat disparityMap) {
        return decode_1(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, disparityMap.nativeObj);
    }

    public boolean generate(List<Mat> patternImages) {
        Mat patternImages_mat = new Mat();
        boolean retVal = generate_0(this.nativeObj, patternImages_mat.nativeObj);
        Converters.Mat_to_vector_Mat(patternImages_mat, patternImages);
        patternImages_mat.release();
        return retVal;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
