package com.google.android.exoplayer.extractor.mp4;

final class DefaultSampleValues {
    public final int duration;
    public final int flags;
    public final int sampleDescriptionIndex;
    public final int size;

    public DefaultSampleValues(int sampleDescriptionIndex2, int duration2, int size2, int flags2) {
        this.sampleDescriptionIndex = sampleDescriptionIndex2;
        this.duration = duration2;
        this.size = size2;
        this.flags = flags2;
    }
}
