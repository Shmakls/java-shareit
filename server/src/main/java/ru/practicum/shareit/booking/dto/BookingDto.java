package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemShortForResponse item;

    private Booker booker;

    private BookingStatus status;

}
