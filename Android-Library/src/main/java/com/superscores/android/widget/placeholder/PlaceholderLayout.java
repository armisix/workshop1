package com.superscores.android.widget.placeholder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.superscores.android.R;
import com.superscores.android.common.utils.SuperLog;

@SuppressWarnings("unused")
public class PlaceholderLayout extends FrameLayout implements OnDataSetChangedListener {

    public enum State {
        CONTENT,
        LOADING,
        NO_DATA,
        RETRY
    }

    private PlaceholderDataObserver mPlaceholderDataObserver;
    private OnPlaceholderStateChangedListener mOnPlaceholderStateChangedListener;
    private OnRetryClickListener mOnRetryClickListener;

    private String mTagRetryButton;
    private State mCurrentState = null;

    private View mContentView;
    private PlaceholderLoadingWrapperLayout mLoadingLayout;
    private PlaceholderNoDataWrapperLayout mNoDataLayout;
    private PlaceholderRetryWrapperLayout mRetryLayout;

    private boolean mObservingData = false;

    public interface OnRetryClickListener {
        public void onRetryClicked();
    }

    public interface OnPlaceholderStateChangedListener {
        public void onPlaceholderStateChanged(State state);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Init
    // /////////////////////////////////////////////////////////////////////////

    public PlaceholderLayout(Context context, View contentView) {
        super(context);
        if (contentView == null) {
            throw new IllegalArgumentException("contentView must not be null");
        }
        mContentView = contentView;
        addView(mContentView);

        showLoading();
    }

    public PlaceholderLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PlaceholderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public PlaceholderLayout(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() == 1) {
            //if only one child we assume that child is content
            mContentView = getChildAt(0);
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);

                if (view instanceof PlaceholderLoadingWrapperLayout) {
                    if (mLoadingLayout == null) {
                        mLoadingLayout = (PlaceholderLoadingWrapperLayout) view;
                    } else {
                        throw new IllegalStateException("Only one loading placeholder allowed");
                    }
                } else if (view instanceof PlaceholderNoDataWrapperLayout) {
                    if (mNoDataLayout == null) {
                        mNoDataLayout = (PlaceholderNoDataWrapperLayout) view;
                    } else {
                        throw new IllegalStateException("Only one no data placeholder allowed");
                    }
                } else if (view instanceof PlaceholderRetryWrapperLayout) {
                    if (mRetryLayout == null) {
                        mRetryLayout = (PlaceholderRetryWrapperLayout) view;
                    } else {
                        throw new IllegalStateException("Only one retry placeholder allowed");
                    }
                } else {
                    //no wrapper assume that view is content
                    if (mContentView == null) {
                        mContentView = view;
                    } else {
                        throw new IllegalStateException("Only one content view allowed");
                    }
                }
            }
        }

        if (mContentView == null) {
            throw new IllegalStateException("Cannot find content view");
        }
        showLoading();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Content
    // /////////////////////////////////////////////////////////////////////////

    public void setContent(@LayoutRes int layoutId) {
        setContent(LayoutInflater.from(getContext())
                .inflate(layoutId, this, false));
    }

    public void setContent(@NonNull View view) {
        if (mContentView != null) {
            removeView(mContentView);
        }
        mContentView = view;
        addView(mContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public View getContent() {
        return mContentView;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Loading Placeholder
    // /////////////////////////////////////////////////////////////////////////

    public void setLoading(@LayoutRes int layoutId) {
        setLoading(LayoutInflater.from(getContext())
                .inflate(layoutId, this, false));
    }

    public void setLoading(@NonNull View view) {
        setLoading(new PlaceholderLoadingWrapperLayout(getContext(), view));
    }

    public void setLoading(@NonNull PlaceholderLoadingWrapperLayout loadingLayout) {
        if (mLoadingLayout != null) {
            removeView(mLoadingLayout);
        }
        mLoadingLayout = loadingLayout;
        mLoadingLayout.setVisibility(View.INVISIBLE);
        addPlaceholderLayout(mLoadingLayout);
    }

    public PlaceholderLoadingWrapperLayout getLoadingLayout() {
        return mLoadingLayout;
    }

    // /////////////////////////////////////////////////////////////////////////
    // No Data Placeholder
    // /////////////////////////////////////////////////////////////////////////

    public void setNoData(@LayoutRes int layoutId) {
        setNoData(LayoutInflater.from(getContext())
                .inflate(layoutId, this, false));
    }

    public void setNoData(@NonNull View view) {
        setNoData(new PlaceholderNoDataWrapperLayout(getContext(), view));
    }

    public void setNoData(@NonNull PlaceholderNoDataWrapperLayout noDataLayout) {
        if (mNoDataLayout != null) {
            removeView(mNoDataLayout);
        }
        mNoDataLayout = noDataLayout;
        mNoDataLayout.setVisibility(View.INVISIBLE);
        addPlaceholderLayout(mNoDataLayout);
    }

    public PlaceholderNoDataWrapperLayout getNoDataLayout() {
        return mNoDataLayout;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Retry Placeholder
    // /////////////////////////////////////////////////////////////////////////

    public void setRetry(@LayoutRes int layoutId, @IdRes int retryButtonId) {
        setRetry(LayoutInflater.from(getContext())
                .inflate(layoutId, this, false), retryButtonId);
    }

    public void setRetry(@LayoutRes int layoutId, @NonNull String retryButtonTag) {
        setRetry(LayoutInflater.from(getContext())
                .inflate(layoutId, this, false), retryButtonTag);
    }

    public void setRetry(@NonNull View view, @IdRes int retryButtonId) {
        setRetry(new PlaceholderRetryWrapperLayout(getContext(), view, retryButtonId));
    }

    public void setRetry(@NonNull View view, @NonNull String retryButtonTag) {
        setRetry(new PlaceholderRetryWrapperLayout(getContext(), view, retryButtonTag));
    }

    public void setRetry(@NonNull PlaceholderRetryWrapperLayout retryLayout) {
        if (mRetryLayout != null) {
            removeView(mRetryLayout);
        }

        mRetryLayout = retryLayout;
        mRetryLayout.setVisibility(View.INVISIBLE);
        addPlaceholderLayout(mRetryLayout);
        setRetryWrapperListener();
    }

    private void setRetryWrapperListener() {
        mRetryLayout.setOnRetryClickListener(
                new PlaceholderRetryWrapperLayout.OnRetryClickListener() {
                    @Override
                    public void onRetryClicked() {
                        showLoading();
                        if (mOnRetryClickListener != null) {
                            mOnRetryClickListener.onRetryClicked();
                        }
                    }
                });
    }

    public void setOnRetryClickListener(@NonNull final OnRetryClickListener listener) {
        mOnRetryClickListener = listener;
    }

    public PlaceholderRetryWrapperLayout getRetryLayout() {
        return mRetryLayout;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Placeholder
    // /////////////////////////////////////////////////////////////////////////

    private void addPlaceholderLayout(@NonNull View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(view.getLayoutParams());
        params.gravity = Gravity.CENTER;
        view.setLayoutParams(params);
        addView(view);
    }

    public void showContent() {
        if (mCurrentState == State.CONTENT) {
            return;
        }
        mCurrentState = State.CONTENT;

        //we set to invisible not gone because placeholder should have the same size as largest
        // view.
        mContentView.setVisibility(View.VISIBLE);
        setLoadingVisibility(View.INVISIBLE);
        setNoDataVisibility(View.INVISIBLE);
        setRetryVisibility(View.INVISIBLE);

        handlePlaceholderStateChanged(State.CONTENT);
    }

    public void showLoading() {
        if (mCurrentState == State.LOADING) {
            return;
        }
        mCurrentState = State.LOADING;

        //we set to invisible not gone because placeholder should have the same size as largest
        // view.
        mContentView.setVisibility(View.INVISIBLE);
        setLoadingVisibility(View.VISIBLE);
        setNoDataVisibility(View.INVISIBLE);
        setRetryVisibility(View.INVISIBLE);

        handlePlaceholderStateChanged(State.LOADING);
    }

    public void showNoData() {
        if (mCurrentState == State.NO_DATA) {
            return;
        }
        mCurrentState = State.NO_DATA;

        //we set to invisible not gone because placeholder should have the same size as largest
        // view.
        mContentView.setVisibility(View.INVISIBLE);
        setLoadingVisibility(View.INVISIBLE);
        setNoDataVisibility(View.VISIBLE);
        setRetryVisibility(View.INVISIBLE);

        handlePlaceholderStateChanged(State.NO_DATA);
    }

    public void showRetry() {
        if (mCurrentState == State.RETRY) {
            return;
        }
        mCurrentState = State.RETRY;

        //we set to invisible not gone because placeholder should have the same size as largest
        // view.
        mContentView.setVisibility(View.INVISIBLE);
        setLoadingVisibility(View.INVISIBLE);
        setNoDataVisibility(View.INVISIBLE);
        setRetryVisibility(View.VISIBLE);

        handlePlaceholderStateChanged(State.RETRY);
    }

    private void setLoadingVisibility(int visibility) {
        if (mLoadingLayout == null && visibility == View.VISIBLE) {
            inflateDefaultLoadingPlaceholder();
        }
        if (mLoadingLayout != null) {
            mLoadingLayout.setVisibility(visibility);
        }
    }

    private void setNoDataVisibility(int visibility) {
        if (mNoDataLayout == null && visibility == View.VISIBLE) {
            inflateDefaultNoDataPlaceholder();
        }
        if (mNoDataLayout != null) {
            mNoDataLayout.setVisibility(visibility);
        }
    }

    private void setRetryVisibility(int visibility) {
        if (mRetryLayout == null && visibility == View.VISIBLE) {
            inflateDefaultRetryPlaceholder();
        }
        if (mRetryLayout != null) {
            mRetryLayout.setVisibility(visibility);
        }
    }

    private void inflateDefaultLoadingPlaceholder() {
        logInfo("Inflating default loading placeholder.");
        mLoadingLayout = (PlaceholderLoadingWrapperLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.placeholder_default_loading, this, false);
        LayoutParams params = (LayoutParams) mLoadingLayout.getLayoutParams();
        // params.addRule(CENTER_IN_PARENT);
        params.gravity = Gravity.CENTER;
        mLoadingLayout.setVisibility(View.INVISIBLE);
        addView(mLoadingLayout, params);
    }

    private void inflateDefaultNoDataPlaceholder() {
        logInfo("Inflating default no data placeholder.");
        mNoDataLayout = (PlaceholderNoDataWrapperLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.placeholder_default_no_data, this, false);
        LayoutParams params = (LayoutParams) mNoDataLayout.getLayoutParams();
        // params.addRule(CENTER_IN_PARENT);
        params.gravity = Gravity.CENTER;
        mNoDataLayout.setVisibility(View.INVISIBLE);
        addView(mNoDataLayout, params);
    }

    private void inflateDefaultRetryPlaceholder() {
        logInfo("Inflating default retry placeholder.");
        mRetryLayout = (PlaceholderRetryWrapperLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.placeholder_default_retry, this, false);
        LayoutParams params = (LayoutParams) mRetryLayout.getLayoutParams();
        // params.addRule(CENTER_IN_PARENT);
        params.gravity = Gravity.CENTER;
        mRetryLayout.setVisibility(View.VISIBLE);
        addView(mRetryLayout);
        setRetryWrapperListener();
    }

    // /////////////////////////////////////////////////////////////////////////
    // State
    // /////////////////////////////////////////////////////////////////////////

    public boolean isContentShowing() {
        return mCurrentState == State.CONTENT;
    }

    @SuppressWarnings("unused")
    public boolean isLoadingPlaceholderShowing() {
        return mCurrentState == State.LOADING;
    }

    @SuppressWarnings("unused")
    public boolean isNoDataPlaceholderShowing() {
        return mCurrentState == State.NO_DATA;
    }

    @SuppressWarnings("unused")
    public boolean isRetryPlaceholderShowing() {
        return mCurrentState == State.RETRY;
    }

    private void handlePlaceholderStateChanged(State state) {
        if (mOnPlaceholderStateChangedListener != null) {
            mOnPlaceholderStateChangedListener.onPlaceholderStateChanged(state);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Data observer
    // /////////////////////////////////////////////////////////////////////////

    public void pauseObservingData() {
        mObservingData = false;
    }

    public void startObservingData() {
        mObservingData = true;
        onDataSetChanged();
    }

    public void setDataObserver(@NonNull PlaceholderDataObserver observer) {
        if (observer == null) {
            return;
        }
        mPlaceholderDataObserver = observer;
        onDataSetChanged();
    }

    public boolean isDataObservable() {
        return mPlaceholderDataObserver != null;
    }

    @Override
    public void onDataSetChanged() {
        if (!mObservingData) {
            return;
        }

        if (mPlaceholderDataObserver != null) {
            if (mPlaceholderDataObserver.isDataAvailable()) {
                showContent();
            } else {
                if (isContentShowing()) {
                    // show no data placeholder only if content is showing
                    showNoData();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    public void setOnPlaceholderStateChangedListener(
            final OnPlaceholderStateChangedListener listener) {
        mOnPlaceholderStateChangedListener = listener;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Log
    // /////////////////////////////////////////////////////////////////////////

    private void logInfo(String message) {
        SuperLog.info(getClass(), SuperLog.LogTag.PLACEHOLDER, message);
    }
}