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

public class WebVTTSourceLabelBox extends AbstractBox {
    public static final String TYPE = "vlab";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    String sourceLabel = "";

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("WebVTTSourceLabelBox.java", WebVTTSourceLabelBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSourceLabel", "com.mp4parser.iso14496.part30.WebVTTSourceLabelBox", "", "", "", "java.lang.String"), 37);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSourceLabel", "com.mp4parser.iso14496.part30.WebVTTSourceLabelBox", "java.lang.String", "sourceLabel", "", "void"), 41);
    }

    public WebVTTSourceLabelBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) Utf8.utf8StringLengthInBytes(this.sourceLabel);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(Utf8.convert(this.sourceLabel));
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        this.sourceLabel = IsoTypeReader.readString(content, content.remaining());
    }

    public String getSourceLabel() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.sourceLabel;
    }

    public void setSourceLabel(String sourceLabel2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) sourceLabel2));
        this.sourceLabel = sourceLabel2;
    }
}
