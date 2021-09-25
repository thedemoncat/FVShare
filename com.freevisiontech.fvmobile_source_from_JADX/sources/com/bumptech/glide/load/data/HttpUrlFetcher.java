package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.google.android.exoplayer.ExoPlayer;
import com.lzy.okgo.model.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class HttpUrlFetcher implements DataFetcher<InputStream> {
    private static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
    private static final int MAXIMUM_REDIRECTS = 5;
    private static final String TAG = "HttpUrlFetcher";
    private final HttpUrlConnectionFactory connectionFactory;
    private final GlideUrl glideUrl;
    private volatile boolean isCancelled;
    private InputStream stream;
    private HttpURLConnection urlConnection;

    interface HttpUrlConnectionFactory {
        HttpURLConnection build(URL url) throws IOException;
    }

    public HttpUrlFetcher(GlideUrl glideUrl2) {
        this(glideUrl2, DEFAULT_CONNECTION_FACTORY);
    }

    HttpUrlFetcher(GlideUrl glideUrl2, HttpUrlConnectionFactory connectionFactory2) {
        this.glideUrl = glideUrl2;
        this.connectionFactory = connectionFactory2;
    }

    public InputStream loadData(Priority priority) throws Exception {
        return loadDataWithRedirects(this.glideUrl.toURL(), 0, (URL) null, this.glideUrl.getHeaders());
    }

    private InputStream loadDataWithRedirects(URL url, int redirects, URL lastUrl, Map<String, String> headers) throws IOException {
        if (redirects >= 5) {
            throw new IOException("Too many (> 5) redirects!");
        }
        if (lastUrl != null) {
            try {
                if (url.toURI().equals(lastUrl.toURI())) {
                    throw new IOException("In re-direct loop");
                }
            } catch (URISyntaxException e) {
            }
        }
        this.urlConnection = this.connectionFactory.build(url);
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            this.urlConnection.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }
        this.urlConnection.setConnectTimeout(ExoPlayer.Factory.DEFAULT_MIN_BUFFER_MS);
        this.urlConnection.setReadTimeout(ExoPlayer.Factory.DEFAULT_MIN_BUFFER_MS);
        this.urlConnection.setUseCaches(false);
        this.urlConnection.setDoInput(true);
        this.urlConnection.connect();
        if (this.isCancelled) {
            return null;
        }
        int statusCode = this.urlConnection.getResponseCode();
        if (statusCode / 100 == 2) {
            return getStreamForSuccessfulRequest(this.urlConnection);
        }
        if (statusCode / 100 == 3) {
            String redirectUrlString = this.urlConnection.getHeaderField(HttpHeaders.HEAD_KEY_LOCATION);
            if (!TextUtils.isEmpty(redirectUrlString)) {
                return loadDataWithRedirects(new URL(url, redirectUrlString), redirects + 1, url, headers);
            }
            throw new IOException("Received empty or null redirect url");
        } else if (statusCode == -1) {
            throw new IOException("Unable to retrieve response code from HttpUrlConnection.");
        } else {
            throw new IOException("Request failed " + statusCode + ": " + this.urlConnection.getResponseMessage());
        }
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection urlConnection2) throws IOException {
        if (TextUtils.isEmpty(urlConnection2.getContentEncoding())) {
            this.stream = ContentLengthInputStream.obtain(urlConnection2.getInputStream(), (long) urlConnection2.getContentLength());
        } else {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Got non empty content encoding: " + urlConnection2.getContentEncoding());
            }
            this.stream = urlConnection2.getInputStream();
        }
        return this.stream;
    }

    public void cleanup() {
        if (this.stream != null) {
            try {
                this.stream.close();
            } catch (IOException e) {
            }
        }
        if (this.urlConnection != null) {
            this.urlConnection.disconnect();
        }
    }

    public String getId() {
        return this.glideUrl.getCacheKey();
    }

    public void cancel() {
        this.isCancelled = true;
    }

    private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory {
        private DefaultHttpUrlConnectionFactory() {
        }

        public HttpURLConnection build(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }
}
