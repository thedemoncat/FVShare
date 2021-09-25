package com.google.android.exoplayer.drm;

import android.annotation.TargetApi;
import android.media.MediaCrypto;
import com.google.android.exoplayer.util.Assertions;

@TargetApi(16)
public final class FrameworkMediaCrypto implements ExoMediaCrypto {
    private final MediaCrypto mediaCrypto;

    FrameworkMediaCrypto(MediaCrypto mediaCrypto2) {
        this.mediaCrypto = (MediaCrypto) Assertions.checkNotNull(mediaCrypto2);
    }

    public MediaCrypto getWrappedMediaCrypto() {
        return this.mediaCrypto;
    }

    public boolean requiresSecureDecoderComponent(String mimeType) {
        return this.mediaCrypto.requiresSecureDecoderComponent(mimeType);
    }
}
