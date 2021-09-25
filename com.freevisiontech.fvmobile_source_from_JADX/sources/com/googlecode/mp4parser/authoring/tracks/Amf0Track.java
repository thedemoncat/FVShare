package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.SampleImpl;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.adobe.ActionMessageFormat0SampleEntryBox;
import com.lzy.okgo.cache.CacheEntity;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Amf0Track extends AbstractTrack {
    SortedMap<Long, byte[]> rawSamples = new TreeMap<Long, byte[]>() {
    };
    private TrackMetaData trackMetaData = new TrackMetaData();

    public Amf0Track(Map<Long, byte[]> rawSamples2) {
        super(ActionMessageFormat0SampleEntryBox.TYPE);
        this.rawSamples = new TreeMap(rawSamples2);
        this.trackMetaData.setCreationTime(new Date());
        this.trackMetaData.setModificationTime(new Date());
        this.trackMetaData.setTimescale(1000);
        this.trackMetaData.setLanguage("eng");
    }

    public List<Sample> getSamples() {
        LinkedList<Sample> samples = new LinkedList<>();
        for (byte[] bytes : this.rawSamples.values()) {
            samples.add(new SampleImpl(ByteBuffer.wrap(bytes)));
        }
        return samples;
    }

    public void close() throws IOException {
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        SampleDescriptionBox stsd = new SampleDescriptionBox();
        ActionMessageFormat0SampleEntryBox amf0 = new ActionMessageFormat0SampleEntryBox();
        amf0.setDataReferenceIndex(1);
        stsd.addBox(amf0);
        return stsd;
    }

    public long[] getSampleDurations() {
        LinkedList<Long> keys = new LinkedList<>(this.rawSamples.keySet());
        Collections.sort(keys);
        long[] rc = new long[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            rc[i] = keys.get(i).longValue() - 0;
        }
        return rc;
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

    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    public String getHandler() {
        return CacheEntity.DATA;
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return null;
    }
}
