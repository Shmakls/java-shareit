package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User addUser (User user);

    User updateUser (User user);

    void removeUserById (Integer userId);

    List<User> findAll ();

    boolean isExists (Integer userId);

    User getUserById (Integer userId);

}
