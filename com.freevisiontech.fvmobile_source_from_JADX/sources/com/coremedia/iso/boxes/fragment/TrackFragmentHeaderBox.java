package com.coremedia.iso.boxes.fragment;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.common.BleConstant;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import java.nio.ByteBuffer;
import org.mp4parser.aspectj.lang.JoinPoint;
import org.mp4parser.aspectj.lang.Signature;
import org.mp4parser.aspectj.runtime.internal.Conversions;
import org.mp4parser.aspectj.runtime.reflect.Factory;

public class TrackFragmentHeaderBox extends AbstractFullBox {
    public static final String TYPE = "tfhd";
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
    private static final JoinPoint.StaticPart ajc$tjp_3 = null;
    private static final JoinPoint.StaticPart ajc$tjp_4 = null;
    private static final JoinPoint.StaticPart ajc$tjp_5 = null;
    private static final JoinPoint.StaticPart ajc$tjp_6 = null;
    private static final JoinPoint.StaticPart ajc$tjp_7 = null;
    private static final JoinPoint.StaticPart ajc$tjp_8 = null;
    private static final JoinPoint.StaticPart ajc$tjp_9 = null;
    private long baseDataOffset = -1;
    private boolean defaultBaseIsMoof;
    private long defaultSampleDuration = -1;
    private SampleFlags defaultSampleFlags;
    private long defaultSampleSize = -1;
    private boolean durationIsEmpty;
    private long sampleDescriptionIndex;
    private long trackId;

    static {
        ajc$preClinit();
    }

    private static void ajc$preClinit() {
        Factory factory = new Factory("TrackFragmentHeaderBox.java", TrackFragmentHeaderBox.class);
        ajc$tjp_0 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hasBaseDataOffset", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), 126);
        ajc$tjp_1 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hasSampleDescriptionIndex", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), 130);
        ajc$tjp_10 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setSampleDescriptionIndex", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "long", "sampleDescriptionIndex", "", "void"), (int) CompanyIdentifierResolver.INGENIEURSYSTEMGRUPPE_ZAHN_GMBH);
        ajc$tjp_11 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDefaultSampleDuration", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "long"), 180);
        ajc$tjp_12 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultSampleDuration", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "long", "defaultSampleDuration", "", "void"), (int) CompanyIdentifierResolver.QUALCOMM_INNOVATION_CENTER_INC_QUIC);
        ajc$tjp_13 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDefaultSampleSize", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "long"), (int) CompanyIdentifierResolver.STALMART_TECHNOLOGY_LIMITED);
        ajc$tjp_14 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultSampleSize", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "long", "defaultSampleSize", "", "void"), 195);
        ajc$tjp_15 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getDefaultSampleFlags", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "com.coremedia.iso.boxes.fragment.SampleFlags"), (int) CompanyIdentifierResolver.BEATS_ELECTRONICS);
        ajc$tjp_16 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultSampleFlags", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "com.coremedia.iso.boxes.fragment.SampleFlags", "defaultSampleFlags", "", "void"), (int) CompanyIdentifierResolver.DEXCOM_INC);
        ajc$tjp_17 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isDurationIsEmpty", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.VOYETRA_TURTLE_BEACH);
        ajc$tjp_18 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDurationIsEmpty", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "boolean", "durationIsEmpty", "", "void"), (int) CompanyIdentifierResolver.HOSIDEN_CORPORATION);
        ajc$tjp_19 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "isDefaultBaseIsMoof", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.FRESHTEMP);
        ajc$tjp_2 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hasDefaultSampleDuration", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), 134);
        ajc$tjp_20 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setDefaultBaseIsMoof", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "boolean", "defaultBaseIsMoof", "", "void"), (int) CompanyIdentifierResolver.NIELSENKELLERMAN_COMPANY);
        ajc$tjp_21 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "toString", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "java.lang.String"), 244);
        ajc$tjp_3 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hasDefaultSampleSize", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), 138);
        ajc$tjp_4 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "hasDefaultSampleFlags", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "boolean"), (int) CompanyIdentifierResolver.QUINTIC_CORP);
        ajc$tjp_5 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "long"), 146);
        ajc$tjp_6 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setTrackId", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "long", "trackId", "", "void"), 150);
        ajc$tjp_7 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getBaseDataOffset", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "long"), (int) CompanyIdentifierResolver.ALPWISE);
        ajc$tjp_8 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "setBaseDataOffset", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "long", "baseDataOffset", "", "void"), (int) CompanyIdentifierResolver.BOSE_CORPORATION);
        ajc$tjp_9 = factory.makeSJP(JoinPoint.METHOD_EXECUTION, (Signature) factory.makeMethodSig(BleConstant.SHUTTER, "getSampleDescriptionIndex", "com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox", "", "", "", "long"), (int) CompanyIdentifierResolver.VISTEON_CORPORATION);
    }

    public TrackFragmentHeaderBox() {
        super(TYPE);
    }

    /* access modifiers changed from: protected */
    public long getContentSize() {
        long size = 8;
        int flags = getFlags();
        if ((flags & 1) == 1) {
            size = 8 + 8;
        }
        if ((flags & 2) == 2) {
            size += 4;
        }
        if ((flags & 8) == 8) {
            size += 4;
        }
        if ((flags & 16) == 16) {
            size += 4;
        }
        if ((flags & 32) == 32) {
            return size + 4;
        }
        return size;
    }

    /* access modifiers changed from: protected */
    public void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, this.trackId);
        if ((getFlags() & 1) == 1) {
            IsoTypeWriter.writeUInt64(byteBuffer, getBaseDataOffset());
        }
        if ((getFlags() & 2) == 2) {
            IsoTypeWriter.writeUInt32(byteBuffer, getSampleDescriptionIndex());
        }
        if ((getFlags() & 8) == 8) {
            IsoTypeWriter.writeUInt32(byteBuffer, getDefaultSampleDuration());
        }
        if ((getFlags() & 16) == 16) {
            IsoTypeWriter.writeUInt32(byteBuffer, getDefaultSampleSize());
        }
        if ((getFlags() & 32) == 32) {
            this.defaultSampleFlags.getContent(byteBuffer);
        }
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        this.trackId = IsoTypeReader.readUInt32(content);
        if ((getFlags() & 1) == 1) {
            this.baseDataOffset = IsoTypeReader.readUInt64(content);
        }
        if ((getFlags() & 2) == 2) {
            this.sampleDescriptionIndex = IsoTypeReader.readUInt32(content);
        }
        if ((getFlags() & 8) == 8) {
            this.defaultSampleDuration = IsoTypeReader.readUInt32(content);
        }
        if ((getFlags() & 16) == 16) {
            this.defaultSampleSize = IsoTypeReader.readUInt32(content);
        }
        if ((getFlags() & 32) == 32) {
            this.defaultSampleFlags = new SampleFlags(content);
        }
        if ((getFlags() & 65536) == 65536) {
            this.durationIsEmpty = true;
        }
        if ((getFlags() & 131072) == 131072) {
            this.defaultBaseIsMoof = true;
        }
    }

    public boolean hasBaseDataOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return (getFlags() & 1) != 0;
    }

    public boolean hasSampleDescriptionIndex() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, this, this));
        return (getFlags() & 2) != 0;
    }

    public boolean hasDefaultSampleDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        return (getFlags() & 8) != 0;
    }

    public boolean hasDefaultSampleSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_3, this, this));
        return (getFlags() & 16) != 0;
    }

    public boolean hasDefaultSampleFlags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_4, this, this));
        return (getFlags() & 32) != 0;
    }

    public long getTrackId() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_5, this, this));
        return this.trackId;
    }

    public void setTrackId(long trackId2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_6, (Object) this, (Object) this, Conversions.longObject(trackId2)));
        this.trackId = trackId2;
    }

    public long getBaseDataOffset() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_7, this, this));
        return this.baseDataOffset;
    }

    public void setBaseDataOffset(long baseDataOffset2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_8, (Object) this, (Object) this, Conversions.longObject(baseDataOffset2)));
        if (baseDataOffset2 == -1) {
            setFlags(getFlags() & 2147483646);
        } else {
            setFlags(getFlags() | 1);
        }
        this.baseDataOffset = baseDataOffset2;
    }

    public long getSampleDescriptionIndex() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_9, this, this));
        return this.sampleDescriptionIndex;
    }

    public void setSampleDescriptionIndex(long sampleDescriptionIndex2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_10, (Object) this, (Object) this, Conversions.longObject(sampleDescriptionIndex2)));
        if (sampleDescriptionIndex2 == -1) {
            setFlags(getFlags() & 2147483645);
        } else {
            setFlags(getFlags() | 2);
        }
        this.sampleDescriptionIndex = sampleDescriptionIndex2;
    }

    public long getDefaultSampleDuration() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_11, this, this));
        return this.defaultSampleDuration;
    }

    public void setDefaultSampleDuration(long defaultSampleDuration2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_12, (Object) this, (Object) this, Conversions.longObject(defaultSampleDuration2)));
        setFlags(getFlags() | 8);
        this.defaultSampleDuration = defaultSampleDuration2;
    }

    public long getDefaultSampleSize() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_13, this, this));
        return this.defaultSampleSize;
    }

    public void setDefaultSampleSize(long defaultSampleSize2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_14, (Object) this, (Object) this, Conversions.longObject(defaultSampleSize2)));
        if (defaultSampleSize2 != -1) {
            setFlags(getFlags() | 16);
        } else {
            setFlags(getFlags() & 16777199);
        }
        this.defaultSampleSize = defaultSampleSize2;
    }

    public SampleFlags getDefaultSampleFlags() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_15, this, this));
        return this.defaultSampleFlags;
    }

    public void setDefaultSampleFlags(SampleFlags defaultSampleFlags2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_16, (Object) this, (Object) this, (Object) defaultSampleFlags2));
        if (defaultSampleFlags2 != null) {
            setFlags(getFlags() | 32);
        } else {
            setFlags(getFlags() & 16777183);
        }
        this.defaultSampleFlags = defaultSampleFlags2;
    }

    public boolean isDurationIsEmpty() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_17, this, this));
        return this.durationIsEmpty;
    }

    public void setDurationIsEmpty(boolean durationIsEmpty2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_18, (Object) this, (Object) this, Conversions.booleanObject(durationIsEmpty2)));
        if (this.defaultBaseIsMoof) {
            setFlags(getFlags() | 65536);
        } else {
            setFlags(getFlags() & 16711679);
        }
        this.durationIsEmpty = durationIsEmpty2;
    }

    public boolean isDefaultBaseIsMoof() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_19, this, this));
        return this.defaultBaseIsMoof;
    }

    public void setDefaultBaseIsMoof(boolean defaultBaseIsMoof2) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_20, (Object) this, (Object) this, Conversions.booleanObject(defaultBaseIsMoof2)));
        if (defaultBaseIsMoof2) {
            setFlags(getFlags() | 131072);
        } else {
            setFlags(getFlags() & 16646143);
        }
        this.defaultBaseIsMoof = defaultBaseIsMoof2;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_21, this, this));
        StringBuilder sb = new StringBuilder();
        sb.append("TrackFragmentHeaderBox");
        sb.append("{trackId=").append(this.trackId);
        sb.append(", baseDataOffset=").append(this.baseDataOffset);
        sb.append(", sampleDescriptionIndex=").append(this.sampleDescriptionIndex);
        sb.append(", defaultSampleDuration=").append(this.defaultSampleDuration);
        sb.append(", defaultSampleSize=").append(this.defaultSampleSize);
        sb.append(", defaultSampleFlags=").append(this.defaultSampleFlags);
        sb.append(", durationIsEmpty=").append(this.durationIsEmpty);
        sb.append(", defaultBaseIsMoof=").append(this.defaultBaseIsMoof);
        sb.append('}');
        return sb.toString();
    }
}
