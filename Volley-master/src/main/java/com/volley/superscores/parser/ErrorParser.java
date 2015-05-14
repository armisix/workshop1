package com.volley.superscores.parser;

import android.content.Context;

import java.util.Map;

/**
 * Created by Pongpat on 3/13/15.
 */
public abstract class ErrorParser<E> implements Parser<Void, E> {

    @Override
    public Void parse(Context appContext, String response, int httpStatus,
            Map<String, String> headers) {
        return null;
    }
}