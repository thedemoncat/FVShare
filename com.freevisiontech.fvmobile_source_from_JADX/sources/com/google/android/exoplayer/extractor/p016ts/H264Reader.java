package com.google.android.exoplayer.extractor.p016ts;

import android.util.SparseArray;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.google.android.exoplayer.extractor.ts.H264Reader */
final class H264Reader extends ElementaryStreamReader {
    private static final int NAL_UNIT_TYPE_PPS = 8;
    private static final int NAL_UNIT_TYPE_SEI = 6;
    private static final int NAL_UNIT_TYPE_SPS = 7;
    private boolean hasOutputFormat;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps;
    private final boolean[] prefixFlags = new boolean[3];
    private final SampleReader sampleReader;
    private final NalUnitTargetBuffer sei;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps;
    private long totalBytesWritten;

    public H264Reader(TrackOutput output, SeiReader seiReader2, boolean allowNonIdrKeyframes, boolean detectAccessUnits) {
        super(output);
        this.seiReader = seiReader2;
        this.sampleReader = new SampleReader(output, allowNonIdrKeyframes, detectAccessUnits);
        this.sps = new NalUnitTargetBuffer(7, 128);
        this.pps = new NalUnitTargetBuffer(8, 128);
        this.sei = new NalUnitTargetBuffer(6, 128);
        this.seiWrapper = new ParsableByteArray();
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.sps.reset();
        this.pps.reset();
        this.sei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0;
    }

    public void packetStarted(long pesTimeUs2, boolean dataAlignmentIndicator) {
        this.pesTimeUs = pesTimeUs2;
    }

    public void consume(ParsableByteArray data) {
        if (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += (long) data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            while (true) {
                int nalUnitOffset = NalUnitUtil.findNalUnit(dataArray, offset, limit, this.prefixFlags);
                if (nalUnitOffset == limit) {
                    nalUnitData(dataArray, offset, limit);
                    return;
                }
                int nalUnitType = NalUnitUtil.getNalUnitType(dataArray, nalUnitOffset);
                int lengthToNalUnit = nalUnitOffset - offset;
                if (lengthToNalUnit > 0) {
                    nalUnitData(dataArray, offset, nalUnitOffset);
                }
                int bytesWrittenPastPosition = limit - nalUnitOffset;
                long absolutePosition = this.totalBytesWritten - ((long) bytesWrittenPastPosition);
                endNalUnit(absolutePosition, bytesWrittenPastPosition, lengthToNalUnit < 0 ? -lengthToNalUnit : 0, this.pesTimeUs);
                startNalUnit(absolutePosition, nalUnitType, this.pesTimeUs);
                offset = nalUnitOffset + 3;
            }
        }
    }

    public void packetFinished() {
    }

    private void startNalUnit(long position, int nalUnitType, long pesTimeUs2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.startNalUnit(nalUnitType);
            this.pps.startNalUnit(nalUnitType);
        }
        this.sei.startNalUnit(nalUnitType);
        this.sampleReader.startNalUnit(position, nalUnitType, pesTimeUs2);
    }

    private void nalUnitData(byte[] dataArray, int offset, int limit) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.appendToNalUnit(dataArray, offset, limit);
            this.pps.appendToNalUnit(dataArray, offset, limit);
        }
        this.sei.appendToNalUnit(dataArray, offset, limit);
        this.sampleReader.appendToNalUnit(dataArray, offset, limit);
    }

    private void endNalUnit(long position, int offset, int discardPadding, long pesTimeUs2) {
        if (!this.hasOutputFormat || this.sampleReader.needsSpsPps()) {
            this.sps.endNalUnit(discardPadding);
            this.pps.endNalUnit(discardPadding);
            if (!this.hasOutputFormat) {
                if (this.sps.isCompleted() && this.pps.isCompleted()) {
                    List<byte[]> initializationData = new ArrayList<>();
                    initializationData.add(Arrays.copyOf(this.sps.nalData, this.sps.nalLength));
                    initializationData.add(Arrays.copyOf(this.pps.nalData, this.pps.nalLength));
                    NalUnitUtil.SpsData spsData = NalUnitUtil.parseSpsNalUnit(unescape(this.sps));
                    NalUnitUtil.PpsData ppsData = NalUnitUtil.parsePpsNalUnit(unescape(this.pps));
                    this.output.format(MediaFormat.createVideoFormat((String) null, MimeTypes.VIDEO_H264, -1, -1, -1, spsData.width, spsData.height, initializationData, -1, spsData.pixelWidthAspectRatio));
                    this.hasOutputFormat = true;
                    this.sampleReader.putSps(spsData);
                    this.sampleReader.putPps(ppsData);
                    this.sps.reset();
                    this.pps.reset();
                }
            } else if (this.sps.isCompleted()) {
                this.sampleReader.putSps(NalUnitUtil.parseSpsNalUnit(unescape(this.sps)));
                this.sps.reset();
            } else if (this.pps.isCompleted()) {
                this.sampleReader.putPps(NalUnitUtil.parsePpsNalUnit(unescape(this.pps)));
                this.pps.reset();
            }
        }
        if (this.sei.endNalUnit(discardPadding)) {
            this.seiWrapper.reset(this.sei.nalData, NalUnitUtil.unescapeStream(this.sei.nalData, this.sei.nalLength));
            this.seiWrapper.setPosition(4);
            this.seiReader.consume(pesTimeUs2, this.seiWrapper);
        }
        this.sampleReader.endNalUnit(position, offset);
    }

    private static ParsableBitArray unescape(NalUnitTargetBuffer buffer) {
        ParsableBitArray bitArray = new ParsableBitArray(buffer.nalData, NalUnitUtil.unescapeStream(buffer.nalData, buffer.nalLength));
        bitArray.skipBits(32);
        return bitArray;
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.H264Reader$SampleReader */
    private static final class SampleReader {
        private static final int DEFAULT_BUFFER_SIZE = 128;
        private static final int NAL_UNIT_TYPE_AUD = 9;
        private static final int NAL_UNIT_TYPE_IDR = 5;
        private static final int NAL_UNIT_TYPE_NON_IDR = 1;
        private static final int NAL_UNIT_TYPE_PARTITION_A = 2;
        private final boolean allowNonIdrKeyframes;
        private byte[] buffer = new byte[128];
        private int bufferLength;
        private final boolean detectAccessUnits;
        private boolean isFilling;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private int nalUnitType;
        private final TrackOutput output;
        private final SparseArray<NalUnitUtil.PpsData> pps = new SparseArray<>();
        private SliceHeaderData previousSliceHeader = new SliceHeaderData();
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private final ParsableBitArray scratch = new ParsableBitArray();
        private SliceHeaderData sliceHeader = new SliceHeaderData();
        private final SparseArray<NalUnitUtil.SpsData> sps = new SparseArray<>();

        public SampleReader(TrackOutput output2, boolean allowNonIdrKeyframes2, boolean detectAccessUnits2) {
            this.output = output2;
            this.allowNonIdrKeyframes = allowNonIdrKeyframes2;
            this.detectAccessUnits = detectAccessUnits2;
            reset();
        }

        public boolean needsSpsPps() {
            return this.detectAccessUnits;
        }

        public void putSps(NalUnitUtil.SpsData spsData) {
            this.sps.append(spsData.seqParameterSetId, spsData);
        }

        public void putPps(NalUnitUtil.PpsData ppsData) {
            this.pps.append(ppsData.picParameterSetId, ppsData);
        }

        public void reset() {
            this.isFilling = false;
            this.readingSample = false;
            this.sliceHeader.clear();
        }

        public void startNalUnit(long position, int type, long pesTimeUs) {
            this.nalUnitType = type;
            this.nalUnitTimeUs = pesTimeUs;
            this.nalUnitStartPosition = position;
            if (!this.allowNonIdrKeyframes || this.nalUnitType != 1) {
                if (!this.detectAccessUnits) {
                    return;
                }
                if (!(this.nalUnitType == 5 || this.nalUnitType == 1 || this.nalUnitType == 2)) {
                    return;
                }
            }
            SliceHeaderData newSliceHeader = this.previousSliceHeader;
            this.previousSliceHeader = this.sliceHeader;
            this.sliceHeader = newSliceHeader;
            this.sliceHeader.clear();
            this.bufferLength = 0;
            this.isFilling = true;
        }

        public void appendToNalUnit(byte[] data, int offset, int limit) {
            if (this.isFilling) {
                int readLength = limit - offset;
                if (this.buffer.length < this.bufferLength + readLength) {
                    this.buffer = Arrays.copyOf(this.buffer, (this.bufferLength + readLength) * 2);
                }
                System.arraycopy(data, offset, this.buffer, this.bufferLength, readLength);
                this.bufferLength += readLength;
                this.scratch.reset(this.buffer, this.bufferLength);
                if (this.scratch.bitsLeft() >= 8) {
                    this.scratch.skipBits(1);
                    int nalRefIdc = this.scratch.readBits(2);
                    this.scratch.skipBits(5);
                    if (this.scratch.canReadExpGolombCodedNum()) {
                        this.scratch.readUnsignedExpGolombCodedInt();
                        if (this.scratch.canReadExpGolombCodedNum()) {
                            int sliceType = this.scratch.readUnsignedExpGolombCodedInt();
                            if (!this.detectAccessUnits) {
                                this.isFilling = false;
                                this.sliceHeader.setSliceType(sliceType);
                            } else if (this.scratch.canReadExpGolombCodedNum()) {
                                int picParameterSetId = this.scratch.readUnsignedExpGolombCodedInt();
                                if (this.pps.indexOfKey(picParameterSetId) < 0) {
                                    this.isFilling = false;
                                    return;
                                }
                                NalUnitUtil.PpsData ppsData = this.pps.get(picParameterSetId);
                                NalUnitUtil.SpsData spsData = this.sps.get(ppsData.seqParameterSetId);
                                if (spsData.separateColorPlaneFlag) {
                                    if (this.scratch.bitsLeft() >= 2) {
                                        this.scratch.skipBits(2);
                                    } else {
                                        return;
                                    }
                                }
                                if (this.scratch.bitsLeft() >= spsData.frameNumLength) {
                                    boolean fieldPicFlag = false;
                                    boolean bottomFieldFlagPresent = false;
                                    boolean bottomFieldFlag = false;
                                    int frameNum = this.scratch.readBits(spsData.frameNumLength);
                                    if (!spsData.frameMbsOnlyFlag) {
                                        if (this.scratch.bitsLeft() >= 1) {
                                            fieldPicFlag = this.scratch.readBit();
                                            if (fieldPicFlag) {
                                                if (this.scratch.bitsLeft() >= 1) {
                                                    bottomFieldFlag = this.scratch.readBit();
                                                    bottomFieldFlagPresent = true;
                                                } else {
                                                    return;
                                                }
                                            }
                                        } else {
                                            return;
                                        }
                                    }
                                    boolean idrPicFlag = this.nalUnitType == 5;
                                    int idrPicId = 0;
                                    if (idrPicFlag) {
                                        if (this.scratch.canReadExpGolombCodedNum()) {
                                            idrPicId = this.scratch.readUnsignedExpGolombCodedInt();
                                        } else {
                                            return;
                                        }
                                    }
                                    int picOrderCntLsb = 0;
                                    int deltaPicOrderCntBottom = 0;
                                    int deltaPicOrderCnt0 = 0;
                                    int deltaPicOrderCnt1 = 0;
                                    if (spsData.picOrderCountType == 0) {
                                        if (this.scratch.bitsLeft() >= spsData.picOrderCntLsbLength) {
                                            picOrderCntLsb = this.scratch.readBits(spsData.picOrderCntLsbLength);
                                            if (ppsData.bottomFieldPicOrderInFramePresentFlag && !fieldPicFlag) {
                                                if (this.scratch.canReadExpGolombCodedNum()) {
                                                    deltaPicOrderCntBottom = this.scratch.readSignedExpGolombCodedInt();
                                                } else {
                                                    return;
                                                }
                                            }
                                        } else {
                                            return;
                                        }
                                    } else if (spsData.picOrderCountType == 1 && !spsData.deltaPicOrderAlwaysZeroFlag) {
                                        if (this.scratch.canReadExpGolombCodedNum()) {
                                            deltaPicOrderCnt0 = this.scratch.readSignedExpGolombCodedInt();
                                            if (ppsData.bottomFieldPicOrderInFramePresentFlag && !fieldPicFlag) {
                                                if (this.scratch.canReadExpGolombCodedNum()) {
                                                    deltaPicOrderCnt1 = this.scratch.readSignedExpGolombCodedInt();
                                                } else {
                                                    return;
                                                }
                                            }
                                        } else {
                                            return;
                                        }
                                    }
                                    this.sliceHeader.setAll(spsData, nalRefIdc, sliceType, frameNum, picParameterSetId, fieldPicFlag, bottomFieldFlagPresent, bottomFieldFlag, idrPicFlag, idrPicId, picOrderCntLsb, deltaPicOrderCntBottom, deltaPicOrderCnt0, deltaPicOrderCnt1);
                                    this.isFilling = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        public void endNalUnit(long position, int offset) {
            boolean z = false;
            if (this.nalUnitType == 9 || (this.detectAccessUnits && this.sliceHeader.isFirstVclNalUnitOfPicture(this.previousSliceHeader))) {
                if (this.readingSample) {
                    outputSample(offset + ((int) (position - this.nalUnitStartPosition)));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.sampleIsKeyframe = false;
                this.readingSample = true;
            }
            boolean z2 = this.sampleIsKeyframe;
            if (this.nalUnitType == 5 || (this.allowNonIdrKeyframes && this.nalUnitType == 1 && this.sliceHeader.isISlice())) {
                z = true;
            }
            this.sampleIsKeyframe = z | z2;
        }

        private void outputSample(int offset) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe ? 1 : 0, (int) (this.nalUnitStartPosition - this.samplePosition), offset, (byte[]) null);
        }

        /* renamed from: com.google.android.exoplayer.extractor.ts.H264Reader$SampleReader$SliceHeaderData */
        private static final class SliceHeaderData {
            private static final int SLICE_TYPE_ALL_I = 7;
            private static final int SLICE_TYPE_I = 2;
            private boolean bottomFieldFlag;
            private boolean bottomFieldFlagPresent;
            private int deltaPicOrderCnt0;
            private int deltaPicOrderCnt1;
            private int deltaPicOrderCntBottom;
            private boolean fieldPicFlag;
            private int frameNum;
            private boolean hasSliceType;
            private boolean idrPicFlag;
            private int idrPicId;
            private boolean isComplete;
            private int nalRefIdc;
            private int picOrderCntLsb;
            private int picParameterSetId;
            private int sliceType;
            private NalUnitUtil.SpsData spsData;

            private SliceHeaderData() {
            }

            public void clear() {
                this.hasSliceType = false;
                this.isComplete = false;
            }

            public void setSliceType(int sliceType2) {
                this.sliceType = sliceType2;
                this.hasSliceType = true;
            }

            public void setAll(NalUnitUtil.SpsData spsData2, int nalRefIdc2, int sliceType2, int frameNum2, int picParameterSetId2, boolean fieldPicFlag2, boolean bottomFieldFlagPresent2, boolean bottomFieldFlag2, boolean idrPicFlag2, int idrPicId2, int picOrderCntLsb2, int deltaPicOrderCntBottom2, int deltaPicOrderCnt02, int deltaPicOrderCnt12) {
                this.spsData = spsData2;
                this.nalRefIdc = nalRefIdc2;
                this.sliceType = sliceType2;
                this.frameNum = frameNum2;
                this.picParameterSetId = picParameterSetId2;
                this.fieldPicFlag = fieldPicFlag2;
                this.bottomFieldFlagPresent = bottomFieldFlagPresent2;
                this.bottomFieldFlag = bottomFieldFlag2;
                this.idrPicFlag = idrPicFlag2;
                this.idrPicId = idrPicId2;
                this.picOrderCntLsb = picOrderCntLsb2;
                this.deltaPicOrderCntBottom = deltaPicOrderCntBottom2;
                this.deltaPicOrderCnt0 = deltaPicOrderCnt02;
                this.deltaPicOrderCnt1 = deltaPicOrderCnt12;
                this.isComplete = true;
                this.hasSliceType = true;
            }

            public boolean isISlice() {
                return this.hasSliceType && (this.sliceType == 7 || this.sliceType == 2);
            }

            /* access modifiers changed from: private */
            public boolean isFirstVclNalUnitOfPicture(SliceHeaderData other) {
                if (this.isComplete) {
                    if (!other.isComplete || this.frameNum != other.frameNum || this.picParameterSetId != other.picParameterSetId || this.fieldPicFlag != other.fieldPicFlag) {
                        return true;
                    }
                    if (this.bottomFieldFlagPresent && other.bottomFieldFlagPresent && this.bottomFieldFlag != other.bottomFieldFlag) {
                        return true;
                    }
                    if (this.nalRefIdc != other.nalRefIdc && (this.nalRefIdc == 0 || other.nalRefIdc == 0)) {
                        return true;
                    }
                    if (this.spsData.picOrderCountType == 0 && other.spsData.picOrderCountType == 0 && (this.picOrderCntLsb != other.picOrderCntLsb || this.deltaPicOrderCntBottom != other.deltaPicOrderCntBottom)) {
                        return true;
                    }
                    if ((this.spsData.picOrderCountType != 1 || other.spsData.picOrderCountType != 1 || (this.deltaPicOrderCnt0 == other.deltaPicOrderCnt0 && this.deltaPicOrderCnt1 == other.deltaPicOrderCnt1)) && this.idrPicFlag == other.idrPicFlag) {
                        return this.idrPicFlag && other.idrPicFlag && this.idrPicId != other.idrPicId;
                    }
                    return true;
                }
            }
        }
    }
}
