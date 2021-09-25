package p012tv.danmaku.ijk.media.player.misc;

/* renamed from: tv.danmaku.ijk.media.player.misc.IMediaFormat */
public interface IMediaFormat {
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_MIME = "mime";
    public static final String KEY_WIDTH = "width";

    int getInteger(String str);

    String getString(String str);
}
