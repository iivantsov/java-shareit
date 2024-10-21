package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper
public interface BookingMapper {

    BookingDto bookingToBookingDto(Booking booking);
}
