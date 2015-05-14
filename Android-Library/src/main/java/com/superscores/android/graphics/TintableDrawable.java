package com.superscores.android.graphics;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class TintableDrawable extends StateListDrawable {

    private static final int[] STATE_1 = {
            -android.R.attr.state_window_focused,
            android.R.attr.state_enabled};

    private static final int[] STATE_2 = {
            -android.R.attr.state_window_focused,
            -android.R.attr.state_enabled};

    private static final int[] STATE_3 = {android.R.attr.state_pressed};

    private static final int[] STATE_4 = {
            android.R.attr.state_focused,
            android.R.attr.state_enabled};

    private static final int[] STATE_5 = {android.R.attr.state_enabled};

    private static final int[] STATE_6 = {android.R.attr.state_focused};

    private static final int[] STATE_7 = {};

    private ColorStateList mTint;

    public TintableDrawable(Drawable drawable) {
        initDrawable(drawable);
    }

    public TintableDrawable(Drawable drawable, int color) {
        initDrawable(drawable);
        updateColorState(color, color, color);
    }

    public TintableDrawable(Drawable drawable, int colorNormal, int colorDisabled,
            int colorActivated) {
        initDrawable(drawable);
        updateColorState(colorNormal, colorDisabled, colorActivated);
    }

    private void initDrawable(Drawable drawable) {
        addState(TintableDrawable.STATE_1, drawable);
        addState(TintableDrawable.STATE_2, drawable);
        addState(TintableDrawable.STATE_3, drawable);
        addState(TintableDrawable.STATE_4, drawable);
        addState(TintableDrawable.STATE_5, drawable);
        addState(TintableDrawable.STATE_6, drawable);
        addState(TintableDrawable.STATE_7, drawable);
    }

    public void setTintColor(int colorNormal, int colorDisabled, int colorActivated) {
        updateColorState(colorNormal, colorDisabled, colorActivated);
    }

    public void disableTint() {
        mTint = null;
        setColorFilter(null);
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        if (mTint != null && mTint.isStateful()) {
            updateColorFilter(stateSet);
        }
        return super.onStateChange(stateSet);
    }

    private void updateColorState(int colorNormal, int colorDisabled, int colorActivated) {
        mTint = new ColorStateList(new int[][]{
                STATE_1,
                STATE_2,
                STATE_3,
                STATE_4,
                STATE_5,
                STATE_6,
                STATE_7}, new int[]{
                colorNormal,
                colorDisabled,
                colorActivated,
                colorActivated,
                colorNormal,
                colorDisabled,
                colorDisabled});

        updateColorFilter(getState());
    }

    private void updateColorFilter(int[] stateSet) {
        int color = mTint.getColorForState(stateSet, 0);
        setColorFilter(color, Mode.SRC_IN);
    }
}