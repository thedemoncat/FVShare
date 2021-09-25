package com.google.android.exoplayer.extractor.p016ts;

import android.util.Log;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Collections;

/* renamed from: com.google.android.exoplayer.extractor.ts.H265Reader */
final class H265Reader extends ElementaryStreamReader {
    private static final int BLA_W_LP = 16;
    private static final int CRA_NUT = 21;
    private static final int PPS_NUT = 34;
    private static final int PREFIX_SEI_NUT = 39;
    private static final int RASL_R = 9;
    private static final int SPS_NUT = 33;
    private static final int SUFFIX_SEI_NUT = 40;
    private static final String TAG = "H265Reader";
    private static final int VPS_NUT = 32;
    private boolean hasOutputFormat;
    private long pesTimeUs;
    private final NalUnitTargetBuffer pps = new NalUnitTargetBuffer(34, 128);
    private final boolean[] prefixFlags = new boolean[3];
    private final NalUnitTargetBuffer prefixSei = new NalUnitTargetBuffer(39, 128);
    private final SampleReader sampleReader;
    private final SeiReader seiReader;
    private final ParsableByteArray seiWrapper;
    private final NalUnitTargetBuffer sps = new NalUnitTargetBuffer(33, 128);
    private final NalUnitTargetBuffer suffixSei = new NalUnitTargetBuffer(40, 128);
    private long totalBytesWritten;
    private final NalUnitTargetBuffer vps = new NalUnitTargetBuffer(32, 128);

    public H265Reader(TrackOutput output, SeiReader seiReader2) {
        super(output);
        this.seiReader = seiReader2;
        this.sampleReader = new SampleReader(output);
        this.seiWrapper = new ParsableByteArray();
    }

    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.vps.reset();
        this.sps.reset();
        this.pps.reset();
        this.prefixSei.reset();
        this.suffixSei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0;
    }

    public void packetStarted(long pesTimeUs2, boolean dataAlignmentIndicator) {
        this.pesTimeUs = pesTimeUs2;
    }

    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += (long) data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            while (true) {
                if (offset < limit) {
                    int nalUnitOffset = NalUnitUtil.findNalUnit(dataArray, offset, limit, this.prefixFlags);
                    if (nalUnitOffset == limit) {
                        nalUnitData(dataArray, offset, limit);
                        return;
                    }
                    int nalUnitType = NalUnitUtil.getH265NalUnitType(dataArray, nalUnitOffset);
                    int lengthToNalUnit = nalUnitOffset - offset;
                    if (lengthToNalUnit > 0) {
                        nalUnitData(dataArray, offset, nalUnitOffset);
                    }
                    int bytesWrittenPastPosition = limit - nalUnitOffset;
                    long absolutePosition = this.totalBytesWritten - ((long) bytesWrittenPastPosition);
                    endNalUnit(absolutePosition, bytesWrittenPastPosition, lengthToNalUnit < 0 ? -lengthToNalUnit : 0, this.pesTimeUs);
                    startNalUnit(absolutePosition, bytesWrittenPastPosition, nalUnitType, this.pesTimeUs);
                    offset = nalUnitOffset + 3;
                }
            }
        }
    }

    public void packetFinished() {
    }

    private void startNalUnit(long position, int offset, int nalUnitType, long pesTimeUs2) {
        if (this.hasOutputFormat) {
            this.sampleReader.startNalUnit(position, offset, nalUnitType, pesTimeUs2);
        } else {
            this.vps.startNalUnit(nalUnitType);
            this.sps.startNalUnit(nalUnitType);
            this.pps.startNalUnit(nalUnitType);
        }
        this.prefixSei.startNalUnit(nalUnitType);
        this.suffixSei.startNalUnit(nalUnitType);
    }

    private void nalUnitData(byte[] dataArray, int offset, int limit) {
        if (this.hasOutputFormat) {
            this.sampleReader.readNalUnitData(dataArray, offset, limit);
        } else {
            this.vps.appendToNalUnit(dataArray, offset, limit);
            this.sps.appendToNalUnit(dataArray, offset, limit);
            this.pps.appendToNalUnit(dataArray, offset, limit);
        }
        this.prefixSei.appendToNalUnit(dataArray, offset, limit);
        this.suffixSei.appendToNalUnit(dataArray, offset, limit);
    }

    private void endNalUnit(long position, int offset, int discardPadding, long pesTimeUs2) {
        if (this.hasOutputFormat) {
            this.sampleReader.endNalUnit(position, offset);
        } else {
            this.vps.endNalUnit(discardPadding);
            this.sps.endNalUnit(discardPadding);
            this.pps.endNalUnit(discardPadding);
            if (this.vps.isCompleted() && this.sps.isCompleted() && this.pps.isCompleted()) {
                this.output.format(parseMediaFormat(this.vps, this.sps, this.pps));
                this.hasOutputFormat = true;
            }
        }
        if (this.prefixSei.endNalUnit(discardPadding)) {
            this.seiWrapper.reset(this.prefixSei.nalData, NalUnitUtil.unescapeStream(this.prefixSei.nalData, this.prefixSei.nalLength));
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(pesTimeUs2, this.seiWrapper);
        }
        if (this.suffixSei.endNalUnit(discardPadding)) {
            this.seiWrapper.reset(this.suffixSei.nalData, NalUnitUtil.unescapeStream(this.suffixSei.nalData, this.suffixSei.nalLength));
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(pesTimeUs2, this.seiWrapper);
        }
    }

    private static MediaFormat parseMediaFormat(NalUnitTargetBuffer vps2, NalUnitTargetBuffer sps2, NalUnitTargetBuffer pps2) {
        int i;
        byte[] csd = new byte[(vps2.nalLength + sps2.nalLength + pps2.nalLength)];
        System.arraycopy(vps2.nalData, 0, csd, 0, vps2.nalLength);
        System.arraycopy(sps2.nalData, 0, csd, vps2.nalLength, sps2.nalLength);
        System.arraycopy(pps2.nalData, 0, csd, vps2.nalLength + sps2.nalLength, pps2.nalLength);
        NalUnitUtil.unescapeStream(sps2.nalData, sps2.nalLength);
        ParsableBitArray bitArray = new ParsableBitArray(sps2.nalData);
        bitArray.skipBits(44);
        int maxSubLayersMinus1 = bitArray.readBits(3);
        bitArray.skipBits(1);
        bitArray.skipBits(88);
        bitArray.skipBits(8);
        int toSkip = 0;
        for (int i2 = 0; i2 < maxSubLayersMinus1; i2++) {
            if (bitArray.readBit()) {
                toSkip += 89;
            }
            if (bitArray.readBit()) {
                toSkip += 8;
            }
        }
        bitArray.skipBits(toSkip);
        if (maxSubLayersMinus1 > 0) {
            bitArray.skipBits((8 - maxSubLayersMinus1) * 2);
        }
        bitArray.readUnsignedExpGolombCodedInt();
        int chromaFormatIdc = bitArray.readUnsignedExpGolombCodedInt();
        if (chromaFormatIdc == 3) {
            bitArray.skipBits(1);
        }
        int picWidthInLumaSamples = bitArray.readUnsignedExpGolombCodedInt();
        int picHeightInLumaSamples = bitArray.readUnsignedExpGolombCodedInt();
        if (bitArray.readBit()) {
            int confWinLeftOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinRightOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinTopOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinBottomOffset = bitArray.readUnsignedExpGolombCodedInt();
            picWidthInLumaSamples -= (confWinLeftOffset + confWinRightOffset) * ((chromaFormatIdc == 1 || chromaFormatIdc == 2) ? 2 : 1);
            picHeightInLumaSamples -= (confWinTopOffset + confWinBottomOffset) * (chromaFormatIdc == 1 ? 2 : 1);
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        int log2MaxPicOrderCntLsbMinus4 = bitArray.readUnsignedExpGolombCodedInt();
        if (bitArray.readBit()) {
            i = 0;
        } else {
            i = maxSubLayersMinus1;
        }
        while (i <= maxSubLayersMinus1) {
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            i++;
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        if (bitArray.readBit() && bitArray.readBit()) {
            skipScalingList(bitArray);
        }
        bitArray.skipBits(2);
        if (bitArray.readBit()) {
            bitArray.skipBits(8);
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.skipBits(1);
        }
        skipShortTermRefPicSets(bitArray);
        if (bitArray.readBit()) {
            for (int i3 = 0; i3 < bitArray.readUnsignedExpGolombCodedInt(); i3++) {
                bitArray.skipBits(log2MaxPicOrderCntLsbMinus4 + 4 + 1);
            }
        }
        bitArray.skipBits(2);
        float pixelWidthHeightRatio = 1.0f;
        if (bitArray.readBit() && bitArray.readBit()) {
            int aspectRatioIdc = bitArray.readBits(8);
            if (aspectRatioIdc == 255) {
                int sarWidth = bitArray.readBits(16);
                int sarHeight = bitArray.readBits(16);
                if (!(sarWidth == 0 || sarHeight == 0)) {
                    pixelWidthHeightRatio = ((float) sarWidth) / ((float) sarHeight);
                }
            } else if (aspectRatioIdc < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                pixelWidthHeightRatio = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[aspectRatioIdc];
            } else {
                Log.w(TAG, "Unexpected aspect_ratio_idc value: " + aspectRatioIdc);
            }
        }
        return MediaFormat.createVideoFormat((String) null, MimeTypes.VIDEO_H265, -1, -1, -1, picWidthInLumaSamples, picHeightInLumaSamples, Collections.singletonList(csd), -1, pixelWidthHeightRatio);
    }

    private static void skipScalingList(ParsableBitArray bitArray) {
        int i;
        for (int sizeId = 0; sizeId < 4; sizeId++) {
            int matrixId = 0;
            while (matrixId < 6) {
                if (!bitArray.readBit()) {
                    bitArray.readUnsignedExpGolombCodedInt();
                } else {
                    int coefNum = Math.min(64, 1 << ((sizeId << 1) + 4));
                    if (sizeId > 1) {
                        bitArray.readSignedExpGolombCodedInt();
                    }
                    for (int i2 = 0; i2 < coefNum; i2++) {
                        bitArray.readSignedExpGolombCodedInt();
                    }
                }
                if (sizeId == 3) {
                    i = 3;
                } else {
                    i = 1;
                }
                matrixId += i;
            }
        }
    }

    private static void skipShortTermRefPicSets(ParsableBitArray bitArray) {
        int numShortTermRefPicSets = bitArray.readUnsignedExpGolombCodedInt();
        boolean interRefPicSetPredictionFlag = false;
        int previousNumDeltaPocs = 0;
        for (int stRpsIdx = 0; stRpsIdx < numShortTermRefPicSets; stRpsIdx++) {
            if (stRpsIdx != 0) {
                interRefPicSetPredictionFlag = bitArray.readBit();
            }
            if (interRefPicSetPredictionFlag) {
                bitArray.skipBits(1);
                bitArray.readUnsignedExpGolombCodedInt();
                for (int j = 0; j <= previousNumDeltaPocs; j++) {
                    if (bitArray.readBit()) {
                        bitArray.skipBits(1);
                    }
                }
            } else {
                int numNegativePics = bitArray.readUnsignedExpGolombCodedInt();
                int numPositivePics = bitArray.readUnsignedExpGolombCodedInt();
                previousNumDeltaPocs = numNegativePics + numPositivePics;
                for (int i = 0; i < numNegativePics; i++) {
                    bitArray.readUnsignedExpGolombCodedInt();
                    bitArray.skipBits(1);
                }
                for (int i2 = 0; i2 < numPositivePics; i2++) {
                    bitArray.readUnsignedExpGolombCodedInt();
                    bitArray.skipBits(1);
                }
            }
        }
    }

    /* renamed from: com.google.android.exoplayer.extractor.ts.H265Reader$SampleReader */
    private static final class SampleReader {
        private static final int FIRST_SLICE_FLAG_OFFSET = 2;
        private boolean isFirstParameterSet;
        private boolean isFirstSlice;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private boolean writingParameterSets;

        public SampleReader(TrackOutput output2) {
            this.output = output2;
        }

        public void reset() {
            this.lookingForFirstSliceFlag = false;
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.readingSample = false;
            this.writingParameterSets = false;
        }

        public void startNalUnit(long position, int offset, int nalUnitType, long pesTimeUs) {
            boolean z;
            boolean z2 = false;
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.nalUnitTimeUs = pesTimeUs;
            this.nalUnitBytesRead = 0;
            this.nalUnitStartPosition = position;
            if (nalUnitType >= 32) {
                if (!this.writingParameterSets && this.readingSample) {
                    outputSample(offset);
                    this.readingSample = false;
                }
                if (nalUnitType <= 34) {
                    this.isFirstParameterSet = !this.writingParameterSets;
                    this.writingParameterSets = true;
                }
            }
            if (nalUnitType < 16 || nalUnitType > 21) {
                z = false;
            } else {
                z = true;
            }
            this.nalUnitHasKeyframeData = z;
            if (this.nalUnitHasKeyframeData || nalUnitType <= 9) {
                z2 = true;
            }
            this.lookingForFirstSliceFlag = z2;
        }

        public void readNalUnitData(byte[] data, int offset, int limit) {
            boolean z;
            if (this.lookingForFirstSliceFlag) {
                int headerOffset = (offset + 2) - this.nalUnitBytesRead;
                if (headerOffset < limit) {
                    if ((data[headerOffset] & 128) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    this.isFirstSlice = z;
                    this.lookingForFirstSliceFlag = false;
                    return;
                }
                this.nalUnitBytesRead += limit - offset;
            }
        }

        public void endNalUnit(long position, int offset) {
            if (this.writingParameterSets && this.isFirstSlice) {
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
                this.writingParameterSets = false;
            } else if (this.isFirstParameterSet || this.isFirstSlice) {
                if (this.readingSample) {
                    outputSample(offset + ((int) (position - this.nalUnitStartPosition)));
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.readingSample = true;
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            }
        }

        private void outputSample(int offset) {
            this.output.sampleMetadata(this.sampleTimeUs, this.sampleIsKeyframe ? 1 : 0, (int) (this.nalUnitStartPosition - this.samplePosition), offset, (byte[]) null);
        }
    }
}
