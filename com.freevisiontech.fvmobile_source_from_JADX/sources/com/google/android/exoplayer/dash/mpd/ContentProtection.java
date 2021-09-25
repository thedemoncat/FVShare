package com.google.android.exoplayer.dash.mpd;

import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.UUID;

public class ContentProtection {
    public final DrmInitData.SchemeInitData data;
    public final String schemeUriId;
    public final UUID uuid;

    public ContentProtection(String schemeUriId2, UUID uuid2, DrmInitData.SchemeInitData data2) {
        this.schemeUriId = (String) Assertions.checkNotNull(schemeUriId2);
        this.uuid = uuid2;
        this.data = data2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ContentProtection)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        ContentProtection other = (ContentProtection) obj;
        if (!this.schemeUriId.equals(other.schemeUriId) || !Util.areEqual(this.uuid, other.uuid) || !Util.areEqual(this.data, other.data)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int hashCode = this.schemeUriId.hashCode() * 37;
        if (this.uuid != null) {
            i = this.uuid.hashCode();
        } else {
            i = 0;
        }
        int i3 = (hashCode + i) * 37;
        if (this.data != null) {
            i2 = this.data.hashCode();
        }
        return i3 + i2;
    }
}
