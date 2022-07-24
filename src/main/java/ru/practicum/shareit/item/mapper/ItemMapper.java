package ru.practicum.shareit.item.mapper;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public ItemDto toDto(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        if (item.getId() != null) {
            itemDto.setId(item.getId());
        }

        if (item.getOwner() != null) {
            itemDto.setOwner(item.getOwner());
        }

        if (item.getRentCount() != null) {
            itemDto.setRentCount(item.getRentCount());
        }

        return itemDto;

    }

    public Item fromDto(ItemDto itemDto) {

        Item item = new Item();

        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        if (itemDto.getId() != null) {
            item.setId(itemDto.getId());
        }

        if (itemDto.getOwner() != null) {
            item.setOwner(itemDto.getOwner());
        }

        if (itemDto.getRentCount() != null) {
            item.setRentCount(item.getRentCount());
        }

        return item;

    }

}
