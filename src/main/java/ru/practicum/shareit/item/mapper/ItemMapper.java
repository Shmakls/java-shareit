package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGetItems;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        Optional.ofNullable(item.getId()).ifPresent(itemDto::setId);

        Optional.ofNullable(item.getOwnerId()).ifPresent(itemDto::setOwner);

        Optional.ofNullable(item.getRequestId()).ifPresent(itemDto::setRequestId);

        return itemDto;

    }

    public Item fromDto(ItemDto itemDto) {

        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        Optional.ofNullable(itemDto.getId()).ifPresent(item::setId);

        Optional.ofNullable(itemDto.getOwner()).ifPresent(item::setOwnerId);

        Optional.ofNullable(itemDto.getRequestId()).ifPresent(item::setRequestId);

        return item;

    }

    public ItemDtoForGetItems fromDtoToFindAll(Item item) {

        ItemDtoForGetItems itemDtoForGetItems = new ItemDtoForGetItems();

        itemDtoForGetItems.setId(item.getId());
        itemDtoForGetItems.setName(item.getName());
        itemDtoForGetItems.setDescription(item.getDescription());
        itemDtoForGetItems.setAvailable(item.getAvailable());

        return itemDtoForGetItems;

    }

    public ItemForItemRequestDto toItemDtoForItemRequestDto(Item item) {

        ItemForItemRequestDto itemForItemRequestDto = new ItemForItemRequestDto();

        itemForItemRequestDto.setId(item.getId());
        itemForItemRequestDto.setName(item.getName());
        itemForItemRequestDto.setDescription(item.getDescription());
        itemForItemRequestDto.setOwnerId(item.getOwnerId());
        itemForItemRequestDto.setAvailable(item.getAvailable());

        Optional.ofNullable(item.getRequestId()).ifPresent(itemForItemRequestDto::setRequestId);

        return itemForItemRequestDto;

    }

}
