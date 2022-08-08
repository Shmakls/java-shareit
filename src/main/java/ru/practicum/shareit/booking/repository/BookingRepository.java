package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingsByBookerId(Integer bookerId);

    List<Booking> findBookingsByItemId(Integer itemId);

    Booking findBookingByBookerIdAndItemId(Integer bookerId, Integer ItemId);


}
