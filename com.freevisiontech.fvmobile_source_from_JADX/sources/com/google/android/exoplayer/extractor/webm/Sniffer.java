package com.google.android.exoplayer.extractor.webm;

import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.util.ParsableByteArray;
import java.io.IOException;

final class Sniffer {
    private static final int ID_EBML = 440786851;
    private static final int SEARCH_LENGTH = 1024;
    private int peekLength;
    private final ParsableByteArray scratch = new ParsableByteArray(8);

    /* JADX WARNING: CFG modification limit reached, blocks count: 145 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sniff(com.google.android.exoplayer.extractor.ExtractorInput r23) throws java.io.IOException, java.lang.InterruptedException {
        /*
            r22 = this;
            long r12 = r23.getLength()
            r18 = -1
            int r5 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x0010
            r18 = 1024(0x400, double:5.06E-321)
            int r5 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
            if (r5 <= 0) goto L_0x004a
        L_0x0010:
            r18 = 1024(0x400, double:5.06E-321)
        L_0x0012:
            r0 = r18
            int r4 = (int) r0
            r0 = r22
            com.google.android.exoplayer.util.ParsableByteArray r5 = r0.scratch
            byte[] r5 = r5.data
            r18 = 0
            r19 = 4
            r0 = r23
            r1 = r18
            r2 = r19
            r0.peekFully(r5, r1, r2)
            r0 = r22
            com.google.android.exoplayer.util.ParsableByteArray r5 = r0.scratch
            long r16 = r5.readUnsignedInt()
            r5 = 4
            r0 = r22
            r0.peekLength = r5
        L_0x0035:
            r18 = 440786851(0x1a45dfa3, double:2.1777764E-315)
            int r5 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x007a
            r0 = r22
            int r5 = r0.peekLength
            int r5 = r5 + 1
            r0 = r22
            r0.peekLength = r5
            if (r5 != r4) goto L_0x004d
            r5 = 0
        L_0x0049:
            return r5
        L_0x004a:
            r18 = r12
            goto L_0x0012
        L_0x004d:
            r0 = r22
            com.google.android.exoplayer.util.ParsableByteArray r5 = r0.scratch
            byte[] r5 = r5.data
            r18 = 0
            r19 = 1
            r0 = r23
            r1 = r18
            r2 = r19
            r0.peekFully(r5, r1, r2)
            r5 = 8
            long r18 = r16 << r5
            r20 = -256(0xffffffffffffff00, double:NaN)
            long r16 = r18 & r20
            r0 = r22
            com.google.android.exoplayer.util.ParsableByteArray r5 = r0.scratch
            byte[] r5 = r5.data
            r18 = 0
            byte r5 = r5[r18]
            r5 = r5 & 255(0xff, float:3.57E-43)
            long r0 = (long) r5
            r18 = r0
            long r16 = r16 | r18
            goto L_0x0035
        L_0x007a:
            long r6 = r22.readUint(r23)
            r0 = r22
            int r5 = r0.peekLength
            long r8 = (long) r5
            r18 = -9223372036854775808
            int r5 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x0095
            r18 = -1
            int r5 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x00b3
            long r18 = r8 + r6
            int r5 = (r18 > r12 ? 1 : (r18 == r12 ? 0 : -1))
            if (r5 < 0) goto L_0x00b3
        L_0x0095:
            r5 = 0
            goto L_0x0049
        L_0x0097:
            r18 = 0
            int r5 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x00b3
            int r5 = (int) r14
            r0 = r23
            r0.advancePeekPosition(r5)
            r0 = r22
            int r5 = r0.peekLength
            long r0 = (long) r5
            r18 = r0
            long r18 = r18 + r14
            r0 = r18
            int r5 = (int) r0
            r0 = r22
            r0.peekLength = r5
        L_0x00b3:
            r0 = r22
            int r5 = r0.peekLength
            long r0 = (long) r5
            r18 = r0
            long r20 = r8 + r6
            int r5 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1))
            if (r5 >= 0) goto L_0x00e1
            long r10 = r22.readUint(r23)
            r18 = -9223372036854775808
            int r5 = (r10 > r18 ? 1 : (r10 == r18 ? 0 : -1))
            if (r5 != 0) goto L_0x00cd
            r5 = 0
            goto L_0x0049
        L_0x00cd:
            long r14 = r22.readUint(r23)
            r18 = 0
            int r5 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1))
            if (r5 < 0) goto L_0x00de
            r18 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r5 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1))
            if (r5 <= 0) goto L_0x0097
        L_0x00de:
            r5 = 0
            goto L_0x0049
        L_0x00e1:
            r0 = r22
            int r5 = r0.peekLength
            long r0 = (long) r5
            r18 = r0
            long r20 = r8 + r6
            int r5 = (r18 > r20 ? 1 : (r18 == r20 ? 0 : -1))
            if (r5 != 0) goto L_0x00f1
            r5 = 1
            goto L_0x0049
        L_0x00f1:
            r5 = 0
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.extractor.webm.Sniffer.sniff(com.google.android.exoplayer.extractor.ExtractorInput):boolean");
    }

    private long readUint(ExtractorInput input) throws IOException, InterruptedException {
        input.peekFully(this.scratch.data, 0, 1);
        int value = this.scratch.data[0] & 255;
        if (value == 0) {
            return Long.MIN_VALUE;
        }
        int mask = 128;
        int length = 0;
        while ((value & mask) == 0) {
            mask >>= 1;
            length++;
        }
        int value2 = value & (mask ^ -1);
        input.peekFully(this.scratch.data, 1, length);
        for (int i = 0; i < length; i++) {
            value2 = (value2 << 8) + (this.scratch.data[i + 1] & 255);
        }
        this.peekLength += length + 1;
        return (long) value2;
    }
}
