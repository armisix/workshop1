package com.superscores.android.common.utils;

import java.util.Map;

import android.net.Uri;

public class HttpUtils {

    public static String generateGetParams(Map<String, String> params) {
        String result = "";
        if (params == null || params.size() == 0) {
            return result;
        }

        Uri.Builder uriBuilder = new Uri.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key != null && value != null) {
                uriBuilder.appendQueryParameter(key, value);
            }
        }

        Uri uri = uriBuilder.build();
        return uri.toString();
    }
}