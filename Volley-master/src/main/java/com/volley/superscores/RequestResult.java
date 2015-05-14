package com.volley.superscores;

import android.support.annotation.NonNull;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import java.util.Map;

public class RequestResult<D, E> {

    private final D mData;
    private final E mError;
    private final boolean mIsSuccess;

    private final int mHttpStatus;
    private final Map<String, String> mHeaders;
    private final StaleCacheInfo mStaleCacheInfo;

    private RequestResult(D data, E error, boolean isSuccess, int httpStatus,
            Map<String, String> headers) {
        this(data, error, isSuccess, httpStatus, headers, null);
    }

    private RequestResult(D data, E error, boolean isSuccess, int httpStatus,
            Map<String, String> headers, StaleCacheInfo staleCacheInfo) {
        mData = data;
        mError = error;
        mIsSuccess = isSuccess;

        mHttpStatus = httpStatus;
        mHeaders = headers;
        mStaleCacheInfo = staleCacheInfo;
    }

    public static <D, E> RequestResult<D, E> success(@NonNull NetworkResponseWrapper<D> wrapper) {
        return new RequestResult<>(wrapper.getData(), null, true, wrapper.getHttpStatus(),
                wrapper.getHeaders(), wrapper.getStaleCacheInfo());
    }

    public static <D, E> RequestResult<D, E> error(@NonNull NetworkResponseWrapper<E> wrapper) {
        return new RequestResult<>(null, wrapper.getData(), false, wrapper.getHttpStatus(),
                wrapper.getHeaders());
    }

    public D getData() {
        return mData;
    }

    public E getError() {
        return mError;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public int getHttpStatus() {
        return mHttpStatus;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public boolean isFromStaleCached() {
        return mStaleCacheInfo != null;
    }

    public StaleCacheInfo getStaleCacheInfo() {
        return mStaleCacheInfo;
    }

    public long getDate() {
        String dateStr = mHeaders.get(HTTP.DATE_HEADER);
        try {
            // Parse date in RFC1123 format if this header contains one
            return DateUtils.parseDate(dateStr)
                    .getTime();
        } catch (DateParseException e) {
            // Date in invalid format, fallback to Const.NONE
            //MUST_DO
            //return Const.NONE;
            return -1;
        }
    }
}