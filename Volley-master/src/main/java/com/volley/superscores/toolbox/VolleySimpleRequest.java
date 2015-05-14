package com.volley.superscores.toolbox;

import android.content.Context;

import com.volley.superscores.SuperScoresRequest;
import com.volley.superscores.Response;
import com.volley.superscores.parser.SimpleParser;

import java.util.Map;

/**
 * Created by Pongpat on 3/11/15.
 */
public class VolleySimpleRequest extends SuperScoresRequest<Void, Void> {

    public VolleySimpleRequest(Context context, int method, Map<String, String> headers, String url,
            Response.Listener<Void> listener, Response.ErrorListener<Void> errorListener) {
        super(context, method, headers, url, new SimpleParser(), listener, errorListener);
    }
}