package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer.extractor.ts.ElementaryStreamReader */
abstract class ElementaryStreamReader {
    protected final TrackOutput output;

    public abstract void consume(ParsableByteArray parsableByteArray);

    public abstract void packetFinished();

    public abstract void packetStarted(long j, boolean z);

    public abstract void seek();

    protected ElementaryStreamReader(TrackOutput output2) {
        this.output = output2;
    }
}
