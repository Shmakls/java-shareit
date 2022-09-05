package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Component
public class UserMapper {

    public UserDto toDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());

        Optional.ofNullable(user.getId()).ifPresent(userDto::setId);

        return userDto;

    }

    public User fromDto(UserDto userDto) {

        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());

        Optional.ofNullable(userDto.getId()).ifPresent(user::setId);

        return user;

    }

}
