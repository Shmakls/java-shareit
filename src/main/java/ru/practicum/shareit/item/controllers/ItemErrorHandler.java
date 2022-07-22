package ru.practicum.shareit.item.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exceptions.IncorrectItemOwnerId;
import ru.practicum.shareit.item.exceptions.InvalidAvailableStatusException;
import ru.practicum.shareit.item.exceptions.InvalidItemDescriptionException;
import ru.practicum.shareit.item.exceptions.InvalidItemNameException;

@RestControllerAdvice
public class ItemErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidItemNameExceptionHandler (final InvalidItemNameException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidItemDescriptionExceptionHandler (final InvalidItemDescriptionException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidAvailableStatusExceptionHandler (final InvalidAvailableStatusException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String incorrectItemIdOwnerHandler (final IncorrectItemOwnerId e) {
        return "error: " + e.getMessage();
    }

}
