package com.google.android.exoplayer.smoothstreaming;

import android.net.Uri;
import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.chunk.FormatWrapper;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.UriUtil;
import com.google.android.exoplayer.util.Util;
import java.util.List;
import java.util.UUID;

public class SmoothStreamingManifest {
    public final long durationUs;
    public final long dvrWindowLengthUs;
    public final boolean isLive;
    public final int lookAheadCount;
    public final int majorVersion;
    public final int minorVersion;
    public final ProtectionElement protectionElement;
    public final StreamElement[] streamElements;

    public SmoothStreamingManifest(int majorVersion2, int minorVersion2, long timescale, long duration, long dvrWindowLength, int lookAheadCount2, boolean isLive2, ProtectionElement protectionElement2, StreamElement[] streamElements2) {
        long scaleLargeTimestamp;
        long scaleLargeTimestamp2;
        this.majorVersion = majorVersion2;
        this.minorVersion = minorVersion2;
        this.lookAheadCount = lookAheadCount2;
        this.isLive = isLive2;
        this.protectionElement = protectionElement2;
        this.streamElements = streamElements2;
        if (dvrWindowLength == 0) {
            scaleLargeTimestamp = -1;
        } else {
            scaleLargeTimestamp = Util.scaleLargeTimestamp(dvrWindowLength, C1907C.MICROS_PER_SECOND, timescale);
        }
        this.dvrWindowLengthUs = scaleLargeTimestamp;
        if (duration == 0) {
            scaleLargeTimestamp2 = -1;
        } else {
            scaleLargeTimestamp2 = Util.scaleLargeTimestamp(duration, C1907C.MICROS_PER_SECOND, timescale);
        }
        this.durationUs = scaleLargeTimestamp2;
    }

    public static class ProtectionElement {
        public final byte[] data;
        public final UUID uuid;

        public ProtectionElement(UUID uuid2, byte[] data2) {
            this.uuid = uuid2;
            this.data = data2;
        }
    }

    public static class TrackElement implements FormatWrapper {
        public final byte[][] csd;
        public final Format format;

        public TrackElement(int index, int bitrate, String mimeType, byte[][] csd2, int maxWidth, int maxHeight, int sampleRate, int numChannels, String language) {
            this.csd = csd2;
            this.format = new Format(String.valueOf(index), mimeType, maxWidth, maxHeight, -1.0f, numChannels, sampleRate, bitrate, language);
        }

        public Format getFormat() {
            return this.format;
        }
    }

    public static class StreamElement {
        public static final int TYPE_AUDIO = 0;
        public static final int TYPE_TEXT = 2;
        public static final int TYPE_UNKNOWN = -1;
        public static final int TYPE_VIDEO = 1;
        private static final String URL_PLACEHOLDER_BITRATE = "{bitrate}";
        private static final String URL_PLACEHOLDER_START_TIME = "{start time}";
        private final String baseUri;
        public final int chunkCount;
        private final List<Long> chunkStartTimes;
        private final long[] chunkStartTimesUs;
        private final String chunkTemplate;
        public final int displayHeight;
        public final int displayWidth;
        public final String language;
        private final long lastChunkDurationUs;
        public final int maxHeight;
        public final int maxWidth;
        public final String name;
        public final int qualityLevels;
        public final String subType;
        public final long timescale;
        public final TrackElement[] tracks;
        public final int type;

        public StreamElement(String baseUri2, String chunkTemplate2, int type2, String subType2, long timescale2, String name2, int qualityLevels2, int maxWidth2, int maxHeight2, int displayWidth2, int displayHeight2, String language2, TrackElement[] tracks2, List<Long> chunkStartTimes2, long lastChunkDuration) {
            this.baseUri = baseUri2;
            this.chunkTemplate = chunkTemplate2;
            this.type = type2;
            this.subType = subType2;
            this.timescale = timescale2;
            this.name = name2;
            this.qualityLevels = qualityLevels2;
            this.maxWidth = maxWidth2;
            this.maxHeight = maxHeight2;
            this.displayWidth = displayWidth2;
            this.displayHeight = displayHeight2;
            this.language = language2;
            this.tracks = tracks2;
            this.chunkCount = chunkStartTimes2.size();
            this.chunkStartTimes = chunkStartTimes2;
            this.lastChunkDurationUs = Util.scaleLargeTimestamp(lastChunkDuration, C1907C.MICROS_PER_SECOND, timescale2);
            this.chunkStartTimesUs = Util.scaleLargeTimestamps(chunkStartTimes2, C1907C.MICROS_PER_SECOND, timescale2);
        }

        public int getChunkIndex(long timeUs) {
            return Util.binarySearchFloor(this.chunkStartTimesUs, timeUs, true, true);
        }

        public long getStartTimeUs(int chunkIndex) {
            return this.chunkStartTimesUs[chunkIndex];
        }

        public long getChunkDurationUs(int chunkIndex) {
            return chunkIndex == this.chunkCount + -1 ? this.lastChunkDurationUs : this.chunkStartTimesUs[chunkIndex + 1] - this.chunkStartTimesUs[chunkIndex];
        }

        public Uri buildRequestUri(int track, int chunkIndex) {
            boolean z;
            boolean z2;
            boolean z3 = true;
            if (this.tracks != null) {
                z = true;
            } else {
                z = false;
            }
            Assertions.checkState(z);
            if (this.chunkStartTimes != null) {
                z2 = true;
            } else {
                z2 = false;
            }
            Assertions.checkState(z2);
            if (chunkIndex >= this.chunkStartTimes.size()) {
                z3 = false;
            }
            Assertions.checkState(z3);
            return UriUtil.resolveToUri(this.baseUri, this.chunkTemplate.replace(URL_PLACEHOLDER_BITRATE, Integer.toString(this.tracks[track].format.bitrate)).replace(URL_PLACEHOLDER_START_TIME, this.chunkStartTimes.get(chunkIndex).toString()));
        }
    }
}
