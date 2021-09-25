package com.google.android.exoplayer.extractor.p016ts;

import android.support.p001v4.view.InputDeviceCompat;
import android.util.SparseArray;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.umeng.analytics.pro.C0217dk;
import java.io.IOException;

/* renamed from: com.google.android.exoplayer.extractor.ts.PsExtractor */
public final class PsExtractor implements Extractor {
    public static final int AUDIO_STREAM = 192;
    public static final int AUDIO_STREAM_MASK = 224;
    private static final long MAX_SEARCH_LENGTH = 1048576;
    private static final int MPEG_PROGRAM_END_CODE = 441;
    private static final int PACKET_START_CODE_PREFIX = 1;
    private static final int PACK_START_CODE = 442;
    public static final int PRIVATE_STREAM_1 = 189;
    private static final int SYSTEM_HEADER_START_CODE = 443;
    public static final int VIDEO_STREAM = 224;
    public static final int VIDEO_STREAM_MASK = 240;
    private boolean foundAllTracks;
    private boolean foundAudioTrack;
    private boolean foundVideoTrack;
    private ExtractorOutput output;
    private final ParsableByteArray psPacketBuffer;
    private final SparseArray<PesReader> psPayloadReaders;
    private final PtsTimestampAdjuster ptsTimestampAdjuster;

    public PsExtractor() {
        this(new PtsTimestampAdjuster(0));
    }

    public PsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster2) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster2;
        this.psPacketBuffer = new ParsableByteArray(4096);
        this.psPayloadReaders = new SparseArray<>();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        boolean z = true;
        byte[] scratch = new byte[14];
        input.peekFully(scratch, 0, 14);
        if (PACK_START_CODE != (((scratch[0] & 255) << 24) | ((scratch[1] & 255) << C0217dk.f724n) | ((scratch[2] & 255) << 8) | (scratch[3] & 255)) || (scratch[4] & 196) != 68 || (scratch[6] & 4) != 4 || (scratch[8] & 4) != 4 || (scratch[9] & 1) != 1 || (scratch[12] & 3) != 3) {
            return false;
        }
        input.advancePeekPosition(scratch[13] & 7);
        input.peekFully(scratch, 0, 3);
        if (1 != (((scratch[0] & 255) << C0217dk.f724n) | ((scratch[1] & 255) << 8) | (scratch[2] & 255))) {
            z = false;
        }
        return z;
    }

    public void init(ExtractorOutput output2) {
        this.output = output2;
        output2.seekMap(SeekMap.UNSEEKABLE);
    }

    public void seek() {
        this.ptsTimestampAdjuster.reset();
        for (int i = 0; i < this.psPayloadReaders.size(); i++) {
            this.psPayloadReaders.valueAt(i).seek();
        }
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        if (!input.peekFully(this.psPacketBuffer.data, 0, 4, true)) {
            return -1;
        }
        this.psPacketBuffer.setPosition(0);
        int nextStartCode = this.psPacketBuffer.readInt();
        if (nextStartCode == MPEG_PROGRAM_END_CODE) {
            return -1;
        }
        if (nextStartCode == PACK_START_CODE) {
            input.peekFully(this.psPacketBuffer.data, 0, 10);
            this.psPacketBuffer.setPosition(0);
            this.psPacketBuffer.skipBytes(9);
            input.skipFully((this.psPacketBuffer.readUnsignedByte() & 7) + 14);
            return 0;
        } else if (nextStartCode == SYSTEM_HEADER_START_CODE) {
            input.peekFully(this.psPacketBuffer.data, 0, 2);
            this.psPacketBuffer.setPosition(0);
            input.skipFully(this.psPacketBuffer.readUnsignedShort() + 6);
            return 0;
        } else if (((nextStartCode & InputDeviceCompat.SOURCE_ANY) >> 8) != 1) {
            input.skipFully(1);
            return 0;
        } else {
            int streamId = nextStartCode & 255;
            PesReader payloadReader = this.psPayloadReaders.get(streamId);
            if (!this.foundAllTracks) {
                if (payloadReader == null) {
                    ElementaryStreamReader elementaryStreamReader = null;
                    if (!this.foundAudioTrack && streamId == 189) {
                        elementaryStreamReader = new Ac3Reader(this.output.track(streamId), false);
                        this.foundAudioTrack = true;
                    } else if (!this.foundAudioTrack && (streamId & 224) == 192) {
                        elementaryStreamReader = new MpegAudioReader(this.output.track(streamId));
                        this.foundAudioTrack = true;
                    } else if (!this.foundVideoTrack && (streamId & 240) == 224) {
                        elementaryStreamReader = new H262Reader(this.output.track(streamId));
                        this.foundVideoTrack = true;
                    }
                    if (elementaryStreamReader != null) {
                        payloadReader = new PesReader(elementaryStreamReader, this.ptsTimestampAdjuster);
                        this.psPayloadReaders.put(streamId, payloadReader);
                    }
                }
                if ((this.foundAudioTrack && this.foundVideoTrack) || input.getPosition() > 1048576) {
                    this.foundAllTracks = true;
                    this.output.endTracks();
                }
            }
            input.peekFully(this.psPacketBuffer.data, 0, 2);
            this.psPacketBuffer.setPosition(0);
            int pesLength = this.psPacketBuffer.readUnsignedShort() + 6;
            if (payloadReader == null) {
                input.skipFully(pesLength);
            } else {
                if (this.psPacketBuffer.capacity() < pesLength) {
                    this.psPacketBuffer.reset(new byte[pesLength], pesLength);
                }
                input.readFully(this.psPacketBuffer.data, 0, pesLength);
                this.psPacketBuffer.setPosition(6);
                this.psPacketBuffer.setLimit(pesLength);
                payloadReader.consume(this.psPacketBuffer, this.output);
                this.psPacketBuffer.setLimit(this.psPacketBuffer.capacity());
            }
            return 0;
        }
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.PsExtractor$PesReader */
    private static final class PesReader {
        private static final int PES_SCRATCH_SIZE = 64;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch = new ParsableBitArray(new byte[64]);
        private boolean ptsFlag;
        private final PtsTimestampAdjuster ptsTimestampAdjuster;
        private boolean seenFirstDts;
        private long timeUs;

        public PesReader(ElementaryStreamReader pesPayloadReader2, PtsTimestampAdjuster ptsTimestampAdjuster2) {
            this.pesPayloadReader = pesPayloadReader2;
            this.ptsTimestampAdjuster = ptsTimestampAdjuster2;
        }

        public void seek() {
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }

        public void consume(ParsableByteArray data, ExtractorOutput output) {
            data.readBytes(this.pesScratch.data, 0, 3);
            this.pesScratch.setPosition(0);
            parseHeader();
            data.readBytes(this.pesScratch.data, 0, this.extendedHeaderLength);
            this.pesScratch.setPosition(0);
            parseHeaderExtension();
            this.pesPayloadReader.packetStarted(this.timeUs, true);
            this.pesPayloadReader.consume(data);
            this.pesPayloadReader.packetFinished();
        }

        private void parseHeader() {
            this.pesScratch.skipBits(8);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
        }

        private void parseHeaderExtension() {
            this.timeUs = 0;
            if (this.ptsFlag) {
                this.pesScratch.skipBits(4);
                long pts = ((long) this.pesScratch.readBits(3)) << 30;
                this.pesScratch.skipBits(1);
                long pts2 = pts | ((long) (this.pesScratch.readBits(15) << 15));
                this.pesScratch.skipBits(1);
                long pts3 = pts2 | ((long) this.pesScratch.readBits(15));
                this.pesScratch.skipBits(1);
                if (!this.seenFirstDts && this.dtsFlag) {
                    this.pesScratch.skipBits(4);
                    long dts = ((long) this.pesScratch.readBits(3)) << 30;
                    this.pesScratch.skipBits(1);
                    long dts2 = dts | ((long) (this.pesScratch.readBits(15) << 15));
                    this.pesScratch.skipBits(1);
                    long dts3 = dts2 | ((long) this.pesScratch.readBits(15));
                    this.pesScratch.skipBits(1);
                    this.ptsTimestampAdjuster.adjustTimestamp(dts3);
                    this.seenFirstDts = true;
                }
                this.timeUs = this.ptsTimestampAdjuster.adjustTimestamp(pts3);
            }
        }
    }
}
