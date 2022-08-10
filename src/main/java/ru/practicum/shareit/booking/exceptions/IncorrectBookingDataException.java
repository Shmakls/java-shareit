package ru.practicum.shareit.booking.exceptions;

public class IncorrectBookingDataException extends RuntimeException {

    public IncorrectBookingDataException(String message) {
        super(message);
    }
}
