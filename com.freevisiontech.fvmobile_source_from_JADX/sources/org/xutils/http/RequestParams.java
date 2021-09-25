package org.xutils.http;

import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLSocketFactory;
import org.xutils.common.task.Priority;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParamsHelper;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;
import org.xutils.http.app.HttpRetryHandler;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.http.app.RedirectHandler;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.body.RequestBody;

public class RequestParams extends BaseParams {
    private boolean autoRename;
    private boolean autoResume;
    private String buildCacheKey;
    private String buildUri;
    private ParamsBuilder builder;
    private String cacheDirName;
    private final String[] cacheKeys;
    private long cacheMaxAge;
    private long cacheSize;
    private boolean cancelFast;
    private int connectTimeout;
    private Executor executor;
    private HttpRequest httpRequest;
    private HttpRetryHandler httpRetryHandler;
    private boolean invokedGetHttpRequest;
    private int loadingUpdateMaxTimeSpan;
    private int maxRetryCount;
    private Priority priority;
    private Proxy proxy;
    private RedirectHandler redirectHandler;
    private RequestTracker requestTracker;
    private String saveFilePath;
    private final String[] signs;
    private SSLSocketFactory sslSocketFactory;
    private final String uri;
    private boolean useCookie;

    public /* bridge */ /* synthetic */ void addBodyParameter(String str, File file) {
        super.addBodyParameter(str, file);
    }

    public /* bridge */ /* synthetic */ void addBodyParameter(String str, Object obj, String str2) {
        super.addBodyParameter(str, obj, str2);
    }

    public /* bridge */ /* synthetic */ void addBodyParameter(String str, Object obj, String str2, String str3) {
        super.addBodyParameter(str, obj, str2, str3);
    }

    public /* bridge */ /* synthetic */ void addBodyParameter(String str, String str2) {
        super.addBodyParameter(str, str2);
    }

    public /* bridge */ /* synthetic */ void addHeader(String str, String str2) {
        super.addHeader(str, str2);
    }

    public /* bridge */ /* synthetic */ void addParameter(String str, Object obj) {
        super.addParameter(str, obj);
    }

    public /* bridge */ /* synthetic */ void addQueryStringParameter(String str, String str2) {
        super.addQueryStringParameter(str, str2);
    }

    public /* bridge */ /* synthetic */ void clearParams() {
        super.clearParams();
    }

    public /* bridge */ /* synthetic */ String getBodyContent() {
        return super.getBodyContent();
    }

    public /* bridge */ /* synthetic */ List getBodyParams() {
        return super.getBodyParams();
    }

    public /* bridge */ /* synthetic */ String getCharset() {
        return super.getCharset();
    }

    public /* bridge */ /* synthetic */ List getFileParams() {
        return super.getFileParams();
    }

    public /* bridge */ /* synthetic */ List getHeaders() {
        return super.getHeaders();
    }

    public /* bridge */ /* synthetic */ HttpMethod getMethod() {
        return super.getMethod();
    }

    public /* bridge */ /* synthetic */ List getParams(String str) {
        return super.getParams(str);
    }

    public /* bridge */ /* synthetic */ List getQueryStringParams() {
        return super.getQueryStringParams();
    }

    public /* bridge */ /* synthetic */ RequestBody getRequestBody() throws IOException {
        return super.getRequestBody();
    }

    public /* bridge */ /* synthetic */ String getStringParameter(String str) {
        return super.getStringParameter(str);
    }

    public /* bridge */ /* synthetic */ List getStringParams() {
        return super.getStringParams();
    }

    public /* bridge */ /* synthetic */ boolean isAsJsonContent() {
        return super.isAsJsonContent();
    }

    public /* bridge */ /* synthetic */ boolean isMultipart() {
        return super.isMultipart();
    }

    public /* bridge */ /* synthetic */ void removeParameter(String str) {
        super.removeParameter(str);
    }

    public /* bridge */ /* synthetic */ void setAsJsonContent(boolean z) {
        super.setAsJsonContent(z);
    }

    public /* bridge */ /* synthetic */ void setBodyContent(String str) {
        super.setBodyContent(str);
    }

    public /* bridge */ /* synthetic */ void setCharset(String str) {
        super.setCharset(str);
    }

    public /* bridge */ /* synthetic */ void setHeader(String str, String str2) {
        super.setHeader(str, str2);
    }

    public /* bridge */ /* synthetic */ void setMethod(HttpMethod httpMethod) {
        super.setMethod(httpMethod);
    }

    public /* bridge */ /* synthetic */ void setMultipart(boolean z) {
        super.setMultipart(z);
    }

    public /* bridge */ /* synthetic */ void setRequestBody(RequestBody requestBody) {
        super.setRequestBody(requestBody);
    }

    public /* bridge */ /* synthetic */ String toJSONString() {
        return super.toJSONString();
    }

    public RequestParams() {
        this((String) null, (ParamsBuilder) null, (String[]) null, (String[]) null);
    }

    public RequestParams(String uri2) {
        this(uri2, (ParamsBuilder) null, (String[]) null, (String[]) null);
    }

    public RequestParams(String uri2, ParamsBuilder builder2, String[] signs2, String[] cacheKeys2) {
        this.useCookie = true;
        this.priority = Priority.DEFAULT;
        this.connectTimeout = 15000;
        this.autoResume = true;
        this.autoRename = false;
        this.maxRetryCount = 2;
        this.cancelFast = false;
        this.loadingUpdateMaxTimeSpan = 300;
        this.invokedGetHttpRequest = false;
        if (uri2 != null && builder2 == null) {
            builder2 = new DefaultParamsBuilder();
        }
        this.uri = uri2;
        this.signs = signs2;
        this.cacheKeys = cacheKeys2;
        this.builder = builder2;
    }

    /* access modifiers changed from: package-private */
    public void init() throws Throwable {
        if (TextUtils.isEmpty(this.buildUri)) {
            if (!TextUtils.isEmpty(this.uri) || getHttpRequest() != null) {
                initEntityParams();
                this.buildUri = this.uri;
                HttpRequest httpRequest2 = getHttpRequest();
                if (httpRequest2 != null) {
                    this.builder = (ParamsBuilder) httpRequest2.builder().newInstance();
                    this.buildUri = this.builder.buildUri(this, httpRequest2);
                    this.builder.buildParams(this);
                    this.builder.buildSign(this, httpRequest2.signs());
                    if (this.sslSocketFactory == null) {
                        this.sslSocketFactory = this.builder.getSSLSocketFactory();
                    }
                } else if (this.builder != null) {
                    this.builder.buildParams(this);
                    this.builder.buildSign(this, this.signs);
                    if (this.sslSocketFactory == null) {
                        this.sslSocketFactory = this.builder.getSSLSocketFactory();
                    }
                }
            } else {
                throw new IllegalStateException("uri is empty && @HttpRequest == null");
            }
        }
    }

    public String getUri() {
        return TextUtils.isEmpty(this.buildUri) ? this.uri : this.buildUri;
    }

    public String getCacheKey() {
        if (TextUtils.isEmpty(this.buildCacheKey) && this.builder != null) {
            HttpRequest httpRequest2 = getHttpRequest();
            if (httpRequest2 != null) {
                this.buildCacheKey = this.builder.buildCacheKey(this, httpRequest2.cacheKeys());
            } else {
                this.buildCacheKey = this.builder.buildCacheKey(this, this.cacheKeys);
            }
        }
        return this.buildCacheKey;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory2) {
        this.sslSocketFactory = sslSocketFactory2;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public boolean isUseCookie() {
        return this.useCookie;
    }

    public void setUseCookie(boolean useCookie2) {
        this.useCookie = useCookie2;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(Proxy proxy2) {
        this.proxy = proxy2;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority2) {
        this.priority = priority2;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout2) {
        if (connectTimeout2 > 0) {
            this.connectTimeout = connectTimeout2;
        }
    }

    public String getCacheDirName() {
        return this.cacheDirName;
    }

    public void setCacheDirName(String cacheDirName2) {
        this.cacheDirName = cacheDirName2;
    }

    public long getCacheSize() {
        return this.cacheSize;
    }

    public void setCacheSize(long cacheSize2) {
        this.cacheSize = cacheSize2;
    }

    public long getCacheMaxAge() {
        return this.cacheMaxAge;
    }

    public void setCacheMaxAge(long cacheMaxAge2) {
        this.cacheMaxAge = cacheMaxAge2;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public void setExecutor(Executor executor2) {
        this.executor = executor2;
    }

    public boolean isAutoResume() {
        return this.autoResume;
    }

    public void setAutoResume(boolean autoResume2) {
        this.autoResume = autoResume2;
    }

    public boolean isAutoRename() {
        return this.autoRename;
    }

    public void setAutoRename(boolean autoRename2) {
        this.autoRename = autoRename2;
    }

    public String getSaveFilePath() {
        return this.saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath2) {
        this.saveFilePath = saveFilePath2;
    }

    public int getMaxRetryCount() {
        return this.maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount2) {
        this.maxRetryCount = maxRetryCount2;
    }

    public boolean isCancelFast() {
        return this.cancelFast;
    }

    public void setCancelFast(boolean cancelFast2) {
        this.cancelFast = cancelFast2;
    }

    public int getLoadingUpdateMaxTimeSpan() {
        return this.loadingUpdateMaxTimeSpan;
    }

    public void setLoadingUpdateMaxTimeSpan(int loadingUpdateMaxTimeSpan2) {
        this.loadingUpdateMaxTimeSpan = loadingUpdateMaxTimeSpan2;
    }

    public HttpRetryHandler getHttpRetryHandler() {
        return this.httpRetryHandler;
    }

    public void setHttpRetryHandler(HttpRetryHandler httpRetryHandler2) {
        this.httpRetryHandler = httpRetryHandler2;
    }

    public RedirectHandler getRedirectHandler() {
        return this.redirectHandler;
    }

    public void setRedirectHandler(RedirectHandler redirectHandler2) {
        this.redirectHandler = redirectHandler2;
    }

    public RequestTracker getRequestTracker() {
        return this.requestTracker;
    }

    public void setRequestTracker(RequestTracker requestTracker2) {
        this.requestTracker = requestTracker2;
    }

    private void initEntityParams() {
        RequestParamsHelper.parseKV(this, getClass(), new RequestParamsHelper.ParseKVListener() {
            public void onParseKV(String name, Object value) {
                RequestParams.this.addParameter(name, value);
            }
        });
    }

    private HttpRequest getHttpRequest() {
        if (this.httpRequest == null && !this.invokedGetHttpRequest) {
            this.invokedGetHttpRequest = true;
            Class<?> thisCls = getClass();
            if (thisCls != RequestParams.class) {
                this.httpRequest = (HttpRequest) thisCls.getAnnotation(HttpRequest.class);
            }
        }
        return this.httpRequest;
    }

    public String toString() {
        try {
            init();
        } catch (Throwable ex) {
            LogUtil.m1565e(ex.getMessage(), ex);
        }
        String url = getUri();
        if (TextUtils.isEmpty(url)) {
            return super.toString();
        }
        return url + (url.contains("?") ? "&" : "?") + super.toString();
    }
}
