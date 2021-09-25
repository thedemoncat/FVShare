package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AppleGPSCoordinatesBox extends AbstractBox {
    private static final int DEFAULT_LANG = 5575;
    public static final String TYPE = "©xyz";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    String coords;
    int lang = DEFAULT_LANG;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleGPSCoordinatesBox.java", AppleGPSCoordinatesBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getValue", "com.googlecode.mp4parser.boxes.apple.AppleGPSCoordinatesBox", "", "", "", "java.lang.String"), 22);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setValue", "com.googlecode.mp4parser.boxes.apple.AppleGPSCoordinatesBox", "java.lang.String", "iso6709String", "", "void"), 26);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.apple.AppleGPSCoordinatesBox", "", "", "", "java.lang.String"), 52);
    }

    public AppleGPSCoordinatesBox() {
        super(TYPE);
    }

    public String getValue() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.coords;
    }

    public void setValue(String iso6709String) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) iso6709String));
        this.lang = DEFAULT_LANG;
        this.coords = iso6709String;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.coords) + 4);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.putShort((short) this.coords.length());
        byteBuffer.putShort((short) this.lang);
        byteBuffer.put(Utf8.convert(this.coords));
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        int length = content.getShort();
        this.lang = content.getShort();
        byte[] bytes = new byte[length];
        content.get(bytes);
        this.coords = Utf8.convert(bytes);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "AppleGPSCoordinatesBox[" + this.coords + "]";
    }
}
