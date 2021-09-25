package p002cn.jzvd;

import android.view.Surface;

/* renamed from: cn.jzvd.JZMediaInterface */
public abstract class JZMediaInterface {
    public Object currentDataSource;
    public Object[] dataSourceObjects;

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract boolean isPlaying();

    public abstract void pause();

    public abstract void prepare();

    public abstract void release();

    public abstract void seekTo(long j);

    public abstract void setSurface(Surface surface);

    public abstract void setVolume(float f, float f2);

    public abstract void start();
}
