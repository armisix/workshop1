package com.superscores.android.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.superscores.android.R;

public class DrawableCompat {

    // /////////////////////////////////////////////////////////////////////////
    // Set background
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public static void setBackgroundFromAttr(@NonNull View view, @AttrRes int attrId) {
        setBackgroundFromAttr(view, attrId, null);
    }

    public static void setBackgroundFromAttr(@NonNull View view, @AttrRes int attrId, Theme theme) {
        setBackground(view, getDrawableFromAttr(view.getContext(), attrId, theme));
    }

    @SuppressWarnings("unused")
    public static void setBackground(@NonNull View view, @DrawableRes int drawableId) {
        setBackground(view, drawableId, null);
    }

    public static void setBackground(@NonNull View view, @DrawableRes int drawableId, Theme theme) {
        setBackground(view, getDrawable(view.getContext(), drawableId, theme));
    }

    @SuppressWarnings("deprecation")
    @TargetApi(16)
    public static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Get drawable
    // /////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
        return getDrawable(context, drawableId, null);
    }

    @TargetApi(21)
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId,
            Theme theme) {

        if (theme != null && Build.VERSION.SDK_INT >= 21) {
            return context.getResources()
                    .getDrawable(drawableId, theme);
        } else {
            return ContextCompat.getDrawable(context, drawableId);
        }
    }

    public static Drawable getDrawableFromAttr(@NonNull Context context, @AttrRes int attrId) {
        return getDrawableFromAttr(context, attrId, null);
    }

    public static Drawable getDrawableFromAttr(@NonNull Context context, @AttrRes int attrId,
            Theme theme) {
        int attrs[] = new int[]{attrId};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int drawableId = ta.getResourceId(0, R.drawable.item_background_material_compat_light);
        ta.recycle();
        return getDrawable(context, drawableId, theme);
    }
}