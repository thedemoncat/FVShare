package com.coremedia.iso.boxes;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.cache.CacheEntity;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ItemDataBox extends AbstractBox {
    public static final String TYPE = "idat";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    ByteBuffer data = ByteBuffer.allocate(0);

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ItemDataBox.java", ItemDataBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getData", "com.coremedia.iso.boxes.ItemDataBox", "", "", "", "java.nio.ByteBuffer"), 19);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setData", "com.coremedia.iso.boxes.ItemDataBox", "java.nio.ByteBuffer", CacheEntity.DATA, "", "void"), 23);
    }

    public ItemDataBox() {
        super(TYPE);
    }

    public ByteBuffer getData() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.data;
    }

    public void setData(ByteBuffer data2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) data2));
        this.data = data2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) this.data.limit();
    }

    public void _parseDetails(ByteBuffer content) {
        this.data = content.slice();
        content.position(content.position() + content.remaining());
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(this.data);
    }
}
