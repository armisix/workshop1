package com.volley.superscores.toolbox;

import com.volley.AuthFailureError;
import com.volley.DefaultRetryPolicy;
import com.volley.NetworkError;
import com.volley.VolleyError;

public class DefaultRequestRetryPolicy extends DefaultRetryPolicy {

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 4000;

    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 2;

    /** The default backoff multiplier */
    public static final float DEFAULT_BACKOFF_MULT = 1.4f;

    public DefaultRequestRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    public DefaultRequestRetryPolicy(int initialTimeoutMs, int maxNumRetries,
            float backoffMultiplier) {
        super(initialTimeoutMs, maxNumRetries, backoffMultiplier);
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