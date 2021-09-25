package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class PerformerBox extends AbstractFullBox {
    public static final String TYPE = "perf";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private String language;
    private String performer;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("PerformerBox.java", PerformerBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.PerformerBox", "", "", "", "java.lang.String"), 41);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPerformer", "com.coremedia.iso.boxes.PerformerBox", "", "", "", "java.lang.String"), 45);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.PerformerBox", "java.lang.String", "language", "", "void"), 49);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPerformer", "com.coremedia.iso.boxes.PerformerBox", "java.lang.String", "performer", "", "void"), 53);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.PerformerBox", "", "", "", "java.lang.String"), 76);
    }

    public PerformerBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public String getPerformer() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.performer;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void setPerformer(String performer2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) performer2));
        this.performer = performer2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.performer) + 6 + 1);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.performer));
        byteBuffer.put((byte) 0);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.language = IsoTypeReader.readIso639(content);
        this.performer = IsoTypeReader.readString(content);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return "PerformerBox[language=" + getLanguage() + ";performer=" + getPerformer() + "]";
    }
}
