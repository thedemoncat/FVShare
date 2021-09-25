package com.googlecode.mp4parser.boxes.threegpp26244;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SegmentIndexBox extends AbstractFullBox {
    public static final String TYPE = "sidx";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    long earliestPresentationTime;
    List<Entry> entries = new ArrayList();
    long firstOffset;
    long referenceId;
    int reserved;
    long timeScale;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SegmentIndexBox.java", SegmentIndexBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "java.util.List"), 128);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "java.util.List", "entries", "", "void"), 132);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "int"), (int) CompanyIdentifierResolver.ARP_DEVICES_LIMITED);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "int", "reserved", "", "void"), (int) CompanyIdentifierResolver.GREEN_THROTTLE_GAMES);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.CHANGZHOU_YONGSE_INFOTECH_CO_LTD);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReferenceId", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 136);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReferenceId", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "referenceId", "", "void"), (int) CompanyIdentifierResolver.QUALCOMM_RETAIL_SOLUTIONS_INC_FORMERLY_QUALCOMM_LABS_INC);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTimeScale", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 144);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTimeScale", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "timeScale", "", "void"), 148);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEarliestPresentationTime", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 152);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEarliestPresentationTime", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "earliestPresentationTime", "", "void"), (int) CompanyIdentifierResolver.COLORFY_INC);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFirstOffset", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "", "", "", "long"), 160);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFirstOffset", "com.googlecode.mp4parser.boxes.threegpp26244.SegmentIndexBox", "long", "firstOffset", "", "void"), (int) CompanyIdentifierResolver.LINAK_A_S);
    }

    public SegmentIndexBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 4 + 4 + 4 + ((long) (getVersion() == 0 ? 8 : 16)) + 2 + 2 + ((long) (this.entries.size() * 12));
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.referenceId);
        IsoTypeWriter.writeUInt32(byteBuffer, this.timeScale);
        if (getVersion() == 0) {
            IsoTypeWriter.writeUInt32(byteBuffer, this.earliestPresentationTime);
            IsoTypeWriter.writeUInt32(byteBuffer, this.firstOffset);
        } else {
            IsoTypeWriter.writeUInt64(byteBuffer, this.earliestPresentationTime);
            IsoTypeWriter.writeUInt64(byteBuffer, this.firstOffset);
        }
        IsoTypeWriter.writeUInt16(byteBuffer, this.reserved);
        IsoTypeWriter.writeUInt16(byteBuffer, this.entries.size());
        for (Entry entry : this.entries) {
            BitWriterBuffer b = new BitWriterBuffer(byteBuffer);
            b.writeBits(entry.getReferenceType(), 1);
            b.writeBits(entry.getReferencedSize(), 31);
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSubsegmentDuration());
            BitWriterBuffer b2 = new BitWriterBuffer(byteBuffer);
            b2.writeBits(entry.getStartsWithSap(), 1);
            b2.writeBits(entry.getSapType(), 3);
            b2.writeBits(entry.getSapDeltaTime(), 28);
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.referenceId = IsoTypeReader.readUInt32(content);
        this.timeScale = IsoTypeReader.readUInt32(content);
        if (getVersion() == 0) {
            this.earliestPresentationTime = IsoTypeReader.readUInt32(content);
            this.firstOffset = IsoTypeReader.readUInt32(content);
        } else {
            this.earliestPresentationTime = IsoTypeReader.readUInt64(content);
            this.firstOffset = IsoTypeReader.readUInt64(content);
        }
        this.reserved = IsoTypeReader.readUInt16(content);
        int numEntries = IsoTypeReader.readUInt16(content);
        for (int i = 0; i < numEntries; i++) {
            BitReaderBuffer b = new BitReaderBuffer(content);
            Entry e = new Entry();
            e.setReferenceType((byte) b.readBits(1));
            e.setReferencedSize(b.readBits(31));
            e.setSubsegmentDuration(IsoTypeReader.readUInt32(content));
            BitReaderBuffer b2 = new BitReaderBuffer(content);
            e.setStartsWithSap((byte) b2.readBits(1));
            e.setSapType((byte) b2.readBits(3));
            e.setSapDeltaTime(b2.readBits(28));
            this.entries.add(e);
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

    public long getReferenceId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.referenceId;
    }

    public void setReferenceId(long referenceId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.longObject(referenceId2)));
        this.referenceId = referenceId2;
    }

    public long getTimeScale() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.timeScale;
    }

    public void setTimeScale(long timeScale2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.longObject(timeScale2)));
        this.timeScale = timeScale2;
    }

    public long getEarliestPresentationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.earliestPresentationTime;
    }

    public void setEarliestPresentationTime(long earliestPresentationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.longObject(earliestPresentationTime2)));
        this.earliestPresentationTime = earliestPresentationTime2;
    }

    public long getFirstOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.firstOffset;
    }

    public void setFirstOffset(long firstOffset2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.longObject(firstOffset2)));
        this.firstOffset = firstOffset2;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.reserved;
    }

    public void setReserved(int reserved2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.intObject(reserved2)));
        this.reserved = reserved2;
    }

    public static class Entry {
        byte referenceType;
        int referencedSize;
        int sapDeltaTime;
        byte sapType;
        byte startsWithSap;
        long subsegmentDuration;

        public Entry() {
        }

        public Entry(int referenceType2, int referencedSize2, long subsegmentDuration2, boolean startsWithSap2, int sapType2, int sapDeltaTime2) {
            this.referenceType = (byte) referenceType2;
            this.referencedSize = referencedSize2;
            this.subsegmentDuration = subsegmentDuration2;
            this.startsWithSap = (byte) (startsWithSap2 ? 1 : 0);
            this.sapType = (byte) sapType2;
            this.sapDeltaTime = sapDeltaTime2;
        }

        public byte getReferenceType() {
            return this.referenceType;
        }

        public void setReferenceType(byte referenceType2) {
            this.referenceType = referenceType2;
        }

        public int getReferencedSize() {
            return this.referencedSize;
        }

        public void setReferencedSize(int referencedSize2) {
            this.referencedSize = referencedSize2;
        }

        public long getSubsegmentDuration() {
            return this.subsegmentDuration;
        }

        public void setSubsegmentDuration(long subsegmentDuration2) {
            this.subsegmentDuration = subsegmentDuration2;
        }

        public byte getStartsWithSap() {
            return this.startsWithSap;
        }

        public void setStartsWithSap(byte startsWithSap2) {
            this.startsWithSap = startsWithSap2;
        }

        public byte getSapType() {
            return this.sapType;
        }

        public void setSapType(byte sapType2) {
            this.sapType = sapType2;
        }

        public int getSapDeltaTime() {
            return this.sapDeltaTime;
        }

        public void setSapDeltaTime(int sapDeltaTime2) {
            this.sapDeltaTime = sapDeltaTime2;
        }

        public String toString() {
            return "Entry{referenceType=" + this.referenceType + ", referencedSize=" + this.referencedSize + ", subsegmentDuration=" + this.subsegmentDuration + ", startsWithSap=" + this.startsWithSap + ", sapType=" + this.sapType + ", sapDeltaTime=" + this.sapDeltaTime + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.referenceType != entry.referenceType) {
                return false;
            }
            if (this.referencedSize != entry.referencedSize) {
                return false;
            }
            if (this.sapDeltaTime != entry.sapDeltaTime) {
                return false;
            }
            if (this.sapType != entry.sapType) {
                return false;
            }
            if (this.startsWithSap != entry.startsWithSap) {
                return false;
            }
            if (this.subsegmentDuration != entry.subsegmentDuration) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((((((this.referenceType * 31) + this.referencedSize) * 31) + ((int) (this.subsegmentDuration ^ (this.subsegmentDuration >>> 32)))) * 31) + this.startsWithSap) * 31) + this.sapType) * 31) + this.sapDeltaTime;
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return "SegmentIndexBox{entries=" + this.entries + ", referenceId=" + this.referenceId + ", timeScale=" + this.timeScale + ", earliestPresentationTime=" + this.earliestPresentationTime + ", firstOffset=" + this.firstOffset + ", reserved=" + this.reserved + '}';
    }
}
