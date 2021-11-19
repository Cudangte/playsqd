package com.bemonovoid.playsqd.rest.api.controller.advice;

import com.bemonovoid.playsqd.core.exception.DatabaseItemNotFoundException;
import com.bemonovoid.playsqd.core.exception.UnsupportedAudioFormatException;
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

    @ExceptionHandler(UnsupportedAudioFormatException.class)
    ResponseEntity<Object> handle(UnsupportedAudioFormatException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("'wma' audio format is not supported");
    }


}
