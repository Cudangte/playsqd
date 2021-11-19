package com.bemonovoid.playsqd.core.exception;

public class PlaysqdException extends RuntimeException {

    public PlaysqdException(String message) {
        super(message);
    }

    public PlaysqdException(String message, Throwable cause) {
        super(message, cause);
    }
}
