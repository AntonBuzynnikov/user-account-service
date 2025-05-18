package ru.buzynnikov.user_acount_service.exceptions;

public class LimitEmailException extends RuntimeException {
    public LimitEmailException(String message) {
        super(message);
    }
}
