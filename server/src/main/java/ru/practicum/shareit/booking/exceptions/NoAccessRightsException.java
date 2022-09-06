package ru.practicum.shareit.booking.exceptions;

public class NoAccessRightsException extends RuntimeException {

    public NoAccessRightsException(String message) {
        super(message);
    }
}
