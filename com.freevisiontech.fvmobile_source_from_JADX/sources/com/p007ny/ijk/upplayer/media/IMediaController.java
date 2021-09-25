package com.p007ny.ijk.upplayer.media;

import android.view.View;
import android.widget.MediaController;

/* renamed from: com.ny.ijk.upplayer.media.IMediaController */
public interface IMediaController {
    void hide();

    boolean isShowing();

    void setAnchorView(View view);

    void setEnabled(boolean z);

    void setMediaPlayer(MediaController.MediaPlayerControl mediaPlayerControl);

    void show();

    void show(int i);

    void showOnce(View view);
}
