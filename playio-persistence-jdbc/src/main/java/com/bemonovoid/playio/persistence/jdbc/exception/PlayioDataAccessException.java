package com.bemonovoid.playio.persistence.jdbc.exception;

import com.bemonovoid.playio.core.exception.PlayioException;

public class PlayioDataAccessException extends PlayioException {

    public PlayioDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
