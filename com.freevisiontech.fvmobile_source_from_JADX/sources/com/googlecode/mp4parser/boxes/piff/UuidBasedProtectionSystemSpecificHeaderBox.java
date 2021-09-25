package com.googlecode.mp4parser.boxes.piff;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.UserBox;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.UUIDConverter;
import com.umeng.analytics.pro.C0217dk;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class UuidBasedProtectionSystemSpecificHeaderBox extends AbstractFullBox {
    public static byte[] USER_TYPE = {-48, -118, 79, 24, C0217dk.f724n, -13, 74, -126, -74, -56, 50, -40, -85, -95, -125, -45};
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    ProtectionSpecificHeader protectionSpecificHeader;
    UUID systemId;

    private static void ajc$preClinit() {
        Factory factory = new Factory("UuidBasedProtectionSystemSpecificHeaderBox.java", UuidBasedProtectionSystemSpecificHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSystemId", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "", "", "", "java.util.UUID"), 67);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSystemId", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "java.util.UUID", "systemId", "", "void"), 71);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSystemIdString", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "", "", "", "java.lang.String"), 75);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProtectionSpecificHeader", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "", "", "", "com.googlecode.mp4parser.boxes.piff.ProtectionSpecificHeader"), 79);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProtectionSpecificHeaderString", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "", "", "", "java.lang.String"), 83);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProtectionSpecificHeader", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "com.googlecode.mp4parser.boxes.piff.ProtectionSpecificHeader", "protectionSpecificHeader", "", "void"), 87);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.piff.UuidBasedProtectionSystemSpecificHeaderBox", "", "", "", "java.lang.String"), 92);
    }

    static {
        ajc$preClinit();
    }

    public UuidBasedProtectionSystemSpecificHeaderBox() {
        super(UserBox.TYPE, USER_TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (this.protectionSpecificHeader.getData().limit() + 24);
    }

    public byte[] getUserType() {
        return USER_TYPE;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt64(byteBuffer, this.systemId.getMostSignificantBits());
        IsoTypeWriter.writeUInt64(byteBuffer, this.systemId.getLeastSignificantBits());
        ByteBuffer data = this.protectionSpecificHeader.getData();
        data.rewind();
        IsoTypeWriter.writeUInt32(byteBuffer, (long) data.limit());
        byteBuffer.put(data);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        byte[] systemIdBytes = new byte[16];
        content.get(systemIdBytes);
        this.systemId = UUIDConverter.convert(systemIdBytes);
        CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.protectionSpecificHeader = ProtectionSpecificHeader.createFor(this.systemId, content);
    }

    public UUID getSystemId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.systemId;
    }

    public void setSystemId(UUID systemId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) systemId2));
        this.systemId = systemId2;
    }

    public String getSystemIdString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.systemId.toString();
    }

    public ProtectionSpecificHeader getProtectionSpecificHeader() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.protectionSpecificHeader;
    }

    public String getProtectionSpecificHeaderString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.protectionSpecificHeader.toString();
    }

    public void setProtectionSpecificHeader(ProtectionSpecificHeader protectionSpecificHeader2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) protectionSpecificHeader2));
        this.protectionSpecificHeader = protectionSpecificHeader2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        StringBuilder sb = new StringBuilder();
        sb.append("UuidBasedProtectionSystemSpecificHeaderBox");
        sb.append("{systemId=").append(this.systemId.toString());
        sb.append(", dataSize=").append(this.protectionSpecificHeader.getData().limit());
        sb.append('}');
        return sb.toString();
    }
}
