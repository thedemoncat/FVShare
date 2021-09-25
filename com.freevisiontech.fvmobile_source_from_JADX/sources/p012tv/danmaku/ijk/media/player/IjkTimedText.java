package p012tv.danmaku.ijk.media.player;

import android.graphics.Rect;

/* renamed from: tv.danmaku.ijk.media.player.IjkTimedText */
public final class IjkTimedText {
    private Rect mTextBounds = null;
    private String mTextChars = null;

    public IjkTimedText(Rect bounds, String text) {
        this.mTextBounds = bounds;
        this.mTextChars = text;
    }

    public Rect getBounds() {
        return this.mTextBounds;
    }

    public String getText() {
        return this.mTextChars;
    }
}
