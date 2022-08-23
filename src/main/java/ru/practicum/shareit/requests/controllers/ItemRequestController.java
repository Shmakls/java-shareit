package ru.practicum.shareit.requests.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final CommonService commonService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Integer requestorId,
                                         @RequestBody ItemRequest itemRequest) {

        return commonService.addItemRequest(itemRequest, requestorId);

    }

    @GetMapping
    public List<ItemRequestDto> findAllRequestsByUser(@RequestHeader("X-Sharer-User-Id") Integer requestorId) {

        return commonService.findAllRequestsByRequestorId(requestorId);

    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequestsByPages(@RequestHeader("X-Sharer-User-Id") Integer requestorId,
                                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                                       @RequestParam(required = false, defaultValue = "20") Integer size) {

        return commonService.findAllRequestsByPages(requestorId, from, size);

    }

    @GetMapping("{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @PathVariable Integer requestId) {

        return commonService.getItemRequestById(userId, requestId);

    }




}
