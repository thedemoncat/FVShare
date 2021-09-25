package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleToChunkBox extends AbstractFullBox {
    public static final String TYPE = "stsc";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    List<Entry> entries = Collections.emptyList();

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleToChunkBox.java", SampleToChunkBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.util.List"), 47);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.SampleToChunkBox", "java.util.List", "entries", "", "void"), 51);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SampleToChunkBox", "", "", "", "java.lang.String"), 84);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "blowup", "com.coremedia.iso.boxes.SampleToChunkBox", "int", "chunkCount", "", "[J"), 95);
    }

    public SampleToChunkBox() {
        super(TYPE);
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 12) + 8);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int entryCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.entries = new ArrayList(entryCount);
        for (int i = 0; i < entryCount; i++) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(content), IsoTypeReader.readUInt32(content), IsoTypeReader.readUInt32(content)));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getFirstChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSamplesPerChunk());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSampleDescriptionIndex());
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "SampleToChunkBox[entryCount=" + this.entries.size() + "]";
    }

    public long[] blowup(int chunkCount) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(chunkCount)));
        long[] numberOfSamples = new long[chunkCount];
        List sampleToChunkEntries = new LinkedList(this.entries);
        Collections.reverse(sampleToChunkEntries);
        Iterator iterator = sampleToChunkEntries.iterator();
        Entry currentEntry = (Entry) iterator.next();
        for (int i = numberOfSamples.length; i > 1; i--) {
            numberOfSamples[i - 1] = currentEntry.getSamplesPerChunk();
            if (((long) i) == currentEntry.getFirstChunk()) {
                currentEntry = (Entry) iterator.next();
            }
        }
        numberOfSamples[0] = currentEntry.getSamplesPerChunk();
        return numberOfSamples;
    }

    public static class Entry {
        long firstChunk;
        long sampleDescriptionIndex;
        long samplesPerChunk;

        public Entry(long firstChunk2, long samplesPerChunk2, long sampleDescriptionIndex2) {
            this.firstChunk = firstChunk2;
            this.samplesPerChunk = samplesPerChunk2;
            this.sampleDescriptionIndex = sampleDescriptionIndex2;
        }

        public long getFirstChunk() {
            return this.firstChunk;
        }

        public void setFirstChunk(long firstChunk2) {
            this.firstChunk = firstChunk2;
        }

        public long getSamplesPerChunk() {
            return this.samplesPerChunk;
        }

        public void setSamplesPerChunk(long samplesPerChunk2) {
            this.samplesPerChunk = samplesPerChunk2;
        }

        public long getSampleDescriptionIndex() {
            return this.sampleDescriptionIndex;
        }

        public void setSampleDescriptionIndex(long sampleDescriptionIndex2) {
            this.sampleDescriptionIndex = sampleDescriptionIndex2;
        }

        public String toString() {
            return "Entry{firstChunk=" + this.firstChunk + ", samplesPerChunk=" + this.samplesPerChunk + ", sampleDescriptionIndex=" + this.sampleDescriptionIndex + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.firstChunk != entry.firstChunk) {
                return false;
            }
            if (this.sampleDescriptionIndex != entry.sampleDescriptionIndex) {
                return false;
            }
            if (this.samplesPerChunk != entry.samplesPerChunk) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((int) (this.firstChunk ^ (this.firstChunk >>> 32))) * 31) + ((int) (this.samplesPerChunk ^ (this.samplesPerChunk >>> 32)))) * 31) + ((int) (this.sampleDescriptionIndex ^ (this.sampleDescriptionIndex >>> 32)));
        }
    }
}
