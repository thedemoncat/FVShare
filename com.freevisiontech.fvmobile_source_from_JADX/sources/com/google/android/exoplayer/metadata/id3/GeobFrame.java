package com.google.android.exoplayer.metadata.id3;

public final class GeobFrame extends Id3Frame {

    /* renamed from: ID */
    public static final String f1200ID = "GEOB";
    public final byte[] data;
    public final String description;
    public final String filename;
    public final String mimeType;

    public GeobFrame(String mimeType2, String filename2, String description2, byte[] data2) {
        super(f1200ID);
        this.mimeType = mimeType2;
        this.filename = filename2;
        this.description = description2;
        this.data = data2;
    }
}
