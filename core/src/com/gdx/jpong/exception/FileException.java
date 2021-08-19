package com.gdx.jpong.exception;

import java.io.IOException;

/**
 * Abstract class for anything file related exception
 */
public abstract class FileException extends IOException {

    public FileException(String message) {
        super(message);
    }

}
