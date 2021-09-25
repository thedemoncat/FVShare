package org.xutils.common;

import org.xutils.common.Callback;
import org.xutils.common.task.AbsTask;

public interface TaskController {
    void autoPost(Runnable runnable);

    void post(Runnable runnable);

    void postDelayed(Runnable runnable, long j);

    void removeCallbacks(Runnable runnable);

    void run(Runnable runnable);

    <T> AbsTask<T> start(AbsTask<T> absTask);

    <T> T startSync(AbsTask<T> absTask) throws Throwable;

    <T extends AbsTask<?>> Callback.Cancelable startTasks(Callback.GroupCallback<T> groupCallback, T... tArr);
}
