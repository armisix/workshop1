package com.volley.superscores;

import com.volley.NetworkResponse;
import com.volley.VolleyError;

/**
 * Created by Pongpat on 3/9/15.
 */
public class ErrorResponse extends VolleyError {

    private Object mErrorObject;

    public ErrorResponse(NetworkResponse response) {
        super(response);
    }

    public void setErrorObject(Object errorObject) {
        mErrorObject = errorObject;
    }

    public Object getErrorObject() {
        return mErrorObject;
    }
}