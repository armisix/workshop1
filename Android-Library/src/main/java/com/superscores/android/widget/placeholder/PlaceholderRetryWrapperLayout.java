package com.superscores.android.widget.placeholder;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.superscores.android.R;

/**
 * Placeholder retry wrapper layout
 *
 * Created by Pongpat on 4/29/15.
 */
public class PlaceholderRetryWrapperLayout extends PlaceholderWrapperLayout {

    private static final int INVALID_ID = -1;

    private OnRetryClickListener mOnRetryClickListener;

    private int mRetryButtonId;
    private View mRetryButton;

    interface OnRetryClickListener {
        public void onRetryClicked();
    }

    public PlaceholderRetryWrapperLayout(@NonNull Context context, @NonNull View placeholder) {
        super(context, placeholder);
    }

    public PlaceholderRetryWrapperLayout(@NonNull Context context, @NonNull View placeholder,
            @IdRes int retryButtonId) {
        super(context, placeholder);
        mRetryButton = placeholder.findViewById(retryButtonId);
    }

    public PlaceholderRetryWrapperLayout(@NonNull Context context, @NonNull View placeholder,
            @NonNull String retryButtonTag) {
        super(context, placeholder);
        mRetryButton = placeholder.findViewWithTag(retryButtonTag);
    }

    public PlaceholderRetryWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public PlaceholderRetryWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public PlaceholderRetryWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.PlaceholderWrapperLayout, 0, 0);
        try {
            mRetryButtonId = ta.getResourceId(R.styleable.PlaceholderWrapperLayout_plhRetryButton,
                    INVALID_ID);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mRetryButtonId == INVALID_ID) {
            mRetryButton = getWrappedView();
        } else {
            mRetryButton = getWrappedView().findViewById(mRetryButtonId);
        }
    }

    void setOnRetryClickListener(@NonNull final OnRetryClickListener listener) {
        mOnRetryClickListener = listener;
        mRetryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryClickListener != null) {
                    mOnRetryClickListener.onRetryClicked();
                }
            }
        });
    }

    //void setOnRetryClickListener(@NonNull final OnRetryClickListener listener) {
    //    setOnRetryClickListener(listener, mRetryButton);
    //}
    //
    //void setOnRetryClickListener(@NonNull final OnRetryClickListener listener,
    //        @IdRes int retryButtonId) {
    //    View retryButton = getWrappedView().findViewById(retryButtonId);
    //    setOnRetryClickListener(listener, retryButton);
    //}
    //
    //void setOnRetryClickListener(@NonNull final OnRetryClickListener listener,
    //        String retryButtonTag) {
    //    View retryButton = getWrappedView().findViewWithTag(retryButtonTag);
    //    setOnRetryClickListener(listener, retryButton);
    //}
}