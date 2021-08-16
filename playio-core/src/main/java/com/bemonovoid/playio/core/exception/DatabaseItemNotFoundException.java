package com.bemonovoid.playio.core.exception;

public class DatabaseItemNotFoundException extends PlayioException {

    public DatabaseItemNotFoundException(String itemType, String id) {
        super(String.format("%s with id: '%s' was not found", itemType, id));
    }
}
