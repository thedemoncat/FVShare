package p012tv.danmaku.ijk.media.player.misc;

import android.text.TextUtils;
import p012tv.danmaku.ijk.media.player.IjkMediaMeta;

/* renamed from: tv.danmaku.ijk.media.player.misc.IjkTrackInfo */
public class IjkTrackInfo implements ITrackInfo {
    private IjkMediaMeta.IjkStreamMeta mStreamMeta;
    private int mTrackType = 0;

    public IjkTrackInfo(IjkMediaMeta.IjkStreamMeta streamMeta) {
        this.mStreamMeta = streamMeta;
    }

    public void setMediaMeta(IjkMediaMeta.IjkStreamMeta streamMeta) {
        this.mStreamMeta = streamMeta;
    }

    public IMediaFormat getFormat() {
        return new IjkMediaFormat(this.mStreamMeta);
    }

    public String getLanguage() {
        if (this.mStreamMeta == null || TextUtils.isEmpty(this.mStreamMeta.mLanguage)) {
            return "und";
        }
        return this.mStreamMeta.mLanguage;
    }

    public int getTrackType() {
        return this.mTrackType;
    }

    public void setTrackType(int trackType) {
        this.mTrackType = trackType;
    }

    public String toString() {
        return getClass().getSimpleName() + '{' + getInfoInline() + "}";
    }

    public String getInfoInline() {
        StringBuilder out = new StringBuilder(128);
        switch (this.mTrackType) {
            case 1:
                out.append("VIDEO");
                out.append(", ");
                out.append(this.mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(this.mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(this.mStreamMeta.getResolutionInline());
                break;
            case 2:
                out.append("AUDIO");
                out.append(", ");
                out.append(this.mStreamMeta.getCodecShortNameInline());
                out.append(", ");
                out.append(this.mStreamMeta.getBitrateInline());
                out.append(", ");
                out.append(this.mStreamMeta.getSampleRateInline());
                break;
            case 3:
                out.append("TIMEDTEXT");
                out.append(", ");
                out.append(this.mStreamMeta.mLanguage);
                break;
            case 4:
                out.append("SUBTITLE");
                break;
            default:
                out.append("UNKNOWN");
                break;
        }
        return out.toString();
    }
}
