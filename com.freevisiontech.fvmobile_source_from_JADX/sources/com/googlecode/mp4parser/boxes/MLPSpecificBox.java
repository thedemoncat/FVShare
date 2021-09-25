package com.googlecode.mp4parser.boxes;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitReaderBuffer;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BitWriterBuffer;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class MLPSpecificBox extends AbstractBox {
    public static final String TYPE = "dmlp";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    int format_info;
    int peak_data_rate;
    int reserved;
    int reserved2;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("MLPSpecificBox.java", MLPSpecificBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFormat_info", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "", "", "", "int"), 49);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFormat_info", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "int", "format_info", "", "void"), 53);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPeak_data_rate", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "", "", "", "int"), 57);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPeak_data_rate", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "int", "peak_data_rate", "", "void"), 61);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "", "", "", "int"), 65);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "int", "reserved", "", "void"), 69);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved2", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "", "", "", "int"), 73);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved2", "com.googlecode.mp4parser.boxes.MLPSpecificBox", "int", "reserved2", "", "void"), 77);
    }

    public MLPSpecificBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 10;
    }

    public void _parseDetails(ByteBuffer content) {
        BitReaderBuffer brb = new BitReaderBuffer(content);
        this.format_info = brb.readBits(32);
        this.peak_data_rate = brb.readBits(15);
        this.reserved = brb.readBits(1);
        this.reserved2 = brb.readBits(32);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        BitWriterBuffer bwb = new BitWriterBuffer(byteBuffer);
        bwb.writeBits(this.format_info, 32);
        bwb.writeBits(this.peak_data_rate, 15);
        bwb.writeBits(this.reserved, 1);
        bwb.writeBits(this.reserved2, 32);
    }

    public int getFormat_info() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.format_info;
    }

    public void setFormat_info(int format_info2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(format_info2)));
        this.format_info = format_info2;
    }

    public int getPeak_data_rate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.peak_data_rate;
    }

    public void setPeak_data_rate(int peak_data_rate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.intObject(peak_data_rate2)));
        this.peak_data_rate = peak_data_rate2;
    }

    public int getReserved() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.reserved;
    }

    public void setReserved(int reserved3) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(reserved3)));
        this.reserved = reserved3;
    }

    public int getReserved2() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.reserved2;
    }

    public void setReserved2(int reserved22) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.intObject(reserved22)));
        this.reserved2 = reserved22;
    }
}
