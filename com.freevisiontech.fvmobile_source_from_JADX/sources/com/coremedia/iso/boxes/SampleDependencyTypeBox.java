package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleDependencyTypeBox extends AbstractFullBox {
    public static final String TYPE = "sdtp";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private List<Entry> entries = new ArrayList();

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleDependencyTypeBox.java", SampleDependencyTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEntries", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "", "", "", "java.util.List"), 139);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEntries", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "java.util.List", "entries", "", "void"), (int) CompanyIdentifierResolver.STOLLMAN_EV_GMBH);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SampleDependencyTypeBox", "", "", "", "java.lang.String"), 148);
    }

    public static class Entry {
        /* access modifiers changed from: private */
        public int value;

        public Entry(int value2) {
            this.value = value2;
        }

        public int getIsLeading() {
            return (this.value >> 6) & 3;
        }

        public void setIsLeading(int res) {
            this.value = ((res & 3) << 6) | (this.value & 63);
        }

        public int getSampleDependsOn() {
            return (this.value >> 4) & 3;
        }

        public void setSampleDependsOn(int sdo) {
            this.value = ((sdo & 3) << 4) | (this.value & CompanyIdentifierResolver.ARCHOS_SA);
        }

        public int getSampleIsDependentOn() {
            return (this.value >> 2) & 3;
        }

        public void setSampleIsDependentOn(int sido) {
            this.value = ((sido & 3) << 2) | (this.value & CompanyIdentifierResolver.KENT_DISPLAYS_INC);
        }

        public int getSampleHasRedundancy() {
            return this.value & 3;
        }

        public void setSampleHasRedundancy(int shr) {
            this.value = (shr & 3) | (this.value & CompanyIdentifierResolver.DELPHI_CORPORATION);
        }

        public String toString() {
            return "Entry{isLeading=" + getIsLeading() + ", sampleDependsOn=" + getSampleDependsOn() + ", sampleIsDependentOn=" + getSampleIsDependentOn() + ", sampleHasRedundancy=" + getSampleHasRedundancy() + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (this.value != ((Entry) o).value) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.value;
        }
    }

    public SampleDependencyTypeBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (this.entries.size() + 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt8(byteBuffer, entry.value);
        }
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        while (content.remaining() > 0) {
            this.entries.add(new Entry(IsoTypeReader.readUInt8(content)));
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
        StringBuilder sb = new StringBuilder();
        sb.append("SampleDependencyTypeBox");
        sb.append("{entries=").append(this.entries);
        sb.append('}');
        return sb.toString();
    }
}
