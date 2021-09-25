package com.google.android.exoplayer.dash.mpd;

import com.google.android.exoplayer.dash.DashSegmentIndex;

final class DashSingleSegmentIndex implements DashSegmentIndex {
    private final RangedUri uri;

    public DashSingleSegmentIndex(RangedUri uri2) {
        this.uri = uri2;
    }

    public int getSegmentNum(long timeUs, long periodDurationUs) {
        return 0;
    }

    public long getTimeUs(int segmentNum) {
        return 0;
    }

    public long getDurationUs(int segmentNum, long periodDurationUs) {
        return periodDurationUs;
    }

    public RangedUri getSegmentUrl(int segmentNum) {
        return this.uri;
    }

    public int getFirstSegmentNum() {
        return 0;
    }

    public int getLastSegmentNum(long periodDurationUs) {
        return 0;
    }

    public boolean isExplicit() {
        return true;
    }
}
