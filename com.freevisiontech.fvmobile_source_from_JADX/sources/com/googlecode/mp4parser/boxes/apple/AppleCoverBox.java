package com.googlecode.mp4parser.boxes.apple;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.cache.CacheEntity;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AppleCoverBox extends AppleDataBox {
    private static final int IMAGE_TYPE_JPG = 13;
    private static final int IMAGE_TYPE_PNG = 14;
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private byte[] data;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleCoverBox.java", AppleCoverBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCoverData", "com.googlecode.mp4parser.boxes.apple.AppleCoverBox", "", "", "", "[B"), 21);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setJpg", "com.googlecode.mp4parser.boxes.apple.AppleCoverBox", "[B", CacheEntity.DATA, "", "void"), 25);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPng", "com.googlecode.mp4parser.boxes.apple.AppleCoverBox", "[B", CacheEntity.DATA, "", "void"), 29);
    }

    public AppleCoverBox() {
        super("covr", 1);
    }

    public byte[] getCoverData() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.data;
    }

    public void setJpg(byte[] data2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) data2));
        setImageData(data2, 13);
    }

    public void setPng(byte[] data2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) data2));
        setImageData(data2, 14);
    }

    /* access modifiers changed from: protected */
    public byte[] writeData() {
        return this.data;
    }

    /* access modifiers changed from: protected */
    public void parseData(ByteBuffer data2) {
        this.data = new byte[data2.limit()];
        data2.get(this.data);
    }

    /* access modifiers changed from: protected */
    public int getDataLength() {
        return this.data.length;
    }

    private void setImageData(byte[] data2, int dataType) {
        this.data = data2;
        this.dataType = dataType;
    }
}
