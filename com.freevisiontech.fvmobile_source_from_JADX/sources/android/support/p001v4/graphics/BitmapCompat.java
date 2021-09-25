package android.support.p001v4.graphics;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;

/* renamed from: android.support.v4.graphics.BitmapCompat */
public final class BitmapCompat {
    static final BitmapCompatBaseImpl IMPL;

    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatBaseImpl */
    static class BitmapCompatBaseImpl {
        BitmapCompatBaseImpl() {
        }

        public boolean hasMipMap(Bitmap bitmap) {
            return false;
        }

        public void setHasMipMap(Bitmap bitmap, boolean hasMipMap) {
        }

        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getByteCount();
        }
    }

    @RequiresApi(18)
    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatApi18Impl */
    static class BitmapCompatApi18Impl extends BitmapCompatBaseImpl {
        BitmapCompatApi18Impl() {
        }

        public boolean hasMipMap(Bitmap bitmap) {
            return bitmap.hasMipMap();
        }

        public void setHasMipMap(Bitmap bitmap, boolean hasMipMap) {
            bitmap.setHasMipMap(hasMipMap);
        }
    }

    @RequiresApi(19)
    /* renamed from: android.support.v4.graphics.BitmapCompat$BitmapCompatApi19Impl */
    static class BitmapCompatApi19Impl extends BitmapCompatApi18Impl {
        BitmapCompatApi19Impl() {
        }

        public int getAllocationByteCount(Bitmap bitmap) {
            return bitmap.getAllocationByteCount();
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new BitmapCompatApi19Impl();
        } else if (Build.VERSION.SDK_INT >= 18) {
            IMPL = new BitmapCompatApi18Impl();
        } else {
            IMPL = new BitmapCompatBaseImpl();
        }
    }

    public static boolean hasMipMap(Bitmap bitmap) {
        return IMPL.hasMipMap(bitmap);
    }

    public static void setHasMipMap(Bitmap bitmap, boolean hasMipMap) {
        IMPL.setHasMipMap(bitmap, hasMipMap);
    }

    public static int getAllocationByteCount(Bitmap bitmap) {
        return IMPL.getAllocationByteCount(bitmap);
    }

    private BitmapCompat() {
    }
}
