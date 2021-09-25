package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.util.Assertions;
import java.util.Comparator;

public class Format {
    public final int audioChannels;
    public final int audioSamplingRate;
    public final int bitrate;
    public final String codecs;
    public final float frameRate;
    public final int height;

    /* renamed from: id */
    public final String f1192id;
    public final String language;
    public final String mimeType;
    public final int width;

    public static final class DecreasingBandwidthComparator implements Comparator<Format> {
        public int compare(Format a, Format b) {
            return b.bitrate - a.bitrate;
        }
    }

    public Format(String id, String mimeType2, int width2, int height2, float frameRate2, int numChannels, int audioSamplingRate2, int bitrate2) {
        this(id, mimeType2, width2, height2, frameRate2, numChannels, audioSamplingRate2, bitrate2, (String) null);
    }

    public Format(String id, String mimeType2, int width2, int height2, float frameRate2, int numChannels, int audioSamplingRate2, int bitrate2, String language2) {
        this(id, mimeType2, width2, height2, frameRate2, numChannels, audioSamplingRate2, bitrate2, language2, (String) null);
    }

    public Format(String id, String mimeType2, int width2, int height2, float frameRate2, int audioChannels2, int audioSamplingRate2, int bitrate2, String language2, String codecs2) {
        this.f1192id = (String) Assertions.checkNotNull(id);
        this.mimeType = mimeType2;
        this.width = width2;
        this.height = height2;
        this.frameRate = frameRate2;
        this.audioChannels = audioChannels2;
        this.audioSamplingRate = audioSamplingRate2;
        this.bitrate = bitrate2;
        this.language = language2;
        this.codecs = codecs2;
    }

    public int hashCode() {
        return this.f1192id.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return ((Format) obj).f1192id.equals(this.f1192id);
    }
}
