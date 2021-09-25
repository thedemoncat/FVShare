package org.opencv.aruco;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;

public class Aruco {
    public static final int DICT_4X4_100 = 1;
    public static final int DICT_4X4_1000 = 3;
    public static final int DICT_4X4_250 = 2;
    public static final int DICT_4X4_50 = 0;
    public static final int DICT_5X5_100 = 5;
    public static final int DICT_5X5_1000 = 7;
    public static final int DICT_5X5_250 = 6;
    public static final int DICT_5X5_50 = 4;
    public static final int DICT_6X6_100 = 9;
    public static final int DICT_6X6_1000 = 11;
    public static final int DICT_6X6_250 = 10;
    public static final int DICT_6X6_50 = 8;
    public static final int DICT_7X7_100 = 13;
    public static final int DICT_7X7_1000 = 15;
    public static final int DICT_7X7_250 = 14;
    public static final int DICT_7X7_50 = 12;
    public static final int DICT_ARUCO_ORIGINAL = 16;

    private static native double calibrateCameraArucoExtended_0(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6, long j7, long j8, long j9, long j10, long j11, int i, int i2, int i3, double d3);

    private static native double calibrateCameraArucoExtended_1(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6, long j7, long j8, long j9, long j10, long j11, int i);

    private static native double calibrateCameraArucoExtended_2(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6, long j7, long j8, long j9, long j10, long j11);

    private static native double calibrateCameraAruco_0(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6, long j7, long j8, int i, int i2, int i3, double d3);

    private static native double calibrateCameraAruco_1(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6, long j7, long j8, int i);

    private static native double calibrateCameraAruco_2(long j, long j2, long j3, long j4, double d, double d2, long j5, long j6);

    private static native double calibrateCameraCharucoExtended_0(long j, long j2, long j3, double d, double d2, long j4, long j5, long j6, long j7, long j8, long j9, long j10, int i, int i2, int i3, double d3);

    private static native double calibrateCameraCharucoExtended_1(long j, long j2, long j3, double d, double d2, long j4, long j5, long j6, long j7, long j8, long j9, long j10, int i);

    private static native double calibrateCameraCharucoExtended_2(long j, long j2, long j3, double d, double d2, long j4, long j5, long j6, long j7, long j8, long j9, long j10);

    private static native double calibrateCameraCharuco_0(long j, long j2, long j3, double d, double d2, long j4, long j5, long j6, long j7, int i, int i2, int i3, double d3);

    private static native double calibrateCameraCharuco_1(long j, long j2, long j3, double d, double d2, long j4, long j5, long j6, long j7, int i);

    private static native double calibrateCameraCharuco_2(long j, long j2, long j3, double d, double d2, long j4, long j5);

    private static native long custom_dictionary_0(int i, int i2);

    private static native long custom_dictionary_from_0(int i, int i2, long j);

    private static native void detectCharucoDiamond_0(long j, long j2, long j3, float f, long j4, long j5, long j6, long j7);

    private static native void detectCharucoDiamond_1(long j, long j2, long j3, float f, long j4, long j5);

    private static native void detectMarkers_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void detectMarkers_1(long j, long j2, long j3, long j4);

    private static native void drawAxis_0(long j, long j2, long j3, long j4, long j5, float f);

    private static native void drawDetectedCornersCharuco_0(long j, long j2, long j3, double d, double d2, double d3, double d4);

    private static native void drawDetectedCornersCharuco_1(long j, long j2);

    private static native void drawDetectedDiamonds_0(long j, long j2, long j3, double d, double d2, double d3, double d4);

    private static native void drawDetectedDiamonds_1(long j, long j2);

    private static native void drawDetectedMarkers_0(long j, long j2, long j3, double d, double d2, double d3, double d4);

    private static native void drawDetectedMarkers_1(long j, long j2);

    private static native void drawMarker_0(long j, int i, int i2, long j2, int i3);

    private static native void drawMarker_1(long j, int i, int i2, long j2);

    private static native void drawPlanarBoard_0(long j, double d, double d2, long j2, int i, int i2);

    private static native void drawPlanarBoard_1(long j, double d, double d2, long j2);

    private static native int estimatePoseBoard_0(long j, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native boolean estimatePoseCharucoBoard_0(long j, long j2, long j3, long j4, long j5, long j6, long j7);

    private static native void estimatePoseSingleMarkers_0(long j, float f, long j2, long j3, long j4, long j5);

    private static native long getPredefinedDictionary_0(int i);

    private static native int interpolateCornersCharuco_0(long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, int i);

    private static native int interpolateCornersCharuco_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void refineDetectedMarkers_0(long j, long j2, long j3, long j4, long j5, long j6, long j7, float f, float f2, boolean z, long j8, long j9);

    private static native void refineDetectedMarkers_1(long j, long j2, long j3, long j4, long j5);

    public static Dictionary custom_dictionary_from(int nMarkers, int markerSize, Dictionary baseDictionary) {
        return new Dictionary(custom_dictionary_from_0(nMarkers, markerSize, baseDictionary.nativeObj));
    }

    public static Dictionary custom_dictionary(int nMarkers, int markerSize) {
        return new Dictionary(custom_dictionary_0(nMarkers, markerSize));
    }

    public static Dictionary getPredefinedDictionary(int dict) {
        return new Dictionary(getPredefinedDictionary_0(dict));
    }

    public static boolean estimatePoseCharucoBoard(Mat charucoCorners, Mat charucoIds, CharucoBoard board, Mat cameraMatrix, Mat distCoeffs, Mat rvec, Mat tvec) {
        return estimatePoseCharucoBoard_0(charucoCorners.nativeObj, charucoIds.nativeObj, board.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvec.nativeObj, tvec.nativeObj);
    }

    public static double calibrateCameraArucoExtended(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors, int flags, TermCriteria criteria) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraArucoExtended_0(corners_mat.nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj, flags, criteria.type, criteria.maxCount, criteria.epsilon);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraArucoExtended(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors, int flags) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraArucoExtended_1(corners_mat.nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj, flags);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraArucoExtended(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraArucoExtended_2(corners_mat.nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraAruco(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, int flags, TermCriteria criteria) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraAruco_0(corners_mat.nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, flags, criteria.type, criteria.maxCount, criteria.epsilon);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraAruco(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, int flags) {
        Mat corners_mat = Converters.vector_Mat_to_Mat(corners);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraAruco_1(corners_mat.nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, flags);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraAruco(List<Mat> corners, Mat ids, Mat counter, Board board, Size imageSize, Mat cameraMatrix, Mat distCoeffs) {
        return calibrateCameraAruco_2(Converters.vector_Mat_to_Mat(corners).nativeObj, ids.nativeObj, counter.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj);
    }

    public static double calibrateCameraCharucoExtended(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors, int flags, TermCriteria criteria) {
        Mat charucoCorners_mat = Converters.vector_Mat_to_Mat(charucoCorners);
        Mat charucoIds_mat = Converters.vector_Mat_to_Mat(charucoIds);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraCharucoExtended_0(charucoCorners_mat.nativeObj, charucoIds_mat.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj, flags, criteria.type, criteria.maxCount, criteria.epsilon);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraCharucoExtended(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors, int flags) {
        Mat charucoCorners_mat = Converters.vector_Mat_to_Mat(charucoCorners);
        Mat charucoIds_mat = Converters.vector_Mat_to_Mat(charucoIds);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraCharucoExtended_1(charucoCorners_mat.nativeObj, charucoIds_mat.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj, flags);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraCharucoExtended(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, Mat stdDeviationsIntrinsics, Mat stdDeviationsExtrinsics, Mat perViewErrors) {
        Mat charucoCorners_mat = Converters.vector_Mat_to_Mat(charucoCorners);
        Mat charucoIds_mat = Converters.vector_Mat_to_Mat(charucoIds);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraCharucoExtended_2(charucoCorners_mat.nativeObj, charucoIds_mat.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, stdDeviationsIntrinsics.nativeObj, stdDeviationsExtrinsics.nativeObj, perViewErrors.nativeObj);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraCharuco(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, int flags, TermCriteria criteria) {
        Mat charucoCorners_mat = Converters.vector_Mat_to_Mat(charucoCorners);
        Mat charucoIds_mat = Converters.vector_Mat_to_Mat(charucoIds);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraCharuco_0(charucoCorners_mat.nativeObj, charucoIds_mat.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, flags, criteria.type, criteria.maxCount, criteria.epsilon);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraCharuco(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs, List<Mat> rvecs, List<Mat> tvecs, int flags) {
        Mat charucoCorners_mat = Converters.vector_Mat_to_Mat(charucoCorners);
        Mat charucoIds_mat = Converters.vector_Mat_to_Mat(charucoIds);
        Mat rvecs_mat = new Mat();
        Mat tvecs_mat = new Mat();
        double retVal = calibrateCameraCharuco_1(charucoCorners_mat.nativeObj, charucoIds_mat.nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs_mat.nativeObj, tvecs_mat.nativeObj, flags);
        Converters.Mat_to_vector_Mat(rvecs_mat, rvecs);
        rvecs_mat.release();
        Converters.Mat_to_vector_Mat(tvecs_mat, tvecs);
        tvecs_mat.release();
        return retVal;
    }

    public static double calibrateCameraCharuco(List<Mat> charucoCorners, List<Mat> charucoIds, CharucoBoard board, Size imageSize, Mat cameraMatrix, Mat distCoeffs) {
        return calibrateCameraCharuco_2(Converters.vector_Mat_to_Mat(charucoCorners).nativeObj, Converters.vector_Mat_to_Mat(charucoIds).nativeObj, board.nativeObj, imageSize.width, imageSize.height, cameraMatrix.nativeObj, distCoeffs.nativeObj);
    }

    public static int estimatePoseBoard(List<Mat> corners, Mat ids, Board board, Mat cameraMatrix, Mat distCoeffs, Mat rvec, Mat tvec) {
        return estimatePoseBoard_0(Converters.vector_Mat_to_Mat(corners).nativeObj, ids.nativeObj, board.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvec.nativeObj, tvec.nativeObj);
    }

    public static int interpolateCornersCharuco(List<Mat> markerCorners, Mat markerIds, Mat image, CharucoBoard board, Mat charucoCorners, Mat charucoIds, Mat cameraMatrix, Mat distCoeffs, int minMarkers) {
        return interpolateCornersCharuco_0(Converters.vector_Mat_to_Mat(markerCorners).nativeObj, markerIds.nativeObj, image.nativeObj, board.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, minMarkers);
    }

    public static int interpolateCornersCharuco(List<Mat> markerCorners, Mat markerIds, Mat image, CharucoBoard board, Mat charucoCorners, Mat charucoIds) {
        return interpolateCornersCharuco_1(Converters.vector_Mat_to_Mat(markerCorners).nativeObj, markerIds.nativeObj, image.nativeObj, board.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj);
    }

    public static void detectCharucoDiamond(Mat image, List<Mat> markerCorners, Mat markerIds, float squareMarkerLengthRate, List<Mat> diamondCorners, Mat diamondIds, Mat cameraMatrix, Mat distCoeffs) {
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        Mat diamondCorners_mat = new Mat();
        detectCharucoDiamond_0(image.nativeObj, markerCorners_mat.nativeObj, markerIds.nativeObj, squareMarkerLengthRate, diamondCorners_mat.nativeObj, diamondIds.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj);
        Converters.Mat_to_vector_Mat(diamondCorners_mat, diamondCorners);
        diamondCorners_mat.release();
    }

    public static void detectCharucoDiamond(Mat image, List<Mat> markerCorners, Mat markerIds, float squareMarkerLengthRate, List<Mat> diamondCorners, Mat diamondIds) {
        Mat markerCorners_mat = Converters.vector_Mat_to_Mat(markerCorners);
        Mat diamondCorners_mat = new Mat();
        detectCharucoDiamond_1(image.nativeObj, markerCorners_mat.nativeObj, markerIds.nativeObj, squareMarkerLengthRate, diamondCorners_mat.nativeObj, diamondIds.nativeObj);
        Converters.Mat_to_vector_Mat(diamondCorners_mat, diamondCorners);
        diamondCorners_mat.release();
    }

    public static void detectMarkers(Mat image, Dictionary dictionary, List<Mat> corners, Mat ids, DetectorParameters parameters, List<Mat> rejectedImgPoints) {
        Mat corners_mat = new Mat();
        Mat rejectedImgPoints_mat = new Mat();
        detectMarkers_0(image.nativeObj, dictionary.nativeObj, corners_mat.nativeObj, ids.nativeObj, parameters.nativeObj, rejectedImgPoints_mat.nativeObj);
        Converters.Mat_to_vector_Mat(corners_mat, corners);
        corners_mat.release();
        Converters.Mat_to_vector_Mat(rejectedImgPoints_mat, rejectedImgPoints);
        rejectedImgPoints_mat.release();
    }

    public static void detectMarkers(Mat image, Dictionary dictionary, List<Mat> corners, Mat ids) {
        Mat corners_mat = new Mat();
        detectMarkers_1(image.nativeObj, dictionary.nativeObj, corners_mat.nativeObj, ids.nativeObj);
        Converters.Mat_to_vector_Mat(corners_mat, corners);
        corners_mat.release();
    }

    public static void drawAxis(Mat image, Mat cameraMatrix, Mat distCoeffs, Mat rvec, Mat tvec, float length) {
        drawAxis_0(image.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvec.nativeObj, tvec.nativeObj, length);
    }

    public static void drawDetectedCornersCharuco(Mat image, Mat charucoCorners, Mat charucoIds, Scalar cornerColor) {
        drawDetectedCornersCharuco_0(image.nativeObj, charucoCorners.nativeObj, charucoIds.nativeObj, cornerColor.val[0], cornerColor.val[1], cornerColor.val[2], cornerColor.val[3]);
    }

    public static void drawDetectedCornersCharuco(Mat image, Mat charucoCorners) {
        drawDetectedCornersCharuco_1(image.nativeObj, charucoCorners.nativeObj);
    }

    public static void drawDetectedDiamonds(Mat image, List<Mat> diamondCorners, Mat diamondIds, Scalar borderColor) {
        drawDetectedDiamonds_0(image.nativeObj, Converters.vector_Mat_to_Mat(diamondCorners).nativeObj, diamondIds.nativeObj, borderColor.val[0], borderColor.val[1], borderColor.val[2], borderColor.val[3]);
    }

    public static void drawDetectedDiamonds(Mat image, List<Mat> diamondCorners) {
        drawDetectedDiamonds_1(image.nativeObj, Converters.vector_Mat_to_Mat(diamondCorners).nativeObj);
    }

    public static void drawDetectedMarkers(Mat image, List<Mat> corners, Mat ids, Scalar borderColor) {
        drawDetectedMarkers_0(image.nativeObj, Converters.vector_Mat_to_Mat(corners).nativeObj, ids.nativeObj, borderColor.val[0], borderColor.val[1], borderColor.val[2], borderColor.val[3]);
    }

    public static void drawDetectedMarkers(Mat image, List<Mat> corners) {
        drawDetectedMarkers_1(image.nativeObj, Converters.vector_Mat_to_Mat(corners).nativeObj);
    }

    public static void drawMarker(Dictionary dictionary, int id, int sidePixels, Mat img, int borderBits) {
        drawMarker_0(dictionary.nativeObj, id, sidePixels, img.nativeObj, borderBits);
    }

    public static void drawMarker(Dictionary dictionary, int id, int sidePixels, Mat img) {
        drawMarker_1(dictionary.nativeObj, id, sidePixels, img.nativeObj);
    }

    public static void drawPlanarBoard(Board board, Size outSize, Mat img, int marginSize, int borderBits) {
        drawPlanarBoard_0(board.nativeObj, outSize.width, outSize.height, img.nativeObj, marginSize, borderBits);
    }

    public static void drawPlanarBoard(Board board, Size outSize, Mat img) {
        drawPlanarBoard_1(board.nativeObj, outSize.width, outSize.height, img.nativeObj);
    }

    public static void estimatePoseSingleMarkers(List<Mat> corners, float markerLength, Mat cameraMatrix, Mat distCoeffs, Mat rvecs, Mat tvecs) {
        estimatePoseSingleMarkers_0(Converters.vector_Mat_to_Mat(corners).nativeObj, markerLength, cameraMatrix.nativeObj, distCoeffs.nativeObj, rvecs.nativeObj, tvecs.nativeObj);
    }

    public static void refineDetectedMarkers(Mat image, Board board, List<Mat> detectedCorners, Mat detectedIds, List<Mat> rejectedCorners, Mat cameraMatrix, Mat distCoeffs, float minRepDistance, float errorCorrectionRate, boolean checkAllOrders, Mat recoveredIdxs, DetectorParameters parameters) {
        Mat detectedCorners_mat = Converters.vector_Mat_to_Mat(detectedCorners);
        Mat rejectedCorners_mat = Converters.vector_Mat_to_Mat(rejectedCorners);
        refineDetectedMarkers_0(image.nativeObj, board.nativeObj, detectedCorners_mat.nativeObj, detectedIds.nativeObj, rejectedCorners_mat.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, minRepDistance, errorCorrectionRate, checkAllOrders, recoveredIdxs.nativeObj, parameters.nativeObj);
        Converters.Mat_to_vector_Mat(detectedCorners_mat, detectedCorners);
        detectedCorners_mat.release();
        Converters.Mat_to_vector_Mat(rejectedCorners_mat, rejectedCorners);
        rejectedCorners_mat.release();
    }

    public static void refineDetectedMarkers(Mat image, Board board, List<Mat> detectedCorners, Mat detectedIds, List<Mat> rejectedCorners) {
        Mat detectedCorners_mat = Converters.vector_Mat_to_Mat(detectedCorners);
        Mat rejectedCorners_mat = Converters.vector_Mat_to_Mat(rejectedCorners);
        refineDetectedMarkers_1(image.nativeObj, board.nativeObj, detectedCorners_mat.nativeObj, detectedIds.nativeObj, rejectedCorners_mat.nativeObj);
        Converters.Mat_to_vector_Mat(detectedCorners_mat, detectedCorners);
        detectedCorners_mat.release();
        Converters.Mat_to_vector_Mat(rejectedCorners_mat, rejectedCorners);
        rejectedCorners_mat.release();
    }
}
