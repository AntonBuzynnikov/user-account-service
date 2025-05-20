package ru.buzynnikov.user_acount_service.exceptions;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String message) {super(message);}
}
