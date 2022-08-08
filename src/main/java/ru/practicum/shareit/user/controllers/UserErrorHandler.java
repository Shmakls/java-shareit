package ru.practicum.shareit.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.Error;
import ru.practicum.shareit.user.exceptions.InvalidUserEmailException;
import ru.practicum.shareit.user.exceptions.UserAlreadyExistsException;
import ru.practicum.shareit.user.exceptions.UserErrorCreateException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

@RestControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userAlreadyExistsExceptionHandler(final UserAlreadyExistsException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(final UserNotFoundException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidEmailExceptionHandler(final InvalidUserEmailException e) {
        return "error: " + e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error userErrorCreateExceptionHandler(final UserErrorCreateException e) {
        return new Error(e.getMessage());
    }

}
