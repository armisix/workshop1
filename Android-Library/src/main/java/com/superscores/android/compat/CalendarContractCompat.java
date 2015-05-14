package com.superscores.android.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.provider.CalendarContract;

@TargetApi(14)
public class CalendarContractCompat {

    public static final String EXTRA_EVENT_BEGIN_TIME;
    public static final String EXTRA_EVENT_END_TIME;
    public static final String EXTRA_EVENT_ALL_DAY;

    static {
        if (Build.VERSION.SDK_INT >= 14) {
            EXTRA_EVENT_BEGIN_TIME = CalendarContract.EXTRA_EVENT_BEGIN_TIME;
            EXTRA_EVENT_END_TIME = CalendarContract.EXTRA_EVENT_END_TIME;
            EXTRA_EVENT_ALL_DAY = CalendarContract.EXTRA_EVENT_ALL_DAY;
        } else {
            EXTRA_EVENT_BEGIN_TIME = "beginTime";
            EXTRA_EVENT_END_TIME = "endTime";
            EXTRA_EVENT_ALL_DAY = "allDay";
        }
    }
}