package com.superscores.android.common.utils;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Utilities class to help activity transition.
 *
 * Created by Pongpat on 3/30/15.
 */
public class ActivityTransitionUtils {

    public static void scheduleStartPostponedTransition(final Activity activity,
            final View sharedElement) {

        ViewTreeObserver observer = sharedElement.getViewTreeObserver();
        if (observer != null && observer.isAlive()) {
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    sharedElement.getViewTreeObserver()
                            .removeOnPreDrawListener(this);
                    ActivityCompat.startPostponedEnterTransition(activity);
                    return true;
                }
            });
        }
    }
}