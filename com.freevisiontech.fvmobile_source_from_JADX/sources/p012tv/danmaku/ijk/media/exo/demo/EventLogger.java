package p012tv.danmaku.ijk.media.exo.demo;

import android.media.MediaCodec;
import android.os.SystemClock;
import android.util.Log;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.util.VerboseLogUtil;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import p012tv.danmaku.ijk.media.exo.demo.player.DemoPlayer;

/* renamed from: tv.danmaku.ijk.media.exo.demo.EventLogger */
public class EventLogger implements DemoPlayer.Listener, DemoPlayer.InfoListener, DemoPlayer.InternalErrorListener {
    private static final String TAG = "EventLogger";
    private static final NumberFormat TIME_FORMAT = NumberFormat.getInstance(Locale.US);
    private long[] availableRangeValuesUs;
    private long[] loadStartTimeMs = new long[4];
    private long sessionStartTimeMs;

    static {
        TIME_FORMAT.setMinimumFractionDigits(2);
        TIME_FORMAT.setMaximumFractionDigits(2);
    }

    public void startSession() {
        this.sessionStartTimeMs = SystemClock.elapsedRealtime();
        Log.d(TAG, "start [0]");
    }

    public void endSession() {
        Log.d(TAG, "end [" + getSessionTimeString() + "]");
    }

    public void onStateChanged(boolean playWhenReady, int state) {
        Log.d(TAG, "state [" + getSessionTimeString() + ", " + playWhenReady + ", " + getStateString(state) + "]");
    }

    public void onError(Exception e) {
        Log.e(TAG, "playerFailed [" + getSessionTimeString() + "]", e);
    }

    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "videoSizeChanged [" + width + ", " + height + ", " + unappliedRotationDegrees + ", " + pixelWidthHeightRatio + "]");
    }

    public void onBandwidthSample(int elapsedMs, long bytes, long bitrateEstimate) {
        Log.d(TAG, "bandwidth [" + getSessionTimeString() + ", " + bytes + ", " + getTimeString((long) elapsedMs) + ", " + bitrateEstimate + "]");
    }

    public void onDroppedFrames(int count, long elapsed) {
        Log.d(TAG, "droppedFrames [" + getSessionTimeString() + ", " + count + "]");
    }

    public void onLoadStarted(int sourceId, long length, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
        this.loadStartTimeMs[sourceId] = SystemClock.elapsedRealtime();
        if (VerboseLogUtil.isTagEnabled(TAG)) {
            Log.v(TAG, "loadStart [" + getSessionTimeString() + ", " + sourceId + ", " + type + ", " + mediaStartTimeMs + ", " + mediaEndTimeMs + "]");
        }
    }

    public void onLoadCompleted(int sourceId, long bytesLoaded, int type, int trigger, Format format, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
        if (VerboseLogUtil.isTagEnabled(TAG)) {
            Log.v(TAG, "loadEnd [" + getSessionTimeString() + ", " + sourceId + ", " + (SystemClock.elapsedRealtime() - this.loadStartTimeMs[sourceId]) + "]");
        }
    }

    public void onVideoFormatEnabled(Format format, int trigger, long mediaTimeMs) {
        Log.d(TAG, "videoFormat [" + getSessionTimeString() + ", " + format.f1192id + ", " + Integer.toString(trigger) + "]");
    }

    public void onAudioFormatEnabled(Format format, int trigger, long mediaTimeMs) {
        Log.d(TAG, "audioFormat [" + getSessionTimeString() + ", " + format.f1192id + ", " + Integer.toString(trigger) + "]");
    }

    public void onLoadError(int sourceId, IOException e) {
        printInternalError("loadError", e);
    }

    public void onRendererInitializationError(Exception e) {
        printInternalError("rendererInitError", e);
    }

    public void onDrmSessionManagerError(Exception e) {
        printInternalError("drmSessionManagerError", e);
    }

    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {
        printInternalError("decoderInitializationError", e);
    }

    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {
        printInternalError("audioTrackInitializationError", e);
    }

    public void onAudioTrackWriteError(AudioTrack.WriteException e) {
        printInternalError("audioTrackWriteError", e);
    }

    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        printInternalError("audioTrackUnderrun [" + bufferSize + ", " + bufferSizeMs + ", " + elapsedSinceLastFeedMs + "]", (Exception) null);
    }

    public void onCryptoError(MediaCodec.CryptoException e) {
        printInternalError("cryptoError", e);
    }

    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {
        Log.d(TAG, "decoderInitialized [" + getSessionTimeString() + ", " + decoderName + "]");
    }

    public void onAvailableRangeChanged(int sourceId, TimeRange availableRange) {
        this.availableRangeValuesUs = availableRange.getCurrentBoundsUs(this.availableRangeValuesUs);
        Log.d(TAG, "availableRange [" + availableRange.isStatic() + ", " + this.availableRangeValuesUs[0] + ", " + this.availableRangeValuesUs[1] + "]");
    }

    private void printInternalError(String type, Exception e) {
        Log.e(TAG, "internalError [" + getSessionTimeString() + ", " + type + "]", e);
    }

    private String getStateString(int state) {
        switch (state) {
            case 1:
                return "I";
            case 2:
                return "P";
            case 3:
                return "B";
            case 4:
                return "R";
            case 5:
                return "E";
            default:
                return "?";
        }
    }

    private String getSessionTimeString() {
        return getTimeString(SystemClock.elapsedRealtime() - this.sessionStartTimeMs);
    }

    private String getTimeString(long timeMs) {
        return TIME_FORMAT.format((double) (((float) timeMs) / 1000.0f));
    }
}
