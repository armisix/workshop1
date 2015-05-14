package com.superscores.android.common.utils;

import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;

/**
 * Created by Pongpat on 2/25/15.
 */
public class AnnotationUtils {

    @Nullable
    public static Annotation[] getEnumAnnotations(Enum<?> enumType) {
        try {
            return enumType.getClass()
                    .getField(((Enum) enumType).name())
                    .getAnnotations();

        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Nullable
    public static <T extends Annotation> T getEnumAnnotation(Enum<?> enumType, Class<T> clazz) {
        try {
            return enumType.getClass()
                    .getField(((Enum) enumType).name())
                    .getAnnotation(clazz);

        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}