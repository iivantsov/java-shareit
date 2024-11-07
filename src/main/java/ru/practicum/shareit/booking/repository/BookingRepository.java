package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findAllByItemIdAndStartAfterOrderByStartAsc(long itemId, LocalDateTime time);

    Collection<Booking> findAllByItemIdInAndStartAfterOrderByStartAsc(Collection<Long> itemIds, LocalDateTime time);

    Collection<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, LocalDateTime time);

    @Query("SELECT b from Booking AS b " +
            "WHERE b.booker.id = ?1 " +
            "AND ?2 > b.start " +
            "AND ?2 < b.end")
    Collection<Booking> findAllCurrentBookings(long bookerId, LocalDateTime time);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);
}
