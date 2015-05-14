/**
 *
 */
package com.volley.superscores.parser;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * @author Pongpat Ratanaamornpin
 */
public interface Parser<D, E> {
    public D parse(@NonNull Context appContext, @NonNull String response, int httpStatus,
            @NonNull Map<String, String> headers);

    public E parseError(@NonNull Context appContext, @NonNull String response, int httpStatus,
            @NonNull Map<String, String> headers);
}