package android.support.p001v4.media.session;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.p001v4.media.session.MediaSessionCompatApi21;

@RequiresApi(23)
/* renamed from: android.support.v4.media.session.MediaSessionCompatApi23 */
class MediaSessionCompatApi23 {

    /* renamed from: android.support.v4.media.session.MediaSessionCompatApi23$Callback */
    public interface Callback extends MediaSessionCompatApi21.Callback {
        void onPlayFromUri(Uri uri, Bundle bundle);
    }

    MediaSessionCompatApi23() {
    }

    public static Object createCallback(Callback callback) {
        return new CallbackProxy(callback);
    }

    /* renamed from: android.support.v4.media.session.MediaSessionCompatApi23$CallbackProxy */
    static class CallbackProxy<T extends Callback> extends MediaSessionCompatApi21.CallbackProxy<T> {
        public CallbackProxy(T callback) {
            super(callback);
        }

        public void onPlayFromUri(Uri uri, Bundle extras) {
            ((Callback) this.mCallback).onPlayFromUri(uri, extras);
        }
    }
}
