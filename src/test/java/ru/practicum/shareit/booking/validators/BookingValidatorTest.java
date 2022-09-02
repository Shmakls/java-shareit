package ru.practicum.shareit.booking.validators;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.exceptions.IncorrectBookingDateException;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingValidatorTest {

    private static BookingValidator bookingValidator;

    private static Booking booking;

    private static Booking bookingWithWrongStart;

    private static Booking bookingWithWrongEnd;

    private static Booking bookingWithWrongRentTime;

    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeAll
    static void beforeAll() {

        bookingValidator = new BookingValidator();

        booking = new Booking();
        booking.setStart(NOW.plusDays(1));
        booking.setEnd(NOW.plusDays(2));

        bookingWithWrongStart = new Booking();
        bookingWithWrongStart.setStart(NOW.minusDays(1));
        bookingWithWrongStart.setEnd(NOW.plusDays(1));

        bookingWithWrongEnd = new Booking();
        bookingWithWrongEnd.setStart(NOW.plusDays(1));
        bookingWithWrongEnd.setEnd(NOW.minusDays(1));

        bookingWithWrongRentTime = new Booking();
        bookingWithWrongRentTime.setStart(NOW.plusDays(3));
        bookingWithWrongRentTime.setEnd(NOW.plusDays(1));

    }

    @Test
    void shouldBeOk() {

        boolean result = bookingValidator.isValid(booking);

        assertTrue(result);

    }

    @Test
    void shouldBeThrowExceptionIfWrongTimes() {

        final IncorrectBookingDateException e1 = assertThrows(
                IncorrectBookingDateException.class,
                () -> bookingValidator.isValid(bookingWithWrongStart)
        );

        final IncorrectBookingDateException e2 = assertThrows(
                IncorrectBookingDateException.class,
                () -> bookingValidator.isValid(bookingWithWrongEnd)
        );

        final IncorrectBookingDateException e3 = assertThrows(
                IncorrectBookingDateException.class,
                () -> bookingValidator.isValid(bookingWithWrongRentTime)
        );

        assertEquals("Дата начала аренды не может быть в прошлом", e1.getMessage());
        assertEquals("Дата окончания аренды не может быть в прошлом", e2.getMessage());
        assertEquals("Дата начала аренды не может быть после даты окончания", e3.getMessage());

    }

}