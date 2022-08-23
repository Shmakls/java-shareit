package ru.practicum.shareit.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.Optional;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toDto(ItemRequest itemRequest) {

        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestorId(itemRequest.getRequestorId());

        Optional.ofNullable(itemRequest.getId()).ifPresent(itemRequestDto::setId);

        Optional.ofNullable(itemRequest.getCreated()).ifPresent(itemRequestDto::setCreated);

        return itemRequestDto;

    }

    public ItemRequest fromDto(ItemRequestDto itemRequestDto) {

        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestorId(itemRequestDto.getRequestorId());

        Optional.ofNullable(itemRequestDto.getId()).ifPresent(itemRequest::setId);

        Optional.ofNullable(itemRequestDto.getCreated()).ifPresent(itemRequest::setCreated);

        return itemRequest;

    }

}
