package com.p007ny.ijk.upplayer.media;

import p012tv.danmaku.ijk.media.player.IMediaPlayer;
import p012tv.danmaku.ijk.media.player.IjkMediaPlayer;
import p012tv.danmaku.ijk.media.player.MediaPlayerProxy;
import p012tv.danmaku.ijk.media.player.TextureMediaPlayer;

/* renamed from: com.ny.ijk.upplayer.media.MediaPlayerCompat */
public class MediaPlayerCompat {
    public static String getName(IMediaPlayer mp) {
        if (mp == null) {
            return "null";
        }
        if (!(mp instanceof TextureMediaPlayer)) {
            return mp.getClass().getSimpleName();
        }
        StringBuilder sb = new StringBuilder("TextureMediaPlayer <");
        IMediaPlayer internalMediaPlayer = ((TextureMediaPlayer) mp).getInternalMediaPlayer();
        if (internalMediaPlayer == null) {
            sb.append("null>");
        } else {
            sb.append(internalMediaPlayer.getClass().getSimpleName());
            sb.append(">");
        }
        return sb.toString();
    }

    public static IjkMediaPlayer getIjkMediaPlayer(IMediaPlayer mp) {
        IjkMediaPlayer ijkMediaPlayer = null;
        if (mp == null) {
            return null;
        }
        if (mp instanceof IjkMediaPlayer) {
            ijkMediaPlayer = (IjkMediaPlayer) mp;
        } else if ((mp instanceof MediaPlayerProxy) && (((MediaPlayerProxy) mp).getInternalMediaPlayer() instanceof IjkMediaPlayer)) {
            ijkMediaPlayer = (IjkMediaPlayer) ((MediaPlayerProxy) mp).getInternalMediaPlayer();
        }
        return ijkMediaPlayer;
    }

    public static void selectTrack(IMediaPlayer mp, int stream) {
        IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.selectTrack(stream);
        }
    }

    public static void deselectTrack(IMediaPlayer mp, int stream) {
        IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.deselectTrack(stream);
        }
    }

    public static int getSelectedTrack(IMediaPlayer mp, int trackType) {
        IjkMediaPlayer ijkMediaPlayer = getIjkMediaPlayer(mp);
        if (ijkMediaPlayer == null) {
            return -1;
        }
        return ijkMediaPlayer.getSelectedTrack(trackType);
    }
}
