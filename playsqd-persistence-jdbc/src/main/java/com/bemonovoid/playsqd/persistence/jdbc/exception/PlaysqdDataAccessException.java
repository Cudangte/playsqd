package com.bemonovoid.playsqd.persistence.jdbc.exception;

import com.bemonovoid.playsqd.core.exception.PlaysqdException;

public class PlaysqdDataAccessException extends PlaysqdException {

    public PlaysqdDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
