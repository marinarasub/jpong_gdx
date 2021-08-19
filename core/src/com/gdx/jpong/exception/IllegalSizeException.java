package com.gdx.jpong.exception;

import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Use when an invalid size is attempted to be used for an 2D object
 */
public class IllegalSizeException extends GdxRuntimeException {

    static final String MESSAGE = "Illegal Size Provided: ";

    public IllegalSizeException(String message) {
        super(MESSAGE + message);
    }
}
