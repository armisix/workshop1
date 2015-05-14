package com.superscores.android.material.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;

import com.superscores.android.R;
import com.superscores.android.common.utils.ColorUtils;

public class SpinnerBackgroundCompat {

    public static Drawable getBackgroundCompat(Context context) {
        int inset = context.getResources()
                .getDimensionPixelSize(R.dimen.abc_control_inset_material);
        Drawable stateDrawable = new BackgroundStateDrawable(context);
        return new InsetDrawable(stateDrawable, inset);
    }

    private static class BackgroundStateDrawable extends StateListDrawableCompat {

        private static final int[] STATE_1 = {-android.R.attr.state_enabled};

        private static final int[] STATE_2 = {android.R.attr.state_pressed};

        private static final int[] STATE_3 = {
                -android.R.attr.state_pressed,
                android.R.attr.state_focused};

        private static final int[] STATE_4 = {};

        @TargetApi(19)
        private BackgroundStateDrawable(Context context) {
            super(context);

            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.drawable.spinner_mtrl_am_alpha);

            if (Build.VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(true);
            }

            addState(STATE_1, drawable);
            addState(STATE_2, drawable);
            addState(STATE_3, drawable);
            addState(STATE_4, drawable);
        }

        @Override
        protected ColorStateList initTint(Context context) {

            int[] colors = ColorUtils.getColors(context, ColorUtils.ColorType.ACCENT,
                    ColorUtils.ColorType.CONTROL_NORMAL, ColorUtils.ColorType.CONTROL_DISABLED);

            int colorAccent = colors[0];
            int colorControlNormal = colors[1];
            int colorControlDisabled = colors[2];

            ColorStateList tint = new ColorStateList(new int[][]{
                    STATE_1,
                    STATE_2,
                    STATE_3,
                    STATE_4}, new int[]{
                    colorControlDisabled,
                    colorAccent,
                    colorAccent,
                    colorControlNormal});
            return tint;
        }
    }
}