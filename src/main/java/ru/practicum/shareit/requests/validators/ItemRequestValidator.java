package ru.practicum.shareit.requests.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.requests.exceptions.InvalidItemRequestDescription;
import ru.practicum.shareit.requests.model.ItemRequest;

@Component
@Slf4j
public class ItemRequestValidator {

    public boolean isValid(ItemRequest itemRequest) {

        itemRequestDescriptionValidator(itemRequest.getDescription());

        return true;

    }

    private void itemRequestDescriptionValidator(String description) {

        if (!StringUtils.hasText(description)) {
            log.error("ItemRequestValidator: получено пустое имя или null");
            throw new InvalidItemRequestDescription("Имя не может быть пустым или равняться null");
        }

    }

}
