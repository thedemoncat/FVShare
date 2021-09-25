package com.google.android.exoplayer.upstream;

import android.os.Handler;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Clock;
import com.google.android.exoplayer.util.SlidingPercentile;
import com.google.android.exoplayer.util.SystemClock;

public final class DefaultBandwidthMeter implements BandwidthMeter {
    public static final int DEFAULT_MAX_WEIGHT = 2000;
    private long bitrateEstimate;
    private long bytesAccumulator;
    private final Clock clock;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final BandwidthMeter.EventListener eventListener;
    private final SlidingPercentile slidingPercentile;
    private long startTimeMs;
    private int streamCount;

    public DefaultBandwidthMeter() {
        this((Handler) null, (BandwidthMeter.EventListener) null);
    }

    public DefaultBandwidthMeter(Handler eventHandler2, BandwidthMeter.EventListener eventListener2) {
        this(eventHandler2, eventListener2, (Clock) new SystemClock());
    }

    public DefaultBandwidthMeter(Handler eventHandler2, BandwidthMeter.EventListener eventListener2, Clock clock2) {
        this(eventHandler2, eventListener2, clock2, 2000);
    }

    public DefaultBandwidthMeter(Handler eventHandler2, BandwidthMeter.EventListener eventListener2, int maxWeight) {
        this(eventHandler2, eventListener2, new SystemClock(), maxWeight);
    }

    public DefaultBandwidthMeter(Handler eventHandler2, BandwidthMeter.EventListener eventListener2, Clock clock2, int maxWeight) {
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.clock = clock2;
        this.slidingPercentile = new SlidingPercentile(maxWeight);
        this.bitrateEstimate = -1;
    }

    public synchronized long getBitrateEstimate() {
        return this.bitrateEstimate;
    }

    public synchronized void onTransferStart() {
        if (this.streamCount == 0) {
            this.startTimeMs = this.clock.elapsedRealtime();
        }
        this.streamCount++;
    }

    public synchronized void onBytesTransferred(int bytes) {
        this.bytesAccumulator += (long) bytes;
    }

    public synchronized void onTransferEnd() {
        Assertions.checkState(this.streamCount > 0);
        long nowMs = this.clock.elapsedRealtime();
        int elapsedMs = (int) (nowMs - this.startTimeMs);
        if (elapsedMs > 0) {
            this.slidingPercentile.addSample((int) Math.sqrt((double) this.bytesAccumulator), (float) ((this.bytesAccumulator * 8000) / ((long) elapsedMs)));
            float bandwidthEstimateFloat = this.slidingPercentile.getPercentile(0.5f);
            this.bitrateEstimate = Float.isNaN(bandwidthEstimateFloat) ? -1 : (long) bandwidthEstimateFloat;
            notifyBandwidthSample(elapsedMs, this.bytesAccumulator, this.bitrateEstimate);
        }
        this.streamCount--;
        if (this.streamCount > 0) {
            this.startTimeMs = nowMs;
        }
        this.bytesAccumulator = 0;
    }

    private void notifyBandwidthSample(int elapsedMs, long bytes, long bitrate) {
        if (this.eventHandler != null && this.eventListener != null) {
            final int i = elapsedMs;
            final long j = bytes;
            final long j2 = bitrate;
            this.eventHandler.post(new Runnable() {
                public void run() {
                    DefaultBandwidthMeter.this.eventListener.onBandwidthSample(i, j, j2);
                }
            });
        }
    }
}
