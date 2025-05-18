package ru.buzynnikov.user_acount_service.exceptions;

public class LimitPhoneException extends RuntimeException {
    public LimitPhoneException(String message) {
        super(message);
    }
}
