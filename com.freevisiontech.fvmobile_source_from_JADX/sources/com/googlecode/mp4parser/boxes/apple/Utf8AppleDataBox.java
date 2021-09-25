package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public abstract class Utf8AppleDataBox extends AppleDataBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    String value;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("Utf8AppleDataBox.java", Utf8AppleDataBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getValue", "com.googlecode.mp4parser.boxes.apple.Utf8AppleDataBox", "", "", "", "java.lang.String"), 21);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setValue", "com.googlecode.mp4parser.boxes.apple.Utf8AppleDataBox", "java.lang.String", "value", "", "void"), 30);
    }

    protected Utf8AppleDataBox(String type) {
        super(type, 1);
    }

    public String getValue() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        if (!isParsed()) {
            parseDetails();
        }
        return this.value;
    }

    public void setValue(String value2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) value2));
        this.value = value2;
    }

    @DoNotParseDetail
    public byte[] writeData() {
        return Utf8.convert(this.value);
    }

    /* access modifiers changed from: protected */
    public int getDataLength() {
        return this.value.getBytes(Charset.forName("UTF-8")).length;
    }

    /* access modifiers changed from: protected */
    public void parseData(ByteBuffer data) {
        this.value = IsoTypeReader.readString(data, data.remaining());
    }
}
