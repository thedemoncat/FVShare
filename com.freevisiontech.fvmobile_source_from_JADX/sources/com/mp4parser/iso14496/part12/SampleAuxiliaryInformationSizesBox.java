package com.mp4parser.iso14496.part12;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleAuxiliaryInformationSizesBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = (!SampleAuxiliaryInformationSizesBox.class.desiredAssertionStatus());
    public static final String TYPE = "saiz";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private String auxInfoType;
    private String auxInfoTypeParameter;
    private short defaultSampleInfoSize;
    private int sampleCount;
    private short[] sampleInfoSizes = new short[0];

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleAuxiliaryInformationSizesBox.java", SampleAuxiliaryInformationSizesBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSize", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "int", "index", "", "short"), 57);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAuxInfoType", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "java.lang.String"), 106);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleCount", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "int", "sampleCount", "", "void"), 146);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "java.lang.String"), 151);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAuxInfoType", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "java.lang.String", "auxInfoType", "", "void"), 110);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAuxInfoTypeParameter", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "java.lang.String"), 114);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAuxInfoTypeParameter", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "java.lang.String", "auxInfoTypeParameter", "", "void"), 118);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDefaultSampleInfoSize", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "int"), 122);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultSampleInfoSize", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "int", "defaultSampleInfoSize", "", "void"), 126);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleInfoSizes", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "[S"), 131);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleInfoSizes", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "[S", "sampleInfoSizes", "", "void"), 137);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleCount", "com.mp4parser.iso14496.part12.SampleAuxiliaryInformationSizesBox", "", "", "", "int"), (int) CompanyIdentifierResolver.QUINTIC_CORP);
    }

    static {
        ajc$preClinit();
    }

    public SampleAuxiliaryInformationSizesBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        int size = 4;
        if ((getFlags() & 1) == 1) {
            size = 4 + 8;
        }
        return (long) (size + 5 + (this.defaultSampleInfoSize == 0 ? this.sampleInfoSizes.length : 0));
    }

    public short getSize(int index) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, (Object) this, (Object) this, Conversions.intObject(index)));
        if (getDefaultSampleInfoSize() == 0) {
            return this.sampleInfoSizes[index];
        }
        return this.defaultSampleInfoSize;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if ((getFlags() & 1) == 1) {
            byteBuffer.put(IsoFile.fourCCtoBytes(this.auxInfoType));
            byteBuffer.put(IsoFile.fourCCtoBytes(this.auxInfoTypeParameter));
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.defaultSampleInfoSize);
        if (this.defaultSampleInfoSize == 0) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.sampleInfoSizes.length);
            for (short sampleInfoSize : this.sampleInfoSizes) {
                IsoTypeWriter.writeUInt8(byteBuffer, sampleInfoSize);
            }
            return;
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.sampleCount);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        if ((getFlags() & 1) == 1) {
            this.auxInfoType = IsoTypeReader.read4cc(content);
            this.auxInfoTypeParameter = IsoTypeReader.read4cc(content);
        }
        this.defaultSampleInfoSize = (short) IsoTypeReader.readUInt8(content);
        this.sampleCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        if (this.defaultSampleInfoSize == 0) {
            this.sampleInfoSizes = new short[this.sampleCount];
            for (int i = 0; i < this.sampleCount; i++) {
                this.sampleInfoSizes[i] = (short) IsoTypeReader.readUInt8(content);
            }
        }
    }

    public String getAuxInfoType() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.auxInfoType;
    }

    public void setAuxInfoType(String auxInfoType2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) auxInfoType2));
        this.auxInfoType = auxInfoType2;
    }

    public String getAuxInfoTypeParameter() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.auxInfoTypeParameter;
    }

    public void setAuxInfoTypeParameter(String auxInfoTypeParameter2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, (Object) auxInfoTypeParameter2));
        this.auxInfoTypeParameter = auxInfoTypeParameter2;
    }

    public int getDefaultSampleInfoSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.defaultSampleInfoSize;
    }

    public void setDefaultSampleInfoSize(int defaultSampleInfoSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, Conversions.intObject(defaultSampleInfoSize2)));
        if ($assertionsDisabled || defaultSampleInfoSize2 <= 255) {
            this.defaultSampleInfoSize = (short) defaultSampleInfoSize2;
            return;
        }
        throw new AssertionError();
    }

    public short[] getSampleInfoSizes() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        short[] copy = new short[this.sampleInfoSizes.length];
        System.arraycopy(this.sampleInfoSizes, 0, copy, 0, this.sampleInfoSizes.length);
        return copy;
    }

    public void setSampleInfoSizes(short[] sampleInfoSizes2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, (Object) this, (Object) this, (Object) sampleInfoSizes2));
        this.sampleInfoSizes = new short[sampleInfoSizes2.length];
        System.arraycopy(sampleInfoSizes2, 0, this.sampleInfoSizes, 0, sampleInfoSizes2.length);
    }

    public int getSampleCount() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.sampleCount;
    }

    public void setSampleCount(int sampleCount2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, Conversions.intObject(sampleCount2)));
        this.sampleCount = sampleCount2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        return "SampleAuxiliaryInformationSizesBox{defaultSampleInfoSize=" + this.defaultSampleInfoSize + ", sampleCount=" + this.sampleCount + ", auxInfoType='" + this.auxInfoType + '\'' + ", auxInfoTypeParameter='" + this.auxInfoTypeParameter + '\'' + '}';
    }
}
