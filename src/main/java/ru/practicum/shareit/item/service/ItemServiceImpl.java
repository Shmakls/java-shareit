package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.ItemValidator;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.IncorrectItemOwnerId;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final ItemValidator itemValidator;

    @Override
    public ItemDto addItem(Integer userId, ItemDto itemDto) {

        Item item = itemMapper.fromDto(itemDto);

        item.setOwner(userId);

        itemValidator.isValid(item);

        item = itemRepository.addItem(item);

        return itemMapper.toDto(item);

    }

    @Override
    public ItemDto updateItem(Integer userId, ItemDto itemDto, Integer itemId) {

        Item updateItem = itemMapper.fromDto(itemDto);

        Item oldItem = itemRepository.getItemById(itemId);

        if (!oldItem.getOwner().equals(userId)) {
            log.error("ItemService: вещь с id={} не принадлежит пользователю с id={}", itemId, userId);
            throw new IncorrectItemOwnerId("Нельзя редактировать не принадлежащую вам вещь");
        }

        log.info("ItemService: вещь с id={} обновлена", itemId);

        Item item = itemConstructorToUpdate(updateItem, oldItem);

        item.setId(itemId);

        item = itemRepository.updateItem(item);

        return itemMapper.toDto(item);

    }

    @Override
    public ItemDto getItemById(Integer itemId) {

        Item item = itemRepository.getItemById(itemId);

        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getItemsListByOwnerId(Integer userId) {

        log.info("ItemService: направляю запрос в ItemDb для получения списка вещей владельца с id={} ", userId);

        return itemRepository.getItemListByOwnerId(userId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<ItemDto> searchItemForRentByText(String text) {

        if (!StringUtils.hasText(text)) {
            log.info("ItemService: текст для поиска пустой, возвращаю пустой список");
            return new ArrayList<>();
        } else {
            log.info("ItemService: направляю запрос в ItemDb для поиска вещей содержащих текст \"{}\"", text);
            return itemRepository.searchItemForRentByText(text).stream()
                    .map(itemMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ItemDto> findAllItems() {

        List<Item> items = itemRepository.findAll();

        return items.stream().map(itemMapper::toDto)
                .collect(Collectors.toList());

    }

    private Item itemConstructorToUpdate(Item updateItem, Item oldItem) {

        Item item = new Item();

        if (updateItem.getName() != null) {
            item.setName(updateItem.getName());
        } else {
            item.setName(oldItem.getName());
        }

        if (updateItem.getDescription() != null) {
            item.setDescription(updateItem.getDescription());
        } else {
            item.setDescription(oldItem.getDescription());
        }

        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        } else {
            item.setAvailable(oldItem.getAvailable());
        }

        if (updateItem.getRentCount() != null) {
            item.setRentCount(updateItem.getRentCount());
        } else {
            item.setRentCount(oldItem.getRentCount());
        }

        if (updateItem.getOwner() != null) {
            item.setOwner(updateItem.getOwner());
        } else {
            item.setOwner(oldItem.getOwner());
        }

        return item;

    }

}
