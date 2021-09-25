package com.google.android.exoplayer.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.DeniedByServerException;
import android.media.NotProvisionedException;
import android.media.UnsupportedSchemeException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.google.android.exoplayer.drm.DrmInitData;
import com.google.android.exoplayer.drm.ExoMediaCrypto;
import com.google.android.exoplayer.drm.ExoMediaDrm;
import com.google.android.exoplayer.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer.util.Util;
import java.util.HashMap;
import java.util.UUID;

@TargetApi(18)
public class StreamingDrmSessionManager<T extends ExoMediaCrypto> implements DrmSessionManager<T> {
    private static final int MSG_KEYS = 1;
    private static final int MSG_PROVISION = 0;
    public static final String PLAYREADY_CUSTOM_DATA_KEY = "PRCustomData";
    public static final UUID PLAYREADY_UUID = new UUID(-7348484286925749626L, -6083546864340672619L);
    public static final UUID WIDEVINE_UUID = new UUID(-1301668207276963122L, -6645017420763422227L);
    final MediaDrmCallback callback;
    private final Handler eventHandler;
    /* access modifiers changed from: private */
    public final EventListener eventListener;
    private Exception lastException;
    private T mediaCrypto;
    private final ExoMediaDrm<T> mediaDrm;
    final StreamingDrmSessionManager<T>.MediaDrmHandler mediaDrmHandler;
    /* access modifiers changed from: private */
    public int openCount;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private Handler postRequestHandler;
    final StreamingDrmSessionManager<T>.PostResponseHandler postResponseHandler;
    private boolean provisioningInProgress;
    private HandlerThread requestHandlerThread;
    private DrmInitData.SchemeInitData schemeInitData;
    private byte[] sessionId;
    /* access modifiers changed from: private */
    public int state = 1;
    final UUID uuid;

    public interface EventListener {
        void onDrmKeysLoaded();

        void onDrmSessionManagerError(Exception exc);
    }

    private static FrameworkMediaDrm createFrameworkDrm(UUID uuid2) throws UnsupportedDrmException {
        try {
            return new FrameworkMediaDrm(uuid2);
        } catch (UnsupportedSchemeException e) {
            throw new UnsupportedDrmException(1, e);
        } catch (Exception e2) {
            throw new UnsupportedDrmException(2, e2);
        }
    }

    public static StreamingDrmSessionManager<FrameworkMediaCrypto> newWidevineInstance(Looper playbackLooper, MediaDrmCallback callback2, HashMap<String, String> optionalKeyRequestParameters2, Handler eventHandler2, EventListener eventListener2) throws UnsupportedDrmException {
        return newFrameworkInstance(WIDEVINE_UUID, playbackLooper, callback2, optionalKeyRequestParameters2, eventHandler2, eventListener2);
    }

    public static StreamingDrmSessionManager<FrameworkMediaCrypto> newPlayReadyInstance(Looper playbackLooper, MediaDrmCallback callback2, String customData, Handler eventHandler2, EventListener eventListener2) throws UnsupportedDrmException {
        HashMap<String, String> optionalKeyRequestParameters2;
        if (!TextUtils.isEmpty(customData)) {
            optionalKeyRequestParameters2 = new HashMap<>();
            optionalKeyRequestParameters2.put(PLAYREADY_CUSTOM_DATA_KEY, customData);
        } else {
            optionalKeyRequestParameters2 = null;
        }
        return newFrameworkInstance(PLAYREADY_UUID, playbackLooper, callback2, optionalKeyRequestParameters2, eventHandler2, eventListener2);
    }

    public static StreamingDrmSessionManager<FrameworkMediaCrypto> newFrameworkInstance(UUID uuid2, Looper playbackLooper, MediaDrmCallback callback2, HashMap<String, String> optionalKeyRequestParameters2, Handler eventHandler2, EventListener eventListener2) throws UnsupportedDrmException {
        return newInstance(uuid2, playbackLooper, callback2, optionalKeyRequestParameters2, eventHandler2, eventListener2, createFrameworkDrm(uuid2));
    }

    public static <T extends ExoMediaCrypto> StreamingDrmSessionManager<T> newInstance(UUID uuid2, Looper playbackLooper, MediaDrmCallback callback2, HashMap<String, String> optionalKeyRequestParameters2, Handler eventHandler2, EventListener eventListener2, ExoMediaDrm<T> mediaDrm2) throws UnsupportedDrmException {
        return new StreamingDrmSessionManager<>(uuid2, playbackLooper, callback2, optionalKeyRequestParameters2, eventHandler2, eventListener2, mediaDrm2);
    }

    private StreamingDrmSessionManager(UUID uuid2, Looper playbackLooper, MediaDrmCallback callback2, HashMap<String, String> optionalKeyRequestParameters2, Handler eventHandler2, EventListener eventListener2, ExoMediaDrm<T> mediaDrm2) throws UnsupportedDrmException {
        this.uuid = uuid2;
        this.callback = callback2;
        this.optionalKeyRequestParameters = optionalKeyRequestParameters2;
        this.eventHandler = eventHandler2;
        this.eventListener = eventListener2;
        this.mediaDrm = mediaDrm2;
        mediaDrm2.setOnEventListener(new MediaDrmEventListener());
        this.mediaDrmHandler = new MediaDrmHandler(playbackLooper);
        this.postResponseHandler = new PostResponseHandler(playbackLooper);
    }

    public final int getState() {
        return this.state;
    }

    public final T getMediaCrypto() {
        if (this.state == 3 || this.state == 4) {
            return this.mediaCrypto;
        }
        throw new IllegalStateException();
    }

    public boolean requiresSecureDecoderComponent(String mimeType) {
        if (this.state == 3 || this.state == 4) {
            return this.mediaCrypto.requiresSecureDecoderComponent(mimeType);
        }
        throw new IllegalStateException();
    }

    public final Exception getError() {
        if (this.state == 0) {
            return this.lastException;
        }
        return null;
    }

    public final String getPropertyString(String key) {
        return this.mediaDrm.getPropertyString(key);
    }

    public final void setPropertyString(String key, String value) {
        this.mediaDrm.setPropertyString(key, value);
    }

    public final byte[] getPropertyByteArray(String key) {
        return this.mediaDrm.getPropertyByteArray(key);
    }

    public final void setPropertyByteArray(String key, byte[] value) {
        this.mediaDrm.setPropertyByteArray(key, value);
    }

    public void open(DrmInitData drmInitData) {
        byte[] psshData;
        int i = this.openCount + 1;
        this.openCount = i;
        if (i == 1) {
            if (this.postRequestHandler == null) {
                this.requestHandlerThread = new HandlerThread("DrmRequestHandler");
                this.requestHandlerThread.start();
                this.postRequestHandler = new PostRequestHandler(this.requestHandlerThread.getLooper());
            }
            if (this.schemeInitData == null) {
                this.schemeInitData = drmInitData.get(this.uuid);
                if (this.schemeInitData == null) {
                    onError(new IllegalStateException("Media does not support uuid: " + this.uuid));
                    return;
                } else if (Util.SDK_INT < 21 && (psshData = PsshAtomUtil.parseSchemeSpecificData(this.schemeInitData.data, WIDEVINE_UUID)) != null) {
                    this.schemeInitData = new DrmInitData.SchemeInitData(this.schemeInitData.mimeType, psshData);
                }
            }
            this.state = 2;
            openInternal(true);
        }
    }

    public void close() {
        int i = this.openCount - 1;
        this.openCount = i;
        if (i == 0) {
            this.state = 1;
            this.provisioningInProgress = false;
            this.mediaDrmHandler.removeCallbacksAndMessages((Object) null);
            this.postResponseHandler.removeCallbacksAndMessages((Object) null);
            this.postRequestHandler.removeCallbacksAndMessages((Object) null);
            this.postRequestHandler = null;
            this.requestHandlerThread.quit();
            this.requestHandlerThread = null;
            this.schemeInitData = null;
            this.mediaCrypto = null;
            this.lastException = null;
            if (this.sessionId != null) {
                this.mediaDrm.closeSession(this.sessionId);
                this.sessionId = null;
            }
        }
    }

    private void openInternal(boolean allowProvisioning) {
        try {
            this.sessionId = this.mediaDrm.openSession();
            this.mediaCrypto = this.mediaDrm.createMediaCrypto(this.uuid, this.sessionId);
            this.state = 3;
            postKeyRequest();
        } catch (NotProvisionedException e) {
            if (allowProvisioning) {
                postProvisionRequest();
            } else {
                onError(e);
            }
        } catch (Exception e2) {
            onError(e2);
        }
    }

    /* access modifiers changed from: private */
    public void postProvisionRequest() {
        if (!this.provisioningInProgress) {
            this.provisioningInProgress = true;
            this.postRequestHandler.obtainMessage(0, this.mediaDrm.getProvisionRequest()).sendToTarget();
        }
    }

    /* access modifiers changed from: private */
    public void onProvisionResponse(Object response) {
        this.provisioningInProgress = false;
        if (this.state != 2 && this.state != 3 && this.state != 4) {
            return;
        }
        if (response instanceof Exception) {
            onError((Exception) response);
            return;
        }
        try {
            this.mediaDrm.provideProvisionResponse((byte[]) response);
            if (this.state == 2) {
                openInternal(false);
            } else {
                postKeyRequest();
            }
        } catch (DeniedByServerException e) {
            onError(e);
        }
    }

    /* access modifiers changed from: private */
    public void postKeyRequest() {
        try {
            this.postRequestHandler.obtainMessage(1, this.mediaDrm.getKeyRequest(this.sessionId, this.schemeInitData.data, this.schemeInitData.mimeType, 1, this.optionalKeyRequestParameters)).sendToTarget();
        } catch (NotProvisionedException e) {
            onKeysError(e);
        }
    }

    /* access modifiers changed from: private */
    public void onKeyResponse(Object response) {
        if (this.state != 3 && this.state != 4) {
            return;
        }
        if (response instanceof Exception) {
            onKeysError((Exception) response);
            return;
        }
        try {
            this.mediaDrm.provideKeyResponse(this.sessionId, (byte[]) response);
            this.state = 4;
            if (this.eventHandler != null && this.eventListener != null) {
                this.eventHandler.post(new Runnable() {
                    public void run() {
                        StreamingDrmSessionManager.this.eventListener.onDrmKeysLoaded();
                    }
                });
            }
        } catch (Exception e) {
            onKeysError(e);
        }
    }

    private void onKeysError(Exception e) {
        if (e instanceof NotProvisionedException) {
            postProvisionRequest();
        } else {
            onError(e);
        }
    }

    /* access modifiers changed from: private */
    public void onError(final Exception e) {
        this.lastException = e;
        if (!(this.eventHandler == null || this.eventListener == null)) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    StreamingDrmSessionManager.this.eventListener.onDrmSessionManagerError(e);
                }
            });
        }
        if (this.state != 4) {
            this.state = 0;
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class MediaDrmHandler extends Handler {
        public MediaDrmHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (StreamingDrmSessionManager.this.openCount == 0) {
                return;
            }
            if (StreamingDrmSessionManager.this.state == 3 || StreamingDrmSessionManager.this.state == 4) {
                switch (msg.what) {
                    case 1:
                        int unused = StreamingDrmSessionManager.this.state = 3;
                        StreamingDrmSessionManager.this.postProvisionRequest();
                        return;
                    case 2:
                        StreamingDrmSessionManager.this.postKeyRequest();
                        return;
                    case 3:
                        int unused2 = StreamingDrmSessionManager.this.state = 3;
                        StreamingDrmSessionManager.this.onError(new KeysExpiredException());
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private class MediaDrmEventListener implements ExoMediaDrm.OnEventListener<T> {
        private MediaDrmEventListener() {
        }

        public void onEvent(ExoMediaDrm<? extends T> exoMediaDrm, byte[] sessionId, int event, int extra, byte[] data) {
            StreamingDrmSessionManager.this.mediaDrmHandler.sendEmptyMessage(event);
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class PostResponseHandler extends Handler {
        public PostResponseHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    StreamingDrmSessionManager.this.onProvisionResponse(msg.obj);
                    return;
                case 1:
                    StreamingDrmSessionManager.this.onKeyResponse(msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class PostRequestHandler extends Handler {
        public PostRequestHandler(Looper backgroundLooper) {
            super(backgroundLooper);
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r6) {
            /*
                r5 = this;
                int r2 = r6.what     // Catch:{ Exception -> 0x000b }
                switch(r2) {
                    case 0: goto L_0x001b;
                    case 1: goto L_0x002c;
                    default: goto L_0x0005;
                }     // Catch:{ Exception -> 0x000b }
            L_0x0005:
                java.lang.RuntimeException r2 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x000b }
                r2.<init>()     // Catch:{ Exception -> 0x000b }
                throw r2     // Catch:{ Exception -> 0x000b }
            L_0x000b:
                r0 = move-exception
                r1 = r0
            L_0x000d:
                com.google.android.exoplayer.drm.StreamingDrmSessionManager r2 = com.google.android.exoplayer.drm.StreamingDrmSessionManager.this
                com.google.android.exoplayer.drm.StreamingDrmSessionManager<T>$PostResponseHandler r2 = r2.postResponseHandler
                int r3 = r6.what
                android.os.Message r2 = r2.obtainMessage(r3, r1)
                r2.sendToTarget()
                return
            L_0x001b:
                com.google.android.exoplayer.drm.StreamingDrmSessionManager r2 = com.google.android.exoplayer.drm.StreamingDrmSessionManager.this     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.MediaDrmCallback r3 = r2.callback     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.StreamingDrmSessionManager r2 = com.google.android.exoplayer.drm.StreamingDrmSessionManager.this     // Catch:{ Exception -> 0x000b }
                java.util.UUID r4 = r2.uuid     // Catch:{ Exception -> 0x000b }
                java.lang.Object r2 = r6.obj     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.ExoMediaDrm$ProvisionRequest r2 = (com.google.android.exoplayer.drm.ExoMediaDrm.ProvisionRequest) r2     // Catch:{ Exception -> 0x000b }
                byte[] r1 = r3.executeProvisionRequest(r4, r2)     // Catch:{ Exception -> 0x000b }
                goto L_0x000d
            L_0x002c:
                com.google.android.exoplayer.drm.StreamingDrmSessionManager r2 = com.google.android.exoplayer.drm.StreamingDrmSessionManager.this     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.MediaDrmCallback r3 = r2.callback     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.StreamingDrmSessionManager r2 = com.google.android.exoplayer.drm.StreamingDrmSessionManager.this     // Catch:{ Exception -> 0x000b }
                java.util.UUID r4 = r2.uuid     // Catch:{ Exception -> 0x000b }
                java.lang.Object r2 = r6.obj     // Catch:{ Exception -> 0x000b }
                com.google.android.exoplayer.drm.ExoMediaDrm$KeyRequest r2 = (com.google.android.exoplayer.drm.ExoMediaDrm.KeyRequest) r2     // Catch:{ Exception -> 0x000b }
                byte[] r1 = r3.executeKeyRequest(r4, r2)     // Catch:{ Exception -> 0x000b }
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.drm.StreamingDrmSessionManager.PostRequestHandler.handleMessage(android.os.Message):void");
        }
    }
}
