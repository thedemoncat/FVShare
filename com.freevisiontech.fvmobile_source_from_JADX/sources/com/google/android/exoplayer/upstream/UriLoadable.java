package com.google.android.exoplayer.upstream;

import android.net.Uri;
import com.google.android.exoplayer.ParserException;
import com.google.android.exoplayer.upstream.Loader;
import java.io.IOException;
import java.io.InputStream;

public final class UriLoadable<T> implements Loader.Loadable {
    private final DataSpec dataSpec;
    private volatile boolean isCanceled;
    private final Parser<T> parser;
    private volatile T result;
    private final UriDataSource uriDataSource;

    public interface Parser<T> {
        T parse(String str, InputStream inputStream) throws ParserException, IOException;
    }

    public UriLoadable(String url, UriDataSource uriDataSource2, Parser<T> parser2) {
        this.uriDataSource = uriDataSource2;
        this.parser = parser2;
        this.dataSpec = new DataSpec(Uri.parse(url), 1);
    }

    public final T getResult() {
        return this.result;
    }

    public final void cancelLoad() {
        this.isCanceled = true;
    }

    public final boolean isLoadCanceled() {
        return this.isCanceled;
    }

    public final void load() throws IOException, InterruptedException {
        DataSourceInputStream inputStream = new DataSourceInputStream(this.uriDataSource, this.dataSpec);
        try {
            inputStream.open();
            this.result = this.parser.parse(this.uriDataSource.getUri(), inputStream);
        } finally {
            inputStream.close();
        }
    }
}
