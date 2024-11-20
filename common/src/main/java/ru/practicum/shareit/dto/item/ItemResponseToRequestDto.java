package ru.practicum.shareit.dto.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponseToRequestDto {
    long id;
    long ownerId;
    String name;
}
