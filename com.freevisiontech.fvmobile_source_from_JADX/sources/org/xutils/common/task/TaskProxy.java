package org.xutils.common.task;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import org.xutils.common.Callback;
import org.xutils.common.task.AbsTask;

class TaskProxy<ResultType> extends AbsTask<ResultType> {
    private static final int MSG_WHAT_BASE = 1000000000;
    private static final int MSG_WHAT_ON_CANCEL = 1000000006;
    private static final int MSG_WHAT_ON_ERROR = 1000000004;
    private static final int MSG_WHAT_ON_FINISHED = 1000000007;
    private static final int MSG_WHAT_ON_START = 1000000002;
    private static final int MSG_WHAT_ON_SUCCESS = 1000000003;
    private static final int MSG_WHAT_ON_UPDATE = 1000000005;
    private static final int MSG_WHAT_ON_WAITING = 1000000001;
    static final PriorityExecutor sDefaultExecutor = new PriorityExecutor(true);
    static final InternalHandler sHandler = new InternalHandler();
    /* access modifiers changed from: private */
    public volatile boolean callOnCanceled = false;
    /* access modifiers changed from: private */
    public volatile boolean callOnFinished = false;
    private final Executor executor;
    /* access modifiers changed from: private */
    public final AbsTask<ResultType> task;

    TaskProxy(AbsTask<ResultType> task2) {
        super(task2);
        this.task = task2;
        this.task.setTaskProxy(this);
        setTaskProxy((TaskProxy) null);
        Executor taskExecutor = task2.getExecutor();
        this.executor = taskExecutor == null ? sDefaultExecutor : taskExecutor;
    }

    /* access modifiers changed from: protected */
    public final ResultType doBackground() throws Throwable {
        onWaiting();
        this.executor.execute(new PriorityRunnable(this.task.getPriority(), new Runnable() {
            public void run() {
                try {
                    if (TaskProxy.this.callOnCanceled || TaskProxy.this.isCancelled()) {
                        throw new Callback.CancelledException("");
                    }
                    TaskProxy.this.onStarted();
                    if (TaskProxy.this.isCancelled()) {
                        throw new Callback.CancelledException("");
                    }
                    TaskProxy.this.task.setResult(TaskProxy.this.task.doBackground());
                    TaskProxy.this.setResult(TaskProxy.this.task.getResult());
                    if (TaskProxy.this.isCancelled()) {
                        throw new Callback.CancelledException("");
                    }
                    TaskProxy.this.onSuccess(TaskProxy.this.task.getResult());
                } catch (Callback.CancelledException cex) {
                    TaskProxy.this.onCancelled(cex);
                } catch (Throwable ex) {
                    TaskProxy.this.onError(ex, false);
                } finally {
                    TaskProxy.this.onFinished();
                }
            }
        }));
        return null;
    }

    /* access modifiers changed from: protected */
    public void onWaiting() {
        setState(AbsTask.State.WAITING);
        sHandler.obtainMessage(MSG_WHAT_ON_WAITING, this).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onStarted() {
        setState(AbsTask.State.STARTED);
        sHandler.obtainMessage(MSG_WHAT_ON_START, this).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onSuccess(ResultType resulttype) {
        setState(AbsTask.State.SUCCESS);
        sHandler.obtainMessage(MSG_WHAT_ON_SUCCESS, this).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onError(Throwable ex, boolean isCallbackError) {
        setState(AbsTask.State.ERROR);
        sHandler.obtainMessage(MSG_WHAT_ON_ERROR, new ArgsObj(this, ex)).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onUpdate(int flag, Object... args) {
        sHandler.obtainMessage(MSG_WHAT_ON_UPDATE, flag, flag, new ArgsObj(this, args)).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Callback.CancelledException cex) {
        setState(AbsTask.State.CANCELLED);
        sHandler.obtainMessage(MSG_WHAT_ON_CANCEL, new ArgsObj(this, cex)).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void onFinished() {
        sHandler.obtainMessage(MSG_WHAT_ON_FINISHED, this).sendToTarget();
    }

    /* access modifiers changed from: package-private */
    public final void setState(AbsTask.State state) {
        super.setState(state);
        this.task.setState(state);
    }

    public final Priority getPriority() {
        return this.task.getPriority();
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    private static class ArgsObj {
        final Object[] args;
        final TaskProxy taskProxy;

        public ArgsObj(TaskProxy taskProxy2, Object... args2) {
            this.taskProxy = taskProxy2;
            this.args = args2;
        }
    }

    static final class InternalHandler extends Handler {
        static final /* synthetic */ boolean $assertionsDisabled = (!TaskProxy.class.desiredAssertionStatus());

        private InternalHandler() {
            super(Looper.getMainLooper());
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x004f  */
        /* JADX WARNING: Removed duplicated region for block: B:42:0x00cc  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r9) {
            /*
                r8 = this;
                r7 = 1
                java.lang.Object r5 = r9.obj
                if (r5 != 0) goto L_0x000e
                java.lang.IllegalArgumentException r5 = new java.lang.IllegalArgumentException
                java.lang.String r6 = "msg must not be null"
                r5.<init>(r6)
                throw r5
            L_0x000e:
                r3 = 0
                r0 = 0
                java.lang.Object r5 = r9.obj
                boolean r5 = r5 instanceof org.xutils.common.task.TaskProxy
                if (r5 == 0) goto L_0x0025
                java.lang.Object r3 = r9.obj
                org.xutils.common.task.TaskProxy r3 = (org.xutils.common.task.TaskProxy) r3
            L_0x001a:
                if (r3 != 0) goto L_0x0034
                java.lang.RuntimeException r5 = new java.lang.RuntimeException
                java.lang.String r6 = "msg.obj not instanceof TaskProxy"
                r5.<init>(r6)
                throw r5
            L_0x0025:
                java.lang.Object r5 = r9.obj
                boolean r5 = r5 instanceof org.xutils.common.task.TaskProxy.ArgsObj
                if (r5 == 0) goto L_0x001a
                java.lang.Object r1 = r9.obj
                org.xutils.common.task.TaskProxy$ArgsObj r1 = (org.xutils.common.task.TaskProxy.ArgsObj) r1
                org.xutils.common.task.TaskProxy r3 = r1.taskProxy
                java.lang.Object[] r0 = r1.args
                goto L_0x001a
            L_0x0034:
                int r5 = r9.what     // Catch:{ Throwable -> 0x0042 }
                switch(r5) {
                    case 1000000001: goto L_0x003a;
                    case 1000000002: goto L_0x0057;
                    case 1000000003: goto L_0x005f;
                    case 1000000004: goto L_0x006b;
                    case 1000000005: goto L_0x008c;
                    case 1000000006: goto L_0x0096;
                    case 1000000007: goto L_0x00b9;
                    default: goto L_0x0039;
                }     // Catch:{ Throwable -> 0x0042 }
            L_0x0039:
                return
            L_0x003a:
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                r5.onWaiting()     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x0042:
                r2 = move-exception
                org.xutils.common.task.AbsTask$State r5 = org.xutils.common.task.AbsTask.State.ERROR
                r3.setState(r5)
                int r5 = r9.what
                r6 = 1000000004(0x3b9aca04, float:0.004723789)
                if (r5 == r6) goto L_0x00cc
                org.xutils.common.task.AbsTask r5 = r3.task
                r5.onError(r2, r7)
                goto L_0x0039
            L_0x0057:
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                r5.onStarted()     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x005f:
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                java.lang.Object r6 = r3.getResult()     // Catch:{ Throwable -> 0x0042 }
                r5.onSuccess(r6)     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x006b:
                boolean r5 = $assertionsDisabled     // Catch:{ Throwable -> 0x0042 }
                if (r5 != 0) goto L_0x0077
                if (r0 != 0) goto L_0x0077
                java.lang.AssertionError r5 = new java.lang.AssertionError     // Catch:{ Throwable -> 0x0042 }
                r5.<init>()     // Catch:{ Throwable -> 0x0042 }
                throw r5     // Catch:{ Throwable -> 0x0042 }
            L_0x0077:
                r5 = 0
                r4 = r0[r5]     // Catch:{ Throwable -> 0x0042 }
                java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ Throwable -> 0x0042 }
                java.lang.String r5 = r4.getMessage()     // Catch:{ Throwable -> 0x0042 }
                org.xutils.common.util.LogUtil.m1563d(r5, r4)     // Catch:{ Throwable -> 0x0042 }
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                r6 = 0
                r5.onError(r4, r6)     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x008c:
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                int r6 = r9.arg1     // Catch:{ Throwable -> 0x0042 }
                r5.onUpdate(r6, r0)     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x0096:
                boolean r5 = r3.callOnCanceled     // Catch:{ Throwable -> 0x0042 }
                if (r5 != 0) goto L_0x0039
                r5 = 1
                boolean unused = r3.callOnCanceled = r5     // Catch:{ Throwable -> 0x0042 }
                boolean r5 = $assertionsDisabled     // Catch:{ Throwable -> 0x0042 }
                if (r5 != 0) goto L_0x00ac
                if (r0 != 0) goto L_0x00ac
                java.lang.AssertionError r5 = new java.lang.AssertionError     // Catch:{ Throwable -> 0x0042 }
                r5.<init>()     // Catch:{ Throwable -> 0x0042 }
                throw r5     // Catch:{ Throwable -> 0x0042 }
            L_0x00ac:
                org.xutils.common.task.AbsTask r6 = r3.task     // Catch:{ Throwable -> 0x0042 }
                r5 = 0
                r5 = r0[r5]     // Catch:{ Throwable -> 0x0042 }
                org.xutils.common.Callback$CancelledException r5 = (org.xutils.common.Callback.CancelledException) r5     // Catch:{ Throwable -> 0x0042 }
                r6.onCancelled(r5)     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x00b9:
                boolean r5 = r3.callOnFinished     // Catch:{ Throwable -> 0x0042 }
                if (r5 != 0) goto L_0x0039
                r5 = 1
                boolean unused = r3.callOnFinished = r5     // Catch:{ Throwable -> 0x0042 }
                org.xutils.common.task.AbsTask r5 = r3.task     // Catch:{ Throwable -> 0x0042 }
                r5.onFinished()     // Catch:{ Throwable -> 0x0042 }
                goto L_0x0039
            L_0x00cc:
                boolean r5 = org.xutils.C2090x.isDebug()
                if (r5 == 0) goto L_0x0039
                java.lang.RuntimeException r5 = new java.lang.RuntimeException
                r5.<init>(r2)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: org.xutils.common.task.TaskProxy.InternalHandler.handleMessage(android.os.Message):void");
        }
    }
}
