package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.Booker;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ItemShortForResponse;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapper {

    public BookingDto toDto(Booking booking) {

        BookingDto bookingDto = new BookingDto();

        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(new ItemShortForResponse(booking.getItemId()));
        bookingDto.setBooker(new Booker(booking.getBookerId()));

        if (booking.getId() != null) {
            bookingDto.setId(booking.getId());
        }

        return bookingDto;

    }

    public Booking fromDto(BookingDto bookingDto) {

        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBookerId(bookingDto.getBooker().getId());
        booking.setItemId(bookingDto.getItem().getId());
        booking.setStatus(bookingDto.getStatus());

        if (bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }

        return booking;

    }

}
