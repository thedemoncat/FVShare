package com.mp4parser.iso14496.part30;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class WebVTTConfigurationBox extends AbstractBox {
    public static final String TYPE = "vttC";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    String config = "";

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("WebVTTConfigurationBox.java", WebVTTConfigurationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getConfig", "com.mp4parser.iso14496.part30.WebVTTConfigurationBox", "", "", "", "java.lang.String"), 36);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setConfig", "com.mp4parser.iso14496.part30.WebVTTConfigurationBox", "java.lang.String", "config", "", "void"), 40);
    }

    public WebVTTConfigurationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) Utf8.utf8StringLengthInBytes(this.config);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(Utf8.convert(this.config));
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        this.config = IsoTypeReader.readString(content, content.remaining());
    }

    public String getConfig() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.config;
    }

    public void setConfig(String config2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) config2));
        this.config = config2;
    }
}
