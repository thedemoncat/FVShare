package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.TrackExtension;

public class TrackIdTrackExtension implements TrackExtension {
    private long trackId = 1;

    public TrackIdTrackExtension(long trackId2) {
        this.trackId = trackId2;
    }

    public long getTrackId() {
        return this.trackId;
    }
}
