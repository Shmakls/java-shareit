package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(Booking booking, Integer bookerId);

    BookingDto getBookingById(Integer bookingId);

    Boolean isExist(Integer bookingId);

    BookingDto updateBookingStatus(BookingDto bookingDto);

    List<BookingDto> findBookingsByBookerId(Integer bookerId,Integer from, Integer size);

    List<BookingDto> findBookingsByIdItemsList(List<Integer> itemsId, Integer from, Integer size);

    List<Booking> findBookingsByItemId(Integer itemId);
}
