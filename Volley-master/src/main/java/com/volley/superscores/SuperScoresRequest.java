package com.volley.superscores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.superscores.android.common.utils.HttpUtils;
import com.volley.AuthFailureError;
import com.volley.NetworkResponse;
import com.volley.RequestQueue;
import com.volley.VolleyError;
import com.volley.superscores.parser.Parser;
import com.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

/**
 * The enhanced of normal request.
 *
 * Created by Pongpat on 3/9/15.
 */
public class SuperScoresRequest<D, E> extends com.volley.Request<NetworkResponseWrapper<D>> {

    private static long sLastRequestTimestamp = Long.MIN_VALUE;

    private Response.Listener<D> mListener;
    private Response.ErrorListener<E> mErrorListener;

    private Parser<D, E> mParser;

    private final Context mContext;
    private RequestQueue mRequestQueue;

    private final Map<String, String> mHeaders;
    private final boolean mMustNotCache;
    private boolean mIsStaleCacheEnabled = true;

    private Priority mPriority = Priority.NORMAL;
    private Map<String, String> mParams;

    public SuperScoresRequest(@NonNull Context context, int method,
            @Nullable Map<String, String> headers, @NonNull String url,
            @NonNull Parser<D, E> parser, @NonNull Response.Listener<D> listener,
            @NonNull Response.ErrorListener<E> errorListener) {
        super(method, url, null);

        SuperScoresLog.request(method, headers, url);

        mContext = context.getApplicationContext();
        if (headers != null) {
            mHeaders = headers;
        } else {
            mHeaders = Collections.emptyMap();
        }

        // only allow cache for GET request
        // if authorization header is set, do not cache
        mMustNotCache = (method != Method.GET) || !TextUtils.isEmpty(mHeaders.get("Authorization"));

        mListener = listener;
        mErrorListener = errorListener;
        mParser = parser;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public final Priority getPriority() {
        return mPriority;
    }

    public final void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public final Map<String, String> getParams() {
        return mParams;
    }

    public final void setParams(@NonNull Map<String, String> params) {
        mParams = params;
    }

    @SuppressWarnings("unused")
    public boolean isStaleCacheEnabled() {
        return mIsStaleCacheEnabled;
    }

    public void setStaleCacheEnabled(boolean staleCacheEnabled) {
        mIsStaleCacheEnabled = staleCacheEnabled;
    }

    @Override
    public final String getCacheKey() {
        if (mMustNotCache) {
            // do not get data from cache if this request indicate that it do not need cache
            return null;
        }
        return getUrl() + HttpUtils.generateGetParams(mParams);
    }

    @Override
    protected com.volley.Response<NetworkResponseWrapper<D>> parseNetworkResponse(
            NetworkResponse networkResponse) {
        checkInvalidNetworkState();

        NetworkResponseWrapper<D> wrapper = wrapSuccessResponse(mContext, mParser, networkResponse);

        if (mMustNotCache) {
            // do not allow cache by set cache headers null
            return com.volley.Response.success(wrapper, null);
        } else {
            return com.volley.Response.success(wrapper,
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        }
    }

    @Override
    protected ErrorResponse parseErrorNetworkResponse(VolleyError volleyError) {
        NetworkResponseWrapper<E> wrapper = wrapErrorResponse(mContext, mParser,
                volleyError.networkResponse);

        ErrorResponse errorNetworkResponse = new ErrorResponse(volleyError.networkResponse);
        errorNetworkResponse.setErrorObject(wrapper);

        return errorNetworkResponse;
    }

    @Override
    protected final void deliverResponse(NetworkResponseWrapper<D> response) {
        mListener.onResponse(response);
    }

    @Override
    public final void deliverError(VolleyError volleyError) {
        if (volleyError instanceof ErrorResponse) {
            ErrorResponse errorNetworkResponse = (ErrorResponse) volleyError;

            @SuppressWarnings("unchecked")
            NetworkResponseWrapper<E> wrapper = (NetworkResponseWrapper<E>) errorNetworkResponse
                    .getErrorObject();
            mErrorListener.onErrorResponse(wrapper);
        }
    }

    private NetworkResponseWrapper<D> wrapSuccessResponse(Context context,
            @NonNull Parser<D, E> parser, @Nullable NetworkResponse networkResponse) {

        D data;
        StringResponse response = parseRawNetworkResponse(networkResponse);
        if (TextUtils.isEmpty(response.getRawResponse()) || parser == null) {
            data = null;
        } else {
            data = parser.parse(context, response.getRawResponse(), response.getHttpStatus(),
                    response.getHeaders());
        }
        return new NetworkResponseWrapper<>(data, response);
    }

    private NetworkResponseWrapper<E> wrapErrorResponse(@NonNull Context context,
            @NonNull Parser<D, E> parser, @Nullable NetworkResponse networkResponse) {

        E data;
        StringResponse response = parseRawNetworkResponse(networkResponse);

        if (TextUtils.isEmpty(response.getRawResponse()) || parser == null) {
            data = null;
        } else {
            data = parser.parseError(context, response.getRawResponse(), response.getHttpStatus(),
                    response.getHeaders());
        }
        return new NetworkResponseWrapper<>(data, response);
    }

    private StringResponse parseRawNetworkResponse(@Nullable NetworkResponse networkResponse) {
        int httpStatus;
        Map<String, String> headers;
        boolean notModified;
        String rawResponse;

        if (networkResponse != null) {
            httpStatus = networkResponse.statusCode;
            headers = networkResponse.headers;
            notModified = networkResponse.notModified;

            try {
                rawResponse = new String(networkResponse.data,
                        HttpHeaderParser.parseCharset(networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                rawResponse = new String(networkResponse.data);
            }
        } else {
            httpStatus = 0;
            headers = null;
            notModified = false;
            rawResponse = null;
        }

        SuperScoresLog.response(httpStatus, notModified, getUrl(), headers, rawResponse);
        return new StringResponse(rawResponse, httpStatus, headers, notModified);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Check request state
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public com.volley.Request<?> setRequestQueue(@NonNull RequestQueue requestQueue) {
        mRequestQueue = requestQueue;
        return super.setRequestQueue(requestQueue);
    }

    private void checkInvalidNetworkState() {
        if (sLastRequestTimestamp == Long.MIN_VALUE) {
            sLastRequestTimestamp = VolleyPrefUtils.getLastRequestTimestamp(mContext);
        }

        if (sLastRequestTimestamp > System.currentTimeMillis()) {
            //it's likely that user previously manually set time to the future. The cache state
            // will be invalid here.
            clearCache();
        }

        sLastRequestTimestamp = System.currentTimeMillis();
        VolleyPrefUtils.saveLastRequestTimestamp(mContext, sLastRequestTimestamp);
    }

    private void clearCache() {
        if (mRequestQueue != null) {
            mRequestQueue.getCache()
                    .clear();
        }
    }
}