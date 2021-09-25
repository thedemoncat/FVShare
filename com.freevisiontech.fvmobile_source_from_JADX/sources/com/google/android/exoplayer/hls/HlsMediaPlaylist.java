package com.google.android.exoplayer.hls;

import java.util.List;

public final class HlsMediaPlaylist extends HlsPlaylist {
    public static final String ENCRYPTION_METHOD_AES_128 = "AES-128";
    public static final String ENCRYPTION_METHOD_NONE = "NONE";
    public final long durationUs;
    public final boolean live;
    public final int mediaSequence;
    public final List<Segment> segments;
    public final int targetDurationSecs;
    public final int version;

    public static final class Segment implements Comparable<Long> {
        public final long byterangeLength;
        public final long byterangeOffset;
        public final int discontinuitySequenceNumber;
        public final double durationSecs;
        public final String encryptionIV;
        public final String encryptionKeyUri;
        public final boolean isEncrypted;
        public final long startTimeUs;
        public final String url;

        public Segment(String uri, double durationSecs2, int discontinuitySequenceNumber2, long startTimeUs2, boolean isEncrypted2, String encryptionKeyUri2, String encryptionIV2, long byterangeOffset2, long byterangeLength2) {
            this.url = uri;
            this.durationSecs = durationSecs2;
            this.discontinuitySequenceNumber = discontinuitySequenceNumber2;
            this.startTimeUs = startTimeUs2;
            this.isEncrypted = isEncrypted2;
            this.encryptionKeyUri = encryptionKeyUri2;
            this.encryptionIV = encryptionIV2;
            this.byterangeOffset = byterangeOffset2;
            this.byterangeLength = byterangeLength2;
        }

        public int compareTo(Long startTimeUs2) {
            if (this.startTimeUs > startTimeUs2.longValue()) {
                return 1;
            }
            return this.startTimeUs < startTimeUs2.longValue() ? -1 : 0;
        }
    }

    public HlsMediaPlaylist(String baseUri, int mediaSequence2, int targetDurationSecs2, int version2, boolean live2, List<Segment> segments2) {
        super(baseUri, 1);
        this.mediaSequence = mediaSequence2;
        this.targetDurationSecs = targetDurationSecs2;
        this.version = version2;
        this.live = live2;
        this.segments = segments2;
        if (!segments2.isEmpty()) {
            Segment last = segments2.get(segments2.size() - 1);
            this.durationUs = last.startTimeUs + ((long) (last.durationSecs * 1000000.0d));
            return;
        }
        this.durationUs = 0;
    }
}
