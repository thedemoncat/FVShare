package com.mp4parser.iso14496.part12;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public final class BitRateBox extends AbstractBox {
    public static final String TYPE = "btrt";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private long avgBitrate;
    private long bufferSizeDb;
    private long maxBitrate;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("BitRateBox.java", BitRateBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBufferSizeDb", "com.mp4parser.iso14496.part12.BitRateBox", "", "", "", "long"), 74);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBufferSizeDb", "com.mp4parser.iso14496.part12.BitRateBox", "long", "bufferSizeDb", "", "void"), 82);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMaxBitrate", "com.mp4parser.iso14496.part12.BitRateBox", "", "", "", "long"), 90);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMaxBitrate", "com.mp4parser.iso14496.part12.BitRateBox", "long", "maxBitrate", "", "void"), 98);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAvgBitrate", "com.mp4parser.iso14496.part12.BitRateBox", "", "", "", "long"), 106);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAvgBitrate", "com.mp4parser.iso14496.part12.BitRateBox", "long", "avgBitrate", "", "void"), 114);
    }

    public BitRateBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 12;
    }

    public void _parseDetails(ByteBuffer content) {
        this.bufferSizeDb = IsoTypeReader.readUInt32(content);
        this.maxBitrate = IsoTypeReader.readUInt32(content);
        this.avgBitrate = IsoTypeReader.readUInt32(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeUInt32(byteBuffer, this.bufferSizeDb);
        IsoTypeWriter.writeUInt32(byteBuffer, this.maxBitrate);
        IsoTypeWriter.writeUInt32(byteBuffer, this.avgBitrate);
    }

    public long getBufferSizeDb() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.bufferSizeDb;
    }

    public void setBufferSizeDb(long bufferSizeDb2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.longObject(bufferSizeDb2)));
        this.bufferSizeDb = bufferSizeDb2;
    }

    public long getMaxBitrate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.maxBitrate;
    }

    public void setMaxBitrate(long maxBitrate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, Conversions.longObject(maxBitrate2)));
        this.maxBitrate = maxBitrate2;
    }

    public long getAvgBitrate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.avgBitrate;
    }

    public void setAvgBitrate(long avgBitrate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.longObject(avgBitrate2)));
        this.avgBitrate = avgBitrate2;
    }
}
