package com.google.android.exoplayer.util;

import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.vending.expansion.downloader.impl.DownloaderService;
import java.nio.ByteBuffer;
import java.util.List;

public final class Ac3Util {
    private static final int AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT = 1536;
    private static final int AUDIO_SAMPLES_PER_AUDIO_BLOCK = 256;
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = {32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 384, 448, 512, 576, 640};
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = {1, 2, 3, 6};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = {2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] SAMPLE_RATE_BY_FSCOD = {Camera2Constants.AudioSampleRate, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = {24000, 22050, 16000};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = {69, 87, 104, 121, 139, CompanyIdentifierResolver.OMEGAWAVE_OY, CompanyIdentifierResolver.DEXCOM_INC, CompanyIdentifierResolver.KENT_DISPLAYS_INC, CompanyIdentifierResolver.ONE_OAK_TECHNOLOGIES, 348, 417, DownloaderService.STATUS_FILE_DELIVERED_INCORRECTLY, 557, 696, 835, 975, 1114, 1253, 1393};

    public static MediaFormat parseAc3AnnexFFormat(ParsableByteArray data, String trackId, long durationUs, String language) {
        int sampleRate = SAMPLE_RATE_BY_FSCOD[(data.readUnsignedByte() & 192) >> 6];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 56) >> 3];
        if ((nextByte & 4) != 0) {
            channelCount++;
        }
        return MediaFormat.createAudioFormat(trackId, MimeTypes.AUDIO_AC3, -1, -1, durationUs, channelCount, sampleRate, (List<byte[]>) null, language);
    }

    public static MediaFormat parseEAc3AnnexFFormat(ParsableByteArray data, String trackId, long durationUs, String language) {
        data.skipBytes(2);
        int sampleRate = SAMPLE_RATE_BY_FSCOD[(data.readUnsignedByte() & 192) >> 6];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 14) >> 1];
        if ((nextByte & 1) != 0) {
            channelCount++;
        }
        return MediaFormat.createAudioFormat(trackId, MimeTypes.AUDIO_E_AC3, -1, -1, durationUs, channelCount, sampleRate, (List<byte[]>) null, language);
    }

    public static MediaFormat parseAc3SyncframeFormat(ParsableBitArray data, String trackId, long durationUs, String language) {
        data.skipBits(32);
        int fscod = data.readBits(2);
        data.skipBits(14);
        int acmod = data.readBits(3);
        if (!((acmod & 1) == 0 || acmod == 1)) {
            data.skipBits(2);
        }
        if ((acmod & 4) != 0) {
            data.skipBits(2);
        }
        if (acmod == 2) {
            data.skipBits(2);
        }
        return MediaFormat.createAudioFormat(trackId, MimeTypes.AUDIO_AC3, -1, -1, durationUs, CHANNEL_COUNT_BY_ACMOD[acmod] + (data.readBit() ? 1 : 0), SAMPLE_RATE_BY_FSCOD[fscod], (List<byte[]>) null, language);
    }

    public static MediaFormat parseEac3SyncframeFormat(ParsableBitArray data, String trackId, long durationUs, String language) {
        int sampleRate;
        int i;
        data.skipBits(32);
        int fscod = data.readBits(2);
        if (fscod == 3) {
            sampleRate = SAMPLE_RATE_BY_FSCOD2[data.readBits(2)];
        } else {
            data.skipBits(2);
            sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        }
        int acmod = data.readBits(3);
        boolean lfeon = data.readBit();
        int i2 = CHANNEL_COUNT_BY_ACMOD[acmod];
        if (lfeon) {
            i = 1;
        } else {
            i = 0;
        }
        return MediaFormat.createAudioFormat(trackId, MimeTypes.AUDIO_E_AC3, -1, -1, durationUs, i2 + i, sampleRate, (List<byte[]>) null, language);
    }

    public static int parseAc3SyncframeSize(byte[] data) {
        return getAc3SyncframeSize((data[4] & 192) >> 6, data[4] & 63);
    }

    public static int parseEAc3SyncframeSize(byte[] data) {
        return (((data[2] & 7) << 8) + (data[3] & 255) + 1) * 2;
    }

    public static int getAc3SyncframeAudioSampleCount() {
        return AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
    }

    public static int parseEAc3SyncframeAudioSampleCount(byte[] data) {
        return (((data[4] & 192) >> 6) == 3 ? 6 : BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(data[4] & 48) >> 4]) * 256;
    }

    public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer buffer) {
        int i;
        if (((buffer.get(buffer.position() + 4) & 192) >> 6) == 3) {
            i = 6;
        } else {
            i = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(buffer.get(buffer.position() + 4) & 48) >> 4];
        }
        return i * 256;
    }

    private static int getAc3SyncframeSize(int fscod, int frmsizecod) {
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        if (sampleRate == 44100) {
            return (SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[frmsizecod / 2] + (frmsizecod % 2)) * 2;
        }
        int bitrate = BITRATE_BY_HALF_FRMSIZECOD[frmsizecod / 2];
        if (sampleRate == 32000) {
            return bitrate * 6;
        }
        return bitrate * 4;
    }

    private Ac3Util() {
    }
}
