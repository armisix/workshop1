package com.superscores.android.exception;

/**
 * This Exception will throw if parent method required super to be called, but extended class do not
 * call super.
 * <p/>
 * Created by Pongpat on 2/11/15.
 */
public class SuperNotCalledException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SuperNotCalledException(String message) {
        super(message);
    }
}