package com.google.android.exoplayer.util;

import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.google.android.exoplayer.MediaFormat;
import com.vise.log.common.LogConstant;
import java.nio.ByteBuffer;
import java.util.List;
import org.opencv.p011ml.DTrees;

public final class DtsUtil {
    private static final int[] CHANNELS_BY_AMODE = {1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};
    private static final int[] SAMPLE_RATE_BY_SFREQ = {-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, Camera2Constants.AudioSampleRate, -1, -1};
    private static final ParsableBitArray SCRATCH_BITS = new ParsableBitArray();
    private static final int[] TWICE_BITRATE_KBPS_BY_RATE = {64, 112, 128, 192, 224, 256, 384, 448, 512, 640, DTrees.PREDICT_MASK, 896, 1024, 1152, 1280, 1536, 1920, 2048, 2304, 2560, 2688, 2816, 2823, 2944, LogConstant.LINE_MAX, 3840, 4096, 6144, 7680};

    public static MediaFormat parseDtsFormat(byte[] frame, String trackId, long durationUs, String language) {
        ParsableBitArray frameBits = SCRATCH_BITS;
        frameBits.reset(frame);
        frameBits.skipBits(60);
        int channelCount = CHANNELS_BY_AMODE[frameBits.readBits(6)];
        int sampleRate = SAMPLE_RATE_BY_SFREQ[frameBits.readBits(4)];
        int rate = frameBits.readBits(5);
        int bitrate = rate >= TWICE_BITRATE_KBPS_BY_RATE.length ? -1 : (TWICE_BITRATE_KBPS_BY_RATE[rate] * 1000) / 2;
        frameBits.skipBits(10);
        return MediaFormat.createAudioFormat(trackId, MimeTypes.AUDIO_DTS, bitrate, -1, durationUs, channelCount + (frameBits.readBits(2) > 0 ? 1 : 0), sampleRate, (List<byte[]>) null, language);
    }

    public static int parseDtsAudioSampleCount(byte[] data) {
        return ((((data[4] & 1) << 6) | ((data[5] & 252) >> 2)) + 1) * 32;
    }

    public static int parseDtsAudioSampleCount(ByteBuffer data) {
        int position = data.position();
        return ((((data.get(position + 4) & 1) << 6) | ((data.get(position + 5) & 252) >> 2)) + 1) * 32;
    }

    public static int getDtsFrameSize(byte[] data) {
        return (((data[5] & 2) << 12) | ((data[6] & 255) << 4) | ((data[7] & 240) >> 4)) + 1;
    }

    private DtsUtil() {
    }
}
