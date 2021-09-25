package com.google.android.exoplayer.extractor.ogg;

import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.TrackOutput;
import java.io.IOException;

public class OggExtractor implements Extractor {
    private StreamReader streamReader;

    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return false;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sniff(com.google.android.exoplayer.extractor.ExtractorInput r8) throws java.io.IOException, java.lang.InterruptedException {
        /*
            r7 = this;
            r6 = 7
            r3 = 1
            r2 = 0
            com.google.android.exoplayer.util.ParsableByteArray r1 = new com.google.android.exoplayer.util.ParsableByteArray     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r4 = 27
            byte[] r4 = new byte[r4]     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r5 = 0
            r1.<init>(r4, r5)     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            com.google.android.exoplayer.extractor.ogg.OggUtil$PageHeader r0 = new com.google.android.exoplayer.extractor.ogg.OggUtil$PageHeader     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r0.<init>()     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r4 = 1
            boolean r4 = com.google.android.exoplayer.extractor.ogg.OggUtil.populatePageHeader(r8, r0, r1, r4)     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            if (r4 == 0) goto L_0x0024
            int r4 = r0.type     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r4 = r4 & 2
            r5 = 2
            if (r4 != r5) goto L_0x0024
            int r4 = r0.bodySize     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            if (r4 >= r6) goto L_0x0025
        L_0x0024:
            return r2
        L_0x0025:
            r1.reset()     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            byte[] r4 = r1.data     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r5 = 0
            r6 = 7
            r8.peekFully(r4, r5, r6)     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            boolean r4 = com.google.android.exoplayer.extractor.ogg.FlacReader.verifyBitstreamType(r1)     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            if (r4 == 0) goto L_0x003e
            com.google.android.exoplayer.extractor.ogg.FlacReader r4 = new com.google.android.exoplayer.extractor.ogg.FlacReader     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r4.<init>()     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r7.streamReader = r4     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
        L_0x003c:
            r2 = r3
            goto L_0x0024
        L_0x003e:
            r1.reset()     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            boolean r4 = com.google.android.exoplayer.extractor.ogg.VorbisReader.verifyBitstreamType(r1)     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            if (r4 == 0) goto L_0x0024
            com.google.android.exoplayer.extractor.ogg.VorbisReader r4 = new com.google.android.exoplayer.extractor.ogg.VorbisReader     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r4.<init>()     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            r7.streamReader = r4     // Catch:{ ParserException -> 0x004f, all -> 0x0051 }
            goto L_0x003c
        L_0x004f:
            r3 = move-exception
            goto L_0x0024
        L_0x0051:
            r2 = move-exception
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.extractor.ogg.OggExtractor.sniff(com.google.android.exoplayer.extractor.ExtractorInput):boolean");
    }

    public void init(ExtractorOutput output) {
        TrackOutput trackOutput = output.track(0);
        output.endTracks();
        this.streamReader.init(output, trackOutput);
    }

    public void seek() {
        this.streamReader.seek();
    }

    public void release() {
    }

    public int read(ExtractorInput input, PositionHolder seekPosition) throws IOException, InterruptedException {
        return this.streamReader.read(input, seekPosition);
    }
}
