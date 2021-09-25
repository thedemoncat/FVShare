package android.support.p001v4.graphics;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;

/* renamed from: android.support.v4.graphics.PaintCompat */
public final class PaintCompat {
    public static boolean hasGlyph(@NonNull Paint paint, @NonNull String string) {
        if (Build.VERSION.SDK_INT >= 23) {
            return paint.hasGlyph(string);
        }
        return PaintCompatApi14.hasGlyph(paint, string);
    }

    private PaintCompat() {
    }
}
