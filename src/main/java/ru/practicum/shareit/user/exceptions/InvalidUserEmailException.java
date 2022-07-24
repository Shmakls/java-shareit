package ru.practicum.shareit.user.exceptions;

public class InvalidUserEmailException extends RuntimeException {

    public InvalidUserEmailException(String message) {
        super(message);
    }
}
