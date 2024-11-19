package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.dto.booking.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(long userId, BookingSaveDto bookingSaveDto);

    BookingDto manageBooking(long userId, long bookingId, boolean approved);

    BookingDto getBooking(long userId, long bookingId);

    Collection<BookingDto> getAllUserBookings(long userId, BookingState state);

    Collection<BookingDto> getAllUserItemsBookings(long userId, BookingState state);
}
