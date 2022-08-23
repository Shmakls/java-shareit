package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Page<Booking> findBookingsByBookerId(Integer bookerId, Pageable pageable);

    List<Booking> findBookingsByItemId(Integer itemId);

    Booking findBookingByBookerIdAndItemId(Integer bookerId, Integer itemId);

    List<Booking> findBookingsByItemIdIn(List<Integer> itemsId);


}
