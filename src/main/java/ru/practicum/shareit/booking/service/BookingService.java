package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(long userId, BookingSaveDto bookingSaveDto);

    BookingDto manageBooking(long userId, long bookingId, boolean approved);

    BookingDto getBooking(long userId, long bookingId);

    Collection<BookingDto> getAllUserBookings(long userId, BookingState state);

    Collection<BookingDto> getAllUserItemsBookings(long userId, BookingState state);
}
