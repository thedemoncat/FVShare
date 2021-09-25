package p008jp.p009co.cyberagent.android.gpuimage;

import com.umeng.analytics.C0015a;

/* renamed from: jp.co.cyberagent.android.gpuimage.Rotation */
public enum Rotation {
    NORMAL,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270;

    public int asInt() {
        switch (this) {
            case NORMAL:
                return 0;
            case ROTATION_90:
                return 90;
            case ROTATION_180:
                return 180;
            case ROTATION_270:
                return 270;
            default:
                throw new IllegalStateException("Unknown Rotation!");
        }
    }

    public static Rotation fromInt(int rotation) {
        switch (rotation) {
            case 0:
                return NORMAL;
            case 90:
                return ROTATION_90;
            case 180:
                return ROTATION_180;
            case 270:
                return ROTATION_270;
            case C0015a.f29p:
                return NORMAL;
            default:
                throw new IllegalStateException(rotation + " is an unknown rotation. Needs to be either 0, 90, 180 or 270!");
        }
    }
}
