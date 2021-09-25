package com.google.android.exoplayer.dash.mpd;

import java.util.Collections;
import java.util.List;

public class AdaptationSet {
    public static final int TYPE_AUDIO = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_UNKNOWN = -1;
    public static final int TYPE_VIDEO = 0;
    public final List<ContentProtection> contentProtections;

    /* renamed from: id */
    public final int f1193id;
    public final List<Representation> representations;
    public final int type;

    public AdaptationSet(int id, int type2, List<Representation> representations2, List<ContentProtection> contentProtections2) {
        this.f1193id = id;
        this.type = type2;
        this.representations = Collections.unmodifiableList(representations2);
        if (contentProtections2 == null) {
            this.contentProtections = Collections.emptyList();
        } else {
            this.contentProtections = Collections.unmodifiableList(contentProtections2);
        }
    }

    public AdaptationSet(int id, int type2, List<Representation> representations2) {
        this(id, type2, representations2, (List<ContentProtection>) null);
    }

    public boolean hasContentProtection() {
        return !this.contentProtections.isEmpty();
    }
}
