package com.googlecode.mp4parser.boxes.mp4;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ESDescriptorBox extends AbstractDescriptorBox {
    public static final String TYPE = "esds";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ESDescriptorBox.java", ESDescriptorBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor"), 35);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEsDescriptor", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "com.googlecode.mp4parser.boxes.mp4.objectdescriptors.ESDescriptor", "esDescriptor", "", "void"), 39);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "equals", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "java.lang.Object", "o", "", "boolean"), 44);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hashCode", "com.googlecode.mp4parser.boxes.mp4.ESDescriptorBox", "", "", "", "int"), 55);
    }

    public ESDescriptorBox() {
        super(TYPE);
    }

    public ESDescriptor getEsDescriptor() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return (ESDescriptor) super.getDescriptor();
    }

    public void setEsDescriptor(ESDescriptor esDescriptor) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) esDescriptor));
        super.setDescriptor(esDescriptor);
    }

    public boolean equals(Object o) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, o));
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ESDescriptorBox that = (ESDescriptorBox) o;
        if (this.data != null) {
            if (this.data.equals(that.data)) {
                return true;
            }
        } else if (that.data == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        if (this.data != null) {
            return this.data.hashCode();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        ESDescriptor esd = getEsDescriptor();
        if (esd != null) {
            return (long) (esd.getSize() + 4);
        }
        return (long) (this.data.remaining() + 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        ESDescriptor esd = getEsDescriptor();
        if (esd != null) {
            byteBuffer.put((ByteBuffer) esd.serialize().rewind());
        } else {
            byteBuffer.put(this.data.duplicate());
        }
    }
}
