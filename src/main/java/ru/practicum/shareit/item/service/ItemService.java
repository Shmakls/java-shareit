package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.List;

public interface ItemService {

    ItemDto addItem(Integer userId, ItemDto itemDto);

    ItemDto updateItem(Integer userId, ItemDto itemDto, Integer itemId);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> getItemsListByOwnerId(Integer userId);

    List<ItemDto> searchItemForRentByText(String text);

    List<ItemDto> findAllItems();

}
