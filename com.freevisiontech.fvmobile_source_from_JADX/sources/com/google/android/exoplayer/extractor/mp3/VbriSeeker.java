package com.google.android.exoplayer.extractor.mp3;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.util.MpegAudioHeader;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

final class VbriSeeker implements Mp3Extractor.Seeker {
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;

    public static VbriSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength) {
        int segmentSize;
        long min;
        frame.skipBytes(10);
        int numFrames = frame.readInt();
        if (numFrames <= 0) {
            return null;
        }
        int sampleRate = mpegAudioHeader.sampleRate;
        long durationUs2 = Util.scaleLargeTimestamp((long) numFrames, ((long) (sampleRate >= 32000 ? 1152 : 576)) * C1907C.MICROS_PER_SECOND, (long) sampleRate);
        int entryCount = frame.readUnsignedShort();
        int scale = frame.readUnsignedShort();
        int entrySize = frame.readUnsignedShort();
        frame.skipBytes(2);
        long position2 = position + ((long) mpegAudioHeader.frameSize);
        long[] timesUs2 = new long[(entryCount + 1)];
        long[] positions2 = new long[(entryCount + 1)];
        timesUs2[0] = 0;
        positions2[0] = position2;
        for (int index = 1; index < timesUs2.length; index++) {
            switch (entrySize) {
                case 1:
                    segmentSize = frame.readUnsignedByte();
                    break;
                case 2:
                    segmentSize = frame.readUnsignedShort();
                    break;
                case 3:
                    segmentSize = frame.readUnsignedInt24();
                    break;
                case 4:
                    segmentSize = frame.readUnsignedIntToInt();
                    break;
                default:
                    return null;
            }
            position2 += (long) (segmentSize * scale);
            timesUs2[index] = (((long) index) * durationUs2) / ((long) entryCount);
            if (inputLength == -1) {
                min = position2;
            } else {
                min = Math.min(inputLength, position2);
            }
            positions2[index] = min;
        }
        return new VbriSeeker(timesUs2, positions2, durationUs2);
    }

    private VbriSeeker(long[] timesUs2, long[] positions2, long durationUs2) {
        this.timesUs = timesUs2;
        this.positions = positions2;
        this.durationUs = durationUs2;
    }

    public boolean isSeekable() {
        return true;
    }

    public long getPosition(long timeUs) {
        return this.positions[Util.binarySearchFloor(this.timesUs, timeUs, true, true)];
    }

    public long getTimeUs(long position) {
        return this.timesUs[Util.binarySearchFloor(this.positions, position, true, true)];
    }

    public long getDurationUs() {
        return this.durationUs;
    }
}
