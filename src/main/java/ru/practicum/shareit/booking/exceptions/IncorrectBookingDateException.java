package ru.practicum.shareit.booking.exceptions;

public class IncorrectBookingDateException extends RuntimeException {

    public IncorrectBookingDateException(String message) {
        super(message);
    }
}
