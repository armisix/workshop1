package com.superscores.android.common.utils;

import android.util.Log;

/**
 * Created by Pongpat on 2/13/15.
 */

public class SuperLog {

    public static boolean sLog = false;
    public static boolean sLogInfo = false;
    public static boolean sLogDebug = false;
    public static boolean sLogWarning = false;
    public static boolean sLogError = false;

    public enum LogTag {
        LOADER("Loader"),
        DRAWER("Drawer"),
        PLACEHOLDER("Placeholder"),
        GENERAL("SuperScores");

        private String mTag;

        private LogTag(String tag) {
            this.mTag = tag;
        }

        public String getTag() {
            return mTag;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Turn log on/off
    // /////////////////////////////////////////////////////////////////////////

    public static boolean shouldLog() {
        return sLog;
    }

    public static void turnOnAllLog() {
        setAllLog(true);
    }

    public static void turnOffAllLog() {
        setAllLog(false);
    }

    private static void setAllLog(boolean flag) {
        sLog = flag;
        sLogInfo = flag;
        sLogDebug = flag;
        sLogWarning = flag;
        sLogError = flag;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Info
    // /////////////////////////////////////////////////////////////////////////

    public static void info(Class<?> clazz, LogTag logTag, String message) {
        info(getTag(clazz, logTag), message);
    }

    public static void info(LogTag logTag, String message) {
        info(logTag.getTag(), message);
    }

    private static void info(String tag, String message) {
        if (sLogInfo) {
            Log.i(tag, message);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Debug
    // /////////////////////////////////////////////////////////////////////////

    public static void debug(Class<?> clazz, LogTag logTag, String message) {
        debug(getTag(clazz, logTag), message);
    }

    public static void debug(LogTag logTag, String message) {
        debug(logTag.getTag(), message);
    }

    private static void debug(String tag, String message) {
        if (sLogDebug) {
            Log.d(tag, message);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Warning
    // /////////////////////////////////////////////////////////////////////////

    public static void warning(Class<?> clazz, LogTag logTag, String message) {
        warning(getTag(clazz, logTag), message);
    }

    public static void warning(LogTag logTag, String message) {
        warning(logTag.getTag(), message);
    }

    private static void warning(String tag, String message) {
        if (sLogWarning) {
            Log.w(tag, message);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Error
    // /////////////////////////////////////////////////////////////////////////

    public static void error(Class<?> clazz, LogTag logTag, String message) {
        error(getTag(clazz, logTag), message);
    }

    public static void error(LogTag logTag, String message) {
        error(logTag.getTag(), message);
    }

    private static void error(String tag, String message) {
        if (sLogError) {
            Log.e(tag, message);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Tag
    // /////////////////////////////////////////////////////////////////////////

    private static String getTag(Class<?> clazz, LogTag logTag) {
        if (logTag == null) {
            return (clazz == null) ? "" : clazz.getSimpleName();
        } else {
            return logTag.getTag() + ((clazz == null) ? "" : "_" + clazz.getSimpleName());
        }
    }
}
