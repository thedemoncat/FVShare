package com.google.android.exoplayer.extractor.ogg;

import android.util.Log;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.util.Arrays;

final class VorbisUtil {
    private static final String TAG = "VorbisUtil";

    VorbisUtil() {
    }

    public static int iLog(int x) {
        int val = 0;
        while (x > 0) {
            val++;
            x >>>= 1;
        }
        return val;
    }

    public static VorbisIdHeader readVorbisIdentificationHeader(ParsableByteArray headerData) throws ParserException {
        verifyVorbisHeaderCapturePattern(1, headerData, false);
        long version = headerData.readLittleEndianUnsignedInt();
        int channels = headerData.readUnsignedByte();
        long sampleRate = headerData.readLittleEndianUnsignedInt();
        int bitrateMax = headerData.readLittleEndianInt();
        int bitrateNominal = headerData.readLittleEndianInt();
        int bitrateMin = headerData.readLittleEndianInt();
        int blockSize = headerData.readUnsignedByte();
        return new VorbisIdHeader(version, channels, sampleRate, bitrateMax, bitrateNominal, bitrateMin, (int) Math.pow(2.0d, (double) (blockSize & 15)), (int) Math.pow(2.0d, (double) ((blockSize & 240) >> 4)), (headerData.readUnsignedByte() & 1) > 0, Arrays.copyOf(headerData.data, headerData.limit()));
    }

    public static CommentHeader readVorbisCommentHeader(ParsableByteArray headerData) throws ParserException {
        verifyVorbisHeaderCapturePattern(3, headerData, false);
        int length = 7 + 4;
        String vendor = headerData.readString((int) headerData.readLittleEndianUnsignedInt());
        int length2 = vendor.length() + 11;
        long commentListLen = headerData.readLittleEndianUnsignedInt();
        String[] comments = new String[((int) commentListLen)];
        int length3 = length2 + 4;
        for (int i = 0; ((long) i) < commentListLen; i++) {
            comments[i] = headerData.readString((int) headerData.readLittleEndianUnsignedInt());
            length3 = length3 + 4 + comments[i].length();
        }
        if ((headerData.readUnsignedByte() & 1) != 0) {
            return new CommentHeader(vendor, comments, length3 + 1);
        }
        throw new ParserException("framing bit expected to be set");
    }

    public static boolean verifyVorbisHeaderCapturePattern(int headerType, ParsableByteArray header, boolean quite) throws ParserException {
        if (header.readUnsignedByte() != headerType) {
            if (quite) {
                return false;
            }
            throw new ParserException("expected header type " + Integer.toHexString(headerType));
        } else if (header.readUnsignedByte() == 118 && header.readUnsignedByte() == 111 && header.readUnsignedByte() == 114 && header.readUnsignedByte() == 98 && header.readUnsignedByte() == 105 && header.readUnsignedByte() == 115) {
            return true;
        } else {
            if (quite) {
                return false;
            }
            throw new ParserException("expected characters 'vorbis'");
        }
    }

    public static Mode[] readVorbisModes(ParsableByteArray headerData, int channels) throws ParserException {
        verifyVorbisHeaderCapturePattern(5, headerData, false);
        int numberOfBooks = headerData.readUnsignedByte() + 1;
        VorbisBitArray bitArray = new VorbisBitArray(headerData.data);
        bitArray.skipBits(headerData.getPosition() * 8);
        for (int i = 0; i < numberOfBooks; i++) {
            readBook(bitArray);
        }
        int timeCount = bitArray.readBits(6) + 1;
        for (int i2 = 0; i2 < timeCount; i2++) {
            if (bitArray.readBits(16) != 0) {
                throw new ParserException("placeholder of time domain transforms not zeroed out");
            }
        }
        readFloors(bitArray);
        readResidues(bitArray);
        readMappings(channels, bitArray);
        Mode[] modes = readModes(bitArray);
        if (bitArray.readBit()) {
            return modes;
        }
        throw new ParserException("framing bit after modes not set as expected");
    }

    private static Mode[] readModes(VorbisBitArray bitArray) {
        int modeCount = bitArray.readBits(6) + 1;
        Mode[] modes = new Mode[modeCount];
        for (int i = 0; i < modeCount; i++) {
            modes[i] = new Mode(bitArray.readBit(), bitArray.readBits(16), bitArray.readBits(16), bitArray.readBits(8));
        }
        return modes;
    }

    private static void readMappings(int channels, VorbisBitArray bitArray) throws ParserException {
        int submaps;
        int mappingsCount = bitArray.readBits(6) + 1;
        for (int i = 0; i < mappingsCount; i++) {
            int mappingType = bitArray.readBits(16);
            switch (mappingType) {
                case 0:
                    if (bitArray.readBit()) {
                        submaps = bitArray.readBits(4) + 1;
                    } else {
                        submaps = 1;
                    }
                    if (bitArray.readBit()) {
                        int couplingSteps = bitArray.readBits(8) + 1;
                        for (int j = 0; j < couplingSteps; j++) {
                            bitArray.skipBits(iLog(channels - 1));
                            bitArray.skipBits(iLog(channels - 1));
                        }
                    }
                    if (bitArray.readBits(2) == 0) {
                        if (submaps > 1) {
                            for (int j2 = 0; j2 < channels; j2++) {
                                bitArray.skipBits(4);
                            }
                        }
                        for (int j3 = 0; j3 < submaps; j3++) {
                            bitArray.skipBits(8);
                            bitArray.skipBits(8);
                            bitArray.skipBits(8);
                        }
                        break;
                    } else {
                        throw new ParserException("to reserved bits must be zero after mapping coupling steps");
                    }
                default:
                    Log.e(TAG, "mapping type other than 0 not supported: " + mappingType);
                    break;
            }
        }
    }

    private static void readResidues(VorbisBitArray bitArray) throws ParserException {
        int residueCount = bitArray.readBits(6) + 1;
        for (int i = 0; i < residueCount; i++) {
            if (bitArray.readBits(16) > 2) {
                throw new ParserException("residueType greater than 2 is not decodable");
            }
            bitArray.skipBits(24);
            bitArray.skipBits(24);
            bitArray.skipBits(24);
            int classifications = bitArray.readBits(6) + 1;
            bitArray.skipBits(8);
            int[] cascade = new int[classifications];
            for (int j = 0; j < classifications; j++) {
                int highBits = 0;
                int lowBits = bitArray.readBits(3);
                if (bitArray.readBit()) {
                    highBits = bitArray.readBits(5);
                }
                cascade[j] = (highBits * 8) + lowBits;
            }
            for (int j2 = 0; j2 < classifications; j2++) {
                for (int k = 0; k < 8; k++) {
                    if ((cascade[j2] & (1 << k)) != 0) {
                        bitArray.skipBits(8);
                    }
                }
            }
        }
    }

    private static void readFloors(VorbisBitArray bitArray) throws ParserException {
        int floorCount = bitArray.readBits(6) + 1;
        for (int i = 0; i < floorCount; i++) {
            int floorType = bitArray.readBits(16);
            switch (floorType) {
                case 0:
                    bitArray.skipBits(8);
                    bitArray.skipBits(16);
                    bitArray.skipBits(16);
                    bitArray.skipBits(6);
                    bitArray.skipBits(8);
                    int floorNumberOfBooks = bitArray.readBits(4) + 1;
                    for (int j = 0; j < floorNumberOfBooks; j++) {
                        bitArray.skipBits(8);
                    }
                    break;
                case 1:
                    int partitions = bitArray.readBits(5);
                    int maximumClass = -1;
                    int[] partitionClassList = new int[partitions];
                    for (int j2 = 0; j2 < partitions; j2++) {
                        partitionClassList[j2] = bitArray.readBits(4);
                        if (partitionClassList[j2] > maximumClass) {
                            maximumClass = partitionClassList[j2];
                        }
                    }
                    int[] classDimensions = new int[(maximumClass + 1)];
                    for (int j3 = 0; j3 < classDimensions.length; j3++) {
                        classDimensions[j3] = bitArray.readBits(3) + 1;
                        int classSubclasses = bitArray.readBits(2);
                        if (classSubclasses > 0) {
                            bitArray.skipBits(8);
                        }
                        for (int k = 0; k < (1 << classSubclasses); k++) {
                            bitArray.skipBits(8);
                        }
                    }
                    bitArray.skipBits(2);
                    int rangeBits = bitArray.readBits(4);
                    int count = 0;
                    int k2 = 0;
                    for (int j4 = 0; j4 < partitions; j4++) {
                        count += classDimensions[partitionClassList[j4]];
                        while (k2 < count) {
                            bitArray.skipBits(rangeBits);
                            k2++;
                        }
                    }
                    break;
                default:
                    throw new ParserException("floor type greater than 1 not decodable: " + floorType);
            }
        }
    }

    private static CodeBook readBook(VorbisBitArray bitArray) throws ParserException {
        long lookupValuesCount;
        if (bitArray.readBits(24) != 5653314) {
            throw new ParserException("expected code book to start with [0x56, 0x43, 0x42] at " + bitArray.getPosition());
        }
        int dimensions = bitArray.readBits(16);
        int entries = bitArray.readBits(24);
        long[] lengthMap = new long[entries];
        boolean isOrdered = bitArray.readBit();
        if (!isOrdered) {
            boolean isSparse = bitArray.readBit();
            for (int i = 0; i < lengthMap.length; i++) {
                if (!isSparse) {
                    lengthMap[i] = (long) (bitArray.readBits(5) + 1);
                } else if (bitArray.readBit()) {
                    lengthMap[i] = (long) (bitArray.readBits(5) + 1);
                } else {
                    lengthMap[i] = 0;
                }
            }
        } else {
            int length = bitArray.readBits(5) + 1;
            int i2 = 0;
            while (i2 < lengthMap.length) {
                int num = bitArray.readBits(iLog(entries - i2));
                for (int j = 0; j < num && i2 < lengthMap.length; j++) {
                    lengthMap[i2] = (long) length;
                    i2++;
                }
                length++;
            }
        }
        int lookupType = bitArray.readBits(4);
        if (lookupType > 2) {
            throw new ParserException("lookup type greater than 2 not decodable: " + lookupType);
        }
        if (lookupType == 1 || lookupType == 2) {
            bitArray.skipBits(32);
            bitArray.skipBits(32);
            int valueBits = bitArray.readBits(4) + 1;
            bitArray.skipBits(1);
            if (lookupType != 1) {
                lookupValuesCount = (long) (entries * dimensions);
            } else if (dimensions != 0) {
                lookupValuesCount = mapType1QuantValues((long) entries, (long) dimensions);
            } else {
                lookupValuesCount = 0;
            }
            bitArray.skipBits((int) (((long) valueBits) * lookupValuesCount));
        }
        return new CodeBook(dimensions, entries, lengthMap, lookupType, isOrdered);
    }

    private static long mapType1QuantValues(long entries, long dimension) {
        return (long) Math.floor(Math.pow((double) entries, 1.0d / ((double) dimension)));
    }

    public static final class CodeBook {
        public final int dimensions;
        public final int entries;
        public final boolean isOrdered;
        public final long[] lengthMap;
        public final int lookupType;

        public CodeBook(int dimensions2, int entries2, long[] lengthMap2, int lookupType2, boolean isOrdered2) {
            this.dimensions = dimensions2;
            this.entries = entries2;
            this.lengthMap = lengthMap2;
            this.lookupType = lookupType2;
            this.isOrdered = isOrdered2;
        }
    }

    public static final class CommentHeader {
        public final String[] comments;
        public final int length;
        public final String vendor;

        public CommentHeader(String vendor2, String[] comments2, int length2) {
            this.vendor = vendor2;
            this.comments = comments2;
            this.length = length2;
        }
    }

    public static final class VorbisIdHeader {
        public final int bitrateMax;
        public final int bitrateMin;
        public final int bitrateNominal;
        public final int blockSize0;
        public final int blockSize1;
        public final int channels;
        public final byte[] data;
        public final boolean framingFlag;
        public final long sampleRate;
        public final long version;

        public VorbisIdHeader(long version2, int channels2, long sampleRate2, int bitrateMax2, int bitrateNominal2, int bitrateMin2, int blockSize02, int blockSize12, boolean framingFlag2, byte[] data2) {
            this.version = version2;
            this.channels = channels2;
            this.sampleRate = sampleRate2;
            this.bitrateMax = bitrateMax2;
            this.bitrateNominal = bitrateNominal2;
            this.bitrateMin = bitrateMin2;
            this.blockSize0 = blockSize02;
            this.blockSize1 = blockSize12;
            this.framingFlag = framingFlag2;
            this.data = data2;
        }

        public int getApproximateBitrate() {
            return this.bitrateNominal == 0 ? (this.bitrateMin + this.bitrateMax) / 2 : this.bitrateNominal;
        }
    }

    public static final class Mode {
        public final boolean blockFlag;
        public final int mapping;
        public final int transformType;
        public final int windowType;

        public Mode(boolean blockFlag2, int windowType2, int transformType2, int mapping2) {
            this.blockFlag = blockFlag2;
            this.windowType = windowType2;
            this.transformType = transformType2;
            this.mapping = mapping2;
        }
    }
}
