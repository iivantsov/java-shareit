package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
