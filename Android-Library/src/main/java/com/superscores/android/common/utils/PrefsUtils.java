package com.superscores.android.common.utils;

import android.content.Context;

import com.superscores.android.BuildConfig;

/**
 * Created by Pongpat on 2/14/15.
 */
public class PrefsUtils {

    private static final String PREF_NAME = BuildConfig.APPLICATION_ID + "_SuperScoresLibrary";

    private static final String DRAWER_ACTIVELY_OPEN = "drawer_actively_open";


    // /////////////////////////////////////////////////////////////////////////
    // Drawer
    // /////////////////////////////////////////////////////////////////////////

    public static boolean isDrawerActivelyOpened(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(DRAWER_ACTIVELY_OPEN, false);
    }

    public static void saveDrawerActivelyOpened(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(DRAWER_ACTIVELY_OPEN, true)
                .apply();
    }
}