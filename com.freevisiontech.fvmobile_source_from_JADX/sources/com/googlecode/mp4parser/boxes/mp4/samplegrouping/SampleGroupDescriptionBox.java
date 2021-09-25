package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import com.mp4parser.iso14496.part15.StepwiseTemporalLayerEntry;
import com.mp4parser.iso14496.part15.SyncSampleEntry;
import com.mp4parser.iso14496.part15.TemporalLayerSampleGroup;
import com.mp4parser.iso14496.part15.TemporalSubLayerSampleGroup;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleGroupDescriptionBox extends AbstractFullBox {
    public static final String TYPE = "sgpd";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private int defaultLength;
    private List<GroupEntry> groupEntries = new LinkedList();
    private String groupingType;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleGroupDescriptionBox.java", SampleGroupDescriptionBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.lang.String"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.lang.String", "groupingType", "", "void"), 61);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDefaultLength", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "int"), 153);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultLength", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "int", "defaultLength", "", "void"), (int) CompanyIdentifierResolver.GEOFORCE_INC);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGroupEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.util.List"), (int) CompanyIdentifierResolver.SRMEDIZINELEKTRONIK);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setGroupEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.util.List", "groupEntries", "", "void"), (int) CompanyIdentifierResolver.OTL_DYNAMICS_LLC);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "equals", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "java.lang.Object", "o", "", "boolean"), (int) CompanyIdentifierResolver.CAEN_RFID_SRL);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hashCode", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "int"), (int) CompanyIdentifierResolver.STALMART_TECHNOLOGY_LIMITED);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.QUUPPA_OY);
    }

    public SampleGroupDescriptionBox() {
        super(TYPE);
        setVersion(1);
    }

    public String getGroupingType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.groupingType;
    }

    public void setGroupingType(String groupingType2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) groupingType2));
        this.groupingType = groupingType2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size = 8;
        if (getVersion() == 1) {
            size = 8 + 4;
        }
        long size2 = size + 4;
        for (GroupEntry groupEntry : this.groupEntries) {
            if (getVersion() == 1 && this.defaultLength == 0) {
                size2 += 4;
            }
            size2 += (long) groupEntry.size();
        }
        return size2;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(IsoFile.fourCCtoBytes(this.groupingType));
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.defaultLength);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.groupEntries.size());
        for (GroupEntry entry : this.groupEntries) {
            if (getVersion() == 1 && this.defaultLength == 0) {
                IsoTypeWriter.writeUInt32(byteBuffer, (long) entry.get().limit());
            }
            byteBuffer.put(entry.get());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        if (getVersion() != 1) {
            throw new RuntimeException("SampleGroupDescriptionBox are only supported in version 1");
        }
        this.groupingType = IsoTypeReader.read4cc(content);
        if (getVersion() == 1) {
            this.defaultLength = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        }
        long entryCount = IsoTypeReader.readUInt32(content);
        while (true) {
            long entryCount2 = entryCount;
            entryCount = entryCount2 - 1;
            if (entryCount2 > 0) {
                int length = this.defaultLength;
                if (getVersion() == 1) {
                    if (this.defaultLength == 0) {
                        length = CastUtils.l2i(IsoTypeReader.readUInt32(content));
                    }
                    int finalPos = content.position() + length;
                    ByteBuffer parseMe = content.slice();
                    parseMe.limit(length);
                    this.groupEntries.add(parseGroupEntry(parseMe, this.groupingType));
                    content.position(finalPos);
                } else {
                    throw new RuntimeException("This should be implemented");
                }
            } else {
                return;
            }
        }
    }

    private GroupEntry parseGroupEntry(ByteBuffer content, String groupingType2) {
        GroupEntry unknownEntry;
        if (RollRecoveryEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new RollRecoveryEntry();
        } else if (RateShareEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new RateShareEntry();
        } else if (CencSampleEncryptionInformationGroupEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new CencSampleEncryptionInformationGroupEntry();
        } else if (VisualRandomAccessEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new VisualRandomAccessEntry();
        } else if (TemporalLevelEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new TemporalLevelEntry();
        } else if (SyncSampleEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new SyncSampleEntry();
        } else if (TemporalLayerSampleGroup.TYPE.equals(groupingType2)) {
            unknownEntry = new TemporalLayerSampleGroup();
        } else if (TemporalSubLayerSampleGroup.TYPE.equals(groupingType2)) {
            unknownEntry = new TemporalSubLayerSampleGroup();
        } else if (StepwiseTemporalLayerEntry.TYPE.equals(groupingType2)) {
            unknownEntry = new StepwiseTemporalLayerEntry();
        } else {
            unknownEntry = new UnknownEntry(groupingType2);
        }
        unknownEntry.parse(content);
        return unknownEntry;
    }

    public int getDefaultLength() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.defaultLength;
    }

    public void setDefaultLength(int defaultLength2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(defaultLength2)));
        this.defaultLength = defaultLength2;
    }

    public List<GroupEntry> getGroupEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.groupEntries;
    }

    public void setGroupEntries(List<GroupEntry> groupEntries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) groupEntries2));
        this.groupEntries = groupEntries2;
    }

    public boolean equals(Object o) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, o));
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleGroupDescriptionBox that = (SampleGroupDescriptionBox) o;
        if (this.defaultLength != that.defaultLength) {
            return false;
        }
        if (this.groupEntries != null) {
            if (this.groupEntries.equals(that.groupEntries)) {
                return true;
            }
        } else if (that.groupEntries == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return ((this.defaultLength + 0) * 31) + (this.groupEntries != null ? this.groupEntries.hashCode() : 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return "SampleGroupDescriptionBox{groupingType='" + (this.groupEntries.size() > 0 ? this.groupEntries.get(0).getType() : "????") + '\'' + ", defaultLength=" + this.defaultLength + ", groupEntries=" + this.groupEntries + '}';
    }
}
