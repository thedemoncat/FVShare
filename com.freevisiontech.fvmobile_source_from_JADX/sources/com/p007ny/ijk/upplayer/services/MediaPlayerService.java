package com.p007ny.ijk.upplayer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import p012tv.danmaku.ijk.media.player.IMediaPlayer;

/* renamed from: com.ny.ijk.upplayer.services.MediaPlayerService */
public class MediaPlayerService extends Service {
    private static IMediaPlayer sMediaPlayer;

    public static Intent newIntent(Context context) {
        return new Intent(context, MediaPlayerService.class);
    }

    public static void intentToStart(Context context) {
        context.startService(newIntent(context));
    }

    public static void intentToStop(Context context) {
        context.stopService(newIntent(context));
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setMediaPlayer(IMediaPlayer mp) {
        if (!(sMediaPlayer == null || sMediaPlayer == mp)) {
            if (sMediaPlayer.isPlaying()) {
                sMediaPlayer.stop();
            }
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
        sMediaPlayer = mp;
    }

    public static IMediaPlayer getMediaPlayer() {
        return sMediaPlayer;
    }
}
