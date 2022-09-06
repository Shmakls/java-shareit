package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingGatewayDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingGatewayController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @RequestBody @Valid BookingGatewayDto bookingGatewayDto) {
		log.info("BookingGatewayController.addBooking: получен запрос на создание бронирования от пользователя id={}", userId);

		return bookingClient.addBooking(userId, bookingGatewayDto);

	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
												 @PathVariable long bookingId,
												 @RequestParam Boolean approved) {

		log.info("BookingGatewayController.approveBooking: получен запрос на подтверждение или отклонение "
				+ "запроса на бронирование с id={}", bookingId);

		return bookingClient.approveBooking(userId, bookingId, approved);

	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
												 @PathVariable Long bookingId) {
		log.info("BookingGatewayController.getBookingById: получен запрос на поиск бронирования с id={}", bookingId);

		return bookingClient.getBookingById(userId, bookingId);

	}

	@GetMapping
	public ResponseEntity<Object> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
														@RequestParam(name = "state", defaultValue = "all") String stateParam,
														@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
														@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

		log.info("BookingGatewayController.getBookingsByBookerId: получен запрос на поиск бронирований владельцем бронирований");

		return bookingClient.getBookingsByBookerId(userId, state, from, size);

	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByItemOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
														  @RequestParam(name = "state", defaultValue = "all") String stateParam,
														  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
														  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

		log.info("BookingGatewayController.getBookingsByBookerId: получен запрос на поиск бронирований владельцем бронирований");

		return bookingClient.getBookingByItemOwnerId(userId, state, from, size);

	}

}
