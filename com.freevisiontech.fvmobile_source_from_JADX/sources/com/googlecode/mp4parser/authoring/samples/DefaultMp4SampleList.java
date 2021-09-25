package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleToChunkBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class DefaultMp4SampleList extends AbstractList<Sample> {
    /* access modifiers changed from: private */
    public static final Logger LOG = Logger.getLogger(DefaultMp4SampleList.class);
    SoftReference<ByteBuffer>[] cache = null;
    int[] chunkNumsStartSampleNum;
    long[] chunkOffsets;
    long[] chunkSizes;
    int lastChunk = 0;
    long[][] sampleOffsetsWithinChunks;
    SampleSizeBox ssb;
    Container topLevel;
    TrackBox trackBox = null;

    public DefaultMp4SampleList(long track, Container topLevel2) {
        int s2cIndex;
        int currentChunkNo;
        this.topLevel = topLevel2;
        for (TrackBox tb : topLevel2.getBoxes(MovieBox.class).get(0).getBoxes(TrackBox.class)) {
            if (tb.getTrackHeaderBox().getTrackId() == track) {
                this.trackBox = tb;
            }
        }
        if (this.trackBox == null) {
            throw new RuntimeException("This MP4 does not contain track " + track);
        }
        this.chunkOffsets = this.trackBox.getSampleTableBox().getChunkOffsetBox().getChunkOffsets();
        this.chunkSizes = new long[this.chunkOffsets.length];
        this.cache = new SoftReference[this.chunkOffsets.length];
        Arrays.fill(this.cache, new SoftReference((Object) null));
        this.sampleOffsetsWithinChunks = new long[this.chunkOffsets.length][];
        this.ssb = this.trackBox.getSampleTableBox().getSampleSizeBox();
        List<SampleToChunkBox.Entry> s2chunkEntries = this.trackBox.getSampleTableBox().getSampleToChunkBox().getEntries();
        SampleToChunkBox.Entry[] entries = (SampleToChunkBox.Entry[]) s2chunkEntries.toArray(new SampleToChunkBox.Entry[s2chunkEntries.size()]);
        int s2cIndex2 = 0 + 1;
        SampleToChunkBox.Entry next = entries[0];
        int currentChunkNo2 = 0;
        int currentSamplePerChunk = 0;
        long nextFirstChunk = next.getFirstChunk();
        int nextSamplePerChunk = CastUtils.l2i(next.getSamplesPerChunk());
        int currentSampleNo = 1;
        int lastSampleNo = size();
        while (true) {
            currentChunkNo2++;
            if (((long) currentChunkNo2) == nextFirstChunk) {
                currentSamplePerChunk = nextSamplePerChunk;
                if (entries.length > s2cIndex2) {
                    s2cIndex = s2cIndex2 + 1;
                    SampleToChunkBox.Entry next2 = entries[s2cIndex2];
                    nextSamplePerChunk = CastUtils.l2i(next2.getSamplesPerChunk());
                    nextFirstChunk = next2.getFirstChunk();
                } else {
                    nextSamplePerChunk = -1;
                    nextFirstChunk = Long.MAX_VALUE;
                    s2cIndex = s2cIndex2;
                }
            } else {
                s2cIndex = s2cIndex2;
            }
            this.sampleOffsetsWithinChunks[currentChunkNo2 - 1] = new long[currentSamplePerChunk];
            currentSampleNo += currentSamplePerChunk;
            if (currentSampleNo > lastSampleNo) {
                break;
            }
            s2cIndex2 = s2cIndex;
        }
        this.chunkNumsStartSampleNum = new int[(currentChunkNo2 + 1)];
        SampleToChunkBox.Entry next3 = entries[0];
        int currentChunkNo3 = 0;
        int currentSamplePerChunk2 = 0;
        long nextFirstChunk2 = next3.getFirstChunk();
        int nextSamplePerChunk2 = CastUtils.l2i(next3.getSamplesPerChunk());
        int currentSampleNo2 = 1;
        int s2cIndex3 = 0 + 1;
        while (true) {
            currentChunkNo = currentChunkNo3 + 1;
            this.chunkNumsStartSampleNum[currentChunkNo3] = currentSampleNo2;
            if (((long) currentChunkNo) == nextFirstChunk2) {
                currentSamplePerChunk2 = nextSamplePerChunk2;
                if (entries.length > s2cIndex3) {
                    SampleToChunkBox.Entry next4 = entries[s2cIndex3];
                    nextSamplePerChunk2 = CastUtils.l2i(next4.getSamplesPerChunk());
                    nextFirstChunk2 = next4.getFirstChunk();
                    s2cIndex3++;
                } else {
                    nextSamplePerChunk2 = -1;
                    nextFirstChunk2 = Long.MAX_VALUE;
                }
            }
            currentSampleNo2 += currentSamplePerChunk2;
            if (currentSampleNo2 > lastSampleNo) {
                break;
            }
            currentChunkNo3 = currentChunkNo;
        }
        this.chunkNumsStartSampleNum[currentChunkNo] = Integer.MAX_VALUE;
        int currentChunkNo4 = 0;
        long sampleSum = 0;
        for (int i = 1; ((long) i) <= this.ssb.getSampleCount(); i++) {
            while (i == this.chunkNumsStartSampleNum[currentChunkNo4]) {
                currentChunkNo4++;
                sampleSum = 0;
            }
            long[] jArr = this.chunkSizes;
            int i2 = currentChunkNo4 - 1;
            jArr[i2] = jArr[i2] + this.ssb.getSampleSizeAtIndex(i - 1);
            this.sampleOffsetsWithinChunks[currentChunkNo4 - 1][i - this.chunkNumsStartSampleNum[currentChunkNo4 - 1]] = sampleSum;
            sampleSum += this.ssb.getSampleSizeAtIndex(i - 1);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized int getChunkForSample(int index) {
        int i;
        int sampleNum = index + 1;
        if (sampleNum >= this.chunkNumsStartSampleNum[this.lastChunk] && sampleNum < this.chunkNumsStartSampleNum[this.lastChunk + 1]) {
            i = this.lastChunk;
        } else if (sampleNum < this.chunkNumsStartSampleNum[this.lastChunk]) {
            this.lastChunk = 0;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            i = this.lastChunk;
        } else {
            this.lastChunk++;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            i = this.lastChunk;
        }
        return i;
    }

    public Sample get(int index) {
        if (((long) index) < this.ssb.getSampleCount()) {
            return new SampleImpl(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public int size() {
        return CastUtils.l2i(this.trackBox.getSampleTableBox().getSampleSizeBox().getSampleCount());
    }

    class SampleImpl implements Sample {
        private int index;

        public SampleImpl(int index2) {
            this.index = index2;
        }

        public void writeTo(WritableByteChannel channel) throws IOException {
            channel.write(asByteBuffer());
        }

        public long getSize() {
            return DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index);
        }

        public synchronized ByteBuffer asByteBuffer() {
            long offsetWithInChunk;
            ByteBuffer chunkBuffer;
            int chunkNumber = DefaultMp4SampleList.this.getChunkForSample(this.index);
            SoftReference<ByteBuffer> chunkBufferSr = DefaultMp4SampleList.this.cache[chunkNumber];
            int chunkStartSample = DefaultMp4SampleList.this.chunkNumsStartSampleNum[chunkNumber] - 1;
            int sampleInChunk = this.index - chunkStartSample;
            long[] sampleOffsetsWithinChunk = DefaultMp4SampleList.this.sampleOffsetsWithinChunks[CastUtils.l2i((long) chunkNumber)];
            offsetWithInChunk = sampleOffsetsWithinChunk[sampleInChunk];
            if (chunkBufferSr == null || (chunkBuffer = chunkBufferSr.get()) == null) {
                try {
                    chunkBuffer = DefaultMp4SampleList.this.topLevel.getByteBuffer(DefaultMp4SampleList.this.chunkOffsets[CastUtils.l2i((long) chunkNumber)], sampleOffsetsWithinChunk[sampleOffsetsWithinChunk.length - 1] + DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex((sampleOffsetsWithinChunk.length + chunkStartSample) - 1));
                    DefaultMp4SampleList.this.cache[chunkNumber] = new SoftReference<>(chunkBuffer);
                } catch (IOException e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    DefaultMp4SampleList.LOG.logError(sw.toString());
                    throw new IndexOutOfBoundsException(e.getMessage());
                }
            }
            return (ByteBuffer) ((ByteBuffer) chunkBuffer.duplicate().position(CastUtils.l2i(offsetWithInChunk))).slice().limit(CastUtils.l2i(DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index)));
        }

        public String toString() {
            return "Sample(index: " + this.index + " size: " + DefaultMp4SampleList.this.ssb.getSampleSizeAtIndex(this.index) + ")";
        }
    }
}
