package com.googlecode.mp4parser.boxes.cenc;

import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.RangeStartMap;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class CencEncryptingSampleList extends AbstractList<Sample> {
    List<CencSampleAuxiliaryDataFormat> auxiliaryDataFormats;
    RangeStartMap<Integer, SecretKey> ceks;
    Cipher cipher;
    /* access modifiers changed from: private */
    public final String encryptionAlgo;
    List<Sample> parent;

    public CencEncryptingSampleList(SecretKey defaultCek, List<Sample> parent2, List<CencSampleAuxiliaryDataFormat> auxiliaryDataFormats2) {
        this(new RangeStartMap(0, defaultCek), parent2, auxiliaryDataFormats2, "cenc");
    }

    public CencEncryptingSampleList(RangeStartMap<Integer, SecretKey> ceks2, List<Sample> parent2, List<CencSampleAuxiliaryDataFormat> auxiliaryDataFormats2, String encryptionAlgo2) {
        this.ceks = new RangeStartMap<>();
        this.auxiliaryDataFormats = auxiliaryDataFormats2;
        this.ceks = ceks2;
        this.encryptionAlgo = encryptionAlgo2;
        this.parent = parent2;
        try {
            if ("cenc".equals(encryptionAlgo2)) {
                this.cipher = Cipher.getInstance("AES/CTR/NoPadding");
            } else if ("cbc1".equals(encryptionAlgo2)) {
                this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } else {
                throw new RuntimeException("Only cenc & cbc1 is supported as encryptionAlgo");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e2) {
            throw new RuntimeException(e2);
        }
    }

    public Sample get(int index) {
        Sample clearSample = this.parent.get(index);
        if (this.ceks.get(Integer.valueOf(index)) == null) {
            return clearSample;
        }
        return new EncryptedSampleImpl(this, clearSample, this.auxiliaryDataFormats.get(index), this.cipher, this.ceks.get(Integer.valueOf(index)), (EncryptedSampleImpl) null);
    }

    /* access modifiers changed from: protected */
    public void initCipher(byte[] iv, SecretKey cek) {
        try {
            byte[] fullIv = new byte[16];
            System.arraycopy(iv, 0, fullIv, 0, iv.length);
            this.cipher.init(1, cek, new IvParameterSpec(fullIv));
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e2) {
            throw new RuntimeException(e2);
        }
    }

    public int size() {
        return this.parent.size();
    }

    private class EncryptedSampleImpl implements Sample {
        static final /* synthetic */ boolean $assertionsDisabled = (!CencEncryptingSampleList.class.desiredAssertionStatus());
        private final SecretKey cek;
        private final CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat;
        private final Cipher cipher;
        private final Sample clearSample;

        /* synthetic */ EncryptedSampleImpl(CencEncryptingSampleList cencEncryptingSampleList, Sample sample, CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat2, Cipher cipher2, SecretKey secretKey, EncryptedSampleImpl encryptedSampleImpl) {
            this(sample, cencSampleAuxiliaryDataFormat2, cipher2, secretKey);
        }

        private EncryptedSampleImpl(Sample clearSample2, CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat2, Cipher cipher2, SecretKey cek2) {
            this.clearSample = clearSample2;
            this.cencSampleAuxiliaryDataFormat = cencSampleAuxiliaryDataFormat2;
            this.cipher = cipher2;
            this.cek = cek2;
        }

        public void writeTo(WritableByteChannel channel) throws IOException {
            ByteBuffer sample = (ByteBuffer) this.clearSample.asByteBuffer().rewind();
            CencEncryptingSampleList.this.initCipher(this.cencSampleAuxiliaryDataFormat.f1048iv, this.cek);
            try {
                if (this.cencSampleAuxiliaryDataFormat.pairs == null || this.cencSampleAuxiliaryDataFormat.pairs.length <= 0) {
                    byte[] fullyEncryptedSample = new byte[sample.limit()];
                    sample.get(fullyEncryptedSample);
                    if ("cbc1".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        int encryptedLength = (fullyEncryptedSample.length / 16) * 16;
                        channel.write(ByteBuffer.wrap(this.cipher.doFinal(fullyEncryptedSample, 0, encryptedLength)));
                        channel.write(ByteBuffer.wrap(fullyEncryptedSample, encryptedLength, fullyEncryptedSample.length - encryptedLength));
                    } else if ("cenc".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        channel.write(ByteBuffer.wrap(this.cipher.doFinal(fullyEncryptedSample)));
                    }
                } else {
                    byte[] fullSample = new byte[sample.limit()];
                    sample.get(fullSample);
                    int offset = 0;
                    for (CencSampleAuxiliaryDataFormat.Pair pair : this.cencSampleAuxiliaryDataFormat.pairs) {
                        offset += pair.clear();
                        if (pair.encrypted() > 0) {
                            this.cipher.update(fullSample, offset, CastUtils.l2i(pair.encrypted()), fullSample, offset);
                            offset = (int) (((long) offset) + pair.encrypted());
                        }
                    }
                    channel.write(ByteBuffer.wrap(fullSample));
                }
                sample.rewind();
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e2) {
                throw new RuntimeException(e2);
            } catch (ShortBufferException e3) {
                throw new RuntimeException(e3);
            }
        }

        public long getSize() {
            return this.clearSample.getSize();
        }

        public ByteBuffer asByteBuffer() {
            ByteBuffer sample = (ByteBuffer) this.clearSample.asByteBuffer().rewind();
            ByteBuffer encSample = ByteBuffer.allocate(sample.limit());
            CencSampleAuxiliaryDataFormat entry = this.cencSampleAuxiliaryDataFormat;
            CencEncryptingSampleList.this.initCipher(this.cencSampleAuxiliaryDataFormat.f1048iv, this.cek);
            try {
                if (entry.pairs != null) {
                    for (CencSampleAuxiliaryDataFormat.Pair pair : entry.pairs) {
                        byte[] clears = new byte[pair.clear()];
                        sample.get(clears);
                        encSample.put(clears);
                        if (pair.encrypted() > 0) {
                            byte[] toBeEncrypted = new byte[CastUtils.l2i(pair.encrypted())];
                            sample.get(toBeEncrypted);
                            if ($assertionsDisabled || toBeEncrypted.length % 16 == 0) {
                                byte[] encrypted = this.cipher.update(toBeEncrypted);
                                if ($assertionsDisabled || encrypted.length == toBeEncrypted.length) {
                                    encSample.put(encrypted);
                                } else {
                                    throw new AssertionError();
                                }
                            } else {
                                throw new AssertionError();
                            }
                        }
                    }
                } else {
                    byte[] fullyEncryptedSample = new byte[sample.limit()];
                    sample.get(fullyEncryptedSample);
                    if ("cbc1".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        int encryptedLength = (fullyEncryptedSample.length / 16) * 16;
                        encSample.put(this.cipher.doFinal(fullyEncryptedSample, 0, encryptedLength));
                        encSample.put(fullyEncryptedSample, encryptedLength, fullyEncryptedSample.length - encryptedLength);
                    } else if ("cenc".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        encSample.put(this.cipher.doFinal(fullyEncryptedSample));
                    }
                }
                sample.rewind();
                encSample.rewind();
                return encSample;
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
