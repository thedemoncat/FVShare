package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.MediaChunk;
import com.google.android.exoplayer.extractor.DefaultExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public final class TsChunk extends MediaChunk {
    private long adjustedEndTimeUs;
    private int bytesLoaded;
    public final int discontinuitySequenceNumber;
    public final HlsExtractorWrapper extractorWrapper;
    private final boolean isEncrypted = (this.dataSource instanceof Aes128DataSource);
    private volatile boolean loadCanceled;

    public TsChunk(DataSource dataSource, DataSpec dataSpec, int trigger, Format format, long startTimeUs, long endTimeUs, int chunkIndex, int discontinuitySequenceNumber2, HlsExtractorWrapper extractorWrapper2, byte[] encryptionKey, byte[] encryptionIv) {
        super(buildDataSource(dataSource, encryptionKey, encryptionIv), dataSpec, trigger, format, startTimeUs, endTimeUs, chunkIndex);
        this.discontinuitySequenceNumber = discontinuitySequenceNumber2;
        this.extractorWrapper = extractorWrapper2;
        this.adjustedEndTimeUs = startTimeUs;
    }

    public long bytesLoaded() {
        return (long) this.bytesLoaded;
    }

    public void cancelLoad() {
        this.loadCanceled = true;
    }

    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    public void load() throws IOException, InterruptedException {
        DataSpec loadDataSpec;
        boolean skipLoadedBytes;
        ExtractorInput input;
        if (this.isEncrypted) {
            loadDataSpec = this.dataSpec;
            skipLoadedBytes = this.bytesLoaded != 0;
        } else {
            loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
            skipLoadedBytes = false;
        }
        try {
            input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (skipLoadedBytes) {
                input.skipFully(this.bytesLoaded);
            }
            int result = 0;
            while (result == 0) {
                if (!this.loadCanceled) {
                    result = this.extractorWrapper.read(input);
                }
            }
            long tsChunkEndTimeUs = this.extractorWrapper.getAdjustedEndTimeUs();
            if (tsChunkEndTimeUs != Long.MIN_VALUE) {
                this.adjustedEndTimeUs = tsChunkEndTimeUs;
            }
            this.bytesLoaded = (int) (input.getPosition() - this.dataSpec.absoluteStreamPosition);
            this.dataSource.close();
        } catch (Throwable th) {
            this.dataSource.close();
            throw th;
        }
    }

    public long getAdjustedEndTimeUs() {
        return this.adjustedEndTimeUs;
    }

    private static DataSource buildDataSource(DataSource dataSource, byte[] encryptionKey, byte[] encryptionIv) {
        return (encryptionKey == null || encryptionIv == null) ? dataSource : new Aes128DataSource(dataSource, encryptionKey, encryptionIv);
    }
}
