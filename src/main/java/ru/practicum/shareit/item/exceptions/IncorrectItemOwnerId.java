package ru.practicum.shareit.item.exceptions;

public class IncorrectItemOwnerId extends RuntimeException {

    public IncorrectItemOwnerId(String message) {
        super(message);
    }
}
