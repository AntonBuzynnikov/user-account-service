package ru.buzynnikov.user_acount_service.exceptions;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
