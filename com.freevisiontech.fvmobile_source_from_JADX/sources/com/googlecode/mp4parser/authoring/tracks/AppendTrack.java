package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.SampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.mp4.AbstractDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.DecoderConfigDescriptor;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import com.googlecode.mp4parser.util.Logger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AppendTrack extends AbstractTrack {
    private static Logger LOG = Logger.getLogger(AppendTrack.class);
    long[] decodingTimes;
    List<Sample> lists;
    SampleDescriptionBox stsd;
    Track[] tracks;

    public AppendTrack(Track... tracks2) throws IOException {
        super(appendTracknames(tracks2));
        this.tracks = tracks2;
        for (Track track : tracks2) {
            if (this.stsd == null) {
                this.stsd = new SampleDescriptionBox();
                this.stsd.addBox(track.getSampleDescriptionBox().getBoxes(SampleEntry.class).get(0));
            } else {
                this.stsd = mergeStsds(this.stsd, track.getSampleDescriptionBox());
            }
        }
        this.lists = new ArrayList();
        for (Track track2 : tracks2) {
            this.lists.addAll(track2.getSamples());
        }
        int numSamples = 0;
        for (Track track3 : tracks2) {
            numSamples += track3.getSampleDurations().length;
        }
        this.decodingTimes = new long[numSamples];
        int index = 0;
        for (Track track4 : tracks2) {
            long[] durs = track4.getSampleDurations();
            System.arraycopy(durs, 0, this.decodingTimes, index, durs.length);
            index += durs.length;
        }
    }

    public static String appendTracknames(Track... tracks2) {
        String name = "";
        for (int i = 0; i < tracks2.length; i++) {
            name = String.valueOf(name) + tracks2[i].getName() + " + ";
        }
        return name.substring(0, name.length() - 3);
    }

    public void close() throws IOException {
        for (Track track : this.tracks) {
            track.close();
        }
    }

    private SampleDescriptionBox mergeStsds(SampleDescriptionBox stsd1, SampleDescriptionBox stsd2) throws IOException {
        ByteArrayOutputStream curBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream refBaos = new ByteArrayOutputStream();
        try {
            stsd1.getBox(Channels.newChannel(curBaos));
            stsd2.getBox(Channels.newChannel(refBaos));
            if (Arrays.equals(refBaos.toByteArray(), curBaos.toByteArray())) {
                return stsd1;
            }
            SampleEntry se = mergeSampleEntry(stsd1.getBoxes(SampleEntry.class).get(0), stsd2.getBoxes(SampleEntry.class).get(0));
            if (se != null) {
                stsd1.setBoxes(Collections.singletonList(se));
                return stsd1;
            }
            throw new IOException("Cannot merge " + stsd1.getBoxes(SampleEntry.class).get(0) + " and " + stsd2.getBoxes(SampleEntry.class).get(0));
        } catch (IOException e) {
            LOG.logError(e.getMessage());
            return null;
        }
    }

    private SampleEntry mergeSampleEntry(SampleEntry se1, SampleEntry se2) {
        if (!se1.getType().equals(se2.getType())) {
            return null;
        }
        if ((se1 instanceof VisualSampleEntry) && (se2 instanceof VisualSampleEntry)) {
            return mergeVisualSampleEntry((VisualSampleEntry) se1, (VisualSampleEntry) se2);
        }
        if (!(se1 instanceof AudioSampleEntry) || !(se2 instanceof AudioSampleEntry)) {
            return null;
        }
        return mergeAudioSampleEntries((AudioSampleEntry) se1, (AudioSampleEntry) se2);
    }

    private VisualSampleEntry mergeVisualSampleEntry(VisualSampleEntry vse1, VisualSampleEntry vse2) {
        VisualSampleEntry vse = new VisualSampleEntry();
        if (vse1.getHorizresolution() == vse2.getHorizresolution()) {
            vse.setHorizresolution(vse1.getHorizresolution());
            vse.setCompressorname(vse1.getCompressorname());
            if (vse1.getDepth() == vse2.getDepth()) {
                vse.setDepth(vse1.getDepth());
                if (vse1.getFrameCount() == vse2.getFrameCount()) {
                    vse.setFrameCount(vse1.getFrameCount());
                    if (vse1.getHeight() == vse2.getHeight()) {
                        vse.setHeight(vse1.getHeight());
                        if (vse1.getWidth() == vse2.getWidth()) {
                            vse.setWidth(vse1.getWidth());
                            if (vse1.getVertresolution() == vse2.getVertresolution()) {
                                vse.setVertresolution(vse1.getVertresolution());
                                if (vse1.getHorizresolution() == vse2.getHorizresolution()) {
                                    vse.setHorizresolution(vse1.getHorizresolution());
                                    if (vse1.getBoxes().size() != vse2.getBoxes().size()) {
                                        return vse;
                                    }
                                    Iterator<Box> bxs2 = vse2.getBoxes().iterator();
                                    for (Box cur1 : vse1.getBoxes()) {
                                        Box cur2 = bxs2.next();
                                        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                        try {
                                            cur1.getBox(Channels.newChannel(baos1));
                                            cur2.getBox(Channels.newChannel(baos2));
                                            if (Arrays.equals(baos1.toByteArray(), baos2.toByteArray())) {
                                                vse.addBox(cur1);
                                            } else if ((cur1 instanceof AbstractDescriptorBox) && (cur2 instanceof AbstractDescriptorBox)) {
                                                ((AbstractDescriptorBox) cur1).setDescriptor(mergeDescriptors(((AbstractDescriptorBox) cur1).getDescriptor(), ((AbstractDescriptorBox) cur2).getDescriptor()));
                                                vse.addBox(cur1);
                                            }
                                        } catch (IOException e) {
                                            LOG.logWarn(e.getMessage());
                                            return null;
                                        }
                                    }
                                    return vse;
                                }
                                LOG.logError("horizontal resolution differs");
                                return null;
                            }
                            LOG.logError("vert resolution differs");
                            return null;
                        }
                        LOG.logError("width differs");
                        return null;
                    }
                    LOG.logError("height differs");
                    return null;
                }
                LOG.logError("frame count differs");
                return null;
            }
            LOG.logError("Depth differs");
            return null;
        }
        LOG.logError("Horizontal Resolution differs");
        return null;
    }

    private AudioSampleEntry mergeAudioSampleEntries(AudioSampleEntry ase1, AudioSampleEntry ase2) {
        AudioSampleEntry ase = new AudioSampleEntry(ase2.getType());
        if (ase1.getBytesPerFrame() == ase2.getBytesPerFrame()) {
            ase.setBytesPerFrame(ase1.getBytesPerFrame());
            if (ase1.getBytesPerPacket() != ase2.getBytesPerPacket()) {
                return null;
            }
            ase.setBytesPerPacket(ase1.getBytesPerPacket());
            if (ase1.getBytesPerSample() == ase2.getBytesPerSample()) {
                ase.setBytesPerSample(ase1.getBytesPerSample());
                if (ase1.getChannelCount() != ase2.getChannelCount()) {
                    return null;
                }
                ase.setChannelCount(ase1.getChannelCount());
                if (ase1.getPacketSize() == ase2.getPacketSize()) {
                    ase.setPacketSize(ase1.getPacketSize());
                    if (ase1.getCompressionId() != ase2.getCompressionId()) {
                        return null;
                    }
                    ase.setCompressionId(ase1.getCompressionId());
                    if (ase1.getSampleRate() != ase2.getSampleRate()) {
                        return null;
                    }
                    ase.setSampleRate(ase1.getSampleRate());
                    if (ase1.getSampleSize() != ase2.getSampleSize()) {
                        return null;
                    }
                    ase.setSampleSize(ase1.getSampleSize());
                    if (ase1.getSamplesPerPacket() != ase2.getSamplesPerPacket()) {
                        return null;
                    }
                    ase.setSamplesPerPacket(ase1.getSamplesPerPacket());
                    if (ase1.getSoundVersion() != ase2.getSoundVersion()) {
                        return null;
                    }
                    ase.setSoundVersion(ase1.getSoundVersion());
                    if (!Arrays.equals(ase1.getSoundVersion2Data(), ase2.getSoundVersion2Data())) {
                        return null;
                    }
                    ase.setSoundVersion2Data(ase1.getSoundVersion2Data());
                    if (ase1.getBoxes().size() != ase2.getBoxes().size()) {
                        return ase;
                    }
                    Iterator<Box> bxs2 = ase2.getBoxes().iterator();
                    for (Box cur1 : ase1.getBoxes()) {
                        Box cur2 = bxs2.next();
                        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                        try {
                            cur1.getBox(Channels.newChannel(baos1));
                            cur2.getBox(Channels.newChannel(baos2));
                            if (Arrays.equals(baos1.toByteArray(), baos2.toByteArray())) {
                                ase.addBox(cur1);
                            } else if (ESDescriptorBox.TYPE.equals(cur1.getType()) && ESDescriptorBox.TYPE.equals(cur2.getType())) {
                                ESDescriptorBox esdsBox1 = (ESDescriptorBox) cur1;
                                esdsBox1.setDescriptor(mergeDescriptors(esdsBox1.getEsDescriptor(), ((ESDescriptorBox) cur2).getEsDescriptor()));
                                ase.addBox(cur1);
                            }
                        } catch (IOException e) {
                            LOG.logWarn(e.getMessage());
                            return null;
                        }
                    }
                    return ase;
                }
                LOG.logError("ChannelCount differ");
                return null;
            }
            LOG.logError("BytesPerSample differ");
            return null;
        }
        LOG.logError("BytesPerFrame differ");
        return null;
    }

    private ESDescriptor mergeDescriptors(BaseDescriptor des1, BaseDescriptor des2) {
        if (!(des1 instanceof ESDescriptor) || !(des2 instanceof ESDescriptor)) {
            LOG.logError("I can only merge ESDescriptors");
            return null;
        }
        ESDescriptor esds1 = (ESDescriptor) des1;
        ESDescriptor esds2 = (ESDescriptor) des2;
        if (esds1.getURLFlag() != esds2.getURLFlag()) {
            return null;
        }
        esds1.getURLLength();
        esds2.getURLLength();
        if (esds1.getDependsOnEsId() != esds2.getDependsOnEsId()) {
            return null;
        }
        if (esds1.getEsId() != esds2.getEsId()) {
            return null;
        }
        if (esds1.getoCREsId() != esds2.getoCREsId()) {
            return null;
        }
        if (esds1.getoCRstreamFlag() != esds2.getoCRstreamFlag()) {
            return null;
        }
        if (esds1.getRemoteODFlag() != esds2.getRemoteODFlag()) {
            return null;
        }
        if (esds1.getStreamDependenceFlag() != esds2.getStreamDependenceFlag()) {
            return null;
        }
        esds1.getStreamPriority();
        esds2.getStreamPriority();
        if (esds1.getURLString() != null) {
            esds1.getURLString().equals(esds2.getURLString());
        } else {
            esds2.getURLString();
        }
        if (esds1.getDecoderConfigDescriptor() == null ? esds2.getDecoderConfigDescriptor() != null : !esds1.getDecoderConfigDescriptor().equals(esds2.getDecoderConfigDescriptor())) {
            DecoderConfigDescriptor dcd1 = esds1.getDecoderConfigDescriptor();
            DecoderConfigDescriptor dcd2 = esds2.getDecoderConfigDescriptor();
            if (dcd1.getAudioSpecificInfo() != null && dcd2.getAudioSpecificInfo() != null && !dcd1.getAudioSpecificInfo().equals(dcd2.getAudioSpecificInfo())) {
                return null;
            }
            if (dcd1.getAvgBitRate() != dcd2.getAvgBitRate()) {
                dcd1.setAvgBitRate((dcd1.getAvgBitRate() + dcd2.getAvgBitRate()) / 2);
            }
            dcd1.getBufferSizeDB();
            dcd2.getBufferSizeDB();
            if (dcd1.getDecoderSpecificInfo() == null ? dcd2.getDecoderSpecificInfo() != null : !dcd1.getDecoderSpecificInfo().equals(dcd2.getDecoderSpecificInfo())) {
                return null;
            }
            if (dcd1.getMaxBitRate() != dcd2.getMaxBitRate()) {
                dcd1.setMaxBitRate(Math.max(dcd1.getMaxBitRate(), dcd2.getMaxBitRate()));
            }
            if (!dcd1.getProfileLevelIndicationDescriptors().equals(dcd2.getProfileLevelIndicationDescriptors())) {
                return null;
            }
            if (dcd1.getObjectTypeIndication() != dcd2.getObjectTypeIndication()) {
                return null;
            }
            if (dcd1.getStreamType() != dcd2.getStreamType()) {
                return null;
            }
            if (dcd1.getUpStream() != dcd2.getUpStream()) {
                return null;
            }
        }
        if (esds1.getOtherDescriptors() == null ? esds2.getOtherDescriptors() != null : !esds1.getOtherDescriptors().equals(esds2.getOtherDescriptors())) {
            return null;
        }
        if (esds1.getSlConfigDescriptor() != null) {
            if (esds1.getSlConfigDescriptor().equals(esds2.getSlConfigDescriptor())) {
                return esds1;
            }
        } else if (esds2.getSlConfigDescriptor() == null) {
            return esds1;
        }
        return null;
    }

    public List<Sample> getSamples() {
        return this.lists;
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.stsd;
    }

    public synchronized long[] getSampleDurations() {
        return this.decodingTimes;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        if (this.tracks[0].getCompositionTimeEntries() == null || this.tracks[0].getCompositionTimeEntries().isEmpty()) {
            return null;
        }
        List<int[]> lists2 = new LinkedList<>();
        for (Track track : this.tracks) {
            lists2.add(CompositionTimeToSample.blowupCompositionTimes(track.getCompositionTimeEntries()));
        }
        LinkedList<CompositionTimeToSample.Entry> compositionTimeEntries = new LinkedList<>();
        for (int[] list : lists2) {
            for (int compositionTime : r9.next()) {
                if (compositionTimeEntries.isEmpty() || compositionTimeEntries.getLast().getOffset() != compositionTime) {
                    compositionTimeEntries.add(new CompositionTimeToSample.Entry(1, compositionTime));
                } else {
                    CompositionTimeToSample.Entry e = compositionTimeEntries.getLast();
                    e.setCount(e.getCount() + 1);
                }
            }
        }
        return compositionTimeEntries;
    }

    public long[] getSyncSamples() {
        int pos;
        if (this.tracks[0].getSyncSamples() == null || this.tracks[0].getSyncSamples().length <= 0) {
            return null;
        }
        int numSyncSamples = 0;
        for (Track track : this.tracks) {
            numSyncSamples += track.getSyncSamples() != null ? track.getSyncSamples().length : 0;
        }
        long[] returnSyncSamples = new long[numSyncSamples];
        int pos2 = 0;
        long samplesBefore = 0;
        for (Track track2 : this.tracks) {
            if (track2.getSyncSamples() != null) {
                long[] syncSamples = track2.getSyncSamples();
                int length = syncSamples.length;
                int i = 0;
                while (true) {
                    pos = pos2;
                    if (i >= length) {
                        break;
                    }
                    pos2 = pos + 1;
                    returnSyncSamples[pos] = samplesBefore + syncSamples[i];
                    i++;
                }
                pos2 = pos;
            }
            samplesBefore += (long) track2.getSamples().size();
        }
        return returnSyncSamples;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        if (this.tracks[0].getSampleDependencies() == null || this.tracks[0].getSampleDependencies().isEmpty()) {
            return null;
        }
        List<SampleDependencyTypeBox.Entry> list = new LinkedList<>();
        for (Track track : this.tracks) {
            list.addAll(track.getSampleDependencies());
        }
        return list;
    }

    public TrackMetaData getTrackMetaData() {
        return this.tracks[0].getTrackMetaData();
    }

    public String getHandler() {
        return this.tracks[0].getHandler();
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.tracks[0].getSubsampleInformationBox();
    }
}
