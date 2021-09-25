package com.google.android.exoplayer.text.ttml;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class TtmlSubtitle implements Subtitle {
    private final long[] eventTimesUs;
    private final Map<String, TtmlStyle> globalStyles;
    private final Map<String, TtmlRegion> regionMap;
    private final TtmlNode root;

    public TtmlSubtitle(TtmlNode root2, Map<String, TtmlStyle> globalStyles2, Map<String, TtmlRegion> regionMap2) {
        this.root = root2;
        this.regionMap = regionMap2;
        this.globalStyles = globalStyles2 != null ? Collections.unmodifiableMap(globalStyles2) : Collections.emptyMap();
        this.eventTimesUs = root2.getEventTimesUs();
    }

    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.eventTimesUs, timeUs, false, false);
        if (index < this.eventTimesUs.length) {
            return index;
        }
        return -1;
    }

    public int getEventTimeCount() {
        return this.eventTimesUs.length;
    }

    public long getEventTime(int index) {
        return this.eventTimesUs[index];
    }

    public long getLastEventTime() {
        if (this.eventTimesUs.length == 0) {
            return -1;
        }
        return this.eventTimesUs[this.eventTimesUs.length - 1];
    }

    /* access modifiers changed from: package-private */
    public TtmlNode getRoot() {
        return this.root;
    }

    public List<Cue> getCues(long timeUs) {
        return this.root.getCues(timeUs, this.globalStyles, this.regionMap);
    }

    /* access modifiers changed from: package-private */
    public Map<String, TtmlStyle> getGlobalStyles() {
        return this.globalStyles;
    }
}
