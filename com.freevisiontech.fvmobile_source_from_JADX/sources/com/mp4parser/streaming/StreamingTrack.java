package com.mp4parser.streaming;

import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import java.util.concurrent.BlockingQueue;

public interface StreamingTrack {
    void addTrackExtension(TrackExtension trackExtension);

    String getHandler();

    String getLanguage();

    SampleDescriptionBox getSampleDescriptionBox();

    BlockingQueue<StreamingSample> getSamples();

    long getTimescale();

    <T extends TrackExtension> T getTrackExtension(Class<T> cls);

    TrackHeaderBox getTrackHeaderBox();

    boolean hasMoreSamples();

    void removeTrackExtension(Class<? extends TrackExtension> cls);
}
