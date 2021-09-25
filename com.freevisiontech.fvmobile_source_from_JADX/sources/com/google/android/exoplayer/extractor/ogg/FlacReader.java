package com.google.android.exoplayer.extractor.ogg;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.FlacSeekTable;
import com.google.android.exoplayer.util.FlacStreamInfo;
import com.google.android.exoplayer.util.FlacUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

final class FlacReader extends StreamReader {
    private static final byte AUDIO_PACKET_TYPE = -1;
    private static final byte SEEKTABLE_PACKET_TYPE = 3;
    private boolean firstAudioPacketProcessed;
    private FlacSeekTable seekTable;
    private FlacStreamInfo streamInfo;

    FlacReader() {
    }

    static boolean verifyBitstreamType(ParsableByteArray data) {
        return data.readUnsignedByte() == 127 && data.readUnsignedInt() == 1179402563;
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        long position = input.getPosition();
        if (!this.oggParser.readPacket(input, this.scratch)) {
            return -1;
        }
        byte[] data = this.scratch.data;
        if (this.streamInfo == null) {
            this.streamInfo = new FlacStreamInfo(data, 17);
            byte[] metadata = Arrays.copyOfRange(data, 9, this.scratch.limit());
            metadata[4] = Byte.MIN_VALUE;
            this.trackOutput.format(MediaFormat.createAudioFormat((String) null, MimeTypes.AUDIO_FLAC, this.streamInfo.bitRate(), -1, this.streamInfo.durationUs(), this.streamInfo.channels, this.streamInfo.sampleRate, Collections.singletonList(metadata), (String) null));
        } else if (data[0] == -1) {
            if (!this.firstAudioPacketProcessed) {
                if (this.seekTable != null) {
                    this.extractorOutput.seekMap(this.seekTable.createSeekMap(position, (long) this.streamInfo.sampleRate));
                    this.seekTable = null;
                } else {
                    this.extractorOutput.seekMap(SeekMap.UNSEEKABLE);
                }
                this.firstAudioPacketProcessed = true;
            }
            this.trackOutput.sampleData(this.scratch, this.scratch.limit());
            this.scratch.setPosition(0);
            this.trackOutput.sampleMetadata(FlacUtil.extractSampleTimestamp(this.streamInfo, this.scratch), 1, this.scratch.limit(), 0, (byte[]) null);
        } else if ((data[0] & Byte.MAX_VALUE) == 3 && this.seekTable == null) {
            this.seekTable = FlacSeekTable.parseSeekTable(this.scratch);
        }
        this.scratch.reset();
        return 0;
    }
}
