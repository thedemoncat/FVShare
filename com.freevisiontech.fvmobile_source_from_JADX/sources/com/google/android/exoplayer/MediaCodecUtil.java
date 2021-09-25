package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(16)
public final class MediaCodecUtil {
    private static final DecoderInfo PASSTHROUGH_DECODER_INFO = new DecoderInfo("OMX.google.raw.decoder", (MediaCodecInfo.CodecCapabilities) null);
    private static final String TAG = "MediaCodecUtil";
    private static final Map<CodecKey, List<DecoderInfo>> decoderInfosCache = new HashMap();
    private static int maxH264DecodableFrameSize = -1;

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, MediaCodecInfo.CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    public static class DecoderQueryException extends IOException {
        private DecoderQueryException(Throwable cause) {
            super("Failed to query underlying media codecs", cause);
        }
    }

    private MediaCodecUtil() {
    }

    public static void warmCodec(String mimeType, boolean secure) {
        try {
            getDecoderInfos(mimeType, secure);
        } catch (DecoderQueryException e) {
            Log.e(TAG, "Codec warming failed", e);
        }
    }

    public static DecoderInfo getPassthroughDecoderInfo() {
        return PASSTHROUGH_DECODER_INFO;
    }

    public static DecoderInfo getDecoderInfo(String mimeType, boolean secure) throws DecoderQueryException {
        List<DecoderInfo> decoderInfos = getDecoderInfos(mimeType, secure);
        if (decoderInfos.isEmpty()) {
            return null;
        }
        return decoderInfos.get(0);
    }

    public static synchronized List<DecoderInfo> getDecoderInfos(String mimeType, boolean secure) throws DecoderQueryException {
        MediaCodecListCompat mediaCodecList;
        List<DecoderInfo> decoderInfos;
        synchronized (MediaCodecUtil.class) {
            CodecKey key = new CodecKey(mimeType, secure);
            List<DecoderInfo> decoderInfos2 = decoderInfosCache.get(key);
            if (decoderInfos2 != null) {
                decoderInfos = decoderInfos2;
            } else {
                if (Util.SDK_INT >= 21) {
                    mediaCodecList = new MediaCodecListCompatV21(secure);
                } else {
                    mediaCodecList = new MediaCodecListCompatV16();
                }
                List<DecoderInfo> decoderInfos3 = getDecoderInfosInternal(key, mediaCodecList);
                if (secure && decoderInfos3.isEmpty() && 21 <= Util.SDK_INT && Util.SDK_INT <= 23) {
                    decoderInfos3 = getDecoderInfosInternal(key, new MediaCodecListCompatV16());
                    if (!decoderInfos3.isEmpty()) {
                        Log.w(TAG, "MediaCodecList API didn't list secure decoder for: " + mimeType + ". Assuming: " + decoderInfos3.get(0).name);
                    }
                }
                List<DecoderInfo> decoderInfos4 = Collections.unmodifiableList(decoderInfos3);
                decoderInfosCache.put(key, decoderInfos4);
                decoderInfos = decoderInfos4;
            }
        }
        return decoderInfos;
    }

    private static List<DecoderInfo> getDecoderInfosInternal(CodecKey key, MediaCodecListCompat mediaCodecList) throws DecoderQueryException {
        List<DecoderInfo> decoderInfos;
        String codecName;
        String supportedType;
        try {
            decoderInfos = new ArrayList<>();
            String mimeType = key.mimeType;
            int numberOfCodecs = mediaCodecList.getCodecCount();
            boolean secureDecodersExplicit = mediaCodecList.secureDecodersExplicit();
            int i = 0;
            loop0:
            while (true) {
                if (i >= numberOfCodecs) {
                    break;
                }
                MediaCodecInfo codecInfo = mediaCodecList.getCodecInfoAt(i);
                codecName = codecInfo.getName();
                if (isCodecUsableDecoder(codecInfo, codecName, secureDecodersExplicit)) {
                    String[] supportedTypes = codecInfo.getSupportedTypes();
                    int length = supportedTypes.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        supportedType = supportedTypes[i2];
                        if (supportedType.equalsIgnoreCase(mimeType)) {
                            MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(supportedType);
                            boolean secure = mediaCodecList.isSecurePlaybackSupported(mimeType, capabilities);
                            if ((!secureDecodersExplicit || key.secure != secure) && (secureDecodersExplicit || key.secure)) {
                                if (!secureDecodersExplicit && secure) {
                                    decoderInfos.add(new DecoderInfo(codecName + ".secure", capabilities));
                                    break loop0;
                                }
                            } else {
                                decoderInfos.add(new DecoderInfo(codecName, capabilities));
                            }
                        }
                    }
                    continue;
                }
                i++;
            }
            return decoderInfos;
        } catch (Exception e) {
            if (Util.SDK_INT > 23 || decoderInfos.isEmpty()) {
                Log.e(TAG, "Failed to query codec " + codecName + " (" + supportedType + ")");
                throw e;
            }
            Log.e(TAG, "Skipping codec " + codecName + " (failed to query capabilities)");
        } catch (Exception e2) {
            throw new DecoderQueryException(e2);
        }
    }

    private static boolean isCodecUsableDecoder(MediaCodecInfo info, String name, boolean secureDecodersExplicit) {
        if (info.isEncoder()) {
            return false;
        }
        if (!secureDecodersExplicit && name.endsWith(".secure")) {
            return false;
        }
        if (Util.SDK_INT < 21 && ("CIPAACDecoder".equals(name) || "CIPMP3Decoder".equals(name) || "CIPVorbisDecoder".equals(name) || "AACDecoder".equals(name) || "MP3Decoder".equals(name))) {
            return false;
        }
        if (Util.SDK_INT < 18 && "OMX.SEC.MP3.Decoder".equals(name)) {
            return false;
        }
        if (Util.SDK_INT < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(name) && "a70".equals(Util.DEVICE)) {
            return false;
        }
        if (Util.SDK_INT == 16 && Util.DEVICE != null && "OMX.qcom.audio.decoder.mp3".equals(name) && ("dlxu".equals(Util.DEVICE) || "protou".equals(Util.DEVICE) || "ville".equals(Util.DEVICE) || "villeplus".equals(Util.DEVICE) || "villec2".equals(Util.DEVICE) || Util.DEVICE.startsWith("gee") || "C6602".equals(Util.DEVICE) || "C6603".equals(Util.DEVICE) || "C6606".equals(Util.DEVICE) || "C6616".equals(Util.DEVICE) || "L36h".equals(Util.DEVICE) || "SO-02E".equals(Util.DEVICE))) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(name) && ("C1504".equals(Util.DEVICE) || "C1505".equals(Util.DEVICE) || "C1604".equals(Util.DEVICE) || "C1605".equals(Util.DEVICE))) {
            return false;
        }
        if (Util.SDK_INT <= 19 && Util.DEVICE != null && ((Util.DEVICE.startsWith("d2") || Util.DEVICE.startsWith("serrano") || Util.DEVICE.startsWith("jflte") || Util.DEVICE.startsWith("santos")) && "samsung".equals(Util.MANUFACTURER) && name.equals("OMX.SEC.vp8.dec"))) {
            return false;
        }
        if (Util.SDK_INT > 19 || Util.DEVICE == null || !Util.DEVICE.startsWith("jflte") || !"OMX.qcom.video.decoder.vp8".equals(name)) {
            return true;
        }
        return false;
    }

    @TargetApi(21)
    public static boolean isSizeSupportedV21(String mimeType, boolean secure, int width, int height) throws DecoderQueryException {
        boolean z;
        if (Util.SDK_INT >= 21) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        MediaCodecInfo.VideoCapabilities videoCapabilities = getVideoCapabilitiesV21(mimeType, secure);
        return videoCapabilities != null && videoCapabilities.isSizeSupported(width, height);
    }

    @TargetApi(21)
    public static boolean isSizeAndRateSupportedV21(String mimeType, boolean secure, int width, int height, double frameRate) throws DecoderQueryException {
        boolean z;
        if (Util.SDK_INT >= 21) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        MediaCodecInfo.VideoCapabilities videoCapabilities = getVideoCapabilitiesV21(mimeType, secure);
        return videoCapabilities != null && videoCapabilities.areSizeAndRateSupported(width, height, frameRate);
    }

    @Deprecated
    public static boolean isH264ProfileSupported(int profile, int level) throws DecoderQueryException {
        DecoderInfo decoderInfo = getDecoderInfo(MimeTypes.VIDEO_H264, false);
        if (decoderInfo == null) {
            return false;
        }
        for (MediaCodecInfo.CodecProfileLevel profileLevel : decoderInfo.capabilities.profileLevels) {
            if (profileLevel.profile == profile && profileLevel.level >= level) {
                return true;
            }
        }
        return false;
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize == -1) {
            int result = 0;
            DecoderInfo decoderInfo = getDecoderInfo(MimeTypes.VIDEO_H264, false);
            if (decoderInfo != null) {
                for (MediaCodecInfo.CodecProfileLevel profileLevel : decoderInfo.capabilities.profileLevels) {
                    result = Math.max(avcLevelToMaxFrameSize(profileLevel.level), result);
                }
                result = Math.max(result, 172800);
            }
            maxH264DecodableFrameSize = result;
        }
        return maxH264DecodableFrameSize;
    }

    @TargetApi(21)
    private static MediaCodecInfo.VideoCapabilities getVideoCapabilitiesV21(String mimeType, boolean secure) throws DecoderQueryException {
        DecoderInfo decoderInfo = getDecoderInfo(mimeType, secure);
        if (decoderInfo == null) {
            return null;
        }
        return decoderInfo.capabilities.getVideoCapabilities();
    }

    private static int avcLevelToMaxFrameSize(int avcLevel) {
        switch (avcLevel) {
            case 1:
            case 2:
                return 25344;
            case 8:
                return 101376;
            case 16:
                return 101376;
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
                return 414720;
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
                return 2097152;
            case 4096:
                return 2097152;
            case 8192:
                return 2228224;
            case 16384:
                return 5652480;
            case 32768:
                return 9437184;
            default:
                return -1;
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;

        public MediaCodecListCompatV21(boolean includeSecure) {
            this.codecKind = includeSecure ? 1 : 0;
        }

        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        public MediaCodecInfo getCodecInfoAt(int index) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[index];
        }

        public boolean secureDecodersExplicit() {
            return true;
        }

        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return capabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        public MediaCodecInfo getCodecInfoAt(int index) {
            return MediaCodecList.getCodecInfoAt(index);
        }

        public boolean secureDecodersExplicit() {
            return false;
        }

        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return MimeTypes.VIDEO_H264.equals(mimeType);
        }
    }

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String mimeType2, boolean secure2) {
            this.mimeType = mimeType2;
            this.secure = secure2;
        }

        public int hashCode() {
            return (((this.mimeType == null ? 0 : this.mimeType.hashCode()) + 31) * 31) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != CodecKey.class) {
                return false;
            }
            CodecKey other = (CodecKey) obj;
            if (!TextUtils.equals(this.mimeType, other.mimeType) || this.secure != other.secure) {
                return false;
            }
            return true;
        }
    }
}
