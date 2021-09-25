package com.google.android.exoplayer.upstream;

public final class Allocation {
    public final byte[] data;
    private final int offset;

    public Allocation(byte[] data2, int offset2) {
        this.data = data2;
        this.offset = offset2;
    }

    public int translateOffset(int offset2) {
        return this.offset + offset2;
    }
}
