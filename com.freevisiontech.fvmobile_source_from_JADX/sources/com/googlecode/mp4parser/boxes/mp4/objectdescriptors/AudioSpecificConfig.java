package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Descriptor(objectTypeIndication = 64, tags = {5})
public class AudioSpecificConfig extends BaseDescriptor {
    public static Map<Integer, String> audioObjectTypeMap = new HashMap();
    public static Map<Integer, Integer> samplingFrequencyIndexMap = new HashMap();
    public boolean aacScalefactorDataResilienceFlag;
    public boolean aacSectionDataResilienceFlag;
    public boolean aacSpectralDataResilienceFlag;
    public int audioObjectType;
    public int channelConfiguration;
    byte[] configBytes;
    public int coreCoderDelay;
    public int dependsOnCoreCoder;
    public int directMapping;
    public ELDSpecificConfig eldSpecificConfig;
    public int epConfig;
    public int erHvxcExtensionFlag;
    public int extensionAudioObjectType;
    public int extensionChannelConfiguration;
    public int extensionFlag;
    public int extensionFlag3;
    public int extensionSamplingFrequency;
    public int extensionSamplingFrequencyIndex = -1;
    public int fillBits;
    public int frameLengthFlag;
    public boolean gaSpecificConfig;
    public int hilnContMode;
    public int hilnEnhaLayer;
    public int hilnEnhaQuantMode;
    public int hilnFrameLength;
    public int hilnMaxNumLine;
    public int hilnQuantMode;
    public int hilnSampleRateCode;
    public int hvxcRateMode;
    public int hvxcVarMode;
    public int innerSyncExtensionType = -1;
    public int isBaseLayer;
    public int layerNr;
    public int layer_length;
    public int numOfSubFrame;
    public int origExtensionAudioObjectType;
    public int originalAudioObjectType;
    public int outerSyncExtensionType = -1;
    public int paraExtensionFlag;
    public int paraMode;
    public boolean parametricSpecificConfig;
    boolean parsed = false;
    public boolean psPresentFlag;
    public int sacPayloadEmbedding;
    public int samplingFrequency;
    public int samplingFrequencyIndex;
    public boolean sbrPresentFlag;
    public int syncExtensionType = -1;
    public int var_ScalableFlag;

    static {
        samplingFrequencyIndexMap.put(0, 96000);
        samplingFrequencyIndexMap.put(1, 88200);
        samplingFrequencyIndexMap.put(2, 64000);
        samplingFrequencyIndexMap.put(3, Integer.valueOf(Camera2Constants.AudioSampleRate));
        samplingFrequencyIndexMap.put(4, 44100);
        samplingFrequencyIndexMap.put(5, 32000);
        samplingFrequencyIndexMap.put(6, 24000);
        samplingFrequencyIndexMap.put(7, 22050);
        samplingFrequencyIndexMap.put(8, 16000);
        samplingFrequencyIndexMap.put(9, 12000);
        samplingFrequencyIndexMap.put(10, 11025);
        samplingFrequencyIndexMap.put(11, 8000);
        audioObjectTypeMap.put(1, "AAC main");
        audioObjectTypeMap.put(2, "AAC LC");
        audioObjectTypeMap.put(3, "AAC SSR");
        audioObjectTypeMap.put(4, "AAC LTP");
        audioObjectTypeMap.put(5, "SBR");
        audioObjectTypeMap.put(6, "AAC Scalable");
        audioObjectTypeMap.put(7, "TwinVQ");
        audioObjectTypeMap.put(8, "CELP");
        audioObjectTypeMap.put(9, "HVXC");
        audioObjectTypeMap.put(10, "(reserved)");
        audioObjectTypeMap.put(11, "(reserved)");
        audioObjectTypeMap.put(12, "TTSI");
        audioObjectTypeMap.put(13, "Main synthetic");
        audioObjectTypeMap.put(14, "Wavetable synthesis");
        audioObjectTypeMap.put(15, "General MIDI");
        audioObjectTypeMap.put(16, "Algorithmic Synthesis and Audio FX");
        audioObjectTypeMap.put(17, "ER AAC LC");
        audioObjectTypeMap.put(18, "(reserved)");
        audioObjectTypeMap.put(19, "ER AAC LTP");
        audioObjectTypeMap.put(20, "ER AAC Scalable");
        audioObjectTypeMap.put(21, "ER TwinVQ");
        audioObjectTypeMap.put(22, "ER BSAC");
        audioObjectTypeMap.put(23, "ER AAC LD");
        audioObjectTypeMap.put(24, "ER CELP");
        audioObjectTypeMap.put(25, "ER HVXC");
        audioObjectTypeMap.put(26, "ER HILN");
        audioObjectTypeMap.put(27, "ER Parametric");
        audioObjectTypeMap.put(28, "SSC");
        audioObjectTypeMap.put(29, "PS");
        audioObjectTypeMap.put(30, "MPEG Surround");
        audioObjectTypeMap.put(31, "(escape)");
        audioObjectTypeMap.put(32, "Layer-1");
        audioObjectTypeMap.put(33, "Layer-2");
        audioObjectTypeMap.put(34, "Layer-3");
        audioObjectTypeMap.put(35, "DST");
        audioObjectTypeMap.put(36, "ALS");
        audioObjectTypeMap.put(37, "SLS");
        audioObjectTypeMap.put(38, "SLS non-core");
        audioObjectTypeMap.put(39, "ER AAC ELD");
        audioObjectTypeMap.put(40, "SMR Simple");
        audioObjectTypeMap.put(41, "SMR Main");
    }

    public AudioSpecificConfig() {
        this.tag = 5;
    }

    public void parseDetail(ByteBuffer bb) throws IOException {
        this.parsed = true;
        ByteBuffer configBytes2 = bb.slice();
        configBytes2.limit(this.sizeOfInstance);
        bb.position(bb.position() + this.sizeOfInstance);
        this.configBytes = new byte[this.sizeOfInstance];
        configBytes2.get(this.configBytes);
        configBytes2.rewind();
        BitReaderBuffer bitReaderBuffer = new BitReaderBuffer(configBytes2);
        int audioObjectType2 = getAudioObjectType(bitReaderBuffer);
        this.audioObjectType = audioObjectType2;
        this.originalAudioObjectType = audioObjectType2;
        this.samplingFrequencyIndex = bitReaderBuffer.readBits(4);
        if (this.samplingFrequencyIndex == 15) {
            this.samplingFrequency = bitReaderBuffer.readBits(24);
        }
        this.channelConfiguration = bitReaderBuffer.readBits(4);
        if (this.audioObjectType == 5 || this.audioObjectType == 29) {
            this.extensionAudioObjectType = 5;
            this.sbrPresentFlag = true;
            if (this.audioObjectType == 29) {
                this.psPresentFlag = true;
            }
            this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
            if (this.extensionSamplingFrequencyIndex == 15) {
                this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
            }
            this.audioObjectType = getAudioObjectType(bitReaderBuffer);
            if (this.audioObjectType == 22) {
                this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
            }
        } else {
            this.extensionAudioObjectType = 0;
        }
        switch (this.audioObjectType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                parseGaSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                break;
            case 8:
                throw new UnsupportedOperationException("can't parse CelpSpecificConfig yet");
            case 9:
                throw new UnsupportedOperationException("can't parse HvxcSpecificConfig yet");
            case 12:
                throw new UnsupportedOperationException("can't parse TTSSpecificConfig yet");
            case 13:
            case 14:
            case 15:
            case 16:
                throw new UnsupportedOperationException("can't parse StructuredAudioSpecificConfig yet");
            case 24:
                throw new UnsupportedOperationException("can't parse ErrorResilientCelpSpecificConfig yet");
            case 25:
                throw new UnsupportedOperationException("can't parse ErrorResilientHvxcSpecificConfig yet");
            case 26:
            case 27:
                parseParametricSpecificConfig(this.samplingFrequencyIndex, this.channelConfiguration, this.audioObjectType, bitReaderBuffer);
                break;
            case 28:
                throw new UnsupportedOperationException("can't parse SSCSpecificConfig yet");
            case 30:
                this.sacPayloadEmbedding = bitReaderBuffer.readBits(1);
                throw new UnsupportedOperationException("can't parse SpatialSpecificConfig yet");
            case 32:
            case 33:
            case 34:
                throw new UnsupportedOperationException("can't parse MPEG_1_2_SpecificConfig yet");
            case 35:
                throw new UnsupportedOperationException("can't parse DSTSpecificConfig yet");
            case 36:
                this.fillBits = bitReaderBuffer.readBits(5);
                throw new UnsupportedOperationException("can't parse ALSSpecificConfig yet");
            case 37:
            case 38:
                throw new UnsupportedOperationException("can't parse SLSSpecificConfig yet");
            case 39:
                this.eldSpecificConfig = new ELDSpecificConfig(this.channelConfiguration, bitReaderBuffer);
                break;
            case 40:
            case 41:
                throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
        }
        switch (this.audioObjectType) {
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 39:
                this.epConfig = bitReaderBuffer.readBits(2);
                if (this.epConfig == 2 || this.epConfig == 3) {
                    throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
                } else if (this.epConfig == 3) {
                    this.directMapping = bitReaderBuffer.readBits(1);
                    if (this.directMapping == 0) {
                        throw new RuntimeException("not implemented");
                    }
                }
                break;
        }
        if (this.extensionAudioObjectType != 5 && bitReaderBuffer.remainingBits() >= 16) {
            int readBits = bitReaderBuffer.readBits(11);
            this.syncExtensionType = readBits;
            this.outerSyncExtensionType = readBits;
            if (this.syncExtensionType == 695) {
                this.extensionAudioObjectType = getAudioObjectType(bitReaderBuffer);
                if (this.extensionAudioObjectType == 5) {
                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                    if (this.sbrPresentFlag) {
                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                        }
                        if (bitReaderBuffer.remainingBits() >= 12) {
                            int readBits2 = bitReaderBuffer.readBits(11);
                            this.syncExtensionType = readBits2;
                            this.innerSyncExtensionType = readBits2;
                            if (this.syncExtensionType == 1352) {
                                this.psPresentFlag = bitReaderBuffer.readBool();
                            }
                        }
                    }
                }
                if (this.extensionAudioObjectType == 22) {
                    this.sbrPresentFlag = bitReaderBuffer.readBool();
                    if (this.sbrPresentFlag) {
                        this.extensionSamplingFrequencyIndex = bitReaderBuffer.readBits(4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            this.extensionSamplingFrequency = bitReaderBuffer.readBits(24);
                        }
                    }
                    this.extensionChannelConfiguration = bitReaderBuffer.readBits(4);
                }
            }
        }
    }

    private int gaSpecificConfigSize() {
        int n = 0 + 1 + 1;
        if (this.dependsOnCoreCoder == 1) {
            n += 14;
        }
        int n2 = n + 1;
        if (this.channelConfiguration == 0) {
            throw new UnsupportedOperationException("can't parse program_config_element yet");
        }
        if (this.audioObjectType == 6 || this.audioObjectType == 20) {
            n2 += 3;
        }
        if (this.extensionFlag == 1) {
            if (this.audioObjectType == 22) {
                n2 = n2 + 5 + 11;
            }
            if (this.audioObjectType == 17 || this.audioObjectType == 19 || this.audioObjectType == 20 || this.audioObjectType == 23) {
                n2 = n2 + 1 + 1 + 1;
            }
            n2++;
            if (this.extensionFlag3 == 1) {
                throw new RuntimeException("Not implemented");
            }
        }
        return n2;
    }

    /* access modifiers changed from: package-private */
    public int getContentSize() {
        int sizeInBits;
        int sizeInBits2 = 5;
        if (this.originalAudioObjectType > 30) {
            sizeInBits2 = 5 + 6;
        }
        int sizeInBits3 = sizeInBits2 + 4;
        if (this.samplingFrequencyIndex == 15) {
            sizeInBits3 += 24;
        }
        int sizeInBits4 = sizeInBits3 + 4;
        if (this.audioObjectType == 5 || this.audioObjectType == 29) {
            sizeInBits4 += 4;
            if (this.extensionSamplingFrequencyIndex == 15) {
                sizeInBits4 += 24;
            }
        }
        if (this.audioObjectType == 22) {
            sizeInBits4 += 4;
        }
        if (this.gaSpecificConfig) {
            sizeInBits4 += gaSpecificConfigSize();
        }
        if (this.outerSyncExtensionType >= 0) {
            sizeInBits += 11;
            if (this.outerSyncExtensionType == 695) {
                int sizeInBits5 = sizeInBits + 5;
                if (this.extensionAudioObjectType > 30) {
                    sizeInBits5 += 6;
                }
                if (this.extensionAudioObjectType == 5) {
                    sizeInBits++;
                    if (this.sbrPresentFlag) {
                        sizeInBits += 4;
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            sizeInBits += 24;
                        }
                        if (this.innerSyncExtensionType >= 0) {
                            sizeInBits += 11;
                            if (this.innerSyncExtensionType == 1352) {
                                sizeInBits++;
                            }
                        }
                    }
                }
                if (this.extensionAudioObjectType == 22) {
                    int sizeInBits6 = sizeInBits + 1;
                    if (this.sbrPresentFlag) {
                        sizeInBits6 += 4;
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            sizeInBits6 += 24;
                        }
                    }
                    sizeInBits = sizeInBits6 + 4;
                }
            }
        }
        return (int) Math.ceil(((double) sizeInBits) / 8.0d);
    }

    public ByteBuffer serialize() {
        ByteBuffer out = ByteBuffer.allocate(getSize());
        IsoTypeWriter.writeUInt8(out, this.tag);
        writeSize(out, getContentSize());
        out.put(serializeConfigBytes());
        return (ByteBuffer) out.rewind();
    }

    private ByteBuffer serializeConfigBytes() {
        ByteBuffer out = ByteBuffer.wrap(new byte[getContentSize()]);
        BitWriterBuffer bitWriterBuffer = new BitWriterBuffer(out);
        writeAudioObjectType(this.originalAudioObjectType, bitWriterBuffer);
        bitWriterBuffer.writeBits(this.samplingFrequencyIndex, 4);
        if (this.samplingFrequencyIndex == 15) {
            bitWriterBuffer.writeBits(this.samplingFrequency, 24);
        }
        bitWriterBuffer.writeBits(this.channelConfiguration, 4);
        if (this.audioObjectType == 5 || this.audioObjectType == 29) {
            this.extensionAudioObjectType = 5;
            this.sbrPresentFlag = true;
            if (this.audioObjectType == 29) {
                this.psPresentFlag = true;
            }
            bitWriterBuffer.writeBits(this.extensionSamplingFrequencyIndex, 4);
            if (this.extensionSamplingFrequencyIndex == 15) {
                bitWriterBuffer.writeBits(this.extensionSamplingFrequency, 24);
            }
            writeAudioObjectType(this.audioObjectType, bitWriterBuffer);
            if (this.audioObjectType == 22) {
                bitWriterBuffer.writeBits(this.extensionChannelConfiguration, 4);
            }
        }
        switch (this.audioObjectType) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                writeGaSpecificConfig(bitWriterBuffer);
                break;
            case 8:
                throw new UnsupportedOperationException("can't write CelpSpecificConfig yet");
            case 9:
                throw new UnsupportedOperationException("can't write HvxcSpecificConfig yet");
            case 12:
                throw new UnsupportedOperationException("can't write TTSSpecificConfig yet");
            case 13:
            case 14:
            case 15:
            case 16:
                throw new UnsupportedOperationException("can't write StructuredAudioSpecificConfig yet");
            case 24:
                throw new UnsupportedOperationException("can't write ErrorResilientCelpSpecificConfig yet");
            case 25:
                throw new UnsupportedOperationException("can't write ErrorResilientHvxcSpecificConfig yet");
            case 26:
            case 27:
                throw new UnsupportedOperationException("can't write parseParametricSpecificConfig yet");
            case 28:
                throw new UnsupportedOperationException("can't write SSCSpecificConfig yet");
            case 30:
                bitWriterBuffer.writeBits(this.sacPayloadEmbedding, 1);
                throw new UnsupportedOperationException("can't write SpatialSpecificConfig yet");
            case 32:
            case 33:
            case 34:
                throw new UnsupportedOperationException("can't write MPEG_1_2_SpecificConfig yet");
            case 35:
                throw new UnsupportedOperationException("can't write DSTSpecificConfig yet");
            case 36:
                bitWriterBuffer.writeBits(this.fillBits, 5);
                throw new UnsupportedOperationException("can't write ALSSpecificConfig yet");
            case 37:
            case 38:
                throw new UnsupportedOperationException("can't write SLSSpecificConfig yet");
            case 39:
                throw new UnsupportedOperationException("can't write ELDSpecificConfig yet");
            case 40:
            case 41:
                throw new UnsupportedOperationException("can't parse SymbolicMusicSpecificConfig yet");
        }
        switch (this.audioObjectType) {
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 39:
                bitWriterBuffer.writeBits(this.epConfig, 2);
                if (this.epConfig == 2 || this.epConfig == 3) {
                    throw new UnsupportedOperationException("can't parse ErrorProtectionSpecificConfig yet");
                } else if (this.epConfig == 3) {
                    bitWriterBuffer.writeBits(this.directMapping, 1);
                    if (this.directMapping == 0) {
                        throw new RuntimeException("not implemented");
                    }
                }
                break;
        }
        if (this.outerSyncExtensionType >= 0) {
            bitWriterBuffer.writeBits(this.outerSyncExtensionType, 11);
            if (this.outerSyncExtensionType == 695) {
                writeAudioObjectType(this.extensionAudioObjectType, bitWriterBuffer);
                if (this.extensionAudioObjectType == 5) {
                    bitWriterBuffer.writeBool(this.sbrPresentFlag);
                    if (this.sbrPresentFlag) {
                        bitWriterBuffer.writeBits(this.extensionSamplingFrequencyIndex, 4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            bitWriterBuffer.writeBits(this.extensionSamplingFrequency, 24);
                        }
                        if (this.innerSyncExtensionType >= 0) {
                            bitWriterBuffer.writeBits(this.innerSyncExtensionType, 11);
                            if (this.syncExtensionType == 1352) {
                                bitWriterBuffer.writeBool(this.psPresentFlag);
                            }
                        }
                    }
                }
                if (this.extensionAudioObjectType == 22) {
                    bitWriterBuffer.writeBool(this.sbrPresentFlag);
                    if (this.sbrPresentFlag) {
                        bitWriterBuffer.writeBits(this.extensionSamplingFrequencyIndex, 4);
                        if (this.extensionSamplingFrequencyIndex == 15) {
                            bitWriterBuffer.writeBits(this.extensionSamplingFrequency, 24);
                        }
                    }
                    bitWriterBuffer.writeBits(this.extensionChannelConfiguration, 4);
                }
            }
        }
        return (ByteBuffer) out.rewind();
    }

    private void writeAudioObjectType(int audioObjectType2, BitWriterBuffer bitWriterBuffer) {
        if (audioObjectType2 >= 32) {
            bitWriterBuffer.writeBits(31, 5);
            bitWriterBuffer.writeBits(audioObjectType2 - 32, 6);
            return;
        }
        bitWriterBuffer.writeBits(audioObjectType2, 5);
    }

    private int getAudioObjectType(BitReaderBuffer in) throws IOException {
        int audioObjectType2 = in.readBits(5);
        if (audioObjectType2 == 31) {
            return in.readBits(6) + 32;
        }
        return audioObjectType2;
    }

    private void parseGaSpecificConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.frameLengthFlag = in.readBits(1);
        this.dependsOnCoreCoder = in.readBits(1);
        if (this.dependsOnCoreCoder == 1) {
            this.coreCoderDelay = in.readBits(14);
        }
        this.extensionFlag = in.readBits(1);
        if (channelConfiguration2 == 0) {
            throw new UnsupportedOperationException("can't parse program_config_element yet");
        }
        if (audioObjectType2 == 6 || audioObjectType2 == 20) {
            this.layerNr = in.readBits(3);
        }
        if (this.extensionFlag == 1) {
            if (audioObjectType2 == 22) {
                this.numOfSubFrame = in.readBits(5);
                this.layer_length = in.readBits(11);
            }
            if (audioObjectType2 == 17 || audioObjectType2 == 19 || audioObjectType2 == 20 || audioObjectType2 == 23) {
                this.aacSectionDataResilienceFlag = in.readBool();
                this.aacScalefactorDataResilienceFlag = in.readBool();
                this.aacSpectralDataResilienceFlag = in.readBool();
            }
            this.extensionFlag3 = in.readBits(1);
            if (this.extensionFlag3 == 1) {
                throw new RuntimeException("not yet implemented");
            }
        }
        this.gaSpecificConfig = true;
    }

    private void writeGaSpecificConfig(BitWriterBuffer out) {
        out.writeBits(this.frameLengthFlag, 1);
        out.writeBits(this.dependsOnCoreCoder, 1);
        if (this.dependsOnCoreCoder == 1) {
            out.writeBits(this.coreCoderDelay, 14);
        }
        out.writeBits(this.extensionFlag, 1);
        if (this.channelConfiguration == 0) {
            throw new UnsupportedOperationException("can't parse program_config_element yet");
        }
        if (this.audioObjectType == 6 || this.audioObjectType == 20) {
            out.writeBits(this.layerNr, 3);
        }
        if (this.extensionFlag == 1) {
            if (this.audioObjectType == 22) {
                out.writeBits(this.numOfSubFrame, 5);
                out.writeBits(this.layer_length, 11);
            }
            if (this.audioObjectType == 17 || this.audioObjectType == 19 || this.audioObjectType == 20 || this.audioObjectType == 23) {
                out.writeBool(this.aacSectionDataResilienceFlag);
                out.writeBool(this.aacScalefactorDataResilienceFlag);
                out.writeBool(this.aacSpectralDataResilienceFlag);
            }
            out.writeBits(this.extensionFlag3, 1);
            if (this.extensionFlag3 == 1) {
                throw new RuntimeException("not yet implemented");
            }
        }
    }

    private void parseParametricSpecificConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.isBaseLayer = in.readBits(1);
        if (this.isBaseLayer == 1) {
            parseParaConfig(samplingFrequencyIndex2, channelConfiguration2, audioObjectType2, in);
        } else {
            parseHilnEnexConfig(samplingFrequencyIndex2, channelConfiguration2, audioObjectType2, in);
        }
    }

    private void parseParaConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.paraMode = in.readBits(2);
        if (this.paraMode != 1) {
            parseErHvxcConfig(samplingFrequencyIndex2, channelConfiguration2, audioObjectType2, in);
        }
        if (this.paraMode != 0) {
            parseHilnConfig(samplingFrequencyIndex2, channelConfiguration2, audioObjectType2, in);
        }
        this.paraExtensionFlag = in.readBits(1);
        this.parametricSpecificConfig = true;
    }

    private void parseErHvxcConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.hvxcVarMode = in.readBits(1);
        this.hvxcRateMode = in.readBits(2);
        this.erHvxcExtensionFlag = in.readBits(1);
        if (this.erHvxcExtensionFlag == 1) {
            this.var_ScalableFlag = in.readBits(1);
        }
    }

    private void parseHilnConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.hilnQuantMode = in.readBits(1);
        this.hilnMaxNumLine = in.readBits(8);
        this.hilnSampleRateCode = in.readBits(4);
        this.hilnFrameLength = in.readBits(12);
        this.hilnContMode = in.readBits(2);
    }

    private void parseHilnEnexConfig(int samplingFrequencyIndex2, int channelConfiguration2, int audioObjectType2, BitReaderBuffer in) throws IOException {
        this.hilnEnhaLayer = in.readBits(1);
        if (this.hilnEnhaLayer == 1) {
            this.hilnEnhaQuantMode = in.readBits(2);
        }
    }

    public byte[] getConfigBytes() {
        return serializeConfigBytes().array();
    }

    public int getAudioObjectType() {
        return this.audioObjectType;
    }

    public void setAudioObjectType(int audioObjectType2) {
        this.audioObjectType = audioObjectType2;
    }

    public void setOriginalAudioObjectType(int originalAudioObjectType2) {
        this.originalAudioObjectType = originalAudioObjectType2;
    }

    public int getExtensionAudioObjectType() {
        return this.extensionAudioObjectType;
    }

    public void setSamplingFrequencyIndex(int samplingFrequencyIndex2) {
        this.samplingFrequencyIndex = samplingFrequencyIndex2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioSpecificConfig");
        sb.append("{configBytes=").append(Hex.encodeHex(this.configBytes));
        sb.append(", audioObjectType=").append(this.audioObjectType).append(" (").append(audioObjectTypeMap.get(Integer.valueOf(this.audioObjectType))).append(")");
        sb.append(", samplingFrequencyIndex=").append(this.samplingFrequencyIndex).append(" (").append(samplingFrequencyIndexMap.get(Integer.valueOf(this.samplingFrequencyIndex))).append(")");
        sb.append(", samplingFrequency=").append(this.samplingFrequency);
        sb.append(", channelConfiguration=").append(this.channelConfiguration);
        if (this.extensionAudioObjectType > 0) {
            sb.append(", extensionAudioObjectType=").append(this.extensionAudioObjectType).append(" (").append(audioObjectTypeMap.get(Integer.valueOf(this.extensionAudioObjectType))).append(")");
            sb.append(", sbrPresentFlag=").append(this.sbrPresentFlag);
            sb.append(", psPresentFlag=").append(this.psPresentFlag);
            sb.append(", extensionSamplingFrequencyIndex=").append(this.extensionSamplingFrequencyIndex).append(" (").append(samplingFrequencyIndexMap.get(Integer.valueOf(this.extensionSamplingFrequencyIndex))).append(")");
            sb.append(", extensionSamplingFrequency=").append(this.extensionSamplingFrequency);
            sb.append(", extensionChannelConfiguration=").append(this.extensionChannelConfiguration);
        }
        sb.append(", syncExtensionType=").append(this.syncExtensionType);
        if (this.gaSpecificConfig) {
            sb.append(", frameLengthFlag=").append(this.frameLengthFlag);
            sb.append(", dependsOnCoreCoder=").append(this.dependsOnCoreCoder);
            sb.append(", coreCoderDelay=").append(this.coreCoderDelay);
            sb.append(", extensionFlag=").append(this.extensionFlag);
            sb.append(", layerNr=").append(this.layerNr);
            sb.append(", numOfSubFrame=").append(this.numOfSubFrame);
            sb.append(", layer_length=").append(this.layer_length);
            sb.append(", aacSectionDataResilienceFlag=").append(this.aacSectionDataResilienceFlag);
            sb.append(", aacScalefactorDataResilienceFlag=").append(this.aacScalefactorDataResilienceFlag);
            sb.append(", aacSpectralDataResilienceFlag=").append(this.aacSpectralDataResilienceFlag);
            sb.append(", extensionFlag3=").append(this.extensionFlag3);
        }
        if (this.parametricSpecificConfig) {
            sb.append(", isBaseLayer=").append(this.isBaseLayer);
            sb.append(", paraMode=").append(this.paraMode);
            sb.append(", paraExtensionFlag=").append(this.paraExtensionFlag);
            sb.append(", hvxcVarMode=").append(this.hvxcVarMode);
            sb.append(", hvxcRateMode=").append(this.hvxcRateMode);
            sb.append(", erHvxcExtensionFlag=").append(this.erHvxcExtensionFlag);
            sb.append(", var_ScalableFlag=").append(this.var_ScalableFlag);
            sb.append(", hilnQuantMode=").append(this.hilnQuantMode);
            sb.append(", hilnMaxNumLine=").append(this.hilnMaxNumLine);
            sb.append(", hilnSampleRateCode=").append(this.hilnSampleRateCode);
            sb.append(", hilnFrameLength=").append(this.hilnFrameLength);
            sb.append(", hilnContMode=").append(this.hilnContMode);
            sb.append(", hilnEnhaLayer=").append(this.hilnEnhaLayer);
            sb.append(", hilnEnhaQuantMode=").append(this.hilnEnhaQuantMode);
        }
        sb.append('}');
        return sb.toString();
    }

    public int getSamplingFrequency() {
        return this.samplingFrequencyIndex == 15 ? this.samplingFrequency : samplingFrequencyIndexMap.get(Integer.valueOf(this.samplingFrequencyIndex)).intValue();
    }

    public int getExtensionSamplingFrequency() {
        return this.extensionSamplingFrequencyIndex == 15 ? this.extensionSamplingFrequency : samplingFrequencyIndexMap.get(Integer.valueOf(this.extensionSamplingFrequencyIndex)).intValue();
    }

    public void setSamplingFrequency(int samplingFrequency2) {
        this.samplingFrequency = samplingFrequency2;
    }

    public int getChannelConfiguration() {
        return this.channelConfiguration;
    }

    public void setChannelConfiguration(int channelConfiguration2) {
        this.channelConfiguration = channelConfiguration2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioSpecificConfig that = (AudioSpecificConfig) o;
        if (this.aacScalefactorDataResilienceFlag != that.aacScalefactorDataResilienceFlag) {
            return false;
        }
        if (this.aacSectionDataResilienceFlag != that.aacSectionDataResilienceFlag) {
            return false;
        }
        if (this.aacSpectralDataResilienceFlag != that.aacSpectralDataResilienceFlag) {
            return false;
        }
        if (this.audioObjectType != that.audioObjectType) {
            return false;
        }
        if (this.channelConfiguration != that.channelConfiguration) {
            return false;
        }
        if (this.coreCoderDelay != that.coreCoderDelay) {
            return false;
        }
        if (this.dependsOnCoreCoder != that.dependsOnCoreCoder) {
            return false;
        }
        if (this.directMapping != that.directMapping) {
            return false;
        }
        if (this.epConfig != that.epConfig) {
            return false;
        }
        if (this.erHvxcExtensionFlag != that.erHvxcExtensionFlag) {
            return false;
        }
        if (this.extensionAudioObjectType != that.extensionAudioObjectType) {
            return false;
        }
        if (this.extensionChannelConfiguration != that.extensionChannelConfiguration) {
            return false;
        }
        if (this.extensionFlag != that.extensionFlag) {
            return false;
        }
        if (this.extensionFlag3 != that.extensionFlag3) {
            return false;
        }
        if (this.extensionSamplingFrequency != that.extensionSamplingFrequency) {
            return false;
        }
        if (this.extensionSamplingFrequencyIndex != that.extensionSamplingFrequencyIndex) {
            return false;
        }
        if (this.fillBits != that.fillBits) {
            return false;
        }
        if (this.frameLengthFlag != that.frameLengthFlag) {
            return false;
        }
        if (this.gaSpecificConfig != that.gaSpecificConfig) {
            return false;
        }
        if (this.hilnContMode != that.hilnContMode) {
            return false;
        }
        if (this.hilnEnhaLayer != that.hilnEnhaLayer) {
            return false;
        }
        if (this.hilnEnhaQuantMode != that.hilnEnhaQuantMode) {
            return false;
        }
        if (this.hilnFrameLength != that.hilnFrameLength) {
            return false;
        }
        if (this.hilnMaxNumLine != that.hilnMaxNumLine) {
            return false;
        }
        if (this.hilnQuantMode != that.hilnQuantMode) {
            return false;
        }
        if (this.hilnSampleRateCode != that.hilnSampleRateCode) {
            return false;
        }
        if (this.hvxcRateMode != that.hvxcRateMode) {
            return false;
        }
        if (this.hvxcVarMode != that.hvxcVarMode) {
            return false;
        }
        if (this.isBaseLayer != that.isBaseLayer) {
            return false;
        }
        if (this.layerNr != that.layerNr) {
            return false;
        }
        if (this.layer_length != that.layer_length) {
            return false;
        }
        if (this.numOfSubFrame != that.numOfSubFrame) {
            return false;
        }
        if (this.paraExtensionFlag != that.paraExtensionFlag) {
            return false;
        }
        if (this.paraMode != that.paraMode) {
            return false;
        }
        if (this.parametricSpecificConfig != that.parametricSpecificConfig) {
            return false;
        }
        if (this.psPresentFlag != that.psPresentFlag) {
            return false;
        }
        if (this.sacPayloadEmbedding != that.sacPayloadEmbedding) {
            return false;
        }
        if (this.samplingFrequency != that.samplingFrequency) {
            return false;
        }
        if (this.samplingFrequencyIndex != that.samplingFrequencyIndex) {
            return false;
        }
        if (this.sbrPresentFlag != that.sbrPresentFlag) {
            return false;
        }
        if (this.syncExtensionType != that.syncExtensionType) {
            return false;
        }
        if (this.var_ScalableFlag != that.var_ScalableFlag) {
            return false;
        }
        if (!Arrays.equals(this.configBytes, that.configBytes)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6 = 1;
        if (this.configBytes != null) {
            result = Arrays.hashCode(this.configBytes);
        } else {
            result = 0;
        }
        int i7 = ((((((((((result * 31) + this.audioObjectType) * 31) + this.samplingFrequencyIndex) * 31) + this.samplingFrequency) * 31) + this.channelConfiguration) * 31) + this.extensionAudioObjectType) * 31;
        if (this.sbrPresentFlag) {
            i = 1;
        } else {
            i = 0;
        }
        int i8 = (i7 + i) * 31;
        if (this.psPresentFlag) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i9 = (((((((((((((((((((((((((((((((((i8 + i2) * 31) + this.extensionSamplingFrequencyIndex) * 31) + this.extensionSamplingFrequency) * 31) + this.extensionChannelConfiguration) * 31) + this.sacPayloadEmbedding) * 31) + this.fillBits) * 31) + this.epConfig) * 31) + this.directMapping) * 31) + this.syncExtensionType) * 31) + this.frameLengthFlag) * 31) + this.dependsOnCoreCoder) * 31) + this.coreCoderDelay) * 31) + this.extensionFlag) * 31) + this.layerNr) * 31) + this.numOfSubFrame) * 31) + this.layer_length) * 31) + (this.aacSectionDataResilienceFlag ? 1 : 0)) * 31;
        if (this.aacScalefactorDataResilienceFlag) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i10 = (i9 + i3) * 31;
        if (this.aacSpectralDataResilienceFlag) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i11 = (((i10 + i4) * 31) + this.extensionFlag3) * 31;
        if (this.gaSpecificConfig) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i12 = (((((((((((((((((((((((((((((i11 + i5) * 31) + this.isBaseLayer) * 31) + this.paraMode) * 31) + this.paraExtensionFlag) * 31) + this.hvxcVarMode) * 31) + this.hvxcRateMode) * 31) + this.erHvxcExtensionFlag) * 31) + this.var_ScalableFlag) * 31) + this.hilnQuantMode) * 31) + this.hilnMaxNumLine) * 31) + this.hilnSampleRateCode) * 31) + this.hilnFrameLength) * 31) + this.hilnContMode) * 31) + this.hilnEnhaLayer) * 31) + this.hilnEnhaQuantMode) * 31;
        if (!this.parametricSpecificConfig) {
            i6 = 0;
        }
        return i12 + i6;
    }

    public class ELDSpecificConfig {
        private static final int ELDEXT_TERM = 0;
        public boolean aacScalefactorDataResilienceFlag;
        public boolean aacSectionDataResilienceFlag;
        public boolean aacSpectralDataResilienceFlag;
        public boolean frameLengthFlag;
        public boolean ldSbrCrcFlag;
        public boolean ldSbrPresentFlag;
        public boolean ldSbrSamplingRate;

        public ELDSpecificConfig(int channelConfiguration, BitReaderBuffer bitReaderBuffer) {
            this.frameLengthFlag = bitReaderBuffer.readBool();
            this.aacSectionDataResilienceFlag = bitReaderBuffer.readBool();
            this.aacScalefactorDataResilienceFlag = bitReaderBuffer.readBool();
            this.aacSpectralDataResilienceFlag = bitReaderBuffer.readBool();
            this.ldSbrPresentFlag = bitReaderBuffer.readBool();
            if (this.ldSbrPresentFlag) {
                this.ldSbrSamplingRate = bitReaderBuffer.readBool();
                this.ldSbrCrcFlag = bitReaderBuffer.readBool();
                ld_sbr_header(channelConfiguration, bitReaderBuffer);
            }
            while (bitReaderBuffer.readBits(4) != 0) {
                int eldExtLen = bitReaderBuffer.readBits(4);
                int len = eldExtLen;
                int eldExtLenAdd = 0;
                if (eldExtLen == 15) {
                    eldExtLenAdd = bitReaderBuffer.readBits(8);
                    len += eldExtLenAdd;
                }
                len = eldExtLenAdd == 255 ? len + bitReaderBuffer.readBits(16) : len;
                for (int cnt = 0; cnt < len; cnt++) {
                    bitReaderBuffer.readBits(8);
                }
            }
        }

        public void ld_sbr_header(int channelConfiguration, BitReaderBuffer bitReaderBuffer) {
            int numSbrHeader;
            switch (channelConfiguration) {
                case 1:
                case 2:
                    numSbrHeader = 1;
                    break;
                case 3:
                    numSbrHeader = 2;
                    break;
                case 4:
                case 5:
                case 6:
                    numSbrHeader = 3;
                    break;
                case 7:
                    numSbrHeader = 4;
                    break;
                default:
                    numSbrHeader = 0;
                    break;
            }
            for (int el = 0; el < numSbrHeader; el++) {
                new sbr_header(bitReaderBuffer);
            }
        }
    }

    public class sbr_header {
        public boolean bs_alter_scale;
        public boolean bs_amp_res;
        public int bs_freq_scale;
        public boolean bs_header_extra_1;
        public boolean bs_header_extra_2;
        public boolean bs_interpol_freq;
        public int bs_limiter_bands;
        public int bs_limiter_gains;
        public int bs_noise_bands;
        public int bs_reserved;
        public boolean bs_smoothing_mode;
        public int bs_start_freq;
        public int bs_stop_freq;
        public int bs_xover_band;

        public sbr_header(BitReaderBuffer b) {
            this.bs_amp_res = b.readBool();
            this.bs_start_freq = b.readBits(4);
            this.bs_stop_freq = b.readBits(4);
            this.bs_xover_band = b.readBits(3);
            this.bs_reserved = b.readBits(2);
            this.bs_header_extra_1 = b.readBool();
            this.bs_header_extra_2 = b.readBool();
            if (this.bs_header_extra_1) {
                this.bs_freq_scale = b.readBits(2);
                this.bs_alter_scale = b.readBool();
                this.bs_noise_bands = b.readBits(2);
            }
            if (this.bs_header_extra_2) {
                this.bs_limiter_bands = b.readBits(2);
                this.bs_limiter_gains = b.readBits(2);
                this.bs_interpol_freq = b.readBool();
            }
            this.bs_smoothing_mode = b.readBool();
        }
    }
}
