package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;

public abstract class MediaChunk extends Chunk {
    public final int chunkIndex;
    public final long endTimeUs;
    public final long startTimeUs;

    public MediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs2, long endTimeUs2, int chunkIndex2) {
        this(dataSource, dataSpec, trigger, format, startTimeUs2, endTimeUs2, chunkIndex2, -1);
    }

    public MediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs2, long endTimeUs2, int chunkIndex2, int parentId) {
        super(dataSource, dataSpec, 1, trigger, format, parentId);
        Assertions.checkNotNull(format);
        this.startTimeUs = startTimeUs2;
        this.endTimeUs = endTimeUs2;
        this.chunkIndex = chunkIndex2;
    }

    public int getNextChunkIndex() {
        return this.chunkIndex + 1;
    }

    public long getDurationUs() {
        return this.endTimeUs - this.startTimeUs;
    }
}
