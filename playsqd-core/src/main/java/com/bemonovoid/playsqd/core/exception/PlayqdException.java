package com.bemonovoid.playsqd.core.exception;

import lombok.Getter;

@Getter
public class PlayqdException extends RuntimeException {

    private final int errorCode;

    private PlayqdException(String message) {
        this(message, null);
    }

    private PlayqdException(String message, Throwable cause) {
        this(message, 500, cause);
    }

    private PlayqdException(String message, int errorCode) {
        this(message, errorCode, null);
    }

    private PlayqdException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public static PlayqdException ioException(String message, Throwable t) {
        return new PlayqdException(message, t);
    }

    public static PlayqdException ioException(String message, int errorCode) {
        return ioException(message, errorCode, null);
    }

    public static PlayqdException ioException(String message, int errorCode, Throwable t) {
        return genericException(message, errorCode, t);
    }

    public static PlayqdException genericException(Throwable t) {
        return genericException(null, 500, t);
    }

    public static PlayqdException genericException(String message) {
        return genericException(message, 500);
    }

    public static PlayqdException genericException(String message, int errorCode) {
        return genericException(message, errorCode, null);
    }

    public static PlayqdException genericException(String message, int errorCode, Throwable t) {
        return new PlayqdException(message, errorCode, t);
    }

    public static PlayqdException objectAlreadyExistsException(String message) {
        return new PlayqdException(message, 400);
    }

    public static PlayqdException objectDoesNotExistException(Object id, String itemName) {
        return genericException(String.format("%s with id: '%s' does not exist", itemName, id), 404);
    }
}
