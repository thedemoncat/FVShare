package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import com.googlecode.mp4parser.util.Logger;
import java.nio.ByteBuffer;
import java.util.Date;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class MediaHeaderBox extends AbstractFullBox {
    private static Logger LOG = Logger.getLogger(MediaHeaderBox.class);
    public static final String TYPE = "mdhd";
    private static final JoinPoint.StaticPart ajc$tjp_0 = null;
    private static final JoinPoint.StaticPart ajc$tjp_1 = null;
    private static final JoinPoint.StaticPart ajc$tjp_10 = null;
    private static final JoinPoint.StaticPart ajc$tjp_2 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private Date creationTime = new Date();
    private long duration;
    private String language = "eng";
    private Date modificationTime = new Date();
    private long timescale;

    private static void ajc$preClinit() {
        Factory factory = new Factory("MediaHeaderBox.java", MediaHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCreationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.util.Date"), 48);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getModificationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.util.Date"), 52);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.lang.String"), 125);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTimescale", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "long"), 56);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDuration", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "long"), 60);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLanguage", "com.coremedia.iso.boxes.MediaHeaderBox", "", "", "", "java.lang.String"), 64);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCreationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "java.util.Date", "creationTime", "", "void"), 81);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setModificationTime", "com.coremedia.iso.boxes.MediaHeaderBox", "java.util.Date", "modificationTime", "", "void"), 85);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTimescale", "com.coremedia.iso.boxes.MediaHeaderBox", "long", "timescale", "", "void"), 89);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDuration", "com.coremedia.iso.boxes.MediaHeaderBox", "long", "duration", "", "void"), 93);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLanguage", "com.coremedia.iso.boxes.MediaHeaderBox", "java.lang.String", "language", "", "void"), 97);
    }

    static {
        ajc$preClinit();
    }

    public MediaHeaderBox() {
        super(TYPE);
    }

    public Date getCreationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.creationTime;
    }

    public Date getModificationTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return this.modificationTime;
    }

    public long getTimescale() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.timescale;
    }

    public long getDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.duration;
    }

    public String getLanguage() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.language;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long contentSize;
        if (getVersion() == 1) {
            contentSize = 4 + 28;
        } else {
            contentSize = 4 + 16;
        }
        return contentSize + 2 + 2;
    }

    public void setCreationTime(Date creationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, (Object) this, (Object) this, (Object) creationTime2));
        this.creationTime = creationTime2;
    }

    public void setModificationTime(Date modificationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, (Object) modificationTime2));
        this.modificationTime = modificationTime2;
    }

    public void setTimescale(long timescale2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, (Object) this, (Object) this, Conversions.longObject(timescale2)));
        this.timescale = timescale2;
    }

    public void setDuration(long duration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, (Object) this, (Object) this, Conversions.longObject(duration2)));
        this.duration = duration2;
    }

    public void setLanguage(String language2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) language2));
        this.language = language2;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        if (getVersion() == 1) {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(content));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(content));
            this.timescale = IsoTypeReader.readUInt32(content);
            this.duration = content.getLong();
        } else {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(content));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(content));
            this.timescale = IsoTypeReader.readUInt32(content);
            this.duration = (long) content.getInt();
        }
        if (this.duration < -1) {
            LOG.logWarn("mdhd duration is not in expected range");
        }
        this.language = IsoTypeReader.readIso639(content);
        IsoTypeReader.readUInt16(content);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, this, this));
        StringBuilder result = new StringBuilder();
        result.append("MediaHeaderBox[");
        result.append("creationTime=").append(getCreationTime());
        result.append(";");
        result.append("modificationTime=").append(getModificationTime());
        result.append(";");
        result.append("timescale=").append(getTimescale());
        result.append(";");
        result.append("duration=").append(getDuration());
        result.append(";");
        result.append("language=").append(getLanguage());
        result.append("]");
        return result.toString();
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.timescale);
            byteBuffer.putLong(this.duration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.timescale);
            byteBuffer.putInt((int) this.duration);
        }
        IsoTypeWriter.writeIso639(byteBuffer, this.language);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
    }
}
