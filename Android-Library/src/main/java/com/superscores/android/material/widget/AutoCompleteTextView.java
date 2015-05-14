package com.superscores.android.material.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class AutoCompleteTextView extends android.widget.AutoCompleteTextView {

    public AutoCompleteTextView(Context context) {
        super(context);
        setBackgroundCompat();
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundCompat();
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundCompat();
    }

    @SuppressLint("NewApi")
    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBackgroundCompat();
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(EditTextBackgroundCompat.getBackgroundCompat(getContext()));
            } else {
                setBackground(EditTextBackgroundCompat.getBackgroundCompat(getContext()));
            }
        }
    }
}