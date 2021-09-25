package com.googlecode.mp4parser.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class DTSSpecificBox extends AbstractBox {
    public static final String TYPE = "ddts";
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
    private static final JoinPoint.StaticPart ajc$tjp_22 = null;
    private static final JoinPoint.StaticPart ajc$tjp_23 = null;
    private static final JoinPoint.StaticPart ajc$tjp_24 = null;
    private static final JoinPoint.StaticPart ajc$tjp_25 = null;
    private static final JoinPoint.StaticPart ajc$tjp_26 = null;
    private static final JoinPoint.StaticPart ajc$tjp_27 = null;
    private static final JoinPoint.StaticPart ajc$tjp_28 = null;
    private static final JoinPoint.StaticPart ajc$tjp_29 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_30 = null;
    private static final JoinPoint.StaticPart ajc$tjp_31 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    long DTSSamplingFrequency;
    int LBRDurationMod;
    long avgBitRate;
    int channelLayout;
    int coreLFEPresent;
    int coreLayout;
    int coreSize;
    int frameDuration;
    long maxBitRate;
    int multiAssetFlag;
    int pcmSampleDepth;
    int representationType;
    int reserved;
    int reservedBoxPresent;
    int stereoDownmix;
    int streamConstruction;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("DTSSpecificBox.java", DTSSpecificBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAvgBitRate", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "long"), 89);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAvgBitRate", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "long", "avgBitRate", "", "void"), 93);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getStreamConstruction", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 129);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setStreamConstruction", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "streamConstruction", "", "void"), 133);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCoreLFEPresent", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 137);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCoreLFEPresent", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "coreLFEPresent", "", "void"), (int) CompanyIdentifierResolver.ZSCAN_SOFTWARE);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCoreLayout", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 145);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCoreLayout", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "coreLayout", "", "void"), 149);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCoreSize", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 153);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCoreSize", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "coreSize", "", "void"), (int) CompanyIdentifierResolver.GEOFORCE_INC);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getStereoDownmix", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.SRMEDIZINELEKTRONIK);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setStereoDownmix", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "stereoDownmix", "", "void"), (int) CompanyIdentifierResolver.OTL_DYNAMICS_LLC);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDTSSamplingFrequency", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "long"), 97);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRepresentationType", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.MAGNETI_MARELLI_SPA);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRepresentationType", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "representationType", "", "void"), (int) CompanyIdentifierResolver.PETER_SYSTEMTECHNIK_GMBH);
        ajc$tjp_22 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getChannelLayout", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.SARIS_CYCLING_GROUP_INC);
        ajc$tjp_23 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setChannelLayout", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "channelLayout", "", "void"), (int) CompanyIdentifierResolver.SWIRL_NETWORKS);
        ajc$tjp_24 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMultiAssetFlag", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.JOHNSON_CONTROLS_INC);
        ajc$tjp_25 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMultiAssetFlag", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "multiAssetFlag", "", "void"), 189);
        ajc$tjp_26 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLBRDurationMod", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 193);
        ajc$tjp_27 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLBRDurationMod", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "LBRDurationMod", "", "void"), 197);
        ajc$tjp_28 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.EVLUMA);
        ajc$tjp_29 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "reserved", "", "void"), (int) CompanyIdentifierResolver.MICROCHIP_TECHNOLOGY_INC);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDTSSamplingFrequency", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "long", "DTSSamplingFrequency", "", "void"), 101);
        ajc$tjp_30 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReservedBoxPresent", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), (int) CompanyIdentifierResolver.POLAR_ELECTRO_EUROPE_BV);
        ajc$tjp_31 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReservedBoxPresent", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "reservedBoxPresent", "", "void"), (int) CompanyIdentifierResolver.AUSTCO_COMMUNICATION_SYSTEMS);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMaxBitRate", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "long"), 105);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMaxBitRate", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "long", "maxBitRate", "", "void"), 109);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPcmSampleDepth", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 113);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPcmSampleDepth", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "pcmSampleDepth", "", "void"), 117);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFrameDuration", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "", "", "", "int"), 121);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFrameDuration", "com.googlecode.mp4parser.boxes.DTSSpecificBox", "int", "frameDuration", "", "void"), 125);
    }

    public DTSSpecificBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 20;
    }

    public void _parseDetails(ByteBuffer content) {
        this.DTSSamplingFrequency = IsoTypeReader.readUInt32(content);
        this.maxBitRate = IsoTypeReader.readUInt32(content);
        this.avgBitRate = IsoTypeReader.readUInt32(content);
        this.pcmSampleDepth = IsoTypeReader.readUInt8(content);
        BitReaderBuffer brb = new BitReaderBuffer(content);
        this.frameDuration = brb.readBits(2);
        this.streamConstruction = brb.readBits(5);
        this.coreLFEPresent = brb.readBits(1);
        this.coreLayout = brb.readBits(6);
        this.coreSize = brb.readBits(14);
        this.stereoDownmix = brb.readBits(1);
        this.representationType = brb.readBits(3);
        this.channelLayout = brb.readBits(16);
        this.multiAssetFlag = brb.readBits(1);
        this.LBRDurationMod = brb.readBits(1);
        this.reservedBoxPresent = brb.readBits(1);
        this.reserved = brb.readBits(5);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt32(byteBuffer, this.DTSSamplingFrequency);
        IsoTypeWriter.writeUInt32(byteBuffer, this.maxBitRate);
        IsoTypeWriter.writeUInt32(byteBuffer, this.avgBitRate);
        IsoTypeWriter.writeUInt8(byteBuffer, this.pcmSampleDepth);
        BitWriterBuffer bwb = new BitWriterBuffer(byteBuffer);
        bwb.writeBits(this.frameDuration, 2);
        bwb.writeBits(this.streamConstruction, 5);
        bwb.writeBits(this.coreLFEPresent, 1);
        bwb.writeBits(this.coreLayout, 6);
        bwb.writeBits(this.coreSize, 14);
        bwb.writeBits(this.stereoDownmix, 1);
        bwb.writeBits(this.representationType, 3);
        bwb.writeBits(this.channelLayout, 16);
        bwb.writeBits(this.multiAssetFlag, 1);
        bwb.writeBits(this.LBRDurationMod, 1);
        bwb.writeBits(this.reservedBoxPresent, 1);
        bwb.writeBits(this.reserved, 5);
    }

    public long getAvgBitRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.avgBitRate;
    }

    public void setAvgBitRate(long avgBitRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.longObject(avgBitRate2)));
        this.avgBitRate = avgBitRate2;
    }

    public long getDTSSamplingFrequency() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.DTSSamplingFrequency;
    }

    public void setDTSSamplingFrequency(long DTSSamplingFrequency2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.longObject(DTSSamplingFrequency2)));
        this.DTSSamplingFrequency = DTSSamplingFrequency2;
    }

    public long getMaxBitRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.maxBitRate;
    }

    public void setMaxBitRate(long maxBitRate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.longObject(maxBitRate2)));
        this.maxBitRate = maxBitRate2;
    }

    public int getPcmSampleDepth() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.pcmSampleDepth;
    }

    public void setPcmSampleDepth(int pcmSampleDepth2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(pcmSampleDepth2)));
        this.pcmSampleDepth = pcmSampleDepth2;
    }

    public int getFrameDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.frameDuration;
    }

    public void setFrameDuration(int frameDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.intObject(frameDuration2)));
        this.frameDuration = frameDuration2;
    }

    public int getStreamConstruction() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.streamConstruction;
    }

    public void setStreamConstruction(int streamConstruction2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.intObject(streamConstruction2)));
        this.streamConstruction = streamConstruction2;
    }

    public int getCoreLFEPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.coreLFEPresent;
    }

    public void setCoreLFEPresent(int coreLFEPresent2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.intObject(coreLFEPresent2)));
        this.coreLFEPresent = coreLFEPresent2;
    }

    public int getCoreLayout() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, this, this));
        return this.coreLayout;
    }

    public void setCoreLayout(int coreLayout2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, Conversions.intObject(coreLayout2)));
        this.coreLayout = coreLayout2;
    }

    public int getCoreSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.coreSize;
    }

    public void setCoreSize(int coreSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, Conversions.intObject(coreSize2)));
        this.coreSize = coreSize2;
    }

    public int getStereoDownmix() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        return this.stereoDownmix;
    }

    public void setStereoDownmix(int stereoDownmix2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, Conversions.intObject(stereoDownmix2)));
        this.stereoDownmix = stereoDownmix2;
    }

    public int getRepresentationType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, this, this));
        return this.representationType;
    }

    public void setRepresentationType(int representationType2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, (Object) this, (Object) this, Conversions.intObject(representationType2)));
        this.representationType = representationType2;
    }

    public int getChannelLayout() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_22, this, this));
        return this.channelLayout;
    }

    public void setChannelLayout(int channelLayout2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_23, (Object) this, (Object) this, Conversions.intObject(channelLayout2)));
        this.channelLayout = channelLayout2;
    }

    public int getMultiAssetFlag() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_24, this, this));
        return this.multiAssetFlag;
    }

    public void setMultiAssetFlag(int multiAssetFlag2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_25, (Object) this, (Object) this, Conversions.intObject(multiAssetFlag2)));
        this.multiAssetFlag = multiAssetFlag2;
    }

    public int getLBRDurationMod() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_26, this, this));
        return this.LBRDurationMod;
    }

    public void setLBRDurationMod(int LBRDurationMod2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_27, (Object) this, (Object) this, Conversions.intObject(LBRDurationMod2)));
        this.LBRDurationMod = LBRDurationMod2;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_28, this, this));
        return this.reserved;
    }

    public void setReserved(int reserved2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_29, (Object) this, (Object) this, Conversions.intObject(reserved2)));
        this.reserved = reserved2;
    }

    public int getReservedBoxPresent() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_30, this, this));
        return this.reservedBoxPresent;
    }

    public void setReservedBoxPresent(int reservedBoxPresent2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_31, (Object) this, (Object) this, Conversions.intObject(reservedBoxPresent2)));
        this.reservedBoxPresent = reservedBoxPresent2;
    }

    @DoNotParseDetail
    public int[] getDashAudioChannelConfiguration() {
        int channelLayout2 = getChannelLayout();
        int numChannels = 0;
        int dwChannelMask = 0;
        if ((channelLayout2 & 1) == 1) {
            numChannels = 0 + 1;
            dwChannelMask = 0 | 4;
        }
        if ((channelLayout2 & 2) == 2) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 1 | 2;
        }
        if ((channelLayout2 & 4) == 4) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 16 | 32;
        }
        if ((channelLayout2 & 8) == 8) {
            numChannels++;
            dwChannelMask |= 8;
        }
        if ((channelLayout2 & 16) == 16) {
            numChannels++;
            dwChannelMask |= 256;
        }
        if ((channelLayout2 & 32) == 32) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 4096 | 16384;
        }
        if ((channelLayout2 & 64) == 64) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 16 | 32;
        }
        if ((channelLayout2 & 128) == 128) {
            numChannels++;
            dwChannelMask |= 8192;
        }
        if ((channelLayout2 & 256) == 256) {
            numChannels++;
            dwChannelMask |= 2048;
        }
        if ((channelLayout2 & 512) == 512) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 64 | 128;
        }
        if ((channelLayout2 & 1024) == 1024) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 512 | 1024;
        }
        if ((channelLayout2 & 2048) == 2048) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 16 | 32;
        }
        if ((channelLayout2 & 4096) == 4096) {
            numChannels++;
            dwChannelMask |= 8;
        }
        if ((channelLayout2 & 8192) == 8192) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 16 | 32;
        }
        if ((channelLayout2 & 16384) == 16384) {
            numChannels++;
            dwChannelMask |= 65536;
        }
        if ((channelLayout2 & 32768) == 32768) {
            numChannels += 2;
            dwChannelMask = dwChannelMask | 32768 | 131072;
        }
        if ((channelLayout2 & 65536) == 65536) {
            numChannels++;
        }
        if ((channelLayout2 & 131072) == 131072) {
            numChannels += 2;
        }
        return new int[]{numChannels, dwChannelMask};
    }
}
