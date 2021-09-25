package com.google.android.exoplayer.upstream;

import android.content.Context;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Predicate;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;

public final class DefaultUriDataSource implements UriDataSource {
    private static final String SCHEME_ASSET = "asset";
    private static final String SCHEME_CONTENT = "content";
    private final UriDataSource assetDataSource;
    private final UriDataSource contentDataSource;
    private UriDataSource dataSource;
    private final UriDataSource fileDataSource;
    private final UriDataSource httpDataSource;

    public DefaultUriDataSource(Context context, String userAgent) {
        this(context, (TransferListener) null, userAgent, false);
    }

    public DefaultUriDataSource(Context context, TransferListener listener, String userAgent) {
        this(context, listener, userAgent, false);
    }

    public DefaultUriDataSource(Context context, TransferListener listener, String userAgent, boolean allowCrossProtocolRedirects) {
        this(context, listener, (UriDataSource) new DefaultHttpDataSource(userAgent, (Predicate<String>) null, listener, 8000, 8000, allowCrossProtocolRedirects));
    }

    public DefaultUriDataSource(Context context, TransferListener listener, UriDataSource httpDataSource2) {
        this.httpDataSource = (UriDataSource) Assertions.checkNotNull(httpDataSource2);
        this.fileDataSource = new FileDataSource(listener);
        this.assetDataSource = new AssetDataSource(context, listener);
        this.contentDataSource = new ContentDataSource(context, listener);
    }

    public long open(DataSpec dataSpec) throws IOException {
        Assertions.checkState(this.dataSource == null);
        String scheme = dataSpec.uri.getScheme();
        if (Util.isLocalFileUri(dataSpec.uri)) {
            if (dataSpec.uri.getPath().startsWith("/android_asset/")) {
                this.dataSource = this.assetDataSource;
            } else {
                this.dataSource = this.fileDataSource;
            }
        } else if (SCHEME_ASSET.equals(scheme)) {
            this.dataSource = this.assetDataSource;
        } else if (SCHEME_CONTENT.equals(scheme)) {
            this.dataSource = this.contentDataSource;
        } else {
            this.dataSource = this.httpDataSource;
        }
        return this.dataSource.open(dataSpec);
    }

    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        return this.dataSource.read(buffer, offset, readLength);
    }

    public String getUri() {
        if (this.dataSource == null) {
            return null;
        }
        return this.dataSource.getUri();
    }

    public void close() throws IOException {
        if (this.dataSource != null) {
            try {
                this.dataSource.close();
            } finally {
                this.dataSource = null;
            }
        }
    }
}
