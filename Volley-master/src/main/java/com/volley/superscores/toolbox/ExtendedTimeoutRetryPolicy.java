package com.volley.superscores.toolbox;

import com.volley.AuthFailureError;
import com.volley.NetworkError;
import com.volley.VolleyError;

/**
 * Sign in retry policy will allow longer time before timeout as the server need to request to third
 * party server and may result in extra delay.
 *
 * @author Pongpat Ratanaamornpin
 */
public class ExtendedTimeoutRetryPolicy extends DefaultRequestRetryPolicy {

    public static final int UPLOAD_TIMEOUT_MS = 30000;

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 8000;

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULTIPLY = 1.5f;

    public ExtendedTimeoutRetryPolicy() {
        super(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULTIPLY);
    }

    public ExtendedTimeoutRetryPolicy(int timeout) {
        super(timeout, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULTIPLY);
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {
        if (error instanceof AuthFailureError) {
            // if authorization failed we should not attempt to retry
            throw new NetworkError(error.networkResponse);
        } else {
            super.retry(error);
        }
    }
}