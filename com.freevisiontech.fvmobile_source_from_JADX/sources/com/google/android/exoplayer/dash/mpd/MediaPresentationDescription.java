package com.google.android.exoplayer.dash.mpd;

import com.google.android.exoplayer.util.ManifestFetcher;
import java.util.Collections;
import java.util.List;

public class MediaPresentationDescription implements ManifestFetcher.RedirectingManifest {
    public final long availabilityStartTime;
    public final long duration;
    public final boolean dynamic;
    public final String location;
    public final long minBufferTime;
    public final long minUpdatePeriod;
    private final List<Period> periods;
    public final long timeShiftBufferDepth;
    public final UtcTimingElement utcTiming;

    public MediaPresentationDescription(long availabilityStartTime2, long duration2, long minBufferTime2, boolean dynamic2, long minUpdatePeriod2, long timeShiftBufferDepth2, UtcTimingElement utcTiming2, String location2, List<Period> periods2) {
        this.availabilityStartTime = availabilityStartTime2;
        this.duration = duration2;
        this.minBufferTime = minBufferTime2;
        this.dynamic = dynamic2;
        this.minUpdatePeriod = minUpdatePeriod2;
        this.timeShiftBufferDepth = timeShiftBufferDepth2;
        this.utcTiming = utcTiming2;
        this.location = location2;
        this.periods = periods2 == null ? Collections.emptyList() : periods2;
    }

    public final String getNextManifestUri() {
        return this.location;
    }

    public final int getPeriodCount() {
        return this.periods.size();
    }

    public final Period getPeriod(int index) {
        return this.periods.get(index);
    }

    public final long getPeriodDuration(int index) {
        if (index != this.periods.size() - 1) {
            return this.periods.get(index + 1).startMs - this.periods.get(index).startMs;
        }
        if (this.duration == -1) {
            return -1;
        }
        return this.duration - this.periods.get(index).startMs;
    }
}
