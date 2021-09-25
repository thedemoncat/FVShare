package com.coremedia.iso.boxes.threegpp26244;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.cookie.SerializableCookie;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class LocationInformationBox extends AbstractFullBox {
    public static final String TYPE = "loci";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private String additionalNotes = "";
    private double altitude;
    private String astronomicalBody = "";
    private String language;
    private double latitude;
    private double longitude;
    private String name = "";
    private int role;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("LocationInformationBox.java", LocationInformationBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "java.lang.String"), 30);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "java.lang.String", "language", "", "void"), 34);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAltitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "double"), 70);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAltitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "double", "altitude", "", "void"), 74);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAstronomicalBody", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "java.lang.String"), 78);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAstronomicalBody", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "java.lang.String", "astronomicalBody", "", "void"), 82);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAdditionalNotes", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "java.lang.String"), 86);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAdditionalNotes", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "java.lang.String", "additionalNotes", "", "void"), 90);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getName", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "java.lang.String"), 38);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setName", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "java.lang.String", SerializableCookie.NAME, "", "void"), 42);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRole", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "int"), 46);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRole", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "int", "role", "", "void"), 50);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLongitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "double"), 54);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLongitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "double", "longitude", "", "void"), 58);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLatitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "", "", "", "double"), 62);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLatitude", "com.coremedia.iso.boxes.threegpp26244.LocationInformationBox", "double", "latitude", "", "void"), 66);
    }

    public LocationInformationBox() {
        super(TYPE);
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.language;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public String getName() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.name;
    }

    public void setName(String name2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) name2));
        this.name = name2;
    }

    public int getRole() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.role;
    }

    public void setRole(int role2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.intObject(role2)));
        this.role = role2;
    }

    public double getLongitude() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.longitude;
    }

    public void setLongitude(double longitude2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.doubleObject(longitude2)));
        this.longitude = longitude2;
    }

    public double getLatitude() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.latitude;
    }

    public void setLatitude(double latitude2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.doubleObject(latitude2)));
        this.latitude = latitude2;
    }

    public double getAltitude() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.altitude;
    }

    public void setAltitude(double altitude2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.doubleObject(altitude2)));
        this.altitude = altitude2;
    }

    public String getAstronomicalBody() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.astronomicalBody;
    }

    public void setAstronomicalBody(String astronomicalBody2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) astronomicalBody2));
        this.astronomicalBody = astronomicalBody2;
    }

    public String getAdditionalNotes() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, this, this));
        return this.additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, (Object) additionalNotes2));
        this.additionalNotes = additionalNotes2;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.convert(this.name).length + 22 + Utf8.convert(this.astronomicalBody).length + Utf8.convert(this.additionalNotes).length);
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.language = IsoTypeReader.readIso639(content);
        this.name = IsoTypeReader.readString(content);
        this.role = IsoTypeReader.readUInt8(content);
        this.longitude = IsoTypeReader.readFixedPoint1616(content);
        this.latitude = IsoTypeReader.readFixedPoint1616(content);
        this.altitude = IsoTypeReader.readFixedPoint1616(content);
        this.astronomicalBody = IsoTypeReader.readString(content);
        this.additionalNotes = IsoTypeReader.readString(content);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        byteBuffer.put(Utf8.convert(this.name));
        byteBuffer.put((byte) 0);
        IsoTypeWriter.writeUInt8(byteBuffer, this.role);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.longitude);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.latitude);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.altitude);
        byteBuffer.put(Utf8.convert(this.astronomicalBody));
        byteBuffer.put((byte) 0);
        byteBuffer.put(Utf8.convert(this.additionalNotes));
        byteBuffer.put((byte) 0);
    }
}
