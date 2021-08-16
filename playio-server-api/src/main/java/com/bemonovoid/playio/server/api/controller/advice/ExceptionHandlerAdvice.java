package com.bemonovoid.playio.server.api.controller.advice;

import com.bemonovoid.playio.core.exception.DatabaseItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(DatabaseItemNotFoundException.class)
    ResponseEntity<Object> handle(DatabaseItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
