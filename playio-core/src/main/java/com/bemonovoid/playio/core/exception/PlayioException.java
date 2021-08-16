package com.bemonovoid.playio.core.exception;

public class PlayioException extends RuntimeException {

    public PlayioException(String message) {
        super(message);
    }

    public PlayioException(String message, Throwable cause) {
        super(message, cause);
    }
}
