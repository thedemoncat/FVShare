package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public final class OmaDrmAccessUnitFormatBox extends AbstractFullBox {
    public static final String TYPE = "odaf";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private byte allBits;
    private int initVectorLength;
    private int keyIndicatorLength;
    private boolean selectiveEncryption;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("OmaDrmAccessUnitFormatBox.java", OmaDrmAccessUnitFormatBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isSelectiveEncryption", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "", "", "", "boolean"), 46);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getKeyIndicatorLength", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "", "", "", "int"), 50);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getInitVectorLength", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "", "", "", "int"), 54);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setInitVectorLength", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "int", "initVectorLength", "", "void"), 58);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setKeyIndicatorLength", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "int", "keyIndicatorLength", "", "void"), 62);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAllBits", "com.coremedia.iso.boxes.OmaDrmAccessUnitFormatBox", "byte", "allBits", "", "void"), 66);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 7;
    }

    public OmaDrmAccessUnitFormatBox() {
        super(TYPE);
    }

    public boolean isSelectiveEncryption() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.selectiveEncryption;
    }

    public int getKeyIndicatorLength() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.keyIndicatorLength;
    }

    public int getInitVectorLength() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.initVectorLength;
    }

    public void setInitVectorLength(int initVectorLength2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(initVectorLength2)));
        this.initVectorLength = initVectorLength2;
    }

    public void setKeyIndicatorLength(int keyIndicatorLength2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, Conversions.intObject(keyIndicatorLength2)));
        this.keyIndicatorLength = keyIndicatorLength2;
    }

    public void setAllBits(byte allBits2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.byteObject(allBits2)));
        this.allBits = allBits2;
        this.selectiveEncryption = (allBits2 & 128) == 128;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.allBits = (byte) IsoTypeReader.readUInt8(content);
        this.selectiveEncryption = (this.allBits & 128) == 128;
        this.keyIndicatorLength = IsoTypeReader.readUInt8(content);
        this.initVectorLength = IsoTypeReader.readUInt8(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt8(byteBuffer, this.allBits);
        IsoTypeWriter.writeUInt8(byteBuffer, this.keyIndicatorLength);
        IsoTypeWriter.writeUInt8(byteBuffer, this.initVectorLength);
    }
}
