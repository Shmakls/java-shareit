package ru.practicum.shareit.booking.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.CommonService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final CommonService commonService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                 @RequestBody Booking booking) {

        return commonService.addBooking(booking, bookerId);

    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                     @PathVariable Integer bookingId, @RequestParam Boolean approved) {

        return commonService.approveBooking(ownerId, bookingId, approved);

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                     @PathVariable Integer bookingId) {

        return commonService.getBookingById(bookerId, bookingId);

    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerIdDesc(@RequestHeader("X-Sharer-User-Id") Integer bookerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state,
                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(required = false, defaultValue = "20") Integer size) {

        return commonService.getAllBookingsByBookerIdDesc(bookerId, state, from, size);

    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByItemOwnerId(@RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                        @RequestParam(required = false, defaultValue = "ALL") String state,
                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(required = false, defaultValue = "20") Integer size) {

        return commonService.getAllBookingsByItemOwnerId(ownerId, state, from, size);

    }


}
