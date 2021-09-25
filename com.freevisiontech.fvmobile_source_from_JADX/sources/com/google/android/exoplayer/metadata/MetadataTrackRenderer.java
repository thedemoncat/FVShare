package com.google.android.exoplayer.metadata;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.MediaFormatHolder;
import com.google.android.exoplayer.SampleHolder;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.SampleSourceTrackRenderer;
import com.google.android.exoplayer.util.Assertions;
import java.io.IOException;

public final class MetadataTrackRenderer<T> extends SampleSourceTrackRenderer implements Handler.Callback {
    private static final int MSG_INVOKE_RENDERER = 0;
    private final MediaFormatHolder formatHolder;
    private boolean inputStreamEnded;
    private final Handler metadataHandler;
    private final MetadataParser<T> metadataParser;
    private final MetadataRenderer<T> metadataRenderer;
    private T pendingMetadata;
    private long pendingMetadataTimestamp;
    private final SampleHolder sampleHolder;

    public interface MetadataRenderer<T> {
        void onMetadata(T t);
    }

    public MetadataTrackRenderer(SampleSource source, MetadataParser<T> metadataParser2, MetadataRenderer<T> metadataRenderer2, Looper metadataRendererLooper) {
        super(source);
        this.metadataParser = (MetadataParser) Assertions.checkNotNull(metadataParser2);
        this.metadataRenderer = (MetadataRenderer) Assertions.checkNotNull(metadataRenderer2);
        this.metadataHandler = metadataRendererLooper == null ? null : new Handler(metadataRendererLooper, this);
        this.formatHolder = new MediaFormatHolder();
        this.sampleHolder = new SampleHolder(1);
    }

    /* access modifiers changed from: protected */
    public boolean handlesTrack(MediaFormat mediaFormat) {
        return this.metadataParser.canParse(mediaFormat.mimeType);
    }

    /* access modifiers changed from: protected */
    public void onDiscontinuity(long positionUs) {
        this.pendingMetadata = null;
        this.inputStreamEnded = false;
    }

    /* access modifiers changed from: protected */
    public void doSomeWork(long positionUs, long elapsedRealtimeUs, boolean sourceIsReady) throws ExoPlaybackException {
        if (!this.inputStreamEnded && this.pendingMetadata == null) {
            this.sampleHolder.clearData();
            int result = readSource(positionUs, this.formatHolder, this.sampleHolder);
            if (result == -3) {
                this.pendingMetadataTimestamp = this.sampleHolder.timeUs;
                try {
                    this.pendingMetadata = this.metadataParser.parse(this.sampleHolder.data.array(), this.sampleHolder.size);
                } catch (IOException e) {
                    throw new ExoPlaybackException((Throwable) e);
                }
            } else if (result == -1) {
                this.inputStreamEnded = true;
            }
        }
        if (this.pendingMetadata != null && this.pendingMetadataTimestamp <= positionUs) {
            invokeRenderer(this.pendingMetadata);
            this.pendingMetadata = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onDisabled() throws ExoPlaybackException {
        this.pendingMetadata = null;
        super.onDisabled();
    }

    /* access modifiers changed from: protected */
    public long getBufferedPositionUs() {
        return -3;
    }

    /* access modifiers changed from: protected */
    public boolean isEnded() {
        return this.inputStreamEnded;
    }

    /* access modifiers changed from: protected */
    public boolean isReady() {
        return true;
    }

    private void invokeRenderer(T metadata) {
        if (this.metadataHandler != null) {
            this.metadataHandler.obtainMessage(0, metadata).sendToTarget();
        } else {
            invokeRendererInternal(metadata);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                invokeRendererInternal(msg.obj);
                return true;
            default:
                return false;
        }
    }

    private void invokeRendererInternal(T metadata) {
        this.metadataRenderer.onMetadata(metadata);
    }
}
