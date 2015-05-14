package com.volley.superscores;

import android.util.Log;

import com.volley.Request;

import java.util.Map;

/**
 * Created by Pongpat on 2/13/15.
 */

public class SuperScoresLog {

    public static String TAG = "Loader_Volley";

    private static final boolean LOG_RESPONSE_HEADER = false;
    private static final boolean LOG_RAW_RESPONSE = false;

    public static boolean sLogInfo = true;
    public static boolean sLogDebug = true;
    public static boolean sLogWarning = true;
    public static boolean sLogError = true;
    public static boolean sLogWtf = true;

    public static void request(int method, Map<String, String> headers, String url) {

        if (!sLogInfo) {
            return;
        }

        String logText;
        if (method == Request.Method.DEPRECATED_GET_OR_POST) {
            logText = "DEPRECATED_GET_OR_POST";
        } else if (method == Request.Method.GET) {
            logText = "GET";
        } else if (method == Request.Method.POST) {
            logText = "POST";
        } else if (method == Request.Method.PUT) {
            logText = "PUT";
        } else if (method == Request.Method.DELETE) {
            logText = "DELETE";
        } else if (method == Request.Method.HEAD) {
            logText = "HEAD";
        } else if (method == Request.Method.OPTIONS) {
            logText = "OPTIONS";
        } else if (method == Request.Method.TRACE) {
            logText = "TRACE";
        } else if (method == Request.Method.PATCH) {
            logText = "PATCH";
        } else {
            logText = "[UNKNOWN METHOD (" + method + ")]";
        }

        logText += " " + url + "\n";
        logText += serializeMap(headers);

        info(logText);
    }

    public static void response(int httpStatus, boolean notModified, String url,
            Map<String, String> headers, String rawResponse) {

        if (!sLogInfo) {
            return;
        }

        String logText = "[" + httpStatus;
        if (notModified) {
            logText += ", NOT MODIFIED]";
        } else {
            logText += "]";
        }
        logText += " " + url + "\n";
        if (LOG_RESPONSE_HEADER) {
            logText += serializeMap(headers) + "\n";
        }
        if (LOG_RAW_RESPONSE) {
            logText += rawResponse;
        }

        info(logText);
    }

    private static String serializeMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        String serialized = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            serialized += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return serialized;
    }

    public static void info(String message) {
        if (sLogInfo) {
            Log.i(TAG, message);
        }
    }

    private static void debug(String message) {
        if (sLogDebug) {
            Log.d(TAG, message);
        }
    }

    private static void warning(String message) {
        if (sLogWarning) {
            Log.w(TAG, message);
        }
    }

    private static void error(String message) {
        if (sLogError) {
            Log.e(TAG, message);
        }
    }

    private static void wtf(String message) {
        if (sLogWtf) {
            Log.wtf(TAG, message);
        }
    }
}