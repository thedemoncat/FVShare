package com.google.android.exoplayer.metadata.id3;

public final class TxxxFrame extends Id3Frame {

    /* renamed from: ID */
    public static final String f1203ID = "TXXX";
    public final String description;
    public final String value;

    public TxxxFrame(String description2, String value2) {
        super(f1203ID);
        this.description = description2;
        this.value = value2;
    }
}
