package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.DateHelper;
import com.googlecode.mp4parser.util.Logger;
import com.googlecode.mp4parser.util.Matrix;
import java.nio.ByteBuffer;
import java.util.Date;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

public class MovieHeaderBox extends AbstractFullBox {
    private static Logger LOG = Logger.getLogger(MovieHeaderBox.class);
    public static final String TYPE = "mvhd";
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
    private static final JoinPoint.StaticPart ajc$tjp_23 = null;
    private static final JoinPoint.StaticPart ajc$tjp_24 = null;
    private static final JoinPoint.StaticPart ajc$tjp_25 = null;
    private static final JoinPoint.StaticPart ajc$tjp_26 = null;
    private static final JoinPoint.StaticPart ajc$tjp_27 = null;
    private static final JoinPoint.StaticPart ajc$tjp_28 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private Date creationTime;
    private int currentTime;
    private long duration;
    private Matrix matrix = Matrix.ROTATE_0;
    private Date modificationTime;
    private long nextTrackId;
    private int posterTime;
    private int previewDuration;
    private int previewTime;
    private double rate = 1.0d;
    private int selectionDuration;
    private int selectionTime;
    private long timescale;
    private float volume = 1.0f;

    private static void ajc$preClinit() {
        Factory factory = new Factory("MovieHeaderBox.java", MovieHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCreationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.util.Date"), 66);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getModificationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.util.Date"), 70);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setModificationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "java.util.Date", "modificationTime", "", "void"), (int) CompanyIdentifierResolver.KAWANTECH);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTimescale", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "timescale", "", "void"), (int) CompanyIdentifierResolver.PROCTER_GAMBLE);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "duration", "", "void"), 224);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setRate", "com.coremedia.iso.boxes.MovieHeaderBox", "double", "rate", "", "void"), (int) CompanyIdentifierResolver.KS_TECHNOLOGIES);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setVolume", "com.coremedia.iso.boxes.MovieHeaderBox", "float", "volume", "", "void"), (int) CompanyIdentifierResolver.SERVER_TECHNOLOGY_INC);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMatrix", "com.coremedia.iso.boxes.MovieHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), (int) CompanyIdentifierResolver.BITSPLITTERS_GMBH);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setNextTrackId", "com.coremedia.iso.boxes.MovieHeaderBox", "long", "nextTrackId", "", "void"), (int) CompanyIdentifierResolver.KENT_DISPLAYS_INC);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPreviewTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), (int) CompanyIdentifierResolver.VSN_TECHNOLOGIES_INC);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPreviewTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "previewTime", "", "void"), (int) CompanyIdentifierResolver.KOUKAAM_AS);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPreviewDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), 255);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTimescale", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 74);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPreviewDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "previewDuration", "", "void"), (int) CompanyIdentifierResolver.BANG_OLUFSEN_A_S);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getPosterTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), (int) CompanyIdentifierResolver.WILLIAM_DEMANT_HOLDING_A_S);
        ajc$tjp_22 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setPosterTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "posterTime", "", "void"), (int) CompanyIdentifierResolver.ERI_INC);
        ajc$tjp_23 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSelectionTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), (int) CompanyIdentifierResolver.HISILICON_TECHNOLOGIES_CO_LTD);
        ajc$tjp_24 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSelectionTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "selectionTime", "", "void"), (int) CompanyIdentifierResolver.OPENBRAIN_TECHNOLOGIES_CO_LTD);
        ajc$tjp_25 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSelectionDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), (int) CompanyIdentifierResolver.WIMOTO_TECHNOLOGIES_INC);
        ajc$tjp_26 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSelectionDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "selectionDuration", "", "void"), (int) CompanyIdentifierResolver.ARUBA_NETWORKS);
        ajc$tjp_27 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCurrentTime", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "int"), (int) CompanyIdentifierResolver.VOLKSWAGON_AG);
        ajc$tjp_28 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCurrentTime", "com.coremedia.iso.boxes.MovieHeaderBox", "int", "currentTime", "", "void"), 291);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDuration", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 78);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getRate", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "double"), 82);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getVolume", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "float"), 86);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMatrix", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 90);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getNextTrackId", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "long"), 94);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.MovieHeaderBox", "", "", "", "java.lang.String"), 148);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCreationTime", "com.coremedia.iso.boxes.MovieHeaderBox", "java.util.Date", "creationTime", "", "void"), (int) CompanyIdentifierResolver.BEATS_ELECTRONICS);
    }

    static {
        ajc$preClinit();
    }

    public MovieHeaderBox() {
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

    public double getRate() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.rate;
    }

    public float getVolume() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.volume;
    }

    public Matrix getMatrix() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.matrix;
    }

    public long getNextTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.nextTrackId;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long contentSize;
        if (getVersion() == 1) {
            contentSize = 4 + 28;
        } else {
            contentSize = 4 + 16;
        }
        return contentSize + 80;
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
            LOG.logWarn("mvhd duration is not in expected range");
        }
        this.rate = IsoTypeReader.readFixedPoint1616(content);
        this.volume = IsoTypeReader.readFixedPoint88(content);
        IsoTypeReader.readUInt16(content);
        IsoTypeReader.readUInt32(content);
        IsoTypeReader.readUInt32(content);
        this.matrix = Matrix.fromByteBuffer(content);
        this.previewTime = content.getInt();
        this.previewDuration = content.getInt();
        this.posterTime = content.getInt();
        this.selectionTime = content.getInt();
        this.selectionDuration = content.getInt();
        this.currentTime = content.getInt();
        this.nextTrackId = IsoTypeReader.readUInt32(content);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        StringBuilder result = new StringBuilder();
        result.append("MovieHeaderBox[");
        result.append("creationTime=").append(getCreationTime());
        result.append(";");
        result.append("modificationTime=").append(getModificationTime());
        result.append(";");
        result.append("timescale=").append(getTimescale());
        result.append(";");
        result.append("duration=").append(getDuration());
        result.append(";");
        result.append("rate=").append(getRate());
        result.append(";");
        result.append("volume=").append(getVolume());
        result.append(";");
        result.append("matrix=").append(this.matrix);
        result.append(";");
        result.append("nextTrackId=").append(getNextTrackId());
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
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.rate);
        IsoTypeWriter.writeFixedPoint88(byteBuffer, (double) this.volume);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        this.matrix.getContent(byteBuffer);
        byteBuffer.putInt(this.previewTime);
        byteBuffer.putInt(this.previewDuration);
        byteBuffer.putInt(this.posterTime);
        byteBuffer.putInt(this.selectionTime);
        byteBuffer.putInt(this.selectionDuration);
        byteBuffer.putInt(this.currentTime);
        IsoTypeWriter.writeUInt32(byteBuffer, this.nextTrackId);
    }

    public void setCreationTime(Date creationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, (Object) this, (Object) this, (Object) creationTime2));
        this.creationTime = creationTime2;
        if (DateHelper.convert(creationTime2) >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setVersion(1);
        }
    }

    public void setModificationTime(Date modificationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, (Object) modificationTime2));
        this.modificationTime = modificationTime2;
        if (DateHelper.convert(modificationTime2) >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setVersion(1);
        }
    }

    public void setTimescale(long timescale2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, (Object) this, (Object) this, Conversions.longObject(timescale2)));
        this.timescale = timescale2;
    }

    public void setDuration(long duration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, Conversions.longObject(duration2)));
        this.duration = duration2;
        if (duration2 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setVersion(1);
        }
    }

    public void setRate(double rate2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, Conversions.doubleObject(rate2)));
        this.rate = rate2;
    }

    public void setVolume(float volume2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.floatObject(volume2)));
        this.volume = volume2;
    }

    public void setMatrix(Matrix matrix2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, (Object) matrix2));
        this.matrix = matrix2;
    }

    public void setNextTrackId(long nextTrackId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, (Object) this, (Object) this, Conversions.longObject(nextTrackId2)));
        this.nextTrackId = nextTrackId2;
    }

    public int getPreviewTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, this, this));
        return this.previewTime;
    }

    public void setPreviewTime(int previewTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, (Object) this, (Object) this, Conversions.intObject(previewTime2)));
        this.previewTime = previewTime2;
    }

    public int getPreviewDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, this, this));
        return this.previewDuration;
    }

    public void setPreviewDuration(int previewDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, (Object) this, (Object) this, Conversions.intObject(previewDuration2)));
        this.previewDuration = previewDuration2;
    }

    public int getPosterTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, this, this));
        return this.posterTime;
    }

    public void setPosterTime(int posterTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_22, (Object) this, (Object) this, Conversions.intObject(posterTime2)));
        this.posterTime = posterTime2;
    }

    public int getSelectionTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_23, this, this));
        return this.selectionTime;
    }

    public void setSelectionTime(int selectionTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_24, (Object) this, (Object) this, Conversions.intObject(selectionTime2)));
        this.selectionTime = selectionTime2;
    }

    public int getSelectionDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_25, this, this));
        return this.selectionDuration;
    }

    public void setSelectionDuration(int selectionDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_26, (Object) this, (Object) this, Conversions.intObject(selectionDuration2)));
        this.selectionDuration = selectionDuration2;
    }

    public int getCurrentTime() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_27, this, this));
        return this.currentTime;
    }

    public void setCurrentTime(int currentTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_28, (Object) this, (Object) this, Conversions.intObject(currentTime2)));
        this.currentTime = currentTime2;
    }
}
