package com.google.android.exoplayer.upstream;

import android.net.Uri;
import com.google.android.exoplayer.util.Assertions;
import java.util.Arrays;

public final class DataSpec {
    public static final int FLAG_ALLOW_GZIP = 1;
    public final long absoluteStreamPosition;
    public final int flags;
    public final String key;
    public final long length;
    public final long position;
    public final byte[] postBody;
    public final Uri uri;

    public DataSpec(Uri uri2) {
        this(uri2, 0);
    }

    public DataSpec(Uri uri2, int flags2) {
        this(uri2, 0, -1, (String) null, flags2);
    }

    public DataSpec(Uri uri2, long absoluteStreamPosition2, long length2, String key2) {
        this(uri2, absoluteStreamPosition2, absoluteStreamPosition2, length2, key2, 0);
    }

    public DataSpec(Uri uri2, long absoluteStreamPosition2, long length2, String key2, int flags2) {
        this(uri2, absoluteStreamPosition2, absoluteStreamPosition2, length2, key2, flags2);
    }

    public DataSpec(Uri uri2, long absoluteStreamPosition2, long position2, long length2, String key2, int flags2) {
        this(uri2, (byte[]) null, absoluteStreamPosition2, position2, length2, key2, flags2);
    }

    public DataSpec(Uri uri2, byte[] postBody2, long absoluteStreamPosition2, long position2, long length2, String key2, int flags2) {
        Assertions.checkArgument(absoluteStreamPosition2 >= 0);
        Assertions.checkArgument(position2 >= 0);
        Assertions.checkArgument(length2 > 0 || length2 == -1);
        this.uri = uri2;
        this.postBody = postBody2;
        this.absoluteStreamPosition = absoluteStreamPosition2;
        this.position = position2;
        this.length = length2;
        this.key = key2;
        this.flags = flags2;
    }

    public String toString() {
        return "DataSpec[" + this.uri + ", " + Arrays.toString(this.postBody) + ", " + this.absoluteStreamPosition + ", " + this.position + ", " + this.length + ", " + this.key + ", " + this.flags + "]";
    }
}
