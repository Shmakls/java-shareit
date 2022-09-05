package ru.practicum.shareit.requests.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.Error;
import ru.practicum.shareit.requests.exceptions.InvalidParametersException;
import ru.practicum.shareit.requests.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ItemRequestErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error userNotFoundExceptionHandler(final UserNotFoundException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error itemRequestNotFoundExceptionHandler(final ItemRequestNotFoundException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error invalidParametersExceptionHandler(final InvalidParametersException e) {
        return new Error(e.getMessage());
    }

}
