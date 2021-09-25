package p004pl.droidsonroids.gif;

import android.support.annotation.NonNull;
import java.io.IOException;

/* renamed from: pl.droidsonroids.gif.GifIOException */
public class GifIOException extends IOException {
    private static final long serialVersionUID = 13038402904505L;
    private final String mErrnoMessage;
    @NonNull
    public final GifError reason;

    public String getMessage() {
        if (this.mErrnoMessage == null) {
            return this.reason.getFormattedDescription();
        }
        return this.reason.getFormattedDescription() + ": " + this.mErrnoMessage;
    }

    private GifIOException(int errorCode, String errnoMessage) {
        this.reason = GifError.fromCode(errorCode);
        this.mErrnoMessage = errnoMessage;
    }

    static GifIOException fromCode(int nativeErrorCode) {
        if (nativeErrorCode == GifError.NO_ERROR.errorCode) {
            return null;
        }
        return new GifIOException(nativeErrorCode, (String) null);
    }
}
