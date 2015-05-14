package com.volley.superscores.toolbox;

import android.content.Context;

import com.volley.superscores.SuperScoresRequest;
import com.volley.superscores.Response;
import com.volley.superscores.parser.StringParser;

import java.util.Map;

/**
 * @author Pongpat Ratanaamornpin
 */
public class VolleyStringRequest extends SuperScoresRequest<String, String> {

    public VolleyStringRequest(Context context, int method, Map<String, String> headers, String url,
            Response.Listener<String> listener, Response.ErrorListener<String> errorListener) {
        super(context, method, headers, url, new StringParser(), listener, errorListener);
    }
}