package com.superscores.android.common.utils;

/**
 * Created by Pongpat on 4/1/15.
 */
public class ThreadUtils {

    public static void sleep(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}