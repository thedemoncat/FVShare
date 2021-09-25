package com.google.android.exoplayer.text.webvtt;

import android.text.SpannableStringBuilder;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class WebvttSubtitle implements Subtitle {
    private final long[] cueTimesUs = new long[(this.numCues * 2)];
    private final List<WebvttCue> cues;
    private final int numCues;
    private final long[] sortedCueTimesUs;

    public WebvttSubtitle(List<WebvttCue> cues2) {
        this.cues = cues2;
        this.numCues = cues2.size();
        for (int cueIndex = 0; cueIndex < this.numCues; cueIndex++) {
            WebvttCue cue = cues2.get(cueIndex);
            int arrayIndex = cueIndex * 2;
            this.cueTimesUs[arrayIndex] = cue.startTime;
            this.cueTimesUs[arrayIndex + 1] = cue.endTime;
        }
        this.sortedCueTimesUs = Arrays.copyOf(this.cueTimesUs, this.cueTimesUs.length);
        Arrays.sort(this.sortedCueTimesUs);
    }

    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.sortedCueTimesUs, timeUs, false, false);
        if (index < this.sortedCueTimesUs.length) {
            return index;
        }
        return -1;
    }

    public int getEventTimeCount() {
        return this.sortedCueTimesUs.length;
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
        if (index >= this.sortedCueTimesUs.length) {
            z2 = false;
        }
        Assertions.checkArgument(z2);
        return this.sortedCueTimesUs[index];
    }

    public long getLastEventTime() {
        if (getEventTimeCount() == 0) {
            return -1;
        }
        return this.sortedCueTimesUs[this.sortedCueTimesUs.length - 1];
    }

    public List<Cue> getCues(long timeUs) {
        ArrayList<Cue> list = null;
        WebvttCue firstNormalCue = null;
        SpannableStringBuilder normalCueTextBuilder = null;
        for (int i = 0; i < this.numCues; i++) {
            if (this.cueTimesUs[i * 2] <= timeUs && timeUs < this.cueTimesUs[(i * 2) + 1]) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                WebvttCue cue = this.cues.get(i);
                if (!cue.isNormalCue()) {
                    list.add(cue);
                } else if (firstNormalCue == null) {
                    firstNormalCue = cue;
                } else if (normalCueTextBuilder == null) {
                    normalCueTextBuilder = new SpannableStringBuilder();
                    normalCueTextBuilder.append(firstNormalCue.text).append("\n").append(cue.text);
                } else {
                    normalCueTextBuilder.append("\n").append(cue.text);
                }
            }
        }
        if (normalCueTextBuilder != null) {
            list.add(new WebvttCue(normalCueTextBuilder));
        } else if (firstNormalCue != null) {
            list.add(firstNormalCue);
        }
        return list != null ? list : Collections.emptyList();
    }
}
