package com.googlecode.mp4parser.authoring.builder;

import com.googlecode.mp4parser.authoring.Track;

public interface Fragmenter {
    long[] sampleNumbers(Track track);
}
