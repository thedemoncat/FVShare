package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;

final class ConstantBitrateSeeker implements Mp3Extractor.Seeker {
    private static final int BITS_PER_BYTE = 8;
    private final int bitrate;
    private final long durationUs;
    private final long firstFramePosition;

    public ConstantBitrateSeeker(long firstFramePosition2, int bitrate2, long inputLength) {
        long j = -1;
        this.firstFramePosition = firstFramePosition2;
        this.bitrate = bitrate2;
        this.durationUs = inputLength != -1 ? getTimeUs(inputLength) : j;
    }

    public boolean isSeekable() {
        return this.durationUs != -1;
    }

    public long getPosition(long timeUs) {
        if (this.durationUs == -1) {
            return 0;
        }
        return this.firstFramePosition + ((((long) this.bitrate) * timeUs) / 8000000);
    }

    public long getTimeUs(long position) {
        return ((Math.max(0, position - this.firstFramePosition) * C1907C.MICROS_PER_SECOND) * 8) / ((long) this.bitrate);
    }

    public long getDurationUs() {
        return this.durationUs;
    }
}
