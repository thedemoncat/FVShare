package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

public final class StreamAllocation {
    static final /* synthetic */ boolean $assertionsDisabled = (!StreamAllocation.class.desiredAssertionStatus());
    public final Address address;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private int refusedStreamCount;
    private boolean released;
    private Route route;
    private final RouteSelector routeSelector;

    public StreamAllocation(ConnectionPool connectionPool2, Address address2, Object callStackTrace2) {
        this.connectionPool = connectionPool2;
        this.address = address2;
        this.routeSelector = new RouteSelector(address2, routeDatabase());
        this.callStackTrace = callStackTrace2;
    }

    public HttpCodec newStream(OkHttpClient client, boolean doExtensiveHealthChecks) {
        try {
            HttpCodec resultCodec = findHealthyConnection(client.connectTimeoutMillis(), client.readTimeoutMillis(), client.writeTimeoutMillis(), client.retryOnConnectionFailure(), doExtensiveHealthChecks).newCodec(client, this);
            synchronized (this.connectionPool) {
                this.codec = resultCodec;
            }
            return resultCodec;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    private RealConnection findHealthyConnection(int connectTimeout, int readTimeout, int writeTimeout, boolean connectionRetryEnabled, boolean doExtensiveHealthChecks) throws IOException {
        RealConnection candidate;
        while (true) {
            candidate = findConnection(connectTimeout, readTimeout, writeTimeout, connectionRetryEnabled);
            synchronized (this.connectionPool) {
                if (candidate.successCount != 0) {
                    if (candidate.isHealthy(doExtensiveHealthChecks)) {
                        break;
                    }
                    noNewStreams();
                } else {
                    break;
                }
            }
        }
        return candidate;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x004c, code lost:
        if (r2 != null) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x004e, code lost:
        r2 = r9.routeSelector.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0054, code lost:
        r5 = r9.connectionPool;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0056, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0059, code lost:
        if (r9.canceled == false) goto L_0x0067;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0063, code lost:
        throw new java.io.IOException("Canceled");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        okhttp3.internal.Internal.instance.get(r9.connectionPool, r9.address, r9, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0072, code lost:
        if (r9.connection == null) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0074, code lost:
        r9.route = r2;
        r0 = r9.connection;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0078, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x007a, code lost:
        r9.route = r2;
        r9.refusedStreamCount = 0;
        r1 = new okhttp3.internal.connection.RealConnection(r9.connectionPool, r2);
        acquire(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0089, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x008a, code lost:
        r1.connect(r10, r11, r12, r13);
        routeDatabase().connected(r1.route());
        r3 = null;
        r5 = r9.connectionPool;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x009b, code lost:
        monitor-enter(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        okhttp3.internal.Internal.instance.put(r9.connectionPool, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00a7, code lost:
        if (r1.isMultiplexed() == false) goto L_0x00b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a9, code lost:
        r3 = okhttp3.internal.Internal.instance.deduplicate(r9.connectionPool, r9.address, r9);
        r1 = r9.connection;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b5, code lost:
        monitor-exit(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b6, code lost:
        okhttp3.internal.Util.closeQuietly(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private okhttp3.internal.connection.RealConnection findConnection(int r10, int r11, int r12, boolean r13) throws java.io.IOException {
        /*
            r9 = this;
            okhttp3.ConnectionPool r5 = r9.connectionPool
            monitor-enter(r5)
            boolean r4 = r9.released     // Catch:{ all -> 0x0010 }
            if (r4 == 0) goto L_0x0013
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0010 }
            java.lang.String r6 = "released"
            r4.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r4     // Catch:{ all -> 0x0010 }
        L_0x0010:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0010 }
            throw r4
        L_0x0013:
            okhttp3.internal.http.HttpCodec r4 = r9.codec     // Catch:{ all -> 0x0010 }
            if (r4 == 0) goto L_0x0020
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0010 }
            java.lang.String r6 = "codec != null"
            r4.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r4     // Catch:{ all -> 0x0010 }
        L_0x0020:
            boolean r4 = r9.canceled     // Catch:{ all -> 0x0010 }
            if (r4 == 0) goto L_0x002d
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0010 }
            java.lang.String r6 = "Canceled"
            r4.<init>(r6)     // Catch:{ all -> 0x0010 }
            throw r4     // Catch:{ all -> 0x0010 }
        L_0x002d:
            okhttp3.internal.connection.RealConnection r0 = r9.connection     // Catch:{ all -> 0x0010 }
            if (r0 == 0) goto L_0x0037
            boolean r4 = r0.noNewStreams     // Catch:{ all -> 0x0010 }
            if (r4 != 0) goto L_0x0037
            monitor-exit(r5)     // Catch:{ all -> 0x0010 }
        L_0x0036:
            return r0
        L_0x0037:
            okhttp3.internal.Internal r4 = okhttp3.internal.Internal.instance     // Catch:{ all -> 0x0010 }
            okhttp3.ConnectionPool r6 = r9.connectionPool     // Catch:{ all -> 0x0010 }
            okhttp3.Address r7 = r9.address     // Catch:{ all -> 0x0010 }
            r8 = 0
            r4.get(r6, r7, r9, r8)     // Catch:{ all -> 0x0010 }
            okhttp3.internal.connection.RealConnection r4 = r9.connection     // Catch:{ all -> 0x0010 }
            if (r4 == 0) goto L_0x0049
            okhttp3.internal.connection.RealConnection r0 = r9.connection     // Catch:{ all -> 0x0010 }
            monitor-exit(r5)     // Catch:{ all -> 0x0010 }
            goto L_0x0036
        L_0x0049:
            okhttp3.Route r2 = r9.route     // Catch:{ all -> 0x0010 }
            monitor-exit(r5)     // Catch:{ all -> 0x0010 }
            if (r2 != 0) goto L_0x0054
            okhttp3.internal.connection.RouteSelector r4 = r9.routeSelector
            okhttp3.Route r2 = r4.next()
        L_0x0054:
            okhttp3.ConnectionPool r5 = r9.connectionPool
            monitor-enter(r5)
            boolean r4 = r9.canceled     // Catch:{ all -> 0x0064 }
            if (r4 == 0) goto L_0x0067
            java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x0064 }
            java.lang.String r6 = "Canceled"
            r4.<init>(r6)     // Catch:{ all -> 0x0064 }
            throw r4     // Catch:{ all -> 0x0064 }
        L_0x0064:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0064 }
            throw r4
        L_0x0067:
            okhttp3.internal.Internal r4 = okhttp3.internal.Internal.instance     // Catch:{ all -> 0x0064 }
            okhttp3.ConnectionPool r6 = r9.connectionPool     // Catch:{ all -> 0x0064 }
            okhttp3.Address r7 = r9.address     // Catch:{ all -> 0x0064 }
            r4.get(r6, r7, r9, r2)     // Catch:{ all -> 0x0064 }
            okhttp3.internal.connection.RealConnection r4 = r9.connection     // Catch:{ all -> 0x0064 }
            if (r4 == 0) goto L_0x007a
            r9.route = r2     // Catch:{ all -> 0x0064 }
            okhttp3.internal.connection.RealConnection r0 = r9.connection     // Catch:{ all -> 0x0064 }
            monitor-exit(r5)     // Catch:{ all -> 0x0064 }
            goto L_0x0036
        L_0x007a:
            r9.route = r2     // Catch:{ all -> 0x0064 }
            r4 = 0
            r9.refusedStreamCount = r4     // Catch:{ all -> 0x0064 }
            okhttp3.internal.connection.RealConnection r1 = new okhttp3.internal.connection.RealConnection     // Catch:{ all -> 0x0064 }
            okhttp3.ConnectionPool r4 = r9.connectionPool     // Catch:{ all -> 0x0064 }
            r1.<init>(r4, r2)     // Catch:{ all -> 0x0064 }
            r9.acquire(r1)     // Catch:{ all -> 0x0064 }
            monitor-exit(r5)     // Catch:{ all -> 0x0064 }
            r1.connect(r10, r11, r12, r13)
            okhttp3.internal.connection.RouteDatabase r4 = r9.routeDatabase()
            okhttp3.Route r5 = r1.route()
            r4.connected(r5)
            r3 = 0
            okhttp3.ConnectionPool r5 = r9.connectionPool
            monitor-enter(r5)
            okhttp3.internal.Internal r4 = okhttp3.internal.Internal.instance     // Catch:{ all -> 0x00bc }
            okhttp3.ConnectionPool r6 = r9.connectionPool     // Catch:{ all -> 0x00bc }
            r4.put(r6, r1)     // Catch:{ all -> 0x00bc }
            boolean r4 = r1.isMultiplexed()     // Catch:{ all -> 0x00bc }
            if (r4 == 0) goto L_0x00b5
            okhttp3.internal.Internal r4 = okhttp3.internal.Internal.instance     // Catch:{ all -> 0x00bc }
            okhttp3.ConnectionPool r6 = r9.connectionPool     // Catch:{ all -> 0x00bc }
            okhttp3.Address r7 = r9.address     // Catch:{ all -> 0x00bc }
            java.net.Socket r3 = r4.deduplicate(r6, r7, r9)     // Catch:{ all -> 0x00bc }
            okhttp3.internal.connection.RealConnection r1 = r9.connection     // Catch:{ all -> 0x00bc }
        L_0x00b5:
            monitor-exit(r5)     // Catch:{ all -> 0x00bc }
            okhttp3.internal.Util.closeQuietly((java.net.Socket) r3)
            r0 = r1
            goto L_0x0036
        L_0x00bc:
            r4 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x00bc }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.StreamAllocation.findConnection(int, int, int, boolean):okhttp3.internal.connection.RealConnection");
    }

    public void streamFinished(boolean noNewStreams, HttpCodec codec2) {
        Socket socket;
        synchronized (this.connectionPool) {
            if (codec2 != null) {
                if (codec2 == this.codec) {
                    if (!noNewStreams) {
                        this.connection.successCount++;
                    }
                    socket = deallocate(noNewStreams, false, true);
                }
            }
            throw new IllegalStateException("expected " + this.codec + " but was " + codec2);
        }
        Util.closeQuietly(socket);
    }

    public HttpCodec codec() {
        HttpCodec httpCodec;
        synchronized (this.connectionPool) {
            httpCodec = this.codec;
        }
        return httpCodec;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void release() {
        Socket socket;
        synchronized (this.connectionPool) {
            socket = deallocate(false, true, false);
        }
        Util.closeQuietly(socket);
    }

    public void noNewStreams() {
        Socket socket;
        synchronized (this.connectionPool) {
            socket = deallocate(true, false, false);
        }
        Util.closeQuietly(socket);
    }

    private Socket deallocate(boolean noNewStreams, boolean released2, boolean streamFinished) {
        if ($assertionsDisabled || Thread.holdsLock(this.connectionPool)) {
            if (streamFinished) {
                this.codec = null;
            }
            if (released2) {
                this.released = true;
            }
            Socket socket = null;
            if (this.connection != null) {
                if (noNewStreams) {
                    this.connection.noNewStreams = true;
                }
                if (this.codec == null && (this.released || this.connection.noNewStreams)) {
                    release(this.connection);
                    if (this.connection.allocations.isEmpty()) {
                        this.connection.idleAtNanos = System.nanoTime();
                        if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                            socket = this.connection.socket();
                        }
                    }
                    this.connection = null;
                }
            }
            return socket;
        }
        throw new AssertionError();
    }

    public void cancel() {
        HttpCodec codecToCancel;
        RealConnection connectionToCancel;
        synchronized (this.connectionPool) {
            this.canceled = true;
            codecToCancel = this.codec;
            connectionToCancel = this.connection;
        }
        if (codecToCancel != null) {
            codecToCancel.cancel();
        } else if (connectionToCancel != null) {
            connectionToCancel.cancel();
        }
    }

    public void streamFailed(IOException e) {
        Socket socket;
        boolean noNewStreams = false;
        synchronized (this.connectionPool) {
            if (e instanceof StreamResetException) {
                StreamResetException streamResetException = (StreamResetException) e;
                if (streamResetException.errorCode == ErrorCode.REFUSED_STREAM) {
                    this.refusedStreamCount++;
                }
                if (streamResetException.errorCode != ErrorCode.REFUSED_STREAM || this.refusedStreamCount > 1) {
                    noNewStreams = true;
                    this.route = null;
                }
            } else if (this.connection != null && (!this.connection.isMultiplexed() || (e instanceof ConnectionShutdownException))) {
                noNewStreams = true;
                if (this.connection.successCount == 0) {
                    if (!(this.route == null || e == null)) {
                        this.routeSelector.connectFailed(this.route, e);
                    }
                    this.route = null;
                }
            }
            socket = deallocate(noNewStreams, false, true);
        }
        Util.closeQuietly(socket);
    }

    public void acquire(RealConnection connection2) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
            throw new AssertionError();
        } else if (this.connection != null) {
            throw new IllegalStateException();
        } else {
            this.connection = connection2;
            connection2.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
        }
    }

    private void release(RealConnection connection2) {
        int size = connection2.allocations.size();
        for (int i = 0; i < size; i++) {
            if (connection2.allocations.get(i).get() == this) {
                connection2.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    public Socket releaseAndAcquire(RealConnection newConnection) {
        if (!$assertionsDisabled && !Thread.holdsLock(this.connectionPool)) {
            throw new AssertionError();
        } else if (this.codec == null && this.connection.allocations.size() == 1) {
            Socket socket = deallocate(true, false, false);
            this.connection = newConnection;
            newConnection.allocations.add(this.connection.allocations.get(0));
            return socket;
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean hasMoreRoutes() {
        return this.route != null || this.routeSelector.hasNext();
    }

    public String toString() {
        RealConnection connection2 = connection();
        return connection2 != null ? connection2.toString() : this.address.toString();
    }

    public static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
        public final Object callStackTrace;

        StreamAllocationReference(StreamAllocation referent, Object callStackTrace2) {
            super(referent);
            this.callStackTrace = callStackTrace2;
        }
    }
}
