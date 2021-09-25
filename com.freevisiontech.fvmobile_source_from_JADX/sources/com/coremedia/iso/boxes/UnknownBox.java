package com.coremedia.iso.boxes;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.cache.CacheEntity;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class UnknownBox extends AbstractBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    ByteBuffer data;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("UnknownBox.java", UnknownBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getData", "com.coremedia.iso.boxes.UnknownBox", "", "", "", "java.nio.ByteBuffer"), 52);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setData", "com.coremedia.iso.boxes.UnknownBox", "java.nio.ByteBuffer", CacheEntity.DATA, "", "void"), 56);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.UnknownBox", "", "", "", "java.lang.String"), 61);
    }

    public UnknownBox(String type) {
        super(type);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) this.data.limit();
    }

    public void _parseDetails(ByteBuffer content) {
        this.data = content;
        content.position(content.position() + content.remaining());
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        this.data.rewind();
        byteBuffer.put(this.data);
    }

    public ByteBuffer getData() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.data;
    }

    public void setData(ByteBuffer data2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) data2));
        this.data = data2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return String.valueOf(getClass().getName()) + "[" + getType() + "]@" + Integer.toHexString(hashCode());
    }
}
