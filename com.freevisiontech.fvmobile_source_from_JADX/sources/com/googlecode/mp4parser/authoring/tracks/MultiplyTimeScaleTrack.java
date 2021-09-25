package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiplyTimeScaleTrack implements Track {
    Track source;
    private int timeScaleFactor;

    public MultiplyTimeScaleTrack(Track source2, int timeScaleFactor2) {
        this.source = source2;
        this.timeScaleFactor = timeScaleFactor2;
    }

    static List<CompositionTimeToSample.Entry> adjustCtts(List<CompositionTimeToSample.Entry> source2, int timeScaleFactor2) {
        if (source2 == null) {
            return null;
        }
        List<CompositionTimeToSample.Entry> entries2 = new ArrayList<>(source2.size());
        for (CompositionTimeToSample.Entry entry : source2) {
            entries2.add(new CompositionTimeToSample.Entry(entry.getCount(), entry.getOffset() * timeScaleFactor2));
        }
        return entries2;
    }

    public void close() throws IOException {
        this.source.close();
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.source.getSampleDescriptionBox();
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return adjustCtts(this.source.getCompositionTimeEntries(), this.timeScaleFactor);
    }

    public long[] getSyncSamples() {
        return this.source.getSyncSamples();
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.source.getSampleDependencies();
    }

    public TrackMetaData getTrackMetaData() {
        TrackMetaData trackMetaData = (TrackMetaData) this.source.getTrackMetaData().clone();
        trackMetaData.setTimescale(this.source.getTrackMetaData().getTimescale() * ((long) this.timeScaleFactor));
        return trackMetaData;
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return this.source.getSamples();
    }

    public long[] getSampleDurations() {
        long[] scaled = new long[this.source.getSampleDurations().length];
        for (int i = 0; i < this.source.getSampleDurations().length; i++) {
            scaled[i] = this.source.getSampleDurations()[i] * ((long) this.timeScaleFactor);
        }
        return scaled;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.source.getSubsampleInformationBox();
    }

    public long getDuration() {
        return this.source.getDuration() * ((long) this.timeScaleFactor);
    }

    public String toString() {
        return "MultiplyTimeScaleTrack{source=" + this.source + '}';
    }

    public String getName() {
        return "timscale(" + this.source.getName() + ")";
    }

    public List<Edit> getEdits() {
        return this.source.getEdits();
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.source.getSampleGroups();
    }
}
