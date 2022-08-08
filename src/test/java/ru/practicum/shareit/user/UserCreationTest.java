package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserCreationTest {

    private final UserService userService;

    @Test
    void wrongIdTest() {

        UserDto user1 = new UserDto();
        user1.setName("user1");
        user1.setEmail("user1@user.com");

        UserDto user2 = new UserDto();
        user2.setName("user2");
        user2.setEmail("user2@user.com");

        UserDto userDuplicateEmail = new UserDto();
        userDuplicateEmail.setName("duplicateUser");
        userDuplicateEmail.setEmail("user1@user.com");

        UserDto user1AfterAdd = userService.addUser(user1);

        assertEquals(1, user1AfterAdd.getId());

        try {
            userService.addUser(userDuplicateEmail);
        } catch (Throwable e) {
            System.out.println("Ошибка имейл");
        }

        UserDto user2AfterAdd = userService.addUser(user2);

        assertEquals(2, userService.findAll().size());

        assertEquals(3, user2AfterAdd.getId());

    }

    @Test
    void okIdTest() {

        UserDto user1 = new UserDto();
        user1.setName("user1");
        user1.setEmail("user1@user.com");

        UserDto user2 = new UserDto();
        user2.setName("user2");
        user2.setEmail("user2@user.com");

        UserDto user1AfterAdd = userService.addUser(user1);

        assertEquals(1, user1AfterAdd.getId());

        UserDto user2AfterAdd = userService.addUser(user2);

        assertEquals(2, userService.findAll().size());

        assertEquals(2, user2AfterAdd.getId());

    }

}
