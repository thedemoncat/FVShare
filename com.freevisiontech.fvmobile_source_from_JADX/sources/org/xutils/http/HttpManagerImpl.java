package org.xutils.http;

import java.lang.reflect.Type;
import org.xutils.C2090x;
import org.xutils.HttpManager;
import org.xutils.common.Callback;

public final class HttpManagerImpl implements HttpManager {
    private static volatile HttpManagerImpl instance;
    private static final Object lock = new Object();

    private HttpManagerImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new HttpManagerImpl();
                }
            }
        }
        C2090x.Ext.setHttpManager(instance);
    }

    public <T> Callback.Cancelable get(RequestParams entity, Callback.CommonCallback<T> callback) {
        return request(HttpMethod.GET, entity, callback);
    }

    public <T> Callback.Cancelable post(RequestParams entity, Callback.CommonCallback<T> callback) {
        return request(HttpMethod.POST, entity, callback);
    }

    public <T> Callback.Cancelable request(HttpMethod method, RequestParams entity, Callback.CommonCallback<T> callback) {
        entity.setMethod(method);
        Callback.Cancelable cancelable = null;
        if (callback instanceof Callback.Cancelable) {
            cancelable = (Callback.Cancelable) callback;
        }
        return C2090x.task().start(new HttpTask<>(entity, cancelable, callback));
    }

    public <T> T getSync(RequestParams entity, Class<T> resultType) throws Throwable {
        return requestSync(HttpMethod.GET, entity, resultType);
    }

    public <T> T postSync(RequestParams entity, Class<T> resultType) throws Throwable {
        return requestSync(HttpMethod.POST, entity, resultType);
    }

    public <T> T requestSync(HttpMethod method, RequestParams entity, Class<T> resultType) throws Throwable {
        return requestSync(method, entity, new DefaultSyncCallback<>(resultType));
    }

    public <T> T requestSync(HttpMethod method, RequestParams entity, Callback.TypedCallback<T> callback) throws Throwable {
        entity.setMethod(method);
        return C2090x.task().startSync(new HttpTask<>(entity, (Callback.Cancelable) null, callback));
    }

    private class DefaultSyncCallback<T> implements Callback.TypedCallback<T> {
        private final Class<T> resultType;

        public DefaultSyncCallback(Class<T> resultType2) {
            this.resultType = resultType2;
        }

        public Type getLoadType() {
            return this.resultType;
        }

        public void onSuccess(T t) {
        }

        public void onError(Throwable ex, boolean isOnCallback) {
        }

        public void onCancelled(Callback.CancelledException cex) {
        }

        public void onFinished() {
        }
    }
}
