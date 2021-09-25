package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.sampleentry.SampleEntry;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.googlecode.mp4parser.AbstractBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Collections;
import java.util.List;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TimeCodeBox extends AbstractBox implements SampleEntry, Container {
    public static final String TYPE = "tmcd";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_11 = null;
    private static final JoinPoint.StaticPart ajc$tjp_12 = null;
    private static final JoinPoint.StaticPart ajc$tjp_13 = null;
    private static final JoinPoint.StaticPart ajc$tjp_14 = null;
    private static final JoinPoint.StaticPart ajc$tjp_15 = null;
    private static final JoinPoint.StaticPart ajc$tjp_16 = null;
    private static final JoinPoint.StaticPart ajc$tjp_17 = null;
    private static final JoinPoint.StaticPart ajc$tjp_18 = null;
    private static final JoinPoint.StaticPart ajc$tjp_19 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_20 = null;
    private static final JoinPoint.StaticPart ajc$tjp_21 = null;
    private static final JoinPoint.StaticPart ajc$tjp_22 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    int dataReferenceIndex;
    long flags;
    int frameDuration;
    int numberOfFrames;
    int reserved1;
    int reserved2;
    byte[] rest = new byte[0];
    int timeScale;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("TimeCodeBox.java", TimeCodeBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDataReferenceIndex", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), 88);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDataReferenceIndex", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "dataReferenceIndex", "", "void"), 92);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved1", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "reserved1", "", "void"), 137);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved2", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), (int) CompanyIdentifierResolver.ZSCAN_SOFTWARE);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setReserved2", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "reserved2", "", "void"), 145);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFlags", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "long"), 149);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFlags", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "long", DownloaderServiceMarshaller.PARAMS_FLAGS, "", "void"), 153);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRest", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "[B"), (int) CompanyIdentifierResolver.GEOFORCE_INC);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRest", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "[B", "rest", "", "void"), (int) CompanyIdentifierResolver.SRMEDIZINELEKTRONIK);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBoxes", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "java.util.List"), (int) CompanyIdentifierResolver.PANDA_OCEAN_INC);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBoxes", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "java.util.List", "boxes", "", "void"), (int) CompanyIdentifierResolver.CAEN_RFID_SRL);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBoxes", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "java.lang.Class", "clazz", "", "java.util.List"), (int) CompanyIdentifierResolver.OMEGAWAVE_OY);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "java.lang.String"), 98);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBoxes", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "java.lang.Class:boolean", "clazz:recursive", "", "java.util.List"), (int) CompanyIdentifierResolver.BEKEY_A_S);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getByteBuffer", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "long:long", "start:size", "java.io.IOException", "java.nio.ByteBuffer"), (int) CompanyIdentifierResolver.MESO_INTERNATIONAL);
        ajc$tjp_22 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "writeContainer", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "java.nio.channels.WritableByteChannel", "bb", "java.io.IOException", "void"), (int) CompanyIdentifierResolver.STARKEY_LABORATORIES_INC);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTimeScale", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), 109);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTimeScale", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "timeScale", "", "void"), 113);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getFrameDuration", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), 117);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setFrameDuration", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "frameDuration", "", "void"), 121);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getNumberOfFrames", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), 125);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setNumberOfFrames", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "int", "numberOfFrames", "", "void"), 129);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getReserved1", "com.googlecode.mp4parser.boxes.apple.TimeCodeBox", "", "", "", "int"), 133);
    }

    public TimeCodeBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (this.rest.length + 28);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer bb) {
        bb.put(new byte[6]);
        IsoTypeWriter.writeUInt16(bb, this.dataReferenceIndex);
        bb.putInt(this.reserved1);
        IsoTypeWriter.writeUInt32(bb, this.flags);
        bb.putInt(this.timeScale);
        bb.putInt(this.frameDuration);
        IsoTypeWriter.writeUInt8(bb, this.numberOfFrames);
        IsoTypeWriter.writeUInt24(bb, this.reserved2);
        bb.put(this.rest);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        content.position(6);
        this.dataReferenceIndex = IsoTypeReader.readUInt16(content);
        this.reserved1 = content.getInt();
        this.flags = IsoTypeReader.readUInt32(content);
        this.timeScale = content.getInt();
        this.frameDuration = content.getInt();
        this.numberOfFrames = IsoTypeReader.readUInt8(content);
        this.reserved2 = IsoTypeReader.readUInt24(content);
        this.rest = new byte[content.remaining()];
        content.get(this.rest);
    }

    public int getDataReferenceIndex() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.dataReferenceIndex;
    }

    public void setDataReferenceIndex(int dataReferenceIndex2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, Conversions.intObject(dataReferenceIndex2)));
        this.dataReferenceIndex = dataReferenceIndex2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return "TimeCodeBox{timeScale=" + this.timeScale + ", frameDuration=" + this.frameDuration + ", numberOfFrames=" + this.numberOfFrames + ", reserved1=" + this.reserved1 + ", reserved2=" + this.reserved2 + ", flags=" + this.flags + '}';
    }

    public int getTimeScale() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.timeScale;
    }

    public void setTimeScale(int timeScale2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, (Object) this, (Object) this, Conversions.intObject(timeScale2)));
        this.timeScale = timeScale2;
    }

    public int getFrameDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.frameDuration;
    }

    public void setFrameDuration(int frameDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, Conversions.intObject(frameDuration2)));
        this.frameDuration = frameDuration2;
    }

    public int getNumberOfFrames() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.numberOfFrames;
    }

    public void setNumberOfFrames(int numberOfFrames2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, (Object) this, (Object) this, Conversions.intObject(numberOfFrames2)));
        this.numberOfFrames = numberOfFrames2;
    }

    public int getReserved1() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.reserved1;
    }

    public void setReserved1(int reserved12) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, Conversions.intObject(reserved12)));
        this.reserved1 = reserved12;
    }

    public int getReserved2() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        return this.reserved2;
    }

    public void setReserved2(int reserved22) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, Conversions.intObject(reserved22)));
        this.reserved2 = reserved22;
    }

    public long getFlags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, this, this));
        return this.flags;
    }

    public void setFlags(long flags2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.longObject(flags2)));
        this.flags = flags2;
    }

    public byte[] getRest() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, this, this));
        return this.rest;
    }

    public void setRest(byte[] rest2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, (Object) this, (Object) this, (Object) rest2));
        this.rest = rest2;
    }

    public List<Box> getBoxes() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, this, this));
        return Collections.emptyList();
    }

    public void setBoxes(List<Box> boxes) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, (Object) this, (Object) this, (Object) boxes));
        throw new RuntimeException("Time Code Box doesn't accept any children");
    }

    public <T extends Box> List<T> getBoxes(Class<T> clazz) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, (Object) clazz));
        return Collections.emptyList();
    }

    public <T extends Box> List<T> getBoxes(Class<T> clazz, boolean recursive) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, this, this, clazz, Conversions.booleanObject(recursive)));
        return Collections.emptyList();
    }

    public ByteBuffer getByteBuffer(long start, long size) throws IOException {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, this, this, Conversions.longObject(start), Conversions.longObject(size)));
        return null;
    }

    public void writeContainer(WritableByteChannel bb) throws IOException {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_22, (Object) this, (Object) this, (Object) bb));
    }
}
