package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SubSampleInformationBox extends AbstractFullBox {
    public static final String TYPE = "subs";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private List<SubSampleEntry> entries = new ArrayList();

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SubSampleInformationBox.java", SubSampleInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", "", "", "", "java.util.List"), 50);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.SubSampleInformationBox", "java.util.List", "entries", "", "void"), 54);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SubSampleInformationBox", "", "", "", "java.lang.String"), 124);
    }

    public SubSampleInformationBox() {
        super(TYPE);
    }

    public List<SubSampleEntry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<SubSampleEntry> entries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries2));
        this.entries = entries2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size;
        long size2 = 8;
        for (SubSampleEntry entry : this.entries) {
            size2 = size2 + 4 + 2;
            for (int j = 0; j < entry.getSubsampleEntries().size(); j++) {
                if (getVersion() == 1) {
                    size = size2 + 4;
                } else {
                    size = size2 + 2;
                }
                size2 = size + 2 + 4;
            }
        }
        return size2;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        long entryCount = IsoTypeReader.readUInt32(content);
        for (int i = 0; ((long) i) < entryCount; i++) {
            SubSampleEntry SubSampleEntry2 = new SubSampleEntry();
            SubSampleEntry2.setSampleDelta(IsoTypeReader.readUInt32(content));
            int subsampleCount = IsoTypeReader.readUInt16(content);
            for (int j = 0; j < subsampleCount; j++) {
                SubSampleEntry.SubsampleEntry subsampleEntry = new SubSampleEntry.SubsampleEntry();
                subsampleEntry.setSubsampleSize(getVersion() == 1 ? IsoTypeReader.readUInt32(content) : (long) IsoTypeReader.readUInt16(content));
                subsampleEntry.setSubsamplePriority(IsoTypeReader.readUInt8(content));
                subsampleEntry.setDiscardable(IsoTypeReader.readUInt8(content));
                subsampleEntry.setReserved(IsoTypeReader.readUInt32(content));
                SubSampleEntry2.getSubsampleEntries().add(subsampleEntry);
            }
            this.entries.add(SubSampleEntry2);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (SubSampleEntry subSampleEntry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, subSampleEntry.getSampleDelta());
            IsoTypeWriter.writeUInt16(byteBuffer, subSampleEntry.getSubsampleCount());
            for (SubSampleEntry.SubsampleEntry subsampleEntry : subSampleEntry.getSubsampleEntries()) {
                if (getVersion() == 1) {
                    IsoTypeWriter.writeUInt32(byteBuffer, subsampleEntry.getSubsampleSize());
                } else {
                    IsoTypeWriter.writeUInt16(byteBuffer, CastUtils.l2i(subsampleEntry.getSubsampleSize()));
                }
                IsoTypeWriter.writeUInt8(byteBuffer, subsampleEntry.getSubsamplePriority());
                IsoTypeWriter.writeUInt8(byteBuffer, subsampleEntry.getDiscardable());
                IsoTypeWriter.writeUInt32(byteBuffer, subsampleEntry.getReserved());
            }
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "SubSampleInformationBox{entryCount=" + this.entries.size() + ", entries=" + this.entries + '}';
    }

    public static class SubSampleEntry {
        private long sampleDelta;
        private List<SubsampleEntry> subsampleEntries = new ArrayList();

        public long getSampleDelta() {
            return this.sampleDelta;
        }

        public void setSampleDelta(long sampleDelta2) {
            this.sampleDelta = sampleDelta2;
        }

        public int getSubsampleCount() {
            return this.subsampleEntries.size();
        }

        public List<SubsampleEntry> getSubsampleEntries() {
            return this.subsampleEntries;
        }

        public static class SubsampleEntry {
            private int discardable;
            private long reserved;
            private int subsamplePriority;
            private long subsampleSize;

            public long getSubsampleSize() {
                return this.subsampleSize;
            }

            public void setSubsampleSize(long subsampleSize2) {
                this.subsampleSize = subsampleSize2;
            }

            public int getSubsamplePriority() {
                return this.subsamplePriority;
            }

            public void setSubsamplePriority(int subsamplePriority2) {
                this.subsamplePriority = subsamplePriority2;
            }

            public int getDiscardable() {
                return this.discardable;
            }

            public void setDiscardable(int discardable2) {
                this.discardable = discardable2;
            }

            public long getReserved() {
                return this.reserved;
            }

            public void setReserved(long reserved2) {
                this.reserved = reserved2;
            }

            public String toString() {
                return "SubsampleEntry{subsampleSize=" + this.subsampleSize + ", subsamplePriority=" + this.subsamplePriority + ", discardable=" + this.discardable + ", reserved=" + this.reserved + '}';
            }
        }

        public String toString() {
            return "SampleEntry{sampleDelta=" + this.sampleDelta + ", subsampleCount=" + this.subsampleEntries.size() + ", subsampleEntries=" + this.subsampleEntries + '}';
        }
    }
}
