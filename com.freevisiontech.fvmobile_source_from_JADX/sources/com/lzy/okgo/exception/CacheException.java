package com.lzy.okgo.exception;

public class CacheException extends Exception {
    private static final long serialVersionUID = 845628123701073013L;

    public static CacheException NON_OR_EXPIRE(String cacheKey) {
        return new CacheException("cacheKey = " + cacheKey + " ,can't find cache by cacheKey, or cache has expired!");
    }

    public static CacheException NON_AND_304(String cacheKey) {
        return new CacheException("the http response code is 304, but the cache with cacheKey = " + cacheKey + " is null or expired!");
    }

    public CacheException(String detailMessage) {
        super(detailMessage);
    }
}
