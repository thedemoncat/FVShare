package org.xutils.common;

import java.lang.reflect.Type;

public interface Callback {

    public interface CacheCallback<ResultType> extends CommonCallback<ResultType> {
        boolean onCache(ResultType resulttype);
    }

    public interface Callable<ResultType> {
        void call(ResultType resulttype);
    }

    public interface Cancelable {
        void cancel();

        boolean isCancelled();
    }

    public interface CommonCallback<ResultType> extends Callback {
        void onCancelled(CancelledException cancelledException);

        void onError(Throwable th, boolean z);

        void onFinished();

        void onSuccess(ResultType resulttype);
    }

    public interface GroupCallback<ItemType> extends Callback {
        void onAllFinished();

        void onCancelled(ItemType itemtype, CancelledException cancelledException);

        void onError(ItemType itemtype, Throwable th, boolean z);

        void onFinished(ItemType itemtype);

        void onSuccess(ItemType itemtype);
    }

    public interface PrepareCallback<PrepareType, ResultType> extends CommonCallback<ResultType> {
        ResultType prepare(PrepareType preparetype);
    }

    public interface ProgressCallback<ResultType> extends CommonCallback<ResultType> {
        void onLoading(long j, long j2, boolean z);

        void onStarted();

        void onWaiting();
    }

    public interface ProxyCacheCallback<ResultType> extends CacheCallback<ResultType> {
        boolean onlyCache();
    }

    public interface TypedCallback<ResultType> extends CommonCallback<ResultType> {
        Type getLoadType();
    }

    public static class CancelledException extends RuntimeException {
        public CancelledException(String detailMessage) {
            super(detailMessage);
        }
    }
}
