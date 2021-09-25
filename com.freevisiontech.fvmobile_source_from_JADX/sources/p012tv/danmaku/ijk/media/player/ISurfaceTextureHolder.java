package p012tv.danmaku.ijk.media.player;

import android.graphics.SurfaceTexture;

/* renamed from: tv.danmaku.ijk.media.player.ISurfaceTextureHolder */
public interface ISurfaceTextureHolder {
    SurfaceTexture getSurfaceTexture();

    void setSurfaceTexture(SurfaceTexture surfaceTexture);

    void setSurfaceTextureHost(ISurfaceTextureHost iSurfaceTextureHost);
}
