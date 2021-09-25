package android.support.p001v4.p014os;

import android.os.AsyncTask;

@Deprecated
/* renamed from: android.support.v4.os.AsyncTaskCompat */
public final class AsyncTaskCompat {
    @Deprecated
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(AsyncTask<Params, Progress, Result> task, Params... params) {
        if (task == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        return task;
    }

    private AsyncTaskCompat() {
    }
}
