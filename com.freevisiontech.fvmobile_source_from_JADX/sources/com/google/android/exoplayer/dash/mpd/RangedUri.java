package com.google.android.exoplayer.dash.mpd;

import android.net.Uri;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.UriUtil;

public final class RangedUri {
    private final String baseUri;
    private int hashCode;
    public final long length;
    private final String referenceUri;
    public final long start;

    public RangedUri(String baseUri2, String referenceUri2, long start2, long length2) {
        Assertions.checkArgument((baseUri2 == null && referenceUri2 == null) ? false : true);
        this.baseUri = baseUri2;
        this.referenceUri = referenceUri2;
        this.start = start2;
        this.length = length2;
    }

    public Uri getUri() {
        return UriUtil.resolveToUri(this.baseUri, this.referenceUri);
    }

    public String getUriString() {
        return UriUtil.resolve(this.baseUri, this.referenceUri);
    }

    public RangedUri attemptMerge(RangedUri other) {
        RangedUri rangedUri = null;
        long j = -1;
        if (other != null && getUriString().equals(other.getUriString())) {
            if (this.length != -1 && this.start + this.length == other.start) {
                String str = this.baseUri;
                String str2 = this.referenceUri;
                long j2 = this.start;
                if (other.length != -1) {
                    j = this.length + other.length;
                }
                rangedUri = new RangedUri(str, str2, j2, j);
            } else if (other.length != -1 && other.start + other.length == this.start) {
                String str3 = this.baseUri;
                String str4 = this.referenceUri;
                long j3 = other.start;
                if (this.length != -1) {
                    j = other.length + this.length;
                }
                rangedUri = new RangedUri(str3, str4, j3, j);
            }
        }
        return rangedUri;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((((((int) this.start) + 527) * 31) + ((int) this.length)) * 31) + getUriString().hashCode();
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RangedUri other = (RangedUri) obj;
        if (this.start == other.start && this.length == other.length && getUriString().equals(other.getUriString())) {
            return true;
        }
        return false;
    }
}
