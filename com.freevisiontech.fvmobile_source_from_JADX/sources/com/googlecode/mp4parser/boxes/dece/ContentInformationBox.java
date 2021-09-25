package com.googlecode.mp4parser.boxes.dece;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class ContentInformationBox extends AbstractFullBox {
    public static final String TYPE = "cinf";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    Map<String, String> brandEntries = new LinkedHashMap();
    String codecs;
    Map<String, String> idEntries = new LinkedHashMap();
    String languages;
    String mimeSubtypeName;
    String profileLevelIdc;
    String protection;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("ContentInformationBox.java", ContentInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMimeSubtypeName", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 144);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMimeSubtypeName", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "mimeSubtypeName", "", "void"), 148);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBrandEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.util.Map"), (int) CompanyIdentifierResolver.QUALCOMM_INNOVATION_CENTER_INC_QUIC);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBrandEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.util.Map", "brandEntries", "", "void"), (int) CompanyIdentifierResolver.ACE_SENSOR_INC);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getIdEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.util.Map"), 192);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setIdEntries", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.util.Map", "idEntries", "", "void"), 196);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProfileLevelIdc", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 152);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProfileLevelIdc", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "profileLevelIdc", "", "void"), (int) CompanyIdentifierResolver.COLORFY_INC);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCodecs", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), 160);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCodecs", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "codecs", "", "void"), (int) CompanyIdentifierResolver.LINAK_A_S);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getProtection", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.ARP_DEVICES_LIMITED);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setProtection", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "protection", "", "void"), (int) CompanyIdentifierResolver.GREEN_THROTTLE_GAMES);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguages", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.PASSIF_SEMICONDUCTOR_CORP);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguages", "com.googlecode.mp4parser.boxes.dece.ContentInformationBox", "java.lang.String", "languages", "", "void"), 180);
    }

    public ContentInformationBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size = 4 + ((long) (Utf8.utf8StringLengthInBytes(this.mimeSubtypeName) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.profileLevelIdc) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.codecs) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.protection) + 1)) + ((long) (Utf8.utf8StringLengthInBytes(this.languages) + 1)) + 1;
        for (Map.Entry brandEntry : this.brandEntries.entrySet()) {
            size = size + ((long) (Utf8.utf8StringLengthInBytes((String) brandEntry.getKey()) + 1)) + ((long) (Utf8.utf8StringLengthInBytes((String) brandEntry.getValue()) + 1));
        }
        long size2 = size + 1;
        for (Map.Entry idEntry : this.idEntries.entrySet()) {
            size2 = size2 + ((long) (Utf8.utf8StringLengthInBytes((String) idEntry.getKey()) + 1)) + ((long) (Utf8.utf8StringLengthInBytes((String) idEntry.getValue()) + 1));
        }
        return size2;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.mimeSubtypeName);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.profileLevelIdc);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.codecs);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.protection);
        IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, this.languages);
        IsoTypeWriter.writeUInt8(byteBuffer, this.brandEntries.size());
        for (Map.Entry brandEntry : this.brandEntries.entrySet()) {
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) brandEntry.getKey());
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) brandEntry.getValue());
        }
        IsoTypeWriter.writeUInt8(byteBuffer, this.idEntries.size());
        for (Map.Entry idEntry : this.idEntries.entrySet()) {
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) idEntry.getKey());
            IsoTypeWriter.writeZeroTermUtf8String(byteBuffer, (String) idEntry.getValue());
        }
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.mimeSubtypeName = IsoTypeReader.readString(content);
        this.profileLevelIdc = IsoTypeReader.readString(content);
        this.codecs = IsoTypeReader.readString(content);
        this.protection = IsoTypeReader.readString(content);
        this.languages = IsoTypeReader.readString(content);
        int brandEntryCount = IsoTypeReader.readUInt8(content);
        while (true) {
            int brandEntryCount2 = brandEntryCount;
            brandEntryCount = brandEntryCount2 - 1;
            if (brandEntryCount2 <= 0) {
                break;
            }
            this.brandEntries.put(IsoTypeReader.readString(content), IsoTypeReader.readString(content));
        }
        int idEntryCount = IsoTypeReader.readUInt8(content);
        while (true) {
            int idEntryCount2 = idEntryCount;
            idEntryCount = idEntryCount2 - 1;
            if (idEntryCount2 > 0) {
                this.idEntries.put(IsoTypeReader.readString(content), IsoTypeReader.readString(content));
            } else {
                return;
            }
        }
    }

    public static class BrandEntry {
        String iso_brand;
        String version;

        public BrandEntry(String iso_brand2, String version2) {
            this.iso_brand = iso_brand2;
            this.version = version2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BrandEntry that = (BrandEntry) o;
            if (this.iso_brand == null ? that.iso_brand != null : !this.iso_brand.equals(that.iso_brand)) {
                return false;
            }
            if (this.version != null) {
                if (this.version.equals(that.version)) {
                    return true;
                }
            } else if (that.version == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result;
            int i = 0;
            if (this.iso_brand != null) {
                result = this.iso_brand.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.version != null) {
                i = this.version.hashCode();
            }
            return i2 + i;
        }
    }

    public String getMimeSubtypeName() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.mimeSubtypeName;
    }

    public void setMimeSubtypeName(String mimeSubtypeName2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) mimeSubtypeName2));
        this.mimeSubtypeName = mimeSubtypeName2;
    }

    public String getProfileLevelIdc() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.profileLevelIdc;
    }

    public void setProfileLevelIdc(String profileLevelIdc2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) profileLevelIdc2));
        this.profileLevelIdc = profileLevelIdc2;
    }

    public String getCodecs() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.codecs;
    }

    public void setCodecs(String codecs2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) codecs2));
        this.codecs = codecs2;
    }

    public String getProtection() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.protection;
    }

    public void setProtection(String protection2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, (Object) protection2));
        this.protection = protection2;
    }

    public String getLanguages() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.languages;
    }

    public void setLanguages(String languages2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) languages2));
        this.languages = languages2;
    }

    public Map<String, String> getBrandEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.brandEntries;
    }

    public void setBrandEntries(Map<String, String> brandEntries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, (Object) brandEntries2));
        this.brandEntries = brandEntries2;
    }

    public Map<String, String> getIdEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.idEntries;
    }

    public void setIdEntries(Map<String, String> idEntries2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) idEntries2));
        this.idEntries = idEntries2;
    }
}
