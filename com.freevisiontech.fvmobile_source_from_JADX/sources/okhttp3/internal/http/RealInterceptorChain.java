package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor.Chain {
    private int calls;
    private final RealConnection connection;
    private final HttpCodec httpCodec;
    private final int index;
    private final List<Interceptor> interceptors;
    private final Request request;
    private final StreamAllocation streamAllocation;

    public RealInterceptorChain(List<Interceptor> interceptors2, StreamAllocation streamAllocation2, HttpCodec httpCodec2, RealConnection connection2, int index2, Request request2) {
        this.interceptors = interceptors2;
        this.connection = connection2;
        this.streamAllocation = streamAllocation2;
        this.httpCodec = httpCodec2;
        this.index = index2;
        this.request = request2;
    }

    public Connection connection() {
        return this.connection;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    public HttpCodec httpStream() {
        return this.httpCodec;
    }

    public Request request() {
        return this.request;
    }

    public Response proceed(Request request2) throws IOException {
        return proceed(request2, this.streamAllocation, this.httpCodec, this.connection);
    }

    public Response proceed(Request request2, StreamAllocation streamAllocation2, HttpCodec httpCodec2, RealConnection connection2) throws IOException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        this.calls++;
        if (this.httpCodec != null && !this.connection.supportsUrl(request2.url())) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port");
        } else if (this.httpCodec == null || this.calls <= 1) {
            RealInterceptorChain next = new RealInterceptorChain(this.interceptors, streamAllocation2, httpCodec2, connection2, this.index + 1, request2);
            Interceptor interceptor = this.interceptors.get(this.index);
            Response response = interceptor.intercept(next);
            if (httpCodec2 != null && this.index + 1 < this.interceptors.size() && next.calls != 1) {
                throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
            } else if (response != null) {
                return response;
            } else {
                throw new NullPointerException("interceptor " + interceptor + " returned null");
            }
        } else {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once");
        }
    }
}
