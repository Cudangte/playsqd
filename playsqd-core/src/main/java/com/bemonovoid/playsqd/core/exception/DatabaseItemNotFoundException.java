package com.bemonovoid.playsqd.core.exception;

public class DatabaseItemNotFoundException extends PlaysqdException {

    public DatabaseItemNotFoundException(String itemType, String id) {
        super(String.format("%s with id: '%s' does not exist", itemType, id));
    }
}
