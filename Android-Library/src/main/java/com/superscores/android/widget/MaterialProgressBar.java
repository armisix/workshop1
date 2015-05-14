package com.superscores.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.superscores.android.common.utils.ColorUtils;

public class MaterialProgressBar extends ProgressBar {

    private MaterialProgressDrawable mProgress;

    public MaterialProgressBar(Context context) {
        super(context);
        init();
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        init();
    }

    @SuppressLint("NewApi")
    private void init() {
        setIndeterminate(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mProgress = new MaterialProgressDrawable(getContext(), this);
            mProgress.setColorSchemeColors(
                    ColorUtils.getColor(getContext(), ColorUtils.ColorType.ACCENT));
            mProgress.start();
            setIndeterminateDrawable(mProgress);
        }
    }

    public void setColorSchemeColors(int... colors) {
        mProgress.setColorSchemeColors(colors);
    }
}