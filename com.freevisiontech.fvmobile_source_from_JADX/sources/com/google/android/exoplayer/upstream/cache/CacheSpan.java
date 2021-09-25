package com.google.android.exoplayer.upstream.cache;

import com.google.android.exoplayer.util.Util;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CacheSpan implements Comparable<CacheSpan> {
    private static final Pattern CACHE_FILE_PATTERN_V1 = Pattern.compile("^(.+)\\.(\\d+)\\.(\\d+)\\.v1\\.exo$", 32);
    private static final Pattern CACHE_FILE_PATTERN_V2 = Pattern.compile("^(.+)\\.(\\d+)(E?)\\.(\\d+)\\.v2\\.exo$", 32);
    private static final String SUFFIX = ".v2.exo";
    public final File file;
    public final boolean isCached;
    public final String key;
    public final long lastAccessTimestamp;
    public final long length;
    public final long position;

    public static File getCacheFileName(File cacheDir, String key2, long offset, long lastAccessTimestamp2) {
        return new File(cacheDir, Util.escapeFileName(key2) + "." + offset + "." + lastAccessTimestamp2 + SUFFIX);
    }

    public static CacheSpan createLookup(String key2, long position2) {
        return new CacheSpan(key2, position2, -1, false, -1, (File) null);
    }

    public static CacheSpan createOpenHole(String key2, long position2) {
        return new CacheSpan(key2, position2, -1, false, -1, (File) null);
    }

    public static CacheSpan createClosedHole(String key2, long position2, long length2) {
        return new CacheSpan(key2, position2, length2, false, -1, (File) null);
    }

    public static CacheSpan createCacheEntry(File file2) {
        String key2;
        Matcher matcher = CACHE_FILE_PATTERN_V2.matcher(file2.getName());
        if (matcher.matches() && (key2 = Util.unescapeFileName(matcher.group(1))) != null) {
            return createCacheEntry(key2, Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(4)), file2);
        }
        return null;
    }

    static File upgradeIfNeeded(File file2) {
        Matcher matcher = CACHE_FILE_PATTERN_V1.matcher(file2.getName());
        if (!matcher.matches()) {
            return file2;
        }
        File newCacheFile = getCacheFileName(file2.getParentFile(), matcher.group(1), Long.parseLong(matcher.group(2)), Long.parseLong(matcher.group(3)));
        file2.renameTo(newCacheFile);
        return newCacheFile;
    }

    private static CacheSpan createCacheEntry(String key2, long position2, long lastAccessTimestamp2, File file2) {
        return new CacheSpan(key2, position2, file2.length(), true, lastAccessTimestamp2, file2);
    }

    CacheSpan(String key2, long position2, long length2, boolean isCached2, long lastAccessTimestamp2, File file2) {
        this.key = key2;
        this.position = position2;
        this.length = length2;
        this.isCached = isCached2;
        this.file = file2;
        this.lastAccessTimestamp = lastAccessTimestamp2;
    }

    public boolean isOpenEnded() {
        return this.length == -1;
    }

    public CacheSpan touch() {
        long now = System.currentTimeMillis();
        File newCacheFile = getCacheFileName(this.file.getParentFile(), this.key, this.position, now);
        this.file.renameTo(newCacheFile);
        return createCacheEntry(this.key, this.position, now, newCacheFile);
    }

    public int compareTo(CacheSpan another) {
        if (!this.key.equals(another.key)) {
            return this.key.compareTo(another.key);
        }
        long startOffsetDiff = this.position - another.position;
        if (startOffsetDiff == 0) {
            return 0;
        }
        return startOffsetDiff < 0 ? -1 : 1;
    }
}
