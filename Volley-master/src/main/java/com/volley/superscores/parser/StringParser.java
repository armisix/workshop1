package com.volley.superscores.parser;

import android.content.Context;

import java.util.Map;

/**
 * Created by Pongpat on 3/11/15.
 */
public class StringParser implements Parser<String, String> {

    @Override
    public String parse(Context appContext, String response, int httpStatus,
            Map<String, String> headers) {
        return response;
    }

    @Override
    public String parseError(Context appContext, String response, int httpStatus,
            Map<String, String> headers) {
        return response;
    }
}