package com.google.android.exoplayer.metadata.id3;

public final class BinaryFrame extends Id3Frame {
    public final byte[] data;

    public BinaryFrame(String type, byte[] data2) {
        super(type);
        this.data = data2;
    }
}
