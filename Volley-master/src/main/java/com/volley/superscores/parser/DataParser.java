package com.volley.superscores.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by Pongpat on 3/13/15.
 */
public abstract class DataParser<D> implements Parser<D, Void> {

    @Override
    public Void parseError(@NonNull Context appContext,@NonNull String response, int httpStatus,
            @NonNull   Map<String, String> headers) {
        return null;
    }
}