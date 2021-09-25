package com.google.android.exoplayer.text.webvtt;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Assertions;
import java.util.Collections;
import java.util.List;

final class Mp4WebvttSubtitle implements Subtitle {
    private final List<Cue> cues;

    public Mp4WebvttSubtitle(List<Cue> cueList) {
        this.cues = Collections.unmodifiableList(cueList);
    }

    public int getNextEventTimeIndex(long timeUs) {
        return timeUs < 0 ? 0 : -1;
    }

    public int getEventTimeCount() {
        return 1;
    }

    public long getEventTime(int index) {
        Assertions.checkArgument(index == 0);
        return 0;
    }

    public long getLastEventTime() {
        return 0;
    }

    public List<Cue> getCues(long timeUs) {
        return timeUs >= 0 ? this.cues : Collections.emptyList();
    }
}
