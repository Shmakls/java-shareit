package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, ItemDto itemDto, Integer itemId);

    Item getItemById(Integer itemId);

    List<Item> getItemsListByOwnerId(Integer userId, Integer from, Integer size);

    List<Item> getItemsListByOwnerId(Integer userId);

    List<ItemDto> searchItemForRentByText(String text, Integer from, Integer size);

    List<ItemDtoForGetItems> findAllItems(Integer from, Integer size);

    Boolean isExist(Integer itemId);

    Comment addComment(Comment comment, Integer itemId, Integer authorId);

    List<Comment> getCommentsByItemId(Integer itemId);

    List<ItemForItemRequestDto> getItemsDtoForItemRequestDtoByRequestId(Integer requestId);
}
