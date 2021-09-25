package com.google.android.exoplayer.hls;

import android.util.SparseArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.extractor.DefaultTrackOutput;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import java.io.IOException;

public final class HlsExtractorWrapper implements ExtractorOutput {
    private final int adaptiveMaxHeight;
    private final int adaptiveMaxWidth;
    private Allocator allocator;
    private final Extractor extractor;
    public final Format format;
    private boolean prepared;
    private MediaFormat[] sampleQueueFormats;
    private final SparseArray<DefaultTrackOutput> sampleQueues = new SparseArray<>();
    private final boolean shouldSpliceIn;
    private boolean spliceConfigured;
    public final long startTimeUs;
    private volatile boolean tracksBuilt;
    public final int trigger;

    public HlsExtractorWrapper(int trigger2, Format format2, long startTimeUs2, Extractor extractor2, boolean shouldSpliceIn2, int adaptiveMaxWidth2, int adaptiveMaxHeight2) {
        this.trigger = trigger2;
        this.format = format2;
        this.startTimeUs = startTimeUs2;
        this.extractor = extractor2;
        this.shouldSpliceIn = shouldSpliceIn2;
        this.adaptiveMaxWidth = adaptiveMaxWidth2;
        this.adaptiveMaxHeight = adaptiveMaxHeight2;
    }

    public void init(Allocator allocator2) {
        this.allocator = allocator2;
        this.extractor.init(this);
    }

    public boolean isPrepared() {
        if (!this.prepared && this.tracksBuilt) {
            for (int i = 0; i < this.sampleQueues.size(); i++) {
                if (!this.sampleQueues.valueAt(i).hasFormat()) {
                    return false;
                }
            }
            this.prepared = true;
            this.sampleQueueFormats = new MediaFormat[this.sampleQueues.size()];
            for (int i2 = 0; i2 < this.sampleQueueFormats.length; i2++) {
                MediaFormat format2 = this.sampleQueues.valueAt(i2).getFormat();
                if (MimeTypes.isVideo(format2.mimeType) && !(this.adaptiveMaxWidth == -1 && this.adaptiveMaxHeight == -1)) {
                    format2 = format2.copyWithMaxVideoDimensions(this.adaptiveMaxWidth, this.adaptiveMaxHeight);
                }
                this.sampleQueueFormats[i2] = format2;
            }
        }
        return this.prepared;
    }

    public void clear() {
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            this.sampleQueues.valueAt(i).clear();
        }
    }

    public long getLargestParsedTimestampUs() {
        long largestParsedTimestampUs = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            largestParsedTimestampUs = Math.max(largestParsedTimestampUs, this.sampleQueues.valueAt(i).getLargestParsedTimestampUs());
        }
        return largestParsedTimestampUs;
    }

    public final void configureSpliceTo(HlsExtractorWrapper nextExtractor) {
        Assertions.checkState(isPrepared());
        if (!this.spliceConfigured && nextExtractor.shouldSpliceIn && nextExtractor.isPrepared()) {
            boolean spliceConfigured2 = true;
            int trackCount = getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                spliceConfigured2 &= this.sampleQueues.valueAt(i).configureSpliceTo(nextExtractor.sampleQueues.valueAt(i));
            }
            this.spliceConfigured = spliceConfigured2;
        }
    }

    public int getTrackCount() {
        Assertions.checkState(isPrepared());
        return this.sampleQueues.size();
    }

    public MediaFormat getMediaFormat(int track) {
        Assertions.checkState(isPrepared());
        return this.sampleQueueFormats[track];
    }

    public boolean getSample(int track, SampleHolder holder) {
        Assertions.checkState(isPrepared());
        return this.sampleQueues.valueAt(track).getSample(holder);
    }

    public void discardUntil(int track, long timeUs) {
        Assertions.checkState(isPrepared());
        this.sampleQueues.valueAt(track).discardUntil(timeUs);
    }

    public boolean hasSamples(int track) {
        Assertions.checkState(isPrepared());
        return !this.sampleQueues.valueAt(track).isEmpty();
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

    public long getAdjustedEndTimeUs() {
        long largestAdjustedPtsParsed = Long.MIN_VALUE;
        for (int i = 0; i < this.sampleQueues.size(); i++) {
            largestAdjustedPtsParsed = Math.max(largestAdjustedPtsParsed, this.sampleQueues.valueAt(i).getLargestParsedTimestampUs());
        }
        return largestAdjustedPtsParsed;
    }

    public TrackOutput track(int id) {
        DefaultTrackOutput sampleQueue = new DefaultTrackOutput(this.allocator);
        this.sampleQueues.put(id, sampleQueue);
        return sampleQueue;
    }

    public void endTracks() {
        this.tracksBuilt = true;
    }

    public void seekMap(SeekMap seekMap) {
    }

    public void drmInitData(DrmInitData drmInit) {
    }
}
