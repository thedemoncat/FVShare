package p004pl.droidsonroids.gif;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import p004pl.droidsonroids.gif.annotations.Beta;

@Beta
/* renamed from: pl.droidsonroids.gif.GifOptions */
public class GifOptions {
    boolean inIsOpaque;
    char inSampleSize;

    public GifOptions() {
        reset();
    }

    private void reset() {
        this.inSampleSize = 1;
        this.inIsOpaque = false;
    }

    public void setInSampleSize(@IntRange(from = 1, mo8779to = 65535) int inSampleSize2) {
        if (inSampleSize2 < 1 || inSampleSize2 > 65535) {
            this.inSampleSize = 1;
        } else {
            this.inSampleSize = (char) inSampleSize2;
        }
    }

    public void setInIsOpaque(boolean inIsOpaque2) {
        this.inIsOpaque = inIsOpaque2;
    }

    /* access modifiers changed from: package-private */
    public void setFrom(@Nullable GifOptions source) {
        if (source == null) {
            reset();
            return;
        }
        this.inIsOpaque = source.inIsOpaque;
        this.inSampleSize = source.inSampleSize;
    }
}
