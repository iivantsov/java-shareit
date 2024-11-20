package ru.practicum.shareit.dto.request;

import ru.practicum.shareit.dto.item.ItemResponseToRequestDto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    long id;
    String description;
    LocalDateTime created;
    Collection<ItemResponseToRequestDto> items;
}
