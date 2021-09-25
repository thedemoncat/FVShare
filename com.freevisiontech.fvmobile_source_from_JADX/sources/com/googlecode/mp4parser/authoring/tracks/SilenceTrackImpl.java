package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.googlecode.mp4parser.authoring.Edit;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.util.CastUtils;
import com.umeng.analytics.pro.C0217dk;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SilenceTrackImpl implements Track {
    long[] decodingTimes;
    String name;
    List<Sample> samples = new LinkedList();
    Track source;

    public SilenceTrackImpl(Track ofType, long ms) {
        this.source = ofType;
        this.name = ms + "ms silence";
        if (AudioSampleEntry.TYPE3.equals(ofType.getSampleDescriptionBox().getSampleEntry().getType())) {
            int numFrames = CastUtils.l2i(((getTrackMetaData().getTimescale() * ms) / 1000) / 1024);
            this.decodingTimes = new long[numFrames];
            Arrays.fill(this.decodingTimes, ((getTrackMetaData().getTimescale() * ms) / ((long) numFrames)) / 1000);
            while (true) {
                int numFrames2 = numFrames;
                numFrames = numFrames2 - 1;
                if (numFrames2 > 0) {
                    this.samples.add(new SampleImpl((ByteBuffer) ByteBuffer.wrap(new byte[]{ClosedCaptionCtrl.BACKSPACE, C0217dk.f724n, 4, 96, -116, ClosedCaptionCtrl.MISC_CHAN_2}).rewind()));
                } else {
                    return;
                }
            }
        } else {
            throw new RuntimeException("Tracks of type " + ofType.getClass().getSimpleName() + " are not supported");
        }
    }

    public void close() throws IOException {
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.source.getSampleDescriptionBox();
    }

    public long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public long getDuration() {
        long duration = 0;
        for (long delta : this.decodingTimes) {
            duration += delta;
        }
        return duration;
    }

    public TrackMetaData getTrackMetaData() {
        return this.source.getTrackMetaData();
    }

    public String getHandler() {
        return this.source.getHandler();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return null;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return null;
    }

    public long[] getSyncSamples() {
        return null;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return null;
    }

    public String getName() {
        return this.name;
    }

    public List<Edit> getEdits() {
        return null;
    }

    public Map<GroupEntry, long[]> getSampleGroups() {
        return this.source.getSampleGroups();
    }
}
