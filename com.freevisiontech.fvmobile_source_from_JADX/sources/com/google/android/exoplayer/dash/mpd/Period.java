package com.google.android.exoplayer.dash.mpd;

import java.util.Collections;
import java.util.List;

public class Period {
    public final List<AdaptationSet> adaptationSets;

    /* renamed from: id */
    public final String f1194id;
    public final long startMs;

    public Period(String id, long start, List<AdaptationSet> adaptationSets2) {
        this.f1194id = id;
        this.startMs = start;
        this.adaptationSets = Collections.unmodifiableList(adaptationSets2);
    }

    public int getAdaptationSetIndex(int type) {
        int adaptationCount = this.adaptationSets.size();
        for (int i = 0; i < adaptationCount; i++) {
            if (this.adaptationSets.get(i).type == type) {
                return i;
            }
        }
        return -1;
    }
}
