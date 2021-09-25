package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TierInfoBox extends AbstractBox {
    public static final String TYPE = "tiri";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final JoinPoint.StaticPart ajc$tjp_16 = null;
    private static final JoinPoint.StaticPart ajc$tjp_17 = null;
    private static final JoinPoint.StaticPart ajc$tjp_18 = null;
    private static final JoinPoint.StaticPart ajc$tjp_19 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_20 = null;
    private static final JoinPoint.StaticPart ajc$tjp_21 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    int constantFrameRate;
    int discardable;
    int frameRate;
    int levelIndication;
    int profileIndication;
    int profile_compatibility;
    int reserved1 = 0;
    int reserved2 = 0;
    int tierID;
    int visualHeight;
    int visualWidth;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("TierInfoBox.java", TierInfoBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTierID", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 69);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTierID", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "tierID", "", "void"), 73);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getVisualWidth", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 109);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setVisualWidth", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "visualWidth", "", "void"), 113);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getVisualHeight", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 117);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setVisualHeight", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "visualHeight", "", "void"), 121);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDiscardable", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 125);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDiscardable", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "discardable", "", "void"), 129);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getConstantFrameRate", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 133);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setConstantFrameRate", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "constantFrameRate", "", "void"), 137);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved2", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), (int) CompanyIdentifierResolver.ZSCAN_SOFTWARE);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved2", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "reserved2", "", "void"), 145);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProfileIndication", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 77);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFrameRate", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 149);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFrameRate", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "frameRate", "", "void"), 153);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProfileIndication", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "profileIndication", "", "void"), 81);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProfile_compatibility", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 85);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProfile_compatibility", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "profile_compatibility", "", "void"), 89);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLevelIndication", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 93);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLevelIndication", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "levelIndication", "", "void"), 97);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved1", "com.mp4parser.iso14496.part15.TierInfoBox", "", "", "", "int"), 101);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved1", "com.mp4parser.iso14496.part15.TierInfoBox", "int", "reserved1", "", "void"), 105);
    }

    public TierInfoBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 13;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt16(byteBuffer, this.tierID);
        IsoTypeWriter.writeUInt8(byteBuffer, this.profileIndication);
        IsoTypeWriter.writeUInt8(byteBuffer, this.profile_compatibility);
        IsoTypeWriter.writeUInt8(byteBuffer, this.levelIndication);
        IsoTypeWriter.writeUInt8(byteBuffer, this.reserved1);
        IsoTypeWriter.writeUInt16(byteBuffer, this.visualWidth);
        IsoTypeWriter.writeUInt16(byteBuffer, this.visualHeight);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.discardable << 6) + (this.constantFrameRate << 4) + this.reserved2);
        IsoTypeWriter.writeUInt16(byteBuffer, this.frameRate);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        this.tierID = IsoTypeReader.readUInt16(content);
        this.profileIndication = IsoTypeReader.readUInt8(content);
        this.profile_compatibility = IsoTypeReader.readUInt8(content);
        this.levelIndication = IsoTypeReader.readUInt8(content);
        this.reserved1 = IsoTypeReader.readUInt8(content);
        this.visualWidth = IsoTypeReader.readUInt16(content);
        this.visualHeight = IsoTypeReader.readUInt16(content);
        int a = IsoTypeReader.readUInt8(content);
        this.discardable = (a & 192) >> 6;
        this.constantFrameRate = (a & 48) >> 4;
        this.reserved2 = a & 15;
        this.frameRate = IsoTypeReader.readUInt16(content);
    }

    public int getTierID() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.tierID;
    }

    public void setTierID(int tierID2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(tierID2)));
        this.tierID = tierID2;
    }

    public int getProfileIndication() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.profileIndication;
    }

    public void setProfileIndication(int profileIndication2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(profileIndication2)));
        this.profileIndication = profileIndication2;
    }

    public int getProfile_compatibility() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.profile_compatibility;
    }

    public void setProfile_compatibility(int profile_compatibility2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(profile_compatibility2)));
        this.profile_compatibility = profile_compatibility2;
    }

    public int getLevelIndication() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.levelIndication;
    }

    public void setLevelIndication(int levelIndication2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(levelIndication2)));
        this.levelIndication = levelIndication2;
    }

    public int getReserved1() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.reserved1;
    }

    public void setReserved1(int reserved12) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.intObject(reserved12)));
        this.reserved1 = reserved12;
    }

    public int getVisualWidth() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.visualWidth;
    }

    public void setVisualWidth(int visualWidth2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.intObject(visualWidth2)));
        this.visualWidth = visualWidth2;
    }

    public int getVisualHeight() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.visualHeight;
    }

    public void setVisualHeight(int visualHeight2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.intObject(visualHeight2)));
        this.visualHeight = visualHeight2;
    }

    public int getDiscardable() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, this, this));
        return this.discardable;
    }

    public void setDiscardable(int discardable2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, Conversions.intObject(discardable2)));
        this.discardable = discardable2;
    }

    public int getConstantFrameRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.constantFrameRate;
    }

    public void setConstantFrameRate(int constantFrameRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, Conversions.intObject(constantFrameRate2)));
        this.constantFrameRate = constantFrameRate2;
    }

    public int getReserved2() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        return this.reserved2;
    }

    public void setReserved2(int reserved22) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, Conversions.intObject(reserved22)));
        this.reserved2 = reserved22;
    }

    public int getFrameRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, this, this));
        return this.frameRate;
    }

    public void setFrameRate(int frameRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, (Object) this, (Object) this, Conversions.intObject(frameRate2)));
        this.frameRate = frameRate2;
    }
}
