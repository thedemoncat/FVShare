package com.coremedia.iso.boxes.vodafone;

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

public class ContentDistributorIdBox extends AbstractFullBox {
    public static final String TYPE = "cdis";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private String contentDistributorId;
    private String language;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ContentDistributorIdBox.java", ContentDistributorIdBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.vodafone.ContentDistributorIdBox", "", "", "", "java.lang.String"), 40);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getContentDistributorId", "com.coremedia.iso.boxes.vodafone.ContentDistributorIdBox", "", "", "", "java.lang.String"), 44);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.vodafone.ContentDistributorIdBox", "", "", "", "java.lang.String"), 68);
    }

    public ContentDistributorIdBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public String getContentDistributorId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.contentDistributorId;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.contentDistributorId) + 2 + 5);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.language = IsoTypeReader.readIso639(content);
        this.contentDistributorId = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.contentDistributorId));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "ContentDistributorIdBox[language=" + getLanguage() + ";contentDistributorId=" + getContentDistributorId() + "]";
    }
}
