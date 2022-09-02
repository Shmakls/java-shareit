package ru.practicum.shareit.requests.exceptions;

public class InvalidItemRequestDescription extends RuntimeException {

    public InvalidItemRequestDescription(String message) {
        super(message);
    }
}
