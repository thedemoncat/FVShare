package com.coremedia.iso.boxes.vodafone;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class LyricsUriBox extends AbstractFullBox {
    public static final String TYPE = "lrcu";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private String lyricsUri;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("LyricsUriBox.java", LyricsUriBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLyricsUri", "com.coremedia.iso.boxes.vodafone.LyricsUriBox", "", "", "", "java.lang.String"), 39);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLyricsUri", "com.coremedia.iso.boxes.vodafone.LyricsUriBox", "java.lang.String", "lyricsUri", "", "void"), 43);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.vodafone.LyricsUriBox", "", "", "", "java.lang.String"), 64);
    }

    public LyricsUriBox() {
        super(TYPE);
    }

    public String getLyricsUri() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.lyricsUri;
    }

    public void setLyricsUri(String lyricsUri2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) lyricsUri2));
        this.lyricsUri = lyricsUri2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.lyricsUri) + 5);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.lyricsUri = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(Utf8.convert(this.lyricsUri));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "LyricsUriBox[lyricsUri=" + getLyricsUri() + "]";
    }
}
