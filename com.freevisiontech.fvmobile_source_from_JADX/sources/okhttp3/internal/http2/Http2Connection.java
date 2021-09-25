package okhttp3.internal.http2;

import android.support.p001v4.internal.view.SupportMenu;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Protocol;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.Http2Reader;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class Http2Connection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    static final ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Http2Connection", true));
    long bytesLeftInWriteWindow;
    final boolean client;
    final Set<Integer> currentPushRequests = new LinkedHashSet();
    final String hostname;
    int lastGoodStreamId;
    final Listener listener;
    private int nextPingId;
    int nextStreamId;
    Settings okHttpSettings = new Settings();
    final Settings peerSettings = new Settings();
    private Map<Integer, Ping> pings;
    private final ExecutorService pushExecutor;
    final PushObserver pushObserver;
    final ReaderRunnable readerRunnable;
    boolean receivedInitialPeerSettings = false;
    boolean shutdown;
    final Socket socket;
    final Map<Integer, Http2Stream> streams = new LinkedHashMap();
    long unacknowledgedBytesRead = 0;
    final Http2Writer writer;

    static {
        boolean z;
        if (!Http2Connection.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    Http2Connection(Builder builder) {
        int i = 2;
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client) {
            this.nextStreamId += 2;
        }
        this.nextPingId = builder.client ? 1 : i;
        if (builder.client) {
            this.okHttpSettings.set(7, 16777216);
        }
        this.hostname = builder.hostname;
        this.pushExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(Util.format("OkHttp %s Push Observer", this.hostname), true));
        this.peerSettings.set(7, SupportMenu.USER_MASK);
        this.peerSettings.set(5, 16384);
        this.bytesLeftInWriteWindow = (long) this.peerSettings.getInitialWindowSize();
        this.socket = builder.socket;
        this.writer = new Http2Writer(builder.sink, this.client);
        this.readerRunnable = new ReaderRunnable(new Http2Reader(builder.source, this.client));
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    /* access modifiers changed from: package-private */
    public synchronized Http2Stream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    /* access modifiers changed from: package-private */
    public synchronized Http2Stream removeStream(int streamId) {
        Http2Stream stream;
        stream = this.streams.remove(Integer.valueOf(streamId));
        notifyAll();
        return stream;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(Integer.MAX_VALUE);
    }

    public Http2Stream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        if (!this.client) {
            return newStream(associatedStreamId, requestHeaders, out);
        }
        throw new IllegalStateException("Client cannot push requests.");
    }

    public Http2Stream newStream(List<Header> requestHeaders, boolean out) throws IOException {
        return newStream(0, requestHeaders, out);
    }

    private Http2Stream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        int streamId;
        Http2Stream stream;
        boolean flushHeaders;
        boolean outFinished = !out;
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                streamId = this.nextStreamId;
                this.nextStreamId += 2;
                stream = new Http2Stream(streamId, this, outFinished, false, requestHeaders);
                flushHeaders = !out || this.bytesLeftInWriteWindow == 0 || stream.bytesLeftInWriteWindow == 0;
                if (stream.isOpen()) {
                    this.streams.put(Integer.valueOf(streamId), stream);
                }
            }
            if (associatedStreamId == 0) {
                this.writer.synStream(outFinished, streamId, associatedStreamId, requestHeaders);
            } else if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            } else {
                this.writer.pushPromise(associatedStreamId, streamId, requestHeaders);
            }
        }
        if (flushHeaders) {
            this.writer.flush();
        }
        return stream;
    }

    /* access modifiers changed from: package-private */
    public void writeSynReply(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
        this.writer.synReply(outFinished, streamId, alternating);
    }

    public void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        int toWrite;
        boolean z;
        if (byteCount == 0) {
            this.writer.data(outFinished, streamId, buffer, 0);
            return;
        }
        while (byteCount > 0) {
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(streamId))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                toWrite = Math.min((int) Math.min(byteCount, this.bytesLeftInWriteWindow), this.writer.maxDataLength());
                this.bytesLeftInWriteWindow -= (long) toWrite;
            }
            byteCount -= (long) toWrite;
            Http2Writer http2Writer = this.writer;
            if (!outFinished || byteCount != 0) {
                z = false;
            } else {
                z = true;
            }
            http2Writer.data(z, streamId, buffer, toWrite);
        }
    }

    /* access modifiers changed from: package-private */
    public void addBytesToWriteWindow(long delta) {
        this.bytesLeftInWriteWindow += delta;
        if (delta > 0) {
            notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void writeSynResetLater(int streamId, ErrorCode errorCode) {
        final int i = streamId;
        final ErrorCode errorCode2 = errorCode;
        executor.execute(new NamedRunnable("OkHttp %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    Http2Connection.this.writeSynReset(i, errorCode2);
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void writeSynReset(int streamId, ErrorCode statusCode) throws IOException {
        this.writer.rstStream(streamId, statusCode);
    }

    /* access modifiers changed from: package-private */
    public void writeWindowUpdateLater(int streamId, long unacknowledgedBytesRead2) {
        final int i = streamId;
        final long j = unacknowledgedBytesRead2;
        executor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    Http2Connection.this.writer.windowUpdate(i, j);
                } catch (IOException e) {
                }
            }
        });
    }

    public Ping ping() throws IOException {
        int pingId;
        Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new ConnectionShutdownException();
            }
            pingId = this.nextPingId;
            this.nextPingId += 2;
            if (this.pings == null) {
                this.pings = new LinkedHashMap();
            }
            this.pings.put(Integer.valueOf(pingId), ping);
        }
        writePing(false, pingId, 1330343787, ping);
        return ping;
    }

    /* access modifiers changed from: package-private */
    public void writePingLater(boolean reply, int payload1, int payload2, Ping ping) {
        final boolean z = reply;
        final int i = payload1;
        final int i2 = payload2;
        final Ping ping2 = ping;
        executor.execute(new NamedRunnable("OkHttp %s ping %08x%08x", new Object[]{this.hostname, Integer.valueOf(payload1), Integer.valueOf(payload2)}) {
            public void execute() {
                try {
                    Http2Connection.this.writePing(z, i, i2, ping2);
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void writePing(boolean reply, int payload1, int payload2, Ping ping) throws IOException {
        synchronized (this.writer) {
            if (ping != null) {
                ping.send();
            }
            this.writer.ping(reply, payload1, payload2);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized Ping removePing(int id) {
        return this.pings != null ? this.pings.remove(Integer.valueOf(id)) : null;
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void shutdown(ErrorCode statusCode) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (!this.shutdown) {
                    this.shutdown = true;
                    int lastGoodStreamId2 = this.lastGoodStreamId;
                    this.writer.goAway(lastGoodStreamId2, statusCode, Util.EMPTY_BYTE_ARRAY);
                }
            }
        }
    }

    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    /* JADX WARNING: type inference failed for: r7v15, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r7v19, types: [java.lang.Object[]] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close(okhttp3.internal.http2.ErrorCode r11, okhttp3.internal.http2.ErrorCode r12) throws java.io.IOException {
        /*
            r10 = this;
            r8 = 0
            boolean r7 = $assertionsDisabled
            if (r7 != 0) goto L_0x0011
            boolean r7 = java.lang.Thread.holdsLock(r10)
            if (r7 == 0) goto L_0x0011
            java.lang.AssertionError r7 = new java.lang.AssertionError
            r7.<init>()
            throw r7
        L_0x0011:
            r6 = 0
            r10.shutdown(r11)     // Catch:{ IOException -> 0x0067 }
        L_0x0015:
            r5 = 0
            r3 = 0
            monitor-enter(r10)
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r7 = r10.streams     // Catch:{ all -> 0x006a }
            boolean r7 = r7.isEmpty()     // Catch:{ all -> 0x006a }
            if (r7 != 0) goto L_0x003b
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r7 = r10.streams     // Catch:{ all -> 0x006a }
            java.util.Collection r7 = r7.values()     // Catch:{ all -> 0x006a }
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r9 = r10.streams     // Catch:{ all -> 0x006a }
            int r9 = r9.size()     // Catch:{ all -> 0x006a }
            okhttp3.internal.http2.Http2Stream[] r9 = new okhttp3.internal.http2.Http2Stream[r9]     // Catch:{ all -> 0x006a }
            java.lang.Object[] r7 = r7.toArray(r9)     // Catch:{ all -> 0x006a }
            r0 = r7
            okhttp3.internal.http2.Http2Stream[] r0 = (okhttp3.internal.http2.Http2Stream[]) r0     // Catch:{ all -> 0x006a }
            r5 = r0
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r7 = r10.streams     // Catch:{ all -> 0x006a }
            r7.clear()     // Catch:{ all -> 0x006a }
        L_0x003b:
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Ping> r7 = r10.pings     // Catch:{ all -> 0x006a }
            if (r7 == 0) goto L_0x0058
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Ping> r7 = r10.pings     // Catch:{ all -> 0x006a }
            java.util.Collection r7 = r7.values()     // Catch:{ all -> 0x006a }
            java.util.Map<java.lang.Integer, okhttp3.internal.http2.Ping> r9 = r10.pings     // Catch:{ all -> 0x006a }
            int r9 = r9.size()     // Catch:{ all -> 0x006a }
            okhttp3.internal.http2.Ping[] r9 = new okhttp3.internal.http2.Ping[r9]     // Catch:{ all -> 0x006a }
            java.lang.Object[] r7 = r7.toArray(r9)     // Catch:{ all -> 0x006a }
            r0 = r7
            okhttp3.internal.http2.Ping[] r0 = (okhttp3.internal.http2.Ping[]) r0     // Catch:{ all -> 0x006a }
            r3 = r0
            r7 = 0
            r10.pings = r7     // Catch:{ all -> 0x006a }
        L_0x0058:
            monitor-exit(r10)     // Catch:{ all -> 0x006a }
            if (r5 == 0) goto L_0x0072
            int r9 = r5.length
            r7 = r8
        L_0x005d:
            if (r7 >= r9) goto L_0x0072
            r4 = r5[r7]
            r4.close(r12)     // Catch:{ IOException -> 0x006d }
        L_0x0064:
            int r7 = r7 + 1
            goto L_0x005d
        L_0x0067:
            r1 = move-exception
            r6 = r1
            goto L_0x0015
        L_0x006a:
            r7 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x006a }
            throw r7
        L_0x006d:
            r1 = move-exception
            if (r6 == 0) goto L_0x0064
            r6 = r1
            goto L_0x0064
        L_0x0072:
            if (r3 == 0) goto L_0x0080
            int r9 = r3.length
            r7 = r8
        L_0x0076:
            if (r7 >= r9) goto L_0x0080
            r2 = r3[r7]
            r2.cancel()
            int r7 = r7 + 1
            goto L_0x0076
        L_0x0080:
            okhttp3.internal.http2.Http2Writer r7 = r10.writer     // Catch:{ IOException -> 0x008d }
            r7.close()     // Catch:{ IOException -> 0x008d }
        L_0x0085:
            java.net.Socket r7 = r10.socket     // Catch:{ IOException -> 0x0092 }
            r7.close()     // Catch:{ IOException -> 0x0092 }
        L_0x008a:
            if (r6 == 0) goto L_0x0095
            throw r6
        L_0x008d:
            r1 = move-exception
            if (r6 != 0) goto L_0x0085
            r6 = r1
            goto L_0x0085
        L_0x0092:
            r1 = move-exception
            r6 = r1
            goto L_0x008a
        L_0x0095:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.close(okhttp3.internal.http2.ErrorCode, okhttp3.internal.http2.ErrorCode):void");
    }

    public void start() throws IOException {
        start(true);
    }

    /* access modifiers changed from: package-private */
    public void start(boolean sendConnectionPreface) throws IOException {
        if (sendConnectionPreface) {
            this.writer.connectionPreface();
            this.writer.settings(this.okHttpSettings);
            int windowSize = this.okHttpSettings.getInitialWindowSize();
            if (windowSize != 65535) {
                this.writer.windowUpdate(0, (long) (windowSize - SupportMenu.USER_MASK));
            }
        }
        new Thread(this.readerRunnable).start();
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.writer) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new ConnectionShutdownException();
                }
                this.okHttpSettings.merge(settings);
                this.writer.settings(settings);
            }
        }
    }

    public synchronized boolean isShutdown() {
        return this.shutdown;
    }

    public static class Builder {
        boolean client;
        String hostname;
        Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        PushObserver pushObserver = PushObserver.CANCEL;
        BufferedSink sink;
        Socket socket;
        BufferedSource source;

        public Builder(boolean client2) {
            this.client = client2;
        }

        public Builder socket(Socket socket2) throws IOException {
            return socket(socket2, ((InetSocketAddress) socket2.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket2)), Okio.buffer(Okio.sink(socket2)));
        }

        public Builder socket(Socket socket2, String hostname2, BufferedSource source2, BufferedSink sink2) {
            this.socket = socket2;
            this.hostname = hostname2;
            this.source = source2;
            this.sink = sink2;
            return this;
        }

        public Builder listener(Listener listener2) {
            this.listener = listener2;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver2) {
            this.pushObserver = pushObserver2;
            return this;
        }

        public Http2Connection build() throws IOException {
            return new Http2Connection(this);
        }
    }

    class ReaderRunnable extends NamedRunnable implements Http2Reader.Handler {
        final Http2Reader reader;

        ReaderRunnable(Http2Reader reader2) {
            super("OkHttp %s", Http2Connection.this.hostname);
            this.reader = reader2;
        }

        /* access modifiers changed from: protected */
        public void execute() {
            ErrorCode connectionErrorCode = ErrorCode.INTERNAL_ERROR;
            ErrorCode streamErrorCode = ErrorCode.INTERNAL_ERROR;
            try {
                this.reader.readConnectionPreface(this);
                do {
                } while (this.reader.nextFrame(false, this));
                try {
                    Http2Connection.this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
                } catch (IOException e) {
                }
                Util.closeQuietly((Closeable) this.reader);
            } catch (IOException e2) {
                connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
                try {
                    Http2Connection.this.close(connectionErrorCode, ErrorCode.PROTOCOL_ERROR);
                } catch (IOException e3) {
                }
                Util.closeQuietly((Closeable) this.reader);
            } catch (Throwable th) {
                try {
                    Http2Connection.this.close(connectionErrorCode, streamErrorCode);
                } catch (IOException e4) {
                }
                Util.closeQuietly((Closeable) this.reader);
                throw th;
            }
        }

        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushDataLater(streamId, source, length, inFinished);
                return;
            }
            Http2Stream dataStream = Http2Connection.this.getStream(streamId);
            if (dataStream == null) {
                Http2Connection.this.writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
                source.skip((long) length);
                return;
            }
            dataStream.receiveData(source, length);
            if (inFinished) {
                dataStream.receiveFin();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0073, code lost:
            r6.receiveHeaders(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0076, code lost:
            if (r10 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0078, code lost:
            r6.receiveFin();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void headers(boolean r10, int r11, int r12, java.util.List<okhttp3.internal.http2.Header> r13) {
            /*
                r9 = this;
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this
                boolean r1 = r1.pushedStream(r11)
                if (r1 == 0) goto L_0x000e
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this
                r1.pushHeadersLater(r11, r13, r10)
            L_0x000d:
                return
            L_0x000e:
                okhttp3.internal.http2.Http2Connection r7 = okhttp3.internal.http2.Http2Connection.this
                monitor-enter(r7)
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                boolean r1 = r1.shutdown     // Catch:{ all -> 0x0019 }
                if (r1 == 0) goto L_0x001c
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                goto L_0x000d
            L_0x0019:
                r1 = move-exception
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                throw r1
            L_0x001c:
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                okhttp3.internal.http2.Http2Stream r6 = r1.getStream(r11)     // Catch:{ all -> 0x0019 }
                if (r6 != 0) goto L_0x0072
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                int r1 = r1.lastGoodStreamId     // Catch:{ all -> 0x0019 }
                if (r11 > r1) goto L_0x002c
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                goto L_0x000d
            L_0x002c:
                int r1 = r11 % 2
                okhttp3.internal.http2.Http2Connection r2 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                int r2 = r2.nextStreamId     // Catch:{ all -> 0x0019 }
                int r2 = r2 % 2
                if (r1 != r2) goto L_0x0038
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                goto L_0x000d
            L_0x0038:
                okhttp3.internal.http2.Http2Stream r0 = new okhttp3.internal.http2.Http2Stream     // Catch:{ all -> 0x0019 }
                okhttp3.internal.http2.Http2Connection r2 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                r3 = 0
                r1 = r11
                r4 = r10
                r5 = r13
                r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x0019 }
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                r1.lastGoodStreamId = r11     // Catch:{ all -> 0x0019 }
                okhttp3.internal.http2.Http2Connection r1 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r1 = r1.streams     // Catch:{ all -> 0x0019 }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x0019 }
                r1.put(r2, r0)     // Catch:{ all -> 0x0019 }
                java.util.concurrent.ExecutorService r1 = okhttp3.internal.http2.Http2Connection.executor     // Catch:{ all -> 0x0019 }
                okhttp3.internal.http2.Http2Connection$ReaderRunnable$1 r2 = new okhttp3.internal.http2.Http2Connection$ReaderRunnable$1     // Catch:{ all -> 0x0019 }
                java.lang.String r3 = "OkHttp %s stream %d"
                r4 = 2
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0019 }
                r5 = 0
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0019 }
                java.lang.String r8 = r8.hostname     // Catch:{ all -> 0x0019 }
                r4[r5] = r8     // Catch:{ all -> 0x0019 }
                r5 = 1
                java.lang.Integer r8 = java.lang.Integer.valueOf(r11)     // Catch:{ all -> 0x0019 }
                r4[r5] = r8     // Catch:{ all -> 0x0019 }
                r2.<init>(r3, r4, r0)     // Catch:{ all -> 0x0019 }
                r1.execute(r2)     // Catch:{ all -> 0x0019 }
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                goto L_0x000d
            L_0x0072:
                monitor-exit(r7)     // Catch:{ all -> 0x0019 }
                r6.receiveHeaders(r13)
                if (r10 == 0) goto L_0x000d
                r6.receiveFin()
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.headers(boolean, int, int, java.util.List):void");
        }

        public void rstStream(int streamId, ErrorCode errorCode) {
            if (Http2Connection.this.pushedStream(streamId)) {
                Http2Connection.this.pushResetLater(streamId, errorCode);
                return;
            }
            Http2Stream rstStream = Http2Connection.this.removeStream(streamId);
            if (rstStream != null) {
                rstStream.receiveRstStream(errorCode);
            }
        }

        /* JADX WARNING: type inference failed for: r8v24, types: [java.lang.Object[]] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void settings(boolean r16, okhttp3.internal.http2.Settings r17) {
            /*
                r15 = this;
                r2 = 0
                r7 = 0
                okhttp3.internal.http2.Http2Connection r9 = okhttp3.internal.http2.Http2Connection.this
                monitor-enter(r9)
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0097 }
                int r5 = r8.getInitialWindowSize()     // Catch:{ all -> 0x0097 }
                if (r16 == 0) goto L_0x0017
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0097 }
                r8.clear()     // Catch:{ all -> 0x0097 }
            L_0x0017:
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0097 }
                r0 = r17
                r8.merge(r0)     // Catch:{ all -> 0x0097 }
                r0 = r17
                r15.applyAndAckSettings(r0)     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x0097 }
                int r4 = r8.getInitialWindowSize()     // Catch:{ all -> 0x0097 }
                r8 = -1
                if (r4 == r8) goto L_0x0069
                if (r4 == r5) goto L_0x0069
                int r8 = r4 - r5
                long r2 = (long) r8     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                boolean r8 = r8.receivedInitialPeerSettings     // Catch:{ all -> 0x0097 }
                if (r8 != 0) goto L_0x0045
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                r8.addBytesToWriteWindow(r2)     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                r10 = 1
                r8.receivedInitialPeerSettings = r10     // Catch:{ all -> 0x0097 }
            L_0x0045:
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r8 = r8.streams     // Catch:{ all -> 0x0097 }
                boolean r8 = r8.isEmpty()     // Catch:{ all -> 0x0097 }
                if (r8 != 0) goto L_0x0069
                okhttp3.internal.http2.Http2Connection r8 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r8 = r8.streams     // Catch:{ all -> 0x0097 }
                java.util.Collection r8 = r8.values()     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Connection r10 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                java.util.Map<java.lang.Integer, okhttp3.internal.http2.Http2Stream> r10 = r10.streams     // Catch:{ all -> 0x0097 }
                int r10 = r10.size()     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Stream[] r10 = new okhttp3.internal.http2.Http2Stream[r10]     // Catch:{ all -> 0x0097 }
                java.lang.Object[] r8 = r8.toArray(r10)     // Catch:{ all -> 0x0097 }
                r0 = r8
                okhttp3.internal.http2.Http2Stream[] r0 = (okhttp3.internal.http2.Http2Stream[]) r0     // Catch:{ all -> 0x0097 }
                r7 = r0
            L_0x0069:
                java.util.concurrent.ExecutorService r8 = okhttp3.internal.http2.Http2Connection.executor     // Catch:{ all -> 0x0097 }
                okhttp3.internal.http2.Http2Connection$ReaderRunnable$2 r10 = new okhttp3.internal.http2.Http2Connection$ReaderRunnable$2     // Catch:{ all -> 0x0097 }
                java.lang.String r11 = "OkHttp %s settings"
                r12 = 1
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x0097 }
                r13 = 0
                okhttp3.internal.http2.Http2Connection r14 = okhttp3.internal.http2.Http2Connection.this     // Catch:{ all -> 0x0097 }
                java.lang.String r14 = r14.hostname     // Catch:{ all -> 0x0097 }
                r12[r13] = r14     // Catch:{ all -> 0x0097 }
                r10.<init>(r11, r12)     // Catch:{ all -> 0x0097 }
                r8.execute(r10)     // Catch:{ all -> 0x0097 }
                monitor-exit(r9)     // Catch:{ all -> 0x0097 }
                if (r7 == 0) goto L_0x009d
                r8 = 0
                int r8 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r8 == 0) goto L_0x009d
                int r9 = r7.length
                r8 = 0
            L_0x008b:
                if (r8 >= r9) goto L_0x009d
                r6 = r7[r8]
                monitor-enter(r6)
                r6.addBytesToWriteWindow(r2)     // Catch:{ all -> 0x009a }
                monitor-exit(r6)     // Catch:{ all -> 0x009a }
                int r8 = r8 + 1
                goto L_0x008b
            L_0x0097:
                r8 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x0097 }
                throw r8
            L_0x009a:
                r8 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x009a }
                throw r8
            L_0x009d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http2.Http2Connection.ReaderRunnable.settings(boolean, okhttp3.internal.http2.Settings):void");
        }

        private void applyAndAckSettings(final Settings peerSettings) {
            Http2Connection.executor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[]{Http2Connection.this.hostname}) {
                public void execute() {
                    try {
                        Http2Connection.this.writer.applyAndAckSettings(peerSettings);
                    } catch (IOException e) {
                    }
                }
            });
        }

        public void ackSettings() {
        }

        public void ping(boolean reply, int payload1, int payload2) {
            if (reply) {
                Ping ping = Http2Connection.this.removePing(payload1);
                if (ping != null) {
                    ping.receive();
                    return;
                }
                return;
            }
            Http2Connection.this.writePingLater(true, payload1, payload2, (Ping) null);
        }

        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            Http2Stream[] streamsCopy;
            if (debugData.size() > 0) {
            }
            synchronized (Http2Connection.this) {
                streamsCopy = (Http2Stream[]) Http2Connection.this.streams.values().toArray(new Http2Stream[Http2Connection.this.streams.size()]);
                Http2Connection.this.shutdown = true;
            }
            for (Http2Stream http2Stream : streamsCopy) {
                if (http2Stream.getId() > lastGoodStreamId && http2Stream.isLocallyInitiated()) {
                    http2Stream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    Http2Connection.this.removeStream(http2Stream.getId());
                }
            }
        }

        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (Http2Connection.this) {
                    Http2Connection.this.bytesLeftInWriteWindow += windowSizeIncrement;
                    Http2Connection.this.notifyAll();
                }
                return;
            }
            Http2Stream stream = Http2Connection.this.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                }
            }
        }

        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
            Http2Connection.this.pushRequestLater(promisedStreamId, requestHeaders);
        }

        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
        }
    }

    /* access modifiers changed from: package-private */
    public boolean pushedStream(int streamId) {
        return streamId != 0 && (streamId & 1) == 0;
    }

    /* access modifiers changed from: package-private */
    public void pushRequestLater(int streamId, List<Header> requestHeaders) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
                writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(streamId));
            final int i = streamId;
            final List<Header> list = requestHeaders;
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
                public void execute() {
                    if (Http2Connection.this.pushObserver.onRequest(i, list)) {
                        try {
                            Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                            synchronized (Http2Connection.this) {
                                Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public void pushHeadersLater(int streamId, List<Header> requestHeaders, boolean inFinished) {
        final int i = streamId;
        final List<Header> list = requestHeaders;
        final boolean z = inFinished;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            public void execute() {
                boolean cancel = Http2Connection.this.pushObserver.onHeaders(i, list, z);
                if (cancel) {
                    try {
                        Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                    } catch (IOException e) {
                        return;
                    }
                }
                if (cancel || z) {
                    synchronized (Http2Connection.this) {
                        Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void pushDataLater(int streamId, BufferedSource source, int byteCount, boolean inFinished) throws IOException {
        final Buffer buffer = new Buffer();
        source.require((long) byteCount);
        source.read(buffer, (long) byteCount);
        if (buffer.size() != ((long) byteCount)) {
            throw new IOException(buffer.size() + " != " + byteCount);
        }
        final int i = streamId;
        final int i2 = byteCount;
        final boolean z = inFinished;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    boolean cancel = Http2Connection.this.pushObserver.onData(i, buffer, i2, z);
                    if (cancel) {
                        Http2Connection.this.writer.rstStream(i, ErrorCode.CANCEL);
                    }
                    if (cancel || z) {
                        synchronized (Http2Connection.this) {
                            Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void pushResetLater(int streamId, ErrorCode errorCode) {
        final int i = streamId;
        final ErrorCode errorCode2 = errorCode;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[]{this.hostname, Integer.valueOf(streamId)}) {
            public void execute() {
                Http2Connection.this.pushObserver.onReset(i, errorCode2);
                synchronized (Http2Connection.this) {
                    Http2Connection.this.currentPushRequests.remove(Integer.valueOf(i));
                }
            }
        });
    }

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
            public void onStream(Http2Stream stream) throws IOException {
                stream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public abstract void onStream(Http2Stream http2Stream) throws IOException;

        public void onSettings(Http2Connection connection) {
        }
    }
}
