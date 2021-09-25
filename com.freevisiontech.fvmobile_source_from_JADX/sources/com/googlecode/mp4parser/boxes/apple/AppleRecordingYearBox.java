package com.googlecode.mp4parser.boxes.apple;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.Utf8;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.lzy.okgo.model.Progress;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class AppleRecordingYearBox extends AppleDataBox {
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    Date date = new Date();

    /* renamed from: df */
    DateFormat f1032df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ssZ");

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("AppleRecordingYearBox.java", AppleRecordingYearBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDate", "com.googlecode.mp4parser.boxes.apple.AppleRecordingYearBox", "", "", "", "java.util.Date"), 27);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDate", "com.googlecode.mp4parser.boxes.apple.AppleRecordingYearBox", "java.util.Date", Progress.DATE, "", "void"), 31);
    }

    public AppleRecordingYearBox() {
        super("©day", 1);
        this.f1032df.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public Date getDate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.date;
    }

    public void setDate(Date date2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) date2));
        this.date = date2;
    }

    /* access modifiers changed from: protected */
    public byte[] writeData() {
        return Utf8.convert(rfc822toIso8601Date(this.f1032df.format(this.date)));
    }

    /* access modifiers changed from: protected */
    public void parseData(ByteBuffer data) {
        try {
            this.date = this.f1032df.parse(iso8601toRfc822Date(IsoTypeReader.readString(data, data.remaining())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String iso8601toRfc822Date(String iso8601) {
        return iso8601.replaceAll("Z$", "+0000").replaceAll("([0-9][0-9]):([0-9][0-9])$", "$1$2");
    }

    protected static String rfc822toIso8601Date(String rfc622) {
        return rfc622.replaceAll("\\+0000$", "Z");
    }

    /* access modifiers changed from: protected */
    public int getDataLength() {
        return Utf8.convert(rfc822toIso8601Date(this.f1032df.format(this.date))).length;
    }
}
