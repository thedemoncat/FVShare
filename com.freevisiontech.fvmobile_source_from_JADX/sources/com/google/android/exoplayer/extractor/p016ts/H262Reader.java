package com.google.android.exoplayer.extractor.p016ts;

import android.util.Pair;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.umeng.analytics.pro.C0217dk;
import java.util.Arrays;
import java.util.Collections;

/* renamed from: com.google.android.exoplayer.extractor.ts.H262Reader */
final class H262Reader extends ElementaryStreamReader {
    private static final double[] FRAME_RATE_VALUES = {23.976023976023978d, 24.0d, 25.0d, 29.97002997002997d, 30.0d, 50.0d, 59.94005994005994d, 60.0d};
    private static final int START_EXTENSION = 181;
    private static final int START_GROUP = 184;
    private static final int START_PICTURE = 0;
    private static final int START_SEQUENCE_HEADER = 179;
    private final CsdBuffer csdBuffer = new CsdBuffer(128);
    private boolean foundFirstFrameInGroup;
    private long frameDurationUs;
    private long framePosition;
    private long frameTimeUs;
    private boolean hasOutputFormat;
    private boolean isKeyframe;
    private boolean pesPtsUsAvailable;
    private long pesTimeUs;
    private final boolean[] prefixFlags = new boolean[4];
    private long totalBytesWritten;

    public H262Reader(TrackOutput output) {
        super(output);
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.csdBuffer.reset();
        this.pesPtsUsAvailable = false;
        this.foundFirstFrameInGroup = false;
        this.totalBytesWritten = 0;
    }

    public void packetStarted(long pesTimeUs2, boolean dataAlignmentIndicator) {
        this.pesPtsUsAvailable = pesTimeUs2 != -1;
        if (this.pesPtsUsAvailable) {
            this.pesTimeUs = pesTimeUs2;
        }
    }

    public void consume(ParsableByteArray data) {
        if (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += (long) data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            int searchOffset = offset;
            while (true) {
                int startCodeOffset = NalUnitUtil.findNalUnit(dataArray, searchOffset, limit, this.prefixFlags);
                if (startCodeOffset == limit) {
                    break;
                }
                int startCodeValue = data.data[startCodeOffset + 3] & 255;
                if (!this.hasOutputFormat) {
                    int lengthToStartCode = startCodeOffset - offset;
                    if (lengthToStartCode > 0) {
                        this.csdBuffer.onData(dataArray, offset, startCodeOffset);
                    }
                    if (this.csdBuffer.onStartCode(startCodeValue, lengthToStartCode < 0 ? -lengthToStartCode : 0)) {
                        Pair<MediaFormat, Long> result = parseCsdBuffer(this.csdBuffer);
                        this.output.format((MediaFormat) result.first);
                        this.frameDurationUs = ((Long) result.second).longValue();
                        this.hasOutputFormat = true;
                    }
                }
                if (this.hasOutputFormat && (startCodeValue == 184 || startCodeValue == 0)) {
                    int bytesWrittenPastStartCode = limit - startCodeOffset;
                    if (this.foundFirstFrameInGroup) {
                        this.output.sampleMetadata(this.frameTimeUs, this.isKeyframe ? 1 : 0, ((int) (this.totalBytesWritten - this.framePosition)) - bytesWrittenPastStartCode, bytesWrittenPastStartCode, (byte[]) null);
                        this.isKeyframe = false;
                    }
                    if (startCodeValue == 184) {
                        this.foundFirstFrameInGroup = false;
                        this.isKeyframe = true;
                    } else {
                        this.frameTimeUs = this.pesPtsUsAvailable ? this.pesTimeUs : this.frameTimeUs + this.frameDurationUs;
                        this.framePosition = this.totalBytesWritten - ((long) bytesWrittenPastStartCode);
                        this.pesPtsUsAvailable = false;
                        this.foundFirstFrameInGroup = true;
                    }
                }
                offset = startCodeOffset;
                searchOffset = offset + 3;
            }
            if (!this.hasOutputFormat) {
                this.csdBuffer.onData(dataArray, offset, limit);
            }
        }
    }

    public void packetFinished() {
    }

    private static Pair<MediaFormat, Long> parseCsdBuffer(CsdBuffer csdBuffer2) {
        byte[] csdData = Arrays.copyOf(csdBuffer2.data, csdBuffer2.length);
        int firstByte = csdData[4] & 255;
        int secondByte = csdData[5] & 255;
        int width = (firstByte << 4) | (secondByte >> 4);
        int height = ((secondByte & 15) << 8) | (csdData[6] & 255);
        float pixelWidthHeightRatio = 1.0f;
        switch ((csdData[7] & 240) >> 4) {
            case 2:
                pixelWidthHeightRatio = ((float) (height * 4)) / ((float) (width * 3));
                break;
            case 3:
                pixelWidthHeightRatio = ((float) (height * 16)) / ((float) (width * 9));
                break;
            case 4:
                pixelWidthHeightRatio = ((float) (height * 121)) / ((float) (width * 100));
                break;
        }
        MediaFormat format = MediaFormat.createVideoFormat((String) null, MimeTypes.VIDEO_MPEG2, -1, -1, -1, width, height, Collections.singletonList(csdData), -1, pixelWidthHeightRatio);
        long frameDurationUs2 = 0;
        int frameRateCodeMinusOne = (csdData[7] & C0217dk.f723m) - 1;
        if (frameRateCodeMinusOne >= 0 && frameRateCodeMinusOne < FRAME_RATE_VALUES.length) {
            double frameRate = FRAME_RATE_VALUES[frameRateCodeMinusOne];
            int sequenceExtensionPosition = csdBuffer2.sequenceExtensionPosition;
            int frameRateExtensionN = (csdData[sequenceExtensionPosition + 9] & 96) >> 5;
            int frameRateExtensionD = csdData[sequenceExtensionPosition + 9] & 31;
            if (frameRateExtensionN != frameRateExtensionD) {
                frameRate *= (((double) frameRateExtensionN) + 1.0d) / ((double) (frameRateExtensionD + 1));
            }
            frameDurationUs2 = (long) (1000000.0d / frameRate);
        }
        return Pair.create(format, Long.valueOf(frameDurationUs2));
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.H262Reader$CsdBuffer */
    private static final class CsdBuffer {
        public byte[] data;
        private boolean isFilling;
        public int length;
        public int sequenceExtensionPosition;

        public CsdBuffer(int initialCapacity) {
            this.data = new byte[initialCapacity];
        }

        public void reset() {
            this.isFilling = false;
            this.length = 0;
            this.sequenceExtensionPosition = 0;
        }

        public boolean onStartCode(int startCodeValue, int bytesAlreadyPassed) {
            if (this.isFilling) {
                if (this.sequenceExtensionPosition == 0 && startCodeValue == 181) {
                    this.sequenceExtensionPosition = this.length;
                } else {
                    this.length -= bytesAlreadyPassed;
                    this.isFilling = false;
                    return true;
                }
            } else if (startCodeValue == 179) {
                this.isFilling = true;
            }
            return false;
        }

        public void onData(byte[] newData, int offset, int limit) {
            if (this.isFilling) {
                int readLength = limit - offset;
                if (this.data.length < this.length + readLength) {
                    this.data = Arrays.copyOf(this.data, (this.length + readLength) * 2);
                }
                System.arraycopy(newData, offset, this.data, this.length, readLength);
                this.length += readLength;
            }
        }
    }
}
