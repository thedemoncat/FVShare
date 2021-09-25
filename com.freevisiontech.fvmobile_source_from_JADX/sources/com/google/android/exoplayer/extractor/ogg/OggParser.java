package com.google.android.exoplayer.extractor.ogg;

import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ogg.OggUtil;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

final class OggParser {
    public static final int OGG_MAX_SEGMENT_SIZE = 255;
    private int currentSegmentIndex = -1;
    private long elapsedSamples;
    private final ParsableByteArray headerArray = new ParsableByteArray((int) CompanyIdentifierResolver.QUALCOMM_LABS_INC);
    private final OggUtil.PacketInfoHolder holder = new OggUtil.PacketInfoHolder();
    private final OggUtil.PageHeader pageHeader = new OggUtil.PageHeader();

    OggParser() {
    }

    public void reset() {
        this.pageHeader.reset();
        this.headerArray.reset();
        this.currentSegmentIndex = -1;
    }

    public boolean readPacket(ExtractorInput input, ParsableByteArray packetArray) throws IOException, InterruptedException {
        boolean z;
        if (input == null || packetArray == null) {
            z = false;
        } else {
            z = true;
        }
        Assertions.checkState(z);
        boolean packetComplete = false;
        while (!packetComplete) {
            if (this.currentSegmentIndex < 0) {
                if (!OggUtil.populatePageHeader(input, this.pageHeader, this.headerArray, true)) {
                    return false;
                }
                int segmentIndex = 0;
                int bytesToSkip = this.pageHeader.headerSize;
                if ((this.pageHeader.type & 1) == 1 && packetArray.limit() == 0) {
                    OggUtil.calculatePacketSize(this.pageHeader, 0, this.holder);
                    segmentIndex = 0 + this.holder.segmentCount;
                    bytesToSkip += this.holder.size;
                }
                input.skipFully(bytesToSkip);
                this.currentSegmentIndex = segmentIndex;
            }
            OggUtil.calculatePacketSize(this.pageHeader, this.currentSegmentIndex, this.holder);
            int segmentIndex2 = this.currentSegmentIndex + this.holder.segmentCount;
            if (this.holder.size > 0) {
                input.readFully(packetArray.data, packetArray.limit(), this.holder.size);
                packetArray.setLimit(packetArray.limit() + this.holder.size);
                if (this.pageHeader.laces[segmentIndex2 - 1] != 255) {
                    packetComplete = true;
                } else {
                    packetComplete = false;
                }
            }
            if (segmentIndex2 == this.pageHeader.pageSegmentCount) {
                segmentIndex2 = -1;
            }
            this.currentSegmentIndex = segmentIndex2;
        }
        return true;
    }

    public long readGranuleOfLastPage(ExtractorInput input) throws IOException, InterruptedException {
        Assertions.checkArgument(input.getLength() != -1);
        OggUtil.skipToNextPage(input);
        this.pageHeader.reset();
        while ((this.pageHeader.type & 4) != 4 && input.getPosition() < input.getLength()) {
            OggUtil.populatePageHeader(input, this.pageHeader, this.headerArray, false);
            input.skipFully(this.pageHeader.headerSize + this.pageHeader.bodySize);
        }
        return this.pageHeader.granulePosition;
    }

    public long skipToPageOfGranule(ExtractorInput input, long targetGranule) throws IOException, InterruptedException {
        OggUtil.skipToNextPage(input);
        OggUtil.populatePageHeader(input, this.pageHeader, this.headerArray, false);
        while (this.pageHeader.granulePosition < targetGranule) {
            input.skipFully(this.pageHeader.headerSize + this.pageHeader.bodySize);
            this.elapsedSamples = this.pageHeader.granulePosition;
            OggUtil.populatePageHeader(input, this.pageHeader, this.headerArray, false);
        }
        if (this.elapsedSamples == 0) {
            throw new ParserException();
        }
        input.resetPeekPosition();
        long returnValue = this.elapsedSamples;
        this.elapsedSamples = 0;
        this.currentSegmentIndex = -1;
        return returnValue;
    }

    public OggUtil.PageHeader getPageHeader() {
        return this.pageHeader;
    }
}
