package com.googlecode.mp4parser.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class UUIDConverter {
    public static byte[] convert(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];
        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) ((int) (msb >>> ((7 - i) * 8)));
        }
        for (int i2 = 8; i2 < 16; i2++) {
            buffer[i2] = (byte) ((int) (lsb >>> ((7 - i2) * 8)));
        }
        return buffer;
    }

    public static UUID convert(byte[] uuidBytes) {
        ByteBuffer b = ByteBuffer.wrap(uuidBytes);
        b.order(ByteOrder.BIG_ENDIAN);
        return new UUID(b.getLong(), b.getLong());
    }
}
