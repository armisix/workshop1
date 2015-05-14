package com.volley.superscores;

/**
 * Created by Pongpat on 3/10/15.
 */
public class Response {

    public interface Listener<D> {
        public void onResponse(NetworkResponseWrapper<D> response);
    }

    public interface ErrorListener<E> {
        public void onErrorResponse(NetworkResponseWrapper<E> errorResponse);
    }
}