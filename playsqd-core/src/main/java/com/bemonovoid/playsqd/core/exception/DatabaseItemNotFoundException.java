package com.bemonovoid.playsqd.core.exception;

public class DatabaseItemNotFoundException extends PlaysqdException {

    public DatabaseItemNotFoundException(String itemType, String id) {
        super(String.format("%s with  : '%s' was not found", itemType, id));
    }
}
