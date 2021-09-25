package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public final class SingleSampleMediaChunk extends BaseMediaChunk {
    private volatile int bytesLoaded;
    private volatile boolean loadCanceled;
    private final DrmInitData sampleDrmInitData;
    private final MediaFormat sampleFormat;

    public SingleSampleMediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, MediaFormat sampleFormat2, DrmInitData sampleDrmInitData2, int parentId) {
        super(dataSource, dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex, true, parentId);
        this.sampleFormat = sampleFormat2;
        this.sampleDrmInitData = sampleDrmInitData2;
    }

    public long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public MediaFormat getMediaFormat() {
        return this.sampleFormat;
    }

    public DrmInitData getDrmInitData() {
        return this.sampleDrmInitData;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public void load() throws IOException, InterruptedException {
        try {
            this.dataSource.open(Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded));
            int result = 0;
            while (result != -1) {
                this.bytesLoaded += result;
                result = getOutput().sampleData(this.dataSource, Integer.MAX_VALUE, true);
            }
            getOutput().sampleMetadata(this.startTimeUs, 1, this.bytesLoaded, 0, (byte[]) null);
        } finally {
            this.dataSource.close();
        }
    }
}
