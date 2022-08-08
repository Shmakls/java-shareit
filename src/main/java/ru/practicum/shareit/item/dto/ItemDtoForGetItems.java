package ru.practicum.shareit.item.dto;

import lombok.Data;
import java.util.List;

@Data
public class ItemDtoForGetItems {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private BookingForItem lastBooking;

    private BookingForItem nextBooking;

    private List<CommentDto> comments;

}
