package ru.buzynnikov.user_acount_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.buzynnikov.user_acount_service.dto.ExceptionResponse;
import ru.buzynnikov.user_acount_service.exceptions.LimitEmailException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LimitEmailException.class)
    public ResponseEntity<ExceptionResponse> handleException(LimitEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
}
