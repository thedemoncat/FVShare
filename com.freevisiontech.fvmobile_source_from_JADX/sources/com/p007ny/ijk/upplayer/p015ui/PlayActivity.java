package com.p007ny.ijk.upplayer.p015ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.view.MotionEvent;
import com.p007ny.ijk.upplayer.C1646R;
import com.p007ny.ijk.upplayer.media.IjkVideoView;
import com.p007ny.ijk.upplayer.media.PlayerManager;
import java.util.List;

/* renamed from: com.ny.ijk.upplayer.ui.PlayActivity */
public class PlayActivity extends AppCompatActivity {
    private PlayerManager mPlayerManager;
    private int mPosition;
    private IjkVideoView mVideoView;
    List<String> urls = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1646R.layout.player_content);
        this.urls = getIntent().getStringArrayListExtra("urls");
        this.mPosition = getIntent().getIntExtra("position", 0);
        initVideo();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mPlayerManager.gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void initVideo() {
        if (this.urls != null && this.urls.size() > 0) {
            this.mPlayerManager = new PlayerManager(this);
            this.mPlayerManager.live(false);
            this.mPlayerManager.setScaleType(PlayerManager.SCALETYPE_FITPARENT);
            this.mPlayerManager.play(this.urls, this.mPosition);
        }
    }

    private void initView() {
        this.mVideoView = (IjkVideoView) findViewById(C1646R.C1648id.up_player_view);
        this.mVideoView.setAspectRatio(0);
        this.mVideoView.start();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mPlayerManager.pause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.mPlayerManager.onResume();
        super.onResume();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mPlayerManager.onDestroy();
    }
}
