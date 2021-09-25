package com.google.android.exoplayer.text.subrip;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.Collections;
import java.util.List;

final class SubripSubtitle implements Subtitle {
    private final long[] cueTimesUs;
    private final Cue[] cues;

    public SubripSubtitle(Cue[] cues2, long[] cueTimesUs2) {
        this.cues = cues2;
        this.cueTimesUs = cueTimesUs2;
    }

    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.cueTimesUs, timeUs, false, false);
        if (index < this.cueTimesUs.length) {
            return index;
        }
        return -1;
    }

    public int getEventTimeCount() {
        return this.cueTimesUs.length;
    }

    public long getEventTime(int index) {
        boolean z;
        boolean z2 = true;
        if (index >= 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkArgument(z);
        if (index >= this.cueTimesUs.length) {
            z2 = false;
        }
        Assertions.checkArgument(z2);
        return this.cueTimesUs[index];
    }

    public long getLastEventTime() {
        if (getEventTimeCount() == 0) {
            return -1;
        }
        return this.cueTimesUs[this.cueTimesUs.length - 1];
    }

    public List<Cue> getCues(long timeUs) {
        int index = Util.binarySearchFloor(this.cueTimesUs, timeUs, true, false);
        if (index == -1 || this.cues[index] == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(this.cues[index]);
    }
}
