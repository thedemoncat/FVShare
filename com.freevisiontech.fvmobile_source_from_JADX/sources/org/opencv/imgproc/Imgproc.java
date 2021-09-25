package org.opencv.imgproc;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;

public class Imgproc {
    public static final int ADAPTIVE_THRESH_GAUSSIAN_C = 1;
    public static final int ADAPTIVE_THRESH_MEAN_C = 0;
    public static final int CCL_DEFAULT = -1;
    public static final int CCL_GRANA = 1;
    public static final int CCL_WU = 0;
    public static final int CC_STAT_AREA = 4;
    public static final int CC_STAT_HEIGHT = 3;
    public static final int CC_STAT_LEFT = 0;
    public static final int CC_STAT_MAX = 5;
    public static final int CC_STAT_TOP = 1;
    public static final int CC_STAT_WIDTH = 2;
    public static final int CHAIN_APPROX_NONE = 1;
    public static final int CHAIN_APPROX_SIMPLE = 2;
    public static final int CHAIN_APPROX_TC89_KCOS = 4;
    public static final int CHAIN_APPROX_TC89_L1 = 3;
    public static final int COLORMAP_AUTUMN = 0;
    public static final int COLORMAP_BONE = 1;
    public static final int COLORMAP_COOL = 8;
    public static final int COLORMAP_HOT = 11;
    public static final int COLORMAP_HSV = 9;
    public static final int COLORMAP_JET = 2;
    public static final int COLORMAP_OCEAN = 5;
    public static final int COLORMAP_PARULA = 12;
    public static final int COLORMAP_PINK = 10;
    public static final int COLORMAP_RAINBOW = 4;
    public static final int COLORMAP_SPRING = 7;
    public static final int COLORMAP_SUMMER = 6;
    public static final int COLORMAP_WINTER = 3;
    public static final int COLOR_BGR2BGR555 = 22;
    public static final int COLOR_BGR2BGR565 = 12;
    public static final int COLOR_BGR2BGRA = 0;
    public static final int COLOR_BGR2GRAY = 6;
    public static final int COLOR_BGR2HLS = 52;
    public static final int COLOR_BGR2HLS_FULL = 68;
    public static final int COLOR_BGR2HSV = 40;
    public static final int COLOR_BGR2HSV_FULL = 66;
    public static final int COLOR_BGR2Lab = 44;
    public static final int COLOR_BGR2Luv = 50;
    public static final int COLOR_BGR2RGB = 4;
    public static final int COLOR_BGR2RGBA = 2;
    public static final int COLOR_BGR2XYZ = 32;
    public static final int COLOR_BGR2YCrCb = 36;
    public static final int COLOR_BGR2YUV = 82;
    public static final int COLOR_BGR2YUV_I420 = 128;
    public static final int COLOR_BGR2YUV_IYUV = 128;
    public static final int COLOR_BGR2YUV_YV12 = 132;
    public static final int COLOR_BGR5552BGR = 24;
    public static final int COLOR_BGR5552BGRA = 28;
    public static final int COLOR_BGR5552GRAY = 31;
    public static final int COLOR_BGR5552RGB = 25;
    public static final int COLOR_BGR5552RGBA = 29;
    public static final int COLOR_BGR5652BGR = 14;
    public static final int COLOR_BGR5652BGRA = 18;
    public static final int COLOR_BGR5652GRAY = 21;
    public static final int COLOR_BGR5652RGB = 15;
    public static final int COLOR_BGR5652RGBA = 19;
    public static final int COLOR_BGRA2BGR = 1;
    public static final int COLOR_BGRA2BGR555 = 26;
    public static final int COLOR_BGRA2BGR565 = 16;
    public static final int COLOR_BGRA2GRAY = 10;
    public static final int COLOR_BGRA2RGB = 3;
    public static final int COLOR_BGRA2RGBA = 5;
    public static final int COLOR_BGRA2YUV_I420 = 130;
    public static final int COLOR_BGRA2YUV_IYUV = 130;
    public static final int COLOR_BGRA2YUV_YV12 = 134;
    public static final int COLOR_BayerBG2BGR = 46;
    public static final int COLOR_BayerBG2BGR_EA = 135;
    public static final int COLOR_BayerBG2BGR_VNG = 62;
    public static final int COLOR_BayerBG2GRAY = 86;
    public static final int COLOR_BayerBG2RGB = 48;
    public static final int COLOR_BayerBG2RGB_EA = 137;
    public static final int COLOR_BayerBG2RGB_VNG = 64;
    public static final int COLOR_BayerGB2BGR = 47;
    public static final int COLOR_BayerGB2BGR_EA = 136;
    public static final int COLOR_BayerGB2BGR_VNG = 63;
    public static final int COLOR_BayerGB2GRAY = 87;
    public static final int COLOR_BayerGB2RGB = 49;
    public static final int COLOR_BayerGB2RGB_EA = 138;
    public static final int COLOR_BayerGB2RGB_VNG = 65;
    public static final int COLOR_BayerGR2BGR = 49;
    public static final int COLOR_BayerGR2BGR_EA = 138;
    public static final int COLOR_BayerGR2BGR_VNG = 65;
    public static final int COLOR_BayerGR2GRAY = 89;
    public static final int COLOR_BayerGR2RGB = 47;
    public static final int COLOR_BayerGR2RGB_EA = 136;
    public static final int COLOR_BayerGR2RGB_VNG = 63;
    public static final int COLOR_BayerRG2BGR = 48;
    public static final int COLOR_BayerRG2BGR_EA = 137;
    public static final int COLOR_BayerRG2BGR_VNG = 64;
    public static final int COLOR_BayerRG2GRAY = 88;
    public static final int COLOR_BayerRG2RGB = 46;
    public static final int COLOR_BayerRG2RGB_EA = 135;
    public static final int COLOR_BayerRG2RGB_VNG = 62;
    public static final int COLOR_COLORCVT_MAX = 139;
    public static final int COLOR_GRAY2BGR = 8;
    public static final int COLOR_GRAY2BGR555 = 30;
    public static final int COLOR_GRAY2BGR565 = 20;
    public static final int COLOR_GRAY2BGRA = 9;
    public static final int COLOR_GRAY2RGB = 8;
    public static final int COLOR_GRAY2RGBA = 9;
    public static final int COLOR_HLS2BGR = 60;
    public static final int COLOR_HLS2BGR_FULL = 72;
    public static final int COLOR_HLS2RGB = 61;
    public static final int COLOR_HLS2RGB_FULL = 73;
    public static final int COLOR_HSV2BGR = 54;
    public static final int COLOR_HSV2BGR_FULL = 70;
    public static final int COLOR_HSV2RGB = 55;
    public static final int COLOR_HSV2RGB_FULL = 71;
    public static final int COLOR_LBGR2Lab = 74;
    public static final int COLOR_LBGR2Luv = 76;
    public static final int COLOR_LRGB2Lab = 75;
    public static final int COLOR_LRGB2Luv = 77;
    public static final int COLOR_Lab2BGR = 56;
    public static final int COLOR_Lab2LBGR = 78;
    public static final int COLOR_Lab2LRGB = 79;
    public static final int COLOR_Lab2RGB = 57;
    public static final int COLOR_Luv2BGR = 58;
    public static final int COLOR_Luv2LBGR = 80;
    public static final int COLOR_Luv2LRGB = 81;
    public static final int COLOR_Luv2RGB = 59;
    public static final int COLOR_RGB2BGR = 4;
    public static final int COLOR_RGB2BGR555 = 23;
    public static final int COLOR_RGB2BGR565 = 13;
    public static final int COLOR_RGB2BGRA = 2;
    public static final int COLOR_RGB2GRAY = 7;
    public static final int COLOR_RGB2HLS = 53;
    public static final int COLOR_RGB2HLS_FULL = 69;
    public static final int COLOR_RGB2HSV = 41;
    public static final int COLOR_RGB2HSV_FULL = 67;
    public static final int COLOR_RGB2Lab = 45;
    public static final int COLOR_RGB2Luv = 51;
    public static final int COLOR_RGB2RGBA = 0;
    public static final int COLOR_RGB2XYZ = 33;
    public static final int COLOR_RGB2YCrCb = 37;
    public static final int COLOR_RGB2YUV = 83;
    public static final int COLOR_RGB2YUV_I420 = 127;
    public static final int COLOR_RGB2YUV_IYUV = 127;
    public static final int COLOR_RGB2YUV_YV12 = 131;
    public static final int COLOR_RGBA2BGR = 3;
    public static final int COLOR_RGBA2BGR555 = 27;
    public static final int COLOR_RGBA2BGR565 = 17;
    public static final int COLOR_RGBA2BGRA = 5;
    public static final int COLOR_RGBA2GRAY = 11;
    public static final int COLOR_RGBA2RGB = 1;
    public static final int COLOR_RGBA2YUV_I420 = 129;
    public static final int COLOR_RGBA2YUV_IYUV = 129;
    public static final int COLOR_RGBA2YUV_YV12 = 133;
    public static final int COLOR_RGBA2mRGBA = 125;
    public static final int COLOR_XYZ2BGR = 34;
    public static final int COLOR_XYZ2RGB = 35;
    public static final int COLOR_YCrCb2BGR = 38;
    public static final int COLOR_YCrCb2RGB = 39;
    public static final int COLOR_YUV2BGR = 84;
    public static final int COLOR_YUV2BGRA_I420 = 105;
    public static final int COLOR_YUV2BGRA_IYUV = 105;
    public static final int COLOR_YUV2BGRA_NV12 = 95;
    public static final int COLOR_YUV2BGRA_NV21 = 97;
    public static final int COLOR_YUV2BGRA_UYNV = 112;
    public static final int COLOR_YUV2BGRA_UYVY = 112;
    public static final int COLOR_YUV2BGRA_Y422 = 112;
    public static final int COLOR_YUV2BGRA_YUNV = 120;
    public static final int COLOR_YUV2BGRA_YUY2 = 120;
    public static final int COLOR_YUV2BGRA_YUYV = 120;
    public static final int COLOR_YUV2BGRA_YV12 = 103;
    public static final int COLOR_YUV2BGRA_YVYU = 122;
    public static final int COLOR_YUV2BGR_I420 = 101;
    public static final int COLOR_YUV2BGR_IYUV = 101;
    public static final int COLOR_YUV2BGR_NV12 = 91;
    public static final int COLOR_YUV2BGR_NV21 = 93;
    public static final int COLOR_YUV2BGR_UYNV = 108;
    public static final int COLOR_YUV2BGR_UYVY = 108;
    public static final int COLOR_YUV2BGR_Y422 = 108;
    public static final int COLOR_YUV2BGR_YUNV = 116;
    public static final int COLOR_YUV2BGR_YUY2 = 116;
    public static final int COLOR_YUV2BGR_YUYV = 116;
    public static final int COLOR_YUV2BGR_YV12 = 99;
    public static final int COLOR_YUV2BGR_YVYU = 118;
    public static final int COLOR_YUV2GRAY_420 = 106;
    public static final int COLOR_YUV2GRAY_I420 = 106;
    public static final int COLOR_YUV2GRAY_IYUV = 106;
    public static final int COLOR_YUV2GRAY_NV12 = 106;
    public static final int COLOR_YUV2GRAY_NV21 = 106;
    public static final int COLOR_YUV2GRAY_UYNV = 123;
    public static final int COLOR_YUV2GRAY_UYVY = 123;
    public static final int COLOR_YUV2GRAY_Y422 = 123;
    public static final int COLOR_YUV2GRAY_YUNV = 124;
    public static final int COLOR_YUV2GRAY_YUY2 = 124;
    public static final int COLOR_YUV2GRAY_YUYV = 124;
    public static final int COLOR_YUV2GRAY_YV12 = 106;
    public static final int COLOR_YUV2GRAY_YVYU = 124;
    public static final int COLOR_YUV2RGB = 85;
    public static final int COLOR_YUV2RGBA_I420 = 104;
    public static final int COLOR_YUV2RGBA_IYUV = 104;
    public static final int COLOR_YUV2RGBA_NV12 = 94;
    public static final int COLOR_YUV2RGBA_NV21 = 96;
    public static final int COLOR_YUV2RGBA_UYNV = 111;
    public static final int COLOR_YUV2RGBA_UYVY = 111;
    public static final int COLOR_YUV2RGBA_Y422 = 111;
    public static final int COLOR_YUV2RGBA_YUNV = 119;
    public static final int COLOR_YUV2RGBA_YUY2 = 119;
    public static final int COLOR_YUV2RGBA_YUYV = 119;
    public static final int COLOR_YUV2RGBA_YV12 = 102;
    public static final int COLOR_YUV2RGBA_YVYU = 121;
    public static final int COLOR_YUV2RGB_I420 = 100;
    public static final int COLOR_YUV2RGB_IYUV = 100;
    public static final int COLOR_YUV2RGB_NV12 = 90;
    public static final int COLOR_YUV2RGB_NV21 = 92;
    public static final int COLOR_YUV2RGB_UYNV = 107;
    public static final int COLOR_YUV2RGB_UYVY = 107;
    public static final int COLOR_YUV2RGB_Y422 = 107;
    public static final int COLOR_YUV2RGB_YUNV = 115;
    public static final int COLOR_YUV2RGB_YUY2 = 115;
    public static final int COLOR_YUV2RGB_YUYV = 115;
    public static final int COLOR_YUV2RGB_YV12 = 98;
    public static final int COLOR_YUV2RGB_YVYU = 117;
    public static final int COLOR_YUV420p2BGR = 99;
    public static final int COLOR_YUV420p2BGRA = 103;
    public static final int COLOR_YUV420p2GRAY = 106;
    public static final int COLOR_YUV420p2RGB = 98;
    public static final int COLOR_YUV420p2RGBA = 102;
    public static final int COLOR_YUV420sp2BGR = 93;
    public static final int COLOR_YUV420sp2BGRA = 97;
    public static final int COLOR_YUV420sp2GRAY = 106;
    public static final int COLOR_YUV420sp2RGB = 92;
    public static final int COLOR_YUV420sp2RGBA = 96;
    public static final int COLOR_mRGBA2RGBA = 126;
    public static final int CV_BILATERAL = 4;
    public static final int CV_BLUR = 1;
    public static final int CV_BLUR_NO_SCALE = 0;
    public static final int CV_CANNY_L2_GRADIENT = Integer.MIN_VALUE;
    private static final int CV_CHAIN_APPROX_NONE = 1;
    private static final int CV_CHAIN_APPROX_SIMPLE = 2;
    private static final int CV_CHAIN_APPROX_TC89_KCOS = 4;
    private static final int CV_CHAIN_APPROX_TC89_L1 = 3;
    public static final int CV_CHAIN_CODE = 0;
    public static final int CV_CLOCKWISE = 1;
    public static final int CV_COMP_BHATTACHARYYA = 3;
    public static final int CV_COMP_CHISQR = 1;
    public static final int CV_COMP_CHISQR_ALT = 4;
    public static final int CV_COMP_CORREL = 0;
    public static final int CV_COMP_HELLINGER = 3;
    public static final int CV_COMP_INTERSECT = 2;
    public static final int CV_COMP_KL_DIV = 5;
    public static final int CV_CONTOURS_MATCH_I1 = 1;
    public static final int CV_CONTOURS_MATCH_I2 = 2;
    public static final int CV_CONTOURS_MATCH_I3 = 3;
    public static final int CV_COUNTER_CLOCKWISE = 2;
    public static final int CV_DIST_C = 3;
    public static final int CV_DIST_FAIR = 5;
    public static final int CV_DIST_HUBER = 7;
    public static final int CV_DIST_L1 = 1;
    public static final int CV_DIST_L12 = 4;
    public static final int CV_DIST_L2 = 2;
    public static final int CV_DIST_LABEL_CCOMP = 0;
    public static final int CV_DIST_LABEL_PIXEL = 1;
    public static final int CV_DIST_MASK_3 = 3;
    public static final int CV_DIST_MASK_5 = 5;
    public static final int CV_DIST_MASK_PRECISE = 0;
    public static final int CV_DIST_USER = -1;
    public static final int CV_DIST_WELSCH = 6;
    public static final int CV_GAUSSIAN = 2;
    public static final int CV_GAUSSIAN_5x5 = 7;
    public static final int CV_HOUGH_GRADIENT = 3;
    public static final int CV_HOUGH_MULTI_SCALE = 2;
    public static final int CV_HOUGH_PROBABILISTIC = 1;
    public static final int CV_HOUGH_STANDARD = 0;
    private static final int CV_INTER_AREA = 3;
    private static final int CV_INTER_CUBIC = 2;
    private static final int CV_INTER_LANCZOS4 = 4;
    private static final int CV_INTER_LINEAR = 1;
    private static final int CV_INTER_NN = 0;
    public static final int CV_LINK_RUNS = 5;
    public static final int CV_MAX_SOBEL_KSIZE = 7;
    public static final int CV_MEDIAN = 3;
    private static final int CV_MOP_BLACKHAT = 6;
    private static final int CV_MOP_CLOSE = 3;
    private static final int CV_MOP_DILATE = 1;
    private static final int CV_MOP_ERODE = 0;
    private static final int CV_MOP_GRADIENT = 4;
    private static final int CV_MOP_OPEN = 2;
    private static final int CV_MOP_TOPHAT = 5;
    public static final int CV_POLY_APPROX_DP = 0;
    private static final int CV_RETR_CCOMP = 2;
    private static final int CV_RETR_EXTERNAL = 0;
    private static final int CV_RETR_FLOODFILL = 4;
    private static final int CV_RETR_LIST = 1;
    private static final int CV_RETR_TREE = 3;
    public static final int CV_RGBA2mRGBA = 125;
    public static final int CV_SCHARR = -1;
    public static final int CV_SHAPE_CROSS = 1;
    public static final int CV_SHAPE_CUSTOM = 100;
    public static final int CV_SHAPE_ELLIPSE = 2;
    public static final int CV_SHAPE_RECT = 0;
    private static final int CV_THRESH_BINARY = 0;
    private static final int CV_THRESH_BINARY_INV = 1;
    private static final int CV_THRESH_MASK = 7;
    private static final int CV_THRESH_OTSU = 8;
    private static final int CV_THRESH_TOZERO = 3;
    private static final int CV_THRESH_TOZERO_INV = 4;
    private static final int CV_THRESH_TRIANGLE = 16;
    private static final int CV_THRESH_TRUNC = 2;
    public static final int CV_WARP_FILL_OUTLIERS = 8;
    public static final int CV_WARP_INVERSE_MAP = 16;
    public static final int CV_mRGBA2RGBA = 126;
    public static final int DIST_C = 3;
    public static final int DIST_FAIR = 5;
    public static final int DIST_HUBER = 7;
    public static final int DIST_L1 = 1;
    public static final int DIST_L12 = 4;
    public static final int DIST_L2 = 2;
    public static final int DIST_LABEL_CCOMP = 0;
    public static final int DIST_LABEL_PIXEL = 1;
    public static final int DIST_MASK_3 = 3;
    public static final int DIST_MASK_5 = 5;
    public static final int DIST_MASK_PRECISE = 0;
    public static final int DIST_USER = -1;
    public static final int DIST_WELSCH = 6;
    public static final int FLOODFILL_FIXED_RANGE = 65536;
    public static final int FLOODFILL_MASK_ONLY = 131072;
    public static final int GC_BGD = 0;
    public static final int GC_EVAL = 2;
    public static final int GC_FGD = 1;
    public static final int GC_INIT_WITH_MASK = 1;
    public static final int GC_INIT_WITH_RECT = 0;
    public static final int GC_PR_BGD = 2;
    public static final int GC_PR_FGD = 3;
    public static final int HISTCMP_BHATTACHARYYA = 3;
    public static final int HISTCMP_CHISQR = 1;
    public static final int HISTCMP_CHISQR_ALT = 4;
    public static final int HISTCMP_CORREL = 0;
    public static final int HISTCMP_HELLINGER = 3;
    public static final int HISTCMP_INTERSECT = 2;
    public static final int HISTCMP_KL_DIV = 5;
    public static final int HOUGH_GRADIENT = 3;
    public static final int HOUGH_MULTI_SCALE = 2;
    public static final int HOUGH_PROBABILISTIC = 1;
    public static final int HOUGH_STANDARD = 0;
    public static final int INTERSECT_FULL = 2;
    public static final int INTERSECT_NONE = 0;
    public static final int INTERSECT_PARTIAL = 1;
    public static final int INTER_AREA = 3;
    public static final int INTER_BITS = 5;
    public static final int INTER_BITS2 = 10;
    public static final int INTER_CUBIC = 2;
    public static final int INTER_LANCZOS4 = 4;
    public static final int INTER_LINEAR = 1;
    public static final int INTER_MAX = 7;
    public static final int INTER_NEAREST = 0;
    public static final int INTER_TAB_SIZE = 32;
    public static final int INTER_TAB_SIZE2 = 1024;
    private static final int IPL_BORDER_CONSTANT = 0;
    private static final int IPL_BORDER_REFLECT = 2;
    private static final int IPL_BORDER_REFLECT_101 = 4;
    private static final int IPL_BORDER_REPLICATE = 1;
    private static final int IPL_BORDER_TRANSPARENT = 5;
    private static final int IPL_BORDER_WRAP = 3;
    public static final int LINE_4 = 4;
    public static final int LINE_8 = 8;
    public static final int LINE_AA = 16;
    public static final int LSD_REFINE_ADV = 2;
    public static final int LSD_REFINE_NONE = 0;
    public static final int LSD_REFINE_STD = 1;
    public static final int MARKER_CROSS = 0;
    public static final int MARKER_DIAMOND = 3;
    public static final int MARKER_SQUARE = 4;
    public static final int MARKER_STAR = 2;
    public static final int MARKER_TILTED_CROSS = 1;
    public static final int MARKER_TRIANGLE_DOWN = 6;
    public static final int MARKER_TRIANGLE_UP = 5;
    public static final int MORPH_BLACKHAT = 6;
    public static final int MORPH_CLOSE = 3;
    public static final int MORPH_CROSS = 1;
    public static final int MORPH_DILATE = 1;
    public static final int MORPH_ELLIPSE = 2;
    public static final int MORPH_ERODE = 0;
    public static final int MORPH_GRADIENT = 4;
    public static final int MORPH_HITMISS = 7;
    public static final int MORPH_OPEN = 2;
    public static final int MORPH_RECT = 0;
    public static final int MORPH_TOPHAT = 5;
    public static final int PROJ_SPHERICAL_EQRECT = 1;
    public static final int PROJ_SPHERICAL_ORTHO = 0;
    public static final int RETR_CCOMP = 2;
    public static final int RETR_EXTERNAL = 0;
    public static final int RETR_FLOODFILL = 4;
    public static final int RETR_LIST = 1;
    public static final int RETR_TREE = 3;
    public static final int THRESH_BINARY = 0;
    public static final int THRESH_BINARY_INV = 1;
    public static final int THRESH_MASK = 7;
    public static final int THRESH_OTSU = 8;
    public static final int THRESH_TOZERO = 3;
    public static final int THRESH_TOZERO_INV = 4;
    public static final int THRESH_TRIANGLE = 16;
    public static final int THRESH_TRUNC = 2;
    public static final int TM_CCOEFF = 4;
    public static final int TM_CCOEFF_NORMED = 5;
    public static final int TM_CCORR = 2;
    public static final int TM_CCORR_NORMED = 3;
    public static final int TM_SQDIFF = 0;
    public static final int TM_SQDIFF_NORMED = 1;
    public static final int WARP_FILL_OUTLIERS = 8;
    public static final int WARP_INVERSE_MAP = 16;

    private static native void Canny_0(long j, long j2, long j3, double d, double d2, boolean z);

    private static native void Canny_1(long j, long j2, long j3, double d, double d2);

    private static native void Canny_2(long j, long j2, double d, double d2, int i, boolean z);

    private static native void Canny_3(long j, long j2, double d, double d2);

    private static native void GaussianBlur_0(long j, long j2, double d, double d2, double d3, double d4, int i);

    private static native void GaussianBlur_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void GaussianBlur_2(long j, long j2, double d, double d2, double d3);

    private static native void HoughCircles_0(long j, long j2, int i, double d, double d2, double d3, double d4, int i2, int i3);

    private static native void HoughCircles_1(long j, long j2, int i, double d, double d2);

    private static native void HoughLinesP_0(long j, long j2, double d, double d2, int i, double d3, double d4);

    private static native void HoughLinesP_1(long j, long j2, double d, double d2, int i);

    private static native void HoughLines_0(long j, long j2, double d, double d2, int i, double d3, double d4, double d5, double d6);

    private static native void HoughLines_1(long j, long j2, double d, double d2, int i);

    private static native void HuMoments_0(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, long j);

    private static native void Laplacian_0(long j, long j2, int i, int i2, double d, double d2, int i3);

    private static native void Laplacian_1(long j, long j2, int i, int i2, double d, double d2);

    private static native void Laplacian_2(long j, long j2, int i);

    private static native void Scharr_0(long j, long j2, int i, int i2, int i3, double d, double d2, int i4);

    private static native void Scharr_1(long j, long j2, int i, int i2, int i3, double d, double d2);

    private static native void Scharr_2(long j, long j2, int i, int i2, int i3);

    private static native void Sobel_0(long j, long j2, int i, int i2, int i3, int i4, double d, double d2, int i5);

    private static native void Sobel_1(long j, long j2, int i, int i2, int i3, int i4, double d, double d2);

    private static native void Sobel_2(long j, long j2, int i, int i2, int i3);

    private static native void accumulateProduct_0(long j, long j2, long j3, long j4);

    private static native void accumulateProduct_1(long j, long j2, long j3);

    private static native void accumulateSquare_0(long j, long j2, long j3);

    private static native void accumulateSquare_1(long j, long j2);

    private static native void accumulateWeighted_0(long j, long j2, double d, long j3);

    private static native void accumulateWeighted_1(long j, long j2, double d);

    private static native void accumulate_0(long j, long j2, long j3);

    private static native void accumulate_1(long j, long j2);

    private static native void adaptiveThreshold_0(long j, long j2, double d, int i, int i2, int i3, double d2);

    private static native void applyColorMap_0(long j, long j2, int i);

    private static native void approxPolyDP_0(long j, long j2, double d, boolean z);

    private static native double arcLength_0(long j, boolean z);

    private static native void arrowedLine_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, int i3, double d9);

    private static native void arrowedLine_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    private static native void bilateralFilter_0(long j, long j2, int i, double d, double d2, int i2);

    private static native void bilateralFilter_1(long j, long j2, int i, double d, double d2);

    private static native void blur_0(long j, long j2, double d, double d2, double d3, double d4, int i);

    private static native void blur_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void blur_2(long j, long j2, double d, double d2);

    private static native double[] boundingRect_0(long j);

    private static native void boxFilter_0(long j, long j2, int i, double d, double d2, double d3, double d4, boolean z, int i2);

    private static native void boxFilter_1(long j, long j2, int i, double d, double d2, double d3, double d4, boolean z);

    private static native void boxFilter_2(long j, long j2, int i, double d, double d2);

    private static native void boxPoints_0(double d, double d2, double d3, double d4, double d5, long j);

    private static native void calcBackProject_0(long j, long j2, long j3, long j4, long j5, double d);

    private static native void calcHist_0(long j, long j2, long j3, long j4, long j5, long j6, boolean z);

    private static native void calcHist_1(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void circle_0(long j, double d, double d2, int i, double d3, double d4, double d5, double d6, int i2, int i3, int i4);

    private static native void circle_1(long j, double d, double d2, int i, double d3, double d4, double d5, double d6, int i2);

    private static native void circle_2(long j, double d, double d2, int i, double d3, double d4, double d5, double d6);

    private static native boolean clipLine_0(int i, int i2, int i3, int i4, double d, double d2, double[] dArr, double d3, double d4, double[] dArr2);

    private static native double compareHist_0(long j, long j2, int i);

    private static native int connectedComponentsWithAlgorithm_0(long j, long j2, int i, int i2, int i3);

    private static native int connectedComponentsWithStatsWithAlgorithm_0(long j, long j2, long j3, long j4, int i, int i2, int i3);

    private static native int connectedComponentsWithStats_0(long j, long j2, long j3, long j4, int i, int i2);

    private static native int connectedComponentsWithStats_1(long j, long j2, long j3, long j4);

    private static native int connectedComponents_0(long j, long j2, int i, int i2);

    private static native int connectedComponents_1(long j, long j2);

    private static native double contourArea_0(long j, boolean z);

    private static native double contourArea_1(long j);

    private static native void convertMaps_0(long j, long j2, long j3, long j4, int i, boolean z);

    private static native void convertMaps_1(long j, long j2, long j3, long j4, int i);

    private static native void convexHull_0(long j, long j2, boolean z);

    private static native void convexHull_1(long j, long j2);

    private static native void convexityDefects_0(long j, long j2, long j3);

    private static native void cornerEigenValsAndVecs_0(long j, long j2, int i, int i2, int i3);

    private static native void cornerEigenValsAndVecs_1(long j, long j2, int i, int i2);

    private static native void cornerHarris_0(long j, long j2, int i, int i2, double d, int i3);

    private static native void cornerHarris_1(long j, long j2, int i, int i2, double d);

    private static native void cornerMinEigenVal_0(long j, long j2, int i, int i2, int i3);

    private static native void cornerMinEigenVal_1(long j, long j2, int i, int i2);

    private static native void cornerMinEigenVal_2(long j, long j2, int i);

    private static native void cornerSubPix_0(long j, long j2, double d, double d2, double d3, double d4, int i, int i2, double d5);

    private static native long createCLAHE_0(double d, double d2, double d3);

    private static native long createCLAHE_1();

    private static native void createHanningWindow_0(long j, double d, double d2, int i);

    private static native long createLineSegmentDetector_0(int i, double d, double d2, double d3, double d4, double d5, double d6, int i2);

    private static native long createLineSegmentDetector_1();

    private static native void cvtColor_0(long j, long j2, int i, int i2);

    private static native void cvtColor_1(long j, long j2, int i);

    private static native void demosaicing_0(long j, long j2, int i, int i2);

    private static native void demosaicing_1(long j, long j2, int i);

    private static native void dilate_0(long j, long j2, long j3, double d, double d2, int i, int i2, double d3, double d4, double d5, double d6);

    private static native void dilate_1(long j, long j2, long j3, double d, double d2, int i);

    private static native void dilate_2(long j, long j2, long j3);

    private static native void distanceTransformWithLabels_0(long j, long j2, long j3, int i, int i2, int i3);

    private static native void distanceTransformWithLabels_1(long j, long j2, long j3, int i, int i2);

    private static native void distanceTransform_0(long j, long j2, int i, int i2, int i3);

    private static native void distanceTransform_1(long j, long j2, int i, int i2);

    private static native void drawContours_0(long j, long j2, int i, double d, double d2, double d3, double d4, int i2, int i3, long j3, int i4, double d5, double d6);

    private static native void drawContours_1(long j, long j2, int i, double d, double d2, double d3, double d4, int i2);

    private static native void drawContours_2(long j, long j2, int i, double d, double d2, double d3, double d4);

    private static native void drawMarker_0(long j, double d, double d2, double d3, double d4, double d5, double d6, int i, int i2, int i3, int i4);

    private static native void drawMarker_1(long j, double d, double d2, double d3, double d4, double d5, double d6);

    private static native void ellipse2Poly_0(double d, double d2, double d3, double d4, int i, int i2, int i3, int i4, long j);

    private static native void ellipse_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i, int i2, int i3);

    private static native void ellipse_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i);

    private static native void ellipse_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11);

    private static native void ellipse_3(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i, int i2);

    private static native void ellipse_4(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i);

    private static native void ellipse_5(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    private static native void equalizeHist_0(long j, long j2);

    private static native void erode_0(long j, long j2, long j3, double d, double d2, int i, int i2, double d3, double d4, double d5, double d6);

    private static native void erode_1(long j, long j2, long j3, double d, double d2, int i);

    private static native void erode_2(long j, long j2, long j3);

    private static native void fillConvexPoly_0(long j, long j2, double d, double d2, double d3, double d4, int i, int i2);

    private static native void fillConvexPoly_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void fillPoly_0(long j, long j2, double d, double d2, double d3, double d4, int i, int i2, double d5, double d6);

    private static native void fillPoly_1(long j, long j2, double d, double d2, double d3, double d4);

    private static native void filter2D_0(long j, long j2, int i, long j3, double d, double d2, double d3, int i2);

    private static native void filter2D_1(long j, long j2, int i, long j3, double d, double d2, double d3);

    private static native void filter2D_2(long j, long j2, int i, long j3);

    private static native void findContours_0(long j, long j2, long j3, int i, int i2, double d, double d2);

    private static native void findContours_1(long j, long j2, long j3, int i, int i2);

    private static native double[] fitEllipse_0(long j);

    private static native void fitLine_0(long j, long j2, int i, double d, double d2, double d3);

    private static native int floodFill_0(long j, long j2, double d, double d2, double d3, double d4, double d5, double d6, double[] dArr, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, int i);

    private static native int floodFill_1(long j, long j2, double d, double d2, double d3, double d4, double d5, double d6);

    private static native long getAffineTransform_0(long j, long j2);

    private static native long getDefaultNewCameraMatrix_0(long j, double d, double d2, boolean z);

    private static native long getDefaultNewCameraMatrix_1(long j);

    private static native void getDerivKernels_0(long j, long j2, int i, int i2, int i3, boolean z, int i4);

    private static native void getDerivKernels_1(long j, long j2, int i, int i2, int i3);

    private static native long getGaborKernel_0(double d, double d2, double d3, double d4, double d5, double d6, double d7, int i);

    private static native long getGaborKernel_1(double d, double d2, double d3, double d4, double d5, double d6);

    private static native long getGaussianKernel_0(int i, double d, int i2);

    private static native long getGaussianKernel_1(int i, double d);

    private static native long getPerspectiveTransform_0(long j, long j2);

    private static native void getRectSubPix_0(long j, double d, double d2, double d3, double d4, long j2, int i);

    private static native void getRectSubPix_1(long j, double d, double d2, double d3, double d4, long j2);

    private static native long getRotationMatrix2D_0(double d, double d2, double d3, double d4);

    private static native long getStructuringElement_0(int i, double d, double d2, double d3, double d4);

    private static native long getStructuringElement_1(int i, double d, double d2);

    private static native void goodFeaturesToTrack_0(long j, long j2, int i, double d, double d2, long j3, int i2, boolean z, double d3);

    private static native void goodFeaturesToTrack_1(long j, long j2, int i, double d, double d2);

    private static native void grabCut_0(long j, long j2, int i, int i2, int i3, int i4, long j3, long j4, int i5, int i6);

    private static native void grabCut_1(long j, long j2, int i, int i2, int i3, int i4, long j3, long j4, int i5);

    private static native void initUndistortRectifyMap_0(long j, long j2, long j3, long j4, double d, double d2, int i, long j5, long j6);

    private static native float initWideAngleProjMap_0(long j, long j2, double d, double d2, int i, int i2, long j3, long j4, int i3, double d3);

    private static native float initWideAngleProjMap_1(long j, long j2, double d, double d2, int i, int i2, long j3, long j4);

    private static native void integral2_0(long j, long j2, long j3, int i, int i2);

    private static native void integral2_1(long j, long j2, long j3);

    private static native void integral3_0(long j, long j2, long j3, long j4, int i, int i2);

    private static native void integral3_1(long j, long j2, long j3, long j4);

    private static native void integral_0(long j, long j2, int i);

    private static native void integral_1(long j, long j2);

    private static native float intersectConvexConvex_0(long j, long j2, long j3, boolean z);

    private static native float intersectConvexConvex_1(long j, long j2, long j3);

    private static native void invertAffineTransform_0(long j, long j2);

    private static native boolean isContourConvex_0(long j);

    private static native void line_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, int i3);

    private static native void line_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i);

    private static native void line_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    private static native void linearPolar_0(long j, long j2, double d, double d2, double d3, int i);

    private static native void logPolar_0(long j, long j2, double d, double d2, double d3, int i);

    private static native double matchShapes_0(long j, long j2, int i, double d);

    private static native void matchTemplate_0(long j, long j2, long j3, int i, long j4);

    private static native void matchTemplate_1(long j, long j2, long j3, int i);

    private static native void medianBlur_0(long j, long j2, int i);

    private static native double[] minAreaRect_0(long j);

    private static native void minEnclosingCircle_0(long j, double[] dArr, double[] dArr2);

    private static native double minEnclosingTriangle_0(long j, long j2);

    private static native double[] moments_0(long j, boolean z);

    private static native double[] moments_1(long j);

    private static native void morphologyEx_0(long j, long j2, int i, long j3, double d, double d2, int i2, int i3, double d3, double d4, double d5, double d6);

    private static native void morphologyEx_1(long j, long j2, int i, long j3, double d, double d2, int i2);

    private static native void morphologyEx_2(long j, long j2, int i, long j3);

    private static native double[] n_getTextSize(String str, int i, double d, int i2, int[] iArr);

    private static native double[] phaseCorrelate_0(long j, long j2, long j3, double[] dArr);

    private static native double[] phaseCorrelate_1(long j, long j2);

    private static native double pointPolygonTest_0(long j, double d, double d2, boolean z);

    private static native void polylines_0(long j, long j2, boolean z, double d, double d2, double d3, double d4, int i, int i2, int i3);

    private static native void polylines_1(long j, long j2, boolean z, double d, double d2, double d3, double d4, int i);

    private static native void polylines_2(long j, long j2, boolean z, double d, double d2, double d3, double d4);

    private static native void preCornerDetect_0(long j, long j2, int i, int i2);

    private static native void preCornerDetect_1(long j, long j2, int i);

    private static native void putText_0(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7, int i2, int i3, boolean z);

    private static native void putText_1(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7, int i2);

    private static native void putText_2(long j, String str, double d, double d2, int i, double d3, double d4, double d5, double d6, double d7);

    private static native void pyrDown_0(long j, long j2, double d, double d2, int i);

    private static native void pyrDown_1(long j, long j2, double d, double d2);

    private static native void pyrDown_2(long j, long j2);

    private static native void pyrMeanShiftFiltering_0(long j, long j2, double d, double d2, int i, int i2, int i3, double d3);

    private static native void pyrMeanShiftFiltering_1(long j, long j2, double d, double d2);

    private static native void pyrUp_0(long j, long j2, double d, double d2, int i);

    private static native void pyrUp_1(long j, long j2, double d, double d2);

    private static native void pyrUp_2(long j, long j2);

    private static native void rectangle_0(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i, int i2, int i3);

    private static native void rectangle_1(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, int i);

    private static native void rectangle_2(long j, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    private static native void remap_0(long j, long j2, long j3, long j4, int i, int i2, double d, double d2, double d3, double d4);

    private static native void remap_1(long j, long j2, long j3, long j4, int i);

    private static native void resize_0(long j, long j2, double d, double d2, double d3, double d4, int i);

    private static native void resize_1(long j, long j2, double d, double d2);

    private static native int rotatedRectangleIntersection_0(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, long j);

    private static native void sepFilter2D_0(long j, long j2, int i, long j3, long j4, double d, double d2, double d3, int i2);

    private static native void sepFilter2D_1(long j, long j2, int i, long j3, long j4, double d, double d2, double d3);

    private static native void sepFilter2D_2(long j, long j2, int i, long j3, long j4);

    private static native void spatialGradient_0(long j, long j2, long j3, int i, int i2);

    private static native void spatialGradient_1(long j, long j2, long j3, int i);

    private static native void spatialGradient_2(long j, long j2, long j3);

    private static native void sqrBoxFilter_0(long j, long j2, int i, double d, double d2, double d3, double d4, boolean z, int i2);

    private static native void sqrBoxFilter_1(long j, long j2, int i, double d, double d2, double d3, double d4, boolean z);

    private static native void sqrBoxFilter_2(long j, long j2, int i, double d, double d2);

    private static native double threshold_0(long j, long j2, double d, double d2, int i);

    private static native void undistortPoints_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void undistortPoints_1(long j, long j2, long j3, long j4);

    private static native void undistort_0(long j, long j2, long j3, long j4, long j5);

    private static native void undistort_1(long j, long j2, long j3, long j4);

    private static native void warpAffine_0(long j, long j2, long j3, double d, double d2, int i, int i2, double d3, double d4, double d5, double d6);

    private static native void warpAffine_1(long j, long j2, long j3, double d, double d2, int i);

    private static native void warpAffine_2(long j, long j2, long j3, double d, double d2);

    private static native void warpPerspective_0(long j, long j2, long j3, double d, double d2, int i, int i2, double d3, double d4, double d5, double d6);

    private static native void warpPerspective_1(long j, long j2, long j3, double d, double d2, int i);

    private static native void warpPerspective_2(long j, long j2, long j3, double d, double d2);

    private static native void watershed_0(long j, long j2);

    public static Mat getAffineTransform(MatOfPoint2f src, MatOfPoint2f dst) {
        return new Mat(getAffineTransform_0(src.nativeObj, dst.nativeObj));
    }

    public static Mat getDefaultNewCameraMatrix(Mat cameraMatrix, Size imgsize, boolean centerPrincipalPoint) {
        return new Mat(getDefaultNewCameraMatrix_0(cameraMatrix.nativeObj, imgsize.width, imgsize.height, centerPrincipalPoint));
    }

    public static Mat getDefaultNewCameraMatrix(Mat cameraMatrix) {
        return new Mat(getDefaultNewCameraMatrix_1(cameraMatrix.nativeObj));
    }

    public static Mat getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma, double psi, int ktype) {
        return new Mat(getGaborKernel_0(ksize.width, ksize.height, sigma, theta, lambd, gamma, psi, ktype));
    }

    public static Mat getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma) {
        return new Mat(getGaborKernel_1(ksize.width, ksize.height, sigma, theta, lambd, gamma));
    }

    public static Mat getGaussianKernel(int ksize, double sigma, int ktype) {
        return new Mat(getGaussianKernel_0(ksize, sigma, ktype));
    }

    public static Mat getGaussianKernel(int ksize, double sigma) {
        return new Mat(getGaussianKernel_1(ksize, sigma));
    }

    public static Mat getPerspectiveTransform(Mat src, Mat dst) {
        return new Mat(getPerspectiveTransform_0(src.nativeObj, dst.nativeObj));
    }

    public static Mat getRotationMatrix2D(Point center, double angle, double scale) {
        return new Mat(getRotationMatrix2D_0(center.f1125x, center.f1126y, angle, scale));
    }

    public static Mat getStructuringElement(int shape, Size ksize, Point anchor) {
        return new Mat(getStructuringElement_0(shape, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y));
    }

    public static Mat getStructuringElement(int shape, Size ksize) {
        return new Mat(getStructuringElement_1(shape, ksize.width, ksize.height));
    }

    public static Moments moments(Mat array, boolean binaryImage) {
        return new Moments(moments_0(array.nativeObj, binaryImage));
    }

    public static Moments moments(Mat array) {
        return new Moments(moments_1(array.nativeObj));
    }

    public static Point phaseCorrelate(Mat src1, Mat src2, Mat window, double[] response) {
        double[] response_out = new double[1];
        Point retVal = new Point(phaseCorrelate_0(src1.nativeObj, src2.nativeObj, window.nativeObj, response_out));
        if (response != null) {
            response[0] = response_out[0];
        }
        return retVal;
    }

    public static Point phaseCorrelate(Mat src1, Mat src2) {
        return new Point(phaseCorrelate_1(src1.nativeObj, src2.nativeObj));
    }

    public static CLAHE createCLAHE(double clipLimit, Size tileGridSize) {
        return new CLAHE(createCLAHE_0(clipLimit, tileGridSize.width, tileGridSize.height));
    }

    public static CLAHE createCLAHE() {
        return new CLAHE(createCLAHE_1());
    }

    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps, double _density_th, int _n_bins) {
        return new LineSegmentDetector(createLineSegmentDetector_0(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps, _density_th, _n_bins));
    }

    public static LineSegmentDetector createLineSegmentDetector() {
        return new LineSegmentDetector(createLineSegmentDetector_1());
    }

    public static Rect boundingRect(MatOfPoint points) {
        return new Rect(boundingRect_0(points.nativeObj));
    }

    public static RotatedRect fitEllipse(MatOfPoint2f points) {
        return new RotatedRect(fitEllipse_0(points.nativeObj));
    }

    public static RotatedRect minAreaRect(MatOfPoint2f points) {
        return new RotatedRect(minAreaRect_0(points.nativeObj));
    }

    public static boolean clipLine(Rect imgRect, Point pt1, Point pt2) {
        double[] pt1_out = new double[2];
        double[] pt2_out = new double[2];
        boolean retVal = clipLine_0(imgRect.f1130x, imgRect.f1131y, imgRect.width, imgRect.height, pt1.f1125x, pt1.f1126y, pt1_out, pt2.f1125x, pt2.f1126y, pt2_out);
        if (pt1 != null) {
            pt1.f1125x = pt1_out[0];
            pt1.f1126y = pt1_out[1];
        }
        if (pt2 != null) {
            pt2.f1125x = pt2_out[0];
            pt2.f1126y = pt2_out[1];
        }
        return retVal;
    }

    public static boolean isContourConvex(MatOfPoint contour) {
        return isContourConvex_0(contour.nativeObj);
    }

    public static double arcLength(MatOfPoint2f curve, boolean closed) {
        return arcLength_0(curve.nativeObj, closed);
    }

    public static double compareHist(Mat H1, Mat H2, int method) {
        return compareHist_0(H1.nativeObj, H2.nativeObj, method);
    }

    public static double contourArea(Mat contour, boolean oriented) {
        return contourArea_0(contour.nativeObj, oriented);
    }

    public static double contourArea(Mat contour) {
        return contourArea_1(contour.nativeObj);
    }

    public static double matchShapes(Mat contour1, Mat contour2, int method, double parameter) {
        return matchShapes_0(contour1.nativeObj, contour2.nativeObj, method, parameter);
    }

    public static double minEnclosingTriangle(Mat points, Mat triangle) {
        return minEnclosingTriangle_0(points.nativeObj, triangle.nativeObj);
    }

    public static double pointPolygonTest(MatOfPoint2f contour, Point pt, boolean measureDist) {
        return pointPolygonTest_0(contour.nativeObj, pt.f1125x, pt.f1126y, measureDist);
    }

    public static double threshold(Mat src, Mat dst, double thresh, double maxval, int type) {
        return threshold_0(src.nativeObj, dst.nativeObj, thresh, maxval, type);
    }

    public static float initWideAngleProjMap(Mat cameraMatrix, Mat distCoeffs, Size imageSize, int destImageWidth, int m1type, Mat map1, Mat map2, int projType, double alpha) {
        return initWideAngleProjMap_0(cameraMatrix.nativeObj, distCoeffs.nativeObj, imageSize.width, imageSize.height, destImageWidth, m1type, map1.nativeObj, map2.nativeObj, projType, alpha);
    }

    public static float initWideAngleProjMap(Mat cameraMatrix, Mat distCoeffs, Size imageSize, int destImageWidth, int m1type, Mat map1, Mat map2) {
        return initWideAngleProjMap_1(cameraMatrix.nativeObj, distCoeffs.nativeObj, imageSize.width, imageSize.height, destImageWidth, m1type, map1.nativeObj, map2.nativeObj);
    }

    public static float intersectConvexConvex(Mat _p1, Mat _p2, Mat _p12, boolean handleNested) {
        return intersectConvexConvex_0(_p1.nativeObj, _p2.nativeObj, _p12.nativeObj, handleNested);
    }

    public static float intersectConvexConvex(Mat _p1, Mat _p2, Mat _p12) {
        return intersectConvexConvex_1(_p1.nativeObj, _p2.nativeObj, _p12.nativeObj);
    }

    public static int connectedComponentsWithAlgorithm(Mat image, Mat labels, int connectivity, int ltype, int ccltype) {
        return connectedComponentsWithAlgorithm_0(image.nativeObj, labels.nativeObj, connectivity, ltype, ccltype);
    }

    public static int connectedComponents(Mat image, Mat labels, int connectivity, int ltype) {
        return connectedComponents_0(image.nativeObj, labels.nativeObj, connectivity, ltype);
    }

    public static int connectedComponents(Mat image, Mat labels) {
        return connectedComponents_1(image.nativeObj, labels.nativeObj);
    }

    public static int connectedComponentsWithStatsWithAlgorithm(Mat image, Mat labels, Mat stats, Mat centroids, int connectivity, int ltype, int ccltype) {
        return connectedComponentsWithStatsWithAlgorithm_0(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj, connectivity, ltype, ccltype);
    }

    public static int connectedComponentsWithStats(Mat image, Mat labels, Mat stats, Mat centroids, int connectivity, int ltype) {
        return connectedComponentsWithStats_0(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj, connectivity, ltype);
    }

    public static int connectedComponentsWithStats(Mat image, Mat labels, Mat stats, Mat centroids) {
        return connectedComponentsWithStats_1(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj);
    }

    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff, Scalar upDiff, int flags) {
        double[] rect_out = new double[4];
        int retVal = floodFill_0(image.nativeObj, mask.nativeObj, seedPoint.f1125x, seedPoint.f1126y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out, loDiff.val[0], loDiff.val[1], loDiff.val[2], loDiff.val[3], upDiff.val[0], upDiff.val[1], upDiff.val[2], upDiff.val[3], flags);
        if (rect != null) {
            rect.f1130x = (int) rect_out[0];
            rect.f1131y = (int) rect_out[1];
            rect.width = (int) rect_out[2];
            rect.height = (int) rect_out[3];
        }
        return retVal;
    }

    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal) {
        return floodFill_1(image.nativeObj, mask.nativeObj, seedPoint.f1125x, seedPoint.f1126y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3]);
    }

    public static int rotatedRectangleIntersection(RotatedRect rect1, RotatedRect rect2, Mat intersectingRegion) {
        return rotatedRectangleIntersection_0(rect1.center.f1125x, rect1.center.f1126y, rect1.size.width, rect1.size.height, rect1.angle, rect2.center.f1125x, rect2.center.f1126y, rect2.size.width, rect2.size.height, rect2.angle, intersectingRegion.nativeObj);
    }

    public static void Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2, boolean L2gradient) {
        Canny_0(dx.nativeObj, dy.nativeObj, edges.nativeObj, threshold1, threshold2, L2gradient);
    }

    public static void Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2) {
        Canny_1(dx.nativeObj, dy.nativeObj, edges.nativeObj, threshold1, threshold2);
    }

    public static void Canny(Mat image, Mat edges, double threshold1, double threshold2, int apertureSize, boolean L2gradient) {
        Canny_2(image.nativeObj, edges.nativeObj, threshold1, threshold2, apertureSize, L2gradient);
    }

    public static void Canny(Mat image, Mat edges, double threshold1, double threshold2) {
        Canny_3(image.nativeObj, edges.nativeObj, threshold1, threshold2);
    }

    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY, int borderType) {
        GaussianBlur_0(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX, sigmaY, borderType);
    }

    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY) {
        GaussianBlur_1(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX, sigmaY);
    }

    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX) {
        GaussianBlur_2(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX);
    }

    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist, double param1, double param2, int minRadius, int maxRadius) {
        HoughCircles_0(image.nativeObj, circles.nativeObj, method, dp, minDist, param1, param2, minRadius, maxRadius);
    }

    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist) {
        HoughCircles_1(image.nativeObj, circles.nativeObj, method, dp, minDist);
    }

    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold, double srn, double stn, double min_theta, double max_theta) {
        HoughLines_0(image.nativeObj, lines.nativeObj, rho, theta, threshold, srn, stn, min_theta, max_theta);
    }

    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold) {
        HoughLines_1(image.nativeObj, lines.nativeObj, rho, theta, threshold);
    }

    public static void HoughLinesP(Mat image, Mat lines, double rho, double theta, int threshold, double minLineLength, double maxLineGap) {
        HoughLinesP_0(image.nativeObj, lines.nativeObj, rho, theta, threshold, minLineLength, maxLineGap);
    }

    public static void HoughLinesP(Mat image, Mat lines, double rho, double theta, int threshold) {
        HoughLinesP_1(image.nativeObj, lines.nativeObj, rho, theta, threshold);
    }

    public static void HuMoments(Moments m, Mat hu) {
        HuMoments_0(m.m00, m.m10, m.m01, m.m20, m.m11, m.m02, m.m30, m.m21, m.m12, m.m03, hu.nativeObj);
    }

    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize, double scale, double delta, int borderType) {
        Laplacian_0(src.nativeObj, dst.nativeObj, ddepth, ksize, scale, delta, borderType);
    }

    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize, double scale, double delta) {
        Laplacian_1(src.nativeObj, dst.nativeObj, ddepth, ksize, scale, delta);
    }

    public static void Laplacian(Mat src, Mat dst, int ddepth) {
        Laplacian_2(src.nativeObj, dst.nativeObj, ddepth);
    }

    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy, double scale, double delta, int borderType) {
        Scharr_0(src.nativeObj, dst.nativeObj, ddepth, dx, dy, scale, delta, borderType);
    }

    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy, double scale, double delta) {
        Scharr_1(src.nativeObj, dst.nativeObj, ddepth, dx, dy, scale, delta);
    }

    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy) {
        Scharr_2(src.nativeObj, dst.nativeObj, ddepth, dx, dy);
    }

    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize, double scale, double delta, int borderType) {
        Sobel_0(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize, scale, delta, borderType);
    }

    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize, double scale, double delta) {
        Sobel_1(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize, scale, delta);
    }

    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy) {
        Sobel_2(src.nativeObj, dst.nativeObj, ddepth, dx, dy);
    }

    public static void accumulate(Mat src, Mat dst, Mat mask) {
        accumulate_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void accumulate(Mat src, Mat dst) {
        accumulate_1(src.nativeObj, dst.nativeObj);
    }

    public static void accumulateProduct(Mat src1, Mat src2, Mat dst, Mat mask) {
        accumulateProduct_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void accumulateProduct(Mat src1, Mat src2, Mat dst) {
        accumulateProduct_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
    }

    public static void accumulateSquare(Mat src, Mat dst, Mat mask) {
        accumulateSquare_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
    }

    public static void accumulateSquare(Mat src, Mat dst) {
        accumulateSquare_1(src.nativeObj, dst.nativeObj);
    }

    public static void accumulateWeighted(Mat src, Mat dst, double alpha, Mat mask) {
        accumulateWeighted_0(src.nativeObj, dst.nativeObj, alpha, mask.nativeObj);
    }

    public static void accumulateWeighted(Mat src, Mat dst, double alpha) {
        accumulateWeighted_1(src.nativeObj, dst.nativeObj, alpha);
    }

    public static void adaptiveThreshold(Mat src, Mat dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C) {
        adaptiveThreshold_0(src.nativeObj, dst.nativeObj, maxValue, adaptiveMethod, thresholdType, blockSize, C);
    }

    public static void applyColorMap(Mat src, Mat dst, int colormap) {
        applyColorMap_0(src.nativeObj, dst.nativeObj, colormap);
    }

    public static void approxPolyDP(MatOfPoint2f curve, MatOfPoint2f approxCurve, double epsilon, boolean closed) {
        approxPolyDP_0(curve.nativeObj, approxCurve.nativeObj, epsilon, closed);
    }

    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int line_type, int shift, double tipLength) {
        arrowedLine_0(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, line_type, shift, tipLength);
    }

    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color) {
        arrowedLine_1(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace, int borderType) {
        bilateralFilter_0(src.nativeObj, dst.nativeObj, d, sigmaColor, sigmaSpace, borderType);
    }

    public static void bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace) {
        bilateralFilter_1(src.nativeObj, dst.nativeObj, d, sigmaColor, sigmaSpace);
    }

    public static void blur(Mat src, Mat dst, Size ksize, Point anchor, int borderType) {
        blur_0(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y, borderType);
    }

    public static void blur(Mat src, Mat dst, Size ksize, Point anchor) {
        blur_1(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y);
    }

    public static void blur(Mat src, Mat dst, Size ksize) {
        blur_2(src.nativeObj, dst.nativeObj, ksize.width, ksize.height);
    }

    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize, int borderType) {
        boxFilter_0(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y, normalize, borderType);
    }

    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize) {
        boxFilter_1(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y, normalize);
    }

    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize) {
        boxFilter_2(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height);
    }

    public static void boxPoints(RotatedRect box, Mat points) {
        boxPoints_0(box.center.f1125x, box.center.f1126y, box.size.width, box.size.height, box.angle, points.nativeObj);
    }

    public static void calcBackProject(List<Mat> images, MatOfInt channels, Mat hist, Mat dst, MatOfFloat ranges, double scale) {
        calcBackProject_0(Converters.vector_Mat_to_Mat(images).nativeObj, channels.nativeObj, hist.nativeObj, dst.nativeObj, ranges.nativeObj, scale);
    }

    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges, boolean accumulate) {
        calcHist_0(Converters.vector_Mat_to_Mat(images).nativeObj, channels.nativeObj, mask.nativeObj, hist.nativeObj, histSize.nativeObj, ranges.nativeObj, accumulate);
    }

    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges) {
        calcHist_1(Converters.vector_Mat_to_Mat(images).nativeObj, channels.nativeObj, mask.nativeObj, hist.nativeObj, histSize.nativeObj, ranges.nativeObj);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType, int shift) {
        circle_0(img.nativeObj, center.f1125x, center.f1126y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness) {
        circle_1(img.nativeObj, center.f1125x, center.f1126y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void circle(Mat img, Point center, int radius, Scalar color) {
        circle_2(img.nativeObj, center.f1125x, center.f1126y, radius, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type, boolean nninterpolation) {
        convertMaps_0(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type, nninterpolation);
    }

    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type) {
        convertMaps_1(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type);
    }

    public static void convexHull(MatOfPoint points, MatOfInt hull, boolean clockwise) {
        convexHull_0(points.nativeObj, hull.nativeObj, clockwise);
    }

    public static void convexHull(MatOfPoint points, MatOfInt hull) {
        convexHull_1(points.nativeObj, hull.nativeObj);
    }

    public static void convexityDefects(MatOfPoint contour, MatOfInt convexhull, MatOfInt4 convexityDefects) {
        convexityDefects_0(contour.nativeObj, convexhull.nativeObj, convexityDefects.nativeObj);
    }

    public static void cornerEigenValsAndVecs(Mat src, Mat dst, int blockSize, int ksize, int borderType) {
        cornerEigenValsAndVecs_0(src.nativeObj, dst.nativeObj, blockSize, ksize, borderType);
    }

    public static void cornerEigenValsAndVecs(Mat src, Mat dst, int blockSize, int ksize) {
        cornerEigenValsAndVecs_1(src.nativeObj, dst.nativeObj, blockSize, ksize);
    }

    public static void cornerHarris(Mat src, Mat dst, int blockSize, int ksize, double k, int borderType) {
        cornerHarris_0(src.nativeObj, dst.nativeObj, blockSize, ksize, k, borderType);
    }

    public static void cornerHarris(Mat src, Mat dst, int blockSize, int ksize, double k) {
        cornerHarris_1(src.nativeObj, dst.nativeObj, blockSize, ksize, k);
    }

    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize, int ksize, int borderType) {
        cornerMinEigenVal_0(src.nativeObj, dst.nativeObj, blockSize, ksize, borderType);
    }

    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize, int ksize) {
        cornerMinEigenVal_1(src.nativeObj, dst.nativeObj, blockSize, ksize);
    }

    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize) {
        cornerMinEigenVal_2(src.nativeObj, dst.nativeObj, blockSize);
    }

    public static void cornerSubPix(Mat image, MatOfPoint2f corners, Size winSize, Size zeroZone, TermCriteria criteria) {
        cornerSubPix_0(image.nativeObj, corners.nativeObj, winSize.width, winSize.height, zeroZone.width, zeroZone.height, criteria.type, criteria.maxCount, criteria.epsilon);
    }

    public static void createHanningWindow(Mat dst, Size winSize, int type) {
        createHanningWindow_0(dst.nativeObj, winSize.width, winSize.height, type);
    }

    public static void cvtColor(Mat src, Mat dst, int code, int dstCn) {
        cvtColor_0(src.nativeObj, dst.nativeObj, code, dstCn);
    }

    public static void cvtColor(Mat src, Mat dst, int code) {
        cvtColor_1(src.nativeObj, dst.nativeObj, code);
    }

    public static void demosaicing(Mat _src, Mat _dst, int code, int dcn) {
        demosaicing_0(_src.nativeObj, _dst.nativeObj, code, dcn);
    }

    public static void demosaicing(Mat _src, Mat _dst, int code) {
        demosaicing_1(_src.nativeObj, _dst.nativeObj, code);
    }

    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue) {
        dilate_0(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations) {
        dilate_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations);
    }

    public static void dilate(Mat src, Mat dst, Mat kernel) {
        dilate_2(src.nativeObj, dst.nativeObj, kernel.nativeObj);
    }

    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize, int labelType) {
        distanceTransformWithLabels_0(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize, labelType);
    }

    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize) {
        distanceTransformWithLabels_1(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize);
    }

    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize, int dstType) {
        distanceTransform_0(src.nativeObj, dst.nativeObj, distanceType, maskSize, dstType);
    }

    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize) {
        distanceTransform_1(src.nativeObj, dst.nativeObj, distanceType, maskSize);
    }

    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness, int lineType, Mat hierarchy, int maxLevel, Point offset) {
        drawContours_0(image.nativeObj, Converters.vector_vector_Point_to_Mat(contours, new ArrayList(contours != null ? contours.size() : 0)).nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, hierarchy.nativeObj, maxLevel, offset.f1125x, offset.f1126y);
    }

    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness) {
        drawContours_1(image.nativeObj, Converters.vector_vector_Point_to_Mat(contours, new ArrayList(contours != null ? contours.size() : 0)).nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color) {
        drawContours_2(image.nativeObj, Converters.vector_vector_Point_to_Mat(contours, new ArrayList(contours != null ? contours.size() : 0)).nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void drawMarker(Mat img, Point position, Scalar color, int markerType, int markerSize, int thickness, int line_type) {
        drawMarker_0(img.nativeObj, position.f1125x, position.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], markerType, markerSize, thickness, line_type);
    }

    public static void drawMarker(Mat img, Point position, Scalar color) {
        drawMarker_1(img.nativeObj, position.f1125x, position.f1126y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType, int shift) {
        ellipse_0(img.nativeObj, center.f1125x, center.f1126y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness) {
        ellipse_1(img.nativeObj, center.f1125x, center.f1126y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color) {
        ellipse_2(img.nativeObj, center.f1125x, center.f1126y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness, int lineType) {
        ellipse_3(img.nativeObj, box.center.f1125x, box.center.f1126y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness) {
        ellipse_4(img.nativeObj, box.center.f1125x, box.center.f1126y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void ellipse(Mat img, RotatedRect box, Scalar color) {
        ellipse_5(img.nativeObj, box.center.f1125x, box.center.f1126y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, MatOfPoint pts) {
        ellipse2Poly_0(center.f1125x, center.f1126y, axes.width, axes.height, angle, arcStart, arcEnd, delta, pts.nativeObj);
    }

    public static void equalizeHist(Mat src, Mat dst) {
        equalizeHist_0(src.nativeObj, dst.nativeObj);
    }

    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue) {
        erode_0(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations) {
        erode_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations);
    }

    public static void erode(Mat src, Mat dst, Mat kernel) {
        erode_2(src.nativeObj, dst.nativeObj, kernel.nativeObj);
    }

    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color, int lineType, int shift) {
        fillConvexPoly_0(img.nativeObj, points.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift);
    }

    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color) {
        fillConvexPoly_1(img.nativeObj, points.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color, int lineType, int shift, Point offset) {
        fillPoly_0(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift, offset.f1125x, offset.f1126y);
    }

    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color) {
        fillPoly_1(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList<>(pts != null ? pts.size() : 0)).nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel, Point anchor, double delta, int borderType) {
        filter2D_0(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj, anchor.f1125x, anchor.f1126y, delta, borderType);
    }

    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel, Point anchor, double delta) {
        filter2D_1(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj, anchor.f1125x, anchor.f1126y, delta);
    }

    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel) {
        filter2D_2(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj);
    }

    public static void findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset) {
        Mat contours_mat = new Mat();
        findContours_0(image.nativeObj, contours_mat.nativeObj, hierarchy.nativeObj, mode, method, offset.f1125x, offset.f1126y);
        Converters.Mat_to_vector_vector_Point(contours_mat, contours);
        contours_mat.release();
    }

    public static void findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method) {
        Mat contours_mat = new Mat();
        findContours_1(image.nativeObj, contours_mat.nativeObj, hierarchy.nativeObj, mode, method);
        Converters.Mat_to_vector_vector_Point(contours_mat, contours);
        contours_mat.release();
    }

    public static void fitLine(Mat points, Mat line, int distType, double param, double reps, double aeps) {
        fitLine_0(points.nativeObj, line.nativeObj, distType, param, reps, aeps);
    }

    public static void getDerivKernels(Mat kx, Mat ky, int dx, int dy, int ksize, boolean normalize, int ktype) {
        getDerivKernels_0(kx.nativeObj, ky.nativeObj, dx, dy, ksize, normalize, ktype);
    }

    public static void getDerivKernels(Mat kx, Mat ky, int dx, int dy, int ksize) {
        getDerivKernels_1(kx.nativeObj, ky.nativeObj, dx, dy, ksize);
    }

    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch, int patchType) {
        getRectSubPix_0(image.nativeObj, patchSize.width, patchSize.height, center.f1125x, center.f1126y, patch.nativeObj, patchType);
    }

    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch) {
        getRectSubPix_1(image.nativeObj, patchSize.width, patchSize.height, center.f1125x, center.f1126y, patch.nativeObj);
    }

    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, boolean useHarrisDetector, double k) {
        goodFeaturesToTrack_0(image.nativeObj, corners.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, useHarrisDetector, k);
    }

    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance) {
        goodFeaturesToTrack_1(image.nativeObj, corners.nativeObj, maxCorners, qualityLevel, minDistance);
    }

    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount, int mode) {
        grabCut_0(img.nativeObj, mask.nativeObj, rect.f1130x, rect.f1131y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount, mode);
    }

    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount) {
        grabCut_1(img.nativeObj, mask.nativeObj, rect.f1130x, rect.f1131y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount);
    }

    public static void initUndistortRectifyMap(Mat cameraMatrix, Mat distCoeffs, Mat R, Mat newCameraMatrix, Size size, int m1type, Mat map1, Mat map2) {
        initUndistortRectifyMap_0(cameraMatrix.nativeObj, distCoeffs.nativeObj, R.nativeObj, newCameraMatrix.nativeObj, size.width, size.height, m1type, map1.nativeObj, map2.nativeObj);
    }

    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted, int sdepth, int sqdepth) {
        integral3_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj, sdepth, sqdepth);
    }

    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted) {
        integral3_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj);
    }

    public static void integral2(Mat src, Mat sum, Mat sqsum, int sdepth, int sqdepth) {
        integral2_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, sdepth, sqdepth);
    }

    public static void integral2(Mat src, Mat sum, Mat sqsum) {
        integral2_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj);
    }

    public static void integral(Mat src, Mat sum, int sdepth) {
        integral_0(src.nativeObj, sum.nativeObj, sdepth);
    }

    public static void integral(Mat src, Mat sum) {
        integral_1(src.nativeObj, sum.nativeObj);
    }

    public static void invertAffineTransform(Mat M, Mat iM) {
        invertAffineTransform_0(M.nativeObj, iM.nativeObj);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) {
        line_0(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness) {
        line_1(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void line(Mat img, Point pt1, Point pt2, Scalar color) {
        line_2(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void linearPolar(Mat src, Mat dst, Point center, double maxRadius, int flags) {
        linearPolar_0(src.nativeObj, dst.nativeObj, center.f1125x, center.f1126y, maxRadius, flags);
    }

    public static void logPolar(Mat src, Mat dst, Point center, double M, int flags) {
        logPolar_0(src.nativeObj, dst.nativeObj, center.f1125x, center.f1126y, M, flags);
    }

    public static void matchTemplate(Mat image, Mat templ, Mat result, int method, Mat mask) {
        matchTemplate_0(image.nativeObj, templ.nativeObj, result.nativeObj, method, mask.nativeObj);
    }

    public static void matchTemplate(Mat image, Mat templ, Mat result, int method) {
        matchTemplate_1(image.nativeObj, templ.nativeObj, result.nativeObj, method);
    }

    public static void medianBlur(Mat src, Mat dst, int ksize) {
        medianBlur_0(src.nativeObj, dst.nativeObj, ksize);
    }

    public static void minEnclosingCircle(MatOfPoint2f points, Point center, float[] radius) {
        double[] center_out = new double[2];
        double[] radius_out = new double[1];
        minEnclosingCircle_0(points.nativeObj, center_out, radius_out);
        if (center != null) {
            center.f1125x = center_out[0];
            center.f1126y = center_out[1];
        }
        if (radius != null) {
            radius[0] = (float) radius_out[0];
        }
    }

    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue) {
        morphologyEx_0(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations) {
        morphologyEx_1(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.f1125x, anchor.f1126y, iterations);
    }

    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel) {
        morphologyEx_2(src.nativeObj, dst.nativeObj, op, kernel.nativeObj);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness, int lineType, int shift) {
        polylines_0(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness) {
        polylines_1(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color) {
        polylines_2(img.nativeObj, Converters.vector_vector_Point_to_Mat(pts, new ArrayList(pts != null ? pts.size() : 0)).nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void preCornerDetect(Mat src, Mat dst, int ksize, int borderType) {
        preCornerDetect_0(src.nativeObj, dst.nativeObj, ksize, borderType);
    }

    public static void preCornerDetect(Mat src, Mat dst, int ksize) {
        preCornerDetect_1(src.nativeObj, dst.nativeObj, ksize);
    }

    public static void putText(Mat img, String text, Point org2, int fontFace, double fontScale, Scalar color, int thickness, int lineType, boolean bottomLeftOrigin) {
        putText_0(img.nativeObj, text, org2.f1125x, org2.f1126y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, bottomLeftOrigin);
    }

    public static void putText(Mat img, String text, Point org2, int fontFace, double fontScale, Scalar color, int thickness) {
        putText_1(img.nativeObj, text, org2.f1125x, org2.f1126y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void putText(Mat img, String text, Point org2, int fontFace, double fontScale, Scalar color) {
        putText_2(img.nativeObj, text, org2.f1125x, org2.f1126y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void pyrDown(Mat src, Mat dst, Size dstsize, int borderType) {
        pyrDown_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
    }

    public static void pyrDown(Mat src, Mat dst, Size dstsize) {
        pyrDown_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
    }

    public static void pyrDown(Mat src, Mat dst) {
        pyrDown_2(src.nativeObj, dst.nativeObj);
    }

    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr, int maxLevel, TermCriteria termcrit) {
        pyrMeanShiftFiltering_0(src.nativeObj, dst.nativeObj, sp, sr, maxLevel, termcrit.type, termcrit.maxCount, termcrit.epsilon);
    }

    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr) {
        pyrMeanShiftFiltering_1(src.nativeObj, dst.nativeObj, sp, sr);
    }

    public static void pyrUp(Mat src, Mat dst, Size dstsize, int borderType) {
        pyrUp_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
    }

    public static void pyrUp(Mat src, Mat dst, Size dstsize) {
        pyrUp_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
    }

    public static void pyrUp(Mat src, Mat dst) {
        pyrUp_2(src.nativeObj, dst.nativeObj);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift) {
        rectangle_0(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness) {
        rectangle_1(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
    }

    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color) {
        rectangle_2(img.nativeObj, pt1.f1125x, pt1.f1126y, pt2.f1125x, pt2.f1126y, color.val[0], color.val[1], color.val[2], color.val[3]);
    }

    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation, int borderMode, Scalar borderValue) {
        remap_0(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation) {
        remap_1(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation);
    }

    public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation) {
        resize_0(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, fx, fy, interpolation);
    }

    public static void resize(Mat src, Mat dst, Size dsize) {
        resize_1(src.nativeObj, dst.nativeObj, dsize.width, dsize.height);
    }

    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor, double delta, int borderType) {
        sepFilter2D_0(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj, anchor.f1125x, anchor.f1126y, delta, borderType);
    }

    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor, double delta) {
        sepFilter2D_1(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj, anchor.f1125x, anchor.f1126y, delta);
    }

    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY) {
        sepFilter2D_2(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj);
    }

    public static void spatialGradient(Mat src, Mat dx, Mat dy, int ksize, int borderType) {
        spatialGradient_0(src.nativeObj, dx.nativeObj, dy.nativeObj, ksize, borderType);
    }

    public static void spatialGradient(Mat src, Mat dx, Mat dy, int ksize) {
        spatialGradient_1(src.nativeObj, dx.nativeObj, dy.nativeObj, ksize);
    }

    public static void spatialGradient(Mat src, Mat dx, Mat dy) {
        spatialGradient_2(src.nativeObj, dx.nativeObj, dy.nativeObj);
    }

    public static void sqrBoxFilter(Mat _src, Mat _dst, int ddepth, Size ksize, Point anchor, boolean normalize, int borderType) {
        sqrBoxFilter_0(_src.nativeObj, _dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y, normalize, borderType);
    }

    public static void sqrBoxFilter(Mat _src, Mat _dst, int ddepth, Size ksize, Point anchor, boolean normalize) {
        sqrBoxFilter_1(_src.nativeObj, _dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.f1125x, anchor.f1126y, normalize);
    }

    public static void sqrBoxFilter(Mat _src, Mat _dst, int ddepth, Size ksize) {
        sqrBoxFilter_2(_src.nativeObj, _dst.nativeObj, ddepth, ksize.width, ksize.height);
    }

    public static void undistort(Mat src, Mat dst, Mat cameraMatrix, Mat distCoeffs, Mat newCameraMatrix) {
        undistort_0(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, newCameraMatrix.nativeObj);
    }

    public static void undistort(Mat src, Mat dst, Mat cameraMatrix, Mat distCoeffs) {
        undistort_1(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj);
    }

    public static void undistortPoints(MatOfPoint2f src, MatOfPoint2f dst, Mat cameraMatrix, Mat distCoeffs, Mat R, Mat P) {
        undistortPoints_0(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj, R.nativeObj, P.nativeObj);
    }

    public static void undistortPoints(MatOfPoint2f src, MatOfPoint2f dst, Mat cameraMatrix, Mat distCoeffs) {
        undistortPoints_1(src.nativeObj, dst.nativeObj, cameraMatrix.nativeObj, distCoeffs.nativeObj);
    }

    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue) {
        warpAffine_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags) {
        warpAffine_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
    }

    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize) {
        warpAffine_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
    }

    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue) {
        warpPerspective_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
    }

    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags) {
        warpPerspective_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
    }

    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize) {
        warpPerspective_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
    }

    public static void watershed(Mat image, Mat markers) {
        watershed_0(image.nativeObj, markers.nativeObj);
    }

    public static Size getTextSize(String text, int fontFace, double fontScale, int thickness, int[] baseLine) {
        if (baseLine == null || baseLine.length == 1) {
            return new Size(n_getTextSize(text, fontFace, fontScale, thickness, baseLine));
        }
        throw new IllegalArgumentException("'baseLine' must be 'int[1]' or 'null'.");
    }
}
