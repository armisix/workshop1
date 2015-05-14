/**
 *
 */
package com.volley.superscores;

import android.content.Context;

import com.superscores.android.common.utils.HttpUtils;
import com.volley.AuthFailureError;
import com.volley.Request.Method;
import com.volley.Request.Priority;
import com.volley.RetryPolicy;
import com.volley.superscores.parser.Parser;
import com.volley.superscores.toolbox.VolleyErrorAwareRequest;
import com.volley.superscores.toolbox.VolleyParsableRequest;
import com.volley.superscores.toolbox.VolleySimpleRequest;
import com.volley.superscores.toolbox.VolleyStringRequest;

import java.util.Map;

/**
 * + Request builder to help build volley request
 *
 * @author Pongpat Ratanaamornpin
 */

@SuppressWarnings("unused")
public class RequestBuilder {
    protected final int mMethod;
    protected final Map<String, String> mHeaders;
    protected final String mUrl;

    private Priority mPriority;
    private Map<String, String> mUrlParams;
    private Map<String, String> mParams;
    private boolean mIsCacheEnabled;
    private boolean mIsStaleCacheEnabled;
    private RetryPolicy mRetryPolicy;

    public RequestBuilder(int method, Map<String, String> headers, String url) {
        mMethod = method;
        mHeaders = headers;
        mUrl = url;
        mPriority = Priority.NORMAL;

        if (mMethod == Method.GET || mMethod == Method.DEPRECATED_GET_OR_POST) {
            mIsCacheEnabled = true;
            mIsStaleCacheEnabled = true;
        } else {
            mIsCacheEnabled = false;
            mIsStaleCacheEnabled = false;
        }
    }

    public int getMethod() {
        return mMethod;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    public String getUrl() {
        return (mUrlParams == null) ? mUrl : mUrl + HttpUtils.generateGetParams(mUrlParams);
    }

    public RequestBuilder setPriority(Priority priority) {
        mPriority = priority;
        return this;
    }

    public RequestBuilder setUrlParams(Map<String, String> urlParams) {
        mUrlParams = urlParams;
        return this;
    }

    public RequestBuilder setParams(Map<String, String> params) {
        mParams = params;
        return this;
    }

    public RequestBuilder setCacheEnabled(boolean cacheEnabled) {
        mIsCacheEnabled = cacheEnabled;
        return this;
    }

    public RequestBuilder setStaleCacheEnabled(boolean staleCacheEnabled) {
        mIsStaleCacheEnabled = staleCacheEnabled;
        return this;
    }

    public RequestBuilder setRetryPolicy(RetryPolicy retryPolicy) {
        mRetryPolicy = retryPolicy;
        return this;
    }

    public <D, E> SuperScoresRequest<D, E> build(Context context, Parser<D, E> parser,
            Response.Listener<D> listener, Response.ErrorListener<E> errorListener) {

        //we intended to allow parser to be set during build as classes that use same request may
        // use difference parser.
        SuperScoresRequest<D, E> request = new SuperScoresRequest<>(context, mMethod, mHeaders,
                getUrl(), parser, listener, errorListener);
        decorateRequest(request);

        return request;
    }

    public VolleySimpleRequest buildSimpleRequest(Context context, Response.Listener<Void> listener,
            Response.ErrorListener<Void> errorListener) {
        VolleySimpleRequest request = new VolleySimpleRequest(context, mMethod, mHeaders, getUrl(),
                listener, errorListener);
        decorateRequest(request);

        return request;
    }

    public VolleyStringRequest buildStringRequest(Context context,
            Response.Listener<String> listener, Response.ErrorListener<String> errorListener) {

        VolleyStringRequest request = new VolleyStringRequest(context, mMethod, mHeaders, getUrl(),
                listener, errorListener);
        decorateRequest(request);

        return request;
    }

    public <E> VolleyErrorAwareRequest<E> buildErrorAwareRequest(Context context,
            Parser<Void, E> parser, Response.Listener<Void> listener,
            Response.ErrorListener<E> errorListener) {

        VolleyErrorAwareRequest<E> request = new VolleyErrorAwareRequest<>(context, mMethod,
                mHeaders, getUrl(), parser, listener, errorListener);
        decorateRequest(request);

        return request;
    }

    public <D, E> VolleyParsableRequest<D, E> buildParsableRequest(Context context,
            Parser<D, E> parser, Response.Listener<D> listener,
            Response.ErrorListener<E> errorListener) {

        VolleyParsableRequest<D, E> request = new VolleyParsableRequest<>(context, mMethod,
                mHeaders, getUrl(), parser, listener, errorListener);
        decorateRequest(request);

        return request;
    }

    protected void decorateRequest(SuperScoresRequest request) {
        if (mParams != null) {
            request.setParams(mParams);
        }
        request.setRetryPolicy(mRetryPolicy);
        request.setPriority(mPriority);
        request.setShouldCache(mIsCacheEnabled);
        request.setStaleCacheEnabled(mIsStaleCacheEnabled);
    }
}