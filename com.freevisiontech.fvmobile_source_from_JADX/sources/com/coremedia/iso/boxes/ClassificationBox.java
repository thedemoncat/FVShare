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
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ClassificationBox extends AbstractFullBox {
    public static final String TYPE = "clsf";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private String classificationEntity;
    private String classificationInfo;
    private int classificationTableIndex;
    private String language;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ClassificationBox.java", ClassificationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.ClassificationBox", "", "", "", "java.lang.String"), 44);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getClassificationEntity", "com.coremedia.iso.boxes.ClassificationBox", "", "", "", "java.lang.String"), 48);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getClassificationTableIndex", "com.coremedia.iso.boxes.ClassificationBox", "", "", "", "int"), 52);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getClassificationInfo", "com.coremedia.iso.boxes.ClassificationBox", "", "", "", "java.lang.String"), 56);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setClassificationEntity", "com.coremedia.iso.boxes.ClassificationBox", "java.lang.String", "classificationEntity", "", "void"), 60);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setClassificationTableIndex", "com.coremedia.iso.boxes.ClassificationBox", "int", "classificationTableIndex", "", "void"), 64);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.ClassificationBox", "java.lang.String", "language", "", "void"), 68);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setClassificationInfo", "com.coremedia.iso.boxes.ClassificationBox", "java.lang.String", "classificationInfo", "", "void"), 72);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.ClassificationBox", "", "", "", "java.lang.String"), 101);
    }

    public ClassificationBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public String getClassificationEntity() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.classificationEntity;
    }

    public int getClassificationTableIndex() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.classificationTableIndex;
    }

    public String getClassificationInfo() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.classificationInfo;
    }

    public void setClassificationEntity(String classificationEntity2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, (Object) classificationEntity2));
        this.classificationEntity = classificationEntity2;
    }

    public void setClassificationTableIndex(int classificationTableIndex2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(classificationTableIndex2)));
        this.classificationTableIndex = classificationTableIndex2;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void setClassificationInfo(String classificationInfo2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, (Object) classificationInfo2));
        this.classificationInfo = classificationInfo2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.classificationInfo) + 8 + 1);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        byte[] cE = new byte[4];
        content.get(cE);
        this.classificationEntity = IsoFile.bytesToFourCC(cE);
        this.classificationTableIndex = IsoTypeReader.readUInt16(content);
        this.language = IsoTypeReader.readIso639(content);
        this.classificationInfo = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(IsoFile.fourCCtoBytes(this.classificationEntity));
        IsoTypeWriter.writeUInt16(byteBuffer, this.classificationTableIndex);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.classificationInfo));
        byteBuffer.put((byte) 0);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        StringBuilder buffer = new StringBuilder();
        buffer.append("ClassificationBox[language=").append(getLanguage());
        buffer.append("classificationEntity=").append(getClassificationEntity());
        buffer.append(";classificationTableIndex=").append(getClassificationTableIndex());
        buffer.append(";language=").append(getLanguage());
        buffer.append(";classificationInfo=").append(getClassificationInfo());
        buffer.append("]");
        return buffer.toString();
    }
}
