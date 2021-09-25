package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.OriginalFormatBox;
import com.coremedia.iso.boxes.ProtectionSchemeInformationBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SchemeTypeBox;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.AbstractContainerBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.MemoryDataSourceImpl;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import com.googlecode.mp4parser.boxes.cenc.CencDecryptingSampleList;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.CencSampleEncryptionInformationGroupEntry;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.util.Path;
import com.googlecode.mp4parser.util.RangeStartMap;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;

public class CencDecryptingTrackImpl extends AbstractTrack {
    RangeStartMap<Integer, SecretKey> indexToKey;
    Track original;
    CencDecryptingSampleList samples;

    public CencDecryptingTrackImpl(CencEncryptedTrack original2, SecretKey sk) {
        this(original2, (Map<UUID, SecretKey>) Collections.singletonMap(original2.getDefaultKeyId(), sk));
    }

    public CencDecryptingTrackImpl(CencEncryptedTrack original2, Map<UUID, SecretKey> keys) {
        super("dec(" + original2.getName() + ")");
        this.indexToKey = new RangeStartMap<>();
        this.original = original2;
        SchemeTypeBox schm = (SchemeTypeBox) Path.getPath((AbstractContainerBox) original2.getSampleDescriptionBox(), "enc./sinf/schm");
        if ("cenc".equals(schm.getSchemeType()) || "cbc1".equals(schm.getSchemeType())) {
            List<CencSampleEncryptionInformationGroupEntry> groupEntries = new ArrayList<>();
            for (Map.Entry<GroupEntry, long[]> groupEntry : original2.getSampleGroups().entrySet()) {
                if (groupEntry.getKey() instanceof CencSampleEncryptionInformationGroupEntry) {
                    groupEntries.add((CencSampleEncryptionInformationGroupEntry) groupEntry.getKey());
                } else {
                    getSampleGroups().put(groupEntry.getKey(), groupEntry.getValue());
                }
            }
            int lastSampleGroupDescriptionIndex = -1;
            for (int i = 0; i < original2.getSamples().size(); i++) {
                int index = 0;
                for (int j = 0; j < groupEntries.size(); j++) {
                    if (Arrays.binarySearch(original2.getSampleGroups().get(groupEntries.get(j)), (long) i) >= 0) {
                        index = j + 1;
                    }
                }
                if (lastSampleGroupDescriptionIndex != index) {
                    if (index == 0) {
                        this.indexToKey.put(Integer.valueOf(i), keys.get(original2.getDefaultKeyId()));
                    } else if (groupEntries.get(index - 1).isEncrypted()) {
                        SecretKey sk = keys.get(groupEntries.get(index - 1).getKid());
                        if (sk == null) {
                            throw new RuntimeException("Key " + groupEntries.get(index - 1).getKid() + " was not supplied for decryption");
                        }
                        this.indexToKey.put(Integer.valueOf(i), sk);
                    } else {
                        this.indexToKey.put(Integer.valueOf(i), null);
                    }
                    lastSampleGroupDescriptionIndex = index;
                }
            }
            this.samples = new CencDecryptingSampleList(this.indexToKey, original2.getSamples(), original2.getSampleEncryptionEntries(), schm.getSchemeType());
            return;
        }
        throw new RuntimeException("You can only use the CencDecryptingTrackImpl with CENC (cenc or cbc1) encrypted tracks");
    }

    public void close() throws IOException {
        this.original.close();
    }

    public long[] getSyncSamples() {
        return this.original.getSyncSamples();
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        OriginalFormatBox frma = (OriginalFormatBox) Path.getPath((AbstractContainerBox) this.original.getSampleDescriptionBox(), "enc./sinf/frma");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            this.original.getSampleDescriptionBox().getBox(Channels.newChannel(baos));
            SampleDescriptionBox stsd = (SampleDescriptionBox) new IsoFile((DataSource) new MemoryDataSourceImpl(baos.toByteArray())).getBoxes().get(0);
            if (stsd.getSampleEntry() instanceof AudioSampleEntry) {
                ((AudioSampleEntry) stsd.getSampleEntry()).setType(frma.getDataFormat());
            } else if (stsd.getSampleEntry() instanceof VisualSampleEntry) {
                ((VisualSampleEntry) stsd.getSampleEntry()).setType(frma.getDataFormat());
            } else {
                throw new RuntimeException("I don't know " + stsd.getSampleEntry().getType());
            }
            List<Box> nuBoxes = new LinkedList<>();
            for (Box box : stsd.getSampleEntry().getBoxes()) {
                if (!box.getType().equals(ProtectionSchemeInformationBox.TYPE)) {
                    nuBoxes.add(box);
                }
            }
            stsd.getSampleEntry().setBoxes(nuBoxes);
            return stsd;
        } catch (IOException e) {
            throw new RuntimeException("Dumping stsd to memory failed");
        }
    }

    public long[] getSampleDurations() {
        return this.original.getSampleDurations();
    }

    public TrackMetaData getTrackMetaData() {
        return this.original.getTrackMetaData();
    }

    public String getHandler() {
        return this.original.getHandler();
    }

    public List<Sample> getSamples() {
        return this.samples;
    }
}
