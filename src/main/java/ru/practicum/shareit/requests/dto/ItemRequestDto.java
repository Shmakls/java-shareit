package ru.practicum.shareit.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {

    private Integer id;

    private String description;

    private Integer requestorId;

    private LocalDateTime created = LocalDateTime.now();

    private List<ItemForItemRequestDto> items;

}
