package ru.practicum.shareit.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {

        log.info("UserController: Получен запрос на добавление нового пользователя {} ", userDto.getEmail());

        return userService.addUser(userDto);

    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {

        log.info("UserController: Получен запрос на обновление данных пользователя с id={} ", userId);

        return userService.updateUser(userDto, userId);

    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {

        log.info("UserController: Получен запрос на получение пользователя с id={} ", userId);

        return userService.getUserById(userId);

    }

    @DeleteMapping("/{userId}")
    public void removeUserById(@PathVariable Integer userId) {

        log.info("UserController: Получен запрос на удаление пользователя с id={} ", userId);

        userService.removeUserById(userId);

    }

    @GetMapping
    public List<UserDto> findAllUsers() {

        log.info("UserController: Получен запрос на получения списка всех пользователей");

        return userService.findAll();
    }

}
