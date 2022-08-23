package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoForItemRequestDto {

    private Integer id;

    private String name;

    private String description;

    private Integer ownerId;

    private Boolean available;

    private Integer requestId;

}
