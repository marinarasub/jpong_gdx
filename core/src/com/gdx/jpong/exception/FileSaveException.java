package com.gdx.jpong.exception;

import java.io.IOException;

/**
 * Use when a file has failed to save
 */
public class FileSaveException extends IOException {

    static final String MESSAGE = "Failed to save file: ";

    public FileSaveException(String message) {
        super(MESSAGE + message);
    }

}
