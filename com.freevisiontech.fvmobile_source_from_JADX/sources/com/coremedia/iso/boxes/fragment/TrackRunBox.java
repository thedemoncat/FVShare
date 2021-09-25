package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackRunBox extends AbstractFullBox {
    public static final String TYPE = "trun";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final JoinPoint.StaticPart ajc$tjp_16 = null;
    private static final JoinPoint.StaticPart ajc$tjp_17 = null;
    private static final JoinPoint.StaticPart ajc$tjp_18 = null;
    private static final JoinPoint.StaticPart ajc$tjp_19 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private int dataOffset;
    private List<Entry> entries = new ArrayList();
    private SampleFlags firstSampleFlags;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("TrackRunBox.java", TrackRunBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "java.util.List"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDataOffset", "com.coremedia.iso.boxes.fragment.TrackRunBox", "int", "dataOffset", "", "void"), 120);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDataOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), (int) CompanyIdentifierResolver.ERI_INC);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleSizePresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), (int) CompanyIdentifierResolver.OPENBRAIN_TECHNOLOGIES_CO_LTD);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleDurationPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), (int) CompanyIdentifierResolver.ARUBA_NETWORKS);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), (int) CompanyIdentifierResolver.HID_GLOBAL);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleCompositionTimeOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "boolean", "v", "", "void"), 300);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDataOffset", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "int"), (int) CompanyIdentifierResolver.AIREWEAR_LLC);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFirstSampleFlags", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "com.coremedia.iso.boxes.fragment.SampleFlags"), 313);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFirstSampleFlags", "com.coremedia.iso.boxes.fragment.TrackRunBox", "com.coremedia.iso.boxes.fragment.SampleFlags", "firstSampleFlags", "", "void"), 317);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "java.lang.String"), 327);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.fragment.TrackRunBox", "java.util.List", "entries", "", "void"), 342);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleCompositionTimeOffsets", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "[J"), 129);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleCount", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "long"), (int) CompanyIdentifierResolver.ABOVE_AVERAGE_OUTCOMES_INC);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isDataOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.MORSE_PROJECT_INC);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isFirstSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.ELCOMETER_LIMITED);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isSampleSizePresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.KOUKAAM_AS);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isSampleDurationPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), 255);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isSampleFlagsPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.BANG_OLUFSEN_A_S);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isSampleCompositionTimeOffsetPresent", "com.coremedia.iso.boxes.fragment.TrackRunBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.WILLIAM_DEMANT_HOLDING_A_S);
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public long sampleCompositionTimeOffset;
        /* access modifiers changed from: private */
        public long sampleDuration;
        /* access modifiers changed from: private */
        public SampleFlags sampleFlags;
        /* access modifiers changed from: private */
        public long sampleSize;

        public Entry() {
        }

        public Entry(long sampleDuration2, long sampleSize2, SampleFlags sampleFlags2, int sampleCompositionTimeOffset2) {
            this.sampleDuration = sampleDuration2;
            this.sampleSize = sampleSize2;
            this.sampleFlags = sampleFlags2;
            this.sampleCompositionTimeOffset = (long) sampleCompositionTimeOffset2;
        }

        public long getSampleDuration() {
            return this.sampleDuration;
        }

        public long getSampleSize() {
            return this.sampleSize;
        }

        public SampleFlags getSampleFlags() {
            return this.sampleFlags;
        }

        public long getSampleCompositionTimeOffset() {
            return this.sampleCompositionTimeOffset;
        }

        public void setSampleDuration(long sampleDuration2) {
            this.sampleDuration = sampleDuration2;
        }

        public void setSampleSize(long sampleSize2) {
            this.sampleSize = sampleSize2;
        }

        public void setSampleFlags(SampleFlags sampleFlags2) {
            this.sampleFlags = sampleFlags2;
        }

        public void setSampleCompositionTimeOffset(int sampleCompositionTimeOffset2) {
            this.sampleCompositionTimeOffset = (long) sampleCompositionTimeOffset2;
        }

        public String toString() {
            return "Entry{duration=" + this.sampleDuration + ", size=" + this.sampleSize + ", dlags=" + this.sampleFlags + ", compTimeOffset=" + this.sampleCompositionTimeOffset + '}';
        }
    }

    public void setDataOffset(int dataOffset2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(dataOffset2)));
        if (dataOffset2 == -1) {
            setFlags(getFlags() & 16777214);
        } else {
            setFlags(getFlags() | 1);
        }
        this.dataOffset = dataOffset2;
    }

    public long[] getSampleCompositionTimeOffsets() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        if (!isSampleCompositionTimeOffsetPresent()) {
            return null;
        }
        long[] result = new long[this.entries.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = this.entries.get(i).getSampleCompositionTimeOffset();
        }
        return result;
    }

    public TrackRunBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size = 8;
        int flags = getFlags();
        if ((flags & 1) == 1) {
            size = 8 + 4;
        }
        if ((flags & 4) == 4) {
            size += 4;
        }
        long entrySize = 0;
        if ((flags & 256) == 256) {
            entrySize = 0 + 4;
        }
        if ((flags & 512) == 512) {
            entrySize += 4;
        }
        if ((flags & 1024) == 1024) {
            entrySize += 4;
        }
        if ((flags & 2048) == 2048) {
            entrySize += 4;
        }
        return size + (((long) this.entries.size()) * entrySize);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        int flags = getFlags();
        if ((flags & 1) == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.dataOffset);
        }
        if ((flags & 4) == 4) {
            this.firstSampleFlags.getContent(byteBuffer);
        }
        for (Entry entry : this.entries) {
            if ((flags & 256) == 256) {
                IsoTypeWriter.writeUInt32(byteBuffer, entry.sampleDuration);
            }
            if ((flags & 512) == 512) {
                IsoTypeWriter.writeUInt32(byteBuffer, entry.sampleSize);
            }
            if ((flags & 1024) == 1024) {
                entry.sampleFlags.getContent(byteBuffer);
            }
            if ((flags & 2048) == 2048) {
                if (getVersion() == 0) {
                    IsoTypeWriter.writeUInt32(byteBuffer, entry.sampleCompositionTimeOffset);
                } else {
                    byteBuffer.putInt((int) entry.sampleCompositionTimeOffset);
                }
            }
        }
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        long sampleCount = IsoTypeReader.readUInt32(content);
        if ((getFlags() & 1) == 1) {
            this.dataOffset = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        } else {
            this.dataOffset = -1;
        }
        if ((getFlags() & 4) == 4) {
            this.firstSampleFlags = new SampleFlags(content);
        }
        for (int i = 0; ((long) i) < sampleCount; i++) {
            Entry entry = new Entry();
            if ((getFlags() & 256) == 256) {
                entry.sampleDuration = IsoTypeReader.readUInt32(content);
            }
            if ((getFlags() & 512) == 512) {
                entry.sampleSize = IsoTypeReader.readUInt32(content);
            }
            if ((getFlags() & 1024) == 1024) {
                entry.sampleFlags = new SampleFlags(content);
            }
            if ((getFlags() & 2048) == 2048) {
                entry.sampleCompositionTimeOffset = (long) content.getInt();
            }
            this.entries.add(entry);
        }
    }

    public long getSampleCount() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return (long) this.entries.size();
    }

    public boolean isDataOffsetPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return (getFlags() & 1) == 1;
    }

    public boolean isFirstSampleFlagsPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return (getFlags() & 4) == 4;
    }

    public boolean isSampleSizePresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return (getFlags() & 512) == 512;
    }

    public boolean isSampleDurationPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return (getFlags() & 256) == 256;
    }

    public boolean isSampleFlagsPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return (getFlags() & 1024) == 1024;
    }

    public boolean isSampleCompositionTimeOffsetPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return (getFlags() & 2048) == 2048;
    }

    public void setDataOffsetPresent(boolean v) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, Conversions.booleanObject(v)));
        if (v) {
            setFlags(getFlags() | 1);
        } else {
            setFlags(getFlags() & 16777214);
        }
    }

    public void setSampleSizePresent(boolean v) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.booleanObject(v)));
        if (v) {
            setFlags(getFlags() | 512);
        } else {
            setFlags(getFlags() & 16776703);
        }
    }

    public void setSampleDurationPresent(boolean v) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, Conversions.booleanObject(v)));
        if (v) {
            setFlags(getFlags() | 256);
        } else {
            setFlags(getFlags() & 16776959);
        }
    }

    public void setSampleFlagsPresent(boolean v) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.booleanObject(v)));
        if (v) {
            setFlags(getFlags() | 1024);
        } else {
            setFlags(getFlags() & 16776191);
        }
    }

    public void setSampleCompositionTimeOffsetPresent(boolean v) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.booleanObject(v)));
        if (v) {
            setFlags(getFlags() | 2048);
        } else {
            setFlags(getFlags() & 16775167);
        }
    }

    public int getDataOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, this, this));
        return this.dataOffset;
    }

    public SampleFlags getFirstSampleFlags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.firstSampleFlags;
    }

    public void setFirstSampleFlags(SampleFlags firstSampleFlags2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, (Object) firstSampleFlags2));
        if (firstSampleFlags2 == null) {
            setFlags(getFlags() & 16777211);
        } else {
            setFlags(getFlags() | 4);
        }
        this.firstSampleFlags = firstSampleFlags2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        StringBuilder sb = new StringBuilder();
        sb.append("TrackRunBox");
        sb.append("{sampleCount=").append(this.entries.size());
        sb.append(", dataOffset=").append(this.dataOffset);
        sb.append(", dataOffsetPresent=").append(isDataOffsetPresent());
        sb.append(", sampleSizePresent=").append(isSampleSizePresent());
        sb.append(", sampleDurationPresent=").append(isSampleDurationPresent());
        sb.append(", sampleFlagsPresentPresent=").append(isSampleFlagsPresent());
        sb.append(", sampleCompositionTimeOffsetPresent=").append(isSampleCompositionTimeOffsetPresent());
        sb.append(", firstSampleFlags=").append(this.firstSampleFlags);
        sb.append('}');
        return sb.toString();
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }
}
