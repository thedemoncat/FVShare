package com.googlecode.mp4parser.boxes.piff;

import com.coremedia.iso.boxes.UserBox;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import com.googlecode.mp4parser.boxes.AbstractSampleEncryptionBox;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class PiffSampleEncryptionBox extends AbstractSampleEncryptionBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("PiffSampleEncryptionBox.java", PiffSampleEncryptionBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAlgorithmId", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "", "", "", "int"), 46);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAlgorithmId", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "int", "algorithmId", "", "void"), 50);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getIvSize", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "", "", "", "int"), 54);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setIvSize", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "int", "ivSize", "", "void"), 58);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getKid", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "", "", "", "[B"), 62);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setKid", "com.googlecode.mp4parser.boxes.piff.PiffSampleEncryptionBox", "[B", "kid", "", "void"), 66);
    }

    public PiffSampleEncryptionBox() {
        super(UserBox.TYPE);
    }

    public byte[] getUserType() {
        return new byte[]{-94, 57, 79, 82, 90, -101, 79, ClosedCaptionCtrl.MISC_CHAN_1, -94, 68, 108, 66, 124, Camera2Constants.JPEG_QUALITY, -115, -12};
    }

    public int getAlgorithmId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.algorithmId;
    }

    public void setAlgorithmId(int algorithmId) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(algorithmId)));
        this.algorithmId = algorithmId;
    }

    public int getIvSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.ivSize;
    }

    public void setIvSize(int ivSize) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(ivSize)));
        this.ivSize = ivSize;
    }

    public byte[] getKid() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.kid;
    }

    public void setKid(byte[] kid) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) kid));
        this.kid = kid;
    }

    @DoNotParseDetail
    public boolean isOverrideTrackEncryptionBoxParameters() {
        return (getFlags() & 1) > 0;
    }

    @DoNotParseDetail
    public void setOverrideTrackEncryptionBoxParameters(boolean b) {
        if (b) {
            setFlags(getFlags() | 1);
        } else {
            setFlags(getFlags() & 16777214);
        }
    }
}
