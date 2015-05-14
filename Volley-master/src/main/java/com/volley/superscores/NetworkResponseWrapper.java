package com.volley.superscores;

import android.support.annotation.NonNull;

import java.util.Map;

@SuppressWarnings("unused")
public class NetworkResponseWrapper<D> {

    private final D mData;
    private final int mHttpStatus;
    private final Map<String, String> mHeaders;
    private final boolean mNotModified;

    private StaleCacheInfo mStaleCacheInfo;

    public NetworkResponseWrapper(D data, @NonNull StringResponse stringResponse) {
        this(data, stringResponse, null);
    }

    public NetworkResponseWrapper(D data, @NonNull StringResponse stringResponse,
            StaleCacheInfo staleCacheInfo) {
        mData = data;
        mHttpStatus = stringResponse.getHttpStatus();
        mHeaders = stringResponse.getHeaders();
        mNotModified = stringResponse.isNotModified();
        mStaleCacheInfo = staleCacheInfo;
    }

    public int getHttpStatus() {
        return mHttpStatus;
    }

    public D getData() {
        return mData;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public boolean isNotModified() {
        return mNotModified;
    }

    public boolean isFromStaleCached() {
        return mStaleCacheInfo != null;
    }

    public StaleCacheInfo getStaleCacheInfo() {
        return mStaleCacheInfo;
    }

    public void setStaleCacheInfo(StaleCacheInfo staleCacheInfo) {
        mStaleCacheInfo = staleCacheInfo;
    }
}