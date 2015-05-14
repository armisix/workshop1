package com.volley.superscores.parser;

import android.content.Context;

import java.util.Map;

/**
 * Created by Pongpat on 3/11/15.
 */
public class SimpleParser implements Parser<Void, Void> {
    @Override
    public Void parse(Context appContext, String response, int httpStatus,
            Map<String, String> headers) {
        return null;
    }

    @Override
    public Void parseError(Context appContext, String response, int httpStatus,
            Map<String, String> headers) {
        return null;
    }
}