package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.annotations.DoNotParseDetail;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class FileTypeBox extends AbstractBox {
    public static final String TYPE = "ftyp";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private List<String> compatibleBrands = Collections.emptyList();
    private String majorBrand;
    private long minorVersion;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("FileTypeBox.java", FileTypeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.lang.String"), 85);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMajorBrand", "com.coremedia.iso.boxes.FileTypeBox", "java.lang.String", "majorBrand", "", "void"), 94);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "long", "minorVersion", "", "void"), 103);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMinorVersion", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "long"), 113);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "", "", "", "java.util.List"), 122);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCompatibleBrands", "com.coremedia.iso.boxes.FileTypeBox", "java.util.List", "compatibleBrands", "", "void"), 126);
    }

    public FileTypeBox() {
        super(TYPE);
    }

    public FileTypeBox(String majorBrand2, long minorVersion2, List<String> compatibleBrands2) {
        super(TYPE);
        this.majorBrand = majorBrand2;
        this.minorVersion = minorVersion2;
        this.compatibleBrands = compatibleBrands2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) ((this.compatibleBrands.size() * 4) + 8);
    }

    public void _parseDetails(ByteBuffer content) {
        this.majorBrand = IsoTypeReader.read4cc(content);
        this.minorVersion = IsoTypeReader.readUInt32(content);
        int compatibleBrandsCount = content.remaining() / 4;
        this.compatibleBrands = new LinkedList();
        for (int i = 0; i < compatibleBrandsCount; i++) {
            this.compatibleBrands.add(IsoTypeReader.read4cc(content));
        }
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        byteBuffer.put(IsoFile.fourCCtoBytes(this.majorBrand));
        IsoTypeWriter.writeUInt32(byteBuffer, this.minorVersion);
        for (String compatibleBrand : this.compatibleBrands) {
            byteBuffer.put(IsoFile.fourCCtoBytes(compatibleBrand));
        }
    }

    public String getMajorBrand() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.majorBrand;
    }

    public void setMajorBrand(String majorBrand2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) majorBrand2));
        this.majorBrand = majorBrand2;
    }

    public void setMinorVersion(long minorVersion2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, (Object) this, (Object) this, Conversions.longObject(minorVersion2)));
        this.minorVersion = minorVersion2;
    }

    public long getMinorVersion() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.minorVersion;
    }

    public List<String> getCompatibleBrands() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.compatibleBrands;
    }

    public void setCompatibleBrands(List<String> compatibleBrands2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) compatibleBrands2));
        this.compatibleBrands = compatibleBrands2;
    }

    @DoNotParseDetail
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("FileTypeBox[");
        result.append("majorBrand=").append(getMajorBrand());
        result.append(";");
        result.append("minorVersion=").append(getMinorVersion());
        for (String compatibleBrand : this.compatibleBrands) {
            result.append(";");
            result.append("compatibleBrand=").append(compatibleBrand);
        }
        result.append("]");
        return result.toString();
    }
}
