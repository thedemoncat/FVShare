package com.mp4parser.iso14496.part15;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.mp4parser.iso14496.part15.HevcDecoderConfigurationRecord;
import java.nio.ByteBuffer;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class HevcConfigurationBox extends AbstractBox {
    public static final String TYPE = "hvcC";
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
    private HevcDecoderConfigurationRecord hevcDecoderConfigurationRecord = new HevcDecoderConfigurationRecord();

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("HevcConfigurationBox.java", HevcConfigurationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getHevcDecoderConfigurationRecord", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "com.mp4parser.iso14496.part15.HevcDecoderConfigurationRecord"), 38);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setHevcDecoderConfigurationRecord", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "com.mp4parser.iso14496.part15.HevcDecoderConfigurationRecord", "hevcDecoderConfigurationRecord", "", "void"), 42);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGeneral_level_idc", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 90);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMin_spatial_segmentation_idc", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 94);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getParallelismType", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 98);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getChromaFormat", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 102);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBitDepthLumaMinus8", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 106);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBitDepthChromaMinus8", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 110);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAvgFrameRate", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 114);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getNumTemporalLayers", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 118);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLengthSizeMinusOne", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 122);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isTemporalIdNested", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "boolean"), 126);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "equals", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "java.lang.Object", "o", "", "boolean"), 47);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getConstantFrameRate", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 130);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getArrays", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "java.util.List"), 134);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hashCode", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 60);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getConfigurationVersion", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 65);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGeneral_profile_space", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 69);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isGeneral_tier_flag", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "boolean"), 73);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGeneral_profile_idc", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "int"), 78);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGeneral_profile_compatibility_flags", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "long"), 82);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getGeneral_constraint_indicator_flags", "com.mp4parser.iso14496.part15.HevcConfigurationBox", "", "", "", "long"), 86);
    }

    public HevcConfigurationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) this.hevcDecoderConfigurationRecord.getSize();
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        this.hevcDecoderConfigurationRecord.write(byteBuffer);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        this.hevcDecoderConfigurationRecord.parse(content);
    }

    public HevcDecoderConfigurationRecord getHevcDecoderConfigurationRecord() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.hevcDecoderConfigurationRecord;
    }

    public void setHevcDecoderConfigurationRecord(HevcDecoderConfigurationRecord hevcDecoderConfigurationRecord2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) hevcDecoderConfigurationRecord2));
        this.hevcDecoderConfigurationRecord = hevcDecoderConfigurationRecord2;
    }

    public boolean equals(Object o) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, o));
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HevcConfigurationBox that = (HevcConfigurationBox) o;
        if (this.hevcDecoderConfigurationRecord != null) {
            if (this.hevcDecoderConfigurationRecord.equals(that.hevcDecoderConfigurationRecord)) {
                return true;
            }
        } else if (that.hevcDecoderConfigurationRecord == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        if (this.hevcDecoderConfigurationRecord != null) {
            return this.hevcDecoderConfigurationRecord.hashCode();
        }
        return 0;
    }

    public int getConfigurationVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.hevcDecoderConfigurationRecord.configurationVersion;
    }

    public int getGeneral_profile_space() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.hevcDecoderConfigurationRecord.general_profile_space;
    }

    public boolean isGeneral_tier_flag() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.hevcDecoderConfigurationRecord.general_tier_flag;
    }

    public int getGeneral_profile_idc() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.hevcDecoderConfigurationRecord.general_profile_idc;
    }

    public long getGeneral_profile_compatibility_flags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.hevcDecoderConfigurationRecord.general_profile_compatibility_flags;
    }

    public long getGeneral_constraint_indicator_flags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.hevcDecoderConfigurationRecord.general_constraint_indicator_flags;
    }

    public int getGeneral_level_idc() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.hevcDecoderConfigurationRecord.general_level_idc;
    }

    public int getMin_spatial_segmentation_idc() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        return this.hevcDecoderConfigurationRecord.min_spatial_segmentation_idc;
    }

    public int getParallelismType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.hevcDecoderConfigurationRecord.parallelismType;
    }

    public int getChromaFormat() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, this, this));
        return this.hevcDecoderConfigurationRecord.chromaFormat;
    }

    public int getBitDepthLumaMinus8() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, this, this));
        return this.hevcDecoderConfigurationRecord.bitDepthLumaMinus8;
    }

    public int getBitDepthChromaMinus8() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, this, this));
        return this.hevcDecoderConfigurationRecord.bitDepthChromaMinus8;
    }

    public int getAvgFrameRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, this, this));
        return this.hevcDecoderConfigurationRecord.avgFrameRate;
    }

    public int getNumTemporalLayers() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, this, this));
        return this.hevcDecoderConfigurationRecord.numTemporalLayers;
    }

    public int getLengthSizeMinusOne() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, this, this));
        return this.hevcDecoderConfigurationRecord.lengthSizeMinusOne;
    }

    public boolean isTemporalIdNested() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, this, this));
        return this.hevcDecoderConfigurationRecord.temporalIdNested;
    }

    public int getConstantFrameRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, this, this));
        return this.hevcDecoderConfigurationRecord.constantFrameRate;
    }

    public List<HevcDecoderConfigurationRecord.Array> getArrays() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, this, this));
        return this.hevcDecoderConfigurationRecord.arrays;
    }
}
