package com.google.android.exoplayer.extractor.p016ts;

import android.support.p001v4.app.FrameMetricsAggregator;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Arrays;
import java.util.Collections;

/* renamed from: com.google.android.exoplayer.extractor.ts.AdtsReader */
final class AdtsReader extends ElementaryStreamReader {
    private static final int CRC_SIZE = 2;
    private static final int HEADER_SIZE = 5;
    private static final int ID3_HEADER_SIZE = 10;
    private static final byte[] ID3_IDENTIFIER = {73, 68, 51};
    private static final int ID3_SIZE_OFFSET = 6;
    private static final int MATCH_STATE_FF = 512;
    private static final int MATCH_STATE_I = 768;
    private static final int MATCH_STATE_ID = 1024;
    private static final int MATCH_STATE_START = 256;
    private static final int MATCH_STATE_VALUE_SHIFT = 8;
    private static final int STATE_FINDING_SAMPLE = 0;
    private static final int STATE_READING_ADTS_HEADER = 2;
    private static final int STATE_READING_ID3_HEADER = 1;
    private static final int STATE_READING_SAMPLE = 3;
    private static final String TAG = "AdtsReader";
    private final ParsableBitArray adtsScratch = new ParsableBitArray(new byte[7]);
    private int bytesRead;
    private TrackOutput currentOutput;
    private long currentSampleDuration;
    private boolean hasCrc;
    private boolean hasOutputFormat;
    private final ParsableByteArray id3HeaderBuffer = new ParsableByteArray(Arrays.copyOf(ID3_IDENTIFIER, 10));
    private final TrackOutput id3Output;
    private int matchState;
    private long sampleDurationUs;
    private int sampleSize;
    private int state;
    private long timeUs;

    public AdtsReader(TrackOutput output, TrackOutput id3Output2) {
        super(output);
        this.id3Output = id3Output2;
        id3Output2.format(MediaFormat.createId3Format());
        setFindingSampleState();
    }

    public void seek() {
        setFindingSampleState();
    }

    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    findNextSample(data);
                    break;
                case 1:
                    if (!continueRead(data, this.id3HeaderBuffer.data, 10)) {
                        break;
                    } else {
                        parseId3Header();
                        break;
                    }
                case 2:
                    if (!continueRead(data, this.adtsScratch.data, this.hasCrc ? 7 : 5)) {
                        break;
                    } else {
                        parseAdtsHeader();
                        break;
                    }
                case 3:
                    readSample(data);
                    break;
            }
        }
    }

    public void packetFinished() {
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        source.readBytes(target, this.bytesRead, bytesToRead);
        this.bytesRead += bytesToRead;
        return this.bytesRead == targetLength;
    }

    private void setFindingSampleState() {
        this.state = 0;
        this.bytesRead = 0;
        this.matchState = 256;
    }

    private void setReadingId3HeaderState() {
        this.state = 1;
        this.bytesRead = ID3_IDENTIFIER.length;
        this.sampleSize = 0;
        this.id3HeaderBuffer.setPosition(0);
    }

    private void setReadingSampleState(TrackOutput outputToUse, long currentSampleDuration2, int priorReadBytes, int sampleSize2) {
        this.state = 3;
        this.bytesRead = priorReadBytes;
        this.currentOutput = outputToUse;
        this.currentSampleDuration = currentSampleDuration2;
        this.sampleSize = sampleSize2;
    }

    private void setReadingAdtsHeaderState() {
        this.state = 2;
        this.bytesRead = 0;
    }

    private void findNextSample(ParsableByteArray pesBuffer) {
        byte[] adtsData = pesBuffer.data;
        int position = pesBuffer.getPosition();
        int endOffset = pesBuffer.limit();
        int position2 = position;
        while (position2 < endOffset) {
            int position3 = position2 + 1;
            int data = adtsData[position2] & 255;
            if (this.matchState != 512 || data < 240 || data == 255) {
                switch (this.matchState | data) {
                    case 329:
                        this.matchState = 768;
                        break;
                    case FrameMetricsAggregator.EVERY_DURATION /*511*/:
                        this.matchState = 512;
                        break;
                    case 836:
                        this.matchState = 1024;
                        break;
                    case 1075:
                        setReadingId3HeaderState();
                        pesBuffer.setPosition(position3);
                        return;
                    default:
                        if (this.matchState == 256) {
                            break;
                        } else {
                            this.matchState = 256;
                            position3--;
                            break;
                        }
                }
                position2 = position3;
            } else {
                this.hasCrc = (data & 1) == 0;
                setReadingAdtsHeaderState();
                pesBuffer.setPosition(position3);
                return;
            }
        }
        pesBuffer.setPosition(position2);
        int i = position2;
    }

    private void parseId3Header() {
        this.id3Output.sampleData(this.id3HeaderBuffer, 10);
        this.id3HeaderBuffer.setPosition(6);
        setReadingSampleState(this.id3Output, 0, 10, this.id3HeaderBuffer.readSynchSafeInt() + 10);
    }

    private void parseAdtsHeader() {
        this.adtsScratch.setPosition(0);
        if (!this.hasOutputFormat) {
            int audioObjectType = this.adtsScratch.readBits(2) + 1;
            if (audioObjectType != 2) {
                Log.w(TAG, "Detected audio object type: " + audioObjectType + ", but assuming AAC LC.");
                audioObjectType = 2;
            }
            int sampleRateIndex = this.adtsScratch.readBits(4);
            this.adtsScratch.skipBits(1);
            byte[] audioSpecificConfig = CodecSpecificDataUtil.buildAacAudioSpecificConfig(audioObjectType, sampleRateIndex, this.adtsScratch.readBits(3));
            Pair<Integer, Integer> audioParams = CodecSpecificDataUtil.parseAacAudioSpecificConfig(audioSpecificConfig);
            MediaFormat mediaFormat = MediaFormat.createAudioFormat((String) null, MimeTypes.AUDIO_AAC, -1, -1, -1, ((Integer) audioParams.second).intValue(), ((Integer) audioParams.first).intValue(), Collections.singletonList(audioSpecificConfig), (String) null);
            this.sampleDurationUs = 1024000000 / ((long) mediaFormat.sampleRate);
            this.output.format(mediaFormat);
            this.hasOutputFormat = true;
        } else {
            this.adtsScratch.skipBits(10);
        }
        this.adtsScratch.skipBits(4);
        int sampleSize2 = (this.adtsScratch.readBits(13) - 2) - 5;
        if (this.hasCrc) {
            sampleSize2 -= 2;
        }
        setReadingSampleState(this.output, this.sampleDurationUs, 0, sampleSize2);
    }

    private void readSample(ParsableByteArray data) {
        int bytesToRead = Math.min(data.bytesLeft(), this.sampleSize - this.bytesRead);
        this.currentOutput.sampleData(data, bytesToRead);
        this.bytesRead += bytesToRead;
        if (this.bytesRead == this.sampleSize) {
            this.currentOutput.sampleMetadata(this.timeUs, 1, this.sampleSize, 0, (byte[]) null);
            this.timeUs += this.currentSampleDuration;
            setFindingSampleState();
        }
    }
}
