package org.xutils.image;

final class MemCacheKey {
    public final ImageOptions options;
    public final String url;

    public MemCacheKey(String url2, ImageOptions options2) {
        this.url = url2;
        this.options = options2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemCacheKey that = (MemCacheKey) o;
        if (this.url.equals(that.url)) {
            return this.options.equals(that.options);
        }
        return false;
    }

    public int hashCode() {
        return (this.url.hashCode() * 31) + this.options.hashCode();
    }

    public String toString() {
        return this.url + this.options.toString();
    }
}
