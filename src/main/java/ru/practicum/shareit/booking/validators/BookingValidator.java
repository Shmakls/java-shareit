package ru.practicum.shareit.booking.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.exceptions.IncorrectBookingDateException;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@Component
@Slf4j
public class BookingValidator {

    public boolean isValid(Booking booking) {

        log.info("BookingValidator.isValid: Начинаю валидацию даты начала бронирования - {}", booking.getStart());

        startValidator(booking.getStart());

        log.info("BookingValidator.isValid: Начинаю валидацию даты окончания бронирования - {}", booking.getEnd());

        endValidator(booking.getEnd());

        log.info("BookingValidator.isValid: Начинаю валидацию дат бронирования с {} по {}", booking.getStart(), booking.getEnd());

        rentDateValidator(booking.getStart(), booking.getEnd());

        return true;

    }

    private void startValidator(LocalDateTime start) {

        if (start.isBefore(LocalDateTime.now())) {
            log.error("BookingValidator.startValidator: Дата начала аренды {} не может быть в прошлом", start);
            throw new IncorrectBookingDateException("Дата начала аренды не может быть в прошлом");
        }

    }

    private void endValidator(LocalDateTime end) {

        if (end.isBefore(LocalDateTime.now())) {
            log.error("BookingValidator.endValidator: Дата окончания бронирования {} не может быть в прошлом", end);
            throw new IncorrectBookingDateException("Дата окончания аренды не может быть в прошлом");
        }

    }

    private void rentDateValidator(LocalDateTime start, LocalDateTime end) {

        if (start.isAfter(end)) {
            log.error("BookingValidator.rentDateValidator: Дата начала бронирования {} "
                    + "не может быть позже даты окончания бронирования {}", start, end);
            throw new IncorrectBookingDateException("Дата начала аренды не может быть после даты окончания");
        }

    }

}
