package ru.practicum.shareit.user.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.exceptions.InvalidUserEmailException;
import ru.practicum.shareit.user.exceptions.InvalidUserNameException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private static User user;
    private static User userWithEmptyName;
    private static User userWithEmptyEmail;
    private static User userWithWrongEmail;
    private static User userWithNullEmail;
    private static User userWithNullName;

    private static UserValidator userValidator;

    @BeforeAll
    static void beforeAll() {

        userValidator = new UserValidator();

        user = new User();
        user.setName("user1");
        user.setEmail("user1@email");

        userWithEmptyName = new User();
        userWithEmptyName.setName("");
        userWithEmptyName.setEmail("userWithEmpty@name");

        userWithEmptyEmail = new User();
        userWithEmptyEmail.setName("userWithEmptyEmail");
        userWithEmptyEmail.setEmail("");

        userWithWrongEmail = new User();
        userWithWrongEmail.setName("userWithWrongEmail");
        userWithWrongEmail.setEmail("email");

        userWithNullEmail = new User();
        userWithNullEmail.setName("userWithNullEmail");

        userWithNullName = new User();
        userWithNullName.setEmail("userWithNull@name");

    }

    @Test
    void shouldBeCheckOk() {

        boolean result = userValidator.isValid(user);

        assertTrue(result);

    }

    @Test
    void shouldBeThrowExceptionIfWrongName() {

        final InvalidUserNameException e1 = assertThrows(
                InvalidUserNameException.class,
                () -> userValidator.isValid(userWithEmptyName)
        );

        final InvalidUserNameException e2 = assertThrows(
                InvalidUserNameException.class,
                () -> userValidator.isValid(userWithNullName)
        );

        assertEquals("Имя не может быть пустым", e1.getMessage());
        assertEquals("Имя не может быть пустым", e2.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfWrongEmail() {

        final InvalidUserEmailException e1 = assertThrows(
                InvalidUserEmailException.class,
                () -> userValidator.isValid(userWithEmptyEmail)
        );

        final InvalidUserEmailException e2 = assertThrows(
                InvalidUserEmailException.class,
                () -> userValidator.isValid(userWithNullEmail)
        );

        final InvalidUserEmailException e3 = assertThrows(
                InvalidUserEmailException.class,
                () -> userValidator.isValid(userWithWrongEmail)
        );

        assertEquals("Email не может быть пустым", e1.getMessage());
        assertEquals("Email не может быть пустым", e2.getMessage());
        assertEquals("Email должен содержать символ \"@\"", e3.getMessage());

    }

}
