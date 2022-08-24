package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    private final UserService userService;

    private static UserDto userDto1;

    private static UserDto userDto2;

    @BeforeAll
    static void beforeALl() {

        userDto1 = new UserDto();
        userDto1.setEmail("user1@email");
        userDto1.setName("user1");

        userDto2 = new UserDto();
        userDto2.setEmail("user2@email");
        userDto2.setName("user2");

    }

    @Test
    void updateUser() {

        UserDto userDtoToUpdate1 = new UserDto();
        userDtoToUpdate1.setEmail("user1update@email");

        userService.addUser(userDto1);

        UserDto updatedUserDto = userService.updateUser(userDtoToUpdate1, 1);

        assertEquals("user1update@email", updatedUserDto.getEmail());

        UserDto userDtoToUpdate2 = new UserDto();
        userDtoToUpdate2.setName("updateUser1");

        updatedUserDto = userService.updateUser(userDtoToUpdate2, 1);

        assertEquals("updateUser1", updatedUserDto.getName());

    }

    @Test
    void getUserById() {

        userService.addUser(userDto1);

        UserDto resulUserDto = userService.getUserById(1);

        assertEquals(1, resulUserDto.getId());

        final UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99)
        );

        assertEquals("Такого пользователя в базе нет", userNotFoundException.getMessage());

    }

    @Test
    void findAll() {

        userService.addUser(userDto1);
        userService.addUser(userDto2);

        List<UserDto> users = userService.findAll();

        assertEquals(2, users.size());

    }

    @Test
    void removeUserById() {

        userService.addUser(userDto1);
        userService.addUser(userDto2);

        List<UserDto> users = userService.findAll();

        assertEquals(2, users.size());

        userService.removeUserById(2);

        users = userService.findAll();

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

    }

    @Test
    void isExists() {

        userService.addUser(userDto1);

        assertTrue(userService.isExists(1));

    }
}