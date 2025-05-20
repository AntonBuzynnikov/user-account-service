package ru.buzynnikov.user_acount_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.buzynnikov.user_acount_service.aspect.ErrorLog;
import ru.buzynnikov.user_acount_service.dto.ExceptionResponse;
import ru.buzynnikov.user_acount_service.exceptions.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ErrorLog
    @ExceptionHandler(LimitEmailException.class)
    public ResponseEntity<ExceptionResponse> handleException(LimitEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(LimitPhoneException.class)
    public ResponseEntity<ExceptionResponse> handleException(LimitPhoneException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(PhoneAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<ExceptionResponse> handleException(NotAuthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EmailNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(PhoneNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(PhoneNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ExceptionResponse> handleException(NotEnoughMoneyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(e.getMessage()));
    }
    @ErrorLog
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
    @ErrorLog
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
    }

}
