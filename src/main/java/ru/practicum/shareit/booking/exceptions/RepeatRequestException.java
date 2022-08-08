package ru.practicum.shareit.booking.exceptions;

public class RepeatRequestException extends RuntimeException {

    public RepeatRequestException(String message) {
        super(message);
    }
}
