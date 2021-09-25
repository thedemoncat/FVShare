package com.googlecode.mp4parser.authoring.tracks;

import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.AbstractTrack;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.TrackMetaData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CroppedTrack extends AbstractTrack {
    static final /* synthetic */ boolean $assertionsDisabled = (!CroppedTrack.class.desiredAssertionStatus());
    private int fromSample;
    Track origTrack;
    private int toSample;

    public CroppedTrack(Track origTrack2, long fromSample2, long toSample2) {
        super("crop(" + origTrack2.getName() + ")");
        this.origTrack = origTrack2;
        if (!$assertionsDisabled && fromSample2 > 2147483647L) {
            throw new AssertionError();
        } else if ($assertionsDisabled || toSample2 <= 2147483647L) {
            this.fromSample = (int) fromSample2;
            this.toSample = (int) toSample2;
        } else {
            throw new AssertionError();
        }
    }

    public void close() throws IOException {
        this.origTrack.close();
    }

    public List<Sample> getSamples() {
        return this.origTrack.getSamples().subList(this.fromSample, this.toSample);
    }

    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.origTrack.getSampleDescriptionBox();
    }

    public synchronized long[] getSampleDurations() {
        long[] decodingTimes;
        decodingTimes = new long[(this.toSample - this.fromSample)];
        System.arraycopy(this.origTrack.getSampleDurations(), this.fromSample, decodingTimes, 0, decodingTimes.length);
        return decodingTimes;
    }

    static List<TimeToSampleBox.Entry> getDecodingTimeEntries(List<TimeToSampleBox.Entry> origSamples, long fromSample2, long toSample2) {
        TimeToSampleBox.Entry currentEntry;
        if (origSamples == null || origSamples.isEmpty()) {
            return null;
        }
        long current = 0;
        ListIterator<TimeToSampleBox.Entry> e = origSamples.listIterator();
        LinkedList<TimeToSampleBox.Entry> nuList = new LinkedList<>();
        while (true) {
            currentEntry = e.next();
            if (currentEntry.getCount() + current > fromSample2) {
                break;
            }
            current += currentEntry.getCount();
        }
        if (currentEntry.getCount() + current >= toSample2) {
            nuList.add(new TimeToSampleBox.Entry(toSample2 - fromSample2, currentEntry.getDelta()));
            return nuList;
        }
        nuList.add(new TimeToSampleBox.Entry((currentEntry.getCount() + current) - fromSample2, currentEntry.getDelta()));
        long count = currentEntry.getCount();
        while (true) {
            current += count;
            if (!e.hasNext()) {
                break;
            }
            currentEntry = e.next();
            if (currentEntry.getCount() + current >= toSample2) {
                break;
            }
            nuList.add(currentEntry);
            count = currentEntry.getCount();
        }
        nuList.add(new TimeToSampleBox.Entry(toSample2 - current, currentEntry.getDelta()));
        return nuList;
    }

    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return getCompositionTimeEntries(this.origTrack.getCompositionTimeEntries(), (long) this.fromSample, (long) this.toSample);
    }

    static List<CompositionTimeToSample.Entry> getCompositionTimeEntries(List<CompositionTimeToSample.Entry> origSamples, long fromSample2, long toSample2) {
        CompositionTimeToSample.Entry currentEntry;
        if (origSamples == null || origSamples.isEmpty()) {
            return null;
        }
        long current = 0;
        ListIterator<CompositionTimeToSample.Entry> e = origSamples.listIterator();
        ArrayList<CompositionTimeToSample.Entry> nuList = new ArrayList<>();
        while (true) {
            currentEntry = e.next();
            if (((long) currentEntry.getCount()) + current > fromSample2) {
                break;
            }
            current += (long) currentEntry.getCount();
        }
        if (((long) currentEntry.getCount()) + current >= toSample2) {
            nuList.add(new CompositionTimeToSample.Entry((int) (toSample2 - fromSample2), currentEntry.getOffset()));
            return nuList;
        }
        nuList.add(new CompositionTimeToSample.Entry((int) ((((long) currentEntry.getCount()) + current) - fromSample2), currentEntry.getOffset()));
        int count = currentEntry.getCount();
        while (true) {
            current += (long) count;
            if (!e.hasNext()) {
                break;
            }
            currentEntry = e.next();
            if (((long) currentEntry.getCount()) + current >= toSample2) {
                break;
            }
            nuList.add(currentEntry);
            count = currentEntry.getCount();
        }
        nuList.add(new CompositionTimeToSample.Entry((int) (toSample2 - current), currentEntry.getOffset()));
        return nuList;
    }

    public synchronized long[] getSyncSamples() {
        long[] syncSampleArray;
        if (this.origTrack.getSyncSamples() != null) {
            long[] origSyncSamples = this.origTrack.getSyncSamples();
            int i = 0;
            int j = origSyncSamples.length;
            while (i < origSyncSamples.length && origSyncSamples[i] < ((long) this.fromSample)) {
                i++;
            }
            while (j > 0 && ((long) this.toSample) < origSyncSamples[j - 1]) {
                j--;
            }
            syncSampleArray = new long[(j - i)];
            System.arraycopy(this.origTrack.getSyncSamples(), i, syncSampleArray, 0, j - i);
            for (int k = 0; k < syncSampleArray.length; k++) {
                syncSampleArray[k] = syncSampleArray[k] - ((long) this.fromSample);
            }
        } else {
            syncSampleArray = null;
        }
        return syncSampleArray;
    }

    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        if (this.origTrack.getSampleDependencies() == null || this.origTrack.getSampleDependencies().isEmpty()) {
            return null;
        }
        return this.origTrack.getSampleDependencies().subList(this.fromSample, this.toSample);
    }

    public TrackMetaData getTrackMetaData() {
        return this.origTrack.getTrackMetaData();
    }

    public String getHandler() {
        return this.origTrack.getHandler();
    }

    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.origTrack.getSubsampleInformationBox();
    }
}
