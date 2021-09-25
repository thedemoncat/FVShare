package com.google.android.exoplayer.extractor.mp4;

public final class TrackEncryptionBox {
    public final int initializationVectorSize;
    public final boolean isEncrypted;
    public final byte[] keyId;

    public TrackEncryptionBox(boolean isEncrypted2, int initializationVectorSize2, byte[] keyId2) {
        this.isEncrypted = isEncrypted2;
        this.initializationVectorSize = initializationVectorSize2;
        this.keyId = keyId2;
    }
}
