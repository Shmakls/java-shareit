package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.Booker;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemShortForResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {

        BookingDto bookingDto = new BookingDto();

        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(new ItemShortForResponse(booking.getItemId()));
        bookingDto.setBooker(new Booker(booking.getBookerId()));

        Optional.ofNullable(booking.getId()).ifPresent(bookingDto::setId);

        return bookingDto;

    }

    public Booking fromDto(BookingDto bookingDto) {

        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBookerId(bookingDto.getBooker().getId());
        booking.setItemId(bookingDto.getItem().getId());
        booking.setStatus(bookingDto.getStatus());

        Optional.ofNullable(bookingDto.getId()).ifPresent(booking::setId);

        return booking;

    }

}
