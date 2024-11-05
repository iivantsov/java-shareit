package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    @Override
    public BookingDto addBooking(long userId, BookingSaveDto bookingSaveDto) {
        return null;
    }

    @Override
    public BookingDto manageBooking(long userId, long bookingId, boolean approved) {
        return null;
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        return null;
    }

    @Override
    public Collection<BookingDto> getAllBookings(long userId, BookingState state) {
        return List.of();
    }

    @Override
    public Collection<BookingDto> getAllOwnersItemBookings(long userId, BookingState state) {
        return List.of();
    }
}
