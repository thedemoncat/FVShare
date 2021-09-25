package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SampleSizeBox extends AbstractFullBox {
    public static final String TYPE = "stsz";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    int sampleCount;
    private long sampleSize;
    private long[] sampleSizes = new long[0];

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SampleSizeBox.java", SampleSizeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleSize", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "long"), 50);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleSize", "com.coremedia.iso.boxes.SampleSizeBox", "long", "sampleSize", "", "void"), 54);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleSizeAtIndex", "com.coremedia.iso.boxes.SampleSizeBox", "int", "index", "", "long"), 59);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleCount", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "long"), 67);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleSizes", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "[J"), 76);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleSizes", "com.coremedia.iso.boxes.SampleSizeBox", "[J", "sampleSizes", "", "void"), 80);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SampleSizeBox", "", "", "", "java.lang.String"), 119);
    }

    public SampleSizeBox() {
        super(TYPE);
    }

    public long getSampleSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.sampleSize;
    }

    public void setSampleSize(long sampleSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.longObject(sampleSize2)));
        this.sampleSize = sampleSize2;
    }

    public long getSampleSizeAtIndex(int index) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, Conversions.intObject(index)));
        if (this.sampleSize > 0) {
            return this.sampleSize;
        }
        return this.sampleSizes[index];
    }

    public long getSampleCount() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        if (this.sampleSize > 0) {
            return (long) this.sampleCount;
        }
        return (long) this.sampleSizes.length;
    }

    public long[] getSampleSizes() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.sampleSizes;
    }

    public void setSampleSizes(long[] sampleSizes2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) sampleSizes2));
        this.sampleSizes = sampleSizes2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.sampleSize == 0 ? this.sampleSizes.length * 4 : 0) + 12);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.sampleSize = IsoTypeReader.readUInt32(content);
        this.sampleCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        if (this.sampleSize == 0) {
            this.sampleSizes = new long[this.sampleCount];
            for (int i = 0; i < this.sampleCount; i++) {
                this.sampleSizes[i] = IsoTypeReader.readUInt32(content);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.sampleSize);
        if (this.sampleSize == 0) {
            IsoTypeWriter.writeUInt32(byteBuffer, (long) this.sampleSizes.length);
            for (long sampleSize1 : this.sampleSizes) {
                IsoTypeWriter.writeUInt32(byteBuffer, sampleSize1);
            }
            return;
        }
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.sampleCount);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return "SampleSizeBox[sampleSize=" + getSampleSize() + ";sampleCount=" + getSampleCount() + "]";
    }
}
