package ru.practicum.shareit.dto.booking;

import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.user.UserDto;

import lombok.Data;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    ItemDto item;
    UserDto booker;
}
