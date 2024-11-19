package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.booking.model.Booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {

    Booking map(BookingSaveDto booking);

    BookingDto map(Booking booking);

    Collection<BookingDto> map(Collection<Booking> bookings);
}
