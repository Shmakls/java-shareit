package ru.practicum.shareit.booking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.exceptions.Error;

@RestControllerAdvice
public class BookingErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidItemStatusForBookingExceptionHandler(final IncorrectItemStatusForBookingException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error incorrectStateExceptionHandler(final IncorrectStateException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error bookingNotFoundExceptionHandler(final BookingNotFoundException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error noAccessRightsExceptionHandler(final NoAccessRightsException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error repeatRequestExceptionHandler(final RepeatRequestException e) {
        return new Error(e.getMessage());
    }

}
