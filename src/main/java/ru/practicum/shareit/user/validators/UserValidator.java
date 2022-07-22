package ru.practicum.shareit.user.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.user.exceptions.InvalidUserEmailException;
import ru.practicum.shareit.user.exceptions.InvalidUserNameException;
import ru.practicum.shareit.user.model.User;

@Component
@Slf4j
public class UserValidator {

    public boolean isValid(User user) {

        log.info("UserValidator: начинаю валидацию email - {}", user.getEmail());

        emailValidator(user.getEmail());

        log.info("UserValidator: начинаю валидацию name - {}", user.getName());

        nameValidator(user.getName());

        return true;

    }

    private void emailValidator (String email) {

        if (!StringUtils.hasText(email)) {

            log.error("UserValidator: получен пустой email");

            throw new InvalidUserEmailException("Email не может быть пустым");
        }

        if (!email.contains("@")) {

            log.error("UserValidator: получен некорректный email");

            throw new InvalidUserEmailException("Email должен содержать символ \"@\"");
        }

        if (email == null) {

            log.error("UserValidator: получен email null");

            throw new InvalidUserEmailException("Email не может быть null");
        }

    }

    private void nameValidator (String name) {

        if (!StringUtils.hasText(name)) {
            log.error("UserValidator: получено пустое имя");
            throw new InvalidUserNameException("Имя не может быть пустым");
        }

    }

}
