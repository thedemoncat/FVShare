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
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SyncSampleBox extends AbstractFullBox {
    public static final String TYPE = "stss";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private long[] sampleNumber;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SyncSampleBox.java", SyncSampleBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "[J"), 46);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SyncSampleBox", "", "", "", "java.lang.String"), 77);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleNumber", "com.coremedia.iso.boxes.SyncSampleBox", "[J", "sampleNumber", "", "void"), 81);
    }

    public SyncSampleBox() {
        super(TYPE);
    }

    public long[] getSampleNumber() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.sampleNumber;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.sampleNumber.length * 4) + 8);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int entryCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.sampleNumber = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            this.sampleNumber[i] = IsoTypeReader.readUInt32(content);
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.sampleNumber.length);
        for (long aSampleNumber : this.sampleNumber) {
            IsoTypeWriter.writeUInt32(byteBuffer, aSampleNumber);
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return "SyncSampleBox[entryCount=" + this.sampleNumber.length + "]";
    }

    public void setSampleNumber(long[] sampleNumber2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) sampleNumber2));
        this.sampleNumber = sampleNumber2;
    }
}
