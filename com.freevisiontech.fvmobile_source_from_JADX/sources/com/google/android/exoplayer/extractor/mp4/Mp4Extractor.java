package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.GaplessInfo;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.mp4.Atom;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public final class Mp4Extractor implements Extractor, SeekMap {
    private static final int BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
    private static final long RELOAD_MINIMUM_SEEK_DISTANCE = 262144;
    private static final int STATE_AFTER_SEEK = 0;
    private static final int STATE_READING_ATOM_HEADER = 1;
    private static final int STATE_READING_ATOM_PAYLOAD = 2;
    private static final int STATE_READING_SAMPLE = 3;
    private ParsableByteArray atomData;
    private final ParsableByteArray atomHeader = new ParsableByteArray(16);
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private final Stack<Atom.ContainerAtom> containerAtoms = new Stack<>();
    private ExtractorOutput extractorOutput;
    private boolean isQuickTime;
    private final ParsableByteArray nalLength = new ParsableByteArray(4);
    private final ParsableByteArray nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
    private int parserState;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private int sampleSize;
    private Mp4Track[] tracks;

    public Mp4Extractor() {
        enterReadingAtomHeaderState();
    }

    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException {
        return Sniffer.sniffUnfragmented(input);
    }

    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    public void seek() {
        this.containerAtoms.clear();
        this.atomHeaderBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.parserState = 0;
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        while (true) {
            switch (this.parserState) {
                case 0:
                    if (input.getPosition() != 0) {
                        this.parserState = 3;
                        break;
                    } else {
                        enterReadingAtomHeaderState();
                        break;
                    }
                case 1:
                    if (readAtomHeader(input)) {
                        break;
                    } else {
                        return -1;
                    }
                case 2:
                    if (!readAtomPayload(input, seekPosition)) {
                        break;
                    } else {
                        return 1;
                    }
                default:
                    return readSample(input, seekPosition);
            }
        }
    }

    public boolean isSeekable() {
        return true;
    }

    public long getPosition(long timeUs) {
        long earliestSamplePosition = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < this.tracks.length; trackIndex++) {
            TrackSampleTable sampleTable = this.tracks[trackIndex].sampleTable;
            int sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(timeUs);
            if (sampleIndex == -1) {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(timeUs);
            }
            this.tracks[trackIndex].sampleIndex = sampleIndex;
            long offset = sampleTable.offsets[sampleIndex];
            if (offset < earliestSamplePosition) {
                earliestSamplePosition = offset;
            }
        }
        return earliestSamplePosition;
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 1;
        this.atomHeaderBytesRead = 0;
    }

    private boolean readAtomHeader(ExtractorInput input) throws IOException, InterruptedException {
        boolean z;
        boolean z2;
        if (this.atomHeaderBytesRead == 0) {
            if (!input.readFully(this.atomHeader.data, 0, 8, true)) {
                return false;
            }
            this.atomHeaderBytesRead = 8;
            this.atomHeader.setPosition(0);
            this.atomSize = this.atomHeader.readUnsignedInt();
            this.atomType = this.atomHeader.readInt();
        }
        if (this.atomSize == 1) {
            input.readFully(this.atomHeader.data, 8, 8);
            this.atomHeaderBytesRead += 8;
            this.atomSize = this.atomHeader.readUnsignedLongToLong();
        }
        if (shouldParseContainerAtom(this.atomType)) {
            long endPosition = (input.getPosition() + this.atomSize) - ((long) this.atomHeaderBytesRead);
            this.containerAtoms.add(new Atom.ContainerAtom(this.atomType, endPosition));
            if (this.atomSize == ((long) this.atomHeaderBytesRead)) {
                processAtomEnded(endPosition);
            } else {
                enterReadingAtomHeaderState();
            }
        } else if (shouldParseLeafAtom(this.atomType)) {
            if (this.atomHeaderBytesRead == 8) {
                z = true;
            } else {
                z = false;
            }
            Assertions.checkState(z);
            if (this.atomSize <= 2147483647L) {
                z2 = true;
            } else {
                z2 = false;
            }
            Assertions.checkState(z2);
            this.atomData = new ParsableByteArray((int) this.atomSize);
            System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
            this.parserState = 2;
        } else {
            this.atomData = null;
            this.parserState = 2;
        }
        return true;
    }

    private boolean readAtomPayload(ExtractorInput input, PositionHolder positionHolder) throws IOException, InterruptedException {
        long atomPayloadSize = this.atomSize - ((long) this.atomHeaderBytesRead);
        long atomEndPosition = input.getPosition() + atomPayloadSize;
        boolean seekRequired = false;
        if (this.atomData != null) {
            input.readFully(this.atomData.data, this.atomHeaderBytesRead, (int) atomPayloadSize);
            if (this.atomType == Atom.TYPE_ftyp) {
                this.isQuickTime = processFtypAtom(this.atomData);
            } else if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(new Atom.LeafAtom(this.atomType, this.atomData));
            }
        } else if (atomPayloadSize < 262144) {
            input.skipFully((int) atomPayloadSize);
        } else {
            positionHolder.position = input.getPosition() + atomPayloadSize;
            seekRequired = true;
        }
        processAtomEnded(atomEndPosition);
        if (!seekRequired || this.parserState == 3) {
            return false;
        }
        return true;
    }

    private void processAtomEnded(long atomEndPosition) throws ParserException {
        while (!this.containerAtoms.isEmpty() && this.containerAtoms.peek().endPosition == atomEndPosition) {
            Atom.ContainerAtom containerAtom = this.containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov) {
                processMoovAtom(containerAtom);
                this.containerAtoms.clear();
                this.parserState = 3;
            } else if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(containerAtom);
            }
        }
        if (this.parserState != 3) {
            enterReadingAtomHeaderState();
        }
    }

    private static boolean processFtypAtom(ParsableByteArray atomData2) {
        atomData2.setPosition(8);
        if (atomData2.readInt() == BRAND_QUICKTIME) {
            return true;
        }
        atomData2.skipBytes(4);
        while (atomData2.bytesLeft() > 0) {
            if (atomData2.readInt() == BRAND_QUICKTIME) {
                return true;
            }
        }
        return false;
    }

    private void processMoovAtom(Atom.ContainerAtom moov) throws ParserException {
        Track track;
        ArrayList arrayList = new ArrayList();
        long earliestSampleOffset = Long.MAX_VALUE;
        GaplessInfo gaplessInfo = null;
        Atom.LeafAtom udta = moov.getLeafAtomOfType(Atom.TYPE_udta);
        if (udta != null) {
            gaplessInfo = AtomParsers.parseUdta(udta, this.isQuickTime);
        }
        for (int i = 0; i < moov.containerChildren.size(); i++) {
            Atom.ContainerAtom atom = moov.containerChildren.get(i);
            if (atom.type == Atom.TYPE_trak && (track = AtomParsers.parseTrak(atom, moov.getLeafAtomOfType(Atom.TYPE_mvhd), -1, this.isQuickTime)) != null) {
                TrackSampleTable trackSampleTable = AtomParsers.parseStbl(track, atom.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl));
                if (trackSampleTable.sampleCount != 0) {
                    Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, this.extractorOutput.track(i));
                    MediaFormat mediaFormat = track.mediaFormat.copyWithMaxInputSize(trackSampleTable.maximumSize + 30);
                    if (gaplessInfo != null) {
                        mediaFormat = mediaFormat.copyWithGaplessInfo(gaplessInfo.encoderDelay, gaplessInfo.encoderPadding);
                    }
                    mp4Track.trackOutput.format(mediaFormat);
                    arrayList.add(mp4Track);
                    long firstSampleOffset = trackSampleTable.offsets[0];
                    if (firstSampleOffset < earliestSampleOffset) {
                        earliestSampleOffset = firstSampleOffset;
                    }
                }
            }
        }
        this.tracks = (Mp4Track[]) arrayList.toArray(new Mp4Track[0]);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
    }

    private int readSample(ExtractorInput input, PositionHolder positionHolder) throws IOException, InterruptedException {
        int trackIndex = getTrackIndexOfEarliestCurrentSample();
        if (trackIndex == -1) {
            return -1;
        }
        Mp4Track track = this.tracks[trackIndex];
        TrackOutput trackOutput = track.trackOutput;
        int sampleIndex = track.sampleIndex;
        long position = track.sampleTable.offsets[sampleIndex];
        long skipAmount = (position - input.getPosition()) + ((long) this.sampleBytesWritten);
        if (skipAmount < 0 || skipAmount >= 262144) {
            positionHolder.position = position;
            return 1;
        }
        input.skipFully((int) skipAmount);
        this.sampleSize = track.sampleTable.sizes[sampleIndex];
        if (track.track.nalUnitLengthFieldLength != -1) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.track.nalUnitLengthFieldLength;
            while (this.sampleBytesWritten < this.sampleSize) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    input.readFully(this.nalLength.data, nalUnitLengthFieldLengthDiff, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    trackOutput.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                    this.sampleSize += nalUnitLengthFieldLengthDiff;
                } else {
                    int writtenBytes = trackOutput.sampleData(input, this.sampleCurrentNalBytesRemaining, false);
                    this.sampleBytesWritten += writtenBytes;
                    this.sampleCurrentNalBytesRemaining -= writtenBytes;
                }
            }
        } else {
            while (this.sampleBytesWritten < this.sampleSize) {
                int writtenBytes2 = trackOutput.sampleData(input, this.sampleSize - this.sampleBytesWritten, false);
                this.sampleBytesWritten += writtenBytes2;
                this.sampleCurrentNalBytesRemaining -= writtenBytes2;
            }
        }
        trackOutput.sampleMetadata(track.sampleTable.timestampsUs[sampleIndex], track.sampleTable.flags[sampleIndex], this.sampleSize, 0, (byte[]) null);
        track.sampleIndex++;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        return 0;
    }

    private int getTrackIndexOfEarliestCurrentSample() {
        int earliestSampleTrackIndex = -1;
        long earliestSampleOffset = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < this.tracks.length; trackIndex++) {
            Mp4Track track = this.tracks[trackIndex];
            int sampleIndex = track.sampleIndex;
            if (sampleIndex != track.sampleTable.sampleCount) {
                long trackSampleOffset = track.sampleTable.offsets[sampleIndex];
                if (trackSampleOffset < earliestSampleOffset) {
                    earliestSampleOffset = trackSampleOffset;
                    earliestSampleTrackIndex = trackIndex;
                }
            }
        }
        return earliestSampleTrackIndex;
    }

    private static boolean shouldParseLeafAtom(int atom) {
        return atom == Atom.TYPE_mdhd || atom == Atom.TYPE_mvhd || atom == Atom.TYPE_hdlr || atom == Atom.TYPE_stsd || atom == Atom.TYPE_stts || atom == Atom.TYPE_stss || atom == Atom.TYPE_ctts || atom == Atom.TYPE_elst || atom == Atom.TYPE_stsc || atom == Atom.TYPE_stsz || atom == Atom.TYPE_stz2 || atom == Atom.TYPE_stco || atom == Atom.TYPE_co64 || atom == Atom.TYPE_tkhd || atom == Atom.TYPE_ftyp || atom == Atom.TYPE_udta;
    }

    private static boolean shouldParseContainerAtom(int atom) {
        return atom == Atom.TYPE_moov || atom == Atom.TYPE_trak || atom == Atom.TYPE_mdia || atom == Atom.TYPE_minf || atom == Atom.TYPE_stbl || atom == Atom.TYPE_edts;
    }

    private static final class Mp4Track {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;

        public Mp4Track(Track track2, TrackSampleTable sampleTable2, TrackOutput trackOutput2) {
            this.track = track2;
            this.sampleTable = sampleTable2;
            this.trackOutput = trackOutput2;
        }
    }
}
