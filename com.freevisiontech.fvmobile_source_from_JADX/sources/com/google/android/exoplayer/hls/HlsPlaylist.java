package com.google.android.exoplayer.hls;

public abstract class HlsPlaylist {
    public static final int TYPE_MASTER = 0;
    public static final int TYPE_MEDIA = 1;
    public final String baseUri;
    public final int type;

    protected HlsPlaylist(String baseUri2, int type2) {
        this.baseUri = baseUri2;
        this.type = type2;
    }
}
