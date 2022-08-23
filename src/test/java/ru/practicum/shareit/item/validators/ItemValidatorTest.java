package ru.practicum.shareit.item.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exceptions.InvalidAvailableStatusException;
import ru.practicum.shareit.item.exceptions.InvalidItemDescriptionException;
import ru.practicum.shareit.item.exceptions.InvalidItemNameException;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.*;

class ItemValidatorTest {

    private static ItemValidator itemValidator;

    private static Item item;

    private static Item itemWithEmptyName;

    private static Item itemWithNullName;

    private static Item itemWithEmptyDescription;

    private static Item itemWithNullDescription;

    private static Item itemWithNullAvailable;

    @BeforeAll
    static void beforeAll() {

        itemValidator = new ItemValidator();

        item = new Item();
        item.setDescription("description");
        item.setAvailable(true);
        item.setName("name");

        itemWithEmptyName = new Item();
        itemWithEmptyName.setName("");
        itemWithEmptyName.setDescription("description");
        itemWithEmptyName.setAvailable(true);

        itemWithNullName = new Item();
        itemWithNullName.setDescription("description");
        itemWithNullName.setAvailable(true);

        itemWithEmptyDescription = new Item();
        itemWithEmptyDescription.setDescription("");
        itemWithEmptyDescription.setName("name");
        itemWithEmptyDescription.setAvailable(true);

        itemWithNullDescription = new Item();
        itemWithNullDescription.setName("name");
        itemWithNullDescription.setAvailable(true);

        itemWithNullAvailable = new Item();
        itemWithNullAvailable.setName("name");
        itemWithNullAvailable.setDescription("description");

    }

    @Test
    void shouldBeOk() {

        boolean result = itemValidator.isValid(item);

        assertTrue(result);

    }

    @Test
    void shouldBeThrowExceptionIfWrongName() {

        final InvalidItemNameException e1 = assertThrows(
                InvalidItemNameException.class,
                () -> itemValidator.isValid(itemWithEmptyName)
        );

        final InvalidItemNameException e2 = assertThrows(
                InvalidItemNameException.class,
                () -> itemValidator.isValid(itemWithNullName)
        );

        assertEquals("Имя не может быть пустым или ровняться null", e1.getMessage());
        assertEquals("Имя не может быть пустым или ровняться null", e2.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfWrongDescription() {

        final InvalidItemDescriptionException e1 = assertThrows(
                InvalidItemDescriptionException.class,
                () -> itemValidator.isValid(itemWithEmptyDescription)
        );

        final InvalidItemDescriptionException e2 = assertThrows(
                InvalidItemDescriptionException.class,
                () -> itemValidator.isValid(itemWithNullDescription)
        );

        assertEquals("Описание не может быть пустым или ровняться null", e1.getMessage());
        assertEquals("Описание не может быть пустым или ровняться null", e2.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfAvailableNull() {

        final InvalidAvailableStatusException e = assertThrows(
                InvalidAvailableStatusException.class,
                () -> itemValidator.isValid(itemWithNullAvailable)
        );

        assertEquals("Статус для аренды не может быть null", e.getMessage());

    }

}