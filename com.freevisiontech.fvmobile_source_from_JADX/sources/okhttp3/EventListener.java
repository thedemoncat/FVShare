package okhttp3;

import java.net.InetAddress;
import java.util.List;

abstract class EventListener {
    public static final EventListener NONE = new EventListener() {
    };

    public interface Factory {
        EventListener create(Call call);
    }

    EventListener() {
    }

    static Factory factory(EventListener listener) {
        return new Factory() {
            public EventListener create(Call call) {
                return EventListener.this;
            }
        };
    }

    public void fetchStart(Call call) {
    }

    public void dnsStart(Call call, String domainName) {
    }

    public void dnsEnd(Call call, String domainName, List<InetAddress> list, Throwable throwable) {
    }

    public void connectStart(Call call, InetAddress address, int port) {
    }

    public void secureConnectStart(Call call) {
    }

    public void secureConnectEnd(Call call, Handshake handshake, Throwable throwable) {
    }

    public void connectEnd(Call call, InetAddress address, int port, String protocol, Throwable throwable) {
    }

    public void requestHeadersStart(Call call) {
    }

    public void requestHeadersEnd(Call call, Throwable throwable) {
    }

    public void requestBodyStart(Call call) {
    }

    public void requestBodyEnd(Call call, Throwable throwable) {
    }

    public void responseHeadersStart(Call call) {
    }

    public void responseHeadersEnd(Call call, Throwable throwable) {
    }

    public void responseBodyStart(Call call) {
    }

    public void responseBodyEnd(Call call, Throwable throwable) {
    }

    public void fetchEnd(Call call, Throwable throwable) {
    }
}
