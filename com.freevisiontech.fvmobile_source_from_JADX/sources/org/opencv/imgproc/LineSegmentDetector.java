package org.opencv.imgproc;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class LineSegmentDetector extends Algorithm {
    private static native int compareSegments_0(long j, double d, double d2, long j2, long j3, long j4);

    private static native int compareSegments_1(long j, double d, double d2, long j2, long j3);

    private static native void delete(long j);

    private static native void detect_0(long j, long j2, long j3, long j4, long j5, long j6);

    private static native void detect_1(long j, long j2, long j3);

    private static native void drawSegments_0(long j, long j2, long j3);

    protected LineSegmentDetector(long addr) {
        super(addr);
    }

    public int compareSegments(Size size, Mat lines1, Mat lines2, Mat _image) {
        return compareSegments_0(this.nativeObj, size.width, size.height, lines1.nativeObj, lines2.nativeObj, _image.nativeObj);
    }

    public int compareSegments(Size size, Mat lines1, Mat lines2) {
        return compareSegments_1(this.nativeObj, size.width, size.height, lines1.nativeObj, lines2.nativeObj);
    }

    public void detect(Mat _image, Mat _lines, Mat width, Mat prec, Mat nfa) {
        detect_0(this.nativeObj, _image.nativeObj, _lines.nativeObj, width.nativeObj, prec.nativeObj, nfa.nativeObj);
    }

    public void detect(Mat _image, Mat _lines) {
        detect_1(this.nativeObj, _image.nativeObj, _lines.nativeObj);
    }

    public void drawSegments(Mat _image, Mat lines) {
        drawSegments_0(this.nativeObj, _image.nativeObj, lines.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
