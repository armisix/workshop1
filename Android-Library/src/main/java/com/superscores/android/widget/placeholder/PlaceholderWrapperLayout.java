package com.superscores.android.widget.placeholder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Placeholder wrapper layout
 *
 * Created by Pongpat on 4/28/15.
 */
public abstract class PlaceholderWrapperLayout extends FrameLayout {

    private View mPlaceholderView;

    public PlaceholderWrapperLayout(Context context, View placeholder) {
        super(context);
        mPlaceholderView = placeholder;
        initLayoutParams(placeholder);
    }

    public PlaceholderWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceholderWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaceholderWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 1) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " must have exactly one child.");
        }
        mPlaceholderView = getChildAt(0);
    }

    private void initLayoutParams(@NonNull View placeholder) {
        setLayoutParams(new LayoutParams(placeholder.getLayoutParams()));
    }
    
    protected View getWrappedView() {
        return mPlaceholderView;
    }
}