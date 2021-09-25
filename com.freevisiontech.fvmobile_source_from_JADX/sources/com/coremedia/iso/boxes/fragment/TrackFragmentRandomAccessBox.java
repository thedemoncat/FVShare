package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeReaderVariable;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.IsoTypeWriterVariable;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackFragmentRandomAccessBox extends AbstractFullBox {
    public static final String TYPE = "tfra";
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
    private List<Entry> entries = Collections.emptyList();
    private int lengthSizeOfSampleNum = 2;
    private int lengthSizeOfTrafNum = 2;
    private int lengthSizeOfTrunNum = 2;
    private int reserved;
    private long trackId;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("TrackFragmentRandomAccessBox.java", TrackFragmentRandomAccessBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "long", "trackId", "", "void"), 145);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLengthSizeOfTrafNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfTrafNum", "", "void"), 149);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "java.util.List"), (int) CompanyIdentifierResolver.JOHNSON_CONTROLS_INC);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "java.util.List", "entries", "", "void"), 189);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.AIRTURN_INC);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLengthSizeOfTrunNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfTrunNum", "", "void"), 153);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLengthSizeOfSampleNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "int", "lengthSizeOfSampleNum", "", "void"), (int) CompanyIdentifierResolver.GEOFORCE_INC);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "long"), (int) CompanyIdentifierResolver.SRMEDIZINELEKTRONIK);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) CompanyIdentifierResolver.OTL_DYNAMICS_LLC);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLengthSizeOfTrafNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) CompanyIdentifierResolver.MAGNETI_MARELLI_SPA);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLengthSizeOfTrunNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) CompanyIdentifierResolver.PETER_SYSTEMTECHNIK_GMBH);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLengthSizeOfSampleNum", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "int"), (int) CompanyIdentifierResolver.SARIS_CYCLING_GROUP_INC);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getNumberOfEntries", "com.coremedia.iso.boxes.fragment.TrackFragmentRandomAccessBox", "", "", "", "long"), (int) CompanyIdentifierResolver.SWIRL_NETWORKS);
    }

    public TrackFragmentRandomAccessBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long contentSize;
        long contentSize2 = 4 + 12;
        if (getVersion() == 1) {
            contentSize = contentSize2 + ((long) (this.entries.size() * 16));
        } else {
            contentSize = contentSize2 + ((long) (this.entries.size() * 8));
        }
        return contentSize + ((long) (this.lengthSizeOfTrafNum * this.entries.size())) + ((long) (this.lengthSizeOfTrunNum * this.entries.size())) + ((long) (this.lengthSizeOfSampleNum * this.entries.size()));
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.trackId = IsoTypeReader.readUInt32(content);
        long temp = IsoTypeReader.readUInt32(content);
        this.reserved = (int) (temp >> 6);
        this.lengthSizeOfTrafNum = (((int) (63 & temp)) >> 4) + 1;
        this.lengthSizeOfTrunNum = (((int) (12 & temp)) >> 2) + 1;
        this.lengthSizeOfSampleNum = ((int) (3 & temp)) + 1;
        long numberOfEntries = IsoTypeReader.readUInt32(content);
        this.entries = new ArrayList();
        for (int i = 0; ((long) i) < numberOfEntries; i++) {
            Entry entry = new Entry();
            if (getVersion() == 1) {
                entry.time = IsoTypeReader.readUInt64(content);
                entry.moofOffset = IsoTypeReader.readUInt64(content);
            } else {
                entry.time = IsoTypeReader.readUInt32(content);
                entry.moofOffset = IsoTypeReader.readUInt32(content);
            }
            entry.trafNumber = IsoTypeReaderVariable.read(content, this.lengthSizeOfTrafNum);
            entry.trunNumber = IsoTypeReaderVariable.read(content, this.lengthSizeOfTrunNum);
            entry.sampleNumber = IsoTypeReaderVariable.read(content, this.lengthSizeOfSampleNum);
            this.entries.add(entry);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
        IsoTypeWriter.writeUInt32(byteBuffer, ((long) (this.reserved << 6)) | ((long) (((this.lengthSizeOfTrafNum - 1) & 3) << 4)) | ((long) (((this.lengthSizeOfTrunNum - 1) & 3) << 2)) | ((long) ((this.lengthSizeOfSampleNum - 1) & 3)));
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            if (getVersion() == 1) {
                IsoTypeWriter.writeUInt64(byteBuffer, entry.time);
                IsoTypeWriter.writeUInt64(byteBuffer, entry.moofOffset);
            } else {
                IsoTypeWriter.writeUInt32(byteBuffer, entry.time);
                IsoTypeWriter.writeUInt32(byteBuffer, entry.moofOffset);
            }
            IsoTypeWriterVariable.write(entry.trafNumber, byteBuffer, this.lengthSizeOfTrafNum);
            IsoTypeWriterVariable.write(entry.trunNumber, byteBuffer, this.lengthSizeOfTrunNum);
            IsoTypeWriterVariable.write(entry.sampleNumber, byteBuffer, this.lengthSizeOfSampleNum);
        }
    }

    public void setTrackId(long trackId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, (Object) this, (Object) this, Conversions.longObject(trackId2)));
        this.trackId = trackId2;
    }

    public void setLengthSizeOfTrafNum(int lengthSizeOfTrafNum2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(lengthSizeOfTrafNum2)));
        this.lengthSizeOfTrafNum = lengthSizeOfTrafNum2;
    }

    public void setLengthSizeOfTrunNum(int lengthSizeOfTrunNum2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, Conversions.intObject(lengthSizeOfTrunNum2)));
        this.lengthSizeOfTrunNum = lengthSizeOfTrunNum2;
    }

    public void setLengthSizeOfSampleNum(int lengthSizeOfSampleNum2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(lengthSizeOfSampleNum2)));
        this.lengthSizeOfSampleNum = lengthSizeOfSampleNum2;
    }

    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.trackId;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.reserved;
    }

    public int getLengthSizeOfTrafNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.lengthSizeOfTrafNum;
    }

    public int getLengthSizeOfTrunNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.lengthSizeOfTrunNum;
    }

    public int getLengthSizeOfSampleNum() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.lengthSizeOfSampleNum;
    }

    public long getNumberOfEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return (long) this.entries.size();
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return Collections.unmodifiableList(this.entries);
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public long moofOffset;
        /* access modifiers changed from: private */
        public long sampleNumber;
        /* access modifiers changed from: private */
        public long time;
        /* access modifiers changed from: private */
        public long trafNumber;
        /* access modifiers changed from: private */
        public long trunNumber;

        public Entry() {
        }

        public Entry(long time2, long moofOffset2, long trafNumber2, long trunNumber2, long sampleNumber2) {
            this.moofOffset = moofOffset2;
            this.sampleNumber = sampleNumber2;
            this.time = time2;
            this.trafNumber = trafNumber2;
            this.trunNumber = trunNumber2;
        }

        public long getTime() {
            return this.time;
        }

        public long getMoofOffset() {
            return this.moofOffset;
        }

        public long getTrafNumber() {
            return this.trafNumber;
        }

        public long getTrunNumber() {
            return this.trunNumber;
        }

        public long getSampleNumber() {
            return this.sampleNumber;
        }

        public void setTime(long time2) {
            this.time = time2;
        }

        public void setMoofOffset(long moofOffset2) {
            this.moofOffset = moofOffset2;
        }

        public void setTrafNumber(long trafNumber2) {
            this.trafNumber = trafNumber2;
        }

        public void setTrunNumber(long trunNumber2) {
            this.trunNumber = trunNumber2;
        }

        public void setSampleNumber(long sampleNumber2) {
            this.sampleNumber = sampleNumber2;
        }

        public String toString() {
            return "Entry{time=" + this.time + ", moofOffset=" + this.moofOffset + ", trafNumber=" + this.trafNumber + ", trunNumber=" + this.trunNumber + ", sampleNumber=" + this.sampleNumber + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.moofOffset != entry.moofOffset) {
                return false;
            }
            if (this.sampleNumber != entry.sampleNumber) {
                return false;
            }
            if (this.time != entry.time) {
                return false;
            }
            if (this.trafNumber != entry.trafNumber) {
                return false;
            }
            if (this.trunNumber != entry.trunNumber) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((((((((int) (this.time ^ (this.time >>> 32))) * 31) + ((int) (this.moofOffset ^ (this.moofOffset >>> 32)))) * 31) + ((int) (this.trafNumber ^ (this.trafNumber >>> 32)))) * 31) + ((int) (this.trunNumber ^ (this.trunNumber >>> 32)))) * 31) + ((int) (this.sampleNumber ^ (this.sampleNumber >>> 32)));
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return "TrackFragmentRandomAccessBox{trackId=" + this.trackId + ", entries=" + this.entries + '}';
    }
}
