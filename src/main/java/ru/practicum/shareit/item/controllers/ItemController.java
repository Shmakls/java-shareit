package ru.practicum.shareit.item.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final UserService userService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                         @RequestBody ItemDto itemDto) {

        if (!userService.isExists(userId) || userId == null) {

            log.info("ItemController: Пользователь не существует или некорректный id={} ", userId);

            throw new UserNotFoundException("Пользователь не существует или некорректный id");
        }

        log.info("ItemController: получен запрос на добавление вещи {} пользователя с id={} ", itemDto.getName(), userId);

        return itemService.addItem(userId, itemDto);

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                            @RequestBody ItemDto itemDto, @PathVariable Integer itemId) {

        log.info("ItemController: получен запрос на обновление данных вещи {} с id={} ", itemDto.getName(), itemId);

        return itemService.updateItem(userId, itemDto, itemId);

    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Integer itemId) {

        log.info("ItemController: Получен запрос на получение данных о вещи с id={} ", itemId);

        return itemService.getItemById(itemId);

    }

    @GetMapping
    public List<ItemDto> getItemsListByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false, defaultValue = "-1") Integer userId) {

        if (userId == -1) {

            log.info("ItemController: получен запрос на получение списка всех вещей");

            return itemService.findAllItems();

        } else {

            log.info("ItemController: получен запрос на просмотр списка вещей своим владельцем с id={} ", userId);

            return itemService.getItemsListByOwnerId(userId);

        }



    }

    @GetMapping("/search")
    public List<ItemDto> searchItemForRentByText(@RequestParam String text) {

        log.info("ItemController: получен запрос на поиск доступных к аренде вещей с текстом \"{}\" ", text);

        return itemService.searchItemForRentByText(text);

    }


}
