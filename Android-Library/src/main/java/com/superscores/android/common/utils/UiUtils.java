package com.superscores.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class UiUtils {

    public static String getScreenDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        String density = "unknown";
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                density = "low-density";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = "medium-density";
                break;
            case DisplayMetrics.DENSITY_TV:
                density = "tv-density";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = "high-density";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = "extra-high-density";
                break;
            case DisplayMetrics.DENSITY_400:
                density = "400-density";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = "extra-extra-high-density";
                break;
            case DisplayMetrics.DENSITY_560:
                density = "560-density";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = "extra-extra-extra-high-density";
                break;
        }
        return density;
    }

    public static String getScreenSize(Context context) {
        int screenSizeFlag = (context.getResources()
                .getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        String screenSize = "unknown";
        switch (screenSizeFlag) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                screenSize = "small";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                screenSize = "normal";
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                screenSize = "large";
                break;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                screenSize = "xlarge";
                break;
        }
        return screenSize;
    }

    public static Typeface getTypeface(Context context, String fontAssetPath) {
        return Typeface.createFromAsset(context.getAssets(), fontAssetPath);
    }

    /**
     * This method converts dp unit to equivalent device specific value in pixels.
     *
     * @param context Context to get resources and device specific display metrics
     * @param dp      A value in dp(Device independent pixels) unit. Which we need to convert into
     *                pixels
     * @return A int value to represent Pixels equivalent to dp according to device
     */
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources()
                .getDisplayMetrics().density;
        return (int) Math.ceil(dp * density);
    }

    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param context Context to get resources and device specific display metrics
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent db equivalent to px value
     */
    public static float pxToDp(Context context, float px) {
        float density = context.getResources()
                .getDisplayMetrics().density;
        return px / density;

    }

    public static int spToPx(Context context, float sp) {
        float scaledDensity = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) Math.ceil(sp * scaledDensity);
    }

    public static float pxToSp(Context context, float px) {
        float scaledDensity = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
}