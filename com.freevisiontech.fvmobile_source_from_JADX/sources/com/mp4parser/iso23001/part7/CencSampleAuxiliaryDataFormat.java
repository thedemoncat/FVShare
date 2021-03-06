package com.mp4parser.iso23001.part7;

import com.coremedia.iso.Hex;
import java.math.BigInteger;
import java.util.Arrays;

public class CencSampleAuxiliaryDataFormat {

    /* renamed from: iv */
    public byte[] f1048iv = new byte[0];
    public Pair[] pairs = null;

    public interface Pair {
        int clear();

        long encrypted();
    }

    public int getSize() {
        int size = this.f1048iv.length;
        if (this.pairs == null || this.pairs.length <= 0) {
            return size;
        }
        return size + 2 + (this.pairs.length * 6);
    }

    public Pair createPair(int clear, long encrypted) {
        if (clear <= 127) {
            if (encrypted <= 127) {
                return new ByteBytePair(clear, encrypted);
            }
            if (encrypted <= 32767) {
                return new ByteShortPair(clear, encrypted);
            }
            if (encrypted <= 2147483647L) {
                return new ByteIntPair(clear, encrypted);
            }
            return new ByteLongPair(clear, encrypted);
        } else if (clear <= 32767) {
            if (encrypted <= 127) {
                return new ShortBytePair(clear, encrypted);
            }
            if (encrypted <= 32767) {
                return new ShortShortPair(clear, encrypted);
            }
            if (encrypted <= 2147483647L) {
                return new ShortIntPair(clear, encrypted);
            }
            return new ShortLongPair(clear, encrypted);
        } else if (encrypted <= 127) {
            return new IntBytePair(clear, encrypted);
        } else {
            if (encrypted <= 32767) {
                return new IntShortPair(clear, encrypted);
            }
            if (encrypted <= 2147483647L) {
                return new IntIntPair(clear, encrypted);
            }
            return new IntLongPair(clear, encrypted);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CencSampleAuxiliaryDataFormat entry = (CencSampleAuxiliaryDataFormat) o;
        if (!new BigInteger(this.f1048iv).equals(new BigInteger(entry.f1048iv))) {
            return false;
        }
        if (this.pairs != null) {
            if (Arrays.equals(this.pairs, entry.pairs)) {
                return true;
            }
        } else if (entry.pairs == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.f1048iv != null) {
            result = Arrays.hashCode(this.f1048iv);
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.pairs != null) {
            i = Arrays.hashCode(this.pairs);
        }
        return i2 + i;
    }

    public String toString() {
        return "Entry{iv=" + Hex.encodeHex(this.f1048iv) + ", pairs=" + Arrays.toString(this.pairs) + '}';
    }

    private class ByteBytePair extends AbstractPair {
        private byte clear;
        private byte encrypted;

        public ByteBytePair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (byte) clear2;
            this.encrypted = (byte) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ByteShortPair extends AbstractPair {
        private byte clear;
        private short encrypted;

        public ByteShortPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (byte) clear2;
            this.encrypted = (short) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ByteIntPair extends AbstractPair {
        private byte clear;
        private int encrypted;

        public ByteIntPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (byte) clear2;
            this.encrypted = (int) encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ByteLongPair extends AbstractPair {
        private byte clear;
        private long encrypted;

        public ByteLongPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (byte) clear2;
            this.encrypted = encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ShortBytePair extends AbstractPair {
        private short clear;
        private byte encrypted;

        public ShortBytePair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (short) clear2;
            this.encrypted = (byte) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ShortShortPair extends AbstractPair {
        private short clear;
        private short encrypted;

        public ShortShortPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (short) clear2;
            this.encrypted = (short) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ShortIntPair extends AbstractPair {
        private short clear;
        private int encrypted;

        public ShortIntPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (short) clear2;
            this.encrypted = (int) encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class ShortLongPair extends AbstractPair {
        private short clear;
        private long encrypted;

        public ShortLongPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = (short) clear2;
            this.encrypted = encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return this.encrypted;
        }
    }

    private class IntBytePair extends AbstractPair {
        private int clear;
        private byte encrypted;

        public IntBytePair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = clear2;
            this.encrypted = (byte) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class IntShortPair extends AbstractPair {
        private int clear;
        private short encrypted;

        public IntShortPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = clear2;
            this.encrypted = (short) ((int) encrypted2);
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class IntIntPair extends AbstractPair {
        private int clear;
        private int encrypted;

        public IntIntPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = clear2;
            this.encrypted = (int) encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return (long) this.encrypted;
        }
    }

    private class IntLongPair extends AbstractPair {
        private int clear;
        private long encrypted;

        public IntLongPair(int clear2, long encrypted2) {
            super(CencSampleAuxiliaryDataFormat.this, (AbstractPair) null);
            this.clear = clear2;
            this.encrypted = encrypted2;
        }

        public int clear() {
            return this.clear;
        }

        public long encrypted() {
            return this.encrypted;
        }
    }

    private abstract class AbstractPair implements Pair {
        private AbstractPair() {
        }

        /* synthetic */ AbstractPair(CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat, AbstractPair abstractPair) {
            this();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair pair = (Pair) o;
            if (clear() != pair.clear()) {
                return false;
            }
            if (encrypted() != pair.encrypted()) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "P(" + clear() + "|" + encrypted() + ")";
        }
    }
}
