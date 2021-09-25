package android.support.p001v4.p014os;

import android.os.Build;
import android.os.Trace;

/* renamed from: android.support.v4.os.TraceCompat */
public final class TraceCompat {
    public static void beginSection(String sectionName) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection(sectionName);
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }

    private TraceCompat() {
    }
}
