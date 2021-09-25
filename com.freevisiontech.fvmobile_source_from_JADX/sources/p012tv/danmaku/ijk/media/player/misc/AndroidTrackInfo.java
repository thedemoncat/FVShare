package p012tv.danmaku.ijk.media.player.misc;

import android.annotation.TargetApi;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.os.Build;

/* renamed from: tv.danmaku.ijk.media.player.misc.AndroidTrackInfo */
public class AndroidTrackInfo implements ITrackInfo {
    private final MediaPlayer.TrackInfo mTrackInfo;

    public static AndroidTrackInfo[] fromMediaPlayer(MediaPlayer mp) {
        if (Build.VERSION.SDK_INT >= 16) {
            return fromTrackInfo(mp.getTrackInfo());
        }
        return null;
    }

    private static AndroidTrackInfo[] fromTrackInfo(MediaPlayer.TrackInfo[] trackInfos) {
        if (trackInfos == null) {
            return null;
        }
        AndroidTrackInfo[] androidTrackInfo = new AndroidTrackInfo[trackInfos.length];
        for (int i = 0; i < trackInfos.length; i++) {
            androidTrackInfo[i] = new AndroidTrackInfo(trackInfos[i]);
        }
        return androidTrackInfo;
    }

    private AndroidTrackInfo(MediaPlayer.TrackInfo trackInfo) {
        this.mTrackInfo = trackInfo;
    }

    @TargetApi(19)
    public IMediaFormat getFormat() {
        MediaFormat mediaFormat;
        if (this.mTrackInfo == null || Build.VERSION.SDK_INT < 19 || (mediaFormat = this.mTrackInfo.getFormat()) == null) {
            return null;
        }
        return new AndroidMediaFormat(mediaFormat);
    }

    @TargetApi(16)
    public String getLanguage() {
        if (this.mTrackInfo == null) {
            return "und";
        }
        return this.mTrackInfo.getLanguage();
    }

    @TargetApi(16)
    public int getTrackType() {
        if (this.mTrackInfo == null) {
            return 0;
        }
        return this.mTrackInfo.getTrackType();
    }

    @TargetApi(16)
    public String toString() {
        StringBuilder out = new StringBuilder(128);
        out.append(getClass().getSimpleName());
        out.append('{');
        if (this.mTrackInfo != null) {
            out.append(this.mTrackInfo.toString());
        } else {
            out.append("null");
        }
        out.append('}');
        return out.toString();
    }

    @TargetApi(16)
    public String getInfoInline() {
        if (this.mTrackInfo != null) {
            return this.mTrackInfo.toString();
        }
        return "null";
    }
}
