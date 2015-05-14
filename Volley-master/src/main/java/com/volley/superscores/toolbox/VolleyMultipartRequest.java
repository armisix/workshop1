package com.volley.superscores.toolbox;

import android.content.Context;

import com.volley.AuthFailureError;
import com.volley.VolleyLog;
import com.volley.superscores.SuperScoresRequest;
import com.volley.superscores.Response;
import com.volley.superscores.parser.Parser;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.ContentType;

/**
 * Created by Pongpat on 3/11/15.
 */
public class VolleyMultipartRequest<D, E> extends SuperScoresRequest<D, E> {

    private final HttpEntity mHttpEntity;

    public VolleyMultipartRequest(Context context, int method, Map<String, String> headers,
            String url, HttpEntity httpEntity, Parser<D, E> parser, Response.Listener<D> listener,
            Response.ErrorListener<E> errorListener) {
        super(context, method, headers, url, parser, listener, errorListener);

        mHttpEntity = httpEntity;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.size() == 0) {
            headers = new HashMap<>();
            headers.put(HTTP.CONTENT_TYPE, ContentType.MULTIPART_FORM_DATA.toString());
        }
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType()
                .getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}