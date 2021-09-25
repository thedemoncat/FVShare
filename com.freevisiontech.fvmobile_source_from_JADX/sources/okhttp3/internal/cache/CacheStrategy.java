package okhttp3.internal.cache;

import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.lzy.okgo.model.HttpHeaders;
import com.umeng.analytics.C0015a;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.http.HttpDate;

public final class CacheStrategy {
    @Nullable
    public final Response cacheResponse;
    @Nullable
    public final Request networkRequest;

    CacheStrategy(Request networkRequest2, Response cacheResponse2) {
        this.networkRequest = networkRequest2;
        this.cacheResponse = cacheResponse2;
    }

    public static boolean isCacheable(Response response, Request request) {
        switch (response.code()) {
            case 200:
            case CompanyIdentifierResolver.BINAURIC_SE /*203*/:
            case CompanyIdentifierResolver.BEATS_ELECTRONICS /*204*/:
            case 300:
            case CompanyIdentifierResolver.SONY_CORPORATION /*301*/:
            case 308:
            case 404:
            case 405:
            case 410:
            case 414:
            case 501:
                break;
            case CompanyIdentifierResolver.ASSA_ABLOY /*302*/:
            case 307:
                if (response.header(HttpHeaders.HEAD_KEY_EXPIRES) == null && response.cacheControl().maxAgeSeconds() == -1 && !response.cacheControl().isPublic() && !response.cacheControl().isPrivate()) {
                    return false;
                }
            default:
                return false;
        }
        return !response.cacheControl().noStore() && !request.cacheControl().noStore();
    }

    public static class Factory {
        private int ageSeconds = -1;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;

        public Factory(long nowMillis2, Request request2, Response cacheResponse2) {
            this.nowMillis = nowMillis2;
            this.request = request2;
            this.cacheResponse = cacheResponse2;
            if (cacheResponse2 != null) {
                this.sentRequestMillis = cacheResponse2.sentRequestAtMillis();
                this.receivedResponseMillis = cacheResponse2.receivedResponseAtMillis();
                Headers headers = cacheResponse2.headers();
                int size = headers.size();
                for (int i = 0; i < size; i++) {
                    String fieldName = headers.name(i);
                    String value = headers.value(i);
                    if (HttpHeaders.HEAD_KEY_DATE.equalsIgnoreCase(fieldName)) {
                        this.servedDate = HttpDate.parse(value);
                        this.servedDateString = value;
                    } else if (HttpHeaders.HEAD_KEY_EXPIRES.equalsIgnoreCase(fieldName)) {
                        this.expires = HttpDate.parse(value);
                    } else if (HttpHeaders.HEAD_KEY_LAST_MODIFIED.equalsIgnoreCase(fieldName)) {
                        this.lastModified = HttpDate.parse(value);
                        this.lastModifiedString = value;
                    } else if (HttpHeaders.HEAD_KEY_E_TAG.equalsIgnoreCase(fieldName)) {
                        this.etag = value;
                    } else if ("Age".equalsIgnoreCase(fieldName)) {
                        this.ageSeconds = okhttp3.internal.http.HttpHeaders.parseSeconds(value, -1);
                    }
                }
            }
        }

        public CacheStrategy get() {
            CacheStrategy candidate = getCandidate();
            if (candidate.networkRequest == null || !this.request.cacheControl().onlyIfCached()) {
                return candidate;
            }
            return new CacheStrategy((Request) null, (Response) null);
        }

        private CacheStrategy getCandidate() {
            String conditionName;
            String conditionValue;
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, (Response) null);
            }
            if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                return new CacheStrategy(this.request, (Response) null);
            }
            if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, (Response) null);
            }
            CacheControl requestCaching = this.request.cacheControl();
            if (requestCaching.noCache() || hasConditions(this.request)) {
                return new CacheStrategy(this.request, (Response) null);
            }
            long ageMillis = cacheResponseAge();
            long freshMillis = computeFreshnessLifetime();
            if (requestCaching.maxAgeSeconds() != -1) {
                freshMillis = Math.min(freshMillis, TimeUnit.SECONDS.toMillis((long) requestCaching.maxAgeSeconds()));
            }
            long minFreshMillis = 0;
            if (requestCaching.minFreshSeconds() != -1) {
                minFreshMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.minFreshSeconds());
            }
            long maxStaleMillis = 0;
            CacheControl responseCaching = this.cacheResponse.cacheControl();
            if (!responseCaching.mustRevalidate() && requestCaching.maxStaleSeconds() != -1) {
                maxStaleMillis = TimeUnit.SECONDS.toMillis((long) requestCaching.maxStaleSeconds());
            }
            if (responseCaching.noCache() || ageMillis + minFreshMillis >= freshMillis + maxStaleMillis) {
                if (this.etag != null) {
                    conditionName = HttpHeaders.HEAD_KEY_IF_NONE_MATCH;
                    conditionValue = this.etag;
                } else if (this.lastModified != null) {
                    conditionName = HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE;
                    conditionValue = this.lastModifiedString;
                } else if (this.servedDate == null) {
                    return new CacheStrategy(this.request, (Response) null);
                } else {
                    conditionName = HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE;
                    conditionValue = this.servedDateString;
                }
                Headers.Builder conditionalRequestHeaders = this.request.headers().newBuilder();
                Internal.instance.addLenient(conditionalRequestHeaders, conditionName, conditionValue);
                return new CacheStrategy(this.request.newBuilder().headers(conditionalRequestHeaders.build()).build(), this.cacheResponse);
            }
            Response.Builder builder = this.cacheResponse.newBuilder();
            if (ageMillis + minFreshMillis >= freshMillis) {
                builder.addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
            }
            if (ageMillis > C0015a.f22i && isFreshnessLifetimeHeuristic()) {
                builder.addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
            }
            return new CacheStrategy((Request) null, builder.build());
        }

        private long computeFreshnessLifetime() {
            long servedMillis;
            long servedMillis2;
            CacheControl responseCaching = this.cacheResponse.cacheControl();
            if (responseCaching.maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis((long) responseCaching.maxAgeSeconds());
            }
            if (this.expires != null) {
                if (this.servedDate != null) {
                    servedMillis2 = this.servedDate.getTime();
                } else {
                    servedMillis2 = this.receivedResponseMillis;
                }
                long delta = this.expires.getTime() - servedMillis2;
                if (delta <= 0) {
                    delta = 0;
                }
                return delta;
            } else if (this.lastModified == null || this.cacheResponse.request().url().query() != null) {
                return 0;
            } else {
                if (this.servedDate != null) {
                    servedMillis = this.servedDate.getTime();
                } else {
                    servedMillis = this.sentRequestMillis;
                }
                long delta2 = servedMillis - this.lastModified.getTime();
                if (delta2 > 0) {
                    return delta2 / 10;
                }
                return 0;
            }
        }

        private long cacheResponseAge() {
            long receivedAge;
            long apparentReceivedAge = 0;
            if (this.servedDate != null) {
                apparentReceivedAge = Math.max(0, this.receivedResponseMillis - this.servedDate.getTime());
            }
            if (this.ageSeconds != -1) {
                receivedAge = Math.max(apparentReceivedAge, TimeUnit.SECONDS.toMillis((long) this.ageSeconds));
            } else {
                receivedAge = apparentReceivedAge;
            }
            long responseDuration = this.receivedResponseMillis - this.sentRequestMillis;
            return receivedAge + responseDuration + (this.nowMillis - this.receivedResponseMillis);
        }

        private boolean isFreshnessLifetimeHeuristic() {
            return this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
        }

        private static boolean hasConditions(Request request2) {
            return (request2.header(HttpHeaders.HEAD_KEY_IF_MODIFIED_SINCE) == null && request2.header(HttpHeaders.HEAD_KEY_IF_NONE_MATCH) == null) ? false : true;
        }
    }
}
