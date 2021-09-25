package com.mp4parser.streaming;

import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class AbstractStreamingTrack implements StreamingTrack {
    protected BlockingQueue<StreamingSample> samples = new ArrayBlockingQueue(1000);
    protected SampleDescriptionBox stsd;
    protected TrackHeaderBox tkhd = new TrackHeaderBox();
    protected HashMap<Class<? extends TrackExtension>, TrackExtension> trackExtensions = new HashMap<>();

    public AbstractStreamingTrack() {
        this.tkhd.setTrackId(1);
    }

    public BlockingQueue<StreamingSample> getSamples() {
        return this.samples;
    }

    public boolean hasMoreSamples() {
        return false;
    }

    public TrackHeaderBox getTrackHeaderBox() {
        return this.tkhd;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public <T extends TrackExtension> T getTrackExtension(Class<T> clazz) {
        return (TrackExtension) this.trackExtensions.get(clazz);
    }

    public void addTrackExtension(TrackExtension trackExtension) {
        this.trackExtensions.put(trackExtension.getClass(), trackExtension);
    }

    public void removeTrackExtension(Class<? extends TrackExtension> clazz) {
        this.trackExtensions.remove(clazz);
    }
}
