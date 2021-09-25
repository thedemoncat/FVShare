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

public class TrackHeaderBox extends AbstractFullBox {
    private static Logger LOG = Logger.getLogger(TrackHeaderBox.class);
    public static final String TYPE = "tkhd";
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
    private static final JoinPoint.StaticPart ajc$tjp_29 = null;
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private int alternateGroup;
    private Date creationTime = new Date(0);
    private long duration;
    private double height;
    private int layer;
    private Matrix matrix = Matrix.ROTATE_0;
    private Date modificationTime = new Date(0);
    private long trackId;
    private float volume;
    private double width;

    private static void ajc$preClinit() {
        Factory factory = new Factory("TrackHeaderBox.java", TrackHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 62);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.util.Date"), 66);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getContent", "com.coremedia.iso.boxes.TrackHeaderBox", "java.nio.ByteBuffer", "byteBuffer", "", "void"), 145);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "java.lang.String"), (int) CompanyIdentifierResolver.PETER_SYSTEMTECHNIK_GMBH);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setCreationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "creationTime", "", "void"), (int) CompanyIdentifierResolver.QUUPPA_OY);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setModificationTime", "com.coremedia.iso.boxes.TrackHeaderBox", "java.util.Date", "modificationTime", "", "void"), (int) CompanyIdentifierResolver.ELGATO_SYSTEMS_GMBH);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "trackId", "", "void"), (int) CompanyIdentifierResolver.TIMEX_GROUP_USA_INC);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "long", "duration", "", "void"), (int) CompanyIdentifierResolver.TXTR_GMBH);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "layer", "", "void"), (int) CompanyIdentifierResolver.DANLERS_LTD);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "int", "alternateGroup", "", "void"), (int) CompanyIdentifierResolver.EDEN_SOFTWARE_CONSULTANTS_LTD);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "float", "volume", "", "void"), 233);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "com.googlecode.mp4parser.util.Matrix", "matrix", "", "void"), (int) CompanyIdentifierResolver.JOLLY_LOGIC_LLC);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTrackId", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 70);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "width", "", "void"), (int) CompanyIdentifierResolver.WITRON_TECHNOLOGY_LIMITED);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "double", "height", "", "void"), 245);
        ajc$tjp_22 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), 250);
        ajc$tjp_23 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.RESERVED);
        ajc$tjp_24 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.KEISER_CORPORATION);
        ajc$tjp_25 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.INNOVATIVE_YACHTTER_SOLUTIONS);
        ajc$tjp_26 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setEnabled", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "enabled", "", "void"), (int) CompanyIdentifierResolver.CODEGATE_LTD);
        ajc$tjp_27 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setInMovie", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inMovie", "", "void"), (int) CompanyIdentifierResolver.VYZYBL_INC);
        ajc$tjp_28 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setInPreview", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPreview", "", "void"), (int) CompanyIdentifierResolver.QUALCOMM_LABS_INC);
        ajc$tjp_29 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setInPoster", "com.coremedia.iso.boxes.TrackHeaderBox", "boolean", "inPoster", "", "void"), (int) CompanyIdentifierResolver.AIRTURN_INC);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDuration", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "long"), 74);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getLayer", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 78);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getAlternateGroup", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "int"), 82);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getVolume", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "float"), 86);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getMatrix", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "com.googlecode.mp4parser.util.Matrix"), 90);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getWidth", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 94);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getHeight", "com.coremedia.iso.boxes.TrackHeaderBox", "", "", "", "double"), 98);
    }

    static {
        ajc$preClinit();
    }

    public TrackHeaderBox() {
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

    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return this.trackId;
    }

    public long getDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return this.duration;
    }

    public int getLayer() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return this.layer;
    }

    public int getAlternateGroup() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.alternateGroup;
    }

    public float getVolume() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, this, this));
        return this.volume;
    }

    public Matrix getMatrix() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.matrix;
    }

    public double getWidth() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, this, this));
        return this.width;
    }

    public double getHeight() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.height;
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long contentSize;
        if (getVersion() == 1) {
            contentSize = 4 + 32;
        } else {
            contentSize = 4 + 20;
        }
        return contentSize + 60;
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        if (getVersion() == 1) {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt64(content));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt64(content));
            this.trackId = IsoTypeReader.readUInt32(content);
            IsoTypeReader.readUInt32(content);
            this.duration = content.getLong();
        } else {
            this.creationTime = DateHelper.convert(IsoTypeReader.readUInt32(content));
            this.modificationTime = DateHelper.convert(IsoTypeReader.readUInt32(content));
            this.trackId = IsoTypeReader.readUInt32(content);
            IsoTypeReader.readUInt32(content);
            this.duration = (long) content.getInt();
        }
        if (this.duration < -1) {
            LOG.logWarn("tkhd duration is not in expected range");
        }
        IsoTypeReader.readUInt32(content);
        IsoTypeReader.readUInt32(content);
        this.layer = IsoTypeReader.readUInt16(content);
        this.alternateGroup = IsoTypeReader.readUInt16(content);
        this.volume = IsoTypeReader.readFixedPoint88(content);
        IsoTypeReader.readUInt16(content);
        this.matrix = Matrix.fromByteBuffer(content);
        this.width = IsoTypeReader.readFixedPoint1616(content);
        this.height = IsoTypeReader.readFixedPoint1616(content);
    }

    public void getContent(ByteBuffer byteBuffer) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, (Object) byteBuffer));
        writeVersionAndFlags(byteBuffer);
        if (getVersion() == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt64(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0);
            byteBuffer.putLong(this.duration);
        } else {
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.creationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, DateHelper.convert(this.modificationTime));
            IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
            IsoTypeWriter.writeUInt32(byteBuffer, 0);
            byteBuffer.putInt((int) this.duration);
        }
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        IsoTypeWriter.writeUInt32(byteBuffer, 0);
        IsoTypeWriter.writeUInt16(byteBuffer, this.layer);
        IsoTypeWriter.writeUInt16(byteBuffer, this.alternateGroup);
        IsoTypeWriter.writeFixedPoint88(byteBuffer, (double) this.volume);
        IsoTypeWriter.writeUInt16(byteBuffer, 0);
        this.matrix.getContent(byteBuffer);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.width);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.height);
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        StringBuilder result = new StringBuilder();
        result.append("TrackHeaderBox[");
        result.append("creationTime=").append(getCreationTime());
        result.append(";");
        result.append("modificationTime=").append(getModificationTime());
        result.append(";");
        result.append("trackId=").append(getTrackId());
        result.append(";");
        result.append("duration=").append(getDuration());
        result.append(";");
        result.append("layer=").append(getLayer());
        result.append(";");
        result.append("alternateGroup=").append(getAlternateGroup());
        result.append(";");
        result.append("volume=").append(getVolume());
        result.append(";");
        result.append("matrix=").append(this.matrix);
        result.append(";");
        result.append("width=").append(getWidth());
        result.append(";");
        result.append("height=").append(getHeight());
        result.append("]");
        return result.toString();
    }

    public void setCreationTime(Date creationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, (Object) creationTime2));
        this.creationTime = creationTime2;
        if (DateHelper.convert(creationTime2) >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setVersion(1);
        }
    }

    public void setModificationTime(Date modificationTime2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, (Object) this, (Object) this, (Object) modificationTime2));
        this.modificationTime = modificationTime2;
        if (DateHelper.convert(modificationTime2) >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setVersion(1);
        }
    }

    public void setTrackId(long trackId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.longObject(trackId2)));
        this.trackId = trackId2;
    }

    public void setDuration(long duration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, (Object) this, (Object) this, Conversions.longObject(duration2)));
        this.duration = duration2;
        if (duration2 >= IjkMediaMeta.AV_CH_WIDE_RIGHT) {
            setFlags(1);
        }
    }

    public void setLayer(int layer2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, (Object) this, (Object) this, Conversions.intObject(layer2)));
        this.layer = layer2;
    }

    public void setAlternateGroup(int alternateGroup2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, (Object) this, (Object) this, Conversions.intObject(alternateGroup2)));
        this.alternateGroup = alternateGroup2;
    }

    public void setVolume(float volume2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, (Object) this, (Object) this, Conversions.floatObject(volume2)));
        this.volume = volume2;
    }

    public void setMatrix(Matrix matrix2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, (Object) this, (Object) this, (Object) matrix2));
        this.matrix = matrix2;
    }

    public void setWidth(double width2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, (Object) this, (Object) this, Conversions.doubleObject(width2)));
        this.width = width2;
    }

    public void setHeight(double height2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, (Object) this, (Object) this, Conversions.doubleObject(height2)));
        this.height = height2;
    }

    public boolean isEnabled() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_22, this, this));
        return (getFlags() & 1) > 0;
    }

    public boolean isInMovie() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_23, this, this));
        return (getFlags() & 2) > 0;
    }

    public boolean isInPreview() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_24, this, this));
        return (getFlags() & 4) > 0;
    }

    public boolean isInPoster() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_25, this, this));
        return (getFlags() & 8) > 0;
    }

    public void setEnabled(boolean enabled) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_26, (Object) this, (Object) this, Conversions.booleanObject(enabled)));
        if (enabled) {
            setFlags(getFlags() | 1);
        } else {
            setFlags(getFlags() & -2);
        }
    }

    public void setInMovie(boolean inMovie) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_27, (Object) this, (Object) this, Conversions.booleanObject(inMovie)));
        if (inMovie) {
            setFlags(getFlags() | 2);
        } else {
            setFlags(getFlags() & -3);
        }
    }

    public void setInPreview(boolean inPreview) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_28, (Object) this, (Object) this, Conversions.booleanObject(inPreview)));
        if (inPreview) {
            setFlags(getFlags() | 4);
        } else {
            setFlags(getFlags() & -5);
        }
    }

    public void setInPoster(boolean inPoster) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_29, (Object) this, (Object) this, Conversions.booleanObject(inPoster)));
        if (inPoster) {
            setFlags(getFlags() | 8);
        } else {
            setFlags(getFlags() & -9);
        }
    }
}
