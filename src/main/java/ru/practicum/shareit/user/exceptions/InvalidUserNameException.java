package ru.practicum.shareit.user.exceptions;

public class InvalidUserNameException extends RuntimeException {

    public InvalidUserNameException(String message) {
        super(message);
    }
}
