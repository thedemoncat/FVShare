package org.xutils.common.task;

import java.util.concurrent.Executor;
import org.xutils.common.Callback;

public abstract class AbsTask<ResultType> implements Callback.Cancelable {
    private final Callback.Cancelable cancelHandler;
    private volatile boolean isCancelled;
    private ResultType result;
    private volatile State state;
    private TaskProxy taskProxy;

    /* access modifiers changed from: protected */
    public abstract ResultType doBackground() throws Throwable;

    /* access modifiers changed from: protected */
    public abstract void onError(Throwable th, boolean z);

    /* access modifiers changed from: protected */
    public abstract void onSuccess(ResultType resulttype);

    public AbsTask() {
        this((Callback.Cancelable) null);
    }

    public AbsTask(Callback.Cancelable cancelHandler2) {
        this.taskProxy = null;
        this.isCancelled = false;
        this.state = State.IDLE;
        this.cancelHandler = cancelHandler2;
    }

    /* access modifiers changed from: protected */
    public void onWaiting() {
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
    }

    /* access modifiers changed from: protected */
    public void onUpdate(int flag, Object... args) {
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Callback.CancelledException cex) {
    }

    /* access modifiers changed from: protected */
    public void onFinished() {
    }

    public Priority getPriority() {
        return null;
    }

    public Executor getExecutor() {
        return null;
    }

    /* access modifiers changed from: protected */
    public final void update(int flag, Object... args) {
        if (this.taskProxy != null) {
            this.taskProxy.onUpdate(flag, args);
        }
    }

    /* access modifiers changed from: protected */
    public void cancelWorks() {
    }

    /* access modifiers changed from: protected */
    public boolean isCancelFast() {
        return false;
    }

    public final synchronized void cancel() {
        if (!this.isCancelled) {
            this.isCancelled = true;
            cancelWorks();
            if (this.cancelHandler != null && !this.cancelHandler.isCancelled()) {
                this.cancelHandler.cancel();
            }
            if (this.state == State.WAITING || (this.state == State.STARTED && isCancelFast())) {
                if (this.taskProxy != null) {
                    this.taskProxy.onCancelled(new Callback.CancelledException("cancelled by user"));
                    this.taskProxy.onFinished();
                } else if (this instanceof TaskProxy) {
                    onCancelled(new Callback.CancelledException("cancelled by user"));
                    onFinished();
                }
            }
        }
    }

    public final boolean isCancelled() {
        return this.isCancelled || this.state == State.CANCELLED || (this.cancelHandler != null && this.cancelHandler.isCancelled());
    }

    public final boolean isFinished() {
        return this.state.value() > State.STARTED.value();
    }

    public final State getState() {
        return this.state;
    }

    public final ResultType getResult() {
        return this.result;
    }

    /* access modifiers changed from: package-private */
    public void setState(State state2) {
        this.state = state2;
    }

    /* access modifiers changed from: package-private */
    public final void setTaskProxy(TaskProxy taskProxy2) {
        this.taskProxy = taskProxy2;
    }

    /* access modifiers changed from: package-private */
    public final void setResult(ResultType result2) {
        this.result = result2;
    }

    public enum State {
        IDLE(0),
        WAITING(1),
        STARTED(2),
        SUCCESS(3),
        CANCELLED(4),
        ERROR(5);
        
        private final int value;

        private State(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }
    }
}
