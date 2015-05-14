package com.superscores.android.material.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.StateListDrawable;

public abstract class StateListDrawableCompat extends StateListDrawable {

    private ColorStateList mTint;

    protected abstract ColorStateList initTint(Context context);

    protected StateListDrawableCompat(Context context) {
        mTint = initTint(context);
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        if (mTint != null && mTint.isStateful()) {
            updateTint();
        }
        return super.onStateChange(stateSet);
    }

    private void updateTint() {
        setColorFilter(mTint.getColorForState(getState(), 0), Mode.SRC_IN);
    }
}