package com.bemonovoid.playio.core.dao;

import com.bemonovoid.playio.core.exception.PlayioException;

public class MusicDirectoryAccessException extends PlayioException {

    public MusicDirectoryAccessException(String message) {
        super(message);
    }

    public MusicDirectoryAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
