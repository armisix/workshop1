package com.superscores.android.material.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

public class EditTextCompat extends android.widget.EditText {

    public EditTextCompat(Context context) {
        super(context);
        setBackgroundCompat();
    }

    public EditTextCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundCompat();
    }

    public EditTextCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundCompat();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditTextCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setBackgroundCompat();
    }

    @TargetApi(16)
    @SuppressWarnings("deprecation")
    private void setBackgroundCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= 16) {
                setBackground(EditTextBackgroundCompat.getBackgroundCompat(getContext()));
            } else {
                setBackgroundDrawable(EditTextBackgroundCompat.getBackgroundCompat(getContext()));
            }
        }
    }
}