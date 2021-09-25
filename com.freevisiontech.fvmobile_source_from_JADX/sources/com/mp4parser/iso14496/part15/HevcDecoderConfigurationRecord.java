package com.mp4parser.iso14496.part15;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class HevcDecoderConfigurationRecord {
    List<Array> arrays = new ArrayList();
    int avgFrameRate;
    int bitDepthChromaMinus8;
    int bitDepthLumaMinus8;
    int chromaFormat;
    int configurationVersion;
    int constantFrameRate;
    boolean frame_only_constraint_flag;
    long general_constraint_indicator_flags;
    int general_level_idc;
    long general_profile_compatibility_flags;
    int general_profile_idc;
    int general_profile_space;
    boolean general_tier_flag;
    boolean interlaced_source_flag;
    int lengthSizeMinusOne;
    int min_spatial_segmentation_idc;
    boolean non_packed_constraint_flag;
    int numTemporalLayers;
    int parallelismType;
    boolean progressive_source_flag;
    int reserved1 = 15;
    int reserved2 = 63;
    int reserved3 = 63;
    int reserved4 = 31;
    int reserved5 = 31;
    boolean temporalIdNested;

    public void parse(ByteBuffer content) {
        this.configurationVersion = IsoTypeReader.readUInt8(content);
        int a = IsoTypeReader.readUInt8(content);
        this.general_profile_space = (a & 192) >> 6;
        this.general_tier_flag = (a & 32) > 0;
        this.general_profile_idc = a & 31;
        this.general_profile_compatibility_flags = IsoTypeReader.readUInt32(content);
        this.general_constraint_indicator_flags = IsoTypeReader.readUInt48(content);
        this.frame_only_constraint_flag = ((this.general_constraint_indicator_flags >> 44) & 8) > 0;
        this.non_packed_constraint_flag = ((this.general_constraint_indicator_flags >> 44) & 4) > 0;
        this.interlaced_source_flag = ((this.general_constraint_indicator_flags >> 44) & 2) > 0;
        this.progressive_source_flag = ((this.general_constraint_indicator_flags >> 44) & 1) > 0;
        this.general_constraint_indicator_flags &= 140737488355327L;
        this.general_level_idc = IsoTypeReader.readUInt8(content);
        int a2 = IsoTypeReader.readUInt16(content);
        this.reserved1 = (61440 & a2) >> 12;
        this.min_spatial_segmentation_idc = a2 & 4095;
        int a3 = IsoTypeReader.readUInt8(content);
        this.reserved2 = (a3 & CompanyIdentifierResolver.DELPHI_CORPORATION) >> 2;
        this.parallelismType = a3 & 3;
        int a4 = IsoTypeReader.readUInt8(content);
        this.reserved3 = (a4 & CompanyIdentifierResolver.DELPHI_CORPORATION) >> 2;
        this.chromaFormat = a4 & 3;
        int a5 = IsoTypeReader.readUInt8(content);
        this.reserved4 = (a5 & CompanyIdentifierResolver.ACEUNI_CORP_LTD) >> 3;
        this.bitDepthLumaMinus8 = a5 & 7;
        int a6 = IsoTypeReader.readUInt8(content);
        this.reserved5 = (a6 & CompanyIdentifierResolver.ACEUNI_CORP_LTD) >> 3;
        this.bitDepthChromaMinus8 = a6 & 7;
        this.avgFrameRate = IsoTypeReader.readUInt16(content);
        int a7 = IsoTypeReader.readUInt8(content);
        this.constantFrameRate = (a7 & 192) >> 6;
        this.numTemporalLayers = (a7 & 56) >> 3;
        this.temporalIdNested = (a7 & 4) > 0;
        this.lengthSizeMinusOne = a7 & 3;
        int numOfArrays = IsoTypeReader.readUInt8(content);
        this.arrays = new ArrayList();
        for (int i = 0; i < numOfArrays; i++) {
            Array array = new Array();
            int a8 = IsoTypeReader.readUInt8(content);
            array.array_completeness = (a8 & 128) > 0;
            array.reserved = (a8 & 64) > 0;
            array.nal_unit_type = a8 & 63;
            int numNalus = IsoTypeReader.readUInt16(content);
            array.nalUnits = new ArrayList();
            for (int j = 0; j < numNalus; j++) {
                byte[] nal = new byte[IsoTypeReader.readUInt16(content)];
                content.get(nal);
                array.nalUnits.add(nal);
            }
            this.arrays.add(array);
        }
    }

    public void write(ByteBuffer byteBuffer) {
        int i;
        int i2;
        int i3;
        IsoTypeWriter.writeUInt8(byteBuffer, this.configurationVersion);
        int i4 = this.general_profile_space << 6;
        if (this.general_tier_flag) {
            i = 32;
        } else {
            i = 0;
        }
        IsoTypeWriter.writeUInt8(byteBuffer, i + i4 + this.general_profile_idc);
        IsoTypeWriter.writeUInt32(byteBuffer, this.general_profile_compatibility_flags);
        long _general_constraint_indicator_flags = this.general_constraint_indicator_flags;
        if (this.frame_only_constraint_flag) {
            _general_constraint_indicator_flags |= 140737488355328L;
        }
        if (this.non_packed_constraint_flag) {
            _general_constraint_indicator_flags |= 70368744177664L;
        }
        if (this.interlaced_source_flag) {
            _general_constraint_indicator_flags |= 35184372088832L;
        }
        if (this.progressive_source_flag) {
            _general_constraint_indicator_flags |= 17592186044416L;
        }
        IsoTypeWriter.writeUInt48(byteBuffer, _general_constraint_indicator_flags);
        IsoTypeWriter.writeUInt8(byteBuffer, this.general_level_idc);
        IsoTypeWriter.writeUInt16(byteBuffer, (this.reserved1 << 12) + this.min_spatial_segmentation_idc);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved2 << 2) + this.parallelismType);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved3 << 2) + this.chromaFormat);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved4 << 3) + this.bitDepthLumaMinus8);
        IsoTypeWriter.writeUInt8(byteBuffer, (this.reserved5 << 3) + this.bitDepthChromaMinus8);
        IsoTypeWriter.writeUInt16(byteBuffer, this.avgFrameRate);
        int i5 = (this.numTemporalLayers << 3) + (this.constantFrameRate << 6);
        if (this.temporalIdNested) {
            i2 = 4;
        } else {
            i2 = 0;
        }
        IsoTypeWriter.writeUInt8(byteBuffer, i2 + i5 + this.lengthSizeMinusOne);
        IsoTypeWriter.writeUInt8(byteBuffer, this.arrays.size());
        for (Array array : this.arrays) {
            if (array.array_completeness) {
                i3 = 128;
            } else {
                i3 = 0;
            }
            IsoTypeWriter.writeUInt8(byteBuffer, i3 + (array.reserved ? 64 : 0) + array.nal_unit_type);
            IsoTypeWriter.writeUInt16(byteBuffer, array.nalUnits.size());
            for (byte[] nalUnit : array.nalUnits) {
                IsoTypeWriter.writeUInt16(byteBuffer, nalUnit.length);
                byteBuffer.put(nalUnit);
            }
        }
    }

    public int getSize() {
        int size = 23;
        for (Array array : this.arrays) {
            size += 3;
            for (byte[] nalUnit : array.nalUnits) {
                size = size + 2 + nalUnit.length;
            }
        }
        return size;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HevcDecoderConfigurationRecord that = (HevcDecoderConfigurationRecord) o;
        if (this.avgFrameRate != that.avgFrameRate) {
            return false;
        }
        if (this.bitDepthChromaMinus8 != that.bitDepthChromaMinus8) {
            return false;
        }
        if (this.bitDepthLumaMinus8 != that.bitDepthLumaMinus8) {
            return false;
        }
        if (this.chromaFormat != that.chromaFormat) {
            return false;
        }
        if (this.configurationVersion != that.configurationVersion) {
            return false;
        }
        if (this.constantFrameRate != that.constantFrameRate) {
            return false;
        }
        if (this.general_constraint_indicator_flags != that.general_constraint_indicator_flags) {
            return false;
        }
        if (this.general_level_idc != that.general_level_idc) {
            return false;
        }
        if (this.general_profile_compatibility_flags != that.general_profile_compatibility_flags) {
            return false;
        }
        if (this.general_profile_idc != that.general_profile_idc) {
            return false;
        }
        if (this.general_profile_space != that.general_profile_space) {
            return false;
        }
        if (this.general_tier_flag != that.general_tier_flag) {
            return false;
        }
        if (this.lengthSizeMinusOne != that.lengthSizeMinusOne) {
            return false;
        }
        if (this.min_spatial_segmentation_idc != that.min_spatial_segmentation_idc) {
            return false;
        }
        if (this.numTemporalLayers != that.numTemporalLayers) {
            return false;
        }
        if (this.parallelismType != that.parallelismType) {
            return false;
        }
        if (this.reserved1 != that.reserved1) {
            return false;
        }
        if (this.reserved2 != that.reserved2) {
            return false;
        }
        if (this.reserved3 != that.reserved3) {
            return false;
        }
        if (this.reserved4 != that.reserved4) {
            return false;
        }
        if (this.reserved5 != that.reserved5) {
            return false;
        }
        if (this.temporalIdNested != that.temporalIdNested) {
            return false;
        }
        if (this.arrays != null) {
            if (this.arrays.equals(that.arrays)) {
                return true;
            }
        } else if (that.arrays == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 1;
        int i2 = 0;
        int i3 = ((((((((((((((((((((((((((((((((((((((this.configurationVersion * 31) + this.general_profile_space) * 31) + (this.general_tier_flag ? 1 : 0)) * 31) + this.general_profile_idc) * 31) + ((int) (this.general_profile_compatibility_flags ^ (this.general_profile_compatibility_flags >>> 32)))) * 31) + ((int) (this.general_constraint_indicator_flags ^ (this.general_constraint_indicator_flags >>> 32)))) * 31) + this.general_level_idc) * 31) + this.reserved1) * 31) + this.min_spatial_segmentation_idc) * 31) + this.reserved2) * 31) + this.parallelismType) * 31) + this.reserved3) * 31) + this.chromaFormat) * 31) + this.reserved4) * 31) + this.bitDepthLumaMinus8) * 31) + this.reserved5) * 31) + this.bitDepthChromaMinus8) * 31) + this.avgFrameRate) * 31) + this.constantFrameRate) * 31) + this.numTemporalLayers) * 31;
        if (!this.temporalIdNested) {
            i = 0;
        }
        int i4 = (((i3 + i) * 31) + this.lengthSizeMinusOne) * 31;
        if (this.arrays != null) {
            i2 = this.arrays.hashCode();
        }
        return i4 + i2;
    }

    public String toString() {
        return "HEVCDecoderConfigurationRecord{configurationVersion=" + this.configurationVersion + ", general_profile_space=" + this.general_profile_space + ", general_tier_flag=" + this.general_tier_flag + ", general_profile_idc=" + this.general_profile_idc + ", general_profile_compatibility_flags=" + this.general_profile_compatibility_flags + ", general_constraint_indicator_flags=" + this.general_constraint_indicator_flags + ", general_level_idc=" + this.general_level_idc + (this.reserved1 != 15 ? ", reserved1=" + this.reserved1 : "") + ", min_spatial_segmentation_idc=" + this.min_spatial_segmentation_idc + (this.reserved2 != 63 ? ", reserved2=" + this.reserved2 : "") + ", parallelismType=" + this.parallelismType + (this.reserved3 != 63 ? ", reserved3=" + this.reserved3 : "") + ", chromaFormat=" + this.chromaFormat + (this.reserved4 != 31 ? ", reserved4=" + this.reserved4 : "") + ", bitDepthLumaMinus8=" + this.bitDepthLumaMinus8 + (this.reserved5 != 31 ? ", reserved5=" + this.reserved5 : "") + ", bitDepthChromaMinus8=" + this.bitDepthChromaMinus8 + ", avgFrameRate=" + this.avgFrameRate + ", constantFrameRate=" + this.constantFrameRate + ", numTemporalLayers=" + this.numTemporalLayers + ", temporalIdNested=" + this.temporalIdNested + ", lengthSizeMinusOne=" + this.lengthSizeMinusOne + ", arrays=" + this.arrays + '}';
    }

    public int getConfigurationVersion() {
        return this.configurationVersion;
    }

    public void setConfigurationVersion(int configurationVersion2) {
        this.configurationVersion = configurationVersion2;
    }

    public int getGeneral_profile_space() {
        return this.general_profile_space;
    }

    public void setGeneral_profile_space(int general_profile_space2) {
        this.general_profile_space = general_profile_space2;
    }

    public boolean isGeneral_tier_flag() {
        return this.general_tier_flag;
    }

    public void setGeneral_tier_flag(boolean general_tier_flag2) {
        this.general_tier_flag = general_tier_flag2;
    }

    public int getGeneral_profile_idc() {
        return this.general_profile_idc;
    }

    public void setGeneral_profile_idc(int general_profile_idc2) {
        this.general_profile_idc = general_profile_idc2;
    }

    public long getGeneral_profile_compatibility_flags() {
        return this.general_profile_compatibility_flags;
    }

    public void setGeneral_profile_compatibility_flags(long general_profile_compatibility_flags2) {
        this.general_profile_compatibility_flags = general_profile_compatibility_flags2;
    }

    public long getGeneral_constraint_indicator_flags() {
        return this.general_constraint_indicator_flags;
    }

    public void setGeneral_constraint_indicator_flags(long general_constraint_indicator_flags2) {
        this.general_constraint_indicator_flags = general_constraint_indicator_flags2;
    }

    public int getGeneral_level_idc() {
        return this.general_level_idc;
    }

    public void setGeneral_level_idc(int general_level_idc2) {
        this.general_level_idc = general_level_idc2;
    }

    public int getMin_spatial_segmentation_idc() {
        return this.min_spatial_segmentation_idc;
    }

    public void setMin_spatial_segmentation_idc(int min_spatial_segmentation_idc2) {
        this.min_spatial_segmentation_idc = min_spatial_segmentation_idc2;
    }

    public int getParallelismType() {
        return this.parallelismType;
    }

    public void setParallelismType(int parallelismType2) {
        this.parallelismType = parallelismType2;
    }

    public int getChromaFormat() {
        return this.chromaFormat;
    }

    public void setChromaFormat(int chromaFormat2) {
        this.chromaFormat = chromaFormat2;
    }

    public int getBitDepthLumaMinus8() {
        return this.bitDepthLumaMinus8;
    }

    public void setBitDepthLumaMinus8(int bitDepthLumaMinus82) {
        this.bitDepthLumaMinus8 = bitDepthLumaMinus82;
    }

    public int getBitDepthChromaMinus8() {
        return this.bitDepthChromaMinus8;
    }

    public void setBitDepthChromaMinus8(int bitDepthChromaMinus82) {
        this.bitDepthChromaMinus8 = bitDepthChromaMinus82;
    }

    public int getAvgFrameRate() {
        return this.avgFrameRate;
    }

    public void setAvgFrameRate(int avgFrameRate2) {
        this.avgFrameRate = avgFrameRate2;
    }

    public int getNumTemporalLayers() {
        return this.numTemporalLayers;
    }

    public void setNumTemporalLayers(int numTemporalLayers2) {
        this.numTemporalLayers = numTemporalLayers2;
    }

    public int getLengthSizeMinusOne() {
        return this.lengthSizeMinusOne;
    }

    public void setLengthSizeMinusOne(int lengthSizeMinusOne2) {
        this.lengthSizeMinusOne = lengthSizeMinusOne2;
    }

    public boolean isTemporalIdNested() {
        return this.temporalIdNested;
    }

    public void setTemporalIdNested(boolean temporalIdNested2) {
        this.temporalIdNested = temporalIdNested2;
    }

    public int getConstantFrameRate() {
        return this.constantFrameRate;
    }

    public void setConstantFrameRate(int constantFrameRate2) {
        this.constantFrameRate = constantFrameRate2;
    }

    public List<Array> getArrays() {
        return this.arrays;
    }

    public void setArrays(List<Array> arrays2) {
        this.arrays = arrays2;
    }

    public boolean isFrame_only_constraint_flag() {
        return this.frame_only_constraint_flag;
    }

    public void setFrame_only_constraint_flag(boolean frame_only_constraint_flag2) {
        this.frame_only_constraint_flag = frame_only_constraint_flag2;
    }

    public boolean isNon_packed_constraint_flag() {
        return this.non_packed_constraint_flag;
    }

    public void setNon_packed_constraint_flag(boolean non_packed_constraint_flag2) {
        this.non_packed_constraint_flag = non_packed_constraint_flag2;
    }

    public boolean isInterlaced_source_flag() {
        return this.interlaced_source_flag;
    }

    public void setInterlaced_source_flag(boolean interlaced_source_flag2) {
        this.interlaced_source_flag = interlaced_source_flag2;
    }

    public boolean isProgressive_source_flag() {
        return this.progressive_source_flag;
    }

    public void setProgressive_source_flag(boolean progressive_source_flag2) {
        this.progressive_source_flag = progressive_source_flag2;
    }

    public static class Array {
        public boolean array_completeness;
        public List<byte[]> nalUnits;
        public int nal_unit_type;
        public boolean reserved;

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Array array = (Array) o;
            if (this.array_completeness != array.array_completeness || this.nal_unit_type != array.nal_unit_type || this.reserved != array.reserved) {
                return false;
            }
            ListIterator<byte[]> e1 = this.nalUnits.listIterator();
            ListIterator<byte[]> e2 = array.nalUnits.listIterator();
            while (e1.hasNext() && e2.hasNext()) {
                byte[] o1 = e1.next();
                byte[] o2 = e2.next();
                if (o1 == null) {
                    if (o2 != null) {
                        return false;
                    }
                } else if (!Arrays.equals(o1, o2)) {
                    return false;
                }
            }
            if (e1.hasNext() || e2.hasNext()) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int result;
            int i = 1;
            int i2 = 0;
            if (this.array_completeness) {
                result = 1;
            } else {
                result = 0;
            }
            int i3 = result * 31;
            if (!this.reserved) {
                i = 0;
            }
            int i4 = (((i3 + i) * 31) + this.nal_unit_type) * 31;
            if (this.nalUnits != null) {
                i2 = this.nalUnits.hashCode();
            }
            return i4 + i2;
        }

        public String toString() {
            return "Array{nal_unit_type=" + this.nal_unit_type + ", reserved=" + this.reserved + ", array_completeness=" + this.array_completeness + ", num_nals=" + this.nalUnits.size() + '}';
        }
    }
}
