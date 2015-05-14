package com.volley.superscores.toolbox;

import android.content.Context;

import com.volley.superscores.SuperScoresRequest;
import com.volley.superscores.Response;
import com.volley.superscores.parser.Parser;

import java.util.Map;

/**
 * @author Pongpat Ratanaamornpin
 */
public class VolleyParsableRequest<D, E> extends SuperScoresRequest<D, E> {

    public VolleyParsableRequest(Context context, int method, Map<String, String> headers,
            String url, Parser<D, E> parser, Response.Listener<D> listener,
            Response.ErrorListener<E> errorListener) {
        super(context, method, headers, url, parser, listener, errorListener);
    }
}