package com.bumptech.glide.manager;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
    private boolean isPaused;
    private final List<Request> pendingRequests = new ArrayList();
    private final Set<Request> requests = Collections.newSetFromMap(new WeakHashMap());

    public void runRequest(Request request) {
        this.requests.add(request);
        if (!this.isPaused) {
            request.begin();
        } else {
            this.pendingRequests.add(request);
        }
    }

    /* access modifiers changed from: package-private */
    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void removeRequest(Request request) {
        this.requests.remove(request);
        this.pendingRequests.remove(request);
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public void pauseRequests() {
        this.isPaused = true;
        for (T request : Util.getSnapshot(this.requests)) {
            if (request.isRunning()) {
                request.pause();
                this.pendingRequests.add(request);
            }
        }
    }

    public void resumeRequests() {
        this.isPaused = false;
        for (T request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled() && !request.isRunning()) {
                request.begin();
            }
        }
        this.pendingRequests.clear();
    }

    public void clearRequests() {
        for (T request : Util.getSnapshot(this.requests)) {
            request.clear();
        }
        this.pendingRequests.clear();
    }

    public void restartRequests() {
        for (T request : Util.getSnapshot(this.requests)) {
            if (!request.isComplete() && !request.isCancelled()) {
                request.pause();
                if (!this.isPaused) {
                    request.begin();
                } else {
                    this.pendingRequests.add(request);
                }
            }
        }
    }
}
