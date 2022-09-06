package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {

        User user = userMapper.fromDto(userDto);

        user = userRepository.save(user);

        return userMapper.toDto(user);

    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User userUpdate = userMapper.fromDto(userDto);

        User oldUser = userRepository.getReferenceById(userId);

        log.info("UserService: Пользователь с id={} будет обновлен", userId);

        User user = userConstructorToUpdate(userUpdate, oldUser);

        user.setId(userId);

        user = userRepository.save(user);

        return userMapper.toDto(user);

    }

    @Override
    public UserDto getUserById(Integer userId) {

        if (!isExists(userId)) {
            log.info("UserService: Пользователя с id={} в базе нет", userId);
            throw new UserNotFoundException("Такого пользователя в базе нет");
        }

        User user = userRepository.getReferenceById(userId);

        return userMapper.toDto(user);

    }

    @Override
    public List<UserDto> findAll() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public void removeUserById(Integer userId) {

        userRepository.deleteById(userId);

    }

    @Override
    public boolean isExists(Integer userId) {
        return userRepository.existsById(userId);
    }

    private User userConstructorToUpdate(User userUpdate, User oldUser) {

        User user = new User();

        if (userUpdate.getEmail() != null) {
            user.setEmail(userUpdate.getEmail());
        } else {
            user.setEmail(oldUser.getEmail());
        }

        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        } else {
            user.setName(oldUser.getName());
        }

        return user;

    }

}
