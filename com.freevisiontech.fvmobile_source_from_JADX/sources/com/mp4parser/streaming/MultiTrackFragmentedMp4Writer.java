package com.mp4parser.streaming;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.DataEntryUrlBox;
import com.coremedia.iso.boxes.DataInformationBox;
import com.coremedia.iso.boxes.DataReferenceBox;
import com.coremedia.iso.boxes.FileTypeBox;
import com.coremedia.iso.boxes.HandlerBox;
import com.coremedia.iso.boxes.HintMediaHeaderBox;
import com.coremedia.iso.boxes.MediaBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MediaInformationBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.NullMediaHeaderBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.SoundMediaHeaderBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.SubtitleMediaHeaderBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.VideoMediaHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.SampleFlags;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBaseMediaDecodeTimeBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.mdat.MediaDataBox;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import com.googlecode.mp4parser.util.Math;
import com.googlecode.mp4parser.util.Mp4Arrays;
import com.mp4parser.streaming.extensions.CencEncryptTrackExtension;
import com.mp4parser.streaming.extensions.CompositionTimeSampleExtension;
import com.mp4parser.streaming.extensions.CompositionTimeTrackExtension;
import com.mp4parser.streaming.extensions.SampleFlagsSampleExtension;
import com.mp4parser.streaming.extensions.SampleFlagsTrackExtension;
import com.mp4parser.streaming.extensions.TrackIdTrackExtension;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiTrackFragmentedMp4Writer implements StreamingMp4Writer {
    static final /* synthetic */ boolean $assertionsDisabled = (!MultiTrackFragmentedMp4Writer.class.desiredAssertionStatus());
    CompositionTimeTrackExtension compositionTimeTrackExtension;
    Date creationTime;
    private long currentFragmentStartTime = 0;
    private long currentTime = 0;
    Map<StreamingTrack, List<StreamingSample>> fragmentBuffers = new HashMap();
    private final OutputStream outputStream;
    SampleFlagsTrackExtension sampleDependencyTrackExtension;
    private long sequenceNumber = 1;
    StreamingTrack[] source;

    public MultiTrackFragmentedMp4Writer(StreamingTrack[] source2, OutputStream outputStream2) {
        long j;
        this.source = source2;
        this.outputStream = outputStream2;
        this.creationTime = new Date();
        HashSet<Long> trackIds = new HashSet<>();
        for (StreamingTrack streamingTrack : source2) {
            if (streamingTrack.getTrackExtension(TrackIdTrackExtension.class) != null) {
                TrackIdTrackExtension trackIdTrackExtension = (TrackIdTrackExtension) streamingTrack.getTrackExtension(TrackIdTrackExtension.class);
                if (!$assertionsDisabled && trackIdTrackExtension == null) {
                    throw new AssertionError();
                } else if (trackIds.contains(Long.valueOf(trackIdTrackExtension.getTrackId()))) {
                    throw new RuntimeException("There may not be two tracks with the same trackID within one file");
                }
            }
        }
        for (StreamingTrack streamingTrack2 : source2) {
            if (streamingTrack2.getTrackExtension(TrackIdTrackExtension.class) != null) {
                ArrayList<Long> ts = new ArrayList<>(trackIds);
                Collections.sort(ts);
                if (ts.size() > 0) {
                    j = ts.get(ts.size() - 1).longValue() + 1;
                } else {
                    j = 1;
                }
                streamingTrack2.addTrackExtension(new TrackIdTrackExtension(j));
            }
        }
    }

    public void close() {
    }

    /* access modifiers changed from: protected */
    public Box createMvhd() {
        MovieHeaderBox mvhd = new MovieHeaderBox();
        mvhd.setVersion(1);
        mvhd.setCreationTime(this.creationTime);
        mvhd.setModificationTime(this.creationTime);
        mvhd.setDuration(0);
        long[] timescales = new long[0];
        StreamingTrack[] streamingTrackArr = this.source;
        int length = streamingTrackArr.length;
        for (int i = 0; i < length; i++) {
            Mp4Arrays.copyOfAndAppend(timescales, streamingTrackArr[i].getTimescale());
        }
        mvhd.setTimescale(Math.lcm(timescales));
        mvhd.setNextTrackId(2);
        return mvhd;
    }

    /* access modifiers changed from: protected */
    public Box createMdiaHdlr(StreamingTrack streamingTrack) {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(streamingTrack.getHandler());
        return hdlr;
    }

    /* access modifiers changed from: protected */
    public Box createMdhd(StreamingTrack streamingTrack) {
        MediaHeaderBox mdhd = new MediaHeaderBox();
        mdhd.setCreationTime(this.creationTime);
        mdhd.setModificationTime(this.creationTime);
        mdhd.setDuration(0);
        mdhd.setTimescale(streamingTrack.getTimescale());
        mdhd.setLanguage(streamingTrack.getLanguage());
        return mdhd;
    }

    /* access modifiers changed from: protected */
    public Box createMdia(StreamingTrack streamingTrack) {
        MediaBox mdia = new MediaBox();
        mdia.addBox(createMdhd(streamingTrack));
        mdia.addBox(createMdiaHdlr(streamingTrack));
        mdia.addBox(createMinf(streamingTrack));
        return mdia;
    }

    /* access modifiers changed from: protected */
    public Box createMinf(StreamingTrack streamingTrack) {
        MediaInformationBox minf = new MediaInformationBox();
        if (streamingTrack.getHandler().equals("vide")) {
            minf.addBox(new VideoMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("soun")) {
            minf.addBox(new SoundMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("text")) {
            minf.addBox(new NullMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("subt")) {
            minf.addBox(new SubtitleMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("hint")) {
            minf.addBox(new HintMediaHeaderBox());
        } else if (streamingTrack.getHandler().equals("sbtl")) {
            minf.addBox(new NullMediaHeaderBox());
        }
        minf.addBox(createDinf());
        minf.addBox(createStbl(streamingTrack));
        return minf;
    }

    /* access modifiers changed from: protected */
    public Box createStbl(StreamingTrack streamingTrack) {
        SampleTableBox stbl = new SampleTableBox();
        stbl.addBox(streamingTrack.getSampleDescriptionBox());
        stbl.addBox(new TimeToSampleBox());
        stbl.addBox(new SampleToChunkBox());
        stbl.addBox(new SampleSizeBox());
        stbl.addBox(new StaticChunkOffsetBox());
        return stbl;
    }

    /* access modifiers changed from: protected */
    public DataInformationBox createDinf() {
        DataInformationBox dinf = new DataInformationBox();
        DataReferenceBox dref = new DataReferenceBox();
        dinf.addBox(dref);
        DataEntryUrlBox url = new DataEntryUrlBox();
        url.setFlags(1);
        dref.addBox(url);
        return dinf;
    }

    /* access modifiers changed from: protected */
    public Box createTrak(StreamingTrack streamingTrack) {
        TrackBox trackBox = new TrackBox();
        trackBox.addBox(streamingTrack.getTrackHeaderBox());
        trackBox.addBox(streamingTrack.getTrackHeaderBox());
        trackBox.addBox(createMdia(streamingTrack));
        return trackBox;
    }

    public Box createFtyp() {
        List<String> minorBrands = new LinkedList<>();
        minorBrands.add("isom");
        minorBrands.add("iso6");
        minorBrands.add(VisualSampleEntry.TYPE3);
        return new FileTypeBox("isom", 0, minorBrands);
    }

    /* access modifiers changed from: protected */
    public Box createMvex() {
        MovieExtendsBox mvex = new MovieExtendsBox();
        MovieExtendsHeaderBox mved = new MovieExtendsHeaderBox();
        mved.setVersion(1);
        mved.setFragmentDuration(0);
        mvex.addBox(mved);
        for (StreamingTrack streamingTrack : this.source) {
            mvex.addBox(createTrex(streamingTrack));
        }
        return mvex;
    }

    /* access modifiers changed from: protected */
    public Box createTrex(StreamingTrack streamingTrack) {
        TrackExtendsBox trex = new TrackExtendsBox();
        trex.setTrackId(streamingTrack.getTrackHeaderBox().getTrackId());
        trex.setDefaultSampleDescriptionIndex(1);
        trex.setDefaultSampleDuration(0);
        trex.setDefaultSampleSize(0);
        SampleFlags sf = new SampleFlags();
        if ("soun".equals(streamingTrack.getHandler()) || "subt".equals(streamingTrack.getHandler())) {
            sf.setSampleDependsOn(2);
            sf.setSampleIsDependedOn(2);
        }
        trex.setDefaultSampleFlags(sf);
        return trex;
    }

    /* access modifiers changed from: protected */
    public Box createMoov() {
        MovieBox movieBox = new MovieBox();
        movieBox.addBox(createMvhd());
        for (StreamingTrack streamingTrack : this.source) {
            movieBox.addBox(createTrak(streamingTrack));
        }
        movieBox.addBox(createMvex());
        return movieBox;
    }

    class ConsumeSamplesCallable implements Callable {
        private StreamingTrack streamingTrack;

        public ConsumeSamplesCallable(StreamingTrack streamingTrack2) {
            this.streamingTrack = streamingTrack2;
        }

        public Object call() throws Exception {
            while (true) {
                try {
                    StreamingSample ss = this.streamingTrack.getSamples().poll(100, TimeUnit.MILLISECONDS);
                    if (ss != null) {
                        MultiTrackFragmentedMp4Writer.this.consumeSample(this.streamingTrack, ss);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!this.streamingTrack.hasMoreSamples()) {
                    return null;
                }
            }
        }
    }

    public void write() throws IOException {
        WritableByteChannel out = Channels.newChannel(this.outputStream);
        createFtyp().getBox(out);
        createMoov().getBox(out);
        ExecutorService es = Executors.newFixedThreadPool(this.source.length);
        for (StreamingTrack streamingTrack : this.source) {
            es.submit(new ConsumeSamplesCallable(streamingTrack));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: com.mp4parser.streaming.SampleExtension[]} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void consumeSample(com.mp4parser.streaming.StreamingTrack r15, com.mp4parser.streaming.StreamingSample r16) throws java.io.IOException {
        /*
            r14 = this;
            monitor-enter(r14)
            r3 = 0
            com.mp4parser.streaming.SampleExtension[] r6 = r16.getExtensions()     // Catch:{ all -> 0x0077 }
            int r7 = r6.length     // Catch:{ all -> 0x0077 }
            r5 = 0
        L_0x0008:
            if (r5 < r7) goto L_0x0063
            long r6 = r14.currentTime     // Catch:{ all -> 0x0077 }
            long r8 = r16.getDuration()     // Catch:{ all -> 0x0077 }
            long r6 = r6 + r8
            r14.currentTime = r6     // Catch:{ all -> 0x0077 }
            java.util.Map<com.mp4parser.streaming.StreamingTrack, java.util.List<com.mp4parser.streaming.StreamingSample>> r5 = r14.fragmentBuffers     // Catch:{ all -> 0x0077 }
            java.lang.Object r5 = r5.get(r15)     // Catch:{ all -> 0x0077 }
            java.util.List r5 = (java.util.List) r5     // Catch:{ all -> 0x0077 }
            r0 = r16
            r5.add(r0)     // Catch:{ all -> 0x0077 }
            long r6 = r14.currentTime     // Catch:{ all -> 0x0077 }
            long r8 = r14.currentFragmentStartTime     // Catch:{ all -> 0x0077 }
            r10 = 3
            long r12 = r15.getTimescale()     // Catch:{ all -> 0x0077 }
            long r10 = r10 * r12
            long r8 = r8 + r10
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 <= 0) goto L_0x0061
            java.util.Map<com.mp4parser.streaming.StreamingTrack, java.util.List<com.mp4parser.streaming.StreamingSample>> r5 = r14.fragmentBuffers     // Catch:{ all -> 0x0077 }
            int r5 = r5.size()     // Catch:{ all -> 0x0077 }
            if (r5 <= 0) goto L_0x0061
            com.mp4parser.streaming.extensions.SampleFlagsTrackExtension r5 = r14.sampleDependencyTrackExtension     // Catch:{ all -> 0x0077 }
            if (r5 == 0) goto L_0x0044
            if (r3 == 0) goto L_0x0044
            boolean r5 = r3.isSyncSample()     // Catch:{ all -> 0x0077 }
            if (r5 == 0) goto L_0x0061
        L_0x0044:
            java.io.OutputStream r5 = r14.outputStream     // Catch:{ all -> 0x0077 }
            java.nio.channels.WritableByteChannel r2 = java.nio.channels.Channels.newChannel(r5)     // Catch:{ all -> 0x0077 }
            com.coremedia.iso.boxes.Box r5 = r14.createMoof(r15)     // Catch:{ all -> 0x0077 }
            r5.getBox(r2)     // Catch:{ all -> 0x0077 }
            com.coremedia.iso.boxes.Box r5 = r14.createMdat(r15)     // Catch:{ all -> 0x0077 }
            r5.getBox(r2)     // Catch:{ all -> 0x0077 }
            long r6 = r14.currentTime     // Catch:{ all -> 0x0077 }
            r14.currentFragmentStartTime = r6     // Catch:{ all -> 0x0077 }
            java.util.Map<com.mp4parser.streaming.StreamingTrack, java.util.List<com.mp4parser.streaming.StreamingSample>> r5 = r14.fragmentBuffers     // Catch:{ all -> 0x0077 }
            r5.clear()     // Catch:{ all -> 0x0077 }
        L_0x0061:
            monitor-exit(r14)
            return
        L_0x0063:
            r4 = r6[r5]     // Catch:{ all -> 0x0077 }
            boolean r8 = r4 instanceof com.mp4parser.streaming.extensions.SampleFlagsSampleExtension     // Catch:{ all -> 0x0077 }
            if (r8 == 0) goto L_0x0070
            r0 = r4
            com.mp4parser.streaming.extensions.SampleFlagsSampleExtension r0 = (com.mp4parser.streaming.extensions.SampleFlagsSampleExtension) r0     // Catch:{ all -> 0x0077 }
            r3 = r0
        L_0x006d:
            int r5 = r5 + 1
            goto L_0x0008
        L_0x0070:
            boolean r8 = r4 instanceof com.mp4parser.streaming.extensions.CompositionTimeSampleExtension     // Catch:{ all -> 0x0077 }
            if (r8 == 0) goto L_0x006d
            com.mp4parser.streaming.extensions.CompositionTimeSampleExtension r4 = (com.mp4parser.streaming.extensions.CompositionTimeSampleExtension) r4     // Catch:{ all -> 0x0077 }
            goto L_0x006d
        L_0x0077:
            r5 = move-exception
            monitor-exit(r14)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mp4parser.streaming.MultiTrackFragmentedMp4Writer.consumeSample(com.mp4parser.streaming.StreamingTrack, com.mp4parser.streaming.StreamingSample):void");
    }

    private Box createMoof(StreamingTrack streamingTrack) {
        MovieFragmentBox moof = new MovieFragmentBox();
        createMfhd(this.sequenceNumber, moof);
        createTraf(streamingTrack, moof);
        TrackRunBox firstTrun = moof.getTrackRunBoxes().get(0);
        firstTrun.setDataOffset(1);
        firstTrun.setDataOffset((int) (8 + moof.getSize()));
        this.sequenceNumber++;
        return moof;
    }

    /* access modifiers changed from: protected */
    public void createTfhd(StreamingTrack streamingTrack, TrackFragmentBox parent) {
        TrackFragmentHeaderBox tfhd = new TrackFragmentHeaderBox();
        tfhd.setDefaultSampleFlags(new SampleFlags());
        tfhd.setBaseDataOffset(-1);
        tfhd.setTrackId(((TrackIdTrackExtension) streamingTrack.getTrackExtension(TrackIdTrackExtension.class)).getTrackId());
        tfhd.setDefaultBaseIsMoof(true);
        parent.addBox(tfhd);
    }

    /* access modifiers changed from: protected */
    public void createTfdt(TrackFragmentBox parent) {
        TrackFragmentBaseMediaDecodeTimeBox tfdt = new TrackFragmentBaseMediaDecodeTimeBox();
        tfdt.setVersion(1);
        tfdt.setBaseMediaDecodeTime(this.currentFragmentStartTime);
        parent.addBox(tfdt);
    }

    /* access modifiers changed from: protected */
    public void createTrun(StreamingTrack streamingTrack, TrackFragmentBox parent) {
        boolean z;
        boolean sampleFlagsRequired;
        TrackRunBox trun = new TrackRunBox();
        trun.setVersion(1);
        trun.setSampleDurationPresent(true);
        trun.setSampleSizePresent(true);
        List<TrackRunBox.Entry> entries = new ArrayList<>(this.fragmentBuffers.size());
        if (streamingTrack.getTrackExtension(CompositionTimeTrackExtension.class) != null) {
            z = true;
        } else {
            z = false;
        }
        trun.setSampleCompositionTimeOffsetPresent(z);
        if (streamingTrack.getTrackExtension(SampleFlagsTrackExtension.class) != null) {
            sampleFlagsRequired = true;
        } else {
            sampleFlagsRequired = false;
        }
        trun.setSampleFlagsPresent(sampleFlagsRequired);
        for (StreamingSample streamingSample : this.fragmentBuffers.get(streamingTrack)) {
            TrackRunBox.Entry entry = new TrackRunBox.Entry();
            entry.setSampleSize((long) streamingSample.getContent().remaining());
            if (sampleFlagsRequired) {
                SampleFlagsSampleExtension sampleFlagsSampleExtension = (SampleFlagsSampleExtension) StreamingSampleHelper.getSampleExtension(streamingSample, SampleFlagsSampleExtension.class);
                if ($assertionsDisabled || sampleFlagsSampleExtension != null) {
                    SampleFlags sflags = new SampleFlags();
                    sflags.setIsLeading(sampleFlagsSampleExtension.getIsLeading());
                    sflags.setSampleIsDependedOn(sampleFlagsSampleExtension.getSampleIsDependedOn());
                    sflags.setSampleDependsOn(sampleFlagsSampleExtension.getSampleDependsOn());
                    sflags.setSampleHasRedundancy(sampleFlagsSampleExtension.getSampleHasRedundancy());
                    sflags.setSampleIsDifferenceSample(sampleFlagsSampleExtension.isSampleIsNonSyncSample());
                    sflags.setSamplePaddingValue(sampleFlagsSampleExtension.getSamplePaddingValue());
                    sflags.setSampleDegradationPriority(sampleFlagsSampleExtension.getSampleDegradationPriority());
                    entry.setSampleFlags(sflags);
                } else {
                    throw new AssertionError("SampleDependencySampleExtension missing even though SampleDependencyTrackExtension was present");
                }
            }
            entry.setSampleDuration(streamingSample.getDuration());
            if (trun.isSampleCompositionTimeOffsetPresent()) {
                CompositionTimeSampleExtension compositionTimeSampleExtension = (CompositionTimeSampleExtension) StreamingSampleHelper.getSampleExtension(streamingSample, CompositionTimeSampleExtension.class);
                if ($assertionsDisabled || compositionTimeSampleExtension != null) {
                    entry.setSampleCompositionTimeOffset(compositionTimeSampleExtension.getCompositionTimeOffset());
                } else {
                    throw new AssertionError("CompositionTimeSampleExtension missing even though CompositionTimeTrackExtension was present");
                }
            }
            entries.add(entry);
        }
        trun.setEntries(entries);
        parent.addBox(trun);
    }

    private void createTraf(StreamingTrack streamingTrack, MovieFragmentBox moof) {
        TrackFragmentBox traf = new TrackFragmentBox();
        moof.addBox(traf);
        createTfhd(streamingTrack, traf);
        createTfdt(traf);
        createTrun(streamingTrack, traf);
        streamingTrack.getTrackExtension(CencEncryptTrackExtension.class);
    }

    private void createMfhd(long sequenceNumber2, MovieFragmentBox moof) {
        MovieFragmentHeaderBox mfhd = new MovieFragmentHeaderBox();
        mfhd.setSequenceNumber(sequenceNumber2);
        moof.addBox(mfhd);
    }

    private Box createMdat(final StreamingTrack streamingTrack) {
        return new WriteOnlyBox(MediaDataBox.TYPE) {
            public long getSize() {
                long l = 8;
                for (StreamingSample streamingSample : MultiTrackFragmentedMp4Writer.this.fragmentBuffers.get(streamingTrack)) {
                    l += (long) streamingSample.getContent().remaining();
                }
                return l;
            }

            public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                ArrayList<ByteBuffer> sampleContents = new ArrayList<>();
                long l = 8;
                for (StreamingSample streamingSample : MultiTrackFragmentedMp4Writer.this.fragmentBuffers.get(streamingTrack)) {
                    ByteBuffer sampleContent = streamingSample.getContent();
                    sampleContents.add(sampleContent);
                    l += (long) sampleContent.remaining();
                }
                ByteBuffer bb = ByteBuffer.allocate(8);
                IsoTypeWriter.writeUInt32(bb, l);
                bb.put(IsoFile.fourCCtoBytes(getType()));
                writableByteChannel.write((ByteBuffer) bb.rewind());
                Iterator<ByteBuffer> it = sampleContents.iterator();
                while (it.hasNext()) {
                    writableByteChannel.write(it.next());
                }
            }
        };
    }
}
