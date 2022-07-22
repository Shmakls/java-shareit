package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public UserDto toDto (User user) {

        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        if (user.getId() != null) {
            userDto.setId(user.getId());
        }

        return userDto;

    }

    public User fromDto (UserDto userDto) {

        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());

        if (userDto.getId() != null) {
            user.setId(userDto.getId());
        }

        return user;

    }

}
