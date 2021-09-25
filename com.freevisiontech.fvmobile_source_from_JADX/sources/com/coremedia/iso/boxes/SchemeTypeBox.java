package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SchemeTypeBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = (!SchemeTypeBox.class.desiredAssertionStatus());
    public static final String TYPE = "schm";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    String schemeType = "    ";
    String schemeUri = null;
    long schemeVersion;

    private static void ajc$preClinit() {
        Factory factory = new Factory("SchemeTypeBox.java", SchemeTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSchemeType", "com.coremedia.iso.boxes.SchemeTypeBox", "", "", "", "java.lang.String"), 44);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSchemeVersion", "com.coremedia.iso.boxes.SchemeTypeBox", "", "", "", "long"), 48);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSchemeUri", "com.coremedia.iso.boxes.SchemeTypeBox", "", "", "", "java.lang.String"), 52);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSchemeType", "com.coremedia.iso.boxes.SchemeTypeBox", "java.lang.String", "schemeType", "", "void"), 56);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSchemeVersion", "com.coremedia.iso.boxes.SchemeTypeBox", "int", "schemeVersion", "", "void"), 61);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSchemeUri", "com.coremedia.iso.boxes.SchemeTypeBox", "java.lang.String", "schemeUri", "", "void"), 65);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SchemeTypeBox", "", "", "", "java.lang.String"), 93);
    }

    static {
        ajc$preClinit();
    }

    public SchemeTypeBox() {
        super(TYPE);
    }

    public String getSchemeType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.schemeType;
    }

    public long getSchemeVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.schemeVersion;
    }

    public String getSchemeUri() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.schemeUri;
    }

    public void setSchemeType(String schemeType2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) schemeType2));
        if ($assertionsDisabled || (schemeType2 != null && schemeType2.length() == 4)) {
            this.schemeType = schemeType2;
            return;
        }
        throw new AssertionError("SchemeType may not be null or not 4 bytes long");
    }

    public void setSchemeVersion(int schemeVersion2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, Conversions.intObject(schemeVersion2)));
        this.schemeVersion = (long) schemeVersion2;
    }

    public void setSchemeUri(String schemeUri2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) schemeUri2));
        this.schemeUri = schemeUri2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (((getFlags() & 1) == 1 ? Utf8.utf8StringLengthInBytes(this.schemeUri) + 1 : 0) + 12);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.schemeType = IsoTypeReader.read4cc(content);
        this.schemeVersion = IsoTypeReader.readUInt32(content);
        if ((getFlags() & 1) == 1) {
            this.schemeUri = IsoTypeReader.readString(content);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(IsoFile.fourCCtoBytes(this.schemeType));
        IsoTypeWriter.writeUInt32(byteBuffer, this.schemeVersion);
        if ((getFlags() & 1) == 1) {
            byteBuffer.put(Utf8.convert(this.schemeUri));
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        StringBuilder buffer = new StringBuilder();
        buffer.append("Schema Type Box[");
        buffer.append("schemeUri=").append(this.schemeUri).append("; ");
        buffer.append("schemeType=").append(this.schemeType).append("; ");
        buffer.append("schemeVersion=").append(this.schemeVersion).append("; ");
        buffer.append("]");
        return buffer.toString();
    }
}
