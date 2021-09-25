package com.coremedia.iso.boxes;

import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class SubtitleMediaHeaderBox extends AbstractMediaHeaderBox {
    public static final String TYPE = "sthd";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("SubtitleMediaHeaderBox.java", SubtitleMediaHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.SubtitleMediaHeaderBox", "", "", "", "java.lang.String"), 30);
    }

    public SubtitleMediaHeaderBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return 4;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return "SubtitleMediaHeaderBox";
    }
}
