package org.xutils.http.body;

import org.xutils.http.ProgressHandler;

public interface ProgressBody extends RequestBody {
    void setProgressHandler(ProgressHandler progressHandler);
}
