package org.opencv.features2d;

import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;

public class BRISK extends Feature2D {
    private static native long create_0(int i, int i2, float f);

    private static native long create_1();

    private static native long create_2(long j, long j2, float f, float f2, long j3);

    private static native long create_3(long j, long j2);

    private static native void delete(long j);

    protected BRISK(long addr) {
        super(addr);
    }

    public static BRISK create(int thresh, int octaves, float patternScale) {
        return new BRISK(create_0(thresh, octaves, patternScale));
    }

    public static BRISK create() {
        return new BRISK(create_1());
    }

    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList, float dMax, float dMin, MatOfInt indexChange) {
        return new BRISK(create_2(radiusList.nativeObj, numberList.nativeObj, dMax, dMin, indexChange.nativeObj));
    }

    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList) {
        return new BRISK(create_3(radiusList.nativeObj, numberList.nativeObj));
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
