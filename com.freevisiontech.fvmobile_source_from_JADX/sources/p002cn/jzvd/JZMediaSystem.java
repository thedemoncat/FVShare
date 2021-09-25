package p002cn.jzvd;

import android.media.MediaPlayer;
import android.view.Surface;
import java.lang.reflect.Method;
import java.util.Map;

/* renamed from: cn.jzvd.JZMediaSystem */
public class JZMediaSystem extends JZMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;

    public void start() {
        this.mediaPlayer.start();
    }

    public void prepare() {
        try {
            this.mediaPlayer = new MediaPlayer();
            this.mediaPlayer.setAudioStreamType(3);
            if (this.dataSourceObjects.length > 1) {
                this.mediaPlayer.setLooping(((Boolean) this.dataSourceObjects[1]).booleanValue());
            }
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnBufferingUpdateListener(this);
            this.mediaPlayer.setScreenOnWhilePlaying(true);
            this.mediaPlayer.setOnSeekCompleteListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnInfoListener(this);
            this.mediaPlayer.setOnVideoSizeChangedListener(this);
            Method method = MediaPlayer.class.getDeclaredMethod("setDataSource", new Class[]{String.class, Map.class});
            if (this.dataSourceObjects.length > 2) {
                method.invoke(this.mediaPlayer, new Object[]{this.currentDataSource.toString(), this.dataSourceObjects[2]});
            } else {
                method.invoke(this.mediaPlayer, new Object[]{this.currentDataSource.toString(), null});
            }
            this.mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        this.mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    public void seekTo(long time) {
        try {
            this.mediaPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
        }
    }

    public long getCurrentPosition() {
        if (this.mediaPlayer != null) {
            return (long) this.mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public long getDuration() {
        if (this.mediaPlayer != null) {
            return (long) this.mediaPlayer.getDuration();
        }
        return 0;
    }

    public void setSurface(Surface surface) {
        this.mediaPlayer.setSurface(surface);
    }

    public void setVolume(float leftVolume, float rightVolume) {
        this.mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void onPrepared(MediaPlayer mediaPlayer2) {
        mediaPlayer2.start();
        if (this.currentDataSource.toString().toLowerCase().contains("mp3") || this.currentDataSource.toString().toLowerCase().contains("wav")) {
            JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
                public void run() {
                    if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                        JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer2) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer2, final int percent) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    public void onSeekComplete(MediaPlayer mediaPlayer2) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    public boolean onError(MediaPlayer mediaPlayer2, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    public boolean onInfo(MediaPlayer mediaPlayer2, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() == null) {
                    return;
                }
                if (what != 3) {
                    JZVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                } else if (JZVideoPlayerManager.getCurrentJzvd().currentState == 1 || JZVideoPlayerManager.getCurrentJzvd().currentState == 2) {
                    JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                }
            }
        });
        return false;
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer2, int width, int height) {
        JZMediaManager.instance().currentVideoWidth = width;
        JZMediaManager.instance().currentVideoHeight = height;
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
