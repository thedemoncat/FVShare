package org.opencv.structured_light;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.utils.Converters;

public class GrayCodePattern extends StructuredLightPattern {
    private static native long create_0(int i, int i2);

    private static native void delete(long j);

    private static native void getImagesForShadowMasks_0(long j, long j2, long j3);

    private static native long getNumberOfPatternImages_0(long j);

    private static native boolean getProjPixel_0(long j, long j2, int i, int i2, double d, double d2);

    private static native void setBlackThreshold_0(long j, long j2);

    private static native void setWhiteThreshold_0(long j, long j2);

    protected GrayCodePattern(long addr) {
        super(addr);
    }

    public static GrayCodePattern create(int width, int height) {
        return new GrayCodePattern(create_0(width, height));
    }

    public boolean getProjPixel(List<Mat> patternImages, int x, int y, Point projPix) {
        return getProjPixel_0(this.nativeObj, Converters.vector_Mat_to_Mat(patternImages).nativeObj, x, y, projPix.f1125x, projPix.f1126y);
    }

    public long getNumberOfPatternImages() {
        return getNumberOfPatternImages_0(this.nativeObj);
    }

    public void getImagesForShadowMasks(Mat blackImage, Mat whiteImage) {
        getImagesForShadowMasks_0(this.nativeObj, blackImage.nativeObj, whiteImage.nativeObj);
    }

    public void setBlackThreshold(long value) {
        setBlackThreshold_0(this.nativeObj, value);
    }

    public void setWhiteThreshold(long value) {
        setWhiteThreshold_0(this.nativeObj, value);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
