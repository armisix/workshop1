package com.superscores.android.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.provider.CalendarContract.Events;

@TargetApi(14)
public class EventsCompat {

    public static final String TITLE;
    public static final String DESCRIPTION;

    static {
        if (Build.VERSION.SDK_INT >= 14) {
            TITLE = Events.TITLE;
            DESCRIPTION = Events.DESCRIPTION;
        } else {
            TITLE = "title";
            DESCRIPTION = "description";
        }
    }

}
