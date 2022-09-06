package ru.practicum.shareit.requests.service;

import org.springframework.data.domain.Page;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addItemRequest(ItemRequest itemRequest, Integer requestorId);

    List<ItemRequestDto> findItemRequestsByRequestorId(Integer requestorId);

    Page<ItemRequestDto> findItemRequestsByPages(Integer requestorId, Integer from, Integer size);

    Boolean isExists(Integer requestId);

    ItemRequestDto getItemRequestById(Integer requestId);

}
