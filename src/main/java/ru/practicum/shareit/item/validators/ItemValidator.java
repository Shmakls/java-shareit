package ru.practicum.shareit.item.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.exceptions.InvalidAvailableStatusException;
import ru.practicum.shareit.item.exceptions.InvalidItemDescriptionException;
import ru.practicum.shareit.item.exceptions.InvalidItemNameException;
import ru.practicum.shareit.item.model.Item;

@Component
@Slf4j
public class ItemValidator {

    public boolean isValid(Item item) {

        nameValidator(item.getName());

        descriptionValidator(item.getDescription());

        availableStatusValidator(item.getAvailable());

        return true;

    }

    private void nameValidator(String name) {

        if (!StringUtils.hasText(name)) {
            log.error("ItemValidator: получено пустое имя");
            throw new InvalidItemNameException("Имя не может быть пустым или ровняться null");
        }

    }

    private void descriptionValidator(String description) {

        if (!StringUtils.hasText(description)) {
            log.error("ItemValidator: получено пустое описание");
            throw new InvalidItemDescriptionException("Описание не может быть пустым или ровняться null");
        }

    }

    private void availableStatusValidator(Boolean available) {

        if (available == null) {
            log.error("ItemValidator: статус не может быть пустым");
            throw new InvalidAvailableStatusException("Статус для аренды не может быть null");
        }

    }

}
