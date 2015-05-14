package com.volley.superscores;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pongpat on 3/13/15.
 */
public class StringResponse {
    private String mRawResponse;
    private int mHttpStatus;
    private Map<String, String> mHeaders;
    private boolean mNotModified;

    public static StringResponse empty() {
        return new StringResponse(null, 0, new HashMap<String, String>(), false);
    }

    public StringResponse(String rawResponse, int httpStatus, Map<String, String> headers,
            boolean notModified) {
        mRawResponse = rawResponse;
        mHttpStatus = httpStatus;
        mHeaders = headers;
        mNotModified = notModified;
    }

    public String getRawResponse() {
        return mRawResponse;
    }

    public int getHttpStatus() {
        return mHttpStatus;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    public boolean isNotModified() {
        return mNotModified;
    }
}
