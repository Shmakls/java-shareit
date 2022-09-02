package ru.practicum.shareit.requests.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.requests.exceptions.InvalidItemRequestDescription;
import ru.practicum.shareit.requests.model.ItemRequest;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestValidatorTest {

    private static ItemRequestValidator itemRequestValidator;

    private static ItemRequest itemRequest;

    private static ItemRequest itemRequestWithNullDescription;

    private static ItemRequest itemRequestWithEmptyDescription;

    @BeforeAll
    static void beforeAll() {

        itemRequestValidator = new ItemRequestValidator();

        itemRequest = new ItemRequest();
        itemRequest.setDescription("itemRequestDescription");

        itemRequestWithEmptyDescription = new ItemRequest();
        itemRequestWithEmptyDescription.setDescription("");

        itemRequestWithNullDescription = new ItemRequest();

    }

    @Test
    void shouldBeOk() {
        boolean result = itemRequestValidator.isValid(itemRequest);
        assertTrue(result);
    }

    @Test
    void shouldBeThrowExceptionIfWrongDescription() {

        final InvalidItemRequestDescription e1 = assertThrows(
                InvalidItemRequestDescription.class,
                () -> itemRequestValidator.isValid(itemRequestWithEmptyDescription)
        );

        final InvalidItemRequestDescription e2 = assertThrows(
                InvalidItemRequestDescription.class,
                () -> itemRequestValidator.isValid(itemRequestWithNullDescription)
        );

        assertEquals("Имя не может быть пустым или равняться null", e1.getMessage());
        assertEquals("Имя не может быть пустым или равняться null", e2.getMessage());

    }

}