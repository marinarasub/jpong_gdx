package com.gdx.jpong.exception;

import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Use when an invalid value is attempted to be used for the relevant situation
 */
public class IllegalValueException extends GdxRuntimeException {

    static final String MESSAGE = "Illegal Value Provided: ";

    public IllegalValueException(String message) {
        super(message);
    }
}
