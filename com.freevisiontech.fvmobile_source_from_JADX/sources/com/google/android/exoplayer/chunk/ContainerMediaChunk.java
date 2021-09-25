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

public class ContainerMediaChunk extends BaseMediaChunk implements ChunkExtractorWrapper.SingleTrackOutput {
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private volatile int bytesLoaded;
    private DrmInitData drmInitData;
    private final ChunkExtractorWrapper extractorWrapper;
    private volatile boolean loadCanceled;
    private MediaFormat mediaFormat;
    private final long sampleOffsetUs;

    public ContainerMediaChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, long sampleOffsetUs2, ChunkExtractorWrapper extractorWrapper2, MediaFormat mediaFormat2, int adaptiveMaxWidth2, int adaptiveMaxHeight2, DrmInitData drmInitData2, boolean isMediaFormatFinal, int parentId) {
        super(dataSource, dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex, isMediaFormatFinal, parentId);
        this.extractorWrapper = extractorWrapper2;
        this.sampleOffsetUs = sampleOffsetUs2;
        this.adaptiveMaxWidth = adaptiveMaxWidth2;
        this.adaptiveMaxHeight = adaptiveMaxHeight2;
        this.mediaFormat = getAdjustedMediaFormat(mediaFormat2, sampleOffsetUs2, adaptiveMaxWidth2, adaptiveMaxHeight2);
        this.drmInitData = drmInitData2;
    }

    public final long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public final MediaFormat getMediaFormat() {
        return this.mediaFormat;
    }

    public final DrmInitData getDrmInitData() {
        return this.drmInitData;
    }

    public final void seekMap(SeekMap seekMap) {
    }

    public final void drmInitData(DrmInitData drmInitData2) {
        this.drmInitData = drmInitData2;
    }

    public final void format(MediaFormat mediaFormat2) {
        this.mediaFormat = getAdjustedMediaFormat(mediaFormat2, this.sampleOffsetUs, this.adaptiveMaxWidth, this.adaptiveMaxHeight);
    }

    public final int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        return getOutput().sampleData(input, length, allowEndOfInput);
    }

    public final void sampleData(ParsableByteArray data, int length) {
        getOutput().sampleData(data, length);
    }

    public final void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
        getOutput().sampleMetadata(this.sampleOffsetUs + timeUs, flags, size, offset, encryptionKey);
    }

    public final void cancelLoad() {
        this.loadCanceled = true;
    }

    public final boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public final void load() throws IOException, InterruptedException {
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

    private static MediaFormat getAdjustedMediaFormat(MediaFormat format, long sampleOffsetUs2, int adaptiveMaxWidth2, int adaptiveMaxHeight2) {
        if (format == null) {
            return null;
        }
        if (!(sampleOffsetUs2 == 0 || format.subsampleOffsetUs == Long.MAX_VALUE)) {
            format = format.copyWithSubsampleOffsetUs(format.subsampleOffsetUs + sampleOffsetUs2);
        }
        if (adaptiveMaxWidth2 == -1 && adaptiveMaxHeight2 == -1) {
            return format;
        }
        return format.copyWithMaxVideoDimensions(adaptiveMaxWidth2, adaptiveMaxHeight2);
    }
}
