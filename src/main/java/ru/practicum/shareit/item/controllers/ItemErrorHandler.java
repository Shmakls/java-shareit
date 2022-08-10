package ru.practicum.shareit.item.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.Error;
import ru.practicum.shareit.item.exceptions.*;

@RestControllerAdvice
public class ItemErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidItemNameExceptionHandler(final InvalidItemNameException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidItemDescriptionExceptionHandler(final InvalidItemDescriptionException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidAvailableStatusExceptionHandler(final InvalidAvailableStatusException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String incorrectItemIdOwnerHandler(final IncorrectItemOwnerId e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String itemNotFoundExceptionHandler(final ItemNotFoundException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Error invalidCommentTextExceptionHandler(final InvalidCommentTextException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Error bookingForCommentNotFoundExceptionHandler(final BookingForCommentNotFoundException e) {
        return new Error(e.getMessage());
    }

}
