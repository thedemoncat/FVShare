package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

class OriginalKey implements Key {

    /* renamed from: id */
    private final String f1183id;
    private final Key signature;

    public OriginalKey(String id, Key signature2) {
        this.f1183id = id;
        this.signature = signature2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OriginalKey that = (OriginalKey) o;
        if (!this.f1183id.equals(that.f1183id)) {
            return false;
        }
        if (!this.signature.equals(that.signature)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.f1183id.hashCode() * 31) + this.signature.hashCode();
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        messageDigest.update(this.f1183id.getBytes("UTF-8"));
        this.signature.updateDiskCacheKey(messageDigest);
    }
}
