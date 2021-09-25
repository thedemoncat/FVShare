package org.opencv.video;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;

public class Video {
    private static final int CV_LKFLOW_GET_MIN_EIGENVALS = 8;
    private static final int CV_LKFLOW_INITIAL_GUESSES = 4;
    public static final int MOTION_AFFINE = 2;
    public static final int MOTION_EUCLIDEAN = 1;
    public static final int MOTION_HOMOGRAPHY = 3;
    public static final int MOTION_TRANSLATION = 0;
    public static final int OPTFLOW_FARNEBACK_GAUSSIAN = 256;
    public static final int OPTFLOW_LK_GET_MIN_EIGENVALS = 8;
    public static final int OPTFLOW_USE_INITIAL_FLOW = 4;

    private static native double[] CamShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    private static native int buildOpticalFlowPyramid_0(long j, long j2, double d, double d2, int i, boolean z, int i2, int i3, boolean z2);

    private static native int buildOpticalFlowPyramid_1(long j, long j2, double d, double d2, int i);

    private static native void calcOpticalFlowFarneback_0(long j, long j2, long j3, double d, int i, int i2, int i3, int i4, double d2, int i5);

    private static native void calcOpticalFlowPyrLK_0(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i, int i2, int i3, double d3, int i4, double d4);

    private static native void calcOpticalFlowPyrLK_1(long j, long j2, long j3, long j4, long j5, long j6, double d, double d2, int i);

    private static native void calcOpticalFlowPyrLK_2(long j, long j2, long j3, long j4, long j5, long j6);

    private static native long createBackgroundSubtractorKNN_0(int i, double d, boolean z);

    private static native long createBackgroundSubtractorKNN_1();

    private static native long createBackgroundSubtractorMOG2_0(int i, double d, boolean z);

    private static native long createBackgroundSubtractorMOG2_1();

    private static native long createOptFlow_DualTVL1_0();

    private static native long estimateRigidTransform_0(long j, long j2, boolean z);

    private static native double findTransformECC_0(long j, long j2, long j3, int i, int i2, int i3, double d, long j4);

    private static native double findTransformECC_1(long j, long j2, long j3, int i);

    private static native double findTransformECC_2(long j, long j2, long j3);

    private static native int meanShift_0(long j, int i, int i2, int i3, int i4, double[] dArr, int i5, int i6, double d);

    public static Mat estimateRigidTransform(Mat src, Mat dst, boolean fullAffine) {
        return new Mat(estimateRigidTransform_0(src.nativeObj, dst.nativeObj, fullAffine));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history, double dist2Threshold, boolean detectShadows) {
        return new BackgroundSubtractorKNN(createBackgroundSubtractorKNN_0(history, dist2Threshold, detectShadows));
    }

    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN() {
        return new BackgroundSubtractorKNN(createBackgroundSubtractorKNN_1());
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history, double varThreshold, boolean detectShadows) {
        return new BackgroundSubtractorMOG2(createBackgroundSubtractorMOG2_0(history, varThreshold, detectShadows));
    }

    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2() {
        return new BackgroundSubtractorMOG2(createBackgroundSubtractorMOG2_1());
    }

    public static DualTVL1OpticalFlow createOptFlow_DualTVL1() {
        return new DualTVL1OpticalFlow(createOptFlow_DualTVL1_0());
    }

    public static RotatedRect CamShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        RotatedRect retVal = new RotatedRect(CamShift_0(probImage.nativeObj, window.f1130x, window.f1131y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon));
        if (window != null) {
            window.f1130x = (int) window_out[0];
            window.f1131y = (int) window_out[1];
            window.width = (int) window_out[2];
            window.height = (int) window_out[3];
        }
        return retVal;
    }

    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType, TermCriteria criteria, Mat inputMask) {
        return findTransformECC_0(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType, criteria.type, criteria.maxCount, criteria.epsilon, inputMask.nativeObj);
    }

    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType) {
        return findTransformECC_1(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType);
    }

    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix) {
        return findTransformECC_2(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj);
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder, boolean tryReuseInputImage) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_0(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder, tryReuseInputImage);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel) {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_1(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    public static int meanShift(Mat probImage, Rect window, TermCriteria criteria) {
        double[] window_out = new double[4];
        int retVal = meanShift_0(probImage.nativeObj, window.f1130x, window.f1131y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon);
        if (window != null) {
            window.f1130x = (int) window_out[0];
            window.f1131y = (int) window_out[1];
            window.width = (int) window_out[2];
            window.height = (int) window_out[3];
        }
        return retVal;
    }

    public static void calcOpticalFlowFarneback(Mat prev, Mat next, Mat flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags) {
        calcOpticalFlowFarneback_0(prev.nativeObj, next.nativeObj, flow.nativeObj, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags, double minEigThreshold) {
        calcOpticalFlowPyrLK_0(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon, flags, minEigThreshold);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel) {
        calcOpticalFlowPyrLK_1(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj, winSize.width, winSize.height, maxLevel);
    }

    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err) {
        calcOpticalFlowPyrLK_2(prevImg.nativeObj, nextImg.nativeObj, prevPts.nativeObj, nextPts.nativeObj, status.nativeObj, err.nativeObj);
    }
}
