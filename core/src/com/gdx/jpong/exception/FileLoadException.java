package com.gdx.jpong.exception;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;

/**
 * Use when a file has failed to load because of missing information, bad file format or another unexpected result
 */
public class FileLoadException extends FileException {

    static final String MESSAGE = "Failed to load file: ";

    public FileLoadException(String message) {
        super(MESSAGE + message);
    }

}
