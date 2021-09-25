package com.coremedia.iso.boxes.apple;

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

public final class AppleLosslessSpecificBox extends AbstractFullBox {
    public static final String TYPE = "alac";
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
    private long bitRate;
    private int channels;
    private int historyMult;
    private int initialHistory;
    private int kModifier;
    private long maxCodedFrameSize;
    private long maxSamplePerFrame;
    private long sampleRate;
    private int sampleSize;
    private int unknown1;
    private int unknown2;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleLosslessSpecificBox.java", AppleLosslessSpecificBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMaxSamplePerFrame", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "long"), 34);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMaxSamplePerFrame", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "maxSamplePerFrame", "", "void"), 38);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getKModifier", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 74);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setKModifier", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "kModifier", "", "void"), 78);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getChannels", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 82);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setChannels", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "channels", "", "void"), 86);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getUnknown2", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 90);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setUnknown2", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "unknown2", "", "void"), 94);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMaxCodedFrameSize", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "long"), 98);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMaxCodedFrameSize", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "maxCodedFrameSize", "", "void"), 102);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBitRate", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "long"), 106);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBitRate", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "bitRate", "", "void"), 110);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getUnknown1", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 42);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleRate", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "long"), 114);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleRate", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "sampleRate", "", "void"), 118);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setUnknown1", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "unknown1", "", "void"), 46);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleSize", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 50);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleSize", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "sampleSize", "", "void"), 54);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getHistoryMult", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 58);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setHistoryMult", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "historyMult", "", "void"), 62);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getInitialHistory", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "", "", "", "int"), 66);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setInitialHistory", "com.coremedia.iso.boxes.apple.AppleLosslessSpecificBox", "int", "initialHistory", "", "void"), 70);
    }

    public long getMaxSamplePerFrame() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.maxSamplePerFrame;
    }

    public void setMaxSamplePerFrame(int maxSamplePerFrame2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(maxSamplePerFrame2)));
        this.maxSamplePerFrame = (long) maxSamplePerFrame2;
    }

    public int getUnknown1() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.unknown1;
    }

    public void setUnknown1(int unknown12) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(unknown12)));
        this.unknown1 = unknown12;
    }

    public int getSampleSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.sampleSize;
    }

    public void setSampleSize(int sampleSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(sampleSize2)));
        this.sampleSize = sampleSize2;
    }

    public int getHistoryMult() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.historyMult;
    }

    public void setHistoryMult(int historyMult2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(historyMult2)));
        this.historyMult = historyMult2;
    }

    public int getInitialHistory() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.initialHistory;
    }

    public void setInitialHistory(int initialHistory2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.intObject(initialHistory2)));
        this.initialHistory = initialHistory2;
    }

    public int getKModifier() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.kModifier;
    }

    public void setKModifier(int kModifier2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.intObject(kModifier2)));
        this.kModifier = kModifier2;
    }

    public int getChannels() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.channels;
    }

    public void setChannels(int channels2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.intObject(channels2)));
        this.channels = channels2;
    }

    public int getUnknown2() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, this, this));
        return this.unknown2;
    }

    public void setUnknown2(int unknown22) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, Conversions.intObject(unknown22)));
        this.unknown2 = unknown22;
    }

    public long getMaxCodedFrameSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.maxCodedFrameSize;
    }

    public void setMaxCodedFrameSize(int maxCodedFrameSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, Conversions.intObject(maxCodedFrameSize2)));
        this.maxCodedFrameSize = (long) maxCodedFrameSize2;
    }

    public long getBitRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        return this.bitRate;
    }

    public void setBitRate(int bitRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, Conversions.intObject(bitRate2)));
        this.bitRate = (long) bitRate2;
    }

    public long getSampleRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, this, this));
        return this.sampleRate;
    }

    public void setSampleRate(int sampleRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, (Object) this, (Object) this, Conversions.intObject(sampleRate2)));
        this.sampleRate = (long) sampleRate2;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.maxSamplePerFrame = IsoTypeReader.readUInt32(content);
        this.unknown1 = IsoTypeReader.readUInt8(content);
        this.sampleSize = IsoTypeReader.readUInt8(content);
        this.historyMult = IsoTypeReader.readUInt8(content);
        this.initialHistory = IsoTypeReader.readUInt8(content);
        this.kModifier = IsoTypeReader.readUInt8(content);
        this.channels = IsoTypeReader.readUInt8(content);
        this.unknown2 = IsoTypeReader.readUInt16(content);
        this.maxCodedFrameSize = IsoTypeReader.readUInt32(content);
        this.bitRate = IsoTypeReader.readUInt32(content);
        this.sampleRate = IsoTypeReader.readUInt32(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.maxSamplePerFrame);
        IsoTypeWriter.writeUInt8(byteBuffer, this.unknown1);
        IsoTypeWriter.writeUInt8(byteBuffer, this.sampleSize);
        IsoTypeWriter.writeUInt8(byteBuffer, this.historyMult);
        IsoTypeWriter.writeUInt8(byteBuffer, this.initialHistory);
        IsoTypeWriter.writeUInt8(byteBuffer, this.kModifier);
        IsoTypeWriter.writeUInt8(byteBuffer, this.channels);
        IsoTypeWriter.writeUInt16(byteBuffer, this.unknown2);
        IsoTypeWriter.writeUInt32(byteBuffer, this.maxCodedFrameSize);
        IsoTypeWriter.writeUInt32(byteBuffer, this.bitRate);
        IsoTypeWriter.writeUInt32(byteBuffer, this.sampleRate);
    }

    public AppleLosslessSpecificBox() {
        super("alac");
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 28;
    }
}
