package com.google.android.exoplayer.text;

import java.util.List;

final class PlayableSubtitle implements Subtitle {
    private final long offsetUs;
    public final long startTimeUs;
    private final Subtitle subtitle;

    public PlayableSubtitle(Subtitle subtitle2, boolean isRelative, long startTimeUs2, long offsetUs2) {
        this.subtitle = subtitle2;
        this.startTimeUs = startTimeUs2;
        this.offsetUs = (!isRelative ? 0 : startTimeUs2) + offsetUs2;
    }

    public int getEventTimeCount() {
        return this.subtitle.getEventTimeCount();
    }

    public long getEventTime(int index) {
        return this.subtitle.getEventTime(index) + this.offsetUs;
    }

    public long getLastEventTime() {
        return this.subtitle.getLastEventTime() + this.offsetUs;
    }

    public int getNextEventTimeIndex(long timeUs) {
        return this.subtitle.getNextEventTimeIndex(timeUs - this.offsetUs);
    }

    public List<Cue> getCues(long timeUs) {
        return this.subtitle.getCues(timeUs - this.offsetUs);
    }
}
