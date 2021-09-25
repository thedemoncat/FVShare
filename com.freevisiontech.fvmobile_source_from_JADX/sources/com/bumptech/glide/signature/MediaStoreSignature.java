package com.bumptech.glide.signature;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class MediaStoreSignature implements Key {
    private final long dateModified;
    private final String mimeType;
    private final int orientation;

    public MediaStoreSignature(String mimeType2, long dateModified2, int orientation2) {
        this.mimeType = mimeType2;
        this.dateModified = dateModified2;
        this.orientation = orientation2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaStoreSignature that = (MediaStoreSignature) o;
        if (this.dateModified != that.dateModified) {
            return false;
        }
        if (this.orientation != that.orientation) {
            return false;
        }
        if (this.mimeType != null) {
            if (this.mimeType.equals(that.mimeType)) {
                return true;
            }
        } else if (that.mimeType == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((this.mimeType != null ? this.mimeType.hashCode() : 0) * 31) + ((int) (this.dateModified ^ (this.dateModified >>> 32)))) * 31) + this.orientation;
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(ByteBuffer.allocate(12).putLong(this.dateModified).putInt(this.orientation).array());
        messageDigest.update(this.mimeType.getBytes("UTF-8"));
    }
}
