package org.xutils.common.task;

import android.os.Looper;
import java.util.concurrent.atomic.AtomicInteger;
import org.xutils.C2090x;
import org.xutils.common.Callback;
import org.xutils.common.TaskController;
import org.xutils.common.util.LogUtil;

public final class TaskControllerImpl implements TaskController {
    private static volatile TaskController instance;

    private TaskControllerImpl() {
    }

    public static void registerInstance() {
        if (instance == null) {
            synchronized (TaskController.class) {
                if (instance == null) {
                    instance = new TaskControllerImpl();
                }
            }
        }
        C2090x.Ext.setTaskController(instance);
    }

    public <T> AbsTask<T> start(AbsTask<T> task) {
        TaskProxy<T> proxy;
        if (task instanceof TaskProxy) {
            proxy = (TaskProxy) task;
        } else {
            proxy = new TaskProxy<>(task);
        }
        try {
            proxy.doBackground();
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        return proxy;
    }

    public <T> T startSync(AbsTask<T> task) throws Throwable {
        T result = null;
        try {
            task.onWaiting();
            task.onStarted();
            result = task.doBackground();
            task.onSuccess(result);
            task.onFinished();
        } catch (Callback.CancelledException cex) {
            task.onCancelled(cex);
            task.onFinished();
        } catch (Throwable th) {
            task.onFinished();
            throw th;
        }
        return result;
    }

    public <T extends AbsTask<?>> Callback.Cancelable startTasks(final Callback.GroupCallback<T> groupCallback, final T... tasks) {
        if (tasks == null) {
            throw new IllegalArgumentException("task must not be null");
        }
        final Runnable callIfOnAllFinished = new Runnable() {
            private final AtomicInteger count = new AtomicInteger(0);
            private final int total = tasks.length;

            public void run() {
                if (this.count.incrementAndGet() == this.total && groupCallback != null) {
                    groupCallback.onAllFinished();
                }
            }
        };
        for (T task : tasks) {
            final Callback.GroupCallback<T> groupCallback2 = groupCallback;
            final T t = task;
            start(new TaskProxy(task) {
                /* access modifiers changed from: protected */
                public void onSuccess(Object result) {
                    super.onSuccess(result);
                    TaskControllerImpl.this.post(new Runnable() {
                        public void run() {
                            if (groupCallback2 != null) {
                                groupCallback2.onSuccess(t);
                            }
                        }
                    });
                }

                /* access modifiers changed from: protected */
                public void onCancelled(final Callback.CancelledException cex) {
                    super.onCancelled(cex);
                    TaskControllerImpl.this.post(new Runnable() {
                        public void run() {
                            if (groupCallback2 != null) {
                                groupCallback2.onCancelled(t, cex);
                            }
                        }
                    });
                }

                /* access modifiers changed from: protected */
                public void onError(final Throwable ex, final boolean isCallbackError) {
                    super.onError(ex, isCallbackError);
                    TaskControllerImpl.this.post(new Runnable() {
                        public void run() {
                            if (groupCallback2 != null) {
                                groupCallback2.onError(t, ex, isCallbackError);
                            }
                        }
                    });
                }

                /* access modifiers changed from: protected */
                public void onFinished() {
                    super.onFinished();
                    TaskControllerImpl.this.post(new Runnable() {
                        public void run() {
                            if (groupCallback2 != null) {
                                groupCallback2.onFinished(t);
                            }
                            callIfOnAllFinished.run();
                        }
                    });
                }
            });
        }
        return new Callback.Cancelable() {
            public void cancel() {
                for (T task : tasks) {
                    task.cancel();
                }
            }

            public boolean isCancelled() {
                boolean isCancelled = true;
                for (T task : tasks) {
                    if (!task.isCancelled()) {
                        isCancelled = false;
                    }
                }
                return isCancelled;
            }
        };
    }

    public void autoPost(Runnable runnable) {
        if (runnable != null) {
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                runnable.run();
            } else {
                TaskProxy.sHandler.post(runnable);
            }
        }
    }

    public void post(Runnable runnable) {
        if (runnable != null) {
            TaskProxy.sHandler.post(runnable);
        }
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        if (runnable != null) {
            TaskProxy.sHandler.postDelayed(runnable, delayMillis);
        }
    }

    public void run(Runnable runnable) {
        if (!TaskProxy.sDefaultExecutor.isBusy()) {
            TaskProxy.sDefaultExecutor.execute(runnable);
        } else {
            new Thread(runnable).start();
        }
    }

    public void removeCallbacks(Runnable runnable) {
        TaskProxy.sHandler.removeCallbacks(runnable);
    }
}
