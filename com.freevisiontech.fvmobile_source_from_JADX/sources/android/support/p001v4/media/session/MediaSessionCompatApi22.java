package android.support.p001v4.media.session;

import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
/* renamed from: android.support.v4.media.session.MediaSessionCompatApi22 */
class MediaSessionCompatApi22 {
    MediaSessionCompatApi22() {
    }

    public static void setRatingType(Object sessionObj, int type) {
        ((MediaSession) sessionObj).setRatingType(type);
    }
}
