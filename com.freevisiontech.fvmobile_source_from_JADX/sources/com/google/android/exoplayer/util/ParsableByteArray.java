package com.google.android.exoplayer.util;

import com.umeng.analytics.pro.C0217dk;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public final class ParsableByteArray {
    public byte[] data;
    private int limit;
    private int position;

    public ParsableByteArray() {
    }

    public ParsableByteArray(int length) {
        this.data = new byte[length];
        this.limit = this.data.length;
    }

    public ParsableByteArray(byte[] data2) {
        this.data = data2;
        this.limit = data2.length;
    }

    public ParsableByteArray(byte[] data2, int limit2) {
        this.data = data2;
        this.limit = limit2;
    }

    public void reset(int limit2) {
        reset(capacity() < limit2 ? new byte[limit2] : this.data, limit2);
    }

    public void reset(byte[] data2, int limit2) {
        this.data = data2;
        this.limit = limit2;
        this.position = 0;
    }

    public void reset() {
        this.position = 0;
        this.limit = 0;
    }

    public int bytesLeft() {
        return this.limit - this.position;
    }

    public int limit() {
        return this.limit;
    }

    public void setLimit(int limit2) {
        Assertions.checkArgument(limit2 >= 0 && limit2 <= this.data.length);
        this.limit = limit2;
    }

    public int getPosition() {
        return this.position;
    }

    public int capacity() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    public void setPosition(int position2) {
        Assertions.checkArgument(position2 >= 0 && position2 <= this.limit);
        this.position = position2;
    }

    public void skipBytes(int bytes) {
        setPosition(this.position + bytes);
    }

    public void readBytes(ParsableBitArray bitArray, int length) {
        readBytes(bitArray.data, 0, length);
        bitArray.setPosition(0);
    }

    public void readBytes(byte[] buffer, int offset, int length) {
        System.arraycopy(this.data, this.position, buffer, offset, length);
        this.position += length;
    }

    public void readBytes(ByteBuffer buffer, int length) {
        buffer.put(this.data, this.position, length);
        this.position += length;
    }

    public int readUnsignedByte() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        return bArr[i] & 255;
    }

    public int readUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        return ((bArr[i] & 255) << 8) | (bArr2[i2] & 255);
    }

    public int readLittleEndianUnsignedShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        return (bArr[i] & 255) | ((bArr2[i2] & 255) << 8);
    }

    public short readShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        return (short) (((bArr[i] & 255) << 8) | (bArr2[i2] & 255));
    }

    public short readLittleEndianShort() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        return (short) ((bArr[i] & 255) | ((bArr2[i2] & 255) << 8));
    }

    public int readUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        int i2 = (bArr[i] & 255) << C0217dk.f724n;
        byte[] bArr2 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        byte b = i2 | ((bArr2[i3] & 255) << 8);
        byte[] bArr3 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        return b | (bArr3[i4] & 255);
    }

    public int readLittleEndianInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        byte b = (bArr[i] & 255) | ((bArr2[i2] & 255) << 8);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return b | ((bArr3[i3] & 255) << C0217dk.f724n);
    }

    public int readLittleEndianUnsignedInt24() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        byte b = (bArr[i] & 255) | ((bArr2[i2] & 255) << 8);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        return b | ((bArr3[i3] & 255) << C0217dk.f724n);
    }

    public long readUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j = ((((long) bArr[i]) & 255) << 24) | ((((long) bArr2[i2]) & 255) << 16);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        long j2 = j | ((((long) bArr3[i3]) & 255) << 8);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        return j2 | (((long) bArr4[i4]) & 255);
    }

    public long readLittleEndianUnsignedInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j = (((long) bArr[i]) & 255) | ((((long) bArr2[i2]) & 255) << 8);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        long j2 = j | ((((long) bArr3[i3]) & 255) << 16);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        return j2 | ((((long) bArr4[i4]) & 255) << 24);
    }

    public int readInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        byte b = ((bArr[i] & 255) << 24) | ((bArr2[i2] & 255) << C0217dk.f724n);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        byte b2 = b | ((bArr3[i3] & 255) << 8);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        return b2 | (bArr4[i4] & 255);
    }

    public int readLittleEndianInt() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        byte b = (bArr[i] & 255) | ((bArr2[i2] & 255) << 8);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        byte b2 = b | ((bArr3[i3] & 255) << C0217dk.f724n);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        return b2 | ((bArr4[i4] & 255) << 24);
    }

    public long readLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j = ((((long) bArr[i]) & 255) << 56) | ((((long) bArr2[i2]) & 255) << 48);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        long j2 = j | ((((long) bArr3[i3]) & 255) << 40);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        long j3 = j2 | ((((long) bArr4[i4]) & 255) << 32);
        byte[] bArr5 = this.data;
        int i5 = this.position;
        this.position = i5 + 1;
        long j4 = j3 | ((((long) bArr5[i5]) & 255) << 24);
        byte[] bArr6 = this.data;
        int i6 = this.position;
        this.position = i6 + 1;
        long j5 = j4 | ((((long) bArr6[i6]) & 255) << 16);
        byte[] bArr7 = this.data;
        int i7 = this.position;
        this.position = i7 + 1;
        long j6 = j5 | ((((long) bArr7[i7]) & 255) << 8);
        byte[] bArr8 = this.data;
        int i8 = this.position;
        this.position = i8 + 1;
        return j6 | (((long) bArr8[i8]) & 255);
    }

    public long readLittleEndianLong() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        long j = (((long) bArr[i]) & 255) | ((((long) bArr2[i2]) & 255) << 8);
        byte[] bArr3 = this.data;
        int i3 = this.position;
        this.position = i3 + 1;
        long j2 = j | ((((long) bArr3[i3]) & 255) << 16);
        byte[] bArr4 = this.data;
        int i4 = this.position;
        this.position = i4 + 1;
        long j3 = j2 | ((((long) bArr4[i4]) & 255) << 24);
        byte[] bArr5 = this.data;
        int i5 = this.position;
        this.position = i5 + 1;
        long j4 = j3 | ((((long) bArr5[i5]) & 255) << 32);
        byte[] bArr6 = this.data;
        int i6 = this.position;
        this.position = i6 + 1;
        long j5 = j4 | ((((long) bArr6[i6]) & 255) << 40);
        byte[] bArr7 = this.data;
        int i7 = this.position;
        this.position = i7 + 1;
        long j6 = j5 | ((((long) bArr7[i7]) & 255) << 48);
        byte[] bArr8 = this.data;
        int i8 = this.position;
        this.position = i8 + 1;
        return j6 | ((((long) bArr8[i8]) & 255) << 56);
    }

    public int readUnsignedFixedPoint1616() {
        byte[] bArr = this.data;
        int i = this.position;
        this.position = i + 1;
        byte[] bArr2 = this.data;
        int i2 = this.position;
        this.position = i2 + 1;
        int result = ((bArr[i] & 255) << 8) | (bArr2[i2] & 255);
        this.position += 2;
        return result;
    }

    public int readSynchSafeInt() {
        return (readUnsignedByte() << 21) | (readUnsignedByte() << 14) | (readUnsignedByte() << 7) | readUnsignedByte();
    }

    public int readUnsignedIntToInt() {
        int result = readInt();
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Top bit not zero: " + result);
    }

    public int readLittleEndianUnsignedIntToInt() {
        int result = readLittleEndianInt();
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Top bit not zero: " + result);
    }

    public long readUnsignedLongToLong() {
        long result = readLong();
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Top bit not zero: " + result);
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public String readString(int length) {
        return readString(length, Charset.defaultCharset());
    }

    public String readString(int length, Charset charset) {
        String result = new String(this.data, this.position, length, charset);
        this.position += length;
        return result;
    }

    public String readLine() {
        if (bytesLeft() == 0) {
            return null;
        }
        int lineLimit = this.position;
        while (lineLimit < this.limit && this.data[lineLimit] != 10 && this.data[lineLimit] != 13) {
            lineLimit++;
        }
        if (lineLimit - this.position >= 3 && this.data[this.position] == -17 && this.data[this.position + 1] == -69 && this.data[this.position + 2] == -65) {
            this.position += 3;
        }
        String str = new String(this.data, this.position, lineLimit - this.position);
        this.position = lineLimit;
        if (this.position == this.limit) {
            return str;
        }
        if (this.data[this.position] == 13) {
            this.position++;
            if (this.position == this.limit) {
                return str;
            }
        }
        if (this.data[this.position] != 10) {
            return str;
        }
        this.position++;
        return str;
    }

    public String readNullTerminatedString() {
        if (bytesLeft() == 0) {
            return null;
        }
        int stringLimit = this.position;
        while (stringLimit < this.limit && this.data[stringLimit] != 0) {
            stringLimit++;
        }
        String str = new String(this.data, this.position, stringLimit - this.position, Charset.defaultCharset());
        this.position = stringLimit;
        if (this.position == this.limit) {
            return str;
        }
        this.position++;
        return str;
    }

    public long readUTF8EncodedLong() {
        int length = 0;
        long value = (long) this.data[this.position];
        int j = 7;
        while (true) {
            if (j < 0) {
                break;
            } else if ((((long) (1 << j)) & value) != 0) {
                j--;
            } else if (j < 6) {
                value &= (long) ((1 << j) - 1);
                length = 7 - j;
            } else if (j == 7) {
                length = 1;
            }
        }
        if (length == 0) {
            throw new NumberFormatException("Invalid UTF-8 sequence first byte: " + value);
        }
        for (int i = 1; i < length; i++) {
            byte x = this.data[this.position + i];
            if ((x & 192) != 128) {
                throw new NumberFormatException("Invalid UTF-8 sequence continuation byte: " + value);
            }
            value = (value << 6) | ((long) (x & 63));
        }
        this.position += length;
        return value;
    }
}
