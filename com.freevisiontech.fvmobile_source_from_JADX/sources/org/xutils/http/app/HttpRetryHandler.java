package org.xutils.http.app;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.request.UriRequest;
import org.xutils.p019ex.HttpException;

public class HttpRetryHandler {
    protected static HashSet<Class<?>> blackList = new HashSet<>();
    protected int maxRetryCount = 2;

    static {
        blackList.add(HttpException.class);
        blackList.add(Callback.CancelledException.class);
        blackList.add(MalformedURLException.class);
        blackList.add(URISyntaxException.class);
        blackList.add(NoRouteToHostException.class);
        blackList.add(PortUnreachableException.class);
        blackList.add(ProtocolException.class);
        blackList.add(NullPointerException.class);
        blackList.add(FileNotFoundException.class);
        blackList.add(JSONException.class);
        blackList.add(UnknownHostException.class);
        blackList.add(IllegalArgumentException.class);
    }

    public void setMaxRetryCount(int maxRetryCount2) {
        this.maxRetryCount = maxRetryCount2;
    }

    public boolean canRetry(UriRequest request, Throwable ex, int count) {
        LogUtil.m1571w(ex.getMessage(), ex);
        if (count > this.maxRetryCount) {
            LogUtil.m1570w(request.toString());
            LogUtil.m1570w("The Max Retry times has been reached!");
            return false;
        } else if (!HttpMethod.permitsRetry(request.getParams().getMethod())) {
            LogUtil.m1570w(request.toString());
            LogUtil.m1570w("The Request Method can not be retried.");
            return false;
        } else if (!blackList.contains(ex.getClass())) {
            return true;
        } else {
            LogUtil.m1570w(request.toString());
            LogUtil.m1570w("The Exception can not be retried.");
            return false;
        }
    }
}
