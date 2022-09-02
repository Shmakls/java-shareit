package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.RepeatRequestException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validators.BookingValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final BookingValidator bookingValidator;

    @Override
    public BookingDto addBooking(Booking booking, Integer bookerId) {

        Booking bookingChecker = bookingRepository.findBookingByBookerIdAndItemId(bookerId, booking.getItemId());

        if (bookingChecker != null && bookingChecker.getStatus() == BookingStatus.REJECTED) {
                throw new RepeatRequestException("Повторное бронирование делать нельзя");
        }

        booking.setBookerId(bookerId);
        booking.setStatus(BookingStatus.WAITING);

        bookingValidator.isValid(booking);

        booking = bookingRepository.save(booking);

        return bookingMapper.toDto(booking);

    }

    @Override
    public BookingDto getBookingById(Integer bookingId) {

        if (!isExist(bookingId)) {
            log.error("BookingService.getBookingById: Бронирования с id={} не существует", bookingId);
            throw new BookingNotFoundException("Такого бронирования в базе нет");
        }

        return bookingMapper.toDto(bookingRepository.getReferenceById(bookingId));

    }

    @Override
    public Boolean isExist(Integer bookingId) {
        return bookingRepository.existsById(bookingId);
    }

    @Override
    public BookingDto updateBookingStatus(BookingDto bookingDto) {

        return bookingMapper.toDto(bookingRepository.save(bookingMapper.fromDto(bookingDto)));

    }

    @Override
    public List<BookingDto> findBookingsByBookerId(Integer bookerId, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        return bookingRepository.findBookingsByBookerId(bookerId, pageable).getContent()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingDto> findBookingsByIdItemsList(List<Integer> itemsId, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("start").descending());

        return bookingRepository.findBookingsByItemIdIn(itemsId, pageable).getContent()
                .stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<Booking> findBookingsByItemId(Integer itemId) {

        return bookingRepository.findBookingsByItemId(itemId);

    }

}
