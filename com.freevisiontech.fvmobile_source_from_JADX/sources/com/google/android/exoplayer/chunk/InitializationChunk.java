package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.DefaultExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public class InitializationChunk extends Chunk implements ChunkExtractorWrapper.SingleTrackOutput {
    private volatile int bytesLoaded;
    private DrmInitData drmInitData;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private MediaFormat mediaFormat;
    private SeekMap seekMap;

    public InitializationChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, ChunkExtractorWrapper extractorWrapper2) {
        this(dataSource, dataSpec, trigger, format, extractorWrapper2, -1);
    }

    public InitializationChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, ChunkExtractorWrapper extractorWrapper2, int parentId) {
        super(dataSource, dataSpec, 2, trigger, format, parentId);
        this.extractorWrapper = extractorWrapper2;
    }

    public long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public boolean hasFormat() {
        return this.mediaFormat != null;
    }

    public MediaFormat getFormat() {
        return this.mediaFormat;
    }

    public boolean hasDrmInitData() {
        return this.drmInitData != null;
    }

    public DrmInitData getDrmInitData() {
        return this.drmInitData;
    }

    public boolean hasSeekMap() {
        return this.seekMap != null;
    }

    public SeekMap getSeekMap() {
        return this.seekMap;
    }

    public void seekMap(SeekMap seekMap2) {
        this.seekMap = seekMap2;
    }

    public void drmInitData(DrmInitData drmInitData2) {
        this.drmInitData = drmInitData2;
    }

    public void format(MediaFormat mediaFormat2) {
        this.mediaFormat = mediaFormat2;
    }

    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        throw new IllegalStateException("Unexpected sample data in initialization chunk");
    }

    public void sampleData(ParsableByteArray data, int length) {
        throw new IllegalStateException("Unexpected sample data in initialization chunk");
    }

    public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
        throw new IllegalStateException("Unexpected sample data in initialization chunk");
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public void load() throws IOException, InterruptedException {
        ExtractorInput input;
        DataSpec loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
        try {
            input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (this.bytesLoaded == 0) {
                this.extractorWrapper.init(this);
            }
            int result = 0;
            while (result == 0) {
                if (!this.loadCanceled) {
                    result = this.extractorWrapper.read(input);
                }
            }
            this.bytesLoaded = (int) (input.getPosition() - this.dataSpec.absoluteStreamPosition);
            this.dataSource.close();
        } catch (Throwable th) {
            this.dataSource.close();
            throw th;
        }
    }
}
