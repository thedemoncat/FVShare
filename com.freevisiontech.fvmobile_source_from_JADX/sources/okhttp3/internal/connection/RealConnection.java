package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.Address;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.p006ws.RealWebSocket;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.OkHostnameVerifier;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection extends Http2Connection.Listener implements Connection {
    private static final String NPE_THROW_WITH_NULL = "throw with null exception";
    public int allocationLimit = 1;
    public final List<Reference<StreamAllocation>> allocations = new ArrayList();
    private final ConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    public long idleAtNanos = Long.MAX_VALUE;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    public int successCount;

    public RealConnection(ConnectionPool connectionPool2, Route route2) {
        this.connectionPool = connectionPool2;
        this.route = route2;
    }

    public static RealConnection testConnection(ConnectionPool connectionPool2, Route route2, Socket socket2, long idleAtNanos2) {
        RealConnection result = new RealConnection(connectionPool2, route2);
        result.socket = socket2;
        result.idleAtNanos = idleAtNanos2;
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x009d A[SYNTHETIC, Splitter:B:26:0x009d] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0086 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(int r10, int r11, int r12, boolean r13) {
        /*
            r9 = this;
            r6 = 0
            okhttp3.Protocol r5 = r9.protocol
            if (r5 == 0) goto L_0x000e
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "already connected"
            r5.<init>(r6)
            throw r5
        L_0x000e:
            r4 = 0
            okhttp3.Route r5 = r9.route
            okhttp3.Address r5 = r5.address()
            java.util.List r1 = r5.connectionSpecs()
            okhttp3.internal.connection.ConnectionSpecSelector r0 = new okhttp3.internal.connection.ConnectionSpecSelector
            r0.<init>(r1)
            okhttp3.Route r5 = r9.route
            okhttp3.Address r5 = r5.address()
            javax.net.ssl.SSLSocketFactory r5 = r5.sslSocketFactory()
            if (r5 != 0) goto L_0x007e
            okhttp3.ConnectionSpec r5 = okhttp3.ConnectionSpec.CLEARTEXT
            boolean r5 = r1.contains(r5)
            if (r5 != 0) goto L_0x0040
            okhttp3.internal.connection.RouteException r5 = new okhttp3.internal.connection.RouteException
            java.net.UnknownServiceException r6 = new java.net.UnknownServiceException
            java.lang.String r7 = "CLEARTEXT communication not enabled for client"
            r6.<init>(r7)
            r5.<init>(r6)
            throw r5
        L_0x0040:
            okhttp3.Route r5 = r9.route
            okhttp3.Address r5 = r5.address()
            okhttp3.HttpUrl r5 = r5.url()
            java.lang.String r3 = r5.host()
            okhttp3.internal.platform.Platform r5 = okhttp3.internal.platform.Platform.get()
            boolean r5 = r5.isCleartextTrafficPermitted(r3)
            if (r5 != 0) goto L_0x007e
            okhttp3.internal.connection.RouteException r5 = new okhttp3.internal.connection.RouteException
            java.net.UnknownServiceException r6 = new java.net.UnknownServiceException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "CLEARTEXT communication to "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r3)
            java.lang.String r8 = " not permitted by network security policy"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            r5.<init>(r6)
            throw r5
        L_0x007e:
            okhttp3.Route r5 = r9.route     // Catch:{ IOException -> 0x00a1 }
            boolean r5 = r5.requiresTunnel()     // Catch:{ IOException -> 0x00a1 }
            if (r5 == 0) goto L_0x009d
            r9.connectTunnel(r10, r11, r12)     // Catch:{ IOException -> 0x00a1 }
        L_0x0089:
            r9.establishProtocol(r0)     // Catch:{ IOException -> 0x00a1 }
            okhttp3.internal.http2.Http2Connection r5 = r9.http2Connection
            if (r5 == 0) goto L_0x009c
            okhttp3.ConnectionPool r6 = r9.connectionPool
            monitor-enter(r6)
            okhttp3.internal.http2.Http2Connection r5 = r9.http2Connection     // Catch:{ all -> 0x00ce }
            int r5 = r5.maxConcurrentStreams()     // Catch:{ all -> 0x00ce }
            r9.allocationLimit = r5     // Catch:{ all -> 0x00ce }
            monitor-exit(r6)     // Catch:{ all -> 0x00ce }
        L_0x009c:
            return
        L_0x009d:
            r9.connectSocket(r10, r11)     // Catch:{ IOException -> 0x00a1 }
            goto L_0x0089
        L_0x00a1:
            r2 = move-exception
            java.net.Socket r5 = r9.socket
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r5)
            java.net.Socket r5 = r9.rawSocket
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r5)
            r9.socket = r6
            r9.rawSocket = r6
            r9.source = r6
            r9.sink = r6
            r9.handshake = r6
            r9.protocol = r6
            r9.http2Connection = r6
            if (r4 != 0) goto L_0x00ca
            okhttp3.internal.connection.RouteException r4 = new okhttp3.internal.connection.RouteException
            r4.<init>(r2)
        L_0x00c1:
            if (r13 == 0) goto L_0x00c9
            boolean r5 = r0.connectionFailed(r2)
            if (r5 != 0) goto L_0x007e
        L_0x00c9:
            throw r4
        L_0x00ca:
            r4.addConnectException(r2)
            goto L_0x00c1
        L_0x00ce:
            r5 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00ce }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.RealConnection.connect(int, int, int, boolean):void");
    }

    private void connectTunnel(int connectTimeout, int readTimeout, int writeTimeout) throws IOException {
        Request tunnelRequest = createTunnelRequest();
        HttpUrl url = tunnelRequest.url();
        int attemptedConnections = 0;
        while (true) {
            attemptedConnections++;
            if (attemptedConnections > 21) {
                throw new ProtocolException("Too many tunnel connections attempted: " + 21);
            }
            connectSocket(connectTimeout, readTimeout);
            tunnelRequest = createTunnel(readTimeout, writeTimeout, tunnelRequest, url);
            if (tunnelRequest != null) {
                Util.closeQuietly(this.rawSocket);
                this.rawSocket = null;
                this.sink = null;
                this.source = null;
            } else {
                return;
            }
        }
    }

    private void connectSocket(int connectTimeout, int readTimeout) throws IOException {
        Socket socket2;
        Proxy proxy = this.route.proxy();
        Address address = this.route.address();
        if (proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP) {
            socket2 = address.socketFactory().createSocket();
        } else {
            socket2 = new Socket(proxy);
        }
        this.rawSocket = socket2;
        this.rawSocket.setSoTimeout(readTimeout);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), connectTimeout);
            try {
                this.source = Okio.buffer(Okio.source(this.rawSocket));
                this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            } catch (NullPointerException npe) {
                if (NPE_THROW_WITH_NULL.equals(npe.getMessage())) {
                    throw new IOException(npe);
                }
            }
        } catch (ConnectException e) {
            ConnectException ce = new ConnectException("Failed to connect to " + this.route.socketAddress());
            ce.initCause(e);
            throw ce;
        }
    }

    private void establishProtocol(ConnectionSpecSelector connectionSpecSelector) throws IOException {
        if (this.route.address().sslSocketFactory() == null) {
            this.protocol = Protocol.HTTP_1_1;
            this.socket = this.rawSocket;
            return;
        }
        connectTls(connectionSpecSelector);
        if (this.protocol == Protocol.HTTP_2) {
            this.socket.setSoTimeout(0);
            this.http2Connection = new Http2Connection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).build();
            this.http2Connection.start();
        }
    }

    /* JADX WARNING: type inference failed for: r10v5, types: [java.net.Socket] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connectTls(okhttp3.internal.connection.ConnectionSpecSelector r15) throws java.io.IOException {
        /*
            r14 = this;
            okhttp3.Route r10 = r14.route
            okhttp3.Address r1 = r10.address()
            javax.net.ssl.SSLSocketFactory r7 = r1.sslSocketFactory()
            r8 = 0
            r6 = 0
            java.net.Socket r10 = r14.rawSocket     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r11 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r11 = r11.host()     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r12 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            int r12 = r12.port()     // Catch:{ AssertionError -> 0x00c3 }
            r13 = 1
            java.net.Socket r10 = r7.createSocket(r10, r11, r12, r13)     // Catch:{ AssertionError -> 0x00c3 }
            r0 = r10
            javax.net.ssl.SSLSocket r0 = (javax.net.ssl.SSLSocket) r0     // Catch:{ AssertionError -> 0x00c3 }
            r6 = r0
            okhttp3.ConnectionSpec r3 = r15.configureSecureSocket(r6)     // Catch:{ AssertionError -> 0x00c3 }
            boolean r10 = r3.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00c3 }
            if (r10 == 0) goto L_0x0044
            okhttp3.internal.platform.Platform r10 = okhttp3.internal.platform.Platform.get()     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r11 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r11 = r11.host()     // Catch:{ AssertionError -> 0x00c3 }
            java.util.List r12 = r1.protocols()     // Catch:{ AssertionError -> 0x00c3 }
            r10.configureTlsExtensions(r6, r11, r12)     // Catch:{ AssertionError -> 0x00c3 }
        L_0x0044:
            r6.startHandshake()     // Catch:{ AssertionError -> 0x00c3 }
            javax.net.ssl.SSLSession r10 = r6.getSession()     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.Handshake r9 = okhttp3.Handshake.get(r10)     // Catch:{ AssertionError -> 0x00c3 }
            javax.net.ssl.HostnameVerifier r10 = r1.hostnameVerifier()     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r11 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r11 = r11.host()     // Catch:{ AssertionError -> 0x00c3 }
            javax.net.ssl.SSLSession r12 = r6.getSession()     // Catch:{ AssertionError -> 0x00c3 }
            boolean r10 = r10.verify(r11, r12)     // Catch:{ AssertionError -> 0x00c3 }
            if (r10 != 0) goto L_0x00e0
            java.util.List r10 = r9.peerCertificates()     // Catch:{ AssertionError -> 0x00c3 }
            r11 = 0
            java.lang.Object r2 = r10.get(r11)     // Catch:{ AssertionError -> 0x00c3 }
            java.security.cert.X509Certificate r2 = (java.security.cert.X509Certificate) r2     // Catch:{ AssertionError -> 0x00c3 }
            javax.net.ssl.SSLPeerUnverifiedException r10 = new javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ AssertionError -> 0x00c3 }
            r11.<init>()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = "Hostname "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r12 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = r12.host()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = " not verified:\n    certificate: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = okhttp3.CertificatePinner.pin(r2)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = "\n    DN: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.security.Principal r12 = r2.getSubjectDN()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = r12.getName()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r12 = "\n    subjectAltNames: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.util.List r12 = okhttp3.internal.tls.OkHostnameVerifier.allSubjectAltNames(r2)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r11 = r11.toString()     // Catch:{ AssertionError -> 0x00c3 }
            r10.<init>(r11)     // Catch:{ AssertionError -> 0x00c3 }
            throw r10     // Catch:{ AssertionError -> 0x00c3 }
        L_0x00c3:
            r4 = move-exception
            boolean r10 = okhttp3.internal.Util.isAndroidGetsocknameError(r4)     // Catch:{ all -> 0x00d0 }
            if (r10 == 0) goto L_0x013a
            java.io.IOException r10 = new java.io.IOException     // Catch:{ all -> 0x00d0 }
            r10.<init>(r4)     // Catch:{ all -> 0x00d0 }
            throw r10     // Catch:{ all -> 0x00d0 }
        L_0x00d0:
            r10 = move-exception
            if (r6 == 0) goto L_0x00da
            okhttp3.internal.platform.Platform r11 = okhttp3.internal.platform.Platform.get()
            r11.afterHandshake(r6)
        L_0x00da:
            if (r8 != 0) goto L_0x00df
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r6)
        L_0x00df:
            throw r10
        L_0x00e0:
            okhttp3.CertificatePinner r10 = r1.certificatePinner()     // Catch:{ AssertionError -> 0x00c3 }
            okhttp3.HttpUrl r11 = r1.url()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r11 = r11.host()     // Catch:{ AssertionError -> 0x00c3 }
            java.util.List r12 = r9.peerCertificates()     // Catch:{ AssertionError -> 0x00c3 }
            r10.check((java.lang.String) r11, (java.util.List<java.security.cert.Certificate>) r12)     // Catch:{ AssertionError -> 0x00c3 }
            boolean r10 = r3.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00c3 }
            if (r10 == 0) goto L_0x0135
            okhttp3.internal.platform.Platform r10 = okhttp3.internal.platform.Platform.get()     // Catch:{ AssertionError -> 0x00c3 }
            java.lang.String r5 = r10.getSelectedProtocol(r6)     // Catch:{ AssertionError -> 0x00c3 }
        L_0x0101:
            r14.socket = r6     // Catch:{ AssertionError -> 0x00c3 }
            java.net.Socket r10 = r14.socket     // Catch:{ AssertionError -> 0x00c3 }
            okio.Source r10 = okio.Okio.source((java.net.Socket) r10)     // Catch:{ AssertionError -> 0x00c3 }
            okio.BufferedSource r10 = okio.Okio.buffer((okio.Source) r10)     // Catch:{ AssertionError -> 0x00c3 }
            r14.source = r10     // Catch:{ AssertionError -> 0x00c3 }
            java.net.Socket r10 = r14.socket     // Catch:{ AssertionError -> 0x00c3 }
            okio.Sink r10 = okio.Okio.sink((java.net.Socket) r10)     // Catch:{ AssertionError -> 0x00c3 }
            okio.BufferedSink r10 = okio.Okio.buffer((okio.Sink) r10)     // Catch:{ AssertionError -> 0x00c3 }
            r14.sink = r10     // Catch:{ AssertionError -> 0x00c3 }
            r14.handshake = r9     // Catch:{ AssertionError -> 0x00c3 }
            if (r5 == 0) goto L_0x0137
            okhttp3.Protocol r10 = okhttp3.Protocol.get(r5)     // Catch:{ AssertionError -> 0x00c3 }
        L_0x0123:
            r14.protocol = r10     // Catch:{ AssertionError -> 0x00c3 }
            r8 = 1
            if (r6 == 0) goto L_0x012f
            okhttp3.internal.platform.Platform r10 = okhttp3.internal.platform.Platform.get()
            r10.afterHandshake(r6)
        L_0x012f:
            if (r8 != 0) goto L_0x0134
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r6)
        L_0x0134:
            return
        L_0x0135:
            r5 = 0
            goto L_0x0101
        L_0x0137:
            okhttp3.Protocol r10 = okhttp3.Protocol.HTTP_1_1     // Catch:{ AssertionError -> 0x00c3 }
            goto L_0x0123
        L_0x013a:
            throw r4     // Catch:{ all -> 0x00d0 }
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.RealConnection.connectTls(okhttp3.internal.connection.ConnectionSpecSelector):void");
    }

    private Request createTunnel(int readTimeout, int writeTimeout, Request tunnelRequest, HttpUrl url) throws IOException {
        Response response;
        String requestLine = "CONNECT " + Util.hostHeader(url, true) + " HTTP/1.1";
        do {
            Http1Codec tunnelConnection = new Http1Codec((OkHttpClient) null, (StreamAllocation) null, this.source, this.sink);
            this.source.timeout().timeout((long) readTimeout, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout((long) writeTimeout, TimeUnit.MILLISECONDS);
            tunnelConnection.writeRequest(tunnelRequest.headers(), requestLine);
            tunnelConnection.finishRequest();
            response = tunnelConnection.readResponseHeaders(false).request(tunnelRequest).build();
            long contentLength = HttpHeaders.contentLength(response);
            if (contentLength == -1) {
                contentLength = 0;
            }
            Source body = tunnelConnection.newFixedLengthSource(contentLength);
            Util.skipAll(body, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            body.close();
            switch (response.code()) {
                case 200:
                    if (this.source.buffer().exhausted() && this.sink.buffer().exhausted()) {
                        return null;
                    }
                    throw new IOException("TLS tunnel buffered too many bytes!");
                case 407:
                    tunnelRequest = this.route.address().proxyAuthenticator().authenticate(this.route, response);
                    if (tunnelRequest != null) {
                        break;
                    } else {
                        throw new IOException("Failed to authenticate with proxy");
                    }
                default:
                    throw new IOException("Unexpected response code for CONNECT: " + response.code());
            }
        } while (!com.lzy.okgo.model.HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE.equalsIgnoreCase(response.header(com.lzy.okgo.model.HttpHeaders.HEAD_KEY_CONNECTION)));
        return tunnelRequest;
    }

    private Request createTunnelRequest() {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header(com.lzy.okgo.model.HttpHeaders.HEAD_KEY_USER_AGENT, Version.userAgent()).build();
    }

    public boolean isEligible(Address address, @Nullable Route route2) {
        if (this.allocations.size() >= this.allocationLimit || this.noNewStreams || !Internal.instance.equalsNonHost(this.route.address(), address)) {
            return false;
        }
        if (address.url().host().equals(route().address().url().host())) {
            return true;
        }
        if (this.http2Connection == null || route2 == null || route2.proxy().type() != Proxy.Type.DIRECT || this.route.proxy().type() != Proxy.Type.DIRECT || !this.route.socketAddress().equals(route2.socketAddress()) || route2.address().hostnameVerifier() != OkHostnameVerifier.INSTANCE || !supportsUrl(address.url())) {
            return false;
        }
        try {
            address.certificatePinner().check(address.url().host(), handshake().peerCertificates());
            return true;
        } catch (SSLPeerUnverifiedException e) {
            return false;
        }
    }

    public boolean supportsUrl(HttpUrl url) {
        if (url.port() != this.route.address().url().port()) {
            return false;
        }
        if (url.host().equals(this.route.address().url().host())) {
            return true;
        }
        return this.handshake != null && OkHostnameVerifier.INSTANCE.verify(url.host(), (X509Certificate) this.handshake.peerCertificates().get(0));
    }

    public HttpCodec newCodec(OkHttpClient client, StreamAllocation streamAllocation) throws SocketException {
        if (this.http2Connection != null) {
            return new Http2Codec(client, streamAllocation, this.http2Connection);
        }
        this.socket.setSoTimeout(client.readTimeoutMillis());
        this.source.timeout().timeout((long) client.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.sink.timeout().timeout((long) client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
        return new Http1Codec(client, streamAllocation, this.source, this.sink);
    }

    public RealWebSocket.Streams newWebSocketStreams(StreamAllocation streamAllocation) {
        final StreamAllocation streamAllocation2 = streamAllocation;
        return new RealWebSocket.Streams(true, this.source, this.sink) {
            public void close() throws IOException {
                streamAllocation2.streamFinished(true, streamAllocation2.codec());
            }
        };
    }

    public Route route() {
        return this.route;
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public Socket socket() {
        return this.socket;
    }

    public boolean isHealthy(boolean doExtensiveChecks) {
        int readTimeout;
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.http2Connection != null) {
            if (this.http2Connection.isShutdown()) {
                return false;
            }
            return true;
        } else if (!doExtensiveChecks) {
            return true;
        } else {
            try {
                readTimeout = this.socket.getSoTimeout();
                this.socket.setSoTimeout(1);
                if (this.source.exhausted()) {
                    this.socket.setSoTimeout(readTimeout);
                    return false;
                }
                this.socket.setSoTimeout(readTimeout);
                return true;
            } catch (SocketTimeoutException e) {
                return true;
            } catch (IOException e2) {
                return false;
            } catch (Throwable th) {
                this.socket.setSoTimeout(readTimeout);
                throw th;
            }
        }
    }

    public void onStream(Http2Stream stream) throws IOException {
        stream.close(ErrorCode.REFUSED_STREAM);
    }

    public void onSettings(Http2Connection connection) {
        synchronized (this.connectionPool) {
            this.allocationLimit = connection.maxConcurrentStreams();
        }
    }

    public Handshake handshake() {
        return this.handshake;
    }

    public boolean isMultiplexed() {
        return this.http2Connection != null;
    }

    public Protocol protocol() {
        return this.protocol;
    }

    public String toString() {
        Object obj;
        StringBuilder append = new StringBuilder().append("Connection{").append(this.route.address().url().host()).append(":").append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
        if (this.handshake != null) {
            obj = this.handshake.cipherSuite();
        } else {
            obj = "none";
        }
        return append.append(obj).append(" protocol=").append(this.protocol).append('}').toString();
    }
}
