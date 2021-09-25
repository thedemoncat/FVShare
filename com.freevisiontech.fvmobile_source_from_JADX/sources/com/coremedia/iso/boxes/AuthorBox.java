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

public class AuthorBox extends AbstractFullBox {
    public static final String TYPE = "auth";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private String author;
    private String language;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AuthorBox.java", AuthorBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.AuthorBox", "", "", "", "java.lang.String"), 51);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAuthor", "com.coremedia.iso.boxes.AuthorBox", "", "", "", "java.lang.String"), 60);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.AuthorBox", "java.lang.String", "language", "", "void"), 64);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAuthor", "com.coremedia.iso.boxes.AuthorBox", "java.lang.String", "author", "", "void"), 68);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.AuthorBox", "", "", "", "java.lang.String"), 92);
    }

    public AuthorBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public String getAuthor() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.author;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void setAuthor(String author2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) author2));
        this.author = author2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.author) + 7);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.language = IsoTypeReader.readIso639(content);
        this.author = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.author));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return "AuthorBox[language=" + getLanguage() + ";author=" + getAuthor() + "]";
    }
}
