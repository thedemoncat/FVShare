package com.mp4parser.streaming.extensions;

import com.mp4parser.streaming.TrackExtension;

public class NameTrackExtension implements TrackExtension {
    private String name;

    public static NameTrackExtension create(String name2) {
        NameTrackExtension nameTrackExtension = new NameTrackExtension();
        nameTrackExtension.name = name2;
        return nameTrackExtension;
    }

    public String getName() {
        return this.name;
    }
}
