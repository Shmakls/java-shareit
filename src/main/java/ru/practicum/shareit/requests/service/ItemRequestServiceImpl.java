package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.requests.validators.ItemRequestValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final ItemRequestMapper itemRequestMapper;

    private final ItemRequestValidator itemRequestValidator;

    @Override
    public ItemRequestDto addItemRequest(ItemRequest itemRequest, Integer requestorId) {

        itemRequest.setRequestorId(requestorId);

        itemRequestValidator.isValid(itemRequest);

        itemRequest = itemRequestRepository.save(itemRequest);

        return itemRequestMapper.toDto(itemRequest);

    }

    @Override
    public List<ItemRequestDto> findItemRequestsByRequestorId(Integer requestorId) {

        return itemRequestRepository.findItemRequestsByRequestorIdOrderByCreatedDesc(requestorId)
                .stream()
                .map(itemRequestMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public Page<ItemRequestDto> findItemRequestsByPages(Integer requestorId, Integer from, Integer size) {

        int page = from/size;

        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        return itemRequestRepository.findAllByRequestorIdNot(requestorId, pageable)
                .map(itemRequestMapper::toDto);

    }

    @Override
    public Boolean isExists(Integer requestId) {
        return itemRequestRepository.existsById(requestId);
    }

    @Override
    public ItemRequestDto getItemRequestById(Integer requestId) {

        if (!isExists(requestId)) {
            log.error("CommonService.getItemRequestById: Запроса с id={} в базе нет", requestId);
            throw new ItemRequestNotFoundException("Такого запроса в базе нет");
        }

        return itemRequestMapper.toDto(itemRequestRepository.getReferenceById(requestId));

    }
}
