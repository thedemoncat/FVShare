package com.freevisiontech.fvmobile.utility;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import com.freevisiontech.fvmobile.utils.LocalResourceManager;
import com.vise.log.ViseLog;
import java.io.IOException;

public class BackgroundMusic {
    private static final String TAG = "Bg_Music";
    private static BackgroundMusic backgroundMusic = null;
    /* access modifiers changed from: private */
    public MediaPlayer mBackgroundMediaPlayer;
    private Context mContext;
    private String mCurrentPath;
    private boolean mIsPaused;
    private float mLeftVolume;
    private float mRightVolume;

    private BackgroundMusic(Context context) {
        this.mContext = context;
        initData();
    }

    public static BackgroundMusic getInstance(Context context) {
        if (backgroundMusic == null) {
            backgroundMusic = new BackgroundMusic(context);
        }
        return backgroundMusic;
    }

    private void initData() {
        this.mLeftVolume = 0.5f;
        this.mRightVolume = 0.5f;
        this.mBackgroundMediaPlayer = null;
        this.mIsPaused = false;
        this.mCurrentPath = null;
    }

    public void playBackgroundMusic(String path, boolean isLoop) {
        if (this.mCurrentPath == null) {
            this.mBackgroundMediaPlayer = getMediaPlayer(path);
            this.mCurrentPath = path;
        } else if (!this.mCurrentPath.equals(path)) {
            if (this.mBackgroundMediaPlayer != null) {
                this.mBackgroundMediaPlayer.release();
            }
            this.mBackgroundMediaPlayer = getMediaPlayer(path);
            this.mCurrentPath = path;
        }
        if (this.mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
            return;
        }
        this.mBackgroundMediaPlayer.stop();
        this.mBackgroundMediaPlayer.setLooping(isLoop);
        try {
            this.mBackgroundMediaPlayer.prepare();
            this.mBackgroundMediaPlayer.seekTo(0);
            this.mBackgroundMediaPlayer.start();
            this.mIsPaused = false;
        } catch (Exception e) {
            Log.e(TAG, "playBackgroundMusic: error state");
        }
    }

    public void stopBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.stop();
            this.mIsPaused = false;
        }
    }

    public void pauseBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null && this.mBackgroundMediaPlayer.isPlaying()) {
            this.mBackgroundMediaPlayer.pause();
            this.mIsPaused = true;
        }
    }

    public void resumeBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null && this.mIsPaused) {
            this.mBackgroundMediaPlayer.start();
            this.mIsPaused = false;
        }
    }

    public void playRecordSound(Context context, String soundName) {
        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd(soundName + ".mp3");
            ViseLog.m1466e("playRecordSound soundName: " + soundName + ".mp3");
            ViseLog.m1466e("playRecordSound fileDescriptor == null: " + (fileDescriptor == null));
            this.mBackgroundMediaPlayer = new MediaPlayer();
            this.mBackgroundMediaPlayer.setAudioStreamType(3);
            this.mBackgroundMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            this.mBackgroundMediaPlayer.prepareAsync();
            this.mBackgroundMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    if (BackgroundMusic.this.mBackgroundMediaPlayer != null) {
                        BackgroundMusic.this.mBackgroundMediaPlayer.start();
                    }
                }
            });
            this.mBackgroundMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    if (BackgroundMusic.this.mBackgroundMediaPlayer != null) {
                        BackgroundMusic.this.mBackgroundMediaPlayer.release();
                        MediaPlayer unused = BackgroundMusic.this.mBackgroundMediaPlayer = null;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewindBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.stop();
            try {
                this.mBackgroundMediaPlayer.prepare();
                this.mBackgroundMediaPlayer.seekTo(0);
                this.mBackgroundMediaPlayer.start();
                this.mIsPaused = false;
            } catch (Exception e) {
                Log.e(TAG, "rewindBackgroundMusic: error state");
            }
        }
    }

    public boolean isBackgroundMusicPlaying() {
        if (this.mBackgroundMediaPlayer == null) {
            return false;
        }
        return this.mBackgroundMediaPlayer.isPlaying();
    }

    public void end() {
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.release();
        }
        initData();
    }

    public float getBackgroundVolume() {
        if (this.mBackgroundMediaPlayer != null) {
            return (this.mLeftVolume + this.mRightVolume) / 2.0f;
        }
        return 0.0f;
    }

    public void setBackgroundVolume(float volume) {
        this.mRightVolume = volume;
        this.mLeftVolume = volume;
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
        }
    }

    private MediaPlayer getMediaPlayer(String path) {
        try {
            AssetFileDescriptor assetFileDescriptor = LocalResourceManager.getLocalMedia(this.mContext, path);
            MediaPlayer mediaPlayer = new MediaPlayer();
            if (assetFileDescriptor == null) {
                return mediaPlayer;
            }
            try {
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                mediaPlayer.prepare();
                mediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
                return mediaPlayer;
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "error: " + e.getMessage(), e);
            return null;
        }
    }
}
