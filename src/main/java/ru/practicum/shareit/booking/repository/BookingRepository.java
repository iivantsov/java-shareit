package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    Collection<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time);

    Collection<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end")
    Collection<Booking> findCurrentBookings(long bookerId, LocalDateTime time);

    Collection<Booking> findByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);
}
