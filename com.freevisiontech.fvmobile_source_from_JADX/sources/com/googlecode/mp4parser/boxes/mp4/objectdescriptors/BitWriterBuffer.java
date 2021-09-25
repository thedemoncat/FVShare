package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import android.support.p001v4.view.InputDeviceCompat;
import java.nio.ByteBuffer;

public class BitWriterBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = (!BitWriterBuffer.class.desiredAssertionStatus());
    private ByteBuffer buffer;
    int initialPos;
    int position = 0;

    public BitWriterBuffer(ByteBuffer buffer2) {
        this.buffer = buffer2;
        this.initialPos = buffer2.position();
    }

    public void writeBool(boolean b) {
        writeBits(b ? 1 : 0, 1);
    }

    public void writeBits(int i, int numBits) {
        int i2 = 1;
        if ($assertionsDisabled || i <= (1 << numBits) - 1) {
            int left = 8 - (this.position % 8);
            if (numBits <= left) {
                int current = this.buffer.get(this.initialPos + (this.position / 8));
                if (current < 0) {
                    current += 256;
                }
                int current2 = current + (i << (left - numBits));
                ByteBuffer byteBuffer = this.buffer;
                int i3 = this.initialPos + (this.position / 8);
                if (current2 > 127) {
                    current2 += InputDeviceCompat.SOURCE_ANY;
                }
                byteBuffer.put(i3, (byte) current2);
                this.position += numBits;
            } else {
                int bitsSecondWrite = numBits - left;
                writeBits(i >> bitsSecondWrite, left);
                writeBits(((1 << bitsSecondWrite) - 1) & i, bitsSecondWrite);
            }
            ByteBuffer byteBuffer2 = this.buffer;
            int i4 = this.initialPos + (this.position / 8);
            if (this.position % 8 <= 0) {
                i2 = 0;
            }
            byteBuffer2.position(i2 + i4);
            return;
        }
        throw new AssertionError(String.format("Trying to write a value bigger (%s) than the number bits (%s) allows. Please mask the value before writing it and make your code is really working as intended.", new Object[]{Integer.valueOf(i), Integer.valueOf((1 << numBits) - 1)}));
    }
}
