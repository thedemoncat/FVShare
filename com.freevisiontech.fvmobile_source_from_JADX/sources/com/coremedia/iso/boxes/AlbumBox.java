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
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AlbumBox extends AbstractFullBox {
    public static final String TYPE = "albm";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private String albumTitle;
    private String language;
    private int trackNumber;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AlbumBox.java", AlbumBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.AlbumBox", "", "", "", "java.lang.String"), 51);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAlbumTitle", "com.coremedia.iso.boxes.AlbumBox", "", "", "", "java.lang.String"), 55);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTrackNumber", "com.coremedia.iso.boxes.AlbumBox", "", "", "", "int"), 59);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.AlbumBox", "java.lang.String", "language", "", "void"), 63);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAlbumTitle", "com.coremedia.iso.boxes.AlbumBox", "java.lang.String", "albumTitle", "", "void"), 67);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTrackNumber", "com.coremedia.iso.boxes.AlbumBox", "int", "trackNumber", "", "void"), 71);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.AlbumBox", "", "", "", "java.lang.String"), 103);
    }

    public AlbumBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public String getAlbumTitle() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.albumTitle;
    }

    public int getTrackNumber() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.trackNumber;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void setAlbumTitle(String albumTitle2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, (Object) albumTitle2));
        this.albumTitle = albumTitle2;
    }

    public void setTrackNumber(int trackNumber2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(trackNumber2)));
        this.trackNumber = trackNumber2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.trackNumber == -1 ? 0 : 1) + Utf8.utf8StringLengthInBytes(this.albumTitle) + 6 + 1);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.language = IsoTypeReader.readIso639(content);
        this.albumTitle = IsoTypeReader.readString(content);
        if (content.remaining() > 0) {
            this.trackNumber = IsoTypeReader.readUInt8(content);
        } else {
            this.trackNumber = -1;
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.albumTitle));
        byteBuffer.put((byte) 0);
        if (this.trackNumber != -1) {
            IsoTypeWriter.writeUInt8(byteBuffer, this.trackNumber);
        }
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        StringBuilder buffer = new StringBuilder();
        buffer.append("AlbumBox[language=").append(getLanguage()).append(";");
        buffer.append("albumTitle=").append(getAlbumTitle());
        if (this.trackNumber >= 0) {
            buffer.append(";trackNumber=").append(getTrackNumber());
        }
        buffer.append("]");
        return buffer.toString();
    }
}
