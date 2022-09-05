package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserGatewayDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserGatewayController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserGatewayDto userGatewayDto) {

        log.info("UserGatewayController.addUser: Получен запрос на создание пользователя email={}", userGatewayDto.getEmail());

        return userClient.addUser(userGatewayDto);

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserGatewayDto userGatewayDto,
                                             @PathVariable Integer userId) {

        log.info("UserGatewayController.updateUser: Получен запрос на обновление пользователя с id={}", userId);

        return userClient.updateUser(userGatewayDto, userId);

    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("UserGatewayController.getUserById: Получен запрос на получение пользователя с id={}", userId);

        return userClient.getUserById(userId);

    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable Integer userId) {
        log.info("UserGatewayController.deleteUserById: получен запрос на удаление пользователя с id={}", userId);

        userClient.deleteUserById(userId);

    }

    @GetMapping
    public ResponseEntity<Object> findAllUsers() {
        log.info("UserGatewayController.findAllUsers: получен запрос на получение всех пользователей");

        return userClient.findAllUsers();

    }


}
