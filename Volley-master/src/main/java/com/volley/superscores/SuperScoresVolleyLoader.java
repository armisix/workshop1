/**
 *
 */
package com.volley.superscores;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.volley.RequestQueue;
import com.volley.superscores.parser.Parser;

/**
 * @author Pongpat Ratanaamornpin
 */
public class SuperScoresVolleyLoader<D, E> extends AsyncTaskLoader<RequestResult<D, E>> implements
        Response.Listener<D>, Response.ErrorListener<E> {

    private RequestQueue mRequestQueue;
    private RequestResult<D, E> mData;

    private RequestBuilder mRequestBuilder;
    private Parser<D, E> mParser;

    public SuperScoresVolleyLoader(Context context, RequestQueue requestQueue,
            RequestBuilder requestBuilder, Parser<D, E> parser) {
        super(context);
        mRequestQueue = requestQueue;

        mRequestBuilder = requestBuilder;
        mParser = parser;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Volley callback
    // /////////////////////////////////////////////////////////////////////////

    @Override
    @SuppressWarnings("unchecked")
    public void onResponse(NetworkResponseWrapper<D> responseWrapper) {
        mData = RequestResult.success(responseWrapper);
        forceLoad();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onErrorResponse(NetworkResponseWrapper<E> errorResponseWrapper) {
        mData = RequestResult.error(errorResponseWrapper);
        forceLoad();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Loader lifecycle
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected final void onStartLoading() {
        String logMessage = "onStartLoading " + (mData == null ? "(no data exist)" : "(reuse " +
                "" + "cache data)");
        SuperScoresLog.info(logMessage);

        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we start volley request.
            final SuperScoresRequest<?, ?> volleyRequest = mRequestBuilder.build(getContext(),
                    mParser, this, this);
            volleyRequest.setTag(this);

            mRequestQueue.add(volleyRequest);
        }
    }

    @Override
    public final RequestResult<D, E> loadInBackground() {
        return mData;
    }

    /**
     * Called when there is new data to deliver to the client. The super class will take care of
     * delivering it; the implementation here just adds a little more logic.
     */
    @Override
    public final void deliverResult(RequestResult<D, E> data) {
        SuperScoresLog.info("deliverResult");

        if (isReset()) {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }

        RequestResult<D, E> oldData = mData;
        // cache data
        mData = data;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(data);
        }

        // At this point we can release the resources associated with
        // 'oldData' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected final void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected final void onReset() {
        SuperScoresLog.info("onReset");

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'data'
        // if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    @Override
    public final boolean cancelLoad() {
        mRequestQueue.cancelAll(this);
        return super.cancelLoad();
    }

    @Override
    public final void onCanceled(RequestResult<D, E> data) {
        super.onCanceled(data);
        SuperScoresLog.info("onCanceled");

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(data);
    }

    /**
     * Helper function to take care of releasing resources associated with an actively loaded data
     * set.
     */
    @SuppressWarnings("unused")
    protected void onReleaseResources(RequestResult<D, E> data) {
        SuperScoresLog.info("onReleaseResources");
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }
}