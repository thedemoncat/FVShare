package p012tv.danmaku.ijk.media.exo.demo;

import android.annotation.TargetApi;
import android.text.TextUtils;
import com.google.android.exoplayer.drm.ExoMediaDrm;
import com.google.android.exoplayer.drm.MediaDrmCallback;
import com.google.android.exoplayer.util.Util;
import com.lzy.okgo.model.HttpHeaders;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
/* renamed from: tv.danmaku.ijk.media.exo.demo.SmoothStreamingTestMediaDrmCallback */
public class SmoothStreamingTestMediaDrmCallback implements MediaDrmCallback {
    private static final Map<String, String> KEY_REQUEST_PROPERTIES;
    private static final String PLAYREADY_TEST_DEFAULT_URI = "http://playready.directtaps.net/pr/svc/rightsmanager.asmx";
    private static final Map<String, String> PROVISIONING_REQUEST_PROPERTIES = Collections.singletonMap(HttpHeaders.HEAD_KEY_CONTENT_TYPE, "application/octet-stream");

    static {
        HashMap<String, String> keyRequestProperties = new HashMap<>();
        keyRequestProperties.put(HttpHeaders.HEAD_KEY_CONTENT_TYPE, "text/xml");
        keyRequestProperties.put("SOAPAction", "http://schemas.microsoft.com/DRM/2007/03/protocols/AcquireLicense");
        KEY_REQUEST_PROPERTIES = keyRequestProperties;
    }

    public byte[] executeProvisionRequest(UUID uuid, ExoMediaDrm.ProvisionRequest request) throws IOException {
        return Util.executePost(request.getDefaultUrl() + "&signedRequest=" + new String(request.getData()), (byte[]) null, PROVISIONING_REQUEST_PROPERTIES);
    }

    public byte[] executeKeyRequest(UUID uuid, ExoMediaDrm.KeyRequest request) throws Exception {
        String url = request.getDefaultUrl();
        if (TextUtils.isEmpty(url)) {
            url = PLAYREADY_TEST_DEFAULT_URI;
        }
        return Util.executePost(url, request.getData(), KEY_REQUEST_PROPERTIES);
    }
}
