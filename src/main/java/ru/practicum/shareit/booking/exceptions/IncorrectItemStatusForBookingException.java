package ru.practicum.shareit.booking.exceptions;

public class IncorrectItemStatusForBookingException extends RuntimeException{

    public IncorrectItemStatusForBookingException(String message) {
        super(message);
    }
}
