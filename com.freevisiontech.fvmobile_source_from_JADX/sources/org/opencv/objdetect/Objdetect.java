package org.opencv.objdetect;

import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;

public class Objdetect {
    public static final int CASCADE_DO_CANNY_PRUNING = 1;
    public static final int CASCADE_DO_ROUGH_SEARCH = 8;
    public static final int CASCADE_FIND_BIGGEST_OBJECT = 4;
    public static final int CASCADE_SCALE_IMAGE = 2;

    private static native void groupRectangles_0(long j, long j2, int i, double d);

    private static native void groupRectangles_1(long j, long j2, int i);

    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold, double eps) {
        groupRectangles_0(rectList.nativeObj, weights.nativeObj, groupThreshold, eps);
    }

    public static void groupRectangles(MatOfRect rectList, MatOfInt weights, int groupThreshold) {
        groupRectangles_1(rectList.nativeObj, weights.nativeObj, groupThreshold);
    }
}
