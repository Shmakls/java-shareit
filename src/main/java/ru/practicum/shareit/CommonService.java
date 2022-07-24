package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService {

    private final ItemService itemService;

    private final UserService userService;

    public ItemDto addItem(Integer userId, ItemDto itemDto) {

        if (!userService.isExists(userId) || userId == null) {

            log.info("CommonService: Пользователь не существует или некорректный id={} ", userId);

            throw new UserNotFoundException("Пользователь не существует или некорректный id");
        }

        log.info("CommonService: получен запрос на добавление вещи {} пользователя с id={} ", itemDto.getName(), userId);

        return itemService.addItem(userId, itemDto);

    }

}
