package com.google.android.exoplayer.extractor.flv;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

final class VideoTagPayloadReader extends TagPayloadReader {
    private static final int AVC_PACKET_TYPE_AVC_NALU = 1;
    private static final int AVC_PACKET_TYPE_SEQUENCE_HEADER = 0;
    private static final int VIDEO_CODEC_AVC = 7;
    private static final int VIDEO_FRAME_KEYFRAME = 1;
    private static final int VIDEO_FRAME_VIDEO_INFO = 5;
    private int frameType;
    private boolean hasOutputFormat;
    private final ParsableByteArray nalLength = new ParsableByteArray(4);
    private final ParsableByteArray nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
    private int nalUnitLengthFieldLength;

    public VideoTagPayloadReader(TrackOutput output) {
        super(output);
    }

    public void seek() {
    }

    /* access modifiers changed from: protected */
    public boolean parseHeader(ParsableByteArray data) throws TagPayloadReader.UnsupportedFormatException {
        int header = data.readUnsignedByte();
        int frameType2 = (header >> 4) & 15;
        int videoCodec = header & 15;
        if (videoCodec != 7) {
            throw new TagPayloadReader.UnsupportedFormatException("Video format not supported: " + videoCodec);
        }
        this.frameType = frameType2;
        return frameType2 != 5;
    }

    /* access modifiers changed from: protected */
    public void parsePayload(ParsableByteArray data, long timeUs) throws ParserException {
        int packetType = data.readUnsignedByte();
        long timeUs2 = timeUs + (((long) data.readUnsignedInt24()) * 1000);
        if (packetType == 0 && !this.hasOutputFormat) {
            ParsableByteArray parsableByteArray = new ParsableByteArray(new byte[data.bytesLeft()]);
            data.readBytes(parsableByteArray.data, 0, data.bytesLeft());
            AvcSequenceHeaderData avcData = parseAvcCodecPrivate(parsableByteArray);
            this.nalUnitLengthFieldLength = avcData.nalUnitLengthFieldLength;
            this.output.format(MediaFormat.createVideoFormat((String) null, MimeTypes.VIDEO_H264, -1, -1, getDurationUs(), avcData.width, avcData.height, avcData.initializationData, -1, avcData.pixelWidthAspectRatio));
            this.hasOutputFormat = true;
        } else if (packetType == 1) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLengthDiff = 4 - this.nalUnitLengthFieldLength;
            int bytesWritten = 0;
            while (data.bytesLeft() > 0) {
                data.readBytes(this.nalLength.data, nalUnitLengthFieldLengthDiff, this.nalUnitLengthFieldLength);
                this.nalLength.setPosition(0);
                int bytesToWrite = this.nalLength.readUnsignedIntToInt();
                this.nalStartCode.setPosition(0);
                this.output.sampleData(this.nalStartCode, 4);
                this.output.sampleData(data, bytesToWrite);
                bytesWritten = bytesWritten + 4 + bytesToWrite;
            }
            this.output.sampleMetadata(timeUs2, this.frameType == 1 ? 1 : 0, bytesWritten, 0, (byte[]) null);
        }
    }

    private AvcSequenceHeaderData parseAvcCodecPrivate(ParsableByteArray buffer) throws ParserException {
        boolean z;
        buffer.setPosition(4);
        int nalUnitLengthFieldLength2 = (buffer.readUnsignedByte() & 3) + 1;
        if (nalUnitLengthFieldLength2 != 3) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkState(z);
        List<byte[]> initializationData = new ArrayList<>();
        int numSequenceParameterSets = buffer.readUnsignedByte() & 31;
        for (int i = 0; i < numSequenceParameterSets; i++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
        }
        int numPictureParameterSets = buffer.readUnsignedByte();
        for (int j = 0; j < numPictureParameterSets; j++) {
            initializationData.add(NalUnitUtil.parseChildNalUnit(buffer));
        }
        float pixelWidthAspectRatio = 1.0f;
        int width = -1;
        int height = -1;
        if (numSequenceParameterSets > 0) {
            ParsableBitArray spsDataBitArray = new ParsableBitArray(initializationData.get(0));
            spsDataBitArray.setPosition((nalUnitLengthFieldLength2 + 1) * 8);
            NalUnitUtil.SpsData sps = NalUnitUtil.parseSpsNalUnit(spsDataBitArray);
            width = sps.width;
            height = sps.height;
            pixelWidthAspectRatio = sps.pixelWidthAspectRatio;
        }
        return new AvcSequenceHeaderData(initializationData, nalUnitLengthFieldLength2, width, height, pixelWidthAspectRatio);
    }

    private static final class AvcSequenceHeaderData {
        public final int height;
        public final List<byte[]> initializationData;
        public final int nalUnitLengthFieldLength;
        public final float pixelWidthAspectRatio;
        public final int width;

        public AvcSequenceHeaderData(List<byte[]> initializationData2, int nalUnitLengthFieldLength2, int width2, int height2, float pixelWidthAspectRatio2) {
            this.initializationData = initializationData2;
            this.nalUnitLengthFieldLength = nalUnitLengthFieldLength2;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio2;
            this.width = width2;
            this.height = height2;
        }
    }
}
