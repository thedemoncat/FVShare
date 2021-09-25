package com.google.android.exoplayer.util;

public final class ParsableBitArray {
    private int bitOffset;
    private int byteLimit;
    private int byteOffset;
    public byte[] data;

    public ParsableBitArray() {
    }

    public ParsableBitArray(byte[] data2) {
        this(data2, data2.length);
    }

    public ParsableBitArray(byte[] data2, int limit) {
        this.data = data2;
        this.byteLimit = limit;
    }

    public void reset(byte[] data2) {
        reset(data2, data2.length);
    }

    public void reset(byte[] data2, int limit) {
        this.data = data2;
        this.byteOffset = 0;
        this.bitOffset = 0;
        this.byteLimit = limit;
    }

    public int bitsLeft() {
        return ((this.byteLimit - this.byteOffset) * 8) - this.bitOffset;
    }

    public int getPosition() {
        return (this.byteOffset * 8) + this.bitOffset;
    }

    public void setPosition(int position) {
        this.byteOffset = position / 8;
        this.bitOffset = position - (this.byteOffset * 8);
        assertValidOffset();
    }

    public void skipBits(int n) {
        this.byteOffset += n / 8;
        this.bitOffset += n % 8;
        if (this.bitOffset > 7) {
            this.byteOffset++;
            this.bitOffset -= 8;
        }
        assertValidOffset();
    }

    public boolean readBit() {
        return readBits(1) == 1;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r0v0, types: [byte] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int readBits(int r11) {
        /*
            r10 = this;
            r9 = 8
            if (r11 != 0) goto L_0x0006
            r3 = 0
        L_0x0005:
            return r3
        L_0x0006:
            r3 = 0
            int r4 = r11 / 8
            r1 = 0
        L_0x000a:
            if (r1 >= r4) goto L_0x0042
            int r6 = r10.bitOffset
            if (r6 == 0) goto L_0x003b
            byte[] r6 = r10.data
            int r7 = r10.byteOffset
            byte r6 = r6[r7]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r7 = r10.bitOffset
            int r6 = r6 << r7
            byte[] r7 = r10.data
            int r8 = r10.byteOffset
            int r8 = r8 + 1
            byte r7 = r7[r8]
            r7 = r7 & 255(0xff, float:3.57E-43)
            int r8 = r10.bitOffset
            int r8 = 8 - r8
            int r7 = r7 >>> r8
            r0 = r6 | r7
        L_0x002c:
            int r11 = r11 + -8
            r6 = r0 & 255(0xff, float:3.57E-43)
            int r6 = r6 << r11
            r3 = r3 | r6
            int r6 = r10.byteOffset
            int r6 = r6 + 1
            r10.byteOffset = r6
            int r1 = r1 + 1
            goto L_0x000a
        L_0x003b:
            byte[] r6 = r10.data
            int r7 = r10.byteOffset
            byte r0 = r6[r7]
            goto L_0x002c
        L_0x0042:
            if (r11 <= 0) goto L_0x0075
            int r6 = r10.bitOffset
            int r2 = r6 + r11
            r6 = 255(0xff, float:3.57E-43)
            int r7 = 8 - r11
            int r6 = r6 >> r7
            byte r5 = (byte) r6
            if (r2 <= r9) goto L_0x0079
            byte[] r6 = r10.data
            int r7 = r10.byteOffset
            byte r6 = r6[r7]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r7 = r2 + -8
            int r6 = r6 << r7
            byte[] r7 = r10.data
            int r8 = r10.byteOffset
            int r8 = r8 + 1
            byte r7 = r7[r8]
            r7 = r7 & 255(0xff, float:3.57E-43)
            int r8 = 16 - r2
            int r7 = r7 >> r8
            r6 = r6 | r7
            r6 = r6 & r5
            r3 = r3 | r6
            int r6 = r10.byteOffset
            int r6 = r6 + 1
            r10.byteOffset = r6
        L_0x0071:
            int r6 = r2 % 8
            r10.bitOffset = r6
        L_0x0075:
            r10.assertValidOffset()
            goto L_0x0005
        L_0x0079:
            byte[] r6 = r10.data
            int r7 = r10.byteOffset
            byte r6 = r6[r7]
            r6 = r6 & 255(0xff, float:3.57E-43)
            int r7 = 8 - r2
            int r6 = r6 >> r7
            r6 = r6 & r5
            r3 = r3 | r6
            if (r2 != r9) goto L_0x0071
            int r6 = r10.byteOffset
            int r6 = r6 + 1
            r10.byteOffset = r6
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.util.ParsableBitArray.readBits(int):int");
    }

    public boolean canReadExpGolombCodedNum() {
        boolean hitLimit;
        int initialByteOffset = this.byteOffset;
        int initialBitOffset = this.bitOffset;
        int leadingZeros = 0;
        while (this.byteOffset < this.byteLimit && !readBit()) {
            leadingZeros++;
        }
        if (this.byteOffset == this.byteLimit) {
            hitLimit = true;
        } else {
            hitLimit = false;
        }
        this.byteOffset = initialByteOffset;
        this.bitOffset = initialBitOffset;
        if (hitLimit || bitsLeft() < (leadingZeros * 2) + 1) {
            return false;
        }
        return true;
    }

    public int readUnsignedExpGolombCodedInt() {
        return readExpGolombCodeNum();
    }

    public int readSignedExpGolombCodedInt() {
        int codeNum = readExpGolombCodeNum();
        return (codeNum % 2 == 0 ? -1 : 1) * ((codeNum + 1) / 2);
    }

    private int readExpGolombCodeNum() {
        int leadingZeros = 0;
        while (!readBit()) {
            leadingZeros++;
        }
        return (leadingZeros > 0 ? readBits(leadingZeros) : 0) + ((1 << leadingZeros) - 1);
    }

    private void assertValidOffset() {
        Assertions.checkState(this.byteOffset >= 0 && this.bitOffset >= 0 && this.bitOffset < 8 && (this.byteOffset < this.byteLimit || (this.byteOffset == this.byteLimit && this.bitOffset == 0)));
    }
}
