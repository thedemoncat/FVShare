package org.opencv.aruco;

import org.opencv.core.Mat;
import org.opencv.core.Size;

public class GridBoard extends Board {
    private static native long create_0(int i, int i2, float f, float f2, long j, int i3);

    private static native long create_1(int i, int i2, float f, float f2, long j);

    private static native void delete(long j);

    private static native void draw_0(long j, double d, double d2, long j2, int i, int i2);

    private static native void draw_1(long j, double d, double d2, long j2);

    private static native double[] getGridSize_0(long j);

    private static native float getMarkerLength_0(long j);

    private static native float getMarkerSeparation_0(long j);

    protected GridBoard(long addr) {
        super(addr);
    }

    public static GridBoard create(int markersX, int markersY, float markerLength, float markerSeparation, Dictionary dictionary, int firstMarker) {
        return new GridBoard(create_0(markersX, markersY, markerLength, markerSeparation, dictionary.nativeObj, firstMarker));
    }

    public static GridBoard create(int markersX, int markersY, float markerLength, float markerSeparation, Dictionary dictionary) {
        return new GridBoard(create_1(markersX, markersY, markerLength, markerSeparation, dictionary.nativeObj));
    }

    public Size getGridSize() {
        return new Size(getGridSize_0(this.nativeObj));
    }

    public float getMarkerLength() {
        return getMarkerLength_0(this.nativeObj);
    }

    public float getMarkerSeparation() {
        return getMarkerSeparation_0(this.nativeObj);
    }

    public void draw(Size outSize, Mat img, int marginSize, int borderBits) {
        draw_0(this.nativeObj, outSize.width, outSize.height, img.nativeObj, marginSize, borderBits);
    }

    public void draw(Size outSize, Mat img) {
        draw_1(this.nativeObj, outSize.width, outSize.height, img.nativeObj);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
