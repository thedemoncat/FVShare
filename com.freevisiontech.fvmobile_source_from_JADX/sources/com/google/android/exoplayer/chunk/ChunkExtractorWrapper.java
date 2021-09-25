package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

public class ChunkExtractorWrapper implements ExtractorOutput, TrackOutput {
    private final Extractor extractor;
    private boolean extractorInitialized;
    private SingleTrackOutput output;
    private boolean seenTrack;

    public interface SingleTrackOutput extends TrackOutput {
        void drmInitData(DrmInitData drmInitData);

        void seekMap(SeekMap seekMap);
    }

    public ChunkExtractorWrapper(Extractor extractor2) {
        this.extractor = extractor2;
    }

    public void init(SingleTrackOutput output2) {
        this.output = output2;
        if (!this.extractorInitialized) {
            this.extractor.init(this);
            this.extractorInitialized = true;
            return;
        }
        this.extractor.seek();
    }

    public int read(ExtractorInput input) throws IOException, InterruptedException {
        boolean z = true;
        int result = this.extractor.read(input, (PositionHolder) null);
        if (result == 1) {
            z = false;
        }
        Assertions.checkState(z);
        return result;
    }

    public TrackOutput track(int id) {
        Assertions.checkState(!this.seenTrack);
        this.seenTrack = true;
        return this;
    }

    public void endTracks() {
        Assertions.checkState(this.seenTrack);
    }

    public void seekMap(SeekMap seekMap) {
        this.output.seekMap(seekMap);
    }

    public void drmInitData(DrmInitData drmInitData) {
        this.output.drmInitData(drmInitData);
    }

    public void format(MediaFormat format) {
        this.output.format(format);
    }

    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        return this.output.sampleData(input, length, allowEndOfInput);
    }

    public void sampleData(ParsableByteArray data, int length) {
        this.output.sampleData(data, length);
    }

    public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
        this.output.sampleMetadata(timeUs, flags, size, offset, encryptionKey);
    }
}
