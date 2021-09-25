package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.utils.Converters;

public class Features2d {
    public static final int DRAW_OVER_OUTIMG = 1;
    public static final int DRAW_RICH_KEYPOINTS = 4;
    public static final int NOT_DRAW_SINGLE_POINTS = 2;

    private static native void drawKeypoints_0(long j, long j2, long j3, double d, double d2, double d3, double d4, int i);

    private static native void drawKeypoints_1(long j, long j2, long j3);

    private static native void drawMatches2_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, long j7, int i);

    private static native void drawMatches2_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void drawMatchesKnn_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, long j7, int i);

    private static native void drawMatchesKnn_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void drawMatches_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, long j7, int i);

    private static native void drawMatches_1(long j, long j2, long j3, long j4, long j5, long j6);

    public static void drawKeypoints(Mat image, MatOfKeyPoint keypoints, Mat outImage, Scalar color, int flags) {
        drawKeypoints_0(image.nativeObj, keypoints.nativeObj, outImage.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], flags);
    }

    public static void drawKeypoints(Mat image, MatOfKeyPoint keypoints, Mat outImage) {
        drawKeypoints_1(image.nativeObj, keypoints.nativeObj, outImage.nativeObj);
    }

    public static void drawMatches(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, MatOfDMatch matches1to2, Mat outImg, Scalar matchColor, Scalar singlePointColor, MatOfByte matchesMask, int flags) {
        drawMatches_0(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, matches1to2.nativeObj, outImg.nativeObj, matchColor.val[0], matchColor.val[1], matchColor.val[2], matchColor.val[3], singlePointColor.val[0], singlePointColor.val[1], singlePointColor.val[2], singlePointColor.val[3], matchesMask.nativeObj, flags);
    }

    public static void drawMatches(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, MatOfDMatch matches1to2, Mat outImg) {
        drawMatches_1(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, matches1to2.nativeObj, outImg.nativeObj);
    }

    public static void drawMatches2(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, List<MatOfDMatch> matches1to2, Mat outImg, Scalar matchColor, Scalar singlePointColor, List<MatOfByte> matchesMask, int flags) {
        drawMatches2_0(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, Converters.vector_vector_DMatch_to_Mat(matches1to2, new ArrayList(matches1to2 != null ? matches1to2.size() : 0)).nativeObj, outImg.nativeObj, matchColor.val[0], matchColor.val[1], matchColor.val[2], matchColor.val[3], singlePointColor.val[0], singlePointColor.val[1], singlePointColor.val[2], singlePointColor.val[3], Converters.vector_vector_char_to_Mat(matchesMask, new ArrayList(matchesMask != null ? matchesMask.size() : 0)).nativeObj, flags);
    }

    public static void drawMatches2(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, List<MatOfDMatch> matches1to2, Mat outImg) {
        drawMatches2_1(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, Converters.vector_vector_DMatch_to_Mat(matches1to2, new ArrayList(matches1to2 != null ? matches1to2.size() : 0)).nativeObj, outImg.nativeObj);
    }

    public static void drawMatchesKnn(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, List<MatOfDMatch> matches1to2, Mat outImg, Scalar matchColor, Scalar singlePointColor, List<MatOfByte> matchesMask, int flags) {
        drawMatchesKnn_0(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, Converters.vector_vector_DMatch_to_Mat(matches1to2, new ArrayList(matches1to2 != null ? matches1to2.size() : 0)).nativeObj, outImg.nativeObj, matchColor.val[0], matchColor.val[1], matchColor.val[2], matchColor.val[3], singlePointColor.val[0], singlePointColor.val[1], singlePointColor.val[2], singlePointColor.val[3], Converters.vector_vector_char_to_Mat(matchesMask, new ArrayList(matchesMask != null ? matchesMask.size() : 0)).nativeObj, flags);
    }

    public static void drawMatchesKnn(Mat img1, MatOfKeyPoint keypoints1, Mat img2, MatOfKeyPoint keypoints2, List<MatOfDMatch> matches1to2, Mat outImg) {
        drawMatchesKnn_1(img1.nativeObj, keypoints1.nativeObj, img2.nativeObj, keypoints2.nativeObj, Converters.vector_vector_DMatch_to_Mat(matches1to2, new ArrayList(matches1to2 != null ? matches1to2.size() : 0)).nativeObj, outImg.nativeObj);
    }
}
