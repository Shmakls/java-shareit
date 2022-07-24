package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item getItemById(Integer itemId);

    Item updateItem(Item item);

    boolean isExists(Integer itemId);

    List<Item> findAll();

    List<Item> getItemListByOwnerId(Integer userId);

    List<Item> searchItemForRentByText(String text);

}
