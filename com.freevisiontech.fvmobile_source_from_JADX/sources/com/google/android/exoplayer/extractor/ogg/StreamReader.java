package com.google.android.exoplayer.extractor.ogg;

import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

abstract class StreamReader {
    protected ExtractorOutput extractorOutput;
    protected final OggParser oggParser = new OggParser();
    protected final ParsableByteArray scratch = new ParsableByteArray(new byte[65025], 0);
    protected TrackOutput trackOutput;

    /* access modifiers changed from: package-private */
    public abstract int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException;

    StreamReader() {
    }

    /* access modifiers changed from: package-private */
    public void init(ExtractorOutput output, TrackOutput trackOutput2) {
        this.extractorOutput = output;
        this.trackOutput = trackOutput2;
    }

    /* access modifiers changed from: package-private */
    public void seek() {
        this.oggParser.reset();
        this.scratch.reset();
    }
}
