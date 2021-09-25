package com.google.android.exoplayer.metadata.id3;

public final class PrivFrame extends Id3Frame {

    /* renamed from: ID */
    public static final String f1202ID = "PRIV";
    public final String owner;
    public final byte[] privateData;

    public PrivFrame(String owner2, byte[] privateData2) {
        super(f1202ID);
        this.owner = owner2;
        this.privateData = privateData2;
    }
}
