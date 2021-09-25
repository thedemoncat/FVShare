package org.xutils.image;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Movie;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import org.xutils.common.util.LogUtil;

public class GifDrawable extends Drawable implements Runnable, Animatable {
    private final long begin = SystemClock.uptimeMillis();
    private int byteCount;
    private final int duration;
    private final Movie movie;
    private int rate = 300;
    private volatile boolean running;

    public GifDrawable(Movie movie2, int byteCount2) {
        this.movie = movie2;
        this.byteCount = byteCount2;
        this.duration = movie2.duration();
    }

    public int getDuration() {
        return this.duration;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public int getByteCount() {
        if (this.byteCount == 0) {
            this.byteCount = this.movie.width() * this.movie.height() * 3 * 5;
        }
        return this.byteCount;
    }

    public int getRate() {
        return this.rate;
    }

    public void setRate(int rate2) {
        this.rate = rate2;
    }

    public void draw(Canvas canvas) {
        try {
            this.movie.setTime(this.duration > 0 ? ((int) (SystemClock.uptimeMillis() - this.begin)) % this.duration : 0);
            this.movie.draw(canvas, 0.0f, 0.0f);
            start();
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
    }

    public void start() {
        if (!isRunning()) {
            this.running = true;
            run();
        }
    }

    public void stop() {
        if (isRunning()) {
            unscheduleSelf(this);
        }
    }

    public boolean isRunning() {
        return this.running && this.duration > 0;
    }

    public void run() {
        if (this.duration > 0) {
            invalidateSelf();
            scheduleSelf(this, SystemClock.uptimeMillis() + ((long) this.rate));
        }
    }

    public void setAlpha(int alpha) {
    }

    public int getIntrinsicWidth() {
        return this.movie.width();
    }

    public int getIntrinsicHeight() {
        return this.movie.height();
    }

    public void setColorFilter(ColorFilter cf) {
    }

    public int getOpacity() {
        return this.movie.isOpaque() ? -1 : -3;
    }
}
