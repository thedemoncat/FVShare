package org.opencv.imgcodecs;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.utils.Converters;

public class Imgcodecs {
    public static final int CV_CVTIMG_FLIP = 1;
    public static final int CV_CVTIMG_SWAP_RB = 2;
    public static final int CV_IMWRITE_JPEG_CHROMA_QUALITY = 6;
    public static final int CV_IMWRITE_JPEG_LUMA_QUALITY = 5;
    public static final int CV_IMWRITE_JPEG_OPTIMIZE = 3;
    public static final int CV_IMWRITE_JPEG_PROGRESSIVE = 2;
    public static final int CV_IMWRITE_JPEG_QUALITY = 1;
    public static final int CV_IMWRITE_JPEG_RST_INTERVAL = 4;
    public static final int CV_IMWRITE_PAM_FORMAT_BLACKANDWHITE = 1;
    public static final int CV_IMWRITE_PAM_FORMAT_GRAYSCALE = 2;
    public static final int CV_IMWRITE_PAM_FORMAT_GRAYSCALE_ALPHA = 3;
    public static final int CV_IMWRITE_PAM_FORMAT_NULL = 0;
    public static final int CV_IMWRITE_PAM_FORMAT_RGB = 4;
    public static final int CV_IMWRITE_PAM_FORMAT_RGB_ALPHA = 5;
    public static final int CV_IMWRITE_PAM_TUPLETYPE = 128;
    public static final int CV_IMWRITE_PNG_BILEVEL = 18;
    public static final int CV_IMWRITE_PNG_COMPRESSION = 16;
    public static final int CV_IMWRITE_PNG_STRATEGY = 17;
    public static final int CV_IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int CV_IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int CV_IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int CV_IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int CV_IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int CV_IMWRITE_PXM_BINARY = 32;
    public static final int CV_IMWRITE_WEBP_QUALITY = 64;
    public static final int CV_LOAD_IMAGE_ANYCOLOR = 4;
    public static final int CV_LOAD_IMAGE_ANYDEPTH = 2;
    public static final int CV_LOAD_IMAGE_COLOR = 1;
    public static final int CV_LOAD_IMAGE_GRAYSCALE = 0;
    public static final int CV_LOAD_IMAGE_IGNORE_ORIENTATION = 128;
    public static final int CV_LOAD_IMAGE_UNCHANGED = -1;
    public static final int IMREAD_ANYCOLOR = 4;
    public static final int IMREAD_ANYDEPTH = 2;
    public static final int IMREAD_COLOR = 1;
    public static final int IMREAD_GRAYSCALE = 0;
    public static final int IMREAD_IGNORE_ORIENTATION = 128;
    public static final int IMREAD_LOAD_GDAL = 8;
    public static final int IMREAD_REDUCED_COLOR_2 = 17;
    public static final int IMREAD_REDUCED_COLOR_4 = 33;
    public static final int IMREAD_REDUCED_COLOR_8 = 65;
    public static final int IMREAD_REDUCED_GRAYSCALE_2 = 16;
    public static final int IMREAD_REDUCED_GRAYSCALE_4 = 32;
    public static final int IMREAD_REDUCED_GRAYSCALE_8 = 64;
    public static final int IMREAD_UNCHANGED = -1;
    public static final int IMWRITE_JPEG_CHROMA_QUALITY = 6;
    public static final int IMWRITE_JPEG_LUMA_QUALITY = 5;
    public static final int IMWRITE_JPEG_OPTIMIZE = 3;
    public static final int IMWRITE_JPEG_PROGRESSIVE = 2;
    public static final int IMWRITE_JPEG_QUALITY = 1;
    public static final int IMWRITE_JPEG_RST_INTERVAL = 4;
    public static final int IMWRITE_PAM_FORMAT_BLACKANDWHITE = 1;
    public static final int IMWRITE_PAM_FORMAT_GRAYSCALE = 2;
    public static final int IMWRITE_PAM_FORMAT_GRAYSCALE_ALPHA = 3;
    public static final int IMWRITE_PAM_FORMAT_NULL = 0;
    public static final int IMWRITE_PAM_FORMAT_RGB = 4;
    public static final int IMWRITE_PAM_FORMAT_RGB_ALPHA = 5;
    public static final int IMWRITE_PAM_TUPLETYPE = 128;
    public static final int IMWRITE_PNG_BILEVEL = 18;
    public static final int IMWRITE_PNG_COMPRESSION = 16;
    public static final int IMWRITE_PNG_STRATEGY = 17;
    public static final int IMWRITE_PNG_STRATEGY_DEFAULT = 0;
    public static final int IMWRITE_PNG_STRATEGY_FILTERED = 1;
    public static final int IMWRITE_PNG_STRATEGY_FIXED = 4;
    public static final int IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2;
    public static final int IMWRITE_PNG_STRATEGY_RLE = 3;
    public static final int IMWRITE_PXM_BINARY = 32;
    public static final int IMWRITE_WEBP_QUALITY = 64;

    private static native long imdecode_0(long j, int i);

    private static native boolean imencode_0(String str, long j, long j2, long j3);

    private static native boolean imencode_1(String str, long j, long j2);

    private static native long imread_0(String str, int i);

    private static native long imread_1(String str);

    private static native boolean imreadmulti_0(String str, long j, int i);

    private static native boolean imreadmulti_1(String str, long j);

    private static native boolean imwrite_0(String str, long j, long j2);

    private static native boolean imwrite_1(String str, long j);

    public static Mat imdecode(Mat buf, int flags) {
        return new Mat(imdecode_0(buf.nativeObj, flags));
    }

    public static Mat imread(String filename, int flags) {
        return new Mat(imread_0(filename, flags));
    }

    public static Mat imread(String filename) {
        return new Mat(imread_1(filename));
    }

    public static boolean imencode(String ext, Mat img, MatOfByte buf, MatOfInt params) {
        return imencode_0(ext, img.nativeObj, buf.nativeObj, params.nativeObj);
    }

    public static boolean imencode(String ext, Mat img, MatOfByte buf) {
        return imencode_1(ext, img.nativeObj, buf.nativeObj);
    }

    public static boolean imreadmulti(String filename, List<Mat> mats, int flags) {
        return imreadmulti_0(filename, Converters.vector_Mat_to_Mat(mats).nativeObj, flags);
    }

    public static boolean imreadmulti(String filename, List<Mat> mats) {
        return imreadmulti_1(filename, Converters.vector_Mat_to_Mat(mats).nativeObj);
    }

    public static boolean imwrite(String filename, Mat img, MatOfInt params) {
        return imwrite_0(filename, img.nativeObj, params.nativeObj);
    }

    public static boolean imwrite(String filename, Mat img) {
        return imwrite_1(filename, img.nativeObj);
    }
}
