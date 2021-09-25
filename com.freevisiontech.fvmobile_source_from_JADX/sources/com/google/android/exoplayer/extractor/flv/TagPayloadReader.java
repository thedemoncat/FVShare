package com.google.android.exoplayer.extractor.flv;

import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.ParsableByteArray;

abstract class TagPayloadReader {
    private long durationUs = -1;
    protected final TrackOutput output;

    /* access modifiers changed from: protected */
    public abstract boolean parseHeader(ParsableByteArray parsableByteArray) throws ParserException;

    /* access modifiers changed from: protected */
    public abstract void parsePayload(ParsableByteArray parsableByteArray, long j) throws ParserException;

    public abstract void seek();

    public static final class UnsupportedFormatException extends ParserException {
        public UnsupportedFormatException(String msg) {
            super(msg);
        }
    }

    protected TagPayloadReader(TrackOutput output2) {
        this.output = output2;
    }

    public final void setDurationUs(long durationUs2) {
        this.durationUs = durationUs2;
    }

    public final long getDurationUs() {
        return this.durationUs;
    }

    public final void consume(ParsableByteArray data, long timeUs) throws ParserException {
        if (parseHeader(data)) {
            parsePayload(data, timeUs);
        }
    }
}
