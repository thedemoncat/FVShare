package com.coremedia.iso.boxes;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.cache.CacheEntity;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class FreeSpaceBox extends AbstractBox {
    public static final String TYPE = "skip";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    byte[] data;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("FreeSpaceBox.java", FreeSpaceBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setData", "com.coremedia.iso.boxes.FreeSpaceBox", "[B", CacheEntity.DATA, "", "void"), 42);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getData", "com.coremedia.iso.boxes.FreeSpaceBox", "", "", "", "[B"), 46);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.FreeSpaceBox", "", "", "", "java.lang.String"), 61);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) this.data.length;
    }

    public FreeSpaceBox() {
        super(TYPE);
    }

    public void setData(byte[] data2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, (Object) this, (Object) this, (Object) data2));
        this.data = data2;
    }

    public byte[] getData() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.data;
    }

    public void _parseDetails(ByteBuffer content) {
        this.data = new byte[content.remaining()];
        content.get(this.data);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(this.data);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "FreeSpaceBox[size=" + this.data.length + ";type=" + getType() + "]";
    }
}
