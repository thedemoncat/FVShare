package com.google.android.exoplayer.extractor.p016ts;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.text.eia608.Eia608Parser;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.ParsableByteArray;

/* renamed from: com.google.android.exoplayer.extractor.ts.SeiReader */
final class SeiReader {
    private final TrackOutput output;

    public SeiReader(TrackOutput output2) {
        this.output = output2;
        output2.format(MediaFormat.createTextFormat((String) null, MimeTypes.APPLICATION_EIA608, -1, -1, (String) null));
    }

    public void consume(long pesTimeUs, ParsableByteArray seiBuffer) {
        int b;
        int b2;
        while (seiBuffer.bytesLeft() > 1) {
            int payloadType = 0;
            do {
                b = seiBuffer.readUnsignedByte();
                payloadType += b;
            } while (b == 255);
            int payloadSize = 0;
            do {
                b2 = seiBuffer.readUnsignedByte();
                payloadSize += b2;
            } while (b2 == 255);
            if (Eia608Parser.isSeiMessageEia608(payloadType, payloadSize, seiBuffer)) {
                this.output.sampleData(seiBuffer, payloadSize);
                this.output.sampleMetadata(pesTimeUs, 1, payloadSize, 0, (byte[]) null);
            } else {
                seiBuffer.skipBytes(payloadSize);
            }
        }
    }
}
