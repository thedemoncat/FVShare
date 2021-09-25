package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TimeToSampleBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String TYPE = "stts";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    static Map<List<Entry>, SoftReference<long[]>> cache = new WeakHashMap();
    List<Entry> entries = Collections.emptyList();

    private static void ajc$preClinit() {
        Factory factory = new Factory("TimeToSampleBox.java", TimeToSampleBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.util.List"), 79);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "java.util.List", "entries", "", "void"), 83);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.TimeToSampleBox", "", "", "", "java.lang.String"), 87);
    }

    static {
        boolean z;
        ajc$preClinit();
        if (!TimeToSampleBox.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public TimeToSampleBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.entries.size() * 8) + 8);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int entryCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.entries = new ArrayList(entryCount);
        for (int i = 0; i < entryCount; i++) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(content), IsoTypeReader.readUInt32(content)));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getCount());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getDelta());
        }
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "TimeToSampleBox[entryCount=" + this.entries.size() + "]";
    }

    public static class Entry {
        long count;
        long delta;

        public Entry(long count2, long delta2) {
            this.count = count2;
            this.delta = delta2;
        }

        public long getCount() {
            return this.count;
        }

        public long getDelta() {
            return this.delta;
        }

        public void setCount(long count2) {
            this.count = count2;
        }

        public void setDelta(long delta2) {
            this.delta = delta2;
        }

        public String toString() {
            return "Entry{count=" + this.count + ", delta=" + this.delta + '}';
        }
    }

    public static synchronized long[] blowupTimeToSamples(List<Entry> entries2) {
        long[] cacheVal;
        int current;
        synchronized (TimeToSampleBox.class) {
            SoftReference cacheEntry = cache.get(entries2);
            if (cacheEntry == null || (cacheVal = (long[]) cacheEntry.get()) == null) {
                long numOfSamples = 0;
                for (Entry entry : entries2) {
                    numOfSamples += entry.getCount();
                }
                if ($assertionsDisabled || numOfSamples <= 2147483647L) {
                    long[] decodingTime = new long[((int) numOfSamples)];
                    int current2 = 0;
                    for (Entry entry2 : entries2) {
                        int i = 0;
                        while (true) {
                            current = current2;
                            if (((long) i) >= entry2.getCount()) {
                                break;
                            }
                            current2 = current + 1;
                            decodingTime[current] = entry2.getDelta();
                            i++;
                        }
                        current2 = current;
                    }
                    cache.put(entries2, new SoftReference(decodingTime));
                    cacheVal = decodingTime;
                } else {
                    throw new AssertionError();
                }
            }
        }
        return cacheVal;
    }
}
