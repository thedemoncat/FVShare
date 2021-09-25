package org.opencv.highgui;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class Highgui {
    public static final int CV_FONT_BLACK = 87;
    public static final int CV_FONT_BOLD = 75;
    public static final int CV_FONT_DEMIBOLD = 63;
    public static final int CV_FONT_LIGHT = 25;
    public static final int CV_FONT_NORMAL = 50;
    public static final int CV_STYLE_ITALIC = 1;
    public static final int CV_STYLE_NORMAL = 0;
    public static final int CV_STYLE_OBLIQUE = 2;
    public static final int QT_CHECKBOX = 1;
    public static final int QT_FONT_BLACK = 87;
    public static final int QT_FONT_BOLD = 75;
    public static final int QT_FONT_DEMIBOLD = 63;
    public static final int QT_FONT_LIGHT = 25;
    public static final int QT_FONT_NORMAL = 50;
    public static final int QT_NEW_BUTTONBAR = 1024;
    public static final int QT_PUSH_BUTTON = 0;
    public static final int QT_RADIOBOX = 2;
    public static final int QT_STYLE_ITALIC = 1;
    public static final int QT_STYLE_NORMAL = 0;
    public static final int QT_STYLE_OBLIQUE = 2;

    private static native void addText_0(long j, String str, double d, double d2, String str2, int i, double d3, double d4, double d5, double d6, int i2, int i3, int i4);

    private static native void addText_1(long j, String str, double d, double d2, String str2);

    private static native void displayOverlay_0(String str, String str2, int i);

    private static native void displayOverlay_1(String str, String str2);

    private static native void displayStatusBar_0(String str, String str2, int i);

    private static native void displayStatusBar_1(String str, String str2);

    private static native void setTrackbarMax_0(String str, String str2, int i);

    private static native void setTrackbarMin_0(String str, String str2, int i);

    private static native void setWindowTitle_0(String str, String str2);

    private static native int waitKeyEx_0(int i);

    private static native int waitKeyEx_1();

    public static int waitKeyEx(int delay) {
        return waitKeyEx_0(delay);
    }

    public static int waitKeyEx() {
        return waitKeyEx_1();
    }

    public static void addText(Mat img, String text, Point org2, String nameFont, int pointSize, Scalar color, int weight, int style, int spacing) {
        addText_0(img.nativeObj, text, org2.f1125x, org2.f1126y, nameFont, pointSize, color.val[0], color.val[1], color.val[2], color.val[3], weight, style, spacing);
    }

    public static void addText(Mat img, String text, Point org2, String nameFont) {
        addText_1(img.nativeObj, text, org2.f1125x, org2.f1126y, nameFont);
    }

    public static void displayOverlay(String winname, String text, int delayms) {
        displayOverlay_0(winname, text, delayms);
    }

    public static void displayOverlay(String winname, String text) {
        displayOverlay_1(winname, text);
    }

    public static void displayStatusBar(String winname, String text, int delayms) {
        displayStatusBar_0(winname, text, delayms);
    }

    public static void displayStatusBar(String winname, String text) {
        displayStatusBar_1(winname, text);
    }

    public static void setTrackbarMax(String trackbarname, String winname, int maxval) {
        setTrackbarMax_0(trackbarname, winname, maxval);
    }

    public static void setTrackbarMin(String trackbarname, String winname, int minval) {
        setTrackbarMin_0(trackbarname, winname, minval);
    }

    public static void setWindowTitle(String winname, String title) {
        setWindowTitle_0(winname, title);
    }
}
