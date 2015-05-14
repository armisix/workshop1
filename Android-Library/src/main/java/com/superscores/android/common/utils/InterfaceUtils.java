package com.superscores.android.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * This class help you attach interface to Activity, Fragment, or Class faster.
 * <p/>
 * Created by Pongpat on 2/11/15.
 */
public class InterfaceUtils {

    public static <T> boolean isImplemented(@NonNull Object implObject, Class<T> clazz) {
        return clazz.isInstance(implObject);
    }

    public static <T> T attachInterface(@NonNull Context context, @NonNull Object implObject,
            Class<T> clazz) {
        return attachInterface(context, implObject, clazz, true);
    }

    public static <T> T attachInterface(@NonNull Context context, @NonNull Object implObject,
            @NonNull Class<T> clazz, boolean isFetal) {

        if (context == null || implObject == null || clazz == null) {
            throw new ClassCastException("context, implObject, and clazz must not be null");
        }

        T implInterface = null;
        try {
            implInterface = clazz.cast(implObject);
        } catch (ClassCastException e) {
            if (isFetal) {
                throw new ClassCastException(implObject.getClass()
                        .getSimpleName() + " must implement " + clazz.getSimpleName());
            }
        }
        return implInterface;
    }

    public static <T> T attachInterface(@NonNull Context context, @NonNull Class<T> clazz) {
        return attachInterface(context, clazz, true);
    }

    public static <T> T attachInterface(@NonNull Context context, @NonNull Class<T> clazz,
            boolean isFetal) {
        return attachInterface(context, context, clazz, isFetal);
    }

    public static <T> T attachInterface(@NonNull Fragment fragment, @NonNull Class<T> clazz) {
        return attachInterface(fragment, clazz, true);
    }

    public static <T> T attachInterface(@NonNull Fragment fragment, @NonNull Class<T> clazz,
            boolean isFetal) {
        return attachInterface(fragment.getActivity()
                .getApplicationContext(), fragment, clazz, isFetal);
    }
}