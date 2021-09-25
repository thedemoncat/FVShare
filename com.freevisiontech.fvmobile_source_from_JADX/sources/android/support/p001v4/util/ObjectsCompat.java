package android.support.p001v4.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import java.util.Objects;

/* renamed from: android.support.v4.util.ObjectsCompat */
public class ObjectsCompat {
    private static final ImplBase IMPL;

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new ImplApi19();
        } else {
            IMPL = new ImplBase();
        }
    }

    private ObjectsCompat() {
    }

    public static boolean equals(Object a, Object b) {
        return IMPL.equals(a, b);
    }

    /* renamed from: android.support.v4.util.ObjectsCompat$ImplBase */
    private static class ImplBase {
        private ImplBase() {
        }

        public boolean equals(Object a, Object b) {
            return a == b || (a != null && a.equals(b));
        }
    }

    @RequiresApi(19)
    /* renamed from: android.support.v4.util.ObjectsCompat$ImplApi19 */
    private static class ImplApi19 extends ImplBase {
        private ImplApi19() {
            super();
        }

        public boolean equals(Object a, Object b) {
            return Objects.equals(a, b);
        }
    }
}
