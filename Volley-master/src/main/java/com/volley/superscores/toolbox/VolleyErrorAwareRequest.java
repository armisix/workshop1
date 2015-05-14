package com.volley.superscores.toolbox;

import android.content.Context;

import com.volley.superscores.SuperScoresRequest;
import com.volley.superscores.Response;
import com.volley.superscores.parser.Parser;

import java.util.Map;

/**
 * Created by Pongpat on 3/12/15.
 */
public class VolleyErrorAwareRequest<E> extends SuperScoresRequest<Void, E> {

    public VolleyErrorAwareRequest(Context context, int method, Map<String, String> headers,
            String url, Parser<Void, E> parser, Response.Listener<Void> listener,
            Response.ErrorListener<E> errorListener) {
        super(context, method, headers, url, parser, listener, errorListener);
    }
}