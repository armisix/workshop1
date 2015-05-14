package com.superscores.android.common.utils;

import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.superscores.android.R;

public class ScreenSizeUtils {

    public static boolean isLargeHorizontalSpace(Context context) {
        boolean isTablet = context.getResources()
                .getBoolean(R.bool.tablet);

        if (isTablet) {
            return true;
        }

       Display display = ((WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            return true;
        } else {
            return false;
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources()
                .getDisplayMetrics().widthPixels;
    }
}