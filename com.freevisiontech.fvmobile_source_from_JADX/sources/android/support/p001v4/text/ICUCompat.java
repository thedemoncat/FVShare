package android.support.p001v4.text;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.Locale;

/* renamed from: android.support.v4.text.ICUCompat */
public final class ICUCompat {
    private static final ICUCompatBaseImpl IMPL;

    /* renamed from: android.support.v4.text.ICUCompat$ICUCompatBaseImpl */
    static class ICUCompatBaseImpl {
        ICUCompatBaseImpl() {
        }

        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }

    @RequiresApi(21)
    /* renamed from: android.support.v4.text.ICUCompat$ICUCompatApi21Impl */
    static class ICUCompatApi21Impl extends ICUCompatBaseImpl {
        ICUCompatApi21Impl() {
        }

        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatApi21.maximizeAndGetScript(locale);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new ICUCompatApi21Impl();
        } else {
            IMPL = new ICUCompatBaseImpl();
        }
    }

    @Nullable
    public static String maximizeAndGetScript(Locale locale) {
        return IMPL.maximizeAndGetScript(locale);
    }

    private ICUCompat() {
    }
}
