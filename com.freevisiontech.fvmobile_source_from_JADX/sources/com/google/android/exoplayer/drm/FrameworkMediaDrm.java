package com.google.android.exoplayer.drm;

import android.annotation.TargetApi;
import android.media.DeniedByServerException;
import android.media.MediaCrypto;
import android.media.MediaCryptoException;
import android.media.MediaDrm;
import android.media.NotProvisionedException;
import android.media.ResourceBusyException;
import android.media.UnsupportedSchemeException;
import com.google.android.exoplayer.drm.ExoMediaDrm;
import com.google.android.exoplayer.util.Assertions;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
public final class FrameworkMediaDrm implements ExoMediaDrm<FrameworkMediaCrypto> {
    private final MediaDrm mediaDrm;

    public FrameworkMediaDrm(UUID uuid) throws UnsupportedSchemeException {
        this.mediaDrm = new MediaDrm((UUID) Assertions.checkNotNull(uuid));
    }

    public void setOnEventListener(final ExoMediaDrm.OnEventListener<? super FrameworkMediaCrypto> listener) {
        this.mediaDrm.setOnEventListener(listener == null ? null : new MediaDrm.OnEventListener() {
            public void onEvent(MediaDrm md, byte[] sessionId, int event, int extra, byte[] data) {
                listener.onEvent(FrameworkMediaDrm.this, sessionId, event, extra, data);
            }
        });
    }

    public byte[] openSession() throws NotProvisionedException, ResourceBusyException {
        return this.mediaDrm.openSession();
    }

    public void closeSession(byte[] sessionId) {
        this.mediaDrm.closeSession(sessionId);
    }

    public ExoMediaDrm.KeyRequest getKeyRequest(byte[] scope, byte[] init, String mimeType, int keyType, HashMap<String, String> optionalParameters) throws NotProvisionedException {
        final MediaDrm.KeyRequest request = this.mediaDrm.getKeyRequest(scope, init, mimeType, keyType, optionalParameters);
        return new ExoMediaDrm.KeyRequest() {
            public byte[] getData() {
                return request.getData();
            }

            public String getDefaultUrl() {
                return request.getDefaultUrl();
            }
        };
    }

    public byte[] provideKeyResponse(byte[] scope, byte[] response) throws NotProvisionedException, DeniedByServerException {
        return this.mediaDrm.provideKeyResponse(scope, response);
    }

    public ExoMediaDrm.ProvisionRequest getProvisionRequest() {
        final MediaDrm.ProvisionRequest provisionRequest = this.mediaDrm.getProvisionRequest();
        return new ExoMediaDrm.ProvisionRequest() {
            public byte[] getData() {
                return provisionRequest.getData();
            }

            public String getDefaultUrl() {
                return provisionRequest.getDefaultUrl();
            }
        };
    }

    public void provideProvisionResponse(byte[] response) throws DeniedByServerException {
        this.mediaDrm.provideProvisionResponse(response);
    }

    public Map<String, String> queryKeyStatus(byte[] sessionId) {
        return this.mediaDrm.queryKeyStatus(sessionId);
    }

    public void release() {
        this.mediaDrm.release();
    }

    public void restoreKeys(byte[] sessionId, byte[] keySetId) {
        this.mediaDrm.restoreKeys(sessionId, keySetId);
    }

    public String getPropertyString(String propertyName) {
        return this.mediaDrm.getPropertyString(propertyName);
    }

    public byte[] getPropertyByteArray(String propertyName) {
        return this.mediaDrm.getPropertyByteArray(propertyName);
    }

    public void setPropertyString(String propertyName, String value) {
        this.mediaDrm.setPropertyString(propertyName, value);
    }

    public void setPropertyByteArray(String propertyName, byte[] value) {
        this.mediaDrm.setPropertyByteArray(propertyName, value);
    }

    public FrameworkMediaCrypto createMediaCrypto(UUID uuid, byte[] initData) throws MediaCryptoException {
        return new FrameworkMediaCrypto(new MediaCrypto(uuid, initData));
    }
}
