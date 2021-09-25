package org.opencv.aruco;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;

public class CharucoBoard extends Board {
    private static native long create_0(int i, int i2, float f, float f2, long j);

    private static native void delete(long j);

    private static native void draw_0(long j, double d, double d2, long j2, int i, int i2);

    private static native void draw_1(long j, double d, double d2, long j2);

    private static native double[] getChessboardSize_0(long j);

    private static native float getMarkerLength_0(long j);

    private static native float getSquareLength_0(long j);

    private static native long get_chessboardCorners_0(long j);

    protected CharucoBoard(long addr) {
        super(addr);
    }

    public static CharucoBoard create(int squaresX, int squaresY, float squareLength, float markerLength, Dictionary dictionary) {
        return new CharucoBoard(create_0(squaresX, squaresY, squareLength, markerLength, dictionary.nativeObj));
    }

    public Size getChessboardSize() {
        return new Size(getChessboardSize_0(this.nativeObj));
    }

    public float getMarkerLength() {
        return getMarkerLength_0(this.nativeObj);
    }

    public float getSquareLength() {
        return getSquareLength_0(this.nativeObj);
    }

    public void draw(Size outSize, Mat img, int marginSize, int borderBits) {
        draw_0(this.nativeObj, outSize.width, outSize.height, img.nativeObj, marginSize, borderBits);
    }

    public void draw(Size outSize, Mat img) {
        draw_1(this.nativeObj, outSize.width, outSize.height, img.nativeObj);
    }

    public MatOfPoint3f get_chessboardCorners() {
        return MatOfPoint3f.fromNativeAddr(get_chessboardCorners_0(this.nativeObj));
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
