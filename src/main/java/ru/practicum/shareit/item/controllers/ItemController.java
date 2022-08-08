package ru.practicum.shareit.item.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.CommonService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    private final CommonService commonService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                         @RequestBody ItemDto itemDto) {

        return commonService.addItem(userId, itemDto);

    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                            @RequestBody ItemDto itemDto, @PathVariable Integer itemId) {

        log.info("ItemController: получен запрос на обновление данных вещи {} с id={} ", itemDto.getName(), itemId);

        return itemService.updateItem(userId, itemDto, itemId);

    }

    @GetMapping("/{itemId}")
    public ItemDtoForGetItems getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @PathVariable Integer itemId) {

        log.info("ItemController: Получен запрос на получение данных о вещи с id={} ", itemId);

        return commonService.getItemById(itemId, userId);

    }

    @GetMapping
    public List<ItemDtoForGetItems> getItemsListByOwnerId(@RequestHeader(value = "X-Sharer-User-Id", required = false, defaultValue = "-1") Integer userId) {

        log.info("ItemController.getItemsListByOwnerId: Получен запрос на список вещей пользователя с id={}", userId);

        return commonService.getItemsListByOwnerId(userId);

        }


    @GetMapping("/search")
    public List<ItemDto> searchItemForRentByText(@RequestParam String text) {

        log.info("ItemController: получен запрос на поиск доступных к аренде вещей с текстом \"{}\" ", text);

        return itemService.searchItemForRentByText(text);

    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Integer authorId,
                                       @PathVariable Integer itemId,
                                       @RequestBody Comment comment) {

        return commonService.addComment(comment, itemId, authorId);

    }


}
