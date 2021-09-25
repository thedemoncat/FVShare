package com.google.android.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import p012tv.danmaku.ijk.media.player.misc.IMediaFormat;

public final class MediaFormat implements Parcelable {
    public static final Parcelable.Creator<MediaFormat> CREATOR = new Parcelable.Creator<MediaFormat>() {
        public MediaFormat createFromParcel(Parcel in) {
            return new MediaFormat(in);
        }

        public MediaFormat[] newArray(int size) {
            return new MediaFormat[size];
        }
    };
    public static final int NO_VALUE = -1;
    public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;
    public final boolean adaptive;
    public final int bitrate;
    public final int channelCount;
    public final long durationUs;
    public final int encoderDelay;
    public final int encoderPadding;
    private android.media.MediaFormat frameworkMediaFormat;
    private int hashCode;
    public final int height;
    public final List<byte[]> initializationData;
    public final String language;
    public final int maxHeight;
    public final int maxInputSize;
    public final int maxWidth;
    public final String mimeType;
    public final int pcmEncoding;
    public final float pixelWidthHeightRatio;
    public final byte[] projectionData;
    public final int rotationDegrees;
    public final int sampleRate;
    public final int stereoMode;
    public final long subsampleOffsetUs;
    public final String trackId;
    public final int width;

    public static MediaFormat createVideoFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int width2, int height2, List<byte[]> initializationData2) {
        return createVideoFormat(trackId2, mimeType2, bitrate2, maxInputSize2, durationUs2, width2, height2, initializationData2, -1, -1.0f, (byte[]) null, -1);
    }

    public static MediaFormat createVideoFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int width2, int height2, List<byte[]> initializationData2, int rotationDegrees2, float pixelWidthHeightRatio2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, maxInputSize2, durationUs2, width2, height2, rotationDegrees2, pixelWidthHeightRatio2, -1, -1, (String) null, Long.MAX_VALUE, initializationData2, false, -1, -1, -1, -1, -1, (byte[]) null, -1);
    }

    public static MediaFormat createVideoFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int width2, int height2, List<byte[]> initializationData2, int rotationDegrees2, float pixelWidthHeightRatio2, byte[] projectionData2, int stereoMode2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, maxInputSize2, durationUs2, width2, height2, rotationDegrees2, pixelWidthHeightRatio2, -1, -1, (String) null, Long.MAX_VALUE, initializationData2, false, -1, -1, -1, -1, -1, projectionData2, stereoMode2);
    }

    public static MediaFormat createAudioFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int channelCount2, int sampleRate2, List<byte[]> initializationData2, String language2) {
        return createAudioFormat(trackId2, mimeType2, bitrate2, maxInputSize2, durationUs2, channelCount2, sampleRate2, initializationData2, language2, -1);
    }

    public static MediaFormat createAudioFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int channelCount2, int sampleRate2, List<byte[]> initializationData2, String language2, int pcmEncoding2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, maxInputSize2, durationUs2, -1, -1, -1, -1.0f, channelCount2, sampleRate2, language2, Long.MAX_VALUE, initializationData2, false, -1, -1, pcmEncoding2, -1, -1, (byte[]) null, -1);
    }

    public static MediaFormat createTextFormat(String trackId2, String mimeType2, int bitrate2, long durationUs2, String language2) {
        return createTextFormat(trackId2, mimeType2, bitrate2, durationUs2, language2, Long.MAX_VALUE);
    }

    public static MediaFormat createTextFormat(String trackId2, String mimeType2, int bitrate2, long durationUs2, String language2, long subsampleOffsetUs2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, -1, durationUs2, -1, -1, -1, -1.0f, -1, -1, language2, subsampleOffsetUs2, (List<byte[]>) null, false, -1, -1, -1, -1, -1, (byte[]) null, -1);
    }

    public static MediaFormat createImageFormat(String trackId2, String mimeType2, int bitrate2, long durationUs2, List<byte[]> initializationData2, String language2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, -1, durationUs2, -1, -1, -1, -1.0f, -1, -1, language2, Long.MAX_VALUE, initializationData2, false, -1, -1, -1, -1, -1, (byte[]) null, -1);
    }

    public static MediaFormat createFormatForMimeType(String trackId2, String mimeType2, int bitrate2, long durationUs2) {
        return new MediaFormat(trackId2, mimeType2, bitrate2, -1, durationUs2, -1, -1, -1, -1.0f, -1, -1, (String) null, Long.MAX_VALUE, (List<byte[]>) null, false, -1, -1, -1, -1, -1, (byte[]) null, -1);
    }

    public static MediaFormat createId3Format() {
        return createFormatForMimeType((String) null, MimeTypes.APPLICATION_ID3, -1, -1);
    }

    MediaFormat(Parcel in) {
        boolean z;
        boolean hasProjectionData;
        byte[] bArr;
        this.trackId = in.readString();
        this.mimeType = in.readString();
        this.bitrate = in.readInt();
        this.maxInputSize = in.readInt();
        this.durationUs = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.rotationDegrees = in.readInt();
        this.pixelWidthHeightRatio = in.readFloat();
        this.channelCount = in.readInt();
        this.sampleRate = in.readInt();
        this.language = in.readString();
        this.subsampleOffsetUs = in.readLong();
        this.initializationData = new ArrayList();
        in.readList(this.initializationData, (ClassLoader) null);
        if (in.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.adaptive = z;
        this.maxWidth = in.readInt();
        this.maxHeight = in.readInt();
        this.pcmEncoding = in.readInt();
        this.encoderDelay = in.readInt();
        this.encoderPadding = in.readInt();
        if (in.readInt() != 0) {
            hasProjectionData = true;
        } else {
            hasProjectionData = false;
        }
        if (hasProjectionData) {
            bArr = in.createByteArray();
        } else {
            bArr = null;
        }
        this.projectionData = bArr;
        this.stereoMode = in.readInt();
    }

    MediaFormat(String trackId2, String mimeType2, int bitrate2, int maxInputSize2, long durationUs2, int width2, int height2, int rotationDegrees2, float pixelWidthHeightRatio2, int channelCount2, int sampleRate2, String language2, long subsampleOffsetUs2, List<byte[]> initializationData2, boolean adaptive2, int maxWidth2, int maxHeight2, int pcmEncoding2, int encoderDelay2, int encoderPadding2, byte[] projectionData2, int stereoMode2) {
        this.trackId = trackId2;
        this.mimeType = Assertions.checkNotEmpty(mimeType2);
        this.bitrate = bitrate2;
        this.maxInputSize = maxInputSize2;
        this.durationUs = durationUs2;
        this.width = width2;
        this.height = height2;
        this.rotationDegrees = rotationDegrees2;
        this.pixelWidthHeightRatio = pixelWidthHeightRatio2;
        this.channelCount = channelCount2;
        this.sampleRate = sampleRate2;
        this.language = language2;
        this.subsampleOffsetUs = subsampleOffsetUs2;
        this.initializationData = initializationData2 == null ? Collections.emptyList() : initializationData2;
        this.adaptive = adaptive2;
        this.maxWidth = maxWidth2;
        this.maxHeight = maxHeight2;
        this.pcmEncoding = pcmEncoding2;
        this.encoderDelay = encoderDelay2;
        this.encoderPadding = encoderPadding2;
        this.projectionData = projectionData2;
        this.stereoMode = stereoMode2;
    }

    public MediaFormat copyWithMaxInputSize(int maxInputSize2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, maxInputSize2, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyWithMaxVideoDimensions(int maxWidth2, int maxHeight2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, maxWidth2, maxHeight2, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyWithSubsampleOffsetUs(long subsampleOffsetUs2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, subsampleOffsetUs2, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyWithDurationUs(long durationUs2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, durationUs2, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyWithLanguage(String language2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, language2, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyWithFixedTrackInfo(String trackId2, int bitrate2, int width2, int height2, String language2) {
        return new MediaFormat(trackId2, this.mimeType, bitrate2, this.maxInputSize, this.durationUs, width2, height2, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, language2, this.subsampleOffsetUs, this.initializationData, this.adaptive, -1, -1, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.projectionData, this.stereoMode);
    }

    public MediaFormat copyAsAdaptive(String trackId2) {
        return new MediaFormat(trackId2, this.mimeType, -1, -1, this.durationUs, -1, -1, -1, -1.0f, -1, -1, (String) null, Long.MAX_VALUE, (List<byte[]>) null, true, this.maxWidth, this.maxHeight, -1, -1, -1, (byte[]) null, this.stereoMode);
    }

    public MediaFormat copyWithGaplessInfo(int encoderDelay2, int encoderPadding2) {
        return new MediaFormat(this.trackId, this.mimeType, this.bitrate, this.maxInputSize, this.durationUs, this.width, this.height, this.rotationDegrees, this.pixelWidthHeightRatio, this.channelCount, this.sampleRate, this.language, this.subsampleOffsetUs, this.initializationData, this.adaptive, this.maxWidth, this.maxHeight, this.pcmEncoding, encoderDelay2, encoderPadding2, this.projectionData, this.stereoMode);
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(16)
    public final android.media.MediaFormat getFrameworkMediaFormatV16() {
        if (this.frameworkMediaFormat == null) {
            android.media.MediaFormat format = new android.media.MediaFormat();
            format.setString(IMediaFormat.KEY_MIME, this.mimeType);
            maybeSetStringV16(format, "language", this.language);
            maybeSetIntegerV16(format, "max-input-size", this.maxInputSize);
            maybeSetIntegerV16(format, "width", this.width);
            maybeSetIntegerV16(format, "height", this.height);
            maybeSetIntegerV16(format, "rotation-degrees", this.rotationDegrees);
            maybeSetIntegerV16(format, "max-width", this.maxWidth);
            maybeSetIntegerV16(format, "max-height", this.maxHeight);
            maybeSetIntegerV16(format, "channel-count", this.channelCount);
            maybeSetIntegerV16(format, "sample-rate", this.sampleRate);
            maybeSetIntegerV16(format, "encoder-delay", this.encoderDelay);
            maybeSetIntegerV16(format, "encoder-padding", this.encoderPadding);
            for (int i = 0; i < this.initializationData.size(); i++) {
                format.setByteBuffer("csd-" + i, ByteBuffer.wrap(this.initializationData.get(i)));
            }
            if (this.durationUs != -1) {
                format.setLong("durationUs", this.durationUs);
            }
            this.frameworkMediaFormat = format;
        }
        return this.frameworkMediaFormat;
    }

    /* access modifiers changed from: package-private */
    @TargetApi(16)
    @Deprecated
    public final void setFrameworkFormatV16(android.media.MediaFormat format) {
        this.frameworkMediaFormat = format;
    }

    public String toString() {
        return "MediaFormat(" + this.trackId + ", " + this.mimeType + ", " + this.bitrate + ", " + this.maxInputSize + ", " + this.width + ", " + this.height + ", " + this.rotationDegrees + ", " + this.pixelWidthHeightRatio + ", " + this.channelCount + ", " + this.sampleRate + ", " + this.language + ", " + this.durationUs + ", " + this.adaptive + ", " + this.maxWidth + ", " + this.maxHeight + ", " + this.pcmEncoding + ", " + this.encoderDelay + ", " + this.encoderPadding + ")";
    }

    public int hashCode() {
        int i = 0;
        if (this.hashCode == 0) {
            int hashCode2 = ((((((((((((((((((((((((((((((((((this.trackId == null ? 0 : this.trackId.hashCode()) + 527) * 31) + (this.mimeType == null ? 0 : this.mimeType.hashCode())) * 31) + this.bitrate) * 31) + this.maxInputSize) * 31) + this.width) * 31) + this.height) * 31) + this.rotationDegrees) * 31) + Float.floatToRawIntBits(this.pixelWidthHeightRatio)) * 31) + ((int) this.durationUs)) * 31) + (this.adaptive ? 1231 : 1237)) * 31) + this.maxWidth) * 31) + this.maxHeight) * 31) + this.channelCount) * 31) + this.sampleRate) * 31) + this.pcmEncoding) * 31) + this.encoderDelay) * 31) + this.encoderPadding) * 31;
            if (this.language != null) {
                i = this.language.hashCode();
            }
            int result = ((hashCode2 + i) * 31) + ((int) this.subsampleOffsetUs);
            for (int i2 = 0; i2 < this.initializationData.size(); i2++) {
                result = (result * 31) + Arrays.hashCode(this.initializationData.get(i2));
            }
            this.hashCode = (((result * 31) + Arrays.hashCode(this.projectionData)) * 31) + this.stereoMode;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MediaFormat other = (MediaFormat) obj;
        if (this.adaptive != other.adaptive || this.bitrate != other.bitrate || this.maxInputSize != other.maxInputSize || this.durationUs != other.durationUs || this.width != other.width || this.height != other.height || this.rotationDegrees != other.rotationDegrees || this.pixelWidthHeightRatio != other.pixelWidthHeightRatio || this.maxWidth != other.maxWidth || this.maxHeight != other.maxHeight || this.channelCount != other.channelCount || this.sampleRate != other.sampleRate || this.pcmEncoding != other.pcmEncoding || this.encoderDelay != other.encoderDelay || this.encoderPadding != other.encoderPadding || this.subsampleOffsetUs != other.subsampleOffsetUs || !Util.areEqual(this.trackId, other.trackId) || !Util.areEqual(this.language, other.language) || !Util.areEqual(this.mimeType, other.mimeType) || this.initializationData.size() != other.initializationData.size() || !Arrays.equals(this.projectionData, other.projectionData) || this.stereoMode != other.stereoMode) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); i++) {
            if (!Arrays.equals(this.initializationData.get(i), other.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(16)
    private static final void maybeSetStringV16(android.media.MediaFormat format, String key, String value) {
        if (value != null) {
            format.setString(key, value);
        }
    }

    @TargetApi(16)
    private static final void maybeSetIntegerV16(android.media.MediaFormat format, String key, int value) {
        if (value != -1) {
            format.setInteger(key, value);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeString(this.trackId);
        dest.writeString(this.mimeType);
        dest.writeInt(this.bitrate);
        dest.writeInt(this.maxInputSize);
        dest.writeLong(this.durationUs);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.rotationDegrees);
        dest.writeFloat(this.pixelWidthHeightRatio);
        dest.writeInt(this.channelCount);
        dest.writeInt(this.sampleRate);
        dest.writeString(this.language);
        dest.writeLong(this.subsampleOffsetUs);
        dest.writeList(this.initializationData);
        dest.writeInt(this.adaptive ? 1 : 0);
        dest.writeInt(this.maxWidth);
        dest.writeInt(this.maxHeight);
        dest.writeInt(this.pcmEncoding);
        dest.writeInt(this.encoderDelay);
        dest.writeInt(this.encoderPadding);
        if (this.projectionData == null) {
            i = 0;
        }
        dest.writeInt(i);
        if (this.projectionData != null) {
            dest.writeByteArray(this.projectionData);
        }
        dest.writeInt(this.stereoMode);
    }
}
