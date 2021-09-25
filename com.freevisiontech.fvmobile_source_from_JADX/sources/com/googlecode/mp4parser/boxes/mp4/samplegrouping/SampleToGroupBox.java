package com.googlecode.mp4parser.boxes.mp4.samplegrouping;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleToGroupBox extends AbstractFullBox {
    public static final String TYPE = "sbgp";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    List<Entry> entries = new LinkedList();
    private String groupingType;
    private String groupingTypeParameter;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleToGroupBox.java", SampleToGroupBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.lang.String"), 150);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setGroupingType", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.lang.String", "groupingType", "", "void"), (int) CompanyIdentifierResolver.ALPWISE);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGroupingTypeParameter", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.BOSE_CORPORATION);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setGroupingTypeParameter", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.lang.String", "groupingTypeParameter", "", "void"), (int) CompanyIdentifierResolver.VERTU_CORPORATION_LIMITED);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "", "", "", "java.util.List"), (int) CompanyIdentifierResolver.PANDA_OCEAN_INC);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox", "java.util.List", "entries", "", "void"), (int) CompanyIdentifierResolver.CAEN_RFID_SRL);
    }

    public SampleToGroupBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (getVersion() == 1 ? (this.entries.size() * 8) + 16 : (this.entries.size() * 8) + 12);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(this.groupingType.getBytes());
        if (getVersion() == 1) {
            byteBuffer.put(this.groupingTypeParameter.getBytes());
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getSampleCount());
            IsoTypeWriter.writeUInt32(byteBuffer, (long) entry.getGroupDescriptionIndex());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.groupingType = IsoTypeReader.read4cc(content);
        if (getVersion() == 1) {
            this.groupingTypeParameter = IsoTypeReader.read4cc(content);
        }
        long entryCount = IsoTypeReader.readUInt32(content);
        while (true) {
            long entryCount2 = entryCount;
            entryCount = entryCount2 - 1;
            if (entryCount2 > 0) {
                this.entries.add(new Entry((long) CastUtils.l2i(IsoTypeReader.readUInt32(content)), CastUtils.l2i(IsoTypeReader.readUInt32(content))));
            } else {
                return;
            }
        }
    }

    public static class Entry {
        private int groupDescriptionIndex;
        private long sampleCount;

        public Entry(long sampleCount2, int groupDescriptionIndex2) {
            this.sampleCount = sampleCount2;
            this.groupDescriptionIndex = groupDescriptionIndex2;
        }

        public long getSampleCount() {
            return this.sampleCount;
        }

        public void setSampleCount(long sampleCount2) {
            this.sampleCount = sampleCount2;
        }

        public int getGroupDescriptionIndex() {
            return this.groupDescriptionIndex;
        }

        public void setGroupDescriptionIndex(int groupDescriptionIndex2) {
            this.groupDescriptionIndex = groupDescriptionIndex2;
        }

        public String toString() {
            return "Entry{sampleCount=" + this.sampleCount + ", groupDescriptionIndex=" + this.groupDescriptionIndex + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (this.groupDescriptionIndex != entry.groupDescriptionIndex) {
                return false;
            }
            if (this.sampleCount != entry.sampleCount) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((int) (this.sampleCount ^ (this.sampleCount >>> 32))) * 31) + this.groupDescriptionIndex;
        }
    }

    public String getGroupingType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.groupingType;
    }

    public void setGroupingType(String groupingType2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) groupingType2));
        this.groupingType = groupingType2;
    }

    public String getGroupingTypeParameter() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.groupingTypeParameter;
    }

    public void setGroupingTypeParameter(String groupingTypeParameter2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) groupingTypeParameter2));
        this.groupingTypeParameter = groupingTypeParameter2;
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }
}
