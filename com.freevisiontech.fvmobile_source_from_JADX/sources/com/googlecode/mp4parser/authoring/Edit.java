package com.googlecode.mp4parser.authoring;

public class Edit {
    private double mediaRate;
    private long mediaTime;
    private double segmentDuration;
    private long timeScale;

    public Edit(long mediaTime2, long timeScale2, double mediaRate2, double segmentDurationInMs) {
        this.timeScale = timeScale2;
        this.segmentDuration = segmentDurationInMs;
        this.mediaTime = mediaTime2;
        this.mediaRate = mediaRate2;
    }

    public long getTimeScale() {
        return this.timeScale;
    }

    public double getSegmentDuration() {
        return this.segmentDuration;
    }

    public long getMediaTime() {
        return this.mediaTime;
    }

    public double getMediaRate() {
        return this.mediaRate;
    }
}
