package com.googlecode.mp4parser.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ChannelHelper {
    private static ByteBuffer empty = ByteBuffer.allocate(0).asReadOnlyBuffer();

    public static void readFully(ReadableByteChannel channel, ByteBuffer buf) throws IOException {
        readFully(channel, buf, buf.remaining());
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x000a  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0017 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int readFully(java.nio.channels.ReadableByteChannel r4, java.nio.ByteBuffer r5, int r6) throws java.io.IOException {
        /*
            r2 = -1
            r0 = 0
        L_0x0002:
            int r1 = r4.read(r5)
            if (r2 != r1) goto L_0x0013
        L_0x0008:
            if (r1 != r2) goto L_0x0017
            java.io.EOFException r2 = new java.io.EOFException
            java.lang.String r3 = "End of file. No more boxes."
            r2.<init>(r3)
            throw r2
        L_0x0013:
            int r0 = r0 + r1
            if (r0 != r6) goto L_0x0002
            goto L_0x0008
        L_0x0017:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.mp4parser.util.ChannelHelper.readFully(java.nio.channels.ReadableByteChannel, java.nio.ByteBuffer, int):int");
    }
}
