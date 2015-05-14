package com.volley.superscores;

import android.content.Context;

/**
 * Created by Pongpat on 3/12/15.
 */
public class VolleyPrefUtils {

    private static final String PREF_NAME = "com.volley.superscores";
    private static final String LAST_REQUEST_TIMESTAMP = "lastRequestTimestamp";

    public static long getLastRequestTimestamp(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getLong(LAST_REQUEST_TIMESTAMP, 0);
    }

    public static void saveLastRequestTimestamp(Context context, long timestamp) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putLong(LAST_REQUEST_TIMESTAMP, timestamp)
                .apply();
    }
}