package com.google.android.exoplayer.hls;

import java.util.Collections;
import java.util.List;

public final class HlsMasterPlaylist extends HlsPlaylist {
    public final List<Variant> audios;
    public final String muxedAudioLanguage;
    public final String muxedCaptionLanguage;
    public final List<Variant> subtitles;
    public final List<Variant> variants;

    public HlsMasterPlaylist(String baseUri, List<Variant> variants2, List<Variant> audios2, List<Variant> subtitles2, String muxedAudioLanguage2, String muxedCaptionLanguage2) {
        super(baseUri, 0);
        this.variants = Collections.unmodifiableList(variants2);
        this.audios = Collections.unmodifiableList(audios2);
        this.subtitles = Collections.unmodifiableList(subtitles2);
        this.muxedAudioLanguage = muxedAudioLanguage2;
        this.muxedCaptionLanguage = muxedCaptionLanguage2;
    }
}
