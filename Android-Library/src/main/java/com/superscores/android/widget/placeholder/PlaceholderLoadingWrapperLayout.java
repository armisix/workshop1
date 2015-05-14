package com.superscores.android.widget.placeholder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Placeholder loading wrapper layout
 *
 * Created by Pongpat on 4/29/15.
 */
public class PlaceholderLoadingWrapperLayout extends PlaceholderWrapperLayout {

    public PlaceholderLoadingWrapperLayout(Context context, View placeholder) {
        super(context, placeholder);
    }

    public PlaceholderLoadingWrapperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceholderLoadingWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PlaceholderLoadingWrapperLayout(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}