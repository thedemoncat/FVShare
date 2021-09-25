package org.opencv.features2d;

public class AKAZE extends Feature2D {
    public static final int DESCRIPTOR_KAZE = 3;
    public static final int DESCRIPTOR_KAZE_UPRIGHT = 2;
    public static final int DESCRIPTOR_MLDB = 5;
    public static final int DESCRIPTOR_MLDB_UPRIGHT = 4;

    private static native long create_0(int i, int i2, int i3, float f, int i4, int i5, int i6);

    private static native long create_1();

    private static native void delete(long j);

    private static native int getDescriptorChannels_0(long j);

    private static native int getDescriptorSize_0(long j);

    private static native int getDescriptorType_0(long j);

    private static native int getDiffusivity_0(long j);

    private static native int getNOctaveLayers_0(long j);

    private static native int getNOctaves_0(long j);

    private static native double getThreshold_0(long j);

    private static native void setDescriptorChannels_0(long j, int i);

    private static native void setDescriptorSize_0(long j, int i);

    private static native void setDescriptorType_0(long j, int i);

    private static native void setDiffusivity_0(long j, int i);

    private static native void setNOctaveLayers_0(long j, int i);

    private static native void setNOctaves_0(long j, int i);

    private static native void setThreshold_0(long j, double d);

    protected AKAZE(long addr) {
        super(addr);
    }

    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves, int nOctaveLayers, int diffusivity) {
        return new AKAZE(create_0(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves, nOctaveLayers, diffusivity));
    }

    public static AKAZE create() {
        return new AKAZE(create_1());
    }

    public double getThreshold() {
        return getThreshold_0(this.nativeObj);
    }

    public int getDescriptorChannels() {
        return getDescriptorChannels_0(this.nativeObj);
    }

    public int getDescriptorSize() {
        return getDescriptorSize_0(this.nativeObj);
    }

    public int getDescriptorType() {
        return getDescriptorType_0(this.nativeObj);
    }

    public int getDiffusivity() {
        return getDiffusivity_0(this.nativeObj);
    }

    public int getNOctaveLayers() {
        return getNOctaveLayers_0(this.nativeObj);
    }

    public int getNOctaves() {
        return getNOctaves_0(this.nativeObj);
    }

    public void setDescriptorChannels(int dch) {
        setDescriptorChannels_0(this.nativeObj, dch);
    }

    public void setDescriptorSize(int dsize) {
        setDescriptorSize_0(this.nativeObj, dsize);
    }

    public void setDescriptorType(int dtype) {
        setDescriptorType_0(this.nativeObj, dtype);
    }

    public void setDiffusivity(int diff) {
        setDiffusivity_0(this.nativeObj, diff);
    }

    public void setNOctaveLayers(int octaveLayers) {
        setNOctaveLayers_0(this.nativeObj, octaveLayers);
    }

    public void setNOctaves(int octaves) {
        setNOctaves_0(this.nativeObj, octaves);
    }

    public void setThreshold(double threshold) {
        setThreshold_0(this.nativeObj, threshold);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
