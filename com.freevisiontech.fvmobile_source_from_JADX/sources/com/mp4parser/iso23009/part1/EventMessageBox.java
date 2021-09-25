package com.mp4parser.iso23009.part1;

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

public class EventMessageBox extends AbstractFullBox {
    public static final String TYPE = "emsg";
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
    long eventDuration;

    /* renamed from: id */
    long f1049id;
    byte[] messageData;
    long presentationTimeDelta;
    String schemeIdUri;
    long timescale;
    String value;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("EventMessageBox.java", EventMessageBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSchemeIdUri", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "java.lang.String"), 59);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSchemeIdUri", "com.mp4parser.iso23009.part1.EventMessageBox", "java.lang.String", "schemeIdUri", "", "void"), 63);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getId", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "long"), 99);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setId", "com.mp4parser.iso23009.part1.EventMessageBox", "long", "id", "", "void"), 103);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMessageData", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "[B"), 107);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMessageData", "com.mp4parser.iso23009.part1.EventMessageBox", "[B", "messageData", "", "void"), 111);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getValue", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "java.lang.String"), 67);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setValue", "com.mp4parser.iso23009.part1.EventMessageBox", "java.lang.String", "value", "", "void"), 71);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTimescale", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "long"), 75);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTimescale", "com.mp4parser.iso23009.part1.EventMessageBox", "long", "timescale", "", "void"), 79);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPresentationTimeDelta", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "long"), 83);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPresentationTimeDelta", "com.mp4parser.iso23009.part1.EventMessageBox", "long", "presentationTimeDelta", "", "void"), 87);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getEventDuration", "com.mp4parser.iso23009.part1.EventMessageBox", "", "", "", "long"), 91);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEventDuration", "com.mp4parser.iso23009.part1.EventMessageBox", "long", "eventDuration", "", "void"), 95);
    }

    public EventMessageBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.schemeIdUri = IsoTypeReader.readString(content);
        this.value = IsoTypeReader.readString(content);
        this.timescale = IsoTypeReader.readUInt32(content);
        this.presentationTimeDelta = IsoTypeReader.readUInt32(content);
        this.eventDuration = IsoTypeReader.readUInt32(content);
        this.f1049id = IsoTypeReader.readUInt32(content);
        this.messageData = new byte[content.remaining()];
        content.get(this.messageData);
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUtf8String(byteBuffer, this.schemeIdUri);
        IsoTypeWriter.writeUtf8String(byteBuffer, this.value);
        IsoTypeWriter.writeUInt32(byteBuffer, this.timescale);
        IsoTypeWriter.writeUInt32(byteBuffer, this.presentationTimeDelta);
        IsoTypeWriter.writeUInt32(byteBuffer, this.eventDuration);
        IsoTypeWriter.writeUInt32(byteBuffer, this.f1049id);
        byteBuffer.put(this.messageData);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        return (long) (Utf8.utf8StringLengthInBytes(this.schemeIdUri) + 22 + Utf8.utf8StringLengthInBytes(this.value) + this.messageData.length);
    }

    public String getSchemeIdUri() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.schemeIdUri;
    }

    public void setSchemeIdUri(String schemeIdUri2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) schemeIdUri2));
        this.schemeIdUri = schemeIdUri2;
    }

    public String getValue() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.value;
    }

    public void setValue(String value2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, (Object) this, (Object) this, (Object) value2));
        this.value = value2;
    }

    public long getTimescale() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.timescale;
    }

    public void setTimescale(long timescale2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, Conversions.longObject(timescale2)));
        this.timescale = timescale2;
    }

    public long getPresentationTimeDelta() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.presentationTimeDelta;
    }

    public void setPresentationTimeDelta(long presentationTimeDelta2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.longObject(presentationTimeDelta2)));
        this.presentationTimeDelta = presentationTimeDelta2;
    }

    public long getEventDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.eventDuration;
    }

    public void setEventDuration(long eventDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, Conversions.longObject(eventDuration2)));
        this.eventDuration = eventDuration2;
    }

    public long getId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        return this.f1049id;
    }

    public void setId(long id) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.longObject(id)));
        this.f1049id = id;
    }

    public byte[] getMessageData() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, this, this));
        return this.messageData;
    }

    public void setMessageData(byte[] messageData2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) messageData2));
        this.messageData = messageData2;
    }
}
