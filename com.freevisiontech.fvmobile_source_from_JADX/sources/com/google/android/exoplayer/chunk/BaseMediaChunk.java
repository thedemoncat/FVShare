package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.DefaultTrackOutput;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;

public abstract class BaseMediaChunk extends MediaChunk {
    private int firstSampleIndex;
    public final boolean isMediaFormatFinal;
    private DefaultTrackOutput output;

    public abstract DrmInitData getDrmInitData();

    public abstract MediaFormat getMediaFormat();

    public BaseMediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, boolean isMediaFormatFinal2, int parentId) {
        super(dataSource, dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex, parentId);
        this.isMediaFormatFinal = isMediaFormatFinal2;
    }

    public void init(DefaultTrackOutput output2) {
        this.output = output2;
        this.firstSampleIndex = output2.getWriteIndex();
    }

    public final int getFirstSampleIndex() {
        return this.firstSampleIndex;
    }

    /* access modifiers changed from: protected */
    public final DefaultTrackOutput getOutput() {
        return this.output;
    }
}
