package com.google.android.exoplayer.extractor.ogg;

import com.google.android.exoplayer.C1907C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.ogg.VorbisUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;
import java.util.ArrayList;

final class VorbisReader extends StreamReader implements SeekMap {
    private static final long LARGEST_EXPECTED_PAGE_SIZE = 8000;
    private long audioStartPosition;
    private VorbisUtil.CommentHeader commentHeader;
    private long duration;
    private long elapsedSamples;
    private long inputLength;
    private final OggSeeker oggSeeker = new OggSeeker();
    private int previousPacketBlockSize;
    private boolean seenFirstAudioPacket;
    private long targetGranule = -1;
    private long totalSamples;
    private VorbisUtil.VorbisIdHeader vorbisIdHeader;
    private VorbisSetup vorbisSetup;

    VorbisReader() {
    }

    static boolean verifyBitstreamType(ParsableByteArray data) {
        try {
            return VorbisUtil.verifyVorbisHeaderCapturePattern(1, data, true);
        } catch (ParserException e) {
            return false;
        }
    }

    public void seek() {
        super.seek();
        this.previousPacketBlockSize = 0;
        this.elapsedSamples = 0;
        this.seenFirstAudioPacket = false;
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        long readGranuleOfLastPage;
        long j;
        if (this.totalSamples == 0) {
            if (this.vorbisSetup == null) {
                this.inputLength = input.getLength();
                this.vorbisSetup = readSetupHeaders(input, this.scratch);
                this.audioStartPosition = input.getPosition();
                this.extractorOutput.seekMap(this);
                if (this.inputLength != -1) {
                    seekPosition.position = Math.max(0, input.getLength() - LARGEST_EXPECTED_PAGE_SIZE);
                    return 1;
                }
            }
            if (this.inputLength == -1) {
                readGranuleOfLastPage = -1;
            } else {
                readGranuleOfLastPage = this.oggParser.readGranuleOfLastPage(input);
            }
            this.totalSamples = readGranuleOfLastPage;
            ArrayList<byte[]> codecInitialisationData = new ArrayList<>();
            codecInitialisationData.add(this.vorbisSetup.idHeader.data);
            codecInitialisationData.add(this.vorbisSetup.setupHeaderData);
            if (this.inputLength == -1) {
                j = -1;
            } else {
                j = (this.totalSamples * C1907C.MICROS_PER_SECOND) / this.vorbisSetup.idHeader.sampleRate;
            }
            this.duration = j;
            this.trackOutput.format(MediaFormat.createAudioFormat((String) null, MimeTypes.AUDIO_VORBIS, this.vorbisSetup.idHeader.bitrateNominal, 65025, this.duration, this.vorbisSetup.idHeader.channels, (int) this.vorbisSetup.idHeader.sampleRate, codecInitialisationData, (String) null));
            if (this.inputLength != -1) {
                this.oggSeeker.setup(this.inputLength - this.audioStartPosition, this.totalSamples);
                seekPosition.position = this.audioStartPosition;
                return 1;
            }
        }
        if (!this.seenFirstAudioPacket && this.targetGranule > -1) {
            OggUtil.skipToNextPage(input);
            long position = this.oggSeeker.getNextSeekPosition(this.targetGranule, input);
            if (position != -1) {
                seekPosition.position = position;
                return 1;
            }
            this.elapsedSamples = this.oggParser.skipToPageOfGranule(input, this.targetGranule);
            this.previousPacketBlockSize = this.vorbisIdHeader.blockSize0;
            this.seenFirstAudioPacket = true;
        }
        if (!this.oggParser.readPacket(input, this.scratch)) {
            return -1;
        }
        if ((this.scratch.data[0] & 1) != 1) {
            int packetBlockSize = decodeBlockSize(this.scratch.data[0], this.vorbisSetup);
            int samplesInPacket = this.seenFirstAudioPacket ? (this.previousPacketBlockSize + packetBlockSize) / 4 : 0;
            if (this.elapsedSamples + ((long) samplesInPacket) >= this.targetGranule) {
                appendNumberOfSamples(this.scratch, (long) samplesInPacket);
                long timeUs = (this.elapsedSamples * C1907C.MICROS_PER_SECOND) / this.vorbisSetup.idHeader.sampleRate;
                this.trackOutput.sampleData(this.scratch, this.scratch.limit());
                this.trackOutput.sampleMetadata(timeUs, 1, this.scratch.limit(), 0, (byte[]) null);
                this.targetGranule = -1;
            }
            this.seenFirstAudioPacket = true;
            this.elapsedSamples += (long) samplesInPacket;
            this.previousPacketBlockSize = packetBlockSize;
        }
        this.scratch.reset();
        return 0;
    }

    /* access modifiers changed from: package-private */
    public VorbisSetup readSetupHeaders(ExtractorInput input, ParsableByteArray scratch) throws IOException, InterruptedException {
        if (this.vorbisIdHeader == null) {
            this.oggParser.readPacket(input, scratch);
            this.vorbisIdHeader = VorbisUtil.readVorbisIdentificationHeader(scratch);
            scratch.reset();
        }
        if (this.commentHeader == null) {
            this.oggParser.readPacket(input, scratch);
            this.commentHeader = VorbisUtil.readVorbisCommentHeader(scratch);
            scratch.reset();
        }
        this.oggParser.readPacket(input, scratch);
        byte[] setupHeaderData = new byte[scratch.limit()];
        System.arraycopy(scratch.data, 0, setupHeaderData, 0, scratch.limit());
        VorbisUtil.Mode[] modes = VorbisUtil.readVorbisModes(scratch, this.vorbisIdHeader.channels);
        int iLogModes = VorbisUtil.iLog(modes.length - 1);
        scratch.reset();
        return new VorbisSetup(this.vorbisIdHeader, this.commentHeader, setupHeaderData, modes, iLogModes);
    }

    static void appendNumberOfSamples(ParsableByteArray buffer, long packetSampleCount) {
        buffer.setLimit(buffer.limit() + 4);
        buffer.data[buffer.limit() - 4] = (byte) ((int) (packetSampleCount & 255));
        buffer.data[buffer.limit() - 3] = (byte) ((int) ((packetSampleCount >>> 8) & 255));
        buffer.data[buffer.limit() - 2] = (byte) ((int) ((packetSampleCount >>> 16) & 255));
        buffer.data[buffer.limit() - 1] = (byte) ((int) ((packetSampleCount >>> 24) & 255));
    }

    private static int decodeBlockSize(byte firstByteOfAudioPacket, VorbisSetup vorbisSetup2) {
        if (!vorbisSetup2.modes[OggUtil.readBits(firstByteOfAudioPacket, vorbisSetup2.iLogModes, 1)].blockFlag) {
            return vorbisSetup2.idHeader.blockSize0;
        }
        return vorbisSetup2.idHeader.blockSize1;
    }

    public boolean isSeekable() {
        return (this.vorbisSetup == null || this.inputLength == -1) ? false : true;
    }

    public long getPosition(long timeUs) {
        if (timeUs == 0) {
            this.targetGranule = -1;
            return this.audioStartPosition;
        }
        this.targetGranule = (this.vorbisSetup.idHeader.sampleRate * timeUs) / C1907C.MICROS_PER_SECOND;
        return Math.max(this.audioStartPosition, (((this.inputLength - this.audioStartPosition) * timeUs) / this.duration) - 4000);
    }

    static final class VorbisSetup {
        public final VorbisUtil.CommentHeader commentHeader;
        public final int iLogModes;
        public final VorbisUtil.VorbisIdHeader idHeader;
        public final VorbisUtil.Mode[] modes;
        public final byte[] setupHeaderData;

        public VorbisSetup(VorbisUtil.VorbisIdHeader idHeader2, VorbisUtil.CommentHeader commentHeader2, byte[] setupHeaderData2, VorbisUtil.Mode[] modes2, int iLogModes2) {
            this.idHeader = idHeader2;
            this.commentHeader = commentHeader2;
            this.setupHeaderData = setupHeaderData2;
            this.modes = modes2;
            this.iLogModes = iLogModes2;
        }
    }
}
