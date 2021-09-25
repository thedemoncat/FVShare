package com.google.android.exoplayer.dash.mpd;

import android.os.SystemClock;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.upstream.UriLoadable;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CancellationException;

public final class UtcTimingElementResolver implements Loader.Callback {
    private final UtcTimingCallback callback;
    private UriLoadable<Long> singleUseLoadable;
    private Loader singleUseLoader;
    private final UtcTimingElement timingElement;
    private final long timingElementElapsedRealtime;
    private final UriDataSource uriDataSource;

    public interface UtcTimingCallback {
        void onTimestampError(UtcTimingElement utcTimingElement, IOException iOException);

        void onTimestampResolved(UtcTimingElement utcTimingElement, long j);
    }

    public static void resolveTimingElement(UriDataSource uriDataSource2, UtcTimingElement timingElement2, long timingElementElapsedRealtime2, UtcTimingCallback callback2) {
        new UtcTimingElementResolver(uriDataSource2, timingElement2, timingElementElapsedRealtime2, callback2).resolve();
    }

    private UtcTimingElementResolver(UriDataSource uriDataSource2, UtcTimingElement timingElement2, long timingElementElapsedRealtime2, UtcTimingCallback callback2) {
        this.uriDataSource = uriDataSource2;
        this.timingElement = (UtcTimingElement) Assertions.checkNotNull(timingElement2);
        this.timingElementElapsedRealtime = timingElementElapsedRealtime2;
        this.callback = (UtcTimingCallback) Assertions.checkNotNull(callback2);
    }

    private void resolve() {
        String scheme = this.timingElement.schemeIdUri;
        if (Util.areEqual(scheme, "urn:mpeg:dash:utc:direct:2012")) {
            resolveDirect();
        } else if (Util.areEqual(scheme, "urn:mpeg:dash:utc:http-iso:2014")) {
            resolveHttp(new Iso8601Parser());
        } else if (Util.areEqual(scheme, "urn:mpeg:dash:utc:http-xsdate:2012") || Util.areEqual(scheme, "urn:mpeg:dash:utc:http-xsdate:2014")) {
            resolveHttp(new XsDateTimeParser());
        } else {
            this.callback.onTimestampError(this.timingElement, new IOException("Unsupported utc timing scheme"));
        }
    }

    private void resolveDirect() {
        try {
            this.callback.onTimestampResolved(this.timingElement, Util.parseXsDateTime(this.timingElement.value) - this.timingElementElapsedRealtime);
        } catch (ParseException e) {
            this.callback.onTimestampError(this.timingElement, new ParserException((Throwable) e));
        }
    }

    private void resolveHttp(UriLoadable.Parser<Long> parser) {
        this.singleUseLoader = new Loader("utctiming");
        this.singleUseLoadable = new UriLoadable<>(this.timingElement.value, this.uriDataSource, parser);
        this.singleUseLoader.startLoading(this.singleUseLoadable, this);
    }

    public void onLoadCanceled(Loader.Loadable loadable) {
        onLoadError(loadable, new IOException("Load cancelled", new CancellationException()));
    }

    public void onLoadCompleted(Loader.Loadable loadable) {
        releaseLoader();
        this.callback.onTimestampResolved(this.timingElement, this.singleUseLoadable.getResult().longValue() - SystemClock.elapsedRealtime());
    }

    public void onLoadError(Loader.Loadable loadable, IOException exception) {
        releaseLoader();
        this.callback.onTimestampError(this.timingElement, exception);
    }

    private void releaseLoader() {
        this.singleUseLoader.release();
    }

    private static class XsDateTimeParser implements UriLoadable.Parser<Long> {
        private XsDateTimeParser() {
        }

        public Long parse(String connectionUrl, InputStream inputStream) throws ParserException, IOException {
            try {
                return Long.valueOf(Util.parseXsDateTime(new BufferedReader(new InputStreamReader(inputStream)).readLine()));
            } catch (ParseException e) {
                throw new ParserException((Throwable) e);
            }
        }
    }

    private static class Iso8601Parser implements UriLoadable.Parser<Long> {
        private Iso8601Parser() {
        }

        public Long parse(String connectionUrl, InputStream inputStream) throws ParserException, IOException {
            String firstLine = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                return Long.valueOf(format.parse(firstLine).getTime());
            } catch (ParseException e) {
                throw new ParserException((Throwable) e);
            }
        }
    }
}
