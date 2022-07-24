package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
@Qualifier("ItemRepositoryInMemory")
public class ItemRepositoryInMemory implements ItemRepository {

    private final HashMap<Integer, Item> items = new HashMap<>();

    private Integer itemId = 0;

    @Override
    public Item addItem(Item item) {

        item.setId(++itemId);

        items.put(itemId, item);

        log.info("ItemDB: Вещь {} добавлена", item.getName());

        return item;

    }

    @Override
    public Item getItemById(Integer itemId) {

        if (!isExists(itemId)) {

            log.error("ItemDb: вещи с id={} в базе нет", itemId);

            throw new ItemNotFoundException("Такой вещи не существует");

        }

        return items.get(itemId);
    }

    @Override
    public Item updateItem(Item item) {

        if (!isExists(item.getId())) {

            log.error("ItemDb: вещи с id={} в базе нет", item.getId());

            throw new ItemNotFoundException("Такой вещи не существует");

        }

        items.put(item.getId(), item);

        log.info("ItemDb: вещь {} обновлена", item.getName());

        return item;

    }

    @Override
    public boolean isExists(Integer itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemListByOwnerId(Integer userId) {

        log.info("ItemDb: собираю список вещей пользователя с id={} ", userId);

        return findAll().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());

    }

    @Override
    public List<Item> searchItemForRentByText(String text) {

        log.info("ItemDb: готовлю список вещей по запросу \"{}\" ", text);

        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                 || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());

    }
}
