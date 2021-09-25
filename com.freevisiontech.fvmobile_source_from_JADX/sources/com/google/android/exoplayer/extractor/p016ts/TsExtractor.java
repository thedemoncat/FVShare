package com.google.android.exoplayer.extractor.p016ts;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.google.android.exoplayer.extractor.DummyTrackOutput;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

/* renamed from: com.google.android.exoplayer.extractor.ts.TsExtractor */
public final class TsExtractor implements Extractor {
    /* access modifiers changed from: private */
    public static final long AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("AC-3"));
    private static final int BASE_EMBEDDED_TRACK_ID = 8192;
    private static final int BUFFER_PACKET_COUNT = 5;
    private static final int BUFFER_SIZE = 940;
    /* access modifiers changed from: private */
    public static final long E_AC3_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("EAC3"));
    /* access modifiers changed from: private */
    public static final long HEVC_FORMAT_IDENTIFIER = ((long) Util.getIntegerCodeForString("HEVC"));
    private static final String TAG = "TsExtractor";
    private static final int TS_PACKET_SIZE = 188;
    private static final int TS_PAT_PID = 0;
    private static final int TS_STREAM_TYPE_AAC = 15;
    private static final int TS_STREAM_TYPE_AC3 = 129;
    private static final int TS_STREAM_TYPE_DTS = 138;
    private static final int TS_STREAM_TYPE_E_AC3 = 135;
    private static final int TS_STREAM_TYPE_H262 = 2;
    private static final int TS_STREAM_TYPE_H264 = 27;
    private static final int TS_STREAM_TYPE_H265 = 36;
    private static final int TS_STREAM_TYPE_HDMV_DTS = 130;
    private static final int TS_STREAM_TYPE_ID3 = 21;
    private static final int TS_STREAM_TYPE_MPA = 3;
    private static final int TS_STREAM_TYPE_MPA_LSF = 4;
    private static final int TS_SYNC_BYTE = 71;
    public static final int WORKAROUND_ALLOW_NON_IDR_KEYFRAMES = 1;
    public static final int WORKAROUND_DETECT_ACCESS_UNITS = 8;
    public static final int WORKAROUND_IGNORE_AAC_STREAM = 2;
    public static final int WORKAROUND_IGNORE_H264_STREAM = 4;
    public static final int WORKAROUND_MAP_BY_TYPE = 16;
    private final SparseIntArray continuityCounters;
    Id3Reader id3Reader;
    private int nextEmbeddedTrackId;
    private ExtractorOutput output;
    /* access modifiers changed from: private */
    public final PtsTimestampAdjuster ptsTimestampAdjuster;
    final SparseBooleanArray trackIds;
    private final ParsableByteArray tsPacketBuffer;
    final SparseArray<TsPayloadReader> tsPayloadReaders;
    private final ParsableBitArray tsScratch;
    /* access modifiers changed from: private */
    public final int workaroundFlags;

    static /* synthetic */ int access$208(TsExtractor x0) {
        int i = x0.nextEmbeddedTrackId;
        x0.nextEmbeddedTrackId = i + 1;
        return i;
    }

    public TsExtractor() {
        this(new PtsTimestampAdjuster(0));
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster2) {
        this(ptsTimestampAdjuster2, 0);
    }

    public TsExtractor(PtsTimestampAdjuster ptsTimestampAdjuster2, int workaroundFlags2) {
        this.ptsTimestampAdjuster = ptsTimestampAdjuster2;
        this.workaroundFlags = workaroundFlags2;
        this.tsPacketBuffer = new ParsableByteArray((int) BUFFER_SIZE);
        this.tsScratch = new ParsableBitArray(new byte[3]);
        this.tsPayloadReaders = new SparseArray<>();
        this.tsPayloadReaders.put(0, new PatReader());
        this.trackIds = new SparseBooleanArray();
        this.nextEmbeddedTrackId = 8192;
        this.continuityCounters = new SparseIntArray();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        byte[] buffer = this.tsPacketBuffer.data;
        input.peekFully(buffer, 0, BUFFER_SIZE);
        int j = 0;
        while (j < 188) {
            int i = 0;
            while (i != 5) {
                if (buffer[(i * 188) + j] != 71) {
                    j++;
                } else {
                    i++;
                }
            }
            input.skipFully(j);
            return true;
        }
        return false;
    }

    public void init(ExtractorOutput output2) {
        this.output = output2;
        output2.seekMap(SeekMap.UNSEEKABLE);
    }

    public void seek() {
        this.ptsTimestampAdjuster.reset();
        for (int i = 0; i < this.tsPayloadReaders.size(); i++) {
            this.tsPayloadReaders.valueAt(i).seek();
        }
        this.tsPacketBuffer.reset();
        this.continuityCounters.clear();
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        TsPayloadReader payloadReader;
        byte[] data = this.tsPacketBuffer.data;
        if (940 - this.tsPacketBuffer.getPosition() < 188) {
            int bytesLeft = this.tsPacketBuffer.bytesLeft();
            if (bytesLeft > 0) {
                System.arraycopy(data, this.tsPacketBuffer.getPosition(), data, 0, bytesLeft);
            }
            this.tsPacketBuffer.reset(data, bytesLeft);
        }
        while (this.tsPacketBuffer.bytesLeft() < 188) {
            int limit = this.tsPacketBuffer.limit();
            int read = input.read(data, limit, 940 - limit);
            if (read == -1) {
                return -1;
            }
            this.tsPacketBuffer.setLimit(limit + read);
        }
        int limit2 = this.tsPacketBuffer.limit();
        int position = this.tsPacketBuffer.getPosition();
        while (position < limit2 && data[position] != 71) {
            position++;
        }
        this.tsPacketBuffer.setPosition(position);
        int endOfPacket = position + 188;
        if (endOfPacket > limit2) {
            return 0;
        }
        this.tsPacketBuffer.skipBytes(1);
        this.tsPacketBuffer.readBytes(this.tsScratch, 3);
        if (this.tsScratch.readBit()) {
            this.tsPacketBuffer.setPosition(endOfPacket);
            return 0;
        }
        boolean payloadUnitStartIndicator = this.tsScratch.readBit();
        this.tsScratch.skipBits(1);
        int pid = this.tsScratch.readBits(13);
        this.tsScratch.skipBits(2);
        boolean adaptationFieldExists = this.tsScratch.readBit();
        boolean payloadExists = this.tsScratch.readBit();
        boolean discontinuityFound = false;
        int continuityCounter = this.tsScratch.readBits(4);
        int previousCounter = this.continuityCounters.get(pid, continuityCounter - 1);
        this.continuityCounters.put(pid, continuityCounter);
        if (previousCounter == continuityCounter) {
            this.tsPacketBuffer.setPosition(endOfPacket);
            return 0;
        }
        if (continuityCounter != (previousCounter + 1) % 16) {
            discontinuityFound = true;
        }
        if (adaptationFieldExists) {
            this.tsPacketBuffer.skipBytes(this.tsPacketBuffer.readUnsignedByte());
        }
        if (payloadExists && (payloadReader = this.tsPayloadReaders.get(pid)) != null) {
            if (discontinuityFound) {
                payloadReader.seek();
            }
            this.tsPacketBuffer.setLimit(endOfPacket);
            payloadReader.consume(this.tsPacketBuffer, payloadUnitStartIndicator, this.output);
            Assertions.checkState(this.tsPacketBuffer.getPosition() <= endOfPacket);
            this.tsPacketBuffer.setLimit(limit2);
        }
        this.tsPacketBuffer.setPosition(endOfPacket);
        return 0;
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.TsExtractor$TsPayloadReader */
    private static abstract class TsPayloadReader {
        public abstract void consume(ParsableByteArray parsableByteArray, boolean z, ExtractorOutput extractorOutput);

        public abstract void seek();

        private TsPayloadReader() {
        }
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.TsExtractor$PatReader */
    private class PatReader extends TsPayloadReader {
        private int crc;
        private final ParsableBitArray patScratch = new ParsableBitArray(new byte[4]);
        private int sectionBytesRead;
        private final ParsableByteArray sectionData = new ParsableByteArray();
        private int sectionLength;

        public PatReader() {
            super();
        }

        public void seek() {
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            if (payloadUnitStartIndicator) {
                data.skipBytes(data.readUnsignedByte());
                data.readBytes(this.patScratch, 3);
                this.patScratch.skipBits(12);
                this.sectionLength = this.patScratch.readBits(12);
                this.sectionBytesRead = 0;
                this.crc = Util.crc(this.patScratch.data, 0, 3, -1);
                this.sectionData.reset(this.sectionLength);
            }
            int bytesToRead = Math.min(data.bytesLeft(), this.sectionLength - this.sectionBytesRead);
            data.readBytes(this.sectionData.data, this.sectionBytesRead, bytesToRead);
            this.sectionBytesRead += bytesToRead;
            if (this.sectionBytesRead >= this.sectionLength && Util.crc(this.sectionData.data, 0, this.sectionLength, this.crc) == 0) {
                this.sectionData.skipBytes(5);
                int programCount = (this.sectionLength - 9) / 4;
                for (int i = 0; i < programCount; i++) {
                    this.sectionData.readBytes(this.patScratch, 4);
                    int programNumber = this.patScratch.readBits(16);
                    this.patScratch.skipBits(3);
                    if (programNumber == 0) {
                        this.patScratch.skipBits(13);
                    } else {
                        TsExtractor.this.tsPayloadReaders.put(this.patScratch.readBits(13), new PmtReader());
                    }
                }
            }
        }
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.TsExtractor$PmtReader */
    private class PmtReader extends TsPayloadReader {
        private int crc;
        private final ParsableBitArray pmtScratch = new ParsableBitArray(new byte[5]);
        private int sectionBytesRead;
        private final ParsableByteArray sectionData = new ParsableByteArray();
        private int sectionLength;

        public PmtReader() {
            super();
        }

        public void seek() {
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            int trackId;
            ElementaryStreamReader pesPayloadReader;
            if (payloadUnitStartIndicator) {
                data.skipBytes(data.readUnsignedByte());
                data.readBytes(this.pmtScratch, 3);
                this.pmtScratch.skipBits(12);
                this.sectionLength = this.pmtScratch.readBits(12);
                this.sectionBytesRead = 0;
                this.crc = Util.crc(this.pmtScratch.data, 0, 3, -1);
                this.sectionData.reset(this.sectionLength);
            }
            int bytesToRead = Math.min(data.bytesLeft(), this.sectionLength - this.sectionBytesRead);
            data.readBytes(this.sectionData.data, this.sectionBytesRead, bytesToRead);
            this.sectionBytesRead += bytesToRead;
            if (this.sectionBytesRead >= this.sectionLength && Util.crc(this.sectionData.data, 0, this.sectionLength, this.crc) == 0) {
                this.sectionData.skipBytes(7);
                this.sectionData.readBytes(this.pmtScratch, 2);
                this.pmtScratch.skipBits(4);
                int programInfoLength = this.pmtScratch.readBits(12);
                this.sectionData.skipBytes(programInfoLength);
                if ((TsExtractor.this.workaroundFlags & 16) != 0 && TsExtractor.this.id3Reader == null) {
                    TsExtractor.this.id3Reader = new Id3Reader(output.track(21));
                }
                int remainingEntriesLength = ((this.sectionLength - 9) - programInfoLength) - 4;
                while (remainingEntriesLength > 0) {
                    this.sectionData.readBytes(this.pmtScratch, 5);
                    int streamType = this.pmtScratch.readBits(8);
                    this.pmtScratch.skipBits(3);
                    int elementaryPid = this.pmtScratch.readBits(13);
                    this.pmtScratch.skipBits(4);
                    int esInfoLength = this.pmtScratch.readBits(12);
                    if (streamType == 6) {
                        streamType = readPrivateDataStreamType(this.sectionData, esInfoLength);
                    } else {
                        this.sectionData.skipBytes(esInfoLength);
                    }
                    remainingEntriesLength -= esInfoLength + 5;
                    if ((TsExtractor.this.workaroundFlags & 16) != 0) {
                        trackId = streamType;
                    } else {
                        trackId = elementaryPid;
                    }
                    if (!TsExtractor.this.trackIds.get(trackId)) {
                        switch (streamType) {
                            case 2:
                                pesPayloadReader = new H262Reader(output.track(trackId));
                                break;
                            case 3:
                                pesPayloadReader = new MpegAudioReader(output.track(trackId));
                                break;
                            case 4:
                                pesPayloadReader = new MpegAudioReader(output.track(trackId));
                                break;
                            case 15:
                                if ((TsExtractor.this.workaroundFlags & 2) == 0) {
                                    pesPayloadReader = new AdtsReader(output.track(trackId), new DummyTrackOutput());
                                    break;
                                } else {
                                    pesPayloadReader = null;
                                    break;
                                }
                            case 21:
                                if ((TsExtractor.this.workaroundFlags & 16) == 0) {
                                    pesPayloadReader = new Id3Reader(output.track(TsExtractor.access$208(TsExtractor.this)));
                                    break;
                                } else {
                                    pesPayloadReader = TsExtractor.this.id3Reader;
                                    break;
                                }
                            case 27:
                                if ((TsExtractor.this.workaroundFlags & 4) == 0) {
                                    pesPayloadReader = new H264Reader(output.track(trackId), new SeiReader(output.track(TsExtractor.access$208(TsExtractor.this))), (TsExtractor.this.workaroundFlags & 1) != 0, (TsExtractor.this.workaroundFlags & 8) != 0);
                                    break;
                                } else {
                                    pesPayloadReader = null;
                                    break;
                                }
                            case 36:
                                pesPayloadReader = new H265Reader(output.track(trackId), new SeiReader(output.track(TsExtractor.access$208(TsExtractor.this))));
                                break;
                            case 129:
                                pesPayloadReader = new Ac3Reader(output.track(trackId), false);
                                break;
                            case 130:
                            case 138:
                                pesPayloadReader = new DtsReader(output.track(trackId));
                                break;
                            case 135:
                                pesPayloadReader = new Ac3Reader(output.track(trackId), true);
                                break;
                            default:
                                pesPayloadReader = null;
                                break;
                        }
                        if (pesPayloadReader != null) {
                            TsExtractor.this.trackIds.put(trackId, true);
                            TsExtractor.this.tsPayloadReaders.put(elementaryPid, new PesReader(pesPayloadReader, TsExtractor.this.ptsTimestampAdjuster));
                        }
                    }
                }
                output.endTracks();
            }
        }

        private int readPrivateDataStreamType(ParsableByteArray data, int length) {
            int streamType = -1;
            int descriptorsEndPosition = data.getPosition() + length;
            while (true) {
                if (data.getPosition() >= descriptorsEndPosition) {
                    break;
                }
                int descriptorTag = data.readUnsignedByte();
                int descriptorLength = data.readUnsignedByte();
                if (descriptorTag == 5) {
                    long formatIdentifier = data.readUnsignedInt();
                    if (formatIdentifier == TsExtractor.AC3_FORMAT_IDENTIFIER) {
                        streamType = 129;
                    } else if (formatIdentifier == TsExtractor.E_AC3_FORMAT_IDENTIFIER) {
                        streamType = 135;
                    } else if (formatIdentifier == TsExtractor.HEVC_FORMAT_IDENTIFIER) {
                        streamType = 36;
                    }
                } else {
                    if (descriptorTag == 106) {
                        streamType = 129;
                    } else if (descriptorTag == 122) {
                        streamType = 135;
                    } else if (descriptorTag == 123) {
                        streamType = 138;
                    }
                    data.skipBytes(descriptorLength);
                }
            }
            data.setPosition(descriptorsEndPosition);
            return streamType;
        }
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.TsExtractor$PesReader */
    private static final class PesReader extends TsPayloadReader {
        private static final int HEADER_SIZE = 9;
        private static final int MAX_HEADER_EXTENSION_SIZE = 10;
        private static final int PES_SCRATCH_SIZE = 10;
        private static final int STATE_FINDING_HEADER = 0;
        private static final int STATE_READING_BODY = 3;
        private static final int STATE_READING_HEADER = 1;
        private static final int STATE_READING_HEADER_EXTENSION = 2;
        private int bytesRead;
        private boolean dataAlignmentIndicator;
        private boolean dtsFlag;
        private int extendedHeaderLength;
        private int payloadSize;
        private final ElementaryStreamReader pesPayloadReader;
        private final ParsableBitArray pesScratch = new ParsableBitArray(new byte[10]);
        private boolean ptsFlag;
        private final PtsTimestampAdjuster ptsTimestampAdjuster;
        private boolean seenFirstDts;
        private int state = 0;
        private long timeUs;

        public PesReader(ElementaryStreamReader pesPayloadReader2, PtsTimestampAdjuster ptsTimestampAdjuster2) {
            super();
            this.pesPayloadReader = pesPayloadReader2;
            this.ptsTimestampAdjuster = ptsTimestampAdjuster2;
        }

        public void seek() {
            this.state = 0;
            this.bytesRead = 0;
            this.seenFirstDts = false;
            this.pesPayloadReader.seek();
        }

        public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator, ExtractorOutput output) {
            int padding;
            if (payloadUnitStartIndicator) {
                switch (this.state) {
                    case 2:
                        Log.w(TsExtractor.TAG, "Unexpected start indicator reading extended header");
                        break;
                    case 3:
                        if (this.payloadSize != -1) {
                            Log.w(TsExtractor.TAG, "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
                        }
                        this.pesPayloadReader.packetFinished();
                        break;
                }
                setState(1);
            }
            while (data.bytesLeft() > 0) {
                switch (this.state) {
                    case 0:
                        data.skipBytes(data.bytesLeft());
                        break;
                    case 1:
                        if (continueRead(data, this.pesScratch.data, 9)) {
                            setState(parseHeader() ? 2 : 0);
                            break;
                        } else {
                            break;
                        }
                    case 2:
                        if (continueRead(data, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) && continueRead(data, (byte[]) null, this.extendedHeaderLength)) {
                            parseHeaderExtension();
                            this.pesPayloadReader.packetStarted(this.timeUs, this.dataAlignmentIndicator);
                            setState(3);
                            break;
                        }
                    case 3:
                        int readLength = data.bytesLeft();
                        if (this.payloadSize == -1) {
                            padding = 0;
                        } else {
                            padding = readLength - this.payloadSize;
                        }
                        if (padding > 0) {
                            readLength -= padding;
                            data.setLimit(data.getPosition() + readLength);
                        }
                        this.pesPayloadReader.consume(data);
                        if (this.payloadSize == -1) {
                            break;
                        } else {
                            this.payloadSize -= readLength;
                            if (this.payloadSize != 0) {
                                break;
                            } else {
                                this.pesPayloadReader.packetFinished();
                                setState(1);
                                break;
                            }
                        }
                }
            }
        }

        private void setState(int state2) {
            this.state = state2;
            this.bytesRead = 0;
        }

        private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
            int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
            if (bytesToRead <= 0) {
                return true;
            }
            if (target == null) {
                source.skipBytes(bytesToRead);
            } else {
                source.readBytes(target, this.bytesRead, bytesToRead);
            }
            this.bytesRead += bytesToRead;
            if (this.bytesRead != targetLength) {
                return false;
            }
            return true;
        }

        private boolean parseHeader() {
            this.pesScratch.setPosition(0);
            int startCodePrefix = this.pesScratch.readBits(24);
            if (startCodePrefix != 1) {
                Log.w(TsExtractor.TAG, "Unexpected start code prefix: " + startCodePrefix);
                this.payloadSize = -1;
                return false;
            }
            this.pesScratch.skipBits(8);
            int packetLength = this.pesScratch.readBits(16);
            this.pesScratch.skipBits(5);
            this.dataAlignmentIndicator = this.pesScratch.readBit();
            this.pesScratch.skipBits(2);
            this.ptsFlag = this.pesScratch.readBit();
            this.dtsFlag = this.pesScratch.readBit();
            this.pesScratch.skipBits(6);
            this.extendedHeaderLength = this.pesScratch.readBits(8);
            if (packetLength == 0) {
                this.payloadSize = -1;
            } else {
                this.payloadSize = ((packetLength + 6) - 9) - this.extendedHeaderLength;
            }
            return true;
        }

        private void parseHeaderExtension() {
            this.pesScratch.setPosition(0);
            this.timeUs = -1;
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
