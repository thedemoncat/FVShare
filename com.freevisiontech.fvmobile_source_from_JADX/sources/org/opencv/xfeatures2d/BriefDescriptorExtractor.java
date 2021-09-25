package org.opencv.xfeatures2d;

import org.opencv.features2d.Feature2D;

public class BriefDescriptorExtractor extends Feature2D {
    private static native long create_0(int i, boolean z);

    private static native long create_1();

    private static native void delete(long j);

    protected BriefDescriptorExtractor(long addr) {
        super(addr);
    }

    public static BriefDescriptorExtractor create(int bytes, boolean use_orientation) {
        return new BriefDescriptorExtractor(create_0(bytes, use_orientation));
    }

    public static BriefDescriptorExtractor create() {
        return new BriefDescriptorExtractor(create_1());
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        delete(this.nativeObj);
    }
}
