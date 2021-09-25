package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSourceInputStream;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

final class Aes128DataSource implements DataSource {
    private CipherInputStream cipherInputStream;
    private final byte[] encryptionIv;
    private final byte[] encryptionKey;
    private final DataSource upstream;

    public Aes128DataSource(DataSource upstream2, byte[] encryptionKey2, byte[] encryptionIv2) {
        this.upstream = upstream2;
        this.encryptionKey = encryptionKey2;
        this.encryptionIv = encryptionIv2;
    }

    public long open(DataSpec dataSpec) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            try {
                cipher.init(2, new SecretKeySpec(this.encryptionKey, "AES"), new IvParameterSpec(this.encryptionIv));
                this.cipherInputStream = new CipherInputStream(new DataSourceInputStream(this.upstream, dataSpec), cipher);
                return -1;
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new RuntimeException(e2);
            }
        } catch (NoSuchAlgorithmException e3) {
            throw new RuntimeException(e3);
        } catch (NoSuchPaddingException e4) {
            throw new RuntimeException(e4);
        }
    }

    public void close() throws IOException {
        this.cipherInputStream = null;
        this.upstream.close();
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        Assertions.checkState(this.cipherInputStream != null);
        int bytesRead = this.cipherInputStream.read(buffer, offset, readLength);
        if (bytesRead < 0) {
            return -1;
        }
        return bytesRead;
    }
}
