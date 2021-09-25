package com.google.android.exoplayer;

import com.google.android.exoplayer.MediaCodecUtil;

public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = new MediaCodecSelector() {
        public DecoderInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(mimeType, requiresSecureDecoder);
        }

        public DecoderInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    };

    DecoderInfo getDecoderInfo(String str, boolean z) throws MediaCodecUtil.DecoderQueryException;

    DecoderInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException;
}
