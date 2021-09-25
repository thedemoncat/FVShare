package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

class EngineKey implements Key {
    private static final String EMPTY_LOG_STRING = "";
    private final ResourceDecoder cacheDecoder;
    private final ResourceDecoder decoder;
    private final ResourceEncoder encoder;
    private int hashCode;
    private final int height;

    /* renamed from: id */
    private final String f1182id;
    private Key originalKey;
    private final Key signature;
    private final Encoder sourceEncoder;
    private String stringKey;
    private final ResourceTranscoder transcoder;
    private final Transformation transformation;
    private final int width;

    public EngineKey(String id, Key signature2, int width2, int height2, ResourceDecoder cacheDecoder2, ResourceDecoder decoder2, Transformation transformation2, ResourceEncoder encoder2, ResourceTranscoder transcoder2, Encoder sourceEncoder2) {
        this.f1182id = id;
        this.signature = signature2;
        this.width = width2;
        this.height = height2;
        this.cacheDecoder = cacheDecoder2;
        this.decoder = decoder2;
        this.transformation = transformation2;
        this.encoder = encoder2;
        this.transcoder = transcoder2;
        this.sourceEncoder = sourceEncoder2;
    }

    public Key getOriginalKey() {
        if (this.originalKey == null) {
            this.originalKey = new OriginalKey(this.f1182id, this.signature);
        }
        return this.originalKey;
    }

    public boolean equals(Object o) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EngineKey engineKey = (EngineKey) o;
        if (!this.f1182id.equals(engineKey.f1182id) || !this.signature.equals(engineKey.signature) || this.height != engineKey.height || this.width != engineKey.width) {
            return false;
        }
        if ((this.transformation == null) ^ (engineKey.transformation == null)) {
            return false;
        }
        if (this.transformation != null && !this.transformation.getId().equals(engineKey.transformation.getId())) {
            return false;
        }
        if (this.decoder == null) {
            z = true;
        } else {
            z = false;
        }
        if (z ^ (engineKey.decoder == null)) {
            return false;
        }
        if (this.decoder != null && !this.decoder.getId().equals(engineKey.decoder.getId())) {
            return false;
        }
        if (this.cacheDecoder == null) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 ^ (engineKey.cacheDecoder == null)) {
            return false;
        }
        if (this.cacheDecoder != null && !this.cacheDecoder.getId().equals(engineKey.cacheDecoder.getId())) {
            return false;
        }
        if (this.encoder == null) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3 ^ (engineKey.encoder == null)) {
            return false;
        }
        if (this.encoder != null && !this.encoder.getId().equals(engineKey.encoder.getId())) {
            return false;
        }
        if (this.transcoder == null) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (z4 ^ (engineKey.transcoder == null)) {
            return false;
        }
        if (this.transcoder != null && !this.transcoder.getId().equals(engineKey.transcoder.getId())) {
            return false;
        }
        if (this.sourceEncoder == null) {
            z5 = true;
        } else {
            z5 = false;
        }
        if (z5 ^ (engineKey.sourceEncoder == null)) {
            return false;
        }
        if (this.sourceEncoder == null || this.sourceEncoder.getId().equals(engineKey.sourceEncoder.getId())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5 = 0;
        if (this.hashCode == 0) {
            this.hashCode = this.f1182id.hashCode();
            this.hashCode = (this.hashCode * 31) + this.signature.hashCode();
            this.hashCode = (this.hashCode * 31) + this.width;
            this.hashCode = (this.hashCode * 31) + this.height;
            this.hashCode = (this.cacheDecoder != null ? this.cacheDecoder.getId().hashCode() : 0) + (this.hashCode * 31);
            int i6 = this.hashCode * 31;
            if (this.decoder != null) {
                i = this.decoder.getId().hashCode();
            } else {
                i = 0;
            }
            this.hashCode = i + i6;
            int i7 = this.hashCode * 31;
            if (this.transformation != null) {
                i2 = this.transformation.getId().hashCode();
            } else {
                i2 = 0;
            }
            this.hashCode = i2 + i7;
            int i8 = this.hashCode * 31;
            if (this.encoder != null) {
                i3 = this.encoder.getId().hashCode();
            } else {
                i3 = 0;
            }
            this.hashCode = i3 + i8;
            int i9 = this.hashCode * 31;
            if (this.transcoder != null) {
                i4 = this.transcoder.getId().hashCode();
            } else {
                i4 = 0;
            }
            this.hashCode = i4 + i9;
            int i10 = this.hashCode * 31;
            if (this.sourceEncoder != null) {
                i5 = this.sourceEncoder.getId().hashCode();
            }
            this.hashCode = i10 + i5;
        }
        return this.hashCode;
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        if (this.stringKey == null) {
            StringBuilder append = new StringBuilder().append("EngineKey{").append(this.f1182id).append('+').append(this.signature).append("+[").append(this.width).append('x').append(this.height).append("]+").append('\'');
            if (this.cacheDecoder != null) {
                str = this.cacheDecoder.getId();
            } else {
                str = "";
            }
            StringBuilder append2 = append.append(str).append('\'').append('+').append('\'');
            if (this.decoder != null) {
                str2 = this.decoder.getId();
            } else {
                str2 = "";
            }
            StringBuilder append3 = append2.append(str2).append('\'').append('+').append('\'');
            if (this.transformation != null) {
                str3 = this.transformation.getId();
            } else {
                str3 = "";
            }
            StringBuilder append4 = append3.append(str3).append('\'').append('+').append('\'');
            if (this.encoder != null) {
                str4 = this.encoder.getId();
            } else {
                str4 = "";
            }
            StringBuilder append5 = append4.append(str4).append('\'').append('+').append('\'');
            if (this.transcoder != null) {
                str5 = this.transcoder.getId();
            } else {
                str5 = "";
            }
            StringBuilder append6 = append5.append(str5).append('\'').append('+').append('\'');
            if (this.sourceEncoder != null) {
                str6 = this.sourceEncoder.getId();
            } else {
                str6 = "";
            }
            this.stringKey = append6.append(str6).append('\'').append('}').toString();
        }
        return this.stringKey;
    }

    public void updateDiskCacheKey(MessageDigest messageDigest) throws UnsupportedEncodingException {
        byte[] dimensions = ByteBuffer.allocate(8).putInt(this.width).putInt(this.height).array();
        this.signature.updateDiskCacheKey(messageDigest);
        messageDigest.update(this.f1182id.getBytes("UTF-8"));
        messageDigest.update(dimensions);
        messageDigest.update((this.cacheDecoder != null ? this.cacheDecoder.getId() : "").getBytes("UTF-8"));
        messageDigest.update((this.decoder != null ? this.decoder.getId() : "").getBytes("UTF-8"));
        messageDigest.update((this.transformation != null ? this.transformation.getId() : "").getBytes("UTF-8"));
        messageDigest.update((this.encoder != null ? this.encoder.getId() : "").getBytes("UTF-8"));
        messageDigest.update((this.sourceEncoder != null ? this.sourceEncoder.getId() : "").getBytes("UTF-8"));
    }
}
