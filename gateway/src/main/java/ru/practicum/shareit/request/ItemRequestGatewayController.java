package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestGatewayDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestGatewayController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                 @RequestBody @Valid ItemRequestGatewayDto itemRequestGatewayDto) {

        log.info("ItemRequestGatewayController.addItemRequest: получен запрос на добавление запроса");

        return itemRequestClient.addItemRequest(requestorId, itemRequestGatewayDto);

    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long requestorId) {

        log.info("ItemRequestGatewayController.findAllRequestsByUser: получен запрос на получение списка бронирований "
        + "пользователя с id={}", requestorId);

        return itemRequestClient.findAllRequestsByUser(requestorId);

    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequestsByPages(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @PositiveOrZero @RequestParam(name = "from", required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", required = false, defaultValue = "20") Integer size) {

        log.info("ItemRequestGatewayController.findAllRequestByPages: получен запрос на получение запросов постранично");

        return itemRequestClient.findAllRequestsByPages(userId, from, size);

    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @PathVariable Integer requestId) {

        log.info("ItemRequestGatewayController.findRequestById: получен запрос на получение бронирования с "
        + "id={}", requestId);

        return itemRequestClient.findRequestById(userId, requestId);

    }

}
