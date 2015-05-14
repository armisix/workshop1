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

public class EditTextBackgroundCompat {

    public static Drawable getBackgroundCompat(Context context) {
        int inset = context.getResources()
                .getDimensionPixelSize(R.dimen.abc_control_inset_material);
        Drawable stateDrawable = new BackgroundStateDrawable(context);
        return new InsetDrawable(stateDrawable, inset);
    }

    private static class BackgroundStateDrawable extends StateListDrawableCompat {

        private static final int[] STATE_1 = {
                -android.R.attr.state_window_focused,
                android.R.attr.state_enabled};

        private static final int[] STATE_2 = {
                -android.R.attr.state_window_focused,
                -android.R.attr.state_enabled};

        private static final int[] STATE_3 = {
                android.R.attr.state_enabled,
                android.R.attr.state_focused};

        private static final int[] STATE_4 = {android.R.attr.state_enabled};

        private static final int[] STATE_5 = {android.R.attr.state_focused};

        private static final int[] STATE_6 = {};

        @TargetApi(19)
        private BackgroundStateDrawable(Context context) {
            super(context);

            Resources res = context.getResources();
            Drawable drawableDefault = res.getDrawable(R.drawable.textfield_default_mtrl_alpha);
            Drawable drawableActivated = res.getDrawable(R.drawable.textfield_activated_mtrl_alpha);

            if (Build.VERSION.SDK_INT >= 19) {
                drawableDefault.setAutoMirrored(true);
                drawableActivated.setAutoMirrored(true);
            }

            addState(STATE_1, drawableDefault);
            addState(STATE_2, drawableDefault);
            addState(STATE_3, drawableActivated);
            addState(STATE_4, drawableDefault);
            addState(STATE_5, drawableActivated);
            addState(STATE_6, drawableDefault);
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
                    STATE_4,
                    STATE_5,
                    STATE_6}, new int[]{
                    colorControlNormal,
                    colorControlDisabled,
                    colorAccent,
                    colorControlNormal,
                    colorAccent,
                    colorControlDisabled});
            return tint;
        }
    }
}