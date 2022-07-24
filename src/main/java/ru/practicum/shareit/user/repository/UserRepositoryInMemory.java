package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exceptions.UserAlreadyExistsException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("UserRepositoryInMemory")
@Slf4j
public class UserRepositoryInMemory implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();

    private Integer userId = 0;

    @Override
    public User addUser(User user) {

        emailCheckerOnCreation(user);

        user.setId(++userId);

        users.put(userId, user);

        log.info("UserDB: Пользователь {} добавлен", user.getEmail());

        return user;

    }

    @Override
    public User updateUser(User user) {

        if (!isExists(user.getId())) {

            log.error("UserDB: Пользователя с id={} в базе нет", user.getId());

            throw new UserNotFoundException("Пользователя с id=" + user.getId() + " в базе нет");

        }

        uniqueEmailCheckerOnUpdate(user);

        users.put(user.getId(), user);

        log.info("UserDB: Пользователь {} обновлён", user.getEmail());

        return user;

    }

    @Override
    public void removeUserById(Integer userId) {

        if (!isExists(userId)) {

            log.error("UserDB: пользователя с id={} в базе нет", userId);

            throw new UserNotFoundException("Такого пользователя не существует");
        }

        users.remove(userId);

        log.info("UserDB: Пользователь с id={} удалён", userId);

    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isExists(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public User getUserById(Integer userId) {

        if (!isExists(userId)) {

            log.error("UserDB: пользователя с id={} в базе нет", userId);

            throw new UserNotFoundException("Такого пользователя не существует");
        }

        return users.get(userId);
    }

    private List<String> findAllEmails() {
        return users.values().stream().map(User::getEmail).collect(Collectors.toList());
    }

    private void emailCheckerOnCreation(User user) {

        log.info("UserDB: Проверяем при создании пользоваетя занят ли Email={} ", user.getEmail());

        if (findAllEmails().contains(user.getEmail())) {
            log.error("UserDb: email={} занят", user.getEmail());
            throw new UserAlreadyExistsException("Пользователь с такиим Email уже существует");
        }

    }

    private void uniqueEmailCheckerOnUpdate(User user) {

        log.info("UserDB: Проверяем при обновлении пользоваетя занят ли Email={} ", user.getEmail());

        if (findAllEmails().contains(user.getEmail()) && !getUserIdByEmail(user.getEmail()).equals(user.getId())) {
            log.error("UserDb: пользователь с email={} уже существует", user.getEmail());
            throw new UserAlreadyExistsException("Пользователь с такиим Email уже существует");
        }

    }

    private Integer getUserIdByEmail(String email) {

        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user.getId();
            }
        }
        return -1;
    }

}
