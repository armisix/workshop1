package com.superscores.android.material.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class SpinnerCompat extends android.widget.Spinner {

    public SpinnerCompat(Context context) {
        super(context);
        setBackgroundCompat();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SpinnerCompat(Context context, int mode) {
        super(context, mode);
        setBackgroundCompat();
    }

    public SpinnerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundCompat();
    }

    public SpinnerCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundCompat();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SpinnerCompat(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setBackgroundCompat();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpinnerCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes,
                         int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
        setBackgroundCompat();
    }

    @TargetApi(16)
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= 16) {
                setBackground(SpinnerBackgroundCompat.getBackgroundCompat(getContext()));
            } else {
                setBackgroundDrawable(SpinnerBackgroundCompat.getBackgroundCompat(getContext()));
            }
        }
    }
}