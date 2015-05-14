package com.volley.superscores;

import android.content.Context;

import com.volley.superscores.parser.Parser;
import com.volley.superscores.toolbox.VolleyMultipartRequest;

import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;

/**
 * Multipart request builder to help build volley request
 *
 * Created by Pongpat on 3/11/15.
 */
public class MultipartRequestBuilder extends RequestBuilder {

    private final HttpEntity mHttpEntity;

    public MultipartRequestBuilder(int method, Map<String, String> header, String url,
            HttpEntity httpEntity) {
        super(method, header, url);
        mHttpEntity = httpEntity;
    }

    public <D, E> VolleyMultipartRequest buildMultipartRequest(Context context, Parser<D, E> parser,
            Response.Listener<D> listener, Response.ErrorListener<E> errorListener) {

        VolleyMultipartRequest request = new VolleyMultipartRequest<>(context, mMethod, mHeaders,
                mUrl, mHttpEntity, parser, listener, errorListener);
        decorateRequest(request);

        return request;
    }
}