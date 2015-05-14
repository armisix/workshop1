package com.superscores.android.common.utils;

import java.util.Collection;

/**
 * Created by Pongpat on 4/16/15.
 */
public class CollectionsUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }
}
