package com.superscores.android.common.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Pongpat on 2/11/15.
 */
public class FragmentUtils {

    @SuppressWarnings("unchecked")
    public static <T> T findFragmentByTag(FragmentManager fm, String tag, Class<T> clazz) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (clazz.isInstance(fragment)) {
            return (T) fragment;
        }
        return null;
    }

    public static void dispatchOnActivityResult(FragmentManager fragmentManager, int requestCode,
            int resultCode, Intent data) {
        // XXX fix bug fragment do not pass onActivityResult() to child fragment
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}