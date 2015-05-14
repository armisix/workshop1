package com.volley.superscores;

import com.volley.Cache;

/**
 * Cache metadata
 *
 * Created by Pongpat on 3/10/15.
 */
public class StaleCacheInfo {

    private final long mLastLocalModified;
    private final long mTtl;
    private final long mSoftTtl;

    public StaleCacheInfo(long lastLocalModified, Cache.Entry cacheEntry) {
        mLastLocalModified = lastLocalModified;
        mTtl = cacheEntry.ttl;
        mSoftTtl = cacheEntry.softTtl;
    }

    public long getLastLocalModified() {
        return mLastLocalModified;
    }

    public long getTtl() {
        return mTtl;
    }

    public long getSoftTtl() {
        return mSoftTtl;
    }
}