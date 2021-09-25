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
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class CompositionTimeToSample extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = (!CompositionTimeToSample.class.desiredAssertionStatus());
    public static final String TYPE = "ctts";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    List<Entry> entries = Collections.emptyList();

    private static void ajc$preClinit() {
        Factory factory = new Factory("CompositionTimeToSample.java", CompositionTimeToSample.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "", "", "", "java.util.List"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.CompositionTimeToSample", "java.util.List", "entries", "", "void"), 61);
    }

    static {
        ajc$preClinit();
    }

    public CompositionTimeToSample() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 8) + 8);
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int numberOfEntries = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.entries = new ArrayList(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            this.entries.add(new Entry(CastUtils.l2i(IsoTypeReader.readUInt32(content)), content.getInt()));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) entry.getCount());
            byteBuffer.putInt(entry.getOffset());
        }
    }

    public static class Entry {
        int count;
        int offset;

        public Entry(int count2, int offset2) {
            this.count = count2;
            this.offset = offset2;
        }

        public int getCount() {
            return this.count;
        }

        public int getOffset() {
            return this.offset;
        }

        public void setCount(int count2) {
            this.count = count2;
        }

        public void setOffset(int offset2) {
            this.offset = offset2;
        }

        public String toString() {
            return "Entry{count=" + this.count + ", offset=" + this.offset + '}';
        }
    }

    public static int[] blowupCompositionTimes(List<Entry> entries2) {
        long numOfSamples = 0;
        for (Entry entry : entries2) {
            numOfSamples += (long) entry.getCount();
        }
        if ($assertionsDisabled || numOfSamples <= 2147483647L) {
            int[] decodingTime = new int[((int) numOfSamples)];
            int current = 0;
            for (Entry entry2 : entries2) {
                int i = 0;
                while (i < entry2.getCount()) {
                    decodingTime[current] = entry2.getOffset();
                    i++;
                    current++;
                }
            }
            return decodingTime;
        }
        throw new AssertionError();
    }
}
