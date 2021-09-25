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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingleTrackFragmentedMp4Writer implements StreamingMp4Writer {
    static final /* synthetic */ boolean $assertionsDisabled = (!SingleTrackFragmentedMp4Writer.class.desiredAssertionStatus());
    CompositionTimeTrackExtension compositionTimeTrackExtension;
    Date creationTime;
    private long currentFragmentStartTime = 0;
    private long currentTime = 0;
    List<StreamingSample> fragment = new ArrayList();
    private final OutputStream outputStream;
    SampleFlagsTrackExtension sampleDependencyTrackExtension;
    private long sequenceNumber;
    StreamingTrack source;

    public SingleTrackFragmentedMp4Writer(StreamingTrack source2, OutputStream outputStream2) {
        this.source = source2;
        this.outputStream = outputStream2;
        this.creationTime = new Date();
        this.compositionTimeTrackExtension = (CompositionTimeTrackExtension) source2.getTrackExtension(CompositionTimeTrackExtension.class);
        this.sampleDependencyTrackExtension = (SampleFlagsTrackExtension) source2.getTrackExtension(SampleFlagsTrackExtension.class);
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
        mvhd.setTimescale(this.source.getTimescale());
        mvhd.setNextTrackId(2);
        return mvhd;
    }

    /* access modifiers changed from: protected */
    public Box createMdiaHdlr() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(this.source.getHandler());
        return hdlr;
    }

    /* access modifiers changed from: protected */
    public Box createMdhd() {
        MediaHeaderBox mdhd = new MediaHeaderBox();
        mdhd.setCreationTime(this.creationTime);
        mdhd.setModificationTime(this.creationTime);
        mdhd.setDuration(0);
        mdhd.setTimescale(this.source.getTimescale());
        mdhd.setLanguage(this.source.getLanguage());
        return mdhd;
    }

    /* access modifiers changed from: protected */
    public Box createMdia() {
        MediaBox mdia = new MediaBox();
        mdia.addBox(createMdhd());
        mdia.addBox(createMdiaHdlr());
        mdia.addBox(createMinf());
        return mdia;
    }

    /* access modifiers changed from: protected */
    public Box createMinf() {
        MediaInformationBox minf = new MediaInformationBox();
        if (this.source.getHandler().equals("vide")) {
            minf.addBox(new VideoMediaHeaderBox());
        } else if (this.source.getHandler().equals("soun")) {
            minf.addBox(new SoundMediaHeaderBox());
        } else if (this.source.getHandler().equals("text")) {
            minf.addBox(new NullMediaHeaderBox());
        } else if (this.source.getHandler().equals("subt")) {
            minf.addBox(new SubtitleMediaHeaderBox());
        } else if (this.source.getHandler().equals("hint")) {
            minf.addBox(new HintMediaHeaderBox());
        } else if (this.source.getHandler().equals("sbtl")) {
            minf.addBox(new NullMediaHeaderBox());
        }
        minf.addBox(createDinf());
        minf.addBox(createStbl());
        return minf;
    }

    /* access modifiers changed from: protected */
    public Box createStbl() {
        SampleTableBox stbl = new SampleTableBox();
        stbl.addBox(this.source.getSampleDescriptionBox());
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
    public Box createTrak() {
        TrackBox trackBox = new TrackBox();
        trackBox.addBox(this.source.getTrackHeaderBox());
        trackBox.addBox(createMdia());
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
        mvex.addBox(createTrex());
        return mvex;
    }

    /* access modifiers changed from: protected */
    public Box createTrex() {
        TrackExtendsBox trex = new TrackExtendsBox();
        trex.setTrackId(this.source.getTrackHeaderBox().getTrackId());
        trex.setDefaultSampleDescriptionIndex(1);
        trex.setDefaultSampleDuration(0);
        trex.setDefaultSampleSize(0);
        SampleFlags sf = new SampleFlags();
        if ("soun".equals(this.source.getHandler()) || "subt".equals(this.source.getHandler())) {
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
        movieBox.addBox(createTrak());
        movieBox.addBox(createMvex());
        return movieBox;
    }

    public void write() throws IOException {
        WritableByteChannel out = Channels.newChannel(this.outputStream);
        createFtyp().getBox(out);
        createMoov().getBox(out);
        while (true) {
            try {
                StreamingSample ss = this.source.getSamples().poll(100, TimeUnit.MILLISECONDS);
                if (ss != null) {
                    consumeSample(ss, out);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.source.hasMoreSamples()) {
                return;
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: com.mp4parser.streaming.SampleExtension[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void consumeSample(com.mp4parser.streaming.StreamingSample r11, java.nio.channels.WritableByteChannel r12) throws java.io.IOException {
        /*
            r10 = this;
            r0 = 0
            com.mp4parser.streaming.SampleExtension[] r3 = r11.getExtensions()
            int r4 = r3.length
            r2 = 0
        L_0x0007:
            if (r2 < r4) goto L_0x0055
            long r2 = r10.currentTime
            long r4 = r11.getDuration()
            long r2 = r2 + r4
            r10.currentTime = r2
            java.util.List<com.mp4parser.streaming.StreamingSample> r2 = r10.fragment
            r2.add(r11)
            long r2 = r10.currentTime
            long r4 = r10.currentFragmentStartTime
            r6 = 3
            com.mp4parser.streaming.StreamingTrack r8 = r10.source
            long r8 = r8.getTimescale()
            long r6 = r6 * r8
            long r4 = r4 + r6
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 <= 0) goto L_0x0054
            java.util.List<com.mp4parser.streaming.StreamingSample> r2 = r10.fragment
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x0054
            com.mp4parser.streaming.extensions.SampleFlagsTrackExtension r2 = r10.sampleDependencyTrackExtension
            if (r2 == 0) goto L_0x003d
            if (r0 == 0) goto L_0x003d
            boolean r2 = r0.isSyncSample()
            if (r2 == 0) goto L_0x0054
        L_0x003d:
            com.coremedia.iso.boxes.Box r2 = r10.createMoof()
            r2.getBox(r12)
            com.coremedia.iso.boxes.Box r2 = r10.createMdat()
            r2.getBox(r12)
            long r2 = r10.currentTime
            r10.currentFragmentStartTime = r2
            java.util.List<com.mp4parser.streaming.StreamingSample> r2 = r10.fragment
            r2.clear()
        L_0x0054:
            return
        L_0x0055:
            r1 = r3[r2]
            boolean r5 = r1 instanceof com.mp4parser.streaming.extensions.SampleFlagsSampleExtension
            if (r5 == 0) goto L_0x0061
            r0 = r1
            com.mp4parser.streaming.extensions.SampleFlagsSampleExtension r0 = (com.mp4parser.streaming.extensions.SampleFlagsSampleExtension) r0
        L_0x005e:
            int r2 = r2 + 1
            goto L_0x0007
        L_0x0061:
            boolean r5 = r1 instanceof com.mp4parser.streaming.extensions.CompositionTimeSampleExtension
            if (r5 == 0) goto L_0x005e
            com.mp4parser.streaming.extensions.CompositionTimeSampleExtension r1 = (com.mp4parser.streaming.extensions.CompositionTimeSampleExtension) r1
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mp4parser.streaming.SingleTrackFragmentedMp4Writer.consumeSample(com.mp4parser.streaming.StreamingSample, java.nio.channels.WritableByteChannel):void");
    }

    private Box createMoof() {
        MovieFragmentBox moof = new MovieFragmentBox();
        createMfhd(this.sequenceNumber, moof);
        createTraf(this.sequenceNumber, moof);
        TrackRunBox firstTrun = moof.getTrackRunBoxes().get(0);
        firstTrun.setDataOffset(1);
        firstTrun.setDataOffset((int) (8 + moof.getSize()));
        return moof;
    }

    /* access modifiers changed from: protected */
    public void createTfhd(TrackFragmentBox parent) {
        TrackFragmentHeaderBox tfhd = new TrackFragmentHeaderBox();
        tfhd.setDefaultSampleFlags(new SampleFlags());
        tfhd.setBaseDataOffset(-1);
        TrackIdTrackExtension trackIdTrackExtension = (TrackIdTrackExtension) this.source.getTrackExtension(TrackIdTrackExtension.class);
        if (trackIdTrackExtension != null) {
            tfhd.setTrackId(trackIdTrackExtension.getTrackId());
        } else {
            tfhd.setTrackId(1);
        }
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
    public void createTrun(TrackFragmentBox parent) {
        boolean z;
        boolean sampleFlagsRequired;
        TrackRunBox trun = new TrackRunBox();
        trun.setVersion(1);
        trun.setSampleDurationPresent(true);
        trun.setSampleSizePresent(true);
        List<TrackRunBox.Entry> entries = new ArrayList<>(this.fragment.size());
        if (this.source.getTrackExtension(CompositionTimeTrackExtension.class) != null) {
            z = true;
        } else {
            z = false;
        }
        trun.setSampleCompositionTimeOffsetPresent(z);
        if (this.source.getTrackExtension(SampleFlagsTrackExtension.class) != null) {
            sampleFlagsRequired = true;
        } else {
            sampleFlagsRequired = false;
        }
        trun.setSampleFlagsPresent(sampleFlagsRequired);
        for (StreamingSample streamingSample : this.fragment) {
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

    private void createTraf(long sequenceNumber2, MovieFragmentBox moof) {
        TrackFragmentBox traf = new TrackFragmentBox();
        moof.addBox(traf);
        createTfhd(traf);
        createTfdt(traf);
        createTrun(traf);
        this.source.getTrackExtension(CencEncryptTrackExtension.class);
    }

    private void createMfhd(long sequenceNumber2, MovieFragmentBox moof) {
        MovieFragmentHeaderBox mfhd = new MovieFragmentHeaderBox();
        mfhd.setSequenceNumber(sequenceNumber2);
        moof.addBox(mfhd);
    }

    private Box createMdat() {
        return new WriteOnlyBox(MediaDataBox.TYPE) {
            public long getSize() {
                long l = 8;
                for (StreamingSample streamingSample : SingleTrackFragmentedMp4Writer.this.fragment) {
                    l += (long) streamingSample.getContent().remaining();
                }
                return l;
            }

            public void getBox(WritableByteChannel writableByteChannel) throws IOException {
                ArrayList<ByteBuffer> sampleContents = new ArrayList<>();
                long l = 8;
                for (StreamingSample streamingSample : SingleTrackFragmentedMp4Writer.this.fragment) {
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
