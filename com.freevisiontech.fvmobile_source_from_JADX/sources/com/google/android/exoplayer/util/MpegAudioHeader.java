package com.google.android.exoplayer.util;

import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;

public final class MpegAudioHeader {
    private static final int[] BITRATE_V1_L1 = {32, 64, 96, 128, 160, 192, 224, 256, CompanyIdentifierResolver.PORSCHE_AG, 320, 352, 384, 416, 448};
    private static final int[] BITRATE_V1_L2 = {32, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384};
    private static final int[] BITRATE_V1_L3 = {32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320};
    private static final int[] BITRATE_V2 = {8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160};
    private static final int[] BITRATE_V2_L1 = {32, 48, 56, 64, 80, 96, 112, 128, 144, 160, CompanyIdentifierResolver.PASSIF_SEMICONDUCTOR_CORP, 192, 224, 256};
    public static final int MAX_FRAME_SIZE_BYTES = 4096;
    private static final String[] MIME_TYPE_BY_LAYER = {MimeTypes.AUDIO_MPEG_L1, MimeTypes.AUDIO_MPEG_L2, MimeTypes.AUDIO_MPEG};
    private static final int[] SAMPLING_RATE_V1 = {44100, Camera2Constants.AudioSampleRate, 32000};
    public int bitrate;
    public int channels;
    public int frameSize;
    public String mimeType;
    public int sampleRate;
    public int samplesPerFrame;
    public int version;

    public static int getFrameSize(int header) {
        int version2;
        int layer;
        int bitrateIndex;
        int samplingRateIndex;
        int bitrate2;
        if ((header & -2097152) != -2097152 || (version2 = (header >>> 19) & 3) == 1 || (layer = (header >>> 17) & 3) == 0 || (bitrateIndex = (header >>> 12) & 15) == 0 || bitrateIndex == 15 || (samplingRateIndex = (header >>> 10) & 3) == 3) {
            return -1;
        }
        int samplingRate = SAMPLING_RATE_V1[samplingRateIndex];
        if (version2 == 2) {
            samplingRate /= 2;
        } else if (version2 == 0) {
            samplingRate /= 4;
        }
        int padding = (header >>> 9) & 1;
        if (layer == 3) {
            return ((((version2 == 3 ? BITRATE_V1_L1[bitrateIndex - 1] : BITRATE_V2_L1[bitrateIndex - 1]) * 12000) / samplingRate) + padding) * 4;
        }
        if (version2 == 3) {
            bitrate2 = layer == 2 ? BITRATE_V1_L2[bitrateIndex - 1] : BITRATE_V1_L3[bitrateIndex - 1];
        } else {
            bitrate2 = BITRATE_V2[bitrateIndex - 1];
        }
        if (version2 == 3) {
            return ((144000 * bitrate2) / samplingRate) + padding;
        }
        return (((layer == 1 ? 72000 : 144000) * bitrate2) / samplingRate) + padding;
    }

    public static boolean populateHeader(int headerData, MpegAudioHeader header) {
        int version2;
        int layer;
        int bitrateIndex;
        int samplingRateIndex;
        int bitrate2;
        int samplesPerFrame2;
        int frameSize2;
        if ((-2097152 & headerData) != -2097152 || (version2 = (headerData >>> 19) & 3) == 1 || (layer = (headerData >>> 17) & 3) == 0 || (bitrateIndex = (headerData >>> 12) & 15) == 0 || bitrateIndex == 15 || (samplingRateIndex = (headerData >>> 10) & 3) == 3) {
            return false;
        }
        int sampleRate2 = SAMPLING_RATE_V1[samplingRateIndex];
        if (version2 == 2) {
            sampleRate2 /= 2;
        } else if (version2 == 0) {
            sampleRate2 /= 4;
        }
        int padding = (headerData >>> 9) & 1;
        if (layer == 3) {
            bitrate2 = version2 == 3 ? BITRATE_V1_L1[bitrateIndex - 1] : BITRATE_V2_L1[bitrateIndex - 1];
            frameSize2 = (((bitrate2 * 12000) / sampleRate2) + padding) * 4;
            samplesPerFrame2 = 384;
        } else if (version2 == 3) {
            bitrate2 = layer == 2 ? BITRATE_V1_L2[bitrateIndex - 1] : BITRATE_V1_L3[bitrateIndex - 1];
            samplesPerFrame2 = 1152;
            frameSize2 = ((144000 * bitrate2) / sampleRate2) + padding;
        } else {
            bitrate2 = BITRATE_V2[bitrateIndex - 1];
            samplesPerFrame2 = layer == 1 ? 576 : 1152;
            frameSize2 = (((layer == 1 ? 72000 : 144000) * bitrate2) / sampleRate2) + padding;
        }
        header.setValues(version2, MIME_TYPE_BY_LAYER[3 - layer], frameSize2, sampleRate2, ((headerData >> 6) & 3) == 3 ? 1 : 2, bitrate2 * 1000, samplesPerFrame2);
        return true;
    }

    private void setValues(int version2, String mimeType2, int frameSize2, int sampleRate2, int channels2, int bitrate2, int samplesPerFrame2) {
        this.version = version2;
        this.mimeType = mimeType2;
        this.frameSize = frameSize2;
        this.sampleRate = sampleRate2;
        this.channels = channels2;
        this.bitrate = bitrate2;
        this.samplesPerFrame = samplesPerFrame2;
    }
}
