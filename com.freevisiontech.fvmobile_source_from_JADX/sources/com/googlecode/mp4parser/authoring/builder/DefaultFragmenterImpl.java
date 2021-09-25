package com.googlecode.mp4parser.authoring.builder;

import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.util.Mp4Arrays;
import java.util.Arrays;

public class DefaultFragmenterImpl implements Fragmenter {
    private double fragmentLength = 2.0d;

    public DefaultFragmenterImpl(double fragmentLength2) {
        this.fragmentLength = fragmentLength2;
    }

    public long[] sampleNumbers(Track track) {
        long[] segmentStartSamples = {1};
        long[] sampleDurations = track.getSampleDurations();
        long[] syncSamples = track.getSyncSamples();
        long timescale = track.getTrackMetaData().getTimescale();
        double time = 0.0d;
        for (int i = 0; i < sampleDurations.length; i++) {
            time += ((double) sampleDurations[i]) / ((double) timescale);
            if (time >= this.fragmentLength && (syncSamples == null || Arrays.binarySearch(syncSamples, (long) (i + 1)) >= 0)) {
                if (i > 0) {
                    segmentStartSamples = Mp4Arrays.copyOfAndAppend(segmentStartSamples, (long) (i + 1));
                }
                time = 0.0d;
            }
        }
        if (time >= this.fragmentLength || segmentStartSamples.length <= 1) {
            return segmentStartSamples;
        }
        long[] nuSegmentStartSamples = new long[(segmentStartSamples.length - 1)];
        System.arraycopy(segmentStartSamples, 0, nuSegmentStartSamples, 0, segmentStartSamples.length - 1);
        return nuSegmentStartSamples;
    }
}
