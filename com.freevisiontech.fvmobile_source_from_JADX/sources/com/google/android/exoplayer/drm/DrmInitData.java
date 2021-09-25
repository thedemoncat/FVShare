package com.google.android.exoplayer.drm;

import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface DrmInitData {
    SchemeInitData get(UUID uuid);

    public static final class Mapped implements DrmInitData {
        private final Map<UUID, SchemeInitData> schemeData = new HashMap();

        public SchemeInitData get(UUID schemeUuid) {
            return this.schemeData.get(schemeUuid);
        }

        public void put(UUID schemeUuid, SchemeInitData schemeInitData) {
            this.schemeData.put(schemeUuid, schemeInitData);
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Mapped rhs = (Mapped) obj;
            if (this.schemeData.size() != rhs.schemeData.size()) {
                return false;
            }
            for (UUID uuid : this.schemeData.keySet()) {
                if (!Util.areEqual(this.schemeData.get(uuid), rhs.schemeData.get(uuid))) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return this.schemeData.hashCode();
        }
    }

    public static final class Universal implements DrmInitData {
        private SchemeInitData data;

        public Universal(SchemeInitData data2) {
            this.data = data2;
        }

        public SchemeInitData get(UUID schemeUuid) {
            return this.data;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return Util.areEqual(this.data, ((Universal) obj).data);
        }

        public int hashCode() {
            return this.data.hashCode();
        }
    }

    public static final class SchemeInitData {
        public final byte[] data;
        public final String mimeType;

        public SchemeInitData(String mimeType2, byte[] data2) {
            this.mimeType = (String) Assertions.checkNotNull(mimeType2);
            this.data = (byte[]) Assertions.checkNotNull(data2);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof SchemeInitData)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            SchemeInitData other = (SchemeInitData) obj;
            if (!this.mimeType.equals(other.mimeType) || !Arrays.equals(this.data, other.data)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.mimeType.hashCode() + (Arrays.hashCode(this.data) * 31);
        }
    }
}
