package com.googlecode.mp4parser.boxes.dece;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AssetInformationBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = (!AssetInformationBox.class.desiredAssertionStatus());
    public static final String TYPE = "ainf";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    String apid = "";
    String profileVersion = "0000";

    private static void ajc$preClinit() {
        Factory factory = new Factory("AssetInformationBox.java", AssetInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getApid", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "", "", "", "java.lang.String"), 131);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setApid", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "java.lang.String", "apid", "", "void"), 135);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProfileVersion", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "", "", "", "java.lang.String"), 139);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProfileVersion", "com.googlecode.mp4parser.boxes.dece.AssetInformationBox", "java.lang.String", "profileVersion", "", "void"), (int) CompanyIdentifierResolver.STOLLMAN_EV_GMBH);
    }

    static {
        ajc$preClinit();
    }

    public static class Entry {
        public String assetId;
        public String namespace;
        public String profileLevelIdc;

        public Entry(String namespace2, String profileLevelIdc2, String assetId2) {
            this.namespace = namespace2;
            this.profileLevelIdc = profileLevelIdc2;
            this.assetId = assetId2;
        }

        public String toString() {
            return "{namespace='" + this.namespace + '\'' + ", profileLevelIdc='" + this.profileLevelIdc + '\'' + ", assetId='" + this.assetId + '\'' + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Entry entry = (Entry) o;
            if (!this.assetId.equals(entry.assetId)) {
                return false;
            }
            if (!this.namespace.equals(entry.namespace)) {
                return false;
            }
            if (!this.profileLevelIdc.equals(entry.profileLevelIdc)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (((this.namespace.hashCode() * 31) + this.profileLevelIdc.hashCode()) * 31) + this.assetId.hashCode();
        }

        public int getSize() {
            return Utf8.utf8StringLengthInBytes(this.namespace) + 3 + Utf8.utf8StringLengthInBytes(this.profileLevelIdc) + Utf8.utf8StringLengthInBytes(this.assetId);
        }
    }

    public AssetInformationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.apid) + 9);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 0) {
            byteBuffer.put(Utf8.convert(this.profileVersion), 0, 4);
            byteBuffer.put(Utf8.convert(this.apid));
            byteBuffer.put((byte) 0);
            return;
        }
        throw new RuntimeException("Unknown ainf version " + getVersion());
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.profileVersion = IsoTypeReader.readString(content, 4);
        this.apid = IsoTypeReader.readString(content);
    }

    public String getApid() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.apid;
    }

    public void setApid(String apid2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) apid2));
        this.apid = apid2;
    }

    public String getProfileVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.profileVersion;
    }

    public void setProfileVersion(String profileVersion2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) profileVersion2));
        if ($assertionsDisabled || (profileVersion2 != null && profileVersion2.length() == 4)) {
            this.profileVersion = profileVersion2;
            return;
        }
        throw new AssertionError();
    }

    @DoNotParseDetail
    public boolean isHidden() {
        return (getFlags() & 1) == 1;
    }

    @DoNotParseDetail
    public void setHidden(boolean hidden) {
        int flags = getFlags();
        if (!(isHidden() ^ hidden)) {
            return;
        }
        if (hidden) {
            setFlags(flags | 1);
        } else {
            setFlags(16777214 & flags);
        }
    }
}
