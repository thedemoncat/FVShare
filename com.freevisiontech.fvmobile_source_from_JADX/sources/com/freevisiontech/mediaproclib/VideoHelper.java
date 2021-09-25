package com.freevisiontech.mediaproclib;

import com.googlecode.mp4parser.authoring.Track;
import java.util.Arrays;

public class VideoHelper {
    public static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0.0d;
        for (long delta : track.getSampleDurations()) {
            if (Arrays.binarySearch(track.getSyncSamples(), 1 + currentSample) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), 1 + currentSample)] = currentTime;
            }
            currentTime += ((double) delta) / ((double) track.getTrackMetaData().getTimescale());
            currentSample++;
        }
        double previous = 0.0d;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                return next ? timeOfSyncSample : previous;
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }
}
