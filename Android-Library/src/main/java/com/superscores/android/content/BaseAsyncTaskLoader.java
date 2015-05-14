/**
 *
 */
package com.superscores.android.content;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * BaseAsyncTaskLoader with all boilerplate.
 *
 * @author Pongpat Ratanaamornpin
 */

@SuppressWarnings("unused")
public abstract class BaseAsyncTaskLoader<D> extends AsyncTaskLoader<D> {

    protected D mData;

    public BaseAsyncTaskLoader(Context context) {
        super(context);
    }

    /**
     * Called when there is new data to deliver to the client. The super class will take care of
     * delivering it; the implementation here just adds a little more logic.
     */
    @Override
    public void deliverResult(D data) {
        if (isReset()) {
            // An async query came in while the loader is stopped. We
            // don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }

        D oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(data);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldData != null) {
            onReleaseResources(oldData);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }

        /*
         * the follow commented code is from API Demo do not delete it we want to keep it as a
         * reference
         */
        // // Start watching for changes in the app data.
        // if (mPackageObserver == null) {
        // mPackageObserver = new PackageIntentReceiver(this);
        // }
        //
        // // Has something interesting in the configuration changed since we
        // // last built the app list?
        // boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());
        //
        // if (takeContentChanged() || mApps == null || configChange) {
        // // If the data has changed since the last time it was loaded
        // // or is not currently available, start a load.
        // forceLoad();
        // }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(D data) {
        super.onCanceled(data);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(data);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }

        /*
         * the follow commented code is from API Demo do not delete it we want to keep it as a
         * reference
         */
        // Stop monitoring for changes.
        // if (mPackageObserver != null) {
        // getContext().unregisterReceiver(mPackageObserver);
        // mPackageObserver = null;
        // }
    }

    /**
     * Helper function to take care of releasing resources associated with an actively loaded data
     * set.
     */
    protected void onReleaseResources(D data) {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }

}