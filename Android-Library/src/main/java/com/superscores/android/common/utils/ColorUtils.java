package com.superscores.android.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

import com.superscores.android.R;

@SuppressWarnings("unused")
public class ColorUtils {

    public enum ColorType {
        PRIMARY(R.attr.colorPrimary),
        PRIMARY_DARK(R.attr.colorPrimaryDark),
        ACCENT(R.attr.colorAccent),
        ACCENT_LIGHTEN(R.attr.colorAccentLighten),
        ACCENT_DARKEN(R.attr.colorAccentDarken),
        ACCENT_RIPPLE(R.attr.colorAccentRipple),
        CONTROL_NORMAL(R.attr.colorControlNormal),
        CONTROL_ACTIVATED(R.attr.colorControlActivated),
        CONTROL_HIGHLIGHT(R.attr.colorControlHighlight),
        CONTROL_DISABLED(R.attr.colorControlDisabled),
        TEXT_PRIMARY(R.attr.colorTextPrimary),
        TEXT_SECONDARY(R.attr.colorTextSecondary),
        TEXT_HINT(R.attr.colorHintText),
        RIPPLE(R.attr.colorRipple),
        DIVIDER(R.attr.colorDivider);

        private final int mAttrId;

        private ColorType(int attrId) {
            mAttrId = attrId;
        }
    }

    /**
     * Get array of color
     *
     * @param context    context
     * @param colorType  {@link com.superscores.android.common.utils.ColorUtils.ColorType} to
     *                   retrieve color from.
     * @param colorTypes array of {@link com.superscores.android.common.utils.ColorUtils.ColorType}
     *                   to retrieve colors from.
     * @return array of color
     */
    public static int[] getColors(@NonNull Context context, @NonNull ColorType colorType,
            @NonNull ColorType... colorTypes) {

        if (colorTypes == null || colorTypes.length == 0) {
            return new int[]{getColor(context, colorType.mAttrId)};
        }

        int[] attrs = new int[colorTypes.length + 1];
        int[] colors = new int[colorTypes.length + 1];

        attrs[0] = colorType.mAttrId;
        for (int i = 0; i < colorTypes.length; i++) {
            attrs[i + 1] = colorTypes[i].mAttrId;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        return colors;
    }

    public static int getColor(@NonNull Context context, @NonNull ColorType colorType) {
        return getColor(context, colorType.mAttrId);
    }

    public static int getColor(@NonNull Context context, @AttrRes int attrId) {
        return getColor(context, attrId, 0xFF000000);
    }

    public static int getColor(@NonNull Context context, @AttrRes int attrId, int defaultColor) {
        int attrs[] = new int[]{attrId};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getColor(0, defaultColor);
        ta.recycle();
        return color;
    }

    public static int applyAlpha(float alpha, int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int resultAlpha = (int) (alpha * a);
        return Color.argb(resultAlpha, r, g, b);
    }

    public static String toHexString(int color) {
        return toHexString(color, false);
    }

    public static String toHexString(int color, boolean includeAlpha) {
        if (includeAlpha) {
            return String.format("#%08X", (color));
        } else {
            return String.format("#%06X", (0xFFFFFF & color));
        }
    }
}