package com.bemonovoid.playsqd.rest.api.controller.advice;

import com.bemonovoid.playsqd.core.exception.PlayqdException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(PlayqdException.class)
    ResponseEntity<Object> handle(PlayqdException e) {
        return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
    }


}
