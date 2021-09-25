package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.C1907C;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

/* renamed from: com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster */
public final class PtsTimestampAdjuster {
    public static final long DO_NOT_OFFSET = Long.MAX_VALUE;
    private static final long MAX_PTS_PLUS_ONE = 8589934592L;
    private final long firstSampleTimestampUs;
    private volatile long lastPts = Long.MIN_VALUE;
    private long timestampOffsetUs;

    public PtsTimestampAdjuster(long firstSampleTimestampUs2) {
        this.firstSampleTimestampUs = firstSampleTimestampUs2;
    }

    public void reset() {
        this.lastPts = Long.MIN_VALUE;
    }

    public boolean isInitialized() {
        return this.lastPts != Long.MIN_VALUE;
    }

    public long adjustTimestamp(long pts) {
        if (this.lastPts != Long.MIN_VALUE) {
            long closestWrapCount = (this.lastPts + IjkMediaMeta.AV_CH_WIDE_RIGHT) / 8589934592L;
            long ptsWrapBelow = pts + (8589934592L * (closestWrapCount - 1));
            long ptsWrapAbove = pts + (8589934592L * closestWrapCount);
            if (Math.abs(ptsWrapBelow - this.lastPts) < Math.abs(ptsWrapAbove - this.lastPts)) {
                pts = ptsWrapBelow;
            } else {
                pts = ptsWrapAbove;
            }
        }
        long timeUs = ptsToUs(pts);
        if (this.firstSampleTimestampUs != Long.MAX_VALUE && this.lastPts == Long.MIN_VALUE) {
            this.timestampOffsetUs = this.firstSampleTimestampUs - timeUs;
        }
        this.lastPts = pts;
        return this.timestampOffsetUs + timeUs;
    }

    public static long ptsToUs(long pts) {
        return (C1907C.MICROS_PER_SECOND * pts) / 90000;
    }

    public static long usToPts(long us) {
        return (90000 * us) / C1907C.MICROS_PER_SECOND;
    }
}
