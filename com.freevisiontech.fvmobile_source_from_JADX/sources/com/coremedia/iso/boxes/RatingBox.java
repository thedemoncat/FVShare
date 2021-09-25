package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoFile;
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

public class RatingBox extends AbstractFullBox {
    public static final String TYPE = "rtng";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private String language;
    private String ratingCriteria;
    private String ratingEntity;
    private String ratingInfo;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("RatingBox.java", RatingBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRatingEntity", "com.coremedia.iso.boxes.RatingBox", "java.lang.String", "ratingEntity", "", "void"), 46);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRatingCriteria", "com.coremedia.iso.boxes.RatingBox", "java.lang.String", "ratingCriteria", "", "void"), 50);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.RatingBox", "java.lang.String", "language", "", "void"), 54);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRatingInfo", "com.coremedia.iso.boxes.RatingBox", "java.lang.String", "ratingInfo", "", "void"), 58);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.RatingBox", "", "", "", "java.lang.String"), 62);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRatingEntity", "com.coremedia.iso.boxes.RatingBox", "", "", "", "java.lang.String"), 73);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRatingCriteria", "com.coremedia.iso.boxes.RatingBox", "", "", "", "java.lang.String"), 83);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRatingInfo", "com.coremedia.iso.boxes.RatingBox", "", "", "", "java.lang.String"), 87);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.RatingBox", "", "", "", "java.lang.String"), 115);
    }

    public RatingBox() {
        super(TYPE);
    }

    public void setRatingEntity(String ratingEntity2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, (Object) this, (Object) this, (Object) ratingEntity2));
        this.ratingEntity = ratingEntity2;
    }

    public void setRatingCriteria(String ratingCriteria2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) ratingCriteria2));
        this.ratingCriteria = ratingCriteria2;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void setRatingInfo(String ratingInfo2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) ratingInfo2));
        this.ratingInfo = ratingInfo2;
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.language;
    }

    public String getRatingEntity() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.ratingEntity;
    }

    public String getRatingCriteria() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.ratingCriteria;
    }

    public String getRatingInfo() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.ratingInfo;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.ratingInfo) + 15);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.ratingEntity = IsoTypeReader.read4cc(content);
        this.ratingCriteria = IsoTypeReader.read4cc(content);
        this.language = IsoTypeReader.readIso639(content);
        this.ratingInfo = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        byteBuffer.put(IsoFile.fourCCtoBytes(this.ratingEntity));
        byteBuffer.put(IsoFile.fourCCtoBytes(this.ratingCriteria));
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.ratingInfo));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        StringBuilder buffer = new StringBuilder();
        buffer.append("RatingBox[language=").append(getLanguage());
        buffer.append("ratingEntity=").append(getRatingEntity());
        buffer.append(";ratingCriteria=").append(getRatingCriteria());
        buffer.append(";language=").append(getLanguage());
        buffer.append(";ratingInfo=").append(getRatingInfo());
        buffer.append("]");
        return buffer.toString();
    }
}
