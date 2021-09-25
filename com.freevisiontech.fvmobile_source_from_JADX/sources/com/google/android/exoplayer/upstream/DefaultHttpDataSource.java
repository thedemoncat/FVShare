package com.google.android.exoplayer.upstream;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.exoplayer.upstream.HttpDataSource;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Predicate;
import com.google.android.exoplayer.util.Util;
import com.google.android.vending.expansion.downloader.Constants;
import com.lzy.okgo.model.HttpHeaders;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultHttpDataSource implements HttpDataSource {
    private static final Pattern CONTENT_RANGE_HEADER = Pattern.compile("^bytes (\\d+)-(\\d+)/(\\d+)$");
    public static final int DEFAULT_CONNECT_TIMEOUT_MILLIS = 8000;
    public static final int DEFAULT_READ_TIMEOUT_MILLIS = 8000;
    private static final int MAX_REDIRECTS = 20;
    private static final String TAG = "DefaultHttpDataSource";
    private static final AtomicReference<byte[]> skipBufferReference = new AtomicReference<>();
    private final boolean allowCrossProtocolRedirects;
    private long bytesRead;
    private long bytesSkipped;
    private long bytesToRead;
    private long bytesToSkip;
    private final int connectTimeoutMillis;
    private HttpURLConnection connection;
    private final Predicate<String> contentTypePredicate;
    private DataSpec dataSpec;
    private InputStream inputStream;
    private final TransferListener listener;
    private boolean opened;
    private final int readTimeoutMillis;
    private final HashMap<String, String> requestProperties;
    private final String userAgent;

    public DefaultHttpDataSource(String userAgent2, Predicate<String> contentTypePredicate2) {
        this(userAgent2, contentTypePredicate2, (TransferListener) null);
    }

    public DefaultHttpDataSource(String userAgent2, Predicate<String> contentTypePredicate2, TransferListener listener2) {
        this(userAgent2, contentTypePredicate2, listener2, 8000, 8000);
    }

    public DefaultHttpDataSource(String userAgent2, Predicate<String> contentTypePredicate2, TransferListener listener2, int connectTimeoutMillis2, int readTimeoutMillis2) {
        this(userAgent2, contentTypePredicate2, listener2, connectTimeoutMillis2, readTimeoutMillis2, false);
    }

    public DefaultHttpDataSource(String userAgent2, Predicate<String> contentTypePredicate2, TransferListener listener2, int connectTimeoutMillis2, int readTimeoutMillis2, boolean allowCrossProtocolRedirects2) {
        this.userAgent = Assertions.checkNotEmpty(userAgent2);
        this.contentTypePredicate = contentTypePredicate2;
        this.listener = listener2;
        this.requestProperties = new HashMap<>();
        this.connectTimeoutMillis = connectTimeoutMillis2;
        this.readTimeoutMillis = readTimeoutMillis2;
        this.allowCrossProtocolRedirects = allowCrossProtocolRedirects2;
    }

    public String getUri() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getURL().toString();
    }

    public Map<String, List<String>> getResponseHeaders() {
        if (this.connection == null) {
            return null;
        }
        return this.connection.getHeaderFields();
    }

    public void setRequestProperty(String name, String value) {
        Assertions.checkNotNull(name);
        Assertions.checkNotNull(value);
        synchronized (this.requestProperties) {
            this.requestProperties.put(name, value);
        }
    }

    public void clearRequestProperty(String name) {
        Assertions.checkNotNull(name);
        synchronized (this.requestProperties) {
            this.requestProperties.remove(name);
        }
    }

    public void clearAllRequestProperties() {
        synchronized (this.requestProperties) {
            this.requestProperties.clear();
        }
    }

    public long open(DataSpec dataSpec2) throws HttpDataSource.HttpDataSourceException {
        long j;
        long j2 = 0;
        this.dataSpec = dataSpec2;
        this.bytesRead = 0;
        this.bytesSkipped = 0;
        try {
            this.connection = makeConnection(dataSpec2);
            try {
                int responseCode = this.connection.getResponseCode();
                if (responseCode < 200 || responseCode > 299) {
                    Map<String, List<String>> headers = this.connection.getHeaderFields();
                    closeConnectionQuietly();
                    throw new HttpDataSource.InvalidResponseCodeException(responseCode, headers, dataSpec2);
                }
                String contentType = this.connection.getContentType();
                if (this.contentTypePredicate == null || this.contentTypePredicate.evaluate(contentType)) {
                    if (responseCode == 200 && dataSpec2.position != 0) {
                        j2 = dataSpec2.position;
                    }
                    this.bytesToSkip = j2;
                    if ((dataSpec2.flags & 1) == 0) {
                        long contentLength = getContentLength(this.connection);
                        if (dataSpec2.length != -1) {
                            j = dataSpec2.length;
                        } else {
                            j = contentLength != -1 ? contentLength - this.bytesToSkip : -1;
                        }
                        this.bytesToRead = j;
                    } else {
                        this.bytesToRead = dataSpec2.length;
                    }
                    try {
                        this.inputStream = this.connection.getInputStream();
                        this.opened = true;
                        if (this.listener != null) {
                            this.listener.onTransferStart();
                        }
                        return this.bytesToRead;
                    } catch (IOException e) {
                        closeConnectionQuietly();
                        throw new HttpDataSource.HttpDataSourceException(e, dataSpec2, 1);
                    }
                } else {
                    closeConnectionQuietly();
                    throw new HttpDataSource.InvalidContentTypeException(contentType, dataSpec2);
                }
            } catch (IOException e2) {
                closeConnectionQuietly();
                throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec2.uri.toString(), e2, dataSpec2, 1);
            }
        } catch (IOException e3) {
            throw new HttpDataSource.HttpDataSourceException("Unable to connect to " + dataSpec2.uri.toString(), e3, dataSpec2, 1);
        }
    }

    public int read(byte[] buffer, int offset, int readLength) throws HttpDataSource.HttpDataSourceException {
        try {
            skipInternal();
            return readInternal(buffer, offset, readLength);
        } catch (IOException e) {
            throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec, 2);
        }
    }

    public void close() throws HttpDataSource.HttpDataSourceException {
        try {
            if (this.inputStream != null) {
                Util.maybeTerminateInputStream(this.connection, bytesRemaining());
                this.inputStream.close();
            }
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd();
                }
            }
        } catch (IOException e) {
            throw new HttpDataSource.HttpDataSourceException(e, this.dataSpec, 3);
        } catch (Throwable th) {
            this.inputStream = null;
            closeConnectionQuietly();
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd();
                }
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public final HttpURLConnection getConnection() {
        return this.connection;
    }

    /* access modifiers changed from: protected */
    public final long bytesSkipped() {
        return this.bytesSkipped;
    }

    /* access modifiers changed from: protected */
    public final long bytesRead() {
        return this.bytesRead;
    }

    /* access modifiers changed from: protected */
    public final long bytesRemaining() {
        return this.bytesToRead == -1 ? this.bytesToRead : this.bytesToRead - this.bytesRead;
    }

    private HttpURLConnection makeConnection(DataSpec dataSpec2) throws IOException {
        URL url = new URL(dataSpec2.uri.toString());
        byte[] postBody = dataSpec2.postBody;
        long position = dataSpec2.position;
        long length = dataSpec2.length;
        boolean allowGzip = (dataSpec2.flags & 1) != 0;
        if (!this.allowCrossProtocolRedirects) {
            return makeConnection(url, postBody, position, length, allowGzip, true);
        }
        int redirectCount = 0;
        while (true) {
            int redirectCount2 = redirectCount;
            redirectCount = redirectCount2 + 1;
            if (redirectCount2 <= 20) {
                HttpURLConnection connection2 = makeConnection(url, postBody, position, length, allowGzip, false);
                int responseCode = connection2.getResponseCode();
                if (!(responseCode == 300 || responseCode == 301 || responseCode == 302 || responseCode == 303)) {
                    if (postBody != null) {
                        return connection2;
                    }
                    if (!(responseCode == 307 || responseCode == 308)) {
                        return connection2;
                    }
                }
                postBody = null;
                String location = connection2.getHeaderField(HttpHeaders.HEAD_KEY_LOCATION);
                connection2.disconnect();
                url = handleRedirect(url, location);
            } else {
                throw new NoRouteToHostException("Too many redirects: " + redirectCount);
            }
        }
    }

    private HttpURLConnection makeConnection(URL url, byte[] postBody, long position, long length, boolean allowGzip, boolean followRedirects) throws IOException {
        HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
        connection2.setConnectTimeout(this.connectTimeoutMillis);
        connection2.setReadTimeout(this.readTimeoutMillis);
        synchronized (this.requestProperties) {
            for (Map.Entry<String, String> property : this.requestProperties.entrySet()) {
                connection2.setRequestProperty(property.getKey(), property.getValue());
            }
        }
        if (!(position == 0 && length == -1)) {
            String rangeRequest = "bytes=" + position + Constants.FILENAME_SEQUENCE_SEPARATOR;
            if (length != -1) {
                rangeRequest = rangeRequest + ((position + length) - 1);
            }
            connection2.setRequestProperty(HttpHeaders.HEAD_KEY_RANGE, rangeRequest);
        }
        connection2.setRequestProperty(HttpHeaders.HEAD_KEY_USER_AGENT, this.userAgent);
        if (!allowGzip) {
            connection2.setRequestProperty(HttpHeaders.HEAD_KEY_ACCEPT_ENCODING, "identity");
        }
        connection2.setInstanceFollowRedirects(followRedirects);
        connection2.setDoOutput(postBody != null);
        if (postBody != null) {
            connection2.setFixedLengthStreamingMode(postBody.length);
            connection2.connect();
            OutputStream os = connection2.getOutputStream();
            os.write(postBody);
            os.close();
        } else {
            connection2.connect();
        }
        return connection2;
    }

    private static URL handleRedirect(URL originalUrl, String location) throws IOException {
        if (location == null) {
            throw new ProtocolException("Null location redirect");
        }
        URL url = new URL(originalUrl, location);
        String protocol = url.getProtocol();
        if ("https".equals(protocol) || "http".equals(protocol)) {
            return url;
        }
        throw new ProtocolException("Unsupported protocol redirect: " + protocol);
    }

    private static long getContentLength(HttpURLConnection connection2) {
        long contentLength = -1;
        String contentLengthHeader = connection2.getHeaderField(HttpHeaders.HEAD_KEY_CONTENT_LENGTH);
        if (!TextUtils.isEmpty(contentLengthHeader)) {
            try {
                contentLength = Long.parseLong(contentLengthHeader);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Unexpected Content-Length [" + contentLengthHeader + "]");
            }
        }
        String contentRangeHeader = connection2.getHeaderField(HttpHeaders.HEAD_KEY_CONTENT_RANGE);
        if (TextUtils.isEmpty(contentRangeHeader)) {
            return contentLength;
        }
        Matcher matcher = CONTENT_RANGE_HEADER.matcher(contentRangeHeader);
        if (!matcher.find()) {
            return contentLength;
        }
        try {
            long contentLengthFromRange = (Long.parseLong(matcher.group(2)) - Long.parseLong(matcher.group(1))) + 1;
            if (contentLength < 0) {
                return contentLengthFromRange;
            }
            if (contentLength == contentLengthFromRange) {
                return contentLength;
            }
            Log.w(TAG, "Inconsistent headers [" + contentLengthHeader + "] [" + contentRangeHeader + "]");
            return Math.max(contentLength, contentLengthFromRange);
        } catch (NumberFormatException e2) {
            Log.e(TAG, "Unexpected Content-Range [" + contentRangeHeader + "]");
            return contentLength;
        }
    }

    private void skipInternal() throws IOException {
        if (this.bytesSkipped != this.bytesToSkip) {
            byte[] skipBuffer = skipBufferReference.getAndSet((Object) null);
            if (skipBuffer == null) {
                skipBuffer = new byte[4096];
            }
            while (this.bytesSkipped != this.bytesToSkip) {
                int read = this.inputStream.read(skipBuffer, 0, (int) Math.min(this.bytesToSkip - this.bytesSkipped, (long) skipBuffer.length));
                if (Thread.interrupted()) {
                    throw new InterruptedIOException();
                } else if (read == -1) {
                    throw new EOFException();
                } else {
                    this.bytesSkipped += (long) read;
                    if (this.listener != null) {
                        this.listener.onBytesTransferred(read);
                    }
                }
            }
            skipBufferReference.set(skipBuffer);
        }
    }

    private int readInternal(byte[] buffer, int offset, int readLength) throws IOException {
        if (this.bytesToRead != -1) {
            readLength = (int) Math.min((long) readLength, this.bytesToRead - this.bytesRead);
        }
        if (readLength == 0) {
            return -1;
        }
        int read = this.inputStream.read(buffer, offset, readLength);
        if (read != -1) {
            this.bytesRead += (long) read;
            if (this.listener == null) {
                return read;
            }
            this.listener.onBytesTransferred(read);
            return read;
        } else if (this.bytesToRead == -1 || this.bytesToRead == this.bytesRead) {
            return -1;
        } else {
            throw new EOFException();
        }
    }

    private void closeConnectionQuietly() {
        if (this.connection != null) {
            try {
                this.connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Unexpected error while disconnecting", e);
            }
            this.connection = null;
        }
    }
}
